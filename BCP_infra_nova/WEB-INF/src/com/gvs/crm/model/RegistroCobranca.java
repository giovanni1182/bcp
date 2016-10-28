package com.gvs.crm.model;

import java.util.Date;

public interface RegistroCobranca extends Evento {
	// REGISTRO 09

	void atribuirDataCobranca(Date data) throws Exception;

	void atribuirDataVencimento(Date data) throws Exception;

	void atribuirNumeroParcela(int numero) throws Exception;

	void atribuirValorCobrancaGs(double valor) throws Exception;

	void atribuirTipoMoedaValorCobrancaGs(String tipo) throws Exception;

	void atribuirValorCobrancaMe(double valor) throws Exception;

	void atribuirValorInteres(double valor) throws Exception;

	void atribuirTipoInstrumento(String tipo) throws Exception;

	void atribuirNumeroEndoso(double numeroEndoso) throws Exception;

	void atribuirCertificado(double certificado) throws Exception;

	void atualizarDataCobranca(Date data) throws Exception;

	void atualizarDataVencimento(Date data) throws Exception;

	void atualizarNumeroParcela(int numero) throws Exception;

	void atualizarValorCobrancaGs(double valor) throws Exception;

	void atualizarTipoMoedaValorCobrancaGs(String tipo) throws Exception;

	void atualizarValorCobrancaMe(double valor) throws Exception;

	void atualizarValorInteres(double valor) throws Exception;

	void incluir() throws Exception;

	Date obterDataCobranca() throws Exception;

	Date obterDataVencimento() throws Exception;

	int obterNumeroParcela() throws Exception;

	double obterValorCobrancaGs() throws Exception;

	String obterTipoMoedaValorCobrancaGs() throws Exception;

	double obterValorCobrancaMe() throws Exception;

	double obterValorInteres() throws Exception;

	String obterTipoInstrumento() throws Exception;

	double obterNumeroEndoso() throws Exception;

	double obterCertificado() throws Exception;

	void verificarDuplicidade(Apolice apolice, ClassificacaoContas cContas,
			Date dataCobranca, int numeroParcela, double numeroEndoso)
			throws Exception;
}