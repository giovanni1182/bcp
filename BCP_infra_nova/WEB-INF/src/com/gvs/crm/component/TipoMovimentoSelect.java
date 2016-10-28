package com.gvs.crm.component;

import infra.view.Select;

public class TipoMovimentoSelect extends Select {
	public TipoMovimentoSelect(String nome, String valor) throws Exception {
		super(nome, 1);

		this.add("Contabil", "Contabil", "Contabil".equals(valor));
		this.add("Instrumento", "Instrumento", "Instrumento".equals(valor));
		//this.add("Instrumento archivo B", "Instrumento archivo B", "Instrumento archivo B".equals(valor));
		this.add("Meicos", "Meicos", "Meicos".equals(valor));
	}
}