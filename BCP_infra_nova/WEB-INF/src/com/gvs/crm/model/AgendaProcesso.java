package com.gvs.crm.model;

public interface AgendaProcesso extends Evento {
	void atribuirProduto(Produto produto) throws Exception;

	void atualizarProduto(Produto produto) throws Exception;

	Produto obterProduto() throws Exception;

	void atribuirStatus(String status) throws Exception;

	void atualizarStatus(String status) throws Exception;

	String obterStatus() throws Exception;

	//void incluir() throws Exception;
}