package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface Documento extends Evento {
	void atualizarDataAgenda(Date dataAgenda) throws Exception;

	void atualizarDiferenciador(String diferenciador) throws Exception;

	void incluir() throws Exception;

	Date obterDataAgenda() throws Exception;

	String obterDiferenciador() throws Exception;

	void adicionarNovoRamo(String ramo) throws Exception;

	void atualizarRamo(String ramo) throws Exception;

	String obterRamo() throws Exception;

	Collection obterNomeRamos() throws Exception;

}