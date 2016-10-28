package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.AgendaMeicos;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class ListaAgendaMeicosView extends PortalView {

	private Entidade entidade;

	private Collection agendas;

	public ListaAgendaMeicosView(Entidade entidade, Collection agendas)
			throws Exception {
		this.entidade = entidade;
		this.agendas = agendas;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(4);

		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);

		table.addHeader("Fecha");
		table.addHeader("Titulo");
		table.addHeader("Descripción");
		table.addHeader("Responsable");

		for (Iterator i = this.agendas.iterator(); i.hasNext();) {
			AgendaMeicos agenda = (AgendaMeicos) i.next();

			String data = new SimpleDateFormat("dd/MM/yyyy").format(agenda
					.obterDataPrevistaInicio());

			table.add(data);

			Link link = new Link(agenda.obterTitulo(), new Action(
					"visualizarEvento"));
			link.getAction().add("id", agenda.obterId());

			table.add(link);

			table.add(agenda.obterDescricao());

			table.add(agenda.obterResponsavel().obterNome());
		}

		return table;
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception {
		return new Label("Lista de Agenda de Meicos");
	}

	public Entidade getOrigemMenu() throws Exception {
		return this.entidade;
	}
}