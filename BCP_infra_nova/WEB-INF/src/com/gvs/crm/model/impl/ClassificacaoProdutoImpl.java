package com.gvs.crm.model.impl;

import java.util.Collection;

import com.gvs.crm.model.ClassificacaoProduto;
import com.gvs.crm.model.ClassificacaoProdutoHome;
import com.gvs.crm.model.ProdutoHome;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class ClassificacaoProdutoImpl extends EntidadeImpl implements
		ClassificacaoProduto {
	private String descricao;

	private Collection tabelasPreco;

	private Collection produtos;

	public void atribuirDescricao(String descricao) throws Exception {
		this.descricao = descricao;
	}

	public void atualizar() throws Exception {
		super.atualizar();

		if (this.descricao != null) {
			SQLUpdate update = this.getModelManager().createSQLUpdate(
					"update classificacao_produto set descricao=? where id=?");
			update.addString(this.descricao);
			update.addLong(this.obterId());
			update.execute();
		}
	}

	public void excluir() throws Exception {
		super.excluir();
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"delete from classificacao_produto where id=?");
		update.addLong(this.obterId());
		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"insert into classificacao_produto (id, descricao) values (?, ?)");
		update.addLong(this.obterId());
		update.addString(this.descricao);
		update.execute();
	}

	public String obterDescricao() throws Exception {
		if (this.descricao == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select descricao from classificacao_produto where id=?");
			query.addLong(this.obterId());
			this.descricao = query.executeAndGetFirstRow().getString(
					"descricao");
		}
		return this.descricao;
	}

	public Collection obterTabelasPrecos() throws Exception {
		if (this.tabelasPreco == null) {
			ClassificacaoProdutoHome classificacaoProdutoHome = (ClassificacaoProdutoHome) this
					.getModelManager().getHome("ClassificacaoProdutoHome");
			this.tabelasPreco = classificacaoProdutoHome
					.obterTabelasPreco(this);
		}
		return this.tabelasPreco;
	}

	public Collection obterProdutos() throws Exception {
		if (this.produtos == null) {
			ProdutoHome produtoHome = (ProdutoHome) this.getModelManager()
					.getHome("ProdutoHome");
			this.produtos = produtoHome
					.obterProdutosPorClassificacaoProduto(this);
		}
		return this.produtos;
	}
}