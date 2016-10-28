package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.CodificacaoCobertura;
import com.gvs.crm.model.CodificacaoPlano;
import com.gvs.crm.model.CodificacoesHome;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.ListaCodificacaoCoberturaView;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class CodificacaoCoberturaControl extends Control 
{
	public void incluirCodificacaoCobertura(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		CodificacoesHome codificacoesHome = (CodificacoesHome) mm.getHome("CodificacoesHome");
		CodificacaoCobertura cobertura = (CodificacaoCobertura) mm.getEntity("CodificacaoCobertura");
		CodificacaoPlano plano = (CodificacaoPlano) eventoHome.obterEventoPorId(action.getLong("superiorId"));
		mm.beginTransaction();
		try 
		{
			cobertura.atribuirTitulo(action.getString("titulo"));
			if(action.getString("codigo") == null || action.getString("codigo").equals(""))
				throw new Exception("Digite o Codigo");
			if(action.getString("codigo").length() < 3)
				throw new Exception("O Codigo deve ter 3 digitos");
			if(action.getString("titulo") == null || action.getString("titulo").equals(""))
				throw new Exception("Digite o Nombre de la Cobertura");
			
			boolean codigoJaExiste = codificacoesHome.verificaCodigoCobertura(action.getString("codigo"),plano);
			
			if(codigoJaExiste)
				throw new Exception("O Codigo " + action.getString("codigo") + " está siendo utilizado por otra Cobertura");
			
			Entidade origem = entidadeHome.obterEntidadePorApelido("bcp");
			cobertura.atribuirOrigem(origem);
			cobertura.atribuirResponsavel(usuarioAtual);
			cobertura.atribuirTitulo(action.getString("titulo"));
			cobertura.atribuirSuperior(plano);
			cobertura.incluir();
			
			cobertura.atualizarCodigo(action.getString("codigo"));
			cobertura.atualizarFase(Evento.EVENTO_CONCLUIDO);
			
			mm.commitTransaction();
			
			this.setAlert("Cobertura Incluida");
			
			this.setResponseView(new EventoView(cobertura));
		
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(cobertura));
			mm.rollbackTransaction();
		}
	}
	
	public void atualizarCodificacaoCobertura(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		CodificacoesHome codificacoesHome = (CodificacoesHome) mm.getHome("CodificacoesHome");
		CodificacaoCobertura cobertura = (CodificacaoCobertura) eventoHome.obterEventoPorId(action.getLong("id"));
		CodificacaoPlano plano = (CodificacaoPlano) cobertura.obterSuperior();
		mm.beginTransaction();
		try 
		{
			cobertura.atribuirTitulo(action.getString("titulo"));
			if(action.getString("codigo") == null || action.getString("codigo").equals(""))
				throw new Exception("Digite o Codigo");
			if(action.getString("codigo").length() < 3)
				throw new Exception("O Codigo deve ter 3 digitos");
			if(action.getString("titulo") == null || action.getString("titulo").equals(""))
				throw new Exception("Digite o Nombre de la Cobertura");
			
			if(!action.getString("codigo").equals(cobertura.obterCodigo()))
			{
				boolean codigoJaExiste = codificacoesHome.verificaCodigoCobertura(action.getString("codigo"),plano);
				
				if(codigoJaExiste)
					throw new Exception("O Codigo " + action.getString("codigo") + " está siendo utilizado por otra Cobertura");
			}
			
			cobertura.atualizarTitulo(action.getString("titulo"));
			cobertura.atualizarCodigo(action.getString("codigo"));
			
			mm.commitTransaction();
			
			this.setAlert("Cobertura Actualizada");
			
			this.setResponseView(new EventoView(cobertura));
		
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(cobertura));
			mm.rollbackTransaction();
		}
	}
	
	public void listarCodificacaoCoberturas(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		CodificacoesHome codificacoesHome = (CodificacoesHome) mm.getHome("CodificacoesHome");
		mm.beginTransaction();
		try 
		{
			this.setResponseView(new ListaCodificacaoCoberturaView(codificacoesHome.obterCodificacaoCoberturas()));
		
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuarioAtual,usuarioAtual));
			mm.rollbackTransaction();
		}
	}
}
