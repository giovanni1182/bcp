package com.gvs.crm.model;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Produto extends Entidade {
	public interface Fornecedor {
		String obterCodigoExterno() throws Exception;

		Entidade obterEntidade() throws Exception;

		double obterValorFornecedor() throws Exception;
	}

	public interface Preco {
		String obterMoeda() throws Exception;

		String obterTipo() throws Exception;

		double obterValor() throws Exception;
	}

	public interface Status {
		String obterStatus() throws Exception;

		String obterAcao() throws Exception;

		int obterSequencia();
	}

	public interface StatusAgenda {
		String obterStatus() throws Exception;

		int obterSequencia();
	}

	public interface StatusAgendaProcesso {
		String obterStatus() throws Exception;

		int obterDia() throws Exception;

		int obterSequencia();
	}

	void atualizarTipoProduto(String tipo) throws Exception;

	void atualizarPeso(double peso) throws Exception;

	void adicionarFornecedor(Entidade fornecedor, String codigoExterno,
			double valor) throws Exception;

	void adicionarPreco(String tipo, String moeda, double valor)
			throws Exception;

	void atribuirApelidos(Set apelidos) throws Exception;

	void atribuirAtivo(boolean ativo) throws Exception;

	void atribuirTipoProduto(String tipo) throws Exception;

	void atribuirCodigoExterno(String codigoExterno) throws Exception;

	void atribuirUnidade(String unidade) throws Exception;

	boolean eEspecial() throws Exception;

	boolean estaAtivo() throws Exception;

	String obterTipoProduto() throws Exception;

	Collection obterApelidos() throws Exception;

	String obterCodigoExterno() throws Exception;

	String obterDimensao() throws Exception;

	Fornecedor obterFornecedor(Entidade fornecedor) throws Exception;

	Collection obterFornecedores() throws Exception;

	Collection obterOcorrencias() throws Exception;

	Preco obterPreco(String tipo) throws Exception;

	Collection obterPrecos() throws Exception;

	String obterUnidade() throws Exception;

	void removerFornecedor(Entidade fornecedor) throws Exception;

	void removerPreco(Preco preco) throws Exception;

	double obterValorFornecedor(Entidade fornecedor) throws Exception;

	double obterReservaDeEntrada() throws Exception;

	double obterReservaDeSaida() throws Exception;

	double obterQuantidadeDisponivel() throws Exception;

	double obterQuantidadeMinima() throws Exception;

	double obterQuantidadeAtual() throws Exception;

	double obterSugestao() throws Exception;

	double obterPeso() throws Exception;

	String obterIpi() throws Exception;

	void atualizarIpi(String ipi) throws Exception;

	Map obterStatus() throws Exception;

	Status obterStatus(int sequencia) throws Exception;

	void removerStatus(Produto.Status status) throws Exception;

	void adicionarStatus(String status, String acao) throws Exception;

	Map obterStatusAgenda() throws Exception;

	StatusAgenda obterStatusAgenda(int sequencia) throws Exception;

	void removerStatusAgenda(Produto.StatusAgenda status) throws Exception;

	void adicionarStatusAgenda(String status) throws Exception;

	Map obterStatusAgendaProcesso() throws Exception;

	StatusAgendaProcesso obterStatusAgendaProcesso(int sequencia)
			throws Exception;

	void removerStatusAgendaProcesso(Produto.StatusAgendaProcesso status)
			throws Exception;

	void adicionarStatusAgendaProcesso(String status, int dia) throws Exception;
}