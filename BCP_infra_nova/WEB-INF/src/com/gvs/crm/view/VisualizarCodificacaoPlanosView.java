package com.gvs.crm.view;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CodificacaoCoberturaSelect;
import com.gvs.crm.component.CodificacaoDetalheSelect;
import com.gvs.crm.component.CodificacaoPlanoSelect;
import com.gvs.crm.component.CodificacaoRiscoSelect;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Image;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class VisualizarCodificacaoPlanosView extends PortalView
{
	
	private Collection sinistros;
	private Date dataInicio,dataFim;
	
	public VisualizarCodificacaoPlanosView(Collection sinistros,Date dataInicio, Date dataFim) throws Exception
	{
		this.sinistros = sinistros;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		
	}
	public View getBody(User user, Locale locale, Properties properties) throws Exception 
	{
		Table table = new Table(6);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);
		
		long planoId = Long.parseLong(properties.getProperty("_plano", "0"));
		long coberturaId = Long.parseLong(properties.getProperty("_cobertura", "0"));
		long riscoId = Long.parseLong(properties.getProperty("_risco", "0"));
		long detalheId = Long.parseLong(properties.getProperty("_detalhe", "0"));
		
		table.addHeader("Periodo");
		table.addHeader("Plan");
		table.addHeader("Cobertura");
		table.addHeader("Riesgo");
		table.addHeader("Detalle");
		table.add("");
		
		Block block = new Block(Block.HORIZONTAL);
		
		block.add(new InputDate("dataInicio",dataInicio));
		block.add(new Space());
		block.add(new Label("hasta"));
		block.add(new Space());
		block.add(new InputDate("dataFim",dataFim));
		
		table.add(block);
		
		table.add(new CodificacaoPlanoSelect("_plano",planoId));
		table.add(new CodificacaoCoberturaSelect("_cobertura",coberturaId));
		table.add(new CodificacaoRiscoSelect("_risco",riscoId));
		table.add(new CodificacaoDetalheSelect("_detalhe",detalheId));
		
		Link link = new Link(new Image("visualizar.GIF"),new Action("visualizarCodificacaoPlanos"));
		table.add(link);
		
		if(this.sinistros.size() > 0)
		{
			
		}
		else
		{
			table.setNextColSpan(table.getColumns());
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Ningún Siniestro");
		}
		
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
		return new Label("Listado Planes del Siniestro");
	}
}