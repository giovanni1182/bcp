package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.GrupoMensagem;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EntidadeView;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class GrupoMensagemControl extends Control {
	public void incluirGrupoMensagem(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		//Entidade origemMenu =
		// entidadeHome.obterEntidadePorId(action.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());
		GrupoMensagem grupo = (GrupoMensagem) mm.getEntity("GrupoMensagem");
		mm.beginTransaction();
		try {
			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré o Nombre");

			grupo.atribuirSuperior(superior);
			grupo.atribuirNome(action.getString("nome"));
			grupo.atribuirResponsavel(responsavel);

			grupo.incluir();
			this.setResponseView(new EntidadeView(grupo, grupo));
			this.setAlert("Grupo Incluido");
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(grupo, grupo));
			mm.rollbackTransaction();
		}
	}

	public void atualizarGrupoMensagem(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		GrupoMensagem grupo = (GrupoMensagem) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

		mm.beginTransaction();
		try {
			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré o Nombre");

			grupo.atribuirApelido(action.getString("nome"));
			grupo.atribuirNome(action.getString("nome"));
			grupo.atribuirResponsavel(responsavel);

			grupo.atualizar();
			this.setAlert("Grupo Actualizado");
			this.setResponseView(new EntidadeView(grupo, grupo));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(grupo, grupo));
			mm.rollbackTransaction();
		}
	}

	public void excluirMembro(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		//Entidade origemMenu =
		// entidadeHome.obterEntidadePorId(action.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());
		GrupoMensagem grupo = (GrupoMensagem) entidadeHome
				.obterEntidadePorId(action.getLong("id"));
		Entidade membro = (Entidade) entidadeHome.obterEntidadePorId(action
				.getLong("idMembro"));

		mm.beginTransaction();
		try {
			grupo.removerMembro(membro);
			this.setResponseView(new EntidadeView(grupo, grupo));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(grupo, grupo));
			mm.rollbackTransaction();
		}
	}

	public void adicionarMembro(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		GrupoMensagem grupo = (GrupoMensagem) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

		mm.beginTransaction();

		try {

			if (action.getLong("membroId") == 0
					|| action.getLong("membroId") < 0)
				throw new Exception("Escolha o Membro");
			Entidade membro = (Entidade) entidadeHome.obterEntidadePorId(action
					.getLong("membroId"));
			if (membro == null)
				throw new Exception("O Membro não existe");

			if (grupo.obterMembros().containsKey(new Long(membro.obterId())))
				throw new Exception("O Membro " + membro.obterNome()
						+ " já faz parte do grupo.");

			grupo.adicionarMembro(membro);

			this.setResponseView(new EntidadeView(grupo, grupo));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(grupo, grupo));
			mm.rollbackTransaction();
		}
	}

	public void excluirGrupoMensagem(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		//Entidade origemMenu =
		// entidadeHome.obterEntidadePorId(action.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());
		GrupoMensagem grupo = (GrupoMensagem) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

		mm.beginTransaction();
		try {
			grupo.excluir();
			this
					.setResponseView(new PaginaInicialView(responsavel,
							responsavel));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this
					.setResponseView(new PaginaInicialView(responsavel,
							responsavel));
			mm.rollbackTransaction();
		}
	}

}