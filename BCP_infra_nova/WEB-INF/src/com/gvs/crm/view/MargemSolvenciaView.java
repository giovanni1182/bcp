package com.gvs.crm.view;

import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.AseguradorasSelect;
import com.gvs.crm.component.PeriodoDatasBlock;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class MargemSolvenciaView extends PortalView
{
	private Aseguradora aseguradora;
	private Date dataInicio, dataFim;
	
	public MargemSolvenciaView(Aseguradora aseguradora, Date dataInicio, Date dataFim)
	{
		this.aseguradora = aseguradora;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect("aseguradoraId2", aseguradora, true));
		table.addHeader("Periodo:");
		table.add(new PeriodoDatasBlock("dataInicio", dataInicio, "dataFim", dataFim, false));
		
		Button consultarButton = new Button("Generar Excel", new Action("calculoMargemSolvencia"));
		table.addFooter(consultarButton);
		
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
		return new Label("Margen de Solvencia");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}