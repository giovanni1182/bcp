package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.CodificacaoCobertura;
import com.gvs.crm.model.CodificacaoRisco;
import com.gvs.crm.model.CodificacoesHome;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.ListarCodificacaoRiscosView;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class CodificacaoRiscoControl extends Control 
{
	public void incluirCodificacaoRisco(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		CodificacoesHome codificacoesHome = (CodificacoesHome) mm.getHome("CodificacoesHome");
		CodificacaoRisco risco = (CodificacaoRisco) mm.getEntity("CodificacaoRisco");
		CodificacaoCobertura cobertura = (CodificacaoCobertura) eventoHome.obterEventoPorId(action.getLong("superiorId"));
		mm.beginTransaction();
		try 
		{
			risco.atribuirTitulo(action.getString("titulo"));
			if(action.getString("codigo") == null || action.getString("codigo").equals(""))
				throw new Exception("Digite o Codigo");
			if(action.getString("codigo").length() < 3)
				throw new Exception("O Codigo deve ter 3 digitos");
			if(action.getString("titulo") == null || action.getString("titulo").equals(""))
				throw new Exception("Digite o Nombre del Riesgo");
			
			boolean codigoJaExiste = codificacoesHome.verificaCodigoRisco(action.getString("codigo"),cobertura);
			
			if(codigoJaExiste)
				throw new Exception("O Codigo " + action.getString("codigo") + " está siendo utilizado por otro Riesgo");
			
			Entidade origem = entidadeHome.obterEntidadePorApelido("bcp");
			risco.atribuirOrigem(origem);
			risco.atribuirResponsavel(usuarioAtual);
			risco.atribuirTitulo(action.getString("titulo"));
			risco.atribuirSuperior(cobertura);
			risco.incluir();
			
			risco.atualizarCodigo(action.getString("codigo"));
			cobertura.atualizarFase(Evento.EVENTO_CONCLUIDO);
			
			mm.commitTransaction();
			
			this.setAlert("Riesgo Incluido");
			
			this.setResponseView(new EventoView(risco));
		
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(risco));
			mm.rollbackTransaction();
		}
	}
	
	public void atualizarCodificacaoRisco(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		CodificacoesHome codificacoesHome = (CodificacoesHome) mm.getHome("CodificacoesHome");
		CodificacaoRisco risco = (CodificacaoRisco) eventoHome.obterEventoPorId(action.getLong("id"));
		CodificacaoCobertura cobertura = (CodificacaoCobertura) risco.obterSuperior();
		mm.beginTransaction();
		try 
		{
			risco.atribuirTitulo(action.getString("titulo"));
			if(action.getString("codigo") == null || action.getString("codigo").equals(""))
				throw new Exception("Digite o Codigo");
			if(action.getString("codigo").length() < 3)
				throw new Exception("O Codigo deve ter 3 digitos");
			if(action.getString("titulo") == null || action.getString("titulo").equals(""))
				throw new Exception("Digite o Nombre del Riesgo");
			
			if(!action.getString("codigo").equals(risco.obterCodigo()))
			{
				boolean codigoJaExiste = codificacoesHome.verificaCodigoRisco(action.getString("codigo"),cobertura);
				
				if(codigoJaExiste)
					throw new Exception("O Codigo " + action.getString("codigo") + " está siendo utilizado por otro Riesgo");
			}
			
			risco.atualizarTitulo(action.getString("titulo"));
			risco.atualizarCodigo(action.getString("codigo"));
			
			mm.commitTransaction();
			
			this.setAlert("Riesgo Actualizado");
			
			this.setResponseView(new EventoView(risco));
		
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(risco));
			mm.rollbackTransaction();
		}
	}
	
	public void listarCodificacaoRiscos(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		CodificacoesHome codificacoesHome = (CodificacoesHome) mm.getHome("CodificacoesHome");
		mm.beginTransaction();
		try 
		{
			this.setResponseView(new ListarCodificacaoRiscosView(codificacoesHome.obterCodificacaoRiscos()));
		
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuarioAtual,usuarioAtual));
			mm.rollbackTransaction();
		}
	}
}
