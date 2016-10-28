package com.gvs.crm.security;

import java.io.Serializable;
import java.security.Principal;

import infra.security.User;

public class CRMUser implements User, Serializable {
	private String chave;

	CRMUser(String chave) {
		this.chave = chave;
	}

	public String getName() {
		return this.chave;
	}

	public Principal getPrincipal() {
		return null;
	}

	public boolean isUserInRole(String role) {
		return role.equals("CRMUser");
	}

	public boolean isAnonymous() {
		return this.chave == null;
	}

	public Object getExternalUser() {
		return this;
	}
}