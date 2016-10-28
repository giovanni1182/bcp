package com.gvs.crm.component;

import infra.view.Select;

public class IndicadoresSelect extends Select {
	public IndicadoresSelect(String nome, String tipo) throws Exception {
		super(nome, 1);

		this.add("Auditoria Externa", "Auditoria Externa", "Auditoria Externa"
				.equals(tipo));
		this.add("Inspeci�n", "Inspeci�n", "Inspeci�n".equals(tipo));
		this.add("Otros Indicadores", "Otros Indicadores", "Otros Indicadores"
				.equals(tipo));
		this.add("Controle de Documentos", "Controle de Documentos",
				"Controle de Documentos".equals(tipo));
		//this.add("Ratios", "Ratios", "Ratios".equals(tipo));
	}
}