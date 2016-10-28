package com.gvs.crm.component;

import infra.view.Select;

public class DiferenciadorSelect extends Select {
	public DiferenciadorSelect(String nome, String valor) throws Exception {
		super(nome, 1);

		this.add("General", "General", "General".equals(valor));
	}
}