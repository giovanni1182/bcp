package com.gvs.crm.component;

import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.PlanoHome;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class PlanoSecao2Select extends BasicView {

	private String nome,ramo,secao2;
	private Action action;

	public PlanoSecao2Select(String nome, String ramo, String secao, Action action) throws Exception
	{
		this.nome = nome;
		this.ramo = ramo;
		this.secao2 = secao;
		this.action = action;
	}

	public View execute(User user, Locale arg1, Properties arg2) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(user);
		PlanoHome planoHome = (PlanoHome) mm.getHome("PlanoHome");
		Select select = null;
		if(action!=null)
			select = new Select(this.nome, 1, action);
		else
			select = new Select(this.nome, 1);
		
		select.add("[Todas]", "", false);

		if(ramo!=null)
		{
			Collection<String> secoes = planoHome.obterSecoesPorRamo(ramo);
			
			for (String secao : secoes)
			{
				if(secao2!=null)
					select.add(secao, secao, secao.equals(secao2));
				else
					select.add(secao, secao, false);
			}
		}

		return select;
	}
}