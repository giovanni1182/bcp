package com.gvs.crm.model;

import java.util.Date;

public interface DadosPrevisao extends Evento {
	// REGISTRO 03

	void atualizarDataCorte(Date dataCorte) throws Exception;

	void atualizarCurso(double curso) throws Exception;

	void atualizarSinistroPendente(double pendente) throws Exception;

	void atualizarReservasMatematicas(double reservas) throws Exception;

	void atualizarFundosAcumulados(double fundos) throws Exception;

	void atualizarPremios(double premios) throws Exception;

	void atribuirDataCorte(Date dataCorte) throws Exception;

	void atribuirCurso(double curso) throws Exception;

	void atribuirSinistroPendente(double pendente) throws Exception;

	void atribuirReservasMatematicas(double reservas) throws Exception;

	void atribuirFundosAcumulados(double fundos) throws Exception;

	void atribuirPremios(double premios) throws Exception;

	void atribuirTipoInstrumento(String tipo) throws Exception;

	void atribuirNumeroEndoso(double numeroEndoso) throws Exception;

	void atribuirCertificado(double certificado) throws Exception;

	void incluir() throws Exception;

	Date obterDataCorte() throws Exception;

	double obterCurso() throws Exception;

	double obterSinistroPendente() throws Exception;

	double obterReservasMatematicas() throws Exception;

	double obterFundosAcumulados() throws Exception;

	double obterPremios() throws Exception;

	String obterTipoInstrumento() throws Exception;

	double obterNumeroEndoso() throws Exception;

	double obterCertificado() throws Exception;

	void verificarDuplicidade(Apolice apolice, ClassificacaoContas cContas,
			Date dataCorte, double numeroEndoso) throws Exception;
}