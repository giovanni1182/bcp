package com.gvs.crm.control;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.DadosReaseguroHome;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.report.CoberturaReaseguroPDF;
import com.gvs.crm.report.CoberturaReasegurosXLS;
import com.gvs.crm.report.ReasegurosXLS;
import com.gvs.crm.view.ApolicesReaseguroView;
import com.gvs.crm.view.CoberturaReasegurosView;
import com.gvs.crm.view.ReasegurosView;

import infra.control.Action;
import infra.control.Control;

public class ReaseguroControl extends Control
{
	public void visualizarReaseguros(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());

		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");

		Map reaseguradoras = new TreeMap();

		mm.beginTransaction();
		try {

			if (action.getBoolean("listar"))
			{
				if (action.getLong("aseguradoraId") == 0)
					throw new Exception("Elegir la Aseguradora");
				if (dataInicio == null)
					throw new Exception("Elegir la Fecha Inicio");
				if (dataFim == null)
					throw new Exception("Elegir la Fecha Final");

				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " 00:00:00";
				dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
				
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				
				aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				reaseguradoras = aseguradoraHome.obterReaseguradoras(aseguradora, dataInicio, dataFim);
			}
			
			if(!action.getBoolean("excel"))
				this.setResponseView(new ReasegurosView(aseguradora, dataInicio,dataFim, action.getBoolean("listar"), reaseguradoras));
			else
			{
				String textoUsuario = "Generado : " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				
				ReasegurosXLS xls = new ReasegurosXLS(aseguradora, dataInicio,dataFim, reaseguradoras, entidadeHome, textoUsuario);
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio);
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim);
				
				dataInicioStr = dataInicioStr.replace("/", "_");
				dataFimStr = dataFimStr.replace("/", "_");
				
				String hora = new SimpleDateFormat("HH:mm").format(new Date());
				
				InputStream arquivo = xls.obterArquivo();
				this.setResponseInputStream(arquivo);
		        this.setResponseFileName("Aseguradora-Reaseguros_" + aseguradora.obterNome() +"_"+usuarioAtual.obterNome()+ "_" + dataInicioStr + "_" + dataFimStr +"_" + hora+".xls");
				//this.setResponseFileName("Aseguradora - Reaseguros_" + aseguradora.obterNome() + ".xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(arquivo.available());
			}
			
			mm.commitTransaction();

		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ReasegurosView(aseguradora, dataInicio,dataFim, false, reaseguradoras));
			mm.rollbackTransaction();
		}
	}
	
	public void coberturaReaseguros(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		DadosReaseguroHome dadosHome = (DadosReaseguroHome) mm.getHome("DadosReaseguroHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());

		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		String tipoValor = action.getString("tipoValor");
		String situacao = action.getString("situacao");
		Collection<String> dados = new ArrayList<String>();

		try
		{
			if (action.getBoolean("view"))
				this.setResponseView(new CoberturaReasegurosView(aseguradora, dataInicio, dataFim, situacao, tipoValor,dados));
			else
			{
				if(action.getLong("aseguradora") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradora"));
				
				if(dataInicio == null)
					throw new Exception("Fecha Inicio en blanco");
				if(dataFim == null)
					throw new Exception("Fecha Fin en blanco");
				
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				
				if(!action.getString("tipo").equals(""))
				{
					Collection<Apolice> apolices = dadosHome.obterApolicesDadosReaseguro(aseguradora, dataInicio, dataFim, situacao, action.getString("tipo"));
					this.setResponseView(new ApolicesReaseguroView(apolices, "Cobertura de Reaseguros - " + action.getString("tipo")));
				}
				else
				{
					dados = dadosHome.obterDadosReaseguro(aseguradora, dataInicio, dataFim, situacao,tipoValor);
					this.setResponseView(new CoberturaReasegurosView(aseguradora, dataInicio, dataFim, situacao, tipoValor,dados));
				}
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado : " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					CoberturaReasegurosXLS xls = new CoberturaReasegurosXLS(aseguradora, dataInicio, dataFim, situacao, tipoValor, dados, textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Cobertura de Reaseguros.xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else if(action.getBoolean("pdf"))
				{
					Collection<Apolice> apolices = dadosHome.obterApolicesDadosReaseguro(aseguradora, dataInicio, dataFim, situacao, action.getString("tipo"));
					
					String textoUsuario = "Generado : " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					CoberturaReaseguroPDF pdf = new CoberturaReaseguroPDF(aseguradora, dataInicio, dataFim, situacao, apolices, action.getString("tipo"), textoUsuario, "Cobertura de Reaseguros");
					
					InputStream arquivo = pdf.obterArquivo();
					this.setResponseInputStream(arquivo);
					if(aseguradora!=null)
						this.setResponseFileName("Cobertura de Reaseguros_"+aseguradora.obterNome()+"_"+action.getString("tipo")+".pdf");
					else
						this.setResponseFileName("Cobertura de Reaseguros_"+action.getString("tipo")+".pdf");
			        this.setResponseContentType("application/pdf");
			        this.setResponseContentSize(arquivo.available());
				}
			}
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new CoberturaReasegurosView(aseguradora, dataInicio, dataFim, situacao, tipoValor,dados));
		}
	}
}