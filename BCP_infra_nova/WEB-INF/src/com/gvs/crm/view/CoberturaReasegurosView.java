package com.gvs.crm.view;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.AseguradorasSelect2;
import com.gvs.crm.component.SituacaoApoliceSelect2;
import com.gvs.crm.component.UtilizarValorSelect2;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

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

public class CoberturaReasegurosView extends PortalView
{
	private Aseguradora aseguradora;
	private Date dataInicio;
	private Date dataFim;
	private String situacao;
	private String tipoValor;
	private Collection<String> dados;
	
	public CoberturaReasegurosView(Aseguradora aseguradora,Date dataInicio,Date dataFim, String situacao,String tipoValor,Collection<String> dados) throws Exception
	{
		this.aseguradora = aseguradora;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.situacao = situacao;
		this.tipoValor = tipoValor;
		this.dados = dados;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		DecimalFormat format = new DecimalFormat("#,##0.00");
		
		Table table = new Table(2);
		
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect2("aseguradora",aseguradora, false, true));
		table.addHeader("Consulta por:");
		table.add(new UtilizarValorSelect2("tipoValor",tipoValor));
		
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
		
		Button button = new Button("Visualizar en la pantalla",new Action("coberturaReaseguros"));
		table.addFooter(button);
		
		button = new Button("Generar Excel",new Action("coberturaReaseguros"));
		button.getAction().add("excel", true);
		table.addFooter(button);
		
		mainTable.add(table);
		
		if(dados.size() > 0)
		{
			table = new Table(4);
			table.setWidth("20%");
			table.addSubtitle("");
			table.addStyle(Table.STYLE_ALTERNATE);
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Tipo Contrato");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Cantidad");
			table.setNextHAlign(Table.HALIGN_CENTER);
			if(tipoValor.equals("valorPrima"))
				table.addHeader("Prima Reaseguro");
			else if(tipoValor.equals("valorCapital"))
				table.addHeader("Capital Reaseguro");
			else if(tipoValor.equals("valorComissao"))
				table.addHeader("Comisión Reaseguro");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Pólizas");
			
			double total = 0;
			int totalQtde = 0; 
			
			for(Iterator<String> i = this.dados.iterator() ; i.hasNext() ; )
			{
				String linhaSuja = i.next();
				
				String[] linha = linhaSuja.split(";");
				
				String tipoContrato = linha[0];
				int qtde = Integer.valueOf(linha[2]);
				totalQtde+=qtde;
				
				Action action = new Action("coberturaReaseguros");
				if(aseguradora!=null)
					action.add("aseguradora", aseguradora.obterId());
				action.add("dataInicio", dataInicio);
				action.add("dataFim", dataFim);
				action.add("situacao", situacao);
				action.add("tipo", tipoContrato);
				
				Link link = new Link(tipoContrato,action);
				link.setNovaJanela(true);
				if(aseguradora!=null)
					link.getAction().add("aseguradora", aseguradora.obterId());
				
				double valor = Double.valueOf(linha[1]);
				total+=valor;
				
				table.add(link);
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(new Label(qtde));
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(format.format(valor));
				
				action = new Action("coberturaReaseguros");
				if(aseguradora!=null)
					action.add("aseguradora", aseguradora.obterId());
				action.add("dataInicio", dataInicio);
				action.add("dataFim", dataFim);
				action.add("situacao", situacao);
				action.add("tipo", tipoContrato);
				action.add("pdf", true);
				
				Link link2 = new Link(new Image("pdf.jpg"), action);
				link2.setNovaJanela(true);
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(link2);
			}
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("TOTAL");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader(new Label(totalQtde));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(format.format(total));
			table.add("");
			
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
		return new Label("Cobertura de Reaseguros");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}
