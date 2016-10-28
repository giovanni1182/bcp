package com.gvs.crm.component;

import java.util.Iterator;

import com.gvs.crm.model.Reaseguradora;

import infra.view.Select;

public class NivelClassificacaoSelect extends Select {
	public NivelClassificacaoSelect(String nome, Reaseguradora reaseguradora,
			String valor) throws Exception {
		super(nome, 1);

		for (Iterator i = reaseguradora.obterNiveis().iterator(); i.hasNext();) {
			String nivel = (String) i.next();

			this.add(nivel, nivel, nivel.equals(valor));
		}
	}
}