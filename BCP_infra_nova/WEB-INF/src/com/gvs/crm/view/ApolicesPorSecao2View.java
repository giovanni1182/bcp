package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class ApolicesPorSecao2View extends PortalView 
{

	private Aseguradora aseguradora;
	private Date dataInicio, dataFim;
	private ClassificacaoContas cContas;
	private Collection<Apolice> apolices;
	private String situacao;
	private String situacao2;
	
	public ApolicesPorSecao2View(Aseguradora aseguradora, Date dataInicio, Date dataFim, ClassificacaoContas cContas, Collection<Apolice> apolices, String situacao, String situacao2) throws Exception
	{
		this.aseguradora = aseguradora;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.cContas = cContas;
		this.apolices = apolices;
		this.situacao = situacao;
		this.situacao2 = situacao2;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception 
	{
		Table table = new Table(8);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);
		table.addSubtitle("Pólizas de " + new SimpleDateFormat("dd/MM/yyyy").format(this.dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(this.dataFim) + " Cantidad " + this.apolices.size() + " - Situación: " + this.situacao);
		
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Vigencia");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Numero");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Plan");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Asegurado");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Tipo");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Situación");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Valor");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Vlr Pg Aseguradora (Gs)");
		
		double totalValor = 0;
		double totalRecuperado = 0;
		Link link;
		double montante,recuperado;
		
		for(Apolice apolice : this.apolices)
		{
			table.add(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaInicio())
					+ " - "
					+ new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaConclusao()));

			link = new Link(apolice.obterNumeroApolice(), new Action("visualizarEvento"));
			link.getAction().add("id", apolice.obterId());
			table.add(link);
			table.add(apolice.obterPlano().obterPlano()  + " " + apolice.obterPlano().obterIdentificador());
			table.add(apolice.obterNomeAsegurado());
			table.add(apolice.obterTipo());
			table.add(apolice.obterSituacaoSeguro());
			
			montante = apolice.obterMontanteGsSinistro(this.situacao, this.dataInicio, this.dataFim);
			recuperado = apolice.obterRecuperadosSinistro(this.situacao, this.dataInicio, this.dataFim);
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(montante,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(recuperado,"#,##0.00"));
			
			totalValor+=montante;
			totalRecuperado+=recuperado;
		}
		
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("TOTAL");
		table.addHeader("");
		table.addHeader("");
		table.addHeader("");
		table.addHeader("");
		table.addHeader("");
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(new Label(totalValor,"#,##0.00"));
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(new Label(totalRecuperado,"#,##0.00"));
		
		Button voltarButton = new Button("Volver",new Action("visualizarCentralRiscoSinistro"));
		voltarButton.getAction().add("aseguradoraId",this.aseguradora.obterId());
		voltarButton.getAction().add("dataInicio",this.dataInicio);
		voltarButton.getAction().add("dataFim",this.dataFim);
		voltarButton.getAction().add("_pastaSinistro","2");
		
		table.addFooter(voltarButton);
		
		Button excelButton = new Button("Generar Excel",new Action("excelCentralRiscoSinistro2"));
		excelButton.getAction().add("aseguradoraId",this.aseguradora.obterId());
		excelButton.getAction().add("dataInicio",this.dataInicio);
		excelButton.getAction().add("dataFim",this.dataFim);
		excelButton.getAction().add("situacao",this.situacao);
		excelButton.getAction().add("secaoId",this.cContas.obterId());
		excelButton.getAction().add("situacao2",this.situacao2);
		
		
		table.addFooter(excelButton);
		
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
		return new Label("Pólizas de la Aseguradora " + this.aseguradora.obterNome() +" - Sección " + this.cContas.obterNome() + " " + this.cContas.obterApelido());
	}

	public Entidade getOrigemMenu() throws Exception 
	{
		return this.aseguradora;
	}
}