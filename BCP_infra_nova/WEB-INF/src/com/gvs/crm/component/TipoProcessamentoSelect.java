package com.gvs.crm.component;

import infra.view.Select;

public class TipoProcessamentoSelect extends Select
{
	public TipoProcessamentoSelect(String nome, String valor)
	{
		super(nome,1);
		
		this.add("Todos", "", false);
		this.add("Contable", "Contabil", "Contabil".equals(valor));
		this.add("Instrumento", "Instrumento", "Instrumento".equals(valor));
		this.add("Libro", "Livro", "Livro".equals(valor));
		
	}
}
