package com.gvs.crm.component;

import infra.view.Select;

public class SimNao2Select extends Select
{
	public SimNao2Select(String nome, String valor)
	{
		super(nome,1);
		
		this.add("No", "Não", valor.equals("Não"));
		this.add("Sí", "Sim", valor.equals("Sim"));
	}
}
