package com.gvs.crm.component;

import infra.view.Select;

public class OutrosServicosSelect extends Select {
	public OutrosServicosSelect(String nome, String valor) throws Exception {
		super(nome, 1);

		this.add("", "", false);

		this.add("Si", "Si", "Si".equals(valor));
		this.add("No", "No", "No".equals(valor));
	}
}