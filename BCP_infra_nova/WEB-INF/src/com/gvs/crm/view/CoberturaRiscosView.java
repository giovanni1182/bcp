package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.CodificacaoCobertura;
import com.gvs.crm.model.CodificacaoRisco;

import infra.control.Action;
import infra.view.Button;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class CoberturaRiscosView extends Table 
{
	public CoberturaRiscosView(CodificacaoCobertura cobertura) throws Exception
	{
		super(3);
		this.addSubtitle("Riesgos");
		this.addStyle(STYLE_ALTERNATE);
		this.setWidth("50%");
		
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Codigo");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Nombre");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Creación");
		
		Map riscos = new TreeMap();
		
		for(Iterator i = cobertura.obterInferiores().iterator() ; i.hasNext() ; )
		{
			CodificacaoRisco risco = (CodificacaoRisco) i.next();
			
			int codigo = Integer.parseInt(risco.obterCodigo());
			
			riscos.put(new Integer(codigo), risco);
		}
			
		for(Iterator i = riscos.values().iterator() ; i.hasNext() ; )
		{	
			CodificacaoRisco risco = (CodificacaoRisco) i.next();
			
			this.setNextHAlign(HALIGN_CENTER);
			this.add(risco.obterCodigo());
			
			Link link = new Link(risco.obterTitulo(),new Action("visualizarEvento"));
			link.getAction().add("id", risco.obterId());
			
			this.setNextHAlign(HALIGN_CENTER);
			this.add(link);
			
			this.setNextHAlign(HALIGN_CENTER);
			this.add(new SimpleDateFormat("dd/MM/yyyy").format(risco.obterCriacao()));
		}
		
		Button novoRisco = new Button("Nuevo Riesgo",new Action("novoEvento"));
		novoRisco.getAction().add("classe", "codificacaorisco");
		novoRisco.getAction().add("superiorId", cobertura.obterId());
		
		this.setNextColSpan(getColumns());
		this.add(new Space());
		
		this.setNextColSpan(getColumns());
		this.add(novoRisco);
	}
}
