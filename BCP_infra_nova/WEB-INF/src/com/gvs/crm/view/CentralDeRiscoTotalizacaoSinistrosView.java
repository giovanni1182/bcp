package com.gvs.crm.view;

import java.util.Date;

import com.gvs.crm.component.AseguradorasSelect2;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.EntidadeHome;

import infra.control.Action;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class CentralDeRiscoTotalizacaoSinistrosView extends Table 
{
	public CentralDeRiscoTotalizacaoSinistrosView(Aseguradora aseguradora, Date dataInicio, Date dataFim, EntidadeHome home) throws Exception
	{
		super(1);
		this.setWidth("100%");
		
		Table table = new Table(2);
		
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect2("aseguradoraId",aseguradora, false, true));
		table.addHeader("Inicio de Vigencia:");
		
		Label label = new Label("hasta");
		label.setBold(true);
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputDate("dataInicio", dataInicio));
		block.add(new Space(2));
		block.add(label);
		block.add(new Space(2));
		block.add(new InputDate("dataFim", dataFim));
		table.add(block);
		
		Button pesquisarButton = new Button("Generar Pantalla", new Action("visualizarCentralRiscoSinistro"));
		pesquisarButton.getAction().add("naoValidar", true);
		Button excelButton = new Button("Generar Excel", new Action("excelCentralRiscoSinistro"));
		excelButton.getAction().add("naoValidar", true);
		
		table.addFooter(pesquisarButton);
		table.addFooter(excelButton);
		
		this.add(table);
		
		if(aseguradora!=null && dataInicio!=null && dataFim!=null)
		{
			Table table2 = new Table(4);
			table2.setWidth("50%");
			table2.addSubtitle("Vigentes");
			
			table2.addStyle(Table.STYLE_ALTERNATE);
			table2.addHeader("Sección");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Cantidad");
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.addHeader("Valor Total (Gs)");
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.addHeader("Valor Pago Aseguradora (Gs)");
			
			int qtdeVigente = 0;
			double totalSinistroPagosVigente = 0;
			double totalSinistroRecuperadosVigente = 0;
			String[] linha;
			long secaoId;
			int qtde2;
			double totalSinistroPagos2,totalSinistroRecuperados2;
			ClassificacaoContas cContas;
			Link link;
			
			for(String linhaSuja : aseguradora.obterSecoesSinistrosVigente(dataInicio, dataFim))
			{
				linha = linhaSuja.split(";");
				
				secaoId = Long.parseLong(linha[0]); 
				qtde2 = Integer.parseInt(linha[1]);
				totalSinistroPagos2 = Double.parseDouble(linha[2]);
				
				cContas = (ClassificacaoContas) home.obterEntidadePorId(secaoId);				
				
				link = new Link(cContas.obterNome(),new Action("visualizarApolicesPorSecao"));
				link.getAction().add("aseguradoraId2",aseguradora.obterId());
				link.getAction().add("dataInicio2",dataInicio);
				link.getAction().add("dataFim2",dataFim);
				link.getAction().add("secaoId",cContas.obterId());
				link.getAction().add("situacao","Vigente");
				link.getAction().add("situacao2","Vigente");
				
				table2.add(link);
				
				totalSinistroRecuperados2 = cContas.obterValorSinistrosRecuperadosPorSecaoVigente(aseguradora,dataInicio, dataFim);
				
				qtdeVigente+=qtde2;
				totalSinistroPagosVigente+=totalSinistroPagos2;
				totalSinistroRecuperadosVigente+=totalSinistroRecuperados2;
				
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.add(new Label(qtde2,"#,##0.00"));
				table2.setNextHAlign(Table.HALIGN_RIGHT);
				table2.add(new Label(totalSinistroPagos2,"#,##0.00"));
				table2.setNextHAlign(Table.HALIGN_RIGHT);
				table2.add(new Label(totalSinistroRecuperados2,"#,##0.00"));
			}
			
			table2.addHeader("Total");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader(new Label(qtdeVigente,"#,##0.00"));
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.addHeader(new Label(totalSinistroPagosVigente,"#,##0.00"));
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.addHeader(new Label(totalSinistroRecuperadosVigente,"#,##0.00"));
			
			table2.addSubtitle("No Vigentes");
			
			int qtdeNoVigente = 0;
			double totalSinistroPagosNoVigente = 0;
			double totalSinistroRecuperadosNoVigente = 0;
			
			for(String linhaSuja : aseguradora.obterSecoesSinistrosNoVigente(dataInicio, dataFim))
			{
				linha = linhaSuja.split(";");
				
				secaoId = Long.parseLong(linha[0]); 
				qtde2 = Integer.parseInt(linha[1]);
				totalSinistroPagos2 = Double.parseDouble(linha[2]);
				
				cContas = (ClassificacaoContas) home.obterEntidadePorId(secaoId);	
				
				link = new Link(cContas.obterNome(),new Action("visualizarApolicesPorSecao"));
				link.getAction().add("aseguradoraId2",aseguradora.obterId());
				link.getAction().add("dataInicio2",dataInicio);
				link.getAction().add("dataFim2",dataFim);
				link.getAction().add("secaoId",cContas.obterId());
				link.getAction().add("situacao","No Vigente");
				link.getAction().add("situacao2","No Vigente");
				
				table2.add(link);
				
				totalSinistroRecuperados2 = cContas.obterValorSinistrosRecuperadosPorSecaoNoVigente(aseguradora,dataInicio, dataFim);
				
				qtdeNoVigente+=qtde2;
				totalSinistroPagosNoVigente+=totalSinistroPagos2;
				totalSinistroRecuperadosNoVigente+=totalSinistroRecuperados2;
				
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.add(new Label(qtde2,"#,##0.00"));
				table2.setNextHAlign(Table.HALIGN_RIGHT);
				table2.add(new Label(totalSinistroPagos2,"#,##0.00"));
				table2.setNextHAlign(Table.HALIGN_RIGHT);
				table2.add(new Label(totalSinistroRecuperados2,"#,##0.00"));
			}
			
			table2.addHeader("Total");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader(new Label(qtdeNoVigente,"#,##0.00"));
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.addHeader(new Label(totalSinistroPagosNoVigente,"#,##0.00"));
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.addHeader(new Label(totalSinistroRecuperadosNoVigente,"#,##0.00"));
			
			table2.addSubtitle("Vigentes Judicializados");
			
			int qtdeVigenteJudicializado = 0;
			double totalSinistroPagosVigenteJudicializado = 0;
			double totalSinistroRecuperadosVigenteJudicializado = 0;
			
			for(String linhaSuja : aseguradora.obterSecoesSinistrosVigenteJudicializado(dataInicio, dataFim))
			{
				linha = linhaSuja.split(";");
				
				secaoId = Long.parseLong(linha[0]); 
				qtde2 = Integer.parseInt(linha[1]);
				totalSinistroPagos2 = Double.parseDouble(linha[2]);
				
				cContas = (ClassificacaoContas) home.obterEntidadePorId(secaoId);				
				
				link = new Link(cContas.obterNome(),new Action("visualizarApolicesPorSecao"));
				link.getAction().add("aseguradoraId2",aseguradora.obterId());
				link.getAction().add("dataInicio2",dataInicio);
				link.getAction().add("dataFim2",dataFim);
				link.getAction().add("secaoId",cContas.obterId());
				link.getAction().add("situacao","Vigente Judicializado");
				link.getAction().add("situacao2","Vigente");
				
				table2.add(link);
				
				totalSinistroRecuperados2 = cContas.obterValorSinistrosRecuperadosPorSecaoVigenteJudicializado(aseguradora,dataInicio, dataFim);
				
				qtdeVigenteJudicializado+=qtde2;
				totalSinistroPagosVigenteJudicializado+=totalSinistroPagos2;
				totalSinistroRecuperadosVigenteJudicializado+=totalSinistroRecuperados2;
				
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.add(new Label(qtde2,"#,##0.00"));
				table2.setNextHAlign(Table.HALIGN_RIGHT);
				table2.add(new Label(totalSinistroPagos2,"#,##0.00"));
				table2.setNextHAlign(Table.HALIGN_RIGHT);
				table2.add(new Label(totalSinistroRecuperados2,"#,##0.00"));
			}
			
			table2.addHeader("Total");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader(new Label(qtdeVigenteJudicializado,"#,##0.00"));
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.addHeader(new Label(totalSinistroPagosVigenteJudicializado,"#,##0.00"));
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.addHeader(new Label(totalSinistroRecuperadosVigenteJudicializado,"#,##0.00"));
			
			table2.addSubtitle("No Vigentes Judicializados");
			
			int qtdeNoVigenteJudicializado = 0;
			double totalSinistroPagosNoVigenteJudicializado = 0;
			double totalSinistroRecuperadosNoVigenteJudicializado = 0;
			
			for(String linhaSuja : aseguradora.obterSecoesSinistrosNoVigenteJudicializado(dataInicio, dataFim))
			{
				linha = linhaSuja.split(";");
				
				secaoId = Long.parseLong(linha[0]); 
				qtde2 = Integer.parseInt(linha[1]);
				totalSinistroPagos2 = Double.parseDouble(linha[2]);
				
				cContas = (ClassificacaoContas) home.obterEntidadePorId(secaoId);				
				
				link = new Link(cContas.obterNome(),new Action("visualizarApolicesPorSecao"));
				link.getAction().add("aseguradoraId2",aseguradora.obterId());
				link.getAction().add("dataInicio2",dataInicio);
				link.getAction().add("dataFim2",dataFim);
				link.getAction().add("secaoId",cContas.obterId());
				link.getAction().add("situacao","No Vigente Judicializado");
				link.getAction().add("situacao2","No Vigente");
				
				table2.add(link);
				
				totalSinistroRecuperados2 = cContas.obterValorSinistrosRecuperadosPorSecaoNoVigenteJudicializado(aseguradora,dataInicio, dataFim);
				
				qtdeNoVigenteJudicializado+=qtde2;
				totalSinistroPagosNoVigenteJudicializado+=totalSinistroPagos2;
				totalSinistroRecuperadosNoVigenteJudicializado+=totalSinistroRecuperados2;
				
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.add(new Label(qtde2,"#,##0.00"));
				table2.setNextHAlign(Table.HALIGN_RIGHT);
				table2.add(new Label(totalSinistroPagos2,"#,##0.00"));
				table2.setNextHAlign(Table.HALIGN_RIGHT);
				table2.add(new Label(totalSinistroRecuperados2,"#,##0.00"));
			}
			
			table2.addHeader("Total");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader(new Label(qtdeNoVigenteJudicializado,"#,##0.00"));
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.addHeader(new Label(totalSinistroPagosNoVigenteJudicializado,"#,##0.00"));
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.addHeader(new Label(totalSinistroRecuperadosNoVigenteJudicializado,"#,##0.00"));
			
			table2.addHeader("TOTAL GENERAL");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader(new Label(qtdeVigente + qtdeNoVigente + qtdeVigenteJudicializado + qtdeNoVigenteJudicializado,"#,##0.00"));
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.addHeader(new Label(totalSinistroPagosVigente + totalSinistroPagosNoVigente + totalSinistroPagosVigenteJudicializado + totalSinistroPagosNoVigenteJudicializado,"#,##0.00"));
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.addHeader(new Label(totalSinistroRecuperadosVigente + totalSinistroRecuperadosNoVigente + totalSinistroRecuperadosVigenteJudicializado + totalSinistroRecuperadosNoVigenteJudicializado,"#,##0.00"));
			
			//this.setNextColSpan(this.getColumns());
			this.add(table2);
		}
	}
}