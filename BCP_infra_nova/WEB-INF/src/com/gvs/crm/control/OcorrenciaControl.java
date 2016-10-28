package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Ocorrencia;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class OcorrenciaControl extends Control {
	public void atualizarOcorrencia(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Ocorrencia ocorrencia = (Ocorrencia) eventoHome.obterEventoPorId(action
				.getLong("id"));
		mm.beginTransaction();
		try {
			long seguradoraId = action.getLong("seguradoraId");
			Entidade seguradora = entidadeHome.obterEntidadePorId(seguradoraId);

			long responsavelId = action.getLong("responsavelId");
			Entidade responsavel = entidadeHome
					.obterEntidadePorId(responsavelId);

			ocorrencia.atribuirOrigem(seguradora);
			ocorrencia.atribuirResponsavel(responsavel);
			ocorrencia.atribuirTipo(action.getString("tipo"));
			ocorrencia.atribuirTitulo(action.getString("titulo"));
			ocorrencia.atribuirDescricao(action.getString("descricao"));

			if (seguradoraId == 0)
				throw new Exception("Elegiré Para Quién");
			if (seguradora == null)
				throw new Exception("A seguradora não foi encontrado");
			if (responsavelId == 0)
				throw new Exception("Elegiré el Responsable");
			if (responsavel == null)
				throw new Exception("O responsável não foi selecionado");
			if (action.getString("tipo").equals(""))
				throw new Exception("Elegiré el Tipo del Ocurrencia");

			ocorrencia.atualizarOrigem(seguradora);
			ocorrencia.atualizarTipo(action.getString("tipo"));
			ocorrencia.atualizarPrioridade(action.getInt("prioridade"));
			ocorrencia.atualizarTitulo(action.getString("titulo"));
			ocorrencia.atualizarDescricao(action.getString("descricao"));

			this
					.setResponseView(new PaginaInicialView(usuarioAtual,
							origemMenu));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(ocorrencia, origemMenu));
			mm.rollbackTransaction();
		}
	}

	public void incluirOcorrencia(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		Ocorrencia ocorrencia = (Ocorrencia) mm.getEntity("Ocorrencia");
		mm.beginTransaction();
		try {

			long seguradoraId = action.getLong("seguradoraId");
			Entidade seguradora = entidadeHome.obterEntidadePorId(seguradoraId);

			long responsavelId = action.getLong("responsavelId");
			Entidade responsavel = entidadeHome
					.obterEntidadePorId(responsavelId);

			Entidade destino = (ClassificacaoContas) entidadeHome
					.obterEntidadePorApelido("planodecontas");

			ocorrencia.atribuirOrigem(seguradora);
			ocorrencia.atribuirDestino(destino);
			ocorrencia.atribuirResponsavel(responsavel);
			ocorrencia.atribuirTipo(action.getString("tipo"));
			ocorrencia.atribuirTitulo(action.getString("titulo"));
			ocorrencia.atribuirDescricao(action.getString("descricao"));

			if (seguradoraId == 0)
				throw new Exception("Elegiré Para Quién");
			if (seguradora == null)
				throw new Exception("A seguradora não foi encontrado");
			if (responsavelId == 0)
				throw new Exception("Elegiré el Responsable");
			if (responsavel == null)
				throw new Exception("O responsável não foi selecionado");
			if (action.getString("tipo").equals(""))
				throw new Exception("Elegiré el Tipo del Ocurrencia");

			ocorrencia.atribuirOrigem(seguradora);
			ocorrencia.atribuirDestino(destino);
			ocorrencia.atribuirResponsavel(responsavel);
			ocorrencia.atribuirTipo(action.getString("tipo"));
			ocorrencia.atribuirTitulo(action.getString("titulo"));
			ocorrencia.atribuirDescricao(action.getString("descricao"));
			ocorrencia.incluir();

			this
					.setResponseView(new PaginaInicialView(usuarioAtual,
							origemMenu));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(ocorrencia, origemMenu));
			mm.rollbackTransaction();
		}
	}
}