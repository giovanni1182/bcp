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

public class ComentarioView extends PortalView
{
	private Evento evento;

	private Entidade origemMenu;

	public ComentarioView(Evento evento) throws Exception
	{
		this.evento = evento;
	}

	public ComentarioView(Evento evento, Entidade origemMenu) throws Exception
	{
		this.evento = evento;
		this.origemMenu = origemMenu;
	}

	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		mainTable.add(new EventoSumarioView(evento));
		
		Table table = new Table(2);
		
		table.addSubtitle("Comentario");
		table.addHeader("Comentario:");
		table.addData(new EventoComentarioInput("comentario", ""));

		Button enviarButton = new Button("Agregar Comentario", new Action(
				"comentarEvento"));
		enviarButton.getAction().add("id", evento.obterId());
		table.addFooter(enviarButton);

		Button voltarButton = new Button("Volver", new Action(
				"visualizarEvento"));
		voltarButton.getAction().add("id", evento.obterId());
		table.addFooter(voltarButton);
		
		mainTable.add(table);
		
		return mainTable;
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception {
		return new Label(evento.obterClasseDescricao() + " - Comentar");
	}

	public Entidade getOrigemMenu() throws Exception {
		return evento.obterOrigem();
	}
}