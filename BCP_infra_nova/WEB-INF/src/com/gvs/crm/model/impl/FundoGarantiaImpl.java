package com.gvs.crm.model.impl;

import com.gvs.crm.model.FundoGarantia;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class FundoGarantiaImpl extends EventoImpl implements FundoGarantia {

	private double valor;

	public void atribuirValor(double valor) throws Exception {
		this.valor = valor;
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate(
				"insert into fundo_garantia values(?, ?)");
		insert.addLong(this.obterId());
		insert.addDouble(this.valor);

		insert.execute();
	}

	public double obterValor() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select valor from fundo_garantia where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("valor");
	}
}