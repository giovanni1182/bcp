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
import infra.view.Table;
import infra.view.View;

public class LocalizarEntidadesView extends PortalView {
	private String nome;

	private Collection entidades;

	public LocalizarEntidadesView(String nome, Collection entidades)
			throws Exception {
		this.nome = nome;
		this.entidades = entidades;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);
		table.setWidth("100%");
		table.addSubtitle("Localização");

		table.addHeader("Nome:");
		table.addData(new InputString("nome", this.nome, 80));

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add(new Button("Buscar", new Action("localizarEntidades")));

		table.addSubtitle("Entidades");
		table.setNextColSpan(table.getColumns());
		table.add(new EntidadesView(this.entidades));

		return table;
	}

	public String getSelectedGroup() throws Exception {
		return "Entidades";
	}

	public String getSelectedOption() throws Exception {
		return "Localizar";
	}

	public View getTitle() throws Exception {
		return new Label("Localizar Entidades");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gvs.crm.view.PortalView#getOrigemMenu()
	 */
	public Entidade getOrigemMenu() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}