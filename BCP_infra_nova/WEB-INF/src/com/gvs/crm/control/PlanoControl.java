package com.gvs.crm.control;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.ApoliceHome;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Plano;
import com.gvs.crm.model.PlanoHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.ApolicesRg001View;
import com.gvs.crm.view.AprovarPlanoPorForaView;
import com.gvs.crm.view.AtivarView;
import com.gvs.crm.view.CancelarView;
import com.gvs.crm.view.EntidadeView;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.LocalizarPlanosView;
import com.gvs.crm.view.ModificarPlanosView;
import com.gvs.crm.view.PaginaInicialView;
import com.gvs.crm.view.RejeitarView;
import com.gvs.crm.view.RelPlanosRGView;
import com.gvs.crm.view.SegmentarPlanos2View;
import com.gvs.crm.view.SegmentarPlanosView;
import com.gvs.crm.view.SuspenderView;

import infra.control.Action;
import infra.control.Control;

public class PlanoControl extends Control 
{
	public void incluirPlano(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Plano plano = (Plano) mm.getEntity("Plano");
		mm.beginTransaction();

		try {

			String ramo = action.getString("ramo");
			String secao = action.getString("secao");
			String modalidade = action.getString("plano");
			int especial = action.getInt("especial");
			
			plano.atribuirDescricao(action.getString("descricao"));
			plano.atribuirTitulo(action.getString("denominacao"));
			plano.atribuirEspecial(especial);
			plano.atribuirRamo(ramo);
			plano.atribuirSecao(secao);
			plano.atribuirModalidade(modalidade);
			
			Entidade aseguradora = null;
			if(especial != -1)
			{
				aseguradora = entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				plano.atribuirOrigem(aseguradora);
			}
			
			if(especial != 1)
			{
				if (action.getLong("aseguradoraId") == 0)
					throw new Exception("Elija la Entidad");
				
				if(ramo.equals("") && action.getString("novoRamo").trim().equals(""))
					throw new Exception("Informe el Ramo");
				
				if(secao.equals("") && action.getString("novaSecao").trim().equals(""))
					throw new Exception("Informe la Seccíon");
				
				if(modalidade.equals("") && action.getString("novoPlano").trim().equals(""))
					throw new Exception("Informe la Modalidad");
			}
			
			if(action.getString("denominacao").trim().equals(""))
				throw new Exception("Informe la Denominación");
			
			if(action.getLong("responsavelId") == 0)
				throw new Exception("Informe el Responsable");
			
			if (action.getString("identificador").length() == 0)
				throw new Exception("Complete el Identificador");

			if (action.getString("resolucao").length() == 0)
				throw new Exception("Informe el número de la Resolución");

			if (action.getDate("data") == null)
				throw new Exception("Informe la Fecha de la Resolución");
			
			Entidade responsavel = entidadeHome.obterEntidadePorId(action.getLong("responsavelId"));
			
			plano.verificarPlano(action.getString("identificador"));
			
			Entidade bcp = entidadeHome.obterEntidadePorApelido("bcp");
			plano.atribuirDestino(bcp);
			plano.atribuirResponsavel(usuario);
			plano.atribuirDescricao(action.getString("descricao"));
			plano.atribuirTitulo(action.getString("denominacao"));
			plano.incluir();

			if (action.getString("novaSecao") != null && !action.getString("novaSecao").equals(""))
			{
				plano.adicionarNovaSecao(action.getString("novaSecao"));
				plano.atualizarSecao(action.getString("novaSecao"));
			}
			else
				plano.atualizarSecao(action.getString("secao"));

			if (action.getString("novoPlano") != null && !action.getString("novoPlano").equals(""))
			{
				plano.adicionarNovoPlano(action.getString("novoPlano"));
				plano.atualizarPlano(action.getString("novoPlano"));
			}
			else
				plano.atualizarPlano(action.getString("plano"));

			if (action.getString("novoRamo") != null && !action.getString("novoRamo").equals(""))
			{
				plano.adicionarNovoRamo(action.getString("novoRamo"));
				plano.atualizarRamo(action.getString("novoRamo"));
			}
			else
				plano.atualizarRamo(action.getString("ramo"));

			plano.atualizarIdentificador(action.getString("identificador"));
			plano.atualizarDenominacao(action.getString("denominacao"));
			plano.atualizarResolucao(action.getString("resolucao"));
			if (action.getDate("data") != null)
				plano.atualizarDataResolucao(action.getDate("data"));
			
			plano.atualizarResponsavel(responsavel);

			plano.atualizarSituacao("No Activo");
			
			plano.atualizarEspecial(especial);
			
			if(!action.getString("novoRamo").trim().equals(""))
				plano.atualizarSegmentoRamo(action.getString("novoRamo").trim());
			else
				plano.atualizarSegmentoRamo(ramo);	
			
			if(!action.getString("novaSecao").trim().equals(""))
				plano.atualizarSegmentoSecao(action.getString("novaSecao").trim());
			else
				plano.atualizarSegmentoSecao(secao);
			
			mm.commitTransaction();

			this.setResponseView(new EventoView(plano));

			this.setAlert("Plan Incluido");

		}

		catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(plano));
			mm.rollbackTransaction();
		}
	}

	public void atualizarPlano(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Plano plano = (Plano) eventoHome.obterEventoPorId(action.getLong("id"));
		mm.beginTransaction();
		try
		{
			String identificarAntigo = plano.obterIdentificador().trim();
			
			String ramo = action.getString("ramo");
			String secao = action.getString("secao");
			String modalidade = action.getString("plano");
			String identificador = action.getString("identificador").trim();
			int especial = action.getInt("especial");
			
			plano.atribuirDescricao(action.getString("descricao"));
			plano.atribuirTitulo(action.getString("denominacao"));
			plano.atribuirEspecial(especial);
			plano.atribuirRamo(ramo);
			plano.atribuirSecao(secao);
			plano.atribuirModalidade(modalidade);
			Entidade aseguradora = null;
			if(especial != -1)
			{
				aseguradora = entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				plano.atribuirOrigem(aseguradora);
			}
			
			if(especial != 1)
			{
				if(action.getLong("aseguradoraId") == 0)
					throw new Exception("Elija la Entidad");
	
				if(ramo.equals("") && action.getString("novoRamo").trim().equals(""))
					throw new Exception("Informe el Ramo");
				
				if(secao.equals("") && action.getString("novaSecao").trim().equals(""))
					throw new Exception("Informe la Seccíon");
				
				if(modalidade.equals("") && action.getString("novoPlano").trim().equals(""))
					throw new Exception("Informe la Modalidad");
			}
			
			if(action.getString("denominacao").trim().equals(""))
				throw new Exception("Informe la Denominación");
			
			if (action.getString("identificador").length() == 0)
				throw new Exception("Complete el Identificador");

			if (action.getString("resolucao").length() == 0)
				throw new Exception("Informe el número de la Resolución");

			if (action.getDate("data") == null)
				throw new Exception("Informe la Fecha de la Resolución");
			
			if(action.getLong("responsavelId") == 0)
				throw new Exception("Informe el Responsable");
			
			Entidade responsavel = entidadeHome.obterEntidadePorId(action.getLong("responsavelId"));

			if(!identificarAntigo.equals(identificador))
				plano.verificarPlano(identificador);

			if (action.getString("novaSecao") != null && !action.getString("novaSecao").equals(""))
			{
				plano.adicionarNovaSecao(action.getString("novaSecao"));
				plano.atualizarSecao(action.getString("novaSecao"));
			}
			else
				plano.atualizarSecao(action.getString("secao"));

			if (action.getString("novoPlano") != null && !action.getString("novoPlano").equals(""))
			{
				plano.adicionarNovoPlano(action.getString("novoPlano"));
				plano.atualizarPlano(action.getString("novoPlano"));
			}
			else
				plano.atualizarPlano(action.getString("plano"));

			Entidade bcp = entidadeHome.obterEntidadePorApelido("bcp");

			plano.atualizarOrigem(aseguradora);
			plano.atualizarDescricao(action.getString("descricao"));
			plano.atualizarTitulo(action.getString("denominacao").trim());

			if (action.getString("novoRamo") != null && !action.getString("novoRamo").equals(""))
			{
				plano.adicionarNovoRamo(action.getString("novoRamo"));
				plano.atualizarRamo(action.getString("novoRamo"));
			}
			else
				plano.atualizarRamo(action.getString("ramo"));

			plano.atribuirTitulo(action.getString("denominacao"));

			plano.atualizarIdentificador(identificador);
			plano.atualizarResolucao(action.getString("resolucao"));
			if (action.getDate("data") != null)
				plano.atualizarDataResolucao(action.getDate("data"));
			
			plano.atualizarResponsavel(responsavel);
			plano.atualizarEspecial(especial);
			
			if(!action.getString("novoRamo").trim().equals(""))
				plano.atualizarSegmentoRamo(action.getString("novoRamo").trim());
			else
				plano.atualizarSegmentoRamo(ramo);	
			
			if(!action.getString("novaSecao").trim().equals(""))
				plano.atualizarSegmentoSecao(action.getString("novaSecao").trim());
			else
				plano.atualizarSegmentoSecao(secao);

			//plano.atualizarSituacao(action.getString("situacao"));

			mm.commitTransaction();

			this.setResponseView(new EventoView(plano));

			this.setAlert("Plan Actualizado");
		}

		catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(plano));
			mm.rollbackTransaction();
		}
	}

	public void aprovarPlano(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Plano plano = (Plano) eventoHome.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {

			if (action.getBoolean("view"))
				this.setResponseView(new AprovarPlanoPorForaView(plano,	action));
			else {
				/*
				 * boolean ativa = false;
				 * 
				 * for(Iterator i =
				 * plano.obterOrigem().obterEventosComoOrigem().iterator() ;
				 * i.hasNext() ; ) { Evento e = (Evento) i.next();
				 * 
				 * if(e instanceof Plano && e.obterId()!=plano.obterId()) {
				 * Plano inscricaoVelha = (Plano) e;
				 * 
				 * if((inscricaoVelha.obterFase().obterCodigo().equals(Plano.EVENTO_CONCLUIDO) ||
				 * inscricaoVelha.obterFase().obterCodigo().equals(Plano.APROVADA)) &&
				 * inscricaoVelha.obterSituacao().equals("Activa")) ativa =
				 * true; } }
				 * 
				 * if(ativa) throw new Exception("Já existe uma Inscripción
				 * Activa");
				 */

				plano.adicionarComentario("Plan aprobado por "	+ usuario.obterNome(), action.getString("comentario"));
				plano.atualizarFase(Plano.APROVADA);
				plano.atualizarResponsavel(plano.obterCriador());
				plano.atualizarSituacao("Activo");

				/*
				 * for(Iterator i =
				 * plano.obterOrigem().obterEventosComoOrigem().iterator() ;
				 * i.hasNext() ; ) { Evento e = (Evento) i.next();
				 * 
				 * if(e instanceof Plano && e.obterId()!=plano.obterId()) {
				 * Plano inscricaoVelha = (Plano) e;
				 * 
				 * if((inscricaoVelha.obterFase().obterCodigo().equals(Plano.EVENTO_CONCLUIDO) ||
				 * inscricaoVelha.obterFase().obterCodigo().equals(Plano.APROVADA)) &&
				 * inscricaoVelha.obterSituacao().equals("Activa")) {
				 * //inscricaoVelha.atualizarFase(Inscricao.EVENTO_CONCLUIDO);
				 * inscricaoVelha.atualizarSituacao("Encerrada"); } } }
				 */
				if(plano.obterEspecial() == 0)
					this.setResponseView(new EntidadeView(plano.obterOrigem()));
				else
					this.setResponseView(new PaginaInicialView(usuario, usuario));
			}

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(plano));
			mm.rollbackTransaction();
		}
	}

	public void ativarPlano(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Plano plano = (Plano) eventoHome.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {

			if (action.getBoolean("view"))
				this.setResponseView(new AtivarView(plano));
			else {
				if (plano.validarResolucao(action.getString("numero")))
					throw new Exception("La Resolución Nº "
							+ action.getString("numero")
							+ " no fue encontrada o fue aprobada");

				plano.adicionarComentario("Plan reactivado por "
						+ usuario.obterNome() + " con base en la Resolución "
						+ action.getString("numero"), action
						.getString("comentario"));
				plano.atualizarFase(Plano.APROVADA);
				plano.atualizarResponsavel(plano.obterCriador());
				plano.atualizarSituacao("Activa");

				this.setResponseView(new PaginaInicialView(usuario, usuario));
			}

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(plano));
			mm.rollbackTransaction();
		}
	}

	public void rejeitarPlano(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Plano plano = (Plano) eventoHome.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {

			if (action.getBoolean("view")) {
				this.setResponseView(new RejeitarView(plano, action));
			} else {
				plano.adicionarComentario("Plan rechasado por "
						+ usuario.obterNome(), action.getString("comentario"));
				plano.atualizarFase(Plano.REJEITADA);
				plano.atualizarResponsavel(plano.obterCriador());
				plano.atualizarSituacao("Rejeitada");

				this.setResponseView(new PaginaInicialView(usuario, usuario));
			}

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(plano));
			mm.rollbackTransaction();
		}
	}

	public void suspenderPlano(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Plano plano = (Plano) eventoHome.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {

			if (action.getBoolean("view")) {
				this.setResponseView(new SuspenderView(plano, action));
			} else {
				if (plano.validarResolucao(action.getString("numero")))
					throw new Exception("La Resolución Nº "
							+ action.getString("numero") + " no fue encontrada");

				plano.adicionarComentario("Plan suspendido por "
						+ usuario.obterNome() + " con base en la Resolución "
						+ action.getString("numero"), action
						.getString("comentario"));
				plano.atualizarResponsavel(plano.obterCriador());
				plano.atualizarSituacao("Suspenso");

				this.setResponseView(new PaginaInicialView(usuario, usuario));
			}

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(plano));
			mm.rollbackTransaction();
		}
	}

	public void cancelarPlano(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Plano plano = (Plano) eventoHome.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {

			if (action.getBoolean("view"))
			{
				this.setResponseView(new CancelarView(plano, action));
			}
			else
			{
				//if (plano.validarResolucao(action.getString("numero")))
					//throw new Exception("La Resolución Nº "	+ action.getString("numero") + " no fue encontrada");

				plano.adicionarComentario("Plan cancelado por "	+ usuario.obterNome() + " con base en la Resolución "
						+ action.getString("numero"), action.getString("comentario"));
				plano.atualizarResponsavel(plano.obterCriador());
				plano.atualizarSituacao("Cancelada");

				this.setResponseView(new EventoView(plano));
			}

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(plano));
			mm.rollbackTransaction();
		}
	}

	public void carregarPlanos(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorChave("fkubota");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		mm.beginTransaction();
		try {

			Collection linhas = new ArrayList();

			String erros = "";

			String bufferAux = "";

			int qtdeLinha = 1;

			File file = new File("" + "c:/Aseguradoras/Archivos/planos.txt");

			if (file.exists()) {
				FileInputStream inputStreamArquivo = new FileInputStream(""
						+ "c:/Aseguradoras/Archivos/planos.txt");

				DataInputStream inputStreamData = new DataInputStream(
						inputStreamArquivo);

				while ((bufferAux = inputStreamData.readLine()) != null) {
					linhas.add(new String(bufferAux));
				}

				for (Iterator i = linhas.iterator(); i.hasNext();) {
					String linha = (String) i.next();

					StringTokenizer st = new StringTokenizer(linha, ";");

					Plano plano = (Plano) mm.getEntity("Plano");

					String inscricao = st.nextToken();

					String codigoPlano = st.nextToken();

					System.out.println("Linha: " + qtdeLinha + " - Inscricao: "
							+ inscricao + " Plano: " + codigoPlano);

					if (inscricao.length() == 1)
						inscricao = "00" + inscricao;
					else if (inscricao.length() == 2)
						inscricao = "0" + inscricao;

					Aseguradora aseguradora = (Aseguradora) entidadeHome
							.obterEntidadePorSigla(inscricao);

					Entidade bcp = entidadeHome.obterEntidadePorApelido("bcp");

					if (aseguradora != null) {

						String descricaoRamo = st.nextToken();

						String descricaoSecao = st.nextToken();

						String resolucao = st.nextToken();

						String dataResolucaoStr = st.nextToken();

						Date dataResolucao = new SimpleDateFormat("dd/MM/yyyy")
								.parse(dataResolucaoStr);

						String denominacion = st.nextToken();

						String modalidade = st.nextToken();

						String comentario = "";

						if (st.countTokens() == 1)
							comentario = st.nextToken();

						plano.atribuirOrigem(aseguradora);
						plano.atribuirDestino(bcp);
						plano.atribuirResponsavel(usuario);
						plano.atribuirTitulo(modalidade);
						plano.incluir();

						plano.atualizarIdentificador(codigoPlano);

						if (!plano.obterNomeRamos().contains(descricaoRamo))
							plano.adicionarNovoRamo(descricaoRamo);

						plano.atualizarRamo(descricaoRamo);

						if (!plano.obterNomeSecoes().contains(descricaoSecao))
							plano.adicionarNovaSecao(descricaoSecao);

						plano.atualizarSecao(descricaoSecao);
						plano.atualizarResolucao(resolucao);
						plano.atualizarDataResolucao(dataResolucao);

						if (!plano.obterNomePlanos().contains(denominacion))
							plano.adicionarNovoPlano(denominacion);

						plano.atualizarPlano(denominacion);

						if (!comentario.equals(""))
							plano.adicionarComentario("Comentario", comentario);

						plano.atualizarFase(Plano.APROVADA);

						plano.atualizarSituacao("Activo");
					} else {
						erros += "Nao carregou a inscricao: " + inscricao
								+ "\n";
					}

					qtdeLinha++;
				}
			} else {
				throw new Exception(
						"Erro: 26 - El Archivo planos.txt no fue encontrado");
			}

			this.setAlert("Planos Nao carregados");

			System.out.println("Erros: " + erros);

			this.setResponseView(new PaginaInicialView(usuario, usuario));

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuario, usuario));
			mm.rollbackTransaction();
		}
	}

	public void visualizarPlanos(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		PlanoHome planoHome = (PlanoHome) mm.getHome("PlanoHome");
		
		Aseguradora aseguradora = null;
		String ramo = null;
		String secao = null;
		String plano = null;
		
		String situacao = action.getString("situacao");
		if(!action.getString("ramo").equals(""))
			ramo = action.getString("ramo");
		if(!action.getString("secao").equals(""))
			secao = action.getString("secao");
		if(!action.getString("plano").equals(""))
			plano = action.getString("plano");
		long aseguradoraId = action.getLong("aseguradora");
		boolean especial = aseguradoraId == -1;
		boolean modificado = aseguradoraId == -2;
		
		if(aseguradoraId > 0)
			aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(aseguradoraId);
		
		Collection<Plano> planos = new ArrayList<Plano>();
		try
		{
			if(action.getBoolean("view"))
				this.setResponseView(new LocalizarPlanosView(planos, ramo, secao, plano, situacao, aseguradora, especial, modificado));
			else
			{
				if(ramo == null)
					ramo = "";
				if(secao == null)
					secao = "";
				if(plano == null)
					plano = "";
				
				if(ramo.equals("") && secao.equals("") && plano.equals("") && situacao.equals("") && aseguradoraId == 0)
					throw new Exception("Es necesario un filtro adicional");
				
				planos = planoHome.localizarPlanos(ramo, secao, plano, situacao, aseguradora, especial, modificado);
				
				this.setResponseView(new LocalizarPlanosView(planos, ramo, secao, plano, situacao, aseguradora, especial, modificado));
			}

		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new LocalizarPlanosView(planos, ramo, secao, plano, situacao, aseguradora, especial, modificado));
		}
	}
	
	public void alterarPlanos(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		PlanoHome planoHome = (PlanoHome) mm.getHome("PlanoHome");
		Collection<String> secoes = planoHome.obterNomesSecao();
		Collection<String> codigos = planoHome.obterNomesPlano();
		mm.beginTransaction();
		try
		{
			if(!action.getBoolean("view"))
			{
				if(action.getString("tipo").equals("secao"))
				{
					for(int i = 1 ; i <=secoes.size() ; i++)
					{
						if(!action.getString("novaSecao"+i).equals(""))
						{
							String nomeAntigo = action.getString("secao"+i);
							String nomeNovo = action.getString("novaSecao"+i).trim();
							
							planoHome.atualizarSecao(nomeAntigo, nomeNovo);
						}
					}
				}
				else
				{
					for(int i = 1 ; i <=codigos.size() ; i++)
					{
						if(!action.getString("novoCodigo"+i).equals(""))
						{
							String nomeAntigo = action.getString("codigo"+i);
							String nomeNovo = action.getString("novoCodigo"+i).trim();
							
							planoHome.atualizarCodigo(nomeAntigo, nomeNovo);
						}
					}
				}
			}
			
			mm.commitTransaction();
			
			secoes = planoHome.obterNomesSecao();
			codigos = planoHome.obterNomesPlano();
			
			this.setResponseView(new ModificarPlanosView(secoes, codigos));

		}
		catch (Exception exception)
		{
			mm.rollbackTransaction();
			this.setResponseView(new ModificarPlanosView(secoes, codigos));
			this.setAlert(Util.translateException(exception));
		}
	}
	
	public void segmentarPlanos(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		PlanoHome planoHome = (PlanoHome) mm.getHome("PlanoHome");
		String tipo = action.getString("tipo");
		Collection<String> dados = new ArrayList<String>();
		try
		{
			if(action.getBoolean("view"))
				this.setResponseView(new SegmentarPlanosView());
			else
			{
				if(tipo.equals("secao"))
					dados = planoHome.obterNomesSecao();
				else if(tipo.equals("modalidade"))
					dados = planoHome.obterNomesPlano();
				
				this.setResponseView(new SegmentarPlanos2View(tipo, dados));
			}
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new SegmentarPlanos2View(tipo, dados));
		}
	}
	
	public void atualizarSegmentosPlano(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		PlanoHome planoHome = (PlanoHome) mm.getHome("PlanoHome");
		String tipo = action.getString("tipo");
		Collection<String> dados = new ArrayList<String>();
		mm.beginTransaction();
		try
		{
			if(tipo.equals("secao"))
			{
				dados = planoHome.obterNomesSecao();
				
				for(Iterator i = action.getParameters().keySet().iterator() ; i.hasNext() ; )
				{
					String key = (String) i.next();
					
					if(key.startsWith("secao"))
					{
						String cont = key.substring(5, key.length());
						String secao = action.getString(key);
						String ramo = action.getString("ramo"+cont);
						
						if(!ramo.equals(""))
							planoHome.atualizarSegmentoRamo(ramo, secao);
					}
				}
			}
			else if(tipo.equals("modalidade"))
			{
				dados = planoHome.obterNomesPlano();
				
				for(Iterator i = action.getParameters().keySet().iterator() ; i.hasNext() ; )
				{
					String key = (String) i.next();
					
					if(key.startsWith("modalidade"))
					{
						String cont = key.substring(10, key.length());
						String modalidade = action.getString(key);
						String secao = action.getString("secao"+cont);
						
						if(!secao.equals(""))
							planoHome.atualizarSegmentoSecao(secao, modalidade);
					}
				}
			}
			
			mm.commitTransaction();
			
			this.setResponseView(new SegmentarPlanos2View(tipo, dados));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new SegmentarPlanos2View(tipo, dados));
			mm.rollbackTransaction();
		}
	}
	
	public void selecionaRamo(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Plano plano = null;
		long id  = action.getLong("id");
		if(id > 0)
			plano = (Plano) eventoHome.obterEventoPorId(id);
		else
			plano = (Plano) mm.getEntity("Plano");
		try
		{
			long aseguradoraId = action.getLong("aseguradoraId"); 
			long responsavelId = action.getLong("responsavelId");
			String ramo = action.getString("ramo");
			
			if(aseguradoraId > 0)
			{
				Entidade aseguradora = entidadeHome.obterEntidadePorId(aseguradoraId);
				plano.atribuirOrigem(aseguradora);
			}

			if(responsavelId > 0)
			{
				Entidade responsavel = entidadeHome.obterEntidadePorId(responsavelId);
				plano.atribuirResponsavel(responsavel);
			}
			
			if(!ramo.equals(""))
				plano.atribuirRamo(ramo);
			else
				plano.atribuirRamo(null);
			
			plano.atribuirEspecial(action.getInt("especial"));
			
			this.setResponseView(new EventoView(plano));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(plano));
		}
	}
	
	public void selecionaSecao(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Plano plano = null;
		long id  = action.getLong("id");
		if(id > 0)
			plano = (Plano) eventoHome.obterEventoPorId(id);
		else
			plano = (Plano) mm.getEntity("Plano");
		try
		{
			long aseguradoraId = action.getLong("aseguradoraId"); 
			long responsavelId = action.getLong("responsavelId");
			String ramo = action.getString("ramo");
			String secao = action.getString("secao");
			
			if(aseguradoraId > 0)
			{
				Entidade aseguradora = entidadeHome.obterEntidadePorId(aseguradoraId);
				plano.atribuirOrigem(aseguradora);
			}

			if(responsavelId > 0)
			{
				Entidade responsavel = entidadeHome.obterEntidadePorId(responsavelId);
				plano.atribuirResponsavel(responsavel);
			}
			
			if(!ramo.equals(""))
				plano.atribuirRamo(ramo);
			else
				plano.atribuirRamo(null);
			
			if(!secao.equals(""))
				plano.atribuirSecao(secao);
			else
				plano.atribuirSecao(null);
			
			plano.atribuirEspecial(action.getInt("especial"));
			
			this.setResponseView(new EventoView(plano));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(plano));
		}
	}
	
	public void selecionaModalidade(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Plano plano = null;
		long id  = action.getLong("id");
		if(id > 0)
			plano = (Plano) eventoHome.obterEventoPorId(id);
		else
			plano = (Plano) mm.getEntity("Plano");
		try
		{
			long aseguradoraId = action.getLong("aseguradoraId"); 
			long responsavelId = action.getLong("responsavelId");
			String ramo = action.getString("ramo");
			String secao = action.getString("secao");
			String modalidade = action.getString("plano");
			
			if(aseguradoraId > 0)
			{
				Entidade aseguradora = entidadeHome.obterEntidadePorId(aseguradoraId);
				plano.atribuirOrigem(aseguradora);
			}

			if(responsavelId > 0)
			{
				Entidade responsavel = entidadeHome.obterEntidadePorId(responsavelId);
				plano.atribuirResponsavel(responsavel);
			}
			
			if(!ramo.equals(""))
				plano.atribuirRamo(ramo);
			else
				plano.atribuirRamo(null);
			
			if(!secao.equals(""))
				plano.atribuirSecao(secao);
			else
				plano.atribuirSecao(null);
			
			if(!modalidade.equals(""))
				plano.atribuirModalidade(modalidade);
			else
				plano.atribuirModalidade(null);
			
			plano.atribuirEspecial(action.getInt("especial"));
			
			this.setResponseView(new EventoView(plano));
			
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(plano));
		}
	}
	
	public void relPlanoRG(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		Collection<String> informacoes = new ArrayList<String>();
		Aseguradora aseguradora = null;
		String situacaoSeguro = action.getString("situacaoSeguro");
		boolean especial = action.getBoolean("especial");
		boolean modificado = action.getBoolean("modificado");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new RelPlanosRGView(aseguradora, situacaoSeguro, dataInicio, dataFim, informacoes, especial, modificado));
			else
			{
				if(dataInicio == null)
					throw new Exception("Fecha Inicio en blanco");
				if(dataFim == null)
					throw new Exception("Fecha Fin en blanco");
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " 00:00:00";
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
				
				dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				informacoes = apoliceHome.obterApolicesRG001(aseguradora, situacaoSeguro, dataInicio, dataFim, especial, modificado);
				
				/*if(excel)
				{
					ConsolidadoApolicesSinistrosXLS xls = new ConsolidadoApolicesSinistrosXLS(aseguradora, opcao, situacaoSeguro, dataInicio, dataFim, informacoes);
					
					String dataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
					
					this.setResponseInputStream(xls.obterArquivo());
			        this.setResponseFileName("Consolidado Pólizas/Sección_" + opcao+"_"+usuario.obterNome() + "_"+dataHora + ".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(xls.obterArquivo().available());
				}
				else
					this.setResponseView(new ConsolidadoApolicesSinistrosView(aseguradora, opcao, situacaoSeguro, dataInicio, dataFim,mostraTela,excel,informacoes));*/
				
				this.setResponseView(new RelPlanosRGView(aseguradora, situacaoSeguro, dataInicio, dataFim, informacoes, especial, modificado));
			}
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new RelPlanosRGView(aseguradora, situacaoSeguro, dataInicio, dataFim, informacoes, especial, modificado));
		}
	}
	
	public void relPlanoRGAseg(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		long dataInicioLong = action.getLong("inicio");
		long dataFimLong = action.getLong("fim");
		boolean especial = action.getBoolean("especial");
		boolean modificado = action.getBoolean("modificado");
		
		Date dataInicio = new Date(dataInicioLong);
		Date dataFim = new Date(dataFimLong);
		Aseguradora aseguradora = null;
		String situacaoSeguro = action.getString("sitSeguro");
		Collection<Apolice> apolices = new ArrayList<Apolice>();
		try 
		{
			if(action.getLong("asegId") > 0)
				aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("asegId"));
			
			String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " 00:00:00";
			String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
			
			dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
			dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
			
			apolices = apoliceHome.obterApolicesRG001Aseg(aseguradora, situacaoSeguro, dataInicio, dataFim, especial,modificado);
			
			this.setResponseView(new ApolicesRg001View(aseguradora, situacaoSeguro, dataInicio, dataFim, apolices,especial, modificado));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ApolicesRg001View(aseguradora, situacaoSeguro, dataInicio, dataFim, apolices,especial, modificado));
		}
	}
}