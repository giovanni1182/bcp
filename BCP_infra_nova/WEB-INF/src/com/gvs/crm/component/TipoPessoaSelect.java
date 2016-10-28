package com.gvs.crm.component;

import infra.view.Select;

public class TipoPessoaSelect extends Select {
	public TipoPessoaSelect(String nome) throws Exception {
		super(nome, 1);

		this.add("Autor", "Autor", false);
		this.add("Abogado", "Abogado", false);
		//		this.add("Outorgante", "Outorgante", false);
		//		this.add("Outorgado", "Outorgado", false);
		this.add("Juez", "Juez", false);
		this.add("Responsable", "Responsable", false);
		this.add("Sumariado", "Sumariado", false);
	}
}