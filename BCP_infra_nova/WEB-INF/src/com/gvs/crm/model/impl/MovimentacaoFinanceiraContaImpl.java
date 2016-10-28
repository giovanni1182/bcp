package com.gvs.crm.model.impl;

import java.math.BigDecimal;
import java.util.Date;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.MovimentacaoFinanceiraConta;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class MovimentacaoFinanceiraContaImpl extends EventoImpl implements
		MovimentacaoFinanceiraConta {
	private Date dataPrevista;

	private Entidade conta;

	private BigDecimal saldoAtual;

	private BigDecimal saldoEstrangeiro;

	private BigDecimal saldoAnterior;

	private BigDecimal debito;

	private BigDecimal credito;

	public void atribuirDataPrevista(Date data) throws Exception {
		this.dataPrevista = data;
	}

	public void atribuirCredito(BigDecimal credito) throws Exception {
		this.credito = credito;
	}

	public void atribuirDebito(BigDecimal debito) throws Exception {
		this.debito = debito;
	}

	public void atribuirConta(Entidade conta) throws Exception {
		this.conta = conta;
	}

	public void atribuirSaldoAnterior(BigDecimal saldoAnterior) throws Exception {
		this.saldoAnterior = saldoAnterior;
	}

	public void atribuirSaldoAtual(BigDecimal saldoAtual) throws Exception {
		this.saldoAtual = saldoAtual;
	}

	public void atribuirSaldoEstrangeiro(BigDecimal saldoEstrangeiro)
			throws Exception {
		this.saldoEstrangeiro = saldoEstrangeiro;
	}

	public void atualizarDataPrevista(Date data) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update movimentacao_financeira_conta set data_prevista=? where id=?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarConta(Entidade conta) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update movimentacao_financeira_conta set conta=? where id=?");
		update.addLong(conta.obterId());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDebito(double debito) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update movimentacao_financeira_conta set debito=? where id=?");
		update.addDouble(debito);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarCredito(double credito) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update movimentacao_financeira_conta set credito=? where id=?");
		update.addDouble(credito);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarSaldoAnterior(double saldoAnterior) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update movimentacao_financeira_conta set saldo_anterior=? where id=?");
		update.addDouble(saldoAnterior);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarSaldoAtual(double saldoAtual) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update movimentacao_financeira_conta set saldo_atual=? where id=?");
		update.addDouble(saldoAtual);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarSaldoEstrangeiro(double saldoEstrangeiro)
			throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update movimentacao_financeira_conta set saldo_estrangeiro=? where id=?");
		update.addDouble(saldoEstrangeiro);
		update.addLong(this.obterId());
		update.execute();
	}

	public Date obterDataPrevista() throws Exception {
		if (this.dataPrevista == null) {
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery("crm",
							"select data_prevista from movimentacao_financeira_conta where id=?");
			query.addLong(this.obterId());
			this.dataPrevista = new Date(query.executeAndGetFirstRow().getLong(
					"data_prevista"));
		}
		return this.dataPrevista;
	}

	public Entidade obterConta() throws Exception {
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager()
				.getHome("EntidadeHome");

		if (this.conta == null) {
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery("crm",
							"select conta from movimentacao_financeira_conta where id=?");
			query.addLong(this.obterId());
			this.conta = entidadeHome.obterEntidadePorId(query
					.executeAndGetFirstRow().getLong("conta"));
		}
		return this.conta;
	}

	public BigDecimal obterDebito() throws Exception
	{
		if (this.debito == null)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select debito from movimentacao_financeira_conta where id=?");
			query.addLong(this.obterId());
			this.debito = new BigDecimal(new Double(query.executeAndGetFirstRow().getDouble("debito")));
		}
		return this.debito;
	}

	public BigDecimal obterCredito() throws Exception
	{
		if (this.credito == null)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select credito from movimentacao_financeira_conta where id=?");
			query.addLong(this.obterId());
			this.credito = new BigDecimal(new Double(query.executeAndGetFirstRow().getDouble("credito")));
		}
		return this.credito;
	}

	public BigDecimal obterSaldoAnterior() throws Exception
	{
		if (this.saldoAnterior == null)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select saldo_anterior from movimentacao_financeira_conta where id=?");
			query.addLong(this.obterId());
			this.saldoAnterior = new BigDecimal(new Double(query.executeAndGetFirstRow().getDouble("saldo_anterior")));
		}
		return this.saldoAnterior;
	}

	public BigDecimal obterSaldoAtual() throws Exception 
	{
		if (this.saldoAtual == null)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select saldo_atual from movimentacao_financeira_conta where id=?");
			query.addLong(this.obterId());
			this.saldoAtual = new BigDecimal(query.executeAndGetFirstRow().getDouble("saldo_atual"));
		}
		return this.saldoAtual;
	}

	public BigDecimal obterSaldoEstrangeiro() throws Exception
	{
		if (this.saldoEstrangeiro == null)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select saldo_estrangeiro from movimentacao_financeira_conta where id=?");
			query.addLong(this.obterId());
			this.saldoEstrangeiro = new BigDecimal(new Double(query.executeAndGetFirstRow().getDouble("saldo_estrangeiro")));
		}
		return this.saldoEstrangeiro;
	}

	public void incluir() throws Exception {
		super.incluir();
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"insert into movimentacao_financeira_conta (id, data_prevista, conta, saldo_atual, saldo_estrangeiro, debito, credito, saldo_anterior) values (?, ?, ?, ?, ?, ?, ?, ?)");
		update.addLong(this.obterId());
		update.addLong(this.dataPrevista.getTime());
		update.addLong(this.conta.obterId());
		update.addDouble(this.saldoAtual.doubleValue());
		update.addDouble(this.saldoEstrangeiro.doubleValue());
		update.addDouble(this.debito.doubleValue());
		update.addDouble(this.credito.doubleValue());
		update.addDouble(this.saldoAnterior.doubleValue());
		update.execute();
	}
}