package com.gvs.crm.model;


public interface DadosReaseguro extends Evento {
	// REGISTRO 04

	void atualizarReaseguradora(Entidade reaseguradora) throws Exception;

	void atualizarTipoContrato(String tipo) throws Exception;

	void atualizarCorredora(Entidade corredora) throws Exception;

	void atualizarCapitalGs(double valor) throws Exception;

	void atualizarTipoMoedaCapitalGs(String tipo) throws Exception;

	void atualizarCapitalMe(double valor) throws Exception;

	void atualizarPrimaGs(double valor) throws Exception;

	void atualizarTipoMoedaPrimaGs(String tipo) throws Exception;

	void atualizarPrimaMe(double valor) throws Exception;

	void atualizarComissaoGs(double valor) throws Exception;

	void atualizarTipoMoedaComissaoGs(String tipo) throws Exception;

	void atualizarComissaoMe(double valor) throws Exception;

	void atualizarSituacao(String situacao) throws Exception;

	void atribuirReaseguradora(Entidade reaseguradora) throws Exception;

	void atribuirTipoContrato(String tipo) throws Exception;

	void atribuirCorredora(Entidade corredora) throws Exception;

	void atribuirCapitalGs(double valor) throws Exception;

	void atribuirTipoMoedaCapitalGs(String tipo) throws Exception;

	void atribuirCapitalMe(double valor) throws Exception;

	void atribuirPrimaGs(double valor) throws Exception;

	void atribuirTipoMoedaPrimaGs(String tipo) throws Exception;

	void atribuirPrimaMe(double valor) throws Exception;

	void atribuirComissaoGs(double valor) throws Exception;

	void atribuirTipoMoedaComissaoGs(String tipo) throws Exception;

	void atribuirComissaoMe(double valor) throws Exception;

	void atribuirSituacao(String situacao) throws Exception;

	void atribuirValorEndoso(double valor) throws Exception;

	void atribuirTipoInstrumento(String tipo) throws Exception;

	void incluir() throws Exception;

	Entidade obterReaseguradora() throws Exception;

	String obterTipoContrato() throws Exception;

	Entidade obterCorredora() throws Exception;

	double obterCapitalGs() throws Exception;

	String obterTipoMoedaCapitalGs() throws Exception;

	double obterCapitalMe() throws Exception;

	double obterPrimaGs() throws Exception;

	String obterTipoMoedaPrimaGs() throws Exception;

	double obterPrimaMe() throws Exception;

	double obterComissaoGs() throws Exception;

	String obterTipoMoedaComissaoGs() throws Exception;

	double obterComissaoMe() throws Exception;

	String obterSituacao() throws Exception;

	double obterValorEndoso() throws Exception;

	String obterTipoInstrumento() throws Exception;
	String obterValoresParciaisAnulacao() throws Exception;

	void verificarDuplicidade(Apolice apolice, ClassificacaoContas cContas,
			Entidade reaseguradora, String tipoContrato, double valorEndoso)
			throws Exception;
	
	//Collection<String> obterDadosReaseguro(Aseguradora aseguradora, Date dataInicio, Date dataFim, String situacao) throws Exception;
}