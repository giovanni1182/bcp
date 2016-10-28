package com.gvs.crm.model.impl;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Parametro;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class ParametroImpl extends EntidadeImpl implements Parametro {
	private Map dias = new TreeMap();

	private Map<String,Consistencia> consistencias = new TreeMap<>();

	private Map configuracoes = new TreeMap();

	public class FeriadoImpl implements Feriado {
		private EntidadeImpl entidade;

		private int id;

		private String descricao;

		private Date data;

		FeriadoImpl(EntidadeImpl entidade, int id, String descricao, Date data) {
			this.entidade = entidade;
			this.id = id;
			this.descricao = descricao;
			this.data = data;
		}

		public void atualizarValor(String descricao, Date data)
				throws Exception {
			SQLUpdate update = this.entidade
					.getModelManager()
					.createSQLUpdate(
							"update feriado set descricao=?, data=? where entidade=? and id=?");
			update.addString(descricao);
			update.addLong(data.getTime());
			update.addLong(this.entidade.obterId());
			update.addInt(this.id);
			update.execute();
		}

		public String obterDescricaoFeriado() throws Exception {
			return this.descricao;
		}

		public Date obterDataFeriado() throws Exception {
			return this.data;
		}

		public Entidade obterEntidade() throws Exception {
			return this.entidade;
		}

		public int obterId() throws Exception {
			return this.id;
		}
	}

	public class ConsistenciaImpl implements Consistencia {
		private EntidadeImpl entidade;

		private int sequencial;

		private String operando1;

		private String operando2;

		private String operador;

		private String mensagem;

		private int regra;

		ConsistenciaImpl(EntidadeImpl entidade, int sequencial,
				String operando1, String operador, String operando2,
				String mensagem, int regra) {
			this.entidade = entidade;
			this.sequencial = sequencial;
			this.operando1 = operando1;
			this.operando2 = operando2;
			this.operador = operador;
			this.mensagem = mensagem;
			this.regra = regra;
		}

		public void atualizarValor(String operando1, String operador,
				String operando2, String mensagem, int regra) throws Exception {
			SQLUpdate update = this.entidade
					.getModelManager()
					.createSQLUpdate(
							"update consistencia set operando1=?, operador=?, operando2=?, mensagem=?, regra=? where entidade=? and sequencial=? and regra=?");
			update.addString(operando1);
			update.addString(operador);
			update.addString(operando2);
			update.addString(mensagem);
			update.addInt(regra);
			update.addLong(this.entidade.obterId());
			update.addLong(this.obterSequencial());
			update.addLong(regra);
			update.execute();
		}

		public Entidade obterEntidade() throws Exception {
			return this.entidade;
		}

		public int obterSequencial() throws Exception {
			return this.sequencial;
		}

		public String obterOperando1() throws Exception {
			return this.operando1;
		}

		public String obterOperando2() throws Exception {
			return this.operando2;
		}

		public String obterOperador() throws Exception {
			return this.operador;
		}

		public String obterMensagem() throws Exception {
			return this.mensagem;
		}

		public int obterRegra() throws Exception {
			return this.regra;
		}
	}

	/*
	 * public class ConfiguracoesImpl implements Configuracao { private
	 * EntidadeImpl entidade; private String eventoEntidade; private String
	 * campo; private String argumento; private String acao; private int
	 * sequencial;
	 * 
	 * ConfiguracoesImpl(EntidadeImpl entidade, int sequencial, String
	 * eventoEntidade, String campo, String argumento, String acao) {
	 * this.entidade = entidade; this.sequencial = sequencial;
	 * this.eventoEntidade = eventoEntidade; this.campo = campo; this.argumento =
	 * argumento; this.acao = acao; } public void atualizarValor(int sequencial,
	 * String eventoEntidade, String campo, String argumento, String acao)
	 * throws Exception { SQLUpdate update =
	 * this.entidade.getModelManager().createSQLUpdate("update configuracao set
	 * evento_entidade=?, campo=?, argumento=?, acao=? where id=? and
	 * sequencial=?"); update.addString(eventoEntidade);
	 * update.addString(campo); update.addString(argumento);
	 * update.addString(acao); update.addLong(this.entidade.obterId());
	 * update.addLong(this.obterSequencial()); update.execute(); }
	 * 
	 * public Entidade obterEntidade() throws Exception { return this.entidade; }
	 * 
	 * public String obterEventoEntidade() throws Exception { return
	 * this.eventoEntidade; }
	 * 
	 * public String obterCampo() throws Exception { return this.campo; }
	 * 
	 * public String obterArgumento() throws Exception { return this.argumento; }
	 * 
	 * public String obterAcao() throws Exception { return this.acao; }
	 * 
	 * public int obterSequencial() throws Exception { return this.sequencial; } }
	 */

	public class ControleDocumentoImpl implements ControleDocumento {
		private ParametroImpl parametro;

		private int seq;

		private String descricao;

		private Date dataLimite;

		public ControleDocumentoImpl(ParametroImpl parametro, int seq,
				String descricao, Date dataLimite) throws Exception {
			this.parametro = parametro;
			this.seq = seq;
			this.descricao = descricao;
			this.dataLimite = dataLimite;
		}

		public void atualizar(String descricao, Date dataLimite)
				throws Exception {
			SQLUpdate update = this.parametro
					.getModelManager()
					.createSQLUpdate(
							"crm",
							"update controle_documento set descricao = ?, data_limite = ? where id = ? and seq = ?");
			update.addString(descricao);
			update.addLong(dataLimite.getTime());
			update.addLong(this.parametro.obterId());
			update.addInt(this.seq);

			update.execute();
		}

		public Parametro obterParametro() throws Exception {
			return this.parametro;
		}

		public int obterSequencial() throws Exception {
			return this.seq;
		}

		public String obterDescricao() throws Exception {
			return this.descricao;
		}

		public Date obterDataLimite() throws Exception {
			return this.dataLimite;
		}
	}

	public class IndicadorImpl implements Indicador {
		private ParametroImpl parametro;

		private int seq;

		private String tipo;

		private String descricao;

		private int peso;

		private boolean excludente;

		public IndicadorImpl(ParametroImpl parametro, int seq, String tipo,
				String descricao, int peso, boolean excludente)
				throws Exception {
			this.parametro = parametro;
			this.seq = seq;
			this.tipo = tipo;
			this.descricao = descricao;
			this.peso = peso;
			this.excludente = excludente;
		}

		public void atualizar(String descricao, int peso) throws Exception {
			SQLUpdate update = this.parametro
					.getModelManager()
					.createSQLUpdate(
							"crm",
							"update indicadores set descricao = ?, peso = ? where id = ? and seq = ? and tipo = ?");
			update.addString(descricao);
			update.addInt(peso);
			update.addLong(this.parametro.obterId());
			update.addInt(this.seq);
			update.addString(this.tipo);

			update.execute();
		}

		public Parametro obterParametro() throws Exception {
			return this.parametro;
		}

		public int obterSequencial() throws Exception {
			return this.seq;
		}

		public String obterTipo() throws Exception {
			return this.tipo;
		}

		public String obterDescricao() throws Exception {
			return this.descricao;
		}

		public int obterPeso() throws Exception {
			return this.peso;
		}

		public boolean eExcludente() throws Exception {
			return this.excludente;
		}
	}

	/*
	 * public void incluir() throws Exception {
	 * 
	 * super.incluir();
	 * 
	 * SQLUpdate insert = this.getModelManager().createSQLUpdate("insert into
	 * parametros(id) values(?)"); insert.addLong(this.obterId());
	 * 
	 * insert.execute(); }
	 * 
	 * public void atualizarTipoValorMinimo(String tipo) throws Exception {
	 * SQLUpdate update = this.getModelManager().createSQLUpdate("update
	 * parametros set tipo_valor_minimo = ? where id = ?");
	 * update.addString(tipo); update.addLong(this.obterId());
	 * 
	 * update.execute(); }
	 * 
	 * public void atualizarValorMinimo(double valor) throws Exception {
	 * SQLUpdate update = this.getModelManager().createSQLUpdate("update
	 * parametros set valor_minimo = ? where id = ?"); update.addDouble(valor);
	 * update.addLong(this.obterId());
	 * 
	 * update.execute(); }
	 * 
	 * public String obterTipoValorMinimo() throws Exception { SQLQuery query =
	 * this.getModelManager().createSQLQuery("select tipo_valor_minimo from
	 * parametros where id = ?"); query.addLong(this.obterId());
	 * 
	 * return query.executeAndGetFirstRow().getString("tipo_valor_minimo"); }
	 * 
	 * public double obterValorMinimo() throws Exception { SQLQuery query =
	 * this.getModelManager().createSQLQuery("select valor_minimo from
	 * parametros where id = ?"); query.addLong(this.obterId());
	 * 
	 * return query.executeAndGetFirstRow().getDouble("valor_minimo"); }
	 */

	public void adicionarIndicador(String tipo, String descricao, int peso,
			String excludente) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"select max(seq) as MX from indicadores where id = ? and tipo = ?");
		query.addLong(this.obterId());
		query.addString(tipo);

		long seq = query.executeAndGetFirstRow().getLong("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into indicadores (id, seq, tipo, descricao, peso, excludente) values (?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addLong(seq);
		insert.addString(tipo);
		insert.addString(descricao);
		insert.addInt(peso);
		insert.addString(excludente);

		insert.execute();
	}

	public void adicionarFeriado(String descricao, Date data) throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery(
				"select max(id) as MX from feriado where entidade=?");
		query.addLong(this.obterId());
		long id = query.executeAndGetFirstRow().getLong("MX") + 1;
		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into feriado (id, entidade, descricao, data) values (?, ?, ?, ?)");
		insert.addLong(id);
		insert.addLong(this.obterId());
		insert.addString(descricao);
		insert.addLong(data.getTime());
		insert.execute();
	}

	public void adicionarConsistencia(String operando1, String operador,
			String operando2, String mensagem, int regra) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"select max(sequencial) as maximo from consistencia where entidade=?");
		query.addLong(this.obterId());
		long id = query.executeAndGetFirstRow().getLong("maximo") + 1;
		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into consistencia (entidade, sequencial, operando1, operador, operando2, mensagem, regra) values (?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addLong(id);
		insert.addString(operando1);
		insert.addString(operador);
		insert.addString(operando2);
		insert.addString(mensagem);
		if (regra == 0)
			regra = 1;
		insert.addInt(regra);
		insert.execute();
	}

	public void adicionarConfiguracao(String eventoEntidade, String campo,
			String argumento, String acao) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"select max(sequencial) as maximo from configuracao where id=?");
		query.addLong(this.obterId());
		long sequencial = query.executeAndGetFirstRow().getLong("maximo") + 1;
		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into configuracao (id, evento_entidade, campo, argumento, acao, sequencial) values (?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addString(eventoEntidade);
		insert.addString(campo);
		insert.addString(argumento);
		insert.addString(acao);
		insert.addLong(sequencial);
		insert.execute();
	}

	public void adicionarControleDocumento(String descricao, Date dataLimite)
			throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery(
				"select max(seq) as MX from controle_documento where id=?");
		query.addLong(this.obterId());

		int seq = query.executeAndGetFirstRow().getInt("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into controle_documento (id, seq, descricao, data_limite) values (?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addInt(seq);
		insert.addString(descricao);
		insert.addLong(dataLimite.getTime());

		insert.execute();
	}

	public void excluirControleDocumento(ControleDocumento controle)
			throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate(
				"delete from controle_documento where id = ? and seq = ?");
		delete.addLong(this.obterId());
		delete.addInt(controle.obterSequencial());

		delete.execute();
	}

	public Feriado obterFeriado(int id) throws Exception {
		this.obterFeriados();
		return (Feriado) this.dias.get(Integer.toString(id));
	}

	public Consistencia obterConsistencia(int id, int regra) throws Exception {
		this.obterConsistencias();

		return (Consistencia) this.consistencias.get(new Integer(id)
				+ Integer.toString(regra));
	}

	/*
	 * public Configuracao obterConfiguracao(int sequencial) throws Exception {
	 * this.obterConsistencias();
	 * 
	 * return (Configuracao) this.configuracoes.get(new Integer(sequencial)); }
	 */

	public Collection obterFeriados() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select id, descricao, entidade, data from feriado where entidade = ?");
		query.addLong(this.obterId());
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			int id = rows[i].getInt("id");
			this.dias.put(Integer.toString(id), new FeriadoImpl(this, id,
					rows[i].getString("descricao"), new Date(rows[i]
							.getLong("data"))));
		}

		return this.dias.values();
	}

	/*
	 * public Collection obterConfiguracoes() throws Exception { SQLQuery query =
	 * this.getModelManager().createSQLQuery("crm", "select sequencial,
	 * evento_entidade, campo, argumento, acao from configuracao where id = ?");
	 * query.addLong(this.obterId()); SQLRow[] rows = query.execute();
	 * 
	 * for(int i = 0 ; i < rows.length ; i++) { int sequencial =
	 * rows[i].getInt("sequencial");
	 * this.configuracoes.put(Integer.toString(sequencial), new
	 * ConfiguracoesImpl(this, sequencial, rows[i].getString("evento_entidade"),
	 * rows[i].getString("campo"), rows[i].getString("argumento"),
	 * rows[i].getString("acao"))); }
	 * 
	 * return this.configuracoes.values(); }
	 */

	public Collection<Consistencia> obterConsistencias() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery(	"crm",
						"select entidade, sequencial, operando1, operador, operando2, mensagem, regra  from consistencia where entidade = ? and regra=? order by sequencial");
		query.addLong(this.obterId());
		query.addInt(1);
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
		{
			int id = rows[i].getInt("sequencial");
			int regra = rows[i].getInt("regra");
			this.consistencias.put(new Integer(id) + Integer.toString(regra),
					new ConsistenciaImpl(this, id, rows[i]
							.getString("operando1"), rows[i]
							.getString("operador"), rows[i]
							.getString("operando2"), rows[i]
							.getString("mensagem"), rows[i].getInt("regra")));
		}

		return this.consistencias.values();
	}

	public Map obterIndicadores(String tipo) throws Exception {
		Map indicadores = new TreeMap();

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from indicadores where id = ? and tipo = ?");
		query.addLong(this.obterId());
		query.addString(tipo);

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			int seq = rows[i].getInt("seq");
			String descricao = rows[i].getString("descricao");
			int peso = rows[i].getInt("peso");

			boolean excludente = false;

			if (rows[i].getString("excludente") != null) {
				if (rows[i].getString("excludente").equals("Sim"))
					excludente = true;
			}

			indicadores.put(new Integer(seq), new IndicadorImpl(this, seq,
					tipo, descricao, peso, excludente));
		}

		return indicadores;
	}

	public Indicador obterIndicador(String tipo, int seq) throws Exception {
		return (Indicador) this.obterIndicadores(tipo).get(new Integer(seq));
	}

	public Map obterControleDocumentos() throws Exception {
		Map controleDocumentos = new TreeMap();

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from controle_documento where id = ?");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			int seq = rows[i].getInt("seq");
			String descricao = rows[i].getString("descricao");

			Date data = null;

			long dataLong = rows[i].getLong("data_limite");

			if (dataLong != 0)
				data = new Date(dataLong);

			controleDocumentos.put(new Integer(seq), new ControleDocumentoImpl(
					this, seq, descricao, data));
		}

		return controleDocumentos;
	}

	public ControleDocumento obterControleDocumento(int seq) throws Exception {
		return (ControleDocumento) this.obterControleDocumentos().get(
				new Integer(seq));
	}

	public void removerControleDocumento(ControleDocumento controle)
			throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate(
				"delete from controle_documento where id=? and seq=?");
		delete.addLong(this.obterId());
		delete.addInt(controle.obterSequencial());

		delete.execute();
	}

	public void removerIndicador(Indicador indicador) throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate(
				"delete from indicadores where id=? and seq=? and tipo = ?");
		delete.addLong(this.obterId());
		delete.addInt(indicador.obterSequencial());
		delete.addString(indicador.obterTipo());

		delete.execute();
	}

	public void removerFeriado(Feriado feriado) throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate(
				"delete from feriado where entidade=? and id=?");
		delete.addLong(this.obterId());
		delete.addInt(feriado.obterId());
		delete.execute();
		if (this.dias != null)
			this.dias.remove(Integer.toString(feriado.obterId()));
	}

	public void removerConsistencia(Consistencia consistencia, int regra)
			throws Exception {
		SQLUpdate delete = this
				.getModelManager()
				.createSQLUpdate(
						"delete from consistencia where entidade=? and sequencial=? and regra=?");
		delete.addLong(this.obterId());
		delete.addInt(consistencia.obterSequencial());
		delete.addInt(regra);
		delete.execute();
		if (this.consistencias != null)
			this.consistencias.remove(Integer.toString(consistencia
					.obterSequencial()));
	}
}