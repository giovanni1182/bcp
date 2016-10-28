package com.gvs.crm.control;

import java.util.Iterator;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Processo;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.CancelarProcessoView;
import com.gvs.crm.view.EventoView;

import infra.control.Action;
import infra.control.Control;

public class ProcessoControl extends Control {
	public void atualizarProcesso(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Processo processo = (Processo) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {

			/*
			 * if(action.getLong("destinoId") == 0) throw new Exception("Elegiré
			 * el Demandante");
			 * 
			 * if(action.getLong("origemId") == 0) throw new Exception("Elegiré
			 * el Demandado");
			 */

			/*
			 * if(action.getLong("produto")==0) throw new Exception("Escolha o
			 * Tipo do Processo");
			 */

			//Entidade origem =
			// entidadeHome.obterEntidadePorId(action.getLong("origemId"));
			//Entidade destino =
			// entidadeHome.obterEntidadePorId(action.getLong("destinoId"));
			//Produto produto = (Produto)
			// entidadeHome.obterEntidadePorId(action.getLong("produto"));
			/*
			 * processo.atualizarOrigem(origem);
			 * processo.atualizarDestino(destino);
			 */
			processo.atualizarResponsavel(usuarioAtual);
			processo.atualizarTitulo("Processo");
			processo.atualizarDescricao(action.getString("descricao"));
			//processo.atribuirTipo(produto.obterNome());
			processo.atualizarExpediente(action.getString("expediente"));
			processo.atualizarValorAcao(action.getDouble("valorAcao"));
			processo.atualizarJulgado(action.getString("julgado"));
			processo.atualizarJuiz(action.getString("juiz"));
			processo.atualizarSecretaria(action.getString("secretaria"));
			processo.atualizarFiscal(action.getString("fiscal"));
			processo.atualizarTurno(action.getString("turno"));
			processo.atualizarCargo(action.getString("cargo"));
			processo.atualizarObjeto(action.getString("objeto"));
			processo.atualizarCircunscricao(action.getString("circunscricao"));
			processo.atualizarForum(action.getString("forum"));

			if (action.getDate("dataDemanda") != null)
				processo.atualizarDataDemanda(action.getDate("dataDemanda"));

			processo.atualizarSentenca(action.getString("sentenca"));

			if (action.getDate("dataCancelamento") != null)
				processo.atualizarDataCancelamento(action
						.getDate("dataCancelamento"));

			processo.atualizarTitulo("Processo - "
					+ action.getString("expediente"));

			this.setResponseView(new EventoView(processo));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(processo));
			mm.rollbackTransaction();
		}
	}

	public void cancelarProcesso(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Processo processo = (Processo) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {

			if (action.getBoolean("view"))
				this.setResponseView(new CancelarProcessoView(processo));
			else {
				processo.adicionarComentario("Processo cancelado por "
						+ usuarioAtual.obterNome(), action
						.getString("comentario"));

				processo.atualizarFase(Processo.PROCESSO_CANCELADO);

				processo.atualizarTitulo(processo.obterTitulo());

				this.setResponseView(new EventoView(processo));
			}

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(processo));
			mm.rollbackTransaction();
		}
	}

	public void incluirProcesso(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Processo processo = (Processo) mm.getEntity("Processo");

		mm.beginTransaction();
		try {

			/*
			 * if(action.getLong("destinoId") == 0) throw new Exception("Elegiré
			 * el Demandante");
			 * 
			 * if(action.getLong("origemId") == 0) throw new Exception("Elegiré
			 * el Demandado");
			 */
			/*
			 * if(action.getLong("produto")==0) throw new Exception("Escolha o
			 * Tipo do Processo");
			 */

			//Entidade origem =
			// entidadeHome.obterEntidadePorId(action.getLong("origemId"));
			//Entidade destino =
			// entidadeHome.obterEntidadePorId(action.getLong("destinoId"));
			//Produto produto = (Produto)
			// entidadeHome.obterEntidadePorId(action.getLong("produto"));
			/*
			 * processo.atribuirOrigem(origem);
			 * processo.atribuirDestino(destino);
			 */
			processo.atribuirResponsavel(usuarioAtual);
			processo.atribuirTitulo("Processo");
			processo.atribuirDescricao(action.getString("descricao"));
			//processo.atribuirTipo(produto.obterNome());
			processo.incluir();

			processo.atualizarExpediente(action.getString("expediente"));
			processo.atualizarValorAcao(action.getDouble("valorAcao"));
			processo.atualizarJulgado(action.getString("julgado"));
			processo.atualizarJuiz(action.getString("juiz"));
			processo.atualizarSecretaria(action.getString("secretaria"));
			processo.atualizarFiscal(action.getString("fiscal"));
			processo.atualizarTurno(action.getString("turno"));
			processo.atualizarCargo(action.getString("cargo"));
			processo.atualizarObjeto(action.getString("objeto"));
			processo.atualizarCircunscricao(action.getString("circunscricao"));
			processo.atualizarForum(action.getString("forum"));

			if (action.getDate("dataDemanda") != null)
				processo.atualizarDataDemanda(action.getDate("dataDemanda"));

			processo.atualizarSentenca(action.getString("sentenca"));

			if (action.getDate("dataCancelamento") != null)
				processo.atualizarDataCancelamento(action
						.getDate("dataCancelamento"));

			processo.atualizarTitulo("Processo - "
					+ action.getString("expediente"));

			this.setResponseView(new EventoView(processo));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(processo));
			mm.rollbackTransaction();
		}
	}

	public void incluirPessoaProcesso(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Processo processo = (Processo) eventoHome.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {
			Entidade pessoa = null;

			if (!action.getString("nome").equals("")) 
			{
				Entidade novaPessoa = (Entidade) mm.getEntity("pessoa");

				Entidade superior = entidadeHome.obterEntidadePorApelido("autores");

				novaPessoa.atribuirResponsavel(usuarioAtual);
				novaPessoa.atribuirSuperior(superior);
				novaPessoa.atribuirNome(action.getString("nome"));
				novaPessoa.incluir();

				if (!action.getString("telefone").equals(""))
					novaPessoa.adicionarContato("Telèfono", action.getString("telefone"), "");

				if (!action.getString("email").equals(""))
					novaPessoa.adicionarContato("Email", action.getString("email"),	"");

				pessoa = novaPessoa;

			} 
			else if (action.getLong("pessoaId") == 0)
				throw new Exception("Elegiré a persona");
			else
				pessoa = entidadeHome.obterEntidadePorId(action.getLong("pessoaId"));

			for (Iterator i = processo.obterPessoas().iterator(); i.hasNext();)
			{
				Processo.Pessoa pessoa2 = (Processo.Pessoa) i.next();

				if (pessoa2.obterPessoa().obterId() == pessoa.obterId())
					throw new Exception(pessoa.obterNome()	+ " já faz parte deste processo.");
			}

			processo.adicionarPessoa(pessoa, action.getString("tipoPessoa"));

			if (action.getString("tipoPessoa").equals("Autor"))
				processo.atualizarOrigem(pessoa);
			else if (action.getString("tipoPessoa").equals("Abogado") || action.getString("tipoPessoa").equals("Responsable"))
				processo.atualizarResponsaveis(pessoa);
			else if (action.getString("tipoPessoa").equals("Sumariado"))
				processo.atualizarDestino(pessoa);

			mm.commitTransaction();
			this.setResponseView(new EventoView(processo));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(processo));
			mm.rollbackTransaction();
		}
	}

	public void excluirPessoaProcesso(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Processo processo = (Processo) eventoHome.obterEventoPorId(action
				.getLong("id"));
		Processo.Pessoa pessoa = (Processo.Pessoa) processo.obterPessoa(action
				.getInt("pessoaId2"));

		mm.beginTransaction();
		try {
			if (pessoa.obterTipo().equals("Autor"))
				processo.excluirOrigem(pessoa.obterPessoa());
			else if (pessoa.obterTipo().equals("Advogado")
					|| pessoa.obterTipo().equals("Responsable"))
				processo.excluirResponsavel(pessoa.obterPessoa());

			processo.removerPessoa(pessoa);

			mm.commitTransaction();
			this.setResponseView(new EventoView(processo));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(processo));
			mm.rollbackTransaction();
		}
	}

	public void reabrirProcesso(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Processo processo = (Processo) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {
			processo.atualizarFase(Processo.PROCESSO_RECORRIDO);

			mm.commitTransaction();
			this.setResponseView(new EventoView(processo));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(processo));
			mm.rollbackTransaction();
		}
	}
}