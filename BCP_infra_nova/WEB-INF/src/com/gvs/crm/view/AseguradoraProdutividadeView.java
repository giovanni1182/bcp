package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.Border;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class AseguradoraProdutividadeView extends BasicView
{
	private Aseguradora aseguradora;
	private Collection agentes;
	private Date dataInicio;
	private Date dataFim;
	private boolean lista;
	private Collection<Apolice> apolicesSemAgentes;
	
	public AseguradoraProdutividadeView(Aseguradora aseguradora,Collection agentes, Date dataInicio, Date dataFim, boolean lista, Collection<Apolice> apolicesSemAgentes) throws Exception 
	{
		this.aseguradora = aseguradora;
		this.agentes = agentes;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.lista = lista;
		this.apolicesSemAgentes = apolicesSemAgentes;
	}

	public View execute(User arg0, Locale arg1, Properties arg2)throws Exception 
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		table.addSubtitle("Detalle de la Produción de la Aseguradora");
		
		table.addHeader("Aseguradora:");
		table.add(new EntidadePopup("aseguradoraId", "aseguradoraNome",aseguradora, "Aseguradora", true));
		table.addHeader("Periodo:");
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputDate("dataInicio", dataInicio));
		block.add(new Space(2));
		block.add(new Label("hasta"));
		block.add(new Space(2));
		block.add(new InputDate("dataFim", dataFim));
		
		table.add(block);
		
		Button button = new Button("Listar", new Action("visualizarProdutividadeAseguradora"));
		button.getAction().add("lista", true);
		table.addFooter(button);
		
		Button button3 = new Button("Generar Excel", new Action("visualizarProdutividadeAseguradora"));
		button3.getAction().add("lista", true);
		button3.getAction().add("excel", true);
		table.addFooter(button3);

		Button button2 = new Button("Volver", new Action("visualizarInspecaoSitu"));
		table.addFooter(button2);
		
		mainTable.add(table);
		
		table = new Table(14);
		table.addSubtitle("");
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);

		if (this.lista)
		{
			if (agentes.size() == 0) 
			{
				table.setNextColSpan(table.getColumns());
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Nenhum agente nesse periodo");
			} 
			else 
			{
				for (Iterator i = agentes.iterator(); i.hasNext();) 
				{
					Entidade agente = (Entidade) i.next();

					table.setNextColSpan(table.getColumns());
					table.setNextHAlign(Table.HALIGN_LEFT);
					table.addHeader("Agente: " + agente.obterNome());
					
					Collection<Apolice> apolices = agente.obterApolicesComoAgentePorPeriodo(this.dataInicio, this.dataFim, this.aseguradora);
					
					this.montaTabela(table, apolices);
					
					table.setNextColSpan(table.getColumns());
					table.add(new Space());
				}
			}
			if(apolicesSemAgentes.size() > 0)
			{
				table.setNextColSpan(table.getColumns());
				table.setNextHAlign(Table.HALIGN_LEFT);
				table.addHeader("Agente: 000");
				this.montaTabela(table, apolicesSemAgentes);
			}
		}
		
		mainTable.add(table);

		Border border = new Border(mainTable);

		return border;
	}
	
	private void montaTabela(Table table, Collection<Apolice> apolices) throws Exception
	{
		table.addHeader("Nº Póliza");
		table.addHeader("Situación");
		table.addHeader("Asegurado");
		table.addHeader("Emisión");
		table.setNextColSpan(2);
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Vigencia");
		table.addHeader("Tipo Operación");
		table.addHeader("Secccón");
		table.setNextColSpan(2);
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Comisión");
		table.addHeader("Prima");
		table.addHeader("Premio");
		table.setNextColSpan(2);
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Cap. Asegurado");

		table.add("");
		table.add("");
		table.add("");
		table.add("");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add("Inicio");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add("Fin");
		//table.add("");
		table.add("");
		table.add("");
		//table.add("%");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add("Gs");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add("M.E.");
		table.add("");
		table.add("");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add("Gs");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add("M.E.");

		for (Iterator<Apolice> j = apolices.iterator(); j.hasNext();) 
		{
			Apolice apolice = (Apolice) j.next();

			Link link2 = new Link(apolice.obterNumeroApolice(),	new Action("visualizarEvento"));
			link2.getAction().add("id", apolice.obterId());

			table.add(link2);

			table.add(apolice.obterSituacaoSeguro());
			table.add(apolice.obterNomeAsegurado());
			table.add(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataEmissao()));
			table.add(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaInicio()));
			table.add(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaConclusao()));
			table.add(apolice.obterTipo());
			table.add(apolice.obterSecao().obterNome());

			double comissaoGs = apolice.obterComissaoGs();
			double comissaoMe = apolice.obterComissaoMe();

			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(comissaoGs,"#,##0.00"));//, ));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(comissaoMe,"#,##0.00"));//, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(apolice.obterPrimaGs(),"#,##0.00"));//,
														 // "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(apolice.obterPremiosGs(),"#,##0.00"));//,
														   // "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(apolice.obterCapitalGs(),"#,##0.00"));//,
														   // "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(apolice.obterCapitalMe(),"#,##0.00"));//,
														   // "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
		}
	}
}