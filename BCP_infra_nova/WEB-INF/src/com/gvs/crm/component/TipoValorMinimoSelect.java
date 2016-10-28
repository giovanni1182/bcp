package com.gvs.crm.component;

import infra.view.Select;

public class TipoValorMinimoSelect extends Select {
	public TipoValorMinimoSelect(String nome, String valor) throws Exception {
		super(nome, 1);

		this.add("Vida", "Vida", "Vida".equals(valor));
		this.add("Patrimonial", "Patrimonial", "Patrimonial".equals(valor));
		this.add("Vida/Patrimonial", "Vida/Patrimonial", "Vida/Patrimonial"
				.equals(valor));
	}
}