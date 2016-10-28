package com.gvs.crm.view;

import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class RelatorioSetorEconomicoView extends PortalView
{
	private Date data;
	
	public RelatorioSetorEconomicoView(Date data) throws Exception
	{
		this.data = data;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		
		table.addSubtitle("");
		
		table.addHeader("Fecha:");
		table.add(new InputDate("data", this.data));
		
		Button b = new Button("Generar",new Action("visualizarRelEconomico"));
		
		table.addFooter(b);
		
		return table;
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}

	public String getSelectedGroup() throws Exception
	{
		return null;
	}

	public String getSelectedOption() throws Exception
	{
		return null;
	}

	public View getTitle() throws Exception
	{
		return new Label("Listado - Cuentas Contables");
	}
}