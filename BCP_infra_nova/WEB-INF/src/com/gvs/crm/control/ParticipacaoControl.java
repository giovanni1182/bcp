package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Participacao;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.AceitarView;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.PaginaInicialView;
import com.gvs.crm.view.RecusarView;

import infra.control.Action;
import infra.control.Control;

public class ParticipacaoControl extends Control {
	public void aceitarParticipacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Participacao participacao = (Participacao) eventoHome
				.obterEventoPorId(action.getLong("id"));
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		if (action.getBoolean("view")) {
			this.setResponseView(new AceitarView(participacao));
		} else {
			mm.beginTransaction();
			try {
				participacao.aceitar(action.getString("comentario"));
				this.setResponseView(new PaginaInicialView(usuarioAtual,
						origemMenu));
				mm.commitTransaction();
			} catch (Exception exception) {
				this.setAlert(Util.translateException(exception));
				this.setResponseView(new EventoView(participacao));
				mm.rollbackTransaction();
			}
		}
	}

	public void recusarParticipacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Participacao participacao = (Participacao) eventoHome
				.obterEventoPorId(action.getLong("id"));
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		if (action.getBoolean("view")) {
			this.setResponseView(new RecusarView(participacao));
		} else {
			mm.beginTransaction();
			try {
				participacao.recusar(action.getString("comentario"));
				this.setResponseView(new PaginaInicialView(usuarioAtual,
						origemMenu));
				mm.commitTransaction();
			} catch (Exception exception) {
				this.setAlert(Util.translateException(exception));
				this.setResponseView(new EventoView(participacao));
				mm.rollbackTransaction();
			}
		}
	}
}