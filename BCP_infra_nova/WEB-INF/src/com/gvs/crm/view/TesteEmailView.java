package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class TesteEmailView extends PortalView
{
	private String msg;
	
	public TesteEmailView(String msg)
	{
		this.msg = msg;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(1);
		
		if(msg!=null)
			table.add(msg);
		
		Button enviarButton = new Button("Enviar Giovanni gmail ", new Action("enviarEmail"));
		enviarButton.getAction().add("gmail", true);
		
		table.addFooter(enviarButton);
		
		enviarButton = new Button("Enviar Gilberto bcp", new Action("enviarEmail"));
		table.addFooter(enviarButton);
		
		return table;
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
		return new Label("Teste Email");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}