package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.CotacaoDolar;
import com.gvs.crm.model.CotacaoDolarHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDouble;
import infra.view.InputInteger;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class CotacaoDolarView extends EventoAbstratoView
{
	public View execute(User user, Locale arg1, Properties arg2) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(user);
		CotacaoDolarHome home = (CotacaoDolarHome) mm.getHome("CotacaoDolarHome");
		
		CotacaoDolar cotacao = (CotacaoDolar) this.obterEvento();
		
		boolean novo = cotacao.obterId() == 0;
		
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		table.addHeader("Mes:");
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputInteger("mes", cotacao.obterMes(), 2));
		block.add(new Space(2));
		Label l = new Label("Año:");
		l.setBold(true);
		block.add(l);
		block.add(new Space(2));
		block.add(new InputInteger("ano", cotacao.obterAno(), 4));
		
		table.add(block);
		
		table.addHeader("Cotización:");
		table.add(new InputDouble("cotacao", cotacao.obterCotacao(), 8));
		
		if(novo)
		{
			Button incluirButton = new Button("Agregar", new Action("incluirCotacaoDolar"));
			table.addFooter(incluirButton);
		}
		else
		{
			Button incluirButton = new Button("Actualizar", new Action("atualizarCotacaoDolar"));
			incluirButton.getAction().add("id", cotacao.obterId());
			table.addFooter(incluirButton);
			
			incluirButton = new Button("Eliminar", new Action("excluirEvento"));
			incluirButton.getAction().add("id", cotacao.obterId());
			table.addFooter(incluirButton);
			
			incluirButton = new Button("Nueva Variación", new Action("novoEvento"));
			incluirButton.getAction().add("classe", "cotacaodolar");
			table.addFooter(incluirButton);
		}
		
		mainTable.add(table);
		
		Collection<CotacaoDolar> cotacoes = home.obter10Ultimas();
		if(cotacoes.size() > 0)
		{
			cotacoes.remove(cotacao);
			
			table = new Table(2);
			table.setWidth("10%");
			table.addStyle(Table.STYLE_ALTERNATE);
			table.addSubtitle("Ultimas Cotizaciones");
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Mes/Año");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Cotizacion");
			
			CotacaoDolar cotacao2;
			int mes,ano;
			double valor;
			String mesAno,mesStr;
			Link link;
			
			for(Iterator<CotacaoDolar> i = cotacoes.iterator() ; i.hasNext() ; )
			{
				cotacao2 = i.next();
				
				mes = cotacao2.obterMes();
				ano = cotacao2.obterAno();
				valor = cotacao2.obterCotacao();
				
				mesStr = new Integer(mes).toString();
				
				if(mesStr.length() == 1)
					mesStr="0"+mes;
				
				mesAno = mesStr+"/"+ano;
				
				link = new Link(mesAno, new Action("visualizarEvento"));
				link.getAction().add("id", cotacao2.obterId());
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(link);
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(new Label(valor));
			}
			
			mainTable.add(table);
			
		}
		return mainTable;
	}
}