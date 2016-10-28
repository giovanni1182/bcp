package com.gvs.crm.model;

public interface Renovacao extends Evento {
	void atualizarMatriculaAnterior(int arg) throws Exception;

	void atualizarCertificadoAntecedentes(int arg) throws Exception;

	void atualizarCertificadoJudicial(int arg) throws Exception;

	void atualizarCertificadoTributario(int arg) throws Exception;

	void atualizarDeclaracao(int arg) throws Exception;

	void atualizarComprovanteMatricula(int arg) throws Exception;

	void atualizarApoliceSeguro(int arg) throws Exception;

	void atualizarLivro(int arg) throws Exception;

	void incluir() throws Exception;

	boolean obterMatriculaAnterior() throws Exception;

	boolean obterCertificadoAntecedentes() throws Exception;

	boolean obterCertificadoJudicial() throws Exception;

	boolean obterCertificadoTributario() throws Exception;

	boolean obterDeclaracao() throws Exception;

	boolean obterComprovanteMatricula() throws Exception;

	boolean obterApoliceSeguro() throws Exception;

	boolean obterLivro() throws Exception;

}