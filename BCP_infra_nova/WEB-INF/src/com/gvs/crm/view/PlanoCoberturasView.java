package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.CodificacaoCobertura;
import com.gvs.crm.model.CodificacaoPlano;

import infra.control.Action;
import infra.view.Button;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class PlanoCoberturasView extends Table 
{
	public PlanoCoberturasView(CodificacaoPlano plano) throws Exception
	{
		super(3);
		this.addSubtitle("Coberturas");
		this.addStyle(STYLE_ALTERNATE);
		this.setWidth("50%");
		
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Codigo");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Nombre");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Creación");
		
		Map coberturas = new TreeMap();
		
		for(Iterator i = plano.obterInferiores().iterator() ; i.hasNext() ; )
		{
			CodificacaoCobertura cobertura = (CodificacaoCobertura) i.next();
			
			int codigo = Integer.parseInt(cobertura.obterCodigo());
			
			coberturas.put(new Integer(codigo), cobertura);
		}
			
		for(Iterator i = coberturas.values().iterator() ; i.hasNext() ; )
		{	
			CodificacaoCobertura cobertura = (CodificacaoCobertura) i.next();
			
			this.setNextHAlign(HALIGN_CENTER);
			this.add(cobertura.obterCodigo());
			
			Link link = new Link(cobertura.obterTitulo(),new Action("visualizarEvento"));
			link.getAction().add("id", cobertura.obterId());
			
			this.setNextHAlign(HALIGN_CENTER);
			this.add(link);
			
			this.setNextHAlign(HALIGN_CENTER);
			this.add(new SimpleDateFormat("dd/MM/yyyy").format(cobertura.obterCriacao()));
		}
		
		Button novaCobertura = new Button("Nueva Cobertura",new Action("novoEvento"));
		novaCobertura.getAction().add("classe", "codificacaocobertura");
		novaCobertura.getAction().add("superiorId", plano.obterId());
		
		this.setNextColSpan(getColumns());
		this.add(new Space());
		
		this.setNextColSpan(getColumns());
		this.add(novaCobertura);
	}
}
