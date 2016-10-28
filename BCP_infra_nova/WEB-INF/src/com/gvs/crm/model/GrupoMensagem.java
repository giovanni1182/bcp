package com.gvs.crm.model;

import java.util.Map;

public interface GrupoMensagem extends Entidade {
	public interface Membro {
		Entidade obterEntidade() throws Exception;

		Entidade obterMembro() throws Exception;

		String obterEmail() throws Exception;
	}

	public Map obterMembros() throws Exception;

	Entidade obterMembro(Entidade membro) throws Exception;

	void adicionarMembro(Entidade membro) throws Exception;

	void removerMembro(Entidade membro) throws Exception;

	void excluir() throws Exception;

}