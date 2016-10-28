package com.gvs.crm.model.impl;

import java.util.Date;

import com.gvs.crm.model.AnulacaoInstrumento;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class AnulacaoInstrumentoImpl extends EventoImpl implements
		AnulacaoInstrumento {

	private Date dataAnulacao;

	private double capitalGs;

	private String tipoMoedaCapitalGs;

	private double capitalMe;

	private String solicitadoPor;

	private int diasCorridos;

	private double primaGs;

	private String tipoMoedaPrimaGs;

	private double primaMe;

	private double comissaoGs;

	private String tipoMoedaComissaoGs;

	private double comissaoMe;

	private double comissaoRecuperarGs;

	private String tipoMoedaComissaoRecuperarGs;

	private double comissaoRecuperarMe;

	private double saldoAnulacaoGs;

	private String tipoMoedaSaldoAnulacaoGs;

	private double saldoAnulacaoMe;

	private String destinoSaldoAnulacao;

	private String tipoInstrumento;

	private double numeroEndoso;

	private double certificado;

	public void atribuirDataAnulacao(Date data) throws Exception 
	{
		this.dataAnulacao = data;
		
	}

	public void atribuirCapitalGs(double valor) throws Exception {
		this.capitalGs = valor;
	}

	public void atribuirTipoMoedaCapitalGs(String tipo) throws Exception {
		this.tipoMoedaCapitalGs = tipo;
	}

	public void atribuirCapitalMe(double valor) throws Exception {
		this.capitalMe = valor;
	}

	public void atribuirSolicitadoPor(String valor) throws Exception {
		this.solicitadoPor = valor;
	}

	public void atribuirDiasCorridos(int dias) throws Exception {
		this.diasCorridos = dias;
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

	public void atribuirComissaoGs(double valor) throws Exception {
		this.comissaoGs = valor;
	}

	public void atribuirTipoMoedaComissaoGs(String tipo) throws Exception {
		this.tipoMoedaComissaoGs = tipo;
	}

	public void atribuirComissaoMe(double valor) throws Exception {
		this.comissaoMe = valor;
	}

	public void atribuirComissaoRecuperarGs(double valor) throws Exception {
		this.comissaoRecuperarGs = valor;
	}

	public void atribuirTipoMoedaComissaoRecuperarGs(String tipo)
			throws Exception {
		this.tipoMoedaComissaoRecuperarGs = tipo;
	}

	public void atribuirComissaoRecuperarMe(double valor) throws Exception {
		this.comissaoRecuperarMe = valor;
	}

	public void atribuirSaldoAnulacaoGs(double valor) throws Exception {
		this.saldoAnulacaoGs = valor;
	}

	public void atribuirTipoMoedaSaldoAnulacaoGs(String tipo) throws Exception {
		this.tipoMoedaSaldoAnulacaoGs = tipo;
	}

	public void atribuirSaldoAnulacaoMe(double valor) throws Exception {
		this.saldoAnulacaoMe = valor;
	}

	public void atribuirDestinoSaldoAnulacao(String destino) throws Exception {
		this.destinoSaldoAnulacao = destino;
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

	public void atualizarDataAnulacao(Date data) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update anulacao_instrumento set data_anulacao = ? where id = ?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarCapitalGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update anulacao_instrumento set capital_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaCapitalGs(String tipo) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update anulacao_instrumento set tipo_moeda_capital_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarCapitalMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update anulacao_instrumento set capital_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarSolicitadoPor(String valor) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update anulacao_instrumento set solicitado_por = ? where id = ?");
		update.addString(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarDiasCorridos(int dias) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update anulacao_instrumento set dias_corridos = ? where id = ?");
		update.addInt(dias);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPrimaGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update anulacao_instrumento set prima_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaPrimaGs(String tipo) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update anulacao_instrumento set tipo_moeda_prima_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPrimaMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update anulacao_instrumento set prima_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarComissaoGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update anulacao_instrumento set comissao_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaComissaoGs(String tipo) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update anulacao_instrumento set tipo_moeda_comissao_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarComissaoMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update anulacao_instrumento set comissao_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarComissaoRecuperarGs(double valor) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update anulacao_instrumento set comissao_recuperar_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaComissaoRecuperarGs(String tipo)
			throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"crm",
						"update anulacao_instrumento set tipo_moeda_comissao_recuperar_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarComissaoRecuperarMe(double valor) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update anulacao_instrumento set comissao_recuperar_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarSaldoAnulacaoGs(double valor) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update anulacao_instrumento set saldo_anulacao_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaSaldoAnulacaoGs(String tipo) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update anulacao_instrumento set tipo_moeda_saldo_anulacao_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarSaldoAnulacaoMe(double valor) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update anulacao_instrumento set saldo_anulacao_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarDestinoSaldoAnulacao(String destino) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update anulacao_instrumento set destino_saldo_anulacao = ? where id = ?");
		update.addString(destino);
		update.addLong(this.obterId());

		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"crm",
						"insert into anulacao_instrumento values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());

		if (this.dataAnulacao != null)
			insert.addLong(this.dataAnulacao.getTime());
		else
			insert.addLong(0);

		insert.addDouble(this.capitalGs);
		insert.addString(this.tipoMoedaCapitalGs);
		insert.addDouble(this.capitalMe);
		insert.addString(this.solicitadoPor);
		insert.addInt(this.diasCorridos);
		insert.addDouble(this.primaGs);
		insert.addString(this.tipoMoedaPrimaGs);
		insert.addDouble(this.primaMe);
		insert.addDouble(this.comissaoGs);
		insert.addString(this.tipoMoedaComissaoGs);
		insert.addDouble(this.comissaoMe);
		insert.addDouble(this.comissaoRecuperarGs);
		insert.addString(this.tipoMoedaComissaoRecuperarGs);
		insert.addDouble(this.comissaoRecuperarMe);
		insert.addDouble(this.saldoAnulacaoGs);
		insert.addString(this.tipoMoedaSaldoAnulacaoGs);
		insert.addDouble(this.saldoAnulacaoMe);
		insert.addString(this.destinoSaldoAnulacao);
		insert.addString(this.tipoInstrumento);
		insert.addDouble(this.numeroEndoso);
		insert.addDouble(this.certificado);

		insert.execute();
	}

	public Date obterDataAnulacao() throws Exception {
		if (this.dataAnulacao == null) {
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery("crm",
							"select data_anulacao from anulacao_instrumento where id = ?");
			query.addLong(this.obterId());

			long dataLong = query.executeAndGetFirstRow().getLong(
					"data_anulacao");

			if (dataLong > 0)
				this.dataAnulacao = new Date(dataLong);
		}

		return this.dataAnulacao;

	}

	public double obterCapitalGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select capital_gs from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("capital_gs");
	}

	public String obterTipoMoedaCapitalGs() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select tipo_moeda_capital_gs from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_moeda_capital_gs");
	}

	public double obterCapitalMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select capital_me from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("capital_me");
	}

	public String obterSolicitadoPor() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select solicitado_por from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("solicitado_por");
	}

	public int obterDiasCorridos() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select dias_corridos from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getInt("dias_corridos");
	}

	public double obterPrimaGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select prima_gs from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("prima_gs");
	}

	public String obterTipoMoedaPrimaGs() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select tipo_moeda_prima_gs from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_moeda_prima_gs");
	}

	public double obterPrimaMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select prima_me from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("prima_me");
	}

	public double obterComissaoGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select comissao_gs from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("comissao_gs");
	}

	public String obterTipoMoedaComissaoGs() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select tipo_moeda_comissao_gs from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow()
				.getString("tipo_moeda_comissao_gs");
	}

	public double obterComissaoMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select comissao_me from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("comissao_me");
	}

	public double obterComissaoRecuperarGs() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select comissao_recuperar_gs from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("comissao_recuperar_gs");
	}

	public String obterTipoMoedaComissaoRecuperarGs() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select tipo_moeda_comissao_recuperar_gs from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString(
				"tipo_moeda_comissao_recuperar_gs");
	}

	public double obterComissaoRecuperarMe() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select comissao_recuperar_me from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("comissao_recuperar_me");
	}

	public double obterSaldoAnulacaoGs() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select saldo_anulacao_gs from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("saldo_anulacao_gs");
	}

	public String obterTipoMoedaSaldoAnulacaoGs() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select tipo_moeda_saldo_anulacao_gs from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString(
				"tipo_moeda_saldo_anulacao_gs");
	}

	public double obterSaldoAnulacaoMe() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select saldo_anulacao_me from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("saldo_anulacao_me");
	}

	public String obterDestinoSaldoAnulacao() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select destino_saldo_anulacao from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow()
				.getString("destino_saldo_anulacao");
	}

	public String obterTipoInstrumento() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select tipo_instrumento from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_instrumento");
	}

	public double obterNumeroEndoso() throws Exception {
		if (this.numeroEndoso == 0) {
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery("crm",
							"select numero_endoso from anulacao_instrumento where id = ?");
			query.addLong(this.obterId());

			this.numeroEndoso = query.executeAndGetFirstRow().getDouble(
					"numero_endoso");
		}
		return this.numeroEndoso;
	}

	public double obterCertificado() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select certificado from anulacao_instrumento where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("certificado");
	}

	public void verificarDuplicidade(Apolice apolice,
			ClassificacaoContas cContas, Date dataAnulacao, double numeroEndoso)
			throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,anulacao_instrumento,apolice where evento.id = anulacao_instrumento.id and superior = ? and secao = ? and data_anulacao = ? and numero_endoso = ? group by evento.id");
		query.addLong(apolice.obterId());
		query.addLong(cContas.obterId());
		query.addLong(dataAnulacao.getTime());
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