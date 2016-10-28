package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.gvs.crm.model.ClassificacaoProduto;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Produto;
import com.gvs.crm.model.ProdutoHome;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class ProdutoHomeImpl extends Home implements ProdutoHome {
	public Collection obterMoedas() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select moeda from produto_preco group by moeda");
		SQLRow[] rows = query.execute();
		Collection moedas = new ArrayList();
		for (int i = 0; i < rows.length; i++)
			moedas.add(rows[i].getString("moeda"));
		return moedas;
	}

	public Produto obterProdutoPorCodigoExterno(String codigoExternoOuApelido)
			throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select id from produto where codigo_externo=?");
		query.addString(codigoExternoOuApelido);
		long id = query.executeAndGetFirstRow().getLong("id");
		if (id == 0) {
			return null;
		} else {
			EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager()
					.getHome("EntidadeHome");
			return (Produto) entidadeHome.obterEntidadePorId(id);
		}
	}

	public Collection obterProdutosPorFornecedor(Entidade entidade,
			String pesquisa) throws Exception {
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager()
				.getHome("EntidadeHome");
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select entidade.id, entidade.nome from entidade,produto_fornecedor where entidade.id=produto_fornecedor.id and produto_fornecedor.fornecedor=? and entidade.nome like ?");
		query.addLong(entidade.obterId());
		query.addString("%" + pesquisa + "%");
		SQLRow[] rows = query.execute();
		Collection produtos = new ArrayList();
		for (int i = 0; i < rows.length; i++)
			produtos
					.add(entidadeHome.obterEntidadePorId(rows[i].getLong("id")));
		return produtos;
	}

	public Collection obterProdutosPorFornecedor(Entidade entidade)
			throws Exception {
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager()
				.getHome("EntidadeHome");
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select id from produto_fornecedor where fornecedor=?");
		query.addLong(entidade.obterId());
		SQLRow[] rows = query.execute();
		Collection produtos = new ArrayList();
		for (int i = 0; i < rows.length; i++)
			produtos
					.add(entidadeHome.obterEntidadePorId(rows[i].getLong("id")));
		return produtos;
	}

	public Collection obterUnidades() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select unidade from produto group by unidade");
		SQLRow[] rows = query.execute();
		Collection unidades = new ArrayList();
		for (int i = 0; i < rows.length; i++)
			unidades.add(rows[i].getString("unidade"));
		return unidades;
	}

	public Collection obterProdutosPorClassificacaoProduto(
			ClassificacaoProduto classificacaoProduto) throws Exception {
		Collection produtos = new ArrayList();
		for (Iterator i = classificacaoProduto.obterInferiores().iterator(); i
				.hasNext();) {
			Entidade e = (Entidade) i.next();
			if (e instanceof ClassificacaoProduto)
				produtos
						.addAll(this
								.obterProdutosPorClassificacaoProduto((ClassificacaoProduto) e));
			else if (e instanceof Produto)
				produtos.add(e);
		}
		return produtos;
	}

	/*
	 * public Collection obterProdutos() throws Exception { Collection produtos =
	 * new ArrayList();
	 * 
	 * SQLQuery query = this.getModelManager().createSQLQuery("crm", "select
	 * entidade.id, entidade_atributo.valor from entidade, entidade_atributo
	 * where entidade.id=entidade_atributo.entidade and classe='Produto' and
	 * entidade_atributo.nome='peso' group by entidade.id"); SQLRow[] rows =
	 * query.execute();
	 * 
	 * EntidadeHome entidadeHome = (EntidadeHome)
	 * this.getModelManager().getHome("EntidadeHome");
	 * 
	 * for (int i = 0; i < rows.length; i++)
	 * produtos.add(entidadeHome.obterEntidadePorId(rows[i].getLong("id")));
	 * 
	 * return produtos; }
	 */

}