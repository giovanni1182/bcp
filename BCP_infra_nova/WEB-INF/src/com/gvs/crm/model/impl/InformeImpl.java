package com.gvs.crm.model.impl;

import com.gvs.crm.model.Informe;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class InformeImpl extends EventoImpl implements Informe {
	public void atualizarDescricao(String descricao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update informe set descricao=? where id=?");
		update.addString(descricao);
		update.addLong(this.obterId());
		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into informe(id) values(?)");
		insert.addLong(this.obterId());
		insert.execute();
	}

	public String obterDescricao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select descricao from informe where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("descricao");
	}
}