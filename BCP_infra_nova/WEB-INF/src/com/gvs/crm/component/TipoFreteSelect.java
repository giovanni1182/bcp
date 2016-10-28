package com.gvs.crm.component;

import infra.view.Select;

public class TipoFreteSelect extends Select {
	public TipoFreteSelect(String nome, String valor) {
		super(nome, 1);
		this.add("Battistella / Cliente", "stella_cliente", "stella_cliente"
				.equals(valor));
		this.add("Por conta do Cliente", "cliente", "cliente".equals(valor));
		this.add("Por conta da Battistella", "stella", "stella".equals(valor));

	}
}