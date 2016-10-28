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

public class PlanoSelect extends BasicView
{
	private String nome,secao,modalidade2;
	private Action action;
	private boolean todas;
	
	public PlanoSelect(String nome, String secao, String modalidade, Action action, boolean todas) throws Exception
	{
		this.nome = nome;
		this.secao = secao;
		this.modalidade2 = modalidade;
		this.action = action;
		this.todas = todas;
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
		
		if(todas)
			select.add("[Todas]", "", false);
		else
			select.add("", "", false);

		if(secao!=null)
		{
			Collection<String> secoes = planoHome.obterModalidadePorSecao(secao);
			
			for (Iterator<String> i = secoes.iterator(); i.hasNext();)
			{
				String modalidade = i.next();
	
				if(modalidade2!=null)
					select.add(modalidade, modalidade, modalidade.equals(modalidade2));
				else
					select.add(modalidade, modalidade, false);
			}
		}

		return select;
	}
}