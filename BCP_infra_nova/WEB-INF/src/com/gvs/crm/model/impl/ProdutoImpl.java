package com.gvs.crm.model.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.OcorrenciaHome;
import com.gvs.crm.model.Produto;
import com.gvs.crm.model.ProdutoHome;

import infra.config.InfraProperties;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class ProdutoImpl extends EntidadeImpl implements Produto {
	public class FornecedorImpl implements Fornecedor {
		private String codigoExterno;

		private Entidade fornecedor;

		private double valor;

		FornecedorImpl(Entidade fornecedor, String codigoExterno, double valor) {
			this.fornecedor = fornecedor;
			this.codigoExterno = codigoExterno;
			this.valor = valor;
		}

		public String obterCodigoExterno() throws Exception {
			return this.codigoExterno;
		}

		public Entidade obterEntidade() throws Exception {
			return this.fornecedor;
		}

		public double obterValorFornecedor() throws Exception {
			return this.valor;
		}

	}

	public class PrecoImpl implements Preco {
		private String moeda;

		private String tipo;

		private double valor;

		PrecoImpl(String tipo, String moeda, double valor) {
			this.tipo = tipo;
			this.moeda = moeda;
			this.valor = valor;
		}

		public String obterMoeda() {
			return this.moeda;
		}

		public String obterTipo() {
			return this.tipo;
		}

		public double obterValor() {
			return this.valor;
		}
	}

	public class StatusImpl implements Status {
		private String staus;

		private String acao;

		private int sequencia;

		StatusImpl(String status, String acao, int sequencia) {
			this.staus = status;
			this.acao = acao;
			this.sequencia = sequencia;
		}

		public String obterStatus() {
			return this.staus;
		}

		public String obterAcao() {
			return this.acao;
		}

		public int obterSequencia() {
			return this.sequencia;
		}
	}

	public class StatusAgendaImpl implements StatusAgenda {
		private String status;

		private int sequencia;

		StatusAgendaImpl(String status, int sequencia) {
			this.status = status;
			this.sequencia = sequencia;
		}

		public String obterStatus() {
			return this.status;
		}

		public int obterSequencia() {
			return this.sequencia;
		}
	}

	public class StatusAgendaProcessoImpl implements StatusAgendaProcesso {
		private String staus;

		private int sequencia;

		private int dia;

		StatusAgendaProcessoImpl(String status, int dia, int sequencia) {
			this.staus = status;
			this.dia = dia;
			this.sequencia = sequencia;
		}

		public String obterStatus() {
			return this.staus;
		}

		public int obterDia() {
			return this.dia;
		}

		public int obterSequencia() {
			return this.sequencia;
		}
	}

	private Set apelidos;

	private Boolean ativo;

	private String codigoExterno;

	private Boolean especial;

	private Collection estoques;

	private Map fornecedores;

	private Map precos;

	private String unidade;

	private double peso;

	private String tipoProduto;

	public void atualizarPeso(double peso) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update produto set peso=? where id=?");
		update.addDouble(peso);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarTipoProduto(String tipo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update produto set tipo=? where id=?");
		update.addString(tipo);
		update.addLong(this.obterId());
		update.execute();
	}

	public double obterPeso() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select peso from produto where id = ?");
		query.addLong(this.obterId());
		this.peso = query.executeAndGetFirstRow().getDouble("peso");

		return this.peso;
	}

	public String obterDimensao() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select valor from entidade_atributo where (nome='dimensao' or nome='Dimensões') and entidade=?");
		query.addLong(this.obterId());
		String dimensao = query.executeAndGetFirstRow().getString("valor");
		if (dimensao == null)
			dimensao = "";

		return dimensao;
	}

	public void atualizarIpi(String ipi) throws Exception {
		Entidade.Atributo atributoIpi = (Entidade.Atributo) this
				.obterAtributo("ipi");
		atributoIpi.atualizarValor(ipi);
	}

	public String obterIpi() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select valor from entidade_atributo where nome='ipi' and entidade=?");
		query.addLong(this.obterId());
		String ipi = query.executeAndGetFirstRow().getString("valor");
		return ipi;
	}

	public String obterTipoProduto() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo from produto where id=?");
		query.addLong(this.obterId());
		this.tipoProduto = query.executeAndGetFirstRow().getString("tipo");
		return this.tipoProduto;
	}

	public double obterQuantidadeMinima() throws Exception {
		double retorno = 0;
		String quantidade_minima = this.obterAtributo("quantidade_minima")
				.obterValor();
		if (quantidade_minima != null && !quantidade_minima.equals(""))
			retorno = Double.parseDouble(quantidade_minima);
		return retorno;
	}

	public double obterReservaDeEntrada() throws Exception {
		double retorno = 0;
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select produto,reserva_entrada from estoque_produto where produto=?");
		query.addLong(this.obterId());
		retorno = query.executeAndGetFirstRow().getDouble("reserva_entrada");
		return retorno;
	}

	public double obterReservaDeSaida() throws Exception {
		double retorno = 0;
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select produto, reserva_saida from estoque_produto where produto=?");
		query.addLong(this.obterId());
		retorno = query.executeAndGetFirstRow().getDouble("reserva_saida");
		return retorno;
	}

	public Status obterStatus(int sequencia) throws Exception {
		return (Produto.Status) this.obterStatus().get(new Integer(sequencia));
	}

	public Map obterStatus() throws Exception {
		Map status = new TreeMap();

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from produto_status where id=?");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
			status.put(new Integer(rows[i].getInt("sequencia")),
					new StatusImpl(rows[i].getString("status"), rows[i]
							.getString("acao"), rows[i].getInt("sequencia")));

		return status;
	}

	public void removerStatus(Produto.Status status) throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate("crm",
				"delete from produto_status where id=? and sequencia=?");
		delete.addLong(this.obterId());
		delete.addInt(status.obterSequencia());

		delete.execute();
	}

	public void adicionarStatus(String status, String acao) throws Exception {

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select MAX(sequencia) as mx from produto_status where id=?");
		query.addLong(this.obterId());

		int sequencia = query.executeAndGetFirstRow().getInt("mx") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate("crm",
						"insert into produto_status (id, status, sequencia, acao) values(?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addString(status);
		insert.addInt(sequencia);
		insert.addString(acao);

		insert.execute();
	}

	public void adicionarStatusAgendaProcesso(String status, int dia)
			throws Exception {

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select MAX(sequencia) as mx from produto_status_agenda_processo where id=?");
		query.addLong(this.obterId());

		int sequencia = query.executeAndGetFirstRow().getInt("mx") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"crm",
						"insert into produto_status_agenda_processo (id, status, dia, sequencia) values(?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addString(status);
		insert.addInt(dia);
		insert.addInt(sequencia);

		insert.execute();
	}

	public Map obterStatusAgendaProcesso() throws Exception {
		Map status = new TreeMap();

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from produto_status_agenda_processo where id=?");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
			status
					.put(new Integer(rows[i].getInt("sequencia")),
							new StatusAgendaProcessoImpl(rows[i]
									.getString("status"),
									rows[i].getInt("dia"), rows[i]
											.getInt("sequencia")));

		return status;
	}

	public StatusAgendaProcesso obterStatusAgendaProcesso(int sequencia)
			throws Exception {
		return (Produto.StatusAgendaProcesso) this.obterStatusAgendaProcesso()
				.get(new Integer(sequencia));
	}

	public StatusAgenda obterStatusAgenda(int sequencia) throws Exception {
		return (Produto.StatusAgenda) this.obterStatusAgenda().get(
				new Integer(sequencia));
	}

	public Map obterStatusAgenda() throws Exception {
		Map status = new TreeMap();

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from produto_status_agenda where id=?");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
			status.put(new Integer(rows[i].getInt("sequencia")),
					new StatusAgendaImpl(rows[i].getString("status"), rows[i]
							.getInt("sequencia")));

		return status;
	}

	public void removerStatusAgendaProcesso(Produto.StatusAgendaProcesso status)
			throws Exception {
		SQLUpdate delete = this
				.getModelManager()
				.createSQLUpdate("crm",
						"delete from produto_status_agenda_processo where id=? and sequencia=?");
		delete.addLong(this.obterId());
		delete.addInt(status.obterSequencia());

		delete.execute();
	}

	public void removerStatusAgenda(Produto.StatusAgenda status)
			throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate("crm",
				"delete from produto_status_agenda where id=? and sequencia=?");
		delete.addLong(this.obterId());
		delete.addInt(status.obterSequencia());

		delete.execute();
	}

	public void adicionarStatusAgenda(String status) throws Exception {

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select MAX(sequencia) as mx from produto_status_agenda where id=?");
		query.addLong(this.obterId());

		int sequencia = query.executeAndGetFirstRow().getInt("mx") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate("crm",
						"insert into produto_status_agenda (id, status, sequencia) values(?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addString(status);
		insert.addInt(sequencia);

		insert.execute();
	}

	public double obterQuantidadeAtual() throws Exception {
		double retorno = 0;
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select produto, quantidade from estoque_produto where produto=?");
		query.addLong(this.obterId());
		retorno = query.executeAndGetFirstRow().getDouble("quantidade");
		return retorno;
	}

	public double obterQuantidadeDisponivel() throws Exception {
		double quantidade = this.obterQuantidadeAtual()
				- this.obterReservaDeSaida();
		if (quantidade < 0)
			return 0;
		else
			return quantidade;
	}

	public double obterSugestao() throws Exception {
		double sugestao = this.obterQuantidadeMinima()
				- this.obterQuantidadeDisponivel();
		if (sugestao > 0)
			return sugestao;
		else
			return 0;
	}

	public void adicionarFornecedor(Entidade fornecedor, String codigoExterno,
			double valor) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"select count(*) quantidade from produto_fornecedor where id=? and fornecedor=?");
		query.addLong(this.obterId());
		query.addLong(fornecedor.obterId());
		if (query.executeAndGetFirstRow().getLong("quantidade") == 1) {
			SQLUpdate update = this
					.getModelManager()
					.createSQLUpdate(
							"update produto_fornecedor set codigo_externo=? , valor=? where id=? and fornecedor=?");
			update.addString(codigoExterno);
			update.addDouble(valor);
			update.addLong(this.obterId());
			update.addLong(fornecedor.obterId());
			update.execute();
		} else {
			SQLUpdate update = this
					.getModelManager()
					.createSQLUpdate(
							"insert into produto_fornecedor (id, fornecedor, codigo_externo, valor) values (?, ?, ?, ?)");
			update.addLong(this.obterId());
			update.addLong(fornecedor.obterId());
			update.addString(codigoExterno);
			update.addDouble(valor);
			update.execute();
		}
		this.fornecedores = null;
	}

	public void adicionarPreco(String tipo, String moeda, double valor)
			throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select count(*) quantidade from produto_preco where id=? and tipo=?");
		query.addLong(this.obterId());
		query.addString(tipo);
		if (query.executeAndGetFirstRow().getLong("quantidade") == 1) {
			if (valor == 0) {
				SQLUpdate update = this.getModelManager().createSQLUpdate(
						"delete from produto_preco where id=? and tipo=?");
				update.addLong(this.obterId());
				update.addString(tipo);
				update.execute();
			} else {
				SQLUpdate update = this
						.getModelManager()
						.createSQLUpdate(
								"update produto_preco set moeda=?, valor=? where id=? and tipo=?");
				update.addString(moeda);
				update.addDouble(valor);
				update.addLong(this.obterId());
				update.addString(tipo);
				update.execute();
			}
		} else {
			if (valor != 0) {
				SQLUpdate update = this
						.getModelManager()
						.createSQLUpdate(
								"insert into produto_preco (id, tipo, moeda, valor) values (?, ?, ?, ?)");
				update.addLong(this.obterId());
				update.addString(tipo);
				update.addString(moeda);
				update.addDouble(valor);
				update.execute();
			}
		}
	}

	public void atribuirApelidos(Set apelidos) throws Exception {
		this.apelidos = apelidos;
	}

	public void atribuirTipoProduto(String tipo) throws Exception {
		this.tipoProduto = tipo;
	}

	public void atribuirAtivo(boolean ativo) throws Exception {
		this.ativo = new Boolean(ativo);
	}

	public void atribuirCodigoExterno(String codigoExterno) throws Exception {
		this.codigoExterno = codigoExterno;
	}

	public void atribuirUnidade(String unidade) throws Exception {
		this.unidade = unidade;
	}

	public void atualizar() throws Exception {
		super.atualizar();
		if (this.apelidos != null) {
			SQLUpdate update1 = this.getModelManager().createSQLUpdate(
					"delete from produto_apelido where id=?");
			update1.addLong(this.obterId());
			update1.execute();
			for (Iterator i = this.apelidos.iterator(); i.hasNext();) {
				SQLUpdate update2 = this
						.getModelManager()
						.createSQLUpdate(
								"insert into produto_apelido (id, apelido) values (?, ?)");
				update2.addLong(this.obterId());
				update2.addString((String) i.next());
				update2.execute();
			}
		}
		if (this.ativo != null) {
			SQLUpdate update = this.getModelManager().createSQLUpdate(
					"update produto set ativo=? where id=?");
			update.addString(this.ativo.booleanValue() ? "1" : "0");
			update.addLong(this.obterId());
			update.execute();
		}
		if (this.codigoExterno != null) {
			ProdutoHome produtoHome = (ProdutoHome) this.getModelManager()
					.getHome("ProdutoHome");
			Produto p = produtoHome
					.obterProdutoPorCodigoExterno(this.codigoExterno);
			if (p != null && p.obterId() != this.obterId())
				throw new Exception("O Código externo " + this.codigoExterno
						+ " já está sendo utilizado pelo produto "
						+ p.obterNome());
			SQLUpdate update = this.getModelManager().createSQLUpdate(
					"update produto set codigo_externo=? where id=?");
			update.addString(this.codigoExterno);
			update.addLong(this.obterId());
			update.execute();
		}
		if (this.unidade != null) {
			SQLUpdate update = this.getModelManager().createSQLUpdate(
					"update produto set unidade=? where id=?");
			update.addString(this.unidade);
			update.addLong(this.obterId());
			update.execute();
		}
	}

	public boolean eEspecial() throws Exception {
		if (this.especial == null) {
			String produtosEspeciais = InfraProperties.getInstance()
					.getProperty("produto.especiais");
			this.especial = new Boolean(produtosEspeciais.indexOf(this
					.obterApelido()) >= 0);
		}
		return this.especial.booleanValue();
	}

	public boolean estaAtivo() throws Exception {
		if (this.ativo == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select ativo from produto where id=?");
			query.addLong(this.obterId());
			String ativo = query.executeAndGetFirstRow().getString("ativo");
			if (ativo != null)
				this.ativo = new Boolean(ativo.equals("1"));
		}
		return this.ativo.booleanValue();
	}

	public void excluir() throws Exception {
		super.excluir();
		/*
		 * SQLUpdate update1 = this.getModelManager().createSQLUpdate("delete
		 * from produto_fornecedor where id=?");
		 * update1.addLong(this.obterId()); update1.execute(); SQLUpdate update2 =
		 * this.getModelManager().createSQLUpdate("delete from produto_apelido
		 * where id=?"); update2.addLong(this.obterId()); update2.execute();
		 * SQLUpdate update3 = this.getModelManager().createSQLUpdate("delete
		 * from produto_preco where id=?"); update3.addLong(this.obterId());
		 * update3.execute(); SQLUpdate update4 =
		 * this.getModelManager().createSQLUpdate("delete from produto where
		 * id=?"); update4.addLong(this.obterId()); update4.execute();
		 */
	}

	public void incluir() throws Exception {
		super.incluir();

		/*
		 * if (this.codigoExterno!= null && !this.codigoExterno.equals("")) {
		 * ProdutoHome produtoHome = (ProdutoHome)
		 * this.getModelManager().getHome("ProdutoHome"); Produto p =
		 * produtoHome.obterProdutoPorCodigoExterno(this.codigoExterno); if (p !=
		 * null) throw new Exception("O Código externo " + this.codigoExterno + "
		 * já está sendo utilizado pelo produto " + p.obterNome()); } else throw
		 * new Exception("Preencha o Código Externo do Produto");
		 * 
		 * if(this.obterNome()==null) throw new Exception("Preencha o Nome do
		 * Produto");
		 * 
		 * if(this.obterApelido()==null) throw new Exception("Preencha o Apelido
		 * do Produto");
		 */

		/*
		 * SQLUpdate update1 = this.getModelManager().createSQLUpdate("insert
		 * into produto (id, codigo_externo, unidade, ativo) values (?, ?, ?,
		 * ?)"); update1.addLong(this.obterId());
		 * update1.addString(this.codigoExterno);
		 * update1.addString(this.unidade);
		 * update1.addString(this.ativo.booleanValue() ? "1" : "0");
		 * update1.execute(); if (this.apelidos != null) for (Iterator i =
		 * this.apelidos.iterator(); i.hasNext();) { SQLUpdate update2 =
		 * this.getModelManager().createSQLUpdate("insert into produto_apelido
		 * (id, apelido) values (?, ?)"); update2.addLong(this.obterId());
		 * update2.addString((String) i.next()); update2.execute(); }
		 */
	}

	public Collection obterApelidos() throws Exception {
		if (this.apelidos == null) {
			this.apelidos = new HashSet();
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select apelido from produto_apelido where id=?");
			query.addLong(this.obterId());
			SQLRow[] rows = query.execute();
			for (int i = 0; i < rows.length; i++)
				this.apelidos.add(rows[i].getString("apelido"));
		}
		return this.apelidos;
	}

	public String obterCodigoExterno() throws Exception {
		if (this.codigoExterno == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select codigo_externo from produto where id=?");
			query.addLong(this.obterId());
			this.codigoExterno = query.executeAndGetFirstRow().getString(
					"codigo_externo");
		}
		return this.codigoExterno;
	}

	public Fornecedor obterFornecedor(Entidade fornecedor) throws Exception {
		if (this.fornecedores == null)
			this.obterFornecedores();
		return (Fornecedor) this.fornecedores.get(fornecedor);
	}

	public Collection obterFornecedores() throws Exception {
		if (this.fornecedores == null) {
			EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager()
					.getHome("EntidadeHome");
			this.fornecedores = new HashMap();
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery("crm",
							"select fornecedor, codigo_externo, valor from produto_fornecedor where id=?");
			query.addLong(this.obterId());
			SQLRow[] rows = query.execute();
			for (int i = 0; i < rows.length; i++) {
				Entidade fornecedor = entidadeHome.obterEntidadePorId(rows[i]
						.getLong("fornecedor"));
				this.fornecedores.put(fornecedor, new FornecedorImpl(
						fornecedor, rows[i].getString("codigo_externo"),
						rows[i].getDouble("valor")));
			}
		}
		return this.fornecedores.values();
	}

	public Collection obterOcorrencias() throws Exception {
		OcorrenciaHome ocorrenciaHome = (OcorrenciaHome) this.getModelManager()
				.getHome("OcorrenciaHome");
		return ocorrenciaHome.obterOcorrenciasPorProduto(this);
	}

	public Preco obterPreco(String tipo) throws Exception {
		if (this.precos == null)
			this.obterPrecos();
		return (Preco) this.precos.get(tipo);
	}

	public Collection obterPrecos() throws Exception {
		if (this.precos == null) {
			this.precos = new HashMap();
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select tipo, moeda, valor from produto_preco where id=?");
			query.addLong(this.obterId());
			SQLRow[] rows = query.execute();
			for (int i = 0; i < rows.length; i++) {
				String tipo = rows[i].getString("tipo");
				String moeda = rows[i].getString("moeda");
				double valor = rows[i].getDouble("valor");
				this.precos.put(tipo, new PrecoImpl(tipo, moeda, valor));
			}
		}
		return this.precos.values();
	}

	public double obterValorFornecedor(Entidade fornecedor) throws Exception {

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select valor from produto_fornecedor where id=? and fornecedor=?");
		query.addLong(this.obterId());
		query.addLong(fornecedor.obterId());
		return query.executeAndGetFirstRow().getDouble("valor");
	}

	public String obterUnidade() throws Exception {
		if (this.unidade == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select unidade from produto where id=?");
			query.addLong(this.obterId());
			this.unidade = query.executeAndGetFirstRow().getString("unidade");
		}
		return this.unidade;
	}

	public boolean permiteExcluir() throws Exception {
		boolean permite = super.permiteExcluir();
		/*
		 * if (permite) { OcorrenciaHome ocorrenciaHome = (OcorrenciaHome)
		 * this.getModelManager().getHome("OcorrenciaHome"); permite =
		 * !ocorrenciaHome.possuiOcorrencias(this); }
		 */return permite;
	}

	public void removerFornecedor(Entidade fornecedor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"delete from produto_fornecedor where id=? and fornecedor=?");
		update.addLong(this.obterId());
		update.addLong(fornecedor.obterId());
		update.execute();
		this.fornecedores = null;
	}

	public void removerPreco(Preco preco) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"delete from produto_preco where id=? and tipo=?");
		update.addLong(this.obterId());
		update.addString(preco.obterTipo());
		update.execute();
		this.precos = null;
	}
}