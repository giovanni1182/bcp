package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface ReaseguradoraHome {
	Collection obterReaseguradoras() throws Exception;

	Collection obterReaseguradorasPorDataResolucao(Date data) throws Exception;
	Collection obterReaseguradorasVigentes(Date data) throws Exception;
	
	Collection<Reaseguradora> obterReaseguradorasVigentes2(Date data) throws Exception;
	Collection<String> obterMaiores(Date dataInicio, Date dataFim, String situacao, String tipoValor, double dolar, double euro, double real, double pesoArg, double pesoUru, double yen, boolean porPais) throws Exception;
	Collection<String> obterMaioresPorSecao(Date dataInicio, Date dataFim, String situacao, String tipoValor, double dolar, double euro, double real, double pesoArg, double pesoUru, double yen, String secao, String modalidade) throws Exception;
}