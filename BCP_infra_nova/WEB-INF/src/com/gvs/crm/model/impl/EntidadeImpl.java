package com.gvs.crm.model.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Inscricao;
import com.gvs.crm.model.Log;
import com.gvs.crm.model.MovimentacaoFinanceiraConta;
import com.gvs.crm.model.Raiz;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.config.InfraProperties;
import infra.model.Entity;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class EntidadeImpl extends Entity implements Entidade
{
	public class AtributoImpl implements Atributo
	{
		private EntidadeImpl entidade;

		private String nome;

		private String properties;

		private int tamanho;

		private String tipo;

		private String titulo;

		private String valor;

		private String valoresTitulos;

		private int ordem;

		AtributoImpl(EntidadeImpl entidade, String nome)
		{
			this.entidade = entidade;
			this.nome = nome;
		}

		public void atualizarValor(String valor) throws Exception {
			SQLQuery query = this.entidade
					.getModelManager()
					.createSQLQuery(
							"select valor from entidade_atributo where entidade=? and nome=?");
			query.addLong(this.entidade.obterId());
			query.addString(this.nome);
			SQLRow[] rows = query.execute();
			if (rows.length == 0) {
				SQLUpdate insert = this.entidade
						.getModelManager()
						.createSQLUpdate(
								"insert into entidade_atributo (entidade, nome, valor) values (?, ?, ?)");
				insert.addLong(this.entidade.obterId());
				insert.addString(this.nome);
				insert.addString(valor);
				insert.execute();
				System.out
						.println("insert into entidade_atributo (entidade, nome, valor) values ("
								+ this.entidade.obterId()
								+ ", "
								+ this.nome
								+ ", " + valor);
			} else {
				SQLUpdate update = this.entidade
						.getModelManager()
						.createSQLUpdate(
								"update entidade_atributo set valor=? where entidade=? and nome=?");
				update.addString(valor);
				update.addLong(this.entidade.obterId());
				update.addString(this.nome);
				update.execute();
				System.out.println("update entidade_atributo set valor="
						+ valor + " where entidade=" + this.entidade.obterId()
						+ " and nome=" + this.nome);
			}
			this.valor = valor;
		}

		private void loadProperties(String nome) throws Exception {
			if (this.properties == null) {
				this.properties = InfraProperties.getInstance().getProperty(
						this.entidade.obterClasse().toLowerCase()
								+ ".atributo." + this.nome);
				StringTokenizer st = new StringTokenizer(this.properties, ",");
				this.titulo = st.nextToken();
				this.tipo = st.nextToken();
				if (!this.tipo.equals("D"))
					this.tamanho = Integer.parseInt(st.nextToken());
				if (this.tipo.equals("S"))
					this.valoresTitulos = st.nextToken();

				this.ordem = Integer.parseInt(st.nextToken());
			}
		}

		public String obterNome() throws Exception {
			return this.nome;
		}

		public int obterTamanho() throws Exception {
			if (this.properties == null)
				this.loadProperties(this.nome);
			return this.tamanho;
		}

		public char obterTipo() throws Exception {
			if (this.properties == null)
				this.loadProperties(this.nome);
			return this.tipo.charAt(0);
		}

		public String obterValoresTitulos() throws Exception {
			if (this.properties == null)
				this.loadProperties(this.nome);
			return this.valoresTitulos;
		}

		public String obterTitulo() throws Exception {
			if (this.properties == null)
				this.loadProperties(this.nome);
			return this.titulo;
		}

		public int obterOrdem() throws Exception {
			if (this.properties == null)
				this.loadProperties(this.nome);
			return this.ordem;
		}

		public String obterValor() throws Exception {
			if (this.valor == null) {
				SQLQuery query = this.entidade
						.getModelManager()
						.createSQLQuery("crm",
								"select valor from entidade_atributo where entidade=? and nome=?");
				query.addLong(this.entidade.obterId());
				query.addString(this.nome);
				SQLRow[] rows = query.execute();
				if (rows.length == 1)
					this.valor = rows[0].getString("valor");
				else
					this.valor = "";
			}
			return this.valor;
		}
	}

	public class ContatoImpl implements Contato {
		private EntidadeImpl entidade;

		private int id;

		private String nome;

		private String valor;

		private String nomeContato;

		ContatoImpl(EntidadeImpl entidade, int id, String nome, String valor,
				String nomeContato) {
			this.entidade = entidade;
			this.id = id;
			this.nome = nome;
			this.valor = valor;
			this.nomeContato = nomeContato;
		}

		public void atualizarValor(String valor, String nomeContato)
				throws Exception {
			SQLUpdate update = this.entidade
					.getModelManager()
					.createSQLUpdate(
							"update entidade_contato set valor=?, nome_contato=? where entidade=? and id=?");
			update.addString(valor);
			update.addString(nomeContato);
			update.addLong(this.entidade.obterId());
			update.addInt(this.id);
			update.execute();
			this.valor = valor;
		}

		public Entidade obterEntidade() throws Exception {
			return this.entidade;
		}

		public int obterId() throws Exception {
			return this.id;
		}

		public String obterNome() throws Exception {
			return this.nome;
		}

		public String obterValor() throws Exception {
			return this.valor;
		}

		public String obterNomeContato() throws Exception {
			if (this.nomeContato == null)
				return "";
			else
				return this.nomeContato;
		}
	}

	public class EnderecoImpl implements Endereco {
		private String bairro;

		private String cep;

		private String cidade;

		private String complemento;

		private EntidadeImpl entidade;

		private String estado;

		private int id;

		private String nome;

		private String numero;

		private String pais;

		private String rua;

		EnderecoImpl() {
		}

		public void atribuirBairro(String bairro) {
			this.bairro = bairro;
		}

		public void atribuirCep(String cep) {
			this.cep = cep;
		}

		public void atribuirCidade(String cidade) {
			this.cidade = cidade;
		}

		public void atribuirComplemento(String complemento) {
			this.complemento = complemento;
		}

		void atribuirEntidade(EntidadeImpl entidade) {
			this.entidade = entidade;
		}

		public void atribuirEstado(String estado) {
			this.estado = estado;
		}

		void atribuirId(int id) {
			this.id = id;
		}

		public void atribuirNome(String nome) {
			this.nome = nome;
		}

		public void atribuirNumero(String numero) {
			this.numero = numero;
		}

		public void atribuirPais(String pais) {
			this.pais = pais;
		}

		public void atribuirRua(String rua) {
			this.rua = rua;
		}

		public void atualizar() throws Exception {
			SQLUpdate update = this.entidade
					.getModelManager()
					.createSQLUpdate(
							"update entidade_endereco set nome=?, cep=?, rua=?, numero=?, complemento=?, bairro=?, cidade=?, estado=?, pais=? where entidade=? and id=?");
			update.addString(this.nome);
			update.addString(this.cep);
			update.addString(this.rua);
			update.addString(this.numero);
			update.addString(this.complemento);
			update.addString(this.bairro);
			update.addString(this.cidade);
			update.addString(this.estado);
			update.addString(this.pais);
			update.addLong(this.entidade.obterId());
			update.addInt(this.id);
			update.execute();
		}

		public void incluir() throws Exception {
			SQLQuery query = this.entidade
					.getModelManager()
					.createSQLQuery(
							"select max(id) as MX from entidade_endereco where entidade=?");
			query.addLong(this.entidade.obterId());
			this.id = query.executeAndGetFirstRow().getInt("MX") + 1;
			SQLUpdate insert = this.entidade
					.getModelManager()
					.createSQLUpdate(
							"insert into entidade_endereco (entidade, id, nome, cep, rua, numero, complemento, bairro, cidade, estado, pais) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			insert.addLong(this.entidade.obterId());
			insert.addLong(this.id);
			insert.addString(this.nome);
			insert.addString(this.cep);
			insert.addString(this.rua);
			insert.addString(this.numero);
			insert.addString(this.complemento);
			insert.addString(this.bairro);
			insert.addString(this.cidade);
			insert.addString(this.estado);
			insert.addString(this.pais);
			insert.execute();
		}

		public String obterBairro() {
			return this.bairro;
		}

		public String obterCep() {
			return this.cep;
		}

		public String obterCidade() {
			return this.cidade;
		}

		public String obterComplemento() {
			return this.complemento;
		}

		public Entidade obterEntidade() {
			return this.entidade;
		}

		public String obterEstado() {
			return this.estado;
		}

		public int obterId() {
			return this.id;
		}

		public String obterNome() {
			return this.nome;
		}

		public String obterNumero() {
			return this.numero;
		}

		public String obterPais() {
			return this.pais;
		}

		public String obterRua() {
			return this.rua;
		}
	}

	private String ruc;

	private String sigla;

	private String apelido;

	private Date atualizacao;

	private String classe;

	private Map contatos;

	private Date criacao;

	private String descricaoClasse;

	private Entidade empresa;

	private Entidade aseguradora;

	private Map enderecos;

	private Map entidadeAtributos = new TreeMap();

	private long id;

	private long idResponsavel;

	private long idSuperior;

	private String nome;

	private List nomesContatos;

	private List nomesEnderecos;

	private Usuario responsavel;

	private Entidade superior;

	private Collection superiores;

	public void adicionarContato(String nome, String valor, String nomeContato)
			throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery(
				"select max(id) as MX from entidade_contato where entidade=?");
		query.addLong(this.obterId());
		long id = query.executeAndGetFirstRow().getLong("MX") + 1;
		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into entidade_contato (entidade, id, nome, valor, nome_contato) values (?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addLong(id);
		insert.addString(nome);
		insert.addString(valor);
		insert.addString(nomeContato);
		insert.execute();
	}

	public void atribuirApelido(String apelido) throws Exception {
		this.apelido = apelido;
	}

	public void atribuirRUC(String ruc) throws Exception {
		this.ruc = ruc;
	}

	public void atribuirSigla(String sigla) throws Exception {
		this.sigla = sigla;
	}

	/*
	 * protected void atribuirId(long id) throws Exception { this.id = id; }
	 */

	public void atribuirIdResponsavel(long idResponsavel) {
		this.idResponsavel = idResponsavel;
	}

	public void atribuirIdSuperior(long idSuperior) {
		this.idSuperior = idSuperior;
	}

	public void atribuirNome(String nome) throws Exception {
		this.nome = nome;
	}

	public void atribuirResponsavel(Usuario responsavel) throws Exception {
		this.responsavel = responsavel;
	}

	public void atribuirSuperior(Entidade superior) throws Exception {
		this.superior = superior;
	}

	public void atualizar() throws Exception {
		if (this.apelido != null) {
			if (!this.apelido.equals("")) {
				SQLQuery query = this.getModelManager().createSQLQuery(
						"select id from entidade where apelido=?");
				query.addString(this.apelido);
				long id = query.executeAndGetFirstRow().getLong("id");
				if (id != 0 && id != this.obterId())
					throw new Exception("O apelido '" + this.apelido
							+ "' já está sendo utilizado por outra entidade");
			}
			SQLUpdate update = this.getModelManager().createSQLUpdate(
					"update entidade set apelido=? where id=?");
			update.addString(this.apelido);
			update.addLong(this.obterId());
			update.execute();
		}

		if (this.sigla != null && !this.sigla.equals("")) {
			SQLQuery query = this.getModelManager().createSQLQuery(
					"select id from entidade where sigla=?");
			query.addString(this.sigla);
			long id = query.executeAndGetFirstRow().getLong("id");
			if (id != 0 && id != this.obterId())
				throw new Exception("A Sigla '" + this.sigla
						+ "' já está sendo utilizada por outra entidade");

			SQLUpdate update = this.getModelManager().createSQLUpdate(
					"update entidade set sigla=? where id=?");
			update.addString(this.sigla);
			update.addLong(this.obterId());
			update.execute();
		}

		if (this.ruc != null && !this.ruc.equals("")) {
			SQLQuery query = this.getModelManager().createSQLQuery(
					"select id from entidade where ruc=?");
			query.addString(this.ruc);
			long id = query.executeAndGetFirstRow().getLong("id");
			if (id != 0 && id != this.obterId())
				throw new Exception("O RUC '" + this.ruc
						+ "' já está sendo utilizada por outra entidade");

			SQLUpdate update = this.getModelManager().createSQLUpdate(
					"update entidade set ruc=? where id=?");
			update.addString(this.ruc);
			update.addLong(this.obterId());
			update.execute();
		}

		if (this.nome != null) {
			if (this.nome.equals(""))
				throw new Exception("O nome da entidade deve ser preenchido");
			SQLUpdate update = this.getModelManager().createSQLUpdate(
					"update entidade set nome=? where id=?");
			update.addString(this.nome);
			update.addLong(this.obterId());
			update.execute();
		}
		if (this.responsavel != null) {
			SQLUpdate update = this.getModelManager().createSQLUpdate(
					"update entidade set responsavel=? where id=?");
			update.addLong(this.responsavel.obterId());
			update.addLong(this.obterId());
			update.execute();
		}
		if (this.superior != null) {
			SQLUpdate update = this.getModelManager().createSQLUpdate(
					"update entidade set superior=? where id=?");
			update.addLong(this.superior.obterId());
			update.addLong(this.obterId());
			update.execute();
		}
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update entidade set atualizacao=? where id=?");
		update.addLong(new Date().getTime());
		update.addLong(this.obterId());
		update.execute();
	}

	protected void atualizarClasse(String novaClasse) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update entidade set classe=? where id=?");
		update.addString(novaClasse);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarRuc(String ruc) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update entidade set ruc=? where id=?");
		update.addString(ruc);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarSigla(String sigla) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update entidade set sigla=? where id=?");
		update.addString(sigla);
		update.addLong(this.obterId());
		update.execute();
	}

	public void apagarTotalizacaoExistente(String mesAno) throws Exception {
		SQLUpdate update1 = this.getModelManager().createSQLUpdate(
				"delete from relatorio where mes_ano=? and seguradora=?");
		update1.addString(mesAno);
		update1.addLong(this.obterId());
		update1.execute();
	}

	public boolean equals(Object object) {
		if (object instanceof EntidadeImpl)
			return ((EntidadeImpl) object).obterId() == this.id;
		else
			return false;
	}

	public void excluir() throws Exception {
		if (this.obterId() != 0) {
			for (Iterator i = this.obterInferiores().iterator(); i.hasNext();) {
				Entidade e = (Entidade) i.next();
				e.excluir();
			}

			SQLUpdate update1 = this.getModelManager().createSQLUpdate(
					"delete from entidade_contato where entidade=?");
			update1.addLong(this.obterId());
			update1.execute();

			SQLUpdate update2 = this.getModelManager().createSQLUpdate(
					"delete from entidade_endereco where entidade=?");
			update2.addLong(this.obterId());
			update2.execute();

			SQLUpdate update3 = this.getModelManager().createSQLUpdate(
					"delete from entidade_atributo where entidade=?");
			update3.addLong(this.obterId());
			update3.execute();

			SQLUpdate update4 = this.getModelManager().createSQLUpdate(
					"delete from entidade where id=?");
			update4.addLong(this.obterId());
			update4.execute();

			SQLUpdate update5 = this.getModelManager().createSQLUpdate(
					"delete from corretora where id=?");
			update5.addLong(this.obterId());
			update5.execute();

			SQLUpdate update6 = this.getModelManager().createSQLUpdate(
					"delete from pessoa_formacao where entidade=?");
			update6.addLong(this.obterId());
			update6.execute();

			SQLUpdate update7 = this.getModelManager().createSQLUpdate(
					"delete from aseguradora_acionista where entidade=?");
			update7.addLong(this.obterId());
			update7.execute();

			SQLUpdate update8 = this.getModelManager().createSQLUpdate(
					"delete from aseguradora_coasegurador where entidade=?");
			update8.addLong(this.obterId());
			update8.execute();

			SQLUpdate update9 = this.getModelManager().createSQLUpdate(
					"delete from aseguradora_filial where entidade=?");
			update9.addLong(this.obterId());
			update9.execute();

			SQLUpdate update10 = this.getModelManager().createSQLUpdate(
					"delete from aseguradora_fusao where entidade=?");
			update10.addLong(this.obterId());
			update10.execute();

			SQLUpdate update11 = this.getModelManager().createSQLUpdate(
					"delete from aseguradora_reaseguradora where entidade=?");
			update11.addLong(this.obterId());
			update11.execute();

			SQLUpdate update12 = this.getModelManager().createSQLUpdate(
					"delete from auditor_externo where id=?");
			update12.addLong(this.obterId());
			update12.execute();

			SQLUpdate update13 = this.getModelManager().createSQLUpdate(
					"delete from auditor_externo_cliente where entidade=?");
			update13.addLong(this.obterId());
			update13.execute();

			SQLUpdate update14 = this.getModelManager().createSQLUpdate(
					"delete from auditor_externo_ramo where entidade=?");
			update14.addLong(this.obterId());
			update14.execute();

			SQLUpdate update15 = this.getModelManager().createSQLUpdate(
					"delete from auditor_externo_servicos where entidade=?");
			update15.addLong(this.obterId());
			update15.execute();

			SQLUpdate update16 = this.getModelManager().createSQLUpdate(
					"delete from auxiliar_seguro where id=?");
			update16.addLong(this.obterId());
			update16.execute();

			SQLUpdate update17 = this.getModelManager().createSQLUpdate(
					"delete from auxiliar_seguro_ramo where entidade=?");
			update17.addLong(this.obterId());
			update17.execute();

			SQLUpdate update19 = this.getModelManager().createSQLUpdate(
					"delete from classificacao_contas where id=?");
			update19.addLong(this.obterId());
			update19.execute();

			SQLUpdate update20 = this.getModelManager().createSQLUpdate(
					"delete from classificacao_documento where id=?");
			update20.addLong(this.obterId());
			update20.execute();

			SQLUpdate update21 = this.getModelManager().createSQLUpdate(
					"delete from consistencia where entidade=?");
			update21.addLong(this.obterId());
			update21.execute();

			SQLUpdate update22 = this.getModelManager().createSQLUpdate(
					"delete from conta where id=?");
			update22.addLong(this.obterId());
			update22.execute();

			SQLUpdate update23 = this.getModelManager().createSQLUpdate(
					"delete from corretora where id=?");
			update23.addLong(this.obterId());
			update23.execute();

			SQLUpdate update24 = this.getModelManager().createSQLUpdate(
					"delete from corretora_ramo where entidade=?");
			update24.addLong(this.obterId());
			update24.execute();

			SQLUpdate update26 = this.getModelManager().createSQLUpdate(
					"delete from grupo where id=?");
			update26.addLong(this.obterId());
			update26.execute();

			SQLUpdate update28 = this.getModelManager().createSQLUpdate(
					"delete from grupo where membro_id=?");
			update28.addLong(this.obterId());
			update28.execute();

			SQLUpdate update27 = this.getModelManager().createSQLUpdate(
					"delete reaseguradora_classificacao where entidade=?");
			update27.addLong(this.obterId());
			update27.execute();

			SQLUpdate update29 = this.getModelManager().createSQLUpdate(
					"delete oficial where id=?");
			update29.addLong(this.obterId());
			update29.execute();

		}
	}

	public void excluirDuplicidade(Date dataInicio, Date dataFim) throws Exception {

		Collection superiores = new ArrayList();
		
		String mes = new SimpleDateFormat("MM").format(dataInicio);
		String ano = new SimpleDateFormat("yyyy").format(dataInicio);
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,movimentacao_financeira_conta where evento.id = movimentacao_financeira_conta.id and origem = ? and data_prevista>= ? and data_prevista<= ?");
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			MovimentacaoFinanceiraConta mf = (MovimentacaoFinanceiraConta) home.obterEventoPorId(id);
			
			/*String mes = new SimpleDateFormat("MM").format(data);
			String ano = new SimpleDateFormat("yyyy").format(data);

			String mesEvento = new SimpleDateFormat("MM").format(mf
					.obterDataPrevista());
			String anoEvento = new SimpleDateFormat("yyyy").format(mf
					.obterDataPrevista());

			if (mes.equals(mesEvento) && ano.equals(anoEvento))
			{*/
				SQLUpdate delete = this.getModelManager().createSQLUpdate("crm","delete from relatorio where id=? and mes_ano=? and seguradora=?");
				delete.addLong(mf.obterConta().obterId());
				delete.addString(mes + ano);
				delete.addLong(this.obterId());

				delete.execute();
			//}
			
			for (Iterator j = mf.obterConta().obterSuperiores().iterator(); j.hasNext();) 
			{
				Entidade entidade = (Entidade) j.next();

				if (entidade instanceof ClassificacaoContas)
				{
					if (!superiores.contains(entidade))
					{
						SQLUpdate delete2 = this.getModelManager().createSQLUpdate("crm", "delete from relatorio where id=? and mes_ano=? and seguradora=?");
						delete2.addLong(entidade.obterId());
						delete2.addString(mes + ano);
						delete2.addLong(this.obterId());

						delete2.execute();

						superiores.add(entidade);
					}
				}
			}

			mf.excluir();
		}
		
		/*for (Iterator i = this.obterEventosComoOrigem().iterator(); i.hasNext();) 
		{
			Evento e = (Evento) i.next();

			if (e instanceof MovimentacaoFinanceiraConta) 
			{
				MovimentacaoFinanceiraConta mf = (MovimentacaoFinanceiraConta) e;

				String mes = new SimpleDateFormat("MM").format(data);
				String ano = new SimpleDateFormat("yyyy").format(data);

				String mesEvento = new SimpleDateFormat("MM").format(mf
						.obterDataPrevista());
				String anoEvento = new SimpleDateFormat("yyyy").format(mf
						.obterDataPrevista());

				if (mes.equals(mesEvento) && ano.equals(anoEvento)) {
					SQLUpdate delete = this
							.getModelManager()
							.createSQLUpdate("crm",
									"delete from relatorio where id=? and mes_ano=? and seguradora=?");
					delete.addLong(mf.obterConta().obterId());
					delete.addString(mes + ano);
					delete.addLong(this.obterId());

					delete.execute();

					for (Iterator j = mf.obterConta().obterSuperiores().iterator(); j.hasNext();) 
					{
						Entidade entidade = (Entidade) j.next();

						if (entidade instanceof ClassificacaoContas)
						{
							if (!superiores.contains(entidade))
							{
								SQLUpdate delete2 = this.getModelManager().createSQLUpdate("crm", "delete from relatorio where id=? and mes_ano=? and seguradora=?");
								delete2.addLong(entidade.obterId());
								delete2.addString(mes + ano);
								delete2.addLong(this.obterId());

								delete2.execute();

								superiores.add(entidade);
							}
						}
					}

					mf.excluir();
				}
			}*/
		//}
	}

	protected void atribuirId(long id) throws Exception {
		this.id = id;
	}

	public void incluir() throws Exception {
		if (this.nome == null)
			throw new Exception("O nome da entidade deve ser preenchido");
		if (this.apelido != null && !this.apelido.equals("")) {
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery(
							"select count(id) as quantidade from entidade where apelido=?");
			query.addString(this.apelido);
			if (query.executeAndGetFirstRow().getLong("quantidade") > 0)
				throw new Exception("O apelido '" + this.obterApelido()
						+ "' já está sendo utilizado por outra entidade");
		}

		if (this.ruc != null && !this.ruc.equals("")) {
			SQLQuery query = this.getModelManager().createSQLQuery(
					"select count(id) as quantidade from entidade where ruc=?");
			query.addString(this.apelido);
			if (query.executeAndGetFirstRow().getLong("quantidade") > 0)
				throw new Exception("El RUC '" + this.obterRuc()
						+ "' já está sendo utilizado por outra entidade");
		}

		if (this.sigla != null && !this.sigla.equals("")) {
			SQLQuery query = this.getModelManager().createSQLQuery(
					"select count(id) quantidade from entidade where sigla=?");
			query.addString(this.sigla);
			if (query.executeAndGetFirstRow().getLong("quantidade") > 0)
				throw new Exception("A sigla '" + this.obterSigla()
						+ "' já está sendo utilizada por outra entidade");
		}

		SQLQuery query = this.getModelManager().createSQLQuery(
				"select max(id) as maximo from entidade");
		this.atribuirId(query.executeAndGetFirstRow().getLong("maximo") + 1);

		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"insert into entidade (id, classe, apelido, nome, superior, responsavel, criacao, atualizacao, sigla) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		update.addLong(this.obterId());
		update.addString(this.obterClasse());
		update.addString(this.obterApelido());
		update.addString(this.obterNome());
		update.addLong(this.superior == null ? 0 : this.superior.obterId());
		update.addLong(this.responsavel.obterId());
		update.addLong(new Date().getTime());
		update.addLong(0);
		update.addString(this.obterSigla());

		//System.out.println("insert into entidade (id, classe, apelido, nome,
		// superior, responsavel, criacao, atualizacao, sigla) values
		// ("+this.obterId()+", "+this.obterClasse()+", "+this.obterApelido()+",
		// "+this.obterNome()+", "+this.superior.obterId()+",
		// "+this.responsavel.obterId()+", "+new Date().getTime()+", 0,
		// "+this.obterSigla()+")");

		update.execute();
	}

	public Endereco novoEndereco() throws Exception {
		EnderecoImpl endereco = new EnderecoImpl();
		endereco.atribuirEntidade(this);
		return endereco;
	}

	public String obterApelido() throws Exception {
		if (this.apelido == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select apelido from entidade where id=?");
			query.addLong(this.obterId());
			this.apelido = query.executeAndGetFirstRow().getString("apelido");
		}
		return this.apelido;
	}

	public String obterSigla() throws Exception
	{
		if (this.sigla == null)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select sigla from entidade where id=?");
			query.addLong(this.obterId());
			
			this.sigla = query.executeAndGetFirstRow().getString("sigla");
		}
		return this.sigla;
	}

	public String obterRuc() throws Exception {
		if (this.ruc == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select ruc from entidade where id=?");
			query.addLong(this.obterId());

			this.ruc = query.executeAndGetFirstRow().getString("ruc");
		}

		return this.ruc;
	}

	public Atributo obterAtributo(String nome) throws Exception {
		AtributoImpl entidadeAtributo = (AtributoImpl) this.entidadeAtributos
				.get(nome);
		if (entidadeAtributo == null) {
			this.obterAtributos();
			entidadeAtributo = (AtributoImpl) this.entidadeAtributos.get(nome);
			if (entidadeAtributo == null)
				entidadeAtributo = new AtributoImpl(this, "nome");
		}
		return entidadeAtributo;
	}

	private Collection entidadesNivel1 = new ArrayList();

	private Collection entidadesNivel2 = new ArrayList();

	private Collection entidadesNivel3 = new ArrayList();

	private Collection entidadesNivel4 = new ArrayList();

	private Collection entidadesNivel5 = new ArrayList();

	public Collection obterEntidadesNivel1(Entidade seguradora, String mesAno) throws Exception
	{
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select id from relatorio where seguradora=? and nivel=? and mes_ano=?");
		query.addLong(seguradora.obterId());
		query.addString("Nivel 1");

		String mesAnoModificado = "";

		for (int i = 0; i < mesAno.length(); i++)
		{
			String caracter = mesAno.substring(i, i + 1);

			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
				mesAnoModificado += caracter;
		}

		query.addString(mesAnoModificado);

		SQLRow[] rows = query.execute();

		if (rows.length > 0)
		{
			for (int i = 0; i < rows.length; i++)
				this.entidadesNivel1.add(home.obterEntidadePorId(rows[i].getLong("id")));
		} 
		else
		{
			SQLQuery query2 = this.getModelManager().createSQLQuery("crm","select entidade.id from entidade,classificacao_contas where entidade.id=classificacao_contas.id and nivel=?");
			query2.addString("Nivel 1");

			SQLRow[] rows2 = query2.execute();

			for (int i = 0; i < rows2.length; i++) 
				this.entidadesNivel1.add(home.obterEntidadePorId(rows2[i].getLong("id")));
		}

		return this.entidadesNivel1;
	}

	public Collection obterEntidadesNivel2(Entidade seguradora, String mesAno)
			throws Exception {
		this.entidadesNivel2.addAll(this.obterEntidadesNivel1(seguradora,
				mesAno));

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select id from relatorio where seguradora=? and nivel=? and mes_ano=?");
		query.addLong(seguradora.obterId());
		query.addString("Nivel 2");

		String mesAnoModificado = "";

		for (int i = 0; i < mesAno.length(); i++) {

			String caracter = mesAno.substring(i, i + 1);

			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
				mesAnoModificado += caracter;
		}

		query.addString(mesAnoModificado);

		SQLRow[] rows = query.execute();

		if (rows.length > 0) {
			for (int i = 0; i < rows.length; i++) {
				this.entidadesNivel2.add(home.obterEntidadePorId(rows[i]
						.getLong("id")));
			}
		} else {
			SQLQuery query2 = this
					.getModelManager()
					.createSQLQuery(
							"crm",
							"select entidade.id from entidade,classificacao_contas where entidade.id=classificacao_contas.id and nivel=?");
			query2.addString("Nivel 2");

			SQLRow[] rows2 = query2.execute();

			for (int i = 0; i < rows2.length; i++) {
				this.entidadesNivel2.add(home.obterEntidadePorId(rows2[i]
						.getLong("id")));
			}
		}

		return this.entidadesNivel2;
	}

	public Collection obterEntidadesNivel3(Entidade seguradora, String mesAno)
			throws Exception {
		this.entidadesNivel3.addAll(this.obterEntidadesNivel2(seguradora,
				mesAno));

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select id from relatorio where seguradora=? and nivel=? and mes_ano=?");
		query.addLong(seguradora.obterId());
		query.addString("Nivel 3");

		String mesAnoModificado = "";

		for (int i = 0; i < mesAno.length(); i++) {

			String caracter = mesAno.substring(i, i + 1);

			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
				mesAnoModificado += caracter;
		}

		query.addString(mesAnoModificado);

		SQLRow[] rows = query.execute();

		if (rows.length > 0) {
			for (int i = 0; i < rows.length; i++) {
				this.entidadesNivel3.add(home.obterEntidadePorId(rows[i]
						.getLong("id")));
			}
		} else {
			SQLQuery query2 = this
					.getModelManager()
					.createSQLQuery(
							"crm",
							"select entidade.id from entidade,classificacao_contas where entidade.id=classificacao_contas.id and nivel=?");
			query2.addString("Nivel 3");

			SQLRow[] rows2 = query2.execute();

			for (int i = 0; i < rows2.length; i++) {
				this.entidadesNivel3.add(home.obterEntidadePorId(rows2[i]
						.getLong("id")));
			}

		}

		return this.entidadesNivel3;
	}

	public Collection obterEntidadesNivel4(Entidade seguradora, String mesAno)
			throws Exception {
		this.entidadesNivel4.addAll(this.obterEntidadesNivel3(seguradora,
				mesAno));

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select id from relatorio where seguradora=? and nivel=? and mes_ano=?");
		query.addLong(seguradora.obterId());
		query.addString("Nivel 4");

		String mesAnoModificado = "";

		for (int i = 0; i < mesAno.length(); i++) {

			String caracter = mesAno.substring(i, i + 1);

			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
				mesAnoModificado += caracter;
		}

		query.addString(mesAnoModificado);

		SQLRow[] rows = query.execute();

		if (rows.length > 0) {
			for (int i = 0; i < rows.length; i++) {
				this.entidadesNivel4.add(home.obterEntidadePorId(rows[i]
						.getLong("id")));
			}
		} else {
			SQLQuery query2 = this
					.getModelManager()
					.createSQLQuery(
							"crm",
							"select entidade.id from entidade,classificacao_contas where entidade.id=classificacao_contas.id and nivel=?");
			query2.addString("Nivel 4");

			SQLRow[] rows2 = query2.execute();

			for (int i = 0; i < rows2.length; i++) {
				this.entidadesNivel4.add(home.obterEntidadePorId(rows2[i]
						.getLong("id")));
			}
		}

		return this.entidadesNivel4;
	}

	public Collection obterEntidadesNivel5(Entidade seguradora, String mesAno) throws Exception
	{
		this.entidadesNivel5.addAll(this.obterEntidadesNivel4(seguradora,mesAno));

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select id from relatorio where seguradora=? and nivel=? and mes_ano=?");
		query.addLong(seguradora.obterId());
		query.addString("Nivel 5");

		String mesAnoModificado = "";

		for (int i = 0; i < mesAno.length(); i++)
		{
			String caracter = mesAno.substring(i, i + 1);

			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
				mesAnoModificado += caracter;
		}

		query.addString(mesAnoModificado);

		SQLRow[] rows = query.execute();

		if (rows.length > 0)
		{
			for (int i = 0; i < rows.length; i++)
			{
				this.entidadesNivel5.add(home.obterEntidadePorId(rows[i].getLong("id")));
			}
		}
		else
		{
			SQLQuery query2 = this.getModelManager().createSQLQuery("crm","select entidade.id from entidade,conta where entidade.id=conta.id and nivel=?");
			query2.addString("Nivel 5");

			SQLRow[] rows2 = query2.execute();

			for (int i = 0; i < rows2.length; i++)
			{
				this.entidadesNivel5.add(home.obterEntidadePorId(rows2[i].getLong("id")));
			}

		}

		return this.entidadesNivel5;
	}

	public Collection obterAtributos() throws Exception {
		if (this.entidadeAtributos.isEmpty()) {
			StringTokenizer atributos = new StringTokenizer(InfraProperties
					.getInstance().getProperty(
							this.obterClasse().toLowerCase() + ".atributos"),
					",");
			while (atributos.hasMoreTokens()) {
				String nome = atributos.nextToken();
				this.entidadeAtributos.put(nome, new AtributoImpl(this, nome));
			}
		}
		return this.entidadeAtributos.values();
	}

	public Date obterAtualizacao() throws Exception {
		if (this.atualizacao == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select atualizacao from entidade where id=?");
			query.addLong(this.obterId());
			this.atualizacao = new Date(query.executeAndGetFirstRow().getLong(
					"atualizacao"));
		}
		return this.atualizacao;
	}

	public Collection obterDatasMovimentacao() throws Exception {
		Map movimentacoes = new TreeMap();

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"SELECT movimentacao_financeira_conta.data_prevista FROM evento,movimentacao_financeira_conta where origem=? and evento.id=movimentacao_financeira_conta.id group by movimentacao_financeira_conta.data_prevista");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
			movimentacoes.put(new Date(rows[i].getLong("data_prevista")),
					new Date(rows[i].getLong("data_prevista")));

		return movimentacoes.values();
	}

	public Inscricao obterInscricaoAtiva() throws Exception
	{
		Inscricao inscricao = null;
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select top 1 evento.id from evento,inscricao where evento.id=inscricao.id and origem=? and situacao = 'Vigente'");
		query.addLong(this.obterId());

		if(query.execute().length >0)
		{
			long id = query.executeAndGetFirstRow().getLong("id");
			
			inscricao = (Inscricao) home.obterEventoPorId(id);
		}
		
		return inscricao;
	}
	
	public Inscricao obterUltimaInscricao() throws Exception
	{
		Inscricao inscricao = null;
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		//long maiorData = 0;
		long id = 0;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select TOP 1 evento.id from evento,inscricao where evento.id=inscricao.id and origem=? order by data_validade desc");
		query.addLong(this.obterId());

		/*SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long dataValidade = rows[i].getLong("data_validade");
			
			if(dataValidade > maiorData)
			{
				maiorData = dataValidade;
				id = rows[i].getLong("id");
			}
		}*/
		
		id = query.executeAndGetFirstRow().getLong("id");
		if(id > 0)
			inscricao = (Inscricao) home.obterEventoPorId(id);
		
		return inscricao;
	}
	
	public Inscricao obterUltimaInscricaoVigente() throws Exception
	{
		Inscricao inscricao = null;
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "select top 1 evento.id from evento,inscricao where evento.id=inscricao.id and origem="+this.obterId()+" and situacao='Vigente' order by data_validade desc";
		
		System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);

		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			if(id > 0)
				inscricao = (Inscricao) home.obterEventoPorId(id);
		}
		
		return inscricao;
	}
	
	public String obterClasse() throws Exception {
		return this.getClassAlias().trim();
	}

	public Contato obterContato(int id) throws Exception {
		//if (this.contatos == null)
		this.obterContatos();
		return (Contato) this.contatos.get(Integer.toString(id));
	}

	public String obterContato(String nome) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select valor from entidade_contato where entidade=? and nome=?");
		query.addLong(this.obterId());
		query.addString(nome);
		return query.executeAndGetFirstRow().getString("valor");
	}

	public Collection obterContatos() throws Exception {
		//if (this.contatos == null)
		//{
		this.contatos = new TreeMap();
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select id, nome, valor,nome_contato from entidade_contato where entidade=? order by nome");
		query.addLong(this.obterId());
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			int id = rows[i].getInt("id");
			this.contatos.put(Integer.toString(id), new ContatoImpl(this, id,
					rows[i].getString("nome"), rows[i].getString("valor"),
					rows[i].getString("nome_contato")));
		}
		//}
		return this.contatos.values();
	}

	public Date obterCriacao() throws Exception {
		if (this.criacao == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select criacao from entidade where id=?");
			query.addLong(this.obterId());
			this.criacao = new Date(query.executeAndGetFirstRow().getLong(
					"criacao"));
		}
		return this.criacao;
	}

	public String obterDescricaoClasse() throws Exception {
		return InfraProperties.getInstance().getProperty(
				this.obterClasse().toLowerCase() + ".descricao");
	}

	public Entidade obterEmpresa() throws Exception {
		if (this.empresa == null) {
			Entidade e = this.obterSuperior();
			while (e != null && !e.obterClasse().equals("empresa")
					&& !e.obterClasse().equals("Raiz"))
				e = e.obterSuperior();
			this.empresa = e;
		}
		return this.empresa;
	}

	public Entidade obterAseguradoraComoEmpresa() throws Exception {
		if (this.aseguradora == null) {
			Entidade e = this.obterSuperior();
			while (e != null && (!(e instanceof Aseguradora))
					&& !e.obterClasse().equals("Raiz"))
				e = e.obterSuperior();
			this.aseguradora = e;
		}
		return this.aseguradora;
	}

	public Endereco obterEndereco(int id) throws Exception
	{
		if (this.enderecos == null)
			this.obterEnderecos();
		return (Endereco) this.enderecos.get(new Integer(id));
	}

	public Collection obterEnderecos() throws Exception
	{
		if (this.enderecos == null)
		{
			this.enderecos = new TreeMap();
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select id, nome, cep, rua, numero, complemento, bairro, cidade, estado, pais from entidade_endereco where entidade=?");
			query.addLong(this.obterId());
			SQLRow[] rows = query.execute();
			
			for (int i = 0; i < rows.length; i++)
			{
				EnderecoImpl endereco = new EnderecoImpl();
				endereco.atribuirEntidade(this);
				endereco.atribuirId(rows[i].getInt("id"));
				endereco.atribuirNome(rows[i].getString("nome"));
				endereco.atribuirCep(rows[i].getString("cep"));
				endereco.atribuirRua(rows[i].getString("rua"));
				endereco.atribuirNumero(rows[i].getString("numero"));
				endereco.atribuirComplemento(rows[i].getString("complemento"));
				endereco.atribuirBairro(rows[i].getString("bairro"));
				endereco.atribuirCidade(rows[i].getString("cidade"));
				endereco.atribuirEstado(rows[i].getString("estado"));
				endereco.atribuirPais(rows[i].getString("pais"));
				this.enderecos.put(new Integer(endereco.obterId()), endereco);
			}
		}
		return this.enderecos.values();
	}
	
	public Endereco obterEndereco(String nome) throws Exception
	{
		EnderecoImpl endereco = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select id, cep, rua, numero, complemento, bairro, cidade, estado, pais from entidade_endereco where entidade=? and nome=?");
		query.addLong(this.obterId());
		query.addString(nome);
		
		SQLRow[] rows = query.execute();
		
		for (int i = 0; i < rows.length; i++)
		{
			endereco = new EnderecoImpl();
			endereco.atribuirEntidade(this);
			endereco.atribuirId(rows[i].getInt("id"));
			endereco.atribuirNome(nome);
			endereco.atribuirCep(rows[i].getString("cep"));
			endereco.atribuirRua(rows[i].getString("rua"));
			endereco.atribuirNumero(rows[i].getString("numero"));
			endereco.atribuirComplemento(rows[i].getString("complemento"));
			endereco.atribuirBairro(rows[i].getString("bairro"));
			endereco.atribuirCidade(rows[i].getString("cidade"));
			endereco.atribuirEstado(rows[i].getString("estado"));
			endereco.atribuirPais(rows[i].getString("pais"));
		}
		
		return endereco;
	}

	public Collection obterEventosComoDestino() throws Exception {
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterEventosPorDestino(this);
	}

	public Collection obterEventosComoDestinoHistorico(int pagina) throws Exception
	{
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome("EventoHome");
		Collection eventosPorDestino = new ArrayList();
		
		StringTokenizer eventos = new StringTokenizer(InfraProperties.getInstance().getProperty("eventos.historicoeventos.origem"),	",");
		String classe = null;

		while (eventos.hasMoreTokens()) 
		{
			String nomeEvento = eventos.nextToken();

			if (classe == null)
				classe = "'" + nomeEvento + "'";
			else
				classe += " or classe = " + "'" + nomeEvento + "'";
		}

		SQLQuery query = this.getModelManager().createSQLQuery(	"crm","select evento.id from evento,agenda_movimentacao where evento.id = agenda_movimentacao.id and destino=? and (classe = "	+ classe + ") order by prioridade,criacao DESC");
		query.addLong(this.obterId());
		
		SQLRow[] rows = query.execute();
		
		//System.out.println("select id from evento where origem=? and (classe = "	+ classe + ") order by prioridade,criacao DESC");

		for (int i = 0; i < rows.length; i++)
		{
			long id = rows[i].getLong("id");
			Evento e = (Evento) eventoHome.obterEventoPorId(id);
			eventosPorDestino.add(e);
		}
		
		rows = null;
		query = null;

		/*SQLQuery query2 = this.getModelManager().createSQLQuery("crm","select evento.id from evento,evento_entidades where evento.id = evento_entidades.id and evento_entidades.destino=? and (classe = "+ classe + ") order by prioridade,criacao DESC");
		query2.addLong(this.obterId());

		SQLRow[] rows2 = query2.execute();
		
		System.out.println("select evento.id from evento,evento_entidades where evento.id = evento_entidades.id and evento_entidades.destino=? and (classe = "+ classe + ") order by prioridade,criacao DESC");

		for (int i = 0; i < rows2.length; i++) 
		{
			long id = rows2[i].getLong("id");
			Evento e = (Evento) eventoHome.obterEventoPorId(id);
			
			if (!eventosPorDestino.contains(e))
				eventosPorDestino.add(e);
		}
		
		rows2 = null;
		query2 = null;*/

		return eventosPorDestino;
		
		//return eventoHome.obterEventosPorDestinoHistorico(this, pagina);
	}

	public Collection obterEventosComoOrigem() throws Exception {
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterEventosPorOrigem(this);
	}

	public Collection obterEventosComoOrigemHistorico(int pagina) throws Exception
	{
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome("EventoHome");
		Collection eventosPorOrigem = new ArrayList();
		
		StringTokenizer eventos = new StringTokenizer(InfraProperties.getInstance().getProperty("eventos.historicoeventos.origem"),	",");
		String classe = null;

		while (eventos.hasMoreTokens()) 
		{
			String nomeEvento = eventos.nextToken();

			if (classe == null)
				classe = "'" + nomeEvento + "'";
			else
				classe += " or classe = " + "'" + nomeEvento + "'";
		}

		//SQLQuery query = this.getModelManager().createSQLQuery(	"crm","select evento.id from evento,agenda_movimentacao where evento.id = agenda_movimentacao.id and origem=? and (classe = "	+ classe + ") order by prioridade,criacao DESC");
		//query.addLong(this.obterId());
		
		SQLQuery query = this.getModelManager().createSQLQuery(	"crm","EXEC obterAgendas ?");
		query.addLong(this.obterId());
		SQLRow[] rows = query.execute();
		
		//System.out.println("select id from evento where origem=? and (classe = "	+ classe + ") order by prioridade,criacao DESC");
		long id;
		Evento e;
		
		for (int i = 0; i < rows.length; i++)
		{
			id = rows[i].getLong("id");
			e = (Evento) eventoHome.obterEventoPorId(id);
			eventosPorOrigem.add(e);
		}
		
		/*rows = null;
		query = null;

		SQLQuery query2 = this.getModelManager().createSQLQuery("crm","select evento.id from evento,evento_entidades where evento.id = evento_entidades.id and evento_entidades.origem=? and (classe = "+ classe + ") order by prioridade,criacao DESC");
		query2.addLong(this.obterId());

		SQLRow[] rows2 = query2.execute();
		
		//System.out.println("select evento.id from evento,evento_entidades where evento.id = evento_entidades.id and evento_entidades.origem=? and (classe = "+ classe + ") order by prioridade,criacao DESC");

		for (int i = 0; i < rows2.length; i++) 
		{
			long id = rows2[i].getLong("id");
			Evento e = (Evento) eventoHome.obterEventoPorId(id);
			
			if (!eventosPorOrigem.contains(e))
				eventosPorOrigem.add(e);
		}
		
		rows2 = null;
		query2 = null;*/

		return eventosPorOrigem;
		
		
		//return eventoHome.obterEventosPorOrigemHistorico(this, pagina);
	}

	public Collection obterEventosAgenda(String mesAno, int pagina) throws Exception {
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterEventosAgenda(mesAno, pagina);
	}

	public Collection obterAgendas(boolean fasePendente) throws Exception {
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterAgendas(fasePendente);
	}

	public Collection obterEventosDeResponsabilidade() throws Exception {
		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return home.obterEventosPorResponsavel(this);
	}
	
	public Collection obterAgendas(String tipo) throws Exception
	{
		Collection agendas = new ArrayList();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "select evento.id from evento,agenda_movimentacao,fase where evento.id = agenda_movimentacao.id and agenda_movimentacao.id=fase.id " + 
				"and evento.origem = "+this.obterId()+" and classe='AgendaMovimentacao' and tipo = '"+tipo+"' and codigo='pendente' and termino=0 order by agenda_movimentacao.movimento_mes, agenda_movimentacao.movimento_ano desc";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
				
		SQLRow[] filas =  query.execute();
	
		for(int i = 0 ; i < filas.length;i++) 
		{
			long id = filas[i].getLong("id");
			
			AgendaMovimentacao agenda = (AgendaMovimentacao) home.obterEventoPorId(id);
			
			agendas.add(agenda);
		}
				
		return agendas; 
	}

	public Collection obterEventosDeResponsabilidadeHistorico(int pagina) throws Exception
	{
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome("EventoHome");
		Collection eventosPorResponsavel = new ArrayList();
		
		//return home.obterEventosPorResponsavelHistorico(this, pagina);
		
		StringTokenizer eventos = new StringTokenizer(InfraProperties.getInstance().getProperty("eventos.historicoeventos.responsavel"), ",");
		String classe = null;

		while (eventos.hasMoreTokens()) 
		{
			String nomeEvento = eventos.nextToken();

			if (classe == null)
				classe = "'" + nomeEvento + "'";
			else
				classe += " or classe = " + "'" + nomeEvento + "'";
		}

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,agenda_movimentacao where evento.id = agenda_movimentacao.id and responsavel=? and (classe = "	+ classe + ") order by prioridade,criacao DESC");
		query.addLong(this.obterId());
		query.setCurrentPage(pagina);
		query.setRowsByPage(30);
		
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
		{
			long id = rows[i].getLong("id");
			Evento e = (Evento) eventoHome.obterEventoPorId(id);
			eventosPorResponsavel.add(e);
		}
		
		query = null;
		rows = null;

		SQLQuery query2 = this.getModelManager().createSQLQuery("crm","select evento.id from evento,evento_entidades where evento.id = evento_entidades.id and evento_entidades.responsavel=? and (classe = "+ classe + ") order by prioridade,criacao DESC");
		query2.addLong(this.obterId());
		query2.setCurrentPage(pagina);
		query2.setRowsByPage(30);

		SQLRow[] rows2 = query2.execute();

		for (int i = 0; i < rows2.length; i++) 
		{
			long id = rows2[i].getLong("id");
			Evento e = (Evento) eventoHome.obterEventoPorId(id);
			if (!eventosPorResponsavel.contains(e))
				eventosPorResponsavel.add(e);
		}
		
		query2 = null;
		rows2 = null;

		return eventosPorResponsavel;
	}

	public String obterIcone() throws Exception {
		return InfraProperties.getInstance().getProperty(
				this.obterClasse().toLowerCase() + ".icone");
	}

	public long obterId() {
		return this.id;
	}

	public Collection obterInferiores() throws Exception {
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");
		return home.obterEntidadesInferiores(this);
	}
	
	public Collection obterLogs(int pagina) throws Exception 
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		Map logs = new TreeMap(); 
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select id from evento where classe = 'Log' order by criacao desc");
		query.setCurrentPage(pagina);
		query.setRowsByPage(30);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Log log = (Log) home.obterEventoPorId(id);
			
			logs.put(new Long(i), log);
		}
		
		return logs.values();
	}

	public Collection obterApolicesComoAgente() throws Exception {
		Map apolices = new TreeMap();

		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,apolice where evento.id = apolice.id and agente = ? group by evento.id");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");

			Apolice apolice = (Apolice) home.obterEventoPorId(id);

			apolices.put(new Long(apolice.obterCriacao().getTime()), apolice);
		}

		return apolices.values();
	}

	public Collection<Apolice> obterApolicesComoAgentePorPeriodo(Date dataInicio,Date dataFim, Aseguradora aseguradora) throws Exception
	{
		Collection<Apolice> apolices = new ArrayList<Apolice>();

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		//SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,apolice where evento.id = apolice.id and agente = ? and data_prevista_inicio>=? and data_prevista_inicio<=? and origem = ? group by evento.id");
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,apolice where evento.id = apolice.id and agente = ? and data_emissao>=? and data_emissao<=? and origem = ? order by data_emissao");
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		query.addLong(aseguradora.obterId());

		SQLRow[] rows = query.execute();
		long id;
		Apolice apolice;

		for (int i = 0; i < rows.length; i++)
		{
			id = rows[i].getLong("id");

			apolice = (Apolice) home.obterEventoPorId(id);

			apolices.add(apolice);
		}

		return apolices;
	}
	
	public Collection<Apolice> obterApolicesComoCorredorPorPeriodo(Date dataInicio,Date dataFim, Aseguradora aseguradora) throws Exception
	{
		Collection<Apolice> apolices = new ArrayList<Apolice>();

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,apolice where evento.id = apolice.id and corredor = ? and data_emissao>=? and data_emissao<=? and origem = ? order by data_emissao");
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		query.addLong(aseguradora.obterId());

		SQLRow[] rows = query.execute();
		long id;
		Apolice apolice;
		
		for (int i = 0; i < rows.length; i++) 
		{
			id = rows[i].getLong("id");

			apolice = (Apolice) home.obterEventoPorId(id);

			apolices.add(apolice);
		}

		return apolices;
	}

	public Collection obterApolicesComoOrigem() throws Exception
	{
		Map apolices = new TreeMap();

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select evento.id from evento,apolice where origem = ? and evento.id = apolice.id group by evento.id");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
		{
			long id = rows[i].getLong("id");

			Apolice apolice = (Apolice) home.obterEventoPorId(id);

			apolices.put(new Long(apolice.obterCriacao().getTime()), apolice);
		}

		return apolices.values();
	}
	
	public Collection obterApolicesVigentes() throws Exception
	{
		Map apolices = new TreeMap();

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select evento.id from evento,apolice where origem = ? and evento.id = apolice.id and situacao_seguro = ? order by criacao ASC");
		query.addLong(this.obterId());
		query.addString("Vigente");

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
		{
			long id = rows[i].getLong("id");

			Apolice apolice = (Apolice) home.obterEventoPorId(id);

			apolices.put(new Long(apolice.obterCriacao().getTime()), apolice);
		}

		return apolices.values();
	}

	public String obterNome() throws Exception {
		if (this.nome == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select nome from entidade where id=?");
			query.addLong(this.obterId());
			this.nome = query.executeAndGetFirstRow().getString("nome");
		}
		return this.nome;
	}

	public boolean verificarRuc(String ruc) throws Exception {
		if (ruc != null && !ruc.equals("")) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select ruc from entidade where ruc=? and id<>?");
			query.addString(ruc);
			query.addLong(this.obterId());

			if (query.execute().length > 0)
				return true;
			else
				return false;
		} else
			return false;
	}

	public Collection obterNomesContatos() throws Exception {
		if (this.nomesContatos == null) {
			this.nomesContatos = new ArrayList();
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select nome from entidade_contato group by nome");
			SQLRow[] rows = query.execute();
			for (int i = 0; i < rows.length; i++)
				this.nomesContatos.add(rows[i].getString("nome"));
		}
		return this.nomesContatos;
	}

	public Collection obterNomesEnderecos() throws Exception {
		if (this.nomesEnderecos == null) {
			this.nomesEnderecos = new ArrayList();
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select nome from entidade_endereco group by nome");
			SQLRow[] rows = query.execute();
			for (int i = 0; i < rows.length; i++)
				this.nomesEnderecos.add(rows[i].getString("nome"));
		}
		return this.nomesEnderecos;
	}

	public long obterNumeroEntidadeInferiores() throws Exception {
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager()
				.getHome("EntidadeHome");
		return entidadeHome.obterNumeroEntidadesInferiores(this);
	}

	public Collection obterPossiveisSuperiores() throws Exception {
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager()
				.getHome("EntidadeHome");
		return entidadeHome.obterPossiveisSuperiores(this);
	}

	public Usuario obterResponsavel() throws Exception {
		if (this.responsavel == null) {
			if (this.idResponsavel > 0) {
				EntidadeHome entidadeHome = (EntidadeHome) this
						.getModelManager().getHome("EntidadeHome");
				this.responsavel = (Usuario) entidadeHome
						.obterEntidadePorId(this.idResponsavel);
			} else {
				SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select responsavel from entidade where id=?");
				query.addLong(this.obterId());
				SQLRow[] rows = query.execute();
				long idUsuario = query.executeAndGetFirstRow().getLong(
						"responsavel");
				if (idUsuario == 0)
					return null;
				else {
					EntidadeHome entidadeHome = (EntidadeHome) this
							.getModelManager().getHome("EntidadeHome");
					this.responsavel = (Usuario) entidadeHome
							.obterEntidadePorId(idUsuario);
				}
			}
		}
		return this.responsavel;
	}

	public Entidade obterSuperior() throws Exception {
		if (this.superior == null) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");
			this.superior = home.obterEntidadeSuperior(this);
		}
		return this.superior;
	}

	public Collection<Entidade> obterSuperiores() throws Exception 
	{
		if (this.superiores == null) 
		{
			EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
			this.superiores = entidadeHome.obterEntidadesSuperiores(this);
		}
		return this.superiores;
	}
	
	public Map<Long,Entidade> obterSuperioresMap() throws Exception
	{
		Map<Long,Entidade> superiores = new TreeMap<Long, Entidade>();
		
		Entidade s = this.obterSuperior();
		while (s != null)
		{
			superiores.put(s.obterId(),s);
			s = s.obterSuperior();
		}
		
		return superiores;
	}

	public final String obterTipo() throws Exception
	{
		InfraProperties ip = InfraProperties.getInstance();
		return ip.getProperty(this.obterClasse().toLowerCase() + ".descricao");
	}

	protected Usuario obterUsuarioAtual() throws Exception {
		UsuarioHome home = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");
		return home.obterUsuarioPorUser(this.getModelManager().getUser());
	}

	public boolean permiteAtualizar() throws Exception
	{
		if(this.obterUsuarioAtual().obterId() == 1 || this.obterUsuarioAtual().obterNivel().equals(Usuario.ADMINISTRADOR))
			return true;
		else
		{
			Usuario responsavel = this.obterResponsavel();
			return responsavel != null && responsavel.obterId() == this.obterUsuarioAtual().obterId();
		}
	}

	public boolean permiteAtualizarResponsavel() throws Exception
	{
		Usuario usuarioAtual = this.obterUsuarioAtual();
		Usuario responsavel = this.obterResponsavel();
		if (responsavel == null)
			return false;
		if (responsavel.obterId() == usuarioAtual.obterId())
			return true;
		Entidade superior = this.obterSuperior();
		while (!(superior instanceof Raiz))
		{
			responsavel = superior.obterResponsavel();
			if (responsavel.obterId() == usuarioAtual.obterId())
				return true;
			superior = superior.obterSuperior();
		}
		return false;
	}

	public boolean permiteAtualizarSuperior() throws Exception {
		UsuarioHome home = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");
		Usuario usuarioAtual = home.obterUsuarioPorUser(this.getModelManager()
				.getUser());
		Usuario responsavel = this.obterResponsavel();
		return responsavel != null
				&& responsavel.obterId() == usuarioAtual.obterId();
	}

	public boolean permiteEventosComoOrigem() throws Exception {
		return InfraProperties.getInstance().getProperty(
				"entidades.permitemeventos").indexOf(
				this.obterClasse().toLowerCase()) >= 0;
	}

	public boolean permiteExcluir() throws Exception 
	{
		Usuario responsavel = this.obterResponsavel();
		Usuario usuarioAtual = this.obterUsuarioAtual();
		return this.obterId() != usuarioAtual.obterId() && !this.possuiEventosVinculados() && this.obterNumeroEntidadeInferiores() == 0;

	}

	public boolean permiteIncluirEntidadesInferiores() throws Exception {
		Usuario responsavel = this.obterResponsavel();
		Usuario usuarioAtual = this.obterUsuarioAtual();
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager()
				.getHome("EntidadeHome");
		return responsavel != null
				&& responsavel.obterId() == usuarioAtual.obterId()
				&& entidadeHome.obterClassesInferiores(this).size() > 0;
	}

	public boolean possuiEventosVinculados() throws Exception {
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager()
				.getHome("EntidadeHome");
		return entidadeHome.possuiEventosVinculados(this);
	}

	public void removerAtributo(String nome) throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate(
				"delete from entidade_atributo where entidade=? and nome=?");
		delete.addLong(this.obterId());
		delete.addString(this.nome);
		delete.execute();
		this.entidadeAtributos.remove(nome);
	}

	public void removerContato(Contato contato) throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate(
				"delete from entidade_contato where entidade=? and id=?");
		delete.addLong(this.obterId());
		delete.addInt(contato.obterId());
		delete.execute();
		if (this.contatos != null)
			this.contatos.remove(Integer.toString(contato.obterId()));
	}

	public void removerEndereco(Endereco endereco) throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate(
				"delete from entidade_endereco where entidade=? and id=?");
		delete.addLong(this.obterId());
		delete.addLong(endereco.obterId());
		delete.execute();
		if (this.enderecos != null)
			this.enderecos.remove(new Integer(endereco.obterId()));
	}

	public boolean temInferiores() throws Exception {
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");
		return home.possuiEntidadesInferiores(this);
	}

	public boolean validaCPF(String cpf) throws Exception {
		boolean cpfValido = false;

		String recebeCPF = cpf;
		String[] numero = new String[11];
		double soma = 0;
		double resultado1 = 0;
		double resultado2 = 0;
		String s = "";

		for (int i = 0; i < recebeCPF.length(); i++) {

			String caracter = recebeCPF.substring(i, i + 1);

			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
				s += caracter;
		}

		recebeCPF = s.trim();

		if (recebeCPF.length() != 11)
			throw new Exception("O CPF deve conter 11 números");
		if (recebeCPF.equals("00000000000"))
			throw new Exception("O CPF inválido");

		numero[0] = recebeCPF.substring(0, 1);
		numero[1] = recebeCPF.substring(1, 2);
		numero[2] = recebeCPF.substring(2, 3);
		numero[3] = recebeCPF.substring(3, 4);
		numero[4] = recebeCPF.substring(4, 5);
		numero[5] = recebeCPF.substring(5, 6);
		numero[6] = recebeCPF.substring(6, 7);
		numero[7] = recebeCPF.substring(7, 8);
		numero[8] = recebeCPF.substring(8, 9);
		numero[9] = recebeCPF.substring(9, 10);
		numero[10] = recebeCPF.substring(10, 11);

		soma = 10 * Double.parseDouble(numero[0]) + 9
				* Double.parseDouble(numero[1]) + 8
				* Double.parseDouble(numero[2]) + 7
				* Double.parseDouble(numero[3]) + 6
				* Double.parseDouble(numero[4]) + 5
				* Double.parseDouble(numero[5]) + 4
				* Double.parseDouble(numero[6]) + 3
				* Double.parseDouble(numero[7]) + 2
				* Double.parseDouble(numero[8]);
		//soma = 10 * Numero(1) + 9 * Numero(2) + 8 * Numero(3) + 7 * Numero(4)
		// + 6 * Numero(5) + 5 * Numero(6) + 4 * Numero(7) + 3 * Numero(8) + 2 *
		// Numero(9)

		soma = soma - (11 * ((int) (soma / 11)));

		if (soma == 0 || soma == 1)
			resultado1 = 0;
		else
			resultado1 = 11 - soma;

		if (resultado1 == Double.parseDouble(numero[9])) {
			soma = Double.parseDouble(numero[0]) * 11
					+ Double.parseDouble(numero[1]) * 10
					+ Double.parseDouble(numero[2]) * 9
					+ Double.parseDouble(numero[3]) * 8
					+ Double.parseDouble(numero[4]) * 7
					+ Double.parseDouble(numero[5]) * 6
					+ Double.parseDouble(numero[6]) * 5
					+ Double.parseDouble(numero[7]) * 4
					+ Double.parseDouble(numero[8]) * 3
					+ Double.parseDouble(numero[9]) * 2;

			soma = soma - (11 * ((int) (soma / 11)));

			if (soma == 0 || soma == 1)
				resultado2 = 0;
			else
				resultado2 = 11 - soma;
		}

		if (resultado2 == Double.parseDouble(numero[10]))
			cpfValido = false;
		else
			throw new Exception("CPF Inválido");

		return cpfValido;
	}

	public boolean validaCNPJ(String cnpj) throws Exception {
		return true;
	}

	public String toString() {
		try {
			return this.obterNome();
		} catch (Exception e) {
			return "";
		}
	}

	//  ************************** Só para o cartão
	// *********************************************
	public Collection obterContasPagarReceber(Date dia) throws Exception {
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterContasPagarReceber(this, dia);
	}

	public Collection obterMovimentoContabil(Date dia) throws Exception {
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterMovimentoContabil(this, dia);
	}

	public Collection obterFluxoCaixa() throws Exception {
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterFluxoCaixa(this);
	}

	public double obterTotalCreditoPrevisto() throws Exception {
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterTotalCreditoPrevisto(this);
	}

	public double obterTotalCreditoRealizado() throws Exception {
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterTotalCreditoRealizado(this);
	}

	public double obterTotalDebitoPrevisto() throws Exception {
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterTotalDebitoPrevisto(this);
	}

	public double obterTotalDebitoRealizado() throws Exception {
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterTotalDebitoRealizado(this);
	}

	public double obterCapitalGsReaseguradora(Aseguradora aseguradora,Date dataInicio, Date dataFim, String tipoContrato)throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery(	"crm",
						"select SUM(caiptal_gs) as valor from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and reaseguradora = ? and tipo_contrato = ? and data_prevista_inicio >= ? and data_prevista_conclusao <= ?");
		query.addLong(aseguradora.obterId());
		query.addLong(this.obterId());
		query.addString(tipoContrato);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		return query.executeAndGetFirstRow().getDouble("valor");
	}

	public double obterPrimaGsReaseguradora(Aseguradora aseguradora,Date dataInicio, Date dataFim, String tipoContrato)throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select SUM(prima_gs) as valor from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and reaseguradora = ? and tipo_contrato = ? and data_prevista_inicio >= ? and data_prevista_conclusao <= ?");
		query.addLong(aseguradora.obterId());
		query.addLong(this.obterId());
		query.addString(tipoContrato);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		return query.executeAndGetFirstRow().getDouble("valor");
	}

	public double obterComissaoGsReaseguradora(Aseguradora aseguradora,Date dataInicio, Date dataFim, String tipoContrato)throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select SUM(comissao_gs) as valor from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and reaseguradora = ? and tipo_contrato = ? and data_prevista_inicio >= ? and data_prevista_conclusao <= ?");
		query.addLong(aseguradora.obterId());
		query.addLong(this.obterId());
		query.addString(tipoContrato);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		return query.executeAndGetFirstRow().getDouble("valor");
	}

	public double obterCapitalGsCorretora(Aseguradora aseguradora,Date dataInicio, Date dataFim, String tipoContrato)throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select SUM(caiptal_gs) as valor from evento, dados_reaseguro where evento.id = dados_reaseguro.id and corretora = ? and data_prevista_inicio >= ? and data_prevista_conclusao <= ? and origem = ? and tipo_contrato = ? ");
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		query.addLong(aseguradora.obterId());		
		query.addString(tipoContrato);
		

		return query.executeAndGetFirstRow().getDouble("valor");
	}

	public double obterPrimaGsCorretora(Aseguradora aseguradora,
			Date dataInicio, Date dataFim, String tipoContrato)
			throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select SUM(prima_gs) as valor from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and corretora = ? and tipo_contrato = ? and data_prevista_inicio >= ? and data_prevista_conclusao <= ?");
		
		query.addLong(aseguradora.obterId());
		query.addLong(this.obterId());
		query.addString(tipoContrato);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		return query.executeAndGetFirstRow().getDouble("valor");
	}

	public double obterComissaoGsCorretora(Aseguradora aseguradora,
			Date dataInicio, Date dataFim, String tipoContrato)
			throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select SUM(comissao_gs) as valor from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and corretora = ? and tipo_contrato = ? and data_prevista_inicio >= ? and data_prevista_conclusao <= ?");
		query.addLong(aseguradora.obterId());
		query.addLong(this.obterId());
		query.addString(tipoContrato);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		return query.executeAndGetFirstRow().getDouble("valor");
	}

	public Collection<Inscricao> obterInscricoes() throws Exception
	{
		Collection inscricoes = new ArrayList();

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,inscricao where evento.id = inscricao.id and origem = ? order by inscricao,data_resolucao");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		long id;
		Inscricao inscricao;
		
		for (int i = 0; i < rows.length; i++)
		{
			id = rows[i].getLong("id");

			inscricao = (Inscricao) home.obterEventoPorId(id);

			inscricoes.add(inscricao);

		}

		return inscricoes;
	}
	
	public void atualizarInferiores(Usuario responsavel) throws Exception
	{
		for(Iterator i = this.obterInferiores().iterator() ; i.hasNext() ; )
		{
			Entidade e = (Entidade) i.next();
			
			e.atribuirResponsavel(responsavel);
			
			e.atualizar();
			
			if(e.obterInferiores().size() > 0)
				e.atualizarInferiores(responsavel);
			
		}
		
	}
	private Collection contaCalculada = new ArrayList();

	private Collection classificacaoContasSuperiores = new ArrayList();

}