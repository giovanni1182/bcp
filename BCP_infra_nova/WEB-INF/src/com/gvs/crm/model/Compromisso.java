package com.gvs.crm.model;

public interface Compromisso extends Evento {
	static String COMPROMISSO_CONFIRMADO = "confirmado";

	void adicionarParticipante(Usuario usuario) throws Exception;

	void confirmar() throws Exception;

	void notificarParticipantes() throws Exception;
}