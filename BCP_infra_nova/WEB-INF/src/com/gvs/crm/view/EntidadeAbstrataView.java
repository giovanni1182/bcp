package com.gvs.crm.view;

import com.gvs.crm.model.Entidade;

import infra.view.BasicView;

public abstract class EntidadeAbstrataView extends BasicView
{
	private Entidade entidade;

	private Entidade origemMenu;

	public void atribuirEntidade(Entidade entidade)
	{
		this.entidade = entidade;
	}

	public void atribuirEntidade(Entidade entidade, Entidade origemMenu)
	{
		this.entidade = entidade;
		this.origemMenu = origemMenu;
	}

	public Entidade obterEntidade()
	{
		return this.entidade;
	}

	public Entidade obterOrigemMenu()
	{
		return this.origemMenu;
	}
}