package com.gvs.crm.control;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Reaseguradora;
import com.gvs.crm.model.ReaseguradoraHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.report.MaioresReaseguradorasPDF;
import com.gvs.crm.report.MaioresReaseguradorasPorSecaoPDF;
import com.gvs.crm.report.MaioresReaseguradorasPorSecaoXLS;
import com.gvs.crm.report.MaioresReaseguradorasXLS;
import com.gvs.crm.report.ReasegurosReaseguradoraXLS;
import com.gvs.crm.view.EntidadeView;
import com.gvs.crm.view.MaioresReaseguradorasPorSecaoView;
import com.gvs.crm.view.MaioresReaseguradorasView;
import com.gvs.crm.view.ReaseguradoraClassificacaoView;
import com.gvs.crm.view.ReasegurosReaseguradoraView;

import infra.control.Action;
import infra.control.Control;

public class ReaseguradoraControl extends Control {
	public void incluirReaseguradora(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		Reaseguradora reaseguradora = (Reaseguradora) mm
				.getEntity("Reaseguradora");

		mm.beginTransaction();
		try {
			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré el Nombre");

			if (action.getLong("entidadeSuperiorId") > 0) {
				superior = entidadeHome.obterEntidadePorId(action
						.getLong("entidadeSuperiorId"));
				reaseguradora.atribuirSuperior(superior);
			}

			reaseguradora.atribuirNome(action.getString("nome"));

			Usuario responsavelView = null;

			if (action.getLong("responsavelId") > 0)
				responsavelView = (Usuario) entidadeHome
						.obterEntidadePorId(action.getLong("responsavelId"));

			if (responsavelView != null)
				reaseguradora.atribuirResponsavel(responsavelView);
			else
				reaseguradora.atribuirResponsavel(responsavel);

			reaseguradora.atribuirSigla(action.getString("sigla"));
			reaseguradora.incluir();

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo_")) {
					String nome = key.substring(9, key.length());

					Entidade.Atributo entidadeAtributo = reaseguradora
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			this
					.setResponseView(new EntidadeView(reaseguradora,
							reaseguradora));
			mm.commitTransaction();

			this.setAlert("Reaseguradora incluida");

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(reaseguradora));
			mm.rollbackTransaction();
		}
	}

	public void atualizarReaseguradora(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = null;
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		Reaseguradora reaseguradora = (Reaseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("id"));
		;

		mm.beginTransaction();
		try {
			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré el Nombre");

			if (action.getLong("entidadeSuperiorId") > 0) {
				superior = entidadeHome.obterEntidadePorId(action
						.getLong("entidadeSuperiorId"));
				reaseguradora.atribuirSuperior(superior);
			}

			reaseguradora.atribuirNome(action.getString("nome"));

			Usuario responsavelView = null;

			if (action.getLong("responsavelId") > 0)
				responsavelView = (Usuario) entidadeHome
						.obterEntidadePorId(action.getLong("responsavelId"));

			if (responsavelView != null)
				reaseguradora.atribuirResponsavel(responsavelView);

			reaseguradora.atribuirSigla(action.getString("sigla"));
			reaseguradora.atualizar();

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo_")) {
					String nome = key.substring(9, key.length());

					Entidade.Atributo entidadeAtributo = reaseguradora
							.obterAtributo(nome);

					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			this
					.setResponseView(new EntidadeView(reaseguradora,
							reaseguradora));
			mm.commitTransaction();

			this.setAlert("Reaseguradora Actualizada");

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(reaseguradora));
			mm.rollbackTransaction();
		}
	}

	public void novaClassificacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		Reaseguradora reaseguradora = (Reaseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		this.setResponseView(new ReaseguradoraClassificacaoView(reaseguradora));
	}

	public void visualizarClassificacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		Reaseguradora reaseguradora = (Reaseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		Reaseguradora.Classificacao classificacao = reaseguradora
				.obterClassificacao(action.getInt("id"));

		this.setResponseView(new ReaseguradoraClassificacaoView(classificacao));
	}

	public void incluirClassificacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Reaseguradora reaseguradora = (Reaseguradora) home
				.obterEntidadePorId(action.getLong("entidadeId"));

		mm.beginTransaction();
		try {

			if (action.getString("classificacao").equals(""))
				throw new Exception("Elegiré a Calificación");

			if (action.getString("nivel").equals("")
					&& action.getString("novoNivel").equals(""))
				throw new Exception("Elegiré el Nivel");

			if (action.getString("qualificacao").equals(""))
				throw new Exception("Elegiré a Calificadora");

			if (action.getDate("data") == null)
				throw new Exception("Elegiré a Fecha de la Calificación");

			String nivel = "";

			if (action.getString("novoNivel") != null
					&& !action.getString("novoNivel").equals(""))
				nivel = action.getString("novoNivel");
			else
				nivel = action.getString("nivel");

			reaseguradora.adicionarClassificacao(action
					.getString("classificacao"), nivel, action
					.getString("codigo"), action.getString("qualificacao"),
					action.getDate("data"));

			if (action.getString("novoNivel") != null
					&& !action.getString("novoNivel").equals(""))
				reaseguradora.adicionarClassificacaoNivel(action
						.getString("novoNivel"));

			mm.commitTransaction();

			this.setResponseView(new EntidadeView(reaseguradora));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ReaseguradoraClassificacaoView(
					reaseguradora));
			mm.rollbackTransaction();
		}
	}

	public void excluirClassificacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		Reaseguradora reaseguradora = (Reaseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		Reaseguradora.Classificacao classificacao = reaseguradora
				.obterClassificacao(action.getInt("id"));

		mm.beginTransaction();
		try {
			reaseguradora.removerClassificacao(classificacao);

			mm.commitTransaction();

			this.setResponseView(new EntidadeView(reaseguradora));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ReaseguradoraClassificacaoView(
					reaseguradora));
			mm.rollbackTransaction();
		}
	}

	public void atualizarClassificacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Reaseguradora reaseguradora = (Reaseguradora) home
				.obterEntidadePorId(action.getLong("entidadeId"));

		Reaseguradora.Classificacao classificacao = reaseguradora
				.obterClassificacao(action.getInt("id"));

		mm.beginTransaction();
		try {
			if (action.getString("classificacao").equals(""))
				throw new Exception("Elegiré a Calificación");

			if (action.getString("nivel").equals("")
					&& action.getString("novoNivel").equals(""))
				throw new Exception("Elegiré el Nivel");

			if (action.getString("qualificacao").equals(""))
				throw new Exception("Elegiré a Calificadora");

			if (action.getDate("data") == null)
				throw new Exception("Elegiré a Fecha de la Calificación");

			String nivel = "";

			if (action.getString("novoNivel") != null
					&& !action.getString("novoNivel").equals(""))
				nivel = action.getString("novoNivel");
			else
				nivel = action.getString("nivel");

			classificacao.atualizar(action.getString("classificacao"), nivel,
					action.getString("codigo"), action
							.getString("qualificacao"), action.getDate("data"));

			if (action.getString("novoNivel") != null
					&& !action.getString("novoNivel").equals(""))
				reaseguradora.adicionarClassificacaoNivel(action
						.getString("novoNivel"));

			this.setResponseView(new EntidadeView(reaseguradora));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ReaseguradoraClassificacaoView(
					classificacao));
			mm.rollbackTransaction();
		}
	}

	public void visualizarReasegurosReaseguradora(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		
		Entidade entidade = null;
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");

		Map<String, String> reaseguradoras = new TreeMap<String, String>();
		
		String d1 = "01/01/2011";
		String d2 = "31/12/2012";
		
		Date dd = new SimpleDateFormat("dd/MM/yyyy").parse(d1);
		Date dd2 = new SimpleDateFormat("dd/MM/yyyy").parse(d2);
		
		System.out.println(dd.getTime());
		System.out.println(dd2.getTime());
		
		mm.beginTransaction();
		try {

			if (action.getBoolean("listar")) 
			{
				if (action.getLong("reaseguradoraId") == 0)
					throw new Exception("Elegir la Reaseguradora");

				if (dataInicio == null)
					throw new Exception("Fecha Inicio en blanco");

				if (dataFim == null)
					throw new Exception("Fecha Fin en blanco");

				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " 00:00:00";
				dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
				
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				
				entidade = entidadeHome.obterEntidadePorId(action.getLong("reaseguradoraId"));
				reaseguradoras = entidadeHome.obterReaseguros(entidade, dataInicio, dataFim);
			}

			if(!action.getBoolean("excel"))
				this.setResponseView(new ReasegurosReaseguradoraView(entidade,dataInicio, dataFim, action.getBoolean("listar"),reaseguradoras));
			else
			{
				String textoUsuario = "Generado : " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				ReasegurosReaseguradoraXLS xls = new ReasegurosReaseguradoraXLS(entidade,dataInicio, dataFim,reaseguradoras, entidadeHome, textoUsuario);
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio);
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim);
				
				dataInicioStr = dataInicioStr.replace("/", "_");
				dataFimStr = dataFimStr.replace("/", "_");
				
				String hora = new SimpleDateFormat("HH:mm").format(new Date());
				
				InputStream arquivo = xls.obterArquivo();
				this.setResponseInputStream(arquivo);
		        this.setResponseFileName("Reaseguradora-Reaseguros_" + entidade.obterNome() + "_"+usuarioAtual.obterNome()+ "_" + dataInicioStr + "_" + dataFimStr + "_"+hora+".xls");
				//this.setResponseFileName("Reaseguradora - Reaseguros_" + entidade.obterNome() + ".xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(arquivo.available());
			}
			
			mm.commitTransaction();

		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ReasegurosReaseguradoraView(entidade,dataInicio, dataFim, action.getBoolean("listar"),reaseguradoras));
			mm.rollbackTransaction();
		}
	}
	
	public void maioresRaseguradoras(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		//EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ReaseguradoraHome reaseguradoraHome = (ReaseguradoraHome) mm.getHome("ReaseguradoraHome");
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
		boolean porPais = Boolean.valueOf(action.getString("porPais"));
		Collection<String> dados = new ArrayList<String>();
		try
		{
			if(action.getBoolean("view"))
				this.setResponseView(new MaioresReaseguradorasView(tipoValor, dataInicio, dataFim, situacao, dolar, euro, real, pesoArg, pesoUru, yen, dados,porPais));
			else
			{
				if(dataInicio == null)
					throw new Exception("Fecha Inicio en blanco");
				if(dataFim == null)
					throw new Exception("Fecha Fin en blanco");
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
				
				String textoUsuario = "Generado : " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				
				dados = reaseguradoraHome.obterMaiores(dataInicio, dataFim, situacao, tipoValor, dolar, euro, real, pesoArg, pesoUru, yen, porPais);
				
				if(action.getBoolean("excel"))
				{
					MaioresReaseguradorasXLS xls = new MaioresReaseguradorasXLS(tipoValor, dataInicio, dataFim, situacao, dolar, euro, real, pesoArg, pesoUru, yen, dados, porPais,textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Mayores Reaseguradoras" + "_"+usuarioAtual.obterNome()+".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				if(action.getBoolean("pdf"))
				{
					MaioresReaseguradorasPDF pdf = new MaioresReaseguradorasPDF(tipoValor, dataInicio, dataFim, situacao, dolar, euro, real, pesoArg, pesoUru, yen, dados, porPais, textoUsuario);
					
					InputStream arquivo = pdf.obterArquivo();
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Mayores Reaseguradoras" + "_"+usuarioAtual.obterNome()+".pdf");
			        this.setResponseContentType("application/pdf");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new MaioresReaseguradorasView(tipoValor, dataInicio, dataFim, situacao, dolar, euro, real, pesoArg, pesoUru, yen, dados,porPais));
			}
				
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new MaioresReaseguradorasView(tipoValor, dataInicio, dataFim, situacao, dolar, euro, real, pesoArg, pesoUru, yen, dados, porPais));
		}
	}
	
	public void maioresRaseguradorasPorSecao(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		//EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ReaseguradoraHome reaseguradoraHome = (ReaseguradoraHome) mm.getHome("ReaseguradoraHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		String tipoValor = action.getString("tipoValor");
		String situacao = action.getString("situacao");
		String secao = action.getString("secao");
		String modalidade = action.getString("modalidade");
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		double dolar = action.getDouble("dolar");
		double euro = action.getDouble("euro");
		double real = action.getDouble("real");
		double pesoArg = action.getDouble("pesoArg");
		double pesoUru = action.getDouble("pesoUru");
		double yen = action.getDouble("yen");
		
		Collection<String> dados = new ArrayList<String>();
		try
		{
			if(action.getBoolean("view"))
				this.setResponseView(new MaioresReaseguradorasPorSecaoView(tipoValor, dataInicio, dataFim, situacao, dolar, euro, real, pesoArg, pesoUru, yen, dados,secao, modalidade));
			else
			{
				if(dataInicio == null)
					throw new Exception("Fecha Inicio en blanco");
				if(dataFim == null)
					throw new Exception("Fecha Fin en blanco");
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
				
				dados = reaseguradoraHome.obterMaioresPorSecao(dataInicio, dataFim, situacao, tipoValor, dolar, euro, real, pesoArg, pesoUru, yen, secao, modalidade);
				
				String textoUsuario = "Generado : " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				
				if(action.getBoolean("excel"))
				{
					MaioresReaseguradorasPorSecaoXLS xls = new MaioresReaseguradorasPorSecaoXLS(tipoValor, dataInicio, dataFim, situacao, dolar, euro, real, pesoArg, pesoUru, yen, dados, secao, modalidade, textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Mayores Reaseguradoras por Sección" + "_"+usuarioAtual.obterNome()+".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				if(action.getBoolean("pdf"))
				{
					MaioresReaseguradorasPorSecaoPDF pdf = new MaioresReaseguradorasPorSecaoPDF(tipoValor, dataInicio, dataFim, situacao, dolar, euro, real, pesoArg, pesoUru, yen, dados, secao, modalidade, textoUsuario);
					
					InputStream arquivo = pdf.obterArquivo();
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Mayores Reaseguradoras por Sección" + "_"+usuarioAtual.obterNome()+".pdf");
			        this.setResponseContentType("application/pdf");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new MaioresReaseguradorasPorSecaoView(tipoValor, dataInicio, dataFim, situacao, dolar, euro, real, pesoArg, pesoUru, yen, dados,secao, modalidade));
			}
				
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new MaioresReaseguradorasPorSecaoView(tipoValor, dataInicio, dataFim, situacao, dolar, euro, real, pesoArg, pesoUru, yen, dados,secao, modalidade));
		}
	}
}