package com.gvs.crm.component;

import infra.view.Select;

public class OperacaoSelect extends Select {
	public OperacaoSelect(String nome, String operacao, boolean habilitado) {
		super(nome, 1);
		this.add("Pedágio", "Pedágio", "Pedágio".equals(operacao));
		this.add("Combustível", "Combustível", "Combustível".equals(operacao));
		this.add("Refeição", "Refeição", "Refeição".equals(operacao));
		this.add("Manutenção", "Manutenção", "Manutenção".equals(operacao));
		this.add("Outras", "Outras", "Outras".equals(operacao));
		this.setEnabled(habilitado);
	}
}