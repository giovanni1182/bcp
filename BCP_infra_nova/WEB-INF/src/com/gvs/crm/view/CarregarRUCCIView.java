package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputFile;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class CarregarRUCCIView extends PortalView
{

	private Entidade entidade;
	
	public CarregarRUCCIView(Entidade entidade) throws Exception
	{
		this.entidade = entidade;
	}
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		
		table.addHeader("Archivo:");
		table.addData(new InputFile("file", ""));

		Button carregarButton = new Button("Cargar", new Action("carregarRUC")); 
		table.addFooter(carregarButton);
		
		return table;
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return this.entidade;
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
		return new Label("Carga RUC y CI");
	}

}
