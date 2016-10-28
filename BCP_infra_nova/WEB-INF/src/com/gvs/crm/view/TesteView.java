package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class TesteView extends PortalView {

	private Collection colecao;

	private int pagina;

	public TesteView(Collection colecao, int pagina) throws Exception {
		this.colecao = colecao;
		this.pagina = pagina;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		if (pagina <= 0)
			pagina = 1;

		int anterior = pagina - 1;
		int proximo = pagina + 1;

		Table table = new Table(5);

		table.addStyle(Table.STYLE_ALTERNATE);

		table.setWidth("100%");

		//MaskFormatter testeMask = new MaskFormatter("#########-##");

		//JFormattedTextField testeCampo = new JFormattedTextField(testeMask);

		for (Iterator i = colecao.iterator(); i.hasNext();) {
			AgendaMovimentacao agenda = (AgendaMovimentacao) i.next();

			table.add(new Label(agenda.obterId()));

			table.add(agenda.obterTitulo());

			table.add(agenda.obterTipo());

			String data = new SimpleDateFormat("dd/MM/yyyy").format(agenda
					.obterCriacao());

			table.add(data);

			table.add(agenda.obterFase().obterNome());

		}

		Button anteriorLink = new Button("<<< Anterior", new Action(
				"testeAction"));
		anteriorLink.getAction().add("_pagina", anterior);

		Button proximoLink = new Button("Próxima >>>",
				new Action("testeAction"));
		proximoLink.getAction().add("_pagina", proximo);

		if (anterior > 0)
			table.addFooter(anteriorLink);

		if (this.colecao.size() >= 20)
			table.addFooter(proximoLink);

		return table;
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception {
		return null;
	}

	public Entidade getOrigemMenu() throws Exception {
		return null;
	}
}