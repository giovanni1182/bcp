package com.gvs.crm.model;

public interface Refinacao extends Evento {
	// REGISTRO 13

	void atribuirFinanciamentoGs(double valor) throws Exception;

	void atribuirTipoMoedaFinanciamentoGs(String tipo) throws Exception;

	void atribuirFinanciamentoMe(double valor) throws Exception;

	void atribuirQtdeParcelas(int qtde) throws Exception;

	void atribuirTipoInstrumento(String tipo) throws Exception;

	void atribuirNumeroEndoso(double numeroEndoso) throws Exception;

	void atribuirCertificado(double certificado) throws Exception;

	void atualizarFinanciamentoGs(double valor) throws Exception;

	void atualizarTipoMoedaFinanciamentoGs(String tipo) throws Exception;

	void atualizarFinanciamentoMe(double valor) throws Exception;

	void atualizarQtdeParcelas(int qtde) throws Exception;

	void incluir() throws Exception;

	double obterFinanciamentoGs() throws Exception;

	String obterTipoMoedaFinanciamentoGs() throws Exception;

	double obterFinanciamentoMe() throws Exception;

	int obterQtdeParcelas() throws Exception;

	String obterTipoInstrumento() throws Exception;

	double obterNumeroEndoso() throws Exception;

	double obterCertificado() throws Exception;

	void verificarDuplicidade(Apolice apolice, ClassificacaoContas cContas,
			double numeroEndoso) throws Exception;
}