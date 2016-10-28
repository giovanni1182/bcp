package com.gvs.crm.model;

public interface Reclamacao extends Evento {
	void atualizarApolice(Apolice apolice) throws Exception;

	void incluir() throws Exception;

	Apolice obterApolice() throws Exception;
}