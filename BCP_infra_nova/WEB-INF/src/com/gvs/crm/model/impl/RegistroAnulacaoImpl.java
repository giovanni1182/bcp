package com.gvs.crm.model.impl;

import java.util.Date;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Reaseguradora;
import com.gvs.crm.model.RegistroAnulacao;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class RegistroAnulacaoImpl extends EventoImpl implements
		RegistroAnulacao {

	private Reaseguradora reaseguradora;

	private String tipoContrato;

	private Date dataAnulacao;

	private double capitalGs;

	private String tipoMoedaCapitalGs;

	private double capitalMe;

	private int diasCorridos;

	private double primaGs;

	private String tipoMoedaPrimaGs;

	private double primaMe;

	private double comissaoGs;

	private String tipoMoedaComissaoGs;

	private double comissaoMe;

	public void atribuirReaeguradora(Reaseguradora reaseguradora)
			throws Exception {
		this.reaseguradora = reaseguradora;
	}

	public void atribuirTipoContrato(String tipo) throws Exception {
		this.tipoContrato = tipo;
	}

	public void atribuirDataAnulacao(Date data) throws Exception {
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

	public void atualizarReaeguradora(Reaseguradora reaseguradora)
			throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update registro_anulacao set reaseguradora = ? where id = ?");
		update.addLong(reaseguradora.obterId());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoContrato(String tipo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update registro_anulacao set tipo_contrato = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarDataAnulacao(Date data) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update registro_anulacao set data_anulacao = ? where id = ?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarCapitalGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update registro_anulacao set capital_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaCapitalGs(String tipo) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update registro_anulacao set tipo_moeda_capital_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarCapitalMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update registro_anulacao set capital_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarDiasCorridos(int dias) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update registro_anulacao set dias = ? where id = ?");
		update.addInt(dias);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPrimaGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update registro_anulacao set prima_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaPrimaGs(String tipo) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update registro_anulacao set tipo_moeda_prima_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPrimaMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update registro_anulacao set prima_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarComissaoGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update registro_anulacao set comissao_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaComissaoGs(String tipo) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update registro_anulacao set tipo_moeda_comissao_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarComissaoMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update registro_anulacao set comissao_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"crm",
						"insert into registro_anulacao values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		if (this.reaseguradora != null)
			insert.addLong(this.reaseguradora.obterId());
		else
			insert.addLong(0);

		insert.addString(this.tipoContrato);

		if (this.dataAnulacao != null)
			insert.addLong(this.dataAnulacao.getTime());
		else
			insert.addLong(0);

		insert.addDouble(this.capitalGs);
		insert.addString(this.tipoMoedaCapitalGs);
		insert.addDouble(this.capitalMe);
		insert.addInt(this.diasCorridos);
		insert.addDouble(this.primaGs);
		insert.addString(this.tipoMoedaPrimaGs);
		insert.addDouble(this.primaMe);
		insert.addDouble(this.comissaoGs);
		insert.addString(this.tipoMoedaComissaoGs);
		insert.addDouble(this.comissaoMe);

		insert.execute();
	}

	public Reaseguradora obterReaeguradora() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select reaseguradora from registro_anulacao where id = ?");
		query.addLong(this.obterId());

		Reaseguradora reaseguradora = null;

		long id = query.executeAndGetFirstRow().getLong("reaseguradora");

		if (id > 0) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");
			reaseguradora = (Reaseguradora) home.obterEntidadePorId(id);
		}

		return reaseguradora;
	}

	public String obterTipoContrato() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_contrato from registro_anulacao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_contrato");
	}

	public Date obterDataAnulacao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_anulacao from registro_anulacao where id = ?");
		query.addLong(this.obterId());

		Date data = null;
		long dataLong = query.executeAndGetFirstRow().getLong("data_anulacao");

		if (dataLong > 0)
			data = new Date(dataLong);

		return data;
	}

	public double obterCapitalGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select capital_gs from registro_anulacao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("capital_gs");
	}

	public String obterTipoMoedaCapitalGs() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select tipo_moeda_capital_gs from registro_anulacao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_moeda_capital_gs");
	}

	public double obterCapitalMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select capital_me from registro_anulacao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("capital_me");
	}

	public int obterDiasCorridos() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select dias from registro_anulacao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getInt("dias");
	}

	public double obterPrimaGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select prima_gs from registro_anulacao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("prima_gs");
	}

	public String obterTipoMoedaPrimaGs() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select tipo_moeda_prima_gs from registro_anulacao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_moeda_prima_gs");
	}

	public double obterPrimaMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select prima_me from registro_anulacao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("prima_me");
	}

	public double obterComissaoGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select comissao_gs from registro_anulacao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("comissao_gs");
	}

	public String obterTipoMoedaComissaoGs() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select tipo_moeda_comissao_gs from registro_anulacao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow()
				.getString("tipo_moeda_comissao_gs");
	}

	public double obterComissaoMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select comissao_me from registro_anulacao where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("comissao_me");
	}

	public void verificarDuplicidade(Apolice apolice,
			ClassificacaoContas cContas, Reaseguradora reaseguradora,
			String tipo) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,registro_anulacao,apolice where evento.id = registro_anulacao.id and superior = ? and secao = ? and reaseguradora = ? and tipo = ? group by evento.id");
		query.addLong(apolice.obterId());
		query.addLong(cContas.obterId());
		query.addLong(reaseguradora.obterId());
		query.addString(tipo);

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