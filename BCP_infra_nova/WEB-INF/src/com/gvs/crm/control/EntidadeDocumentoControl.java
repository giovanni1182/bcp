package com.gvs.crm.control;

import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeDocumento;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EntidadeView;

import infra.control.Action;
import infra.control.Control;

public class EntidadeDocumentoControl extends Control {
	public void atualizarEntidadeDocumento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = (Entidade) entidadeHome.obterEntidadePorId(action
				.getLong("entidadeSuperiorId"));
		Entidade responsavelTecnico = (Entidade) entidadeHome
				.obterEntidadePorId(action.getLong("responsavelTecnicoId"));

		EntidadeDocumento documento = (EntidadeDocumento) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

		documento.atribuirApelido(action.getString("apelido"));
		documento.atribuirNome(action.getString("nome"));
		documento.atribuirSuperior(superior);

		mm.beginTransaction();
		try {
			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo")) {
					String nome = key.substring(9, key.length());
					Entidade.Atributo entidadeAtributo = documento
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			documento.atualizar();

			this.setAlert("Documiento actualizado");
			this.setResponseView(new EntidadeView(documento));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(documento));
			mm.rollbackTransaction();
		}
	}

	public void incluirEntidadeDocumento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		EntidadeDocumento documento = (EntidadeDocumento) mm
				.getEntity("EntidadeDocumento");

		documento.atribuirApelido(action.getString("apelido"));
		documento.atribuirNome(action.getString("nome"));
		documento.atribuirSuperior(superior);
		documento.atribuirResponsavel(responsavel);
		Date data = new Date();

		mm.beginTransaction();
		try {
			documento.incluir();

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo")) {
					String nome = key.substring(9, key.length());
					Entidade.Atributo entidadeAtributo = documento
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}
			this.setAlert("Documiento incluído");
			this.setResponseView(new EntidadeView(documento));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(documento));
			mm.rollbackTransaction();
		}
	}
}