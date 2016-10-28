package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.view.EntidadeContatoView;
import com.gvs.crm.view.EntidadeView;
import com.gvs.crm.view.EventoView;

import infra.control.Action;
import infra.control.Control;

public class EntidadeContatoControl extends Control {
	public void atualizarEntidadeContato(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));
		Entidade.Contato contato = entidade.obterContato(action.getInt("id"));
		mm.beginTransaction();
		try {
			contato.atualizarValor(action.getString("valor"), action
					.getString("nome_contato"));
			this.setResponseView(new EntidadeView(entidade));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeContatoView(contato));
			mm.rollbackTransaction();
		}
	}

	public void excluirEntidadeContato(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));
		Entidade.Contato contato = entidade.obterContato(action.getInt("id"));
		mm.beginTransaction();
		try {
			entidade.removerContato(contato);
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
		}
		this.setResponseView(new EntidadeView(entidade));
	}

	public void incluirEntidadeContato(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));
		mm.beginTransaction();
		try {
			String nome = action.getString("nome");
			if (nome.equals(""))
				nome = action.getString("nomeSelecionado");
			entidade.adicionarContato(nome, action.getString("valor"), action
					.getString("nome_contato"));
			mm.commitTransaction();

			Evento retorno = null;

			if (action.getLong("retornoId") > 0)
				retorno = eventoHome.obterEventoPorId(action
						.getLong("retornoId"));

			if (retorno != null)
				this.setResponseView(new EventoView(retorno));
			else
				this.setResponseView(new EntidadeView(entidade));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
		}
	}

	public void novoEntidadeContato(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));

		Evento retorno = null;

		if (action.getLong("retornoId") > 0)
			retorno = eventoHome.obterEventoPorId(action.getLong("retornoId"));

		if (retorno != null)
			this.setResponseView(new EntidadeContatoView(entidade, origemMenu,
					retorno));
		else
			this.setResponseView(new EntidadeContatoView(entidade, origemMenu));
	}

	public void visualizarEntidadeContato(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		Entidade.Contato contato = entidade.obterContato(action.getInt("id"));
		this.setResponseView(new EntidadeContatoView(contato, origemMenu));
	}
}