package com.gvs.crm.model;

public interface DadosCoaseguro extends Evento {
	// REGISTRO 05

	void atualizarAseguradora(Entidade aseguradora) throws Exception;

	void atualizarCapitalGs(double valor) throws Exception;

	void atualizarTipoMoedaCapitalGs(String tipo) throws Exception;

	void atualizarCapitalMe(double valor) throws Exception;

	void atualizarParticipacao(double valor) throws Exception;

	void atualizarPrimaGs(double valor) throws Exception;

	void atualizarTipoMoedaPrimaGs(String tipo) throws Exception;

	void atualizarPrimaMe(double valor) throws Exception;

	void atualizarGrupo(String grupo) throws Exception;

	void atribuirAseguradora(Entidade aseguradora) throws Exception;

	void atribuirCapitalGs(double valor) throws Exception;

	void atribuirTipoMoedaCapitalGs(String tipo) throws Exception;

	void atribuirCapitalMe(double valor) throws Exception;

	void atribuirParticipacao(double valor) throws Exception;

	void atribuirPrimaGs(double valor) throws Exception;

	void atribuirTipoMoedaPrimaGs(String tipo) throws Exception;

	void atribuirPrimaMe(double valor) throws Exception;

	void atribuirGrupo(String grupo) throws Exception;

	void atribuirTipoInstrumento(String tipo) throws Exception;

	void atribuirNumeroEndoso(double numeroEndoso) throws Exception;

	void atribuirCertificado(double certificado) throws Exception;

	void incluir() throws Exception;

	Entidade obterAseguradora() throws Exception;

	double obterCapitalGs() throws Exception;

	String obterTipoMoedaCapitalGs() throws Exception;

	double obterCapitalMe() throws Exception;

	double obterParticipacao() throws Exception;

	double obterPrimaGs() throws Exception;

	String obterTipoMoedaPrimaGs() throws Exception;

	double obterPrimaMe() throws Exception;

	String obterGrupo() throws Exception;

	String obterTipoInstrumento() throws Exception;

	double obterNumeroEndoso() throws Exception;

	double obterCertificado() throws Exception;

	void verificarDuplicidade(Apolice apolice, ClassificacaoContas cContas,
			Entidade aseguradora, double numeroEndoso) throws Exception;

}