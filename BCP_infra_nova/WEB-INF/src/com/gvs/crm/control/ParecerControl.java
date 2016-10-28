package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Parecer;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class ParecerControl extends Control {
	public void atualizarParecer(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Parecer parecer = (Parecer) eventoHome.obterEventoPorId(action
				.getLong("id"));
		mm.beginTransaction();
		try {

			parecer.atualizarTitulo(action.getString("titulo"));
			parecer.atualizarDescricao(action.getString("descricao"));

			this
					.setResponseView(new PaginaInicialView(usuarioAtual,
							origemMenu));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(parecer, origemMenu));
			mm.rollbackTransaction();
		}
	}

	public void incluirParecer(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		Parecer parecer = (Parecer) mm.getEntity("Parecer");
		mm.beginTransaction();
		try {
			Evento superior = eventoHome.obterEventoPorId(action
					.getLong("superiorId"));

			parecer.atribuirSuperior(superior);
			parecer.atribuirOrigem(superior.obterOrigem());
			parecer.atribuirDestino(superior.obterDestino());
			parecer.atribuirResponsavel(usuarioAtual);
			parecer.atribuirTitulo(action.getString("titulo"));
			parecer.atribuirDescricao(action.getString("descricao"));

			parecer.incluir();

			this.setResponseView(new EventoView(parecer));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(parecer));
			mm.rollbackTransaction();
		}
	}
}