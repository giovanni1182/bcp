package com.gvs.crm.control;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.AuxiliarSeguroHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.report.AuxiliarReport;
import com.gvs.crm.report.AuxiliaresReport;
import com.gvs.crm.report.ComissaoAgenteXLS;
import com.gvs.crm.report.ListaProducaoAgenteXLS;
import com.gvs.crm.report.MaioresAgentesXLS;
import com.gvs.crm.view.AuxiliarSeguroProdView;
import com.gvs.crm.view.ComissaoAgenteView;
import com.gvs.crm.view.ComissaoAgentesApolicesView;
import com.gvs.crm.view.EntidadeView;
import com.gvs.crm.view.ListaProducaoAgentesView;
import com.gvs.crm.view.MaioresAgentesView;
import com.gvs.crm.view.MaioresApolicesAgentesView;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class AuxiliarSeguroControl extends Control {
	public void incluirAuxiliarSeguro(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		AuxiliarSeguro auxiliar = (AuxiliarSeguro) mm
				.getEntity("AuxiliarSeguro");

		mm.beginTransaction();
		try {
			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré o Nombre");

			if (auxiliar.verificarRuc(action.getString("ruc")))
				throw new Exception("El ruc " + action.getString("ruc")
						+ " ja esta sendo utilizado");

			if (action.getLong("entidadeSuperiorId") > 0) {
				superior = entidadeHome.obterEntidadePorId(action
						.getLong("entidadeSuperiorId"));
				auxiliar.atribuirSuperior(superior);
			}

			auxiliar.atribuirNome(action.getString("nome"));

			Usuario responsavelView = null;

			if (action.getLong("responsavelId") > 0)
				responsavelView = (Usuario) entidadeHome
						.obterEntidadePorId(action.getLong("responsavelId"));

			if (responsavelView != null)
				auxiliar.atribuirResponsavel(responsavelView);
			else
				auxiliar.atribuirResponsavel(responsavel);

			auxiliar.incluir();

			if (action.getString("novoRamo") != null
					&& !action.getString("novoRamo").equals(""))
				auxiliar.adicionarNovoRamo(action.getString("novoRamo"));
			else
				auxiliar.adicionarNovoRamo(action.getString("ramo"));

			if (action.getLong("aseguradoraId") > 0) {
				Aseguradora aseguradora = (Aseguradora) entidadeHome
						.obterEntidadePorId(action.getLong("aseguradoraId"));

				auxiliar.atualizarAseguradora(aseguradora);
			}

			auxiliar.atualizarRuc(action.getString("ruc"));

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo_")) {
					String nome = key.substring(9, key.length());

					Entidade.Atributo entidadeAtributo = auxiliar
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			this.setResponseView(new EntidadeView(auxiliar, auxiliar));
			mm.commitTransaction();

			this.setAlert("Auxiliar de Seguro Incluido");

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(auxiliar));
			mm.rollbackTransaction();
		}
	}

	public void atualizarAuxiliarSeguro(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = null;
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		AuxiliarSeguro auxiliar = (AuxiliarSeguro) entidadeHome
				.obterEntidadePorId(action.getLong("id"));
		;

		mm.beginTransaction();
		try {
			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré el Nombre");

			if (auxiliar.verificarRuc(action.getString("ruc")))
				throw new Exception("El ruc " + action.getString("ruc")
						+ " ja esta sendo utilizado");

			if (action.getLong("entidadeSuperiorId") > 0) {
				superior = entidadeHome.obterEntidadePorId(action
						.getLong("entidadeSuperiorId"));
				auxiliar.atribuirSuperior(superior);
			}

			auxiliar.atribuirNome(action.getString("nome"));

			Usuario responsavelView = null;

			if (action.getLong("responsavelId") > 0)
				responsavelView = (Usuario) entidadeHome
						.obterEntidadePorId(action.getLong("responsavelId"));

			if (responsavelView != null)
				auxiliar.atribuirResponsavel(responsavelView);

			auxiliar.atribuirSigla(action.getString("sigla"));
			auxiliar.atualizar();

			if (action.getLong("aseguradoraId") > 0) {
				Aseguradora aseguradora = (Aseguradora) entidadeHome
						.obterEntidadePorId(action.getLong("aseguradoraId"));

				auxiliar.atualizarAseguradora(aseguradora);
			}

			auxiliar.atualizarRuc(action.getString("ruc"));

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo_")) {
					String nome = key.substring(9, key.length());

					Entidade.Atributo entidadeAtributo = auxiliar
							.obterAtributo(nome);

					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			this.setResponseView(new EntidadeView(auxiliar, auxiliar));
			mm.commitTransaction();

			this.setAlert("Auxiliar de Seguro Actualizado");

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(auxiliar));
			mm.rollbackTransaction();
		}
	}

	public void incluirRamoAuxiliarSeguro(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		AuxiliarSeguro auxiliar = (AuxiliarSeguro) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

		mm.beginTransaction();
		try {
			if (action.getString("novoRamo") != null
					&& !action.getString("novoRamo").equals(""))
				auxiliar.adicionarNovoRamo(action.getString("novoRamo"));
			else
				auxiliar.adicionarNovoRamo(action.getString("ramo"));

			mm.commitTransaction();
			this.setResponseView(new EntidadeView(auxiliar));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(auxiliar));
			mm.rollbackTransaction();
		}
	}

	public void excluirRamoAuxiliarSeguro(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		AuxiliarSeguro auxiliar = (AuxiliarSeguro) entidadeHome
				.obterEntidadePorId(action.getLong("id"));
		AuxiliarSeguro.Ramo ramo = auxiliar.obterRamo(action.getInt("seq"));

		mm.beginTransaction();
		try {
			auxiliar.excluirRamo(ramo);

			mm.commitTransaction();

			this.setResponseView(new EntidadeView(auxiliar));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(auxiliar));
			mm.rollbackTransaction();
		}
	}

	public void visualizarProdutividadeAuxiliar(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		AuxiliarSeguroHome auxiliarSeguroHome = (AuxiliarSeguroHome) mm.getHome("AuxiliarSeguroHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		AuxiliarSeguro auxiliar = null;
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		boolean auxiliarSeguro = action.getBoolean("auxiliar");
		Collection<Aseguradora> aseguradoras = new ArrayList<Aseguradora>();
		
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new ListaProducaoAgentesView(auxiliar, dataInicio, dataFim, action.getBoolean("lista"), aseguradoras, auxiliarSeguro));
			else if (action.getBoolean("lista"))
			{
				if (action.getLong("auxiliarId") == 0)
					throw new Exception("Elegiré el Agente");
				if (dataInicio == null)
					throw new Exception("Elegiré el Data Inicio");
				if (dataFim == null)
					throw new Exception("Elegiré el Data Final");
				
				Calendar c = Calendar.getInstance();
				c.setTime(dataInicio);
				String diaInicio = new Integer(c.getActualMinimum(Calendar.DAY_OF_MONTH)).toString();
				if(diaInicio.toString().length()==1)
					diaInicio = "0" + diaInicio;
				String mes = new SimpleDateFormat("MM/yyyy").format(c.getTime());
				dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(diaInicio+"/"+mes +" 00:00:00");
				
				c.setTime(dataFim);
				String diaFim = new Integer(c.getActualMaximum(Calendar.DAY_OF_MONTH)).toString();
				mes = new SimpleDateFormat("MM/yyyy").format(c.getTime());
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(diaFim+"/"+mes +" 23:59:59");
				
				auxiliar = (AuxiliarSeguro) entidadeHome.obterEntidadePorId(action.getLong("auxiliarId"));

				if(action.getBoolean("apolices"))
				{
					Aseguradora aseg = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
					dataInicio = action.getDate("dataInicio2");
					dataFim = action.getDate("dataFim2");
					
					String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio);
					String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim);
					
					dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr + " 00:00:00");
					dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr + " 23:59:59");
					
					aseguradoras.add(aseg);
					
					this.setResponseView(new AuxiliarSeguroProdView(auxiliar, aseguradoras, dataInicio, dataFim, true, auxiliarSeguro));
				}
				else if(action.getBoolean("excel"))
				{
					aseguradoras = auxiliarSeguroHome.obterAseguradorasApolicesAcumuladas(auxiliar, dataInicio, dataFim, auxiliarSeguro);
					
					String textoUsuario = "Generado : " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					ListaProducaoAgenteXLS xls = new ListaProducaoAgenteXLS(auxiliar, dataInicio, dataFim, aseguradoras, auxiliarSeguro, textoUsuario);
					
					String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio);
					String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim);
					
					InputStream arquivo = xls.obterArquivo();
					this.setResponseInputStream(arquivo);
					if(auxiliarSeguro)
						this.setResponseFileName("Produción Agentes de Seguro_" + auxiliar.obterNome() + "_"+usuarioAtual.obterNome() +"_" + dataInicioStr + "_" + dataFimStr+".xls");
					else
						this.setResponseFileName("Produción Corredor de Seguros_" + auxiliar.obterNome() + "_"+usuarioAtual.obterNome() +"_" + dataInicioStr + "_" + dataFimStr+".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
				{
					aseguradoras = auxiliarSeguroHome.obterAseguradorasApolicesAcumuladas(auxiliar, dataInicio, dataFim, auxiliarSeguro);
					
					this.setResponseView(new ListaProducaoAgentesView(auxiliar, dataInicio, dataFim, action.getBoolean("lista"), aseguradoras, auxiliarSeguro));
				}
			}
			else
			{
				/*AuxiliarSeguroProdXLS xls = new AuxiliarSeguroProdXLS(auxiliar,aseguradoras, dataInicio,dataFim);
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio);
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim);
				
				dataInicioStr = dataInicioStr.replace("/", "_");
				dataFimStr = dataFimStr.replace("/", "_");
				String hora = new SimpleDateFormat("HH:mm").format(new Date());
				
				
				InputStream arquivo = xls.obterArquivo();
				this.setResponseInputStream(arquivo);
		        this.setResponseFileName("Produción Agentes de Seguro_" + auxiliar.obterNome() + "_"+usuarioAtual.obterNome() +"_" + dataInicioStr + "_" + dataFimStr + "_"+hora+".xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(arquivo.available());*/
			}

		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AuxiliarSeguroProdView(auxiliar,aseguradoras, action.getDate("dataInicio"), action.getDate("dataFim"), false, auxiliarSeguro));
		}
	}

	public void visualizarAuxiliares(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		AuxiliarSeguroHome home = (AuxiliarSeguroHome) mm
				.getHome("AuxiliarSeguroHome");

		Collection aseguradoras = new ArrayList();

		mm.beginTransaction();
		try {

			this
					.setResponseReport(new AuxiliaresReport(home
							.obterAuxiliares()));

			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuario, usuario));
			mm.rollbackTransaction();
		}
	}

	public void imprimirAuxiliar(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		AuxiliarSeguro auxiliar = (AuxiliarSeguro) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

		mm.beginTransaction();
		try {
			mm.commitTransaction();

			this.setResponseReport(new AuxiliarReport(auxiliar));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(auxiliar));
			mm.rollbackTransaction();
		}
	}
	
	public void maioresAgentes(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		AuxiliarSeguroHome home = (AuxiliarSeguroHome) mm.getHome("AuxiliarSeguroHome");
		AuxiliarSeguro agente = null;
		String situacao = action.getString("situacao");
		Date dataInicioD = action.getDate("dataInicio");
		Date dataFimD = action.getDate("dataFim");
		boolean mostraTela = action.getBoolean("mostraTela");
		boolean excel = action.getBoolean("excel");
		boolean auxiliar = action.getBoolean("auxiliar");
		boolean listaApolices = action.getBoolean("listaApolices");
		Collection<String>  dados = new ArrayList<String>();
		//if(action.getLong("agenteId") > 0)
			//agente = (AuxiliarSeguro) entidadeHome.obterEntidadePorId(action.getLong("agenteId"));
		if(action.getLong("agenteId2") > 0)
			agente = (AuxiliarSeguro) entidadeHome.obterEntidadePorId(action.getLong("agenteId2"));
		
		double monto = action.getDouble("monto");
		try
		{
			if(action.getBoolean("view"))
				this.setResponseView(new MaioresAgentesView(agente, action.getString("tipoValor"),dataInicioD,dataFimD,action.getInt("qtde"),situacao, dados, monto, auxiliar));
			else
			{
				if(dataInicioD == null)
					throw new Exception("Fecha Inicio en blanco");
				if(dataFimD == null)
					throw new Exception("Fecha Fin en blanco");
				if(action.getInt("qtde")<=0)
					throw new Exception("Cantidad Solicitada és cero");
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicioD) + " 00:00:00";
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFimD) + " 23:59:59";
				
				dataInicioD = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
				dataFimD = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				
				if(excel || mostraTela)
				{
					if(action.getString("tipoValor").equals("valorPrima"))
						dados = home.obterApolicesMaioresAgentesPorPrimaStr(agente,dataInicioD,dataFimD,action.getInt("qtde"), situacao, monto, auxiliar);
					else if(action.getString("tipoValor").equals("valorCapital"))
						dados = home.obterApolicesMaioresAgentesPorCapitalStr(agente,dataInicioD,dataFimD,action.getInt("qtde"), situacao, monto, auxiliar);
					else
						dados = home.obterApolicesMaioresAgentesPorComissaoStr(agente,dataInicioD,dataFimD,action.getInt("qtde"), situacao, monto, auxiliar);
				}
				
				if(excel)
				{
					String textoUsuario = "Generado: " + usuario.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					MaioresAgentesXLS xls = new MaioresAgentesXLS(agente, action.getString("tipoValor"), dataInicioD, dataFimD, situacao, dados, monto, action.getInt("qtde"), auxiliar,textoUsuario);
					InputStream arquivo = xls.obterArquivo();
					String nome = "";
					if(auxiliar)
						nome = "Mayores Agentes_" +usuario.obterNome()+"_"+ dataInicioStr + " hasta " +dataFimStr+".xls";
					else
						nome = "Mayores Corredores de Seguros_" +usuario.obterNome()+"_"+ dataInicioStr + " hasta " +dataFimStr+".xls";
					String mime = "application/vnd.ms-excel";
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName(nome);
			        this.setResponseContentType(mime);
			        this.setResponseContentSize(arquivo.available());
				}
				else if(mostraTela)
					this.setResponseView(new MaioresAgentesView(agente, action.getString("tipoValor"),dataInicioD,dataFimD,action.getInt("qtde"),situacao,dados, monto, auxiliar));
				else if(listaApolices)
				{
					Collection<Apolice> apolices;
					
					if(action.getString("tipoValor").equals("valorPrima"))
						apolices = home.obterApolicesMaioresAgentesPorPrima(agente, dataInicioD, dataFimD, situacao, auxiliar);
					else if(action.getString("tipoValor").equals("valorCapital"))
						apolices = home.obterApolicesMaioresAgentesPorCapital(agente, dataInicioD, dataFimD, situacao, auxiliar);
					else
						apolices = home.obterApolicesMaioresAgentesPorComissao(agente, dataInicioD, dataFimD, situacao, auxiliar);
					
					this.setResponseView(new MaioresApolicesAgentesView(agente, action.getString("tipoValor"),dataInicioD,dataFimD,situacao, apolices, monto, auxiliar));
				}
			}
		}
		catch (Exception e)
		{
			this.setAlert(Util.translateException(e));
			this.setResponseView(new MaioresAgentesView(agente, action.getString("tipoValor"),dataInicioD,dataFimD,action.getInt("qtde"),situacao, dados, monto, auxiliar));
			//mm.rollbackTransaction();
		}
	}
	
	public void comissaoAgentes(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		AuxiliarSeguroHome home = (AuxiliarSeguroHome) mm.getHome("AuxiliarSeguroHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		AuxiliarSeguro agente = null;
		String situacao = action.getString("situacao");
		Date dataInicioD = action.getDate("dataInicio");
		Date dataFimD = action.getDate("dataFim");
		boolean auxiliar = action.getBoolean("auxiliar");
		Collection<String>  dados = new ArrayList<String>();
		if(action.getLong("agenteId") > 0)
			agente = (AuxiliarSeguro) entidadeHome.obterEntidadePorId(action.getLong("agenteId"));
		
		try
		{
			if(action.getBoolean("view"))
				this.setResponseView(new ComissaoAgenteView(agente, action.getString("tipoValor"), dataInicioD, dataFimD, situacao, dados, auxiliar));
			else
			{
				if(dataInicioD == null)
					throw new Exception("Fecha Inicio en blanco");
				if(dataFimD == null)
					throw new Exception("Fecha Fin en blanco");
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicioD) + " 00:00:00";
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFimD) + " 23:59:59";
				
				dataInicioD = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
				dataFimD = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				
				if(action.getString("tipoValor").equals("valorPrima"))
					dados = home.obterApolicesComissaoAgentesPorPrima(agente, dataInicioD, dataFimD, situacao, auxiliar);
				else if(action.getString("tipoValor").equals("valorCapital"))
					dados = home.obterApolicesComissaoAgentesPorCapital(agente, dataInicioD, dataFimD, situacao, auxiliar);
				else
					dados = home.obterApolicesComissaoAgentesPorComissao(agente, dataInicioD, dataFimD, situacao, auxiliar);
				
				if(!action.getBoolean("excel"))
					this.setResponseView(new ComissaoAgenteView(agente, action.getString("tipoValor"), dataInicioD, dataFimD, situacao, dados, auxiliar));
				else
				{
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					ComissaoAgenteXLS xls = new ComissaoAgenteXLS(agente, action.getString("tipoValor"), dataInicioD, dataFimD, situacao, dados, auxiliar,textoUsuario);
					
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					InputStream arquivo = xls.obterArquivo();
					this.setResponseInputStream(arquivo);
					if(auxiliar)
						this.setResponseFileName("Comisión Agentes por Sección"+hora+".xls");
					else
						this.setResponseFileName("Comisión Corredores de Seguros por Sección"+hora+".xls");
					
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
			}
		}
		catch (Exception e)
		{
			this.setAlert(Util.translateException(e));
			this.setResponseView(new ComissaoAgenteView(agente, action.getString("tipoValor"), dataInicioD, dataFimD, situacao, dados, auxiliar));
			//mm.rollbackTransaction();
		}
	}
	
	public void comissaoAgentesApolice(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		AuxiliarSeguroHome home = (AuxiliarSeguroHome) mm.getHome("AuxiliarSeguroHome");
		AuxiliarSeguro agente = null;
		String situacao = action.getString("situacao2");
		Date dataInicioD = action.getDate("inicio");
		Date dataFimD = action.getDate("fim");
		String plano = action.getString("plano");
		boolean auxiliar = action.getBoolean("auxiliar");
		if(action.getLong("agenteId2") > 0)
			agente = (AuxiliarSeguro) entidadeHome.obterEntidadePorId(action.getLong("agenteId2"));
		
		Collection<Apolice> apolices = new ArrayList<Apolice>();
		
		try
		{
			apolices = home.obterApolicesComissaoAgentes(agente, dataInicioD, dataFimD, situacao, plano, auxiliar);
			
			this.setResponseView(new ComissaoAgentesApolicesView(agente, dataInicioD, dataFimD, situacao, plano, apolices, auxiliar));
		}
		catch (Exception e)
		{
			this.setAlert(Util.translateException(e));
			this.setResponseView(new ComissaoAgentesApolicesView(agente, dataInicioD, dataFimD, situacao, plano, apolices, auxiliar));
		}
	}
}