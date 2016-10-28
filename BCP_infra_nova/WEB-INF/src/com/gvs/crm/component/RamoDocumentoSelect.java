package com.gvs.crm.component;

import java.util.Iterator;

import com.gvs.crm.model.Documento;

import infra.view.Select;

public class RamoDocumentoSelect extends Select {
	public RamoDocumentoSelect(String nome, Documento documento, String valor)
			throws Exception {
		super(nome, 1);

		for (Iterator i = documento.obterNomeRamos().iterator(); i.hasNext();) {
			String ramo = (String) i.next();

			this.add(ramo, ramo, ramo.equals(documento.obterRamo()));
		}

	}
}