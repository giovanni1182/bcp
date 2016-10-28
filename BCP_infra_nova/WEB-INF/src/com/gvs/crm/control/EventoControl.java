package com.gvs.crm.control;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.model.AgendaProcesso;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.GrupoMensagem;
import com.gvs.crm.model.GrupoMensagem.Membro;
import com.gvs.crm.model.Inspecao;
import com.gvs.crm.model.ManutencaoSite;
import com.gvs.crm.model.Mensagem;
import com.gvs.crm.model.Ocorrencia;
import com.gvs.crm.model.Processo;
import com.gvs.crm.model.Raiz;
import com.gvs.crm.model.Reclamacao;
import com.gvs.crm.model.SampleModelManager;
import com.gvs.crm.model.UploadedFileHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.ComentarioView;
import com.gvs.crm.view.ConcluirView;
import com.gvs.crm.view.EncaminharView;
import com.gvs.crm.view.EntidadeView;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.MudancaFaseView;
import com.gvs.crm.view.NovoEventoView;
import com.gvs.crm.view.PaginaInicialView;
import com.gvs.crm.view.PopupEventosView;
import com.gvs.crm.view.ResponderView;
import com.gvs.crm.view.SelecaoSuperiorEventoView;

import infra.config.InfraProperties;
import infra.control.Action;
import infra.control.Control;

public class EventoControl extends Control {
	public void atualizarEvento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Evento evento = eventoHome.obterEventoPorId(action.getLong("id"));
		mm.beginTransaction();
		try {
			long origemId = action.getLong("origem");
			Entidade origem = null;
			if (origemId > 0)
				evento.atualizarOrigem(entidadeHome
						.obterEntidadePorId(origemId));

			long destinoId = action.getLong("destino");
			Entidade destino = null;
			if (destinoId > 0)
				evento.atualizarDestino(entidadeHome
						.obterEntidadePorId(destinoId));

			evento.atualizarPrioridade(action.getInt("prioridade"));
			evento.atualizarTitulo(action.getString("titulo"));
			evento.atualizarDescricao(action.getString("descricao"));
			evento.atualizarTipo(action.getString("tipo"));
			this.setResponseView(new PaginaInicialView(usuario, origemMenu));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(evento));
			mm.rollbackTransaction();
		}
	}

	public void atualizarSuperiorEvento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		Evento evento = eventoHome.obterEventoPorId(action.getLong("id"));
		long superiorId = action.getLong("superiorId");
		mm.beginTransaction();
		try {
			Evento superior = evento.obterSuperior();
			if (superiorId == 0) {
				evento.atualizarSuperior(null);
				UsuarioHome usuarioHome = (UsuarioHome) mm
						.getHome("UsuarioHome");
				Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this
						.getUser());
				this.setResponseView(new PaginaInicialView(usuarioAtual,
						origemMenu));
			} else {
				Evento novosuperior = eventoHome.obterEventoPorId(superiorId);
				evento.atualizarSuperior(novosuperior);
				this.setResponseView(new EventoView(novosuperior));
			}
			evento.calcularPrevisoes();
			if (superior != null)
				superior.calcularPrevisoes();
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(evento));
			mm.rollbackTransaction();
		}
	}

	public void comentarEvento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		Evento evento = eventoHome.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {
			if (action.getBoolean("view"))
				this.setResponseView(new ComentarioView(evento));
			else {
				evento.adicionarComentario("Comentario realizado por "
						+ usuario.obterNome(), action.getString("comentario"));

				this.setResponseView(new EventoView(evento));
			}

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(evento));
			mm.rollbackTransaction();
		}

	}

	public void concluirEvento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Evento evento = eventoHome.obterEventoPorId(action.getLong("id"));
		if (action.getBoolean("view")) {
			this.setResponseView(new ConcluirView(evento, origemMenu));
		} else {
			mm.beginTransaction();
			try {
				if ((evento instanceof ManutencaoSite
						|| evento instanceof DocumentoProduto
						|| evento instanceof Inspecao || evento instanceof Reclamacao)
						&& evento.obterCriador().obterId() != evento
								.obterResponsavel().obterId()) {
					evento.atualizarResponsavel(evento.obterCriador());
					evento.adicionarComentario("Concluido por "
							+ usuario.obterNome(), action
							.getString("comentario"));

					this
							.setResponseView(new PaginaInicialView(usuario,
									usuario));
				} else {

					if (evento instanceof Inspecao) {
						Inspecao inspecao = (Inspecao) evento;

						evento.adicionarComentario("Concluido por "
								+ usuario.obterNome(), action
								.getString("comentario"));

						if (inspecao.obterInspetor().obterId() == inspecao
								.obterResponsavel().obterId())
							evento.atualizarFase(Evento.EVENTO_CONCLUIDO);
						else
							evento.atualizarResponsavel(inspecao
									.obterInspetor());

						this.setResponseView(new EventoView(evento));
					} else {

						evento.concluir(action.getString("comentario"));

						if (evento instanceof Ocorrencia
								&& evento.obterSuperior() != null)
							this.setResponseView(new EventoView(evento
									.obterSuperior(), origemMenu));
						else
							this.setResponseView(new PaginaInicialView(usuario,
									origemMenu));
					}

				}

				mm.commitTransaction();

			}

			catch (Exception exception) {
				this.setAlert(Util.translateException(exception));
				this.setResponseView(new ConcluirView(evento, origemMenu));
				mm.rollbackTransaction();
			}
		}
	}

	public void devolverEvento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Evento evento = eventoHome.obterEventoPorId(action.getLong("id"));
		mm.beginTransaction();
		try {
			evento.atualizarResponsavel(usuarioAtual.obterSuperior());
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
		}
		this.setResponseView(new EventoView(evento));
	}

	public void encaminharEvento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Evento evento = eventoHome.obterEventoPorId(action.getLong("id"));
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		if (action.getBoolean("view")) {
			this.setResponseView(new EncaminharView(evento, origemMenu));
		} else {
			mm.beginTransaction();
			try {
				Entidade enti = null;
				if (action.getLong("responsavel") == 0) {
					throw new Exception("Elegiré el Nombre para Encaminar");
					//enti = (Entidade) usuario;
				} else
					enti = (Entidade) entidadeHome.obterEntidadePorId(action
							.getLong("responsavel"));

				evento.encaminhar(enti, action.getString("comentario"));
				this
						.setResponseView(new PaginaInicialView(usuario,
								origemMenu));
				mm.commitTransaction();
			} catch (Exception exception) {
				this.setAlert(Util.translateException(exception));
				this.setResponseView(new EncaminharView(evento, origemMenu));
				mm.rollbackTransaction();
			}
		}
	}

	public void excluirEvento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Evento evento = eventoHome.obterEventoPorId(action.getLong("id"));
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		if (origemMenu instanceof Raiz)
			origemMenu = usuario;
		mm.beginTransaction();
		try 
		{
			Evento superior = evento.obterSuperior();
			
			evento.excluir();
			
			if(action.getLong("entidadeId") > 0)
			{
				Entidade entidade = entidadeHome.obterEntidadePorId(action.getLong("entidadeId"));
				this.setResponseView(new EntidadeView(entidade));
			}
			else if (superior == null) 
			{
				this.setResponseView(new PaginaInicialView(usuario,	origemMenu));
			} 
			else 
				this.setResponseView(new EventoView(superior, origemMenu));
			
			mm.commitTransaction();
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(evento, origemMenu));
			mm.rollbackTransaction();
		}
	}

	public void incluirEvento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		mm.beginTransaction();
		Evento evento = (Evento) mm.getEntity(action.getString("classe"));
		try {
			long origemId = action.getLong("origem");
			Entidade origem = null;
			if (origemId > 0)
				origem = entidadeHome.obterEntidadePorId(origemId);

			long destinoId = action.getLong("destino");
			Entidade destino = null;
			if (destinoId > 0)
				destino = entidadeHome.obterEntidadePorId(destinoId);

			Entidade responsavel = usuarioAtual;
			long responsavelId = action.getLong("responsavel");
			if (responsavelId > 0)
				responsavel = entidadeHome.obterEntidadePorId(responsavelId);

			String membrosSemEmail = null;

			if (evento instanceof Mensagem)
			{
				if (responsavel instanceof GrupoMensagem)
				{
					GrupoMensagem grupo = (GrupoMensagem) responsavel;
					for (Iterator i = grupo.obterMembros().values().iterator(); i.hasNext();) 
					{
						Membro membro = (Membro) i.next();
						evento.atribuirOrigem(origem);
						evento.atribuirDestino(destino);
						evento.atribuirResponsavel((Usuario) membro
								.obterMembro());
						evento.atribuirPrioridade(action.getInt("prioridade"));
						evento.atribuirTitulo(action.getString("titulo"));
						evento.atribuirDescricao(action.getString("descricao"));
						evento.atribuirTipo(action.getString("tipo"));
						evento.incluir();

						if (action.getInputStream("file") != null) {
							SampleModelManager sm = new SampleModelManager();
							UploadedFileHome home = (UploadedFileHome) sm
									.getHome("UploadedFileHome");
							InputStream content = action.getInputStream("file");
							String fileName = action.getString("file_name");
							String fileType = action
									.getString("file_content_type");
							long fileSize = action.getLong("file_size");
							home.addUploadedFile(evento, content, fileName,
									fileType, fileSize, 0);
						}

						if (action.getBoolean("email")) {
							String email = null;

							email = usuarioAtual.obterAtributo("email")
									.obterValor();

							if (email.length() == 0)
								throw new Exception(
										"Não foi possível enviar email(s), cadastre o seu email e senha");

							/*if (membro.obterEmail().length() > 0)
							{
								MailManager mail = new MailManager(usuarioAtual);
								MailServer server = new MailServer();

								mail.send(server, membro.obterEmail(), "", "",	usuarioAtual.obterAtributo("email").obterValor(), "Mensagem",action.getString("descricao"));
							}
							else
							{
								if (membrosSemEmail == null)
									membrosSemEmail = membro.obterMembro().obterNome();
								else
									membrosSemEmail += ", "	+ membro.obterMembro().obterNome();
							}*/
						}
					}
				} else {

					if (action.getBoolean("email")) {
						String email = null;

						email = usuarioAtual.obterAtributo("email")
								.obterValor();

						if (email.length() == 0)
							throw new Exception(
									"Não foi possível enviar email(s), cadastre o seu email e senha");

						/*if (responsavel.obterAtributo("email").obterValor().length() > 0)
						{
							MailManager mail = new MailManager(usuarioAtual);
							MailServer server = new MailServer();

							mail.send(server, responsavel
									.obterAtributo("email").obterValor(), "",
									"", email, "Mensagem", action
											.getString("descricao"));
						} 
						else
						{
							if (membrosSemEmail == null)
								membrosSemEmail = responsavel.obterNome();
							else
								membrosSemEmail += ", "
										+ responsavel.obterNome();
						}*/
					}

					evento.atribuirOrigem(origem);
					evento.atribuirDestino(destino);
					evento.atribuirResponsavel(responsavel);
					evento.atribuirPrioridade(action.getInt("prioridade"));
					evento.atribuirTitulo(action.getString("titulo"));
					evento.atribuirDescricao(action.getString("descricao"));
					evento.atribuirTipo(action.getString("tipo"));
					evento.incluir();

					if (action.getInputStream("file") != null) {
						SampleModelManager sm = new SampleModelManager();
						UploadedFileHome home = (UploadedFileHome) sm
								.getHome("UploadedFileHome");
						InputStream content = action.getInputStream("file");
						String fileName = action.getString("file_name");
						String fileType = action.getString("file_content_type");
						long fileSize = action.getLong("file_size");
						home.addUploadedFile(evento, content, fileName,
								fileType, fileSize, 0);
					}
				}
			}
			else
			{
				evento.atribuirOrigem(origem);
				evento.atribuirDestino(destino);
				evento.atribuirResponsavel(responsavel);
				evento.atribuirPrioridade(action.getInt("prioridade"));
				evento.atribuirTitulo(action.getString("titulo"));
				evento.atribuirDescricao(action.getString("descricao"));
				evento.atribuirTipo(action.getString("tipo"));
				evento.incluir();
			}

			if (action.getString("responseView").equals("EventoView"))
				this.setResponseView(new EventoView(evento, origemMenu));
			else
				this.setResponseView(new PaginaInicialView(usuarioAtual,
						origemMenu));

			if (membrosSemEmail != null)
				this.setAlert("Não foi possivel enviar Email para "
						+ membrosSemEmail);

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(evento));
			mm.rollbackTransaction();
		}
	}

	public void novoEvento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		Date data = action.getDate("data");
		String titulo = action.getString("titulo");
		long origemId = action.getLong("origemId");
		Entidade origem = null;
		if (origemId > 0)
			origem = entidadeHome.obterEntidadePorId(origemId);

		long superiorId = action.getLong("superiorId");
		Evento superior = null;
		if (superiorId > 0)
			superior = eventoHome.obterEventoPorId(superiorId);
		String classe = action.getString("classe");
		switch (action.getInt("passo")) {
		case 1:
			// obter superior do evento
			this.setResponseView(new NovoEventoView(1, null, data, origem,
					origemMenu));
			break;
		case 2:
			// obter o tipo do evento
			//            this.setResponseView(new NovoEventoView(2, superior, data,
			// origem, origemMenu));
			this.setResponseView(new NovoEventoView(2, null, data, origem,
					origemMenu));
			break;
		case 3:
			this.setResponseView(new NovoEventoView(2, superior, data, origem,
					origemMenu));
			break;
		default:
			// mostrar formulário de inclusão do evento
			UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
			Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this
					.getUser());
			Evento evento = (Evento) mm.getEntity(classe);
			evento.atribuirSuperior(superior);
			evento.atribuirOrigem(origem);
			evento.atribuirDataPrevistaInicio(data);
			evento.atribuirResponsavel(usuarioAtual);
			evento.atribuirTitulo(titulo);
			this.setResponseView(new EventoView(evento, origemMenu));
			break;
		}
	}

	public void proximaFase(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Evento evento = (Evento) eventoHome.obterEventoPorId(action
				.getLong("id"));
		mm.beginTransaction();
		try {
			if (action.getBoolean("view"))
				this.setResponseView(new MudancaFaseView(evento, action, "",true, false));
			else
			{
				if (evento instanceof Processo)
				{
					Processo processo = (Processo) evento;

					processo.atualizarTitulo("Processo - " + processo.obterExpediente());

					if (action.getDate("data") != null)
					{
						for (Iterator i = processo.obterInferiores().iterator(); i.hasNext();)
						{
							Evento e = (Evento) i.next();

							if (e instanceof AgendaProcesso)
								if (e.obterFase().obterCodigo().equals(Evento.EVENTO_PENDENTE))
									e.atualizarFase(Evento.EVENTO_CONCLUIDO);
						}

						AgendaProcesso agenda = (AgendaProcesso) mm.getEntity("AgendaProcesso");

						Date dataPrevista = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(action.getString("data")	+ " " + action.getString("hora"));

						agenda.atribuirOrigem(processo.obterOrigem());
						agenda.atribuirDestino(processo.obterDestino());

						agenda.atribuirResponsavel(processo.obterResponsavel());
						agenda.atribuirTitulo("Agenda Processo");
						agenda.atribuirSuperior(processo);
						agenda.atribuirDataPrevistaInicio(dataPrevista);
						agenda.incluir();

						agenda.adicionarComentario("Comentário feito por "	+ usuarioAtual.obterNome(), action.getString("comentario"));
					}

					processo.atualizarFase(action.getString("fase"));

					processo.adicionarComentario("Avanço para fase " + eventoHome.obterNomeFase(action.getString("fase")) + " feito por " + usuarioAtual.obterNome(), action.getString("comentario"));

					if (action.getString("fase").equals("superintendente"))
					{
						Entidade superIntendente = entidadeHome.obterEntidadePorApelido("secretaria");
						evento.atualizarResponsavel(superIntendente);
					}
				}
				else if (evento instanceof DocumentoProduto	|| evento instanceof Inspecao || evento instanceof Reclamacao)
				{
					if (action.getString("fase").equals("superintendente"))
					{
						Entidade superIntendente = entidadeHome.obterEntidadePorApelido("secretaria");
						evento.atualizarResponsavel(superIntendente);
					}
					else
					{
						for (Iterator i = evento.obterResponsavel().obterSuperiores().iterator(); i.hasNext();)
						{
							Entidade entidade = (Entidade) i.next();

							if (entidade.obterClasse().toLowerCase().equals("departamento"))
							{
								if (action.getString("fase").equals("analista"))
								{				
									if (entidade.obterApelido().startsWith("analista"))
										evento.atualizarResponsavel(entidade);
								}
								if (action.getString("fase").equals("chefedivisao"))
								{
									if (entidade.obterApelido().startsWith("jefe"))
										evento.atualizarResponsavel(entidade);
								} 
								else if (action.getString("fase").equals("intendente"))
								{
									if (entidade.obterApelido().startsWith("intendente"))
										evento.atualizarResponsavel(entidade);
								}
							}
						}
					}
					evento.atualizarFase(action.getString("fase"));
					evento.adicionarComentario("Avanço para fase " + eventoHome.obterNomeFase(action.getString("fase"))	+ " feito por " + usuarioAtual.obterNome(), action.getString("comentario"));
				}
				else
				{
					evento.adicionarComentario("Avanço para fase "	+ eventoHome.obterNomeFase(action.getString("fase")) + " feito por " + usuarioAtual.obterNome(), action.getString("comentario"));
					evento.atualizarFase(action.getString("fase"));
				}

				//evento.atualizarTitulo(evento.obterClasseDescricao() + " / "
				// + evento.obterFase().obterNome());

				this.setResponseView(new EventoView(evento));
			}

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));

			if (action.getBoolean("cancelar"))
				this.setResponseView(new MudancaFaseView(evento, action,
						"Cancelar", true, true));
			else {
				/*
				 * String proximaFase =
				 * InfraProperties.getInstance().getProperty(evento.obterClasse() +
				 * "." + evento.obterFase().obterCodigo() + ".proximafase");
				 * 
				 * String nomeFase =
				 * InfraProperties.getInstance().getProperty("fase."+proximaFase+".nome");
				 */

				this.setResponseView(new MudancaFaseView(evento, action, "",
						true, false));
			}
			mm.rollbackTransaction();
		}
	}

	public void faseAnterior(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Evento evento = (Evento) eventoHome.obterEventoPorId(action
				.getLong("id"));
		mm.beginTransaction();
		try {
			/*
			 * String faseAnterior =
			 * InfraProperties.getInstance().getProperty(evento.obterClasse() +
			 * "." + evento.obterFase().obterCodigo() + ".faseanterior");
			 * 
			 * String nomeFase =
			 * InfraProperties.getInstance().getProperty("fase."+faseAnterior+".nome");
			 */

			if (action.getBoolean("view"))
				this.setResponseView(new MudancaFaseView(evento, action, "",
						false, false));
			else {
				evento.adicionarComentario("Volta para fase "
						+ action.getString("fase") + " feita por "
						+ usuarioAtual.obterNome(), action
						.getString("comentario"));

				evento.atualizarFase(action.getString("fase"));

				if (evento instanceof Processo) {
					Processo processo = (Processo) evento;

					processo.atualizarTitulo("Processo - "
							+ processo.obterExpediente());

					if (action.getDate("data") != null) {
						for (Iterator i = processo.obterInferiores().iterator(); i
								.hasNext();) {
							Evento e = (Evento) i.next();

							if (e instanceof AgendaProcesso)
								if (e.obterFase().obterCodigo().equals(
										Evento.EVENTO_PENDENTE))
									e.atualizarFase(Evento.EVENTO_CONCLUIDO);
						}

						AgendaProcesso agenda = (AgendaProcesso) mm
								.getEntity("AgendaProcesso");

						Date dataPrevista = new SimpleDateFormat(
								"dd/MM/yyyy HH:mm").parse(action
								.getString("data")
								+ " " + action.getString("hora"));

						agenda.atribuirOrigem(processo.obterOrigem());
						agenda.atribuirDestino(processo.obterDestino());

						agenda.atribuirResponsavel(processo.obterResponsavel());
						agenda.atribuirTitulo("Agenda Processo");
						agenda.atribuirSuperior(processo);
						agenda.atribuirDataPrevistaInicio(dataPrevista);
						agenda.incluir();

						agenda.adicionarComentario("Comentário feito por "
								+ usuarioAtual.obterNome(), action
								.getString("comentario"));
					}

					processo.atualizarFase(action.getString("fase"));

					processo.adicionarComentario("Avanço para fase "
							+ eventoHome
									.obterNomeFase(action.getString("fase"))
							+ " feito por " + usuarioAtual.obterNome(), action
							.getString("comentario"));
				} else if (evento instanceof DocumentoProduto
						|| evento instanceof Inspecao
						|| evento instanceof Reclamacao
						|| evento instanceof Processo) {
					if (action.getString("fase").equals("superintendente")) {
						Entidade superIntendente = entidadeHome
								.obterEntidadePorApelido("superintendente");
						evento.atualizarResponsavel(superIntendente);

					} else {
						for (Iterator i = evento.obterResponsavel()
								.obterSuperiores().iterator(); i.hasNext();) {
							Entidade entidade = (Entidade) i.next();

							if (entidade.obterClasse().toLowerCase().equals(
									"departamento")) {
								if (action.getString("fase").equals("analista")) {
									if (entidade.obterApelido().startsWith(
											"analista"))
										evento.atualizarResponsavel(entidade);
								}

								if (action.getString("fase").equals(
										"chefedivisao")) {
									if (entidade.obterApelido().startsWith(
											"jefe"))
										evento.atualizarResponsavel(entidade);
								} else if (action.getString("fase").equals(
										"intendente")) {
									if (entidade.obterApelido().startsWith(
											"intendente"))
										evento.atualizarResponsavel(entidade);
								}
							}
						}
					}

					evento.adicionarComentario("Avanço para fase "
							+ action.getString("fase") + " feito por "
							+ usuarioAtual.obterNome(), action
							.getString("comentario"));

				} else {
					evento.adicionarComentario("Avanço para fase "
							+ action.getString("fase") + " feito por "
							+ usuarioAtual.obterNome(), action
							.getString("comentario"));

					evento.atualizarFase(action.getString("fase"));
				}

				this.setResponseView(new EventoView(evento));
			}

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));

			if (action.getBoolean("cancelar"))
				this.setResponseView(new MudancaFaseView(evento, action,
						"Cancelar", true, true));
			else {
				String proximaFase = InfraProperties.getInstance().getProperty(
						evento.obterClasse() + "."
								+ evento.obterFase().obterCodigo()
								+ ".proximafase");

				String nomeFase = InfraProperties.getInstance().getProperty(
						"fase." + proximaFase + ".nome");

				this.setResponseView(new MudancaFaseView(evento, action,
						nomeFase, false, false));
			}
			mm.rollbackTransaction();
		}
	}

	public void pegarEvento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Evento evento = eventoHome.obterEventoPorId(action.getLong("id"));
		mm.beginTransaction();
		try {
			evento.atualizarResponsavel(usuarioAtual);
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
		}
		this.setResponseView(new EventoView(evento));
	}

	public void responderEvento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Evento evento = eventoHome.obterEventoPorId(action.getLong("id"));
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		if (action.getBoolean("view")) {
			this.setResponseView(new ResponderView(evento, origemMenu));
		} else {
			mm.beginTransaction();
			try {
				evento.responder(action.getString("comentario"));
				this
						.setResponseView(new PaginaInicialView(usuario,
								origemMenu));
				mm.commitTransaction();
			} catch (Exception exception) {
				this.setAlert(Util.translateException(exception));
				this.setResponseView(new ResponderView(evento, origemMenu));
				mm.rollbackTransaction();
			}
		}
	}

	public void selecionarEventoSuperior(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Evento evento = eventoHome.obterEventoPorId(action.getLong("id"));
		this.setResponseView(new SelecaoSuperiorEventoView(evento));
	}

	public void visualizarEvento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action.getLong("origemMenuId"));
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Evento evento = eventoHome.obterEventoPorId(action.getLong("id"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		if(evento.obterResponsavel()!=null)
		{
			if (usuario.obterId() == evento.obterResponsavel().obterId()) 
			{
				mm.beginTransaction();
				try
				{
					evento.atualizarComoLido();
					mm.commitTransaction();
				}
				catch (Exception exception)
				{
					this.setAlert(Util.translateException(exception));
					mm.rollbackTransaction();
				}
			}
		}
		
		if(evento instanceof Apolice)
		{
			if(!action.getParameters().containsKey("_pastaApolice"))
				action.add("_pastaApolice", 0);
		}
		
		this.setResponseView(new EventoView(evento, origemMenu));
	}

	public void ordenarEvento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Evento evento = eventoHome.obterEventoPorId(action.getLong("id"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		if (usuario.obterId() == evento.obterResponsavel().obterId()) {
			mm.beginTransaction();
			try {
				Evento eventoOrdena = eventoHome.obterEventoPorId(action
						.getLong("eventoOrdenaId"));
				Collection classes = new ArrayList();
				classes.add("Item");
				classes.add("ItemProduto");
				classes.add("ItemStella");
				evento.atribuirClassesParaOrdenar(classes);
				evento.ordenar();
				if (action.getBoolean("ordenaParaBaixo"))
					evento.ordenarParaBaixo(eventoOrdena.obterOrdem());
				else if (action.getBoolean("ordenaParaCima"))
					evento.ordenarParaCima(eventoOrdena.obterOrdem());
				mm.commitTransaction();
			} catch (Exception exception) {
				this.setAlert(Util.translateException(exception));
				mm.rollbackTransaction();
			}
		}
		this.setResponseView(new EventoView(evento));
	}

	public void popupEventos(Action action) throws Exception {
		String campoEventoId = action.getString("campoEventoId");
		String campoEventoTitulo = action.getString("campoEventoTitulo");
		String tipo = action.getString("tipo");
		String nome = action.getString("nome");
		String aseguradora = action.getString("aseguradora");
		String secao = action.getString("secao");

		this.setResponseView(new PopupEventosView(campoEventoId,
				campoEventoTitulo, tipo, nome, aseguradora, secao));
	}
}