package com.gvs.crm.control;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.ApoliceHome;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.AuditorExterno;
import com.gvs.crm.model.AuditorExternoHome;
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.AuxiliarSeguroHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.CorretoraHome;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Inscricao;
import com.gvs.crm.model.Log;
import com.gvs.crm.model.Pessoa;
import com.gvs.crm.model.Raiz;
import com.gvs.crm.model.Reaseguradora;
import com.gvs.crm.model.ReaseguradoraHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.model.impl.FilaProcessamento;
import com.gvs.crm.report.AgentesReport;
import com.gvs.crm.report.AgentesXLS;
import com.gvs.crm.report.AseguradoraValidezPDF;
import com.gvs.crm.report.AseguradoraValidezXLS;
import com.gvs.crm.report.AuditoresReport;
import com.gvs.crm.report.AuditoresXLS;
import com.gvs.crm.report.ConsolidadoApolicesSinistrosXLS;
import com.gvs.crm.report.CorredoresReport;
import com.gvs.crm.report.CorredoresXLS;
import com.gvs.crm.report.CorretorasReport;
import com.gvs.crm.report.CorretorasXLS;
import com.gvs.crm.report.GEEXLS;
import com.gvs.crm.report.LavagemDinheiroXLS;
import com.gvs.crm.report.LiquidadoresReport;
import com.gvs.crm.report.LiquidadoresXLS;
import com.gvs.crm.report.PlanoContas2XLS;
import com.gvs.crm.report.PlanoContasXLS;
import com.gvs.crm.report.ReaseguradoraQualificacaoPDF;
import com.gvs.crm.report.ReaseguradoraQualificacaoXLS;
import com.gvs.crm.report.ReaseguradoraReport;
import com.gvs.crm.report.ReaseguradorasXLS;
import com.gvs.crm.view.AnuarioView;
import com.gvs.crm.view.CarregarRUCCIView;
import com.gvs.crm.view.ConsolidadoApolicesSinistrosView;
import com.gvs.crm.view.EntidadeHierarquiaView;
import com.gvs.crm.view.EntidadeView;
import com.gvs.crm.view.EventosEntidadeView;
import com.gvs.crm.view.ExcluirDuplicidadeView;
import com.gvs.crm.view.FilaProcessamentoView;
import com.gvs.crm.view.GEEView;
import com.gvs.crm.view.InspecaoSituView;
import com.gvs.crm.view.LavagemDinheiroView;
import com.gvs.crm.view.LogsView;
import com.gvs.crm.view.NovaEntidadeView;
import com.gvs.crm.view.PaginaInicialView;
import com.gvs.crm.view.PopupEntidadesView;
import com.gvs.crm.view.SelecaoResponsavelEntidadeView;
import com.gvs.crm.view.SelecaoSuperiorEntidadeView;
import com.gvs.crm.view.SelecionaRelEntidadesVigentesView;
import com.gvs.crm.view.TotalApolicesSinistrosView;

import infra.control.Action;
import infra.control.Control;
import infra.control.ControlManager;
import infra.security.UserManager;

public class EntidadeControl extends Control {
	public void atualizarEntidade(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("id"));
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("entidadeSuperiorId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		boolean podeAtualizar = false;
		mm.beginTransaction();
		try {
			Collection tipos = entidadeHome.obterClassesInferiores(superior);

			for (Iterator i = tipos.iterator(); i.hasNext();) {
				String tipo = (String) i.next();
				String descricao = entidadeHome.obterDescricaoClasse(tipo);
				//System.out.println(descricao);

				if (superior.obterClasse().toLowerCase().equals(
						descricao.toLowerCase()))
					podeAtualizar = true;
			}

			if (!(superior instanceof Raiz))
				if (!podeAtualizar)
					throw new Exception(superior.obterNome()
							+ " não pode ser superior");

			/*
			 * if(entidade.verificarRuc(action.getString("ruc"))) throw new
			 * Exception("El ruc " + action.getString("ruc") + " ja esta sendo
			 * utilizado");
			 */

			Log log = (Log) mm.getEntity("Log");

			String descricaoLog = "";
			descricaoLog = "Usuario que Modificou: " + usuarioAtual.obterNome()
					+ "\n";
			descricaoLog += "Data Modificação: "
					+ new SimpleDateFormat("dd/MM/yyyy HH:mm")
							.format(new Date()) + "\n";
			descricaoLog += "-----------------------------------\n";
			descricaoLog += "Antes da Modificação\n";
			descricaoLog += "Nome: " + entidade.obterNome() + "\n";
			descricaoLog += "Apelido: " + entidade.obterApelido() + "\n";
			descricaoLog += "Sigla: " + entidade.obterSigla() + "\n";
			descricaoLog += "Superior: " + entidade.obterSuperior().obterNome()
					+ "\n";
			descricaoLog += "Responsavel: "
					+ entidade.obterResponsavel().obterNome() + "\n";

			for (Iterator i = entidade.obterAtributos().iterator(); i.hasNext();) {
				Entidade.Atributo atributo = (Entidade.Atributo) i.next();
				descricaoLog += atributo.obterNome() + ": "
						+ atributo.obterValor() + "\n";
			}

			entidade.atribuirNome(action.getString("nome"));
			entidade.atribuirApelido(action.getString("apelido"));
			entidade.atribuirSigla(action.getString("sigla"));
			entidade.atribuirRUC(action.getString("ruc"));
			if (superior.obterId() != 0)
				entidade.atribuirSuperior(superior);
			entidade.atualizar();
			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo")) {
					String nome = key.substring(9, key.length());
					/*
					 * if(nome.equals("cpf"))
					 * entidade.validaCPF(action.getString(key));
					 */
					Entidade.Atributo entidadeAtributo = entidade
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			descricaoLog += "-----------------------------------\n";
			descricaoLog += "Modificação\n";
			descricaoLog += "Nome: " + action.getString("nome") + "\n";
			descricaoLog += "Apelido: " + entidade.obterApelido() + "\n";
			descricaoLog += "Sigla: " + entidade.obterSigla() + "\n";
			descricaoLog += "Superior: " + entidade.obterSuperior().obterNome()
					+ "\n";
			descricaoLog += "Responsavel: "
					+ entidade.obterResponsavel().obterNome() + "\n";

			for (Iterator i = entidade.obterAtributos().iterator(); i.hasNext();) {
				Entidade.Atributo atributo = (Entidade.Atributo) i.next();
				descricaoLog += atributo.obterNome() + ": "
						+ atributo.obterValor() + "\n";
			}

			log.atribuirOrigem(usuarioAtual);
			Entidade bcp = (Entidade) entidadeHome
					.obterEntidadePorApelido("bcp");
			Entidade administrador = (Entidade) entidadeHome
					.obterEntidadePorApelido("admin");
			log.atribuirDestino(bcp);
			log.atribuirResponsavel(administrador);
			log.atribuirTitulo("Log de Alteracao dia: "
					+ new SimpleDateFormat("dd/MM/yyyy HH:mm")
							.format(new Date()) + "\n");
			log.atribuirDescricao(descricaoLog);
			log.atribuirTipo("Alteracao de Entidade");
			log.incluir();
			log.atualizarFase(Log.EVENTO_CONCLUIDO);

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
		}
		this.setResponseView(new EntidadeView(entidade));
	}

	public void atualizarResponsavelEntidade(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = home.obterEntidadePorId(action.getLong("id"));
		Usuario responsavel = (Usuario) home.obterEntidadePorId(action
				.getLong("responsavelId"));
		entidade.atribuirResponsavel(responsavel);
		mm.beginTransaction();
		try {
			entidade.atualizar();
			
			if(entidade instanceof ClassificacaoContas)
				entidade.atualizarInferiores(responsavel);
			
			this.setAlert("Responsable actualizado");
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
		}
		this.setResponseView(new EntidadeView(entidade));
	}

	public void atualizarSuperiorEntidade(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = home.obterEntidadePorId(action.getLong("id"));
		Entidade superior = (Entidade) home.obterEntidadePorId(action
				.getLong("superiorId"));
		entidade.atribuirSuperior(superior);
		mm.beginTransaction();
		try {
			entidade.atualizar();
			this.setAlert("Superior atualizado");

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
		}
		this.setResponseView(new EntidadeView(entidade));
	}

	public void excluirEntidade(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = home.obterEntidadePorId(action.getLong("id"));
		Entidade origemMenu = home.obterEntidadePorId(action
				.getLong("origemMenuId"));
		mm.beginTransaction();
		try 
		{
			
			for(Iterator i = entidade.obterEventosComoOrigem().iterator() ; i.hasNext() ; )
			{
				Evento e = (Evento) i.next();
				
				e.excluir();
			}
			
			for(Iterator i = entidade.obterEventosDeResponsabilidade().iterator() ; i.hasNext() ; )
			{
				Evento e = (Evento) i.next();
				
				e.excluir();
			}
			
			
			entidade.excluir();
			UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
			Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
			this.setResponseView(new PaginaInicialView(usuario, origemMenu));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(entidade));
			mm.rollbackTransaction();
		}
	}

	public void incluirEntidade(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = (Entidade) mm.getEntity(action.getString("classe"));
		Entidade origemMenu = (Entidade) entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		Entidade responsavelTecnico = (Entidade) entidadeHome
				.obterEntidadePorId(action.getLong("responsavelTecnicoId"));
		mm.beginTransaction();
		try {
			long superiorId = action.getLong("superiorId");

			/*
			 * if(entidade.verificarRuc(action.getString("ruc"))) throw new
			 * Exception("El ruc " + action.getString("ruc") + " ja esta sendo
			 * utilizado");
			 */

			Entidade superior = null;
			if (superiorId > 0)
				superior = entidadeHome.obterEntidadePorId(action
						.getLong("superiorId"));
			UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
			Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this
					.getUser());
			entidade.atribuirSuperior(superior);
			entidade.atribuirResponsavel(usuarioAtual);
			entidade.atribuirNome(action.getString("nome"));
			entidade.atribuirApelido(action.getString("apelido"));
			entidade.atribuirSigla(action.getString("sigla"));
			entidade.atribuirRUC(action.getString("ruc"));

			entidade.incluir();
			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo")) {
					String nome = key.substring(9, key.length());
					/*
					 * if(nome.equals("cpf"))
					 * if(!entidade.validaCPF(action.getString(key))) throw new
					 * Exception("CPF Inválido");
					 */
					Entidade.Atributo entidadeAtributo = entidade
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}
			mm.commitTransaction();
			this.setResponseView(new EntidadeView(entidade, origemMenu));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(entidade));
			mm.rollbackTransaction();
		}
	}

	public void novaEntidade(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = home.obterEntidadePorId(action
				.getLong("origemMenuId"));
		long superiorId = action.getLong("superiorId");
		String classe = action.getString("classe");

		mm.beginTransaction();
		try {
			if (superiorId == 0)
				throw new Exception(
						"Elegiré la Compañia o el Departamento que desean para colocar la Entidad nueva");

			if (superiorId == -1) {
				this.setResponseView(new NovaEntidadeView(origemMenu));
			} else if (classe.equals("")) {
				this.setResponseView(new NovaEntidadeView(home
						.obterEntidadePorId(superiorId), origemMenu));
			} else {
				Entidade superior = home.obterEntidadePorId(superiorId);
				if (superior.obterClasse().toLowerCase().equals("pessoa"))
					this.setAlert("Pessoa Fisica não pode ser Superior");
				else {
					Entidade entidade = (Entidade) mm.getEntity(classe);
					entidade.atribuirSuperior(home
							.obterEntidadePorId(superiorId));
					entidade.atribuirNome("");
					this
							.setResponseView(new EntidadeView(entidade,
									origemMenu));
				}
			}
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new NovaEntidadeView(origemMenu));
			mm.rollbackTransaction();
		}
	}

	public void popupEntidades(Action action) throws Exception {
		String campoEntidadeId = action.getString("campoEntidadeId");
		String campoEntidadeNome = action.getString("campoEntidadeNome");
		String tipoFixo = action.getString("tipoFixo");
		String tipo = action.getString("tipo");
		String nome = action.getString("nome");
		boolean empUsuPes = action.getBoolean("empUsuPes");

		this.setResponseView(new PopupEntidadesView(campoEntidadeId,
				campoEntidadeNome, tipoFixo, tipo, nome, empUsuPes));
	}

	public void selecionarResponsavelEntidade(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = home.obterEntidadePorId(action.getLong("id"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Collection usuarios = usuarioHome.obterUsuarios();
		this.setResponseView(new SelecaoResponsavelEntidadeView(entidade,
				usuarios));
	}

	public void selecionarSuperiorEntidade(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = home.obterEntidadePorId(action.getLong("id"));
		this.setResponseView(new SelecaoSuperiorEntidadeView(entidade));
	}

	public void visualizarDetalhesEntidade(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(action.getLong("id"));

		this.setResponseView(new EntidadeView(entidade, entidade));
	}

	public void visualizarEventosEntidade(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(action.getLong("id"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action.getLong("origemMenuId"));
		String classeEvento = action.getString("classeEvento");
		String mesAno = action.getString("mesAno");
		String fase = action.getString("_fase");
		int pagina = action.getInt("_pagina");
		
		if(pagina == 0)
			pagina = 1;
		
		boolean usuarioAtual = false;
		if (entidade instanceof Usuario)
			usuarioAtual = ((Usuario) entidade).obterChave().equals(this.getUser().getName());
		this.setResponseView(new EventosEntidadeView(entidade, usuarioAtual,classeEvento, origemMenu, mesAno, pagina));
	}

	public void visualizarHierarquiaEntidade(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		this.setResponseView(new EntidadeHierarquiaView(entidadeHome
				.obterEntidadePorId(action.getLong("id")), origemMenu));
	}

	public void exibirLogs(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		Usuario usuario = null;
		try
		{
			if(action.getBoolean("view"))
				this.setResponseView(new LogsView(usuarioAtual, usuario, dataInicio, dataFim, new ArrayList<Log>()));
			else
			{
				if(action.getLong("usuarioId") == 0)
					throw new Exception("Usuario en blanco");
				
				usuario = (Usuario) entidadeHome.obterEntidadePorId(action.getLong("usuarioId"));
				
				if(dataInicio == null || dataFim == null)
					throw new Exception("Periodo en blanco");
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " 00:00:00";
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
				
				dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				
				Collection<Log> logs = usuarioHome.obterLos(usuario, dataInicio, dataFim);
				
				this.setResponseView(new LogsView(usuarioAtual, usuario, dataInicio, dataFim, logs));
			}
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new LogsView(usuarioAtual, usuario, dataInicio, dataFim, new ArrayList<Log>()));
		}
	}

	public void exibirAnuario(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(action.getLong("id"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action.getLong("origemMenuId"));
		String mes = action.getString("mes");
		String ano = action.getString("ano");
		String situacao = action.getString("situacao");
		boolean geral = action.getBoolean("geral");

		this.setResponseView(new AnuarioView(entidade, mes, ano,null,"","",null,null,0, situacao, geral));
	}
	
	public void visualizarEstatistica(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		Aseguradora aseguradora = null;
		String situacao = action.getString("situacao");
		Date dataInicioD = action.getDate("dataInicio");
		Date dataFimD = action.getDate("dataFim");
		Collection<Apolice> colecao = new ArrayList<Apolice>();
		if(action.getLong("aseguradora") > 0)
			aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradora"));
		
		String secao = action.getString("secao");
		double monto = action.getDouble("monto");
		String modalidade = action.getString("modalidade");
		String tipoInstrumento = action.getString("tipoInstrumento");
		try
		{
			if(action.getBoolean("view"))
				this.setResponseView(new LavagemDinheiroView(aseguradora, action.getString("tipoPessoa"), action.getString("tipoValor"),dataInicioD,dataFimD,action.getInt("qtde"),situacao,colecao, secao, monto, modalidade,tipoInstrumento));
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
				
				if(action.getLong("aseguradora") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradora"));
				
				if(action.getString("tipoValor").equals("valorPrima"))
					colecao = aseguradoraHome.obterApolicesLavagemDinheiroPorPrima(aseguradora,action.getString("tipoPessoa"),dataInicioD,dataFimD,action.getInt("qtde"), situacao, secao, monto, modalidade, tipoInstrumento);
				else if(action.getString("tipoValor").equals("valorSinistro"))
					colecao = aseguradoraHome.obterApolicesLavagemDinheiroPorSinistro(aseguradora,action.getString("tipoPessoa"),dataInicioD,dataFimD,action.getInt("qtde"), situacao, secao, monto, modalidade, tipoInstrumento);
				else
					colecao = aseguradoraHome.obterApolicesLavagemDinheiroPorCapital(aseguradora,action.getString("tipoPessoa"),dataInicioD,dataFimD,action.getInt("qtde"), situacao, secao, monto, modalidade, tipoInstrumento);
				
				if(action.getBoolean("excel"))
				{
					String dataI = new SimpleDateFormat("dd/MM/yyyy").format(dataInicioD);
					String dataF = new SimpleDateFormat("dd/MM/yyyy").format(dataFimD);
					
					dataI = dataI.replace('/', '_');
					dataF = dataF.replace('/', '_');
					String hora = new SimpleDateFormat("HH:mm").format(new Date());
					
					String textoUsuario = "Generado : " + usuario.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					LavagemDinheiroXLS xls = new LavagemDinheiroXLS(colecao, action.getString("tipoValor"), dataInicioD, dataFimD, situacao, action.getString("tipoPessoa"), secao, action.getInt("qtde"),textoUsuario, modalidade);
					InputStream arquivo = xls.obterArquivo();
					String nome = "Mayores Ocurrencias_" +usuario.obterNome()+"_"+ dataI + " hasta " +dataF+ "_"+hora+".xls";
					String mime = "application/vnd.ms-excel";
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName(nome);
			        this.setResponseContentType(mime);
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new LavagemDinheiroView(aseguradora, action.getString("tipoPessoa"), action.getString("tipoValor"),dataInicioD,dataFimD,action.getInt("qtde"),situacao, colecao, secao, monto, modalidade, tipoInstrumento));
			}
		}
		catch (Exception e)
		{
			/*String descricao = e.getMessage() + ":\n";

			StackTraceElement[] trace = e.getStackTrace();
			for (int i = 0; i < trace.length; i++) 
				descricao += trace[i].toString() + "\n";
			
			Email email = new SimpleEmail();
			email.setHostName("mail.bcp.gov.py");
			email.setSmtpPort(25);
			email.setFrom("sisvalidacion@bcp.gov.py");
			email.setSubject("Erro Maiores Apolices");
			email.setMsg(descricao);
			email.addTo("gbrawer@bcp.gov.py");
			email.addCc("giovanni@gdsd.com.br");

			email.send();*/
			
			this.setAlert(Util.translateException(e));
			this.setResponseView(new LavagemDinheiroView(aseguradora, action.getString("tipoPessoa"), action.getString("tipoValor"),dataInicioD,dataFimD,action.getInt("qtde"),situacao,colecao, secao, monto, modalidade, tipoInstrumento));
			//mm.rollbackTransaction();
		}
	}
	
	public void visualizarTotalApoliceSinistro(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		boolean mostraTela = false;
		boolean geraArquivo = false;
		Collection aseguradoras = new ArrayList();
		String secao = action.getString("secao");
		String situacaoSeguro = action.getString("situacaoSeguro");
		try
		{
			if(action.getBoolean("view"))
			{
				this.setResponseView(new TotalApolicesSinistrosView(aseguradora, dataInicio,dataFim, mostraTela, geraArquivo,null,aseguradoras, secao, situacaoSeguro));
			}
			else
			{
				if(dataInicio == null)
					throw new Exception("Fecha Inicio en blanco");
				if(dataFim == null)
					throw new Exception("Fecha Fin en blanco");
				
				String telaStr = action.getString("tela");
				String arquivoStr = action.getString("arquivo");
				
				if(telaStr.equals("") && arquivoStr.equals(""))
					throw new Exception("Opciones en blanco");
				
				if(!telaStr.equals(""))
					mostraTela = Boolean.parseBoolean(telaStr);
				
				if(!arquivoStr.equals(""))
					geraArquivo = Boolean.parseBoolean(arquivoStr);
				
				File file2 = null;
				
				if(geraArquivo)
				{
					String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio);
					String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim);
					
					String dataInicioStr2 = dataInicioStr;
					String dataFimStr2 = dataFimStr;
					
					dataInicioStr2 = dataInicioStr2.replace('/', '_');
					dataFimStr2 = dataFimStr2.replace('/', '_');
					
					String nomeArquivo = "C:/tmp/TotalPolizasSinistros_"+usuario.obterChave()+ "_" + dataInicioStr2 + "_hasta_"+dataFimStr2 + ".txt";
					FileWriter file = new FileWriter(nomeArquivo);
					
					String titulo = "Cantidad Pólizas/Siniestros - " + dataInicioStr + " - " + dataFimStr; 
					
					file.write(titulo + "\r\n");
					
					if(secao.equals(""))
						file.write("Aseguradoras;Cantidad Pólizas;Cantidad Siniestros;Últ.Mes Grabado" + "\r\n");
					else
						file.write("Aseguradoras;Cantidad Pólizas;Sección;%;Cantidad Siniestros;Sección;%;Últ.Mes Grabado" + "\r\n");
					
					if(action.getLong("aseguradora") > 0)
					{
						aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradora"));
						
						int qtdeApolice = aseguradora.obterQtdeApolicesPorPeriodo(dataInicio, dataFim,secao, situacaoSeguro);
						int qtdeSinistros = aseguradora.obterQtdeSinistrosPorPeriodo(dataInicio, dataFim,secao, situacaoSeguro);
						double porcentagemApolices = 0;
						double porcentagemSinistros = 0;
						
						String ultimaAgendaMCIStr = "";
						AgendaMovimentacao ag = aseguradora.obterUltimaAgendaMCI(); 
						if(ag!=null)
							ultimaAgendaMCIStr = ag.obterMesMovimento() + "/" + ag.obterAnoMovimento();
						
						if(!secao.equals(""))
						{
							porcentagemApolices = aseguradora.obterPorcentagemApolices(qtdeApolice);
							porcentagemSinistros = aseguradora.obterPorcentagemSinistros(qtdeSinistros);
							
							DecimalFormat formataValor = new DecimalFormat("#,#0.0");
							
							file.write(aseguradora.obterNome() + ";" + qtdeApolice + ";" + secao + ";" + formataValor.format(porcentagemApolices) + ";"  + qtdeSinistros + ";" + secao + ";" + formataValor.format(porcentagemSinistros) + ";" + ultimaAgendaMCIStr);
						}
						else
							file.write(aseguradora.obterNome() + ";" + qtdeApolice + ";" + qtdeSinistros + ";" + ultimaAgendaMCIStr);
						
						aseguradoras.add(aseguradora);
						
					}
					else
					{
						aseguradoras = aseguradoraHome.obterAseguradorasPorMenor80OrdenadoPorNome();
						
						for(Iterator i = aseguradoras.iterator() ; i.hasNext() ; )
						{
							Aseguradora aseg = (Aseguradora) i.next();
							
							int qtdeApolice = aseg.obterQtdeApolicesPorPeriodo(dataInicio, dataFim,secao, situacaoSeguro);
							int qtdeSinistros = aseg.obterQtdeSinistrosPorPeriodo(dataInicio, dataFim,secao, situacaoSeguro);
							double porcentagemApolices = 0;
							double porcentagemSinistros = 0;
							
							String ultimaAgendaMCIStr = "";
							AgendaMovimentacao ag = aseguradora.obterUltimaAgendaMCI(); 
							if(ag!=null)
								ultimaAgendaMCIStr = ag.obterMesMovimento() + "/" + ag.obterAnoMovimento();
							
							if(!secao.equals(""))
							{
								porcentagemApolices = aseg.obterPorcentagemApolices(qtdeApolice);
								porcentagemSinistros = aseg.obterPorcentagemSinistros(qtdeSinistros);
								
								DecimalFormat formataValor = new DecimalFormat("#,#0.0");
								
								file.write(aseg.obterNome() + ";" + qtdeApolice + ";" + secao + ";" + formataValor.format(porcentagemApolices) + ";"  + qtdeSinistros + ";" + secao + ";" + formataValor.format(porcentagemSinistros) + ";" + ultimaAgendaMCIStr + "\r\n");
							}
							else
								file.write(aseg.obterNome() + ";" + qtdeApolice + ";" + qtdeSinistros + ";" + ultimaAgendaMCIStr + "\r\n");
						}
					}
					
					file.close();
					file2 = new File(nomeArquivo);
				}
				else
				{
					if(action.getLong("aseguradora") > 0)
					{
						aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradora"));
						aseguradoras.add(aseguradora);
					}
					else
						aseguradoras = aseguradoraHome.obterAseguradorasPorMenor80OrdenadoPorNome();
				}
				
				this.setResponseView(new TotalApolicesSinistrosView(aseguradora, dataInicio,dataFim, mostraTela, geraArquivo,file2,aseguradoras,secao, situacaoSeguro));
				if(geraArquivo)
					this.setAlert("Archivo Generado");
			}
		}
		catch (Exception e)
		{
			this.setAlert(Util.translateException(e));
			this.setResponseView(new TotalApolicesSinistrosView(aseguradora, dataInicio,dataFim, mostraTela, geraArquivo,null,aseguradoras,secao, situacaoSeguro));
		}
	}

	public void gerarAnuario(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		ReaseguradoraHome reaseguradoraHome = (ReaseguradoraHome) mm.getHome("ReaseguradoraHome");
		AuxiliarSeguroHome auxiliarSeguroHome = (AuxiliarSeguroHome) mm.getHome("AuxiliarSeguroHome");
		AuditorExternoHome auditorExternoHome = (AuditorExternoHome) mm.getHome("AuditorExternoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Entidade entidade = entidadeHome.obterEntidadePorId(action.getLong("id"));
		boolean geral = action.getBoolean("geral");
		
		String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

		String mes = action.getString("mes");
		String ano = action.getString("ano");
		
		Aseguradora aseguradoraLavagem = null;
		if(action.getLong("aseguradora") > 0)
			aseguradoraLavagem = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradora"));
		
		String tipoPessoa = action.getString("tipoPessoa");
		String tipoValor = action.getString("tipoValor");
		Date dataInicioLavagem = null;
		Date dataFimLavagem = null;
		
		if(action.getDate("dataInicio")!=null)
			dataInicioLavagem = action.getDate("dataInicio");
		if(action.getDate("dataFim")!=null)
			dataFimLavagem = action.getDate("dataFim");
		
		int qtde = action.getInt("qtde");
		String situacao = action.getString("situacao");
		
		mm.beginTransaction();
		
		try
		{
			Date dataModificada = null;
			
			if(!action.getString("lavagem").equals("True") && !action.getString("nomesBranco").equals("True") && !action.getString("ciRucNaoEncontrados").equals("True"))
			{
				if (mes == null || mes.equals(""))
					throw new Exception("Escolha o mes");
				else if (ano == null || ano.equals(""))
					throw new Exception("Escolha o año");
				else if (Integer.parseInt(mes) < 1 || Integer.parseInt(mes) > 12)
					throw new Exception("Mes inválido");
				else if (ano.length() < 4)
					throw new Exception("Año inválido");
		
				if (mes.length() == 1)
					mes = "0" + mes;
			
				dataModificada = new SimpleDateFormat("MM/yyyy").parse(mes + "/" + ano);
			}
			
			FileWriter fileAseguradora = null;
			FileWriter fileGrupoCoasegurador = null;
			FileWriter fileReaseguradora = null;
			FileWriter fileAgentesDeSeguros = null;
			FileWriter fileCorredoresSeguros = null;
			FileWriter fileLiquidadores = null;
			FileWriter fileAuditores = null;
			FileWriter fileAtivos = null;
			FileWriter filePasivos = null;
			FileWriter filePatrimonioNeto = null;
			FileWriter fileIngresos = null;
			FileWriter fileEngresos = null;
			FileWriter fileContas = null;
			FileWriter fileContas2 = null;
			FileWriter fileLavagem = null;
			FileWriter fileNomesEmBranco = null;
	
			if (action.getString("aseguradorasEndereco").equals("True")) 
			{
				fileAseguradora = new FileWriter("C:/Anuario/Aseguradora-" + mes + "-" + ano + ".txt");
				
				fileAseguradora.write("Nombre;Calle;Número;Complemento;Barrio;Caja Postal;Ciudad;Departamento;País;Teléfono Comercial;Teléfono Residencial;Fax;Celular;Email" + "\r\n");
	
				for (Iterator i = aseguradoraHome.obterAseguradorasPorDataResolucao(dataModificada).iterator(); i.hasNext();) 
				{
					Aseguradora aseguradora = (Aseguradora) i.next();
	
					//System.out.println("aseguradora: " +
					// aseguradora.obterNome());
					
					fileAseguradora.write(aseguradora.obterNome());
	
					if (aseguradora.obterEnderecos().size() > 0) 
					{
						for (Iterator j = aseguradora.obterEnderecos().iterator(); j.hasNext();) 
						{
							Aseguradora.Endereco endereco = (Aseguradora.Endereco) j.next();
	
							if (endereco.obterRua() != null	&& !endereco.obterRua().equals(""))
								fileAseguradora.write(";" + endereco.obterRua());
							else
								fileAseguradora.write("; ");
	
							if (endereco.obterNumero() != null && !endereco.obterNumero().equals(""))
								fileAseguradora.write(";" + endereco.obterNumero());
							else
								fileAseguradora.write("; ");
	
							if (endereco.obterComplemento() != null	&& !endereco.obterComplemento().equals(""))
								fileAseguradora.write(";" + endereco.obterComplemento());
							else
								fileAseguradora.write("; ");
	
							if (endereco.obterBairro() != null && !endereco.obterBairro().equals(""))
								fileAseguradora.write(";" + endereco.obterBairro());
							else
								fileAseguradora.write("; ");
	
							if (endereco.obterCep() != null	&& !endereco.obterCep().equals(""))
								fileAseguradora.write(";" + endereco.obterCep());
							else
								fileAseguradora.write("; ");
	
							if (endereco.obterCidade() != null && !endereco.obterCidade().equals(""))
								fileAseguradora.write(";" + endereco.obterCidade());
							else
								fileAseguradora.write("; ");
	
							if (endereco.obterEstado() != null && !endereco.obterEstado().equals(""))
								fileAseguradora.write(";" + endereco.obterEstado());
							else
								fileAseguradora.write("; ");
	
							if (endereco.obterPais() != null && !endereco.obterPais().equals(""))
								fileAseguradora.write(";" + endereco.obterPais());
							else
								fileAseguradora.write("; ");
	
							break;
	
						}
					} 
					else 
					{
						fileAseguradora.write("; ");
						fileAseguradora.write("; ");
						fileAseguradora.write("; ");
						fileAseguradora.write("; ");
						fileAseguradora.write("; ");
						fileAseguradora.write("; ");
						fileAseguradora.write("; ");
						fileAseguradora.write("; ");
					}
	
					Map contatos = new TreeMap();
	
					if (aseguradora.obterContatos().size() > 0)
					{
						boolean telefone = false;
						boolean email = false;
	
						for (Iterator j = aseguradora.obterContatos().iterator(); j.hasNext();) 
						{
							Aseguradora.Contato contato = (Aseguradora.Contato) j.next();
	
							if (contato.obterNome().equals("Teléfono Comercial") || contato.obterNome().equals("Teléfono Residencial")) 
							{
								if (!telefone) 
								{
									contatos.put(contato.obterNome(), contato);
									telefone = true;
								}
							} 
							else if (contato.obterNome().equals("Fax"))
								contatos.put(contato.obterNome(), contato);
							else if (contato.obterNome().equals("Celular"))
								contatos.put(contato.obterNome(), contato);
							else if (contato.obterNome().equals("Email")) 
							{
								if (!email) 
								{
									contatos.put(contato.obterNome(), contato);
									email = true;
								}
							}
						}
					} 
					else 
					{
						fileAseguradora.write("; ");
						fileAseguradora.write("; ");
						fileAseguradora.write("; ");
						fileAseguradora.write("; ");
					}
	
					if (contatos.size() > 0) 
					{
						if (contatos.containsKey("Teléfono Comercial")) 
						{
							Aseguradora.Contato contato = (Aseguradora.Contato) contatos.get("Teléfono Comercial");
							fileAseguradora.write(";" + contato.obterValor());
						} 
						else
							fileAseguradora.write("; ");
	
						if (contatos.containsKey("Teléfono Residencial")) 
						{
							Aseguradora.Contato contato = (Aseguradora.Contato) contatos.get("Teléfono Residencial");
							fileAseguradora.write(";" + contato.obterValor());
						} 
						else
							fileAseguradora.write("; ");
	
						if (contatos.containsKey("Fax")) 
						{
							Aseguradora.Contato contato = (Aseguradora.Contato) contatos.get("Fax");
							fileAseguradora.write(";" + contato.obterValor());
						} 
						else
							fileAseguradora.write("; ");
	
						if (contatos.containsKey("Celular")) 
						{
							Aseguradora.Contato contato = (Aseguradora.Contato) contatos.get("Celular");
							fileAseguradora.write(";" + contato.obterValor());
						} 
						else
							fileAseguradora.write("; ");
	
						if (contatos.containsKey("Email")) 
						{
							Aseguradora.Contato contato = (Aseguradora.Contato) contatos.get("Email");
							fileAseguradora.write(";" + contato.obterValor());
						} 
						else
							fileAseguradora.write("; ");
					}
					
					
	
					fileAseguradora.write("\r\n");
				}
	
				fileAseguradora.close();
			}
	
			if (action.getString("grupoCoasegurador").equals("True")) 
			{
				fileGrupoCoasegurador = new FileWriter("C:/Anuario/GrupoCoasegurador-" + mes + "-" + ano + ".txt");
				
				fileGrupoCoasegurador.write("Piloto;Resolución;Fecha Resolución;Aseguradoras" + "\r\n");
	
				for (Iterator i = aseguradoraHome.obterAseguradorasPorMaior80DataResolucao(dataModificada).iterator(); i.hasNext();) 
				{
					Aseguradora aseguradora = (Aseguradora) i.next();
	
					fileGrupoCoasegurador.write(aseguradora.obterNome());
	
					for (Iterator k = aseguradora.obterInscricoes().iterator(); k.hasNext();) 
					{
						Inscricao inscricao = (Inscricao) k.next();
	
						if (inscricao.obterSituacao().equals("Vigente")) 
						{
							if (inscricao.obterDataResolucao() != null && inscricao.obterDataValidade() != null) 
							{
								String mesResolucao = new SimpleDateFormat("MM").format(inscricao.obterDataResolucao());
								String anoResolucao = new SimpleDateFormat("yyyy").format(inscricao.obterDataResolucao());
								
								String mesValidade = new SimpleDateFormat("MM").format(inscricao.obterDataValidade());
								String anoValidade = new SimpleDateFormat("yyyy").format(inscricao.obterDataValidade());
								
								Date dataResolucao = new SimpleDateFormat("MM/yyyy").parse(mesResolucao + "/" + anoResolucao);
								Date dataValidade = new SimpleDateFormat("MM/yyyy").parse(mesValidade + "/" + anoValidade);
								
								if((dataModificada.after(dataResolucao) || dataModificada.equals(dataResolucao)) && (dataModificada.before(dataValidade) || dataModificada.equals(dataValidade)))
								{
									if (inscricao.obterNumeroResolucao() != null && !inscricao.obterNumeroResolucao().equals(""))
										fileGrupoCoasegurador.write(";"	+ inscricao.obterNumeroResolucao());
									else
										fileGrupoCoasegurador.write("; ");
									
									String data = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataResolucao());
									
									fileGrupoCoasegurador.write(";" + data);
								}
							}
						}
					}
	
					for (Iterator k = aseguradora.obterCoaseguradoresAnuario().iterator(); k.hasNext();) 
					{
						Aseguradora.Coasegurador coasegurador = (Aseguradora.Coasegurador) k.next();
	
						fileGrupoCoasegurador.write(";"	+ coasegurador.obterCodigo());
					}
	
					fileGrupoCoasegurador.write("\r\n");
				}
	
				fileGrupoCoasegurador.close();
			}
	
			if (action.getString("reaseguradora").equals("True")) 
			{
				fileReaseguradora = new FileWriter("C:/Anuario/Reaseguradora-" + mes + "-" + ano + ".txt");
				
				fileReaseguradora.write("Nombre;Pais" + "\r\n");
	
				for (Iterator i = reaseguradoraHome.obterReaseguradorasPorDataResolucao(dataModificada).iterator(); i.hasNext();) 
				{
					Reaseguradora reaseguradora = (Reaseguradora) i.next();
	
					fileReaseguradora.write(reaseguradora.obterNome());
	
					Reaseguradora.Atributo paisOrigem = reaseguradora.obterAtributo("paisorigem");
	
					if (paisOrigem != null)
						fileReaseguradora.write(";" + paisOrigem.obterValor());
					else
						fileReaseguradora.write("; ");
	
					fileReaseguradora.write("\r\n");
				}
	
				fileReaseguradora.close();
			}
	
			if (action.getString("agentesDeSeguros").equals("True")) 
			{
				fileAgentesDeSeguros = new FileWriter("C:/Anuario/AgentesDeSeguros-" + mes + "-" + ano + ".txt");
				
				fileAgentesDeSeguros.write("Inscripción;Nombre;Resolución;Fecha Resolución" + "\r\n");
	
				for (Iterator i = auxiliarSeguroHome.obterAuxiliaresAgentesDeSegurosPorDataResolucao(dataModificada).iterator(); i.hasNext();) 
				{
					AuxiliarSeguro auxiliar = (AuxiliarSeguro) i.next();
	
					for (Iterator j = auxiliar.obterInscricoes().iterator(); j.hasNext();) 
					{
						Inscricao inscricao = (Inscricao) j.next();
	
						if (inscricao.obterSituacao().equals("Vigente")) 
						{
							if (inscricao.obterDataResolucao() != null && inscricao.obterDataValidade() != null) 
							{
								String mesResolucao = new SimpleDateFormat("MM").format(inscricao.obterDataResolucao());
								String anoResolucao = new SimpleDateFormat("yyyy").format(inscricao.obterDataResolucao());
								
								String mesValidade = new SimpleDateFormat("MM").format(inscricao.obterDataValidade());
								String anoValidade = new SimpleDateFormat("yyyy").format(inscricao.obterDataValidade());
								
								Date dataResolucao = new SimpleDateFormat("MM/yyyy").parse(mesResolucao + "/" + anoResolucao);
								Date dataValidade = new SimpleDateFormat("MM/yyyy").parse(mesValidade + "/" + anoValidade);
								
								if((dataModificada.after(dataResolucao) || dataModificada.equals(dataResolucao)) && (dataModificada.before(dataValidade) || dataModificada.equals(dataValidade)))
								{
									fileAgentesDeSeguros.write(inscricao.obterInscricao());
	
									fileAgentesDeSeguros.write(";"	+ auxiliar.obterNome());
	
									if (inscricao.obterNumeroResolucao() != null && !inscricao.obterNumeroResolucao().equals(""))
										fileAgentesDeSeguros.write(";" + inscricao.obterNumeroResolucao());
									else
										fileAgentesDeSeguros.write("; ");
									
									String data = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataResolucao());
									
									fileAgentesDeSeguros.write(";" + data);
								}
							}
						}
					}
	
					fileAgentesDeSeguros.write("\r\n");
				}
	
				fileAgentesDeSeguros.close();
			}
	
			if (action.getString("corredoresDeSeguros").equals("True"))
			{
				fileCorredoresSeguros = new FileWriter("C:/Anuario/CorredoresSeguros-" + mes + "-" + ano + ".txt");
				
				fileCorredoresSeguros.write("Inscripción;Nombre;Resolución;Fecha Resolución" + "\r\n");
	
				for (Iterator i = auxiliarSeguroHome.obterAuxiliaresCorredoresDeSegurosPorDataResolucao(dataModificada).iterator(); i.hasNext();) 
				{
					AuxiliarSeguro auxiliar = (AuxiliarSeguro) i.next();
	
					for (Iterator j = auxiliar.obterInscricoes().iterator(); j.hasNext();) 
					{
						Inscricao inscricao = (Inscricao) j.next();
	
						if (inscricao.obterSituacao().equals("Vigente")) 
						{
							if (inscricao.obterDataResolucao() != null && inscricao.obterDataValidade() != null) 
							{
								String mesResolucao = new SimpleDateFormat("MM").format(inscricao.obterDataResolucao());
								String anoResolucao = new SimpleDateFormat("yyyy").format(inscricao.obterDataResolucao());
								
								String mesValidade = new SimpleDateFormat("MM").format(inscricao.obterDataValidade());
								String anoValidade = new SimpleDateFormat("yyyy").format(inscricao.obterDataValidade());
								
								Date dataResolucao = new SimpleDateFormat("MM/yyyy").parse(mesResolucao + "/" + anoResolucao);
								Date dataValidade = new SimpleDateFormat("MM/yyyy").parse(mesValidade + "/" + anoValidade);
								
								if((dataModificada.after(dataResolucao) || dataModificada.equals(dataResolucao)) && (dataModificada.before(dataValidade) || dataModificada.equals(dataValidade)))
								{
									fileCorredoresSeguros.write(inscricao.obterInscricao());
									
									fileCorredoresSeguros.write(";"	+  auxiliar.obterNome());
	
									if (inscricao.obterNumeroResolucao() != null && !inscricao.obterNumeroResolucao().equals(""))
										fileCorredoresSeguros.write(";"	+ inscricao.obterNumeroResolucao());
									else
										fileCorredoresSeguros.write("; ");
									
									String data = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataResolucao());
									
									fileCorredoresSeguros.write(";" + data);
								}
							}
						}
					}
	
					fileCorredoresSeguros.write("\r\n");
				}
	
				fileCorredoresSeguros.close();
			}
	
			if (action.getString("liquidadores").equals("True")) 
			{
				fileLiquidadores = new FileWriter("C:/Anuario/Liquidadores-" + mes + "-" + ano + ".txt");
				
				fileLiquidadores.write("Inscripción;Nombre;Fecha Resolución;Resolución" + "\r\n");
	
				for (Iterator i = auxiliarSeguroHome.obterAuxiliaresLiquidadoresPorDataResolucao(dataModificada).iterator(); i.hasNext();) 
				{
					AuxiliarSeguro auxiliar = (AuxiliarSeguro) i.next();
	
					for (Iterator j = auxiliar.obterInscricoes().iterator(); j.hasNext();) 
					{
						Inscricao inscricao = (Inscricao) j.next();
	
						if (inscricao.obterSituacao().equals("Vigente")) 
						{
							if (inscricao.obterDataResolucao() != null && inscricao.obterDataValidade() != null) 
							{
							
								String mesResolucao = new SimpleDateFormat("MM").format(inscricao.obterDataResolucao());
								String anoResolucao = new SimpleDateFormat("yyyy").format(inscricao.obterDataResolucao());
								
								String mesValidade = new SimpleDateFormat("MM").format(inscricao.obterDataValidade());
								String anoValidade = new SimpleDateFormat("yyyy").format(inscricao.obterDataValidade());
								
								Date dataResolucao = new SimpleDateFormat("MM/yyyy").parse(mesResolucao + "/" + anoResolucao);
								Date dataValidade = new SimpleDateFormat("MM/yyyy").parse(mesValidade + "/" + anoValidade);
								
								if((dataModificada.after(dataResolucao) || dataModificada.equals(dataResolucao)) && (dataModificada.before(dataValidade) || dataModificada.equals(dataValidade)))
								{	
									fileLiquidadores.write(inscricao.obterInscricao());
	
									fileLiquidadores.write(";" + auxiliar.obterNome());
	
									String data = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataResolucao());
									
									fileLiquidadores.write(";" + data);
	
									if (inscricao.obterNumeroResolucao() != null && !inscricao.obterNumeroResolucao().equals(""))
										fileLiquidadores.write(";" + inscricao.obterNumeroResolucao());
									else
										fileLiquidadores.write("; ");
								}
							}
						}
					}
	
					fileLiquidadores.write("\r\n");
				}
	
				fileLiquidadores.close();
			}
	
			if (action.getString("auditores").equals("True"))
			{
				fileAuditores = new FileWriter("C:/Anuario/Auditores-"+ mes + "-" + ano + ".txt");
				
				fileAuditores.write("Inscripición;Resolución;Fecha Resolución;Nombre;Socios" + "\r\n");
				
				for (Iterator i = auditorExternoHome.obterAuditoresPorDataResolucao(dataModificada).iterator(); i.hasNext();) 
				{
					AuditorExterno auditor = (AuditorExterno) i.next();
	
					for (Iterator j = auditor.obterInscricoes().iterator(); j.hasNext();) 
					{
						Inscricao inscricao = (Inscricao) j.next();
	
						if (inscricao.obterSituacao().equals("Vigente")) 
						{
							if (inscricao.obterDataResolucao() != null && inscricao.obterDataValidade() != null) 
							{
								String mesResolucao = new SimpleDateFormat("MM").format(inscricao.obterDataResolucao());
								String anoResolucao = new SimpleDateFormat("yyyy").format(inscricao.obterDataResolucao());
								
								String mesValidade = new SimpleDateFormat("MM").format(inscricao.obterDataValidade());
								String anoValidade = new SimpleDateFormat("yyyy").format(inscricao.obterDataValidade());
								
								Date dataResolucao = new SimpleDateFormat("MM/yyyy").parse(mesResolucao + "/" + anoResolucao);
								Date dataValidade = new SimpleDateFormat("MM/yyyy").parse(mesValidade + "/" + anoValidade);
								
								if((dataModificada.after(dataResolucao) || dataModificada.equals(dataResolucao)) && (dataModificada.before(dataValidade) || dataModificada.equals(dataValidade)))
								{	
									fileAuditores.write(inscricao.obterInscricao());
	
									if (inscricao.obterNumeroResolucao() != null && !inscricao.obterNumeroResolucao().equals(""))
										fileAuditores.write(";"	+ inscricao.obterNumeroResolucao());
									else
										fileAuditores.write("; ");
									
									String data = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataResolucao());
									
									fileAuditores.write(";" + data);
								}
							}
						}
					}
	
					fileAuditores.write(";" + auditor.obterNome());
	
					for (Iterator j = auditor.obterInferiores().iterator(); j.hasNext();) 
					{
						Entidade entidade2 = (Entidade) j.next();
	
						if (entidade2 instanceof Pessoa) 
						{
							Pessoa.Atributo tipo = (Pessoa.Atributo) entidade2.obterAtributo("tipo");
							if (tipo != null) 
							{
								if (tipo.obterValor().equals("Socio"))
									fileAuditores.write(";" + entidade2.obterNome());
							}
						}
					}
	
					fileAuditores.write("\r\n");
				}
	
				fileAuditores.close();
			}
			
			if (action.getString("ativos").equals("True"))
			{
				fileAtivos = new FileWriter("C:/Anuario/Ativos-"+ mes + "-" + ano + ".txt");
				
				ClassificacaoContas cContas1 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0101000000");
				ClassificacaoContas cContas2 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0102000000");
				ClassificacaoContas cContas3 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0103000000");
				ClassificacaoContas cContas4 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0104000000");
				ClassificacaoContas cContas5 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0105000000");
				ClassificacaoContas cContas6 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0106000000");
				ClassificacaoContas cContas7 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0107000000");
				ClassificacaoContas cContas8 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0108000000");
				ClassificacaoContas cContas9 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0109000000");
				
				fileAtivos.write("Nombre;" + cContas1.obterNome() + ";" + cContas2.obterNome() + ";" + cContas3.obterNome() + ";" + cContas4.obterNome() + ";" + cContas5.obterNome() + ";" + cContas6.obterNome() + ";" + cContas7.obterNome() + ";" + cContas8.obterNome() + ";" + cContas9.obterNome() + ";" + "\r\n");
				
				//DecimalFormat formataValor = new DecimalFormat("#,##0.00");
				DecimalFormat formataValor = new DecimalFormat("###000");
				
				for(Iterator i = aseguradoraHome.obterAseguradorasPorDataResolucao(dataModificada).iterator() ; i.hasNext() ; )
				{
					Aseguradora aseguradora = (Aseguradora) i.next();
					
					double valorCcontas1 = cContas1.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas2 = cContas2.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas3 = cContas3.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas4 = cContas4.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas5 = cContas5.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas6 = cContas6.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas7 = cContas7.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas8 = cContas8.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas9 = cContas9.obterTotalizacaoExistente(aseguradora, mes+ano);
					
					fileAtivos.write(aseguradora.obterNome());
					fileAtivos.write(";" + formataValor.format(valorCcontas1) + ";" + formataValor.format(valorCcontas2) + ";" + formataValor.format(valorCcontas3) + ";" + formataValor.format(valorCcontas4) + ";" + formataValor.format(valorCcontas5) + ";" + formataValor.format(valorCcontas6) + ";" + formataValor.format(valorCcontas7) + ";" + formataValor.format(valorCcontas8) + ";" + formataValor.format(valorCcontas9) + "\r\n");
				}
				
				fileAtivos.close();
			}
			
			if (action.getString("pasivos").equals("True"))
			{
				filePasivos = new FileWriter("C:/Anuario/Pasivos-"+ mes + "-" + ano + ".txt");
				
				ClassificacaoContas cContas1 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0201000000");
				ClassificacaoContas cContas2 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0202000000");
				ClassificacaoContas cContas3 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0203000000");
				ClassificacaoContas cContas4 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0204000000");
				ClassificacaoContas cContas5 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0205000000");
				ClassificacaoContas cContas6 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0206000000");
				ClassificacaoContas cContas7 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0210000000");
				ClassificacaoContas cContas8 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0211000000");
				ClassificacaoContas cContas9 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0212000000");
				ClassificacaoContas cContas10 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0213000000");
				ClassificacaoContas cContas11 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0214000000");
				
				filePasivos.write("Nombre;" + cContas1.obterNome() + ";" + cContas2.obterNome() + ";" + cContas3.obterNome() + ";" + cContas4.obterNome() + ";" + cContas5.obterNome() + ";" + cContas6.obterNome() + ";" + cContas7.obterNome() + ";" + cContas8.obterNome() + ";" + cContas9.obterNome() + ";" + cContas10.obterNome() + ";" + cContas11.obterNome() + ";"+ "\r\n");
				
				//DecimalFormat formataValor = new DecimalFormat("#,##0.00");
				DecimalFormat formataValor = new DecimalFormat("###000");
				
				for(Iterator i = aseguradoraHome.obterAseguradorasPorDataResolucao(dataModificada).iterator() ; i.hasNext() ; )
				{
					Aseguradora aseguradora = (Aseguradora) i.next();
					
					double valorCcontas1 = cContas1.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas2 = cContas2.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas3 = cContas3.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas4 = cContas4.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas5 = cContas5.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas6 = cContas6.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas7 = cContas7.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas8 = cContas8.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas9 = cContas9.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas10 = cContas10.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas11 = cContas11.obterTotalizacaoExistente(aseguradora, mes+ano);
					
					filePasivos.write(aseguradora.obterNome());
					filePasivos.write(";" + formataValor.format(valorCcontas1) + ";" + formataValor.format(valorCcontas2) + ";" + formataValor.format(valorCcontas3) + ";" + formataValor.format(valorCcontas4) + ";" + formataValor.format(valorCcontas5) + ";" + formataValor.format(valorCcontas6) + ";" + formataValor.format(valorCcontas7) + ";" + formataValor.format(valorCcontas8) + ";" + formataValor.format(valorCcontas9) + ";" + formataValor.format(valorCcontas10) + ";" + formataValor.format(valorCcontas11) + "\r\n");
				}
				
				filePasivos.close();
			}
			
			if (action.getString("patrimonioNeto").equals("True"))
			{
				filePatrimonioNeto = new FileWriter("C:/Anuario/Patrimonio_Neto-"+ mes + "-" + ano + ".txt");
				
				ClassificacaoContas cContas1 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0301000000");
				ClassificacaoContas cContas2 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0302000000");
				ClassificacaoContas cContas3 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0303000000");
				ClassificacaoContas cContas4 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0304000000");
				ClassificacaoContas cContas5 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0305000000");
				
				filePatrimonioNeto.write("Nombre;" + cContas1.obterNome() + ";" + cContas2.obterNome() + ";" + cContas3.obterNome() + ";" + cContas4.obterNome() + ";" + cContas5.obterNome() + ";" + "\r\n");
				
				//DecimalFormat formataValor = new DecimalFormat("#,##0.00");
				DecimalFormat formataValor = new DecimalFormat("###000");
				
				for(Iterator i = aseguradoraHome.obterAseguradorasPorDataResolucao(dataModificada).iterator() ; i.hasNext() ; )
				{
					Aseguradora aseguradora = (Aseguradora) i.next();
					
					double valorCcontas1 = cContas1.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas2 = cContas2.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas3 = cContas3.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas4 = cContas4.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas5 = cContas5.obterTotalizacaoExistente(aseguradora, mes+ano);
					
					filePatrimonioNeto.write(aseguradora.obterNome());
					filePatrimonioNeto.write(";" + formataValor.format(valorCcontas1) + ";" + formataValor.format(valorCcontas2) + ";" + formataValor.format(valorCcontas3) + ";" + formataValor.format(valorCcontas4) + ";" + formataValor.format(valorCcontas5) + ";" + "\r\n");
				}
				
				filePatrimonioNeto.close();
			}
			
			if (action.getString("ingresos").equals("True"))
			{
				fileIngresos = new FileWriter("C:/Anuario/Ingresos-"+ mes + "-" + ano + ".txt");
				
				ClassificacaoContas cContas1 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0401000000");
				ClassificacaoContas cContas2 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0402000000");
				ClassificacaoContas cContas3 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0403000000");
				ClassificacaoContas cContas4 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0404000000");
				ClassificacaoContas cContas5 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0405000000");
				ClassificacaoContas cContas6 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0406000000");
				ClassificacaoContas cContas7 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0407000000");
				ClassificacaoContas cContas8 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0408000000");
				ClassificacaoContas cContas9 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0409000000");
				ClassificacaoContas cContas10 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0410000000");
				ClassificacaoContas cContas11 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0411000000");
				ClassificacaoContas cContas12 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0412000000");
				ClassificacaoContas cContas13 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0413000000");
				ClassificacaoContas cContas14 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0414000000");
				ClassificacaoContas cContas15 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0415000000");
				ClassificacaoContas cContas16 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0425000000");
				ClassificacaoContas cContas17 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0426000000");
				ClassificacaoContas cContas18 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0435000000");
				
				fileIngresos.write("Nombre;" + cContas1.obterNome() + ";" + cContas2.obterNome() + ";" + cContas3.obterNome() + ";" + 
						cContas4.obterNome() + ";" + cContas5.obterNome() + ";" + cContas6.obterNome() + ";" + cContas7.obterNome() + ";" 
						+ cContas8.obterNome() + ";" + cContas9.obterNome() + ";" + cContas10.obterNome() + ";" + cContas11.obterNome() + ";"
						+ cContas12.obterNome() + ";" + cContas13.obterNome() + ";" + cContas14.obterNome() + ";" + cContas15.obterNome() + ";" 
						+ cContas16.obterNome() + ";" + cContas17.obterNome() + ";" + cContas18.obterNome() + ";" + "\r\n");
				
				//DecimalFormat formataValor = new DecimalFormat("#,##0.00");
				DecimalFormat formataValor = new DecimalFormat("###000");
				
				for(Iterator i = aseguradoraHome.obterAseguradorasPorDataResolucao(dataModificada).iterator() ; i.hasNext() ; )
				{
					Aseguradora aseguradora = (Aseguradora) i.next();
					
					double valorCcontas1 = cContas1.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas2 = cContas2.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas3 = cContas3.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas4 = cContas4.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas5 = cContas5.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas6 = cContas6.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas7 = cContas7.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas8 = cContas8.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas9 = cContas9.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas10 = cContas10.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas11 = cContas11.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas12 = cContas12.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas13 = cContas13.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas14 = cContas14.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas15 = cContas15.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas16 = cContas16.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas17 = cContas17.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas18 = cContas18.obterTotalizacaoExistente(aseguradora, mes+ano);
					
					
					fileIngresos.write(aseguradora.obterNome());
					fileIngresos.write(";" + formataValor.format(valorCcontas1) + ";" + formataValor.format(valorCcontas2) + ";" + formataValor.format(valorCcontas3) + ";" + formataValor.format(valorCcontas4) + ";" + formataValor.format(valorCcontas5) + ";" + formataValor.format(valorCcontas6) + ";" + formataValor.format(valorCcontas7) + ";" + formataValor.format(valorCcontas8) + ";" + formataValor.format(valorCcontas9) + ";" + formataValor.format(valorCcontas10) + ";" + formataValor.format(valorCcontas11) + ";" + formataValor.format(valorCcontas12) + ";" + formataValor.format(valorCcontas13) + ";" + formataValor.format(valorCcontas14) + ";" + formataValor.format(valorCcontas15) + ";" + formataValor.format(valorCcontas16) + ";" + formataValor.format(valorCcontas17) + ";" + formataValor.format(valorCcontas18) + "\r\n");
				}
				
				fileIngresos.close();
			}
			
			if (action.getString("engresos").equals("True"))
			{
				fileEngresos = new FileWriter("C:/Anuario/Engresos-"+ mes + "-" + ano + ".txt");
				
				ClassificacaoContas cContas1 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0501000000");
				ClassificacaoContas cContas2 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0502000000");
				ClassificacaoContas cContas3 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0503000000");
				ClassificacaoContas cContas4 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0504000000");
				ClassificacaoContas cContas5 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0505000000");
				ClassificacaoContas cContas6 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0506000000");
				ClassificacaoContas cContas7 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0507000000");
				ClassificacaoContas cContas8 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0508000000");
				ClassificacaoContas cContas9 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0509000000");
				ClassificacaoContas cContas10 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0510000000");
				ClassificacaoContas cContas11 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0511000000");
				ClassificacaoContas cContas12 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0512000000");
				ClassificacaoContas cContas13 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0513000000");
				ClassificacaoContas cContas14 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0514000000");
				ClassificacaoContas cContas15 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0515000000");
				ClassificacaoContas cContas16 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0516000000");
				ClassificacaoContas cContas17 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0525000000");
				ClassificacaoContas cContas18 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0526000000");
				ClassificacaoContas cContas19 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0527000000");
				ClassificacaoContas cContas20 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0535000000");
				ClassificacaoContas cContas21 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0540000000");
				
				fileEngresos.write("Nombre;" + cContas1.obterNome() + ";" + cContas2.obterNome() + ";" + cContas3.obterNome() + ";" + 
						cContas4.obterNome() + ";" + cContas5.obterNome() + ";" + cContas6.obterNome() + ";" + cContas7.obterNome() + ";" 
						+ cContas8.obterNome() + ";" + cContas9.obterNome() + ";" + cContas10.obterNome() + ";" + cContas11.obterNome() + ";"
						+ cContas12.obterNome() + ";" + cContas13.obterNome() + ";" + cContas14.obterNome() + ";" + cContas15.obterNome() + ";" 
						+ cContas16.obterNome() + ";" + cContas17.obterNome() + ";" + cContas18.obterNome() + ";" + cContas19.obterNome() + ";" 
						+ cContas20.obterNome() + ";" + cContas21.obterNome() + ";" + "\r\n");
				
				//DecimalFormat formataValor = new DecimalFormat("#,##0.00");
				DecimalFormat formataValor = new DecimalFormat("###000");
				
				for(Iterator i = aseguradoraHome.obterAseguradorasPorDataResolucao(dataModificada).iterator() ; i.hasNext() ; )
				{
					Aseguradora aseguradora = (Aseguradora) i.next();
					
					double valorCcontas1 = cContas1.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas2 = cContas2.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas3 = cContas3.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas4 = cContas4.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas5 = cContas5.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas6 = cContas6.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas7 = cContas7.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas8 = cContas8.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas9 = cContas9.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas10 = cContas10.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas11 = cContas11.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas12 = cContas12.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas13 = cContas13.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas14 = cContas14.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas15 = cContas15.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas16 = cContas16.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas17 = cContas17.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas18 = cContas18.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas19 = cContas19.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas20 = cContas20.obterTotalizacaoExistente(aseguradora, mes+ano);
					double valorCcontas21 = cContas21.obterTotalizacaoExistente(aseguradora, mes+ano);
					
					
					fileEngresos.write(aseguradora.obterNome());
					fileEngresos.write(";" + formataValor.format(valorCcontas1) + ";" + formataValor.format(valorCcontas2) + ";" + formataValor.format(valorCcontas3) + ";" + formataValor.format(valorCcontas4) + ";" + formataValor.format(valorCcontas5) + ";" + formataValor.format(valorCcontas6) + ";" + formataValor.format(valorCcontas7) + ";" + formataValor.format(valorCcontas8) + ";" + formataValor.format(valorCcontas9) + ";" + formataValor.format(valorCcontas10) + ";" + formataValor.format(valorCcontas11) + ";" + formataValor.format(valorCcontas12) + ";" + formataValor.format(valorCcontas13) + ";" + formataValor.format(valorCcontas14) + ";" + formataValor.format(valorCcontas15) + ";" + formataValor.format(valorCcontas16) + ";" + formataValor.format(valorCcontas17) + ";" + formataValor.format(valorCcontas18) + ";" + formataValor.format(valorCcontas19) + ";" + formataValor.format(valorCcontas20) + ";" + formataValor.format(valorCcontas21) +  "\r\n");
				}
				
				fileEngresos.close();
			}
			
			if (action.getString("contas").equals("True"))
			{
				PlanoContasXLS xls = new PlanoContasXLS(aseguradoraHome,mes,ano);
				
				this.setResponseInputStream(xls.obterArquivo());
		        this.setResponseFileName("PlanCuentas_Aseguradoras_"+ mes + "-" + ano + hora+".xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(xls.obterArquivo().available());
		        
				/*fileContas = new FileWriter("C:/Anuario/PlanCuentas-"+ mes + "-" + ano + ".txt");
				
				//DecimalFormat formataValor = new DecimalFormat("#,##0.00");
				DecimalFormat formataValor = new DecimalFormat("###000");
				
				Collection contas = aseguradoraHome.obterContas();
				
				Collection aseguradoras = aseguradoraHome.obterAseguradorasPorMenor80();
				
				fileContas.write("Codigo;Nombre de la Cuenta;");
				
				for(Iterator j = aseguradoras.iterator() ; j.hasNext() ; )
				{
					Aseguradora aseguradora = (Aseguradora) j.next();
					
					fileContas.write(aseguradora.obterNome() + ";");
				}
				
				for(Iterator i = contas.iterator() ; i.hasNext() ; )
				{
					Entidade e = (Entidade) i.next();
					
					fileContas.write("\r\n" + e.obterApelido() + ";" + e.obterNome() + ";");
					
					for(Iterator j = aseguradoras.iterator() ; j.hasNext() ; )
					{
						Aseguradora aseguradora = (Aseguradora) j.next();
						
						if(e instanceof ClassificacaoContas)
						{
							ClassificacaoContas cContas = (ClassificacaoContas) e;
							
							String valor = formataValor.format(cContas.obterTotalizacaoExistente(aseguradora, mes+ano));
							
							fileContas.write(valor + ";");
							
						}
						else if(e instanceof Conta)
						{
							Conta conta = (Conta) e;
							
							String valor = formataValor.format(conta.obterTotalizacaoExistente(aseguradora, mes+ano));
							
							fileContas.write(valor + ";");
						}
					}
				}
				
				fileContas.close();*/
			}
			
			if (action.getString("contas2").equals("True"))
			{
				PlanoContas2XLS xls = new PlanoContas2XLS(aseguradoraHome,mes,ano);
				
				this.setResponseInputStream(xls.obterArquivo());
		        this.setResponseFileName("PlanCuentas_Grupo_Coasegurador_"+ mes + "-" + ano + hora+".xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(xls.obterArquivo().available());
		        
				/*fileContas2 = new FileWriter("C:/Anuario/PlanCuentasGrupoCoasegurador-"+ mes + "-" + ano + ".txt");
				
				//DecimalFormat formataValor = new DecimalFormat("#,##0.00");
				DecimalFormat formataValor = new DecimalFormat("###000");
				
				Collection contas = aseguradoraHome.obterContas();
				
				Collection aseguradoras = aseguradoraHome.obterAseguradorasPorMaior80();
				
				fileContas2.write("Codigo;Nombre de la Cuenta;");
				
				for(Iterator j = aseguradoras.iterator() ; j.hasNext() ; )
				{
					Aseguradora aseguradora = (Aseguradora) j.next();
					
					fileContas2.write(aseguradora.obterNome() + ";");
				}
				
				for(Iterator i = contas.iterator() ; i.hasNext() ; )
				{
					Entidade e = (Entidade) i.next();
					
					fileContas2.write("\r\n" + e.obterApelido() + ";" + e.obterNome() + ";");
					
					for(Iterator j = aseguradoras.iterator() ; j.hasNext() ; )
					{
						Aseguradora aseguradora = (Aseguradora) j.next();
						
						if(e instanceof ClassificacaoContas)
						{
							ClassificacaoContas cContas = (ClassificacaoContas) e;
							
							String valor = formataValor.format(cContas.obterTotalizacaoExistente(aseguradora, mes+ano));
							
							fileContas2.write(valor + ";");
							
						}
						else if(e instanceof Conta)
						{
							Conta conta = (Conta) e;
							
							String valor = formataValor.format(conta.obterTotalizacaoExistente(aseguradora, mes+ano));
							
							fileContas2.write(valor + ";");
						}
					}
				}
				
				fileContas2.close();*/
			}
			
			/*if (action.getString("lavagem").equals("True")) 
			{
				if(action.getDate("dataInicio") == null)
					throw new Exception("Escolha a Fecha Inicio");
				if(action.getDate("dataFim") == null)
					throw new Exception("Escolha a Fecha Fin");
				
				String dataInicio = new SimpleDateFormat("dd/MM/yyyy").format(action.getDate("dataInicio"));
				String dataFim = new SimpleDateFormat("dd/MM/yyyy").format(action.getDate("dataFim"));
				
				fileLavagem = new FileWriter("C:/Anuario/LavadoDinero.txt");
				
				Aseguradora aseguradora = null;
				
				if(action.getLong("aseguradora") > 0)
				{
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradora"));
					fileLavagem.write("Nombre;");
				}
				else
					fileLavagem.write("Aseguradoras;");
				
				fileLavagem.write("Numero Instrumento;Sección;Nombre Asegurado;Tipo Persona;Numero Identificación;");
				
				//DecimalFormat formataValor = new DecimalFormat("#,##0.00");
				DecimalFormat formataValor = new DecimalFormat("###000");
				
				if(action.getString("tipoValor").equals("valorPrima"))
				{
					fileLavagem.write("Valor Prima Gs;");
					fileLavagem.write("Situacion" + "\r\n");
					
					Collection colecao = aseguradoraHome.obterApolicesLavagemDinheiroPorPrima(aseguradora,action.getString("tipoPessoa"),action.getDate("dataInicio"),action.getDate("dataFim"),action.getInt("qtde"), situacao); 
					
					for(Iterator i = colecao.iterator() ; i.hasNext() ; )
					{
						Apolice apolice = (Apolice) i.next();
						
						fileLavagem.write(apolice.obterOrigem().obterNome()+ ";");
						fileLavagem.write(apolice.obterNumeroApolice()+ ";");
						fileLavagem.write(apolice.obterSecao().obterApelido()+ ";");
						fileLavagem.write(apolice.obterNomeAsegurado().trim()+ ";");
						fileLavagem.write(apolice.obterTipoPessoa().trim()+ ";");
						fileLavagem.write(apolice.obterNumeroIdentificacao().trim()+ ";");
						String valor = formataValor.format(apolice.obterPremiosGs());
						fileLavagem.write(valor + ";");
						fileLavagem.write(apolice.obterSituacaoSeguro() + "\r\n");
					}
					
					colecao.clear();
					
				}
				else if(action.getString("tipoValor").equals("valorSinistro"))
				{
					fileLavagem.write("Valor Siniestros Gs;");
					fileLavagem.write("Situacion" + "\r\n");
					
					Collection colecao = aseguradoraHome.obterApolicesLavagemDinheiroPorSinistro(aseguradora,action.getString("tipoPessoa"),action.getDate("dataInicio"),action.getDate("dataFim"),action.getInt("qtde"), situacao);
					
					for(Iterator i = colecao.iterator() ; i.hasNext() ; )
					{
						Apolice apolice = (Apolice) i.next();
						
						fileLavagem.write(apolice.obterOrigem().obterNome()+ ";");
						fileLavagem.write(apolice.obterNumeroApolice()+ ";");
						fileLavagem.write(apolice.obterSecao().obterApelido()+ ";");
						fileLavagem.write(apolice.obterNomeAsegurado().trim()+ ";");
						fileLavagem.write(apolice.obterTipoPessoa().trim()+ ";");
						fileLavagem.write(apolice.obterNumeroIdentificacao().trim()+ ";");
						String valor = formataValor.format(apolice.obterValorTotalDosSinistros());
						fileLavagem.write(valor + ";");
						fileLavagem.write(apolice.obterSituacaoSeguro() + "\r\n");
					}
					
					colecao.clear();
				}
				else
				{
					fileLavagem.write("Valor Capital en Riesgo Gs;");
					fileLavagem.write("Situacion" + "\r\n");
					
					Collection colecao = aseguradoraHome.obterApolicesLavagemDinheiroPorCapital(aseguradora,action.getString("tipoPessoa"),action.getDate("dataInicio"),action.getDate("dataFim"),action.getInt("qtde"), situacao);
					
					for(Iterator i = colecao.iterator() ; i.hasNext() ; )
					{
						Apolice apolice = (Apolice) i.next();
						
						fileLavagem.write(apolice.obterOrigem().obterNome()+ ";");
						fileLavagem.write(apolice.obterNumeroApolice()+ ";");
						fileLavagem.write(apolice.obterSecao().obterApelido()+ ";");
						fileLavagem.write(apolice.obterNomeAsegurado().trim()+ ";");
						fileLavagem.write(apolice.obterTipoPessoa().trim()+ ";");
						fileLavagem.write(apolice.obterNumeroIdentificacao().trim()+ ";");
						String valor = formataValor.format(apolice.obterCapitalGs());
						fileLavagem.write(valor + ";");
						fileLavagem.write(apolice.obterSituacaoSeguro() + "\r\n");
					}
					
					colecao.clear();
				}
				
				fileLavagem.close();
			}*/
			
			if (action.getString("nomesBranco").equals("True")) 
			{
				ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
				
				fileNomesEmBranco = new FileWriter("C:/Anuario/NombresEnBlanco.txt");
				
				fileNomesEmBranco.write("Aseguradora;Sección;Nº del Instrumento;Nombre del Asegurado;Tomador del Seguro" + "\r\n");
				
				for(Iterator i = apoliceHome.obterNomesAseguradoEmBranco().iterator() ; i.hasNext() ; )
				{
					Apolice apolice = (Apolice) i.next();
					
					fileNomesEmBranco.write(apolice.obterOrigem().obterNome() + ";");
					fileNomesEmBranco.write(apolice.obterSecao().obterApelido() + ";");
					fileNomesEmBranco.write(apolice.obterNumeroApolice() + ";");
					fileNomesEmBranco.write(apolice.obterNomeAsegurado() + ";");
					fileNomesEmBranco.write(apolice.obterNomeTomador() + "\r\n");
				}
				
				fileNomesEmBranco.close();
			}
			if(action.getString("ciRucNaoEncontrados").equals("True"))
			{
				for(Iterator i = aseguradoraHome.obterAseguradoras().iterator() ; i.hasNext() ; )
				{
					Aseguradora aseguradora = (Aseguradora) i.next();
					
					if(!aseguradora.obterSigla().equals("016"))
					{
						FileWriter file = new FileWriter("C:/Anuario/" + aseguradora.obterSigla() + "CIRUCNoEncontrados.txt");
						
						for(Iterator j = aseguradora.obterApolicesVigentes().iterator() ; j.hasNext() ; )
						{
							Apolice apolice = (Apolice) j.next();
							
							//System.out.println(apolice.obterNumeroApolice());
							
							String tipoPessoa2 = "";
		                	
		                    if(apolice.obterTipoPessoa()!=null)
		                    {
								if(apolice.obterTipoPessoa().equals("Persona Fisica"))
			                        tipoPessoa2 = "1";
			                    else if(apolice.obterTipoPessoa().equals("Persona Juridic"))
			                        tipoPessoa2 = "2";
		                    }
		                    else
		                    	tipoPessoa2 = " ";
		                    
		                    String tipoIdentificacao = "";
		                    
		                    if(apolice.obterTipoIdentificacao()!=null)
		                    {
			                    if(apolice.obterTipoIdentificacao().equals("C\351dula de Identidad Paraguaya"))
			                        tipoIdentificacao = "1";
			                    else if(apolice.obterTipoIdentificacao().equals("C\351dula de Identidad Extranjera"))
			                        tipoIdentificacao = "2";
			                    else if(apolice.obterTipoIdentificacao().equals("Passaporte"))
			                        tipoIdentificacao = "3";
			                    else if(apolice.obterTipoIdentificacao().equals("RUC"))
			                        tipoIdentificacao = "4";
			                    else if(apolice.obterTipoIdentificacao().equals("Otro"))
			                        tipoIdentificacao = "5";
		                    }
		                    else
		                    	tipoIdentificacao = " ";
		                    
		                    String situacaoSeguro = "";
		                    
		                    if(apolice.obterSituacaoSeguro().equals("Vigente"))
			                    situacaoSeguro = "1";
			                else if(apolice.obterSituacaoSeguro().equals("No Vigente Pendiente"))
			                    situacaoSeguro = "2";
			                else if(apolice.obterSituacaoSeguro().equals("No Vigente"))
			                    situacaoSeguro = "3";
			                else
			                	situacaoSeguro = " ";
		                    
		                    String tipoInstrumento = "";
		                    if(apolice.obterStatusApolice().equals("P\363liza Individual"))
		                        tipoInstrumento = "1";
		                    else if(apolice.obterStatusApolice().equals("P\363liza Madre"))
		                        tipoInstrumento = "2";
		                    else if(apolice.obterStatusApolice().equals("Certificado de Seguro Colectivo"))
		                        tipoInstrumento = "3";
		                    else if(apolice.obterStatusApolice().equals("Certificado Provisorio"))
		                        tipoInstrumento = "4";
		                    else if(apolice.obterStatusApolice().equals("Nota de Cobertura de Reaseguro"))
		                        tipoInstrumento = "5";
		                    else
		                    	tipoInstrumento = " ";
		                    
							if(apolice.obterNumeroIdentificacao()!=null)
							{
			                	String numeroIdentificacaoLimpo = "";
			                	
			                	for (int k = 0; k < apolice.obterNumeroIdentificacao().length(); k++)
			                	{
			            			String caracter = apolice.obterNumeroIdentificacao().substring(k, k + 1);
		
			            			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
			            				numeroIdentificacaoLimpo += caracter;
			            		}
			                    
			                    //Layout 10 Numero apolice, 10 Secao, 1 tipo instrumento, 1 situacao instrumento, 60 nome asegurado, 1 tipo pessoa, 1 tipo identificacao, 15 numero identificacao
			                	if(!entidadeHome.existeDocumento(apolice.obterTipoIdentificacao(), numeroIdentificacaoLimpo, apolice.obterTipoPessoa()))
			                		file.write(apolice.obterNumeroApolice() + apolice.obterSecao().obterApelido() + tipoInstrumento + situacaoSeguro + apolice.obterNomeAsegurado() + tipoPessoa2 + tipoIdentificacao + apolice.obterNumeroIdentificacao() + "\r\n");
			                }
			                else
			                {
			                	
			                	String nomeAsegurado = "";
			                	
			                	if(apolice.obterNomeAsegurado() == null)
			                		nomeAsegurado = this.colocarEspacosADireira(nomeAsegurado, 60);
			                	else
			                		nomeAsegurado = apolice.obterNomeAsegurado();
			                	
			                	String numeroIdentificacao = "";
			                	
			                	numeroIdentificacao = this.colocarEspacosADireira(numeroIdentificacao, 15);
			                	
			                	file.write(apolice.obterNumeroApolice() + apolice.obterSecao().obterApelido() + tipoInstrumento + situacaoSeguro + nomeAsegurado + tipoPessoa2 + tipoIdentificacao + numeroIdentificacao + "\r\n");
			                }
						}
						
						file.close();
					}
				}
			}
			
			String arquivos = "";
			
			if(fileAseguradora != null)
				arquivos+="/bcp001/Anuario/Aseguradora-" + mes + "-" + ano + ".txt, ";
			if(fileGrupoCoasegurador != null)
				arquivos += "/bcp001/Anuario/GrupoCoasegurador-" + mes + "-" + ano + ".txt, ";
			if(fileReaseguradora != null)
				arquivos += "/bcp001/Anuario/Reaseguradora-" + mes + "-" + ano + ".txt, ";
			if(fileAgentesDeSeguros != null)
				arquivos += "/bcp001/Anuario/AgentesDeSeguros-" + mes + "-" + ano + ".txt, ";
			if(fileCorredoresSeguros != null)
				arquivos += "/bcp001/Anuario/CorredoresSeguros-" + mes + "-" + ano + ".txt, ";
			if(fileLiquidadores != null)
				arquivos += "/bcp001/Anuario/Liquidadores-" + mes + "-" + ano + ".txt, ";
			if(fileAuditores != null)
				arquivos += "/bcp001/Anuario/Auditores-"+ mes + "-" + ano + ".txt, ";
			if(fileAtivos != null)
				arquivos += "/bcp001/Anuario/Ativos-"+ mes + "-" + ano + ".txt, ";
			if(filePasivos != null)
				arquivos += "/bcp001/Anuario/Pasivos-"+ mes + "-" + ano + ".txt, ";
			if(filePatrimonioNeto != null)
				arquivos += "/bcp001/Anuario/Patrimonio_Neto-"+ mes + "-" + ano + ".txt, ";
			if(fileIngresos != null)
				arquivos += "/bcp001/Anuario/Ingresos-"+ mes + "-" + ano + ".txt, ";
			if(fileEngresos != null)
				arquivos += "/bcp001/Anuario/Engresos-"+ mes + "-" + ano + ".txt, ";
			if(fileContas != null)
				arquivos += "/bcp001/Anuario/PlanCuentas-"+ mes + "-" + ano + ".txt, ";
			if(fileContas2 != null)
				arquivos += "/bcp001/Anuario/PlanCuentasGrupoCoasegurador-"+ mes + "-" + ano + ".txt, ";
			if(fileLavagem!= null)
				arquivos += "/bcp001/Anuario/LavadoDinero.txt";
			if(fileNomesEmBranco!= null)
				arquivos += "/bcp001/Anuario/NombresEnBlanco.txt";
			if(fileNomesEmBranco!= null)
				arquivos += "/bcp001/Anuario/NombresEnBlanco.txt";
			
			mm.commitTransaction();
			
			this.setResponseView(new AnuarioView(entidade, mes, ano,aseguradoraLavagem,tipoPessoa,tipoValor,dataInicioLavagem,dataFimLavagem,qtde,situacao, geral));
			this.setAlert("Archivos Generados: " + arquivos);
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AnuarioView(entidade, mes, ano,aseguradoraLavagem,tipoPessoa,tipoValor,dataInicioLavagem,dataFimLavagem,qtde,situacao, geral));
			mm.rollbackTransaction();
		}
		
	}

	public void excluirDuplicidade(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("id"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));

		this.setResponseView(new ExcluirDuplicidadeView(entidade));
	}

	public void excluirMovimentos(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("id"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));

		mm.beginTransaction();
		try {
			if (action.getLong("aseguradoraId") == 0)
				throw new Exception("Escolha a Aseguradora");

			if (action.getDate("data") == null)
				throw new Exception("Escolha a Fecha");

			Entidade aseguradora = entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));

			Date data = action.getDate("data");

			String mesInicio = new SimpleDateFormat("MM").format(data);
			String anoInicio = new SimpleDateFormat("yyyy").format(data);
			
			String dataInicioStr = "01/" +  mesInicio + "/" + anoInicio;
			String dataFimStr = "31/" +  mesInicio + "/" + anoInicio;
			
			Date dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse(dataInicioStr);
			Date dataFim = new SimpleDateFormat("dd/MM/yyyy").parse(dataFimStr);
			
			aseguradora.excluirDuplicidade(dataInicio, dataFim);

			this.setAlert("Relatorio e Movimentos Excluidos com Sucesso !");

			this.setResponseView(new ExcluirDuplicidadeView(entidade));

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ExcluirDuplicidadeView(entidade));
			mm.rollbackTransaction();
		}
	}

	public void gerarLog(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("id"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));

		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		for (Iterator i = usuarioAtual.obterEventosComoOrigem().iterator(); i
				.hasNext();) {
			Evento e = (Evento) i.next();
			if (e instanceof Log) {
				if (e.obterFase().obterCodigo().equals(Evento.EVENTO_PENDENTE)) {
					mm.beginTransaction();
					String descricaoLog = e.obterDescricao();
					descricaoLog += "Fecha Salida : "
							+ new SimpleDateFormat("dd/MM/yyyy HH:mm")
									.format(new Date()) + "\n";
					e.atualizarDescricao(descricaoLog);
					e.atualizarFase(Evento.EVENTO_CONCLUIDO);
					mm.commitTransaction();
				}
			}
		}

		UserManager userManager = UserManager.getInstance();
		ControlManager controlManager = new ControlManager();
		controlManager.setUser(null);

		Action action2 = new Action("_showLogonView");
		action2.add("gerarLog", false);

		//if (userManager.needAuthentication(action.getName(), this.getUser()))
		this.forward(action2);
		//else
		//controlManager.forward(new Action("initial"));

		this.forward(action2);
	}
	
	public void carregarRUC(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Usuario usuarioAtual = (Usuario) usuarioHome.obterUsuarioPorUser(this.getUser());
		String bufferAux = "";
		Collection linhas = new ArrayList();
		mm.beginTransaction();
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new CarregarRUCCIView(usuarioAtual));
			else
			{
				entidadeHome.excluirCIRUC();
				
				InputStream is = action.getInputStream("file");
				
				//FileInputStream inputStreamArquivo = (FileInputStream) is;//new FileInputStream("C:/Aseguradoras/Archivos/A" + nomeArquivo + ".txt");
	            
	           // DataInputStream inputStreamData = new DataInputStream(inputStreamArquivo);
	            
	            //while((bufferAux = inputStreamData.readLine()) != null) 
	               // linhas.add(new String(bufferAux));
				
	            Scanner input = new Scanner(is);
	            
				while (input.hasNext()) 
	            //for(Iterator k = linhas.iterator() ; k.hasNext() ; )
				 {
					 
					 String linhaEntrada = input.nextLine();
	            	//String linhaEntrada = (String) k.next();
					 
					 String tipoDocumento = linhaEntrada.substring(0,3);
					 String numeroDocumento = linhaEntrada.substring(3,18);
					 String paisDocumento = linhaEntrada.substring(18,21);
					 String nomePessoa = linhaEntrada.substring(21,81);
					 String sobreNome = linhaEntrada.substring(81,111);
					 String tipoPessoa = linhaEntrada.substring(111,114);
					 if(tipoPessoa.equals("000"))
						 tipoPessoa = "Persona Fisica";
					 else if(tipoPessoa.equals("001"))
						 tipoPessoa = "Persona Juridica";
					 
					 if(numeroDocumento.trim().length() > 0)
		             {
	                	String numeroIdentificacaoLimpo = "";
	                	
	                	for (int i = 0; i < numeroDocumento.length(); i++)
	                	{

	            			String caracter = numeroDocumento.substring(i, i + 1);

	            			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
	            				numeroIdentificacaoLimpo += caracter;
	            		}
		                	
		                entidadeHome.gravarCIRUC(tipoDocumento.trim(), numeroIdentificacaoLimpo.trim(), tipoPessoa);
		             }
					 
					 //entidadeHome.gravarCIRUC(tipoDocumento.trim(), numeroDocumento.trim(), tipoPessoa);
					 
					 String dtNascimento = linhaEntrada.substring(114,122);
					 String ajusteDataNascimento = linhaEntrada.substring(122,123);
					 String versaoTabela = linhaEntrada.substring(123,128);
					 String dataVersaoTabelaStr = linhaEntrada.substring(128,136);
					 String dataAltaStr = linhaEntrada.substring(136,144);
					 String dataBaixaStr = linhaEntrada.substring(144,152);
					 
					 String anoNascimento = dtNascimento.substring(0,4);
					 String mesNascimento = dtNascimento.substring(4,6);
					 String diaNascimento = dtNascimento.substring(6,8);
					 
					 String anoVersaoTabela = dataVersaoTabelaStr.substring(0,4);
					 String mesVersaoTabela = dataVersaoTabelaStr.substring(4,6);
					 String diaVersaoTabela = dataVersaoTabelaStr.substring(6,8);
					 
					 String anoDataAlta = dataAltaStr.substring(0,4);
					 String mesDataAlta = dataAltaStr.substring(4,6);
					 String diaDataAlta = dataAltaStr.substring(6,8);
					 
					 String anoDataBaixa = dataBaixaStr.substring(0,4);
					 String mesDataBaixa = dataBaixaStr.substring(4,6);
					 String diaDataBaixa = dataBaixaStr.substring(6,8);
					 
					 Date dataNascimento = null;
					 Date dataVersaoTabela = null;
					 Date dataAlta = null;
					 Date dataBaixa = null;
					 
					 if(!anoNascimento.equals("0000"))
						 dataNascimento = new SimpleDateFormat("dd/MM/yyyy").parse(diaNascimento + "/" + mesNascimento + "/" + anoNascimento);
					 
					 if(!anoVersaoTabela.equals("0000"))
						 dataVersaoTabela = new SimpleDateFormat("dd/MM/yyyy").parse(diaVersaoTabela + "/" + mesVersaoTabela + "/" + anoVersaoTabela);
					 
					 // Data de Inicio
					 if(!anoDataAlta.equals("0000"))
						 dataAlta = new SimpleDateFormat("dd/MM/yyyy").parse(diaDataAlta + "/" + mesDataAlta + "/" + anoDataAlta);
					 
					 // Data final, ex.: empresa parou de funcionar
					 if(!anoDataBaixa.equals("0000"))
						 dataBaixa = new SimpleDateFormat("dd/MM/yyyy").parse(diaDataBaixa + "/" + mesDataBaixa + "/" + anoDataBaixa);
					 
					 
					 System.out.println("Tipo Documento:" + tipoDocumento);
					 System.out.println("Numero Documento:" + numeroDocumento);
					 System.out.println("Pais:" + paisDocumento);
					 System.out.println("nomePessoa:" + nomePessoa);
					 System.out.println("sobreNome:" + sobreNome);
					 System.out.println("tipoPessoa:" + tipoPessoa);
					 System.out.println("dtNascimento:" + diaNascimento + "/" + mesNascimento + "/" + anoNascimento);
					 System.out.println("ajusteDataNascimento:" + ajusteDataNascimento);
					 System.out.println("versaoTabela:" + versaoTabela);
					 System.out.println("dataVersaoTabelaStr:" + diaVersaoTabela + "/" + mesVersaoTabela + "/" + anoVersaoTabela);
					 System.out.println("dataAltaStr:" + diaDataAlta + "/" + mesDataAlta + "/" + anoDataAlta);
					 System.out.println("dataBaixaStr:" + diaDataBaixa + "/" + mesDataBaixa + "/" + anoDataBaixa);
					 
					 this.setAlert("RUC e CI Carregadas!");
					 
					 this.setResponseView(new CarregarRUCCIView(usuarioAtual));
				 }
			 }

			mm.commitTransaction();
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuarioAtual,usuarioAtual));
			mm.rollbackTransaction();
		}

	}
	
	public void manutBase(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		Usuario usuarioAtual = (Usuario) usuarioHome.obterUsuarioPorUser(this.getUser());
		mm.beginTransaction();
		try 
		{
			entidadeHome.manutBase();
			
			this.setResponseView(new PaginaInicialView(usuarioAtual,usuarioAtual));
			this.setAlert("Pronto !!");
			mm.commitTransaction();
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuarioAtual,usuarioAtual));
			mm.rollbackTransaction();
		}
	}
	
	public void visualizarConsolidado(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		String opcao = action.getString("opcao");
		String escolha = action.getString("escolha");
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		Collection informacoes = new ArrayList();
		boolean mostraTela = false;
		boolean excel = false;
		boolean admin = usuario.obterId() == 1;
		Aseguradora aseguradora = null;
		String situacaoSeguro = action.getString("situacaoSeguro");
		
		try 
		{
			if(action.getBoolean("calcular"))
			{
				if(dataInicio == null)
					throw new Exception("Fecha Inicio en blanco");
				if(dataFim == null)
					throw new Exception("Fecha Fin en blanco");
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " 00:00:00";
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
				
				dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(escolha.equals(""))
					throw new Exception("Opciones en blanco");
				
				if(escolha.equals("tela"))
					mostraTela = true;
				else
					excel = true;
				
				informacoes = apoliceHome.obterConsolidado(aseguradora, situacaoSeguro, opcao, dataInicio, dataFim, admin);
				
				if(excel)
				{
					String textoUsuario = "Generado : " + usuario.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					ConsolidadoApolicesSinistrosXLS xls = new ConsolidadoApolicesSinistrosXLS(aseguradora, opcao, situacaoSeguro, dataInicio, dataFim, informacoes,textoUsuario);
					
					String dataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
					
					this.setResponseInputStream(xls.obterArquivo());
			        this.setResponseFileName("Consolidado Pólizas/Sección_" + opcao+"_"+usuario.obterNome() + "_"+dataHora + ".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(xls.obterArquivo().available());
				}
				else
					this.setResponseView(new ConsolidadoApolicesSinistrosView(aseguradora, opcao, situacaoSeguro, dataInicio, dataFim,mostraTela,excel,informacoes));
			}
			else
				this.setResponseView(new ConsolidadoApolicesSinistrosView(null, opcao, situacaoSeguro, dataInicio, dataFim,mostraTela,excel,informacoes));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ConsolidadoApolicesSinistrosView(null,opcao, situacaoSeguro, dataInicio, dataFim,mostraTela,excel,informacoes));
		}
	}
	
	
	private String colocarEspacosADireira(String texto, int tamanhoCampo)
	{
		String espacos = "";
		
		for(int i = texto.length() ; i < tamanhoCampo - texto.length() ; i++)
			espacos+=" ";
		
		return texto + espacos;
	}
	
	private void carregarRUCMinhaBase() throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		
		File file = new File("C:/Aseguradoras/Archivos/B055200805.txt");
		
		Scanner scan = new Scanner(file);
		
		entidadeHome.excluirCIRUC();
		
		while(scan.hasNext())
		{
			String linha = scan.nextLine();
			
			System.out.println(linha.substring(5,7));
			
			if(linha.substring(5,7).equals("17"))
			{
				String tipoPessoa = "";
				
                if(linha.substring(87, 88).equals("1"))
                    tipoPessoa = "Persona Fisica";
                else
                if(linha.substring(87, 88).equals("2"))
                    tipoPessoa = "Persona Juridica";
                
                String tipoIdentificacao = "";
                
                if(linha.substring(88, 89).equals("1"))
                    tipoIdentificacao = "C\351dula de Identidad Paraguaya";
                else
                if(linha.substring(88, 89).equals("2"))
                    tipoIdentificacao = "C\351dula de Identidad Extranjera";
                else
                if(linha.substring(88, 89).equals("3"))
                    tipoIdentificacao = "Passaporte";
                else
                if(linha.substring(88, 89).equals("4"))
                    tipoIdentificacao = "RUC";
                else
                if(linha.substring(88, 89).equals("5"))
                    tipoIdentificacao = "Otro";
                
                String numeroIdentificacao = linha.substring(89, 104);
                
                if(numeroIdentificacao.trim().length() > 0)
                {
                	String numeroIdentificacaoLimpo = "";
                	
                	for (int i = 0; i < numeroIdentificacao.length(); i++)
                	{

            			String caracter = numeroIdentificacao.substring(i, i + 1);

            			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
            				numeroIdentificacaoLimpo += caracter;
            		}
                	
                	entidadeHome.gravarCIRUC(tipoIdentificacao.trim(), numeroIdentificacaoLimpo.trim(), tipoPessoa);
                }
                	
			}
		}
	}
	
	public void selecionarRelEntidadesVigentes(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		//EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		ReaseguradoraHome reaseguradoraHome = (ReaseguradoraHome) mm.getHome("ReaseguradoraHome");
		AuditorExternoHome auditorExternoHome = (AuditorExternoHome) mm.getHome("AuditorExternoHome");
		CorretoraHome corretoraHome = (CorretoraHome) mm.getHome("CorretoraHome");
		AuxiliarSeguroHome auxiliarSeguroHome = (AuxiliarSeguroHome) mm.getHome("AuxiliarSeguroHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = (Usuario) usuarioHome.obterUsuarioPorUser(this.getUser());
		int escolha = action.getInt("escolha");
		Date data = action.getDate("data");
		String texto = action.getString("texto");
		String opcao = action.getString("gerar");
		boolean ci = action.getBoolean("ci");
		
		String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
		String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
		
		try
		{
			if(action.getBoolean("view"))
				this.setResponseView(new SelecionaRelEntidadesVigentesView(escolha,data,texto,opcao,ci));
			else
			{
				if(opcao.equals(""))
					throw new Exception("Opciones en blanco");
				
				InputStream arquivo = null;
				String nome = "";
				String mime = "";
				
				if(data == null && escolha!=8)
					throw new Exception("Fecha en blanco");
				
				Collection lista = null;
				
				if(escolha == 1)
				{
					lista = auxiliarSeguroHome.obterAgentesSegurosVigente(data,ci);
					
					if(opcao.equals("pdf"))
					{
						AgentesReport pdf = new AgentesReport(lista,data,texto,textoUsuario,ci);
						
						arquivo = pdf.obterArquivo();
						nome = "Agentes de Seguros Vigentes " + new SimpleDateFormat("dd/MM/yyyy").format(data) + hora+".pdf";
						mime = "application/pdf";
					}
					else
					{
						AgentesXLS xls = new AgentesXLS(lista, data, texto,textoUsuario,ci);
						
						arquivo = xls.obterArquivo();
						nome = "Agentes de Seguros Vigentes " + new SimpleDateFormat("dd/MM/yyyy").format(data) + hora+".xls";
						mime = "application/vnd.ms-excel";
					}
				}
				else if(escolha == 2)
				{
					lista = auxiliarSeguroHome.obterCorredoresSegurosVigente(data,ci);
					
					if(opcao.equals("pdf"))
					{
						CorredoresReport pdf = new CorredoresReport(lista,data,texto,textoUsuario,ci);
						
						arquivo = pdf.obterArquivo();
						nome = "Corredores de Seguros Vigentes " + new SimpleDateFormat("dd/MM/yyyy").format(data) +  hora+".pdf";
						mime = "application/pdf";
					}
					else
					{
						CorredoresXLS xls = new CorredoresXLS(lista, data, texto,textoUsuario,ci);
						
						arquivo = xls.obterArquivo();
						nome = "Corredores de Seguros Vigentes " + new SimpleDateFormat("dd/MM/yyyy").format(data) +  hora+".xls";
						mime = "application/vnd.ms-excel";
					}
				}
				else if(escolha == 3)
				{
					lista = reaseguradoraHome.obterReaseguradorasVigentes(data);
					
					if(opcao.equals("pdf"))
					{
						ReaseguradoraReport pdf = new ReaseguradoraReport(lista,data,texto,textoUsuario,ci);
						
						arquivo = pdf.obterArquivo();
						nome = "Reaseguradoras Vigentes " + new SimpleDateFormat("dd/MM/yyyy").format(data) +  hora+".pdf";
						mime = "application/pdf";
					}
					else
					{
						ReaseguradorasXLS xls = new ReaseguradorasXLS(lista, data, texto,textoUsuario,ci);
						
						arquivo = xls.obterArquivo();
						nome = "Reaseguradoras Vigentes " + new SimpleDateFormat("dd/MM/yyyy").format(data) +  hora+".xls";
						mime = "application/vnd.ms-excel";
					}
				}
				else if(escolha == 4)
				{
					lista = auxiliarSeguroHome.obterLiquidadoresSinistroVigente(data,ci);
					
					if(opcao.equals("pdf"))
					{
						LiquidadoresReport pdf = new LiquidadoresReport(lista,data,texto,textoUsuario,ci);
						
						arquivo = pdf.obterArquivo();
						nome = "Liquidadores de Siniestros Vigentes " + new SimpleDateFormat("dd/MM/yyyy").format(data) +  hora+".pdf";
						mime = "application/pdf";
					}
					else
					{
						LiquidadoresXLS xls = new LiquidadoresXLS(lista, data, texto,textoUsuario,ci);
						
						arquivo = xls.obterArquivo();
						nome = "Liquidadores de Siniestros Vigentes " + new SimpleDateFormat("dd/MM/yyyy").format(data) +  hora+".xls";
						mime = "application/vnd.ms-excel";
					}
				}
				else if(escolha == 5)
				{
					lista = auditorExternoHome.obterAuditoresVigentes(data,ci);
					
					Collection lista2 = auditorExternoHome.obterAuditoresNoVigentes(data,ci);
					
					if(opcao.equals("pdf"))
					{
						AuditoresReport pdf = new AuditoresReport(lista, lista2, data,texto,textoUsuario,ci);
						
						arquivo = pdf.obterArquivo();
						nome = "Auditores Externos Vigentes " + new SimpleDateFormat("dd/MM/yyyy").format(data) +  hora+".pdf";
						mime = "application/pdf";
					}
					else
					{
						AuditoresXLS xls = new AuditoresXLS(lista, lista2, data, texto,textoUsuario,ci);
						
						arquivo = xls.obterArquivo();
						nome = "Auditores Externos Vigentes " + new SimpleDateFormat("dd/MM/yyyy").format(data) +  hora+".xls";
						mime = "application/vnd.ms-excel";
					}
				}
				else if(escolha == 6)
				{
					lista = corretoraHome.obterCorretorasVigentes(data,ci);
					
					if(opcao.equals("pdf"))
					{
						CorretorasReport pdf = new CorretorasReport(lista, data,texto,textoUsuario,ci);
						
						arquivo = pdf.obterArquivo();
						nome = "Corredoras Vigentes " + new SimpleDateFormat("dd/MM/yyyy").format(data) +  hora+".pdf";
						mime = "application/pdf";
					}
					else
					{
						CorretorasXLS xls = new CorretorasXLS(lista, data, texto,textoUsuario,ci);
						
						arquivo = xls.obterArquivo();
						nome = "Corredoras Vigentes " + new SimpleDateFormat("dd/MM/yyyy").format(data) +  hora+".xls";
						mime = "application/vnd.ms-excel";
					}
				}
				else if(escolha == 7)
				{
					lista = reaseguradoraHome.obterReaseguradorasVigentes2(data);
					
					if(opcao.equals("pdf"))
					{
						ReaseguradoraQualificacaoPDF pdf = new ReaseguradoraQualificacaoPDF(lista, data,textoUsuario);
						
						arquivo = pdf.obterArquivo();
						nome = "Reaseguradoras_Calificacion " + new SimpleDateFormat("dd/MM/yyyy").format(data) +  hora+".pdf";
						mime = "application/pdf";
					}
					else
					{
						ReaseguradoraQualificacaoXLS xls = new ReaseguradoraQualificacaoXLS(lista, data,textoUsuario);
						
						arquivo = xls.obterArquivo();
						nome = "Reaseguradoras_Calificacion " + new SimpleDateFormat("dd/MM/yyyy").format(data) +  hora+".xls";
						mime = "application/vnd.ms-excel";
					}
				}
				else if(escolha == 8)
				{
					lista = aseguradoraHome.obterAseguradoras();
					
					if(opcao.equals("pdf"))
					{
						AseguradoraValidezPDF pdf = new AseguradoraValidezPDF(lista, textoUsuario);
						
						arquivo = pdf.obterArquivo();
						nome = "Aseguradoras_Validez.pdf";
						mime = "application/pdf";
					}
					else
					{
						AseguradoraValidezXLS xls = new AseguradoraValidezXLS(lista, textoUsuario);
						
						arquivo = xls.obterArquivo();
						nome = "Aseguradoras_Validez.xls";
						mime = "application/vnd.ms-excel";
					}
				}
				
				if(arquivo!=null)
				{
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName(nome);
			        this.setResponseContentType(mime);
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new SelecionaRelEntidadesVigentesView(escolha,data,texto,opcao,ci));
					
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			this.setAlert(e.getMessage());
			this.setResponseView(new SelecionaRelEntidadesVigentesView(escolha,data,texto,opcao,ci));
		}
	}
	
	public void visualizarGEE(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Date data = action.getDate("data");
		try
		{
			if(action.getBoolean("view"))
				this.setResponseView(new GEEView(data));
			else
			{
				if(data == null)
					throw new Exception("Fecha en blanco");
				
				AseguradoraHome home = (AseguradoraHome) mm.getHome("AseguradoraHome");
				int cont = 0;
				String aseguradorasMenor80 = "";
				
				for(Iterator i = home.obterAseguradorasPorMenor80OrdenadoPorNome().iterator() ; i.hasNext() ; )
				{
					Aseguradora aseg = (Aseguradora) i.next();
					
					if(aseg.obterId()!=5205)
					{
						if(cont == 0)
							aseguradorasMenor80+="(seguradora=" + aseg.obterId();
						else
							aseguradorasMenor80+=" or seguradora=" + aseg.obterId();
						
						cont++;
					}
				}
				
				aseguradorasMenor80+=")";
				
				String textoUsuario = "Generado : " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				
				GEEXLS xls = new GEEXLS(data, entidadeHome, aseguradorasMenor80, textoUsuario);
				
				String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				
				InputStream arquivo = xls.obterArquivo();
				String nome = "SERVICIOS DE SEGUROS - " + new SimpleDateFormat("dd/MM/yyyy").format(data) + hora+".xls";
				String mime = "application/vnd.ms-excel";
				
				this.setResponseInputStream(arquivo);
		        this.setResponseFileName(nome);
		        this.setResponseContentType(mime);
		        this.setResponseContentSize(arquivo.available());
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			this.setAlert(e.getMessage());
			this.setResponseView(new GEEView(data));
		}
	}
	
	public void visualizarInspecaoSitu(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		//EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		try
		{
			this.setResponseView(new InspecaoSituView());
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new InspecaoSituView());
		}
		
	}
	
	public void relArquivosEmProcessamento(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		Collection<FilaProcessamento> arquivos = home.obterArquivosFila();
		try
		{
			if(action.getBoolean("excluir"))
			{
				String nomeArquivo = action.getString("nome");
				
				home.excluirArquivoFila(nomeArquivo);
			}
			else if(action.getBoolean("excluir2"))
			{
				String nomeArquivo = action.getString("nomeArquivo").trim();
				if(nomeArquivo.length() == 0)
					throw new Exception("Nombre en blanco");
				
				home.excluirArquivoFila(nomeArquivo);
				
				if(nomeArquivo.toLowerCase().substring(0, 1).equals("a"))
				{
					nomeArquivo = nomeArquivo.replace("A", "B");
					nomeArquivo = nomeArquivo.replace("a", "B");
					
					home.excluirArquivoFila(nomeArquivo);
				}
				else
					home.excluirArquivoFila(nomeArquivo);
				
				this.setAlert("Archivo Eliminado");
			}
			
			arquivos = home.obterArquivosFila();
			
			this.setResponseView(new FilaProcessamentoView(arquivos));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new FilaProcessamentoView(arquivos));
		}
		
	}
}
