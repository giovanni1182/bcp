package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CodificacaoCobertura;
import com.gvs.crm.model.CodificacaoPlano;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class ListaCodificacaoCoberturaView extends PortalView
{

	private Collection coberturas;
	
	public ListaCodificacaoCoberturaView(Collection coberturas) throws Exception
	{
		this.coberturas = coberturas;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception 
	{
		Table table = new Table(4);
		table.setWidth("50%");
		table.addSubtitle("Coberturas");
		table.addStyle(Table.STYLE_ALTERNATE);
		
		table.addHeader("Codigo del Plan");
		table.addHeader("Nombre del Plan");
		table.addHeader("Codigo de la Cobertura");
		table.addHeader("Nombre de la Cobertura");
		
		for(Iterator i = coberturas.iterator() ; i.hasNext() ; )
		{
			CodificacaoCobertura cobertura = (CodificacaoCobertura) i.next();
			
			CodificacaoPlano plano = (CodificacaoPlano) cobertura.obterSuperior();
			
			table.add(plano.obterCodigo());
			table.add(plano.obterTitulo());
			
			table.add(cobertura.obterCodigo());
			
			Link link = new Link(cobertura.obterTitulo(),new Action("novoEvento"));
			link.getAction().add("classe", "codificacaorisco");
			link.getAction().add("superiorId", cobertura.obterId());
			
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
		return new Label("Codificación de Coberturas del Siniestro");
	}
}