package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.component.AseguradorasSelect;
import com.gvs.crm.component.PeriodoDatasBlock;
import com.gvs.crm.component.PlanoRamoSelect;
import com.gvs.crm.component.PlanoSecao2Select;
import com.gvs.crm.component.PlanoSelect;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class ApolicesPorPessoaView extends PortalView
{
	private Aseguradora aseguradora;
	private String ramo, secao, modalidade;
	private Date dataInicio;
	private Date dataFim;
	private Collection<Aseguradora> aseguradoras;
	private boolean geraNaTela;
	
	public ApolicesPorPessoaView(Aseguradora aseguradora, Date dataInicio, Date dataFim, Collection<Aseguradora> aseguradoras, boolean geraNaTela, String ramo, String secao,String modalidade)
	{
		this.aseguradora = aseguradora;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.aseguradoras = aseguradoras;
		this.geraNaTela = geraNaTela;
		this.ramo = ramo;
		this.secao = secao;
		this.modalidade = modalidade;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect("aseguradora", aseguradora, true));
		table.addHeader("Ramo:");
		Action action = new Action("apolicesPorTipoPessoa");
		action.add("view", true);
		
		table.add(new PlanoRamoSelect("ramo", ramo, action, false, true));
		
		table.addHeader("Sección:");
		table.add(new PlanoSecao2Select("secao", ramo, secao, action));
		
		table.addHeader("Modalidad:");
	    if (!this.secao.equals(""))
	    	table.add(new PlanoSelect("modalidade", secao, modalidade, null, true));
	    else
	      table.add("");
		table.addHeader("Fecha:");
		table.add(new PeriodoDatasBlock("dataInicio", dataInicio, "dataFim", dataFim, false));
		
		Button consultarButton = new Button("Generar en la Pantalla", new Action("apolicesPorTipoPessoa"));
		table.addFooter(consultarButton);
		
		consultarButton = new Button("Generar Excel", new Action("apolicesPorTipoPessoa"));
		consultarButton.getAction().add("excel", true);
		table.addFooter(consultarButton);
		
		mainTable.add(table);
		
		if(dataInicio!=null && dataFim!=null && geraNaTela)
		{
			Calendar c = Calendar.getInstance();
			c.setTime(dataInicio);
			int primeiroDia = c.getActualMinimum(Calendar.DAY_OF_MONTH);
			
			c.setTime(dataFim);
			int ultimoDia = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			
			String mesAnoInicio = new SimpleDateFormat("MM/yyyy").format(dataInicio);
			String mesAnoFim = new SimpleDateFormat("MM/yyyy").format(dataFim);
			
			Date dataInicioReal = new SimpleDateFormat("dd/MM/yyyy").parse(primeiroDia + "/"+mesAnoInicio);
			Date dataFimReal = new SimpleDateFormat("dd/MM/yyyy").parse(ultimoDia + "/"+mesAnoFim);
			
			c.setTime(dataInicioReal);
			
			int tamanhoTabela = 1;
			
			while(c.getTime().compareTo(dataFimReal)<=0)
			{
				tamanhoTabela++;
				c.add(Calendar.MONTH, 1);
			}
			
			c.setTime(dataInicioReal);
			
			table = new Table(tamanhoTabela);
			table.setWidth("100%");
			table.addStyle(Table.STYLE_ALTERNATE);
			table.addSubtitle("");
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Aseguradora");
			
			while(c.getTime().compareTo(dataFimReal)<=0)
			{
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader(new SimpleDateFormat("MM/yyyy").format(c.getTime()));
				
				c.add(Calendar.MONTH, 1);
			}
			
			c.setTime(dataInicioReal);
			table.add("");
			
			Table table2 = null;
			
			while(c.getTime().compareTo(dataFimReal)<=0)
			{
				table2 = new Table(3);
				table2.setWidth("100%");
				table2.addStyle(Table.STYLE_ALTERNATE);
				
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.setNextWidth("33%");
				if(secao.equals(""))
					table2.addHeader("Sección");
				else if(modalidade.equals(""))
					table2.addHeader("Modalidad");
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.setNextWidth("33%");
				table2.addHeader("PF");
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.setNextWidth("33%");
				table2.addHeader("PJ");
				
				table.add(table2);
				
				c.add(Calendar.MONTH, 1);
			}
			
			Map<String, Integer> totais = new TreeMap<String, Integer>();
			Aseguradora aseguradora;
			int ultimoDiaMes;
			Date dataFim2;
			String keyPF,keyPJ,mesAno, tpPessoa, secaoOuPlano, plano;
			int qtde;
			Collection<String> dados;
			String[] linhaSuja;
			Map<String,Integer> pfMap;
			Map<String,Integer> pjMap;
			Map<String,String> planos;
			int qtdePf, qtdePj;
			
			for(Iterator<Aseguradora> i = this.aseguradoras.iterator() ; i.hasNext() ; )
			{
				aseguradora = i.next();
				pfMap = new TreeMap<String, Integer>();
				pjMap = new TreeMap<String, Integer>();
				planos = new TreeMap<String, String>();
				
				table.add(aseguradora.obterNome());
				
				c.setTime(dataInicioReal);
				
				while(c.getTime().compareTo(dataFimReal)<=0)
				{
					table2 = new Table(3);
					table2.setWidth("100%");
					
					ultimoDiaMes = c.getActualMaximum(Calendar.DAY_OF_MONTH);
					mesAno = new SimpleDateFormat("MM/yyyy").format(c.getTime());
					dataFim2 = new SimpleDateFormat("dd/MM/yyyy").parse(ultimoDiaMes + "/"+mesAno);
					
					dados = aseguradora.obterQtdePorTipoPessoa(c.getTime(), dataFim2, ramo, secao, modalidade);
					keyPF = "pf"+mesAno;
					keyPJ = "pj"+mesAno;
					
					for(Iterator<String> j = dados.iterator() ; j.hasNext() ; )
					{
						linhaSuja = j.next().split(";");
						
						tpPessoa = linhaSuja[0];
						secaoOuPlano = linhaSuja[1];
						qtde = Integer.valueOf(linhaSuja[2]);
						
						if(tpPessoa.equals("Persona Fisica"))
							pfMap.put(secaoOuPlano, qtde);
						else
							pjMap.put(secaoOuPlano, qtde);
						
						planos.put(secaoOuPlano, secaoOuPlano);
					}
					
					for(Iterator<String> j = planos.values().iterator() ; j.hasNext() ; )
					{
						plano = j.next();
						
						qtdePf = 0;
						qtdePj = 0;
						
						if(pfMap.containsKey(plano))
							qtdePf = pfMap.get(plano);
						if(pjMap.containsKey(plano))
							qtdePj = pjMap.get(plano);
						
						table2.setNextWidth("33%");
						table2.add(plano);
						table2.setNextHAlign(Table.HALIGN_CENTER);
						table2.setNextWidth("33%");
						table2.add(new Label(qtdePf));
						table2.setNextHAlign(Table.HALIGN_CENTER);
						table2.setNextWidth("33%");
						table2.add(new Label(qtdePj));
						
						if(totais.containsKey(keyPF))
						{
							qtde = totais.get(keyPF);
							qtde+=qtdePf;
							
							totais.put(keyPF, qtde);
						}
						else
							totais.put(keyPF, qtdePf);
						
						if(totais.containsKey(keyPJ))
						{
							qtde = totais.get(keyPJ);
							qtde+=qtdePj;
							
							totais.put(keyPJ, qtde);
						}
						else
							totais.put(keyPJ, qtdePj);
					}
					
					table.setNextHAlign(Table.HALIGN_CENTER);
					table.add(table2);
					
					c.add(Calendar.MONTH, 1);
				}
			}
			
			c.setTime(dataInicioReal);
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("TOTAL");
			int totalPF = 0; 
			int totalPJ = 0;
			
			while(c.getTime().compareTo(dataFimReal)<=0)
			{
				mesAno = new SimpleDateFormat("MM/yyyy").format(c.getTime());
				keyPF = "pf"+mesAno;
				keyPJ = "pj"+mesAno;
				
				table2 = new Table(3);
				table2.setWidth("100%");
				table2.addStyle(Table.STYLE_ALTERNATE);
				
				totalPF = 0; 
				totalPJ = 0;
				
				if(totais.containsKey(keyPF))
					totalPF = totais.get(keyPF);
				if(totais.containsKey(keyPJ))
					totalPJ = totais.get(keyPJ);
				
				table2.setNextWidth("33%");
				table2.add("");
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.setNextWidth("33%");
				table2.addHeader(new Label(totalPF));
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.setNextWidth("33%");
				table2.addHeader(new Label(totalPJ));
				
				table.add(table2);
				
				c.add(Calendar.MONTH, 1);
			}
			
			mainTable.add(table);
		}
		
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
		return new Label("Pólizas por Tipo Persona");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}