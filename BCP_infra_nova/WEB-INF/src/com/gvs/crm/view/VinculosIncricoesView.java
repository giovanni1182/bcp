package com.gvs.crm.view;

import java.util.Iterator;

import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Inscricao;

import infra.control.Action;
import infra.view.Link;
import infra.view.Table;

public class VinculosIncricoesView extends Table {
	public VinculosIncricoesView(Inscricao inscricao) throws Exception {
		super(3);

		this.addSubtitle("");

		this.addStyle(super.STYLE_ALTERNATE);

		this.setWidth("100%");

		this.addHeader("Titulo");
		this.addHeader("Tipo del Documento");
		this.addHeader("Nº do Documento");

		for (Iterator i = inscricao.obterDocumentosVinculados().iterator(); i
				.hasNext();) {
			DocumentoProduto documento = (DocumentoProduto) i.next();

			Link link = new Link(documento.obterTitulo(), new Action(
					"visualizarEvento"));
			link.getAction().add("id", documento.obterId());

			this.add(link);

			this.add(documento.obterDocumento().obterNome());

			this.add(documento.obterNumero());
		}
	}
}