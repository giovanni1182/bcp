package com.gvs.crm.component;

import infra.view.Select;

public class TipoForumSelect extends Select {
	public TipoForumSelect(String nome, String valor) throws Exception {
		super(nome, 1);

		this.add("", "", false);
		this.add("Normal", "Normal", "Normal".equals(valor));
		this.add("Urgente", "Urgente", "Urgente".equals(valor));
	}
}