package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface AseguradoraHome {
	 Collection<Aseguradora> obterAseguradoras() throws Exception;

	Collection obterAseguradorasRelatorio(String mesAno) throws Exception;

	Map obterReaseguradoras(Aseguradora aseguradora, Date dataInicio,Date dataFim) throws Exception;

	Map obterCorretores(Aseguradora aseguradora, Date dataInicio, Date dataFim)throws Exception;
	Collection<Aseguradora> obterAseguradorasPorMenor80()throws Exception;

	Collection<Aseguradora> obterAseguradorasPorDataResolucao(Date data) throws Exception;

	Collection<Aseguradora> obterAseguradorasPorMaior80DataResolucao(Date data)	throws Exception;
	Collection<Aseguradora> obterAseguradorasPorMenor80OrdenadoPorNome()throws Exception;
	Collection<Aseguradora> obterAseguradorasPorMaior80OrdenadasPorNome()throws Exception;
	Collection<Aseguradora> obterAseguradorasPorMaior80()throws Exception;
	
	Collection<SinistroFiniquitadoCentralRisco> obterSinistrosFiniquitadosCentralRisco(Entidade aseguradora, String nomeAsegurado, String documento) throws Exception;
	Collection<SinistroFiniquitadoCentralRisco> obterSinistrosVigentesCentralRisco(Entidade aseguradora, String nomeAsegurado, String documento)  throws Exception;
	Collection<MorosidadeCentralRisco> obterMorosidadesCentralRisco(Entidade aseguradora, String nomeAsegurado, String documento)  throws Exception;
	Collection obterContas() throws Exception;
	
	Collection<Apolice> obterApolicesLavagemDinheiroPorPrima(Aseguradora aseguradora,String tipoPessoa,Date dataInicio,Date dataFim,int maxRegistros, String situacao, String secao, double monto, String modalidade,String tipoInstrumento) throws Exception;
	Collection<Apolice> obterApolicesLavagemDinheiroPorSinistro(Aseguradora aseguradora,String tipoPessoa,Date dataInicio,Date dataFim,int maxRegistros, String situacao, String secao, double monto, String modalidade,String tipoInstrumento) throws Exception;
	Collection<Apolice> obterApolicesLavagemDinheiroPorCapital(Aseguradora aseguradora,String tipoPessoa,Date dataInicio,Date dataFim,int maxRegistros, String situacao, String secao, double monto, String modalidade,String tipoInstrumento) throws Exception;
	void criarTabelaAseguradora() throws Exception;
	
	//Collection<String> obterQtdeSinistrosPorPeriodoTODAS(Date dataInicio, Date dataFim) throws Exception;
	Collection<String> obterQtdeSinistrosPorPeriodoTODAS(Date dataInicio, Date dataFim, boolean admin) throws Exception;
	//Collection<String> obterQtdeApolicesPorPeriodoTODAS(Date dataInicio, Date dataFim, boolean valores) throws Exception;
	Collection<String> obterQtdeApolicesPorPeriodoTODAS(Date dataInicio, Date dataFim, boolean valores, boolean admin) throws Exception;
	//String obterValoresSinistrosPorPeriodoRelSecaoAnualTODAS(Date dataInicio, Date dataFim, String secao) throws Exception;
	
	Map<String,String> obterNomePlanoPeriodoTODAS(Date dataInicio, Date dataFim, String ramo, String secao) throws Exception;
	Map<String,String> obterNomePlanosSinistrosPorPeriodoTODAS(Date dataInicio, Date dataFim, String ramo, String secao) throws Exception;
	
	String[] obterQtdeApolicesPorPeriodoTODASNOVO(Date dataInicio, Date dataFim, boolean valores, String secao, String modalidade, String ramo) throws Exception;
	String[] obterQtdeSinistrosPorPeriodoTODASNOVO(Date dataInicio, Date dataFim, boolean valores, String secao, String modalidade, String ramo) throws Exception;
	
	String[] obterQtdeApolicesPorModalidade(Date dataInicio, Date dataFim, String secao, String modalidade) throws Exception;
	
	Collection<Entidade> obterAgentesPorPeridodo(Aseguradora Aseguradora, Date dataInicio, Date dataFim)throws Exception;
	Collection<Entidade> obterCorredoresPorPeridodo(Aseguradora Aseguradora, Date dataInicio, Date dataFim)throws Exception;
	Collection<Entidade> obterGrupoAlertaTrempana(Aseguradora aseguradora) throws Exception;
	Collection<Entidade> obterAseguradorasComGrupoAlertaTrempana() throws Exception;
}