package com.gvs.crm.component;

import infra.view.Select;

public class RelEntidadesVigentesSelect extends Select
{
	public RelEntidadesVigentesSelect(String nome, int valor,boolean ci) throws Exception
	{
		super(nome,1);
		
		this.add("Agentes de Seguros", 1, 1 == valor);
		this.add("Auditores Externos", 5, 5 == valor);
		this.add("Corredores de Seguros", 2, 2 == valor);
		this.add("Corredores de Reaseguros", 6, 6 == valor);
		if(!ci)
			this.add("Empresas Reaseguradoras del Exterior", 3, 3 == valor);
		this.add("Liquidadores de Siniestro", 4, 4 == valor);
		if(!ci)
			this.add("Reaseguradoras - Calificación", 7, 7 == valor);
		
		this.add("Aseguradoras", 8, 8 == valor);
	}
}
