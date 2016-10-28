package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AspectosLegais;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class ApolicesDemandaJudicialView extends PortalView
{
	private Aseguradora aseguradora;
	private ClassificacaoContas secao;
	private Date dataInicio;
	private Date dataFim;
	private Collection<AspectosLegais> aspectos;
	
	public ApolicesDemandaJudicialView(Aseguradora aseguradora, ClassificacaoContas secao, Date dataInicio, Date dataFim, Collection<AspectosLegais> aspectos) throws Exception
	{
		this.aseguradora = aseguradora;
		this.secao = secao;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.aspectos = aspectos;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(6);
		table.setWidth("100%");
		table.addSubtitle("Sección: " + this.secao.obterNome() + " - Periodo: " + new SimpleDateFormat("dd/MM/yyyy").format(this.dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(this.dataFim));
		table.addStyle(Table.STYLE_ALTERNATE);
		
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Póliza");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Fecha Notificación");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Actor o Demandante");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Demandado");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Monto Demandado");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Provisión Pendiente");
		
		double totalMontante = 0;
		double totalSinistro = 0;
		
		Map<Long,Double> montantes = new TreeMap<Long,Double>();
		Map<Long,Double> sinistros = new TreeMap<Long,Double>();
		Map<Long,AspectosLegais> aspectos2 = new TreeMap<Long,AspectosLegais>();
		
		for(Iterator<AspectosLegais> i = this.aspectos.iterator() ; i.hasNext() ; )
		{
			AspectosLegais aspecto = i.next();
			Apolice apolice = (Apolice) aspecto.obterSuperior();
			
			if(montantes.containsKey(apolice.obterId()))
			{
				double valor = montantes.get(apolice.obterId());
				valor+=aspecto.obterMontanteDemandado();
				
				montantes.put(apolice.obterId(), valor);
			}
			else
				montantes.put(apolice.obterId(), aspecto.obterMontanteDemandado());
			
			if(sinistros.containsKey(apolice.obterId()))
			{
				double valor = sinistros.get(apolice.obterId());
				valor+=aspecto.obterSinistroPendente();
				
				sinistros.put(apolice.obterId(), valor);
			}
			else
				sinistros.put(apolice.obterId(), aspecto.obterSinistroPendente());
			
			aspectos2.put(apolice.obterId(), aspecto);
		}
		
		for(Iterator<AspectosLegais> i = aspectos2.values().iterator() ; i.hasNext() ; )
		{
			AspectosLegais aspecto = i.next();
			
			Apolice apolice = (Apolice) aspecto.obterSuperior();
			
			Link link = new Link(apolice.obterNumeroApolice(),new Action("visualizarEvento"));
			link.getAction().add("id", apolice.obterId());
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(link);
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			if(aspecto.obterDataNotificacao()!=null)
				table.add(new SimpleDateFormat("dd/MM/yyyy").format(aspecto.obterDataNotificacao()));
			else
				table.add("");
			table.add(aspecto.obterDemandante());
			table.add(aspecto.obterDemandado());
			
			//double montante = aspecto.obterMontanteDemandado();
			double montante = montantes.get(apolice.obterId());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(montante,"#,##0.00"));
			totalMontante+=montante;
			
			//double sinistro = aspecto.obterSinistroPendente();
			double sinistro = sinistros.get(apolice.obterId());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(sinistro,"#,##0.00"));
			totalSinistro+=sinistro;
		}
		
		table.add("");
		table.add("");
		table.add("");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("TOTAL");
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(new Label(totalMontante,"#,##0.00"));
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(new Label(totalSinistro,"#,##0.00"));
		
		Button excelButton = new Button("Generar Excel",new Action("listaDemandaJudicialExcel"));
		excelButton.getAction().add("origemId", this.aseguradora.obterId());
		excelButton.getAction().add("secaoId", this.secao.obterId());
		excelButton.getAction().add("dataInicio", this.dataInicio);
		excelButton.getAction().add("dataFim", this.dataFim);
		
		table.addFooter(excelButton);
		
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
		return new Label("Pólizas Demanda Judicial " + this.aseguradora.obterNome());
	}
}