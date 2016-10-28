package com.gvs.crm.model.impl;

import com.gvs.crm.model.RatioUmAno;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class RatioUmAnoImpl extends EventoImpl implements RatioUmAno {

	private double primasDiretas;

	private double primasAceitas;

	private double primasCedidas;

	private double anulacaoPrimasDiretas;

	private double anulacaoPrimasAtivas;

	private double anulacaoPrimasCedidas;

	public void atribuirPrimasDiretas(double valor) throws Exception {
		this.primasDiretas = valor;
	}

	public void atribuirPrimasAceitas(double valor) throws Exception {
		this.primasAceitas = valor;
	}

	public void atribuirPrimasCedidas(double valor) throws Exception {
		this.primasCedidas = valor;
	}

	public void atribuirAnulacaoPrimasDiretas(double valor) throws Exception {
		this.anulacaoPrimasDiretas = valor;
	}

	public void atribuirAnulacaoPrimasAtivas(double valor) throws Exception {
		this.anulacaoPrimasAtivas = valor;
	}

	public void atribuirAnulacaoPrimasCedidas(double valor) throws Exception {
		this.anulacaoPrimasCedidas = valor;
	}

	public void atualizarPrimasDiretas(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update ratio_um_ano set primas_diretas = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPrimasAceitas(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update ratio_um_ano set primas_aceitas = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPrimasCedidas(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update ratio_um_ano set primas_cedidas = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarAnulacaoPrimasDiretas(double valor) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update ratio_um_ano set anulacao_primas_diretas = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarAnulacaoPrimasAtivas(double valor) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update ratio_um_ano set anulacao_primas_ativas = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarAnulacaoPrimasCedidas(double valor) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update ratio_um_ano set anulacao_primas_cedidas = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate(
				"insert into ratio_um_ano values(?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addDouble(this.primasDiretas);
		insert.addDouble(this.primasAceitas);
		insert.addDouble(this.primasCedidas);
		insert.addDouble(this.anulacaoPrimasDiretas);
		insert.addDouble(this.anulacaoPrimasAtivas);
		insert.addDouble(this.anulacaoPrimasCedidas);

		insert.execute();
	}

	public double obterPrimasDiretas() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select primas_diretas from ratio_um_ano where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("primas_diretas");
	}

	public double obterPrimasAceitas() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select primas_aceitas from ratio_um_ano where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("primas_aceitas");
	}

	public double obterPrimasCedidas() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select primas_cedidas from ratio_um_ano where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("primas_cedidas");
	}

	public double obterAnulacaoPrimasDiretas() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select anulacao_primas_diretas from ratio_um_ano where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble(
				"anulacao_primas_diretas");
	}

	public double obterAnulacaoPrimasAtivas() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select anulacao_primas_ativas from ratio_um_ano where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow()
				.getDouble("anulacao_primas_ativas");
	}

	public double obterAnulacaoPrimasCedidas() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select anulacao_primas_cedidas from ratio_um_ano where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble(
				"anulacao_primas_cedidas");
	}
}