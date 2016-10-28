package com.gvs.crm.view;

import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class LocalizacaoView extends PortalView {
	private String pesquisaEntidade;

	private String pesquisaEvento;

	private Collection entidades;

	private Collection eventos;

	private Entidade origemMenu;

	public LocalizacaoView(String pesquisaEntidade, String pesquisaEvento,
			Collection entidades, Collection eventos, Entidade origemMenu) {
		this.pesquisaEntidade = pesquisaEntidade;
		this.pesquisaEvento = pesquisaEvento;
		this.entidades = entidades;
		this.eventos = eventos;
		this.origemMenu = origemMenu;
	}

	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);

		table.addHeader("Buscar Entidades:");
		table.add(new InputString("pesquisaEntidade", this.pesquisaEntidade,80));
		table.addFooter(new Button("Buscar Entidades", new Action("localizar")));
		
		mainTable.add(table);
		mainTable.addSubtitle("Entidades");
		mainTable.add(new EntidadesView(this.entidades));
		
		mainTable.add(new Space());

		table = new Table(2);
		table.addHeader("Buscar Eventos:");
		table.add(new InputString("pesquisaEvento", this.pesquisaEvento, 80));
		table.addFooter(new Button("Buscar Eventos", new Action("localizar")));
		
		mainTable.add(table);
		mainTable.addSubtitle("Eventos");
		mainTable.add(new EventosView(this.eventos));
		
		return mainTable;
	}

	public String getSelectedGroup() throws Exception {
		return "Entidades/Eventos";
	}

	public String getSelectedOption() throws Exception {
		return "Localizar";
	}

	public View getTitle() throws Exception {
		return new Label("Buscar entidad y eventos");
	}

	public Entidade getOrigemMenu() throws Exception {
		return this.origemMenu;

	}
}