package com.gvs.crm.component;

import infra.view.Select;

public class AtivoCheck extends Select {
	public AtivoCheck(String name, boolean ativo) {
		super(name, 1);
		this.add("Sí", "true", ativo);
		this.add("No", "false", !ativo);
	}
}