package com.gvs.crm.model;

public interface Emissor extends Entidade
{
	void incluir() throws Exception;
	void atribuirCodigo(int codigo);
	void atribuirDescricao(String descricao);
	
	int obterCodigo() throws Exception;
	String obterDescricao() throws Exception;
	
	void atualizarCodigo(int codigo) throws Exception;
	void atualizarDescricao(String descricao) throws Exception;
}
