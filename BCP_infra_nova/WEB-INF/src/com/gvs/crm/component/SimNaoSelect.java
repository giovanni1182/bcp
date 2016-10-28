package com.gvs.crm.component;

import infra.view.Select;

public class SimNaoSelect extends Select {
	public SimNaoSelect(String nome, String valor) throws Exception {
		super(nome, 1);

		this.add("Activo", "Activo", "Activo".equals(valor));
		this.add("No Activo", "No Activo", "No Activo".equals(valor));
	}
}