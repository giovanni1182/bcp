package com.gvs.crm.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Inscricao;
import com.gvs.crm.model.InscricaoHome;
import com.gvs.crm.model.Renovacao;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.AprovarInscricaoPorForaView;
import com.gvs.crm.view.CancelarView;
import com.gvs.crm.view.EntidadeView;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.ListaInscricoesView;
import com.gvs.crm.view.PaginaInicialView;
import com.gvs.crm.view.RejeitarView;
import com.gvs.crm.view.SuperintendenteView;
import com.gvs.crm.view.SuspenderView;

import infra.control.Action;
import infra.control.Control;

public class InscricaoControl extends Control {
	public void atualizarInscricao(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Inscricao inscricao = (Inscricao) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try 
		{

			if (action.getString("inscricao").equals(""))
				throw new Exception("Elegiré a Inscripción");
			
			String inscricaoModificada = "";
			
			for(int i = 0 ; i < action.getString("inscricao").length() ; i++)
			{
				String c = action.getString("inscricao").substring(i, i+1);
				
				if(c.equals("0") || c.equals("1") || c.equals("2") || c.equals("3") || c.equals("4") || c.equals("5") || c.equals("6") || c.equals("7") || c.equals("8") || c.equals("9"))
					inscricaoModificada+=c;
			}

			if (action.getDate("dataValidade") == null)
				throw new Exception("Elegiré Fecha de Validad");

			/*if (action.getString("novoRamo") != null && !action.getString("novoRamo").equals(""))
				inscricao.adicionarNovoRamo(action.getString("novoRamo"));
			else if (action.getString("ramo") != null && !action.getString("ramo").equals(""))
				inscricao.adicionarNovoRamo(action.getString("ramo"));*/

			inscricao.atualizarTitulo(inscricaoModificada);

			if (action.getDate("dataResolucao") != null)
				inscricao.atualizarDataResolucao(action.getDate("dataResolucao"));

			inscricao.atualizarDataValidade(action.getDate("dataValidade"));

			Entidade agente = entidadeHome.obterEntidadePorId(action.getLong("agenteId"));
			inscricao.atualizarAgente(agente);

			inscricao.atualizarInscricao(inscricaoModificada);
			inscricao.atualizarNumeroResolucao(action.getString("resolucao"));
			inscricao.atualizarCesion(action.getInt("cesion"));

			if (action.getLong("aseguradoraId") > 0)
			{
				Aseguradora aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				inscricao.atualizarAseguradora(aseguradora);
			}

			if (action.getLong("apoliceId") > 0) 
			{
				Apolice apolice = (Apolice) eventoHome.obterEventoPorId(action.getLong("apoliceId"));
				inscricao.atualizarApolice(apolice);
			}

			inscricao.atualizarNumeroSecao(action.getString("secao"));
			inscricao.atualizarNumeroApolice(action.getString("numeroApolice"));
			
			if (action.getDate("dataEmissao") != null)
				inscricao.atualizarDataEmissao(action.getDate("dataEmissao"));

			if (action.getDate("dataVencimento") != null)
				inscricao.atualizarDataVencimento(action.getDate("dataVencimento"));
			
			if(inscricao.obterId() == 70499434)
				inscricao.atualizarSituacao("Vigente");

			this.setResponseView(new EventoView(inscricao));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(inscricao));
			mm.rollbackTransaction();
		}
	}

	public void incluirInscricao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		InscricaoHome inscricaoHome = (InscricaoHome) mm
				.getHome("InscricaoHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Inscricao inscricao = (Inscricao) mm.getEntity("Inscricao");

		mm.beginTransaction();
		try {

			Entidade origem = entidadeHome.obterEntidadePorId(action.getLong("origemId"));
			inscricao.atribuirOrigem(origem);
			inscricao.atribuirResponsavel(usuario);

			if (action.getDate("dataValidade") == null)
				throw new Exception("Elegiré Fecha de Validad");

			for (Iterator i = origem.obterEventosComoOrigem().iterator(); i.hasNext();) 
			{
				Evento e = (Evento) i.next();

				if (e instanceof Inscricao)
				{
					Inscricao inscricaoVelha = (Inscricao) e;

					if (inscricaoVelha.obterSituacao().equals("Pendiente"))
						throw new Exception("Já existe uma solicitación de Inscrición em andamento");
				}
			}
			
			if (action.getString("inscricao").equals(""))
				throw new Exception("Preencha a Inscripción");
			
			String inscricaoModificada = "";
			
			for(int i = 0 ; i < action.getString("inscricao").length() ; i++)
			{
				String c = action.getString("inscricao").substring(i, i+1);
				
				if(c.equals("0") || c.equals("1") || c.equals("2") || c.equals("3") || c.equals("4") || c.equals("5") || c.equals("6") || c.equals("7") || c.equals("8") || c.equals("9"))
					inscricaoModificada+=c;
					
			}

			if (origem instanceof AuxiliarSeguro)
			{
				AuxiliarSeguro auxiliar = (AuxiliarSeguro) origem;

				String atividade = auxiliar.obterAtributo("atividade").obterValor();

				//if (inscricaoHome.verificarInscricao(auxiliar, atividade, inscricaoModificada))
				Entidade e = inscricaoHome.verificarInscricao2(auxiliar, atividade, inscricaoModificada);
				if (e!=null)
					throw new Exception("La Inscripción " + action.getString("inscricao") + " já está sendo usada por " + e.obterNome());
				
				if(auxiliar.obterRamos().size() == 0)
					throw new Exception("Ramo de la póliza en blanco en lá opción Detalles");
			}
			else
			{
				if (inscricaoHome.verificarInscricao(origem, inscricaoModificada))
					throw new Exception("La Inscripción " + action.getString("inscricao") + " já está sendo usada");
			}

			Entidade destino = entidadeHome.obterEntidadePorApelido("bcp");

			inscricao.atribuirDestino(destino);
			inscricao.atribuirTitulo(inscricaoModificada);
			inscricao.incluir();

			inscricao.atualizarTitulo(inscricaoModificada);

			if (action.getDate("dataResolucao") != null)
				inscricao.atualizarDataResolucao(action
						.getDate("dataResolucao"));

			inscricao.atualizarCesion(action.getInt("cesion"));
			
			inscricao.atualizarDataValidade(action.getDate("dataValidade"));

			inscricao.atualizarInscricao(inscricaoModificada);
			inscricao.atualizarNumeroResolucao(action.getString("resolucao"));
			inscricao.atualizarSituacao("Pendiente");

			//inscricao.atualizarAgente(responsavel);

			Renovacao renovacao = (Renovacao) mm.getEntity("Renovacao");

			renovacao.atribuirOrigem(inscricao.obterOrigem());
			renovacao.atribuirDestino(inscricao.obterDestino());
			renovacao.atribuirTitulo("Ficha de Inscripción");
			renovacao.atribuirSuperior(inscricao);
			renovacao.incluir();

			if (action.getString("novoRamo") != null
					&& !action.getString("novoRamo").equals(""))
				inscricao.adicionarNovoRamo(action.getString("novoRamo"));
			else if (action.getString("ramo") != null
					&& !action.getString("ramo").trim().equals(""))
				inscricao.adicionarNovoRamo(action.getString("ramo"));

			if (action.getLong("aseguradoraId") > 0) {
				Aseguradora aseguradora = (Aseguradora) entidadeHome
						.obterEntidadePorId(action.getLong("aseguradoraId"));

				inscricao.atualizarAseguradora(aseguradora);
			}

			/*
			 * if(action.getLong("apoliceId") > 0) { Apolice apolice = (Apolice)
			 * eventoHome.obterEventoPorId(action.getLong("apoliceId"));
			 * inscricao.atualizarApolice(apolice); }
			 */

			inscricao.atualizarNumeroSecao(action.getString("secao"));

			inscricao.atualizarNumeroApolice(action.getString("numeroApolice"));

			if (action.getDate("dataEmissao") != null)
				inscricao.atualizarDataEmissao(action.getDate("dataEmissao"));

			if (action.getDate("dataVencimento") != null)
				inscricao.atualizarDataVencimento(action.getDate("dataVencimento"));

			mm.commitTransaction();
			this.setResponseView(new EventoView(inscricao));
			this.setAlert("Inscripción incluida");

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(inscricao));
			mm.rollbackTransaction();
		}
	}

	public void incluirRamoInscricao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Inscricao inscricao = (Inscricao) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {
			if (action.getString("novoRamo") != null
					&& !action.getString("novoRamo").equals(""))
				inscricao.adicionarNovoRamo(action.getString("novoRamo"));
			else
				inscricao.adicionarNovoRamo(action.getString("ramo"));

			mm.commitTransaction();
			this.setResponseView(new EventoView(inscricao));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(inscricao));
			mm.rollbackTransaction();
		}
	}

	public void excluirRamoInscricao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Inscricao inscricao = (Inscricao) eventoHome.obterEventoPorId(action
				.getLong("id"));
		Inscricao.Ramo ramo = inscricao.obterRamo(action.getInt("seq"));

		mm.beginTransaction();
		try {
			inscricao.excluirRamo(ramo);

			mm.commitTransaction();

			this.setResponseView(new EventoView(inscricao));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(inscricao));
			mm.rollbackTransaction();
		}
	}

	public void superintendenteInscricao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Inscricao inscricao = (Inscricao) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {

			if (action.getBoolean("view")) {
				this.setResponseView(new SuperintendenteView(inscricao));
			} else {
				inscricao.adicionarComentario(
						"Enviado para Superintendente por "
								+ usuario.obterNome(), action
								.getString("comentario"));

				Entidade superintendente = entidadeHome
						.obterEntidadePorApelido("superintendencia");

				inscricao.atualizarResponsavel(superintendente);

				inscricao.atualizarFase(Inscricao.SUPERINTENDENTE);

				this.setResponseView(new PaginaInicialView(usuario, usuario));
			}

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(inscricao));
			mm.rollbackTransaction();
		}
	}

	public void aprovarInscricao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Inscricao inscricao = (Inscricao) eventoHome.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try 
		{

			if (action.getBoolean("view"))
				this.setResponseView(new AprovarInscricaoPorForaView(inscricao,	action));
			else 
			{
				Entidade origem = inscricao.obterOrigem();
				
				if (origem instanceof AuxiliarSeguro)
				{
					AuxiliarSeguro auxiliar = (AuxiliarSeguro) origem;
					
					if(auxiliar.obterRamos().size() == 0)
						throw new Exception("Ramo de la póliza en blanco en lá opción Detalles");
				}
				
				inscricao.adicionarComentario("Inscripción aprovada por "+ usuario.obterNome(), action.getString("comentario"));
				inscricao.atualizarFase(Inscricao.APROVADA);
				inscricao.atualizarResponsavel(inscricao.obterCriador());
				inscricao.atualizarSituacao("Vigente");
				inscricao.atualizarFase(Inscricao.EVENTO_CONCLUIDO);

				Entidade entidade = inscricao.obterOrigem();
				String nomeAgente = entidade.obterNome();
				
				String faseVelha;
				
				for (Iterator i = entidade.obterInscricoes().iterator(); i.hasNext();)
				{
					Inscricao inscricaoVelha = (Inscricao) i.next();
					faseVelha = inscricaoVelha.obterFase().obterCodigo();
					
					if (inscricaoVelha.obterId() != inscricao.obterId())
					{
						if ((faseVelha.equals(Inscricao.EVENTO_CONCLUIDO) || faseVelha.equals(Inscricao.APROVADA)) && inscricaoVelha.obterSituacao().equals("Vigente"))
							inscricaoVelha.atualizarSituacao("Concluida");
					}
				}

				Entidade.Atributo situacao = (Entidade.Atributo) entidade.obterAtributo("situacao");

				if (situacao != null)
					situacao.atualizarValor("Activa");

				if (entidade instanceof Aseguradora)
					entidade.atualizarSigla(inscricao.obterInscricao());
				
				String[] nomeSujo = nomeAgente.split("#");
				if(nomeSujo.length > 1)
				{
					entidade.atribuirNome(nomeSujo[0].trim());
					entidade.atualizar();
				}

				this.setResponseView(new EntidadeView(inscricao.obterOrigem()));
			}

			mm.commitTransaction();
		}
		catch (Exception exception)
		{
			System.out.println(exception.getMessage());
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(inscricao));
			mm.rollbackTransaction();
		}
	}

	public void rejeitarInscricao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Inscricao inscricao = (Inscricao) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {

			if (action.getBoolean("view")) {
				this.setResponseView(new RejeitarView(inscricao, action));
			} else {
				inscricao.adicionarComentario("Inscripción rejeitada por "
						+ usuario.obterNome(), action.getString("comentario"));
				inscricao.atualizarFase(Inscricao.REJEITADA);
				inscricao.atualizarResponsavel(inscricao.obterCriador());
				inscricao.atualizarSituacao("Rejeitada");

				Entidade entidade = inscricao.obterOrigem();

				Entidade.Atributo situacao = (Entidade.Atributo) entidade
						.obterAtributo("situacao");

				if (situacao != null)
					situacao.atualizarValor("Suspensa");

				this.setResponseView(new PaginaInicialView(usuario, usuario));
			}

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(inscricao));
			mm.rollbackTransaction();
		}
	}

	public void suspenderInscricao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		//EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Inscricao inscricao = (Inscricao) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try
		{

			if (action.getBoolean("view"))
				this.setResponseView(new SuspenderView(inscricao, action));
			else
			{
				Date data = action.getDate("data");
				if(data == null)
					throw new Exception("Fecha en Blanco");
				
				//if (inscricao.validarResolucao(action.getString("numero")))
					//throw new Exception("A Resolución Nº " + action.getString("numero")	+ " não fue encontrada");

				inscricao.adicionarComentario("Inscripción suspensa por "+ usuario.obterNome() + " teniendo por base la Resolución " +  action.getString("numero") + " - Fecha " + new SimpleDateFormat("dd/MM/yyyy").format(data), action.getString("comentario"));
				inscricao.atualizarResponsavel(inscricao.obterCriador());
				inscricao.atualizarSituacao(Inscricao.SUSPENSA);

				Entidade origem = inscricao.obterOrigem();

				Entidade.Atributo situacao = (Entidade.Atributo) origem.obterAtributo("situacao");
				situacao.atualizarValor(Inscricao.SUSPENSA);
				
				//inscricao.addSuspensao(data);
				String nomeAgente = origem.obterNome();
				
				nomeAgente+= " # Suspensión " + action.getString("numero") + " Fecha " + new SimpleDateFormat("dd/MM/yyyy").format(data);
				origem.atribuirNome(nomeAgente);
				origem.atualizar();

				this.setResponseView(new EventoView(inscricao));
			}
			mm.commitTransaction();
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(inscricao));
			mm.rollbackTransaction();
		}
	}

	public void cancelarInscricao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Inscricao inscricao = (Inscricao) eventoHome.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {

			if (action.getBoolean("view"))
			{
				this.setResponseView(new CancelarView(inscricao, action));
			}
			else
			{
				Date data = action.getDate("data");
				if(data == null)
					throw new Exception("Fecha en Blanco");
				
				/*if (inscricao.validarResolucao(action.getString("numero")))
					throw new Exception("A Resolución Nº "	+ action.getString("numero") + " não fue encontrada ou fue aprovada");*/

				inscricao.adicionarComentario("Inscripción cancelada por " + usuario.obterNome() + " teniendo por base la Resolución " +  action.getString("numero") + " - Fecha " + new SimpleDateFormat("dd/MM/yyyy").format(data), action.getString("comentario"));
				inscricao.atualizarResponsavel(inscricao.obterCriador());
				inscricao.atualizarSituacao(Inscricao.CANCELADA);

				Entidade origem = inscricao.obterOrigem();

				Entidade.Atributo situacao = (Entidade.Atributo) origem.obterAtributo("situacao");
				situacao.atualizarValor(Inscricao.CANCELADA);
				
				String nomeAgente = origem.obterNome();
				
				nomeAgente+= " # Cancelación " + action.getString("numero") + " Fecha " + new SimpleDateFormat("dd/MM/yyyy").format(data);
				origem.atribuirNome(nomeAgente);
				origem.atualizar();

				this.setResponseView(new EventoView(inscricao));
			}

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(inscricao));
			mm.rollbackTransaction();
		}
	}

	public void visualizarInsricoes(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		InscricaoHome inscricaoHome = (InscricaoHome) mm.getHome("InscricaoHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		mm.beginTransaction();
		try 
		{

			int pagina = action.getInt("_pagina");
			
			long[] colecaoAnterior = action.getLongArray("inscricaoId");
			
			Collection inscricoesAnteriores = new ArrayList();
			
			Collection inscricoesFiltradas = new ArrayList();
			
			for(int i = 0 ; i < colecaoAnterior.length ; i++)
			{
				long id = colecaoAnterior[i];
				
				Inscricao inscricao = (Inscricao) eventoHome.obterEventoPorId(id);
				
				inscricoesAnteriores.add(inscricao);
				
			}

			if (pagina <= 0)
				pagina = 1;
			
			/*System.out.println("Pagina: " + pagina);
			System.out.println("Tipo: " + action.getString("tipo"));
			System.out.println("Status: " + action.getString("status"));
*/
			Collection inscricoes = new ArrayList();
			
			if (action.getBoolean("lista"))
			{
				inscricoes = inscricaoHome.obterInscricoes(action.getString("tipo"), action.getString("status"), pagina);
				//System.out.println("Inscrições na Control: " + inscricoes.size());
			}
			
			if(pagina > 1)
			{
				for(Iterator i = inscricoes.iterator() ; i.hasNext() ; )
				{
					Inscricao inscricao = (Inscricao) i.next();
					
					if(!inscricoesAnteriores.contains(inscricao))
						inscricoesFiltradas.add(inscricao);
				}
			}
			else
				inscricoesFiltradas = inscricoes;

			this.setResponseView(new ListaInscricoesView(inscricoesFiltradas, action.getBoolean("lista"), action.getString("tipo"), action.getString("status"), pagina));

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuario, usuario));
			mm.rollbackTransaction();
		}
	}
}