package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Documento;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoContato;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;

import infra.control.Action;
import infra.control.Control;

public class DocumentoControl extends Control {
	public void incluirDocumento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");

		Documento documento = (Documento) mm.getEntity("Documento");

		mm.beginTransaction();
		try {

			Evento superior = null;

			if (action.getLong("superiorId") > 0)
				superior = eventoHome.obterEventoPorId(action
						.getLong("superiorId"));

			documento.atribuirTipo(action.getString("tipo"));
			documento.atribuirTitulo(action.getString("titulo"));
			documento.atribuirDescricao(action.getString("descricao"));

			if (action.getLong("origemId") == 0)
				throw new Exception("Elegiré Para Quién");
			Entidade origem = entidadeHome.obterEntidadePorId(action
					.getLong("origemId"));

			if (action.getLong("responsavelId") == 0)
				throw new Exception("Elegiré o Responsable");
			Entidade responsavel = entidadeHome.obterEntidadePorId(action
					.getLong("responsavelId"));

			if (action.getString("titulo").equals(""))
				throw new Exception("Elegiré o Titulo del Documento");

			documento.atribuirTipo(action.getString("tipo"));
			documento.atribuirTitulo(action.getString("titulo"));
			documento.atribuirDescricao(action.getString("descricao"));

			Entidade destino = entidadeHome.obterEntidadePorApelido("bcp");

			if (superior != null)
				documento.atribuirSuperior(superior);
			documento.atribuirOrigem(origem);
			documento.atribuirDestino(destino);
			documento.atribuirTipo(action.getString("tipo"));
			documento.atribuirTitulo(action.getString("titulo"));
			documento.atribuirDescricao(action.getString("descricao"));
			documento.atribuirResponsavel(responsavel);
			documento.incluir();

			if (action.getString("novoRamo") != null
					&& !action.getString("novoRamo").equals("")) {
				documento.adicionarNovoRamo(action.getString("novoRamo"));
				documento.atualizarRamo(action.getString("novoRamo"));
			} else
				documento.atualizarRamo(action.getString("ramo"));

			documento.atualizarTitulo(action.getString("titulo"));

			if (action.getDate("dataAgenda") != null) {
				EventoContato contato = (EventoContato) mm
						.getEntity("EventoContato");

				contato.atribuirOrigem(origem);
				contato.atribuirDestino(destino);
				contato.atribuirSuperior(documento);
				contato.atribuirTipo("Diversos");
				contato.atribuirTitulo(action.getString("titulo"));
				contato
						.atribuirDataPrevistaInicio(action
								.getDate("dataAgenda"));
				contato.atribuirDataPrevistaConclusao(action
						.getDate("dataAgenda"));
				contato.incluir();

				documento.atualizarDataAgenda(action.getDate("dataAgenda"));
			}

			documento.atualizarDiferenciador(action.getString("diferenciador"));

			this.setAlert("Documento incluido");
			this.setResponseView(new EventoView(documento));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(documento));
			mm.rollbackTransaction();
		}
	}

	public void atualizarDocumento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");

		Documento documento = (Documento) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {
			Evento superior = null;

			if (action.getLong("superiorId") > 0)
				superior = eventoHome.obterEventoPorId(action
						.getLong("superiorId"));

			documento.atribuirTipo(action.getString("tipo"));
			documento.atribuirTitulo(action.getString("titulo"));
			documento.atribuirDescricao(action.getString("descricao"));

			if (action.getLong("origemId") == 0)
				throw new Exception("Elegiré Para Quién");
			Entidade origem = entidadeHome.obterEntidadePorId(action
					.getLong("origemId"));

			if (action.getLong("responsavelId") == 0)
				throw new Exception("Elegiré el Responsable");
			Entidade responsavel = entidadeHome.obterEntidadePorId(action
					.getLong("responsavelId"));

			if (action.getString("titulo").equals(""))
				throw new Exception("Elegiré el Titulo del Documento");

			documento.atualizarOrigem(origem);
			documento.atualizarTipo(action.getString("tipo"));
			//documento.atualizarTitulo(action.getString("titulo"));
			documento.atualizarDescricao(action.getString("descricao"));
			documento.atualizarResponsavel(responsavel);

			if (action.getString("novoRamo") != null
					&& !action.getString("novoRamo").equals("")) {
				documento.adicionarNovoRamo(action.getString("novoRamo"));
				documento.atualizarRamo(action.getString("novoRamo"));
			} else
				documento.atualizarRamo(action.getString("ramo"));

			documento.atualizarTitulo(action.getString("titulo"));

			if (action.getDate("dataAgenda") != null)
				documento.atualizarDataAgenda(action.getDate("dataAgenda"));

			documento.atualizarDiferenciador(action.getString("diferenciador"));

			this.setAlert("Documento actualizado");
			this.setResponseView(new EventoView(documento));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(documento));
			mm.rollbackTransaction();
		}
	}
}