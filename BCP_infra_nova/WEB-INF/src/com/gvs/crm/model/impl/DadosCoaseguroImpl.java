package com.gvs.crm.model.impl;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.DadosCoaseguro;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class DadosCoaseguroImpl extends EventoImpl implements DadosCoaseguro {

	private Entidade aseguradora;

	private double capitalGs;

	private String tipoMoedaCapitalGs;

	private double capitalMe;

	private double participacao;

	private double primaGs;

	private String tipoMoedaPrimaGs;

	private double primaMe;

	private String grupo;

	private String tipoInstrumento;

	private double numeroEndoso;

	private double certificado;

	public void atribuirAseguradora(Entidade aseguradora) throws Exception {
		this.aseguradora = aseguradora;
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

	public void atribuirParticipacao(double valor) throws Exception {
		this.participacao = valor;
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

	public void atribuirGrupo(String grupo) throws Exception {
		this.grupo = grupo;
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

	public void atualizarAseguradora(Entidade aseguradora) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_coaseguro set aseguradora = ? where id = ?");
		update.addLong(aseguradora.obterId());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarCapitalGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_coaseguro set capital_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaCapitalGs(String tipo) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update dados_coaseguro set tipo_moeda_capital_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarCapitalMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_coaseguro set capital_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarParticipacao(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_coaseguro set participacao = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPrimaGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_coaseguro set prima_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaPrimaGs(String tipo) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update dados_coaseguro set tipo_moeda_prima_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarGrupo(String grupo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_coaseguro set grupo = ? where id = ?");
		update.addString(grupo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPrimaMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_coaseguro set prima_me = ? where id = ?");
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
						"insert into dados_coaseguro(id, aseguradora, capital_gs, tipo_moeda_capital_gs, capital_me, participacao, prima_gs, tipo_moeda_prima_gs, prima_me, grupo, tipo_instrumento, numero_endoso, certificado) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addLong(this.aseguradora.obterId());
		insert.addDouble(this.capitalGs);
		insert.addString(this.tipoMoedaCapitalGs);
		insert.addDouble(this.capitalMe);
		insert.addDouble(this.participacao);
		insert.addDouble(this.primaGs);
		insert.addString(this.tipoMoedaPrimaGs);
		insert.addDouble(this.primaMe);
		insert.addString(this.grupo);
		insert.addString(this.tipoInstrumento);
		insert.addDouble(this.numeroEndoso);
		insert.addDouble(this.certificado);

		insert.execute();
	}

	public Entidade obterAseguradora() throws Exception {
		if (this.aseguradora == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select aseguradora from dados_coaseguro where id = ?");
			query.addLong(this.obterId());

			long id = query.executeAndGetFirstRow().getLong("aseguradora");

			if (id > 0) {
				EntidadeHome home = (EntidadeHome) this.getModelManager()
						.getHome("EntidadeHome");
				this.aseguradora = home.obterEntidadePorId(id);
			}
		}

		return this.aseguradora;
	}

	public double obterCapitalGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select capital_gs from dados_coaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("capital_gs");
	}

	public String obterTipoMoedaCapitalGs() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select tipo_moeda_capital_gs from dados_coaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_moeda_capital_gs");
	}

	public double obterCapitalMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select capital_me from dados_coaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("capital_me");
	}

	public double obterParticipacao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select participacao from dados_coaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("participacao");
	}

	public double obterPrimaGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select prima_gs from dados_coaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("prima_gs");
	}

	public String obterTipoMoedaPrimaGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_moeda_prima_gs from dados_coaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_moeda_prima_gs");
	}

	public double obterPrimaMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select prima_me from dados_coaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("prima_me");
	}

	public String obterGrupo() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select grupo from dados_coaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("grupo");
	}

	public void verificarDuplicidade(Apolice apolice,
			ClassificacaoContas cContas, Entidade aseguradora,
			double numeroEndoso) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,dados_coaseguro,apolice where evento.id = dados_coaseguro.id and superior = ? and secao = ? and aseguradora = ? and dados_coaseguro.numero_endoso = ? group by evento.id");
		query.addLong(apolice.obterId());
		query.addLong(cContas.obterId());
		query.addLong(aseguradora.obterId());
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
				"select tipo_instrumento from dados_coaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_instrumento");
	}

	public double obterNumeroEndoso() throws Exception {
		if (this.numeroEndoso == 0) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select numero_endoso from dados_coaseguro where id = ?");
			query.addLong(this.obterId());

			this.numeroEndoso = query.executeAndGetFirstRow().getDouble(
					"numero_endoso");
		}

		return this.numeroEndoso;

	}

	public double obterCertificado() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select certificado from dados_coaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("certificado");
	}
}