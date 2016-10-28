package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Tarefa;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class TarefaControl extends Control {
	public void atualizarTarefa(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Tarefa tarefa = (Tarefa) eventoHome.obterEventoPorId(action
				.getLong("id"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		mm.beginTransaction();
		try {
			long origemId = action.getLong("origemId");
			if (origemId == 0)
				throw new Exception("A origem deve ser selecionada");
			Entidade origem = entidadeHome.obterEntidadePorId(origemId);
			if (origem == null)
				throw new Exception("A origem não foi encontrada");

			tarefa.atualizarOrigem(origem);
			tarefa.atualizarPrioridade(action.getInt("prioridade"));
			tarefa.atualizarTitulo(action.getString("titulo"));
			tarefa.atualizarDescricao(action.getString("descricao"));
			tarefa.atualizarDataPrevistaInicio(action
					.getDate("dataPrevistaInicio"));
			tarefa.atualizarDataPrevistaConclusao(action
					.getDate("dataPrevistaConclusao"));
			tarefa.atualizarDuracao(new Long(
					action.getLong("duracaoPrevista") * 3600000));
			tarefa.calcularPrevisoes();

			if (tarefa.obterSuperior() == null) {
				UsuarioHome usuarioHome = (UsuarioHome) mm
						.getHome("UsuarioHome");
				Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this
						.getUser());
				this.setResponseView(new PaginaInicialView(usuarioAtual,
						origemMenu));
			} else {
				this.setResponseView(new EventoView(tarefa.obterSuperior()));
			}
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(tarefa));
			mm.rollbackTransaction();
		}
	}

	public void incluirTarefa(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		Tarefa tarefa = (Tarefa) mm.getEntity("Tarefa");
		mm.beginTransaction();
		try {
			long superiorId = action.getLong("superiorId");
			Evento superior = null;
			if (superiorId > 0) {
				EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
				superior = eventoHome.obterEventoPorId(superiorId);
			}

			tarefa.atribuirSuperior(superior);
			tarefa.atribuirOrigem(entidadeHome.obterEntidadePorId(action
					.getLong("origemId")));
			tarefa.atribuirResponsavel(entidadeHome.obterEntidadePorId(action
					.getLong("responsavelId")));
			tarefa.atribuirPrioridade(action.getInt("prioridade"));
			tarefa.atribuirTitulo(action.getString("titulo"));
			tarefa.atribuirDescricao(action.getString("descricao"));
			tarefa.atribuirDataPrevistaInicio(action
					.getDate("dataPrevistaInicio"));
			tarefa.atribuirDataPrevistaConclusao(action
					.getDate("dataPrevistaConclusao"));
			tarefa.atribuirDuracao(new Long(
					action.getLong("duracaoPrevista") * 3600000));
			tarefa.incluir();
			tarefa.calcularPrevisoes();

			if (superiorId == 0) {
				this.setResponseView(new PaginaInicialView(usuarioAtual,
						origemMenu));
			} else {
				this.setResponseView(new EventoView(superior, origemMenu));
			}
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(tarefa, origemMenu));
			mm.rollbackTransaction();
		}
	}

	/*
	 * Ação para alterar a fase da tarefa para "iniciada".
	 * 
	 * PARÂMETROS OBRIGATÓRIOS id:long : id da tarefa a ser atualizada.
	 * dataEfetivaInicio:Date : data que se deu início a tarefa.
	 * comentario:String : comentario de início da tarefa.
	 * 
	 * SELEÇÃO DA RESPOSTA O EventoView(tarefa) será selecionada se ocorrer
	 * qualquer erro na inclusão. O PaginaInicialView(usuarioAtual) será
	 * selecionado em qualquer outro caso.
	 */
	public void iniciarTarefa(Action action) throws Exception {
	}
}