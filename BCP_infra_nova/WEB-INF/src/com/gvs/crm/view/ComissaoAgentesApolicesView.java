package com.gvs.crm.view;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class ComissaoAgentesApolicesView extends PortalView
{
	private AuxiliarSeguro agente;
	private Date dataInicio;
	private Date dataFim;
	private String situacao;
	private String plano;
	private Collection<Apolice> apolices;
	private boolean auxiliar;
	
	public ComissaoAgentesApolicesView(AuxiliarSeguro agente,Date dataInicio,Date dataFim, String situacao, String plano, Collection<Apolice> apolices, boolean auxiliar)
	{
		this.agente = agente;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.situacao = situacao;
		this.plano = plano;
		this.apolices = apolices;
		this.auxiliar = auxiliar;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		DecimalFormat format = new DecimalFormat("#,##0.00");
		
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		
		if(this.auxiliar)
			table.addHeader("Agente:");
		else
			table.addHeader("Corredor:");
		
		table.add(this.agente.obterNome());
		
		table.addHeader("Pólizas Vigentes desde:");
		table.add(new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
		
		table.addHeader("Situacion:");
		if(situacao.equals("0"))
			table.add("Todas");
		else
			table.add(situacao);
		
		table.addHeader("Sección:");
		table.add(plano);
		
		mainTable.add(table);
		
		table = new Table(8);
		table.addStyle(Table.STYLE_ALTERNATE);
		table.setWidth("100%");
		
		table.addSubtitle(this.apolices.size() + " Póliza(s)");
		
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Número");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Vig. Inicio");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Vig. Final");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Assegurado");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Situacion");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Capital Gs");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Prima Gs");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Comisión Gs");
		
		for(Iterator<Apolice> i = this.apolices.iterator() ; i.hasNext() ; )
		{
			Apolice apolice = i.next();
			
			Link link = new Link(apolice.obterNumeroApolice(), new Action("visualizarEvento"));
			link.getAction().add("id", apolice.obterId());
			link.setNovaJanela(true);
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(link);
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(new Label(apolice.obterDataPrevistaInicio(), "dd/MM/yyyy"));
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(new Label(apolice.obterDataPrevistaConclusao(), "dd/MM/yyyy"));
			
			table.add(apolice.obterNomeAsegurado());
			table.add(apolice.obterSituacaoSeguro());
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(format.format(apolice.obterCapitalGs()));
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(format.format(apolice.obterPrimaGs()));
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(format.format(apolice.obterComissaoGs()));
		}
		
		mainTable.add(table);
		
		return mainTable;
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
		if(this.auxiliar)
			return new Label("Pólizas Agentes por Sección");
		else
			return new Label("Pólizas Corredores de Seguros por Sección");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}