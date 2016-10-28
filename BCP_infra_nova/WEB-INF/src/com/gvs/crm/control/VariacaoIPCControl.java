package com.gvs.crm.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.model.Uteis;
import com.gvs.crm.model.VariacaoIPC;
import com.gvs.crm.model.VariacaoIPCHome;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.RelVariacaoIPCView;

import infra.control.Action;
import infra.control.Control;

public class VariacaoIPCControl extends Control 
{
	public void incluirVariacaoIPC(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		VariacaoIPCHome home = (VariacaoIPCHome) mm.getHome("VariacaoIPCHome");
		
		VariacaoIPC variacao = (VariacaoIPC) mm.getEntity("VariacaoIPC");
		Uteis uteis = new Uteis();
		mm.beginTransaction();
		try 
		{
			Entidade destino = entidadeHome.obterEntidadePorApelido("bcp");
			int mes = action.getInt("mes");
			int ano = action.getInt("ano");
			double valor = action.getDouble("variacao");
			
			variacao.atribuirMes(mes);
			variacao.atribuirAno(ano);
			variacao.atribuirVariacao(valor);
			
			if(mes == 0)
				throw new Exception("Mes en Blanco");
			if(mes>12)
				throw new Exception("Mes no es correcto");
			if(ano == 0)
				throw new Exception("Año en Blanco");

			String mesStr = new Integer(mes).toString();
			if(mesStr.length() == 1)
				mesStr = "0"+mes;

			String dataStr = "01/"+mes+"/"+ano;
			if(!uteis.validaData(dataStr))
				throw new Exception("Fecha no es correcta");
			
			if(valor == 0)
				throw new Exception("Variación es zero");
			
			if(!home.temVariacao(mes, ano, null))
				throw new Exception("Ya existe una Variación en " + mesStr + "/" + ano);
			
			Date dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);
			
			variacao.atribuirTitulo("Variación IPC " + mesStr+"/"+ano);

			variacao.atribuirOrigem(destino);
			variacao.atribuirDestino(destino);
			variacao.atribuirResponsavel(usuarioAtual);
			variacao.atribuirDataPrevistaInicio(dataInicio);
			variacao.incluir();
			
			variacao.atualizarFase(VariacaoIPC.EVENTO_CONCLUIDO);

			this.setResponseView(new EventoView(variacao));
			mm.commitTransaction();
		} 
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(variacao));
			mm.rollbackTransaction();
		}
	}
	
	public void atualizarVariacaoIPC(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EventoHome home = (EventoHome) mm.getHome("EventoHome");
		VariacaoIPC variacao = (VariacaoIPC) home.obterEventoPorId(action.getLong("id"));
		VariacaoIPCHome variacaoHome = (VariacaoIPCHome) mm.getHome("VariacaoIPCHome");
		
		Uteis uteis = new Uteis();
		mm.beginTransaction();
		try 
		{
			int mes = action.getInt("mes");
			int ano = action.getInt("ano");
			double valor = action.getDouble("variacao");
			
			variacao.atribuirMes(mes);
			variacao.atribuirAno(ano);
			variacao.atribuirVariacao(valor);
			
			if(mes == 0)
				throw new Exception("Mes en Blanco");
			if(mes>12)
				throw new Exception("Mes no es correcto");
			if(ano == 0)
				throw new Exception("Año en Blanco");

			String mesStr = new Integer(mes).toString();
			if(mesStr.length() == 1)
				mesStr = "0"+mes;

			String dataStr = "01/"+mes+"/"+ano;
			if(!uteis.validaData(dataStr))
				throw new Exception("Fecha no es correcta");
			
			if(valor == 0)
				throw new Exception("Variación es zero");
			
			if(!variacaoHome.temVariacao(mes, ano, variacao))
				throw new Exception("Ya existe una Variación en " + mesStr + "/" + ano);
			
			Date dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);
			
			variacao.atualizarTitulo("Variación IPC " + mesStr+"/"+ano);
			variacao.atualizarMes(mes);
			variacao.atualizarAno(ano);
			variacao.atualizarVariacao(valor);
			variacao.atualizarDataPrevistaInicio(dataInicio);

			this.setResponseView(new EventoView(variacao));
			mm.commitTransaction();
		} 
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(variacao));
			mm.rollbackTransaction();
		}
	}
	
	public void relVariacaoIPC(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		VariacaoIPCHome home = (VariacaoIPCHome) mm.getHome("VariacaoIPCHome");
		Collection<VariacaoIPC> variacoes = new ArrayList<VariacaoIPC>();
		int mes = 0;
		int ano = 0;
		try 
		{
			if(!action.getBoolean("view"))
			{
				mes = action.getInt("mes");
				ano = action.getInt("ano");
				
				variacoes = home.obterVariacoes(mes, ano);
			}

			this.setResponseView(new RelVariacaoIPCView(mes,ano,variacoes));
		} 
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new RelVariacaoIPCView(mes,ano,variacoes));
		}
	}
}
