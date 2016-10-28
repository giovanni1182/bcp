package com.gvs.crm.component;

import infra.view.Select;

public class TipoFormacaoSelect extends Select {
	public TipoFormacaoSelect(String nome, String valor) throws Exception {
		super(nome, 1);

		this.add("", "", false);
		this.add("Grado", "Grado", "Grado".equals(valor));
		this.add("Posgrado", "Posgrado", "Posgrado".equals(valor));
		this.add("Masterado", "Masterado", "Masterado".equals(valor));
		this.add("Doctorado", "Doctorado", "Doctorado".equals(valor));
		this.add("Otros", "Otros", "Otros".equals(valor));

	}
}