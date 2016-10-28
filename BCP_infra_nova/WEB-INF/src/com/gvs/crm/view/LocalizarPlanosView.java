package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.PlanoRamoSelect;
import com.gvs.crm.component.PlanoSecao2Select;
import com.gvs.crm.component.PlanoSelect;
import com.gvs.crm.component.PlanosAseguradorasSelect;
import com.gvs.crm.component.SituacaoPlanoSelect;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Plano;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class LocalizarPlanosView extends PortalView 
{
	private Collection<Plano> planos;
	private Entidade origemMenu;
	private String ramo, secao, plano, situacao;
	private Aseguradora aseguradora;
	private boolean especial, modificado;
	
	public LocalizarPlanosView(Collection<Plano> planos, String ramo, String secao, String plano, String situacao, Aseguradora aseguradora, boolean especial, boolean modificado) throws Exception
	{
		this.planos = planos;
		this.ramo = ramo;
		this.secao = secao;
		this.plano = plano;
		this.situacao = situacao;
		this.aseguradora = aseguradora;
		this.especial = especial;
		this.modificado = modificado;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception 
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		table.addHeader("Aseguradora:");
		table.add(new PlanosAseguradorasSelect("aseguradora", this.aseguradora, true, especial, modificado));
		table.addHeader("Ramo:");
		//table.add(new RamosSelect("ramo", this.ramo));
		Action action = new Action("visualizarPlanos");
		action.add("view", true);
		
		table.add(new PlanoRamoSelect("ramo", ramo, action, false, true));
		
		table.addHeader("Sección:");
		//table.add(new PlanoSecaoSelect("secao", this.secao));
		table.add(new PlanoSecao2Select("secao", ramo, secao, action));
		
		table.addHeader("Modalidad:");
		//table.add(new PlanoPlanosSelect("plano", this.plano));
		table.add(new PlanoSelect("plano", secao, plano, action, false));
		
		table.addHeader("Situación:");
		table.add(new SituacaoPlanoSelect("situacao", this.situacao));
		
		Button buscarButton = new Button("Buscar", new Action("visualizarPlanos"));
		table.addFooter(buscarButton);
		
		mainTable.add(table);
		
		int qtde = planos.size();
		
		if(qtde > 0)
		{
			table = new Table(9);
			table.setWidth("100%");
			table.addStyle(Table.STYLE_ALTERNATE);
			table.addSubtitle(qtde + " Plan(es)");
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Aseguradora");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Ramo");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Sección");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Modalidad");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Situación");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Codigo");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Resolución");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Fecha Resolución");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Denominación");
			
			for(Iterator<Plano> i = planos.iterator() ; i.hasNext() ; )
			{
				Plano plano = i.next();
				
				if(plano.obterEspecial() != 1)
					table.add(plano.obterOrigem().obterNome());
				else
					table.add("");
				
				table.add(plano.obterRamo());
				
				table.add(plano.obterSecao());
				table.add(plano.obterPlano());
				table.add(plano.obterSituacao());
				
				Link link = new Link(plano.obterIdentificador(), new Action("visualizarEvento"));
				link.getAction().add("id", plano.obterId());
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(link);
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(plano.obterResolucao());
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(new Label(plano.obterDataResolucao(),"dd/MM/yyyy"));
				table.add(plano.obterTitulo());
			}
			
			mainTable.add(table);
		}
		
		return mainTable;
	}

	public String getSelectedGroup() throws Exception 
	{
		return "Entidades/Eventos";
	}

	public String getSelectedOption() throws Exception 
	{
		return "Localizar";
	}

	public View getTitle() throws Exception 
	{
		return new Label("Listado Planes");
	}

	public Entidade getOrigemMenu() throws Exception 
	{
		return this.origemMenu;
	}
}