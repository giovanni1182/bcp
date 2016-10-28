package com.gvs.crm.view;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.AseguradorasSelect2;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Image;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class DemandaJudicialView extends PortalView
{
	private Aseguradora aseguradora;
	private Date dataInicio;
	private Date dataFim;
	private Collection<String> aspectos;
	
	public DemandaJudicialView(Aseguradora aseguradora, Date dataInicio, Date dataFim, Collection<String> aspectos) throws Exception
	{
		this.aseguradora = aseguradora;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.aspectos = aspectos;
	}
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		
		CRMModelManager mm = new CRMModelManager(user);
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect2("aseguradora",this.aseguradora, false, true));
		table.addHeader("Inicio de Vigencia:");
		
		Block block = new Block(Block.HORIZONTAL);
		
		block.add(new InputDate("dataInicio",this.dataInicio));
		block.add(new Space(2));
		Label label = new Label("hasta");
		label.setBold(true);
		block.add(label);
		block.add(new Space(2));
		block.add(new InputDate("dataFim",this.dataFim));
		table.add(block);
		
		Button buscarButton = new Button("Buscar",new Action("demandaJudicial"));
		table.setNextColSpan(table.getColumns());
		table.add(new Space());
		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add(buscarButton);
		
		if(this.aspectos.size() > 0)
		{
			Table table2 = new Table(5);
			table2.setWidth("100%");
			table2.addStyle(Table.STYLE_ALTERNATE);
			table2.addSubtitle("");
			
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Aseguradora");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Sección");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Cantidad");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Monto demandado");
			table2.addHeader("");
			
			Entidade ultima = null;
			int totalQtde = 0;
			double totalMontante = 0;
			int totalGeralQtde = 0;
			double totalGeralMontante = 0;
			
			for(Iterator<String> i = this.aspectos.iterator() ; i.hasNext() ; )
			{
				String linhaSuja = i.next();
				
				String[] linha = linhaSuja.split(";");
				
				long origemId = Long.parseLong(linha[0]);
				long secaoId = Long.parseLong(linha[1]);
				
				Entidade origem = home.obterEntidadePorId(origemId);
				Entidade secao = home.obterEntidadePorId(secaoId);
				
				if(ultima == null)
					ultima = origem;
				else
				{
					if(origem.obterId()!=ultima.obterId())
					{
						table2.addHeader("TOTAL");
						table2.addHeader("");
						table2.setNextHAlign(Table.HALIGN_CENTER);
						table2.addHeader(new Label(totalQtde));
						table2.setNextHAlign(Table.HALIGN_RIGHT);
						table2.addHeader(new Label(totalMontante,"#,##0.00"));
						table2.addHeader("");
						
						totalQtde = 0;
						totalMontante = 0;
						
						ultima = origem;
					}
				}
				
				double valor = Double.parseDouble(linha[2]);
				int qtde = Integer.parseInt(linha[3]);
				
				totalMontante+=valor;
				totalGeralMontante+=valor;
				totalQtde+=qtde;
				totalGeralQtde+=qtde;
				
				Link link = new Link(secao.obterNome(),new Action("listaDemandaJudicial"));
				link.getAction().add("secaoId", secaoId);
				link.getAction().add("origemId", origemId);
				
				table2.add(origem.obterNome());
				table2.add(link);
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.add(new Label(qtde));
				table2.setNextHAlign(Table.HALIGN_RIGHT);
				table2.add(new Label(valor,"#,##0.00"));
				
				link = new Link(new Image("excel.gif"),new Action("listaDemandaJudicialExcel"));
				link.getAction().add("secaoId", secaoId);
				link.getAction().add("origemId", origemId);
				link.setNote("Generar Excel");
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.add(link);
			}
			
			table2.addHeader("TOTAL");
			table2.addHeader("");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader(new Label(totalQtde));
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.addHeader(new Label(totalMontante,"#,##0.00"));
			table2.addHeader("");
			
			table2.addSubtitle("");
			
			table2.addHeader("TOTAL GENERAL");
			table2.addHeader("");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader(new Label(totalGeralQtde));
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.addHeader(new Label(totalGeralMontante,"#,##0.00"));
			table2.addHeader("");
			
			table.setNextColSpan(table.getColumns());
			table.add(table2);
		}
		
		return table;
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}

	public String getSelectedGroup() throws Exception
	{
		return "Seguimiento";
	}

	public String getSelectedOption() throws Exception
	{
		return "Demanda Judicial";
	}

	public View getTitle() throws Exception
	{
		return new Label("Demanda Judicial");
	}
}