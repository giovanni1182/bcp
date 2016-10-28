package com.gvs.crm.model;

import java.util.Collection;

public interface VariacaoIPCHome
{
	Collection<VariacaoIPC> obter10Ultimas() throws Exception;
	Collection<VariacaoIPC> obterVariacoes(int mes, int ano) throws Exception;
	VariacaoIPC obterVariacao(int mes, int ano) throws Exception;
	boolean temVariacao(int mes, int ano, VariacaoIPC variacao) throws Exception;
}
