package com.gvs.crm.control;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Processamento;
import com.gvs.crm.model.Processamento.Agenda;
import com.gvs.crm.model.ProcessamentoHome;
import com.gvs.crm.report.ValidacoesXLS;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.RelProcessamentoView;

import infra.control.Action;
import infra.control.Control;

public class ProcessamentoControl extends Control
{
	public void relProcessamentos(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		ProcessamentoHome home = (ProcessamentoHome) mm.getHome("ProcessamentoHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		
		Entidade aseguradora = null;
		int mes = 0;
		int ano = 0;
		String tipo = "";
		Date dataInicio = null;
		Date dataFim = null;
		Collection<Agenda> agendas = new ArrayList<Agenda>();
		int erro = action.getInt("erro");
		try 
		{
			if(!action.getBoolean("view"))
			{
				mes = action.getInt("mes");
				ano = action.getInt("ano");
				tipo = action.getString("tipo");
				erro = action.getInt("erro");
				dataInicio = action.getDate("dataInicio");
				dataFim = action.getDate("dataFim");
				
				if(dataFim!=null)
				{
					String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
					dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				}
				
				long aseguradoraId = action.getLong("aseguradoraId");
				if(aseguradoraId > 0)
					aseguradora = entidadeHome.obterEntidadePorId(aseguradoraId);
				
				if(action.getBoolean("excel"))
				{
					Collection<Aseguradora> aseguradoras = new ArrayList<Aseguradora>();
					if(aseguradora!=null)
						aseguradoras.add((Aseguradora)aseguradora);
					else
						aseguradoras = aseguradoraHome.obterAseguradoras();
					
					ValidacoesXLS xls = new ValidacoesXLS(aseguradoras,mes,ano,tipo,erro,dataInicio,dataFim,home);
					
					InputStream arquivo = xls.obterArquivo();
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Validaciones.xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
				{
					agendas = home.obterAgendas(aseguradora, mes, ano, tipo, erro, dataInicio, dataFim);
					this.setResponseView(new RelProcessamentoView(aseguradora, mes, ano, tipo, agendas, erro, dataInicio, dataFim));
				}
			}
			else
				this.setResponseView(new RelProcessamentoView(aseguradora, mes, ano, tipo, agendas, erro, dataInicio, dataFim));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new RelProcessamentoView(aseguradora, mes, ano, tipo, agendas, erro, dataInicio, dataFim));
		}
	}
	
	public void forcarProcessamento(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EventoHome home = (EventoHome) mm.getHome("EventoHome");
		Processamento processamento = (Processamento) home.obterEventoPorId(action.getLong("id"));
		String nomeArquivo = action.getString("arquivo");
		mm.beginTransaction();
		try 
		{
			processamento.forcarProcessamento(nomeArquivo);
			
			this.setResponseView(new EventoView(processamento));
			
			mm.commitTransaction();
		}
		catch (Exception exception)
		{
			mm.rollbackTransaction();
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(processamento));
		}
	}
}
