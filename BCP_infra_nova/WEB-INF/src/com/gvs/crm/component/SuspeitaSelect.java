package com.gvs.crm.component;

import infra.view.Select;

public class SuspeitaSelect extends Select {
	public SuspeitaSelect(String nome, long valor) throws Exception {
		super(nome, 1);

		this.add("[Todas]", 0, 0 == valor);
		this.add("> U$ 10.000 Vigente", 1, 1 == valor);
		this.add("> U$ 10.000 Siniestro", 2, 2 == valor);
		this.add("> U$ 10.000 Anulación", 3, 3 == valor);
	}
}