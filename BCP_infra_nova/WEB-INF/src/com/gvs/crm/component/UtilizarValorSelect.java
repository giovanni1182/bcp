package com.gvs.crm.component;

import infra.view.Select;

public class UtilizarValorSelect extends Select 
{
	public UtilizarValorSelect(String nome,String valor) throws Exception
	{
		super(nome,1);
		
		this.add("Prima","valorPrima","valorPrima".equals(valor));
		this.add("Capital en Riesgo","valorCapital","valorCapital".equals(valor));
		this.add("Siniestro","valorSinistro","valorSinistro".equals(valor));
	}
}
