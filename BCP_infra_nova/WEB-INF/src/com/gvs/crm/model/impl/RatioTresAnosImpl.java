package com.gvs.crm.model.impl;

import com.gvs.crm.model.RatioTresAnos;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class RatioTresAnosImpl extends EventoImpl implements RatioTresAnos {

	private double sinistrosPagos;

	private double gastosSinistros;

	private double sinistrosRecuperados;

	private double gastosRecuperados;

	private double recuperoSinistros;

	private double provisoes;

	public void atribuirSinistrosPagos(double valor) throws Exception {
		this.sinistrosPagos = valor;
	}

	public void atribuirGastosSinistros(double valor) throws Exception {
		this.gastosSinistros = valor;
	}

	public void atribuirSinistrosRecuperados(double valor) throws Exception {
		this.sinistrosRecuperados = valor;
	}

	public void atribuirGastosRecuperados(double valor) throws Exception {
		this.gastosRecuperados = valor;
	}

	public void atribuirRecuperoSinistros(double valor) throws Exception {
		this.recuperoSinistros = valor;
	}

	public void atribuirProvisoes(double valor) throws Exception {
		this.provisoes = valor;
	}

	public void atualizarSinistrosPagos(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update ratio_tres_anos set sinistros_pagos = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarGastosSinistros(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update ratio_tres_anos set gastos_sinistros = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarSinistrosRecuperados(double valor) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update ratio_tres_anos set sinistros_recuperados = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarGastosRecuperados(double valor) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update ratio_tres_anos set gastos_recuperados = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarRecuperoSinistros(double valor) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update ratio_tres_anos set recuperado_sinistro = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarProvisoes(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update ratio_tres_anos set provisoes = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate(
				"insert into ratio_tres_anos values(?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addDouble(this.sinistrosPagos);
		insert.addDouble(this.gastosSinistros);
		insert.addDouble(this.sinistrosRecuperados);
		insert.addDouble(this.gastosRecuperados);
		insert.addDouble(this.recuperoSinistros);
		insert.addDouble(this.provisoes);

		insert.execute();
	}

	public double obterSinistrosPagos() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select sinistros_pagos from ratio_tres_anos where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("sinistros_pagos");
	}

	public double obterGastosSinistros() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select gastos_sinistros from ratio_tres_anos where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("gastos_sinistros");
	}

	public double obterSinistrosRecuperados() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select sinistros_recuperados from ratio_tres_anos where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("sinistros_recuperados");
	}

	public double obterGastosRecuperados() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select gastos_recuperados from ratio_tres_anos where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("gastos_recuperados");
	}

	public double obterRecuperoSinistros() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select recuperado_sinistro from ratio_tres_anos where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("recuperado_sinistro");
	}

	public double obterProvisoes() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select provisoes from ratio_tres_anos where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("provisoes");
	}
}