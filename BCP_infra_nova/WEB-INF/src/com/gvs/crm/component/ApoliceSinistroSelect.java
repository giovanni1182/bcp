package com.gvs.crm.component;

import infra.view.Select;

public class ApoliceSinistroSelect extends Select
{
	public ApoliceSinistroSelect(String nome, String valor) throws Exception
	{
		super(nome,1);
		
		if(!valor.equals(""))
		{
			this.add("P�lizas", "P�lizas", "P�lizas".equals(valor));
			this.add("Siniestros", "Siniestros", "Siniestros".equals(valor));
		}
		else
		{
			this.add("P�lizas", "P�lizas", false);
			this.add("Siniestros", "Siniestros", false);
		}
	}
}
