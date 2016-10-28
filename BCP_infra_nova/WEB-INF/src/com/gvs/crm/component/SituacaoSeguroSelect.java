package com.gvs.crm.component;

import infra.view.Select;

public class SituacaoSeguroSelect extends Select
{
	public SituacaoSeguroSelect(String nome, String valor) throws Exception
	{
		super(nome,1);
		this.add("[Todas]", "",false);
		
		if(valor == null)
		{
			this.add("Vigente", "Vigente",false);
			this.add("No Vigente", "No Vigente",false);
			this.add("No Vigente Pendiente", "No Vigente Pendiente", false);
			this.add("Anulada", "Anulada", false);
			
		}
		else
		{
			this.add("Vigente", "Vigente","Vigente".equals(valor));
			this.add("No Vigente", "No Vigente","No Vigente".equals(valor));
			this.add("No Vigente Pendiente", "No Vigente Pendiente", "No Vigente Pendiente".equals(valor));
			this.add("Anulada", "Anulada", "Anulada".equals(valor));
		}
	}
}
