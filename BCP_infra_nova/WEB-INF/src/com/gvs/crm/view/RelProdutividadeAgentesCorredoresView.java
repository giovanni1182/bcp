package com.gvs.crm.view;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.AseguradorasSelect2;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;

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

public class RelProdutividadeAgentesCorredoresView extends PortalView
{
	private Aseguradora aseguradora;
	private Date dataInicio;
	private Date dataFim;
	private boolean lista;
	private DecimalFormat formataValor;
	private SimpleDateFormat formataData;
	private double totalGeralComissaoGs,totalGeralComissaoMe, totalGeralPrimaGs, totalGeralPremioGs, totalGeralCapitalGs, totalGeralCapitalMe;
	private int qtdeGeral;
	
	public RelProdutividadeAgentesCorredoresView(Aseguradora aseguradora, Date dataInicio, Date dataFim, boolean lista) throws Exception 
	{
		this.aseguradora = aseguradora;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.lista = lista;
	}
	
	public View getBody(User user, Locale arg1, Properties arg2) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(user);
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		
		formataValor = new DecimalFormat("#,##0.00");
		formataData = new SimpleDateFormat("dd/MM/yyyy");
		
		Collection<Aseguradora> aseguradoras = new ArrayList<Aseguradora>();
		if(this.aseguradora == null)
			aseguradoras = entidadeHome.obterAseguradoras();
		else
			aseguradoras.add(aseguradora);
		
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		//table.addSubtitle("Detalle de la Produción de la Aseguradora");
		
		//table.addHeader("Aseguradora:");
		//table.add(new EntidadePopup("aseguradoraId", "aseguradoraNome",aseguradora, "Aseguradora", true));
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect2("aseguradoraId", this.aseguradora, false, true));
		table.addHeader("Periodo:");
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputDate("dataInicio", dataInicio));
		block.add(new Space(2));
		block.add(new Label("hasta"));
		block.add(new Space(2));
		block.add(new InputDate("dataFim", dataFim));
		
		table.add(block);
		
		Button button = new Button("Listar", new Action("visualizarProdutividadeAgentesCorredores"));
		button.getAction().add("lista", true);
		table.addFooter(button);
		
		Button button3 = new Button("Generar Excel", new Action("visualizarProdutividadeAgentesCorredores"));
		button3.getAction().add("lista", true);
		button3.getAction().add("excel", true);
		table.addFooter(button3);

		mainTable.add(table);
		
		table = new Table(14);
		table.addSubtitle("");
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);

		if (this.lista)
		{
			Collection<Entidade> agentes;
			Collection<Entidade> corredores;
			Collection<Apolice> apolices;
			Aseguradora aseg;
			Entidade agente,corredor;
			
			for(Iterator<Aseguradora> i = aseguradoras.iterator() ; i.hasNext() ; )
			{
				aseg = i.next();
				
				table.setNextColSpan(table.getColumns());
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader(aseg.obterNome());
				
				agentes = aseguradoraHome.obterAgentesPorPeridodo(aseg, dataInicio, dataFim);
				corredores = aseguradoraHome.obterCorredoresPorPeridodo(aseg, dataInicio, dataFim);
				
				for (Iterator<Entidade> j = agentes.iterator(); j.hasNext();) 
				{
					agente = j.next();

					table.setNextColSpan(table.getColumns());
					table.addHeader("Agente: " + agente.obterNome());
					
					apolices = agente.obterApolicesComoAgentePorPeriodo(this.dataInicio, this.dataFim, aseg);
					
					this.montaTabela(table, apolices);
					
					table.setNextColSpan(table.getColumns());
					table.add(new Space());
				}
				
				for (Iterator<Entidade> j = corredores.iterator(); j.hasNext();) 
				{
					corredor = j.next();

					table.setNextColSpan(table.getColumns());
					table.addHeader("Corredor: " + corredor.obterNome());
					
					apolices = corredor.obterApolicesComoCorredorPorPeriodo(this.dataInicio, this.dataFim, aseg);
					
					this.montaTabela(table, apolices);
					
					table.setNextColSpan(table.getColumns());
					table.add(new Space());
				}
			}
			
			table.addSubtitle("TOTAL GENERAL");
			
			table.addHeader("");
			table.addHeader("");
			table.addHeader("");
			table.addHeader("");
			table.setNextColSpan(2);
			table.addHeader("");
			table.addHeader("");
			table.addHeader("");
			table.setNextColSpan(2);
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Comisión");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Prima");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Premio");
			table.setNextColSpan(2);
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Cap. Asegurado");
			
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
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
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("TOTAL");
			table.add("");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader(new Label(qtdeGeral));
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(formataValor.format(totalGeralComissaoGs));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(formataValor.format(totalGeralComissaoMe));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(formataValor.format(totalGeralPrimaGs));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(formataValor.format(totalGeralPremioGs));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(formataValor.format(totalGeralCapitalGs));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(formataValor.format(totalGeralCapitalMe));
		}
		
		mainTable.add(table);

		return mainTable;
	}
	
	private void montaTabela(Table table, Collection<Apolice> apolices) throws Exception
	{
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Nº Póliza");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Situación");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Asegurado");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Emisión");
		table.setNextColSpan(2);
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Vigencia");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Tipo Operación");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Secccón");
		table.setNextColSpan(2);
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Comisión");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Prima");
		table.setNextHAlign(Table.HALIGN_CENTER);
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
		table.add("");
		table.add("");
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
		
		Apolice apolice;
		Link link2;
		double comissaoGs,comissaoMe, primaGs, premioGs, capitalGs, capitalMe;
		double totalComissaoGs=0,totalComissaoMe=0, totalPrimaGs=0, totalPremioGs=0, totalCapitalGs=0, totalCapitalMe=0;
		int qtde = apolices.size();
		
		for (Iterator<Apolice> j = apolices.iterator(); j.hasNext();) 
		{
			apolice = j.next();

			link2 = new Link(apolice.obterNumeroApolice(),	new Action("visualizarEvento"));
			link2.getAction().add("id", apolice.obterId());

			table.add(link2);

			table.add(apolice.obterSituacaoSeguro());
			table.add(apolice.obterNomeAsegurado());
			table.add(formataData.format(apolice.obterDataEmissao()));
			table.add(formataData.format(apolice.obterDataPrevistaInicio()));
			table.add(formataData.format(apolice.obterDataPrevistaConclusao()));
			table.add(apolice.obterTipo());
			table.add(apolice.obterSecao().obterNome());

			comissaoGs = apolice.obterComissaoGs();
			comissaoMe = apolice.obterComissaoMe();
			primaGs = apolice.obterPrimaGs();
			premioGs = apolice.obterPremiosGs();
			capitalGs = apolice.obterCapitalGs();
			capitalMe = apolice.obterCapitalMe();
			
			totalComissaoGs+=comissaoGs;
			totalComissaoMe+=comissaoMe;
			totalPrimaGs+=primaGs;
			totalPremioGs+=premioGs;
			totalCapitalGs+=capitalGs;
			totalCapitalMe+=capitalMe;
					
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(formataValor.format(comissaoGs));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(formataValor.format(comissaoMe));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(formataValor.format(primaGs));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(formataValor.format(premioGs));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(formataValor.format(capitalGs));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(formataValor.format(capitalMe));
		}
		
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("TOTAL");
		table.add("");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader(new Label(qtde));
		table.add("");
		table.add("");
		table.add("");
		table.add("");
		table.add("");
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(formataValor.format(totalComissaoGs));
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(formataValor.format(totalComissaoMe));
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(formataValor.format(totalPrimaGs));
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(formataValor.format(totalPremioGs));
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(formataValor.format(totalCapitalGs));
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(formataValor.format(totalCapitalMe));
		
		qtdeGeral+=qtde;
		totalGeralComissaoGs+=totalComissaoGs;
		totalGeralComissaoMe+=totalComissaoMe;
		totalGeralPrimaGs+=totalPrimaGs;
		totalGeralPremioGs+=totalPremioGs;
		totalGeralCapitalGs+=totalPrimaGs;
		totalGeralCapitalMe+=totalCapitalMe;
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
		return new Label("Aseguradora - Agente y Corredor");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}