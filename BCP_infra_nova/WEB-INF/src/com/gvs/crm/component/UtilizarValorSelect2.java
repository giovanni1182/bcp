package com.gvs.crm.component;

import infra.view.Select;

public class UtilizarValorSelect2 extends Select
{
	public UtilizarValorSelect2(String nome,String valor) throws Exception
	{
		super(nome,1);
		
		this.add("Prima","valorPrima","valorPrima".equals(valor));
		this.add("Capital","valorCapital","valorCapital".equals(valor));
		this.add("Comisión","valorComissao","valorComissao".equals(valor));
	}
}
