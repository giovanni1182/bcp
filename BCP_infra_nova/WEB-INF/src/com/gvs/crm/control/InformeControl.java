package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Informe;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.report.InformeReport;
import com.gvs.crm.view.EventoView;

import infra.control.Action;
import infra.control.Control;

public class InformeControl extends Control {
	public void abrirInforme(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");

		Informe informe = (Informe) eventoHome.obterEventoPorId(action
				.getLong("id"));

		DocumentoProduto documento = (DocumentoProduto) informe.obterSuperior();

		mm.beginTransaction();
		try {
			if (documento.obterAnalista() == null)
				throw new Exception("Escolha o Analista");
			if (documento.obterChefeDivisao() == null)
				throw new Exception("Escolha o Chefe de Divisão");
			if (documento.obterIntendente() == null)
				throw new Exception("Escolha o Intendente");

			this.setResponseReport(new InformeReport(informe, action
					.getLocale()));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(documento));
			mm.rollbackTransaction();
		}
	}

	public void atualizarInforme(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");

		Informe informe = (Informe) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {

			informe.atualizarDescricao(action.getString("descricao"));

			this.setResponseView(new EventoView(informe));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(informe));
			mm.rollbackTransaction();
		}
	}
}