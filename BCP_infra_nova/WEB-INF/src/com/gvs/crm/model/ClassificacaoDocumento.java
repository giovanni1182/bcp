package com.gvs.crm.model;

public interface ClassificacaoDocumento extends Entidade {
	void atribuirDescricao(String descricao) throws Exception;

	String obterDescricao() throws Exception;
}