package com.gvs.crm.view;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.EventoTipoSelect;
import com.gvs.crm.component.EventoTituloInput;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Ocorrencia;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class OcorrenciaView extends EventoAbstratoView {
	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		Ocorrencia ocorrencia = (Ocorrencia) this.obterEvento();
		boolean incluir = ocorrencia.obterId() == 0;
		Table table = new Table(2);

		table.addHeader("Superior:");
		if (ocorrencia.obterSuperior() != null) {
			Action action = new Action("visualizarEvento");
			action.add("id", ocorrencia.obterSuperior().obterId());

			Link superiorlink = new Link(ocorrencia.obterSuperior()
					.obterTitulo(), action);
			table.add(superiorlink);
		} else
			table.add("Ningún");

		if (!incluir) {
			table.addHeader("Creado por:");
			table.addData(new CriacaoLabel(ocorrencia));
		}
		table.addHeader("Aseguradora:");
		table.addData(new EntidadePopup("seguradoraId", "seguradoraNome",
				ocorrencia.obterOrigem(), "aseguradora", ocorrencia
						.permiteAtualizar()));

		table.addHeader("Responsable:");
		table.addData(new EntidadePopup("responsavelId", "responsavelNome",
				ocorrencia.obterResponsavel(), "Usuario", ocorrencia
						.permiteAtualizar()));

		table.addHeader("Tipo de la ocurrencia:");
		table.addData(new EventoTipoSelect("tipo", ocorrencia, ocorrencia
				.permiteAtualizar()));
		table.addHeader("Título:");
		table.addData(new EventoTituloInput("titulo", ocorrencia, ocorrencia
				.permiteAtualizar()));
		table.addHeader("Ocurrencia:");
		if (ocorrencia.permiteAtualizar())
			table.addData(new InputText("descricao", ocorrencia
					.obterDescricao(), 10, 100));
		else
			table.addData(new Label(ocorrencia.obterDescricao()));

		if (incluir) {
			Button incluirButton = new Button("Agregar", new Action(
					"incluirOcorrencia"));
			incluirButton.getAction().add("origemMenuId",
					this.obterOrigemMenu().obterId());
			table.addFooter(incluirButton);

			Action voltarAction = new Action("novoEvento");
			voltarAction.add("passo", 2);
			voltarAction.add("origemMenuId", this.obterOrigemMenu().obterId());

			Button voltarButton = new Button("Volver", voltarAction);
			table.addFooter(voltarButton);
		} else {
			for (Iterator i = ocorrencia.obterOrigem().obterContatos()
					.iterator(); i.hasNext();) {
				Entidade.Contato contato = (Entidade.Contato) i.next();
				Table tableContatos = new Table(2);
				tableContatos.addHeader(contato.obterNome());
				tableContatos.addData(contato.obterValor());
				table.setNextColSpan(table.getColumns());
				table.addData(tableContatos);
			}

			if (ocorrencia.obterOrigem().obterContatos().size() > 0) {
				table.setNextColSpan(table.getColumns());
				table.add(new Space());
			}

			if (!ocorrencia.obterInferiores().isEmpty()) {
				table.addHeader("Sub-eventos:");
				table.add(new SubEventosView(ocorrencia));

				table.setNextColSpan(table.getColumns());
				table.add(new Space());
			}

			table.addHeader("Comentários:");
			table.add(new ComentariosView(ocorrencia));

			if (ocorrencia.obterTipo().equals("Notificación de Reclamação")) {
				Button comentarButton = new Button("Comentarios", new Action(
						"comentarEvento"));
				comentarButton.getAction().add("id", ocorrencia.obterId());
				comentarButton.getAction().add("view", true);
				comentarButton.setEnabled(ocorrencia.permiteAtualizar());
				table.addFooter(comentarButton);

				if (ocorrencia.permitePegar()) {
					Button pegarButton = new Button("Pegar", new Action(
							"pegarEvento"));
					pegarButton.getAction().add("id", ocorrencia.obterId());
					pegarButton.getAction().add("origemMenuId",
							ocorrencia.obterOrigem().obterId());
					table.addFooter(pegarButton);
				}

				if (ocorrencia.permiteDevolver()) {
					Button devolverButton = new Button("Devolver", new Action(
							"devolverEvento"));
					devolverButton.getAction().add("id", ocorrencia.obterId());
					devolverButton.getAction().add("origemMenuId",
							ocorrencia.obterOrigem().obterId());
					table.addFooter(devolverButton);
				}

				if (ocorrencia.permiteIncluirEventoInferior()) {
					Button novoEventoButton = new Button("Sub-evento",
							new Action("novoEvento"));
					novoEventoButton.getAction().add("passo", 3);
					novoEventoButton.getAction().add("superiorId",
							ocorrencia.obterId());
					novoEventoButton.getAction().add("origemMenuId",
							ocorrencia.obterOrigem().obterId());
					table.addFooter(novoEventoButton);
				}

				if (ocorrencia.permiteAtualizar()) {
					Button atualizarButton = new Button("Atualizar",
							new Action("atualizarOcorrencia"));
					atualizarButton.getAction().add("id", ocorrencia.obterId());
					atualizarButton.getAction().add("origemMenuId",
							ocorrencia.obterOrigem().obterId());
					table.addFooter(atualizarButton);
				}

				if (ocorrencia.permiteResponder()) {
					Button responderButton = new Button("Responder",
							new Action("responderEvento"));
					responderButton.getAction().add("id", ocorrencia.obterId());
					responderButton.getAction().add("view", true);
					responderButton.getAction().add("origemMenuId",
							ocorrencia.obterOrigem().obterId());
					responderButton.setEnabled(ocorrencia.permiteResponder());
					table.addFooter(responderButton);
				}

				if (ocorrencia.permiteEncaminhar()) {
					Button encaminharButton = new Button("Remitir", new Action(
							"encaminharEvento"));
					encaminharButton.getAction()
							.add("id", ocorrencia.obterId());
					encaminharButton.getAction().add("view", true);
					encaminharButton.getAction().add("origemMenuId",
							ocorrencia.obterOrigem().obterId());
					table.addFooter(encaminharButton);
				}
			}

			if (ocorrencia.permiteAtualizar()) {
				Button comentarButton = new Button("Comentario", new Action(
						"comentarEvento"));
				comentarButton.getAction().add("id", ocorrencia.obterId());
				comentarButton.getAction().add("view", true);
				table.addFooter(comentarButton);
			}

			if (ocorrencia.permiteConcluir()) {
				Button concluirButton = new Button("Concluir", new Action(
						"concluirEvento"));
				concluirButton.getAction().add("id", ocorrencia.obterId());
				concluirButton.getAction().add("view", true);
				concluirButton.getAction().add("origemMenuId",
						ocorrencia.obterOrigem().obterId());
				table.addFooter(concluirButton);
			}

			//if (ocorrencia.permiteExcluir())
			//{
			Button excluirButton = new Button("Eliminar", new Action(
					"excluirEvento"));
			excluirButton.getAction().add("id", ocorrencia.obterId());
			excluirButton.getAction().setConfirmation(
					"Confirma exclusão do evento ?");
			table.addFooter(excluirButton);
			//}

			if (ocorrencia.obterSuperior() == null) {
				Button voltarButton = new Button("Volver", new Action(
						"visualizarPaginaInicial"));
				voltarButton.getAction().add("origemMenuId",
						ocorrencia.obterOrigem().obterId());
				table.addFooter(voltarButton);
			} else {
				Button voltarButton = new Button("Volver", new Action(
						"visualizarEvento"));
				voltarButton.getAction().add("id",
						ocorrencia.obterSuperior().obterId());
				voltarButton.getAction().add("origemMenuId",
						ocorrencia.obterOrigem().obterId());
				table.addFooter(voltarButton);
			}
		}
		return table;
	}

}