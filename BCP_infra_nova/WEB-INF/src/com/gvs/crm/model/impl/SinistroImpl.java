package com.gvs.crm.model.impl;

import java.util.Date;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.CodificacaoCobertura;
import com.gvs.crm.model.CodificacaoDetalhe;
import com.gvs.crm.model.CodificacaoPlano;
import com.gvs.crm.model.CodificacaoRisco;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Sinistro;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class SinistroImpl extends EventoImpl implements Sinistro {

	private String numero;

	private Date dataSinistro;

	private Date dataDenuncia;

	private AuxiliarSeguro auxiliar;

	private double montanteGs;

	private String tipoMoedaMontanteGs;

	private double montanteMe;

	private String situacao;

	private Date dataPagamento;

	private Date dataRecuperacao;

	private double valorRecuperacao;

	private double valorRecuperacaoTerceiro;

	private double participacao;

	private String tipoInstrumento;

	private double numeroEndoso;

	private double certificado;

	public void atribuirNumero(String numero) throws Exception {
		this.numero = numero;
	}

	public void atribuirDataSinistro(Date data) throws Exception {
		this.dataSinistro = data;
	}

	public void atribuirDataDenuncia(Date data) throws Exception {
		this.dataDenuncia = data;
	}

	public void atribuirAgente(AuxiliarSeguro auxiliar) throws Exception {
		this.auxiliar = auxiliar;
	}

	public void atribuirMontanteGs(double valor) throws Exception {
		this.montanteGs = valor;
	}

	public void atribuirTipoMoedaMontanteGs(String tipo) throws Exception {
		this.tipoMoedaMontanteGs = tipo;
	}

	public void atribuirMontanteMe(double valor) throws Exception {
		this.montanteMe = valor;
	}

	public void atribuirSituacao(String situacao) throws Exception {
		this.situacao = situacao;
	}

	public void atribuirDataPagamento(Date data) throws Exception {
		this.dataPagamento = data;
	}

	public void atribuirDataRecuperacao(Date data) throws Exception {
		this.dataRecuperacao = data;
	}

	public void atribuirValorRecuperacao(double valor) throws Exception {
		this.valorRecuperacao = valor;
	}

	public void atribuirValorRecuperacaoTerceiro(double valor) throws Exception {
		this.valorRecuperacaoTerceiro = valor;
	}

	public void atribuirParticipacao(double valor) throws Exception {
		this.participacao = valor;
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
				"update sinistro set numero = ? where id = ?");
		update.addString(numero);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarDataSinistro(Date data) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update sinistro set data_sinistro = ? where id = ?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarDataDenuncia(Date data) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update sinistro set data_denuncia = ? where id = ?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarAgente(AuxiliarSeguro auxiliar) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update sinistro set agente = ? where id = ?");
		update.addLong(auxiliar.obterId());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarMontanteGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update sinistro set montante_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaMontanteGs(String tipo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update sinistro set tipo_moeda_montante_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarMontanteMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update sinistro set montante_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarSituacao(String situacao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update sinistro set situacao = ? where id = ?");
		update.addString(situacao);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarDataPagamento(Date data) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update sinistro set data_pagamento = ? where id = ?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarDataRecuperacao(Date data) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update sinistro set data_recuperacao = ? where id = ?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarValorRecuperacao(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update sinistro set valor_recuperacao = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarValorRecuperacaoTerceiro(double valor)
			throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update sinistro set valor_recuperacao_terceiro = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarParticipacao(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update sinistro set participacao = ? where id = ?");
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
						"insert into sinistro values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addString(this.numero);
		if (this.dataSinistro != null)
			insert.addLong(this.dataSinistro.getTime());
		else
			insert.addLong(0);

		if (this.dataDenuncia != null)
			insert.addLong(this.dataDenuncia.getTime());
		else
			insert.addLong(0);

		if (this.auxiliar != null)
			insert.addLong(this.auxiliar.obterId());
		else
			insert.addLong(0);

		insert.addDouble(this.montanteGs);
		insert.addString(this.tipoMoedaMontanteGs);
		insert.addDouble(this.montanteMe);
		insert.addString(this.situacao);

		if (this.dataPagamento != null)
			insert.addLong(this.dataPagamento.getTime());
		else
			insert.addLong(0);

		if (this.dataRecuperacao != null)
			insert.addLong(this.dataRecuperacao.getTime());
		else
			insert.addLong(0);

		insert.addDouble(this.valorRecuperacao);
		insert.addDouble(this.valorRecuperacaoTerceiro);
		insert.addDouble(this.participacao);
		insert.addString(this.tipoInstrumento);
		insert.addDouble(this.numeroEndoso);
		insert.addDouble(this.certificado);

		insert.execute();
	}

	public String obterNumero() throws Exception {
		if (this.numero == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select numero from sinistro where id = ?");
			query.addLong(this.obterId());

			this.numero = query.executeAndGetFirstRow().getString("numero");
		}

		return this.numero;

	}

	public Date obterDataSinistro() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_sinistro from sinistro where id = ?");
		query.addLong(this.obterId());

		Date data = null;

		long dataLong = query.executeAndGetFirstRow().getLong("data_sinistro");

		if (dataLong > 0)
			data = new Date(dataLong);

		return data;
	}

	public Date obterDataDenuncia() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_denuncia from sinistro where id = ?");
		query.addLong(this.obterId());

		Date data = null;

		long dataLong = query.executeAndGetFirstRow().getLong("data_denuncia");

		if (dataLong > 0)
			data = new Date(dataLong);

		return data;
	}

	public AuxiliarSeguro obterAuxiliar() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select agente from sinistro where id = ?");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("agente");

		AuxiliarSeguro auxiliar = null;

		if (id > 0) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			auxiliar = (AuxiliarSeguro) home.obterEntidadePorId(id);
		}
		return auxiliar;
	}

	public double obterMontanteGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select montante_gs from sinistro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("montante_gs");
	}

	public String obterTipoMoedaMontanteGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_moeda_montante_gs from sinistro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow()
				.getString("tipo_moeda_montante_gs");
	}

	public double obterMontanteMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select montante_me from sinistro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("montante_me");
	}

	public String obterSituacao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select situacao from sinistro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("situacao");
	}

	public Date obterDataPagamento() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_pagamento from sinistro where id = ?");
		query.addLong(this.obterId());

		Date data = null;

		long dataLong = query.executeAndGetFirstRow().getLong("data_pagamento");

		if (dataLong > 0)
			data = new Date(dataLong);

		return data;
	}

	public Date obterDataRecuperacao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_recuperacao from sinistro where id = ?");
		query.addLong(this.obterId());

		Date data = null;

		long dataLong = query.executeAndGetFirstRow().getLong(
				"data_recuperacao");

		if (dataLong > 0)
			data = new Date(dataLong);

		return data;
	}

	public double obterValorRecuperacao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select valor_recuperacao from sinistro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("valor_recuperacao");
	}

	public double obterValorRecuperacaoTerceiro() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select valor_recuperacao_terceiro from sinistro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("valor_recuperacao");
	}

	public double obterParticipacao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select participacao from sinistro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("participacao");
	}

	public void verificarDuplicidade(Apolice apolice,
			ClassificacaoContas cContas, String numeroSinistro,
			double numeroEndoso) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,sinistro,apolice where evento.id = sinistro.id and superior = ? and secao = ? and numero = ? and sinistro.numero_endoso = ? group by evento.id");
		query.addLong(apolice.obterId());
		query.addLong(cContas.obterId());
		query.addString(numeroSinistro);
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
				"select tipo_instrumento from sinistro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_instrumento");
	}

	public double obterNumeroEndoso() throws Exception {
		if (this.numeroEndoso == 0) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select numero_endoso from sinistro where id = ?");
			query.addLong(this.obterId());

			this.numeroEndoso = query.executeAndGetFirstRow().getDouble(
					"numero_endoso");
		}
		return this.numeroEndoso;
	}

	public double obterCertificado() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select certificado from sinistro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("certificado");
	}

	public CodificacaoCobertura obterCodificacaoCobertura() throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		CodificacaoCobertura cobertura = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select codificacao_plano from sinistro where id = ?");
		query.addLong(this.obterId());
		
		if(query.execute().length > 0)
		{
			long id = query.executeAndGetFirstRow().getLong("codificacao_plano");
			
			if(id > 0)
				cobertura = (CodificacaoCobertura) home.obterEventoPorId(id);
		}
		
		return cobertura;
	}

	public CodificacaoDetalhe obterCodificacaoDetalhe() throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		CodificacaoDetalhe detalhe = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select codificacao_detalhe from sinistro where id = ?");
		query.addLong(this.obterId());
		
		if(query.execute().length > 0)
		{
			long id = query.executeAndGetFirstRow().getLong("codificacao_detalhe");
			
			if(id > 0)
				detalhe = (CodificacaoDetalhe) home.obterEventoPorId(id);
		}
		
		return detalhe;
	}

	public CodificacaoPlano obterCodificacaoPlano() throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		CodificacaoPlano plano = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select codificacao_plano from sinistro where id = ?");
		query.addLong(this.obterId());
		
		if(query.execute().length > 0)
		{
			long id = query.executeAndGetFirstRow().getLong("codificacao_plano");
			
			if(id > 0)
				plano = (CodificacaoPlano) home.obterEventoPorId(id);
		}
		
		return plano;
	}

	public CodificacaoRisco obterCodificacaoRisco() throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		CodificacaoRisco risco = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select codificacao_risco from sinistro where id = ?");
		query.addLong(this.obterId());
		
		if(query.execute().length > 0)
		{
			long id = query.executeAndGetFirstRow().getLong("codificacao_risco");
			
			if(id > 0)
				risco = (CodificacaoRisco) home.obterEventoPorId(id);
		}
		
		return risco;
	}
}