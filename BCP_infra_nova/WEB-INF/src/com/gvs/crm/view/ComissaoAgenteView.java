package com.gvs.crm.view;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.SituacaoApoliceSelect2;
import com.gvs.crm.component.UtilizarValorSelect2;
import com.gvs.crm.model.AuxiliarSeguro;
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

public class ComissaoAgenteView extends PortalView
{
	private AuxiliarSeguro agente;
	private String tipoValor;
	private Date dataInicio;
	private Date dataFim;
	private String situacao;
	private Collection<String> dados;
	private boolean auxiliar;
	private DecimalFormat format = new DecimalFormat("#,##0.00");
	
	public ComissaoAgenteView(AuxiliarSeguro agente,String tipoValor,Date dataInicio,Date dataFim,String situacao, Collection<String> dados,  boolean auxiliar)
	{
		this.agente = agente;
		this.tipoValor = tipoValor;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.situacao = situacao;
		this.dados = dados;
		this.auxiliar = auxiliar;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		
		if(this.auxiliar)
			table.addHeader("Agente:");
		else
			table.addHeader("Corredor:");
		
		table.add(new EntidadePopup("agenteId", "agenteNome", agente, "AuxiliarSeguro", true));
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
		
		Button button = new Button("Visualizar",new Action("comissaoAgentes"));
		button.getAction().add("auxiliar", this.auxiliar);
		table.addFooter(button);
		
		Button button2 = new Button("Generar Excel",new Action("comissaoAgentes"));
		button2.getAction().add("auxiliar", this.auxiliar);
		button2.getAction().add("excel", true);
		
		table.addFooter(button2);
		
		mainTable.add(table);
		
		long ultimoAgenteId = 0;
		
		if(this.dados.size() > 0)
		{
			int tamanho = dados.size();
			
			double totalGs = 0;
			double totalMe = 0;
			int totalQtde = 0;
			
			int cont = 1;
			
			Table table2 = new Table(5);
			table2.setWidth("100%");
			table2.addStyle(Table.STYLE_ALTERNATE);
			
			for(Iterator<String> i = this.dados.iterator() ; i.hasNext() ; )
			{
				String linhaSuja = i.next();
				
				String[] linha = linhaSuja.split(";");
				
				String nome = linha[0];
				String situacao2 = linha[1];
				int qtde = Integer.valueOf(linha[2]);
				String plano = linha[3];
				double valorGs = Double.parseDouble(linha[4]);
				double valorMe = Double.parseDouble(linha[5]);
				long id = Long.parseLong(linha[6]);
				
				if(ultimoAgenteId > 0)
				{
					if(ultimoAgenteId!=id)
					{
						this.montaTotal(table2, totalGs, totalMe, totalQtde);
						totalGs = 0;
						totalMe = 0;
						totalQtde = 0;
					}
				}
				
				if(ultimoAgenteId!=id)
					this.montaCabecalho(table2, nome);
				
				Link link = new Link(situacao2, new Action("comissaoAgentesApolice"));
				link.getAction().add("agenteId2", id);
				link.getAction().add("auxiliar", this.auxiliar);
				link.getAction().add("inicio", this.dataInicio);
				link.getAction().add("fim", this.dataFim);
				link.getAction().add("situacao2", this.situacao);
				link.getAction().add("plano", plano);
				link.setNovaJanela(true);
				
				totalGs+=valorGs;
				totalMe+=valorMe;
				totalQtde+=qtde;
				
				table2.add(plano);
				table2.add(link);
				
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.add(new Label(qtde));
				
				table2.setNextHAlign(Table.HALIGN_RIGHT);
				table2.add(format.format(valorGs));
				table2.setNextHAlign(Table.HALIGN_RIGHT);
				table2.add(format.format(valorMe));
				
				if(cont == tamanho)
					this.montaTotal(table2, totalGs, totalMe, totalQtde);
				
				ultimoAgenteId = id;
				
				cont++;
			}
			
			mainTable.add(table2);
			
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
		if(this.auxiliar)
			return new Label("Comisión Agentes por Sección");
		else
			return new Label("Comisión Corredores de Seguros por Sección");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
	
	private void montaCabecalho(Table table2, String nomeAgente)
	{
		table2.addSubtitle(nomeAgente);
		
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.addHeader("Sección");
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.addHeader("Situacion");
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.addHeader("Cantidad");
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.addHeader("Valor Gs");
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.addHeader("Valor Me");
	}
	
	private void montaTotal(Table table2, double totalGs, double totalMe, int totalQtde)
	{
		table2.addHeader("TOTAL");
		table2.addHeader("");
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.addHeader(new Label(totalQtde));
		table2.setNextHAlign(Table.HALIGN_RIGHT);
		table2.addHeader(format.format(totalGs));
		table2.setNextHAlign(Table.HALIGN_RIGHT);
		table2.addHeader(format.format(totalMe));
	}
}