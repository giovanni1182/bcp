package com.gvs.crm.control;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.email.MailManager;
import com.gvs.crm.email.MailServer;
import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Notificacao;
import com.gvs.crm.model.Parametro;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.model.Uteis;
import com.gvs.crm.report.AgendaPDF;
import com.gvs.crm.view.AgendarDesagendarView;
import com.gvs.crm.view.ComentarioView;
import com.gvs.crm.view.ControleAgendasView;
import com.gvs.crm.view.EnviarBcpView;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class AgendaMovimentacaoControl extends Control
{
	public void verificarArquivo(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		//Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action.getLong("origemMenuId"));
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		AgendaMovimentacao agendaMovimentacao = (AgendaMovimentacao) eventoHome.obterEventoPorId(action.getLong("id"));
		Collection<String> validacaoErros = new ArrayList<>();

		mm.beginTransaction();
		try 
		{
			Uteis uteis = new Uteis();
			uteis.copiarArquivos();
			
			String ano2 = new Integer(agendaMovimentacao.obterAnoMovimento())
					.toString();
			String mes2 = new Integer(agendaMovimentacao.obterMesMovimento())
					.toString();

			if (mes2.length() == 1)
				mes2 = "0" + mes2;

			String sigla = agendaMovimentacao.obterOrigem().obterSigla();

			if (action.getString("tipo") != null)
				agendaMovimentacao.atualizarTipo(action.getString("tipo"));

			/*if (agendaMovimentacao.obterTipo().equals("Instrumento"))
				validacaoErros = agendaMovimentacao.verificarApolice(sigla.trim()+ ano2 + mes2);
			else*/
				validacaoErros = agendaMovimentacao.validaArquivo(sigla.trim()+ ano2 + mes2);

			if (validacaoErros.size() > 0) 
			{
				Entidade destino = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("planodecontas");
				String msgErros = "";

				System.out.println("Erros: " + validacaoErros.size());

				for (String msgAux : validacaoErros)
					msgErros += msgAux + "\n";

				System.out.println("Erros2: ");

				Parametro parametro = (Parametro) entidadeHome.obterEntidadePorApelido("parametros");
				String descricao = parametro.obterAtributo("notificacaocritica").obterValor();

				Notificacao notificacao = (Notificacao) mm.getEntity("Notificacao");
				notificacao.atribuirSuperior(agendaMovimentacao);
				notificacao.atribuirOrigem(agendaMovimentacao.obterOrigem());
				notificacao.atribuirDestino(destino);
				notificacao.atribuirResponsavel(agendaMovimentacao.obterResponsavel());
				notificacao.atribuirTipo("Notificación de Error de Validación");
				notificacao.atribuirTitulo("Notificación de Error de Validación");
				notificacao.atribuirDescricao(descricao + "\n" + msgErros);
				notificacao.incluir();

				for (Evento e : agendaMovimentacao.obterInferiores())
				{
					if (e instanceof Notificacao && e.obterId() != notificacao.obterId())
					{
						if (e.obterTipo().equals("Notificación de Error de Validación"))
							e.atualizarFase(Evento.EVENTO_CONCLUIDO);
					}
				}

				agendaMovimentacao.atualizarFase(Evento.EVENTO_PENDENTE);

				this.setResponseView(new EventoView(notificacao, origemMenu));

			} 
			else
			{

				Parametro parametro = (Parametro) entidadeHome.obterEntidadePorApelido("parametros");
				String descricao = parametro.obterAtributo("notificacaorecebimento").obterValor();

				Entidade destino = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("planodecontas");
				Notificacao notificacao = (Notificacao) mm.getEntity("Notificacao");
				notificacao.atribuirSuperior(agendaMovimentacao);
				notificacao.atribuirOrigem(agendaMovimentacao.obterOrigem());
				notificacao.atribuirDestino(destino);
				notificacao.atribuirResponsavel(agendaMovimentacao.obterResponsavel());
				notificacao.atribuirTipo("Notificación de Recibimiento");
				notificacao.atribuirTitulo("Notificación de Recibimiento");
				notificacao.atribuirDescricao(descricao);
				notificacao.incluir();

				for (Evento e : agendaMovimentacao.obterInferiores())
					e.atualizarFase(Evento.EVENTO_CONCLUIDO);

				agendaMovimentacao.atualizarFase(Evento.EVENTO_CONCLUIDO);
				
				agendaMovimentacao.atualizaUltimaAgenda("Contabil");

				AgendaMovimentacao novaAgenda = (AgendaMovimentacao) mm.getEntity("AgendaMovimentacao");
				novaAgenda.atribuirOrigem(agendaMovimentacao.obterOrigem());
				novaAgenda.atribuirDestino(agendaMovimentacao.obterDestino());
				novaAgenda.atribuirResponsavel(agendaMovimentacao
						.obterResponsavel());
				novaAgenda.atribuirTitulo(" (Consistencia del archivo)");

				Calendar mesSeguinte = Calendar.getInstance();
				mesSeguinte.setTime(new Date());
				mesSeguinte.add(Calendar.MONTH, 1);

				String dia = parametro.obterAtributo("diaagenda").obterValor();

				String mes = new SimpleDateFormat("MM").format(mesSeguinte.getTime());
				String ano = new SimpleDateFormat("yyyy").format(mesSeguinte.getTime());

				String data = dia + "/" + mes + "/" + ano;

				Date dataModificada = new SimpleDateFormat("dd/MM/yyyy").parse(data);

				int mesMovimento = agendaMovimentacao.obterMesMovimento() + 1;
				int anoMovimento = agendaMovimentacao.obterAnoMovimento();

				if (mesMovimento > 12)
				{
					mesMovimento = 1;
					anoMovimento += 1;
				}

				novaAgenda.atribuirDataPrevistaInicio(dataModificada);
				novaAgenda.atribuirTipo(agendaMovimentacao.obterTipo());
				novaAgenda.atribuirMesMovimento(mesMovimento);
				novaAgenda.atribuirAnoMovimento(anoMovimento);
				novaAgenda.incluir();

				String mesModificado = "";

				if (new Integer(mesMovimento).toString().length() == 1)
					mesModificado = "0" + mesMovimento;
				else
					mesModificado = new Integer(mesMovimento).toString();

				novaAgenda.atualizarTitulo(mesModificado + " - " + anoMovimento	+ " (Consistencia del archivo)");

				this.setResponseView(new EventoView(notificacao, origemMenu));
			}

			mm.commitTransaction();
		} 
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(agendaMovimentacao,	origemMenu));
			mm.rollbackTransaction();
		}
	}

	public void incluirComentario(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		AgendaMovimentacao agendaMovimentacao = (AgendaMovimentacao) eventoHome
				.obterEventoPorId(action.getLong("id"));
		if (action.getBoolean("view")) {
			this.setResponseView(new ComentarioView(agendaMovimentacao,
					origemMenu));
		} else {
			mm.beginTransaction();
			try {
				agendaMovimentacao.adicionarComentario(
						"Comentario efectuado por " + usuario.obterNome(),
						action.getString("comentario"));
				this.setResponseView(new EventoView(agendaMovimentacao,
						origemMenu));
				mm.commitTransaction();
			} catch (Exception exception) {
				this.setAlert(Util.translateException(exception));
				this.setResponseView(new ComentarioView(agendaMovimentacao,
						origemMenu));
				mm.rollbackTransaction();
			}
		}
	}

	public void enviarBcp(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		AgendaMovimentacao agendaMovimentacao = (AgendaMovimentacao) eventoHome
				.obterEventoPorId(action.getLong("id"));
		if (action.getBoolean("view")) {
			this.setResponseView(new EnviarBcpView(agendaMovimentacao,
					origemMenu));
		} else {
			mm.beginTransaction();
			try {
				agendaMovimentacao.enviarBcp(action.getString("comentario"));
				this
						.setResponseView(new PaginaInicialView(usuario,
								origemMenu));
				mm.commitTransaction();
			} catch (Exception exception) {
				this.setAlert(Util.translateException(exception));
				this.setResponseView(new EnviarBcpView(agendaMovimentacao,
						origemMenu));
				mm.rollbackTransaction();
			}
		}
	}

	public void verificarAgendas(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		mm.beginTransaction();
		try {

			Parametro parametro = (Parametro) entidadeHome
					.obterEntidadePorApelido("parametros");

			for (Iterator i = usuario.verificarAgendas().iterator(); i
					.hasNext();) {
				AgendaMovimentacao ag = (AgendaMovimentacao) i.next();

				if (new Date().after(ag.obterDataPrevistaInicio())) {

					Calendar dia = Calendar.getInstance();
					dia.setTime(ag.obterDataPrevistaInicio());
					dia.add(Calendar.DAY_OF_MONTH, Integer.parseInt(parametro
							.obterAtributo("diaatraso").obterValor()));

					System.out.println("Dia Agenda: "
							+ new SimpleDateFormat("dd/MM/yyy").format(ag
									.obterDataPrevistaInicio()));
					System.out.println("DiaGetDay : "
							+ ag.obterDataPrevistaInicio().getDay());

					if (ag.obterDataPrevistaInicio().getDay() != 0
							&& ag.obterDataPrevistaInicio().getDay() != 6
							&& !parametro.obterFeriados().contains(
									ag.obterDataPrevistaInicio())) {
						if (dia.getTime().getDay() != 0
								&& dia.getTime().getDay() != 6
								&& !parametro.obterFeriados().contains(
										dia.getTime())) {
							if ((new Date().getDate() - dia.getTime().getDate()) > Integer
									.parseInt(parametro.obterAtributo(
											"diaatraso").obterValor()))
								this.enviarEmail(ag, action);
						}
					} else {
						boolean diaUtil = false;

						while (!diaUtil) {
							dia.add(Calendar.DAY_OF_MONTH, 1);
							if (dia.getTime().getDay() != 0
									&& dia.getTime().getDay() != 6
									&& !parametro.obterFeriados().contains(
											dia.getTime()))
								diaUtil = true;
						}

						if ((new Date().getDate() - dia.getTime().getDate()) > Integer
								.parseInt(parametro.obterAtributo("diaatraso")
										.obterValor()))
							this.enviarEmail(ag, action);
					}
				}
			}
			this.setResponseView(new PaginaInicialView(usuario, origemMenu));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuario, origemMenu));
			mm.rollbackTransaction();
		}
	}

	private void enviarEmail(AgendaMovimentacao agenda, Action action)
			throws Exception {

		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		mm.beginTransaction();
		try {
			Notificacao notificacao = (Notificacao) mm.getEntity("Notificacao");

			notificacao.atribuirSuperior(agenda);
			notificacao.atribuirOrigem(agenda.obterOrigem());
			notificacao.atribuirDestino(agenda.obterDestino());
			notificacao.atribuirResponsavel(agenda.obterResponsavel());
			notificacao.atribuirTipo("Notificación de Atraso");
			notificacao.atribuirTitulo("Notificación de Atraso");

			Parametro parametro = (Parametro) entidadeHome
					.obterEntidadePorApelido("parametros");

			String descricao = parametro.obterAtributo("notificacaoatraso")
					.obterValor();

			/*
			 * StringTokenizer st = new
			 * StringTokenizer(InfraProperties.getInstance().getProperty("ocorrencia.notificacaoatraso"),
			 * ","); String descricao = ""; while(st.hasMoreTokens()) { Date
			 * dataAtual = new Date(); descricao = st.nextToken(); descricao += " " +
			 * ag.obterOrigem().obterNome(); descricao += " " + st.nextToken(); }
			 */

			notificacao.atribuirDescricao(descricao);
			notificacao.incluir();

			MailManager mail = new MailManager();
			MailServer server = new MailServer();

			mail.send(server, "giovanni@horst.com.br", "", "",
					"giovanni@horst.com.br", "Notificación de Atraso",
					descricao);

			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuario, origemMenu));
			mm.rollbackTransaction();
		}
	}

	public void incluirAgendaMovimentacao(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		//UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		//Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action.getLong("origemMenuId"));
		AgendaMovimentacao agendaMovimentacao = (AgendaMovimentacao) mm.getEntity("AgendaMovimentacao");
		mm.beginTransaction();
		try 
		{
			long origemId = action.getLong("origemId");
			long responsavelId = action.getLong("responsavelId");

			if (origemId == 0)
				throw new Exception("A Seguradora deve ser selecionada");

			Entidade origem = entidadeHome.obterEntidadePorId(origemId);
			agendaMovimentacao.atribuirOrigem(origem);

			if (agendaMovimentacao.existeAgendaNoPeriodo(action.getInt("mesmovimento"), action.getInt("anomovimento"),origem, action.getString("tipo")))
				throw new Exception("Já existe uma agenda cadastrada no mês "+ action.getInt("mesmovimento") + " e ano "+ action.getInt("anomovimento")	+ " para a Aseguradora " + origem.obterNome());

			if (origem == null)
				throw new Exception("A Seguradora não foi encontrada");

			if (responsavelId == 0)
				throw new Exception("A Responsavel deve ser selecionado");

			Entidade responsavel = entidadeHome
					.obterEntidadePorId(responsavelId);
			agendaMovimentacao.atribuirResponsavel(responsavel);

			agendaMovimentacao.atribuirDescricao(action.getString("descricao"));

			if (action.getDate("data") == null)
				throw new Exception("Escolha a data do Compromisso");
			
			Aseguradora aseg = (Aseguradora) origem;
			
			/*String mensagem = "";
			
			for(Iterator i = aseg.obterAgendas().iterator() ; i.hasNext() ; )
			{
				AgendaMovimentacao agenda = (AgendaMovimentacao) i.next();
				
				if(agenda.obterTipo()!=null && !agenda.obterTipo().equals(""))
				{
					if(agenda.obterTipo().equals(action.getString("tipo")))
					{
						if(agenda.obterFase().obterCodigo().equals(AgendaMovimentacao.EVENTO_PENDENTE))
							mensagem += "Agenda " + agenda.obterMesMovimento() + " - " + agenda.obterAnoMovimento() + ", ";
					}
				}
			}
			
			if(mensagem.length() > 0)
				throw new Exception(mensagem + " pendiente(s)");*/
			
			AgendaMovimentacao agendaPendente = aseg.obterAgendaPendente(action.getString("tipo"));
			if(agendaPendente!=null)
				throw new Exception("Agenda " + agendaPendente.obterMesMovimento() + " - " + agendaPendente.obterAnoMovimento() + " pendiente");

			long superiorId = action.getLong("superiorId");
			Evento superior = null;
			
			if (superiorId > 0) 
			{
				EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
				superior = eventoHome.obterEventoPorId(superiorId);
			}

			Entidade destino = (Entidade) entidadeHome.obterEntidadePorApelido("SuperSeg");

			agendaMovimentacao.atribuirSuperior(superior);
			agendaMovimentacao.atribuirOrigem(origem);
			agendaMovimentacao.atribuirDestino(destino);
			agendaMovimentacao.atribuirTitulo(action.getString("titulo"));
			agendaMovimentacao.atribuirTipo(action.getString("tipo"));
			agendaMovimentacao.atribuirDataPrevistaInicio(action.getDate("data"));
			agendaMovimentacao.atribuirDescricao(action.getString("descricao"));
			agendaMovimentacao.atribuirAnoMovimento(action.getInt("anomovimento"));
			agendaMovimentacao.atribuirMesMovimento(action.getInt("mesmovimento"));
			agendaMovimentacao.incluir();
			
			if(action.getString("especial").equals(""))
				agendaMovimentacao.atualizarEspecial("Não");
			else
				agendaMovimentacao.atualizarEspecial(action.getString("especial"));
			
			if(action.getString("inscricaoEspecial").equals(""))
				agendaMovimentacao.atualizarInscricaoEspecial("Não");
			else
				agendaMovimentacao.atualizarInscricaoEspecial(action.getString("inscricaoEspecial"));
			
			agendaMovimentacao.atualizarSuplementosEspecial(action.getString("endosoEspecial"));
			
			agendaMovimentacao.atualizarCapitalEspecial(action.getString("capitalEspecial"));
			
			agendaMovimentacao.atualizarDataEspecial(action.getString("dataEspecial"));
			
			//agendaMovimentacao.atualizarDocumentoEspecial(action.getString("documentoEspecial"));
			agendaMovimentacao.atualizarDocumentoEspecial("Não");
			
			agendaMovimentacao.atualizarApAnteriorEspecial(action.getString("apAnteriorEspecial"));
			
			agendaMovimentacao.atualizarEndosoApolice(action.getString("endosoApolice"));
			
			agendaMovimentacao.atualizarValidacao(action.getString("validacao"));

			this.setResponseView(new EventoView(agendaMovimentacao));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this
					.setResponseView(new EventoView(agendaMovimentacao,
							origemMenu));
			mm.rollbackTransaction();
		}
	}

	public void agendarMovimentacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		AgendaMovimentacao agendaMovimentacao = (AgendaMovimentacao) eventoHome.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try 
		{
			/*boolean email = false;
			
			for(Iterator i = agendaMovimentacao.obterOrigem().obterContatos().iterator() ; i.hasNext() ; )
			{
				Entidade.Contato contato = (Entidade.Contato) i.next();
				
				if(contato.obterNome().toLowerCase().equals("email"))
					email = true;
			}
			
			if(!email)
				throw new Exception("Email da Aseguradora no fue encontrado");*/
			
			if(!action.getString("especial").equals(""))
				agendaMovimentacao.atualizarEspecial(action.getString("especial"));
			
			if(!action.getString("inscricaoEspecial").equals(""))
				agendaMovimentacao.atualizarInscricaoEspecial(action.getString("inscricaoEspecial"));
			
			agendaMovimentacao.atualizarSuplementosEspecial(action.getString("endosoEspecial"));
			
			agendaMovimentacao.atualizarCapitalEspecial(action.getString("capitalEspecial"));
			
			agendaMovimentacao.atualizarDataEspecial(action.getString("dataEspecial"));
			
			//agendaMovimentacao.atualizarDocumentoEspecial(action.getString("documentoEspecial"));
			
			agendaMovimentacao.atualizarApAnteriorEspecial(action.getString("apAnteriorEspecial"));
			
			agendaMovimentacao.atualizarEndosoApolice(action.getString("endosoApolice"));
			
			agendaMovimentacao.atualizarFase(AgendaMovimentacao.AGENDADA);

			this.setResponseView(new EventoView(agendaMovimentacao));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(agendaMovimentacao));
			mm.rollbackTransaction();
		}
	}
	
	public void excluirAgendaMovimentacao(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		AgendaMovimentacao agendaMovimentacao = (AgendaMovimentacao) eventoHome.obterEventoPorId(action.getLong("id"));
		mm.beginTransaction();
		try
		{
			String alert = "";
			
			String tipo = agendaMovimentacao.obterTipo(); 
			
			if(tipo.equals("Contabil"))
			{
				String mes = new Integer(agendaMovimentacao.obterMesMovimento()).toString();
				int ano = agendaMovimentacao.obterAnoMovimento();
				
				if(mes.length() == 1)
					mes = "0" + mes;
				
				String dataInicioStr = "01/" +  mes + "/" + ano;
				String dataFimStr = "31/" +  mes + "/" + ano;
				
				Date dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse(dataInicioStr);
				Date dataFim = new SimpleDateFormat("dd/MM/yyyy").parse(dataFimStr);
				
				agendaMovimentacao.obterOrigem().excluirDuplicidade(dataInicio, dataFim);
				
				alert = "Relatorio e Movimentos Excluidos com Sucesso(MCO) !";
			}
			else if(tipo.equals("Instrumento"))
				alert = "Registros de 2(apolice) a 20(ratios) excluido com sucesso(MCI) !";
			
			agendaMovimentacao.excluir();
			
			mm.commitTransaction();
			
			this.setAlert(alert);
			
			this.setResponseView(new PaginaInicialView(usuario,usuario));
		}
		catch (Exception e)
		{
			mm.rollbackTransaction();
			this.setAlert(Util.translateException(e));
			this.setResponseView(new EventoView(agendaMovimentacao));
		}
	}
	
	public void visualizarCentralAgendarDesagendar(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		try
		{
			Collection agendasPendentes = eventoHome.obterAgendas("pendente"); 
			Collection agendasAgendadas = eventoHome.obterAgendas("agendado");
			
			this.setResponseView(new AgendarDesagendarView(agendasPendentes, agendasAgendadas));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuario,	usuario));
		}
	}
	
	public void agendarAgendas(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		mm.beginTransaction();
		try
		{
			String[] agendas = action.getStringArray("escolha");
			
			for(int i = 0 ; i < agendas.length ; i++)
			{
				String[] agenda = agendas[i].split("_");
				
				int id = Integer.parseInt(agenda[1]);
				
				AgendaMovimentacao agendaMov = (AgendaMovimentacao) eventoHome.obterEventoPorId(id);
				agendaMov.atualizarFase(AgendaMovimentacao.AGENDADA);
			}
			
			Collection agendasPendentes = eventoHome.obterAgendas("pendente"); 
			Collection agendasAgendadas = eventoHome.obterAgendas("agendado");
			
			mm.commitTransaction();
			
			this.setResponseView(new AgendarDesagendarView(agendasPendentes, agendasAgendadas));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuario,	usuario));
			mm.rollbackTransaction();
		}
	}
	
	public void desagendarAgendas(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		mm.beginTransaction();
		try
		{
			String[] agendas = action.getStringArray("escolha");
			
			for(int i = 0 ; i < agendas.length ; i++)
			{
				String[] agenda = agendas[i].split("_");
				
				int id = Integer.parseInt(agenda[1]);
				
				AgendaMovimentacao agendaMov = (AgendaMovimentacao) eventoHome.obterEventoPorId(id);
				agendaMov.atualizarFase(AgendaMovimentacao.EVENTO_PENDENTE);
			}
			
			Collection agendasPendentes = eventoHome.obterAgendas("pendente"); 
			Collection agendasAgendadas = eventoHome.obterAgendas("agendado");
			
			mm.commitTransaction();
			
			this.setResponseView(new AgendarDesagendarView(agendasPendentes, agendasAgendadas));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuario,	usuario));
			mm.rollbackTransaction();
		}
	}
	
	public void visualizarControleAgendas(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		try
		{
			this.setResponseView(new ControleAgendasView(aseguradoraHome.obterAseguradoras()));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ControleAgendasView(aseguradoraHome.obterAseguradorasPorMenor80OrdenadoPorNome()));
		}
	}
	
	public void atualizarControleAgendas(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		mm.beginTransaction();
		try
		{
			
			for(Iterator i = action.getParameters().keySet().iterator() ; i.hasNext() ; )
			{
				String key = (String) i.next();
				
				if(key.startsWith("comentario"))
				{
					String[] comentarioArray = key.split("_");
					long id = Long.parseLong(comentarioArray[1]);
					
					Aseguradora aseg = (Aseguradora) entidadeHome.obterEntidadePorId(id);
					
					String comentario = action.getString(key);
					Date dataCorreio = action.getDate("data_" + id);
					
					aseg.atualizarComentarioControle(comentario);
					if(dataCorreio!=null)
						aseg.atualizarUltimoEnvioCorreio(dataCorreio);
				}
			}
			
			mm.commitTransaction();
			this.setResponseView(new ControleAgendasView(aseguradoraHome.obterAseguradorasPorMenor80OrdenadoPorNome()));
			this.setAlert("Controle Actualizado !");
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ControleAgendasView(aseguradoraHome.obterAseguradorasPorMenor80OrdenadoPorNome()));
			mm.rollbackTransaction();
		}
	}
	
	public void pdfAgenda(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Notificacao notificacao = (Notificacao) eventoHome.obterEventoPorId(action.getLong("notificacaoId"));
		AgendaMovimentacao agenda = (AgendaMovimentacao) notificacao.obterSuperior();
		try
		{
			AgendaPDF pdf = new AgendaPDF(notificacao);
			
			int mes = agenda.obterMesMovimento();
	        String mesAno = "";
	        
	        if(new Integer(mes).toString().length() == 1)
	        	mesAno = "0" + mes + "/" + agenda.obterAnoMovimento();
	        else
	        	mesAno = mes + "/" + agenda.obterAnoMovimento();
			
			InputStream arquivo = pdf.obterArquivo();
			String nome = "Resultado de Validacion - " + mesAno + ".pdf";
			String mime = "application/pdf";
			
			this.setResponseInputStream(arquivo);
	        this.setResponseFileName(nome);
	        this.setResponseContentType(mime);
	        this.setResponseContentSize(arquivo.available());
			
			this.setResponseView(new EventoView(notificacao));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(notificacao));
		}
	}
}