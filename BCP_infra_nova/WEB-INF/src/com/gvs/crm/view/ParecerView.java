package com.gvs.crm.view;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.EventoTituloInput;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Parecer;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ParecerView extends EventoAbstratoView {
	public View execute(User arg0, Locale arg1, Properties arg2)
			throws Exception {
		Parecer parecer = (Parecer) this.obterEvento();
		boolean incluir = parecer.obterId() == 0;
		Table table = new Table(2);

		table.addHeader("Superior:");
		if (parecer.obterSuperior() != null) {
			Action action = new Action("visualizarEvento");
			action.add("id", parecer.obterSuperior().obterId());

			Link superiorlink = new Link(parecer.obterSuperior().obterTitulo(),
					action);
			table.add(superiorlink);
		} else
			table.add("Ningún");

		if (!incluir) {
			table.addHeader("Creado por:");
			table.addData(new CriacaoLabel(parecer));
		}

		table.addHeader("Aseguradora:");
		table.addData(parecer.obterSuperior().obterOrigem().obterNome());

		table.addHeader("Tipo de la ocurrencia:");
		table.addData(parecer.obterSuperior().obterTipo());

		table.addHeader("Título:");
		table.addData(new EventoTituloInput("titulo", parecer, parecer
				.permiteAtualizar()));
		table.addHeader("Parecer:");
		if (parecer.permiteAtualizar())
			table.addData(new InputText("descricao", parecer.obterDescricao(),
					10, 100));
		else
			table.addData(new Label(parecer.obterDescricao()));

		if (incluir) {
			Button incluirButton = new Button("Agregar", new Action(
					"incluirParecer"));
			if (parecer.obterSuperior() != null)
				incluirButton.getAction().add("superiorId",
						parecer.obterSuperior().obterId());

			table.addFooter(incluirButton);

			Action voltarAction = new Action("novoEvento");
			voltarAction.add("passo", 2);
			voltarAction.add("origemMenuId", this.obterOrigemMenu().obterId());

			Button voltarButton = new Button("Volver", voltarAction);
			table.addFooter(voltarButton);
		} else {
			for (Iterator i = parecer.obterOrigem().obterContatos().iterator(); i
					.hasNext();) {
				Entidade.Contato contato = (Entidade.Contato) i.next();
				Table tableContatos = new Table(2);
				tableContatos.addHeader(contato.obterNome());
				tableContatos.addData(contato.obterValor());
				table.setNextColSpan(table.getColumns());
				table.addData(tableContatos);
			}

			if (parecer.obterOrigem().obterContatos().size() > 0) {
				table.setNextColSpan(table.getColumns());
				table.add(new Space());
			}

//			table.addHeader("Comentários:");
//			table.add(new ComentariosView(parecer));

//			Button comentarButton = new Button("Comentarios", new Action(
//					"comentarEvento"));
//			comentarButton.getAction().add("id", parecer.obterId());
//			comentarButton.getAction().add("view", true);
//			comentarButton.setEnabled(parecer.permiteAtualizar());
//			table.addFooter(comentarButton);

			if (parecer.permitePegar()) {
				Button pegarButton = new Button("Pegar", new Action(
						"pegarEvento"));
				pegarButton.getAction().add("id", parecer.obterId());
				pegarButton.getAction().add("origemMenuId",
						parecer.obterOrigem().obterId());
				table.addFooter(pegarButton);
			}

			if (parecer.permiteAtualizar()) {
				Button atualizarButton = new Button("Atualizar", new Action(
						"atualizarParecer"));
				atualizarButton.getAction().add("id", parecer.obterId());
				table.addFooter(atualizarButton);
			}

//			if (parecer.permiteResponder()) {
//				Button responderButton = new Button("Responder", new Action(
//						"responderEvento"));
//				responderButton.getAction().add("id", parecer.obterId());
//				responderButton.getAction().add("view", true);
//				responderButton.getAction().add("origemMenuId",
//						parecer.obterOrigem().obterId());
//				responderButton.setEnabled(parecer.permiteResponder());
//				table.addFooter(responderButton);
//			}

			if (parecer.permiteEncaminhar()) {
				Button encaminharButton = new Button("Remitir", new Action(
						"encaminharEvento"));
				encaminharButton.getAction().add("id", parecer.obterId());
				encaminharButton.getAction().add("view", true);
				encaminharButton.getAction().add("origemMenuId",
						parecer.obterOrigem().obterId());
				table.addFooter(encaminharButton);
			}

			if (parecer.permiteConcluir()) {
				Button concluirButton = new Button("Concluir", new Action(
						"concluirEvento"));
				concluirButton.getAction().add("id", parecer.obterId());
				concluirButton.getAction().add("view", true);
				concluirButton.getAction().add("origemMenuId",
						parecer.obterOrigem().obterId());
				table.addFooter(concluirButton);
			}

			if (parecer.permiteExcluir()) {
				Button excluirButton = new Button("Eliminar", new Action(
						"excluirEvento"));
				excluirButton.getAction().add("id", parecer.obterId());
				excluirButton.getAction().setConfirmation(
						"Confirma exclusão do evento ?");
				table.addFooter(excluirButton);
			}

		}

		Button voltarButton = new Button("Volver", new Action(
				"visualizarEvento"));
		voltarButton.getAction().add("id", parecer.obterSuperior().obterId());
		table.addFooter(voltarButton);

		return table;
	}
}