package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ListaApolicesSuspeitasView extends PortalView
{

	private String rucCi;
	private Collection apolices;
	private Entidade entidade;
	
	public ListaApolicesSuspeitasView(String rucCi, Collection apolices, Entidade entidade) throws Exception
	{
		this.rucCi = rucCi;
		this.apolices = apolices;
		this.entidade = entidade;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(10);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);
		
		table.addHeader("Vigencia");
		table.addHeader("Numero");
		table.addHeader("Sección");
		table.addHeader("Plan");
		table.addHeader("Asegurado");
		table.addHeader("Tipo Doc.");
		table.addHeader("Numero Doc.");
		table.addHeader("Tipo");
		table.addHeader("Aseguradora");
		table.addHeader("Situación");
		
		for(Iterator i = this.apolices.iterator() ; i.hasNext() ; )
		{
			Apolice apolice = (Apolice) i.next();
			
			String dataInicio = new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaInicio());
			String dataFim = new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaConclusao());
			
			Block block = new Block(Block.HORIZONTAL);
			
			block.add(new Label(dataInicio));
			block.add(new Space());
			block.add(new Label("-"));
			block.add(new Space());
			block.add(new Label(dataFim));
			
			table.add(block);
			
			Link link = new Link(apolice.obterNumeroApolice(), new Action("visualizarEvento"));
			link.getAction().add("id", apolice.obterId());
			table.add(link);
			table.add(apolice.obterSecao().obterApelido());
			table.add(apolice.obterPlano().obterPlano());
			table.add(apolice.obterNomeAsegurado());
			table.add(apolice.obterTipoIdentificacao());
			table.add(apolice.obterNumeroIdentificacao());
			table.add(apolice.obterTipo());
			table.add(apolice.obterOrigem().obterNome());
			table.add(apolice.obterSituacaoSeguro());
		}
		
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
		
		return new Label(this.apolices.size() + " Polizas Sospechosas");
	}

}
