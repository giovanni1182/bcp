package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface ApoliceHome {
	Apolice obterApolice(String numero) throws Exception;

	Collection localizarApolices(String numeroInstrumento,
			String secao, Aseguradora aseguradora,
			String nomeAsegurado, String codigoPlano, int pagina,String situacao, Date dataInicio, Date dataFim, String rucCi, String tomador, String tipoInstrumento,String nomeAsegurado1, String nomeAsegurado2) throws Exception;
	
	Collection localizarApolices(String numeroInstrumento,
			ClassificacaoContas cContas, Aseguradora aseguradora,
			String nomeAsegurado, String codigoPlano) throws Exception;

	Collection obterApolicesSuspeitas1() throws Exception;

	Collection obterApolicesSuspeitas2() throws Exception;

	Collection obterApolicesSuspeitas3() throws Exception;

	Apolice obterApolicePorNumeroIdentificacao(String numero) throws Exception;

	Collection obterSecaoApolice(Aseguradora aseguradora) throws Exception;

	Collection obterPlanosApolice(Aseguradora aseguradora) throws Exception;

	Collection obterTiposApolice(Aseguradora aseguradora) throws Exception;
	Collection obterApolicesDuplicadas()throws Exception;
	void excluirApolicesDuplicadas() throws Exception;
	Collection obterApolicesDuplicadasTeste()throws Exception;

	Collection obterSituacoesApolice(Aseguradora aseguradora) throws Exception;

	Collection obterApolicesPorReaseguradora(Aseguradora aseguradora,
			Entidade reaseguradora, Date dataInicio, Date dataFim,
			String tipoContrato) throws Exception;

	Collection obterApolicesPorCorretora(Aseguradora aseguradora,
			Entidade corretora, Date dataInicio, Date dataFim,
			String tipoContrato) throws Exception;
	
	Collection obterApolicesPorCorretoraAseguradora(Aseguradora aseguradora,
			Entidade corretora, Date dataInicio, Date dataFim,
			String tipoContrato) throws Exception;
	
	Collection obterNomesAseguradoEmBranco() throws Exception;
	void atualizarSituacaoApoliceAnterior() throws Exception;
	void atualizarNoVigenteApolicesVenciadas() throws Exception;
	
	Collection obterApolicesSuspeitas(String rucCi) throws Exception;
	Apolice obterApolice(Entidade aseguradora, String numero) throws Exception;
	Collection obterConsolidado(String opcao, String situacaoSeguro, Date dataInicio, Date dataFim) throws Exception;
	Collection obterConsolidado(Aseguradora aseguradora, String situacaoSeguro, String opcao, Date dataInicio, Date dataFim, boolean admin) throws Exception;
	Collection<String> obterSecoes(boolean admin) throws Exception;
	//Map obterQtdeApolicesAnualPorSecao(Date dataInicio, Date dataFim, String situacaoSeguro) throws Exception;
	Map obterQtdeApolicesAnualPorSecao(Date dataInicio, Date dataFim, String situacaoSeguro, boolean userAdmin) throws Exception;
	int obterQtdeApolicesAnualPorSecao(Date dataInicio, Date dataFim, String secao, String situacaoSeguro) throws Exception;
	int obterQtdeSinistrosAnualPorSecao(Date dataInicio, Date dataFim, String secao, String situacaoSeguro) throws Exception;
	Map obterQtdeSinistrosAnualPorSecao(Date dataInicio, Date dataFim, String situacaoSeguro, boolean admin) throws Exception;
	void manutAnulacao() throws Exception;
	Map obterQtdeApolicesPeriodo(Aseguradora aseg, Date dataInicio, Date dataFim, boolean admin) throws Exception;
	Map obterQtdeSinistrosPeriodo(Aseguradora aseg, Date dataInicio, Date dataFim, boolean admin) throws Exception;
	Collection<String> obterDemandaJudicial(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception;
	Collection<AspectosLegais> obterApolicesDemandaJudicial(Aseguradora aseguradora, ClassificacaoContas secao, Date dataInicio, Date dataFim) throws Exception;
	String obterQtdeMontanteDemandaJudicial(Aseguradora aseguradora, Date dataInicio) throws Exception;
	String obterDadosAquivoApolice(Aseguradora aseguradora, Date dataInicio) throws Exception;
	String obterDadosAquivoFinanciamento(Aseguradora aseguradora, Date dataInicio) throws Exception;
	String obterDadosAquivoReservas(Aseguradora aseguradora, Date dataInicio) throws Exception;
	String obterDadosAquivoReaseguros(Aseguradora aseguradora, Date dataInicio) throws Exception;
	String obterDadosAquivoCoaseguros(Aseguradora aseguradora, Date dataInicio) throws Exception;
	String obterDadosAquivoSinistros(Aseguradora aseguradora, Date dataInicio) throws Exception;
	String obterDadosAquivoPagos(Aseguradora aseguradora, Date dataInicio) throws Exception;
	String obterDadosAquivoAnulacao(Aseguradora aseguradora, Date dataInicio) throws Exception;
	String obterDadosAquivoCobranca(Aseguradora aseguradora, Date dataInicio) throws Exception;
	String obterDadosAquivoEndoso(Aseguradora aseguradora, Date dataInicio) throws Exception;
	String obterDadosAquivoFinalizacao(Aseguradora aseguradora, Date dataInicio) throws Exception;
	String obterDadosAquivoGastos(Aseguradora aseguradora, Date dataInicio) throws Exception;
	String obterDadosAquivoAnulacaoReaseguro(Aseguradora aseguradora, Date dataInicio) throws Exception;
	String obterDadosAquivoMorosidade(Aseguradora aseguradora, Date dataInicio) throws Exception;
	String obterDadosAquivoAsegurado(Aseguradora aseguradora, Date dataInicio) throws Exception;
	Collection<String> obterApolicesRG001(Aseguradora aseguradora, String situacaoSeguro, Date dataInicio, Date dataFim, boolean especial, boolean modificado) throws Exception;
	Collection<Apolice> obterApolicesRG001Aseg(Aseguradora aseguradora, String situacaoSeguro, Date dataInicio, Date dataFim, boolean especial, boolean modificado) throws Exception;
	Collection<String> obterApolicesReaseguro(Aseguradora aseguradora,Date dataInicio,Date dataFim,int qtde, String situacao, double valor, double valorMenor, double valorDolar, double valorMenorDolar) throws Exception;
	Collection<Apolice> obterApolicesReaseguro2(Aseguradora aseguradora,Date dataInicio,Date dataFim,int qtde, String situacao,double valor, double valorMenor, boolean comReaseguro, double valorDolar, double valorMenorDolar) throws Exception;
	Collection<Apolice> obterApolicesHistoricoAnual(Date dataInicio, Date dataFim, String situacaoSeguro, String secao) throws Exception;
	Collection<Apolice> obterSinistrosHistoricoAnual(Date dataInicio, Date dataFim, String situacaoSeguro, String secao) throws Exception;
}