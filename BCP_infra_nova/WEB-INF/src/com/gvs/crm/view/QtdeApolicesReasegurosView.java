package com.gvs.crm.view;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.AseguradorasSelect2;
import com.gvs.crm.component.SituacaoApoliceSelect2;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Image;
import infra.view.InputDate;
import infra.view.InputDouble;
import infra.view.InputInteger;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class QtdeApolicesReasegurosView extends PortalView
{
	private Aseguradora aseguradora;
	private Date dataInicio;
	private Date dataFim;
	private int qtde;
	private String situacao;
	private double valor,valorMenor,valorDolar,valorMenorDolar;
	private Collection<String> apolices;
	
	public QtdeApolicesReasegurosView(Aseguradora aseguradora,Date dataInicio,Date dataFim,int qtde, String situacao, double valor, double valorMenor, Collection<String> apolices, double valorDolar, double valorMenorDolar) throws Exception
	{
		this.aseguradora = aseguradora;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.qtde = qtde;
		this.situacao = situacao;
		this.valor = valor;
		this.valorMenor = valorMenor;
		this.apolices = apolices;
		this.valorDolar = valorDolar;
		this.valorMenorDolar = valorMenorDolar;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		DecimalFormat format = new DecimalFormat("#,##0.00");
		
		Table table = new Table(2);
		
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect2("aseguradora",aseguradora, false, true));
		
		Block block = new Block(Block.HORIZONTAL);
		
		table.addHeader("Pólizas desde:");
		block.add(new InputDate("dataInicio",dataInicio));
		block.add(new Space(2));
		Label label = new Label("hasta");
		label.setBold(true);
		block.add(label);
		block.add(new Space(2));
		block.add(new InputDate("dataFim",dataFim));
		table.add(block);
		
		table.addHeader("Situacion:");
		table.add(new SituacaoApoliceSelect2("situacao",situacao,true));
		
		table.addHeader("Capital de las pólizas más que:");
		block = new Block(Block.HORIZONTAL);
		block.add(new InputDouble("valor",this.valor,15));
		block.add(new Space());
		block.add(new Label("Gs"));
		block.add(new Space(2));
		block.add(new InputDouble("valorDolar",this.valorDolar,15));
		block.add(new Space());
		block.add(new Label("Dólar USA"));
		table.add(block);
		
		table.addHeader("Capital de las pólizas menos que:");
		block = new Block(Block.HORIZONTAL);
		block.add(new InputDouble("valorMenor",this.valorMenor,15));
		block.add(new Space());
		block.add(new Label("Gs"));
		block.add(new Space(2));
		block.add(new InputDouble("valorMenorDolar",this.valorMenorDolar,15));
		block.add(new Space());
		block.add(new Label("Dólar USA"));
		table.add(block);
		
		table.addHeader("Cantidad Solicitada:");
		table.add(new InputInteger("qtde",qtde,7));
		
		Button button = new Button("Visualizar en la pantalla",new Action("qtdeApoliceReaseguros"));
		
		Button button2 = new Button("Generar Excel",new Action("qtdeApoliceReaseguros"));
		button2.getAction().add("excel", true);
		
		table.addFooter(button);
		table.addFooter(button2);
		
		mainTable.add(table);
		
		if(apolices.size() > 0)
		{
			table = new Table(5);
			table.addSubtitle("");
			table.addStyle(Table.STYLE_ALTERNATE);
			table.setWidth("40%");
			
			table.add("");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Cantidade");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Prima de Pólizas");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Capital Dólar USA de Pólizas");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Pólizas");
			
			int cont = 0;
			
			for(Iterator<String> i = this.apolices.iterator() ; i.hasNext() ; )
			{
				String linha = i.next();
				
				String[] linhaSuja = linha.split(";");
				
				String titulo = linhaSuja[0];
				int qtde2 = Integer.valueOf(linhaSuja[1]);
				double primaApolice = Double.valueOf(linhaSuja[2]);
				double capitalDolarApolice = Double.valueOf(linhaSuja[3]);
				
				Link link = new Link(titulo, new Action("qtdeApoliceReaseguros"));
				if(cont == 0)
					link.getAction().add("com", "sim");
				else
					link.getAction().add("com", "não");
				if(aseguradora!=null)
					link.getAction().add("aseguradora", aseguradora.obterId());
				link.getAction().add("dataInicio", dataInicio);
				link.getAction().add("dataFim", dataFim);
				link.getAction().add("situacao", situacao);
				link.getAction().add("qtde", this.qtde);
				if(cont!=1)
				{
					link.getAction().add("valor", format.format(valor));
					link.getAction().add("valorMenor", format.format(valorMenor));
					link.getAction().add("valorDolar", format.format(valorDolar));
					link.getAction().add("valorMenorDolar", format.format(valorMenorDolar));
				}
				link.setNovaJanela(true);
				table.add(link);
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(new Label(qtde2));
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(format.format(primaApolice));
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(format.format(capitalDolarApolice));
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				link = new Link(new Image("pdf.jpg"), new Action("qtdeApoliceReaseguros"));
				link.setNote("Generar PDF");
				if(cont == 0)
					link.getAction().add("com", "sim");
				else
					link.getAction().add("com", "não");
				if(aseguradora!=null)
					link.getAction().add("aseguradora", aseguradora.obterId());
				link.getAction().add("dataInicio", dataInicio);
				link.getAction().add("dataFim", dataFim);
				link.getAction().add("situacao", situacao);
				link.getAction().add("qtde", this.qtde);
				link.getAction().add("pdf", true);
				if(cont!=1)
				{
					link.getAction().add("valor", format.format(valor));
					link.getAction().add("valorMenor", format.format(valorMenor));
					link.getAction().add("valorDolar", format.format(valorDolar));
					link.getAction().add("valorMenorDolar", format.format(valorMenorDolar));
				}
				link.setNovaJanela(true);
				table.add(link);
				
				cont++;
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
		return new Label("Cantidad de Pólizas/Reaseguros");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}