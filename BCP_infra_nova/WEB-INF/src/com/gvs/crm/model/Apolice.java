package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface Apolice extends Evento {

	// REGISTRO 02

	public interface Finalizacao {
		Apolice obterApolice() throws Exception;

		Date obterDataFinalizacao() throws Exception;
	}

	Collection obterFinalizacoes() throws Exception;

	void adicionarFinalizacao(Date data) throws Exception;

	void excluirFinalizacao() throws Exception;

	void atualizarSecao(ClassificacaoContas cContas) throws Exception;

	void atualizarNumeroApolice(String numero) throws Exception;

	void atualizarStatusApolice(String status) throws Exception;

	void atualizarApoliceAnterior(Apolice apolice) throws Exception;

	void atualizarAfetadoPorSinistro(String valor) throws Exception;

	void atualizarApoliceFlutuante(String valor) throws Exception;

	void atualizarPlano(Plano plano) throws Exception;

	void atualizarNumeroFatura(String numero) throws Exception;

	void atualizarModalidadeVenda(String valor) throws Exception;

	void atualizarDiasCobertura(int dias) throws Exception;

	void atualizarDataEmissao(Date data) throws Exception;

	void atualizarCapitalGs(double valor) throws Exception;

	void atualizarTipoMoedaCapitalGuarani(String tipo) throws Exception;

	void atualizarCapitalMe(double valor) throws Exception;

	void atualizarPrimaGs(double valor) throws Exception;

	void atualizarTipoMoedaPrimaGs(String tipo) throws Exception;

	void atualizarPrimaMe(double valor) throws Exception;

	void atualizarPrincipalGs(double valor) throws Exception;

	void atualizarTipoMoedaPrincipalGs(String tipo) throws Exception;

	void atualizarPrincipalMe(double valor) throws Exception;

	void atualizarIncapacidadeGs(double valor) throws Exception;

	void atualizarTipoMoedaIncapacidadeGs(String tipo) throws Exception;

	void atualizarIncapacidadeMe(double valor) throws Exception;

	void atualizarEnfermidadeGs(double valor) throws Exception;

	void atualizarTipoMoedaEnfermidadeGs(String tipo) throws Exception;

	void atualizarEnfermidadeMe(double valor) throws Exception;

	void atualizarAcidentesGs(double valor) throws Exception;

	void atualizarTipoMoedaAcidentesGs(String tipo) throws Exception;

	void atualizarAcidentesMe(double valor) throws Exception;

	void atualizarOutrosGs(double valor) throws Exception;

	void atualizarTipoMoedaOutrosGs(String tipo) throws Exception;

	void atualizarOutrosMe(double valor) throws Exception;

	void atualizarFinanciamentoGs(double valor) throws Exception;

	void atualizarTipoMoedaFinanciamentoGs(String tipo) throws Exception;

	void atualizarFinanciamentoMe(double valor) throws Exception;

	void atualizarPremiosGs(double valor) throws Exception;

	void atualizarTipoMoedaPremiosGs(String tipo) throws Exception;

	void atualizarPremiosMe(double valor) throws Exception;

	void atualizarFormaPagamento(String tipo) throws Exception;

	void atualizarQtdeParcelas(int qtde) throws Exception;

	void atualizarRefinacao(String refinacao) throws Exception;

	void atualizarInscricaoAgente(Entidade agente) throws Exception;

	void atualizarComissaoGs(double valor) throws Exception;

	void atualizarTipoMoedaComissaoGs(String tipo) throws Exception;

	void atualizarComissaoMe(double valor) throws Exception;

	void atualizarSituacaoSeguro(String situacao) throws Exception;

	void atualizarDataEncerramento(Date data) throws Exception;

	void atribuirSecao(ClassificacaoContas cContas) throws Exception;

	void atribuirNumeroApolice(String numero) throws Exception;

	void atribuirStatusApolice(String status) throws Exception;

	void atribuirApoliceAnterior(Apolice apolice) throws Exception;

	void atribuirAfetadoPorSinistro(String valor) throws Exception;

	void atribuirApoliceFlutuante(String valor) throws Exception;

	void atribuirPlano(Plano plano) throws Exception;

	void atribuirCodigoPlano(String codigoPlano) throws Exception;

	void atribuirNumeroFatura(String numero) throws Exception;

	void atribuirModalidadeVenda(String valor) throws Exception;

	void atribuirDiasCobertura(int dias) throws Exception;

	void atribuirDataEmissao(Date data) throws Exception;

	void atribuirCapitalGs(double valor) throws Exception;

	void atribuirTipoMoedaCapitalGuarani(String tipo) throws Exception;

	void atribuirCapitalMe(double valor) throws Exception;

	void atribuirPrimaGs(double valor) throws Exception;

	void atribuirTipoMoedaPrimaGs(String tipo) throws Exception;

	void atribuirPrimaMe(double valor) throws Exception;

	void atribuirPrincipalGs(double valor) throws Exception;

	void atribuirTipoMoedaPrincipalGs(String tipo) throws Exception;

	void atribuirPrincipalMe(double valor) throws Exception;

	void atribuirIncapacidadeGs(double valor) throws Exception;

	void atribuirTipoMoedaIncapacidadeGs(String tipo) throws Exception;

	void atribuirIncapacidadeMe(double valor) throws Exception;

	void atribuirEnfermidadeGs(double valor) throws Exception;

	void atribuirTipoMoedaEnfermidadeGs(String tipo) throws Exception;

	void atribuirEnfermidadeMe(double valor) throws Exception;

	void atribuirAcidentesGs(double valor) throws Exception;

	void atribuirTipoMoedaAcidentesGs(String tipo) throws Exception;

	void atribuirAcidentesMe(double valor) throws Exception;

	void atribuirOutrosGs(double valor) throws Exception;

	void atribuirTipoMoedaOutrosGs(String tipo) throws Exception;

	void atribuirOutrosMe(double valor) throws Exception;

	void atribuirFinanciamentoGs(double valor) throws Exception;

	void atribuirTipoMoedaFinanciamentoGs(String tipo) throws Exception;

	void atribuirFinanciamentoMe(double valor) throws Exception;

	void atribuirPremiosGs(double valor) throws Exception;

	void atribuirTipoMoedaPremiosGs(String tipo) throws Exception;

	void atribuirPremiosMe(double valor) throws Exception;

	void atribuirFormaPagamento(String tipo) throws Exception;

	void atribuirQtdeParcelas(int qtde) throws Exception;

	void atribuirRefinacao(String refinacao) throws Exception;

	void atribuirInscricaoAgente(Entidade agente) throws Exception;

	void atribuirComissaoGs(double valor) throws Exception;

	void atribuirTipoMoedaComissaoGs(String tipo) throws Exception;

	void atribuirComissaoMe(double valor) throws Exception;

	void atribuirSituacaoSeguro(String situacao) throws Exception;

	void atribuirDataEncerramento(Date data) throws Exception;

	void atribuirCorredor(Corretora corredor) throws Exception;

	void atribuirNumeroEndoso(double numeroEndoso) throws Exception;

	void atribuirCertificado(double certificado) throws Exception;

	void atribuirNumeroEndosoAnterior(double numeroEndosoAnterior)
			throws Exception;

	void atribuirCertificadoAnterior(double certificadoAnterior)
			throws Exception;

	// REGISTRO 17
	void atualizarNomeAsegurado(String nome) throws Exception;

	void atualizarTipoPessoa(String tipo) throws Exception;

	void atualizarTipoIdentificacao(String tipo) throws Exception;

	void atualizarNumeroIdentificacao(String numero) throws Exception;

	void atualizarDataNascimento(Date data) throws Exception;

	void atualizarNomeTomador(String nome) throws Exception;

	void atribuirNomeAsegurado(String nome) throws Exception;

	void atribuirTipoPessoa(String tipo) throws Exception;

	void atribuirTipoIdentificacao(String tipo) throws Exception;

	void atribuirNumeroIdentificacao(String numero) throws Exception;

	void atribuirDataNascimento(Date data) throws Exception;

	void atribuirNomeTomador(String nome) throws Exception;

	void incluir() throws Exception;

	ClassificacaoContas obterSecao() throws Exception;

	String obterNumeroApolice() throws Exception;

	String obterStatusApolice() throws Exception;

	Apolice obterApoliceAnterior() throws Exception;

	String obterAfetadoPorSinistro() throws Exception;

	String obterApoliceFlutuante() throws Exception;

	Plano obterPlano() throws Exception;

	String obterCodigoPlano() throws Exception;

	String obterNumeroFatura() throws Exception;

	String obterModalidadeVenda() throws Exception;

	int obterDiasCobertura() throws Exception;

	Date obterDataEmissao() throws Exception;

	double obterCapitalGs() throws Exception;

	String obterTipoMoedaCapitalGuarani() throws Exception;

	double obterCapitalMe() throws Exception;

	double obterPrimaGs() throws Exception;

	String obterTipoMoedaPrimaGs() throws Exception;

	double obterPrimaMe() throws Exception;

	double obterPrincipalGs() throws Exception;

	String obterTipoMoedaPrincipalGs() throws Exception;

	double obterPrincipalMe() throws Exception;

	double obterIncapacidadeGs() throws Exception;

	String obterTipoMoedaIncapacidadeGs() throws Exception;

	double obterIncapacidadeMe() throws Exception;

	double obterEnfermidadeGs() throws Exception;

	String obterTipoMoedaEnfermidadeGs() throws Exception;

	double obterEnfermidadeMe() throws Exception;

	double obterAcidentesGs() throws Exception;

	String obterTipoMoedaAcidentesGs() throws Exception;

	double obterAcidentesMe() throws Exception;

	double obterOutrosGs() throws Exception;

	String obterTipoMoedaOutrosGs() throws Exception;

	double obterOutrosMe() throws Exception;

	double obterFinanciamentoGs() throws Exception;

	String obterTipoMoedaFinanciamentoGs() throws Exception;

	double obterFinanciamentoMe() throws Exception;

	double obterPremiosGs() throws Exception;

	String obterTipoMoedaPremiosGs() throws Exception;

	double obterPremiosMe() throws Exception;

	String obterFormaPagamento() throws Exception;

	int obterQtdeParcelas() throws Exception;

	String obterRefinacao() throws Exception;

	Entidade obterInscricaoAgente() throws Exception;

	double obterComissaoGs() throws Exception;

	String obterTipoMoedaComissaoGs() throws Exception;

	double obterComissaoMe() throws Exception;

	String obterSituacaoSeguro() throws Exception;

	Date obterDataEncerramento() throws Exception;

	Entidade obterCorredor() throws Exception;

	double obterNumeroEndoso() throws Exception;

	double obterCertificado() throws Exception;

	double obterNumeroEndosoAnterior() throws Exception;

	double obterCertificadoAnterior() throws Exception;
	
	Collection obterSinistros() throws Exception;
	
	double obterValorTotalDosSinistros(Date dataInicio, Date dataFim) throws Exception;

	// REGISTRO 17
	String obterNomeAsegurado() throws Exception;

	String obterTipoPessoa() throws Exception;

	String obterTipoIdentificacao() throws Exception;

	String obterNumeroIdentificacao() throws Exception;

	Date obterDataNascimento() throws Exception;

	String obterNomeTomador() throws Exception;
	
	String obterApoliceSuspeita() throws Exception;
	boolean estaAnulada() throws Exception;
	
	double obterMontanteGsSinistro(String situacao, Date dataInicio, Date dataFim) throws Exception;
	double obterRecuperadosSinistro(String situacao, Date dataInicio, Date dataFim) throws Exception;
	int obterQtdeReaseguros(double valor) throws Exception;
	String obterValoresBuscaInstrumento() throws Exception;
	boolean realmenteVigente() throws Exception;
	Collection<EntidadeBCP> obterAsegurados() throws Exception;
	Collection<EntidadeBCP> obterTomadores() throws Exception;
	
}