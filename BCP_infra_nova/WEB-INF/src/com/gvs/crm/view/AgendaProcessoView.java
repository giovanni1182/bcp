package com.gvs.crm.view;

import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.DataHoraInput;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.EventoDescricaoInput;
import com.gvs.crm.component.EventoSuperiorLabel;
import com.gvs.crm.component.EventoTituloInput;
import com.gvs.crm.model.AgendaProcesso;
import com.gvs.crm.model.Evento;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Table;
import infra.view.View;

public class AgendaProcessoView extends EventoAbstratoView {
	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		AgendaProcesso agenda = (AgendaProcesso) this.obterEvento();
		Evento superior = agenda.obterSuperior();
		boolean incluir = agenda.obterId() == 0;
		Date data = new Date();

		if (superior != null && incluir) {
			agenda.atribuirOrigem(superior.obterOrigem());
			agenda.atribuirPrioridade(superior.obterPrioridade());
		}

		Table table = new Table(2);
		table.addHeader("Superior:");
		table.add(new EventoSuperiorLabel(agenda));
		if (!incluir) {
			table.addHeader("Criado por:");
			table.addData(new CriacaoLabel(agenda));

			table.addHeader("Responsável:");
			table.add(new EntidadePopup("responsavelId", "responsavelNome",
					agenda.obterResponsavel(), true));

		}
		table.addHeader("Nome:");
		table.addData(new EntidadePopup("origemId", "origemNome", agenda
				.obterOrigem(), agenda.permiteAtualizar(), true));
		table.addHeader("Título:");
		table.addData(new EventoTituloInput("titulo", agenda, agenda
				.permiteAtualizar()));
		table.addHeader("Descrição:");
		table.addData(new EventoDescricaoInput("descricao", agenda, agenda
				.permiteAtualizar()));
		table.addHeader("Data/Hora:");
		table.addData(new DataHoraInput("data", "inicio", agenda
				.obterDataPrevistaInicio(), agenda.permiteAtualizar()));

		if (incluir) {
			Button incluirButton = new Button("Incluir", new Action(
					"incluirAgendaProcesso"));
			if (superior != null)
				incluirButton.getAction().add("superiorId", superior.obterId());

			table.addFooter(incluirButton);

			Action voltarAction = null;

			if (agenda.obterSuperior() != null) {
				voltarAction = new Action("visualizarEvento");
				voltarAction.add("id", agenda.obterSuperior().obterId());
			} else {
				voltarAction = new Action("novoEvento");
				voltarAction.add("passo", 2);
				voltarAction.add("origemMenuId", this.obterOrigemMenu()
						.obterId());
			}

			Button voltarButton = new Button("Voltar", voltarAction);
			table.addFooter(voltarButton);
		} else {
			table.addHeader("Comentários:");
			table.add(new ComentariosView(agenda));

			if (agenda.permiteAtualizar()) {
				Button atualizarButton = new Button("Atualizar", new Action(
						"atualizarAgendaProcesso"));
				atualizarButton.getAction().add("id", agenda.obterId());
				table.addFooter(atualizarButton);
			}

			if (agenda.permiteEncaminhar()) {
				Button encaminharButton = new Button("Encaminhar", new Action(
						"encaminharEvento"));
				encaminharButton.getAction().add("id", agenda.obterId());
				encaminharButton.getAction().add("view", true);
				table.addFooter(encaminharButton);
			}

			if (agenda.permiteConcluir()) {
				Button concluirButton = new Button("Concluir", new Action(
						"concluirEvento"));
				concluirButton.getAction().add("id", agenda.obterId());
				concluirButton.getAction().add("view", true);
				table.addFooter(concluirButton);
			}

			if (agenda.permiteExcluir()) {
				Button excluirButton = new Button("Excluir", new Action(
						"excluirEvento"));
				excluirButton.getAction().add("id", agenda.obterId());
				excluirButton.getAction()
						.setConfirmation("Confirma exclusão ?");
				table.addFooter(excluirButton);
			}

			Button voltarButton = new Button("Voltar", new Action(
					"visualizarPaginaInicial"));
			voltarButton.getAction().add("origemMenuId",
					agenda.obterOrigem().obterId());
			table.addFooter(voltarButton);
		}
		return table;
	}
}