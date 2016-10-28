package com.gvs.crm.model;


public interface CodificacaoPlano extends Codificacoes 
{
	CodificacaoCobertura obterCobertura(String codigo) throws Exception;
}
