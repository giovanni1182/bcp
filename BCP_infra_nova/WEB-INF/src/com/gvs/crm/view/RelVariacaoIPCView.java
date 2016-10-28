package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.VariacaoIPC;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputInteger;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class RelVariacaoIPCView extends PortalView
{
	private int mes, ano;
	private Collection<VariacaoIPC> variacoes;
	
	public RelVariacaoIPCView(int mes, int ano, Collection<VariacaoIPC> variacoes)
	{
		this.mes = mes;
		this.ano = ano;
		this.variacoes = variacoes;
	}
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		
		table.addHeader("Mes:");
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputInteger("mes", mes, 2));
		block.add(new Space(2));
		Label l = new Label("Año:");
		l.setBold(true);
		block.add(l);
		block.add(new Space(2));
		block.add(new InputInteger("ano", ano, 4));
		
		table.add(block);
		
		Button consultarButton = new Button("Buscar", new Action("relVariacaoIPC"));
		table.addFooter(consultarButton);
		
		mainTable.add(table);
		
		int qtde = variacoes.size();
		if(qtde > 0)
		{
			table = new Table(2);
			table.setWidth("10%");
			table.addStyle(Table.STYLE_ALTERNATE);
			table.addSubtitle(qtde + " Variaciones");
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Mes/Año");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Variación");
			
			VariacaoIPC variacao2;
			int mes,ano;
			double valor;
			String mesAno,mesStr;
			Link link;
			
			for(Iterator<VariacaoIPC> i = variacoes.iterator() ; i.hasNext() ; )
			{
				variacao2 = i.next();
				
				mes = variacao2.obterMes();
				ano = variacao2.obterAno();
				valor = variacao2.obterVariacao();
				
				mesStr = new Integer(mes).toString();
				
				if(mesStr.length() == 1)
					mesStr="0"+mes;
				
				mesAno = mesStr+"/"+ano;
				
				link = new Link(mesAno, new Action("visualizarEvento"));
				link.getAction().add("id", variacao2.obterId());
				link.setNovaJanela(true);
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(link);
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(new Label(valor));
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
		return new Label("Listado Variaciones IPC");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}

}
