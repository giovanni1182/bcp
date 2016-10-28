package com.gvs.crm.model.impl;

import com.gvs.crm.model.Resolucao;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class ResolucaoImpl extends EventoImpl implements Resolucao {
	public void atualizarDescricao(String descricao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update resolucao set descricao=? where id=?");
		update.addString(descricao);
		update.addLong(this.obterId());
		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into resolucao(id) values(?)");
		insert.addLong(this.obterId());
		insert.execute();
	}

	public String obterDescricao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select descricao from resolucao where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("descricao");
	}
}