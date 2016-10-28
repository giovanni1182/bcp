package com.gvs.crm.avaliador;

import infra.model.persistence.PersistentEntity;
import infra.model.persistence.PersistentHome;

public class RegraHome extends PersistentHome {
	public Regra obterRegraPorId(long id) throws Exception {
		return (Regra) this.getEntityById(id);
	}

	public Regra obterRegraPorConjunto(Conjunto conjunto) throws Exception {
		return (Regra) this.getFirstEntity(this.getEntitiesByLink(
				(Conjunto) conjunto, "primeiraRegra"));
	}

	public Regra[] obterRegrasPorConjunto(Conjunto conjunto) throws Exception {
		PersistentEntity[] entities = this.getEntitiesByLink(
				(Conjunto) conjunto, "regras");
		Regra[] regras = new Regra[entities.length];
		for (int i = 0; i < regras.length; i++)
			regras[i] = (Regra) entities[i];
		return regras;
	}

	public Regra obterProximaRegra(Regra regra) throws Exception {
		return (Regra) this.getFirstEntity(this.getEntitiesByLink(
				(Regra) regra, "proximaRegra"));
	}

	public Regra obterSuperRegra(Regra regra) throws Exception {
		return (Regra) this.getFirstEntity(this.getEntitiesByLink(
				(Regra) regra, "superRegra"));
	}

	public Regra obterSubRegraSe(Regra regra) throws Exception {
		return (Regra) this.getFirstEntity(this.getEntitiesByLink(
				(Regra) regra, "subRegraSe"));
	}

	public Regra obterSubRegraSenao(Regra regra) throws Exception {
		return (Regra) this.getFirstEntity(this.getEntitiesByLink(
				(Regra) regra, "subRegraSenao"));
	}
}