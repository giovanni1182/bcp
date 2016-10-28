package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface Pessoa extends Entidade {
	public interface Formacao {
		void atualizarFormacao(String instituicao, String curso,
				String cargaHoraria, String tipo, String tipoEducacao,
				Date dataInicio, Date dataFim, String experiencia)
				throws Exception;

		Pessoa obterPessoa() throws Exception;

		int obterId() throws Exception;

		String obterInstituicao() throws Exception;

		String obterCurso() throws Exception;

		String obterTipo() throws Exception;

		Date obterDataInicio() throws Exception;

		Date obterDataFim() throws Exception;

		String obterExperiencia() throws Exception;

		String obterTipoEducacao() throws Exception;

		String obterCargaHoraria() throws Exception;
	}

	void adicionarFormacao(String instituicao, String curso,
			String cargaHoraria, String tipo, String tipoEducacao,
			Date dataInicio, Date dataFim, String experiencia) throws Exception;

	Formacao obterFormacao(int id) throws Exception;

	Collection obterFormacoes() throws Exception;

	void removerFormacao(Formacao formacao) throws Exception;
}