package com.gvs.crm.component;

import infra.view.Select;

public class GrupoSelect extends Select 
{
	public GrupoSelect(String nome, String valor) 
	{
		super(nome,1);
		
		this.add("", "", false);
		this.add("Capital mínimo", "Capital mínimo", "Capital mínimo".equals(valor));
		this.add("Primas", "Primas", "Primas".equals(valor));
		this.add("Siniestros", "Siniestros", "Siniestros".equals(valor));
		
		/*this.add("Calidad de los activos", "Calidad de los activos", "Calidad de los activos".equals(valor));
		this.add("Ganancia", "Ganancia", "Ganancia".equals(valor));
		this.add("Gestión", "Gestión", "Gestión".equals(valor));
		this.add("Reaseguro y reservas", "Reaseguro y reservas", "Reaseguro y reservas".equals(valor));
		this.add("Suficiencia de capital", "Suficiencia de capital", "Suficiencia de capital".equals(valor));*/
	}
}
