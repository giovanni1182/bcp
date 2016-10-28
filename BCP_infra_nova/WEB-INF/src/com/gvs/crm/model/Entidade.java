package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface Entidade {
	public interface Atributo {
		void atualizarValor(String valor) throws Exception;

		String obterNome() throws Exception;

		int obterTamanho() throws Exception;

		char obterTipo() throws Exception;

		String obterValoresTitulos() throws Exception;

		String obterTitulo() throws Exception;

		int obterOrdem() throws Exception;

		String obterValor() throws Exception;
	}

	public interface Contato {
		void atualizarValor(String valor, String nomeContato) throws Exception;

		Entidade obterEntidade() throws Exception;

		int obterId() throws Exception;

		String obterNome() throws Exception;

		String obterValor() throws Exception;

		String obterNomeContato() throws Exception;
	}

	public interface Endereco {
		void atribuirBairro(String bairro);

		void atribuirCep(String cep);

		void atribuirCidade(String cidade);

		void atribuirComplemento(String complemento);

		void atribuirEstado(String estado);

		void atribuirNome(String nome);

		void atribuirNumero(String numero);

		void atribuirPais(String pais);

		void atribuirRua(String rua);

		void atualizar() throws Exception;

		void incluir() throws Exception;

		String obterBairro();

		String obterCep();

		String obterCidade();

		String obterComplemento();

		Entidade obterEntidade();

		String obterEstado();

		int obterId();

		String obterNome();

		String obterNumero();

		String obterPais();

		String obterRua();
	}

	void adicionarContato(String nome, String valor, String nomeContato) throws Exception;

	void atribuirSigla(String sigla) throws Exception;

	void atribuirApelido(String apelido) throws Exception;

	void atribuirRUC(String ruc) throws Exception;

	void atribuirNome(String nome) throws Exception;

	void atribuirResponsavel(Usuario responsavel) throws Exception;

	void atribuirSuperior(Entidade superior) throws Exception;

	void atualizar() throws Exception;

	void atualizarRuc(String ruc) throws Exception;

	void atualizarSigla(String sigla) throws Exception;
	
	void atualizarInferiores(Usuario responsavel) throws Exception;

	void apagarTotalizacaoExistente(String mesAno) throws Exception;

	boolean equals(Object object);

	void excluir() throws Exception;

	void excluirDuplicidade(Date dataInicio, Date dataFim) throws Exception;

	void incluir() throws Exception;

	Endereco novoEndereco() throws Exception;

	String obterSigla() throws Exception;

	String obterRuc() throws Exception;

	String obterApelido() throws Exception;

	Atributo obterAtributo(String nome) throws Exception;

	Collection obterAtributos() throws Exception;

	Date obterAtualizacao() throws Exception;

	Collection obterDatasMovimentacao() throws Exception;

	Inscricao obterInscricaoAtiva() throws Exception;
	
	Inscricao obterUltimaInscricao() throws Exception;
	
	Inscricao obterUltimaInscricaoVigente() throws Exception;

	String obterClasse() throws Exception;

	Contato obterContato(int id) throws Exception;

	String obterContato(String nome) throws Exception;

	Collection obterContatos() throws Exception;

	Date obterCriacao() throws Exception;

	String obterDescricaoClasse() throws Exception;

	Entidade obterEmpresa() throws Exception;

	Entidade obterAseguradoraComoEmpresa() throws Exception;

	Endereco obterEndereco(int id) throws Exception;
	Endereco obterEndereco(String nome) throws Exception;

	Collection obterEnderecos() throws Exception;
	
	Collection obterAgendas(String tipo) throws Exception;

	Collection obterEventosComoDestino() throws Exception;

	Collection obterEventosComoDestinoHistorico(int pagina) throws Exception;

	Collection obterEventosComoOrigem() throws Exception;

	Collection obterEventosComoOrigemHistorico(int pagina) throws Exception;

	Collection obterEventosDeResponsabilidade() throws Exception;

	Collection obterEventosDeResponsabilidadeHistorico(int pagina) throws Exception;

	Collection obterEventosAgenda(String mesAno, int pagina) throws Exception;

	Collection obterAgendas(boolean fasePendente) throws Exception;

	Collection obterEntidadesNivel1(Entidade seguradora, String mesAno)
			throws Exception;

	Collection obterEntidadesNivel2(Entidade seguradora, String mesAno)
			throws Exception;

	Collection obterEntidadesNivel3(Entidade seguradora, String mesAno)
			throws Exception;

	Collection obterEntidadesNivel4(Entidade seguradora, String mesAno)
			throws Exception;

	Collection obterEntidadesNivel5(Entidade seguradora, String mesAno)
			throws Exception;

	String obterIcone() throws Exception;

	long obterId();

	Collection<Entidade> obterInferiores() throws Exception;
	
	Collection obterLogs(int pagina) throws Exception;

	Collection obterApolicesComoAgente() throws Exception;

	Collection<Apolice> obterApolicesComoAgentePorPeriodo(Date dataInicio, Date dataFim, Aseguradora aseguradora)throws Exception;
	Collection<Apolice> obterApolicesComoCorredorPorPeriodo(Date dataInicio,Date dataFim, Aseguradora aseguradora) throws Exception;

	Collection obterApolicesComoOrigem() throws Exception;
	
	Collection obterApolicesVigentes() throws Exception;

	String obterNome() throws Exception;

	boolean verificarRuc(String ruc) throws Exception;

	Collection obterNomesContatos() throws Exception;

	Collection obterNomesEnderecos() throws Exception;

	long obterNumeroEntidadeInferiores() throws Exception;

	Collection obterPossiveisSuperiores() throws Exception;

	Usuario obterResponsavel() throws Exception;

	Entidade obterSuperior() throws Exception;

	Collection<Entidade> obterSuperiores() throws Exception;
	
	Map<Long,Entidade> obterSuperioresMap() throws Exception;

	boolean permiteAtualizar() throws Exception;

	boolean permiteAtualizarResponsavel() throws Exception;

	boolean permiteAtualizarSuperior() throws Exception;

	boolean permiteEventosComoOrigem() throws Exception;

	boolean permiteExcluir() throws Exception;

	boolean permiteIncluirEntidadesInferiores() throws Exception;

	boolean possuiEventosVinculados() throws Exception;

	void removerContato(Contato contato) throws Exception;

	void removerEndereco(Endereco endereco) throws Exception;

	boolean temInferiores() throws Exception;

	boolean validaCPF(String cpf) throws Exception;

	boolean validaCNPJ(String cnpj) throws Exception;
	
	double obterCapitalGsReaseguradora(Aseguradora aseguradora,
			Date dataInicio, Date dataFim, String tipoContrato)
			throws Exception;

	double obterPrimaGsReaseguradora(Aseguradora aseguradora, Date dataInicio,
			Date dataFim, String tipoContrato) throws Exception;

	double obterComissaoGsReaseguradora(Aseguradora aseguradora,
			Date dataInicio, Date dataFim, String tipoContrato)
			throws Exception;

	double obterCapitalGsCorretora(Aseguradora aseguradora, Date dataInicio,
			Date dataFim, String tipoContrato) throws Exception;

	double obterPrimaGsCorretora(Aseguradora aseguradora, Date dataInicio,
			Date dataFim, String tipoContrato) throws Exception;

	double obterComissaoGsCorretora(Aseguradora aseguradora, Date dataInicio,
			Date dataFim, String tipoContrato) throws Exception;

	Collection<Inscricao> obterInscricoes() throws Exception;

	//  ************************** Só para o cartão
	// *********************************************
	Collection obterMovimentoContabil(Date dia) throws Exception;

	Collection obterContasPagarReceber(Date dia) throws Exception;

	Collection obterFluxoCaixa() throws Exception;

	double obterTotalCreditoPrevisto() throws Exception;

	double obterTotalCreditoRealizado() throws Exception;

	double obterTotalDebitoPrevisto() throws Exception;

	double obterTotalDebitoRealizado() throws Exception;

}