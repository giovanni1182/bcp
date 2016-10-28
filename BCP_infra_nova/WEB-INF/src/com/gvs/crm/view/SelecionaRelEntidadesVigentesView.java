package com.gvs.crm.view;

import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.RelEntidadesVigentesSelect;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Radio;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class SelecionaRelEntidadesVigentesView extends PortalView
{
	private int escolha;
	private Date data;
	private String texto;
	private String opcao;
	private boolean ci;
	
	public SelecionaRelEntidadesVigentesView(int escolha, Date data, String texto, String opcao, boolean ci) throws Exception
	{
		this.escolha = escolha;
		this.data = data;
		this.texto = texto;
		this.opcao = opcao;
		this.ci = ci;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		
		table.addSubtitle("");
		
		table.addHeader("Listado:");
		table.add(new RelEntidadesVigentesSelect("escolha",this.escolha,ci));
		table.addHeader("Fecha:");
		table.add(new InputDate("data", this.data));
		table.addHeader("Observación:");
		table.add(new InputText("texto", this.texto,10,70));
		
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
		
		Button gerarButton = new Button("Generar",new Action("selecionarRelEntidadesVigentes"));
		gerarButton.getAction().add("ci", ci);
		
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
		if(ci)
			return new Label("Listado Entidades Vigentes con identificación");
		else
			return new Label("Listado Entidades Vigentes");
	}
}