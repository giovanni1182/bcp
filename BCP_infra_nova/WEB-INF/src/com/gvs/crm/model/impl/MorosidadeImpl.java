package com.gvs.crm.model.impl;

import java.util.Date;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Morosidade;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class MorosidadeImpl extends EventoImpl implements Morosidade {

	private Date dataCorte;

	private int numeroParcela;

	private Date dataVencimento;

	private int diasAtraso;

	private double valorGs;

	private String tipoMoedaValorGs;

	private double valorMe;

	public void atribuirDataCorte(Date data) throws Exception {
		this.dataCorte = data;
	}

	public void atribuirNumeroParcela(int numero) throws Exception {
		this.numeroParcela = numero;
	}

	public void atribuirDataVencimento(Date data) throws Exception {
		this.dataVencimento = data;
	}

	public void atribuirDiasAtraso(int dias) throws Exception {
		this.diasAtraso = dias;
	}

	public void atribuirValorGs(double valor) throws Exception {
		this.valorGs = valor;
	}

	public void atribuirTipoMoedaValorGs(String tipo) throws Exception {
		this.tipoMoedaValorGs = tipo;
	}

	public void atribuirValorMe(double valor) throws Exception {
		this.valorMe = valor;
	}

	public void atualizarDataCorte(Date data) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update morosidade set data_corte = ? where id = ?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarNumeroParcela(int numero) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update morosidade set numero_parcela = ? where id = ?");
		update.addInt(numero);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarDataVencimento(Date data) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update morosidade set data_vencimento = ? where id = ?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarDiasAtraso(int dias) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update morosidade set dias_atraso = ? where id = ?");
		update.addInt(dias);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarValorGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update morosidade set valor_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaValorGs(String tipo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update morosidade set tipo_moeda_valor_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarValorMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update morosidade set valor_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into morosidade values(?, ?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());

		if (this.dataCorte != null)
			insert.addLong(this.dataCorte.getTime());
		else
			insert.addLong(0);

		insert.addInt(this.numeroParcela);

		if (this.dataVencimento != null)
			insert.addLong(this.dataVencimento.getTime());
		else
			insert.addLong(0);

		insert.addInt(this.diasAtraso);
		insert.addDouble(this.valorGs);
		insert.addString(this.tipoMoedaValorGs);
		insert.addDouble(this.valorMe);

		insert.execute();
	}

	public Date obterDataCorte() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_corte from morosidade where id = ?");
		query.addLong(this.obterId());

		Date data = null;

		long dataLong = query.executeAndGetFirstRow().getLong("data_corte");

		if (dataLong > 0)
			data = new Date(dataLong);

		return data;
	}

	public int obterNumeroParcela() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select numero_parcela from morosidade where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getInt("numero_parcela");
	}

	public Date obterDataVencimento() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_vencimento from morosidade where id = ?");
		query.addLong(this.obterId());

		Date data = null;

		long dataLong = query.executeAndGetFirstRow()
				.getLong("data_vencimento");

		if (dataLong > 0)
			data = new Date(dataLong);

		return data;
	}

	public int obterDiasAtraso() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select dias_atraso from morosidade where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getInt("dias_atraso");
	}

	public double obterValorGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select valor_gs from morosidade where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("valor_gs");
	}

	public String obterTipoMoedaValorGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_moeda_valor_gs from morosidade where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_moeda_valor_gs");
	}

	public double obterValorMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select valor_me from morosidade where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("valor_me");
	}

	public void verificarDuplicidade(Apolice apolice,
			ClassificacaoContas cContas, Date dataCorte) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,morosidade,apolice where evento.id = morosidade.id and superior = ? and secao = ? and data_corte = ? group by evento.id");
		query.addLong(apolice.obterId());
		query.addLong(cContas.obterId());
		query.addLong(dataCorte.getTime());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");

			EventoHome home = (EventoHome) this.getModelManager().getHome(
					"EventoHome");

			Evento e = home.obterEventoPorId(id);

			e.excluir();
		}
	}
}