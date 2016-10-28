package com.gvs.crm.model;

public interface Codificacoes extends Evento 
{
	void incluir() throws Exception;
	void atualizarCodigo(String codigo) throws Exception;
	String obterCodigo() throws Exception;
}
