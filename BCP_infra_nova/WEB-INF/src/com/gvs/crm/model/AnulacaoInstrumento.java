package com.gvs.crm.model;

import java.util.Date;

public interface AnulacaoInstrumento extends Evento {
	// REGISTRO 08

	void atribuirDataAnulacao(Date data) throws Exception;

	void atribuirCapitalGs(double valor) throws Exception;

	void atribuirTipoMoedaCapitalGs(String tipo) throws Exception;

	void atribuirCapitalMe(double valor) throws Exception;

	void atribuirSolicitadoPor(String valor) throws Exception;

	void atribuirDiasCorridos(int dias) throws Exception;

	void atribuirPrimaGs(double valor) throws Exception;

	void atribuirTipoMoedaPrimaGs(String tipo) throws Exception;

	void atribuirPrimaMe(double valor) throws Exception;

	void atribuirComissaoGs(double valor) throws Exception;

	void atribuirTipoMoedaComissaoGs(String tipo) throws Exception;

	void atribuirComissaoMe(double valor) throws Exception;

	void atribuirComissaoRecuperarGs(double valor) throws Exception;

	void atribuirTipoMoedaComissaoRecuperarGs(String tipo) throws Exception;

	void atribuirComissaoRecuperarMe(double valor) throws Exception;

	void atribuirSaldoAnulacaoGs(double valor) throws Exception;

	void atribuirTipoMoedaSaldoAnulacaoGs(String tipo) throws Exception;

	void atribuirSaldoAnulacaoMe(double valor) throws Exception;

	void atribuirDestinoSaldoAnulacao(String destino) throws Exception;

	void atribuirTipoInstrumento(String tipo) throws Exception;

	void atribuirNumeroEndoso(double numeroEndoso) throws Exception;

	void atribuirCertificado(double certificado) throws Exception;

	void atualizarDataAnulacao(Date data) throws Exception;

	void atualizarCapitalGs(double valor) throws Exception;

	void atualizarTipoMoedaCapitalGs(String tipo) throws Exception;

	void atualizarCapitalMe(double valor) throws Exception;

	void atualizarSolicitadoPor(String valor) throws Exception;

	void atualizarDiasCorridos(int dias) throws Exception;

	void atualizarPrimaGs(double valor) throws Exception;

	void atualizarTipoMoedaPrimaGs(String tipo) throws Exception;

	void atualizarPrimaMe(double valor) throws Exception;

	void atualizarComissaoGs(double valor) throws Exception;

	void atualizarTipoMoedaComissaoGs(String tipo) throws Exception;

	void atualizarComissaoMe(double valor) throws Exception;

	void atualizarComissaoRecuperarGs(double valor) throws Exception;

	void atualizarTipoMoedaComissaoRecuperarGs(String tipo) throws Exception;

	void atualizarComissaoRecuperarMe(double valor) throws Exception;

	void atualizarSaldoAnulacaoGs(double valor) throws Exception;

	void atualizarTipoMoedaSaldoAnulacaoGs(String tipo) throws Exception;

	void atualizarSaldoAnulacaoMe(double valor) throws Exception;

	void atualizarDestinoSaldoAnulacao(String destino) throws Exception;

	void incluir() throws Exception;

	Date obterDataAnulacao() throws Exception;

	double obterCapitalGs() throws Exception;

	String obterTipoMoedaCapitalGs() throws Exception;

	double obterCapitalMe() throws Exception;

	String obterSolicitadoPor() throws Exception;

	int obterDiasCorridos() throws Exception;

	double obterPrimaGs() throws Exception;

	String obterTipoMoedaPrimaGs() throws Exception;

	double obterPrimaMe() throws Exception;

	double obterComissaoGs() throws Exception;

	String obterTipoMoedaComissaoGs() throws Exception;

	double obterComissaoMe() throws Exception;

	double obterComissaoRecuperarGs() throws Exception;

	String obterTipoMoedaComissaoRecuperarGs() throws Exception;

	double obterComissaoRecuperarMe() throws Exception;

	double obterSaldoAnulacaoGs() throws Exception;

	String obterTipoMoedaSaldoAnulacaoGs() throws Exception;

	double obterSaldoAnulacaoMe() throws Exception;

	String obterDestinoSaldoAnulacao() throws Exception;

	String obterTipoInstrumento() throws Exception;

	double obterNumeroEndoso() throws Exception;

	double obterCertificado() throws Exception;

	void verificarDuplicidade(Apolice apolice, ClassificacaoContas cContas,
			Date dataAnulacao, double numeroEndoso) throws Exception;
}