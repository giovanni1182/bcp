package com.gvs.crm.component;

import java.util.Collection;
import java.util.Iterator;

import com.gvs.crm.model.impl.UsuarioImpl;

import infra.view.Select;

public class UsuarioNivelSelect extends Select
{
	public UsuarioNivelSelect(String nome, String valor) throws Exception
	{
		super(nome,1);
		
		Collection<String> niveis = UsuarioImpl.obterTodosOsNiveis().values();
		
		this.add("Nivel 1", "Nivel 1", false);
		
		for(Iterator<String> i = niveis.iterator() ; i.hasNext() ; )
		{
			String nivel = i.next();
			if(valor!=null)
				this.add(nivel, nivel, nivel.equals(valor));
			else
				this.add(nivel, nivel, false);
			
		}
	}
}
