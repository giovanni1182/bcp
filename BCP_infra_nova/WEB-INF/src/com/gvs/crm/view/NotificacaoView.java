package com.gvs.crm.view;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.EventoTipoSelect;
import com.gvs.crm.component.EventoTituloInput;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Notificacao;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class NotificacaoView extends EventoAbstratoView {
	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);

		Notificacao notificacao = (Notificacao) this.obterEvento();
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		boolean incluir = notificacao.obterId() == 0;
		Usuario usuarioAtual = (Usuario) usuarioHome.obterUsuarioPorUser(user);

		Table table = new Table(2);

		table.addHeader("Superior:");
		if (notificacao.obterSuperior() != null) {
			Action action = new Action("visualizarEvento");
			action.add("id", notificacao.obterSuperior().obterId());

			Link superiorlink = new Link(notificacao.obterSuperior()
					.obterTitulo(), action);
			table.add(superiorlink);
		} else
			table.add("Ningún");

		if (!incluir) {
			table.addHeader("Creado por:");
			table.addData(new CriacaoLabel(notificacao));
		}
		table.addHeader("Aseguradora:");
		table
				.addData(new EntidadePopup("seguradoraId", "seguradoraNome",
						notificacao.obterOrigem(), notificacao
								.permiteAtualizar(), true));

		table.addHeader("Responsable:");
		table.addData(new EntidadePopup("responsavelId", "responsavelNome",
				notificacao.obterResponsavel(), "Usuario", notificacao
						.permiteAtualizar()));

		table.addHeader("Tipo de Notificación:");
		table.addData(new EventoTipoSelect("tipo", notificacao, notificacao
				.permiteAtualizar()));
		table.addHeader("Título:");
		table.addData(new EventoTituloInput("titulo", notificacao, notificacao
				.permiteAtualizar()));
		table.addHeader("Notificación:");
		if (notificacao.permiteAtualizar())
			table.addData(new InputText("descricao", notificacao
					.obterDescricao(), 20, 100));
		else
			table.addData(new Label(notificacao.obterDescricao()));

		if (incluir) {
			Button incluirButton = new Button("Incluir", new Action(
					"incluirNotificacao"));
			incluirButton.getAction().add("origemMenuId",
					this.obterOrigemMenu().obterId());
			table.addFooter(incluirButton);

			Action voltarAction = new Action("novoEvento");
			voltarAction.add("passo", 2);
			voltarAction.add("origemMenuId", this.obterOrigemMenu().obterId());

			Button voltarButton = new Button("Volver", voltarAction);
			table.addFooter(voltarButton);
		} else {
			for (Iterator i = notificacao.obterOrigem().obterContatos()
					.iterator(); i.hasNext();) {
				Entidade.Contato contato = (Entidade.Contato) i.next();
				Table tableContatos = new Table(2);
				tableContatos.addHeader(contato.obterNome());
				tableContatos.addData(contato.obterValor());
				table.setNextColSpan(table.getColumns());
				table.addData(tableContatos);
			}

			if (!notificacao.obterInferiores().isEmpty()) {
				table.addHeader("Sub-eventos:");
				table.add(new SubEventosView(notificacao));
			}

			if (notificacao.obterTipo().equals("Notificación de Reclamação")) {
				/*
				 * if (notificacao.permitePegar()) { Button pegarButton = new
				 * Button("Pegar", new Action("pegarEvento"));
				 * pegarButton.getAction().add("id", notificacao.obterId());
				 * pegarButton.getAction().add("origemMenuId",
				 * notificacao.obterOrigem().obterId());
				 * table.addFooter(pegarButton); }
				 * 
				 * if (notificacao.permiteDevolver()) { Button devolverButton =
				 * new Button("Devolver", new Action("devolverEvento"));
				 * devolverButton.getAction().add("id", notificacao.obterId());
				 * devolverButton.getAction().add("origemMenuId",
				 * notificacao.obterOrigem().obterId());
				 * table.addFooter(devolverButton); }
				 */

				/*
				 * if (notificacao.permiteIncluirEventoInferior()) { Button
				 * novoEventoButton = new Button("Sub-evento", new
				 * Action("novoEvento"));
				 * novoEventoButton.getAction().add("passo", 3);
				 * novoEventoButton.getAction().add("superiorId",
				 * notificacao.obterId());
				 * novoEventoButton.getAction().add("origemMenuId",
				 * notificacao.obterOrigem().obterId());
				 * table.addFooter(novoEventoButton); }
				 */

				if (notificacao.permiteAtualizar()) {
					Button atualizarButton = new Button("Actualizar",
							new Action("atualizarNotificacao"));
					atualizarButton.getAction()
							.add("id", notificacao.obterId());
					atualizarButton.getAction().add("origemMenuId",
							notificacao.obterOrigem().obterId());
					table.addFooter(atualizarButton);
				}

				if (notificacao.permiteResponder()) {
					Button responderButton = new Button("Responder",
							new Action("responderEvento"));
					responderButton.getAction()
							.add("id", notificacao.obterId());
					responderButton.getAction().add("view", true);
					responderButton.getAction().add("origemMenuId",
							notificacao.obterOrigem().obterId());
					responderButton.setEnabled(notificacao.permiteResponder());
					table.addFooter(responderButton);
				}

				if (notificacao.permiteEncaminhar()) {
					Button encaminharButton = new Button("Remitir", new Action(
							"encaminharEvento"));
					encaminharButton.getAction().add("id",
							notificacao.obterId());
					encaminharButton.getAction().add("view", true);
					encaminharButton.getAction().add("origemMenuId",
							notificacao.obterOrigem().obterId());
					table.addFooter(encaminharButton);
				}
			}

			//if (usuarioAtual.obterId() == 1 || usuarioAtual.obterId() == 3)
			//{
			Button concluirButton = new Button("Concluir", new Action(
					"concluirEvento"));
			concluirButton.getAction().add("id", notificacao.obterId());
			concluirButton.getAction().add("view", true);
			concluirButton.getAction().add("origemMenuId",
					notificacao.obterOrigem().obterId());
			concluirButton.setEnabled(notificacao.permiteConcluir());
			table.addFooter(concluirButton);
			//}

			if (usuarioAtual.obterId() == 1 || usuarioAtual.obterId() == 3) {
				Button excluirButton = new Button("Eliminar", new Action(
						"excluirEvento"));
				excluirButton.getAction().add("id", notificacao.obterId());
				excluirButton.getAction().setConfirmation(
						"Confirma exclusion ?");
				table.addFooter(excluirButton);
			}

			if (notificacao.obterSuperior() == null) {
				Button voltarButton = new Button("Volver", new Action(
						"visualizarPaginaInicial"));
				voltarButton.getAction().add("origemMenuId",
						notificacao.obterOrigem().obterId());
				table.addFooter(voltarButton);
			} else {
				Button voltarButton = new Button("Volver", new Action(
						"visualizarEvento"));
				voltarButton.getAction().add("id",
						notificacao.obterSuperior().obterId());
				voltarButton.getAction().add("origemMenuId",
						notificacao.obterOrigem().obterId());
				table.addFooter(voltarButton);
			}
			
			Button pdf = new Button("Visualizar PDF",new Action("pdfAgenda"));
			pdf.getAction().add("notificacaoId", notificacao.obterId());
			
			table.addFooter(pdf);
		}
		return table;
	}

}