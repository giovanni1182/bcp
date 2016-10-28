package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.ManutencaoSite;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class ManutencaoSiteControl extends Control {
	public void atualizarManutencaoSite(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		ManutencaoSite manutencao = (ManutencaoSite) eventoHome
				.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {

			if (action.getLong("origemId") == 0)
				throw new Exception("Elegiré el Solicitant");

			Entidade origem = entidadeHome.obterEntidadePorId(action
					.getLong("origemId"));

			manutencao.atualizarOrigem(origem);
			manutencao.atualizarTipo(action.getString("tipo"));
			manutencao.atualizarTitulo(action.getString("titulo"));
			manutencao.atualizarDescricao(action.getString("descricao"));

			this.setResponseView(new EventoView(manutencao));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(manutencao));
			mm.rollbackTransaction();
		}
	}

	public void incluirManutencaoSite(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		ManutencaoSite manutencao = (ManutencaoSite) mm
				.getEntity("ManutencaoSite");

		mm.beginTransaction();
		try {

			if (action.getLong("origemId") == 0)
				throw new Exception("Elegiré el Solicitant");

			Entidade origem = entidadeHome.obterEntidadePorId(action
					.getLong("origemId"));

			manutencao.atribuirOrigem(origem);
			manutencao.atribuirTipo(action.getString("tipo"));
			manutencao.atribuirTitulo(action.getString("titulo"));
			manutencao.atribuirDescricao(action.getString("descricao"));

			manutencao.incluir();

			this.setResponseView(new EventoView(manutencao));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(manutencao));
			mm.rollbackTransaction();
		}
	}

	public void enviarManutencaoSite(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());

		ManutencaoSite manutencao = (ManutencaoSite) eventoHome
				.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {

			Entidade responsavel = entidadeHome
					.obterEntidadePorApelido("informatica");

			manutencao.atualizarResponsavel(responsavel);

			this.setResponseView(new PaginaInicialView(usuario, usuario));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(manutencao));
			mm.rollbackTransaction();
		}
	}
}