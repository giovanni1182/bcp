package com.gvs.crm.view;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.ApoliceSinistroSelect;
import com.gvs.crm.component.AseguradorasSelect2;
import com.gvs.crm.component.SituacaoSeguroSelect;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Radio;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ConsolidadoApolicesSinistrosView extends PortalView
{

	private String opcao;
	private Date dataInicio;
	private Date dataFim;
	private boolean mostrarTela;
	private boolean gerarArquivo;
	private Aseguradora aseguradora;
	private Collection informacoes;
	private String situacaoSeguro;
	
	public ConsolidadoApolicesSinistrosView(Aseguradora aseguradora, String opcao, String situacaoSeguro, Date dataInicio, Date dataFim,boolean mostrarTela, boolean gerarArquivo,Collection informacoes) throws Exception
	{
		this.aseguradora = aseguradora;
		this.opcao = opcao;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.mostrarTela = mostrarTela;
		this.gerarArquivo = gerarArquivo;
		this.informacoes = informacoes;
		this.situacaoSeguro = situacaoSeguro;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2); 
		
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect2("aseguradoraId", this.aseguradora, false, true));
		
		table.addHeader("Consolidado por:");
		table.add(new ApoliceSinistroSelect("opcao", this.opcao));
		table.addHeader("Situacion del Seguro:");
		table.add(new SituacaoSeguroSelect("situacaoSeguro", this.situacaoSeguro));
		table.addHeader("Periodo:");
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputDate("dataInicio", this.dataInicio));
		Label label = new Label("hasta");
		label.setBold(true);
		block.add(new Space(2));
		block.add(label);
		block.add(new Space(2));
		block.add(new InputDate("dataFim", this.dataFim));
		
		table.add(block);
		
		table.addSubtitle("Opciones");
		
		Block block2 = new Block(Block.HORIZONTAL);
		block2.add(new Radio("escolha", "tela",mostrarTela));
		block2.add(new Space(2));
		block2.add(new Label("Visualizar en la pantalla"));
		table.setNextColSpan(table.getColumns());
		table.add(block2);
		
		Block block3 = new Block(Block.HORIZONTAL);
		block3.add(new Radio("escolha", "excel",gerarArquivo));
		block3.add(new Space(2));
		block3.add(new Label("Generar Excel(.xls)"));
		table.setNextColSpan(table.getColumns());
		table.add(block3);
		
		Button button = new Button("Generar",new Action("visualizarConsolidado"));
		button.getAction().add("calcular", true);
		table.addFooter(button);
		
		mainTable.add(table);
		
		if(this.mostrarTela)
		{
			table = new Table(8);
			table.setWidth("100%");
			table.addStyle(Table.STYLE_ALTERNATE);
			
			if(this.aseguradora!=null)
				table.addSubtitle("Consolidado por " + this.aseguradora.obterNome());
			else
				table.addSubtitle("");
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Seccion");
			table.setNextHAlign(Table.HALIGN_CENTER);
		    table.addHeader("Modalidad");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Cantidad");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("%");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Capital GS");
			table.setNextHAlign(Table.HALIGN_CENTER);
			if(opcao.equals("Pólizas"))
				table.addHeader("Capital ME");
			else
				table.addHeader("Monto GS");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Prima GS");
			table.setNextHAlign(Table.HALIGN_CENTER);
			if(opcao.equals("Pólizas"))
				table.addHeader("Prima ME");
			else
				table.addHeader("Judicializado GS");
			
			double totalQtde = 0;
			double totalCapitalGS = 0;
			double totalCapitalME = 0;
			double totalPrimaGS = 0;
			double totalPrimaME = 0;
			double totalMontanteGS = 0;
			double totalJuizGS = 0;
			
			for(Iterator i = this.informacoes.iterator() ; i.hasNext() ; )
			{
				String linha = (String) i.next();
				
				String[] linhaArray = linha.split(";");
				
				String secao = linhaArray[0];
				int qtde = Integer.parseInt(linhaArray[1]);
				
				totalQtde+=qtde;
			}
			
			for(Iterator i = this.informacoes.iterator() ; i.hasNext() ; )
			{
				String linha = (String) i.next();
				
				String[] linhaArray = linha.split(";");
				
				String secao = linhaArray[0];
				String modalidade = linhaArray[6];
				double qtde = Integer.parseInt(linhaArray[1]);
				
				double capitalGS = 0;
				double capitalME = 0;
				double primaGS = 0;
				double primaME = 0;
				double montanteGS = 0;
				double juizGS = 0;
				
				capitalGS = Double.parseDouble(linhaArray[2]);
				
				if(opcao.equals("Pólizas"))
				{
					capitalME = Double.parseDouble(linhaArray[3]);
					primaGS = Double.parseDouble(linhaArray[4]);
					primaME = Double.parseDouble(linhaArray[5]);
				}
				else
				{
					montanteGS = Double.parseDouble(linhaArray[3]);
					primaGS = Double.parseDouble(linhaArray[4]);
					juizGS = Double.parseDouble(linhaArray[5]);
				}
				
				table.add(secao);
				table.add(modalidade);
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(new Label(qtde));
				table.setNextHAlign(Table.HALIGN_RIGHT);
				double porcentagem = (qtde * 100) / totalQtde;
				table.add(new Label(porcentagem,"0.0"));
				//totalQtde+=qtde;
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(new Label(capitalGS,"#,##0.00"));
				totalCapitalGS+=capitalGS;
				table.setNextHAlign(Table.HALIGN_RIGHT);
				if(opcao.equals("Pólizas"))
				{
					table.add(new Label(capitalME,"#,##0.00"));
					totalCapitalME+=capitalME;
				}
				else
				{
					table.add(new Label(montanteGS,"#,##0.00"));
					totalMontanteGS+=montanteGS;
				}
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(new Label(primaGS,"#,##0.00"));
				totalPrimaGS+=primaGS;
				table.setNextHAlign(Table.HALIGN_RIGHT);
				if(opcao.equals("Pólizas"))
				{
					table.add(new Label(primaME,"#,##0.00"));
					totalPrimaME+=primaME;
				}
				else
				{
					table.add(new Label(juizGS,"#,##0.00"));
					totalJuizGS+=juizGS;
				}
			}
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("TOTAL");
			
			table.add("");
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			Label label2 = new Label(totalQtde);
			label2.setBold(true);
			table.add(label2);
			
			table.add("");
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			Label label3 = new Label(totalCapitalGS,"#,##0.00");
			label3.setBold(true);
			table.add(label3);
			
			if(opcao.equals("Pólizas"))
			{
				table.setNextHAlign(Table.HALIGN_RIGHT);
				Label label4 = new Label(totalCapitalME,"#,##0.00");
				label4.setBold(true);
				table.add(label4);
			}
			else
			{
				table.setNextHAlign(Table.HALIGN_RIGHT);
				Label label4 = new Label(totalMontanteGS,"#,##0.00");
				label4.setBold(true);
				table.add(label4);
			}
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			Label label5 = new Label(totalPrimaGS,"#,##0.00");
			label5.setBold(true);
			table.add(label5);
			
			if(opcao.equals("Pólizas"))
			{
				table.setNextHAlign(Table.HALIGN_RIGHT);
				Label label6 = new Label(totalPrimaME,"#,##0.00");
				label6.setBold(true);
				table.add(label6);
			}
			else
			{
				table.setNextHAlign(Table.HALIGN_RIGHT);
				Label label6 = new Label(totalJuizGS,"#,##0.00");
				label6.setBold(true);
				table.add(label6);
			}
			
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
		return new Label("Consolidado Pólizas/Sección");
	}
}