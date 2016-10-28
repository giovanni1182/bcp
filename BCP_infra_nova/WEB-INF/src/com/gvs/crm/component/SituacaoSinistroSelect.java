package com.gvs.crm.component;

import infra.view.Select;

public class SituacaoSinistroSelect extends Select 
{
	public SituacaoSinistroSelect(String nome, String tipo, boolean todos)
	{
		super(nome,1);
		
		if(todos)
			this.add("[Todos]", "0", false);
		
		this.add("Pendiente de Liquidación", "Pendiente de Liquidación", "Pendiente de Liquidación".equals(tipo));
		this.add("Controvertido", "Controvertido", "Controvertido".equals(tipo));
		this.add("Pendiente de Pago", "Pendiente de Pago", "Pendiente de Pago".equals(tipo));
		this.add("Recharzado", "Recharzado", "Recharzado".equals(tipo));
		this.add("Judicializado", "Judicializado", "Judicializado".equals(tipo));
		this.add("Pagado", "Pagado", "Pagado".equals(tipo));
		this.add("Otros", "Otros", "Otros".equals(tipo));
	}
}
