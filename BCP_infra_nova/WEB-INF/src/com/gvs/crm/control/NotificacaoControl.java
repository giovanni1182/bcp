package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Notificacao;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class NotificacaoControl extends Control {
	public void atualizarNotificacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Notificacao notificacao = (Notificacao) eventoHome
				.obterEventoPorId(action.getLong("id"));
		mm.beginTransaction();
		try {
			long seguradoraId = action.getLong("seguradoraId");
			Entidade seguradora = entidadeHome.obterEntidadePorId(seguradoraId);

			long responsavelId = action.getLong("responsavelId");
			Entidade responsavel = entidadeHome
					.obterEntidadePorId(responsavelId);

			notificacao.atribuirOrigem(seguradora);
			notificacao.atribuirResponsavel(responsavel);
			notificacao.atribuirTipo(action.getString("tipo"));
			notificacao.atribuirTitulo(action.getString("titulo"));
			notificacao.atribuirDescricao(action.getString("descricao"));

			if (seguradoraId == 0)
				throw new Exception("A seguradora deve ser selecionada");
			if (seguradora == null)
				throw new Exception("A seguradora não foi encontrado");
			if (responsavelId == 0)
				throw new Exception("O responsável deve ser selecionado");
			if (responsavel == null)
				throw new Exception("O responsável não foi selecionado");
			if (action.getString("tipo").equals(""))
				throw new Exception("Escolha o tipo da Ocorrência");

			notificacao.atualizarOrigem(seguradora);
			notificacao.atualizarTipo(action.getString("tipo"));
			notificacao.atualizarPrioridade(action.getInt("prioridade"));
			notificacao.atualizarTitulo(action.getString("titulo"));
			notificacao.atualizarDescricao(action.getString("descricao"));

			this
					.setResponseView(new PaginaInicialView(usuarioAtual,
							origemMenu));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(notificacao, origemMenu));
			mm.rollbackTransaction();
		}
	}

	public void incluirNotificacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		Notificacao notificacao = (Notificacao) mm.getEntity("Notificacao");
		mm.beginTransaction();
		try {

			long seguradoraId = action.getLong("seguradoraId");
			Entidade seguradora = entidadeHome.obterEntidadePorId(seguradoraId);

			long responsavelId = action.getLong("responsavelId");
			Entidade responsavel = entidadeHome
					.obterEntidadePorId(responsavelId);

			Entidade destino = (ClassificacaoContas) entidadeHome
					.obterEntidadePorApelido("planodecontas");

			notificacao.atribuirOrigem(seguradora);
			notificacao.atribuirDestino(destino);
			notificacao.atribuirResponsavel(responsavel);
			notificacao.atribuirTipo(action.getString("tipo"));
			notificacao.atribuirTitulo(action.getString("titulo"));
			notificacao.atribuirDescricao(action.getString("descricao"));

			if (seguradoraId == 0)
				throw new Exception("A seguradora deve ser selecionada");
			if (seguradora == null)
				throw new Exception("A seguradora não foi encontrado");
			if (responsavelId == 0)
				throw new Exception("O responsável deve ser selecionado");
			if (responsavel == null)
				throw new Exception("O responsável não foi selecionado");
			if (action.getString("tipo").equals(""))
				throw new Exception("Escolha o tipo da Ocorrência");

			notificacao.atribuirOrigem(seguradora);
			notificacao.atribuirDestino(destino);
			notificacao.atribuirResponsavel(responsavel);
			notificacao.atribuirTipo(action.getString("tipo"));
			notificacao.atribuirTitulo(action.getString("titulo"));
			notificacao.atribuirDescricao(action.getString("descricao"));
			notificacao.incluir();

			this
					.setResponseView(new PaginaInicialView(usuarioAtual,
							origemMenu));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(notificacao, origemMenu));
			mm.rollbackTransaction();
		}
	}
}