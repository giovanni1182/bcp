package com.gvs.crm.view;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.PlanoSecaoSelect;
import com.gvs.crm.component.PlanoSelect;
import com.gvs.crm.component.SituacaoApoliceSelect2;
import com.gvs.crm.component.UtilizarValorSelect2;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputDouble;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class MaioresReaseguradorasPorSecaoView extends PortalView
{
	private String tipoValor, situacao, secao, modalidade;
	private Date dataInicio,dataFim;
	private double dolar, euro, real, pesoArg, pesoUru, yen;
	private Collection<String> dados;
	
	public MaioresReaseguradorasPorSecaoView(String tipoValor,Date dataInicio,Date dataFim, String situacao, double dolar, double euro, double real, double pesoArg, double pesoUru, double yen, Collection<String> dados, String secao, String modalidade)
	{
		this.tipoValor = tipoValor;
		this.situacao = situacao;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.dolar = dolar;
		this.euro = euro;
		this.real = real;
		this.pesoArg = pesoArg;
		this.pesoUru = pesoUru;
		this.yen = yen;
		this.dados = dados;
		this.secao = secao;
		this.modalidade = modalidade;
	}
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");
		
		Table mainTable = new Table(2);
		mainTable.setWidth("35%");
		
		Table tableCol1 = new Table(1);
		
		Table table = new Table(2);
		Action action = new Action("maioresRaseguradorasPorSecao");
		action.add("view", true);
		table.addHeader("Seccion:");
		table.add(new PlanoSecaoSelect("secao", this.secao, action));
		
		table.addHeader("Modalidad:");
		if(!this.secao.equals(""))
			table.add(new PlanoSelect("modalidade", this.secao, modalidade, null, true));
		else
			table.add("");
		
		table.addHeader("Consulta por:");
		table.add(new UtilizarValorSelect2("tipoValor",tipoValor));
		table.addHeader("Pólizas Vigentes desde:");
		
		Block block = new Block(Block.HORIZONTAL);
		
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
		
		Button button = new Button("Visualizar",new Action("maioresRaseguradorasPorSecao"));
		table.addFooter(button);
		
		button = new Button("Generar Excel",new Action("maioresRaseguradorasPorSecao"));
		button.getAction().add("excel", true);
		table.addFooter(button);
		
		button = new Button("Generar PDF",new Action("maioresRaseguradorasPorSecao"));
		button.getAction().add("pdf", true);
		table.addFooter(button);
		
		tableCol1.add(table);
		
		mainTable.add(tableCol1);
		
		table = new Table(2);
		
		table.addSubtitle("Cotización del día en Guaraní");
		table.addHeader("Dólar USA:");
		table.add(new InputDouble("dolar", dolar, 10));
		table.addHeader("Euro:");
		table.add(new InputDouble("euro", euro, 10));
		table.addHeader("Real:");
		table.add(new InputDouble("real", real, 10));
		table.addHeader("Peso Arg.:");
		table.add(new InputDouble("pesoArg", pesoArg, 10));
		table.addHeader("Peso Uru.:");
		table.add(new InputDouble("pesoUru", pesoUru, 10));
		table.addHeader("Yen:");
		table.add(new InputDouble("yen", yen, 10));
		
		mainTable.add(table);
		
		if(dados.size() > 0)
		{
			double totalPrima = 0;
			double totalCapital = 0;
			double totalComissao = 0;
			
			table = new Table(4);
			table.addSubtitle("");
			table.addStyle(Table.STYLE_ALTERNATE);
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Sección");
			
			if(tipoValor.equals("valorPrima"))
			{
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Prima");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Capital");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Comisión");
			}
			else if(tipoValor.equals("valorCapital"))
			{
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Capital");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Prima");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Comisión");
			}
			else
			{
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Comisión");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Capital");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Prima");
			}
			
			for(Iterator<String> i = dados.iterator() ; i.hasNext() ; )
			{
				String linhaSuja = i.next();
				
				String linha[] = linhaSuja.split(";");
				
				String nome = linha[0];
				double capital = Double.valueOf(linha[1]);
				double prima = Double.valueOf(linha[2]);
				double comissao = Double.valueOf(linha[3]);
				
				if(nome.equals("null"))
				{
					totalCapital=capital;
					totalPrima=prima;
					totalComissao=comissao;
				}
				else
				{
					table.add(nome);
					
					if(tipoValor.equals("valorPrima"))
					{
						table.setNextHAlign(Table.HALIGN_RIGHT);
						table.add(formataValor.format(prima));
						table.setNextHAlign(Table.HALIGN_RIGHT);
						table.add(formataValor.format(capital));
						table.setNextHAlign(Table.HALIGN_RIGHT);
						table.add(formataValor.format(comissao));
					}
					else if(tipoValor.equals("valorCapital"))
					{
						table.setNextHAlign(Table.HALIGN_RIGHT);
						table.add(formataValor.format(capital));
						table.setNextHAlign(Table.HALIGN_RIGHT);
						table.add(formataValor.format(prima));
						table.setNextHAlign(Table.HALIGN_RIGHT);
						table.add(formataValor.format(comissao));
					}
					else
					{
						table.setNextHAlign(Table.HALIGN_RIGHT);
						table.add(formataValor.format(comissao));
						table.setNextHAlign(Table.HALIGN_RIGHT);
						table.add(formataValor.format(capital));
						table.setNextHAlign(Table.HALIGN_RIGHT);
						table.add(formataValor.format(prima));
					}
				}
			}
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("TOTAL");
			
			//System.out.println(totalPrima + " " + totalCapital + " " + totalComissao);
			
			if(tipoValor.equals("valorPrima"))
			{
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.addHeader(formataValor.format(totalPrima));
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.addHeader(formataValor.format(totalCapital));
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.addHeader(formataValor.format(totalComissao));
			}
			else if(tipoValor.equals("valorCapital"))
			{
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.addHeader(formataValor.format(totalCapital));
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.addHeader(formataValor.format(totalPrima));
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.addHeader(formataValor.format(totalComissao));
			}
			else
			{
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.addHeader(formataValor.format(totalComissao));
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.addHeader(formataValor.format(totalCapital));
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.addHeader(formataValor.format(totalPrima));
			}
			
			mainTable.setNextColSpan(mainTable.getColumns());
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
		return new Label("Mayores Reaseguradoras por Sección");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}