package com.gvs.crm.component;

import infra.view.Select;

public class OperacaoSelect extends Select {
	public OperacaoSelect(String nome, String operacao, boolean habilitado) {
		super(nome, 1);
		this.add("Ped�gio", "Ped�gio", "Ped�gio".equals(operacao));
		this.add("Combust�vel", "Combust�vel", "Combust�vel".equals(operacao));
		this.add("Refei��o", "Refei��o", "Refei��o".equals(operacao));
		this.add("Manuten��o", "Manuten��o", "Manuten��o".equals(operacao));
		this.add("Outras", "Outras", "Outras".equals(operacao));
		this.setEnabled(habilitado);
	}
}