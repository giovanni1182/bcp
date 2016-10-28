package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;

public class ProducaoAseguradorasView extends Table
{
	public ProducaoAseguradorasView(AuxiliarSeguro agente, Date dataInicio, Date dataFim) throws Exception
	{
		super(1);
		this.setWidth("100%");
		this.addSubtitle("Produción Aseguradoras");
		
		Table table = new Table(2);
		table.addHeader("Periodo:");
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputDate("_dataInicio", dataInicio));
		block.add(new Space(2));
		block.add(new Label("hasta"));
		block.add(new Space(2));
		block.add(new InputDate("_dataFim", dataFim));
		table.add(block);
		
		Button consultarButton = new Button("Consultar", new Action("visualizarDetalhesEntidade"));
		consultarButton.getAction().add("_pastaAseguradora", 13);
		consultarButton.getAction().add("id", agente.obterId());
		table.addFooter(consultarButton);
		
		Button excelButton = new Button("Generar Excel", new Action("gerarExcelProducaoAseguradoras"));
		excelButton.getAction().add("_pastaAseguradora", 13);
		excelButton.getAction().add("id", agente.obterId());
		table.addFooter(excelButton);
		
		this.add(table);
		
		if(dataInicio!=null && dataFim!=null)
		{
			String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
			dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
			
			table = new Table(1);
			table.addStyle(STYLE_ALTERNATE);
			
			Collection agentes = agente.obterAseguradoras2(dataInicio, dataFim);
			
			table.addSubtitle(agentes.size() + " Aseguradora(s)");
			
			for(Iterator i = agentes.iterator() ; i.hasNext() ; )
			{
				Entidade aseguradora = (Entidade) i.next();
				
				table.add(aseguradora.obterNome());
			}
			
			this.add(table);
		}
	}
}
