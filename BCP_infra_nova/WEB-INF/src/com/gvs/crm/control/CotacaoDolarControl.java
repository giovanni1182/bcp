package com.gvs.crm.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.CotacaoDolar;
import com.gvs.crm.model.CotacaoDolarHome;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.model.Uteis;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.RelCotacaoDolarView;

import infra.control.Action;
import infra.control.Control;

public class CotacaoDolarControl extends Control
{
	public void incluirCotacaoDolar(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		CotacaoDolarHome home = (CotacaoDolarHome) mm.getHome("CotacaoDolarHome");
		
		CotacaoDolar cotacao = (CotacaoDolar) mm.getEntity("CotacaoDolar");
		Uteis uteis = new Uteis();
		mm.beginTransaction();
		try 
		{
			Entidade destino = entidadeHome.obterEntidadePorApelido("bcp");
			int mes = action.getInt("mes");
			int ano = action.getInt("ano");
			double valor = action.getDouble("cotacao");
			
			cotacao.atribuirMes(mes);
			cotacao.atribuirAno(ano);
			cotacao.atribuirCotacao(valor);
			
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
				throw new Exception("Cotización es zero");
			
			if(!home.temCotacao(mes, ano, null))
				throw new Exception("Ya existe una Cotización en " + mesStr + "/" + ano);
			
			Date dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);
			
			cotacao.atribuirTitulo("Cotización del dólar " + mesStr+"/"+ano);

			cotacao.atribuirOrigem(destino);
			cotacao.atribuirDestino(destino);
			cotacao.atribuirResponsavel(usuarioAtual);
			cotacao.atribuirDataPrevistaInicio(dataInicio);
			cotacao.incluir();
			
			cotacao.atualizarFase(CotacaoDolar.EVENTO_CONCLUIDO);

			this.setResponseView(new EventoView(cotacao));
			mm.commitTransaction();
		} 
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(cotacao));
			mm.rollbackTransaction();
		}
	}
	
	public void atualizarCotacaoDolar(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EventoHome home = (EventoHome) mm.getHome("EventoHome");
		CotacaoDolar cotacao = (CotacaoDolar) home.obterEventoPorId(action.getLong("id"));
		CotacaoDolarHome cotacaoHome = (CotacaoDolarHome) mm.getHome("CotacaoDolarHome");
		
		Uteis uteis = new Uteis();
		mm.beginTransaction();
		try 
		{
			int mes = action.getInt("mes");
			int ano = action.getInt("ano");
			double valor = action.getDouble("cotacao");
			
			cotacao.atribuirMes(mes);
			cotacao.atribuirAno(ano);
			cotacao.atribuirCotacao(valor);
			
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
				throw new Exception("Cotización es zero");
			
			if(!cotacaoHome.temCotacao(mes, ano, cotacao))
				throw new Exception("Ya existe una Cotización en " + mesStr + "/" + ano);
			
			Date dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);
			
			cotacao.atualizarTitulo("Cotización del dólar " + mesStr+"/"+ano);
			cotacao.atualizarMes(mes);
			cotacao.atualizarAno(ano);
			cotacao.atualizarCotacao(valor);
			cotacao.atualizarDataPrevistaInicio(dataInicio);

			this.setResponseView(new EventoView(cotacao));
			mm.commitTransaction();
		} 
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(cotacao));
			mm.rollbackTransaction();
		}
	}
	
	public void relCotacaoDolar(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		CotacaoDolarHome home = (CotacaoDolarHome) mm.getHome("CotacaoDolarHome");
		Collection<CotacaoDolar> cotacoes = new ArrayList<CotacaoDolar>();
		int mes = 0;
		int ano = 0;
		try 
		{
			if(!action.getBoolean("view"))
			{
				mes = action.getInt("mes");
				ano = action.getInt("ano");
				
				cotacoes = home.obterCotacoes(mes, ano);
			}

			this.setResponseView(new RelCotacaoDolarView(mes,ano,cotacoes));
		} 
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new RelCotacaoDolarView(mes,ano,cotacoes));
		}
	}
}
