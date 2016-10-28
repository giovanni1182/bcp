package com.gvs.crm.component;

import infra.view.Select;

public class TipoComissaoSelect extends Select {
	public TipoComissaoSelect(String nome, double valor) {
		super(nome, 1);
		this.add("[Não tem]", 0, 0 == valor);
		this.add("01%", 0.01, 0.01 == valor);
		this.add("02%", 0.02, 0.02 == valor);
		this.add("03%", 0.03, 0.03 == valor);
		this.add("04%", 0.04, 0.04 == valor);
		this.add("05%", 0.05, 0.05 == valor);
		this.add("06%", 0.06, 0.06 == valor);
		this.add("07%", 0.07, 0.07 == valor);
		this.add("08%", 0.08, 0.08 == valor);
		this.add("09%", 0.09, 0.09 == valor);
		this.add("10%", 0.10, 0.10 == valor);
	}
}