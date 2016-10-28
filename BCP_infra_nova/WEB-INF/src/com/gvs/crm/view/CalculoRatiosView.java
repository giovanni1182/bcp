package com.gvs.crm.view;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.PeriodoDatasBlock;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Check;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class CalculoRatiosView extends PortalView
{
	private Date dataInicio, dataFim;
	private Collection<Entidade> aseguradorasMarcadas;
	private boolean todas, calculoRatio1, ratiosAgregados1, ratiosAgregados;
	
	public CalculoRatiosView(Date dataInicio, Date dataFim, Collection<Entidade> aseguradorasMarcadas, boolean todas, boolean calculoRatio1, boolean ratiosAgregados, boolean ratiosAgregados1) throws Exception
	{
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.aseguradorasMarcadas = aseguradorasMarcadas;
		this.todas = todas;
		this.calculoRatio1 = calculoRatio1;
		this.ratiosAgregados1 = ratiosAgregados1;
		this.ratiosAgregados = ratiosAgregados;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(user);
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		
		Collection<Aseguradora> aseguradoras = home.obterAseguradoras();
		
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		table.addHeader("Periodo:");
		table.add(new PeriodoDatasBlock("dataInicio", dataInicio, "dataFim", dataFim, false));
		table.add(new Space());
		table.add(new Space());
		
		mainTable.add(table);
		
		table = new Table(3);
		table.addStyle(Table.STYLE_ALTERNATE);
		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Aseguradoras");
		
		Aseguradora aseg;
		Check check;
		long id;
		Block block;
		
		Button consultarButton = new Button("Todas", new Action("calculoRatios"));
		consultarButton.getAction().add("todas", !todas);
		consultarButton.getAction().add("view", true);
		consultarButton.getAction().add("calculoRatios1", calculoRatio1);
		consultarButton.getAction().add("ratiosAgregados", ratiosAgregados);
		consultarButton.getAction().add("ratiosAgregados1", ratiosAgregados1);
		table.setNextColSpan(table.getColumns());
		table.add(consultarButton);
		
		for(Iterator<Aseguradora> i = aseguradoras.iterator() ; i.hasNext() ; )
		{
			aseg = i.next();
			
			id = aseg.obterId();
			
			block = new Block(Block.HORIZONTAL);
			
			if(todas)
				check = new Check("aseguradoraId", new Long(id).toString(), true);
			else
				check = new Check("aseguradoraId", new Long(id).toString(), aseguradorasMarcadas.contains(aseg));
			
			block.add(check);
			block.add(new Space(2));
			block.add(new Label(aseg.obterNome()));
			
			table.add(block);
		}
		
		consultarButton = new Button("Generar Excel", new Action("calculoRatios"));
		consultarButton.getAction().add("calculoRatios1", calculoRatio1);
		consultarButton.getAction().add("ratiosAgregados", ratiosAgregados);
		consultarButton.getAction().add("ratiosAgregados1", ratiosAgregados1);
		table.addFooter(consultarButton);
		
		mainTable.add(table);
		
		return mainTable;
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
		if(calculoRatio1)
			return new Label("Calculo de Ratios Financieros 1");
		else if(ratiosAgregados)
			return new Label("Ratios Agregados");
		else if(ratiosAgregados1)
			return new Label("Ratios Agregados 1");
		else
			return new Label("Calculo de Ratios Financieros");
			
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}