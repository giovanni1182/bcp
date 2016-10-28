package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface DadosReaseguroHome {
	DadosReaseguro obterDadosReaseguro(ClassificacaoContas cContas,
			Apolice apolice, Reaseguradora reaseguradora, String tipoContrato)
			throws Exception;
	
	Collection<String> obterDadosReaseguro(Aseguradora aseguradora, Date dataInicio, Date dataFim, String situacao, String tipoValor) throws Exception;
	Collection<Apolice> obterApolicesDadosReaseguro(Aseguradora aseguradora, Date dataInicio, Date dataFim, String situacao, String tipoContrato) throws Exception;
	
	
//Collection obterReaseguros(Aseguradora aseguradora, Date dataInicio, Date
	// dataFim) throws Exception;
}