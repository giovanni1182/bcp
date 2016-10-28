package com.gvs.crm.component;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.PlanoHome;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class PlanoSecaoSelect extends BasicView {

	private String nome;
	private String plano;
	private Action action;

	public PlanoSecaoSelect(String nome, String plano, Action action) throws Exception
	{
		this.nome = nome;
		this.plano = plano;
		this.action = action;
	}

	public View execute(User user, Locale arg1, Properties arg2) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(user);
		PlanoHome planoHome = (PlanoHome) mm.getHome("PlanoHome");
		Select select = null;
		if(this.action!=null)
			select = new Select(this.nome, 1, this.action);
		else
			select = new Select(this.nome, 1);
		
		select.add("[Todas]", "", false);

		for (String ramo : planoHome.obterNomesSecao())
			select.add(ramo, ramo, ramo.equals(this.plano));

		return select;
	}
}