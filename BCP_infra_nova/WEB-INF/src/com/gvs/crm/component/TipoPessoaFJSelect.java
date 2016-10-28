package com.gvs.crm.component;

import infra.view.Select;

public class TipoPessoaFJSelect extends Select 
{
	public TipoPessoaFJSelect(String nome,String valor) throws Exception
	{
		super(nome,1);
		
		this.add("[Todas]","0","0".equals(valor));
		this.add("Persona Fisica","Persona Fisica","Persona Fisica".equals(valor));
		this.add("Persona Juridica","Persona Juridica","Persona Juridica".equals(valor));
	}
}
