package com.gvs.crm.avaliador;

import infra.model.persistence.PersistentModelManager;

public class AvaliadorModelManager extends PersistentModelManager {

	public String getDatabaseResourceName() {
		return "crm";
	}

	public String getModelClassName(String classAlias) {
		return "com.gvs.crm.avaliador." + classAlias;
	}

}