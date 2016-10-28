package com.gvs.crm.view;

import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.AseguradorasSelect2;
import com.gvs.crm.component.PlanoRamoSelect;
import com.gvs.crm.component.PlanoSecao2Select;
import com.gvs.crm.component.PlanoSelect;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Check;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class TotalApolicesPorSecaoAnualView extends PortalView
{
	private Date dataInicio, dataFim;
	private boolean valores;
	private String ramo, secao, modalidade;
	private Aseguradora aseguradora;
	
	public TotalApolicesPorSecaoAnualView(Date dataInicio, Date dataFim, boolean valores, String ramo, String secao, Aseguradora aseguradora, String modalidade) throws Exception
	{
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.valores = valores;
		this.ramo = ramo;
		this.secao = secao;
		this.aseguradora = aseguradora;
		this.modalidade = modalidade;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect2("aseguradora",aseguradora, true, false));
		
		table.addHeader("Ramo:");
		Action action = new Action("apolicesPorSecaoAnual");
		action.add("view", true);
		
		table.add(new PlanoRamoSelect("ramo", ramo, action, false, true));
		
		table.addHeader("Sección:");
		table.add(new PlanoSecao2Select("secao", ramo, secao, action));
		
		table.addHeader("Modalidad:");
	    if (!this.secao.equals(""))
	    	table.add(new PlanoSelect("modalidade", secao, modalidade, null, true));
	    else
	      table.add("");
		
		table.addHeader("Fecha de emisión:");
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputDate("dataInicio", this.dataInicio));
		block.add(new Space(2));
		block.add(new Label("hasta"));
		block.add(new Space(2));
		block.add(new InputDate("dataFim", this.dataFim));
		table.add(block);
		
		table.addHeader("Valores en Gs y Me:");
		table.add(new Check("valores", "true", valores));
		
		Button gerarButton = new Button("Generar",new Action("apolicesPorSecaoAnual"));
		
		table.addFooter(gerarButton);
		
		/*table.addHeader("Ultimo Mes/Año:");
		table.add(new InputDate("data", this.data));*/
		
		return table;
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
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
		return new Label("Pólizas por Sección Anual");
	}
}
