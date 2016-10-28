package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Ditame;
import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.report.DitameReport;
import com.gvs.crm.view.EventoView;

import infra.control.Action;
import infra.control.Control;

public class DitameControl extends Control {
	public void abrirDitame(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");

		Ditame ditame = (Ditame) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {
			DocumentoProduto documento = (DocumentoProduto) ditame
					.obterSuperior();

			if (documento.obterSuperIntendente() == null)
				throw new Exception("Escolha o Superintendente");

			this
					.setResponseReport(new DitameReport(ditame, action
							.getLocale()));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(ditame.obterSuperior()));
			mm.rollbackTransaction();
		}
	}

	public void atualizarDitame(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");

		Ditame ditame = (Ditame) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {

			ditame.atualizarDescricao(action.getString("descricao"));

			this.setResponseView(new EventoView(ditame));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(ditame));
			mm.rollbackTransaction();
		}
	}
}