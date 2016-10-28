package com.gvs.crm.model.impl;

import java.util.Date;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Raiz;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

public class RaizImpl extends EntidadeImpl implements Raiz {
	public void atribuirNome(String nome) throws Exception {
	}

	public void atribuirResponsavel(Usuario usuario) throws Exception {
	}

	public void atribuirSuperior(Entidade entidade) throws Exception {
	}

	public Date obterAtualizacao() throws Exception {
		return new Date(0);
	}

	public Date obterCriacao() throws Exception {
		return new Date(0);
	}

	public String obterNome() {
		return "Raiz";
	}

	public Usuario obterResponsavel() throws Exception {
		return null;
	}

	public boolean permiteIncluirEntidadesInferiores() throws Exception {
		UsuarioHome home = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");
		Usuario usuarioAtual = home.obterUsuarioPorUser(this.getModelManager()
				.getUser());
		return usuarioAtual.obterId() == 1;
	}
}