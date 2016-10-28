package com.gvs.crm.component;

import java.util.StringTokenizer;

import com.gvs.crm.model.Evento;

import infra.config.InfraProperties;
import infra.view.Select;

public class CondicaoPagamentoSelect extends Select {
	public CondicaoPagamentoSelect(String nome, String codigo, Evento evento)
			throws Exception {
		super(nome, 1);
		StringTokenizer st = new StringTokenizer(InfraProperties.getInstance()
				.getProperty(evento.obterClasse() + ".tiposdecindicao"), ",");
		while (st.hasMoreTokens()) {
			String m = st.nextToken();
			this.add(m, m, m.equals(codigo));
		}
	}
}