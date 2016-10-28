package com.gvs.crm.model;

import java.util.Collection;

public interface OcorrenciaHome {
	Collection obterOcorrenciasPorProduto(Produto produto) throws Exception;

	boolean possuiOcorrencias(Produto produto) throws Exception;
}