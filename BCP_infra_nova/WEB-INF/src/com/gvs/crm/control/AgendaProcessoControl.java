package com.gvs.crm.control;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gvs.crm.model.AgendaProcesso;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Processo;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;

import infra.control.Action;
import infra.control.Control;

public class AgendaProcessoControl extends Control {
	public void incluirAgendaProcesso(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		Processo processo = (Processo) eventoHome.obterEventoPorId(action
				.getLong("superiorId"));

		AgendaProcesso agenda = (AgendaProcesso) mm.getEntity("AgendaProcesso");

		mm.beginTransaction();

		try {

			Entidade origem = null;
			if (action.getLong("origemId") == 0)
				throw new Exception("Elegiré el Cliente");
			else
				origem = entidadeHome.obterEntidadePorId(action
						.getLong("origemId"));

			if (action.getString("data") == null)
				throw new Exception("Elegiré a Fecha da Agenda");

			Date dataPrevista = new SimpleDateFormat("dd/MM/yyyy HH:mm")
					.parse(action.getString("data") + " "
							+ action.getString("inicio"));

			agenda.atribuirOrigem(origem);
			agenda.atribuirDestino(processo.obterDestino());
			agenda.atribuirResponsavel(usuario);
			agenda.atribuirTitulo(action.getString("titulo"));
			agenda.atribuirSuperior(processo);
			agenda.atribuirDataPrevistaInicio(dataPrevista);
			agenda.atribuirDescricao(action.getString("descricao"));
			agenda.incluir();

			mm.commitTransaction();

			this.setResponseView(new EventoView(agenda));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(agenda));
			mm.rollbackTransaction();
		}

	}

	public void atualizarAgendaProcesso(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		AgendaProcesso agenda = (AgendaProcesso) eventoHome
				.obterEventoPorId(action.getLong("id"));

		Processo processo = (Processo) agenda.obterSuperior();

		mm.beginTransaction();

		try {
			Entidade origem = null;
			if (action.getLong("origemId") == 0)
				throw new Exception("Elegiré el Cliente");
			else
				origem = entidadeHome.obterEntidadePorId(action
						.getLong("origemId"));

			if (action.getString("data") == null)
				throw new Exception("Elegiré a Fecha da Agenda");

			Date dataPrevista = new SimpleDateFormat("dd/MM/yyyy HH:mm")
					.parse(action.getString("data") + " "
							+ action.getString("inicio"));

			agenda.atualizarOrigem(origem);
			agenda.atualizarDestino(processo.obterDestino());
			agenda.atualizarResponsavel(usuario);
			agenda.atualizarTitulo(action.getString("titulo"));
			agenda.atualizarSuperior(processo);
			agenda.atualizarDataPrevistaInicio(dataPrevista);
			agenda.atualizarDescricao(action.getString("descricao"));

			mm.commitTransaction();

			this.setResponseView(new EventoView(agenda));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(agenda));
			mm.rollbackTransaction();
		}

	}
}