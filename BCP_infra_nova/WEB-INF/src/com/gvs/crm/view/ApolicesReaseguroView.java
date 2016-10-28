package com.gvs.crm.view;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class ApolicesReaseguroView extends PortalView
{
	private Collection<Apolice> apolices;
	private String titulo;
	
	public ApolicesReaseguroView(Collection<Apolice> apolices, String titulo) throws Exception
	{
		this.apolices = apolices;
		this.titulo = titulo;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		DecimalFormat format = new DecimalFormat("#,##0.00");
		
		Table table = new Table(9);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);
		
		table.addSubtitle(apolices.size() + " Póliza(s)");
		
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
		table.addHeader("Capital Gs Póliza");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Prima Gs Póliza");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Comisión Gs Póliza");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Capital Dólar USA");
		
		for(Iterator<Apolice> i = apolices.iterator() ; i.hasNext() ; )
		{
			Apolice apolice = i.next();
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			Link link = new Link(apolice.obterNumeroApolice(), new Action("visualizarEvento"));
			link.setNovaJanela(true);
			link.getAction().add("id", apolice.obterId());
			table.add(link);
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaInicio()));
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaConclusao()));
			
			table.add(apolice.obterNomeAsegurado());
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(apolice.obterSituacaoSeguro());
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(format.format(apolice.obterCapitalGs()));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(format.format(apolice.obterPrimaGs()));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(format.format(apolice.obterComissaoGs()));
			
			String tipoMoeda = apolice.obterTipoMoedaCapitalGuarani();
			double capitalMe = 0;
			if(tipoMoeda.equals("Dólar USA"))
				capitalMe = apolice.obterCapitalMe();
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(format.format(capitalMe));
		}
		
		
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
		return new Label(titulo);
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}