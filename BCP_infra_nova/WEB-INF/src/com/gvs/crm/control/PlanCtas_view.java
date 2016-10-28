/*
 * Created on Mar 14, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gvs.crm.control;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.view.PortalView;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputInteger;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

/**
 * @author User
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PlanCtas_view extends PortalView {
	private Aseguradora aseguradora;
	private int mes,year;
	private Collection plan_ctas;
	
	/* (non-Javadoc)
	 * @see com.gvs.crm.view.PortalView#getBody(infra.security.User, java.util.Locale, java.util.Properties)
	 */
	public PlanCtas_view (Aseguradora aseguradora, int mes, int year,Collection plan_ctas) throws Exception{
		
		this.aseguradora = aseguradora;
		this.mes = mes;
		this.year = year;
		this.plan_ctas = plan_ctas;
	}
	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(4) ;
		Table table2 = new Table(2);
		table.setWidth("100%");
		table2.setWidth("100%");
		
		table2.addHeader("ASEGURADORA:");
		table2.add(new EntidadePopup("aseguradora_id","aseguradora_nombre",aseguradora,"aseguradora",true));
		table2.addHeader("Mes");
		Block bloque = new Block(Block.HORIZONTAL);
		bloque.add(new InputInteger("Mes",mes,2));
		bloque.add(new Label("Año"));
		bloque.add(new InputInteger("Year",year,4));
		table2.add(bloque);
		// TODO Auto-generated method stub
		Button boton = new Button("Buscar",new Action("visualizarPaginaPrueba"));
		boton.getAction().add("Lista",true);
		table2.setNextColSpan(table2.getColumns());
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.add(boton);
		if (plan_ctas.size() > 0){
			table.addSubtitle("Plan de Cuentas:  "+ plan_ctas.size());
			
			table.addHeader("Saldo Actual");
			table.addHeader("Nivel de Cta.");
			table.addHeader("Credito");
			table.addHeader("Debito");
			String mes1 = new Integer(mes).toString();
			String year1 = new Integer(year).toString();
			
		
		for (Iterator i = plan_ctas.iterator();i.hasNext();){
			Entidade entidade = (Entidade) i.next();
			
			//ClassificacaoContas clasifctas = (ClassificacaoContas) i.next();
			if (entidade instanceof ClassificacaoContas){
				ClassificacaoContas clasifctas = (ClassificacaoContas) entidade;
				table.add(new Label(clasifctas.obterTotalizacaoExistente(this.aseguradora,mes1+year1)));
				table.add(clasifctas.obterNivel());
				table.add(new Label (clasifctas.obterTotalizacaoCreditoExistente(this.aseguradora,mes1+year1)));
				table.add(new Label (clasifctas.obterTotalizacaoDebitoExistente(this.aseguradora,mes1+year1)));
			
			}
			else if (entidade instanceof Conta){
				Conta ctas = (Conta) entidade;
				table.add(new Label(ctas.obterTotalizacaoExistente(this.aseguradora,mes1+year1)));
				table.add(ctas.obterNivel());
				table.add(new Label (ctas.obterTotalizacaoCreditoExistente(this.aseguradora,mes1+year1)));
				table.add(new Label (ctas.obterTotalizacaoDebitoExistente(this.aseguradora,mes1+year1)));
				
				}
		}
		}
		else{
			table2.setNextColSpan(table2.getColumns());
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("No se recuperaron las ctas para los parametros solicitados");
			
		}
		table2.setNextColSpan(table2.getColumns());
		table.addStyle(Table.STYLE_ALTERNATE);
		table2.add(table);	
		return table2;
	}

	/* (non-Javadoc)
	 * @see com.gvs.crm.view.PortalView#getSelectedGroup()
	 */
	public String getSelectedGroup() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.gvs.crm.view.PortalView#getSelectedOption()
	 */
	public String getSelectedOption() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.gvs.crm.view.PortalView#getTitle()
	 */
	public View getTitle() throws Exception {
		// TODO Auto-generated method stub
		return new Label("Aseguradoras y plan de cuentas");
	}

	/* (non-Javadoc)
	 * @see com.gvs.crm.view.PortalView#getOrigemMenu()
	 */
	public Entidade getOrigemMenu() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
