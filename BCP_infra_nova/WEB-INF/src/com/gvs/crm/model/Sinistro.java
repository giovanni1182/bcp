package com.gvs.crm.model;

import java.util.Date;

public interface Sinistro extends Evento {
	// REGISTRO 06

	void atribuirNumero(String numero) throws Exception;

	void atribuirDataSinistro(Date data) throws Exception;

	void atribuirDataDenuncia(Date data) throws Exception;

	void atribuirAgente(AuxiliarSeguro auxiliar) throws Exception;

	void atribuirMontanteGs(double valor) throws Exception;

	void atribuirTipoMoedaMontanteGs(String tipo) throws Exception;

	void atribuirMontanteMe(double valor) throws Exception;

	void atribuirSituacao(String situacao) throws Exception;

	void atribuirDataPagamento(Date data) throws Exception;

	void atribuirDataRecuperacao(Date data) throws Exception;

	void atribuirValorRecuperacao(double valor) throws Exception;

	void atribuirValorRecuperacaoTerceiro(double valor) throws Exception;

	void atribuirParticipacao(double valor) throws Exception;

	void atribuirTipoInstrumento(String tipo) throws Exception;

	void atribuirNumeroEndoso(double numeroEndoso) throws Exception;

	void atribuirCertificado(double certificado) throws Exception;

	void atualizarNumero(String numero) throws Exception;

	void atualizarDataSinistro(Date data) throws Exception;

	void atualizarDataDenuncia(Date data) throws Exception;

	void atualizarAgente(AuxiliarSeguro auxiliar) throws Exception;

	void atualizarMontanteGs(double valor) throws Exception;

	void atualizarTipoMoedaMontanteGs(String tipo) throws Exception;

	void atualizarMontanteMe(double valor) throws Exception;

	void atualizarSituacao(String situacao) throws Exception;

	void atualizarDataPagamento(Date data) throws Exception;

	void atualizarDataRecuperacao(Date data) throws Exception;

	void atualizarValorRecuperacao(double valor) throws Exception;

	void atualizarValorRecuperacaoTerceiro(double valor) throws Exception;

	void atualizarParticipacao(double valor) throws Exception;

	void incluir() throws Exception;

	String obterNumero() throws Exception;

	Date obterDataSinistro() throws Exception;

	Date obterDataDenuncia() throws Exception;

	AuxiliarSeguro obterAuxiliar() throws Exception;

	double obterMontanteGs() throws Exception;

	String obterTipoMoedaMontanteGs() throws Exception;

	double obterMontanteMe() throws Exception;

	String obterSituacao() throws Exception;

	Date obterDataPagamento() throws Exception;

	Date obterDataRecuperacao() throws Exception;

	double obterValorRecuperacao() throws Exception;

	double obterValorRecuperacaoTerceiro() throws Exception;

	double obterParticipacao() throws Exception;

	String obterTipoInstrumento() throws Exception;

	double obterNumeroEndoso() throws Exception;

	double obterCertificado() throws Exception;

	void verificarDuplicidade(Apolice apolice, ClassificacaoContas cContas,
			String numeroSinistro, double numeroEndoso) throws Exception;
	
	CodificacaoPlano obterCodificacaoPlano() throws Exception;
	CodificacaoCobertura obterCodificacaoCobertura() throws Exception;
	CodificacaoRisco obterCodificacaoRisco() throws Exception;
	CodificacaoDetalhe obterCodificacaoDetalhe() throws Exception;
	
}