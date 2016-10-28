package com.gvs.crm.view;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.component.AseguradorasSelect2;
import com.gvs.crm.component.PlanoRamoSelect;
import com.gvs.crm.component.PlanoSecao2Select;
import com.gvs.crm.component.PlanoSelect;
import com.gvs.crm.model.ApoliceHome;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ApolicesModalidadeView extends PortalView
{
	private Date dataInicio, dataFim;
	private String ramo, secao, modalidade;
	private Aseguradora aseguradora;
	private Collection<Aseguradora> aseguradoras;
	private ApoliceHome home;
	private AseguradoraHome aseguradoraHome;
	private boolean gerarTela, admin;
	private Map<String,Integer> totalMes = new TreeMap<String,Integer>();
	private Map<String,Double> primaGsMesMap = new TreeMap<String, Double>();
	private Map<String,Double> reaseguroGsMesMap = new TreeMap<String, Double>();
	private Map<String,Double> capitalGsMesMap = new TreeMap<String, Double>();
	
	public ApolicesModalidadeView(Date dataInicio, Date dataFim, String ramo, String secao, Aseguradora aseguradora, String modalidade, Collection<Aseguradora> aseguradoras, ApoliceHome home, AseguradoraHome aseguradoraHome, boolean gerarTela, boolean admin)
	{
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.ramo = ramo;
		this.secao = secao;
		this.aseguradora = aseguradora;
		this.modalidade = modalidade;
		this.aseguradoras = aseguradoras;
		this.home = home;
		this.aseguradoraHome = aseguradoraHome;
		this.gerarTela = gerarTela;
		this.admin = admin;
	}
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect2("aseguradora",aseguradora, false, true));
		
		table.addHeader("Ramo:");
		Action action = new Action("apolicesModalidade");
		action.add("view", true);
		
		table.add(new PlanoRamoSelect("ramo", ramo, action, false, true));
		
		table.addHeader("Sección:");
		table.add(new PlanoSecao2Select("secao", ramo, secao, action));
		
		table.addHeader("Modalidad:");
	    if (!this.secao.equals(""))
	    	table.add(new PlanoSelect("modalidade", secao, modalidade, null, true));
	    else
	      table.add("");
		
		table.addHeader("Fecha de emisión:");
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputDate("dataInicio", this.dataInicio));
		block.add(new Space(2));
		block.add(new Label("hasta"));
		block.add(new Space(2));
		block.add(new InputDate("dataFim", this.dataFim));
		table.add(block);
		
		Button gerarButton = new Button("Generar en la Pantalla",new Action("apolicesModalidade"));
		table.addFooter(gerarButton);
		
		gerarButton = new Button("Generar Excel",new Action("apolicesModalidade"));
		gerarButton.getAction().add("excel", true);
		table.addFooter(gerarButton);
		
		mainTable.add(table);
		
		if(gerarTela)
		{
			DecimalFormat formatavalor = new DecimalFormat("#,##0.00");
			
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(dataFim.getTime());
			
			String anoFim = new SimpleDateFormat("yyyy").format(c.getTime());
			String mesFim = new SimpleDateFormat("MM").format(c.getTime());		
			
			Date dataFim2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(c.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + mesFim + "/" + anoFim + " 23:59:59");
			
			c.setTimeInMillis(dataInicio.getTime());
			String anoInicio = new SimpleDateFormat("yyyy").format(c.getTime());
			String mesInicio = new SimpleDateFormat("MM").format(c.getTime());
			
			Date dataInicio2 = new SimpleDateFormat("dd/MM/yyyy").parse(c.getActualMinimum(Calendar.DAY_OF_MONTH) + "/" + mesInicio + "/" + anoInicio);
			
			c.setTime(dataInicio2);
			
			int tamanhoTabela = 0;
			
			while(c.getTime().compareTo(dataFim2)<=0)
			{
				tamanhoTabela++;
				c.add(Calendar.MONTH, 1);
			}
			
			table = new Table(tamanhoTabela+2);
			table.setWidth("100%");
			table.addStyle(Table.STYLE_ALTERNATE);
			table.addSubtitle("");
			
			Map<String,String> planosaAeguradoraTotal = new TreeMap<String, String>();
			
			for(Iterator<Aseguradora> i = this.aseguradoras.iterator() ; i.hasNext() ; )
			{
				Aseguradora asegutadora = i.next();
				
				this.totalMes.clear();
				this.capitalGsMesMap.clear();
				this.primaGsMesMap.clear();
				this.reaseguroGsMesMap.clear();
				
				Map<String,String> planosaAeguradora = new TreeMap<String,String>();
				if(!secao.equals("") && !modalidade.equals(""))
				{
		       		planosaAeguradora.put(secao+";"+modalidade,secao+";"+modalidade);
		       		planosaAeguradoraTotal.put(secao+";"+modalidade,secao+";"+modalidade);
				}
				else
				{
					planosaAeguradora = asegutadora.obterNomePlanosPeriodo(dataInicio2, dataFim2, secao, admin,ramo);
		        	planosaAeguradora.remove("ZZSección no definida;-");
		        	planosaAeguradoraTotal.putAll(planosaAeguradora);
				}
				
				table.setNextColSpan(table.getColumns());
				table.addHeader(asegutadora.obterNome());
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("SECCIÓN");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("MODALIDAD");
				
				c.setTime(dataInicio2);
				
				while(c.getTime().compareTo(dataFim2)<=0)
				{
					table.setNextHAlign(Table.HALIGN_CENTER);
					table.addHeader(new Label(c.getTime(), "MM/yyyy"));
					c.add(Calendar.MONTH, 1);
				}
				
				table.add("");
				table.add("");
				c.setTime(dataInicio2);
				
				while(c.getTime().compareTo(dataFim2)<=0)
				{
					Table table2 = new Table(4);
					table2.setWidth("100%");;
					
					table2.setNextWidth("25%");
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.addHeader("Cuantidad");
					table2.setNextWidth("25%");
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.addHeader("Capital Gs");
					table2.setNextWidth("25%");
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.addHeader("Prima Gs");
					table2.setNextWidth("25%");
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.addHeader("Reaseguro Gs");
					
					table.add(table2);
					c.add(Calendar.MONTH, 1);
				}
				
				for(Iterator<String> j = planosaAeguradora.values().iterator() ; j.hasNext() ; )
				{
					String[] secaoSuja = j.next().split(";");
		        	
		        	String secao = secaoSuja[0];
		        	String modalidade = secaoSuja[1];
		        	
		        	if(modalidade.equals("-"))
		        		modalidade = "";
		        	
					table.add(secao);
					table.add(modalidade);
		        	
		        	c.setTime(dataInicio2);
					
					while(c.getTime().compareTo(dataFim2)<=0)
					{
						int qtde = 0;
		        		double capitalGs = 0;
		        		double primaGs = 0;
		        		double reaseguroGs = 0;
		        		
		        		String[] dadosSujos = new String[1];
		        		String dados;
		        		String[] dados2 = new String[1];
		        		String ano2 = new SimpleDateFormat("yyyy").format(c.getTime());
		        		String mes2 = new SimpleDateFormat("MM").format(c.getTime());
		        		
		        		Calendar c2 = Calendar.getInstance();
		        		c2.setTime(c.getTime());
		        		Date dataFimMes = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(c2.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + mes2 + "/" + ano2 + " 23:59:59");
		        		
		        		dadosSujos = asegutadora.obterQtdeApolicesPorModalidade(c.getTime(), dataFimMes, secao, modalidade);
		        		dados = dadosSujos[0];
		    			dados2 = dados.split(";");
		    	   
						qtde = Integer.valueOf(dados2[0]);
						capitalGs = Double.valueOf(dados2[1]);
						primaGs = Double.valueOf(dados2[2]);
						reaseguroGs = Double.valueOf(dados2[3]);
						
						Table table2 = new Table(4);
						table2.setWidth("100%");
						
						table2.setNextWidth("25%");
						table2.setNextHAlign(Table.HALIGN_CENTER);
						if(qtde > 0)
						{
							Link link = new Link(new Label(qtde), new Action("apolicesModalidadeLista"));
							link.getAction().add("dataInicio2", c.getTime().getTime());
							link.getAction().add("dataFim2", dataFimMes.getTime());
							link.getAction().add("aseguradoraId", asegutadora.obterId());
							link.getAction().add("secao2", secao);
							link.getAction().add("modalidade2", modalidade);
							link.setNovaJanela(true);
							
							table2.add(link);
						}
						else
							table2.add(new Label(qtde));
						table2.setNextWidth("25%");
						table2.setNextHAlign(Table.HALIGN_CENTER);
						table2.add(formatavalor.format(capitalGs));
						table2.setNextWidth("25%");
						table2.setNextHAlign(Table.HALIGN_CENTER);
						table2.add(formatavalor.format(primaGs));
						table2.setNextWidth("25%");
						table2.setNextHAlign(Table.HALIGN_CENTER);
						table2.add(formatavalor.format(reaseguroGs));
						
						table.add(table2);
						
						c.add(Calendar.MONTH, 1);
						
						this.addTotalMes(mes2 + "/" + ano2, qtde);
			    		this.addTotalMesCapitalGs(mes2 + "/" + ano2, capitalGs);
			    		this.addTotalMesPrimaGs(mes2 + "/" + ano2, primaGs);
			    		this.addTotalMesReaseguroGs(mes2 + "/" + ano2, reaseguroGs);
					}
				}
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("TOTAL");
				table.addHeader("");
				
				c.setTime(dataInicio2);
				while(c.getTime().compareTo(dataFim2)<=0)
				{
					String mesAnoGeral = new SimpleDateFormat("MM/yyyy").format(c.getTime());
					int qtde = 0;
					double capitalGs = 0;
					double primaGs = 0;
					double reaseguroGs = 0;
		    	   
					if(this.totalMes.containsKey(mesAnoGeral))
						qtde = this.totalMes.get(mesAnoGeral);
					if(this.capitalGsMesMap.containsKey(mesAnoGeral))
		    		   	capitalGs = this.capitalGsMesMap.get(mesAnoGeral);
					if(this.primaGsMesMap.containsKey(mesAnoGeral))
						primaGs = this.primaGsMesMap.get(mesAnoGeral);
					if(this.reaseguroGsMesMap.containsKey(mesAnoGeral))
						reaseguroGs = this.reaseguroGsMesMap.get(mesAnoGeral);
	        		
					Table table2 = new Table(4);
					table2.setWidth("100%");
					
					table2.setNextWidth("25%");
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.addHeader(new Label(qtde));
					table2.setNextWidth("25%");
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.addHeader(formatavalor.format(capitalGs));
					table2.setNextWidth("25%");
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.addHeader(formatavalor.format(primaGs));
					table2.setNextWidth("25%");
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.addHeader(formatavalor.format(reaseguroGs));
					
					table.add(table2);
					
					c.add(Calendar.MONTH, 1);
				}
				
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
		return new Label("Pólizas/Siniestros por Modalidad");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
	
	private void addTotalMes(String mesAno, int qtde)
	{
		if(this.totalMes.containsKey(mesAno))
		{
			int qtdeG = Integer.parseInt(this.totalMes.get(mesAno).toString());
			
			qtdeG+=qtde;
			
			this.totalMes.remove(mesAno);
			
			this.totalMes.put(mesAno, qtdeG);
		}
		else
			this.totalMes.put(mesAno, qtde);
	}
	
	private void addTotalMesCapitalGs(String mesAno, double valor)
	{
		if(this.capitalGsMesMap.containsKey(mesAno))
		{
			double valorVelho = this.capitalGsMesMap.get(mesAno);
			
			valorVelho+=valor;
			
			this.capitalGsMesMap.remove(mesAno);
			
			this.capitalGsMesMap.put(mesAno, valorVelho);
		}
		else
			this.capitalGsMesMap.put(mesAno, valor);
	}
	
	private void addTotalMesPrimaGs(String mesAno, double valor)
	{
		if(this.primaGsMesMap.containsKey(mesAno))
		{
			double valorVelho = this.primaGsMesMap.get(mesAno);
			
			valorVelho+=valor;
			
			this.primaGsMesMap.remove(mesAno);
			
			this.primaGsMesMap.put(mesAno, valorVelho);
		}
		else
			this.primaGsMesMap.put(mesAno, valor);
	}
	
	private void addTotalMesReaseguroGs(String mesAno, double valor)
	{
		if(this.reaseguroGsMesMap.containsKey(mesAno))
		{
			double valorVelho = this.reaseguroGsMesMap.get(mesAno);
			
			valorVelho+=valor;
			
			this.reaseguroGsMesMap.remove(mesAno);
			
			this.reaseguroGsMesMap.put(mesAno, valorVelho);
		}
		else
			this.reaseguroGsMesMap.put(mesAno, valor);
	}
}