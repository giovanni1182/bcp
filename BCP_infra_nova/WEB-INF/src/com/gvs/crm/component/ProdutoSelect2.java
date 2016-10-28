package com.gvs.crm.component;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoProduto;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class ProdutoSelect2 extends BasicView {
	private String nome;

	private String valor;

	public ProdutoSelect2(String nome, String valor) throws Exception {
		this.nome = nome;
		this.valor = valor;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Select select = new Select(this.nome, 1);
		select.add("[Todos]", "", false);

		ClassificacaoProduto cProduto = (ClassificacaoProduto) entidadeHome
				.obterEntidadePorApelido("produtos");

		for (Iterator i = cProduto.obterInferiores().iterator(); i.hasNext();) {
			Entidade entidade = (Entidade) i.next();

			select.add(entidade.obterNome(), entidade.obterNome(), entidade
					.obterNome().equals(valor));
		}
		return select;
	}
}