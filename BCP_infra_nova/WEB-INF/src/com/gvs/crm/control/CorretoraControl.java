package com.gvs.crm.control;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Corretora;
import com.gvs.crm.model.CorretoraHome;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.report.CorredorProdXLS;
import com.gvs.crm.report.CorretorReasegurosAseguradoraXLS;
import com.gvs.crm.report.CorretorReasegurosXLS;
import com.gvs.crm.report.MaioresCorretoresPDF;
import com.gvs.crm.report.MaioresCorretoresXLS;
import com.gvs.crm.view.CorredorProdView;
import com.gvs.crm.view.CorretorReasegurosAseguradoraView;
import com.gvs.crm.view.CorretorReasegurosView;
import com.gvs.crm.view.EntidadeView;
import com.gvs.crm.view.MaioresCorretoresView;

import infra.control.Action;
import infra.control.Control;

public class CorretoraControl extends Control {
	public void incluirCorretora(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		Corretora corretora = (Corretora) mm.getEntity("Corretora");

		mm.beginTransaction();
		try {
			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré el Nombre");

			if (corretora.verificarRuc(action.getString("ruc")))
				throw new Exception("El ruc " + action.getString("ruc")
						+ " ja esta sendo utilizado");

			if (action.getLong("entidadeSuperiorId") > 0) {
				superior = entidadeHome.obterEntidadePorId(action
						.getLong("entidadeSuperiorId"));
				corretora.atribuirSuperior(superior);
			}

			corretora.atribuirNome(action.getString("nome"));

			Usuario responsavelView = null;

			if (action.getLong("responsavelId") > 0)
				responsavelView = (Usuario) entidadeHome
						.obterEntidadePorId(action.getLong("responsavelId"));

			if (responsavelView != null)
				corretora.atribuirResponsavel(responsavelView);
			else
				corretora.atribuirResponsavel(responsavel);

			corretora.atribuirSigla(action.getString("sigla"));
			corretora.incluir();

			if (action.getLong("aseguradoraId") > 0) {
				Aseguradora aseguradora = (Aseguradora) entidadeHome
						.obterEntidadePorId(action.getLong("aseguradoraId"));

				corretora.atualizarAseguradora(aseguradora);
			}

			corretora.atualizarRuc(action.getString("ruc"));

			if (action.getString("novoRamo") != null
					&& !action.getString("novoRamo").equals("")) {
				corretora.adicionarNovoRamo(action.getString("novoRamo"));
				corretora.atualizarRamo(action.getString("novoRamo"));
			} else
				corretora.atualizarRamo(action.getString("ramo"));

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo_")) {
					String nome = key.substring(9, key.length());

					Entidade.Atributo entidadeAtributo = corretora
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			this.setResponseView(new EntidadeView(corretora, corretora));
			mm.commitTransaction();

			this.setAlert("Corredora incluida");

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(corretora));
			mm.rollbackTransaction();
		}
	}

	public void atualizarCorretora(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		Corretora corretora = (Corretora) entidadeHome
				.obterEntidadePorId(action.getLong("id"));
		;

		mm.beginTransaction();
		try {
			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré el Nombre");

			if (corretora.verificarRuc(action.getString("ruc")))
				throw new Exception("El ruc " + action.getString("ruc")
						+ " ja esta sendo utilizado");

			if (action.getLong("entidadeSuperiorId") > 0) {
				superior = entidadeHome.obterEntidadePorId(action
						.getLong("entidadeSuperiorId"));
				corretora.atribuirSuperior(superior);
			}

			corretora.atribuirNome(action.getString("nome"));

			Usuario responsavelView = null;

			if (action.getLong("responsavelId") > 0)
				responsavelView = (Usuario) entidadeHome
						.obterEntidadePorId(action.getLong("responsavelId"));

			if (responsavelView != null)
				corretora.atribuirResponsavel(responsavelView);

			corretora.atribuirSigla(action.getString("sigla"));
			corretora.atualizar();

			if (action.getLong("aseguradoraId") > 0) {
				Aseguradora aseguradora = (Aseguradora) entidadeHome
						.obterEntidadePorId(action.getLong("aseguradoraId"));

				corretora.atualizarAseguradora(aseguradora);
			}

			corretora.atualizarRuc(action.getString("ruc"));

			if (action.getString("novoRamo") != null
					&& !action.getString("novoRamo").equals("")) {
				corretora.adicionarNovoRamo(action.getString("novoRamo"));
				corretora.atualizarRamo(action.getString("novoRamo"));
			} else
				corretora.atualizarRamo(action.getString("ramo"));

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo_")) {
					String nome = key.substring(9, key.length());

					Entidade.Atributo entidadeAtributo = corretora
							.obterAtributo(nome);

					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			this.setResponseView(new EntidadeView(corretora, corretora));
			mm.commitTransaction();

			this.setAlert("Corredora Actualizada");

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(corretora));
			mm.rollbackTransaction();
		}
	}

	public void visualizarReasegurosCorredor(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(getUser());
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");

		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");

		Map reaseguradoras = new TreeMap();

		mm.beginTransaction();
		try 
		{

			if (action.getBoolean("listar")) 
			{
				if (action.getLong("aseguradoraId") == 0)
					throw new Exception("Escolha a Aseguradora");

				if (dataInicio == null)
					throw new Exception("Escolha o periodo inicial");

				if (dataFim == null)
					throw new Exception("Escolha o periodo final");
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " 00:00:00";
				dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
				
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				
				aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));

				reaseguradoras = aseguradoraHome.obterCorretores(aseguradora,dataInicio, dataFim);
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado : " + usuario.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					CorretorReasegurosXLS xls = new CorretorReasegurosXLS(aseguradora,dataInicio, dataFim, reaseguradoras, entidadeHome, textoUsuario);
					
					this.setResponseInputStream(xls.obterArquivo());
			        this.setResponseFileName("Aseguradora - Corredora de Reaseguros_"+usuario.obterNome()+"_"+new SimpleDateFormat("dd/MM/yyyy HH:mm" ).format(new Date()) + ".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(xls.obterArquivo().available());
				}
				else
					this.setResponseView(new CorretorReasegurosView(aseguradora,dataInicio, dataFim, action.getBoolean("listar"),reaseguradoras));
			}
			else
				this.setResponseView(new CorretorReasegurosView(aseguradora,dataInicio, dataFim, action.getBoolean("listar"),reaseguradoras));
			
			mm.commitTransaction();

		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new CorretorReasegurosView(aseguradora,dataInicio, dataFim, action.getBoolean("listar"),reaseguradoras));
			mm.rollbackTransaction();
		}
	}

	public void visualizarReasegurosCorredorAseguradora(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(getUser());

		Entidade corretora = null;
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");

		Map aseguradoras = new TreeMap();

		mm.beginTransaction();
		try {

			if (action.getBoolean("listar"))
			{
				if (action.getLong("corretorId") == 0)
					throw new Exception("Escolha a Corredora");

				if (dataInicio == null)
					throw new Exception("Escolha o periodo inicial");

				if (dataFim == null)
					throw new Exception("Escolha o periodo final");
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " 00:00:00";
				dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
				
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);

				corretora = entidadeHome.obterEntidadePorId(action.getLong("corretorId"));

				aseguradoras = entidadeHome.obterAseguradorasPorCorretora(corretora, dataInicio, dataFim);
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado : " + usuario.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				
					CorretorReasegurosAseguradoraXLS xls = new CorretorReasegurosAseguradoraXLS(corretora, dataInicio, dataFim, aseguradoras, entidadeHome, textoUsuario);
					
					this.setResponseInputStream(xls.obterArquivo());
			        this.setResponseFileName("Corredora de Reaseguros - Aseguradoras_"+usuario.obterNome()+"_"+new SimpleDateFormat("dd/MM/yyyy HH:mm" ).format(new Date()) + ".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(xls.obterArquivo().available());
				}
				else
					this.setResponseView(new CorretorReasegurosAseguradoraView(corretora, dataInicio, dataFim,action.getBoolean("listar"), aseguradoras));
			}
			else
				this.setResponseView(new CorretorReasegurosAseguradoraView(corretora, dataInicio, dataFim,action.getBoolean("listar"), aseguradoras));
			
			mm.commitTransaction();

		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new CorretorReasegurosAseguradoraView(corretora, dataInicio, dataFim,action.getBoolean("listar"), aseguradoras));
			mm.rollbackTransaction();
		}
	}
	
	public void visualizarProdutividadeCorredor(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());

		AuxiliarSeguro corretora = null;
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");

		Collection<Aseguradora> aseguradoras = new ArrayList<Aseguradora>();
		try 
		{

			if (action.getBoolean("lista"))
			{
				if (action.getLong("corredorId") == 0)
					throw new Exception("Elegiré el Corredor");
				if (dataInicio == null)
					throw new Exception("Elegiré el Data Inicio");
				if (dataFim == null)
					throw new Exception("Elegiré el Data Final");
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " 00:00:00";
				dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
				
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);

				corretora = (AuxiliarSeguro) entidadeHome.obterEntidadePorId(action.getLong("corredorId"));

				aseguradoras = corretora.obterAseguradorasCorredor(dataInicio, dataFim);
			}
			
			if(!action.getBoolean("excel"))
				this.setResponseView(new CorredorProdView(corretora,aseguradoras, dataInicio, dataFim, action.getBoolean("lista")));
			else
			{
				CorredorProdXLS xls = new CorredorProdXLS(corretora,aseguradoras, dataInicio,dataFim);
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio);
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim);
				
				dataInicioStr = dataInicioStr.replace("/", "_");
				dataFimStr = dataFimStr.replace("/", "_");
				String hora = new SimpleDateFormat("HH:mm").format(new Date());
				
				
				InputStream arquivo = xls.obterArquivo();
				this.setResponseInputStream(arquivo);
		        this.setResponseFileName("Produción Corredor de Seguros_" + corretora.obterNome() + "_"+usuarioAtual.obterNome() +"_" + dataInicioStr + "_" + dataFimStr + "_"+hora+".xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(arquivo.available());
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new CorredorProdView(corretora,aseguradoras, action.getDate("dataInicio"), action.getDate("dataFim"), false));
		}
	}
	
	public void maioresCorretores(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		//EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		CorretoraHome corretoraHome = (CorretoraHome) mm.getHome("CorretoraHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		String tipoValor = action.getString("tipoValor");
		String situacao = action.getString("situacao");
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		double dolar = action.getDouble("dolar");
		double euro = action.getDouble("euro");
		double real = action.getDouble("real");
		double pesoArg = action.getDouble("pesoArg");
		double pesoUru = action.getDouble("pesoUru");
		double yen = action.getDouble("yen");
		int qtde = action.getInt("qtde");
		Collection<String> dados = new ArrayList<String>();
		try
		{
			if(action.getBoolean("view"))
				this.setResponseView(new MaioresCorretoresView(tipoValor, dataInicio, dataFim, situacao, dolar, euro, real, pesoArg, pesoUru, yen, dados, qtde));
			else
			{
				if(dataInicio == null)
					throw new Exception("Fecha Inicio en blanco");
				if(dataFim == null)
					throw new Exception("Fecha Fin en blanco");
				if(qtde == 0)
					throw new Exception("Cantidad en blanco");
				if(dolar == 0)
					throw new Exception("Dólar USA en blanco");
				if(euro == 0)
					throw new Exception("Euro en blanco");
				if(real == 0)
					throw new Exception("Real en blanco");
				if(pesoArg == 0)
					throw new Exception("Peso Argentino en blanco");
				if(pesoUru == 0)
					throw new Exception("Peso Uruguayo en blanco");
				if(yen == 0)
					throw new Exception("Yen en blanco");
				
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				
				dados = corretoraHome.obterMaiores(qtde, dataInicio, dataFim, situacao, tipoValor, dolar, euro, real, pesoArg, pesoUru, yen);
				
				String textoUsuario = "Generado : " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				
				if(action.getBoolean("excel"))
				{
					MaioresCorretoresXLS xls = new MaioresCorretoresXLS(tipoValor, dataInicio, dataFim, situacao, dolar, euro, real, pesoArg, pesoUru, yen, dados, qtde, textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Mayores Corredores de Reaseguros" + "_"+usuarioAtual.obterNome()+".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				if(action.getBoolean("pdf"))
				{
					MaioresCorretoresPDF pdf = new MaioresCorretoresPDF(tipoValor, dataInicio, dataFim, situacao, dolar, euro, real, pesoArg, pesoUru, yen, dados, qtde, textoUsuario);
					
					InputStream arquivo = pdf.obterArquivo();
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Mayores Corredores de Reaseguros" + "_"+usuarioAtual.obterNome()+".pdf");
			        this.setResponseContentType("application/pdf");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new MaioresCorretoresView(tipoValor, dataInicio, dataFim, situacao, dolar, euro, real, pesoArg, pesoUru, yen, dados, qtde));
			}
				
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new MaioresCorretoresView(tipoValor, dataInicio, dataFim, situacao, dolar, euro, real, pesoArg, pesoUru, yen, dados, qtde));
		}
	}
}