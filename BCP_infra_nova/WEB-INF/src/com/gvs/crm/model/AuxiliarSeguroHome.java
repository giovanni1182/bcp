package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface AuxiliarSeguroHome {
	Collection obterAuxiliares() throws Exception;

	Collection obterAuxiliaresPorDataResolucao(Date data) throws Exception;

	Collection obterAuxiliaresCorredoresDeSegurosPorDataResolucao(Date data)
			throws Exception;

	Collection obterAuxiliaresLiquidadoresPorDataResolucao(Date data)
			throws Exception;

	Collection obterAuxiliaresAuditoresPorDataResolucao(Date data)
			throws Exception;

	Collection obterAuxiliaresAgentesDeSegurosPorDataResolucao(Date data)
			throws Exception;
	
	Collection obterLiquidadoresSinistroVigente(Date data, boolean ci) throws Exception;
	Collection obterCorredoresSegurosVigente(Date data, boolean ci) throws Exception;
	Collection obterAgentesSegurosVigente(Date data, boolean ci) throws Exception;
	
	Collection<Apolice> obterApolicesMaioresAgentesPorPrima(AuxiliarSeguro agente,Date dataInicio,Date dataFim,String situacao, boolean auxiliar) throws Exception;
	Collection<String> obterApolicesMaioresAgentesPorPrimaStr(AuxiliarSeguro agente,Date dataInicio,Date dataFim, int maxRegistros, String situacao, double monto, boolean auxiliar) throws Exception;
	Collection<String> obterApolicesMaioresAgentesPorCapitalStr(AuxiliarSeguro agente,Date dataInicio,Date dataFim, int maxRegistros, String situacao, double monto, boolean auxiliar) throws Exception;
	Collection<Apolice> obterApolicesMaioresAgentesPorCapital(AuxiliarSeguro agente,Date dataInicio,Date dataFim,String situacao, boolean auxiliar) throws Exception;
	Collection<String> obterApolicesMaioresAgentesPorComissaoStr(AuxiliarSeguro agente,Date dataInicio,Date dataFim, int maxRegistros, String situacao, double monto, boolean auxiliar) throws Exception;
	Collection<Apolice> obterApolicesMaioresAgentesPorComissao(AuxiliarSeguro agente,Date dataInicio,Date dataFim,String situacao, boolean auxiliar) throws Exception;
	Collection<Aseguradora> obterAseguradorasApolicesAcumuladas(Entidade agente, Date dataInicio,Date dataFim, boolean auxiliar) throws Exception;
	Collection<String> obterApolicesComissaoAgentesPorComissao(AuxiliarSeguro agente,Date dataInicio,Date dataFim, String situacao, boolean auxiliar) throws Exception;
	Collection<String> obterApolicesComissaoAgentesPorCapital(AuxiliarSeguro agente,Date dataInicio,Date dataFim, String situacao, boolean auxiliar) throws Exception;
	Collection<String> obterApolicesComissaoAgentesPorPrima(AuxiliarSeguro agente,Date dataInicio,Date dataFim, String situacao, boolean auxiliar) throws Exception;
	Collection<Apolice> obterApolicesComissaoAgentes(AuxiliarSeguro agente,Date dataInicio,Date dataFim, String situacao, String plano, boolean auxiliar) throws Exception;
}