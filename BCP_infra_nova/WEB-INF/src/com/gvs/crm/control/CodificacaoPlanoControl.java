package com.gvs.crm.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.CodificacaoPlano;
import com.gvs.crm.model.CodificacoesHome;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.ListaCodificacaoPlanosView;
import com.gvs.crm.view.PaginaInicialView;
import com.gvs.crm.view.VisualizarCodificacaoPlanosView;

import infra.control.Action;
import infra.control.Control;

public class CodificacaoPlanoControl extends Control 
{
	public void incluirCodificacaoPlano(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		CodificacoesHome codificacoesHome = (CodificacoesHome) mm.getHome("CodificacoesHome");
		CodificacaoPlano plano = (CodificacaoPlano) mm.getEntity("CodificacaoPlano");
		mm.beginTransaction();
		try 
		{
			plano.atribuirTitulo(action.getString("titulo"));
			if(action.getString("codigo") == null || action.getString("codigo").equals(""))
				throw new Exception("Digite o Codigo");
			if(action.getString("codigo").length() < 3)
				throw new Exception("O Codigo deve ter 3 digitos");
			if(action.getString("titulo") == null || action.getString("titulo").equals(""))
				throw new Exception("Digite o Nombre del Plan");
			
			boolean codigoJaExiste = codificacoesHome.verificaCodigoPlano(action.getString("codigo"));
			
			if(codigoJaExiste)
				throw new Exception("O Codigo " + action.getString("codigo") + " está siendo utilizado por otro Plan");
			
			Entidade origem = entidadeHome.obterEntidadePorApelido("bcp");
			plano.atribuirOrigem(origem);
			plano.atribuirResponsavel(usuarioAtual);
			plano.atribuirTitulo(action.getString("titulo"));
			plano.incluir();
			
			plano.atualizarCodigo(action.getString("codigo"));
			plano.atualizarFase(Evento.EVENTO_CONCLUIDO);
			
			mm.commitTransaction();
			
			this.setAlert("Plan Incluido");
			
			this.setResponseView(new EventoView(plano));
		
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(plano));
			mm.rollbackTransaction();
		}
	}
	
	public void atualizarCodificacaoPlano(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		CodificacoesHome codificacoesHome = (CodificacoesHome) mm.getHome("CodificacoesHome");
		CodificacaoPlano plano = (CodificacaoPlano) eventoHome.obterEventoPorId(action.getLong("id"));
		mm.beginTransaction();
		try 
		{
			plano.atribuirTitulo(action.getString("titulo"));
			if(action.getString("codigo") == null || action.getString("codigo").equals(""))
				throw new Exception("Digite o Codigo");
			if(action.getString("codigo").length() < 3)
				throw new Exception("O Codigo deve ter 3 digitos");
			if(action.getString("titulo") == null || action.getString("titulo").equals(""))
				throw new Exception("Digite o Nombre del Plan");
			
			if(!action.getString("codigo").equals(plano.obterCodigo()))
			{
				boolean codigoJaExiste = codificacoesHome.verificaCodigoPlano(action.getString("codigo"));
				
				if(codigoJaExiste)
					throw new Exception("O Codigo " + action.getString("codigo") + " está siendo utilizado por otro Plan");
			}
			
			plano.atualizarTitulo(action.getString("titulo"));
			plano.atualizarCodigo(action.getString("codigo"));
			
			mm.commitTransaction();
			
			this.setAlert("Plan Actualizado");
			
			this.setResponseView(new EventoView(plano));
		
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(plano));
			mm.rollbackTransaction();
		}
	}
	
	public void listarCodificacaoPlanos(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		CodificacoesHome codificacoesHome = (CodificacoesHome) mm.getHome("CodificacoesHome");
		mm.beginTransaction();
		try 
		{
			this.setResponseView(new ListaCodificacaoPlanosView(codificacoesHome.obterCodificacaoPlanos()));
		
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuarioAtual,usuarioAtual));
			mm.rollbackTransaction();
		}
	}
	
	public void visualizarCodificacaoPlanos(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		CodificacoesHome codificacoesHome = (CodificacoesHome) mm.getHome("CodificacoesHome");
		mm.beginTransaction();
		try 
		{
			Collection sinistros = new ArrayList();
			Date dataInicio = action.getDate("dataInicio");
			Date dataFim = action.getDate("dataFim");
			
			
			this.setResponseView(new VisualizarCodificacaoPlanosView(sinistros, dataInicio, dataFim));
		
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuarioAtual,usuarioAtual));
			mm.rollbackTransaction();
		}
	}
}
