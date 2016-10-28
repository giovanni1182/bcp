package com.gvs.crm.component;

import infra.view.Select;

public class NotificacaoSelect extends Select
{
	public NotificacaoSelect(String nome, int valor)
	{
		super(nome,1);
		
		this.add("Todas", -1, false);
		this.add("Recibimiento", 0, 0 == valor);
		this.add("Error", 1, 1 == valor);
	}
}
