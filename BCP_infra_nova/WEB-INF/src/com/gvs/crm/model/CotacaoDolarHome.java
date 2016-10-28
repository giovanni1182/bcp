package com.gvs.crm.model;

import java.util.Collection;

public interface CotacaoDolarHome 
{
	Collection<CotacaoDolar> obter10Ultimas() throws Exception;
	Collection<CotacaoDolar> obterCotacoes(int mes, int ano) throws Exception;
	CotacaoDolar obterCotacao(int mes, int ano) throws Exception;
	boolean temCotacao(int mes, int ano, CotacaoDolar cotacao) throws Exception;
}
