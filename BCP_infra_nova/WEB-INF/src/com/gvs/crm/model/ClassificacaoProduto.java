package com.gvs.crm.model;

import java.util.Collection;

public interface ClassificacaoProduto extends Entidade {
	void atribuirDescricao(String descricao) throws Exception;

	String obterDescricao() throws Exception;

	Collection obterProdutos() throws Exception;

	Collection obterTabelasPrecos() throws Exception;
}