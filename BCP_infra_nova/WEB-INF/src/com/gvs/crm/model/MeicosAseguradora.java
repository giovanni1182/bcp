package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface MeicosAseguradora extends Evento {
	public interface Indicador {
		void atualizar(String marcado) throws Exception;

		MeicosAseguradora obterMeicosAseguradora() throws Exception;

		int obterSequencial() throws Exception;

		String obterDescricao() throws Exception;

		int obterPeso() throws Exception;

		boolean estaMarcado() throws Exception;

		boolean eExcludente() throws Exception;
	}

	public interface ControleDocumento {
		void atualizar(Date data) throws Exception;

		MeicosAseguradora obterMeicosAseguradora() throws Exception;

		int obterSequencial() throws Exception;

		String obterDescricao() throws Exception;

		Date obterDataEntrega() throws Exception;

		Date obterDataLimite() throws Exception;
	}

	Map obterIndicadores() throws Exception;

	Indicador obterIndicador(int seq) throws Exception;

	void adicionarIndicador(String descricao, int peso, boolean excludente)
			throws Exception;

	Map obterDocumentos() throws Exception;

	ControleDocumento obterDocumento(int seq) throws Exception;

	void adicionarDocumento(String descricao, Date dataLimite) throws Exception;

	Collection obterMeicosCalculos() throws Exception;
}