package com.gvs.crm.model;

public interface Localidade extends Entidade
{
	void incluir() throws Exception;
	void atribuirCodigo(int codigo);
	void atribuirCidade(String cidade);
	void atribuirEstado(String estado);
	
	int obterCodigo() throws Exception;
	String obterCidade() throws Exception;
	String obterEstador() throws Exception;
	
	void atualizarCodigo(int codigo) throws Exception;
	void atualizarCidade(String cidade) throws Exception;
	void atualizarEstado(String estado) throws Exception;
}
