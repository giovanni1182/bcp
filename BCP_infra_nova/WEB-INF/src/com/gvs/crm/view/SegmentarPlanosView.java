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

public class SegmentarPlanosView extends PortalView
{
	public SegmentarPlanosView()
	{
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(1);
		
		Link link = new Link("Sección", new Action("segmentarPlanos"));
		link.getAction().add("tipo", "secao");
		table.add(link);
		
		link = new Link("Modalidad", new Action("segmentarPlanos"));
		link.getAction().add("tipo", "modalidade");
		table.add(link);
		
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
		return new Label("Segmentar Planos");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}