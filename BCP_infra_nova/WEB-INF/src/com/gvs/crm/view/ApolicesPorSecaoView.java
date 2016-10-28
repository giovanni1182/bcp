package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
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

public class ApolicesPorSecaoView extends PortalView 
{

	private Aseguradora aseguradora;
	private Date dataInicio, dataFim;
	private ClassificacaoContas cContas;
	private Collection apolices;
	
	public ApolicesPorSecaoView(Aseguradora aseguradora, Date dataInicio, Date dataFim, ClassificacaoContas cContas, Collection apolices) throws Exception
	{
		this.aseguradora = aseguradora;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.cContas = cContas;
		this.apolices = apolices;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception 
	{
		Table table = new Table(7);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);
		table.addSubtitle("Pólizas de " + new SimpleDateFormat("dd/MM/yyyy").format(this.dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(this.dataFim) + " Cantidad " + this.apolices.size());
		
		table.addHeader("Vigencia");
		table.addHeader("Numero");
		table.addHeader("Sección");
		table.addHeader("Plan");
		table.addHeader("Asegurado");
		table.addHeader("Tipo");
		table.addHeader("Situación");
		
		for(Iterator i = this.apolices.iterator() ; i.hasNext() ; )
		{
			Apolice apolice = (Apolice) i.next();
			
			table.add(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaInicio())
					+ " - "
					+ new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaConclusao()));

			Link link = new Link(apolice.obterNumeroApolice(), new Action("visualizarEvento"));
			link.getAction().add("id", apolice.obterId());
			table.add(link);
			table.add(apolice.obterSecao().obterApelido());
			table.add(apolice.obterPlano().obterPlano()  + " " + apolice.obterPlano().obterIdentificador());
			table.add(apolice.obterNomeAsegurado());
			table.add(apolice.obterTipo());
			
			table.add(apolice.obterSituacaoSeguro());
		}
		
		Button voltarButton = new Button("Volver",new Action("visualizarCentralRiscoSinistro"));
		voltarButton.getAction().add("aseguradoraId",this.aseguradora.obterId());
		voltarButton.getAction().add("dataInicio",this.dataInicio);
		voltarButton.getAction().add("dataFim",this.dataFim);
		voltarButton.getAction().add("_pastaSinistro","2");
		
		table.addFooter(voltarButton);
		
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