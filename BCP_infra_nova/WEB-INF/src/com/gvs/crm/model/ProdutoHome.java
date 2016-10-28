package com.gvs.crm.model;

import java.util.Collection;

public interface ProdutoHome {
	Collection obterMoedas() throws Exception;

	Produto obterProdutoPorCodigoExterno(String codigoExternoOuApelido)
			throws Exception;

	Collection obterProdutosPorClassificacaoProduto(
			ClassificacaoProduto classificacaoProduto) throws Exception;

	Collection obterProdutosPorFornecedor(Entidade entidade) throws Exception;

	Collection obterProdutosPorFornecedor(Entidade entidade, String pesquisa)
			throws Exception;

	Collection obterUnidades() throws Exception;

	//Collection obterProdutos() throws Exception;
}