package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Resolucao;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.report.ResolucaoReport;
import com.gvs.crm.view.EventoView;

import infra.control.Action;
import infra.control.Control;

public class ResolucaoControl extends Control {
	public void abrirResolucao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");

		Resolucao resolucao = (Resolucao) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {
			this.setResponseReport(new ResolucaoReport(resolucao, action
					.getLocale()));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(resolucao.obterSuperior()));
			mm.rollbackTransaction();
		}
	}

	public void atualizarResolucao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");

		Resolucao resolucao = (Resolucao) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {

			resolucao.atualizarDescricao(action.getString("descricao"));

			this.setResponseView(new EventoView(resolucao));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(resolucao));
			mm.rollbackTransaction();
		}
	}
}