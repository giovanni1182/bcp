package com.gvs.crm.component;

import infra.view.Select;

public class StatusInscricaoSelect extends Select {
	public StatusInscricaoSelect(String nome, String valor) throws Exception {
		super(nome, 1);

		this.add("Status - [Todos]", "", false);
		this.add("Pendiente", "Pendiente", "Pendiente".equals(valor));
		this.add("Vigente", "Vigente", "Vigente".equals(valor));
		this.add("No Vigente", "No Vigente", "No Vigente".equals(valor));
		this.add("Concluida", "Concluida", "Concluida".equals(valor));
	}
}