package com.gvs.crm.avaliador;

import infra.model.persistence.PersistentEntity;
import infra.model.persistence.PersistentHome;

public class ConjuntoHome extends PersistentHome {
	public Conjunto obterConjuntoPorId(long id) throws Exception {
		return (Conjunto) this.getEntityById(id);
	}

	public Conjunto obterConjuntoPorRegra(Regra regra) throws Exception {
		return (Conjunto) this.getFirstEntity(this.getEntitiesByLink(
				(Regra) regra, "conjunto"));
	}

	public Conjunto[] obterConjuntos() throws Exception {
		PersistentEntity[] entities = this.getEntities("Conjunto");
		Conjunto[] conjuntos = new Conjunto[entities.length];
		for (int i = 0; i < entities.length; i++)
			conjuntos[i] = (Conjunto) entities[i];
		return conjuntos;
	}
}