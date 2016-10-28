package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class SelecionarUltimasAgendasView extends PortalView
{

	private Entidade entidade;
	
	public SelecionarUltimasAgendasView(Entidade entidade) throws Exception
	{
		this.entidade = entidade;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(1);
		
		Link link = new Link("Agendas MCO Aseguradoras",new Action("visualizarCentralRiscoAgendas"));
		link.getAction().add("view", "MCO");
		
		Link link2 = new Link("Agendas MCI Aseguradoras",new Action("visualizarCentralRiscoAgendas"));
		link2.getAction().add("view", "MCI");
		
		Link link3 = new Link("Agendas MCI Coaseguradoras",new Action("visualizarCentralRiscoAgendas"));
		link3.getAction().add("view", "MCI COA");
		
		Link link4 = new Link("Agendas MCO Coaseguradoras",new Action("visualizarCentralRiscoAgendas"));
		link4.getAction().add("view", "MCO COA");
		
		table.add(link);
		table.add("");
		table.add("");
		table.add("");
		table.add(link2);
		table.add("");
		table.add("");
		table.add("");
		table.add(link3);
		table.add("");
		table.add("");
		table.add("");
		table.add(link4);
		
		
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
		return new Label("Eligir el tipo de Agenda");
	}
}