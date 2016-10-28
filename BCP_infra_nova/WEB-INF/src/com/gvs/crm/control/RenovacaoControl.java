package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Renovacao;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.report.RenovacaoReport;
import com.gvs.crm.view.EventoView;

import infra.control.Action;
import infra.control.Control;

public class RenovacaoControl extends Control {
	public void abrirRenovacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");

		Renovacao renovacao = (Renovacao) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {
			this.setResponseReport(new RenovacaoReport(renovacao, action
					.getLocale()));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(renovacao.obterSuperior()));
			mm.rollbackTransaction();
		}
	}

	public void atualizarRenovacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");

		Renovacao renovacao = (Renovacao) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {

			if (action.getBoolean("matriculaAnterior"))
				renovacao.atualizarMatriculaAnterior(1);
			else
				renovacao.atualizarMatriculaAnterior(0);

			if (action.getBoolean("certificadoAntecedentes"))
				renovacao.atualizarCertificadoAntecedentes(1);
			else
				renovacao.atualizarCertificadoAntecedentes(0);

			if (action.getBoolean("certificadoJudicial"))
				renovacao.atualizarCertificadoJudicial(1);
			else
				renovacao.atualizarCertificadoJudicial(0);

			if (action.getBoolean("certificadoTributario"))
				renovacao.atualizarCertificadoTributario(1);
			else
				renovacao.atualizarCertificadoTributario(0);

			if (action.getBoolean("declaracao"))
				renovacao.atualizarDeclaracao(1);
			else
				renovacao.atualizarDeclaracao(0);

			if (action.getBoolean("comprovanteMatricula"))
				renovacao.atualizarComprovanteMatricula(1);
			else
				renovacao.atualizarComprovanteMatricula(0);

			if (action.getBoolean("apoliceSeguro"))
				renovacao.atualizarApoliceSeguro(1);
			else
				renovacao.atualizarApoliceSeguro(0);

			if (action.getBoolean("livro"))
				renovacao.atualizarLivro(1);
			else
				renovacao.atualizarLivro(0);

			this.setResponseView(new EventoView(renovacao));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(renovacao));
			mm.rollbackTransaction();
		}
	}
}