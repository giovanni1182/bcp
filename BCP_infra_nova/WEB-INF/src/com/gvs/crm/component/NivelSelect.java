package com.gvs.crm.component;

import infra.view.Select;

public class NivelSelect extends Select {
	public NivelSelect(String nome, String valor) throws Exception {
		super(nome, 1);
		this.add("Capítulo", "Nivel 1", "Nivel 1".equals(valor));
		this.add("Rubro", "Nivel 2", "Nivel 2".equals(valor));
		this.add("Cuenta", "Nivel 3", "Nivel 3".equals(valor));
		this.add("SubCuenta", "Nivel 4", "Nivel 4".equals(valor));
		this.add("Cuenta Imputable", "Nivel 5", "Nivel 5".equals(valor));
	}
}