package com.gvs.crm.component;

import com.gvs.crm.model.Usuario;

import infra.view.Select;

public class SimNaoSelect3 extends Select
{
	public SimNaoSelect3(String nome, int valor, Usuario usuarioAtual)
	{
		super(nome,1);
		
		this.add("No", 0, valor == 0);
		this.add("Sí", 1, valor == 1);
		//if(usuarioAtual.obterId() == 1)
			//this.add("Modificado", 2, valor == 2);
			
	}
}

