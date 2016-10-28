package com.gvs.crm.component;

import infra.view.Select;

public class ValidacaoSelect extends Select 
{
	public ValidacaoSelect(String nome, String valor) throws Exception
	{
		super(nome,1);
		
		if(valor!=null)
		{
			this.add("Total", "Total", valor.equals("Total"));
			this.add("Parcial", "Parcial", valor.equals("Parcial"));
		}
		else
		{
			this.add("Total", "Total", false);
			this.add("Parcial", "Parcial", false);
		}
	}
}
