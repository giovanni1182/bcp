package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadeNomeLink;
import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.security.User;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class AseguradoraUltimaAgendaView extends PortalView 
{
	private Entidade entidade;
	private Collection aseguradoras;
	private String view;
	
	public AseguradoraUltimaAgendaView(Entidade entidade, Collection aseguradoras, String view) throws Exception
	{
		this.entidade = entidade;
		this.aseguradoras = aseguradoras;
		this.view = view;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception 
	{
		Table table = new Table(2);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);
		table.addSubtitle("Ultimas Agendas Processadas");
		
		table.addHeader("Aseguradora");
		if(view.equals("MCO"))
			table.addHeader("Agendas MCO Aseguradoras");
		else if(view.equals("MCI"))
			table.addHeader("Agendas MCI Aseguradoras");
		else if(view.equals("MCO COA"))
			table.addHeader("Agendas MCO Coaseguradoras");
		else if(view.equals("MCI COA"))
			table.addHeader("Agendas MCI Coaseguradoras");
			
		for(Iterator i = this.aseguradoras.iterator() ; i.hasNext() ; )
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
			
			if(view.equals("MCO") || view.equals("MCO COA"))
			{
				if(aseguradora.obterId()!=5228)
				{
					table.add(new EntidadeNomeLink(aseguradora));
					
					AgendaMovimentacao ag = aseguradora.obterUltimaAgendaMCO();
					String mesAno = "";
					if(ag!=null)
					{
						if(ag.obterMesMovimento()<10)
							mesAno = "0" + ag.obterMesMovimento() + "/" + ag.obterAnoMovimento();
						else
							mesAno = ag.obterMesMovimento() + "/" + ag.obterAnoMovimento();
					}
					table.add(mesAno);
				}
			}
			else
			{
				table.add(new EntidadeNomeLink(aseguradora));
				AgendaMovimentacao ag = aseguradora.obterUltimaAgendaMCI();
				String mesAno = "";
				if(ag!=null)
				{
					if(ag.obterMesMovimento()<10)
						mesAno = "0" + ag.obterMesMovimento() + "/" + ag.obterAnoMovimento();
					else
						mesAno = ag.obterMesMovimento() + "/" + ag.obterAnoMovimento();
				}
				table.add(mesAno);
			}
			
			/*if((view.equals("MCO") && aseguradora.obterId()!=5228) || view.equals("MCI"))
			{
				Livro ultimoLivro = livroHome.obterUltimoLivro(aseguradora, "");
				table.setNextHAlign(Table.HALIGN_CENTER);
				if(ultimoLivro!=null)
				{
					Link link = new Link(ultimoLivro.obterMes() + "/" + ultimoLivro.obterAno(), new Action("visualizarEvento"));
					link.getAction().add("id", ultimoLivro.obterId());
					link.setNovaJanela(true);
					table.add(link);
				}
				else
					table.add("xx/xxxx");
			}*/
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
		return new Label("Ultimas Agendas Processadas");
	}

	public Entidade getOrigemMenu() throws Exception 
	{
		return this.entidade;
	}
}