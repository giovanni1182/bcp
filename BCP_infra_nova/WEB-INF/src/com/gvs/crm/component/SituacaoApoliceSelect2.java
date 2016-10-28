package com.gvs.crm.component;

import infra.view.Select;

public class SituacaoApoliceSelect2 extends Select 
{
	public SituacaoApoliceSelect2(String nome,String valor, boolean mostrarTodas) throws Exception
	{
		super(nome,1);
		
		if(mostrarTodas)
			this.add("[Todas]","0","0".equals(valor));
		
		this.add("Vigente","Vigente","Vigente".equals(valor));
		this.add("No Vigente","No Vigente","No Vigente".equals(valor));
		this.add("No Vigente Pendiente","No Vigente Pendiente","No Vigente Pendiente".equals(valor));
		this.add("Anulada","Anulada","Anulada".equals(valor));
	}
}