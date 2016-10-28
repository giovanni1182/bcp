package com.gvs.crm.model.impl;

import java.util.Date;

import com.gvs.crm.model.Banco;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.MovimentacaoFinanceira;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class MovimentacaoFinanceiraImpl extends EventoImpl implements
		MovimentacaoFinanceira {

	/*
	 * Tabela para movimentação financeira
	 * 
	 * create table movimentacao_financeira ( id int(11) not null, data_prevista
	 * bigint(20), data_realizada bigint(20), condicao_prevista varchar(32),
	 * condicao_realizada varchar(32), valor_previsto double, valor_realizado
	 * double, primary key (id) ) type=innodb;
	 * 
	 * Alterar e colocar o campo (dia double)
	 *  
	 */
	private Date dataPrevista;

	private Date dataRealizada;

	private Double valorPrevisto;

	private Double valorRealizado;

	private String condicaoPrevista;

	private String condicaoRealizada;

	private Double dias;

	private Entidade conta;

	private Evento referencia;

	public void atribuirDataPrevista(Date data) throws Exception {
		this.dataPrevista = data;
	}

	public void atribuirDataRealizada(Date data) throws Exception {
		this.dataRealizada = data;
	}

	public void atribuirValorPrevisto(double valor) throws Exception {
		this.valorPrevisto = new Double(valor);
	}

	public void atribuirValorRealizado(double valor) throws Exception {
		this.valorRealizado = new Double(valor);
	}

	public void atribuirConta(Conta conta) throws Exception {
		this.conta = conta;
	}

	public void atribuirCondicaoPrevista(String condicao) throws Exception {
		this.condicaoPrevista = condicao;
	}

	public void atribuirCondicaoRealizada(String condicao) throws Exception {
		this.condicaoRealizada = condicao;
	}

	public void atribuirDias(double dias) throws Exception {
		this.dias = new Double(dias);
	}

	public void prever() throws Exception {
		this.atualizarFase(EVENTO_PREVISTO);
		// TODO Criar método para arrumar saldo da origem e destino
	}

	public void realizar(Date data, double valor, String condicao,
			String comentario) throws Exception {
		this.atualizarFase(EVENTO_REALIZADO);
		this.atualizarDataRealizada(data);
		this.atualizarValorRealizado(valor);
		this.atualizarCondicaoRealizada(condicao);
		this.adicionarComentario("Realizado por "
				+ obterUsuarioAtual().obterNome(), comentario);
		// TODO Criar método para arrumar saldo da origem e destino
	}

	public Date obterDataPrevista() throws Exception {
		if (this.dataPrevista == null) {
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery("crm",
							"select data_prevista from movimentacao_financeira where id=?");
			query.addLong(this.obterId());
			SQLRow[] rows = query.execute();
			if (rows.length == 1) {
				long data = rows[0].getLong("data_prevista");
				if (data != 0)
					this.dataPrevista = new Date(data);
			}
		}
		return this.dataPrevista;
	}

	public Date obterDataRealizada() throws Exception {
		if (this.dataRealizada == null) {
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery("crm",
							"select data_realizada from movimentacao_financeira where id=?");
			query.addLong(this.obterId());
			SQLRow[] rows = query.execute();
			if (rows.length == 1) {
				long data = rows[0].getLong("data_realizada");
				if (data != 0)
					this.dataRealizada = new Date(data);
			}
		}
		return this.dataRealizada;
	}

	public double obterValorPrevisto() throws Exception {
		if (this.valorPrevisto == null) {
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery("crm",
							"select valor_previsto from movimentacao_financeira where id=?");
			query.addLong(this.obterId());
			this.valorPrevisto = new Double(query.executeAndGetFirstRow()
					.getDouble("valor_previsto"));
		}
		return this.valorPrevisto.doubleValue();
	}

	public double obterValorRealizado() throws Exception {
		if (this.valorRealizado == null) {
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery("crm",
							"select valor_realizado from movimentacao_financeira where id=?");
			query.addLong(this.obterId());
			this.valorRealizado = new Double(query.executeAndGetFirstRow()
					.getDouble("valor_realizado"));
		}
		return this.valorRealizado.doubleValue();
	}

	public Entidade obterConta() throws Exception {
		if (this.conta == null) {
			EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager()
					.getHome("EntidadeHome");
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select conta from movimentacao_financeira where id=?");
			query.addLong(this.obterId());

			long contaId = query.executeAndGetFirstRow().getLong("conta");

			this.conta = (Entidade) entidadeHome.obterEntidadePorId(contaId);
		}
		return this.conta;
	}

	public String obterCondicaoPrevista() throws Exception {
		if (this.condicaoPrevista == null) {
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery("crm",
							"select condicao_prevista from movimentacao_financeira where id=?");
			query.addLong(this.obterId());
			this.condicaoPrevista = query.executeAndGetFirstRow().getString(
					"condicao_prevista");
		}
		return this.condicaoPrevista;
	}

	public String obterCondicaoRealizada() throws Exception {
		if (this.condicaoRealizada == null) {
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery("crm",
							"select condicao_realizada from movimentacao_financeira where id=?");
			query.addLong(this.obterId());
			this.condicaoRealizada = query.executeAndGetFirstRow().getString(
					"condicao_realizada");
		}
		return this.condicaoRealizada;
	}

	public double obterDias() throws Exception {
		if (this.dias == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select dias from movimentacao_financeira where id=?");
			query.addLong(this.obterId());
			this.dias = new Double(query.executeAndGetFirstRow().getDouble(
					"dias"));
		}
		return this.dias.doubleValue();
	}

	public void incluir() throws Exception {
		super.incluir();
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"insert into movimentacao_financeira (id) values (?)");
		update.addLong(this.obterId());
		update.execute();
		if (this.dataPrevista != null)
			atualizarDataPrevista(this.dataPrevista);
		if (this.valorPrevisto != null)
			atualizarValorPrevisto(this.valorPrevisto.doubleValue());
		if (this.condicaoPrevista != null)
			atualizarCondicaoPrevista(this.condicaoPrevista);

		this.atualizarFase(MovimentacaoFinanceira.EVENTO_PREVISTO);
	}

	public void excluir() throws Exception {
		SQLUpdate delete1 = this.getModelManager().createSQLUpdate(
				"delete from movimentacao_financeira where id=?");
		delete1.addLong(this.obterId());
		delete1.execute();
		super.excluir();
	}

	public void atualizarBanco(Banco banco) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update movimentacao_financeira set banco=? where id=?");
		update.addLong(banco.obterId());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarNumeroCheque(String numeroCheque) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update movimentacao_financeira set numero_cheque=? where id=?");
		update.addString(numeroCheque);
		update.addLong(this.obterId());

		update.execute();
	}

	public Banco obterBanco() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select banco from movimentacao_financeira where id=?");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("banco");

		Banco banco = null;

		if (id > 0) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			banco = (Banco) home.obterEntidadePorId(id);
		}

		return banco;
	}

	public String obterNumeroCheque() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select numero_cheque from movimentacao_financeira where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("numero_cheque");
	}

	public void atualizarDataPrevista(Date data) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update movimentacao_financeira set data_prevista=? where id=?");
		if (data == null)
			update.addLong(null);
		else
			update.addLong(data.getTime());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDataRealizada(Date data) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update movimentacao_financeira set data_realizada=? where id=?");
		if (data == null)
			update.addLong(null);
		else
			update.addLong(data.getTime());
		update.addLong(this.obterId());
		update.execute();

		SQLUpdate update2 = this
				.getModelManager()
				.createSQLUpdate(
						"update movimentacao_financeira set data_realizada2=? where id=?");
		if (data == null)
			update2.addDate(null);
		else
			update2.addDate(data);

		update2.addLong(this.obterId());
		update2.execute();
	}

	public void atualizarValorPrevisto(double valor) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update movimentacao_financeira set valor_previsto=? where id=?");
		update.addDouble(valor);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarValorRealizado(double valor) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update movimentacao_financeira set valor_realizado=? where id=?");
		update.addDouble(valor);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarConta(Conta conta) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update movimentacao_financeira set conta=? where id=?");
		update.addLong(conta.obterId());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarCondicaoPrevista(String condicao) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update movimentacao_financeira set condicao_prevista=? where id=?");
		update.addString(condicao);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarCondicaoRealizada(String condicao) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"update movimentacao_financeira set condicao_realizada=? where id=?");
		update.addString(condicao);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDias(double dias) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update movimentacao_financeira set dias=? where id=?");
		update.addDouble(dias);
		update.addLong(this.obterId());
		update.execute();
	}

	public boolean permiteAtualizar() throws Exception {
		if (this.obterId() == 0)
			return true;
		else
			return this.obterUsuarioAtual().equals(this.obterResponsavel())
					&& !this.obterFase().equals(EVENTO_REALIZADO);
	}

	public boolean permiteMostrarRealizado() throws Exception {
		if (this.obterId() == 0)
			return false;
		else
			return this.obterFase().equals(EVENTO_REALIZADO);
	}

	public boolean permitePrever() throws Exception {
		return this.obterUsuarioAtual().equals(this.obterResponsavel())
				&& this.obterFase().equals(EVENTO_PENDENTE);
	}

	public boolean permiteRealizar() throws Exception {
		return this.obterUsuarioAtual().equals(this.obterResponsavel())
				&& this.obterFase().equals(EVENTO_PREVISTO);
	}

	public void cancelar() throws Exception {
		this.atualizarFase(EVENTO_CANCELADO);
	}

	public boolean permiteCancelar() throws Exception {
		boolean retorno = true;
		if (this.obterFase().equals(EVENTO_REALIZADO))
			retorno = false;
		if (this.obterFase().equals(EVENTO_CONCLUIDO))
			retorno = false;
		return retorno;
	}

	public void atualizarReferencia(Evento evento) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update movimentacao_financeira set referencia=? where id=?");
		update.addLong(evento.obterId());
		update.addLong(this.obterId());
		update.execute();
	}

	public Evento obterReferencia() throws Exception {
		if (this.referencia == null) {
			EventoHome eventoHome = (EventoHome) this.getModelManager()
					.getHome("EventoHome");
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery("crm",
							"select referencia from movimentacao_financeira where id=?");
			query.addLong(this.obterId());

			if (query.executeAndGetFirstRow().getLong("referencia") > 0)
				this.referencia = eventoHome.obterEventoPorId(query
						.executeAndGetFirstRow().getLong("referencia"));
		}
		return this.referencia;
	}
}