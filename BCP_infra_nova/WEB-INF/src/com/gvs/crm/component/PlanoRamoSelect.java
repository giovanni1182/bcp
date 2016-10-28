package com.gvs.crm.component;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.PlanoHome;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class PlanoRamoSelect extends BasicView 
{
	private String nome, valor;
	private Action action;
	private boolean branco, todos;
	
	public PlanoRamoSelect(String nome, String valor, Action action, boolean branco, boolean todos)
	{
		this.nome = nome;
		this.valor = valor;
		this.action = action;
		this.branco = branco;
		this.todos = todos;
	}
	
	public View execute(User user, Locale arg1, Properties arg2) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(user);
		PlanoHome home = (PlanoHome) mm.getHome("PlanoHome");
		
		Select select = null;
		if(this.action!=null)
			select = new Select(this.nome, 1, action);
		else
			select = new Select(this.nome, 1);
		
		if(branco)
			select.add("", "", false);
		else if(todos)
			select.add("Todos", "", false);
		
		Collection<String> ramos = home.obterNomesRamo();
		
		for(Iterator<String> i = ramos.iterator() ; i.hasNext() ; )
		{
			String ramo = i.next();
			
			select.add(ramo, ramo, ramo.equals(valor));
		}
			
		return select;
	}
}