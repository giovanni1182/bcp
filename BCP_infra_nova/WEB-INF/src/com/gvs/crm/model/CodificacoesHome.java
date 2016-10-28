package com.gvs.crm.model;

import java.util.Collection;

public interface CodificacoesHome 
{
	CodificacaoPlano obterPlano(String codigo) throws Exception;
	
	String obterMaiorCodigoPlano() throws Exception;
	String obterMaiorCodigoCobertura(CodificacaoPlano plano) throws Exception;
	String obterMaiorCodigoRisco(CodificacaoCobertura cobertura) throws Exception;
	String obterMaiorCodigoDetalhe(CodificacaoRisco risco) throws Exception;
	
	boolean verificaCodigoPlano(String codigo)throws Exception;
	boolean verificaCodigoCobertura(String codigo, CodificacaoPlano plano)throws Exception;
	boolean verificaCodigoRisco(String codigo, CodificacaoCobertura cobertura)throws Exception;
	boolean verificaCodigoDetalhe(String codigo, CodificacaoRisco risco)throws Exception;
	
	Collection obterCodificacaoPlanos() throws Exception;
	Collection obterCodificacaoCoberturas() throws Exception;
	Collection obterCodificacaoRiscos() throws Exception;
	Collection obterCodificacaoDetalhes() throws Exception;
}
