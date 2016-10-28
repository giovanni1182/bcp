package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface Evento {
	public interface Comentario {
		String obterComentario();

		Date obterCriacao();

		String obterTitulo();
	}

	public interface Fase {
		boolean equals(Object object);

		String obterCodigo();

		Collection obterFasesAnteriores() throws Exception;

		Date obterInicio();

		String obterNome() throws Exception;

		Collection obterProximasFases() throws Exception;

		Date obterTermino();
	}

	static final String EVENTO_CONCLUIDO = "concluido";

	static final String EVENTO_PENDENTE = "pendente";

	static final int PRIORIDADE_ALTA = 1;

	static final int PRIORIDADE_BAIXA = 3;

	static final int PRIORIDADE_NORMAL = 2;

	void adicionarComentario(String titulo, String comentario) throws Exception;

	void atribuirDataPrevistaConclusao(Date dataPrevistaConclusao)
			throws Exception;

	void atribuirDataPrevistaInicio(Date dataPrevistaInicio) throws Exception;

	void atribuirDescricao(String descricao) throws Exception;

	void atribuirDestino(Entidade destino) throws Exception;

	void atribuirDuracao(Long duracao) throws Exception;

	void atribuirId(long id) throws Exception;

	void atribuirOrigem(Entidade origem) throws Exception;

	void atribuirPrioridade(int prioridade) throws Exception;

	void atribuirResponsavel(Entidade responsavel) throws Exception;

	void atribuirSuperior(Evento superior) throws Exception;

	void atribuirTipo(String tipo) throws Exception;

	void atribuirTitulo(String titulo) throws Exception;

	void atualizarComoLido() throws Exception;

	void atualizarComoNaoLido() throws Exception;

	void atualizarDataPrevistaConclusao(Date dataPrevista) throws Exception;

	void atualizarDataPrevistaInicio(Date dataPrevista) throws Exception;

	void atualizarDescricao(String descricao) throws Exception;

	void atualizarDestino(Entidade entidade) throws Exception;

	void atualizarDuracao(Long duracao) throws Exception;

	void atualizarFase(String codigo) throws Exception;

	void atualizarOrigem(Entidade objeto) throws Exception;

	void atualizarPrioridade(int prioridade) throws Exception;

	void atualizarResponsavel(Entidade responsavel) throws Exception;

	void atualizarSuperior(Evento evento) throws Exception;

	void atualizarTipo(String tipo) throws Exception;

	void atualizarTitulo(String nome) throws Exception;

	void calcularPrevisoes() throws Exception;

	void concluir(String comentario) throws Exception;

	void encaminhar(Entidade responsavel, String comentario) throws Exception;

	void excluir() throws Exception;

	boolean foiLido() throws Exception;

	void incluir() throws Exception;

	Date obterAtualizacao() throws Exception;

	String obterClasse() throws Exception;

	String obterClasseDescricao() throws Exception;

	Collection obterComentarios() throws Exception;

	Date obterCriacao() throws Exception;

	Usuario obterCriador() throws Exception;

	Date obterDataPrevistaConclusao() throws Exception;

	Date obterDataPrevistaInicio() throws Exception;

	String obterDescricao() throws Exception;

	Entidade obterDestino() throws Exception;

	long obterDuracao() throws Exception;

	Fase obterFase() throws Exception;

	Collection obterFases() throws Exception;

	Fase obterFaseAnterior() throws Exception;

	String obterIcone() throws Exception;

	long obterId();

	Collection<Evento> obterInferiores() throws Exception;

	Entidade obterOrigem() throws Exception;

	Collection obterPossiveisSuperiores() throws Exception;

	int obterPrioridade() throws Exception;

	int obterQuantidadeComentarios() throws Exception;

	Entidade obterResponsavel() throws Exception;

	Entidade obterResponsavelAnterior() throws Exception;

	Evento obterSuperior() throws Exception;

	Collection obterSuperiores() throws Exception;

	String obterTipo() throws Exception;

	String obterTitulo() throws Exception;

	boolean permiteAdicionarComentario() throws Exception;

	boolean permiteAtualizar() throws Exception;

	boolean permiteConcluir() throws Exception;

	boolean permiteDevolver() throws Exception;

	boolean permiteEncaminhar() throws Exception;

	boolean permiteExcluir() throws Exception;

	boolean permiteIncluirEventoInferior() throws Exception;

	boolean permitePegar() throws Exception;

	boolean permiteResponder() throws Exception;

	void responder(String comentario) throws Exception;

	//    boolean permiteOrdenar() throws Exception;
	//    boolean permiteOrdenarParaCima() throws Exception;
	//    boolean permiteOrdenarParaBaixo() throws Exception;
	void ordenar() throws Exception;

	void ordenarParaCima(long ordem) throws Exception;

	void ordenarParaBaixo(long ordem) throws Exception;

	void atualizarOrdem(long ordem) throws Exception;

	long obterOrdem() throws Exception;

	void atribuirClassesParaOrdenar(Collection classes) throws Exception;
}