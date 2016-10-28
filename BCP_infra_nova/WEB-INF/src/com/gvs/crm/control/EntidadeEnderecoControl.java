package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.view.EntidadeEnderecoView;
import com.gvs.crm.view.EntidadeView;

import infra.control.Action;
import infra.control.Control;

public class EntidadeEnderecoControl extends Control {
	public void atualizarEntidadeEndereco(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));
		Entidade.Endereco endereco = entidade
				.obterEndereco(action.getInt("id"));
		mm.beginTransaction();
		try {
			String nome = action.getString("nomeSelecionado");
			if (nome.equals(""))
				nome = action.getString("nome");

			endereco.atribuirNome(nome);
			endereco.atribuirBairro(action.getString("bairro"));
			endereco.atribuirCep(action.getString("cep"));
			endereco.atribuirCidade(action.getString("cidade"));
			endereco.atribuirComplemento(action.getString("complemento"));
			endereco.atribuirEstado(action.getString("estado"));
			endereco.atribuirNumero(action.getString("numero"));
			endereco.atribuirPais(action.getString("pais"));
			endereco.atribuirRua(action.getString("rua"));
			endereco.atualizar();
			this.setResponseView(new EntidadeView(entidade));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeEnderecoView(endereco));
			mm.rollbackTransaction();
		}
	}

	public void excluirEntidadeEndereco(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));
		Entidade.Endereco endereco = entidade
				.obterEndereco(action.getInt("id"));
		mm.beginTransaction();
		try {
			entidade.removerEndereco(endereco);
			this.setResponseView(new EntidadeView(entidade));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeEnderecoView(endereco));
			mm.rollbackTransaction();
		}
	}

	public void incluirEntidadeEndereco(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());

		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));

		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Evento retorno = null;

		if (action.getLong("retornoId") > 0)
			retorno = eventoHome.obterEventoPorId(action.getLong("retornoId"));

		Entidade.Endereco endereco = entidade.novoEndereco();

		mm.beginTransaction();
		try {
			String nome = action.getString("nome");
			if (nome.equals(""))
				nome = action.getString("nomeSelecionado");

			endereco.atribuirNome(nome);
			endereco.atribuirBairro(action.getString("bairro"));
			endereco.atribuirCep(action.getString("cep"));
			endereco.atribuirCidade(action.getString("cidade"));
			endereco.atribuirComplemento(action.getString("complemento"));
			endereco.atribuirEstado(action.getString("estado"));
			endereco.atribuirNumero(action.getString("numero"));
			endereco.atribuirPais(action.getString("pais"));
			endereco.atribuirRua(action.getString("rua"));
			endereco.incluir();

			this.setResponseView(new EntidadeView(entidade));

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeEnderecoView(endereco));
			mm.rollbackTransaction();
			throw exception;
		}
	}

	public void novoEntidadeEndereco(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));
		Entidade.Endereco endereco = entidade.novoEndereco();
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));

		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Evento retorno = null;

		if (action.getLong("retornoId") > 0)
			retorno = eventoHome.obterEventoPorId(action.getLong("retornoId"));

		if (retorno != null)
			this.setResponseView(new EntidadeEnderecoView(endereco, origemMenu,
					retorno));
		else
			this
					.setResponseView(new EntidadeEnderecoView(endereco,
							origemMenu));
	}

	public void visualizarEntidadeEndereco(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));
		Entidade.Endereco endereco = entidade
				.obterEndereco(action.getInt("id"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		this.setResponseView(new EntidadeEnderecoView(endereco, origemMenu));
	}
}