package com.gvs.crm.model;

public interface Informe extends Evento {
	void atualizarDescricao(String descricao) throws Exception;

	void incluir() throws Exception;

	String obterDescricao() throws Exception;
}