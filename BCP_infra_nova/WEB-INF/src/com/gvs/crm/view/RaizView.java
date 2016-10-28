package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import infra.security.User;
import infra.view.Space;
import infra.view.View;

public class RaizView extends EntidadeAbstrataView {
	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		return new Space();
	}
}