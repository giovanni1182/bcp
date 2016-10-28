package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;

public class ProducaoAgenteView extends Table
{
	public ProducaoAgenteView(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception
	{
		super(1);
		this.setWidth("100%");
		this.addSubtitle("Produción Agentes");
		
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
		consultarButton.getAction().add("id", aseguradora.obterId());
		table.addFooter(consultarButton);
		
		Button excelButton = new Button("Generar Excel", new Action("gerarExcelProducaoAgentes"));
		excelButton.getAction().add("_pastaAseguradora", 13);
		excelButton.getAction().add("id", aseguradora.obterId());
		//excelButton.setEnabled(dataInicio!=null && dataFim!=null);
		table.addFooter(excelButton);
		
		this.add(table);
		
		if(dataInicio!=null && dataFim!=null)
		{
			String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
			dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
			
			table = new Table(1);
			table.addStyle(STYLE_ALTERNATE);
			
			Collection agentes = aseguradora.obterAgentesPorPeridodo(dataInicio, dataFim);
			
			table.addSubtitle(agentes.size() + " Agente(s)");
			
			for(Iterator i = agentes.iterator() ; i.hasNext() ; )
			{
				Entidade agente = (Entidade) i.next();
				if(agente == null)
					table.add("Agente 000");
				else
					table.add(agente.obterNome());
			}
			
			this.add(table);
		}
	}
}
