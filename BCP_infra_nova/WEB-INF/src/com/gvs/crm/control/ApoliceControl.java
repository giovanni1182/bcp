package com.gvs.crm.control;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.ApoliceHome;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.AspectosLegais;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.SinistroHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.report.AnualidadeApolicesSinistrosXLS;
import com.gvs.crm.report.ApolicesDemandaJudicialXLS;
import com.gvs.crm.report.ApolicesModalidadeXLS;
import com.gvs.crm.report.ApolicesPorPessoaViewXLS;
import com.gvs.crm.report.ApolicesSuspeitasXLS;
import com.gvs.crm.report.CoberturaReaseguroPDF;
import com.gvs.crm.report.EstatisticaAnulacaoReasegurosXLS;
import com.gvs.crm.report.EstatisticaAnulacaoXLS;
import com.gvs.crm.report.EstatisticaApoliceXLS;
import com.gvs.crm.report.EstatisticaAseguradoXLS;
import com.gvs.crm.report.EstatisticaCoasegurosXLS;
import com.gvs.crm.report.EstatisticaCobrancaXLS;
import com.gvs.crm.report.EstatisticaDemandaJudicialXLS;
import com.gvs.crm.report.EstatisticaEndosoXLS;
import com.gvs.crm.report.EstatisticaFinalizacaoXLS;
import com.gvs.crm.report.EstatisticaFinancimantoXLS;
import com.gvs.crm.report.EstatisticaGastosXLS;
import com.gvs.crm.report.EstatisticaMorosidadeXLS;
import com.gvs.crm.report.EstatisticaPagosXLS;
import com.gvs.crm.report.EstatisticaReasegurosXLS;
import com.gvs.crm.report.EstatisticaReservasXLS;
import com.gvs.crm.report.EstatisticaSinistrosXLS;
import com.gvs.crm.report.LocalizarApolicesXLS;
import com.gvs.crm.report.QtdeApolicesReasegurosXLS;
import com.gvs.crm.report.TotalApolicesSecaoAnualXLS;
import com.gvs.crm.report.TotalApolicesSecaoXLS;
import com.gvs.crm.view.AnualidadeApolicesSinistrosView;
import com.gvs.crm.view.ApoliceGeralView;
import com.gvs.crm.view.ApolicesDemandaJudicialView;
import com.gvs.crm.view.ApolicesModalidadeView;
import com.gvs.crm.view.ApolicesPorCorretoraAseguradoraView;
import com.gvs.crm.view.ApolicesPorCorretoraView;
import com.gvs.crm.view.ApolicesPorPessoaView;
import com.gvs.crm.view.ApolicesPorReaseguradoraView;
import com.gvs.crm.view.ApolicesReaseguroView;
import com.gvs.crm.view.ApolicesSuspeitasView;
import com.gvs.crm.view.ApolicesView;
import com.gvs.crm.view.DemandaJudicialView;
import com.gvs.crm.view.EstatisticaAnulacaoReasegurosView;
import com.gvs.crm.view.EstatisticaAnulacaoView;
import com.gvs.crm.view.EstatisticaApoliceView;
import com.gvs.crm.view.EstatisticaAseguradoView;
import com.gvs.crm.view.EstatisticaCIView;
import com.gvs.crm.view.EstatisticaCoasegurosView;
import com.gvs.crm.view.EstatisticaCobrancaView;
import com.gvs.crm.view.EstatisticaDemandaJudicialView;
import com.gvs.crm.view.EstatisticaEndosoView;
import com.gvs.crm.view.EstatisticaFinalizacaoView;
import com.gvs.crm.view.EstatisticaFinanciamentoView;
import com.gvs.crm.view.EstatisticaGastosView;
import com.gvs.crm.view.EstatisticaMorosidadeView;
import com.gvs.crm.view.EstatisticaPagosView;
import com.gvs.crm.view.EstatisticaReasegurosView;
import com.gvs.crm.view.EstatisticaReservasView;
import com.gvs.crm.view.EstatisticaSinistrosView;
import com.gvs.crm.view.ListaApolicesSuspeitasView;
import com.gvs.crm.view.LocalizarApoliceView;
import com.gvs.crm.view.PaginaInicialView;
import com.gvs.crm.view.QtdeApolicesReasegurosView;
import com.gvs.crm.view.TotalApolicesPorSecaoAnualView;
import com.gvs.crm.view.TotalApolicesPorSecaoView;

import infra.control.Action;
import infra.control.Control;

public class ApoliceControl extends Control
{
	public void visualizarApoliceGeral(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Apolice apolice = (Apolice) eventoHome.obterEventoPorId(action.getLong("id"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		try
		{
			this.setResponseView(new ApoliceGeralView(apolice));

		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuarioAtual,usuarioAtual));
		}
	}

	public void visualizarApolicesSuspeitas(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");

		Collection apolices = new ArrayList();

		mm.beginTransaction();
		try {

			if (action.getBoolean("lista")) 
			{
				if (action.getLong("valor") == 0) 
				{
					apolices.addAll(apoliceHome.obterApolicesSuspeitas1());

					apolices.addAll(apoliceHome.obterApolicesSuspeitas2());

					apolices.addAll(apoliceHome.obterApolicesSuspeitas3());
				}
				
				else if (action.getLong("valor") == 1)
					apolices = apoliceHome.obterApolicesSuspeitas1();
				else if (action.getLong("valor") == 2)
					apolices = apoliceHome.obterApolicesSuspeitas2();
				else if (action.getLong("valor") == 3)
					apolices = apoliceHome.obterApolicesSuspeitas3();
			}
			
			if(!action.getBoolean("excel"))
				this.setResponseView(new ApolicesSuspeitasView(apolices, action.getLong("valor"), action.getBoolean("lista")));
			else
			{
				String textoUsuario = "Generado : " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				
				ApolicesSuspeitasXLS xls = new ApolicesSuspeitasXLS(apolices,action.getLong("valor"), textoUsuario);
				
				String hora = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				
				InputStream arquivo = xls.obterArquivo();
				this.setResponseInputStream(arquivo);
		        this.setResponseFileName("Operaciones Sospechosas "+"_"+usuarioAtual.obterNome()+"_"+hora+".xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(arquivo.available());
			}
			
			mm.commitTransaction();

		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ApolicesSuspeitasView(apolices, action.getLong("valor"), action.getBoolean("lista")));
			mm.rollbackTransaction();
		}
	}

	public void localizarApolices(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = (Usuario) usuarioHome.obterUsuarioPorUser(this.getUser());
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action.getLong("origemMenuId"));
		Aseguradora aseguradora = null;
		if (action.getLong("aseguradoraId") > 0)
			aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));

		Collection apolices = new ArrayList();

		String numeroInstrumento = action.getString("numeroApolice").trim();
		String secao = action.getString("secao");
		String nomeAsegurado = action.getString("nomeAsegurado").trim();
		String tomador = action.getString("tomador").trim();
		String plano = action.getString("plano").trim();
		int pagina = action.getInt("_paginaBusca");
		String situacao = action.getString("situacao").trim();
		Date dataInicio;
		Date dataFim;
		try
		{
			dataInicio = action.getDate("dataInicio");
		}
		catch(Exception e)
		{
			dataInicio = null;
		}
		try
		{
			dataFim = action.getDate("dataFim");
		}
		catch(Exception e)
		{
			dataFim = null;
		}
		
		String rucCi = action.getString("rucCi").trim();
		String tipoInstrumento = action.getString("tipoInstrumento");
		
		String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
		String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
		
		try 
		{
			if (action.getBoolean("listar")) 
			{
				if(tomador.length() == 0 && rucCi.length() == 0 && nomeAsegurado.length() == 0)
				{
					int cont = 0;
					if(aseguradora != null)
						cont++;
					if(!numeroInstrumento.equals(""))
						cont++;
					if(!secao.equals(""))
						cont++;
					if(!nomeAsegurado.equals(""))
						cont++;
					if(!plano.equals(""))
						cont++;
					if(!situacao.equals("0"))
						cont++;
					if(dataInicio!=null)
						cont++;
					if(dataFim!=null)
						cont++;
					if(!tipoInstrumento.equals("0"))
						cont++;
					
					boolean aa = true;
					
					if(numeroInstrumento.length() > 0 && numeroInstrumento.length()< 10 && aseguradora == null)
					{
						aa = false;
						throw new Exception("Elija Aseguradora");
					}
					
					if(cont<3 && !aa)
						throw new Exception("Es necesario un filtro adicional");
					
				}
				else
				{
					if(rucCi.length() > 0)
					{
						if(rucCi.length() < 5)
							throw new Exception("Mínimo de 5 dígitos para el campo Documento");
					}
					if(nomeAsegurado.length() > 0)
					{
						if(nomeAsegurado.length() < 10)
							throw new Exception("Mínimo de 10 caracteres para el Nombre Asegurado");
					}
					if(tomador.length() > 0)
					{
						if(tomador.length() < 10)
							throw new Exception("Mínimo de 10 caracteres para el Nombre Tomador");
					}
				}
				
				if(dataFim != null)
				{
					String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
					
					dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				}

				apolices = apoliceHome.localizarApolices(numeroInstrumento,secao, aseguradora, nomeAsegurado, plano, pagina,situacao,dataInicio,dataFim,rucCi, tomador, tipoInstrumento, "", "");
			}
			
			if(action.getBoolean("excel"))
			{
				LocalizarApolicesXLS xls = new LocalizarApolicesXLS(apolices, numeroInstrumento,secao, aseguradora, nomeAsegurado, plano, situacao,dataInicio,dataFim,rucCi, tomador, tipoInstrumento, "", "", textoUsuario);
				
				this.setResponseInputStream(xls.obterArquivo());
		        this.setResponseFileName("Pólizas.xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(xls.obterArquivo().available());
			}
			else
				this.setResponseView(new LocalizarApoliceView(apolices,numeroInstrumento, secao, aseguradora, nomeAsegurado,plano, origemMenu, pagina,situacao,dataInicio, dataFim, rucCi, tomador,tipoInstrumento, "", ""));
		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new LocalizarApoliceView(apolices,numeroInstrumento, secao, aseguradora, nomeAsegurado,plano, origemMenu, pagina,situacao,dataInicio, dataFim, rucCi, tomador,tipoInstrumento, "", ""));
		}
	}

	public void visualizarApolicesPorReaseguradora(Action action)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");

		Collection apolices = new ArrayList();

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("aseguradoraId"));

		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");

		String tipoContrato = action.getString("tipoContrato");

		Entidade reaseguradora = entidadeHome.obterEntidadePorId(action
				.getLong("reaseguradoraId"));

		apolices = apoliceHome.obterApolicesPorReaseguradora(aseguradora,
				reaseguradora, dataInicio, dataFim, tipoContrato);

		Action action2 = new Action("visualizarReaseguros");
		action2.add("listar", true);
		action2.add("aseguradoraId", aseguradora.obterId());
		action2.add("dataInicio", dataInicio);
		action2.add("dataFim", dataFim);

		mm.beginTransaction();
		try {

			this.setResponseView(new ApolicesPorReaseguradoraView(aseguradora,
					apolices, tipoContrato, dataInicio, dataFim, action2));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ApolicesPorReaseguradoraView(aseguradora,
					apolices, tipoContrato, dataInicio, dataFim, action2));
			mm.rollbackTransaction();
		}
	}

	public void visualizarApolicesPorCorretor(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");

		Collection apolices = new ArrayList();

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("aseguradoraId"));

		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");

		String tipoContrato = action.getString("tipoContrato");

		Entidade corretor = entidadeHome.obterEntidadePorId(action
				.getLong("corretorId"));

		apolices = apoliceHome.obterApolicesPorCorretora(aseguradora, corretor,
				dataInicio, dataFim, tipoContrato);

		mm.beginTransaction();
		try {

			this.setResponseView(new ApolicesPorCorretoraView(aseguradora,
					apolices, tipoContrato, dataInicio, dataFim));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ApolicesPorCorretoraView(aseguradora,
					apolices, tipoContrato, dataInicio, dataFim));
			mm.rollbackTransaction();
		}
	}

	public void visualizarApolicesPorAseguradora(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");

		Collection apolices = new ArrayList();

		Aseguradora aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));

		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");

		String tipoContrato = action.getString("tipoContrato");

		Entidade reaseguradora = entidadeHome.obterEntidadePorId(action.getLong("reaseguradoraId"));

		apolices = apoliceHome.obterApolicesPorReaseguradora(aseguradora,reaseguradora, dataInicio, dataFim, tipoContrato);

		Action action2 = new Action("visualizarReasegurosReaseguradora");
		action2.add("listar", true);
		action2.add("reaseguradoraId", reaseguradora.obterId());
		action2.add("dataInicio", dataInicio);
		action2.add("dataFim", dataFim);

		mm.beginTransaction();
		try 
		{

			this.setResponseView(new ApolicesPorReaseguradoraView(aseguradora,apolices, tipoContrato, dataInicio, dataFim, action2));
			mm.commitTransaction();

		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ApolicesPorReaseguradoraView(aseguradora,apolices, tipoContrato, dataInicio, dataFim, action2));
			mm.rollbackTransaction();
		}
	}

	public void visualizarApolicesPorCorretorAseguradora(Action action)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");

		Collection apolices = new ArrayList();

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("aseguradoraId"));

		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");

		String tipoContrato = action.getString("tipoContrato");

		Entidade corretor = entidadeHome.obterEntidadePorId(action
				.getLong("corretorId"));

		apolices = apoliceHome.obterApolicesPorCorretoraAseguradora(aseguradora, corretor,
				dataInicio, dataFim, tipoContrato);

		mm.beginTransaction();
		try {

			this.setResponseView(new ApolicesPorCorretoraAseguradoraView(
					corretor, apolices, tipoContrato, dataInicio, dataFim));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ApolicesPorCorretoraAseguradoraView(
					corretor, apolices, tipoContrato, dataInicio, dataFim));
			mm.rollbackTransaction();
		}
	}
	
	public void excluirDuplicidadeApolice(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		SinistroHome sinistroHome = (SinistroHome) mm.getHome("SinistroHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		
		mm.beginTransaction();
		
		try 
		{
			/*if(action.getBoolean("excluir"))
			{
				apoliceHome.excluirApolicesDuplicadas();
				this.setResponseView(new PaginaInicialView(usuario, usuario));
			}
			else
			{
				Collection apolices = apoliceHome.obterApolicesDuplicadas();
				this.setResponseView(new ApolicesDuplicadasView(usuario, apolices));
			}*/
			
			//sinistroHome.atualizarDatas();
			
			//aseguradoraHome.criarTabelaAseguradora();
			
			//sinistroHome.modificarAfetadoPorSinistro();
			//apoliceHome.atualizarSituacaoApoliceAnterior();
			//apoliceHome.atualizarNoVigenteApolicesVenciadas();
			
			//sinistroHome.manutSinistro();
			
			apoliceHome.manutAnulacao();
			
			this.setResponseView(new PaginaInicialView(usuario, usuario));
			
			this.setAlert("Concluido");
			
			mm.commitTransaction();
		
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuario, usuario));
			mm.rollbackTransaction();
		}
	}
	
	public void listarApolicesSuspeitas(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		mm.beginTransaction();
		try 
		{
			String rucCi = action.getString("rucCi");
			
			this.setResponseView(new ListaApolicesSuspeitasView(rucCi, apoliceHome.obterApolicesSuspeitas(rucCi),usuario));
			
			mm.commitTransaction();
		
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuario, usuario));
			mm.rollbackTransaction();
		}
	}
	
	public void visualizarApoliceSinistroAnual(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		boolean excel = action.getBoolean("excel");
		String opcao = action.getString("opcao");
		
		String situacaoSeguro = action.getString("situacaoSeguro");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new AnualidadeApolicesSinistrosView(false,situacaoSeguro,opcao));
			else
			{
				if(opcao.equals(""))
					throw new Exception("Opciones en blanco");
				
				boolean userAdmin = usuario.obterId() == 1;
				
				if(excel)
				{
					String textoUsuario = "Generado : " + usuario.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					AnualidadeApolicesSinistrosXLS xls = new AnualidadeApolicesSinistrosXLS(situacaoSeguro, apoliceHome,textoUsuario, opcao, userAdmin);
					
					String dataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
					
					this.setResponseInputStream(xls.obterArquivo());
			        this.setResponseFileName("Histórico Pólizas/Siniestros_" + situacaoSeguro+"_"+usuario.obterNome() + "_"+dataHora + ".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(xls.obterArquivo().available());
				}
				else
					this.setResponseView(new AnualidadeApolicesSinistrosView(true, situacaoSeguro,opcao));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AnualidadeApolicesSinistrosView(false,situacaoSeguro,opcao));
		}
	}
	
	public void visualizarApoliceSinistroAnualLista(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
		String opcao = action.getString("opcao");
		String situacaoSeguro = action.getString("situacaoSeguro");
		try 
		{
			String horaFim = " 23:59:59";
			String dataInicioStr = "01/01/";
			String dataFimStr = "31/12/";
			String dataInicioStr2 = "01/07/";
			String dataFimStr2 = "30/06/";
			
			int ano = Integer.valueOf(action.getString("ano"));
			String secao = action.getString("secao");
			String tipo = action.getString("tipo");
			
			Date dataInicio = null;
			Date dataFim = null;
			if(opcao.equals("ano"))
			{
				dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse(dataInicioStr+ano);
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr+ano + horaFim);
			}
			else
			{
				dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse(dataInicioStr2+ano);
				
				ano++;
				
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr2+ano + horaFim);
			}
			
			Collection<Apolice> apolices = new ArrayList<Apolice>();
			if(tipo.equals("apolice"))
				apolices = apoliceHome.obterApolicesHistoricoAnual(dataInicio, dataFim, situacaoSeguro, secao);
			else
				apolices = apoliceHome.obterSinistrosHistoricoAnual(dataInicio, dataFim, situacaoSeguro, secao);
			
			this.setResponseView(new ApolicesView(null, apolices, false));
			
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AnualidadeApolicesSinistrosView(false,situacaoSeguro,opcao));
		}
	}
	
	private Calendar calendarInicio = null;
	private Calendar calendarFim = null;
	
	private void AnoMaisUm() throws Exception
	{
		Date dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2006");
		Date dataFim = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2006");
		
		if(calendarInicio == null)
		{
			this.calendarInicio = Calendar.getInstance();
			this.calendarFim = Calendar.getInstance();
		
			calendarInicio.setTime(dataInicio);
			calendarFim.setTime(dataFim);
		}
		else
		{
			String ano = new SimpleDateFormat("yyyy").format(this.calendarInicio.getTime()); 
			
			if(ano.equals("2011"))
			{
				calendarInicio.setTime(dataInicio);
				calendarFim.setTime(dataFim);
			}
			else
			{
				calendarInicio.add(Calendar.YEAR, 1);
				calendarFim.add(Calendar.YEAR, 1);
			}
		}
	}
	
	public void apolicesPorSecao(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		mm.beginTransaction();
		try 
		{
			boolean admin = usuario.obterId() == 1;
			
			if(action.getBoolean("view"))
				this.setResponseView(new TotalApolicesPorSecaoView(action.getDate("data")));
			else
			{
				if(action.getDate("data") == null)
					throw new Exception("Fecha en Blanco");
				
				Collection aseguradoras = new ArrayList();
				
				if(action.getInt("aseguradora") == 0)
					aseguradoras = aseguradoraHome.obterAseguradoras();
				else
					aseguradoras.add(entidadeHome.obterEntidadePorId(action.getInt("aseguradora")));
				
				String textoUsuario = "Generado : " + usuario.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				
				TotalApolicesSecaoXLS xls = new TotalApolicesSecaoXLS(aseguradoras,action.getDate("data"),apoliceHome,aseguradoraHome, textoUsuario, admin);
				
				String mes = new SimpleDateFormat("MM").format(action.getDate("data"));
				String ano = new SimpleDateFormat("yyyy").format(action.getDate("data"));
				
				InputStream arquivo = xls.obterArquivo();
				
				String hora = "_" + usuario.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				
				this.setResponseInputStream(arquivo);
		        this.setResponseFileName("PólizasSiniestrosSecciónMes " + mes + "-" + ano + hora + ".xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(arquivo.available());
			}
			
			mm.commitTransaction();
		
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new TotalApolicesPorSecaoView(action.getDate("data")));
			mm.rollbackTransaction();
		}
	}
	
	public void apolicesPorSecaoAnual(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		boolean valores = false;
		boolean admin = usuario.obterId() == 1;
		String ramo = null;
		String secao = "";
		String modalidade = "";
		Aseguradora aseguradora = null;
		String valoresStr = action.getString("valores");
		if(!valoresStr.equals(""))
			valores = Boolean.parseBoolean(valoresStr);
		if(!action.getString("ramo").equals(""))
			ramo = action.getString("ramo");
		if(!action.getString("secao").equals(""))
			secao = action.getString("secao");
		if(!action.getString("modalidade").trim().equals(""))
		      modalidade = action.getString("modalidade").trim();
		try 
		{
			if(action.getInt("aseguradora") !=714 && action.getInt("aseguradora") !=0)
				aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getInt("aseguradora"));
			
			if(action.getBoolean("view"))
				this.setResponseView(new TotalApolicesPorSecaoAnualView(dataInicio, dataFim, valores, ramo, secao, aseguradora, modalidade));
			else
			{
			   int cont = 1;
			   
			   Calendar c = Calendar.getInstance();
			   c.setTime(dataInicio);
			   
		       while(c.getTime().compareTo(dataFim) < 0)
		       {
		    	   cont++;
		    	   
		    	   c.add(Calendar.MONTH, 1);
		       }
		       
		       if(cont > 13)
		    	   throw new Exception("Período sólo puede ser de un máximo de 12 meses");
		       
				if(dataInicio == null)
					throw new Exception("Fecha inicio en blanco");
				if(dataFim == null)
					throw new Exception("Fecha fin en blanco");
				
				Collection aseguradoras = new ArrayList();
				
				if(action.getInt("aseguradora") == 0 || action.getInt("aseguradora") == 714)
					aseguradoras = aseguradoraHome.obterAseguradoras();
				else
					aseguradoras.add(entidadeHome.obterEntidadePorId(action.getInt("aseguradora")));
				
				String textoUsuario = "Generado : " + usuario.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				
				TotalApolicesSecaoAnualXLS xls = new TotalApolicesSecaoAnualXLS(aseguradoras,dataInicio,dataFim, apoliceHome,aseguradoraHome, valores, textoUsuario, secao, modalidade, admin, ramo, action.getInt("aseguradora") == 714);
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(action.getDate("dataInicio"));
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(action.getDate("dataFim"));
				
				InputStream arquivo = xls.obterArquivo();
				
				String hora = "_" + usuario.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				
				this.setResponseInputStream(arquivo);
		        this.setResponseFileName("PólizasSiniestrosSecciónAnual " + dataInicioStr + "_hasta_" + dataFimStr + hora + ".xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(arquivo.available());
			}
			
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new TotalApolicesPorSecaoAnualView(dataInicio, dataFim, valores, ramo, secao, aseguradora, modalidade));
		}
	}
	
	public void demandaJudicial(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		Collection<String> aspectos = new ArrayList<String>();
		
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new DemandaJudicialView(aseguradora, dataInicio, dataFim, aspectos));
			else
			{
				if(action.getLong("aseguradora") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradora"));
				
				if(dataInicio == null)
					throw new Exception("Data inicio en blanco");
				if(dataFim == null)
					throw new Exception("Data fin en blanco");
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " 00:00:00";
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
				
				dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				
				aspectos = apoliceHome.obterDemandaJudicial(aseguradora, dataInicio, dataFim);
				
				this.setResponseView(new DemandaJudicialView(aseguradora, dataInicio, dataFim, aspectos));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new DemandaJudicialView(aseguradora, dataInicio, dataFim, aspectos));
		}
	}
	
	public void listaDemandaJudicial(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		Collection<AspectosLegais> aspectos = new ArrayList<AspectosLegais>();
		
		try 
		{
			if(dataInicio == null)
				throw new Exception("Data inicio en blanco");
			if(dataFim == null)
				throw new Exception("Data fin en blanco");
			
			ClassificacaoContas secao = null;
			
			if(action.getLong("origemId") > 0)
				aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("origemId"));
			if(action.getLong("secaoId") > 0)
				secao = (ClassificacaoContas) entidadeHome.obterEntidadePorId(action.getLong("secaoId"));
			
			String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " 00:00:00";
			String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
			
			dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
			dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
			
			aspectos = apoliceHome.obterApolicesDemandaJudicial(aseguradora, secao, dataInicio, dataFim);
			
			this.setResponseView(new ApolicesDemandaJudicialView(aseguradora, secao, dataInicio, dataFim, aspectos));
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new DemandaJudicialView(aseguradora, dataInicio, dataFim, new ArrayList<String>()));
		}
	}
	
	public void estatisticaDemandaJudicial(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EstatisticaDemandaJudicialView(aseguradora, dataInicio, false));
			else
			{
				if(dataInicio == null)
					throw new Exception("Data en blanco");
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				this.setResponseView(new EstatisticaDemandaJudicialView(aseguradora, dataInicio, true));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaDemandaJudicialView(aseguradora, dataInicio, false));
		}
	}
	
	public void estatisticaApolice(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EstatisticaApoliceView(aseguradora, dataInicio, false));
			else
			{
				if(dataInicio == null)
					throw new Exception("Data en blanco");
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					EstatisticaApoliceXLS xls = new EstatisticaApoliceXLS(aseguradora,dataInicio,home,entidadeHome,textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Estadística Datos del Instrumento_"+new SimpleDateFormat("MM/yyyy").format(dataInicio) + hora + ".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new EstatisticaApoliceView(aseguradora, dataInicio, true));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaApoliceView(aseguradora, dataInicio, false));
		}
	}
	
	public void estatisticaReservas(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EstatisticaReservasView(aseguradora, dataInicio, false));
			else
			{
				if(dataInicio == null)
					throw new Exception("Data en blanco");
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					EstatisticaReservasXLS xls = new EstatisticaReservasXLS(aseguradora,dataInicio,home,entidadeHome,textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Estadística Provisiones y Reservas_"+new SimpleDateFormat("MM/yyyy").format(dataInicio)+ hora + ".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new EstatisticaReservasView(aseguradora, dataInicio, true));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaReservasView(aseguradora, dataInicio, false));
		}
	}
	
	public void estatisticaReaseguros(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EstatisticaReasegurosView(aseguradora, dataInicio, false));
			else
			{
				if(dataInicio == null)
					throw new Exception("Data en blanco");
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					EstatisticaReasegurosXLS xls = new EstatisticaReasegurosXLS(aseguradora,dataInicio,home,entidadeHome,textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Estadística Reaseguros_"+new SimpleDateFormat("MM/yyyy").format(dataInicio)+ hora +".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new EstatisticaReasegurosView(aseguradora, dataInicio, true));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaReasegurosView(aseguradora, dataInicio, false));
		}
	}
	
	public void estatisticaAnulacaoReaseguros(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EstatisticaAnulacaoReasegurosView(aseguradora, dataInicio, false));
			else
			{
				if(dataInicio == null)
					throw new Exception("Data en blanco");
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					EstatisticaAnulacaoReasegurosXLS xls = new EstatisticaAnulacaoReasegurosXLS(aseguradora,dataInicio,home,entidadeHome,textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Estadística Anulación de Reaseguro_"+new SimpleDateFormat("MM/yyyy").format(dataInicio)+ hora + ".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new EstatisticaAnulacaoReasegurosView(aseguradora, dataInicio, true));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaAnulacaoReasegurosView(aseguradora, dataInicio, false));
		}
	}
	
	public void estatisticaMorosidade(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EstatisticaMorosidadeView(aseguradora, dataInicio, false));
			else
			{
				if(dataInicio == null)
					throw new Exception("Data en blanco");
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					EstatisticaMorosidadeXLS xls = new EstatisticaMorosidadeXLS(aseguradora,dataInicio,home,entidadeHome,textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Estadística Morosidad_"+new SimpleDateFormat("MM/yyyy").format(dataInicio)+ hora + ".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new EstatisticaMorosidadeView(aseguradora, dataInicio, true));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaMorosidadeView(aseguradora, dataInicio, false));
		}
	}
	
	public void estatisticaAsegurado(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EstatisticaAseguradoView(aseguradora, dataInicio, false));
			else
			{
				if(dataInicio == null)
					throw new Exception("Data en blanco");
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					EstatisticaAseguradoXLS xls = new EstatisticaAseguradoXLS(aseguradora,dataInicio,home,entidadeHome,textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Estadística Datos del Asegurado_"+new SimpleDateFormat("MM/yyyy").format(dataInicio)+ hora + ".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new EstatisticaAseguradoView(aseguradora, dataInicio, true));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaAseguradoView(aseguradora, dataInicio, false));
		}
	}
	
	public void estatisticaCoaseguros(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EstatisticaCoasegurosView(aseguradora, dataInicio, false));
			else
			{
				if(dataInicio == null)
					throw new Exception("Data en blanco");
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					EstatisticaCoasegurosXLS xls = new EstatisticaCoasegurosXLS(aseguradora,dataInicio,home,entidadeHome,textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Estadística Coaseguros_"+new SimpleDateFormat("MM/yyyy").format(dataInicio)+ hora +".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new EstatisticaCoasegurosView(aseguradora, dataInicio, true));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaCoasegurosView(aseguradora, dataInicio, false));
		}
	}
	
	public void estatisticaSinistros(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EstatisticaSinistrosView(aseguradora, dataInicio, false));
			else
			{
				if(dataInicio == null)
					throw new Exception("Data en blanco");
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					EstatisticaSinistrosXLS xls = new EstatisticaSinistrosXLS(aseguradora,dataInicio,home,entidadeHome,textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Estadística Siniestros_"+new SimpleDateFormat("MM/yyyy").format(dataInicio)+ hora +".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new EstatisticaSinistrosView(aseguradora, dataInicio, true));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaSinistrosView(aseguradora, dataInicio, false));
		}
	}
	
	public void estatisticaFinancimento(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EstatisticaFinanciamentoView(aseguradora, dataInicio, false));
			else
			{
				if(dataInicio == null)
					throw new Exception("Data en blanco");
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					EstatisticaFinancimantoXLS xls = new EstatisticaFinancimantoXLS(aseguradora,dataInicio,home,entidadeHome,textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Estadística Refinanciación_"+new SimpleDateFormat("MM/yyyy").format(dataInicio)+ hora +".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new EstatisticaFinanciamentoView(aseguradora, dataInicio, true));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaFinanciamentoView(aseguradora, dataInicio, false));
		}
	}
	
	public void estatisticaCI(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(getUser());
		
		try 
		{
			this.setResponseView(new EstatisticaCIView(usuario));
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaCIView(usuario));
		}
	}
	
	public void estatisticaDemandaJudicialExcel(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		try 
		{
			if(dataInicio == null)
				throw new Exception("Data en blanco");
			
			if(action.getLong("aseguradoraId") > 0)
				aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
			
			String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
			
			EstatisticaDemandaJudicialXLS xls = new EstatisticaDemandaJudicialXLS(aseguradora,dataInicio,home,entidadeHome,textoUsuario);
			
			InputStream arquivo = xls.obterArquivo();
			
			String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
			
			this.setResponseInputStream(arquivo);
	        this.setResponseFileName("Estadística Demanda Judicial_"+new SimpleDateFormat("MM/yyyy").format(dataInicio)+ hora +".xls");
	        this.setResponseContentType("application/vnd.ms-excel");
	        this.setResponseContentSize(arquivo.available());
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaDemandaJudicialView(aseguradora, dataInicio, false));
		}
	}
	
	public void estatisticaPagos(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EstatisticaPagosView(aseguradora, dataInicio, false));
			else
			{
				if(dataInicio == null)
					throw new Exception("Data en blanco");
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					EstatisticaPagosXLS xls = new EstatisticaPagosXLS(aseguradora,dataInicio,home,entidadeHome,textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Estadística Pagos a proveedores_"+new SimpleDateFormat("MM/yyyy").format(dataInicio)+ hora +".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new EstatisticaPagosView(aseguradora, dataInicio, true));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaPagosView(aseguradora, dataInicio, false));
		}
	}
	
	public void estatisticaAnulacao(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EstatisticaAnulacaoView(aseguradora, dataInicio, false));
			else
			{
				if(dataInicio == null)
					throw new Exception("Data en blanco");
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					EstatisticaAnulacaoXLS xls = new EstatisticaAnulacaoXLS(aseguradora,dataInicio,home,entidadeHome,textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Estadística Anulación de Instrumento_"+new SimpleDateFormat("MM/yyyy").format(dataInicio)+ hora +".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new EstatisticaAnulacaoView(aseguradora, dataInicio, true));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaAnulacaoView(aseguradora, dataInicio, false));
		}
	}
	
	public void estatisticaCobranca(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EstatisticaCobrancaView(aseguradora, dataInicio, false));
			else
			{
				if(dataInicio == null)
					throw new Exception("Data en blanco");
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					EstatisticaCobrancaXLS xls = new EstatisticaCobrancaXLS(aseguradora,dataInicio,home,entidadeHome,textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Estadística Cobranza_"+new SimpleDateFormat("MM/yyyy").format(dataInicio)+ hora +".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new EstatisticaCobrancaView(aseguradora, dataInicio, true));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaCobrancaView(aseguradora, dataInicio, false));
		}
	}
	
	public void estatisticaEndoso(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EstatisticaEndosoView(aseguradora, dataInicio, false));
			else
			{
				if(dataInicio == null)
					throw new Exception("Data en blanco");
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					EstatisticaEndosoXLS xls = new EstatisticaEndosoXLS(aseguradora,dataInicio,home,entidadeHome,textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Estadística Endoso o Suplemento_"+new SimpleDateFormat("MM/yyyy").format(dataInicio)+ hora +".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new EstatisticaEndosoView(aseguradora, dataInicio, true));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaEndosoView(aseguradora, dataInicio, false));
		}
	}
	
	public void estatisticaFinalizacao(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EstatisticaFinalizacaoView(aseguradora, dataInicio, false));
			else
			{
				if(dataInicio == null)
					throw new Exception("Data en blanco");
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					EstatisticaFinalizacaoXLS xls = new EstatisticaFinalizacaoXLS(aseguradora,dataInicio,home,entidadeHome,textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Estadística Finalización de Vigencia del Instrumento_"+new SimpleDateFormat("MM/yyyy").format(dataInicio)+ hora +".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new EstatisticaFinalizacaoView(aseguradora, dataInicio, true));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaFinalizacaoView(aseguradora, dataInicio, false));
		}
	}
	
	public void estatisticaGastos(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EstatisticaGastosView(aseguradora, dataInicio, false));
			else
			{
				if(dataInicio == null)
					throw new Exception("Data en blanco");
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					EstatisticaGastosXLS xls = new EstatisticaGastosXLS(aseguradora,dataInicio,home,entidadeHome,textoUsuario);
					
					InputStream arquivo = xls.obterArquivo();
					
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Estadística Gastos_"+new SimpleDateFormat("MM/yyyy").format(dataInicio)+ hora +".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new EstatisticaGastosView(aseguradora, dataInicio, true));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaGastosView(aseguradora, dataInicio, false));
		}
	}
	
	public void listaDemandaJudicialExcel(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		Collection<AspectosLegais> aspectos = new ArrayList<AspectosLegais>();
		
		try 
		{
			if(dataInicio == null)
				throw new Exception("Data inicio en blanco");
			if(dataFim == null)
				throw new Exception("Data fin en blanco");
			
			ClassificacaoContas secao = null;
			
			if(action.getLong("origemId") > 0)
				aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("origemId"));
			if(action.getLong("secaoId") > 0)
				secao = (ClassificacaoContas) entidadeHome.obterEntidadePorId(action.getLong("secaoId"));
			
			String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " 00:00:00";
			String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
			
			dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
			dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
			
			aspectos = apoliceHome.obterApolicesDemandaJudicial(aseguradora, secao, dataInicio, dataFim);
			
			ApolicesDemandaJudicialXLS xls = new ApolicesDemandaJudicialXLS(aseguradora, secao, dataInicio, dataFim, aspectos);
			
			String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
			
			String nome = "Demanda Judicial " + aseguradora.obterNome();
			nome+="_" + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + "_hasta_" + new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + hora;
			nome+=".xls";
			
			InputStream arquivo = xls.obterArquivo();
			
			this.setResponseInputStream(arquivo);
	        this.setResponseFileName(nome);
	        this.setResponseContentType("application/vnd.ms-excel");
	        this.setResponseContentSize(arquivo.available());
			
			
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new DemandaJudicialView(aseguradora, dataInicio, dataFim, new ArrayList<String>()));
		}
	}
	
	public void qtdeApoliceReaseguros(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
		Aseguradora aseguradora = null;
		String situacao = action.getString("situacao");
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		int qtde = action.getInt("qtde");
		double valor = action.getDouble("valor");
		double valorDolar = action.getDouble("valorDolar");
		double valorMenor = action.getDouble("valorMenor");
		double valorMenorDolar = action.getDouble("valorMenorDolar");
		Collection<String> apolices = new ArrayList<String>();
		try
		{
			if (action.getBoolean("view")) 
			{
				this.setResponseView(new QtdeApolicesReasegurosView(aseguradora, dataInicio, dataFim, qtde, situacao, valor, valorMenor,apolices, valorDolar, valorMenorDolar));
			}
			else
			{
				if(action.getLong("aseguradora") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradora"));
				
				if(dataInicio == null)
					throw new Exception("Fecha inicio en blanco");
				if(dataFim == null)
					throw new Exception("Fecha fim en blanco");
				if(valor > 0 && valorMenor > 0)
					throw new Exception("Elija un solo valor de capital");
				
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				
				apolices = apoliceHome.obterApolicesReaseguro(aseguradora, dataInicio, dataFim, qtde, situacao, valor, valorMenor,valorDolar,valorMenorDolar);
				
				String com = action.getString("com");
				if(com.equals(""))
					this.setResponseView(new QtdeApolicesReasegurosView(aseguradora, dataInicio, dataFim, qtde, situacao, valor, valorMenor,apolices, valorDolar, valorMenorDolar));
				else
				{
					Collection<Apolice> apolices2 = new ArrayList<Apolice>();
					apolices2 = apoliceHome.obterApolicesReaseguro2(aseguradora, dataInicio, dataFim, qtde, situacao, valor, valorMenor, com.equals("sim"), valorDolar, valorMenorDolar);
					
					String titulo = "Pólizas con Reaseguro";
					if(!com.equals("sim"))
						titulo = "Pólizas sin Reaseguro";
					
					this.setResponseView(new ApolicesReaseguroView(apolices2, titulo));
				}
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado : " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					QtdeApolicesReasegurosXLS xls = new QtdeApolicesReasegurosXLS(aseguradora, dataInicio, dataFim, situacao, valor, apolices,textoUsuario, valorMenor,valorDolar, valorMenorDolar);
					
					InputStream arquivo = xls.obterArquivo();
					this.setResponseInputStream(arquivo);
					if(aseguradora!=null)
						this.setResponseFileName("Cantidad de Pólizas_Reaseguross_"+aseguradora.obterNome()+".xls");
					else
						this.setResponseFileName("Cantidad de Pólizas_Reaseguros.xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else if(action.getBoolean("pdf"))
				{
					Collection<Apolice> apolices2 = new ArrayList<Apolice>();
					apolices2 = apoliceHome.obterApolicesReaseguro2(aseguradora, dataInicio, dataFim, qtde, situacao, valor, valorMenor, com.equals("sim"), valorDolar, valorMenorDolar);
					
					String textoUsuario = "Generado : " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					CoberturaReaseguroPDF pdf = new CoberturaReaseguroPDF(aseguradora, dataInicio, dataFim, situacao, apolices2, "", textoUsuario, "Cantidad de Pólizas/Reaseguros");
					
					InputStream arquivo = pdf.obterArquivo();
					this.setResponseInputStream(arquivo);
					if(aseguradora!=null)
						this.setResponseFileName("Cantidad de Pólizas_Reaseguross_"+aseguradora.obterNome()+".pdf");
					else
						this.setResponseFileName("Cantidad de Pólizas_Reaseguross.pdf");
			        this.setResponseContentType("application/pdf");
			        this.setResponseContentSize(arquivo.available());
				}
			}
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new QtdeApolicesReasegurosView(aseguradora, dataInicio, dataFim, qtde, situacao, valor, valorMenor,apolices, valorDolar, valorMenorDolar));
		}
	}
	
	public void apolicesModalidade(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		String ramo = null;
		String secao = "";
		String modalidade = "";
		Aseguradora aseguradora = null;
		if(!action.getString("ramo").equals(""))
			ramo = action.getString("ramo");
		if(!action.getString("secao").equals(""))
			secao = action.getString("secao");
		if(!action.getString("modalidade").trim().equals(""))
		      modalidade = action.getString("modalidade").trim();
		
		boolean admin = usuario.obterId() == 1;
		
		Collection<Aseguradora> aseguradoras = new ArrayList<Aseguradora>();
		try 
		{
			if(action.getInt("aseguradora") !=714 && action.getInt("aseguradora") !=0)
				aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getInt("aseguradora"));
			
			if(action.getBoolean("view"))
				this.setResponseView(new ApolicesModalidadeView(dataInicio, dataFim, ramo, secao, aseguradora, modalidade, aseguradoras, apoliceHome, aseguradoraHome, false, admin));
			else
			{
			   int cont = 1;
			   
			   Calendar c = Calendar.getInstance();
			   c.setTime(dataInicio);
			   
		       while(c.getTime().compareTo(dataFim) < 0)
		       {
		    	   cont++;
		    	   
		    	   c.add(Calendar.MONTH, 1);
		       }
		       
		       if(cont > 13)
		    	   throw new Exception("Período sólo puede ser de un máximo de 12 meses");
		       
				if(dataInicio == null)
					throw new Exception("Fecha inicio en blanco");
				if(dataFim == null)
					throw new Exception("Fecha fin en blanco");
				
				if(action.getInt("aseguradora") == 0)
					aseguradoras = aseguradoraHome.obterAseguradoras();
				else if(action.getInt("aseguradora") !=714)
					aseguradoras.add((Aseguradora) entidadeHome.obterEntidadePorId(action.getInt("aseguradora")));
				
				String textoUsuario = "Generado : " + usuario.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				
				if(action.getBoolean("excel"))
				{
					ApolicesModalidadeXLS xls = new ApolicesModalidadeXLS(aseguradoras,dataInicio,dataFim, apoliceHome, aseguradoraHome, textoUsuario, secao, modalidade, usuario.obterId() == 1, ramo);
					
					String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(action.getDate("dataInicio"));
					String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(action.getDate("dataFim"));
					
					InputStream arquivo = xls.obterArquivo();
					
					String hora = "_" + usuario.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("PólizasPorModalidad " + dataInicioStr + "_hasta_" + dataFimStr + hora + ".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new ApolicesModalidadeView(dataInicio, dataFim, ramo, secao, aseguradora, modalidade, aseguradoras, apoliceHome, aseguradoraHome, true, admin));
			}
			
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ApolicesModalidadeView(dataInicio, dataFim, ramo, secao, aseguradora, modalidade, aseguradoras, apoliceHome, aseguradoraHome, false, admin));
		}
	}
	
	public void apolicesModalidadeLista(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Date dataInicio = new Date(action.getLong("dataInicio2"));
		Date dataFim = new Date(action.getLong("dataFim2"));
		String secao = action.getString("secao2");
		String modalidade = action.getString("modalidade2");
		String ramo = action.getString("ramo2");
		Aseguradora aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
		try 
		{
			Collection<Apolice> apolices = aseguradora.obterApolicesPorModalidade(dataInicio, dataFim, secao, modalidade);
			this.setResponseView(new ApolicesView(aseguradora, apolices, false));
			
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ApolicesModalidadeView(dataInicio, dataFim, ramo, secao, aseguradora, modalidade, null, null, null, false, false));
		}
	}
	
	public void apolicesPorTipoPessoa(Action action)	throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		boolean excel = action.getBoolean("excel");
		Aseguradora aseguradora = null;
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		String ramo = null;
		String secao = "";
		String modalidade = "";
		if(!action.getString("ramo").equals(""))
			ramo = action.getString("ramo");
		if(!action.getString("secao").equals(""))
			secao = action.getString("secao");
		if(!action.getString("modalidade").trim().equals(""))
		     modalidade = action.getString("modalidade").trim();
		long aseguradoraId = action.getLong("aseguradora");
		if(aseguradoraId > 0)
			aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(aseguradoraId);
		
		Collection<Aseguradora> aseguradoras = new ArrayList<Aseguradora>();
		try 
		{
			if(!action.getBoolean("view"))
			{
				if(aseguradora!=null)
					aseguradoras.add(aseguradora);
				else
					aseguradoras = aseguradoraHome.obterAseguradoras();
				
				if(dataInicio == null)
					throw new Exception("Fecha inicio en Blanco");
				if(dataFim == null)
					throw new Exception("Fecha Fin en Blanco");
				
				if(excel)
				{
					String textoUsuario = "Generado : " + usuario.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					ApolicesPorPessoaViewXLS xls = new ApolicesPorPessoaViewXLS(aseguradora, dataInicio, dataFim, aseguradoras, textoUsuario, ramo, secao, modalidade);
					InputStream arquivo = xls.obterArquivo();
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("PólizasPorTipoPersona.xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new ApolicesPorPessoaView(aseguradora, dataInicio, dataFim, aseguradoras, true, ramo, secao, modalidade));
			}
			else
				this.setResponseView(new ApolicesPorPessoaView(aseguradora, dataInicio, dataFim, aseguradoras, action.getBoolean("view"), ramo, secao, modalidade));
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ApolicesPorPessoaView(aseguradora, dataInicio, dataFim, aseguradoras, false, ramo, secao, modalidade));
		}
	}
}