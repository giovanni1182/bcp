package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EventoComentarioInput;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputString;
import infra.view.Table;
import infra.view.View;

public class CancelarView extends PortalView {

	private Evento evento;

	private Action action;

	public CancelarView(Evento evento, Action action) throws Exception 
	{
		this.evento = evento;
		this.action = action;
	}

	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		//table.setWidth("100%");
		table.setNextColSpan(table.getColumns());
		table.add(new EventoSumarioView(evento));

		table.addHeader("N� de Resoluci�n:");
		table.add(new InputString("numero", null, 10));
		table.addHeader("Fecha de la Resoluci�n:");
		table.add(new InputDate("data", null));

		table.addSubtitle("Cancelaci�n");
		table.addHeader("Cancelaci�n:");
		table.addData(new EventoComentarioInput("comentario", ""));

		Button enviarButton = new Button("Cancelar", action);
		enviarButton.getAction().add("id", evento.obterId());
		enviarButton.getAction().add("view", false);
		table.addFooter(enviarButton);

		Button voltarButton = new Button("Volver", new Action(
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
		return null;
	}

	public Entidade getOrigemMenu() throws Exception {
		return null;
	}

}