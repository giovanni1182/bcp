package com.gvs.crm.model.impl;

import java.util.Date;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.DadosPrevisao;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class DadosPrevisaoImpl extends EventoImpl implements DadosPrevisao {

	private Date dataCorte;

	private double curso;

	private double pendente;

	private double reservas;

	private double fundos;

	private double premios;

	private String tipoInstrumento;

	private double numeroEndoso;

	private double certificado;

	public void atribuirDataCorte(Date dataCorte) throws Exception {
		this.dataCorte = dataCorte;
	}

	public void atribuirCurso(double curso) throws Exception {
		this.curso = curso;
	}

	public void atribuirSinistroPendente(double pendente) throws Exception {
		this.pendente = pendente;
	}

	public void atribuirReservasMatematicas(double reservas) throws Exception {
		this.reservas = reservas;
	}

	public void atribuirFundosAcumulados(double fundos) throws Exception {
		this.fundos = fundos;
	}

	public void atribuirPremios(double premios) throws Exception {
		this.premios = premios;
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

	public void atualizarDataCorte(Date dataCorte) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_previsao set data_corte = ? where id = ?");
		update.addLong(dataCorte.getTime());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarCurso(double curso) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_previsao set curso = ? where id = ?");
		update.addDouble(curso);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarSinistroPendente(double pendente) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_previsao set pendentes = ? where id = ?");
		update.addDouble(pendente);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarReservasMatematicas(double reservas) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_previsao set reservas = ? where id = ?");
		update.addDouble(reservas);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarFundosAcumulados(double fundos) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_previsao set fundos = ? where id = ?");
		update.addDouble(fundos);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPremios(double premios) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_previsao set premios = ? where id = ?");
		update.addDouble(premios);
		update.addLong(this.obterId());

		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"crm",
						"insert into dados_previsao(id, data_corte, curso, pendentes, reservas, fundos, premios, tipo_instrumento, numero_endoso, certificado) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		if (this.dataCorte != null)
			insert.addLong(this.dataCorte.getTime());
		else
			insert.addLong(0);

		insert.addDouble(this.curso);
		insert.addDouble(this.pendente);
		insert.addDouble(this.reservas);
		insert.addDouble(this.fundos);
		insert.addDouble(this.premios);
		insert.addString(this.tipoInstrumento);
		insert.addDouble(this.numeroEndoso);
		insert.addDouble(this.certificado);

		insert.execute();
	}

	public Date obterDataCorte() throws Exception {
		if (this.dataCorte == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select data_corte from dados_previsao where id = ?");
			query.addLong(this.obterId());

			//Date data = null;

			long datalong = query.executeAndGetFirstRow().getLong("data_corte");

			if (datalong > 0)
				this.dataCorte = new Date(datalong);
		}

		return dataCorte;
	}

	public double obterCurso() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select curso from dados_previsao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("curso");
	}

	public double obterSinistroPendente() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select pendentes from dados_previsao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("pendentes");
	}

	public double obterReservasMatematicas() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select reservas from dados_previsao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("reservas");
	}

	public double obterFundosAcumulados() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select fundos from dados_previsao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("fundos");
	}

	public double obterPremios() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select premios from dados_previsao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("premios");
	}

	public void verificarDuplicidade(Apolice apolice,
			ClassificacaoContas cContas, Date dataCorte, double numeroEndoso)
			throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,dados_previsao,apolice where evento.id = dados_previsao.id and superior = ? and secao = ? and data_corte = ? and numero_endoso = ? group by evento.id");
		query.addLong(apolice.obterId());
		query.addLong(cContas.obterId());
		query.addLong(dataCorte.getTime());
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

	public String obterTipoInstrumento() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_instrumento from dados_previsao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_instrumento");
	}

	public double obterNumeroEndoso() throws Exception {
		if (this.numeroEndoso == 0) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select numero_endoso from dados_previsao where id = ?");
			query.addLong(this.obterId());

			this.numeroEndoso = query.executeAndGetFirstRow().getDouble(
					"numero_endoso");
		}

		return this.numeroEndoso;
	}

	public double obterCertificado() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select certificado from dados_previsao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("certificado");
	}
}