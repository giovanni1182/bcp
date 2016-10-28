package com.gvs.crm.component;

import infra.view.Select;

public class AcaoSelect extends Select {
	public AcaoSelect(String nome, String valor) throws Exception {
		super(nome, 1);
		this.add("Start", "Start", "Start".equals(valor));
		this.add("Update", "Update", "Update".equals(valor));
		this.add("Delete", "Delete", "Delete".equals(valor));
	}
}