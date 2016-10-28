package com.gvs.crm.model.impl;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.DadosReaseguro;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class DadosReaseguroImpl extends EventoImpl implements DadosReaseguro {

	private Entidade reaseguradora;

	private String tipoContrato;

	private Entidade corredora;

	private double capitalGs;

	private String tipoMoedaCapitalGs;

	private double capitalMe;

	private double primaGs;

	private String tipoMoedaPrimaGs;

	private double primaMe;

	private double comissaoGs;

	private String tipoMoedaComissaoGs;

	private double comissaoMe;

	private String situacao;

	private double valorEndoso;

	private String tipoInstrumento;

	public void atribuirReaseguradora(Entidade reaseguradora) throws Exception {
		this.reaseguradora = reaseguradora;
	}

	public void atribuirTipoContrato(String tipo) throws Exception {
		this.tipoContrato = tipo;
	}

	public void atribuirCorredora(Entidade corredora) throws Exception {
		this.corredora = corredora;
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

	public void atribuirSituacao(String situacao) throws Exception {
		this.situacao = situacao;
	}

	public void atribuirValorEndoso(double valor) throws Exception {
		this.valorEndoso = valor;
	}

	public void atribuirTipoInstrumento(String tipo) throws Exception {
		this.tipoInstrumento = tipo;
	}

	public void atualizarReaseguradora(Entidade reaseguradora) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_reaseguro set reaseguradora = ? where id = ?");
		update.addLong(reaseguradora.obterId());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoContrato(String tipo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_reaseguro set tipo_contrato = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarCorredora(Entidade corredora) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_reaseguro set corretora = ? where id = ?");
		update.addLong(corredora.obterId());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarCapitalGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_reaseguro set caiptal_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaCapitalGs(String tipo) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update dados_reaseguro set tipo_moeda_capital_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarCapitalMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_reaseguro set capital_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPrimaGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_reaseguro set prima_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaPrimaGs(String tipo) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update dados_reaseguro set tipo_moeda_prima_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPrimaMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_reaseguro set prima_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarComissaoGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_reaseguro set comissao_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaComissaoGs(String tipo) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update dados_reaseguro set tipo_moeda_comissao_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarComissaoMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_reaseguro set comissao_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarSituacao(String situacao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update dados_reaseguro set situacao = ? where id = ?");
		update.addString(situacao);
		update.addLong(this.obterId());

		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"crm",
						"insert dados_reaseguro(id, reaseguradora, tipo_contrato, corretora, caiptal_gs, tipo_moeda_capital_gs, capital_me, prima_gs, tipo_moeda_prima_gs, prima_me, comissao_gs, tipo_moeda_comissao_gs, comissao_me, situacao, valor_endoso, tipo_instrumento) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		if (this.reaseguradora != null)
			insert.addLong(this.reaseguradora.obterId());
		else
			insert.addLong(0);

		insert.addString(this.tipoContrato);
		if (this.corredora != null)
			insert.addLong(this.corredora.obterId());
		else
			insert.addLong(0);
		insert.addDouble(this.capitalGs);
		insert.addString(this.tipoMoedaCapitalGs);
		insert.addDouble(this.capitalMe);
		insert.addDouble(this.primaGs);
		insert.addString(this.tipoMoedaPrimaGs);
		insert.addDouble(this.primaMe);
		insert.addDouble(this.comissaoGs);
		insert.addString(this.tipoMoedaComissaoGs);
		insert.addDouble(this.comissaoMe);
		insert.addString(this.situacao);
		insert.addDouble(this.valorEndoso);
		insert.addString(this.tipoInstrumento);

		insert.execute();
	}

	public Entidade obterReaseguradora() throws Exception {
		if (this.reaseguradora == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select reaseguradora from dados_reaseguro where id = ?");
			query.addLong(this.obterId());

			long id = query.executeAndGetFirstRow().getLong("reaseguradora");

			if (id > 0) {
				EntidadeHome home = (EntidadeHome) this.getModelManager()
						.getHome("EntidadeHome");

				this.reaseguradora = home.obterEntidadePorId(id);
			}
		}

		return this.reaseguradora;
	}

	public String obterTipoContrato() throws Exception {
		if (this.tipoContrato == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select tipo_contrato from dados_reaseguro where id = ?");
			query.addLong(this.obterId());

			this.tipoContrato = query.executeAndGetFirstRow().getString(
					"tipo_contrato");
		}

		return this.tipoContrato;
	}

	public Entidade obterCorredora() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select corretora from dados_reaseguro where id = ?");
		query.addLong(this.obterId());

		Entidade corretora = null;

		long id = query.executeAndGetFirstRow().getLong("corretora");

		if (id > 0) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			corretora = home.obterEntidadePorId(id);
		}

		return corretora;
	}

	public double obterCapitalGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select caiptal_gs from dados_reaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("caiptal_gs");
	}

	public String obterTipoMoedaCapitalGs() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select tipo_moeda_capital_gs from dados_reaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_moeda_capital_gs");
	}

	public double obterCapitalMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select capital_me from dados_reaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("capital_me");
	}

	public double obterPrimaGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select prima_gs from dados_reaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("prima_gs");
	}

	public String obterTipoMoedaPrimaGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_moeda_prima_gs from dados_reaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_moeda_prima_gs");
	}

	public double obterPrimaMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select prima_me from dados_reaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("prima_me");
	}

	public double obterComissaoGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select comissao_gs from dados_reaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("comissao_gs");
	}

	public String obterTipoMoedaComissaoGs() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select tipo_moeda_comissao_gs from dados_reaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow()
				.getString("tipo_moeda_comissao_gs");
	}

	public double obterComissaoMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select comissao_me from dados_reaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("comissao_me");
	}

	public String obterSituacao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select situacao from dados_reaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("situacao");
	}

	public void verificarDuplicidade(Apolice apolice,
			ClassificacaoContas cContas, Entidade reaseguradora,
			String tipoContrato, double valorEndoso) throws Exception {

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,dados_reaseguro,apolice where evento.id = dados_reaseguro.id and superior = ? and secao = ? and reaseguradora = ? and tipo_contrato = ? and valor_endoso = ? group by evento.id");
		query.addLong(apolice.obterId());
		query.addLong(cContas.obterId());
		query.addLong(reaseguradora.obterId());
		query.addString(tipoContrato);
		query.addDouble(valorEndoso);

		//System.out.println("select evento.id from
		// evento,dados_reaseguro,apolice where evento.id = dados_reaseguro.id
		// and superior = "+apolice.obterId()+" and secao =
		// "+cContas.obterId()+" and reaseguradora = "+reaseguradora.obterId()+"
		// and tipo_contrato = " + tipoContrato + " group by evento.id");

		SQLRow[] rows = query.execute();

		//System.out.println("Rows: " + rows.length);

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");

			EventoHome home = (EventoHome) this.getModelManager().getHome(
					"EventoHome");

			Evento e = home.obterEventoPorId(id);

			e.excluir();
		}
	}

	public double obterValorEndoso() throws Exception {
		if (this.valorEndoso == 0) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select valor_endoso from dados_reaseguro where id = ?");
			query.addLong(this.obterId());

			this.valorEndoso = query.executeAndGetFirstRow().getDouble(
					"valor_endoso");
		}

		return this.valorEndoso;
	}

	public String obterTipoInstrumento() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_instrumento from dados_reaseguro where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_instrumento");
	}
	
	public String obterValoresParciaisAnulacao() throws Exception
	{
		double totalCapital = 0;
		double totalPrima = 0;
		double totalComissao = 0;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select capital_gs,prima_gs,comissao_gs from evento,registro_anulacao where evento.id = registro_anulacao.id and superior = ? and tipo='Parcial'");
		query.addLong(this.obterId());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			double capital = rows[i].getDouble("capital_gs");
			double prima = rows[i].getDouble("prima_gs");
			double comissao = rows[i].getDouble("comissao_gs");
			
			totalCapital+=capital;
			totalPrima+=prima;
			totalComissao+=comissao;
		}
		
		return totalCapital+";"+totalPrima+";"+totalComissao;
	}
}