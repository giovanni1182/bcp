package com.gvs.crm.view;

import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.AseguradorasSelect2;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class TotalApolicesPorSecaoView extends PortalView 
{
	private Date data;
	
	public TotalApolicesPorSecaoView(Date data) throws Exception
	{
		this.data = data;
	}
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		
		table.addSubtitle("");
		
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect2("aseguradora",null, false, true));
		
		table.addHeader("Mes:");
		table.add(new InputDate("data", this.data));
		
		Button gerarButton = new Button("Generar",new Action("apolicesPorSecao"));
		
		table.addFooter(gerarButton);
		
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
		return new Label("Pólizas por Sección");
	}
}