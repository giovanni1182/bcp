package com.gvs.crm.model.impl;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Refinacao;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class RefinacaoImpl extends EventoImpl implements Refinacao {

	private double financiamentoGs;

	private String tipoMoedaFinanciamentoGs;

	private double financiamentoMe;

	private int qtdeParcelas;

	private String tipoInstrumento;

	private double numeroEndoso;

	private double certificado;

	public void atribuirFinanciamentoGs(double valor) throws Exception {
		this.financiamentoGs = valor;
	}

	public void atribuirTipoMoedaFinanciamentoGs(String tipo) throws Exception {
		this.tipoMoedaFinanciamentoGs = tipo;
	}

	public void atribuirFinanciamentoMe(double valor) throws Exception {
		this.financiamentoMe = valor;
	}

	public void atribuirQtdeParcelas(int qtde) throws Exception {
		this.qtdeParcelas = qtde;
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

	public void atualizarFinanciamentoGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update refinacao set financiamente_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();

	}

	public void atualizarTipoMoedaFinanciamentoGs(String tipo) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update refinacao set tipo_moeda_financiamente_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarFinanciamentoMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update refinacao set financiamente_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarQtdeParcelas(int qtde) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update refinacao set parcelas = ? where id = ?");
		update.addInt(qtde);
		update.addLong(this.obterId());

		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into refinacao values(?, ?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addDouble(this.financiamentoGs);
		insert.addString(this.tipoMoedaFinanciamentoGs);
		insert.addDouble(this.financiamentoMe);
		insert.addInt(this.qtdeParcelas);
		insert.addString(this.tipoInstrumento);
		insert.addDouble(this.numeroEndoso);
		insert.addDouble(this.certificado);

		insert.execute();
	}

	public double obterFinanciamentoGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select financiamente_gs from refinacao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("financiamente_gs");
	}

	public String obterTipoMoedaFinanciamentoGs() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select tipo_moeda_financiamente_gs from refinacao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString(
				"tipo_moeda_financiamente_gs");
	}

	public double obterFinanciamentoMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select financiamente_me from refinacao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("financiamente_me");
	}

	public int obterQtdeParcelas() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select parcelas from refinacao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getInt("parcelas");
	}

	public String obterTipoInstrumento() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_instrumento from refinacao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_instrumento");
	}

	public double obterNumeroEndoso() throws Exception {
		if (this.numeroEndoso == 0) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select numero_endoso from refinacao where id = ?");
			query.addLong(this.obterId());

			this.numeroEndoso = query.executeAndGetFirstRow().getDouble(
					"numero_endoso");
		}
		return this.numeroEndoso;
	}

	public double obterCertificado() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select certificado from refinacao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("certificado");
	}

	public void verificarDuplicidade(Apolice apolice,
			ClassificacaoContas cContas, double numeroEndoso) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,apolice where superior = ? and secao = ? and classe = ? numero_endoso = ? group by evento.id");
		query.addLong(apolice.obterId());
		query.addLong(cContas.obterId());
		query.addString(this.obterClasse());
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