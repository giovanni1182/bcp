package com.gvs.crm.component;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.ApoliceHome;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class SituacaoApoliceSelect extends BasicView {
	private String nome;

	private Aseguradora aseguradora;

	private String valor;

	public SituacaoApoliceSelect(String nome, Aseguradora aseguradora,
			String valor) throws Exception {
		this.nome = nome;
		this.aseguradora = aseguradora;
		this.valor = valor;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);

		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");

		Select select = new Select(this.nome, 1);

		select.add("[Todos]", "", false);

		for (Iterator i = home.obterSituacoesApolice(aseguradora).iterator(); i
				.hasNext();) {
			String situacao = (String) i.next();

			select.add(situacao, situacao, situacao.equals(this.valor));
		}

		return select;
	}
}