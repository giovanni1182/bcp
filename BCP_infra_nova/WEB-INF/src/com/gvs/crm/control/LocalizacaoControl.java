package com.gvs.crm.control;

import java.util.ArrayList;
import java.util.Collection;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.view.LocalizacaoView;

import infra.control.Action;
import infra.control.Control;

public class LocalizacaoControl extends Control {
	public void localizar(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenuId = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));

		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		String pesquisaEntidade = action.getString("pesquisaEntidade").trim();
		String pesquisaEvento = action.getString("pesquisaEvento").trim();
		Collection entidades;
		Collection eventos;
		if (pesquisaEntidade.equals("") && pesquisaEvento.equals("")) {
			entidades = new ArrayList();
			eventos = new ArrayList();
			this.setResponseView(new LocalizacaoView(pesquisaEntidade,
					pesquisaEvento, entidades, eventos, origemMenuId));
		}
		if (!pesquisaEntidade.equals("")) {
			eventos = new ArrayList();

			entidades = entidadeHome.localizarEntidades(pesquisaEntidade);

			this.setResponseView(new LocalizacaoView(pesquisaEntidade,
					pesquisaEvento, entidades, eventos, origemMenuId));
		}

		if (!pesquisaEvento.equals("")) {
			entidades = new ArrayList();
			eventos = eventoHome.localizarEventos(pesquisaEvento);
			this.setResponseView(new LocalizacaoView(pesquisaEntidade,
					pesquisaEvento, entidades, eventos, origemMenuId));
		}

		if (!pesquisaEntidade.equals("") && !pesquisaEvento.equals("")) {
			//entidades = entidadeHome.localizarEntidades(pesquisaEntidade);
			entidades = entidadeHome.localizarEntidades(pesquisaEntidade);
			eventos = eventoHome.localizarEventos(pesquisaEvento);
			this.setResponseView(new LocalizacaoView(pesquisaEntidade,
					pesquisaEvento, entidades, eventos, origemMenuId));
		}
	}
}