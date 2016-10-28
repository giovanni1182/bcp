package com.gvs.crm.model;

public interface Participacao extends Mensagem {
	static final String EVENTO_ACEITO = "aceito";

	static final String EVENTO_RECUSADO = "recusado";

	void aceitar(String comentario) throws Exception;

	boolean permiteAceitar() throws Exception;

	boolean permiteRejeitar() throws Exception;

	void recusar(String comentario) throws Exception;
}