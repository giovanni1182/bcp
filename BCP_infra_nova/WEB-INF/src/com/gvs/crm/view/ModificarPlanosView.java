package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ModificarPlanosView extends PortalView
{
	private Collection<String> secoes;
	private Collection<String> codigos;
	
	public ModificarPlanosView(Collection<String> secoes, Collection<String> codigos)
	{
		this.secoes = secoes;
		this.codigos = codigos;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(3);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		table.addStyle(Table.STYLE_ALTERNATE);
		table.addSubtitle("Sección");
		
		int cont = 1;
		
		for(Iterator<String> i = this.secoes.iterator() ; i.hasNext() ; )
		{
			String secao = i.next();
			
			InputString input = new InputString("secao"+cont, secao, secao.length());
			input.setEnabled(false);
			table.add(input);
			table.add(new InputString("novaSecao"+cont, "", 50));
			
			cont++;
		}
		
		Button atualizarButton = new Button("Actualizar", new Action("alterarPlanos"));
		atualizarButton.getAction().add("tipo", "secao");
		table.addFooter(atualizarButton);
		
		mainTable.add(table);
		mainTable.add(new Space(10));
		
		table = new Table(2);
		table.addStyle(Table.STYLE_ALTERNATE);
		table.addSubtitle("Modalidad");
		cont = 1;
		
		for(Iterator<String> i = this.codigos.iterator() ; i.hasNext() ; )
		{
			String codigo = i.next();
			
			InputString input = new InputString("codigo"+cont, codigo, codigo.length());
			input.setEnabled(false);
			table.add(input);
			table.add(new InputString("novoCodigo"+cont, "", 50));
			cont++;
		}
		
		atualizarButton = new Button("Actualizar", new Action("alterarPlanos"));
		atualizarButton.getAction().add("tipo", "codigo");
		table.addFooter(atualizarButton);
		
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
		return new Label("Modificación de Sección o Modalidad de Planes de Seguro");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}