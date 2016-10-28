package com.gvs.crm.control;

import java.util.Iterator;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Corretora;
import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeDocumento;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Inscricao;
import com.gvs.crm.model.Inspecao;
import com.gvs.crm.model.Plano;
import com.gvs.crm.model.Reaseguradora;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.report.DocumentoReport;
import com.gvs.crm.view.AprovarView;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.PaginaInicialView;
import com.gvs.crm.view.RejeitarView;

import infra.control.Action;
import infra.control.Control;

public class DocumentoProdutoControl extends Control {
	public void incluirDocumentoProduto(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");

		DocumentoProduto documento = (DocumentoProduto) mm
				.getEntity("DocumentoProduto");

		mm.beginTransaction();
		try {
			Evento superior = null;

			if (action.getLong("superiorId") > 0)
				superior = eventoHome.obterEventoPorId(action
						.getLong("superiorId"));

			documento.atribuirDescricao(action.getString("descricao"));

			/*
			 * if(action.getLong("origemId")==0) throw new Exception("Elegiré
			 * Para Quién"); Entidade origem =
			 * entidadeHome.obterEntidadePorId(action.getLong("origemId"));
			 * documento.atribuirOrigem(origem);
			 */

			if (action.getLong("documento") == 0)
				throw new Exception("Elegiré el Tipo del Documento");

			Entidade destino = entidadeHome.obterEntidadePorApelido("bcp");

			if (superior != null)
				documento.atribuirSuperior(superior);
			//documento.atribuirOrigem(origem);
			documento.atribuirDestino(destino);
			documento.atribuirTitulo("xxx");
			//documento.atribuirDescricao(action.getString("descricao"));
			documento.incluir();

			if (documento.obterSuperior() != null) {
				if (documento.obterSuperior() instanceof DocumentoProduto)
					documento.atualizarFase(DocumentoProduto.INFORMATIVO);
			}

			documento.atualizarNumero(action.getString("numero"));

			documento.atualizarReferente(action.getString("referente"));

			if (action.getDate("data") != null)
				documento.atualizarDataPrevistaInicio(action.getDate("data"));

			EntidadeDocumento entidadeDocumento = (EntidadeDocumento) entidadeHome
					.obterEntidadePorId(action.getLong("documento"));

			documento.atualizarDocumento(entidadeDocumento);

			documento.atualizarTipo(entidadeDocumento.obterNome());

			documento.atualizarTitulo(entidadeDocumento.obterNome() + " - "
					+ action.getString("numero"));

			documento.atualizarTituloDocumento(action.getString("titulo"));

			/*
			 * if(entidadeDocumento.obterApelido().equals("memorando")) {
			 * Memorando memorando = (Memorando) mm.getEntity("Memorando");
			 * 
			 * memorando.atribuirOrigem(documento.obterOrigem());
			 * memorando.atribuirDestino(documento.obterDestino());
			 * memorando.atribuirTitulo("Memorando");
			 * memorando.atribuirSuperior(documento); memorando.incluir(); }
			 * else if(entidadeDocumento.obterApelido().equals("informe")) {
			 * Informe informe = (Informe) mm.getEntity("Informe");
			 * 
			 * informe.atribuirOrigem(documento.obterOrigem());
			 * informe.atribuirDestino(documento.obterDestino());
			 * informe.atribuirTitulo("Informe");
			 * informe.atribuirSuperior(documento); informe.incluir(); } else
			 * if(entidadeDocumento.obterApelido().equals("resolucion")) {
			 * Resolucao resolucao = (Resolucao) mm.getEntity("Resolucao");
			 * 
			 * resolucao.atribuirOrigem(documento.obterOrigem());
			 * resolucao.atribuirDestino(documento.obterDestino());
			 * resolucao.atribuirTitulo("Proyecto de Resolución");
			 * resolucao.atribuirSuperior(documento); resolucao.incluir(); }
			 * else if(entidadeDocumento.obterApelido().equals("circular")) {
			 * Circular circular = (Circular) mm.getEntity("Circular");
			 * 
			 * circular.atribuirOrigem(documento.obterOrigem());
			 * circular.atribuirDestino(documento.obterDestino());
			 * circular.atribuirTitulo("Circular");
			 * circular.atribuirSuperior(documento); circular.incluir(); } else
			 * if(entidadeDocumento.obterApelido().equals("dictame")) { Ditame
			 * ditame = (Ditame) mm.getEntity("Ditame");
			 * 
			 * ditame.atribuirOrigem(documento.obterOrigem());
			 * ditame.atribuirDestino(documento.obterDestino());
			 * ditame.atribuirTitulo("Dictame");
			 * ditame.atribuirSuperior(documento); ditame.incluir(); }
			 */

			this.setAlert("Documento incluido");
			this.setResponseView(new EventoView(documento));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(documento));
			mm.rollbackTransaction();
		}
	}

	public void atualizarDocumentoProduto(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");

		DocumentoProduto documento = (DocumentoProduto) eventoHome
				.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {
			Evento superior = null;

			if (action.getLong("superiorId") > 0)
				superior = eventoHome.obterEventoPorId(action
						.getLong("superiorId"));

			documento.atribuirTitulo(action.getString("titulo"));
			documento.atribuirDescricao(action.getString("descricao"));

			/*
			 * if(action.getLong("origemId")==0) throw new Exception("Elegiré
			 * Para Quién"); Entidade origem =
			 * entidadeHome.obterEntidadePorId(action.getLong("origemId"));
			 */

			if (action.getLong("documento") == 0)
				throw new Exception("Elegiré el Tipo del Documento");

			EntidadeDocumento entidadeDocumento = (EntidadeDocumento) entidadeHome
					.obterEntidadePorId(action.getLong("documento"));

			/*
			 * if(entidadeDocumento.obterNome().equals("Informe") &&
			 * documento.obterDocumento().obterNome().equals("Informe")) {
			 * if(action.getLong("analistaId")==0) throw new Exception("Elegiré
			 * el Analista"); if(action.getLong("chefeId")==0) throw new
			 * Exception("Elegiré el Chefe de División");
			 * if(action.getLong("intendenteId")==0) throw new
			 * Exception("Elegiré el Intendente");
			 * 
			 * Entidade analista =
			 * entidadeHome.obterEntidadePorId(action.getLong("analistaId"));
			 * documento.atualizarAnalista(analista);
			 * 
			 * Entidade chefe =
			 * entidadeHome.obterEntidadePorId(action.getLong("chefeId"));
			 * documento.atualizarChefeDivisao(chefe);
			 * 
			 * Entidade intendente =
			 * entidadeHome.obterEntidadePorId(action.getLong("intendenteId"));
			 * documento.atualizarIntendente(intendente); }
			 */

			/*
			 * else if(entidadeDocumento.obterNome().equals("Dictame") &&
			 * documento.obterDocumento().obterNome().equals("Dictame")) {
			 * Entidade superIntendente =
			 * entidadeHome.obterEntidadePorId(action.getLong("superintendenteId"));
			 * documento.atualizarSuperIntendente(superIntendente); }
			 */

			if (action.getDate("data") != null)
				documento.atualizarDataPrevistaInicio(action.getDate("data"));

			/*
			 * if(entidadeDocumento.obterId()!=documento.obterDocumento().obterId()) {
			 * for(Iterator i = documento.obterInferiores().iterator() ;
			 * i.hasNext() ; ) { Evento e = (Evento) i.next();
			 * 
			 * if(e instanceof Memorando || e instanceof Informe || e instanceof
			 * Resolucao || e instanceof Circular || e instanceof Renovacao)
			 * e.excluir(); }
			 * 
			 * if(entidadeDocumento.obterApelido().equals("memorando")) {
			 * Memorando memorando = (Memorando) mm.getEntity("Memorando");
			 * 
			 * memorando.atribuirOrigem(documento.obterOrigem());
			 * memorando.atribuirDestino(documento.obterDestino());
			 * memorando.atribuirTitulo("Memorando");
			 * memorando.atribuirSuperior(documento); memorando.incluir(); }
			 * else if(entidadeDocumento.obterApelido().equals("informe")) {
			 * Informe informe = (Informe) mm.getEntity("Informe");
			 * 
			 * informe.atribuirOrigem(documento.obterOrigem());
			 * informe.atribuirDestino(documento.obterDestino());
			 * informe.atribuirTitulo("Informe");
			 * informe.atribuirSuperior(documento); informe.incluir(); } else
			 * if(entidadeDocumento.obterApelido().equals("resolucion")) {
			 * Resolucao resolucao = (Resolucao) mm.getEntity("Resolucao");
			 * 
			 * resolucao.atribuirOrigem(documento.obterOrigem());
			 * resolucao.atribuirDestino(documento.obterDestino());
			 * resolucao.atribuirTitulo("Proyecto de Resolución");
			 * resolucao.atribuirSuperior(documento); resolucao.incluir(); }
			 * else if(entidadeDocumento.obterApelido().equals("circular")) {
			 * Circular circular = (Circular) mm.getEntity("Circular");
			 * 
			 * circular.atribuirOrigem(documento.obterOrigem());
			 * circular.atribuirDestino(documento.obterDestino());
			 * circular.atribuirTitulo("Circular");
			 * circular.atribuirSuperior(documento); circular.incluir(); }
			 * 
			 * else if(entidadeDocumento.obterApelido().equals("dictame")) {
			 * Ditame ditame = (Ditame) mm.getEntity("Ditame");
			 * 
			 * ditame.atribuirOrigem(documento.obterOrigem());
			 * ditame.atribuirDestino(documento.obterDestino());
			 * ditame.atribuirTitulo("Dictame");
			 * ditame.atribuirSuperior(documento); ditame.incluir(); }
			 *  // else
			 * if(entidadeDocumento.obterApelido().equals("Renovación")) // { //
			 * Renovacao renovacao = (Renovacao) mm.getEntity("Renovacao"); // //
			 * renovacao.atribuirOrigem(documento.obterOrigem()); //
			 * renovacao.atribuirDestino(documento.obterDestino()); //
			 * renovacao.atribuirTitulo("Renovación"); //
			 * renovacao.atribuirSuperior(documento); // renovacao.incluir(); // } }
			 */

			documento.atualizarDocumento(entidadeDocumento);

			documento.atualizarReferente(action.getString("referente"));
			documento.atualizarTipo(entidadeDocumento.obterNome());
			//documento.atualizarOrigem(origem);
			documento.atualizarTipo(entidadeDocumento.obterNome());
			documento.atualizarTexto(action.getString("texto"));
			documento.atualizarDescricao(action.getString("descricao"));

			documento.atualizarNumero(action.getString("numero"));

			documento.atualizarTitulo(entidadeDocumento.obterNome() + " - "
					+ action.getString("numero"));

			documento.atualizarTituloDocumento(action.getString("titulo"));

			this.setAlert("Documento Actualizado");
			this.setResponseView(new EventoView(documento));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(documento));
			mm.rollbackTransaction();
		}
	}

	public void aprovarDocumentoProduto(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		DocumentoProduto documento = (DocumentoProduto) eventoHome
				.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {

			if (action.getBoolean("view")) {
				if (action.getDate("data") == null)
					throw new Exception("Fecha Documento");

				if (action.getString("numero") == null
						|| action.getString("numero").equals(""))
					throw new Exception("Numero do Documento");

				documento.atualizarDataPrevistaInicio(action.getDate("data"));
				documento.atualizarNumero(action.getString("numero"));

				this.setResponseView(new AprovarView(documento, action));
			} else {
				documento.adicionarComentario("Documento aprovado por "
						+ usuario.obterNome(), action.getString("comentario"));
				documento.atualizarFase(DocumentoProduto.APROVADA);
				documento.atualizarResponsavel(documento.obterCriador());

				if (documento.obterDocumento().obterApelido().equals(
						"resolucion")
						|| documento.obterDocumento().obterApelido().equals(
								"informeinscripcion")) {
					for (Iterator i = documento.obterInscricoesVinculadas()
							.iterator(); i.hasNext();) {
						Inscricao inscricao = (Inscricao) i.next();

						for (Iterator j = inscricao.obterOrigem()
								.obterEventosComoOrigem().iterator(); j
								.hasNext();) {
							Evento e = (Evento) j.next();

							if (e instanceof Inscricao
									&& e.obterId() != inscricao.obterId()) {
								Inscricao inscricaoVelha = (Inscricao) e;

								if ((inscricaoVelha.obterFase().obterCodigo()
										.equals(Inscricao.EVENTO_CONCLUIDO) || inscricaoVelha
										.obterFase().obterCodigo().equals(
												Inscricao.APROVADA))
										&& inscricaoVelha.obterSituacao()
												.equals("Activa"))
									inscricaoVelha
											.atualizarSituacao("Encerrada");
							}
						}

						inscricao.atualizarNumeroResolucao(documento
								.obterNumero());
						inscricao.atualizarDataResolucao(documento
								.obterDataPrevistaInicio());
						inscricao.atualizarFase(Inscricao.APROVADA);
						inscricao
								.atualizarResponsavel(inscricao.obterCriador());
						inscricao.atualizarSituacao("Activa");
						inscricao.atualizarFase(Inscricao.EVENTO_CONCLUIDO);

						Entidade entidade = inscricao.obterOrigem();

						Entidade.Atributo situacao = (Entidade.Atributo) entidade
								.obterAtributo("situacao");

						if (situacao != null)
							situacao.atualizarValor("Activa");
					}

					if (documento.obterSuperior() instanceof Inscricao) {
						Inscricao inscricao = (Inscricao) documento
								.obterSuperior();

						for (Iterator j = inscricao.obterOrigem()
								.obterEventosComoOrigem().iterator(); j
								.hasNext();) {
							Evento e = (Evento) j.next();

							if (e instanceof Inscricao
									&& e.obterId() != inscricao.obterId()) {
								Inscricao inscricaoVelha = (Inscricao) e;

								if ((inscricaoVelha.obterFase().obterCodigo()
										.equals(Inscricao.EVENTO_CONCLUIDO) || inscricaoVelha
										.obterFase().obterCodigo().equals(
												Inscricao.APROVADA))
										&& inscricaoVelha.obterSituacao()
												.equals("Activa"))
									inscricaoVelha
											.atualizarSituacao("Encerrada");
							}
						}

						inscricao.atualizarNumeroResolucao(documento
								.obterNumero());
						inscricao.atualizarDataResolucao(documento
								.obterDataPrevistaInicio());
						inscricao.atualizarSituacao("Activa");
						inscricao.atualizarFase(Inspecao.EVENTO_CONCLUIDO);

						Entidade entidade = inscricao.obterOrigem();

						if (entidade instanceof Aseguradora
								|| entidade instanceof Reaseguradora
								|| entidade instanceof AuxiliarSeguro
								|| entidade instanceof Corretora
								|| entidade instanceof Reaseguradora)
							entidade.atualizarSigla(inscricao.obterInscricao());
					}

					else if (documento.obterSuperior() instanceof Inspecao) {
						Inspecao inspecao = (Inspecao) documento
								.obterSuperior();

						inspecao.atualizarFase(Inspecao.EVENTO_CONCLUIDO);
					}
				}

				if (documento.obterDocumento().obterApelido().equals(
						"resolucion")) {
					if (documento.obterSuperior() instanceof Plano) {
						Plano plano = (Plano) documento.obterSuperior();

						plano.atualizarResolucao(documento.obterNumero());
						plano.atualizarDataResolucao(documento
								.obterDataPrevistaInicio());
						plano.atualizarSituacao("Activo");
						plano.atualizarFase(Evento.EVENTO_CONCLUIDO);
					}
				}

				this.setResponseView(new PaginaInicialView(usuario, usuario));
			}

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(documento));
			mm.rollbackTransaction();
		}
	}

	public void imprimirDocumento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		DocumentoProduto documento = (DocumentoProduto) eventoHome
				.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {

			this.setResponseReport(new DocumentoReport(documento, action
					.getLocale()));

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(documento));
			mm.rollbackTransaction();
		}
	}

	public void rejeitarDocumentoProduto(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		DocumentoProduto documento = (DocumentoProduto) eventoHome
				.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {

			if (action.getBoolean("view")) {
				this.setResponseView(new RejeitarView(documento, action));
			} else {
				documento.adicionarComentario("Documento rejeitado por "
						+ usuario.obterNome(), action.getString("comentario"));
				documento.atualizarFase(DocumentoProduto.REJEITADA);
				documento.atualizarResponsavel(documento.obterCriador());

				if (documento.obterDocumento().obterApelido().equals(
						"resolucion")
						|| documento.obterDocumento().obterApelido().equals(
								"informeinscripcion")) {

					for (Iterator i = documento.obterInscricoesVinculadas()
							.iterator(); i.hasNext();) {
						Inscricao inscricao = (Inscricao) i.next();

						inscricao.atualizarFase(Inscricao.REJEITADA);
						inscricao
								.atualizarResponsavel(inscricao.obterCriador());
						inscricao.atualizarSituacao("Rejeitada");
						inscricao.atualizarFase(Inscricao.EVENTO_CONCLUIDO);

						Entidade entidade = inscricao.obterOrigem();

						Entidade.Atributo situacao = (Entidade.Atributo) entidade
								.obterAtributo("situacao");

						if (situacao != null)
							situacao.atualizarValor("No Activa");
					}

					if (documento.obterSuperior() instanceof Inscricao) {
						Inscricao inscricao = (Inscricao) documento
								.obterSuperior();

						inscricao.atualizarNumeroResolucao(documento
								.obterNumero());
						inscricao.atualizarDataResolucao(documento
								.obterDataPrevistaInicio());
						inscricao.atualizarSituacao("Rejeitada");
						inscricao.atualizarFase(Inscricao.EVENTO_CONCLUIDO);
					}
				}

				if (documento.obterDocumento().obterApelido().equals(
						"resolucion")) {
					if (documento.obterSuperior() instanceof Plano) {
						Plano plano = (Plano) documento.obterSuperior();

						plano.atualizarResolucao(documento.obterNumero());
						plano.atualizarDataResolucao(documento
								.obterDataPrevistaInicio());
						plano.atualizarSituacao("Rejeitado");
						plano.atualizarFase(Evento.EVENTO_CONCLUIDO);
					}
				}

				this.setResponseView(new PaginaInicialView(usuario, usuario));
			}

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(documento));
			mm.rollbackTransaction();
		}
	}

	public void vincularDocumento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		DocumentoProduto documento = (DocumentoProduto) eventoHome
				.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {
			for (Iterator i = action.getParameters().values().iterator(); i
					.hasNext();) {
				String key = (String) i.next();

				if (key.startsWith("check_")) {
					long id = Long.parseLong(key.substring(6, key.length()));

					Inscricao inscricao = (Inscricao) eventoHome
							.obterEventoPorId(id);

					documento.adicionarInscricaoVinculado(inscricao);

					inscricao.adicionarDocumentoVinculado(documento);
				}
			}

			mm.commitTransaction();
			this.setResponseView(new EventoView(documento));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(documento));
			mm.rollbackTransaction();
		}
	}

	public void excluirDocumentoVinculado(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		DocumentoProduto documento = (DocumentoProduto) eventoHome
				.obterEventoPorId(action.getLong("id"));
		Inscricao inscricao = (Inscricao) eventoHome.obterEventoPorId(action
				.getLong("inscricaoId"));

		mm.beginTransaction();
		try {
			documento.exclurInscricaoVinculada(inscricao);

			inscricao.exclurInscricaoVinculada(documento);

			mm.commitTransaction();
			this.setResponseView(new EventoView(documento));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(documento));
			mm.rollbackTransaction();
		}
	}

	public void incluirPessoaAntesDocumento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		DocumentoProduto documento = (DocumentoProduto) eventoHome
				.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {
			if (action.getLong("pessoaAntesId") == 0)
				throw new Exception("Escolha a persona");

			Entidade pessoa = entidadeHome.obterEntidadePorId(action
					.getLong("pessoaAntesId"));

			String tipo = "";

			if (action.getString("novoTipoPessoaAntes") != null
					&& !action.getString("novoTipoPessoaAntes").equals(""))
				tipo = action.getString("novoTipoPessoaAntes");
			else
				tipo = action.getString("tipoPessoaAntes");

			documento.adicionarNovaPessoa(pessoa, tipo, action
					.getInt("posicao"));

			mm.commitTransaction();
			this.setResponseView(new EventoView(documento));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(documento));
			mm.rollbackTransaction();
		}
	}

	public void incluirPessoaDepoisDocumento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		DocumentoProduto documento = (DocumentoProduto) eventoHome
				.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {
			if (action.getLong("pessoaDepoisId") == 0)
				throw new Exception("Escolha a persona");

			Entidade pessoa = entidadeHome.obterEntidadePorId(action
					.getLong("pessoaDepoisId"));

			String tipo = "";

			if (action.getString("novoTipoPessoaDepois") != null
					&& !action.getString("novoTipoPessoaDepois").equals(""))
				tipo = action.getString("novoTipoPessoaDepois");
			else
				tipo = action.getString("tipoPessoaDepois");

			documento.adicionarNovaPessoa(pessoa, tipo, action
					.getInt("posicao"));

			mm.commitTransaction();
			this.setResponseView(new EventoView(documento));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(documento));
			mm.rollbackTransaction();
		}
	}

	public void excluirPessoaDocumento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		DocumentoProduto documento = (DocumentoProduto) eventoHome
				.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {
			Entidade pessoa = entidadeHome.obterEntidadePorId(action
					.getLong("pessoaId2"));

			int posicao = action.getInt("posicao");

			documento.excluirPessoaDocumento(pessoa, posicao);

			mm.commitTransaction();
			this.setResponseView(new EventoView(documento));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(documento));
			mm.rollbackTransaction();
		}
	}
}