package com.gvs.crm.model;

public interface OficialCumprimento extends Entidade {
	void atualizarAseguradora(Aseguradora aseguradora) throws Exception;

	void incluir() throws Exception;

	Aseguradora obterAseguradora() throws Exception;
	boolean permiteAtualizar() throws Exception;
}