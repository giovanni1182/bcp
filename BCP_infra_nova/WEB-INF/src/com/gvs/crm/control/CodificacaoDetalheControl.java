package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.CodificacaoDetalhe;
import com.gvs.crm.model.CodificacaoRisco;
import com.gvs.crm.model.CodificacoesHome;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;

import infra.control.Action;
import infra.control.Control;

public class CodificacaoDetalheControl extends Control 
{
	public void incluirCodificacaoDetalhe(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		CodificacoesHome codificacoesHome = (CodificacoesHome) mm.getHome("CodificacoesHome");
		CodificacaoDetalhe detalhe = (CodificacaoDetalhe) mm.getEntity("CodificacaoDetalhe");
		CodificacaoRisco risco = (CodificacaoRisco) eventoHome.obterEventoPorId(action.getLong("superiorId"));
		mm.beginTransaction();
		try 
		{
			detalhe.atribuirTitulo(action.getString("titulo"));
			if(action.getString("codigo") == null || action.getString("codigo").equals(""))
				throw new Exception("Digite o Codigo");
			if(action.getString("codigo").length() < 3)
				throw new Exception("O Codigo deve ter 3 digitos");
			if(action.getString("titulo") == null || action.getString("titulo").equals(""))
				throw new Exception("Digite o Nombre del Detalle");
			
			boolean codigoJaExiste = codificacoesHome.verificaCodigoDetalhe(action.getString("codigo"),risco);
			
			if(codigoJaExiste)
				throw new Exception("O Codigo " + action.getString("codigo") + " está siendo utilizado por otro Detalle");
			
			Entidade origem = entidadeHome.obterEntidadePorApelido("bcp");
			detalhe.atribuirOrigem(origem);
			detalhe.atribuirResponsavel(usuarioAtual);
			detalhe.atribuirTitulo(action.getString("titulo"));
			detalhe.atribuirSuperior(risco);
			detalhe.incluir();
			
			detalhe.atualizarCodigo(action.getString("codigo"));
			detalhe.atualizarFase(Evento.EVENTO_CONCLUIDO);
			
			mm.commitTransaction();
			
			this.setAlert("Detalle Incluido");
			
			this.setResponseView(new EventoView(detalhe));
		
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(detalhe));
			mm.rollbackTransaction();
		}
	}
	
	public void atualizarCodificacaoDetalhe(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		CodificacoesHome codificacoesHome = (CodificacoesHome) mm.getHome("CodificacoesHome");
		CodificacaoDetalhe detalhe = (CodificacaoDetalhe) eventoHome.obterEventoPorId(action.getLong("id"));
		CodificacaoRisco risco = (CodificacaoRisco) detalhe.obterSuperior();
		mm.beginTransaction();
		try 
		{
			detalhe.atribuirTitulo(action.getString("titulo"));
			if(action.getString("codigo") == null || action.getString("codigo").equals(""))
				throw new Exception("Digite o Codigo");
			if(action.getString("codigo").length() < 3)
				throw new Exception("O Codigo deve ter 3 digitos");
			if(action.getString("titulo") == null || action.getString("titulo").equals(""))
				throw new Exception("Digite o Nombre del Detalle");
			
			if(!action.getString("codigo").equals(detalhe.obterCodigo()))
			{
				boolean codigoJaExiste = codificacoesHome.verificaCodigoDetalhe(action.getString("codigo"),risco);
				
				if(codigoJaExiste)
					throw new Exception("O Codigo " + action.getString("codigo") + " está siendo utilizado por otro Detalle");
			}
			
			detalhe.atualizarTitulo(action.getString("titulo"));
			detalhe.atualizarCodigo(action.getString("codigo"));
			
			mm.commitTransaction();
			
			this.setAlert("Detalle Actualizado");
			
			this.setResponseView(new EventoView(detalhe));
		
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(detalhe));
			mm.rollbackTransaction();
		}
	}
}
