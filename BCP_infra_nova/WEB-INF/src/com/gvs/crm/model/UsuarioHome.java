package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

import infra.security.User;

public interface UsuarioHome {
	Usuario obterUsuarioPorChave(String chave) throws Exception;

	Usuario obterUsuarioPorUser(User user) throws Exception;

	Collection obterUsuarios() throws Exception;

	boolean possuiResponsabilidades(Usuario usuario) throws Exception;
	
	Collection<Usuario> obterUsuariosCadastrados() throws Exception;
	
	Collection<Log> obterLos(Usuario usuario, Date dataInicio, Date dataFim) throws Exception;
	Collection<Usuario> obterUsuariosPorLog(Date dataInicio, Date dataFim) throws Exception;
	Collection<Usuario> obterUsuariosDepartamento(Entidade superior) throws Exception;
	Collection<Usuario> obterUsuariosResEscaneada() throws Exception;
}