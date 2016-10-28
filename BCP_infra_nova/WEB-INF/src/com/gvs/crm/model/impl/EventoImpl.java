package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.Documento;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Inscricao;
import com.gvs.crm.model.ManutencaoSite;
import com.gvs.crm.model.Ocorrencia;
import com.gvs.crm.model.Processo;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.config.InfraProperties;
import infra.model.Entity;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public abstract class EventoImpl extends Entity implements Evento {
	public class ComentarioImpl implements Comentario {
		private String comentario;

		private Date criacao;

		private EventoImpl evento;

		private String titulo;

		public ComentarioImpl(EventoImpl evento, Date criacao, String titulo,
				String comentario) {
			this.evento = evento;
			this.criacao = criacao;
			this.titulo = titulo;
			this.comentario = comentario;
		}

		public String obterComentario() {
			return this.comentario;
		}

		public Date obterCriacao() {
			return this.criacao;
		}

		public String obterTitulo() {
			return this.titulo;
		}
	}

	public class FaseImpl implements Fase {
		private String codigo;

		private EventoImpl evento;

		private Date inicio;

		private String nome;

		private Date termino;

		public FaseImpl(EventoImpl evento, String codigo) {
			this.evento = evento;
			this.codigo = codigo;
			this.inicio = new Date();
		}

		public FaseImpl(EventoImpl evento, String codigo, Date inicio,
				Date termino) {
			this.evento = evento;
			this.codigo = codigo;
			this.inicio = inicio;
			this.termino = termino;
		}

		public boolean equals(Object object) {
			if (object instanceof Fase)
				return this.obterCodigo().equals(((Fase) object).obterCodigo());
			else if (object instanceof String)
				return this.obterCodigo().equals((String) object);
			else
				return false;
		}

		public String obterCodigo() {
			return this.codigo.trim();
		}

		public Collection obterFasesAnteriores() throws Exception {
			SQLQuery query = this.evento.getModelManager().createSQLQuery(
					"crm", "select * from fase where id=? and termino>0");
			query.addLong(this.evento.obterId());
			SQLRow[] rows = query.execute();
			ArrayList fases = new ArrayList();
			for (int i = 0; i < rows.length; i++)
				fases.add(new FaseImpl(this.evento,
						rows[i].getString("codigo"), new Date(rows[i]
								.getLong("inicio")), new Date(rows[i]
								.getLong("termino"))));
			return fases;
		}

		public Date obterInicio() {
			return this.inicio;
		}

		public String obterNome() throws Exception {
			return InfraProperties.getInstance().getProperty(
					"fase." + this.codigo.trim() + ".nome");
		}

		public Collection obterProximasFases() throws Exception {
			InfraProperties ip = InfraProperties.getInstance();
			StringTokenizer st = new StringTokenizer(ip.getProperty(this.evento
					.obterClasse()
					+ "." + this.nome.toLowerCase()), ",");
			ArrayList fases = new ArrayList();
			while (st.hasMoreTokens())
				fases.add(st.nextToken());
			return fases;
		}

		public Date obterTermino() {
			return this.termino;
		}
	}

	private Date atualizacao;

	private String classe;

	private String classeDescricao;

	private Collection comentarios;

	private Date criacao;

	private Usuario criador;

	private Date dataPrevistaConclusao;

	private Date dataPrevistaInicio;

	private String descricao;

	private Entidade destino;

	private Long duracao;

	private Fase fase;

	private long id;

	private Boolean lido;

	private Entidade origem;

	private Integer prioridade;

	private Integer quantidadeComentarios;

	private Entidade responsavel;

	private Entidade responsavelAnterior;

	private Evento superior;

	private Collection superiores;

	private String tipo;

	private String titulo;

	private long ordem;

	private Collection classesOrdem;

	public void adicionarComentario(String titulo, String comentario)
			throws Exception {
		SQLUpdate update1 = this
				.getModelManager()
				.createSQLUpdate(
						"insert into comentario (id, criacao, titulo, comentario) values (?, ?, ?, ?)");
		update1.addLong(this.obterId());
		update1.addLong(new Date().getTime());
		update1.addString(titulo);
		update1.addString(comentario);
		update1.execute();
		SQLUpdate update2 = this.getModelManager().createSQLUpdate(
				"update evento set atualizacao=? where id=?");
		update2.addLong(new Date().getTime());
		update2.addLong(this.obterId());
		update2.execute();
	}

	public void atribuirDataPrevistaConclusao(Date dataPrevistaConclusao)
			throws Exception {
		this.dataPrevistaConclusao = dataPrevistaConclusao;
	}

	public void atribuirDataPrevistaInicio(Date dataPrevistaInicio)
			throws Exception {
		this.dataPrevistaInicio = dataPrevistaInicio;
	}

	public void atribuirDescricao(String descricao) throws Exception {
		this.descricao = descricao;
	}

	public void atribuirDestino(Entidade destino) throws Exception {
		this.destino = destino;
	}

	public void atribuirDuracao(Long duracao) throws Exception {
		this.duracao = duracao;
	}

	public void atribuirId(long id) throws Exception {
		this.id = id;
	}

	public void atribuirOrigem(Entidade origem) throws Exception
	{
		this.origem = origem;
	}

	public void atribuirPrioridade(int prioridade) throws Exception {
		this.prioridade = new Integer(prioridade);
	}

	public void atribuirResponsavel(Entidade responsavel) throws Exception {
		this.responsavel = responsavel;
	}

	public void atribuirSuperior(Evento superior) throws Exception {
		this.superior = superior;
	}

	public void atribuirTipo(String tipo) throws Exception {
		this.tipo = tipo;
	}

	public void atribuirTitulo(String titulo) throws Exception {
		this.titulo = titulo;
	}

	public void atualizarComoLido() throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update evento set lido=1 where id=?");
		update.addLong(this.obterId());
		update.execute();
		this.lido = Boolean.TRUE;
	}

	public void atualizarComoNaoLido() throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update evento set lido=0 where id=?");
		update.addLong(this.obterId());
		update.execute();
		this.lido = Boolean.FALSE;
	}

	public void atualizarDataPrevistaConclusao(Date dataPrevistaConclusao)
			throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update evento set data_prevista_conclusao=? where id=?");
		if (dataPrevistaConclusao == null)
			update.addLong(null);
		else
			update.addLong(dataPrevistaConclusao.getTime());
		update.addLong(this.id);
		update.execute();
		this.dataPrevistaConclusao = dataPrevistaConclusao;
	}

	public void atualizarDataPrevistaInicio(Date dataPrevistaInicio)
			throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update evento set data_prevista_inicio=? where id=?");
		if (dataPrevistaInicio == null)
			update.addLong(null);
		else
			update.addLong(dataPrevistaInicio.getTime());
		update.addLong(this.id);
		update.execute();
		this.dataPrevistaInicio = dataPrevistaInicio;
	}

	public void atualizarDescricao(String descricao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update evento set descricao=? where id=?");
		update.addString(descricao);
		update.addLong(this.id);
		update.execute();
		this.descricao = descricao;
	}

	public void atualizarDestino(Entidade destino) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update evento set destino=? where id=?");
		update.addLong(destino.obterId());
		update.addLong(this.id);
		update.execute();
		this.destino = destino;
	}

	public void atualizarDuracao(Long duracao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update evento set duracao=? where id=?");
		update.addLong(duracao);
		update.addLong(this.id);
		update.execute();
		this.duracao = duracao;
	}

	public void atualizarFase(String codigo) throws Exception {
		Date data = new Date();
		SQLUpdate update1 = this.getModelManager().createSQLUpdate(
				"update fase set termino=? where id=? and termino=0");
		update1.addLong(data.getTime());
		update1.addLong(this.obterId());
		update1.execute();
		SQLUpdate update2 = this
				.getModelManager()
				.createSQLUpdate(
						"insert into fase (id, codigo, inicio, termino) values (?, ?, ?, 0)");
		update2.addLong(this.obterId());
		update2.addString(codigo);
		update2.addLong(data.getTime());
		update2.execute();
	}

	public void atualizarOrigem(Entidade origem) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("update evento set origem=? where id=?");
		if(origem!=null)
			update.addLong(origem.obterId());
		else
			update.addLong(null);
		update.addLong(this.id);
		update.execute();
		
		this.origem = origem;
	}

	public void atualizarPrioridade(int prioridade) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update evento set prioridade=? where id=?");
		update.addInt(prioridade);
		update.addLong(this.id);
		update.execute();
		this.prioridade = new Integer(prioridade);
	}

	public void atualizarResponsavel(Entidade responsavel) throws Exception {
		Entidade responsavelAtual = this.obterResponsavel();
		if (responsavelAtual != null)
			if (responsavelAtual instanceof Usuario) {
				SQLUpdate update = this
						.getModelManager()
						.createSQLUpdate(
								"update evento set responsavel_anterior=?, responsavel=? where id=?");
				update.addLong(responsavelAtual.obterId());
				update.addLong(responsavel.obterId());
				update.addLong(this.id);
				update.execute();
				this.atualizarComoNaoLido();
				this.responsavel = responsavel;
				this.responsavelAnterior = responsavelAtual;
			} else {
				SQLUpdate update = this.getModelManager().createSQLUpdate(
						"update evento set responsavel=? where id=?");
				update.addLong(responsavel.obterId());
				update.addLong(this.id);
				update.execute();
				this.responsavel = responsavel;
			}
	}

	public void atualizarSuperior(Evento superior) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update evento set superior=? where id=?");
		if (superior == null)
			update.addLong(null);
		else
			update.addLong(superior.obterId());
		update.addLong(this.id);
		update.execute();
		this.superior = superior;
	}

	public void atualizarTipo(String tipo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update evento set tipo=? where id=?");
		update.addString(tipo);
		update.addLong(this.id);
		update.execute();
		this.tipo = tipo;
	}

	public void atualizarTitulo(String titulo) throws Exception {
		if (titulo == null || titulo.equals(""))
			throw new Exception("O título deve ser preenchido");
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update evento set titulo=? where id=?");
		update.addString(titulo);
		update.addLong(this.id);
		update.execute();
		this.titulo = titulo;
	}

	public void calcularPrevisoes() throws Exception {
	}

	public void concluir(String comentario) throws Exception {
		if (comentario != null)
			this.adicionarComentario("Concluido por "
					+ obterUsuarioAtual().obterNome(), comentario);

		if (this instanceof Ocorrencia) {
			this.atualizarFase(EVENTO_CONCLUIDO);
			this.adicionarComentario("Concluido por "
					+ obterUsuarioAtual().obterNome(), comentario);
		} else if (this instanceof ManutencaoSite && !this.permiteConcluir()) {
			this.atualizarResponsavel(this.obterCriador());
			this.adicionarComentario("Concluido por "
					+ obterUsuarioAtual().obterNome(), comentario);
		}

		else if (this instanceof Documento) {
			for (Iterator i = this.obterInferiores().iterator(); i.hasNext();) {
				Evento e = (Evento) i.next();

				e.atualizarFase(Evento.EVENTO_CONCLUIDO);
			}

			this.adicionarComentario("Concluido por "
					+ obterUsuarioAtual().obterNome(), comentario);
			this.atualizarFase(Evento.EVENTO_CONCLUIDO);
		}

		else {
			if (this.obterSuperior() == null) {
				if (obterUsuarioAtual().equals(this.obterCriador()))
					this.atualizarFase(Evento.EVENTO_CONCLUIDO);
				else
					this.atualizarResponsavel(this.obterCriador());
			} else {
				this.atualizarFase(Evento.EVENTO_CONCLUIDO);
				boolean concluirSuperior = true;

				for (Iterator i = this.obterSuperior().obterInferiores().iterator(); i.hasNext();)
				{
					Evento e = (Evento) i.next();

					if (!e.obterFase().obterCodigo().equals(Evento.EVENTO_CONCLUIDO))
					{
						concluirSuperior = false;
						break;
					}
				}
				if (concluirSuperior)
					this.obterSuperior().concluir("Concluido automaticamente");
			}
		}
	}

	public void encaminhar(Entidade responsavel, String comentario)
			throws Exception {
		this.adicionarComentario("Encaminado por "
				+ obterUsuarioAtual().obterNome() + " para "
				+ responsavel.obterNome(), comentario);
		this.atualizarResponsavel(responsavel);
	}

	public boolean equals(Object object) {
		if (object instanceof Evento)
			return ((Evento) object).obterId() == this.id;
		else
			return false;
	}

	public void excluir() throws Exception
	{
		for(Evento e : this.obterInferiores())
    		e.excluir();
    	
    	StringTokenizer tabelas = new StringTokenizer(InfraProperties.getInstance().getProperty("eventos.excluir"), ",");
    	SQLUpdate update;
    	String nomeTabela;
    	
    	if(this instanceof AgendaMovimentacao)
		{
			AgendaMovimentacao agenda = (AgendaMovimentacao) this;
			
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
			
			int mesInt = agenda.obterMesMovimento();
			String mes;
			if(new Integer(mesInt).toString().length() == 1)
				mes = "0"+mesInt;
			else
				mes = new Integer(mesInt).toString();
			
			if(this.obterTipo().equals("Instrumento"))
			{
				String nomeArquivo = "A"+ this.obterOrigem().obterSigla() + agenda.obterAnoMovimento() + mes + ".txt";
				
				home.excluirArquivoFila(nomeArquivo);
				
				nomeArquivo = nomeArquivo.replace("A", "B");
				home.excluirArquivoFila(nomeArquivo);
			}
			else
			{
				String nomeArquivo = this.obterOrigem().obterSigla() + agenda.obterAnoMovimento() + mes + ".txt";
				
				home.excluirArquivoFila(nomeArquivo);
			}
		}
    	
		while (tabelas.hasMoreTokens()) 
		{
			nomeTabela = tabelas.nextToken();
			//SQLUpdate update1 = mm.createSQLUpdate("crm","delete from " + nomeTabela + " where id=?");
			update = this.getModelManager().createSQLUpdate("crm","EXEC excluirEvento ?,?");
			update.addString(nomeTabela);
			update.addLong(this.obterId());
			
			update.execute();
		}

		update = this.getModelManager().createSQLUpdate("crm","delete from uploaded_file where id_evento=?");
		update.addLong(this.obterId());
		update.execute();

		update = this.getModelManager().createSQLUpdate("crm","delete from uploaded_file_content where id_evento=?");
		update.addLong(this.obterId());
		update.execute();

		update = this.getModelManager().createSQLUpdate("crm","delete from evento_entidades where sub_evento=?");
		update.addLong(this.obterId());
		update.execute();
		
		update = this.getModelManager().createSQLUpdate("crm", "delete from ultima_agenda where agenda_id=?");
		update.addLong(this.obterId());
		update.execute();

		//update = mm.createSQLUpdate("crm","delete from comentario where id=?");
		update = this.getModelManager().createSQLUpdate("crm","EXEC excluirEvento ?,?");
		update.addString("comentario");
		update.addLong(this.obterId());
		update.execute();

		//update = mm.createSQLUpdate("crm","delete from fase where id=?");
		update = this.getModelManager().createSQLUpdate("crm","EXEC excluirEvento ?,?");
		update.addString("fase");
		update.addLong(this.obterId());
		update.execute();
		
		//update = mm.createSQLUpdate("crm","delete from evento where id=?");
		update = this.getModelManager().createSQLUpdate("crm","EXEC excluirEvento ?,?");
		update.addString("evento");
		update.addLong(this.obterId());
		update.execute();
		
		/*for (Iterator i = this.obterInferiores().iterator(); i.hasNext();)
			((Evento) i.next()).excluir();

		StringTokenizer tabelas = new StringTokenizer(InfraProperties.getInstance().getProperty("eventos.excluir"), ",");

		while (tabelas.hasMoreTokens()) 
		{
			String nomeTabela = tabelas.nextToken();
			SQLUpdate update1 = this.getModelManager().createSQLUpdate("crm","delete from " + nomeTabela + " where id=?");
			update1.addLong(this.obterId());
			update1.execute();
		}

		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"delete from uploaded_file where id_evento=?");
		update.addLong(this.obterId());
		update.execute();

		SQLUpdate update2 = this.getModelManager().createSQLUpdate("crm",
				"delete from uploaded_file_content where id_evento=?");
		update2.addLong(this.obterId());
		update2.execute();

		SQLUpdate update3 = this.getModelManager().createSQLUpdate("crm",
				"delete from evento_entidades where sub_evento=?");
		update3.addLong(this.obterId());
		update3.execute();

		SQLUpdate update4 = this.getModelManager().createSQLUpdate("crm",
				"delete from comentario where id=?");
		update4.addLong(this.obterId());
		update4.execute();

		SQLUpdate update5 = this.getModelManager().createSQLUpdate("crm",
				"delete from fase where id=?");
		update5.addLong(this.obterId());
		update5.execute();

		SQLUpdate update6 = this.getModelManager().createSQLUpdate("crm",
				"delete from evento where id=?");
		update6.addLong(this.obterId());
		update6.execute();
		
		update6 = this.getModelManager().createSQLUpdate("crm", "delete from ultima_agenda where agenda_id=?");
		update6.addLong(this.obterId());
		update6.execute();*/
	}

	public boolean foiLido() throws Exception {
		if (this.lido == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select lido from evento where id=?");
			query.addLong(this.obterId());
			String lido = query.executeAndGetFirstRow().getString("lido");
			if (lido != null)
				this.lido = new Boolean(lido.equals("1"));
			else
				this.lido = Boolean.FALSE;
		}
		return this.lido.booleanValue();
	}

	public void incluir() throws Exception
	{
		if (this.titulo == null || this.titulo.equals(""))
			throw new Exception("El titulo del acontecimiento debe ser llenado");
		if (this.prioridade == null)
			this.prioridade = new Integer(Evento.PRIORIDADE_NORMAL);
		SQLQuery query = this.getModelManager().createSQLQuery(
				"select max(id) as maximo from evento");
		this.atribuirId(query.executeAndGetFirstRow().getLong("maximo") + 1);
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this
				.getModelManager().getUser());
		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into evento (id, classe, criador, criacao, titulo, responsavel_anterior, responsavel) values (?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addString(this.getClassAlias());
		insert.addLong(usuarioAtual.obterId());
		insert.addLong(new Date().getTime());
		insert.addString(this.titulo);
		insert.addLong(usuarioAtual.obterId());
		if (this.responsavel != null)
			insert.addLong(this.responsavel.obterId());
		else
			insert.addLong(usuarioAtual.obterId());

		//System.out.println("insert into evento (id, classe, criador, criacao,
		// titulo, responsavel_anterior, responsavel) values
		// ("+this.obterId()+", "+this.getClassAlias()+",
		// "+usuarioAtual.obterId()+", "+new Date().getTime()+",
		// "+this.titulo+", "+usuarioAtual.obterId()+",
		// "+this.responsavel.obterId()+")");

		insert.execute();

		if (this.dataPrevistaInicio != null)
			this.atualizarDataPrevistaInicio(this.dataPrevistaInicio);
		if (this.dataPrevistaConclusao != null)
			this.atualizarDataPrevistaConclusao(this.dataPrevistaConclusao);
		if (this.descricao != null)
			this.atualizarDescricao(this.descricao);
		if (this.destino != null)
			this.atualizarDestino(this.destino);
		if (this.duracao != null)
			this.atualizarDuracao(this.duracao);
		if (this.origem != null)
			this.atualizarOrigem(this.origem);
		if (this.prioridade != null)
			this.atualizarPrioridade(this.prioridade.intValue());
		if (this.superior != null)
			this.atualizarSuperior(this.superior);
		if (this.tipo != null)
			this.atualizarTipo(this.tipo);
		this.atualizarFase(Evento.EVENTO_PENDENTE);
	}

	public Date obterAtualizacao() throws Exception {
		if (this.atualizacao == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select atualizacao from evento where id=?");
			query.addLong(this.obterId());
			this.atualizacao = new Date(query.executeAndGetFirstRow().getLong(
					"atualizacao"));
		}
		return this.atualizacao;
	}

	public String obterClasse() throws Exception {
		return this.getClassAlias().toLowerCase().trim();
	}

	public String obterClasseDescricao() throws Exception {
		if (this.classeDescricao == null)
			this.classeDescricao = InfraProperties.getInstance().getProperty(
					this.obterClasse() + ".descricao");
		return this.classeDescricao;
	}

	public Collection obterComentarios() throws Exception {
		if (this.comentarios == null) {
			EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager()
					.getHome("EntidadeHome");
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery(
							"crm",
							"select criacao, titulo, comentario from comentario where id=? order by criacao");
			query.addLong(this.obterId());
			SQLRow[] rows = query.execute();
			this.comentarios = new ArrayList();
			for (int i = 0; i < rows.length; i++) {
				Date criacao = new Date(rows[i].getLong("criacao"));
				comentarios.add(new ComentarioImpl(this, criacao, rows[i]
						.getString("titulo"), rows[i].getString("comentario")));
			}
		}
		return this.comentarios;
	}

	public Date obterCriacao() throws Exception {
		if (this.criacao == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select criacao from evento where id=?");
			query.addLong(this.obterId());
			this.criacao = new Date(query.executeAndGetFirstRow().getLong(
					"criacao"));
		}
		return this.criacao;
	}

	public Usuario obterCriador() throws Exception {
		if (this.criador == null) {
			EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager()
					.getHome("EntidadeHome");
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select criador from evento where id=?");
			query.addLong(this.obterId());
			this.criador = (Usuario) entidadeHome.obterEntidadePorId(query
					.executeAndGetFirstRow().getLong("criador"));
		}
		return this.criador;
	}

	public Date obterDataPrevistaConclusao() throws Exception {
		if (this.dataPrevistaConclusao == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select data_prevista_conclusao from evento where id=?");
			query.addLong(this.obterId());
			SQLRow[] rows = query.execute();
			if (rows.length == 1) {
				long data = rows[0].getLong("data_prevista_conclusao");
				if (data != 0)
					this.dataPrevistaConclusao = new Date(data);
			}
		}
		return this.dataPrevistaConclusao;
	}

	public Date obterDataPrevistaInicio() throws Exception {
		if (this.dataPrevistaInicio == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select data_prevista_inicio from evento where id=?");
			query.addLong(this.obterId());
			SQLRow[] rows = query.execute();
			if (rows.length == 1) {
				long data = rows[0].getLong("data_prevista_inicio");
				if (data != 0)
					this.dataPrevistaInicio = new Date(data);
			}
		}
		return this.dataPrevistaInicio;
	}

	public String obterDescricao() throws Exception {
		if (this.descricao == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select descricao from evento where id=?");
			query.addLong(this.obterId());
			this.descricao = query.executeAndGetFirstRow().getString(
					"descricao");
		}
		return this.descricao;
	}

	public Entidade obterDestino() throws Exception {
		if (this.destino == null) {
			EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager()
					.getHome("EntidadeHome");
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select destino from evento where id=?");
			query.addLong(this.obterId());
			this.destino = entidadeHome.obterEntidadePorId(query
					.executeAndGetFirstRow().getLong("destino"));
		}
		return this.destino;
	}

	public long obterDuracao() throws Exception {
		if (this.duracao == null && this.obterId() > 0) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select duracao from evento where id=?");
			query.addLong(this.obterId());
			SQLRow[] rows = query.execute();
			if (rows.length == 1)
				this.duracao = new Long(rows[0].getLong("duracao"));
		}
		if (this.duracao != null)
			return this.duracao.longValue();
		else
			return 0;
	}

	public Fase obterFase() throws Exception
	{
		if (this.fase == null) 
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm", "select codigo, inicio from fase where id=? and termino=0");
			query.addLong(this.obterId());
			if(query.execute().length > 0)
			{
				SQLRow row = query.executeAndGetFirstRow();
				this.fase = new FaseImpl(this, row.getString("codigo"), new Date(row.getLong("inicio")), null);
			}
		}
		return this.fase;
	}

	public Collection obterFases() throws Exception {
		Map fases = new TreeMap();

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select codigo, inicio from fase where id=?");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
			fases.put(new Date(rows[i].getLong("inicio")), new FaseImpl(this,rows[i].getString("codigo"), new Date(rows[i].getLong("inicio")), null));

		return fases.values();
	}

	public Fase obterFaseAnterior() throws Exception {
		if (this.fase == null) {
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery(
							"crm",
							"select codigo, inicio from fase where id=? and termino<>0 order by termino DESC");
			query.addLong(this.obterId());
			SQLRow row = query.executeAndGetFirstRow();
			this.fase = new FaseImpl(this, row.getString("codigo"), new Date(
					row.getLong("inicio")), new Date(row.getLong("termino")));
		}
		return this.fase;
	}

	public String obterIcone() throws Exception {
		return "document.gif";
	}

	public long obterId() {
		return this.id;
	}

	public Collection<Evento> obterInferiores() throws Exception {
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterEventosInferiores(this);
	}

	public Entidade obterOrigem() throws Exception
	{
		if (this.origem == null)
		{
			EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select origem from evento where id=?");
			query.addLong(this.obterId());
			this.origem = entidadeHome.obterEntidadePorId(query.executeAndGetFirstRow().getLong("origem"));
		}
		return this.origem;
	}

	public Collection obterPossiveisSuperiores() throws Exception {
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterPossiveisSuperiores(this);
	}

	public int obterPrioridade() throws Exception {
		if (this.prioridade == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select prioridade from evento where id=?");
			query.addLong(this.obterId());
			this.prioridade = new Integer(query.executeAndGetFirstRow().getInt(
					"prioridade"));
		}
		return this.prioridade.intValue();
	}

	public int obterQuantidadeComentarios() throws Exception {
		if (this.quantidadeComentarios == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select count(*) from comentario where id=?");
			query.addLong(this.obterId());
			this.quantidadeComentarios = new Integer(query
					.executeAndGetFirstRow().getInt("count(*)"));
		}
		return this.quantidadeComentarios.intValue();
	}

	public Entidade obterResponsavel() throws Exception {
		if (this.responsavel == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select responsavel from evento where id=?");
			query.addLong(this.obterId());
			SQLRow[] rows = query.execute();
			if (rows.length == 1) {
				long responsavelId = rows[0].getLong("responsavel");
				if (responsavelId > 0) {
					EntidadeHome entidadeHome = (EntidadeHome) this
							.getModelManager().getHome("EntidadeHome");
					this.responsavel = entidadeHome
							.obterEntidadePorId(responsavelId);
				}
			}
		}
		return this.responsavel;
	}

	public Entidade obterResponsavelAnterior() throws Exception {
		if (this.responsavelAnterior == null && this.obterId() > 0) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select responsavel_anterior from evento where id=?");
			query.addLong(this.obterId());
			SQLRow[] rows = query.execute();
			if (rows.length == 1) {
				long responsavelAnteriorId = rows[0]
						.getLong("responsavel_anterior");
				if (responsavelAnteriorId > 0) {
					EntidadeHome entidadeHome = (EntidadeHome) this
							.getModelManager().getHome("EntidadeHome");
					this.responsavelAnterior = entidadeHome
							.obterEntidadePorId(responsavelAnteriorId);
				}
			}
		}
		return this.responsavelAnterior;
	}

	public Evento obterSuperior() throws Exception {
		if (this.superior == null && this.obterId() > 0) {
			EventoHome eventoHome = (EventoHome) this.getModelManager()
					.getHome("EventoHome");
			this.superior = eventoHome.obterEventoSuperior(this);
		}
		return this.superior;
	}

	public Collection obterSuperiores() throws Exception {
		if (this.superiores == null) {
			EventoHome eventoHome = (EventoHome) this.getModelManager()
					.getHome("EventoHome");
			this.superiores = eventoHome.obterEventosSuperiores(this);
		}
		return this.superiores;
	}

	public String obterTipo() throws Exception {
		if (this.tipo == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select tipo from evento where id=?");
			query.addLong(this.obterId());
			this.tipo = query.executeAndGetFirstRow().getString("tipo");
		}
		return this.tipo;
	}

	public String obterTitulo() throws Exception {
		if (this.titulo == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select titulo from evento where id=?");
			query.addLong(this.obterId());
			this.titulo = query.executeAndGetFirstRow().getString("titulo");
		}
		return this.titulo;
	}

	protected Usuario obterUsuarioAtual() throws Exception {
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");
		return usuarioHome
				.obterUsuarioPorUser(this.getModelManager().getUser());
	}

	public boolean permiteAdicionarComentario() throws Exception {
		return !this.obterFase().equals(EVENTO_CONCLUIDO);
	}

	public boolean permiteAtualizar() throws Exception
	{
		if (this.obterUsuarioAtual().obterNivel().equals(Usuario.ADMINISTRADOR) || this.obterUsuarioAtual().obterNivel().equals(Usuario.DIVISAO_CONTROL_AUXILIARES))
			return true;
		else
		{
			if (this instanceof Processo)
			{
				Processo processo = (Processo) this;

				return (this.obterUsuarioAtual()
						.equals(this.obterResponsavel()) || processo
						.obterResponsaveis().contains(this.obterUsuarioAtual()))
						&& !this.obterFase().equals(EVENTO_CONCLUIDO)
						&& !this.obterFase().equals("cancelado");
			}
			else if(this instanceof Inscricao)
				return this.obterUsuarioAtual().equals(this.obterResponsavel());
			else
				return this.obterUsuarioAtual().equals(this.obterResponsavel())	&& !this.obterFase().equals(EVENTO_CONCLUIDO);
		}

	}

	public boolean permiteConcluir() throws Exception {
		if (this instanceof Processo) {
			Processo processo = (Processo) this;

			return (this.obterUsuarioAtual().equals(this.obterResponsavel()) || processo
					.obterResponsaveis().contains(this.obterUsuarioAtual()))
					&& !this.obterFase().equals(EVENTO_CONCLUIDO);
		} else
			return this.obterUsuarioAtual().equals(this.obterResponsavel())
					&& !this.obterFase().equals(EVENTO_CONCLUIDO);
	}

	public boolean permiteDevolver() throws Exception {
		if (this instanceof Processo) {
			Processo processo = (Processo) this;

			return (this.obterUsuarioAtual().equals(this.obterResponsavel()) || processo
					.obterResponsaveis().contains(this.obterUsuarioAtual()))
					&& !this.obterFase().equals(EVENTO_CONCLUIDO);
		} else
			return this.obterUsuarioAtual().equals(this.obterResponsavel())
					&& !this.obterFase().equals(EVENTO_CONCLUIDO);
	}

	public boolean permiteEncaminhar() throws Exception {
		if (this instanceof Processo) {
			Processo processo = (Processo) this;

			return (this.obterUsuarioAtual().equals(this.obterResponsavel()) || processo
					.obterResponsaveis().contains(this.obterUsuarioAtual()))
					|| this.obterUsuarioAtual().equals(this.obterCriador())
					&& !this.obterFase().equals(EVENTO_CONCLUIDO);
		} else
			return (this.obterUsuarioAtual().equals(this.obterResponsavel()) || this
					.obterUsuarioAtual().equals(this.obterCriador()))
					&& !this.obterFase().equals(EVENTO_CONCLUIDO);
	}

	public boolean permiteExcluir() throws Exception
	{
		if (this instanceof Processo)
		{
			Processo processo = (Processo) this;

			return (this.obterUsuarioAtual().equals(this.obterResponsavel()) || processo
					.obterResponsaveis().contains(this.obterUsuarioAtual()))
					&& this.obterUsuarioAtual().equals(this.obterCriador())
					&& !this.obterFase().equals(EVENTO_CONCLUIDO);
		}
		else
		{	boolean retorno = false;
			if (this instanceof Inscricao)
			{
				if(this.obterUsuarioAtual().obterNivel().equals(Usuario.ADMINISTRADOR) || this.obterUsuarioAtual().obterNivel().equals(Usuario.DIVISAO_CONTROL_AUXILIARES) || this.obterUsuarioAtual().obterNivel().equals(Usuario.DIVISAO_CONTROL_REASEGUROS))
					retorno = true;
			}
			else if(this.obterUsuarioAtual().obterId() == 1 || this.obterUsuarioAtual().obterNivel().equals(Usuario.ADMINISTRADOR))
				retorno = true;
			else
			{
				retorno = this.obterUsuarioAtual().equals(this.obterResponsavel()) && this.obterFase().equals(EVENTO_CONCLUIDO);
				if(retorno == false)
					retorno = this.obterUsuarioAtual().equals(this.obterResponsavel().obterSuperior()) && this.obterFase().equals(EVENTO_CONCLUIDO);
			}
				
			return retorno; 
		}
	}

	public boolean permiteIncluirEventoInferior() throws Exception {
		String classes = InfraProperties.getInstance().getProperty(
				this.obterClasse() + ".inferiores");
		if (classes.equals(""))
			return false;
		return this.permiteAtualizar();
	}

	public boolean permitePegar() throws Exception {
		if (this instanceof Processo) {
			Processo processo = (Processo) this;

			return (this.obterUsuarioAtual().obterSuperior().equals(
					processo.obterResponsavel()) || processo
					.obterResponsaveis().contains(this.obterUsuarioAtual()))
					&& !this.obterFase().equals(EVENTO_CONCLUIDO);
		} else
			return this.obterUsuarioAtual().obterSuperior().equals(
					this.obterResponsavel())
					&& !this.obterFase().equals(EVENTO_CONCLUIDO);
	}

	public boolean permiteResponder() throws Exception {
		return this.obterUsuarioAtual().equals(this.obterResponsavel())
				&& !this.obterFase().equals(EVENTO_CONCLUIDO);
	}

	public void responder(String comentario) throws Exception {
		this.adicionarComentario("Respondido por "
				+ obterUsuarioAtual().obterNome() + " para "
				+ this.obterResponsavelAnterior().obterNome(), comentario);
		this.atualizarResponsavel(this.obterResponsavelAnterior());
	}

	public void ordenar() throws Exception {
		if (permiteAutoOrdenar()) {
			for (Iterator i = this.obterEventosOrdenar().iterator(); i
					.hasNext();) {
				Evento e = (Evento) i.next();
				e.atualizarOrdem(e.obterCriacao().getTime());
			}
		} else {
			for (Iterator i = obterEventoSemOrdem().iterator(); i.hasNext();) {
				Evento e = (Evento) i.next();
				e.atualizarOrdem(e.obterCriacao().getTime());
			}
		}
	}

	private Collection obterEventosOrdenar() throws Exception {
		Collection retornar = new ArrayList();
		for (Iterator i = this.obterInferiores().iterator(); i.hasNext();) {
			Evento e = (Evento) i.next();
			boolean aceita = false;

			for (Iterator i2 = this.classesOrdem.iterator(); i2.hasNext();) {
				String nomeClasse = (String) i2.next();
				if (nomeClasse.toLowerCase().equals(
						e.obterClasse().toLowerCase()))
					aceita = true;
			}

			if (aceita) {
				retornar.add(e);
			}
		}
		return retornar;
	}

	//    private long obterMaxDeOrdem() throws Exception
	//	{
	//    	long retorno = 0;
	//    	for (Iterator i = this.obterEventosOrdenar().iterator();i.hasNext();)
	//    	{
	//    		Evento e = (Evento) i.next();
	//    		if(e.obterOrdem() > retorno)
	//    			retorno = e.obterOrdem();
	//    	}
	//    	return retorno;
	//	}

	private Collection obterEventoSemOrdem() throws Exception {
		Collection semOrdem = new ArrayList();
		for (Iterator i = this.obterEventosOrdenar().iterator(); i.hasNext();) {
			Evento e = (Evento) i.next();
			if (e.obterOrdem() == 0)
				semOrdem.add(e);
		}
		return semOrdem;
	}

	private boolean permiteAutoOrdenar() throws Exception {
		boolean retorno = true;
		for (Iterator i = this.obterEventosOrdenar().iterator(); i.hasNext();) {
			Evento e = (Evento) i.next();
			if (e.obterOrdem() != 0)
				retorno = false;
		}
		return retorno;
	}

	private HashMap obterListaPorOrdem() throws Exception {
		HashMap ordenados = new HashMap();

		for (Iterator i = this.obterEventosOrdenar().iterator(); i.hasNext();) {
			Evento e = (Evento) i.next();
			ordenados.put(new Long(e.obterOrdem()), e);
		}
		return ordenados;
	}

	/*
	 * Ordena para cima
	 */
	public void ordenarParaCima(long ordem) throws Exception {
		HashMap ordenados = obterListaPorOrdem();
		long ordemUm = 0;
		long ordemDois = 0;

		Evento eventoUm = (Evento) ordenados.get(new Long(ordem));
		if (eventoUm != null)
			ordemUm = eventoUm.obterOrdem();
		Evento eventoDois = obterEventoACima(ordem);
		if (eventoDois != null)
			ordemDois = eventoDois.obterOrdem();
		if (ordemUm != 0 && ordemDois != 0) {
			eventoUm.atualizarOrdem(ordemDois);
			eventoDois.atualizarOrdem(ordemUm);
		} else
			throw new Exception("Não pode ser ordenado para cima");
	}

	/*
	 * Ordena para baixo
	 */
	public void ordenarParaBaixo(long ordem) throws Exception {
		HashMap ordenados = obterListaPorOrdem();
		long ordemUm = 0;
		long ordemDois = 0;

		Evento eventoUm = (Evento) ordenados.get(new Long(ordem));
		if (eventoUm != null)
			ordemUm = eventoUm.obterOrdem();
		Evento eventoDois = obterEventoABaixo(ordem);
		if (eventoDois != null)
			ordemDois = eventoDois.obterOrdem();
		if (ordemUm != 0 && ordemDois != 0) {
			eventoUm.atualizarOrdem(ordemDois);
			eventoDois.atualizarOrdem(ordemUm);
		} else
			throw new Exception("Não pode ser ordenado para baixo");
	}

	private Evento obterEventoACima(long ordem) throws Exception {
		long retorno = 0;
		Evento retorna = null;
		for (Iterator i = obterEventosOrdenar().iterator(); i.hasNext();) {
			Evento e = (Evento) i.next();
			if (e.obterOrdem() < ordem) {
				if (retorno < e.obterOrdem()) {
					retorno = e.obterOrdem();
					retorna = e;
				}
			}
		}
		return (Evento) retorna;
	}

	private Evento obterEventoABaixo(long ordem) throws Exception {
		long retorno = 0;
		Evento retorna = null;
		for (Iterator i = obterEventosOrdenar().iterator(); i.hasNext();) {
			Evento e = (Evento) i.next();
			if (e.obterOrdem() > ordem) {
				if (retorno > e.obterOrdem() || retorno == 0) {
					retorno = e.obterOrdem();
					retorna = e;
				}
			}
		}
		return (Evento) retorna;
	}

	public void atualizarOrdem(long ordem) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update evento set ordem=? where id=?");
		update.addLong(ordem);
		update.addLong(this.id);
		update.execute();
		this.ordem = ordem;
	}

	public long obterOrdem() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select ordem from evento where id=?");
		query.addLong(this.obterId());
		this.ordem = query.executeAndGetFirstRow().getLong("ordem");
		return this.ordem;
	}

	public void atribuirClassesParaOrdenar(Collection classes) throws Exception {
		this.classesOrdem = classes;
	}
}