package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;

import infra.security.User;
import infra.view.HelpPopUp;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class HelpView extends PortalView {
	private Entidade entidade;

	public HelpView(Entidade entidade) throws Exception
	{
		this.entidade = entidade;
	}

	public View getBody(User arg0, Locale arg1, Properties arg2) throws Exception
	{
		Table table = new Table(1);

		HelpPopUp help = new HelpPopUp("MCO - Manual del Usuario - Módulo Contable", "help.htm");
		HelpPopUp help2 = new HelpPopUp("MCI - Manual del Usuario - Módulo Central de Información",	"help2.htm");

		table.add(help);
		table.add(new Space());
		table.add(help2);

		return table;
		
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception {
		return new Label("HELP");
	}

	public Entidade getOrigemMenu() throws Exception {
		return this.entidade;
	}
}