package com.gvs.crm.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Mensagem;
import com.gvs.crm.model.Usuario;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class EventosPendentesView extends BasicView {
	private Usuario usuario;

	private Entidade origemMenu;

	public EventosPendentesView(Usuario usuario, Entidade origemMenu)
	{
		this.usuario = usuario;
		this.origemMenu = origemMenu;
	}

	public View execute(User user, Locale locale, Properties properties) throws Exception
	{
		String tipo = properties.getProperty("_tipoEvento");
		
		if(this.usuario.obterId() == 1)
			tipo = "outros";
		else
		{
			if (tipo == null)
				tipo = "mensagens";
		}

		Collection eventosMensagem = new ArrayList();
		Collection eventosOutros = new ArrayList();
		for (Iterator i = usuario.obterEventosPendentes().iterator(); i.hasNext();)
		{
			Evento evento = (Evento) i.next();

			if (evento instanceof Mensagem)
				eventosMensagem.add(evento);
			else 
				eventosOutros.add(evento);
		}

		Table table = new Table(1);
		table.setWidth("100%");
		table.addSubtitle("Eventos Pendientes");

		Label mensagensLabel = new Label("Mensajes (" + eventosMensagem.size()
				+ ")");
		mensagensLabel.setBold(tipo.equals("mensagens"));
		Link mensagensLink = new Link(mensagensLabel, new Action(
				"visualizarPaginaInicial"));
		mensagensLink.getAction().add("_tipoEvento", "mensagens");
		mensagensLink.getAction().add("origemMenuId", origemMenu.obterId());

		Label outrosLabel = new Label("Eventos (" + eventosOutros.size() + ")");
		outrosLabel.setBold(tipo.equals("outros"));
		Link outrosLink = new Link(outrosLabel, new Action(
				"visualizarPaginaInicial"));
		outrosLink.getAction().add("_tipoEvento", "outros");
		outrosLink.getAction().add("origemMenuId", origemMenu.obterId());

		Block linksBlock = new Block(Block.HORIZONTAL);
		linksBlock.add(mensagensLink);
		linksBlock.add(new SeparadorLabel());
		linksBlock.add(outrosLink);
		table.add(linksBlock);

		if (tipo.equals("mensagens"))
			table.add(new EventosView(eventosMensagem));
		else {
			Action action = new Action("visualizarPaginaInicial");
			action.add("id", this.usuario.obterId());
			action.add("origemMenuId", this.origemMenu.obterId());
			table.add(new EventosView(eventosOutros, action));
		}
		return table;
	}
}