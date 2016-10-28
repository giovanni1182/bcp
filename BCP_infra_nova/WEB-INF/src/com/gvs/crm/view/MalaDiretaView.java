package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.DataHoraInput;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.EventoDescricaoInput;
import com.gvs.crm.component.EventoTipoSelect;
import com.gvs.crm.component.EventoTituloInput;
import com.gvs.crm.model.MalaDireta;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Table;
import infra.view.View;

public class MalaDiretaView extends EventoAbstratoView {
	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		MalaDireta malaDireta = (MalaDireta) this.obterEvento();
		Table table = new Table(2);
		boolean incluir = malaDireta.obterId() == 0;

		if (!incluir) {
			table.addHeader("Creado por:");
			table.addData(new CriacaoLabel(malaDireta));
		}
		table.addHeader("Cliente:");
		table.addData(new EntidadePopup("origemId", "origemNome", malaDireta
				.obterOrigem(), malaDireta.permiteAtualizar(), true));
		table.addHeader("Tipo de Mala:");
		table.addData(new EventoTipoSelect("tipo", malaDireta, malaDireta
				.permiteAtualizar()));
		table.addHeader("Título:");
		table.addData(new EventoTituloInput("titulo", malaDireta, malaDireta
				.permiteAtualizar()));
		table.addHeader("Descripción:");
		table.addData(new EventoDescricaoInput("descricao", malaDireta,
				malaDireta.permiteAtualizar()));
		table.addHeader("Fecha/Hora:");
		table.addData(new DataHoraInput("data", "inicio", malaDireta
				.obterDataPrevistaInicio(), malaDireta.permiteAtualizar()));

		if (incluir) {
			Button incluirButton = new Button("Incluir", new Action(
					"incluirMalaDireta"));
			incluirButton.getAction().add("origemMenuId",
					this.obterOrigemMenu().obterId());
			table.addFooter(incluirButton);

			Action voltarAction = new Action("novoEvento");
			voltarAction.add("passo", 2);
			voltarAction.add("origemMenuId", this.obterOrigemMenu().obterId());

			Button voltarButton = new Button("Voltar", voltarAction);
			table.addFooter(voltarButton);
		} else {
			table.addHeader("Comentarios:");
			table.add(new ComentariosView(malaDireta));

			if (malaDireta.permitePegar()) {
				Button pegarButton = new Button("Pegar", new Action(
						"pegarEvento"));
				pegarButton.getAction().add("id", malaDireta.obterId());
				pegarButton.getAction().add("origemMenuId",
						malaDireta.obterOrigem().obterId());
				table.addFooter(pegarButton);
			}

			if (malaDireta.permiteDevolver()) {
				Button devolverButton = new Button("Devolver", new Action(
						"devolverEvento"));
				devolverButton.getAction().add("id", malaDireta.obterId());
				devolverButton.getAction().add("origemMenuId",
						malaDireta.obterOrigem().obterId());
				table.addFooter(devolverButton);
			}

			if (malaDireta.permiteAtualizar()) {
				Button atualizarButton = new Button("Actualizar", new Action(
						"atualizarMalaDireta"));
				atualizarButton.getAction().add("id", malaDireta.obterId());
				atualizarButton.getAction().add("origemMenuId",
						malaDireta.obterOrigem().obterId());
				table.addFooter(atualizarButton);
			}

			if (malaDireta.permiteEncaminhar()) {
				Button encaminharButton = new Button("Encaminar", new Action(
						"encaminharEvento"));
				encaminharButton.getAction().add("id", malaDireta.obterId());
				encaminharButton.getAction().add("origemMenuId",
						malaDireta.obterOrigem().obterId());
				encaminharButton.getAction().add("view", true);
				table.addFooter(encaminharButton);
			}

			if (malaDireta.permiteConcluir()) {
				Button concluirButton = new Button("Concluir", new Action(
						"concluirEvento"));
				concluirButton.getAction().add("id", malaDireta.obterId());
				concluirButton.getAction().add("view", true);
				concluirButton.getAction().add("origemMenuId",
						malaDireta.obterOrigem().obterId());
				table.addFooter(concluirButton);
			}

			if (malaDireta.permiteExcluir()) {
				Button excluirButton = new Button("Excluir", new Action(
						"excluirEvento"));
				excluirButton.getAction().add("id", malaDireta.obterId());
				excluirButton.getAction().add("origemMenuId",
						malaDireta.obterOrigem().obterId());
				excluirButton.getAction().setConfirmation(
						"Confirma exclusão do evento ?");
				table.addFooter(excluirButton);
			}

			Button voltarButton = new Button("Retornar", new Action(
					"visualizarPaginaInicial"));
			voltarButton.getAction().add("origemMenuId",
					malaDireta.obterOrigem().obterId());
			table.addFooter(voltarButton);
		}
		return table;
	}
}