package com.gvs.crm.model.impl;

import java.util.Date;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.RegistroCobranca;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class RegistroCobrancaImpl extends EventoImpl implements
		RegistroCobranca {

	private Date dataCobranca;

	private Date dataVencimento;

	private int numeroParcela;

	private double valorCobrancaGs;

	private String tipoMoedaValorCobrancaGs;

	private double valorCobrancaMe;

	private double valorInteres;

	private String tipoInstrumento;

	private double numeroEndoso;

	private double certificado;

	public void atribuirDataCobranca(Date data) throws Exception {
		this.dataCobranca = data;
	}

	public void atribuirDataVencimento(Date data) throws Exception {
		this.dataVencimento = data;
	}

	public void atribuirNumeroParcela(int numero) throws Exception {
		this.numeroParcela = numero;
	}

	public void atribuirValorCobrancaGs(double valor) throws Exception {
		this.valorCobrancaGs = valor;
	}

	public void atribuirTipoMoedaValorCobrancaGs(String tipo) throws Exception {
		this.tipoMoedaValorCobrancaGs = tipo;
	}

	public void atribuirValorCobrancaMe(double valor) throws Exception {
		this.valorCobrancaMe = valor;
	}

	public void atribuirValorInteres(double valor) throws Exception {
		this.valorInteres = valor;
	}

	public void atribuirTipoInstrumento(String tipo) throws Exception {
		this.tipoInstrumento = tipo;
	}

	public void atribuirNumeroEndoso(double numeroEndoso) throws Exception {
		this.numeroEndoso = numeroEndoso;
	}

	public void atribuirCertificado(double certificado) throws Exception {
		this.certificado = certificado;
	}

	public void atualizarDataCobranca(Date data) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update registro_cobranca set data_cobranca = ? where id = ?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarDataVencimento(Date data) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update registro_cobranca set data_vencimento = ? where id = ?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarNumeroParcela(int numero) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update registro_cobranca set numero_parcela = ? where id = ?");
		update.addInt(numero);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarValorCobrancaGs(double valor) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update registro_cobranca set valor_cobranca_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaValorCobrancaGs(String tipo) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update registro_cobranca set tipo_moeda_valor_cobranca_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarValorCobrancaMe(double valor) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update registro_cobranca set valor_cobranca_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarValorInteres(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update registro_cobranca set valor_interes = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate("crm",
						"insert into registro_cobranca values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());

		if (this.dataCobranca != null)
			insert.addLong(this.dataCobranca.getTime());
		else
			insert.addLong(0);

		if (this.dataVencimento != null)
			insert.addLong(this.dataVencimento.getTime());
		else
			insert.addLong(0);

		insert.addInt(this.numeroParcela);
		insert.addDouble(this.valorCobrancaGs);
		insert.addString(this.tipoMoedaValorCobrancaGs);
		insert.addDouble(this.valorCobrancaMe);
		insert.addDouble(this.valorInteres);
		insert.addString(this.tipoInstrumento);
		insert.addDouble(this.numeroEndoso);
		insert.addDouble(this.certificado);

		insert.execute();
	}

	public Date obterDataCobranca() throws Exception {
		if (this.dataCobranca == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select data_cobranca from registro_cobranca where id = ?");
			query.addLong(this.obterId());

			long dataLong = query.executeAndGetFirstRow().getLong(
					"data_cobranca");

			if (dataLong > 0)
				this.dataCobranca = new Date(dataLong);
		}

		return this.dataCobranca;
	}

	public Date obterDataVencimento() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_vencimento from registro_cobranca where id = ?");
		query.addLong(this.obterId());

		Date data = null;

		long dataLong = query.executeAndGetFirstRow()
				.getLong("data_vencimento");

		if (dataLong > 0)
			data = new Date(dataLong);

		return data;
	}

	public int obterNumeroParcela() throws Exception {
		if (this.numeroParcela == 0) {
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery("crm",
							"select numero_parcela from registro_cobranca where id = ?");
			query.addLong(this.obterId());

			this.numeroParcela = query.executeAndGetFirstRow().getInt(
					"numero_parcela");
		}

		return this.numeroParcela;
	}

	public double obterValorCobrancaGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select valor_cobranca_gs from registro_cobranca where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("valor_cobranca_gs");
	}

	public String obterTipoMoedaValorCobrancaGs() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select tipo_moeda_valor_cobranca_gs from registro_cobranca where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString(
				"tipo_moeda_valor_cobranca_gs");
	}

	public double obterValorCobrancaMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select valor_cobranca_me from registro_cobranca where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("valor_cobranca_me");
	}

	public double obterValorInteres() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select valor_interes from registro_cobranca where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("valor_interes");
	}

	public String obterTipoInstrumento() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_instrumento from registro_cobranca where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_instrumento");
	}

	public double obterNumeroEndoso() throws Exception {
		if (this.numeroEndoso == 0) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select numero_endoso from registro_cobranca where id = ?");
			query.addLong(this.obterId());

			this.numeroEndoso = query.executeAndGetFirstRow().getDouble(
					"numero_endoso");
		}
		return this.numeroEndoso;
	}

	public double obterCertificado() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select certificado from registro_cobranca where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("certificado");
	}

	public void verificarDuplicidade(Apolice apolice,
			ClassificacaoContas cContas, Date dataCobranca, int numeroParcela,
			double numeroEndoso) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,registro_cobranca,apolice where evento.id = registro_cobranca.id and superior = ? and secao = ? and data_cobranca = ? and numero_parcela = ? and numero_endoso = ? group by evento.id");
		query.addLong(apolice.obterId());
		query.addLong(cContas.obterId());
		query.addLong(dataCobranca.getTime());
		query.addInt(numeroParcela);
		query.addDouble(numeroEndoso);

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