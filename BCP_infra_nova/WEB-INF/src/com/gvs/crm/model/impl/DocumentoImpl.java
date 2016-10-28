package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.gvs.crm.model.Documento;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class DocumentoImpl extends EventoImpl implements Documento {
	public void atualizarDataAgenda(Date dataAgenda) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update documento set data_agenda=? where id=?");
		update.addLong(dataAgenda.getTime());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDiferenciador(String diferenciador) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update documento set diferenciador=? where id=?");
		update.addString(diferenciador);
		update.addLong(this.obterId());
		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into documento(id) values(?)");
		insert.addLong(this.obterId());
		insert.execute();
	}

	public Date obterDataAgenda() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_agenda from documento where id=?");
		query.addLong(this.obterId());

		Date data = null;

		if (query.executeAndGetFirstRow().getLong("data_agenda") > 0)
			data = new Date(query.executeAndGetFirstRow()
					.getLong("data_agenda"));

		return data;
	}

	public String obterDiferenciador() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select diferenciador from documento where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("diferenciador");
	}

	public void adicionarNovoRamo(String ramo) throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select MAX(id) as MX from documento_ramo where id=?");
		query.addLong(this.obterId());

		int id = query.executeAndGetFirstRow().getInt("MX") + 1;

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into documento_ramo(id, seq, nome) values (?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addInt(id);
		insert.addString(ramo);
		insert.execute();
	}

	public void atualizarRamo(String ramo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update documento set ramo=? where id=?");
		update.addString(ramo);
		update.addLong(this.obterId());
		update.execute();
	}

	public String obterRamo() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select ramo from documento where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("ramo");
	}

	public Collection obterNomeRamos() throws Exception {
		Collection nomeRamos = new ArrayList();

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select nome from documento_ramo group by nome");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++)
			nomeRamos.add(rows[i].getString("nome"));

		return nomeRamos;
	}
}