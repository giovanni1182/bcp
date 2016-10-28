package com.gvs.crm.component;

import infra.view.Select;

public class TipoEnvioSelect extends Select {
	public TipoEnvioSelect(String nome, String valor) throws Exception {
		super(nome, 1);

		this.add("Adicionar", "Adicionar", "Adicionar".equals(valor));
		this.add("Alterar", "Alterar", "Alterar".equals(valor));
		this.add("Excluir", "Excluir", "Excluir".equals(valor));
	}
}