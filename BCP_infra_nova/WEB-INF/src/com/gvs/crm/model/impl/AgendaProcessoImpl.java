package com.gvs.crm.model.impl;

import com.gvs.crm.model.AgendaProcesso;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Produto;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class AgendaProcessoImpl extends EventoImpl implements AgendaProcesso {
	private Produto produto;

	private String status;

	public void atribuirProduto(Produto produto) throws Exception {
		this.produto = produto;
	}

	public void atribuirStatus(String status) throws Exception {
		this.status = status;
	}

	public void atualizarStatus(String status) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update agenda_processo set status=? where id=?");
		update.addString(status);
		update.addLong(this.obterId());
	}

	public void atualizarProduto(Produto produto) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update agenda_processo set produto=? where id=?");
		update.addLong(produto.obterId());
		update.addLong(this.obterId());
	}

	public String obterStatus() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select status from agenda_processo where id=?");
		query.addLong(this.obterId());

		this.status = query.executeAndGetFirstRow().getString("status");

		return this.status;
	}

	public Produto obterProduto() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select produto from agenda_processo where id=?");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("produto");

		if (id > 0) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			this.produto = (Produto) home.obterEntidadePorId(id);
		}

		return this.produto;
	}

	/*
	 * public void incluir() throws Exception { super.incluir();
	 * 
	 * SQLUpdate insert = this.getModelManager().createSQLUpdate("crm", "insert
	 * into agenda_processo (id, produto, status) values (?, ?, ?)");
	 * insert.addLong(this.obterId()); insert.addLong(this.produto.obterId());
	 * insert.addString(this.status);
	 * 
	 * insert.execute(); }
	 */
}