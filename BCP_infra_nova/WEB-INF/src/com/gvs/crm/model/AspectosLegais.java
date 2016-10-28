package com.gvs.crm.model;

import java.util.Date;

public interface AspectosLegais extends Evento {
	// REGISTRO 10

	void atribuirNumeroOrdem(String numero) throws Exception;

	void atribuirDataNotificacao(Date data) throws Exception;

	void atribuirAssunto(String assunto) throws Exception;

	void atribuirDemandante(String demandante) throws Exception;

	void atribuirDemandado(String demandado) throws Exception;

	void atribuirJulgado(String julgado) throws Exception;

	void atribuirTurno(String turno) throws Exception;

	void atribuirJuiz(String juiz) throws Exception;

	void atribuirSecretaria(String secretaria) throws Exception;

	void atribuirAdvogado(String advogado) throws Exception;

	void atribuirCircunscricao(String circunscricao) throws Exception;

	void atribuirForum(String forum) throws Exception;

	void atribuirDataDemanda(Date data) throws Exception;

	void atribuirMontanteDemandado(double valor) throws Exception;

	void atribuirMontanteSentenca(double valor) throws Exception;

	void atribuirDataCancelamento(Date data) throws Exception;

	void atribuirResponsabilidadeMaxima(double valor) throws Exception;

	void atribuirSinistroPendente(double valor) throws Exception;

	void atribuirObjetoCausa(String objeto) throws Exception;

	void atribuirTipoInstrumento(String tipo) throws Exception;

	void atribuirNumeroEndoso(double numeroEndoso) throws Exception;

	void atribuirCertificado(double certificado) throws Exception;

	void atualizarNumeroOrdem(String numero) throws Exception;

	void atualizarDataNotificacao(Date data) throws Exception;

	void atualizarAssunto(String assunto) throws Exception;

	void atualizarDemandante(String demandante) throws Exception;

	void atualizarDemandado(String demandado) throws Exception;

	void atualizarJulgado(String julgado) throws Exception;

	void atualizarTurno(String turno) throws Exception;

	void atualizarJuiz(String juiz) throws Exception;

	void atualizarSecretaria(String secretaria) throws Exception;

	void atualizarAdvogado(String advogado) throws Exception;

	void atualizarCircunscricao(String circunscricao) throws Exception;

	void atualizarForum(String forum) throws Exception;

	void atualizarDataDemanda(Date data) throws Exception;

	void atualizarMontanteDemandado(double valor) throws Exception;

	void atualizarMontanteSentenca(double valor) throws Exception;

	void atualizarDataCancelamento(Date data) throws Exception;

	void atualizarResponsabilidadeMaxima(double valor) throws Exception;

	void atualizarSinistroPendente(double valor) throws Exception;

	void atualizarObjetoCausa(String objeto) throws Exception;

	void incluir() throws Exception;

	String obterNumeroOrdem() throws Exception;

	Date obterDataNotificacao() throws Exception;

	String obterAssunto() throws Exception;

	String obterDemandante() throws Exception;

	String obterDemandado() throws Exception;

	String obterJulgado() throws Exception;

	String obterTurno() throws Exception;

	String obterJuiz() throws Exception;

	String obterSecretaria() throws Exception;

	String obterAdvogado() throws Exception;

	String obterCircunscricao() throws Exception;

	String obterForum() throws Exception;

	Date obterDataDemanda() throws Exception;

	double obterMontanteDemandado() throws Exception;

	double obterMontanteSentenca() throws Exception;

	Date obterDataCancelamento() throws Exception;

	double obterResponsabilidadeMaxima() throws Exception;

	double obterSinistroPendente() throws Exception;

	String obterObjetoCausa() throws Exception;

	String obterTipoInstrumento() throws Exception;

	double obterNumeroEndoso() throws Exception;

	double obterCertificado() throws Exception;

	void verificarDuplicidade(Apolice apolice, ClassificacaoContas cContas,
			String numeroOrdem, double numeroEndoso) throws Exception;
}