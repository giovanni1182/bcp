package com.gvs.crm.control;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Reclamacao;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;

import infra.control.Action;
import infra.control.Control;

public class ReclamacaoControl extends Control {
	public void atualizarReclamacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		Reclamacao reclamacao = (Reclamacao) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {

			if (action.getLong("origemId") == 0)
				throw new Exception("Elegiré el Solicitante");

			if (action.getLong("responsavelId") == 0)
				throw new Exception("Elegiré el responsable");

			if (action.getString("titulo").equals(""))
				throw new Exception("Elegiré el Titulo");

			Entidade responsavelTela = entidadeHome.obterEntidadePorId(action
					.getLong("responsavelId"));

			Entidade origem = entidadeHome.obterEntidadePorId(action
					.getLong("origemId"));

			if (action.getLong("destinoId") > 0) {
				Entidade destino = entidadeHome.obterEntidadePorId(action
						.getLong("destinoId"));

				reclamacao.atualizarDestino(destino);
			}

			reclamacao.atualizarOrigem(origem);
			reclamacao.atualizarResponsavel(responsavelTela);
			reclamacao.atualizarTipo(action.getString("tipo"));
			reclamacao.atualizarTitulo(action.getString("titulo"));
			reclamacao.atualizarDescricao(action.getString("descricao"));

			if (action.getLong("apoliceId") > 0) {
				Apolice apolice = (Apolice) eventoHome.obterEventoPorId(action
						.getLong("apoliceId"));

				reclamacao.atualizarApolice(apolice);
			}

			this.setResponseView(new EventoView(reclamacao));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(reclamacao));
			mm.rollbackTransaction();
		}
	}

	public void incluirReclamacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		Reclamacao reclamacao = (Reclamacao) mm.getEntity("Reclamacao");
		Entidade pessoa = (Entidade) mm.getEntity("pessoafisica");

		mm.beginTransaction();
		try {

			boolean novaPessoa = false;

			if (action.getLong("responsavelId") == 0)
				throw new Exception("Elegiré el Responsable");

			if (action.getString("titulo").equals(""))
				throw new Exception("Elegiré el Titulo");

			if (!action.getString("paraQuem").equals("")) {
				Entidade superior = entidadeHome
						.obterEntidadePorApelido("reclamantes");

				pessoa.atribuirNome(action.getString("paraQuem"));
				pessoa.atribuirResponsavel(responsavel);
				pessoa.atribuirSuperior(superior);
				pessoa.incluir();

				if (!action.getString("telefone").equals(""))
					pessoa.adicionarContato("Telefone", action
							.getString("telefone"), "");
				if (!action.getString("email").equals(""))
					pessoa.adicionarContato("Email", action.getString("email"),
							"");

				novaPessoa = true;
			} else {
				if (action.getLong("origemId") == 0)
					throw new Exception("Elegiré el Solicitant");
			}

			Entidade responsavelTela = entidadeHome.obterEntidadePorId(action
					.getLong("responsavelId"));
			Entidade origem = entidadeHome.obterEntidadePorId(action
					.getLong("origemId"));

			if (novaPessoa)
				reclamacao.atribuirOrigem(pessoa);
			else
				reclamacao.atribuirOrigem(origem);

			if (action.getLong("destinoId") > 0) {
				Entidade destino = entidadeHome.obterEntidadePorId(action
						.getLong("destinoId"));

				reclamacao.atribuirDestino(destino);
			}

			reclamacao.atribuirResponsavel(responsavelTela);
			reclamacao.atribuirTipo(action.getString("tipo"));
			reclamacao.atribuirTitulo(action.getString("titulo"));
			reclamacao.atribuirDescricao(action.getString("descricao"));
			reclamacao.incluir();

			if (action.getLong("apoliceId") > 0) {
				Apolice apolice = (Apolice) eventoHome.obterEventoPorId(action
						.getLong("apoliceId"));

				reclamacao.atualizarApolice(apolice);
			}

			this.setResponseView(new EventoView(reclamacao));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(reclamacao));
			mm.rollbackTransaction();
		}
	}
}