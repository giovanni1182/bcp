package com.gvs.crm.component;

import java.util.StringTokenizer;

import infra.config.InfraProperties;
import infra.view.Select;

public class MoedaSelect extends Select {
	public MoedaSelect(String nome, String moeda, String entidade_evento)
			throws Exception {
		super(nome, 1);
		StringTokenizer st = new StringTokenizer(InfraProperties.getInstance()
				.getProperty(entidade_evento + ".moedas"), ",");
		this.add("", "", false);
		while (st.hasMoreTokens()) {
			String m = st.nextToken();
			this.add(m, m, m.equals(moeda));
		}
	}
}