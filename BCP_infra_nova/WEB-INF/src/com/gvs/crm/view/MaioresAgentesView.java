package com.gvs.crm.view;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.SituacaoApoliceSelect2;
import com.gvs.crm.component.UtilizarValorSelect2;
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputDouble;
import infra.view.InputInteger;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class MaioresAgentesView extends PortalView
{
	private AuxiliarSeguro agente;
	private String tipoValor;
	private Date dataInicio;
	private Date dataFim;
	private int qtde;
	private String situacao;
	private Collection<String> dados;
	private double monto;
	private boolean auxiliar;
	
	public MaioresAgentesView(AuxiliarSeguro agente,String tipoValor,Date dataInicio,Date dataFim,int qtde, String situacao, Collection<String> dados, double monto, boolean auxiliar) throws Exception
	{
		this.agente = agente;
		this.tipoValor = tipoValor;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.qtde = qtde;
		this.situacao = situacao;
		this.dados = dados;
		this.monto = monto;
		this.auxiliar = auxiliar;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		
		if(this.auxiliar)
		{
			table.addSubtitle("Mayores Agentes");
			//table.addHeader("Agente:");
		}
		else
		{
			table.addSubtitle("Mayores Corredores de Seguros");
			//table.addHeader("Corredor:");
		}
		//table.add(new EntidadePopup("agenteId", "agenteNome", agente, "AuxiliarSeguro", true));
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
		table.addHeader("Monto en Guaraníes:");
		table.add(new InputDouble("monto",this.monto,15));
		table.addHeader("Cantidad Solicitada:");
		table.add(new InputInteger("qtde",qtde,7));
		
		Button button = new Button("Visualizar",new Action("maioresAgentes"));
		button.getAction().add("mostraTela", true);
		button.getAction().add("auxiliar", this.auxiliar);
		block = new Block(Block.HORIZONTAL);
		block.add(button);
		block.add(new Space(2));
		button = new Button("Generar Excel",new Action("maioresAgentes"));
		button.getAction().add("auxiliar", this.auxiliar);
		button.getAction().add("excel", true);
		block.add(button);
		
		table.setNextColSpan(table.getColumns());
		table.add(new Space());
		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add(block);
		
		double total = 0;
		
		if(this.dados.size() > 0)
		{
			DecimalFormat format = new DecimalFormat("#,##0.00");
			
			Table table2 = new Table(3);
			table2.setWidth("100%");
			table2.addStyle(Table.STYLE_ALTERNATE);
			
			table2.setNextHAlign(Table.HALIGN_CENTER);
			if(this.auxiliar)
				table2.addHeader("Agente");
			else
				table2.addHeader("Corredor");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Situacion");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Valor Gs");
			
			for(Iterator<String> i = this.dados.iterator() ; i.hasNext() ; )
			{
				String linhaSuja = i.next();
				
				String[] linha = linhaSuja.split(";");
				
				String nome = linha[0];
				String situacao2 = linha[1];
				long id = Long.parseLong(linha[3]);
				
				Link link = new Link(situacao2, new Action("maioresAgentes"));
				link.getAction().add("listaApolices", true);
				link.getAction().add("agenteId2", id);
				link.getAction().add("auxiliar", this.auxiliar);
				
				double valor = Double.parseDouble(linha[2]);
				
				String valorStr = format.format(valor);
				
				total+=valor;
				
				table2.add(nome);
				table2.add(link);
				table2.setNextHAlign(Table.HALIGN_RIGHT);
				table2.add(valorStr);
			}
			
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("TOTAL");
			table2.addHeader("");
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.addHeader(format.format(total));
			
			table.setNextColSpan(table.getColumns());
			table.add(new Space());
			table.setNextColSpan(table.getColumns());
			table.add(table2);
			
		}
		
		return table;
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
		return null;
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}