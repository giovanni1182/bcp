package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.EventoDescricaoInput;
import com.gvs.crm.component.EventoResponsavelLabel;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.AgendaMeicos;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Parametro;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputDouble;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class AgendaMeicosView extends EventoAbstratoView {

	private static final int DETALHE = 0;

	private static final int TABULEIRO = 1;

	private static final int CONTROLE_DOCUMENTOS = 2;

	public View execute(User user, Locale arg1, Properties properties)
			throws Exception {
		AgendaMeicos agenda = (AgendaMeicos) this.obterEvento();

		int _pasta = Integer.parseInt(properties.getProperty("_pastaAgenda",
				"0"));

		boolean novo = agenda.obterId() == 0;

		Table table = new Table(2);

		if (novo || _pasta > 3)
			_pasta = DETALHE;

		Link dadosLink = new Link("Datos Basicos", new Action(
				"visualizarEvento"));
		((Label) dadosLink.getCaption()).setBold(_pasta == DETALHE);
		dadosLink.getAction().add("id", agenda.obterId());
		dadosLink.getAction().add("_pastaAgenda", DETALHE);
		dadosLink.setEnabled(!novo);

		Link componentesLink = new Link("Tabuleiro", new Action(
				"visualizarEvento"));
		((Label) componentesLink.getCaption()).setBold(_pasta == TABULEIRO);
		componentesLink.getAction().add("id", agenda.obterId());
		componentesLink.getAction().add("_pastaAgenda", TABULEIRO);
		componentesLink.setEnabled(!novo);

		Link parametroLink = new Link("Controle de Documentos", new Action(
				"visualizarEvento"));
		((Label) parametroLink.getCaption())
				.setBold(_pasta == CONTROLE_DOCUMENTOS);
		parametroLink.getAction().add("id", agenda.obterId());
		parametroLink.getAction().add("_pastaAgenda", CONTROLE_DOCUMENTOS);
		parametroLink.setEnabled(!novo);

		Block block = new Block(Block.HORIZONTAL);
		block.add(dadosLink);
		block.add(new SeparadorLabel());
		block.add(componentesLink);
		block.add(new SeparadorLabel());
		block.add(parametroLink);

		table.setNextColSpan(table.getColumns());
		table.add(block);

		if (_pasta == DETALHE) {
			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			if (!novo) {
				table.addHeader("Creado por:");
				table.addData(new CriacaoLabel(agenda));

				table.addHeader("Responsable:");
				table.add(new EventoResponsavelLabel(agenda));
			}

			table.addHeader("Fecha Calculo:");
			table.add(new InputDate("data", agenda.obterDataPrevistaInicio()));

			table.addHeader("IPC último Anõ:");
			table.add(new InputDouble("ipc1Ano", agenda.obterIPC(), 6));

			table.addHeader("IPC últimos 3 Anõs:");
			table.add(new InputDouble("ipc3Anos", agenda.obterIPC3Anos(), 6));

			table.addHeader("Titulo:");
			table.add(new InputString("titulo", agenda.obterTitulo(), 50));

			table.addHeader("Descripción:");
			table.addData(new EventoDescricaoInput("descricao", agenda, agenda
					.permiteAtualizar()));

			if (novo) {
				Button incluirButton = new Button("Agregar", new Action(
						"incluirAgendaMeicos"));

				table.addFooter(incluirButton);

			}
			else 
			{
				Button gerarButton = new Button("Generar Meicos", new Action("gerarMeicos"));
				gerarButton.getAction().add("id", agenda.obterId());
				gerarButton.setEnabled(agenda.permiteAtualizar());

				table.addFooter(gerarButton);

				Button calcularButton = new Button("Calcular Meicos",new Action("calcularMeicos"));
				calcularButton.getAction().add("id", agenda.obterId());
				calcularButton.setEnabled(agenda.permiteAtualizar());
				table.addFooter(calcularButton);

				Button atualizarButton = new Button("Actualizar", new Action("atualizarAgendaMeicos"));
				atualizarButton.setEnabled(agenda.permiteAtualizar());
				atualizarButton.getAction().add("id", agenda.obterId());

				table.addFooter(atualizarButton);

				Button excluirButton = new Button("Eliminar", new Action("excluirEvento"));
				excluirButton.getAction().add("id", agenda.obterId());
				excluirButton.setEnabled(agenda.permiteAtualizar());
				excluirButton.getAction().setConfirmation("Confirma Exclusión ?");

				table.addFooter(excluirButton);
			}
		}
		else if (_pasta == TABULEIRO)
		{
			CRMModelManager mm = new CRMModelManager(user);

			AseguradoraHome home = (AseguradoraHome) mm.getHome("AseguradoraHome");

			table.setNextColSpan(table.getColumns());

			//Collection meicosCalculo = new ArrayList();

			table.add(new AgendaMeicosTabuleiroView(home.obterAseguradoras(), agenda));
		} 
		else if (_pasta == CONTROLE_DOCUMENTOS)
		{
			CRMModelManager mm = new CRMModelManager(user);

			EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

			Parametro parametro = (Parametro) home.obterEntidadePorApelido("parametros");

			table.setNextColSpan(table.getColumns());

			table.add(new AgendaMeicosControleDocumentosView(agenda,parametro));
		}
		return table;
	}
}