package com.gvs.crm.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

public interface Conta extends Entidade {
	public interface Conta2 {
		long obterContaId() throws Exception;

		long obterSuperiorId() throws Exception;

		String obterCodigoConta() throws Exception;

		String obterNomeConta() throws Exception;

	}

	Collection obterContas() throws Exception;
	
	boolean permiteAtualizar() throws Exception;

	void atribuirCodigo(String codigo) throws Exception;

	void atribuirDescricao(String descricao) throws Exception;

	void atribuirAtivo(String ativo) throws Exception;

	void atribuirNivel(String nivel) throws Exception;

	void atribuirConcepto(String descricao) throws Exception;

	void atribuirNoma(String descricao) throws Exception;

	void atribuirNatureza(String descricao) throws Exception;

	void atribuirDinamica(String dinamica) throws Exception;

	void atualizarAtivo(String ativo) throws Exception;

	void atualizarNivel(String moeda) throws Exception;

	void atualizarDescricao(String descricao) throws Exception;

	void atualizarCodigo(String codigo) throws Exception;

	void atualizarConcepto(String descricao) throws Exception;

	void atualizarNoma(String descricao) throws Exception;

	void atualizarNatureza(String descricao) throws Exception;

	void atualizarDinamica(String dinamica) throws Exception;

	String obterDescricao() throws Exception;

	boolean obterAtivo() throws Exception;

	String obterNivel() throws Exception;

	String obterConcepto() throws Exception;

	String obterNoma() throws Exception;

	MovimentacaoFinanceiraConta obterMovimentacao(Entidade seguradora,
			boolean emGuarani, String mesAno) throws Exception;

	String obterNatureza() throws Exception;

	String obterDinamica() throws Exception;

	void incluir() throws Exception;
	
	boolean permiteExcluir() throws Exception;

	void incluirRelatorio(String mesAno, double valor,
			double valorMoedaEstrangeira, double debito, double credito,
			double saldoAnterior, Entidade seguradora) throws Exception;

	String obterCodigo() throws Exception;

	double obterTotalizacaoDebitoExistente(Entidade seguradora, String mesAno)throws Exception;
	double obterTotalizacaoCreditoAnoAnterior(Entidade seguradora, String mesAno)throws Exception;

	String obterPrimeiroMes(Entidade seguradora) throws Exception;

	double obterTotalizacaoCreditoExistente(Entidade seguradora, String mesAno)	throws Exception;

	double obterTotalizacaoSaldoAnteriorExistente(Entidade seguradora,String mesAno) throws Exception;

	double obterTotalizacao(Entidade seguradora, boolean emGuarani,String mesAno) throws Exception;

	double obterTotalizacaoExistente(Collection<Entidade> aseguradoras, String mesAno) throws Exception;
	double obterTotalizacaoExistente(Entidade seguradora, String mesAno)throws Exception;
	BigDecimal obterTotalizacaoExistenteBIG(Entidade seguradora, String mesAno)throws Exception;
	BigDecimal obterTotalizacaoExistenteBIG(Collection<Entidade> aseguradoras, String mesAno)throws Exception;
	BigDecimal obterTotalizacaoExistenteAnualBIG(Collection<Entidade> aseguradoras, int ano)throws Exception;
	BigDecimal obterTotalizacaoExistenteAnualBIG(Entidade aseguradora, int ano)throws Exception;
	BigDecimal obterTotalizacaoExistenteAnualFiscalBIG(Collection<Entidade> aseguradoras, int ano)throws Exception;
	BigDecimal obterTotalizacaoExistenteAnualFiscalBIG(Entidade aseguradora, int ano)throws Exception;
	double obterTotalizacaoExistenteAnual(Entidade aseguradora, int ano)throws Exception;
	
	double obterTotalizacaoExistenteAnual(Collection<Entidade> aseguradoras, int ano)throws Exception;

	double obterTotalizacaoSaldoAnoAnterior(Entidade seguradora, String mesAno)
			throws Exception;

	double obterTotalizacaoExistenteUltimos12Meses(Entidade seguradora)
			throws Exception;

	double obterTotalizacaoExistenteOutraMoeda(Entidade seguradora,
			String mesAno) throws Exception;
	
	double obterTotalSaldoAtualMensal(Date data, String aseguradoras) throws Exception;
}