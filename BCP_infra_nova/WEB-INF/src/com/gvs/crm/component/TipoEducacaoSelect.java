package com.gvs.crm.component;

import infra.view.Select;

public class TipoEducacaoSelect extends Select {
	public TipoEducacaoSelect(String nome, String valor) throws Exception {
		super(nome, 1);

		this.add("", "", false);
		this.add("Continua", "Continua", "Continua".equals(valor));
		this.add("Profisional", "Profisional", "Profisional".equals(valor));
	}
}