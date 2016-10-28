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

public class SecaoApoliceSelect extends BasicView {

	private String nome;

	private Aseguradora aseguradora;

	private String valor;

	public SecaoApoliceSelect(String nome, Aseguradora aseguradora, String valor)
			throws Exception {
		this.nome = nome;
		this.aseguradora = aseguradora;
		this.valor = valor;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);

		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");

		Select select = new Select(this.nome, 1);

		select.add("[Todas]", "", false);

		for (Iterator i = home.obterSecaoApolice(aseguradora).iterator(); i
				.hasNext();) {
			String secao = (String) i.next();

			select.add(secao, secao, secao.equals(this.valor));
		}

		return select;
	}
}