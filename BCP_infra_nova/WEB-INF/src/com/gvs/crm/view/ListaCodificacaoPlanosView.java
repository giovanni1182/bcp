package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CodificacaoPlano;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class ListaCodificacaoPlanosView extends PortalView 
{
	private Collection planos;
	
	public ListaCodificacaoPlanosView(Collection planos) throws Exception
	{
		this.planos = planos;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception 
	{
		Table table = new Table(2);
		table.setWidth("20%");
		table.addSubtitle("Planes");
		table.addStyle(Table.STYLE_ALTERNATE);
		
		table.addHeader("Codigo");
		table.addHeader("Nombre");
		
		for(Iterator i = planos.iterator() ; i.hasNext() ; )
		{
			CodificacaoPlano plano = (CodificacaoPlano) i.next();
			
			table.add(plano.obterCodigo());
			
			Link link = new Link(plano.obterTitulo(),new Action("novoEvento"));
			link.getAction().add("classe", "codificacaocobertura");
			link.getAction().add("superiorId", plano.obterId());
			
			table.add(link);
		}
		
		Button voltarButton = new Button("Volver",new Action("novoEvento"));
		voltarButton.getAction().add("passo", 2);
		
		table.addFooter(voltarButton);
		
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
		return new Label("Codificación de Planes del Siniestro");
	}
}


