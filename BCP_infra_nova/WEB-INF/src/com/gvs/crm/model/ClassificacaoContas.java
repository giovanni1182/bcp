package com.gvs.crm.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

public interface ClassificacaoContas extends Entidade {
	public interface ClassificacaoConta2 {
		long obterContaId() throws Exception;

		long obterSuperiorId() throws Exception;

		String obterCodigoClassificacaoConta() throws Exception;

		String obterNomeClassificacaoConta() throws Exception;

	}

	Collection obterClassificacaoContas() throws Exception;

	void atualizarDescricao(String descricao) throws Exception;

	void atualizarCodigo(String codigo) throws Exception;

	void atualizarConcepto(String descricao) throws Exception;

	void atualizarNivel(String nivel) throws Exception;

	void atualizarNoma(String descricao) throws Exception;

	void atualizarNatureza(String descricao) throws Exception;

	void atualizarDinamica(String dinamica) throws Exception;
	
	String obterDescricao() throws Exception;

	String obterCodigo() throws Exception;

	String obterConcepto() throws Exception;

	Collection obterInferioresNivel1() throws Exception;

	Collection obterInferioresNivel2() throws Exception;

	Collection obterInferioresNivel3() throws Exception;

	Collection obterInferioresNivel4() throws Exception;

	Collection obterInferioresNivel5(Entidade seguradora, String mesAno)
			throws Exception;

	boolean existeValor(String mesAno) throws Exception;

	boolean existeOutroValor(String mesAno) throws Exception;
	boolean permiteAtualizar() throws Exception;
	boolean permiteExcluir() throws Exception;
	//int obterQtdeSinistrosPorSecaoVigente(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception;
	int obterQtdeSinistrosPorSecaoNoVigente(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception;
	double obterValorSinistrosRecuperadosPorSecaoVigente(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception;
	double obterValorSinistrosRecuperadosPorSecaoNoVigente(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception;
	//double obterValorSinistrosPagosPorSecaoVigente(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception;
	double obterValorSinistrosPagosPorSecaoNoVigente(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception;
	double obterValorSinistrosRecuperadosPorSecaoNoVigenteJudicializado(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception;
	double obterValorSinistrosRecuperadosPorSecaoVigenteJudicializado(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception;
	
	//void instanciarAseguradorasMenor80() throws Exception; 
	double obterTotalSaldoAtualMensal(Date data, String aseguradoras) throws Exception;
	

	double obterTotalizacaoExistente(Entidade seguradora, String mesAno) throws Exception;
	BigDecimal obterTotalizacaoExistenteBIG(Entidade seguradora, String mesAno)throws Exception;
	BigDecimal obterTotalizacaoExistenteBIG(Collection<Entidade> aseguradoras, String mesAno)throws Exception;
	double obterTotalizacaoExistente(Collection<Entidade> aseguradoras, String mesAno)throws Exception;
	double obterTotalizacaoExistenteAnual(Collection<Entidade> aseguradoras, int ano)throws Exception;

	double obterTotalizacaoDebitoExistente(Entidade seguradora, String mesAno)
			throws Exception;

	//double obterTotalizacaoCreditoAnoAnterior(Entidade seguradora, String
	// mesAno) throws Exception;

	double obterTotalizacaoCreditoExistente(Entidade seguradora, String mesAno)
			throws Exception;
	
	//double obterTotalizacaoSaldoExistente(Entidade seguradora,String mesAno) throws Exception;

	double obterTotalizacaoSaldoAnoAnterior(Entidade seguradora, String mesAno)
			throws Exception;

	String obterPrimeiroMes(Entidade seguradora) throws Exception;

	double obterTotalizacaoExistenteUltimos12Meses(Entidade seguradora)
			throws Exception;
	
	BigDecimal obterTotalizacaoExistenteAnualBIG(Entidade aseguradora, int ano)throws Exception;
	BigDecimal obterTotalizacaoExistenteAnualBIG(Collection<Entidade> aseguradoras, int ano)throws Exception;
	BigDecimal obterTotalizacaoExistenteAnualFiscalBIG(Collection<Entidade> aseguradoras, int ano)throws Exception;
	BigDecimal obterTotalizacaoExistenteAnualFiscalBIG(Entidade aseguradora, int ano)throws Exception;
	double obterTotalizacaoExistenteAnual(Entidade aseguradora, int ano)throws Exception;

	double obterTotalizacaoExistenteOutraMoeda(Entidade seguradora,
			String mesAno) throws Exception;

	double obterTotalizacao(Entidade seguradora, boolean emGuarani,
			String mesAno) throws Exception;

	double obterTotalizacaoSaldoAnterior(Entidade entidade, String mesAno)
			throws Exception;

	double obterTotalizacaoSaldoAnteriorExistente(Entidade seguradora,
			String mesAno) throws Exception;

	double obterTotalizacaoSaldoAnteriorMoedaEstrangeiraExistente(
			Entidade seguradora, String mesAno) throws Exception;

	double obterTotalizacaoSaldoAnteriorMoedaEstrangeira(Entidade seguradora,
			String mesAno) throws Exception;

	double obterTotalizacaoDebito(Entidade entidade, String mesAno)
			throws Exception;

	double obterTotalizacaoCredito(Entidade entidade, String mesAno)
			throws Exception;

	double obterTotalizacaoClassificaoContas(Entidade entidade,
			boolean emGuarani, String mesAno) throws Exception;

	String obterNivel() throws Exception;

	String obterNoma() throws Exception;

	String obterNatureza() throws Exception;

	String obterDinamica() throws Exception;

	void incluir() throws Exception;

	void incluirRelatorio(String mesAno, double valor, double debito,
			double credito, double saldoAnterior, double saldoMoedaEstrangeira,
			Entidade seguradora) throws Exception;

	void incluirRelatorioOutroValor(String mesAno, double valor, double debito,
			double credito, double saldoAnterior, Entidade seguradora)
			throws Exception;

	void atribuirCodigo(String codigo) throws Exception;

	void atribuirDescricao(String descricao) throws Exception;

	void atribuirConcepto(String descricao) throws Exception;

	void atribuirNivel(String nivel) throws Exception;

	void atribuirNoma(String descricao) throws Exception;

	void atribuirNatureza(String descricao) throws Exception;

	void atribuirDinamica(String dinamica) throws Exception;
}