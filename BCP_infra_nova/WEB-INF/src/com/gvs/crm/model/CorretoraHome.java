package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface CorretoraHome 
{
	Collection obterCorretoras() throws Exception;
	Collection<String> obterCorretorasVigentes(Date data,boolean ci) throws Exception;
	Collection<String> obterMaiores(int qtde,Date dataInicio, Date dataFim, String situacao, String tipoValor, double dolar, double euro, double real, double pesoArg, double pesoUru, double yen) throws Exception;
}
