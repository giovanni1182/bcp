package com.gvs.crm.model.impl;

import com.gvs.crm.model.Ditame;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class DitameImpl extends EventoImpl implements Ditame {
	public void atualizarDescricao(String descricao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update ditame set descricao=? where id=?");
		update.addString(descricao);
		update.addLong(this.obterId());
		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into ditame(id) values(?)");
		insert.addLong(this.obterId());
		insert.execute();
	}

	public String obterDescricao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select descricao from ditame where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("descricao");
	}
}