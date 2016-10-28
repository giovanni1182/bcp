package com.gvs.crm.model.impl;

import java.util.Collection;

import com.gvs.crm.model.ClassificacaoDocumento;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class ClassificacaoDocumentoImpl extends EntidadeImpl implements
		ClassificacaoDocumento {
	private String descricao;

	private Collection tabelasPreco;

	private Collection documentos;

	public void atribuirDescricao(String descricao) throws Exception {
		this.descricao = descricao;
	}

	public void atualizar() throws Exception {
		super.atualizar();

		if (this.descricao != null) {
			SQLUpdate update = this
					.getModelManager()
					.createSQLUpdate(
							"update classificacao_documento set descricao=? where id=?");
			update.addString(this.descricao);
			update.addLong(this.obterId());
			update.execute();
		}
	}

	public void excluir() throws Exception {
		super.excluir();
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"delete from classificacao_documento where id=?");
		update.addLong(this.obterId());
		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"insert into classificacao_documento (id, descricao) values (?, ?)");
		update.addLong(this.obterId());
		update.addString(this.descricao);
		update.execute();
	}

	public String obterDescricao() throws Exception {
		if (this.descricao == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select descricao from classificacao_documento where id=?");
			query.addLong(this.obterId());
			this.descricao = query.executeAndGetFirstRow().getString(
					"descricao");
		}
		return this.descricao;
	}
}