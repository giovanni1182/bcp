package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Memorando;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.report.MemorandoReport;
import com.gvs.crm.view.EventoView;

import infra.control.Action;
import infra.control.Control;

public class MemorandoControl extends Control {
	public void abrirMemorando(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");

		Memorando memorando = (Memorando) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {
			this.setResponseReport(new MemorandoReport(memorando, action
					.getLocale()));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(memorando.obterSuperior()));
			mm.rollbackTransaction();
		}
	}

	public void atualizarMemorando(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");

		Memorando memorando = (Memorando) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {

			memorando.atualizarDescricao(action.getString("descricao"));

			this.setResponseView(new EventoView(memorando));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(memorando));
			mm.rollbackTransaction();
		}
	}
}