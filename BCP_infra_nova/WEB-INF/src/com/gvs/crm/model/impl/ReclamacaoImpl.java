package com.gvs.crm.model.impl;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Reclamacao;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class ReclamacaoImpl extends EventoImpl implements Reclamacao {
	public void atualizarApolice(Apolice apolice) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update reclamacao set apolice = ? where id = ?");
		update.addLong(apolice.obterId());
		update.addLong(this.obterId());

		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into reclamacao(id) values(?)");
		insert.addLong(this.obterId());

		insert.execute();
	}

	public Apolice obterApolice() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select apolice from reclamacao where id = ?");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("apolice");

		Apolice apolice = null;

		if (id > 0) {
			EventoHome home = (EventoHome) this.getModelManager().getHome(
					"EventoHome");

			apolice = (Apolice) home.obterEventoPorId(id);
		}

		return apolice;
	}
}