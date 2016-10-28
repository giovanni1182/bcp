package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Check;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class AgendarDesagendarView extends PortalView
{
	private Collection agendasPendentes;
	private Collection agendasAgendadas;
	
	public AgendarDesagendarView(Collection agendasPendentes, Collection agendasAgendadas) throws Exception
	{
		this.agendasPendentes = agendasPendentes;
		this.agendasAgendadas = agendasAgendadas;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(5);
		
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);
		table.addSubtitle(this.agendasPendentes.size() + " Agendas Instrumento Pendientes");
		
		table.addHeader("");
		table.addHeader("Mes/Año");
		table.addHeader("Validación");
		table.addHeader("Aseguradora");
		table.addHeader("Fecha Agenda");
		
		for(Iterator i = this.agendasPendentes.iterator() ; i.hasNext() ; )
		{
			AgendaMovimentacao agenda = (AgendaMovimentacao) i.next();
			
			Check check = new Check("escolha","escolha_" + agenda.obterId(),false);
			
			table.add(check);
			
			String mesAnoStr = "";
			
			if(new Integer(agenda.obterMesMovimento()).toString().length() == 1)
				mesAnoStr= "0" + agenda.obterMesMovimento();
			else
				mesAnoStr = "" + agenda.obterMesMovimento();
			
			mesAnoStr+="/" + agenda.obterAnoMovimento();
			
			Link link = new Link(mesAnoStr,new Action("visualizarEvento"));
			link.getAction().add("id", agenda.obterId());
			
			table.add(link);
			if(agenda.obterValidacao() == null)
				table.add("Total");
			else
				table.add(agenda.obterValidacao());
			table.add(agenda.obterOrigem().obterNome());
			table.add(new SimpleDateFormat("dd/MM/yyyy").format(agenda.obterDataPrevistaInicio()));
		}
		
		Button agendarButton = new Button("Agendar",new Action("agendarAgendas"));
		agendarButton.setEnabled(this.agendasPendentes.size() > 0);
		
		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add(agendarButton);
		
		table.addSubtitle(this.agendasAgendadas.size() + " Agendas Instrumento Agendadas");
		
		table.addHeader("");
		table.addHeader("Mes/Año");
		table.addHeader("Validación");
		table.addHeader("Aseguradora");
		table.addHeader("Fecha Agenda");
		
		for(Iterator i = this.agendasAgendadas.iterator() ; i.hasNext() ; )
		{
			AgendaMovimentacao agenda = (AgendaMovimentacao) i.next();
			
			Check check = new Check("escolha","escolha_" + agenda.obterId(),false);
			
			table.add(check);
			
			String mesAnoStr = "";
			
			if(new Integer(agenda.obterMesMovimento()).toString().length() == 1)
				mesAnoStr= "0" + agenda.obterMesMovimento();
			else
				mesAnoStr = "" + agenda.obterMesMovimento();
			
			mesAnoStr+="/" + agenda.obterAnoMovimento();
			
			Link link = new Link(mesAnoStr,new Action("visualizarEvento"));
			link.getAction().add("id", agenda.obterId());
			
			table.add(link);
			if(agenda.obterValidacao() == null)
				table.add("Total");
			else
				table.add(agenda.obterValidacao());
			table.add(agenda.obterOrigem().obterNome());
			table.add(new SimpleDateFormat("dd/MM/yyyy").format(agenda.obterDataPrevistaInicio()));
		}
		
		Button desagendarButton = new Button("Desagendar",new Action("desagendarAgendas"));
		desagendarButton.setEnabled(this.agendasAgendadas.size() > 0);
		
		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add(desagendarButton);
		
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
		return new Label("Agendar/Desagendar Agendas de Instrumento");
	}
}