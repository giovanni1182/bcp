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

public class CancelarProcessoView extends PortalView {
	private Evento evento;

	public CancelarProcessoView(Evento evento) throws Exception {
		this.evento = evento;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);
		table.setWidth("100%");
		table.setNextColSpan(table.getColumns());
		table.add(new EventoSumarioView(this.evento));
		table.addSubtitle(evento.obterClasseDescricao());

		table.addHeader("Comentário:");
		table.addData(new EventoComentarioInput("comentario", ""));

		Button enviarButton = new Button("Cancelar", new Action(
				"cancelarProcesso"));
		enviarButton.getAction().add("id", this.evento.obterId());
		table.addFooter(enviarButton);

		Button voltarButton = new Button("Voltar", new Action(
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
		return new Label(this.evento.obterClasseDescricao());
	}

	public Entidade getOrigemMenu() throws Exception {
		return evento.obterOrigem();
	}
}