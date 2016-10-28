package com.gvs.crm.component;

import java.util.Iterator;

import com.gvs.crm.model.Inscricao;

import infra.view.Select;

public class RamoInscricaoSelect extends Select {
	public RamoInscricaoSelect(String nome, Inscricao inscricao, String valor)
			throws Exception {
		super(nome, 1);

		this.add("", "", false);

		for (Iterator i = inscricao.obterNomeRamos().iterator(); i.hasNext();) {
			String ramo = (String) i.next();

			this.add(ramo, ramo, ramo.equals(inscricao.obterRamo()));
		}

	}
}