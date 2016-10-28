package com.gvs.crm.model;

import java.util.Date;

public interface Morosidade extends Evento {
	// REGISTRO 16

	void atribuirDataCorte(Date data) throws Exception;

	void atribuirNumeroParcela(int numero) throws Exception;

	void atribuirDataVencimento(Date data) throws Exception;

	void atribuirDiasAtraso(int dias) throws Exception;

	void atribuirValorGs(double valor) throws Exception;

	void atribuirTipoMoedaValorGs(String tipo) throws Exception;

	void atribuirValorMe(double valor) throws Exception;

	void atualizarDataCorte(Date data) throws Exception;

	void atualizarNumeroParcela(int numero) throws Exception;

	void atualizarDataVencimento(Date data) throws Exception;

	void atualizarDiasAtraso(int dias) throws Exception;

	void atualizarValorGs(double valor) throws Exception;

	void atualizarTipoMoedaValorGs(String tipo) throws Exception;

	void atualizarValorMe(double valor) throws Exception;

	void incluir() throws Exception;

	Date obterDataCorte() throws Exception;

	int obterNumeroParcela() throws Exception;

	Date obterDataVencimento() throws Exception;

	int obterDiasAtraso() throws Exception;

	double obterValorGs() throws Exception;

	String obterTipoMoedaValorGs() throws Exception;

	double obterValorMe() throws Exception;

	void verificarDuplicidade(Apolice apolice, ClassificacaoContas cContas,
			Date dataCorte) throws Exception;
}