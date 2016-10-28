package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.CodificacaoDetalhe;
import com.gvs.crm.model.CodificacaoRisco;

import infra.control.Action;
import infra.view.Button;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class RiscoDetalhesView extends Table 
{
	public RiscoDetalhesView(CodificacaoRisco risco) throws Exception
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
		
		Map detalhes = new TreeMap();
		
		for(Iterator i = risco.obterInferiores().iterator() ; i.hasNext() ; )
		{
			CodificacaoDetalhe detalhe = (CodificacaoDetalhe) i.next();
			
			int codigo = Integer.parseInt(detalhe.obterCodigo());
			
			detalhes.put(new Integer(codigo), detalhe);
		}
			
		for(Iterator i = detalhes.values().iterator() ; i.hasNext() ; )
		{	
			CodificacaoDetalhe detalhe = (CodificacaoDetalhe) i.next();
			
			this.setNextHAlign(HALIGN_CENTER);
			this.add(detalhe.obterCodigo());
			
			Link link = new Link(detalhe.obterTitulo(),new Action("visualizarEvento"));
			link.getAction().add("id", detalhe.obterId());
			
			this.setNextHAlign(HALIGN_CENTER);
			this.add(link);
			
			this.setNextHAlign(HALIGN_CENTER);
			this.add(new SimpleDateFormat("dd/MM/yyyy").format(detalhe.obterCriacao()));
		}
		
		Button novoDetalhe = new Button("Nuevo Detalle",new Action("novoEvento"));
		novoDetalhe.getAction().add("classe", "codificacaodetalhe");
		novoDetalhe.getAction().add("superiorId", risco.obterId());
		
		this.setNextColSpan(getColumns());
		this.add(new Space());
		
		this.setNextColSpan(getColumns());
		this.add(novoDetalhe);
	}
}
