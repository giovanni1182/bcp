package com.gvs.crm.component;

import java.util.Map;

import com.gvs.crm.model.Uteis;

import infra.view.Select;

public class TipoLivroSelect extends Select
{
	public TipoLivroSelect(String nome, String valor, boolean todos)
	{
		super(nome,1);
		
		if(todos)
			this.add("Todos", "", false);
		else
			this.add("", "", false);
		
		Uteis uteis = new Uteis();
		
		Map<String,String> nomeLivros = uteis.nomeLivrosCombo();
		
		for(String nomeLivro : nomeLivros.keySet())
		{
			String nomeLivroReal = nomeLivros.get(nomeLivro);
			
			this.add(nomeLivro, nomeLivroReal, nomeLivroReal.equals(valor));
		}
	}
}
