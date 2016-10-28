package com.gvs.crm.control;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.IndicadoresHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.report.IndicadoresReport;
import com.gvs.crm.report.IndicadoresXLS;
import com.gvs.crm.view.CalcularIndicadoresView;
import com.gvs.crm.view.CalculoIndicadoresXLS;
import com.gvs.crm.view.PaginaInicialView;
import com.gvs.crm.view.VisualizarCalculoIndicadoresView;

import infra.control.Action;
import infra.control.Control;

public class IndicadorControl extends Control
{
	public void calcularTecnicos(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		IndicadoresHome indicadorHome = (IndicadoresHome) mm.getHome("IndicadoresHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario u = usuarioHome.obterUsuarioPorUser(this.getUser());
		Date dataTecnicos = action.getDate("dataTecnicos");
		boolean mostraTela = false;
		boolean geraArquivo = false;
		boolean pdf = false;
		boolean xls = false;
		boolean calculosXls = false;
		try
		{
			/*String telaStr = action.getString("tela");
			String arquivoStr = action.getString("arquivo");
			String pdfStr = action.getString("pdf");*/
			
			String gerar = action.getString("gerar");
			
			if(gerar.equals(""))
				throw new Exception("Opciones en blanco");
			
			if(gerar.equals("tela"))
				mostraTela = true;
			else if(gerar.equals("arquivo"))
				geraArquivo = true;
			else if(gerar.equals("pdf"))
				pdf = true;
			else if(gerar.equals("xls"))
				xls = true;
			else if(gerar.equals("xlsI"))
				calculosXls = true;
			
			if(dataTecnicos == null)
				throw new Exception("Fecha en blanco");
			
			if(geraArquivo)
			{
				indicadorHome.calcularIndicadoresTecnicos(dataTecnicos,u,geraArquivo);
				this.setResponseView(new CalcularIndicadoresView(u,dataTecnicos, mostraTela,geraArquivo,pdf,xls));
				this.setAlert("Archivo Generado");
			}
			else if(mostraTela)
				this.setResponseView(new VisualizarCalculoIndicadoresView(u, dataTecnicos,mostraTela,geraArquivo));
			else if(pdf)
			{
				AseguradoraHome aseguradorHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
				indicadorHome.calcularIndicadoresTecnicos(dataTecnicos, null, false);
				this.setResponseReport(new IndicadoresReport(aseguradorHome.obterAseguradorasPorMenor80OrdenadoPorNome(),indicadorHome,dataTecnicos));
			}
			else if(xls)
			{
				indicadorHome.calcularIndicadoresTecnicos(dataTecnicos, null, false);
				
				IndicadoresXLS xls2 = new IndicadoresXLS(aseguradoraHome.obterAseguradorasPorMenor80OrdenadoPorNome(), dataTecnicos, indicadorHome);
				
				this.setResponseInputStream(xls2.obterArquivo());
		        this.setResponseFileName("Indicadores Financeiros " + new SimpleDateFormat("dd/MM/yyyy").format(dataTecnicos) + ".xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(xls2.obterArquivo().available());
			}
			else if(calculosXls)
			{
				//indicadorHome.calcularIndicadoresTecnicos(dataTecnicos, null, false);
				
				CalculoIndicadoresXLS xls2 = new CalculoIndicadoresXLS(aseguradoraHome.obterAseguradorasPorMenor80OrdenadoPorNome(), dataTecnicos, indicadorHome, entidadeHome);
				
				this.setResponseInputStream(xls2.obterArquivo());
		        this.setResponseFileName("Calculo Indicadores Financeiros " + new SimpleDateFormat("dd/MM/yyyy").format(dataTecnicos) + ".xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(xls2.obterArquivo().available());
			}
		}
		catch (Exception e)
		{
			this.setAlert(Util.translateException(e));
			this.setResponseView(new CalcularIndicadoresView(u,dataTecnicos, mostraTela,geraArquivo,pdf,xls));
		}
	}
	
	public void atualizarMagenSolvencia(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		IndicadoresHome indicadorHome = (IndicadoresHome) mm.getHome("IndicadoresHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario u = usuarioHome.obterUsuarioPorUser(this.getUser());
		
		Date data = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2008");
		//indicadorHome.obterSinistrosBrutosPD(data);
		
		this.setAlert("Pronto");
		
		this.setResponseView(new PaginaInicialView(u,u));
	}
	
	public void visualizarIndicadores(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario u = usuarioHome.obterUsuarioPorUser(this.getUser());
		
		this.setResponseView(new CalcularIndicadoresView(u,action.getDate("dataTecnicos"),action.getBoolean("tela"),action.getBoolean("arquivo"),action.getBoolean("pdf"),action.getBoolean("xls")));
	}
}
