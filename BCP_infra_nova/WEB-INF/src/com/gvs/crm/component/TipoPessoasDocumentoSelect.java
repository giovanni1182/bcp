package com.gvs.crm.component;

import java.util.Iterator;

import com.gvs.crm.model.DocumentoProduto;

import infra.view.Select;

public class TipoPessoasDocumentoSelect extends Select {
	public TipoPessoasDocumentoSelect(String nome, DocumentoProduto documento,
			String valor) throws Exception {
		super(nome, 1);

		for (Iterator i = documento.obterTiposPessoas().iterator(); i.hasNext();) {
			String tipo = (String) i.next();

			this.add(tipo, tipo, false);
		}
	}
}