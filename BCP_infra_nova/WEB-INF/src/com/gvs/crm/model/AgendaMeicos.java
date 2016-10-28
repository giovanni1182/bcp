package com.gvs.crm.model;

import java.util.Collection;

public interface AgendaMeicos extends Evento {
	Collection obterEventoMeicos() throws Exception;

	Collection obterCalculoMeicos(String tipo) throws Exception;

	void incluir() throws Exception;

	void atualizarIPC(double ipc) throws Exception;

	double obterIPC() throws Exception;

	void atualizarIPC3Anos(double ipc) throws Exception;

	double obterIPC3Anos() throws Exception;

	void excluirCalculoMeicos() throws Exception;
	
	boolean permiteAtualizar() throws Exception;
	
	double obterValorQualificacao(Aseguradora aseguradora) throws Exception;
}