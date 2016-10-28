package com.gvs.crm.view;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class MaioresApolicesAgentesView extends PortalView
{
	private AuxiliarSeguro agente;
	private String tipoValor;
	private Date dataInicio;
	private Date dataFim;
	private String situacao;
	private Collection<Apolice> apolices;
	private double monto;
	private boolean auxiliar;
	
	public MaioresApolicesAgentesView(AuxiliarSeguro agente,String tipoValor,Date dataInicio,Date dataFim, String situacao, Collection<Apolice> apolices, double monto, boolean auxiliar) throws Exception
	{
		this.agente = agente;
		this.tipoValor = tipoValor;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.situacao = situacao;
		this.apolices = apolices;
		this.monto = monto;
		this.auxiliar = auxiliar;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		table.setWidth("40%");
		
		String situacao2 = "";
		String consultaPor = "";
		
		if(tipoValor.equals("valorPrima"))
			consultaPor = "Prima";
		else if(tipoValor.equals("valorCapital"))
			consultaPor = "Capital en Riesgo";
		else if(tipoValor.equals("valorComissao"))
			consultaPor = "Comisión";
		
		if(situacao.equals("0"))
			situacao2 = "Todas";
		
		DecimalFormat format = new DecimalFormat("#,##0.00");
		
		table.addSubtitle("Apolices");
		
		if(this.auxiliar)
			table.addHeader("Agente:");
		else
			table.addHeader("Corredor:");
		table.add(this.agente.obterNome());
		table.addHeader("Consulta por:");
		table.add(consultaPor);
		table.addHeader("Pólizas Vigentes desde:");
		
		Block block = new Block(Block.HORIZONTAL);
		
		block.add(new Label(dataInicio, "dd/MM/yyyy"));
		block.add(new Space(2));
		Label label = new Label("hasta");
		label.setBold(true);
		block.add(label);
		block.add(new Space(2));
		block.add(new Label(dataFim, "dd/MM/yyyy"));
		table.add(block);
		
		table.addHeader("Situacion:");
		table.add(situacao2);
		table.addHeader("Monto en Guaraníes:");
		table.add(format.format(this.monto));
		
		table.setNextColSpan(table.getColumns());
		table.add(new Space());
		
		Entidade ultimaSeguradora = null;
		
		Table table2 = new Table(5);
		table2.setWidth("100%");
		table2.addStyle(Table.STYLE_ALTERNATE);
		
		double total = 0;
		double totalAseg = 0;
		
		for(Iterator<Apolice> i = this.apolices.iterator() ; i.hasNext() ; )
		{
			Apolice apolice = i.next();
			
			if(ultimaSeguradora == null)
			{
				table2.addSubtitle(apolice.obterOrigem().obterNome());
				this.montaCabecalho(table2);
			}
			else
			{
				if(apolice.obterOrigem().obterId()!=ultimaSeguradora.obterId())
				{
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.addHeader("TOTAL");
					table2.add("");
					table2.add("");
					table2.add("");
					table2.setNextHAlign(Table.HALIGN_RIGHT);
					table2.addHeader(format.format(totalAseg));
					
					totalAseg=0;
					
					table2.addSubtitle(apolice.obterOrigem().obterNome());
					this.montaCabecalho(table2);
				}
			}
			
			Link link = new Link(apolice.obterNumeroApolice(), new Action("visualizarEvento"));
			link.getAction().add("id", apolice.obterId());
			
			table2.add(link);
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.add(new Label(apolice.obterDataPrevistaInicio(), "dd/MM/yyyy"));
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.add(new Label(apolice.obterDataPrevistaConclusao(), "dd/MM/yyyy"));
			table2.add(apolice.obterSituacaoSeguro());
			
			double valor = 0;
			
			if(tipoValor.equals("valorPrima"))
				valor = apolice.obterPrimaGs();
			else if(tipoValor.equals("valorCapital"))
				valor = apolice.obterCapitalGs();
			else
				valor = apolice.obterComissaoGs();
			
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.add(format.format(valor));
			
			total+=valor;
			totalAseg+=valor;
			
			ultimaSeguradora = apolice.obterOrigem();
		}
		
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.addHeader("TOTAL");
		table2.add("");
		table2.add("");
		table2.add("");
		table2.setNextHAlign(Table.HALIGN_RIGHT);
		table2.addHeader(format.format(totalAseg));
		
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.addHeader("TOTAL GENERAL");
		table2.add("");
		table2.add("");
		table2.add("");
		table2.setNextHAlign(Table.HALIGN_RIGHT);
		table2.addHeader(format.format(total));
		
		table.setNextColSpan(table.getColumns());
		table.add(table2);
		
		return table;
	}
	
	private void montaCabecalho(Table table) throws Exception
	{
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Numero");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Inicio Vigencia");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Fim Vigencia");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Situacion");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Valor");
		
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