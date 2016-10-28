package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface Reaseguradora extends Entidade {
	public interface Classificacao {
		void atualizar(String classificacao, String nivel, String codigo,
				String qualificacao, Date data) throws Exception;

		Reaseguradora obterReaseguradora() throws Exception;

		int obterId() throws Exception;

		String obterClassificacao() throws Exception;

		String obterNivel() throws Exception;

		String obterCodigo() throws Exception;

		String obterQualificacao() throws Exception;

		Date obterData() throws Exception;
	}

	void adicionarClassificacao(String classificacao, String nivel,
			String codigo, String qualificacao, Date data) throws Exception;

	void adicionarClassificacaoNivel(String nivel) throws Exception;

	Classificacao obterClassificacao(int id) throws Exception;

	Collection obterClassificacoes() throws Exception;

	Collection obterNiveis() throws Exception;

	void removerClassificacao(Reaseguradora.Classificacao classificacao)
			throws Exception;
	
	boolean permiteExcluir() throws Exception;
	
	boolean permiteAtualizar() throws Exception;
}