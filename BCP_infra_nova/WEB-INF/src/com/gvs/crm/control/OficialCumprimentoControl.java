package com.gvs.crm.control;

import java.util.Iterator;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.OficialCumprimento;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EntidadeView;

import infra.control.Action;
import infra.control.Control;

public class OficialCumprimentoControl extends Control {
	public void incluirOficialCumprimento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		OficialCumprimento oficial = (OficialCumprimento) mm
				.getEntity("OficialCumprimento");

		mm.beginTransaction();
		try {
			if (action.getLong("entidadeSuperiorId") > 0) {
				superior = entidadeHome.obterEntidadePorId(action
						.getLong("entidadeSuperiorId"));
				oficial.atribuirSuperior(superior);
			}

			Usuario responsavelView = null;

			if (action.getLong("responsavelId") > 0) {
				responsavelView = (Usuario) entidadeHome
						.obterEntidadePorId(action.getLong("responsavelId"));
				oficial.atribuirResponsavel(responsavelView);
			}

			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré el Nombre");

			if (oficial.verificarRuc(action.getString("ruc")))
				throw new Exception("El ruc " + action.getString("ruc")
						+ " ja esta sendo utilizado");

			oficial.atribuirNome(action.getString("nome"));

			if (responsavelView != null)
				oficial.atribuirResponsavel(responsavelView);
			else
				oficial.atribuirResponsavel(responsavel);

			oficial.atribuirSigla(action.getString("sigla"));
			oficial.incluir();

			oficial.atualizarRuc(action.getString("ruc"));

			if (action.getLong("aseguradoraId") > 0) {
				Aseguradora aseguradora = (Aseguradora) entidadeHome
						.obterEntidadePorId(action.getLong("aseguradoraId"));

				oficial.atualizarAseguradora(aseguradora);
			}

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo_")) {
					String nome = key.substring(9, key.length());

					Entidade.Atributo entidadeAtributo = oficial
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			this.setResponseView(new EntidadeView(oficial, oficial));
			mm.commitTransaction();

			this.setAlert("Oficial de Cumplimiento Incluido");

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(oficial));
			mm.rollbackTransaction();
		}
	}

	public void atualizarOficialCumprimento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		OficialCumprimento oficial = (OficialCumprimento) entidadeHome
				.obterEntidadePorId(action.getLong("id"));
		;

		mm.beginTransaction();
		try {
			if (action.getLong("entidadeSuperiorId") > 0) {
				superior = entidadeHome.obterEntidadePorId(action
						.getLong("entidadeSuperiorId"));
				oficial.atribuirSuperior(superior);
			}

			Usuario responsavelView = null;

			if (action.getLong("responsavelId") > 0) {
				responsavelView = (Usuario) entidadeHome
						.obterEntidadePorId(action.getLong("responsavelId"));
				oficial.atribuirResponsavel(responsavelView);
			}

			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré el Nombre");

			if (oficial.verificarRuc(action.getString("ruc")))
				throw new Exception("El ruc " + action.getString("ruc")
						+ " ja esta sendo utilizado");

			oficial.atribuirNome(action.getString("nome"));

			if (responsavelView != null)
				oficial.atribuirResponsavel(responsavelView);

			oficial.atribuirSigla(action.getString("sigla"));
			oficial.atualizar();

			oficial.atualizarRuc(action.getString("ruc"));

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo_")) {
					String nome = key.substring(9, key.length());

					Entidade.Atributo entidadeAtributo = oficial
							.obterAtributo(nome);

					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			if (action.getLong("aseguradoraId") > 0) {
				Aseguradora aseguradora = (Aseguradora) entidadeHome
						.obterEntidadePorId(action.getLong("aseguradoraId"));

				oficial.atualizarAseguradora(aseguradora);
			}

			this.setResponseView(new EntidadeView(oficial, oficial));
			mm.commitTransaction();

			this.setAlert("Oficial de Cumplimiento Actualizado");

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(oficial, responsavel));
			mm.rollbackTransaction();
		}
	}

}