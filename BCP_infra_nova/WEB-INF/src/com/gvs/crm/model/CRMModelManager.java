package com.gvs.crm.model;

import java.util.HashMap;

import infra.config.InfraProperties;
import infra.model.Home;
import infra.model.ModelManager;
import infra.security.User;

public class CRMModelManager extends ModelManager {
	private HashMap homes = new HashMap();

	public CRMModelManager(User user) {
		super(user);
	}

	public String getModelClassName(String classAlias) {
		try {
			return InfraProperties.getInstance().getProperty(
					classAlias.toLowerCase().trim() + ".classe");
		} catch (Exception e) {
			return null;
		}
	}

	public Home getHome(String homeAlias) throws Exception {
		Home home = (Home) homes.get(homeAlias);
		if (home == null) {
			home = super.getHome(homeAlias);
			this.homes.put(homeAlias, home);
		}
		return home;
	}

	public void beginTransaction() throws Exception {
		this.beginTransaction("crm");
	}
}