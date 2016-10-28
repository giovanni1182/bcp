package com.gvs.crm.control;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Compromisso;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class CompromissoControl extends Control {
	public void atualizarCompromisso(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Compromisso compromisso = (Compromisso) eventoHome
				.obterEventoPorId(action.getLong("id"));
		mm.beginTransaction();
		try {
			if (action.getString("data").equals(""))
				throw new Exception("Elegiré la fecha de la Compromiso");

			if (action.getString("tipo").equals(""))
				throw new Exception("Elegiré lo tipo del la Compromiso");

			Date dataPrevistaInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm")
					.parse(action.getString("data") + " "
							+ action.getString("inicio"));
			Date dataPrevistaConclusao = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm").parse(action.getString("data") + " "
					+ action.getString("conclusao"));
			long origemId = action.getLong("origemId");
			if (origemId == 0)
				throw new Exception("O Nombre debe ser seleccionado");
			Entidade origem = entidadeHome.obterEntidadePorId(origemId);
			if (origem == null)
				throw new Exception("Nombre no encontrado");

			if (action.getLong("responsavelId") == 0)
				throw new Exception("Elegiré lo Responsable");

			Entidade responsavel = entidadeHome.obterEntidadePorId(action
					.getLong("responsavelId"));

			compromisso.atualizarOrigem(origem);
			compromisso.atualizarResponsavel(responsavel);
			compromisso.atualizarTitulo(action.getString("titulo"));
			compromisso.atualizarTipo(action.getString("tipo"));
			compromisso.atualizarDataPrevistaInicio(dataPrevistaInicio);
			compromisso.atualizarDataPrevistaConclusao(dataPrevistaConclusao);
			compromisso.atualizarDescricao(action.getString("descricao"));
			compromisso.notificarParticipantes();
			compromisso.atualizarPrioridade(Evento.PRIORIDADE_NORMAL);
			compromisso.calcularPrevisoes();
			long[] participantes = action.getLongArray("participantes");
			for (int i = 0; i < participantes.length; i++)
				if (participantes[i] > 0) {
					Usuario participante = (Usuario) entidadeHome
							.obterEntidadePorId(participantes[i]);
					compromisso.adicionarParticipante(participante);
				}
			this
					.setResponseView(new PaginaInicialView(usuarioAtual,
							origemMenu));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(compromisso, origemMenu));
			mm.rollbackTransaction();
		}
	}

	public void incluirCompromisso(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		Compromisso compromisso = (Compromisso) mm.getEntity("Compromisso");
		mm.beginTransaction();
		try {

			long origemId = action.getLong("origemId");
			Entidade origem = entidadeHome.obterEntidadePorId(origemId);
			compromisso.atribuirOrigem(origem);
			compromisso.atribuirTipo(action.getString("tipo"));
			compromisso.atribuirDescricao(action.getString("descricao"));

			if (action.getString("data").equals(""))
				throw new Exception("Elegiré la fecha de la Compromiso");

			Date dataPrevista = new SimpleDateFormat("dd/MM/yyyy HH:mm")
					.parse(action.getString("data") + " "
							+ action.getString("inicio"));
			Date dataPrevistaConclusao = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm").parse(action.getString("data") + " "
					+ action.getString("conclusao"));

			long superiorId = action.getLong("superiorId");
			Evento superior = null;
			if (superiorId > 0) {
				EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
				superior = eventoHome.obterEventoPorId(superiorId);
			}

			if (origemId == 0)
				throw new Exception("O Nombre debe ser seleccionado");
			if (origem == null)
				throw new Exception("Nombre no encontrado");

			if (action.getString("tipo").equals(""))
				throw new Exception("Elegiré lo tipo del la Compromiso");

			compromisso.atribuirSuperior(superior);
			compromisso.atribuirOrigem(origem);
			compromisso.atribuirResponsavel(usuarioAtual);
			compromisso.atribuirTitulo(action.getString("titulo"));
			compromisso.atribuirTipo(action.getString("tipo"));
			compromisso.atribuirDataPrevistaInicio(dataPrevista);
			compromisso.atribuirDataPrevistaConclusao(dataPrevistaConclusao);
			compromisso.atribuirDescricao(action.getString("descricao"));
			compromisso.incluir();

			long[] participantes = action.getLongArray("participantes");
			for (int i = 0; i < participantes.length; i++)
				if (participantes[i] > 0) {
					Usuario participante = (Usuario) entidadeHome
							.obterEntidadePorId(participantes[i]);
					compromisso.adicionarParticipante(participante);
				}
			this
					.setResponseView(new PaginaInicialView(usuarioAtual,
							origemMenu));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(compromisso, origemMenu));
			mm.rollbackTransaction();
		}
	}
}