package com.gvs.crm.component;

import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.view.Link;

public class EntidadeNomeLink extends Link {
	public EntidadeNomeLink(Entidade entidade) throws Exception {
		super(entidade.obterNome(), new Action("visualizarDetalhesEntidade"));
		this.getAction().add("id", entidade.obterId());
		this.getAction().add("_entidadeId", entidade.obterId());
	}
}