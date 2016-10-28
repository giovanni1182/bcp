package com.gvs.crm.component;

import infra.view.Select;

public class TipoFilialSelect extends Select {
	public TipoFilialSelect(String nome, String valor) throws Exception {
		super(nome, 1);

		this.add("", "", false);
		this.add("Agencia", "Agencia", "Agencia".equals(valor));
		this.add("Sucursal", "Sucursal", "Sucursal".equals(valor));
	}
}