package com.gvs.crm.component;

import infra.view.Select;

public class SinalSelect extends Select {
	public SinalSelect(String nome, String valor) {
		super(nome, 1);
		this.add(">", ">", ">".equals(valor));
		this.add(">=", ">=", ">=".equals(valor));
		this.add("=", "=", "=".equals(valor));
		this.add("==", "==", "==".equals(valor));
		this.add("<=", "<=", "<=".equals(valor));
		this.add("<", "<", "<".equals(valor));
		this.add("<>", "<>", "<>".equals(valor));
		this.add("+", "+", "+".equals(valor));
	}
}