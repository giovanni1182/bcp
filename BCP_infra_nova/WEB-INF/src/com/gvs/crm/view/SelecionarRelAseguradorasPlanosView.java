package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.AseguradorasSelect2;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Label;
import infra.view.Radio;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class SelecionarRelAseguradorasPlanosView extends PortalView
{
	private String opcao;
	
	public SelecionarRelAseguradorasPlanosView(String opcao) throws Exception
	{
		this.opcao = opcao;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception 
	{
		Table table = new Table(2);
		
		table.addSubtitle("");
		
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect2("id",null, false, true));
		
		table.addSubtitle("Opciones");
		
		Block block2 = new Block(Block.HORIZONTAL);
		block2.add(new Radio("gerar", "xls", "xls".equals(opcao)));
		block2.add(new Space(2));
		block2.add(new Label("Generar Excel(.xls)"));
		table.setNextColSpan(table.getColumns());
		table.add(block2);
		
		Block block5 = new Block(Block.HORIZONTAL);
		block5.add(new Radio("gerar", "pdf", "pdf".equals(opcao)));
		block5.add(new Space(2));
		block5.add(new Label("Generar PDF(.pdf)"));
		table.setNextColSpan(table.getColumns());
		table.add(block5);
		
		Button gerarButton = new Button("Generar",new Action("selecionarRelAseguradorasPlanos"));
		
		table.addFooter(gerarButton);
		
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
		return new Label("Listados Aseguradoras Planes");
	}
}