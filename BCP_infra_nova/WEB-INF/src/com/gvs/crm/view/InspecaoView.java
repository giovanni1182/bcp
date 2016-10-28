package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.EventoDescricaoInput;
import com.gvs.crm.component.EventoTipoSelect;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.Inspecao;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class InspecaoView extends EventoAbstratoView {

	public static final int DETALLES = 1;

	public static final int DOCUMENTOS = 2;

	private int _pasta;

	public View execute(User user, Locale arg1, Properties properties)
			throws Exception {
		Inspecao inspecao = (Inspecao) this.obterEvento();

		boolean incluir = inspecao.obterId() == 0;

		_pasta = Integer.parseInt(properties.getProperty("_pasta", "1"));

		if (incluir || _pasta > 2)
			this._pasta = DETALLES;

		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);

		Link basicosLink = new Link("Detalles", new Action("visualizarEvento"));
		((Label) basicosLink.getCaption()).setBold(_pasta == DETALLES);
		basicosLink.getAction().add("id", inspecao.obterId());
		basicosLink.getAction().add("_pasta", DETALLES);
		basicosLink.setEnabled(!incluir);

		Link documentosLink = new Link("Anexos", new Action("visualizarEvento"));
		((Label) documentosLink.getCaption()).setBold(_pasta == DOCUMENTOS);
		documentosLink.getAction().add("id", inspecao.obterId());
		documentosLink.getAction().add("_pasta", DOCUMENTOS);
		documentosLink.setEnabled(!incluir);

		Block block = new Block(Block.HORIZONTAL);
		block.add(basicosLink);
		block.add(new SeparadorLabel());
		block.add(documentosLink);

		table.add(block);
		
		if (_pasta == DETALLES)
		{
			table.add("");
			table.addSubtitle("Detalles");

			if (!incluir)
			{
				table.addHeader("Creado en:");
				table.add(new CriacaoLabel(inspecao));

				table.addHeader("Responsable:");
				table.addData(new EntidadePopup("responsavelId", "responsavelNome", inspecao.obterResponsavel(),"Usuario", inspecao.permiteAtualizar()));
			}

			table.addHeader("Tipo de Inspección:");
			table.add(new EventoTipoSelect("tipo", inspecao, true));

			table.addHeader("Aseguradora:");
			table.add(new EntidadePopup("origemId", "origemNome", inspecao.obterOrigem(), "aseguradora", true));

			table.addHeader("Inspector Responsable:");
			table.add(new EntidadePopup("inspetorId", "inspetorNome", inspecao.obterInspetor(), "Usuario", true));

			table.addHeader("Fecha Prevista Inicio:");
			table.add(new InputDate("dataPrevistaInicio", inspecao
					.obterDataPrevistaInicio()));

			table.addHeader("Fecha Prevista Termino:");
			table.add(new InputDate("dataPrevistaConclusao", inspecao
					.obterDataPrevistaConclusao()));

			table.addHeader("Días corridos:");
			table.addHeader(new InputString("diasCorridos", inspecao
					.obterDiasCorridos(), 10));

			table.addHeader("Fecha de Inicio Real:");
			table.add(new InputDate("dataPrevistaInicioReal", inspecao
					.obterDataInicioReal()));

			table.addHeader("Fecha Termino Real:");
			table.add(new InputDate("dataPrevistaConclusaoReal", inspecao
					.obterDataTerminoReal()));

			table.addHeader("Titulo de la inspección:");
			table.add(new InputString("titulo", inspecao.obterTitulo(), 50));

			table.addHeader("Relato:");
			table.add(new EventoDescricaoInput("descricao", inspecao, true));
			
			if (incluir)
			{
				Button incluirButton = new Button("Agregar", new Action("incluirInspecao"));
				table.addFooter(incluirButton);

				Button cancelarButton = new Button("Volver", new Action("novoEvento"));
				cancelarButton.getAction().add("passo", 2);
				table.addFooter(cancelarButton);
				
				mainTable.add(table);
			} 
			else
			{
				if (inspecao.permitePegar())
				{
					Button pegarButton = new Button("Pegar", new Action("pegarEvento"));
					pegarButton.getAction().add("id", inspecao.obterId());
					table.addFooter(pegarButton);
				}

				Button novaAgendaButton = new Button("Sub-Evento", new Action("novoEvento"));
				novaAgendaButton.getAction().add("passo", 3);
				novaAgendaButton.getAction().add("superiorId", inspecao.obterId());
				novaAgendaButton.setEnabled(inspecao.permiteAtualizar());
				table.addFooter(novaAgendaButton);

				Button atualizarButton = new Button("Actualizar", new Action(
						"atualizarInspecao"));
				atualizarButton.getAction().add("id", inspecao.obterId());
				atualizarButton.setEnabled(inspecao.permiteAtualizar());
				table.addFooter(atualizarButton);

				/*
				 * if(!inspecao.obterFase().obterCodigo().equals(Inspecao.EVENTO_PENDENTE) &&
				 * !inspecao.obterFase().obterCodigo().equals(Inspecao.EVENTO_CONCLUIDO)) {
				 * Button faseAnteriorButton = new Button(" < < < Fase Anterior",
				 * new Action("faseAnterior"));
				 * faseAnteriorButton.getAction().add("view", true);
				 * faseAnteriorButton.getAction().add("id", inspecao.obterId());
				 * faseAnteriorButton.setEnabled(inspecao.permiteAtualizar());
				 * table.addFooter(faseAnteriorButton); }
				 * 
				 * if(!inspecao.obterFase().obterCodigo().equals(Inspecao.EVENTO_CONCLUIDO) &&
				 * !inspecao.obterFase().obterCodigo().equals("superintendente")) {
				 * Button proximaFaseButton = new Button("Próxima Fase >>>", new
				 * Action("proximaFase")); proximaFaseButton.getAction().add("view",
				 * true); proximaFaseButton.getAction().add("id",
				 * inspecao.obterId());
				 * proximaFaseButton.setEnabled(inspecao.permiteAtualizar());
				 * table.addFooter(proximaFaseButton); }
				 */

				Button comentarButton = new Button("Comentarios", new Action("comentarEvento"));
				comentarButton.getAction().add("id", inspecao.obterId());
				comentarButton.getAction().add("view", true);
				comentarButton.setEnabled(inspecao.permiteAtualizar());
				table.addFooter(comentarButton);

				Button encaminharButton = new Button("Remetir", new Action("encaminharEvento"));
				encaminharButton.getAction().add("id", inspecao.obterId());
				encaminharButton.getAction().add("view", true);
				encaminharButton.setEnabled(inspecao.permiteEncaminhar());
				table.addFooter(encaminharButton);

				if (inspecao.permiteConcluir())
				{
					Button concluirButton = new Button("Concluir", new Action("concluirEvento"));
					concluirButton.getAction().add("id", inspecao.obterId());
					concluirButton.getAction().add("view", true);
					concluirButton.setEnabled(inspecao.permiteAtualizar());
					table.addFooter(concluirButton);
				}

				Action action = new Action("excluirEvento");
				action.setConfirmation("Confirma Exclusión");

				Button excluirButton = new Button("Eliminar", action);
				//excluirButton.setEnabled(inspecao.permiteExcluir());
				excluirButton.getAction().add("id", inspecao.obterId());
				table.addFooter(excluirButton);
				
				mainTable.add(table);
				
				mainTable.addSubtitle("Comentários");
				mainTable.add(new ComentariosView(inspecao));

				mainTable.addSubtitle("Sub-eventos:");
				mainTable.add(new SubEventosView(inspecao));
			}
		}

		else if (_pasta == DOCUMENTOS)
		{
			mainTable.add(table);
			mainTable.add(new AnexosView(inspecao));
		}

		return mainTable;
	}
}