package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.component.SituacaoSeguroSelect;
import com.gvs.crm.model.ApoliceHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Radio;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class AnualidadeApolicesSinistrosView extends PortalView
{
	private boolean mostrarTela;
	private String situacaoSeguro;
	private String opcao;
	private Calendar calendarInicio = null;
	private Calendar calendarFim = null;
	
	private Date dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2006 00:00:00");
	private Date dataFim = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2006 23:59:59");
	
	private Date dataInicio2 = new SimpleDateFormat("dd/MM/yyyy").parse("01/07/2006 00:00:00");
	private Date dataFim2 = new SimpleDateFormat("dd/MM/yyyy").parse("30/06/2007 23:59:59");
	
	public AnualidadeApolicesSinistrosView(boolean mostrarTela, String situacaoSeguro, String opcao) throws Exception
	{
		this.situacaoSeguro = situacaoSeguro;
		this.opcao = opcao;
		this.mostrarTela = mostrarTela;
	}
	
	private void AnoMaisUm() throws Exception
	{
		if(calendarInicio == null)
		{
			this.calendarInicio = Calendar.getInstance();
			this.calendarFim = Calendar.getInstance();
			
			if(opcao.equals("ano"))
			{
				calendarInicio.setTime(dataInicio);
				calendarFim.setTime(dataFim);
			}
			else
			{
				calendarInicio.setTime(dataInicio2);
				calendarFim.setTime(dataFim2);
			}
		}
		else
		{
			String ano = new SimpleDateFormat("yyyy").format(this.calendarInicio.getTime()); 
			/*String anoAtual = new SimpleDateFormat("yyyy").format(new Date());
			String ano = new SimpleDateFormat("yyyy").format(this.calendarInicio.getTime());*/
			
			//Sempre deixar no ultimo ano
			if(ano.equals("2014"))
			{
				if(opcao.equals("ano"))
				{
					calendarInicio.setTime(dataInicio);
					calendarFim.setTime(dataFim);
				}
				else
				{
					calendarInicio.setTime(dataInicio2);
					calendarFim.setTime(dataFim2);
				}
			}
			else
			{
				calendarInicio.add(Calendar.YEAR, 1);
				calendarFim.add(Calendar.YEAR, 1);
			}
		}
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		String[] tituloAno = new String[]{"2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014"};
		String[] tituloFiscal = new String[]{"2006-2007", "2007-2008", "2008-209", "2009-2010", "2010-2011", "2011-2012", "2012-2013", "2013-2014", "2014-2015"};
		String[] titulo = new String[]{};
		
		CRMModelManager mm = new CRMModelManager(user);
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(user);
		
		boolean admin = usuarioAtual.obterId() == 1;
		
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table2 = new Table(1);
		table2.addSubtitle("Opciones");
		
		Label l = new Label("Situacion del Seguro:");
		l.setBold(true);
		
		Block block4 = new Block(Block.HORIZONTAL);
		block4.add(l);
		block4.add(new Space(2));
		block4.add(new SituacaoSeguroSelect("situacaoSeguro", this.situacaoSeguro));
		table2.setNextColSpan(table2.getColumns());
		table2.add(block4);
		
		table2.addSubtitle("");
		
		Block block2 = new Block(Block.HORIZONTAL);
		block2.add(new Radio("opcao", "ano",opcao.equals("ano")));
		block2.add(new Space(2));
		block2.add(new Label("Año de enero a diciembre"));
		table2.setNextColSpan(table2.getColumns());
		table2.add(block2);
		
		Block block3 = new Block(Block.HORIZONTAL);
		block3.add(new Radio("opcao", "fiscal", opcao.equals("fiscal")));
		block3.add(new Space(2));
		block3.add(new Label("Ejercício fiscal"));
		table2.setNextColSpan(table2.getColumns());
		table2.add(block3);
		
		Button button = new Button("Generar en la pantalla",new Action("visualizarApoliceSinistroAnual"));
		table2.addFooter(button);
		
		button = new Button("Generar Excel",new Action("visualizarApoliceSinistroAnual"));
		button.getAction().add("excel", true);
		table2.addFooter(button);
		
		mainTable.add(table2);
		
		if(this.mostrarTela)
		{
			Table table = new Table(10);
			
			ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
			
			Collection secoes = new ArrayList();
			secoes = home.obterSecoes(admin);
			
			table.addStyle(Table.STYLE_ALTERNATE);
			table.setWidth("80%");
			
			table.addSubtitle("Cantidad de Pólizas emitidas");
			
			table.addHeader("Sección");
			
			if(opcao.equals("ano"))
				titulo = tituloAno;
			else
				titulo = tituloFiscal;
			
			for(int i = 0 ; i < titulo.length ; i++)
			{
				String t = titulo[i];
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader(t);
			}
			
			int total2006 = 0;
			int total2007 = 0;
			int total2008 = 0;
			int total2009 = 0;
			int total2010 = 0;
			int total2011 = 0;
			int total2012 = 0;
			int total2013 = 0;
			int total2014 = 0;
			
			this.AnoMaisUm();
			
			Map ano2006 = new TreeMap();
			ano2006 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			this.AnoMaisUm();
			
			Map ano2007 = new TreeMap();
			ano2007 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			this.AnoMaisUm();
			
			Map ano2008 = new TreeMap();
			ano2008 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			this.AnoMaisUm();
			
			Map ano2009 = new TreeMap();
			ano2009 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			this.AnoMaisUm();
			
			Map ano2010 = new TreeMap();
			ano2010 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			this.AnoMaisUm();
			
			Map ano2011 = new TreeMap();
			ano2011 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			this.AnoMaisUm();
			
			Map ano2012 = new TreeMap();
			ano2012 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			this.AnoMaisUm();
			
			Map ano2013 = new TreeMap();
			ano2013 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			this.AnoMaisUm();
			
			Map ano2014 = new TreeMap();
			ano2014 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			for(Iterator i = secoes.iterator() ; i.hasNext() ; )
			{
				String secao = (String) i.next();
				
				table.add(secao);
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				
				if(ano2006.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2006.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2006");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "apolice");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2006+=qtde;
				}
				else
					table.add("0");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				
				if(ano2007.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2007.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2007");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "apolice");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2007+=qtde;
				}
				else
					table.add("0");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
					
				if(ano2008.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2008.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2008");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "apolice");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2008+=qtde;
				}
				else
					table.add("0");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
					
				if(ano2009.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2009.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2009");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "apolice");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2009+=qtde;
				}
				else
					table.add("0");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				
				if(ano2010.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2010.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2010");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "apolice");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2010+=qtde;
				}
				else
					table.add("0");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				
				if(ano2011.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2011.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2011");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "apolice");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2011+=qtde;
				}
				else
					table.add("0");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				
				if(ano2012.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2012.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2012");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "apolice");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2012+=qtde;
				}
				else
					table.add("0");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				
				if(ano2013.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2013.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2013");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "apolice");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2013+=qtde;
				}
				else
					table.add("0");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				
				if(ano2014.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2014.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2014");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "apolice");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2014+=qtde;
				}
				else
					table.add("0");
			}
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("TOTAL");
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2006));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2007));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2008));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2009));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2010));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2011));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2012));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2013));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2014));
			
			total2006 = 0;
			total2007 = 0;
			total2008 = 0;
			total2009 = 0;
			total2010 = 0;
			total2011 = 0;
			total2012 = 0;
			total2013 = 0;
			total2014 = 0;
			
			table.addSubtitle("Cantidad de Siniestros ocurridos");
			
			table.addHeader("Sección");
			
			if(opcao.equals("ano"))
				titulo = tituloAno;
			else
				titulo = tituloFiscal;
			
			for(int i = 0 ; i < titulo.length ; i++)
			{
				String t = titulo[i];
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader(t);
			}
			
			this.AnoMaisUm();
			
			ano2006 = new TreeMap();
			ano2006 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			this.AnoMaisUm();
			
			ano2007 = new TreeMap();
			ano2007 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			this.AnoMaisUm();
			
			ano2008 = new TreeMap();
			ano2008 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			this.AnoMaisUm();
			
			ano2009 = new TreeMap();
			ano2009 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			this.AnoMaisUm();
			
			ano2010 = new TreeMap();
			ano2010 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			this.AnoMaisUm();
			
			ano2011 = new TreeMap();
			ano2011 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			this.AnoMaisUm();
			
			ano2012 = new TreeMap();
			ano2012 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			this.AnoMaisUm();
			
			ano2013 = new TreeMap();
			ano2013 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			this.AnoMaisUm();
			
			ano2014 = new TreeMap();
			ano2014 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), this.situacaoSeguro, admin);
			
			for(Iterator i = secoes.iterator() ; i.hasNext() ; )
			{
				String secao = (String) i.next();
				
				table.add(secao);
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				
				if(ano2006.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2006.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2006");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "sinistro");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2006+=qtde;
				}
				else
					table.add("0");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				
				if(ano2007.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2007.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2007");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "sinistro");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2007+=qtde;
				}
				else
					table.add("0");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				
				if(ano2008.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2008.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2008");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "sinistro");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2008+=qtde;
				}
				else
					table.add("0");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				
				if(ano2009.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2009.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2009");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "sinistro");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2009+=qtde;
				}
				else
					table.add("0");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				
				if(ano2010.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2010.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2010");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "sinistro");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2010+=qtde;
				}
				else
					table.add("0");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				
				if(ano2011.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2011.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2011");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "sinistro");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2011+=qtde;
				}
				else
					table.add("0");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				
				if(ano2012.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2012.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2012");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "sinistro");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2012+=qtde;
				}
				else
					table.add("0");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				
				if(ano2013.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2013.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2013");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "sinistro");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2013+=qtde;
				}
				else
					table.add("0");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				
				if(ano2014.containsKey(secao))
				{
					int qtde = Integer.parseInt(ano2014.get(secao).toString());
					
					if(qtde > 0)
					{
						Link link = new Link(new Label(qtde), new Action("visualizarApoliceSinistroAnualLista"));
						link.getAction().add("ano", "2014");
						link.getAction().add("secao", secao);
						link.getAction().add("tipo", "sinistro");
						link.getAction().add("opcao", opcao);
						link.getAction().add("situacaoSeguro", situacaoSeguro);
						link.setNovaJanela(true);
						
						table.add(link);
					}
					else
						table.add(new Label(qtde));
					total2014+=qtde;
				}
				else
					table.add("0");
			}
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("TOTAL");
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2006));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2007));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2008));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2009));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2010));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2011));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2012));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2013));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(total2014));
			
			mainTable.add(table);
		}
		
		return mainTable;
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
		return new Label("Histórico Pólizas/Siniestros");
	}
}