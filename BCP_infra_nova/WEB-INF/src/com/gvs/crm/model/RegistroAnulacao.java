package com.gvs.crm.model;

import java.util.Date;

public interface RegistroAnulacao extends Evento {
	// REGISTRO 15

	void atribuirReaeguradora(Reaseguradora reaseguradora) throws Exception;

	void atribuirTipoContrato(String tipo) throws Exception;

	void atribuirDataAnulacao(Date data) throws Exception;

	void atribuirCapitalGs(double valor) throws Exception;

	void atribuirTipoMoedaCapitalGs(String tipo) throws Exception;

	void atribuirCapitalMe(double valor) throws Exception;

	void atribuirDiasCorridos(int dias) throws Exception;

	void atribuirPrimaGs(double valor) throws Exception;

	void atribuirTipoMoedaPrimaGs(String tipo) throws Exception;

	void atribuirPrimaMe(double valor) throws Exception;

	void atribuirComissaoGs(double valor) throws Exception;

	void atribuirTipoMoedaComissaoGs(String tipo) throws Exception;

	void atribuirComissaoMe(double valor) throws Exception;

	void atualizarReaeguradora(Reaseguradora reaseguradora) throws Exception;

	void atualizarTipoContrato(String tipo) throws Exception;

	void atualizarDataAnulacao(Date data) throws Exception;

	void atualizarCapitalGs(double valor) throws Exception;

	void atualizarTipoMoedaCapitalGs(String tipo) throws Exception;

	void atualizarCapitalMe(double valor) throws Exception;

	void atualizarDiasCorridos(int dias) throws Exception;

	void atualizarPrimaGs(double valor) throws Exception;

	void atualizarTipoMoedaPrimaGs(String tipo) throws Exception;

	void atualizarPrimaMe(double valor) throws Exception;

	void atualizarComissaoGs(double valor) throws Exception;

	void atualizarTipoMoedaComissaoGs(String tipo) throws Exception;

	void atualizarComissaoMe(double valor) throws Exception;

	void incluir() throws Exception;

	Reaseguradora obterReaeguradora() throws Exception;

	String obterTipoContrato() throws Exception;

	Date obterDataAnulacao() throws Exception;

	double obterCapitalGs() throws Exception;

	String obterTipoMoedaCapitalGs() throws Exception;

	double obterCapitalMe() throws Exception;

	int obterDiasCorridos() throws Exception;

	double obterPrimaGs() throws Exception;

	String obterTipoMoedaPrimaGs() throws Exception;

	double obterPrimaMe() throws Exception;

	double obterComissaoGs() throws Exception;

	String obterTipoMoedaComissaoGs() throws Exception;

	double obterComissaoMe() throws Exception;

	void verificarDuplicidade(Apolice apolice, ClassificacaoContas cContas,
			Reaseguradora reaseguradora, String tipo) throws Exception;
}