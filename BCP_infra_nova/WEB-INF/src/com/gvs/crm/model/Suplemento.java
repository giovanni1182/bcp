package com.gvs.crm.model;

import java.util.Date;

public interface Suplemento extends Evento {
	// REGISTRO 11

	void atribuirNumero(String numero) throws Exception;

	void atribuirDataEmissao(Date data) throws Exception;

	void atribuirRazao(String numero) throws Exception;

	void atribuirPrimaGs(double valor) throws Exception;

	void atribuirTipoMoedaPrimaGs(String tipo) throws Exception;

	void atribuirPrimaMe(double valor) throws Exception;

	void atribuirTipoInstrumento(String tipo) throws Exception;

	void atribuirNumeroEndoso(double numeroEndoso) throws Exception;

	void atribuirCertificado(double certificado) throws Exception;

	void atualizarNumero(String numero) throws Exception;

	void atualizarDataEmissao(Date data) throws Exception;

	void atualizarRazao(String numero) throws Exception;

	void atualizarPrimaGs(double valor) throws Exception;

	void atualizarTipoMoedaPrimaGs(String tipo) throws Exception;

	void atualizarPrimaMe(double valor) throws Exception;

	void incluir() throws Exception;

	String obterNumero() throws Exception;

	Date obterDataEmissao() throws Exception;

	String obterRazao() throws Exception;

	double obterPrimaGs() throws Exception;

	String obterTipoMoedaPrimaGs() throws Exception;

	double obterPrimaMe() throws Exception;

	String obterTipoInstrumento() throws Exception;

	double obterNumeroEndoso() throws Exception;

	double obterCertificado() throws Exception;

	void verificarDuplicidade(Apolice apolice, ClassificacaoContas cContas,
			String numeroEndoso) throws Exception;
}