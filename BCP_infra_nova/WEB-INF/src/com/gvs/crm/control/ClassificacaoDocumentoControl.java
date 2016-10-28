package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoDocumento;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EntidadeView;

import infra.control.Action;
import infra.control.Control;

public class ClassificacaoDocumentoControl extends Control {
	public void atualizarClassificacaoDocumento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		ClassificacaoDocumento classificacaoDocumento = (ClassificacaoDocumento) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

		classificacaoDocumento.atribuirApelido(action.getString("apelido"));
		classificacaoDocumento.atribuirNome(action.getString("nome"));
		classificacaoDocumento.atribuirDescricao(action.getString("descricao"));
		mm.beginTransaction();
		try {
			classificacaoDocumento.atualizar();
			this.setAlert("Classificación de documiento Actualizada");
			this.setResponseView(new EntidadeView(classificacaoDocumento));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(classificacaoDocumento));
			mm.rollbackTransaction();
		}
	}

	public void incluirClassificacaoDocumento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		ClassificacaoDocumento classificacaoDocumento = (ClassificacaoDocumento) mm
				.getEntity("classificacaoDocumento");

		classificacaoDocumento.atribuirApelido(action.getString("apelido"));
		classificacaoDocumento.atribuirNome(action.getString("nome"));
		classificacaoDocumento.atribuirDescricao(action.getString("descricao"));
		classificacaoDocumento.atribuirSuperior(superior);
		classificacaoDocumento.atribuirResponsavel(responsavel);
		mm.beginTransaction();
		try {
			classificacaoDocumento.incluir();
			this.setAlert("Classificación de documiento incluída");
			this.setResponseView(new EntidadeView(classificacaoDocumento));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(classificacaoDocumento));
			mm.rollbackTransaction();
		}
	}
}