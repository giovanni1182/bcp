package com.gvs.crm.model;

import infra.model.ModelManager;

/**
 * Classe que representa o modelmanager para o upload/download de arquivos.
 * 
 * @author Gustavo Schmal
 */

public class SampleModelManager extends ModelManager {
	public String getModelClassName(String classAlias) {
		return "com.gvs.crm.model." + classAlias;
	}
}