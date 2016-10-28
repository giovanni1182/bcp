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

public class ProdutoSelect extends BasicView {
	private String nome;

	private long produtoId;

	public ProdutoSelect(String nome, long produtoId) throws Exception {
		this.nome = nome;
		this.produtoId = produtoId;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Select select = new Select(this.nome, 1);
		select.add("", "", false);

		ClassificacaoProduto cProduto = (ClassificacaoProduto) entidadeHome
				.obterEntidadePorApelido("produtos");

		for (Iterator i = cProduto.obterInferiores().iterator(); i.hasNext();) {
			Entidade entidade = (Entidade) i.next();

			select.add(entidade.obterNome(), entidade.obterId(), entidade
					.obterId() == produtoId);
		}
		return select;
	}
}