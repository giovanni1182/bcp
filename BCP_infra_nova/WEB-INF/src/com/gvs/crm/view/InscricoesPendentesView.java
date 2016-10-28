package com.gvs.crm.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Inscricao;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Check;
import infra.view.Image;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class InscricoesPendentesView extends Table {
	public InscricoesPendentesView(DocumentoProduto documento, User user)
			throws Exception {
		super(3);

		CRMModelManager mm = new CRMModelManager(user);
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		this.addSubtitle("");
		this.addStyle(super.STYLE_ALTERNATE);

		this.setNextColSpan(this.getColumns());
		this.addHeader("Inscriciones Vinculadas");

		for (Iterator i = documento.obterInscricoesVinculadas().iterator(); i
				.hasNext();) {
			Inscricao inscricao = (Inscricao) i.next();

			Link link = new Link(new Image("delete.gif"), new Action(
					"excluirDocumentoVinculado"));
			link.getAction().add("id", documento.obterId());
			link.getAction().add("inscricaoId", inscricao.obterId());

			this.add(link);

			Link link2 = new Link(inscricao.obterTitulo(), new Action(
					"visualizarEvento"));
			link2.getAction().add("id", inscricao.obterId());
			link2.getAction().add("_pasta", 1);

			this.add(link2);

			this.add(inscricao.obterOrigem().obterNome());
		}

		this.setNextColSpan(this.getColumns());
		this.add(new Space());

		this.setNextColSpan(this.getColumns());
		this.addHeader("Inscriciones Pendentes");

		Collection inscricoesPendentes = new ArrayList();

		inscricoesPendentes = eventoHome.obterInscricoesPendentes();
		inscricoesPendentes.removeAll(documento.obterInscricoesVinculadas());

		for (Iterator i = inscricoesPendentes.iterator(); i.hasNext();) {
			Inscricao inscricao = (Inscricao) i.next();

			Check check = new Check("check_" + inscricao.obterId(), "check_"
					+ inscricao.obterId(), false);
			this.add(check);

			this.add(inscricao.obterTitulo());

			if (inscricao.obterOrigem() != null)
				this.add(inscricao.obterOrigem().obterNome());
			else
				this.add("Sem Origem");
		}

		Button vincularButton = new Button("Vincular", new Action(
				"vincularDocumento"));
		vincularButton.getAction().add("id", documento.obterId());

		this.addFooter(vincularButton);
	}
}