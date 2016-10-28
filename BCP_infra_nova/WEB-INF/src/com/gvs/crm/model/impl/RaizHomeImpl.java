package com.gvs.crm.model.impl;

import com.gvs.crm.model.Raiz;
import com.gvs.crm.model.RaizHome;

import infra.model.Home;

public class RaizHomeImpl extends Home implements RaizHome {
	private Raiz raiz;

	public Raiz obterRaiz() throws Exception {
		if (this.raiz == null) {
			EntidadeHomeImpl entidadeHome = (EntidadeHomeImpl) this
					.getModelManager().getHome("EntidadeHome");
			this.raiz = (Raiz) entidadeHome.instanciarEntidade(0, "Raiz");
		}
		return this.raiz;
	}
}