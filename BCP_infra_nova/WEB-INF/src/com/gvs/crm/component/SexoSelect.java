package com.gvs.crm.component;

import infra.view.Select;

public class SexoSelect extends Select {
	public SexoSelect(String nome, String valor) {
		super(nome, 1);
		this.add("não definido", "", false);
		this.add("Feminino", "F", "F".equals(valor));
		this.add("Masculino", "M", "M".equals(valor));
	}
}