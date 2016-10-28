package com.gvs.crm.model;

import java.util.Collection;

public interface Corretora extends Entidade {
	void atualizarAseguradora(Aseguradora aseguradora) throws Exception;

	Aseguradora obterAseguradora() throws Exception;

	void atualizarRamo(String ramo) throws Exception;

	void adicionarNovoRamo(String ramo) throws Exception;

	String obterRamo() throws Exception;
	
	boolean permiteExcluir() throws Exception;

	void incluir() throws Exception;

	Collection obterNomeRamos() throws Exception;
	
	boolean permiteAtualizar() throws Exception;
}