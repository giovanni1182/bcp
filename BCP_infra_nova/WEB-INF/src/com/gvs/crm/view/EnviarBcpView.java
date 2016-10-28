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

public class EnviarBcpView extends PortalView {
	private Evento evento;

	private Entidade origemMenu;

	public EnviarBcpView(Evento evento) throws Exception {
		this.evento = evento;
	}

	public EnviarBcpView(Evento evento, Entidade origemMenu) throws Exception {
		this.evento = evento;
		this.origemMenu = origemMenu;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);
		table.setWidth("100%");
		table.setNextColSpan(table.getColumns());
		table.add(new EventoSumarioView(evento));
		table.addSubtitle("Conclusão");
		table.addHeader("Conclusão:");
		table.addData(new EventoComentarioInput("comentario", ""));

		Button enviarButton = new Button("Enviar", new Action("enviarBcp"));
		enviarButton.getAction().add("id", evento.obterId());
		enviarButton.getAction().add("origemMenuId", this.origemMenu.obterId());
		table.addFooter(enviarButton);

		Button voltarButton = new Button("Voltar", new Action(
				"visualizarEvento"));
		voltarButton.getAction().add("id", evento.obterId());
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
		return new Label(evento.obterClasseDescricao() + " - Enviar");
	}

	public Entidade getOrigemMenu() throws Exception {
		return this.origemMenu;
	}
}