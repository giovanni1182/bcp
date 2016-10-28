package com.gvs.crm.component;

import java.util.Iterator;

import com.gvs.crm.model.AuxiliarSeguro;

import infra.view.Select;

public class RamoAuxiliarSelect extends Select {
	public RamoAuxiliarSelect(String nome, AuxiliarSeguro auxiliar, String valor)
			throws Exception {
		super(nome, 1);

		for (Iterator i = auxiliar.obterNomeRamos().iterator(); i.hasNext();) {
			String ramo = (String) i.next();

			this.add(ramo, ramo, false);
		}

	}
}