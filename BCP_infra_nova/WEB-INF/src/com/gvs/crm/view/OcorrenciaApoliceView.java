package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.EventoDescricaoInput;
import com.gvs.crm.component.EventoPopup;
import com.gvs.crm.component.EventoSuperiorLabel;
import com.gvs.crm.component.EventoTipoSelect;
import com.gvs.crm.model.OcorrenciaApolice;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputString;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class OcorrenciaApoliceView extends EventoAbstratoView {
	public View execute(User arg0, Locale arg1, Properties arg2)
			throws Exception {
		OcorrenciaApolice ocorrencia = (OcorrenciaApolice) this.obterEvento();

		boolean incluir = ocorrencia.obterId() == 0;

		Table table = new Table(2);

		if (!incluir) {
			table.addHeader("Creado por:");
			table.add(new CriacaoLabel(ocorrencia));
		}

		table.addHeader("Superior:");
		table.add(new EventoSuperiorLabel(ocorrencia));

		table.addHeader("Fase:");
		if (incluir)
			table.addHeader("PENDIENTE");
		else
			table.addHeader(ocorrencia.obterFase().obterNome().toUpperCase());

		table.addHeader("Aseguradora:");
		table.add(new EntidadePopup("aseguradoraId", "aseguradoraNome",ocorrencia.obterOrigem(), "Aseguradora", true));

		table.addHeader("Número de la póliza");
		table.add(new EventoPopup("apoliceId", "apoliceTitulo", ocorrencia
				.obterApolice(), "Apolice", true));

		table.addHeader("Tipo de reporte:");
		table.add(new EventoTipoSelect("tipo", ocorrencia, true));

		table.addHeader("Nº Expediente:");
		table.add(new InputString("expediente", ocorrencia.obterExpediente(),
				10));

		table.addHeader("Fecha que detectó la sospecha:");
		table
				.add(new InputDate("dataSuspeita", ocorrencia
						.obterDataSuspeita()));

		table.addHeader("Fecha del reporte:");
		table.add(new InputDate("dataReporte", ocorrencia.obterDataReporte()));

		table.addHeader("Responsable:");
		table.add(new EntidadePopup("responsavelId", "responsavelNome",
				ocorrencia.obterResponsavel(), "Usuario", true));

		Table table2 = new Table(2);

		table2.addSubtitle("Personas y/o Cuentas Relacionadas");

		table2.addHeader("Número de Cuenta");
		table2.add(new InputString("numeroConta",
				ocorrencia.obterNumeroConta(), 20));

		table2.addHeader("Entidad:");
		table2.add(new InputString("entidade", ocorrencia.obterEntidade(), 35));

		table2.addHeader("Nombre del Titular:");
		table2.add(new InputString("titular", ocorrencia.obterTitular(), 60));

		table2.addHeader("Dirección:");
		table2.add(new InputString("endereco", ocorrencia.obterEndereco(), 80));

		table2.addHeader("Barrio:");
		table2.add(new InputString("bairro", ocorrencia.obterBairro(), 25));

		table2.addHeader("Ciudad:");
		table2.add(new InputString("cidade", ocorrencia.obterCidade(), 35));

		table2.addHeader("Teléfono:");
		table2.add(new InputString("telefone", ocorrencia.obterTelefone(), 15));

		table2.addHeader("Pais:");
		table2.add(new InputString("pais", ocorrencia.obterPais(), 35));

		table.setNextColSpan(table.getColumns());
		table.add(table2);

		Table table3 = new Table(2);

		table3.addSubtitle("Descriptión");

		table3.addHeader("Descripción de la Operacion:");
		table3.add(new EventoDescricaoInput("descricao", ocorrencia, true));

		Block block = new Block(Block.VERTICAL);

		Label label = new Label("Razones por las que la");
		label.setBold(true);

		Label label2 = new Label("Operación se considera");
		label2.setBold(true);

		Label label3 = new Label("Inusual o Sospechosa");
		label3.setBold(true);

		block.add(label);
		block.add(label2);
		block.add(label3);

		table3.add(block);
		table3.add(new InputText("razao", ocorrencia.obterRazao(), 5, 80));

		table.setNextColSpan(table.getColumns());
		table.add(table3);

		table.addHeader("Comentários");
		table.setNextColSpan(table.getColumns());
		table.add(new ComentariosView(ocorrencia));

		table.addHeader("Sub-eventos:");
		table.setNextColSpan(table.getColumns());
		table.add(new SubEventosView(ocorrencia));

		if (incluir) {
			Button incluirButton = new Button("Agregar", new Action(
					"incluirOcorrenciaApolice"));
			table.addFooter(incluirButton);

			Button cancelarButton = new Button("Volver", new Action(
					"novoEvento"));
			cancelarButton.getAction().add("passo", 2);
			table.addFooter(cancelarButton);
		} else {
			Button novaAgendaButton = new Button("Sub-Evento", new Action(
					"novoEvento"));
			novaAgendaButton.getAction().add("passo", 3);
			novaAgendaButton.getAction()
					.add("superiorId", ocorrencia.obterId());
			novaAgendaButton.setEnabled(ocorrencia.permiteAtualizar());
			table.addFooter(novaAgendaButton);

			Button pegarButton = new Button("Pegar", new Action("pegarEvento"));
			pegarButton.getAction().add("id", ocorrencia.obterId());
			pegarButton.setEnabled(ocorrencia.permitePegar());
			table.addFooter(pegarButton);

			Button atualizarButton = new Button("Actualizar", new Action(
					"atualizarOcorrenciaApolice"));
			atualizarButton.getAction().add("id", ocorrencia.obterId());
			atualizarButton.setEnabled(ocorrencia.permiteAtualizar());
			table.addFooter(atualizarButton);

			Button comentarButton = new Button("Comentarios", new Action(
					"comentarEvento"));
			comentarButton.getAction().add("id", ocorrencia.obterId());
			comentarButton.getAction().add("view", true);
			comentarButton.setEnabled(ocorrencia.permiteAtualizar());
			table.addFooter(comentarButton);

			Button encaminharButton = new Button("Remetir", new Action(
					"encaminharEvento"));
			encaminharButton.getAction().add("id", ocorrencia.obterId());
			encaminharButton.getAction().add("view", true);
			encaminharButton.setEnabled(ocorrencia.permiteEncaminhar());
			table.addFooter(encaminharButton);

			Button concluirButton = new Button("Concluir", new Action(
					"concluirEvento"));
			concluirButton.getAction().add("id", ocorrencia.obterId());
			concluirButton.getAction().add("view", true);
			concluirButton.setEnabled(ocorrencia.permiteAtualizar());
			table.addFooter(concluirButton);

			Action action = new Action("excluirEvento");
			action.setConfirmation("Confirma Exclusión");

			Button excluirButton = new Button("Eliminar", action);
			excluirButton.getAction().add("id", ocorrencia.obterId());
			table.addFooter(excluirButton);
		}

		return table;
	}
}