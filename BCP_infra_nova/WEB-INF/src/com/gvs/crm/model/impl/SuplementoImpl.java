package com.gvs.crm.model.impl;

import java.util.Date;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Suplemento;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class SuplementoImpl extends EventoImpl implements Suplemento {

	private String numero;

	private Date dataEmissao;

	private String razao;

	private double primaGs;

	private String tipoMoedaPrimaGs;

	private double primaMe;

	private String tipoInstrumento;

	private double numeroEndoso;

	private double certificado;

	public void atribuirNumero(String numero) throws Exception {
		this.numero = numero;
	}

	public void atribuirDataEmissao(Date data) throws Exception {
		this.dataEmissao = data;
	}

	public void atribuirRazao(String numero) throws Exception {
		this.razao = numero;
	}

	public void atribuirPrimaGs(double valor) throws Exception {
		this.primaGs = valor;
	}

	public void atribuirTipoMoedaPrimaGs(String tipo) throws Exception {
		this.tipoMoedaPrimaGs = tipo;
	}

	public void atribuirPrimaMe(double valor) throws Exception {
		this.primaMe = valor;
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

	public void atualizarNumero(String numero) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update suplemento set numero = ? where id = ?");
		update.addString(numero);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarDataEmissao(Date data) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update suplemento set data_emissao = ? where id = ?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarRazao(String razao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update suplemento set razao = ? where id = ?");
		update.addString(razao);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPrimaGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update suplemento set prima_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaPrimaGs(String tipo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update suplemento set tipo_moeda_prima_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPrimaMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update suplemento set prima_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert suplemento values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addString(this.numero);

		if (this.dataEmissao != null)
			insert.addLong(this.dataEmissao.getTime());
		else
			insert.addLong(0);

		insert.addString(this.razao);
		insert.addDouble(this.primaGs);
		insert.addString(this.tipoMoedaPrimaGs);
		insert.addDouble(this.primaMe);
		insert.addString(this.tipoInstrumento);
		insert.addDouble(this.numeroEndoso);
		insert.addDouble(this.certificado);

		insert.execute();
	}

	public String obterNumero() throws Exception {
		if (this.numero == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select numero from suplemento where id = ?");
			query.addLong(this.obterId());

			this.numero = query.executeAndGetFirstRow().getString("numero");
		}

		return this.numero;
	}

	public Date obterDataEmissao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_emissao from suplemento where id = ?");
		query.addLong(this.obterId());

		Date data = null;

		long dataLong = query.executeAndGetFirstRow().getLong("data_emissao");

		if (dataLong > 0)
			data = new Date(dataLong);

		return data;

	}

	public String obterRazao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select razao from suplemento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("razao");
	}

	public double obterPrimaGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select prima_gs from suplemento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("prima_gs");
	}

	public String obterTipoMoedaPrimaGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_moeda_prima_gs from suplemento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_moeda_prima_gs");
	}

	public double obterPrimaMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select prima_me from suplemento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("prima_me");
	}

	public String obterTipoInstrumento() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_instrumento from suplemento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_instrumento");
	}

	public double obterNumeroEndoso() throws Exception {
		if (this.numeroEndoso == 0) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select numero_endoso from suplemento where id = ?");
			query.addLong(this.obterId());

			this.numeroEndoso = query.executeAndGetFirstRow().getDouble(
					"numero_endoso");
		}
		return this.numeroEndoso;
	}

	public double obterCertificado() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select certificado from suplemento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("certificado");
	}

	public void verificarDuplicidade(Apolice apolice,
			ClassificacaoContas cContas, String numeroEndoso) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,suplemento,apolice where evento.id = suplemento.id and superior = ? and secao = ? and numero = ? group by evento.id");
		query.addLong(apolice.obterId());
		query.addLong(cContas.obterId());
		query.addString(numeroEndoso);

		SQLRow[] rows = query.execute();

		System.out
				.println("select evento.id from evento,suplemento,apolice where evento.id = suplemento.id and superior = "
						+ apolice.obterId()
						+ " and secao = "
						+ cContas.obterId()
						+ " and numero = "
						+ numeroEndoso
						+ " group by evento.id");

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");

			EventoHome home = (EventoHome) this.getModelManager().getHome(
					"EventoHome");

			Evento e = home.obterEventoPorId(id);

			e.excluir();
		}
	}
}