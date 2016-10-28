package com.gvs.crm.model;

import java.util.Date;

public interface Inspecao extends Evento
{
	void atribuirInspetor(Entidade inspetor);
	
	void atualizarInspetor(Entidade inspetor) throws Exception;

	void atualizarDiasCorridos(String dias) throws Exception;

	void atualizarDataInicioReal(Date data) throws Exception;

	void atualizarDataTerminoReal(Date data) throws Exception;

	void incluir() throws Exception;

	Entidade obterInspetor() throws Exception;

	String obterDiasCorridos() throws Exception;

	Date obterDataInicioReal() throws Exception;

	Date obterDataTerminoReal() throws Exception;
}