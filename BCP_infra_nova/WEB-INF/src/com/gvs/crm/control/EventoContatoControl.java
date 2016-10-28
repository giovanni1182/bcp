package com.gvs.crm.control;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoContato;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class EventoContatoControl extends Control {
	public void atualizarEventoContato(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		EventoContato contato = (EventoContato) eventoHome
				.obterEventoPorId(action.getLong("id"));
		mm.beginTransaction();
		try {
			if (action.getString("data").equals(""))
				throw new Exception("Elegiré la fecha de lo Contacto");

			Date dataPrevista = new SimpleDateFormat("dd/MM/yyyy HH:mm")
					.parse(action.getString("data") + " "
							+ action.getString("inicio"));

			if (action.getString("tipo").equals(""))
				throw new Exception("Elegiré lo tipo del lo Contacto");

			long origemId = action.getLong("origemId");
			if (origemId == 0)
				throw new Exception("El Nombre debe ser seleccionado");
			Entidade origem = entidadeHome.obterEntidadePorId(origemId);
			if (origem == null)
				throw new Exception("Nombre no encontrado");

			if (action.getLong("responsavelId") == 0)
				throw new Exception("Elegiré lo Responsable");

			Entidade responsavel = entidadeHome.obterEntidadePorId(action
					.getLong("responsavelId"));

			contato.atualizarOrigem(origem);
			contato.atualizarTitulo(action.getString("titulo"));
			contato.atualizarTipo(action.getString("tipo"));
			contato.atualizarDataPrevistaInicio(dataPrevista);
			contato.atualizarDataPrevistaConclusao(dataPrevista);
			contato.atualizarDescricao(action.getString("descricao"));
			contato.atualizarPrioridade(Evento.PRIORIDADE_NORMAL);
			contato.atualizarResponsavel(responsavel);

			this.setResponseView(new EventoView(contato, origemMenu));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(contato));
			mm.rollbackTransaction();
		}
	}

	public void incluirEventoContato(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		EventoContato contato = (EventoContato) mm.getEntity("EventoContato");
		mm.beginTransaction();
		try {
			long origemId = action.getLong("origemId");
			Entidade origem = entidadeHome.obterEntidadePorId(origemId);
			contato.atribuirOrigem(origem);
			contato.atribuirTipo(action.getString("tipo"));
			contato.atribuirDescricao(action.getString("descricao"));

			if (origemId == 0)
				throw new Exception("O Nombre debe ser seleccionado");
			if (origem == null)
				throw new Exception("Nombre no encontrado");
			if (action.getString("tipo").equals(""))
				throw new Exception("Elegiré lo tipo del lo Contacto");
			if (action.getString("data").equals(""))
				throw new Exception("Elegiré la fecha de lo Contacto");

			Date dataPrevista = new SimpleDateFormat("dd/MM/yyyy HH:mm")
					.parse(action.getString("data") + " "
							+ action.getString("inicio"));

			long superiorId = action.getLong("superiorId");
			Evento superior = null;
			if (superiorId > 0) {
				EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
				superior = eventoHome.obterEventoPorId(superiorId);
			}

			contato.atribuirSuperior(superior);
			contato.atribuirOrigem(origem);
			contato.atribuirResponsavel(usuarioAtual);
			contato.atribuirTitulo(action.getString("titulo"));
			contato.atribuirTipo(action.getString("tipo"));
			contato.atribuirDataPrevistaInicio(dataPrevista);
			contato.atribuirDataPrevistaConclusao(dataPrevista);
			contato.atribuirDescricao(action.getString("descricao"));
			contato.incluir();

			this
					.setResponseView(new PaginaInicialView(usuarioAtual,
							origemMenu));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(contato, origemMenu));
			mm.rollbackTransaction();
		}
	}
}