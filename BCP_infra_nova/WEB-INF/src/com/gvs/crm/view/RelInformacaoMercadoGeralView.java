package com.gvs.crm.view;

import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.PeriodoDatasBlock;
import com.gvs.crm.component.SimNao2Select;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputInteger;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class RelInformacaoMercadoGeralView extends PortalView
{
	private Date dataInicio, dataFim;
	private int anoInicio, anoFim;
	private String tipo;
	private String anoFiscal;
	
	public RelInformacaoMercadoGeralView(Date dataInicio, Date dataFim, int anoInicio, int anoFim, String tipo, String anoFiscal) throws Exception
	{
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.anoInicio = anoInicio;
		this.anoFim = anoFim;
		this.tipo = tipo;
		this.anoFiscal = anoFiscal;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		
		table = new Table(2);
		table.addHeader("Mes:");
		table.add(new PeriodoDatasBlock("dataInicio", dataInicio, "dataFim", dataFim, true));
		table.addHeader("Año:");
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputInteger("anoInicio", anoInicio, 4));
		Label l = new Label("hasta");
		l.setBold(true);
		block.add(new Space(2));
		block.add(l);
		block.add(new Space(2));
		block.add(new InputInteger("anoFim", anoFim, 4));
		table.add(block);
		
		table.addHeader("Año Fiscal:");
		table.add(new SimNao2Select("anoFiscal", anoFiscal));
		
		Button consultarButton = new Button("Generar Excel", new Action("calculoInformacaoMercadoGeral"));
		consultarButton.getAction().add("tipo", tipo);
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
		return new Label("Información del Mercado - " + tipo);
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}