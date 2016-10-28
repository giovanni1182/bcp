package com.gvs.crm.component;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.PlanoHome;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class SituacaoPlanoSelect extends BasicView {
	private String nome;

	private String situacao;

	public SituacaoPlanoSelect(String nome, String situacao) throws Exception {
		this.nome = nome;
		this.situacao = situacao;
	}

	public View execute(User user, Locale arg1, Properties arg2)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		PlanoHome planoHome = (PlanoHome) mm.getHome("PlanoHome");
		Select select = new Select(this.nome, 1);
		select.add("[Todos]", "", false);

		for (Iterator i = planoHome.obterNomesSituacao().iterator(); i
				.hasNext();) {
			String situacao = (String) i.next();

			select.add(situacao, situacao, situacao.equals(this.situacao));
		}

		return select;
	}
}