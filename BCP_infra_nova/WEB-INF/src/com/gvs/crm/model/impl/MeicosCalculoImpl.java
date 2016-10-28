package com.gvs.crm.model.impl;

import com.gvs.crm.model.MeicosCalculo;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class MeicosCalculoImpl extends EventoImpl implements MeicosCalculo {

	private double valorIndicador;

	public void atribuirValor(double valor) throws Exception {
		this.valorIndicador = valor;
	}

	public void atualizarValorIndicador(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update meicos_calculo set valor = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into meicos_calculo(id, valor) values(?, ?)");
		insert.addLong(this.obterId());
		insert.addDouble(this.valorIndicador);

		insert.execute();

		//System.out.println("insert into meicos_calculo(id)
		// values("+this.obterId()+")");
	}

	public double obterValorIndicador() throws Exception {
		double valor = 0;

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select valor from evento,meicos_calculo where evento.id = meicos_calculo.id and meicos_calculo.id = ?");
		query.addLong(this.obterId());

		valor = query.executeAndGetFirstRow().getDouble("valor");

		return valor;
	}
}