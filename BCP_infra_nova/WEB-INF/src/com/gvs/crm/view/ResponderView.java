package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EventoComentarioInput;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class ResponderView extends PortalView {
	private Evento evento;

	private Entidade origemMenu;

	public ResponderView(Evento evento) throws Exception {
		this.evento = evento;
	}

	public ResponderView(Evento evento, Entidade origemMenu) throws Exception {
		this.evento = evento;
		this.origemMenu = origemMenu;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);
		table.setWidth("100%");
		table.setNextColSpan(table.getColumns());
		table.add(new EventoSumarioView(this.evento));
		table.addSubtitle("Resposta para "
				+ evento.obterResponsavelAnterior().obterNome());
		table.addHeader("Resposta:");
		table.addData(new EventoComentarioInput("comentario", ""));

		Button enviarButton = new Button("Responder", new Action(
				"responderEvento"));
		enviarButton.getAction().add("id", this.evento.obterId());
		enviarButton.getAction().add("origemMenuId", this.origemMenu.obterId());
		table.addFooter(enviarButton);

		Button voltarButton = new Button("Vovler", new Action(
				"visualizarEvento"));
		voltarButton.getAction().add("id", this.evento.obterId());
		table.addFooter(voltarButton);

		return table;
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception {
		return new Label(this.evento.obterClasseDescricao() + " - Resposta");
	}

	public Entidade getOrigemMenu() throws Exception {
		return this.origemMenu;
	}
}