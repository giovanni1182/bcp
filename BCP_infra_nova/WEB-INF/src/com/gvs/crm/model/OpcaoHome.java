package com.gvs.crm.model;

import java.util.Collection;

public interface OpcaoHome
{
	void incluirUsuario(int opcao, Usuario usuario) throws Exception;
	Collection<Usuario> obterUsuarios(int opcao) throws Exception;
	void excluirUsuario(int opcao, Usuario usuario) throws Exception;
	Collection<Integer> obterOpcoes(Usuario usuario) throws Exception;
	String obterNome(int opcao);
	Collection<String> obterOpcoesPorNome(Usuario usuario) throws Exception;
	Collection<Integer> obterOpcoes() throws Exception;
}
