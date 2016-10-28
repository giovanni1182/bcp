package com.gvs.crm.control;

import java.util.Iterator;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Pessoa;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EntidadeView;
import com.gvs.crm.view.PessoaFormacaoView;

import infra.control.Action;
import infra.control.Control;

public class PessoaControl extends Control {
	public void incluirPessoa(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		Pessoa pessoa = (Pessoa) mm.getEntity("Pessoa");

		mm.beginTransaction();
		try {
			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré el Nombre");

			if (action.getLong("superiorId") > 0) {
				superior = entidadeHome.obterEntidadePorId(action
						.getLong("superiorId"));
				pessoa.atribuirSuperior(superior);
			}

			pessoa.atribuirNome(action.getString("nome"));

			Usuario responsavelView = null;

			if (action.getLong("responsavelId") > 0)
				responsavelView = (Usuario) entidadeHome
						.obterEntidadePorId(action.getLong("responsavelId"));

			if (responsavelView != null)
				pessoa.atribuirResponsavel(responsavelView);
			else
				pessoa.atribuirResponsavel(responsavel);

			pessoa.incluir();

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo_")) {
					String nome = key.substring(9, key.length());

					Entidade.Atributo entidadeAtributo = pessoa
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			this.setResponseView(new EntidadeView(pessoa, pessoa));
			mm.commitTransaction();

			this.setAlert("Persona Incluida");

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(pessoa));
			mm.rollbackTransaction();
		}
	}

	public void atualizarPessoa(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		Pessoa oficial = (Pessoa) entidadeHome.obterEntidadePorId(action
				.getLong("id"));
		;

		mm.beginTransaction();
		try {
			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré el Nombre");

			if (action.getLong("superiorId") > 0) {
				superior = entidadeHome.obterEntidadePorId(action
						.getLong("superiorId"));
				oficial.atribuirSuperior(superior);
			}

			oficial.atribuirNome(action.getString("nome"));

			Usuario responsavelView = null;

			if (action.getLong("responsavelId") > 0)
				responsavelView = (Usuario) entidadeHome
						.obterEntidadePorId(action.getLong("responsavelId"));

			if (responsavelView != null)
				oficial.atribuirResponsavel(responsavelView);

			oficial.atualizar();

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

			this.setAlert("Persona Actualizada");

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(oficial));
			mm.rollbackTransaction();
		}
	}

	public void novaFormacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Pessoa pessoa = (Pessoa) entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));

		this.setResponseView(new PessoaFormacaoView(pessoa));
	}

	public void visualizarFormacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Pessoa pessoa = (Pessoa) entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));
		Pessoa.Formacao formacao = pessoa.obterFormacao(action.getInt("id"));
		this.setResponseView(new PessoaFormacaoView(formacao));
	}

	public void incluirFormacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Pessoa pessoa = (Pessoa) entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));

		mm.beginTransaction();
		try {
			if (action.getString("curso").equals(""))
				throw new Exception("Elegiré elo Curso");

			if (action.getString("tipo").equals(""))
				throw new Exception("Elegiré el Tipo");

			if (action.getDate("inicio") == null)
				throw new Exception("Elegiré el Fecha Inicio");

			if (action.getDate("fim") == null)
				throw new Exception("Elegiré el Fecha Finalización");

			pessoa.adicionarFormacao(action.getString("instituicao"), action
					.getString("curso"), action.getString("carga"), action
					.getString("tipo"), action.getString("tipoEducacao"),
					action.getDate("inicio"), action.getDate("fim"), action
							.getString("experiencia"));

			mm.commitTransaction();

			this.setResponseView(new EntidadeView(pessoa));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PessoaFormacaoView(pessoa));
			mm.rollbackTransaction();
		}
	}

	public void excluirFormacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		Pessoa pessoa = (Pessoa) entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));

		Pessoa.Formacao formacao = pessoa.obterFormacao(action.getInt("id"));

		mm.beginTransaction();
		try {
			pessoa.removerFormacao(formacao);
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
		}
		this.setResponseView(new EntidadeView(pessoa));
	}

	public void atualizarFormacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		Pessoa pessoa = (Pessoa) entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));

		Pessoa.Formacao formacao = pessoa.obterFormacao(action.getInt("id"));

		mm.beginTransaction();
		try {
			if (action.getString("curso").equals(""))
				throw new Exception("Elegiré elo Curso");

			if (action.getString("tipo").equals(""))
				throw new Exception("Elegiré el Tipo");

			if (action.getDate("inicio") == null)
				throw new Exception("Elegiré el Fecha Inicio");

			if (action.getDate("fim") == null)
				throw new Exception("Elegiré el Fecha Finalización");

			formacao.atualizarFormacao(action.getString("instituicao"), action
					.getString("curso"), action.getString("carga"), action
					.getString("tipo"), action.getString("tipoEducacao"),
					action.getDate("inicio"), action.getDate("fim"), action
							.getString("experiencia"));

			this.setResponseView(new EntidadeView(pessoa));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PessoaFormacaoView(formacao));
			mm.rollbackTransaction();
		}
	}
}