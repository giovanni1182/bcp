package com.gvs.crm.model;

import java.util.Collection;

public interface DocumentoProduto extends Evento {
	public interface Pessoa {
		DocumentoProduto obterDocumento() throws Exception;

		int obterSeq() throws Exception;

		Entidade obterPessoa() throws Exception;

		String obterTipo() throws Exception;

		int obterPosicao() throws Exception;
	}

	public static String CHEFE_DIVISAO = "chefedivisao";

	public static String INTENDENTE = "intendente";

	public static String SUPERINTENDENTE = "superintendente";

	public static String APROVADA = "aprovado";

	public static String REJEITADA = "rejeitado";

	public static String INFORMATIVO = "informativo";

	void atualizarDocumento(EntidadeDocumento documento) throws Exception;

	void atualizarNumero(String numero) throws Exception;

	void atualizarReferente(String referente) throws Exception;

	void atualizarAnalista(Entidade analista) throws Exception;

	void atualizarChefeDivisao(Entidade chefeDivisao) throws Exception;

	void atualizarIntendente(Entidade intendente) throws Exception;

	void atualizarSuperIntendente(Entidade superIntendente) throws Exception;

	void atualizarTituloDocumento(String tituloDocumento) throws Exception;

	void atualizarTexto(String texto) throws Exception;

	void incluir() throws Exception;

	void exclurInscricaoVinculada(Inscricao inscricao) throws Exception;

	Collection obterInscricoesVinculadas() throws Exception;

	void adicionarInscricaoVinculado(Inscricao inscricao) throws Exception;

	EntidadeDocumento obterDocumento() throws Exception;

	String obterNumero() throws Exception;

	String obterReferente() throws Exception;

	String obterTituloDocumento() throws Exception;

	Entidade obterAnalista() throws Exception;

	Entidade obterChefeDivisao() throws Exception;

	Entidade obterIntendente() throws Exception;

	Entidade obterSuperIntendente() throws Exception;

	String obterTexto() throws Exception;

	void adicionarNovaPessoa(Entidade pessoa, String tipo, int posicao)
			throws Exception;

	Collection obterTiposPessoas() throws Exception;

	Collection obterPessoasDepois() throws Exception;

	Collection obterPessoasAntes() throws Exception;

	void excluirPessoaDocumento(Entidade pessoa, int posicao) throws Exception;
}