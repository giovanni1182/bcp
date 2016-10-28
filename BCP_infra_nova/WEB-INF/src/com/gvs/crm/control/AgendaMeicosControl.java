package com.gvs.crm.control;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.gvs.crm.model.AgendaMeicos;
import com.gvs.crm.model.AgendaMeicosHome;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Inscricao;
import com.gvs.crm.model.MeicosAseguradora;
import com.gvs.crm.model.MeicosCalculo;
import com.gvs.crm.model.Parametro;
import com.gvs.crm.model.RatioHome;
import com.gvs.crm.model.RatioPermanente;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.ListaAgendaMeicosView;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class AgendaMeicosControl extends Control 
{
	private FileWriter fileContas;
	
	private void gerarArquivosContas(Map contasProArquivo, Aseguradora aseguradora, String mesAnoAnterior, String mesDoisAnosAnterior, String mesAnoCalculo) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");
		//DecimalFormat formataValor = new DecimalFormat("###000");
		
		fileContas.write("Aseguradora: " +aseguradora.obterNome().toUpperCase() +  "\r\n");
		fileContas.write("Mes Atual: " +mesAnoCalculo +  "\r\n");
		fileContas.write("Ano Anterior: " +mesAnoAnterior +  "\r\n");
		fileContas.write("2 Anos Anterior: " +mesDoisAnosAnterior +  "\r\n");
		String string, apelidoConta, qtdeAnos;
		StringTokenizer st;
		Entidade e;
		ClassificacaoContas cContasArquivo;
		Conta contaArquivo;
		
		for(Iterator k = contasProArquivo.values().iterator(); k.hasNext() ; )
		{
			string = (String) k.next();
			
			st = new StringTokenizer(string,";");
			
			if(st.countTokens() == 2)
			{
				apelidoConta = st.nextToken();
				qtdeAnos = st.nextToken();
				
				e = entidadeHome.obterEntidadePorApelido(apelidoConta);
				if(e instanceof ClassificacaoContas)
				{
					cContasArquivo = (ClassificacaoContas) e;
					if(qtdeAnos.equals("1"))
					{
						fileContas.write(cContasArquivo.obterApelido() + " Ano Anterior --> ;");
						fileContas.write(formataValor.format(cContasArquivo.obterTotalizacaoExistente(aseguradora, mesAnoAnterior)));
						fileContas.write("\r\n");
					}
					else
					{
						fileContas.write(cContasArquivo.obterApelido() + " 2 Anos Anterior -->;");
						fileContas.write(formataValor.format(cContasArquivo.obterTotalizacaoExistente(aseguradora, mesDoisAnosAnterior)));
						fileContas.write("\r\n");
					}
				}
				else if(e instanceof Conta)
				{
					contaArquivo = (Conta) e;
					if(qtdeAnos.equals("1"))
					{
						fileContas.write(contaArquivo.obterApelido() + " Ano Anterior -->; ");
						fileContas.write(formataValor.format(contaArquivo.obterTotalizacaoExistente(aseguradora, mesAnoAnterior)));
						fileContas.write("\r\n");
					}
					else
					{ 
						fileContas.write(contaArquivo.obterApelido() + " 2 Anos Anterior -->; ");
						fileContas.write(formataValor.format(contaArquivo.obterTotalizacaoExistente(aseguradora, mesDoisAnosAnterior)));
						fileContas.write("\r\n");
					}
					
				}
			}
			else
			{
				apelidoConta = st.nextToken();
				
				e = entidadeHome.obterEntidadePorApelido(apelidoConta);
				if(e instanceof ClassificacaoContas)
				{
					cContasArquivo = (ClassificacaoContas) e;
					fileContas.write(cContasArquivo.obterApelido() + " Ano Atual -->; ");
					fileContas.write(formataValor.format(cContasArquivo.obterTotalizacaoExistente(aseguradora, mesAnoCalculo)));
					fileContas.write("\r\n");
				}
				else if(e instanceof Conta)
				{
					contaArquivo = (Conta) e;
					fileContas.write(contaArquivo.obterApelido() + " Ano Atual -->; ");
					fileContas.write(formataValor.format(contaArquivo.obterTotalizacaoExistente(aseguradora, mesAnoCalculo)));
					fileContas.write("\r\n");
				}
			}
		}
		
		//fileContas.write("-------------------------------------------------------------------------\r\n");
	}
	
	private void gerarContasAcumuladas(Map<String,String> contasAcumuladas) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");
		//DecimalFormat formataValor = new DecimalFormat("###000");
		Entidade e;
		String linhaSuja, apelidoConta, qtdeAnos, valorStr;
		String[] linha;
		double valor;
		
		for(Iterator<String> k = contasAcumuladas.values().iterator(); k.hasNext() ; )
		{
			linhaSuja = k.next();
			
			linha = linhaSuja.split(";");
			
			apelidoConta = linha[0];
			qtdeAnos = linha[1];
			valorStr = linha[2];
			valor = Double.valueOf(valorStr);
			
			e = entidadeHome.obterEntidadePorApelido(apelidoConta);
			
			fileContas.write(e.obterApelido() + " ");
			if(qtdeAnos.equals("3"))
				fileContas.write("3 Anos Acumulados --> " + formataValor.format(valor));
			else if(qtdeAnos.equals("1"))
				fileContas.write("1 Ano Acumulado --> " + formataValor.format(valor));
			
			fileContas.write("\r\n");
		}
		
		fileContas.write("-------------------------------------------------------------------------\r\n");
	}
	
	public void incluirAgendaMeicos(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		//EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());

		AgendaMeicos agenda = (AgendaMeicos) mm.getEntity("AgendaMeicos");

		mm.beginTransaction();
		try {

			if (action.getDate("data") == null)
				throw new Exception("Preencha a Fecha de Calculo");

			if (action.getString("titulo") == null)
				throw new Exception("Preencha Titulo");

			Entidade bcp = entidadeHome.obterEntidadePorApelido("bcp");

			agenda.atribuirResponsavel(usuarioAtual);
			agenda.atribuirOrigem(bcp);
			agenda.atribuirDestino(bcp);
			agenda.atribuirTitulo(action.getString("titulo"));
			agenda.atribuirDescricao(action.getString("descricao"));
			agenda.incluir();

			agenda.atualizarIPC(action.getDouble("ipc1Ano"));
			agenda.atualizarIPC3Anos(action.getDouble("ipc3Anos"));

			agenda.atualizarDataPrevistaInicio(action.getDate("data"));
			agenda.atualizarDataPrevistaConclusao(action.getDate("data"));

			this.setResponseView(new EventoView(agenda));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(agenda));
			mm.rollbackTransaction();
		}
	}

	public void atualizarAgendaMeicos(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		AgendaMeicos agenda = (AgendaMeicos) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {

			if (action.getDate("data") == null)
				throw new Exception("Preencha a Fecha de Calculo");

			if (action.getString("titulo") == null)
				throw new Exception("Preencha Titulo");

			agenda.atualizarTitulo(action.getString("titulo"));
			agenda.atualizarDescricao(action.getString("descricao"));
			agenda.atualizarDataPrevistaInicio(action.getDate("data"));
			agenda.atualizarDataPrevistaConclusao(action.getDate("data"));

			agenda.atualizarIPC(action.getDouble("ipc1Ano"));
			agenda.atualizarIPC3Anos(action.getDouble("ipc3Anos"));

			this.setResponseView(new EventoView(agenda));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(agenda));
			mm.rollbackTransaction();
		}
	}

	public void gerarMeicos(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm
				.getHome("AseguradoraHome");

		AgendaMeicos agenda = (AgendaMeicos) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {

			for (Iterator i = agenda.obterInferiores().iterator(); i.hasNext();) {
				Evento e = (Evento) i.next();

				if (e instanceof MeicosAseguradora)
					e.excluir();
			}

			Parametro parametro = (Parametro) entidadeHome
					.obterEntidadePorApelido("parametros");

			Entidade bcp = entidadeHome.obterEntidadePorApelido("bcp");

			for (Iterator i = aseguradoraHome.obterAseguradoras().iterator(); i.hasNext();) 
			{
				Aseguradora aseguradora = (Aseguradora) i.next();

				MeicosAseguradora meicos1 = (MeicosAseguradora) mm.getEntity("MeicosAseguradora");

				MeicosAseguradora meicos2 = (MeicosAseguradora) mm.getEntity("MeicosAseguradora");

				MeicosAseguradora meicos3 = (MeicosAseguradora) mm.getEntity("MeicosAseguradora");

				MeicosAseguradora meicos4 = (MeicosAseguradora) mm.getEntity("MeicosAseguradora");

				meicos1.atribuirResponsavel(usuarioAtual);
				meicos1.atribuirOrigem(aseguradora);
				meicos1.atribuirDestino(bcp);
				meicos1.atribuirTitulo(agenda.obterTitulo());
				meicos1.atribuirTipo("Auditoria Externa");
				meicos1.atribuirSuperior(agenda);
				meicos1.incluir();

				meicos1.atualizarDataPrevistaInicio(agenda.obterDataPrevistaInicio());
				meicos1.atualizarDataPrevistaConclusao(agenda.obterDataPrevistaConclusao());

				for (Iterator j = parametro.obterIndicadores("Auditoria Externa").values().iterator(); j.hasNext();) 
				{
					Parametro.Indicador indicador = (Parametro.Indicador) j.next();

					String tipo = indicador.obterTipo();

					String descricao = indicador.obterDescricao();
					int peso = indicador.obterPeso();

					meicos1.adicionarIndicador(descricao, peso, indicador.eExcludente());
				}

				meicos2.atribuirResponsavel(usuarioAtual);
				meicos2.atribuirOrigem(aseguradora);
				meicos2.atribuirDestino(bcp);
				meicos2.atribuirTitulo(agenda.obterTitulo());
				meicos2.atribuirTipo("Inspeción");
				meicos2.atribuirSuperior(agenda);
				meicos2.incluir();

				meicos2.atualizarDataPrevistaInicio(agenda
						.obterDataPrevistaInicio());
				meicos2.atualizarDataPrevistaConclusao(agenda
						.obterDataPrevistaConclusao());

				for (Iterator j = parametro.obterIndicadores("Inspeción").values().iterator(); j.hasNext();)
				{
					Parametro.Indicador indicador = (Parametro.Indicador) j.next();

					String tipo = indicador.obterTipo();

					String descricao = indicador.obterDescricao();
					int peso = indicador.obterPeso();

					meicos2.adicionarIndicador(descricao, peso, indicador.eExcludente());
				}

				meicos3.atribuirResponsavel(usuarioAtual);
				meicos3.atribuirOrigem(aseguradora);
				meicos3.atribuirDestino(bcp);
				meicos3.atribuirTitulo(agenda.obterTitulo());
				meicos3.atribuirTipo("Otros Indicadores");
				meicos3.atribuirSuperior(agenda);
				meicos3.incluir();

				meicos3.atualizarDataPrevistaInicio(agenda
						.obterDataPrevistaInicio());
				meicos3.atualizarDataPrevistaConclusao(agenda
						.obterDataPrevistaConclusao());

				for (Iterator j = parametro.obterIndicadores("Otros Indicadores").values().iterator(); j.hasNext();)
				{
					Parametro.Indicador indicador = (Parametro.Indicador) j.next();

					String tipo = indicador.obterTipo();

					String descricao = indicador.obterDescricao();
					int peso = indicador.obterPeso();

					meicos3.adicionarIndicador(descricao, peso, indicador.eExcludente());
				}

				meicos4.atribuirResponsavel(usuarioAtual);
				meicos4.atribuirOrigem(aseguradora);
				meicos4.atribuirDestino(bcp);
				meicos4.atribuirTitulo(agenda.obterTitulo());
				meicos4.atribuirTipo("Controle de Documentos");
				meicos4.atribuirSuperior(agenda);
				meicos4.incluir();

				meicos4.atualizarDataPrevistaInicio(agenda
						.obterDataPrevistaInicio());
				meicos4.atualizarDataPrevistaConclusao(agenda
						.obterDataPrevistaConclusao());

				for (Iterator j = parametro.obterControleDocumentos().values().iterator(); j.hasNext();)
				{
					Parametro.ControleDocumento documento = (Parametro.ControleDocumento) j.next();

					String descricao = documento.obterDescricao();

					meicos4.adicionarDocumento(descricao, documento.obterDataLimite());
				}
			}
			
			this.setAlert("Meicos Generado para las Aseguradoras");

			mm.commitTransaction();

			this.setResponseView(new EventoView(agenda));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(agenda));
			mm.rollbackTransaction();
		}
	}

	public void visualizarAgendasMeicos(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		AgendaMeicosHome agendaMeicosHome = (AgendaMeicosHome) mm
				.getHome("AgendaMeicosHome");

		mm.beginTransaction();
		try {
			Entidade entidade = entidadeHome.obterEntidadePorId(action
					.getLong("id"));

			this.setResponseView(new ListaAgendaMeicosView(entidade,
					agendaMeicosHome.obterAgendas()));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuarioAtual,
					usuarioAtual));
			mm.rollbackTransaction();
		}
	}

	public void calcularMeicos(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		RatioHome ratioHome = (RatioHome) mm.getHome("RatioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");
		AgendaMeicos agenda = (AgendaMeicos) eventoHome.obterEventoPorId(action.getLong("id"));
		

		//		Data do calculo do Meicos e Data do ano anteior
		
		String mesAnoCalculo = new SimpleDateFormat("MMyyyy").format(agenda.obterDataPrevistaInicio());

		String mes = new SimpleDateFormat("MM").format(agenda.obterDataPrevistaInicio());
		
		String ano = new SimpleDateFormat("yyyy").format(agenda.obterDataPrevistaInicio());

		int anoAnterior = Integer.parseInt(new SimpleDateFormat("yyyy").format(agenda.obterDataPrevistaInicio())) - 1;

		int doisAnosAnterior = Integer.parseInt(new SimpleDateFormat("yyyy").format(agenda.obterDataPrevistaInicio())) - 2;
		
		String mesAnoAnterior = mes + anoAnterior;

		String mesDoisAnosAnterior = mes + doisAnosAnterior;
		
		Date dataAgenda = agenda.obterDataPrevistaInicio();

		double IPC = agenda.obterIPC();

		FileWriter file = new FileWriter("C:/Anuario/Margem.txt");

		FileWriter fileRatio = new FileWriter("C:/Anuario/Ratios.txt");
		fileContas = new FileWriter("C:/Anuario/Contas.txt");
		
		String ponderacaoStr = "";

		//FileWriter file = new FileWriter("\\192.168.0.2\rotor\teste.txt");

		mm.beginTransaction();
		try {

			Collection calculos = new ArrayList();

			Map valores = new TreeMap();

			// Excluir o que ja foi calculado, isso server pra nao acumular
			// eventos de calculo
			// que afetariam a calculo do tabuleiro
			agenda.excluirCalculoMeicos();
			

			for (Iterator i = agenda.obterEventoMeicos().iterator(); i.hasNext();) 
			{
				MeicosAseguradora meicos = (MeicosAseguradora) i.next();

				//if(meicos.obterOrigem().obterId() == 5207 || meicos.obterOrigem().obterId() == 5225)
				if(meicos.obterOrigem().obterId() == 5226 || meicos.obterOrigem().obterId() == 5218)
				{
					Aseguradora aseguradora = (Aseguradora) meicos.obterOrigem();
					
					String ramo = "";

					for (Iterator w = aseguradora.obterInscricaoAtiva().obterRamos().iterator(); w.hasNext();) 
					{
						Inscricao.Ramo ramo2 = (Inscricao.Ramo) w.next();

						ramo = ramo2.obterRamo();
					}

					System.out.println("Aseguradora: "+ aseguradora.obterNome());

					System.out.println("Tipo de Meicos: " + meicos.obterTipo());

					if (meicos.obterTipo().equals("Controle de Documentos")) 
					{
						double totalMeicos = meicos.obterDocumentos().size();

						double totalSim = 0;

						double cir = 0;

						double valorIndicador = 0;

						for (Iterator j = meicos.obterDocumentos().values().iterator(); j.hasNext();) 
						{
							MeicosAseguradora.ControleDocumento documento = (MeicosAseguradora.ControleDocumento) j.next();

							if (documento.obterDataEntrega() != null) 
							{
								if (documento.obterDataEntrega().before(documento.obterDataLimite()) || documento.obterDataEntrega().equals(documento.obterDataLimite()))
									totalSim++;
							}
						}

						System.out.println("totalSim: " + totalSim);
						System.out.println("totalMeicos: " + totalMeicos);

						cir = totalSim / totalMeicos;

						System.out.println("Cir: " + cir);

						double x = 0;
						double y = 0;

						double totalA = 0;
						double totalB = 0;

						double PESO_MAXIMO = 15;

						for (Iterator j = aseguradora.obterMeicos("Otros Indicadores").iterator(); j.hasNext();) 
						{
							MeicosAseguradora meicos2 = (MeicosAseguradora) j.next();

							for (Iterator u = meicos2.obterIndicadores().values().iterator(); u.hasNext();) 
							{
								MeicosAseguradora.Indicador indicador = (MeicosAseguradora.Indicador) u.next();

								totalB += indicador.obterPeso();

								if (indicador.estaMarcado()) 
								{
									if (indicador.obterSequencial() == 1|| indicador.obterSequencial() == 2) 
									{
										x = indicador.obterPeso() * cir;
										totalA += x;
									}
									else
										totalA += indicador.obterPeso();
								}
							}
						}

						System.out.println("TotalA: " + totalA);

						System.out.println("TotalB: " + totalB);

						valorIndicador = (totalA * PESO_MAXIMO) / totalB;

						System.out.println("Valor Otros Indicadores: "
								+ valorIndicador);

						MeicosCalculo calculo = (MeicosCalculo) mm.getEntity("MeicosCalculo");

						calculo.atribuirOrigem(meicos.obterOrigem());
						calculo.atribuirDestino(meicos.obterDestino());
						calculo.atribuirResponsavel(usuarioAtual);
						calculo.atribuirSuperior(meicos);
						calculo.atribuirTitulo("Calculo de Meicos");
						calculo.atribuirTipo("Otros Indicadores");
						calculo.atribuirValor(valorIndicador);
						calculo.atribuirDataPrevistaInicio(new Date());
						calculo.atribuirDataPrevistaConclusao(new Date());

						calculos.add(calculo);

					} 
					else if (meicos.obterTipo().equals("Auditoria Externa")) 
					{
						int totalSim = 0;

						double valorIndicador = 0;

						double max1 = 0;
						double max2 = 0;

						double totalA1 = 0;
						double totalB1 = 0;

						double totalA2 = 0;
						double totalB2 = 0;

						double PESO_MAXIMO = 25;

						for (Iterator u = meicos.obterIndicadores().values().iterator(); u.hasNext();) 
						{
							MeicosAseguradora.Indicador indicador = (MeicosAseguradora.Indicador) u.next();

							if (indicador.obterSequencial() == 1)
								max1 = indicador.obterPeso();
							else if (indicador.obterSequencial() == 2) 
							{
								if (indicador.obterPeso() > max1)
									max1 = indicador.obterPeso();
							}
							else if(indicador.obterSequencial() > 2)
								totalB1 += indicador.obterPeso();

							if (indicador.estaMarcado())
								totalA1 += indicador.obterPeso();
						}

						totalB1 += max1;

						for (Iterator j = aseguradora.obterMeicos("Inspeción").iterator(); j.hasNext();) 
						{
							MeicosAseguradora meicos2 = (MeicosAseguradora) j.next();

							for (Iterator u = meicos2.obterIndicadores().values().iterator(); u.hasNext();) 
							{
								MeicosAseguradora.Indicador indicador = (MeicosAseguradora.Indicador) u.next();

								if (indicador.obterSequencial() == 1)
									max2 = indicador.obterPeso();
								else if (indicador.obterSequencial() == 2) 
								{
									if (indicador.obterPeso() > max2)
										max2 = indicador.obterPeso();
								}
								else if(indicador.obterSequencial() > 2)
									totalB2 += indicador.obterPeso();

								if (indicador.estaMarcado())
									totalA2 += indicador.obterPeso();
							}
						}

						totalB2 += max2;

						if (totalA2 > 0)
							valorIndicador = (totalA2 * PESO_MAXIMO) / totalB2;
						else
							valorIndicador = (totalA1 * PESO_MAXIMO) / totalB1;

						System.out.println("totalB1:" + totalB1);
						System.out.println("totalB2:" + totalB2);
						System.out.println("totalA1:" + totalA1);
						System.out.println("totalA2:" + totalA2);

						System.out.println("Valor In-Situ: " + valorIndicador);

						MeicosCalculo calculo2 = (MeicosCalculo) mm.getEntity("MeicosCalculo");

						calculo2.atribuirOrigem(meicos.obterOrigem());
						calculo2.atribuirDestino(meicos.obterDestino());
						calculo2.atribuirResponsavel(usuarioAtual);
						calculo2.atribuirSuperior(meicos);
						calculo2.atribuirTitulo("Calculo de Meicos");
						calculo2.atribuirTipo("In-Situ");
						calculo2.atribuirValor(valorIndicador);
						calculo2.atribuirDataPrevistaInicio(new Date());
						calculo2.atribuirDataPrevistaConclusao(new Date());

						calculos.add(calculo2);

					} 
					else if (meicos.obterTipo().equals("Inspeción")) 
					{
						ponderacaoStr = "";
						
						// CONTAS PRA FAZER O CÁLCULO do RATIOS 1
						ClassificacaoContas cContas1 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0212000000");
						ClassificacaoContas cContas2 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0401000000");
						ClassificacaoContas cContas3 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0402000000");
						ClassificacaoContas cContas4 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0403000000");
						
						Map contasProArquivo = new TreeMap();

						//Calculo do Indicador RATIO1
						double cContas1ValorAtual = cContas1.obterTotalizacaoExistente(aseguradora,	mesAnoCalculo);
						contasProArquivo.put("0212000000;", "0212000000;");
						double cContas1ValorAnterior = cContas1.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0212000000;1", "0212000000;1");
						double cContas1ValorDoisAnosAnterior = cContas1.obterTotalizacaoExistente(aseguradora,mesDoisAnosAnterior);
						contasProArquivo.put("0212000000;2", "0212000000;2");

						double cContas2ValorAtual = cContas2.obterTotalizacaoExistente(aseguradora,	mesAnoCalculo);
						contasProArquivo.put("0401000000;", "0401000000;");
						double cContas2ValorAnterior = cContas2.obterTotalizacaoExistente(aseguradora,	mesAnoAnterior);
						contasProArquivo.put("0401000000;1", "0401000000;1");

						double cContas3ValorAtual = cContas3.obterTotalizacaoExistente(aseguradora,	mesAnoCalculo);
						contasProArquivo.put("0402000000;", "0402000000;");
						double cContas3ValorAnterior = cContas3.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0402000000;1", "0402000000;1");
						
						double cContas4ValorAtual = cContas4.obterTotalizacaoExistente(aseguradora,	mesAnoCalculo);
						contasProArquivo.put("0403000000;", "0403000000;");
						double cContas4ValorAnterior = cContas4.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0403000000;1", "0403000000;1");
						
						double totalDividendoRatio1 = (cContas1ValorAtual
								- cContas1ValorAnterior + cContas2ValorAtual
								+ cContas3ValorAtual + cContas4ValorAtual)
								- (cContas1ValorAnterior
										- cContas1ValorDoisAnosAnterior
										+ cContas2ValorAnterior
										+ cContas3ValorAnterior + cContas4ValorAnterior);
						double totalDivisorRatio1 = (cContas1ValorAnterior
								- cContas1ValorDoisAnosAnterior)
								+ cContas2ValorAnterior + cContas3ValorAnterior
								+ cContas4ValorAnterior;

						double totalRatio1 = 0;

						double ponderacao = 0;

						if (totalDivisorRatio1 > 0)
							totalRatio1 = (totalDividendoRatio1 / totalDivisorRatio1) * 100;
						
						

						fileRatio.write("Aseguradora: "	+ aseguradora.obterNome() + "\r\n");
						fileRatio.write("RATIO1Pri --> ((0212000000 - 0212000000AnoAnt + 0401000000 + 0402000000 + 0403000000) - (0212000000AnoAnt - 0212000000Ano2Ant + 0401000000AnoAnt + 0402000000AnoAnt + 0403000000AnoAnt))  : "
										+ formataValor.format(totalDividendoRatio1) + "\r\n");
						fileRatio.write("RATIO1Seg --> 0212000000AnoAnt - 0212000000Ano2Ant + 0401000000AnoAnt + 0402000000AnoAnt + 0403000000AnoAnt: "
										+ formataValor.format(totalDivisorRatio1) + "\r\n");
						fileRatio.write("RATIO1Result --> (RATIO1Pri / RATIO1Seg)*100 "
										+ formataValor.format(totalRatio1) + "\r\n");

						System.out.println("totalDividendoRatio1: "	+ totalDividendoRatio1);
						System.out.println("totalDivisorRatio1: "+ totalDivisorRatio1);

						System.out.println("totalRatio1: " + totalRatio1);

						if (totalRatio1 >= -10 && totalRatio1 <= 33)
						{
							ponderacao += 2;
							//file.write("Ponderacao1: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao1: " + ponderacao + "\r\n";
						}

						//CONTAS PRA FAZER O CÁLCULO do RATIOS 2
						ClassificacaoContas cContas5 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0109030000");
						ClassificacaoContas cContas6 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0501000000");
						ClassificacaoContas cContas7 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0502000000");

						//Calculo do Indicador RATIO2
						double cContas5ValorAtual = cContas5.obterTotalizacaoExistente(aseguradora,	mesAnoCalculo);
						contasProArquivo.put("0109030000;", "0109030000;");
						double cContas5ValorAnterior = cContas5.obterTotalizacaoExistente(aseguradora,	mesAnoAnterior);
						contasProArquivo.put("0109030000;1", "0109030000;1");
						double cContas5ValorDoisAnosAnterior = cContas5.obterTotalizacaoExistente(aseguradora,mesDoisAnosAnterior);
						contasProArquivo.put("0109030000;2", "0109030000;2");
						
						double cContas6ValorAtual = cContas6.obterTotalizacaoExistente(aseguradora,	mesAnoCalculo);
						contasProArquivo.put("0501000000;", "0501000000;");
						double cContas6ValorAnterior = cContas6.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0501000000;1", "0501000000;1");

						double cContas7ValorAtual = cContas7.obterTotalizacaoExistente(aseguradora,	mesAnoCalculo);
						contasProArquivo.put("0502000000;", "0502000000;");
						double cContas7ValorAnterior = cContas7.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0502000000;1", "0502000000;1");

						double totalDividendoRatio2 = ((cContas1ValorAtual
								- cContas1ValorAnterior + cContas2ValorAtual
								+ cContas3ValorAtual + cContas4ValorAtual) - (cContas5ValorAtual
								- cContas5ValorAnterior + cContas6ValorAtual + cContas7ValorAtual)
								- (cContas1ValorAnterior
										- cContas1ValorDoisAnosAnterior
										+ cContas2ValorAnterior
										+ cContas3ValorAnterior + cContas4ValorAnterior) - (cContas5ValorAnterior
										- cContas5ValorDoisAnosAnterior
										+ cContas6ValorAnterior + cContas7ValorAnterior));
						double totalDivisorRatio2 = (cContas1ValorAnterior
								- cContas1ValorDoisAnosAnterior
								+ cContas2ValorAnterior + cContas3ValorAnterior + cContas4ValorAnterior)
								- (cContas5ValorAnterior
										- cContas5ValorDoisAnosAnterior
										+ cContas6ValorAnterior + cContas7ValorAnterior);

						double totalRatio2 = 0;

						if (totalDivisorRatio2 > 0)
							totalRatio2 = (totalDividendoRatio2 / totalDivisorRatio2) * 100;

						fileRatio.write("RATIO2Pri --> 0212000000 - 0212000000AnoAnt + 0401000000 + 0402000000 + 0403000000 - 0109030000 - 0109030000AnoAnt + 0501000000 + 0502000000 - 0212000000AnoAnt - 0212000000Ano2Ant + 0401000000AnoAnt + 0402000000AnoAnt + 0403000000AnoAnt - 0109030000AnoAnt - 0109030000Ano2Ant + 0501000000AnoAnt + 0502000000AnoAnt: "
										+ formataValor.format(totalDividendoRatio2) + "\r\n");
						fileRatio.write("RATIO2Seg --> 0212000000AnoAnt - 0212000000Ano2Ant + 0401000000AnoAnt + 0402000000AnoAnt + 0403000000AnoAnt - 0109030000AnoAnt - 0109030000Ano2Ant + 0501000000AnoAnt + 0502000000AnoAnt: "
										+ formataValor.format(totalDivisorRatio2) + "\r\n");
						fileRatio.write("RATIO2Result --> (RATIO2Pri / RATIO2Seg)*100: "
										+ formataValor.format(totalRatio2) + "\r\n");

						System.out.println("totalRatio2: " + totalRatio2);

						if (totalRatio2 >= -33 && totalRatio2 <= 33)
						{
							ponderacao += 2;
							//file.write("Ponderacao2: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao2: " + ponderacao + "\r\n";
						}
						
						//CONTAS PRA FAZER O CÁLCULO do RATIOS 3
						ClassificacaoContas cContas8 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0506000000");
						ClassificacaoContas cContas9 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0507000000");
						ClassificacaoContas cContas10 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0508000000");
						ClassificacaoContas cContas11 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0509000000");
						ClassificacaoContas cContas12 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0512000000");
						ClassificacaoContas cContas13 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0407000000");
						ClassificacaoContas cContas14 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0412000000");

						//Calculo do Indicador RATIO 3
						double cContas8ValorAtual = cContas8.obterTotalizacaoExistente(aseguradora,	mesAnoCalculo);
						contasProArquivo.put("0506000000;", "0506000000;");
						double cContas8ValorAnterior = cContas8.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0506000000;1", "0506000000;1");
						
						double cContas9ValorAtual = cContas9.obterTotalizacaoExistente(aseguradora,	mesAnoCalculo);
						contasProArquivo.put("0507000000;", "0507000000;");
						double cContas9ValorAnterior = cContas9.obterTotalizacaoExistente(aseguradora,	mesAnoAnterior);
						contasProArquivo.put("0507000000;1", "0507000000;1");
						
						double cContas10ValorAtual = cContas10.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0508000000;", "0508000000;");
						double cContas10ValorAnterior = cContas10.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0508000000;1", "0508000000;1");
						
						double cContas11ValorAtual = cContas11.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0509000000;", "0509000000;");
						double cContas11ValorAnterior = cContas11.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0509000000;1", "0509000000;1");
						
						double cContas12ValorAtual = cContas12.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0512000000;", "0512000000;");
						double cContas12ValorAnterior = cContas12.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0512000000;1", "0512000000;1");
						
						double cContas13ValorAtual = cContas13.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0407000000;", "0407000000;");
						double cContas13ValorAnterior = cContas13.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0407000000;1", "0407000000;1");
						
						double cContas14ValorAtual = cContas14.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0412000000;", "0412000000;");

						double totalDividendoRatio3 = cContas8ValorAtual
								+ cContas9ValorAtual + cContas10ValorAtual
								+ cContas11ValorAtual + cContas12ValorAtual
								- cContas13ValorAtual - cContas14ValorAtual;

						double totalDivisorRatio3 = cContas1ValorAtual
								- cContas1ValorAnterior + cContas2ValorAtual
								+ cContas3ValorAtual + cContas4ValorAtual;

						double totalRatio3 = 0;

						if (totalDivisorRatio3 > 0)
							totalRatio3 = (totalDividendoRatio3 / totalDivisorRatio3) * 100;

						fileRatio.write("RATIO3Pri --> 0506000000 + 0507000000 + 0508000000 + 0509000000 + 0512000000 - 0407000000 - 0412000000: "
										+ formataValor.format(totalDividendoRatio3) + "\r\n");
						fileRatio.write("RATIO3Seg --> : 0212000000 - 0212000000AnoAnt + 0401000000 + 0402000000 + 0403000000: "
										+ formataValor.format(totalDivisorRatio3) + "\r\n");
						fileRatio.write("RATIO3Result: (RATIO3Pri / RATIO3Seg)*100: "
										+ formataValor.format(totalRatio3) + "\r\n");

						System.out.println("totalRatio3: " + totalRatio3);

						if (totalRatio3 >= 30 && totalRatio3 <= 70)
						{
							ponderacao += 3;
							//file.write("Ponderacao3: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao3: " + ponderacao + "\r\n";
						}

						//CONTAS PRA FAZER O CÁLCULO do RATIOS 4
						ClassificacaoContas cContas15 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0408000000");
						ClassificacaoContas cContas16 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0409000000");

						//Calculo do Indicador RATIO 4

						double cContas15ValorAtual = cContas15.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0408000000;", "0408000000;");
						double cContas15ValorAnterior = cContas15.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0408000000;1", "0408000000;1");
						
						double cContas16ValorAtual = cContas16.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0409000000;", "0409000000;");
						double cContas16ValorAnterior = cContas16.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0409000000;1", "0409000000;1");

						double totalDividendoRatio4 = cContas8ValorAtual
								+ cContas9ValorAtual + cContas10ValorAtual
								+ cContas11ValorAtual + cContas12ValorAtual
								- cContas13ValorAtual - cContas14ValorAtual
								- cContas15ValorAtual - cContas16ValorAtual;

						double totalDivisorRatio4 = cContas1ValorAtual
								- cContas1ValorAnterior + cContas2ValorAtual
								+ cContas3ValorAtual + cContas4ValorAtual
								- cContas5ValorAtual - cContas4ValorAnterior
								+ cContas6ValorAtual + cContas7ValorAtual;

						double totalRatio4 = 0;

						if (totalDivisorRatio4 > 0)
							totalRatio4 = (totalDividendoRatio4 / totalDivisorRatio4) * 100;

						fileRatio.write("RATIO4Pri --> 0506000000 + 0507000000 + 0508000000 + 0509000000 + 0512000000 - 0407000000 - 0412000000 - 0408000000 - 0409000000: "
										+ formataValor.format(totalDividendoRatio4) + "\r\n");
						fileRatio.write("RATIO4Seg --> 0212000000 - 0212000000AnoAnt + 0401000000 + 0402000000 + 0403000000 - 0109030000 - 0109030000AnoAnt + 0501000000 + 0502000000: "
										+ formataValor.format(totalDivisorRatio4) + "\r\n");
						fileRatio.write("RATIO4Result: (RATIO4Pri / RATIO4Seg)*100: "
										+ formataValor.format(totalRatio4) + "\r\n");

						System.out.println("totalRatio4: " + totalRatio4);

						if (totalRatio4 >= 30 && totalRatio4 <= 70)
						{
							ponderacao += 3;
							//file.write("Ponderacao4: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao4: " + ponderacao + "\r\n";
						}

						//CONTAS PRA FAZER O CÁLCULO do RATIOS 5
						ClassificacaoContas cContas17 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0504000000");
						ClassificacaoContas cContas18 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0510000000");
						ClassificacaoContas cContas19 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0514000000");
						ClassificacaoContas cContas20 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0516000000");
						ClassificacaoContas cContas21 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0405000000");
						ClassificacaoContas cContas22 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0410000000");
						ClassificacaoContas cContas23 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0411000000");
						ClassificacaoContas cContas24 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0412000000");
						ClassificacaoContas cContas25 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0525000000");

						//				Calculo do Indicador RATIO 5

						double cContas17ValorAtual = cContas17.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0504000000;", "0504000000;");
						double cContas17ValorAnterior = cContas17.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0504000000;1", "0504000000;1");
						
						double cContas18ValorAtual = cContas18.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0510000000;", "0510000000;");
						double cContas18ValorAnterior = cContas18.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0510000000;1", "0510000000;1");
						
						double cContas19ValorAtual = cContas19.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0514000000;", "0514000000;");
						
						double cContas20ValorAtual = cContas20.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0516000000;", "0516000000;");
						
						double cContas21ValorAtual = cContas21.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0405000000;", "0405000000;");
						
						double cContas22ValorAtual = cContas22.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0410000000;", "0410000000;");
						double cContas22ValorAnterior = cContas22.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0410000000;1", "0410000000;1");
						
						double cContas23ValorAtual = cContas23.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0411000000;", "0411000000;");
						double cContas23ValorAnterior = cContas23.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0411000000;1", "0411000000;1");
						
						double cContas24ValorAtual = cContas24.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0412000000;", "0412000000;");
						double cContas24ValorAnterior = cContas24.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0412000000;1", "0412000000;1");
						
						double cContas25ValorAtual = cContas25.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0525000000;", "0525000000;");

						double totalDividendoRatio5 = ((cContas17ValorAtual
								+ cContas18ValorAtual + cContas12ValorAtual
								+ cContas19ValorAtual + cContas20ValorAtual
								- cContas21ValorAtual - cContas22ValorAtual
								- cContas23ValorAtual - cContas24ValorAtual)
								+ (cContas25ValorAtual) + (cContas8ValorAtual
								+ cContas9ValorAtual + cContas10ValorAtual
								+ cContas11ValorAtual + cContas12ValorAtual
								- cContas13ValorAtual - cContas24ValorAtual)
								- (cContas15ValorAtual + cContas16ValorAtual));

						double totalDivisorRatio5 = (cContas1ValorAtual
								- cContas1ValorAnterior + cContas2ValorAtual
								+ cContas3ValorAtual + cContas4ValorAtual)
								- (cContas5ValorAtual - cContas5ValorAnterior
								+ cContas6ValorAtual + cContas7ValorAtual);

						double totalRatio5 = 0;

						if (totalDivisorRatio5 > 0)
							totalRatio5 = (totalDividendoRatio5 / totalDivisorRatio5) * 100;

						fileRatio
								.write("RATIO5Pri --> 0504000000 + 0510000000 + 0512000000 + 0514000000 + 0516000000 - 0405000000 - 0410000000 - 0411000000 - 0412000000 + 0525000000 + 0506000000 + 0507000000 + 0508000000 + 0509000000 + 0512000000 - 0407000000 - 0412000000 - 0408000000 + 0409000000: "
										+ formataValor.format(totalDividendoRatio5) + "\r\n");
						fileRatio
								.write("RATIO5Seg --> 0212000000 - 0212000000AnoAnt + 0401000000 + 0402000000 + 0403000000 - 0109030000 - 0109030000AnoAnt + 0501000000 + 0502000000: "
										+ formataValor.format(totalDivisorRatio5) + "\r\n");
						fileRatio
								.write("RATIO5Result: (RATIO5Pri / RATIO5Seg)*100: "
										+ formataValor.format(totalRatio5) + "\r\n");

						System.out.println("totalRatio5: " + totalRatio5);

						if (totalRatio5 <= 110)
						{
							ponderacao += 3;
							//file.write("Ponderacao5: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao5: " + ponderacao + "\r\n";
						}

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 6

						//				Calculo do Indicador RATIO 6

						double totalDividendoRatio6 = cContas17ValorAtual
								+ cContas18ValorAtual + cContas12ValorAtual
								+ cContas19ValorAtual + cContas20ValorAtual
								+ cContas25ValorAtual;

						double totalDivisorRatio6 = cContas1ValorAtual
								- cContas1ValorAnterior + cContas2ValorAtual
								+ cContas3ValorAtual + cContas4ValorAtual;

						double totalRatio6 = 0;

						if (totalDivisorRatio6 > 0)
							totalRatio6 = (totalDividendoRatio6 / totalDivisorRatio6) * 100;

						fileRatio
								.write("RATIO6Pri --> 0504000000 + 0510000000 + 0512000000 + 0514000000 + 0516000000 - 0525000000: "
										+ formataValor.format(totalDividendoRatio6) + "\r\n");
						fileRatio
								.write("RATIO6Seg --> 0212000000 - 0212000000AnoAnt + 0401000000 + 0402000000 + 0403000000: "
										+ formataValor.format(totalDivisorRatio6) + "\r\n");
						fileRatio
								.write("RATIO6Result: (RATIO6Pri / RATIO6Seg)*100: "
										+ formataValor.format(totalRatio6) + "\r\n");

						System.out.println("totalRatio6: " + totalRatio6);

						if (totalRatio6 <= 50)
						{
							ponderacao += 3;
							//file.write("Ponderacao6: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao6: " + ponderacao + "\r\n";
						}

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 7

						//				Calculo do Indicador RATIO 7

						double totalDividendoRatio7 = (cContas17ValorAtual
								+ cContas18ValorAtual + cContas12ValorAtual
								+ cContas19ValorAtual + cContas20ValorAtual
								- cContas21ValorAtual - cContas22ValorAtual
								- cContas23ValorAtual - cContas24ValorAtual)
								+ cContas25ValorAtual;

						double totalDivisorRatio7 = (cContas1ValorAtual
								- cContas1ValorAnterior + cContas2ValorAtual
								+ cContas3ValorAtual + cContas4ValorAtual)
								- (cContas5ValorAtual - cContas5ValorAnterior
								+ cContas6ValorAtual + cContas7ValorAtual);

						double totalRatio7 = 0;

						if (totalDivisorRatio7 > 0)
							totalRatio7 = (totalDividendoRatio7 / totalDivisorRatio7) * 100;

						fileRatio
								.write("RATIO7Pri --> 0504000000 + 0510000000 + 0512000000 + 0514000000 + 0516000000 - 0405000000 - 0410000000 - 0411000000 - 0412000000 + 0525000000: "
										+ formataValor.format(totalDividendoRatio7) + "\r\n");
						fileRatio
								.write("RATIO7Seg --> 0212000000 - 0212000000AnoAnt + 0401000000 + 0402000000 + 0403000000 - 0109030000 - 0109030000AnoAnt + 0501000000 + 0502000000: "
										+ formataValor.format(totalDivisorRatio7) + "\r\n");
						fileRatio
								.write("RATIO7Result: (RATIO7Pri / RATIO7Seg)*100: "
										+ formataValor.format(totalRatio7) + "\r\n");

						System.out.println("totalRatio7: " + totalRatio7);

						if (totalRatio7 <= 50)
						{
							ponderacao += 3;
							//file.write("Ponderacao7: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao7: " + ponderacao + "\r\n";
						}

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 8

						//				Calculo do Indicador RATIO 8

						double totalDividendoRatio8 = cContas17ValorAtual
								+ cContas18ValorAtual + cContas12ValorAtual
								+ cContas19ValorAtual + cContas20ValorAtual;

						double totalDivisorRatio8 = cContas1ValorAtual
								- cContas1ValorAnterior + cContas2ValorAtual
								+ cContas3ValorAtual + cContas4ValorAtual;

						double totalRatio8 = 0;

						if (totalDivisorRatio8 > 0)
							totalRatio8 = (totalDividendoRatio8 / totalDivisorRatio8) * 100;

						fileRatio
								.write("RATIO8Pri --> 0504000000 + 0510000000 + 0512000000 + 0514000000 + 0516000000: "
										+ formataValor.format(totalDividendoRatio8) + "\r\n");
						fileRatio
								.write("RATIO8Seg --> 0212000000 - 0212000000AnoAnt + 0401000000 + 0402000000 + 0403000000: "
										+ formataValor.format(totalDivisorRatio8) + "\r\n");
						fileRatio
								.write("RATIO8Result: (RATIO8Pri / RATIO8Seg)*100: "
										+ formataValor.format(totalRatio8) + "\r\n");

						System.out.println("totalRatio8: " + totalRatio8);

						if (totalRatio8 <= 20)
						{
							ponderacao += 2;
							//file.write("Ponderacao8: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao8: " + ponderacao + "\r\n";
						}

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 9

						//				Calculo do Indicador RATIO 9

						double totalDividendoRatio9 = cContas25ValorAtual;

						double totalDivisorRatio9 = cContas1ValorAtual
								- cContas1ValorAnterior + cContas2ValorAtual
								+ cContas3ValorAtual + cContas4ValorAtual;

						double totalRatio9 = 0;

						if (totalDivisorRatio9 > 0)
							totalRatio9 = (totalDividendoRatio9 / totalDivisorRatio9) * 100;

						fileRatio.write("RATIO9Pri --> 0525000000: "
								+ formataValor.format(totalDividendoRatio9) + "\r\n");
						fileRatio
								.write("RATIO9Seg --> 0212000000 - 0212000000AnoAnt + 0401000000 + 0402000000 + 0403000000: "
										+ formataValor.format(totalDivisorRatio9) + "\r\n");
						fileRatio
								.write("RATIO9Result: (RATIO9Pri / RATIO9Seg)*100: "
										+ formataValor.format(totalRatio9) + "\r\n");

						System.out.println("totalRatio9: " + totalRatio9);

						if (totalRatio9 <= 30)
						{
							ponderacao += 2;
							//file.write("Ponderacao9: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao9: " + ponderacao + "\r\n";
						}

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 10
						ClassificacaoContas cContas26 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0213000000");

						//				Calculo do Indicador RATIO 10
						double cContas26ValorAtual = cContas26.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0213000000;", "0213000000;");
						double cContas26ValorAnterior = cContas26.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0213000000;1", "0213000000;1");

						double totalDividendoRatio10 = cContas1ValorAtual
								+ cContas26ValorAtual;

						double totalDivisorRatio10 = cContas1ValorAtual
								- cContas1ValorAnterior + cContas2ValorAtual
								+ cContas3ValorAtual + cContas4ValorAtual;

						double totalRatio10 = 0;

						if (totalDivisorRatio10 > 0)
							totalRatio10 = (totalDividendoRatio10 / totalDivisorRatio10) * 100;

						fileRatio
								.write("RATIO10Pri --> 0212000000 + 0213000000: "
										+ formataValor.format(totalDividendoRatio10) + "\r\n");
						fileRatio
								.write("RATIO10Seg --> 0212000000 - 0212000000AnoAnt + 0401000000 + 0402000000 + 0403000000: "
										+ formataValor.format(totalDivisorRatio10) + "\r\n");
						fileRatio
								.write("RATIO10Result: (RATIO10Pri / RATIO10Seg)*100: "
										+ formataValor.format(totalRatio10) + "\r\n");

						System.out.println("totalRatio10: " + totalRatio10);

						if (totalRatio10 <= 40)
						{
							ponderacao += 2;
							//file.write("Ponderacao10: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao10: " + ponderacao + "\r\n";
						}
						

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 11

						//				Calculo do Indicador RATIO 11

						double totalDividendoRatio11 = cContas1ValorAtual
								+ cContas26ValorAtual;

						double totalDivisorRatio11 = (cContas8ValorAtual
								+ cContas9ValorAtual + cContas10ValorAtual
								+ cContas11ValorAtual + cContas12ValorAtual
								- cContas13ValorAtual - cContas14ValorAtual)
								- (cContas15ValorAtual + cContas16ValorAtual);

						double totalRatio11 = 0;

						if (totalDivisorRatio11 > 0)
							totalRatio11 = (totalDividendoRatio11 / totalDivisorRatio11) * 100;

						fileRatio
								.write("RATIO11Pri --> 0212000000 + 0213000000: "
										+ formataValor.format(totalDividendoRatio11) + "\r\n");
						fileRatio
								.write("RATIO11Seg --> 0506000000 + 0507000000 + 0508000000 + 0509000000 + 0512000000 - 0407000000 - 0412000000 - 0408000000 + 0409000000: "
										+ formataValor.format(totalDivisorRatio11) + "\r\n");
						fileRatio
								.write("RATIO11Result: (RATIO11Pri / RATIO11Seg)*100: "
										+ formataValor.format(totalRatio11) + "\r\n");

						System.out.println("totalRatio11: " + totalRatio11);

						if (totalRatio11 >= 100)
						{
							ponderacao += 2;
							//file.write("Ponderacao11: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao11: " + ponderacao + "\r\n";
						}

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 12
						ClassificacaoContas cContas27 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0300000000");

						//				Calculo do Indicador RATIO 12
						double cContas27ValorAtual = cContas27.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0300000000;", "0300000000;");
						double cContas27ValorAnterior = cContas27.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0300000000;1", "0300000000;1");

						double totalDividendoRatio12 = cContas27ValorAtual
								- cContas27ValorAnterior;

						double totalDivisorRatio12 = cContas27ValorAnterior;

						double totalRatio12 = 0;

						if (totalDivisorRatio12 > 0)
							totalRatio12 = (totalDividendoRatio12 / totalDivisorRatio12) * 100;

						fileRatio
								.write("RATIO12Pri --> 0300000000 - 0300000000AnoAnt: "
										+ formataValor.format(totalDividendoRatio12) + "\r\n");
						fileRatio.write("RATIO12Seg --> 0300000000AnoAnt: "
								+ formataValor.format(totalDivisorRatio12) + "\r\n");
						fileRatio
								.write("RATIO12Result: (RATIO12Pri / RATIO12Seg)*100: "
										+ formataValor.format(totalRatio12) + "\r\n");

						System.out.println("totalRatio12: " + totalRatio12);

						if (totalRatio12 >= 0 && totalRatio12 <= 50)
						{
							ponderacao += 3;
							//file.write("Ponderacao12: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao12: " + ponderacao + "\r\n";
						}

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 13
						ClassificacaoContas cContas28 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0100000000");

						//				Calculo do Indicador RATIO 13
						double cContas28ValorAtual = cContas28.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0100000000;", "0100000000;");

						double totalDividendoRatio13 = cContas27ValorAtual;

						double totalDivisorRatio13 = cContas28ValorAtual;

						double totalRatio13 = 0;

						if (totalDivisorRatio13 > 0)
							totalRatio13 = (totalDividendoRatio13 / totalDivisorRatio13) * 100;

						fileRatio.write("RATIO13Pri --> 0300000000: "
								+ formataValor.format(totalDividendoRatio13) + "\r\n");
						fileRatio.write("RATIO13Seg --> 0100000000: "
								+ formataValor.format(totalDivisorRatio13) + "\r\n");
						fileRatio
								.write("RATIO13Result: (RATIO13Pri / RATIO13Seg)*100: "
										+ formataValor.format(totalRatio13) + "\r\n");

						System.out.println("totalRatio13: " + totalRatio13);

						if (totalRatio13 >= 25 && totalRatio13 <= 75)
						{
							ponderacao += 2;
							//file.write("Ponderacao13: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao13: " + ponderacao + "\r\n";
						}

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 14
						ClassificacaoContas cContas29 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0108000000");

						//				Calculo do Indicador RATIO 14
						double cContas29ValorAtual = cContas29.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0108000000;", "0108000000;");

						double totalDividendoRatio14 = cContas29ValorAtual;

						double totalDivisorRatio14 = cContas28ValorAtual;

						double totalRatio14 = 0;

						if (totalDivisorRatio14 > 0)
							totalRatio14 = (totalDividendoRatio14 / totalDivisorRatio14) * 100;

						fileRatio.write("RATIO14Pri --> 0108000000: "
								+ formataValor.format(totalDividendoRatio14) + "\r\n");
						fileRatio.write("RATIO14Seg --> 0100000000: "
								+ formataValor.format(totalDivisorRatio14) + "\r\n");
						fileRatio
								.write("RATIO14Result: (RATIO14Pri / RATIO14Seg)*100: "
										+ formataValor.format(totalRatio14) + "\r\n");

						System.out.println("totalRatio14: " + totalRatio14);

						if (totalRatio14 <= 35)
						{
							ponderacao += 3;
							//file.write("Ponderacao14: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao14: " + ponderacao + "\r\n";
						}

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 15
						ClassificacaoContas cContas30 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0701010000");
						ClassificacaoContas cContas31 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0601010000");

						//				Calculo do Indicador RATIO 15
						double cContas30ValorAtual = cContas30.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0701010000;", "0701010000;");
						
						double cContas31ValorAtual = cContas31.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0601010000;", "0601010000;");

						double totalDividendoRatio15 = cContas30ValorAtual- cContas31ValorAtual;

						double totalDivisorRatio15 = cContas27ValorAtual;

						double totalRatio15 = 0;

						if (totalDivisorRatio15 > 0)
							totalRatio15 = (totalDividendoRatio15 / totalDivisorRatio15) * 100;

						fileRatio
								.write("RATIO15Pri --> 0701010000 - 0601010000: "
										+ formataValor.format(totalDividendoRatio15) + "\r\n");
						fileRatio.write("RATIO15Seg --> 0300000000: "
								+ formataValor.format(totalDivisorRatio15) + "\r\n");
						fileRatio
								.write("RATIO15Result: (RATIO15Pri / RATIO15Seg)*100: "
										+ formataValor.format(totalRatio15) + "\r\n");

						System.out.println("totalRatio15: " + totalRatio15);

						if(ramo.equals("PATRIMONIALES"))
						{
							// if (totalRatio15 <= 20)
							if(totalRatio15 <= 20)
							{
								ponderacao += 3;
								//file.write("Ponderacao15: " + ponderacao + "\r\n");
								ponderacaoStr+="Ponderacao15: " + ponderacao + "\r\n";
							}
						}
						else if(ramo.equals("PATRIMONIALES Y VIDA"))
						{
							if(totalRatio15 <= 20)
							{
								ponderacao += 3;
								//file.write("Ponderacao15: " + ponderacao + "\r\n");
								ponderacaoStr+="Ponderacao15: " + ponderacao + "\r\n";
							}
						}
						else //Vida
						{
							//if (totalRatio15 <= 10)
							if(totalRatio15 <= 10)
							{
								ponderacao += 3;
								//file.write("Ponderacao15: " + ponderacao + "\r\n");
								ponderacaoStr+="Ponderacao15: " + ponderacao + "\r\n";
							}
						}
						

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 16

						//				Calculo do Indicador RATIO 16

						double totalDividendoRatio16 = cContas1ValorAtual
								- cContas1ValorAnterior + cContas2ValorAtual
								+ cContas3ValorAtual + cContas4ValorAtual
								- cContas5ValorAtual - cContas5ValorAnterior
								+ cContas6ValorAtual + cContas7ValorAtual;

						double totalDivisorRatio16 = cContas27ValorAtual;

						double totalRatio16 = 0;

						if (totalDivisorRatio16 > 0)
							totalRatio16 = (totalDividendoRatio16 / totalDivisorRatio16) * 100;

						fileRatio
								.write("RATIO16Pri --> 0212000000 - 0212000000AnoAnt + 0401000000 + 0402000000 + 0403000000 - 0109030000 - 0109030000AnoAnt + 0501000000 + 0502000000: "
										+ formataValor.format(totalDividendoRatio16) + "\r\n");
						fileRatio.write("RATIO16Seg --> 0300000000: "
								+ formataValor.format(totalDivisorRatio16) + "\r\n");
						fileRatio
								.write("RATIO16Result: (RATIO16Pri / RATIO16Seg)*100: "
										+ formataValor.format(totalRatio16) + "\r\n");

						System.out.println("totalRatio16: " + totalRatio16);

						if (totalRatio16 <= 300)
						{
							ponderacao += 2;
							//file.write("Ponderacao16: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao16: " + ponderacao + "\r\n";
						}

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 17
						ClassificacaoContas cContas32 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0213020000");

						//				Calculo do Indicador RATIO 17
						double cContas32ValorAtual = cContas32.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0213020000;", "0213020000;");
						
						double totalDividendoRatio17 = cContas32ValorAtual;

						double totalDivisorRatio17 = cContas27ValorAtual;

						double totalRatio17 = 0;

						if (totalDivisorRatio17 > 0)
							totalRatio17 = (totalDividendoRatio17 / totalDivisorRatio17) * 100;

						fileRatio.write("RATIO17Pri --> 0213020000: "
								+ formataValor.format(totalDividendoRatio17) + "\r\n");
						fileRatio.write("RATIO17Seg --> 0300000000: "
								+ formataValor.format(totalDivisorRatio17) + "\r\n");
						fileRatio
								.write("RATIO17Result: (RATIO17Pri / RATIO17Seg)*100: "
										+ formataValor.format(totalRatio17) + "\r\n");

						System.out.println("totalRatio17: " + totalRatio17);

						if (totalRatio17 <= 100)
						{
							ponderacao += 2;
							//file.write("Ponderacao17: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao17: " + ponderacao + "\r\n";
						}

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 18
						//ClassificacaoContas cContas33 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0540000000");
						ClassificacaoContas cContas33 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0305000000");

						//				Calculo do Indicador RATIO 18
						//double cContas33ValorAtual = cContas33.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						//contasProArquivo.put("0540000000;", "0540000000;");
						double cContas33ValorAtual = cContas33.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0305000000;", "0305000000;");

						double totalDividendoRatio18 = cContas33ValorAtual;

						double totalDivisorRatio18 = cContas27ValorAtual;

						double totalRatio18 = 0;

						if (totalDivisorRatio18 > 0)
							totalRatio18 = (totalDividendoRatio18 / totalDivisorRatio18) * 100;

						fileRatio.write("RATIO18Pri --> 0305000000: "
								+ formataValor.format(totalDividendoRatio18) + "\r\n");
						fileRatio.write("RATIO18Seg --> 0300000000: "
								+ formataValor.format(totalDivisorRatio18) + "\r\n");
						fileRatio
								.write("RATIO18Result: (RATIO18Pri / RATIO18Seg)*100: "
										+ formataValor.format(totalRatio18) + "\r\n");

						System.out.println("totalRatio18: " + totalRatio18);

						if (totalRatio18 >= IPC + 5)
						{
							ponderacao += 3;
							//file.write("Ponderacao18: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao18: " + ponderacao + "\r\n";
						}

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 19

						//				Calculo do Indicador RATIO 19

						double totalDividendoRatio19 = cContas33ValorAtual;

						double totalDivisorRatio19 = cContas1ValorAtual
								- cContas1ValorAnterior + cContas2ValorAtual
								+ cContas3ValorAtual + cContas4ValorAtual
								- cContas5ValorAtual - cContas5ValorAnterior
								+ cContas6ValorAtual + cContas7ValorAtual;

						double totalRatio19 = 0;

						if (totalDivisorRatio19 > 0)
							totalRatio19 = (totalDividendoRatio19 / totalDivisorRatio19) * 100;

						fileRatio.write("RATIO19Pri --> 0305000000: "
								+ formataValor.format(totalDividendoRatio19) + "\r\n");
						fileRatio
								.write("RATIO19Seg --> 0212000000 - 0212000000AnoAnt + 0401000000 + 0402000000 + 0403000000 - 0109030000 - 0109030000AnoAnt + 0501000000 + 0502000000: "
										+ formataValor.format(totalDivisorRatio19) + "\r\n");
						fileRatio
								.write("RATIO19Result: (RATIO19Pri / RATIO19Seg)*100: "
										+ formataValor.format(totalRatio19) + "\r\n");

						System.out.println("totalRatio19: " + totalRatio19);

						if (totalRatio19 >= 5)
						{
							ponderacao += 2;
							//file.write("Ponderacao19: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao19: " + ponderacao + "\r\n";
						}

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 20
						ClassificacaoContas cContas34 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0425000000");
						ClassificacaoContas cContas35 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0107000000");
						ClassificacaoContas cContas36 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0526000000");

						//				Calculo do Indicador RATIO 20
						double cContas34ValorAtual = cContas34.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0425000000;", "0425000000;");
						double cContas34ValorAnterior = cContas34.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0425000000;1", "0425000000;1");
						
						double cContas35ValorAtual = cContas35.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0107000000;", "0107000000;");
						double cContas35ValorAnterior = cContas35.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0107000000;1", "0107000000;1");
						
						double cContas36ValorAtual = cContas36.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0526000000;", "0526000000;");
						double cContas36ValorAnterior = cContas36.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0526000000;1", "0526000000;1");
						

						double totalDividendoRatio20 = cContas34ValorAtual;

						double totalDivisorRatio20 = cContas35ValorAtual
								- cContas36ValorAtual + cContas35ValorAnterior
								- cContas36ValorAnterior;

						double totalRatio20 = 0;

						if (totalDivisorRatio20 > 0)
							totalRatio20 = (totalDividendoRatio20 / totalDivisorRatio20) * 100;

						fileRatio.write("RATIO20Pri --> 0425000000: "
								+ formataValor.format(totalDividendoRatio20) + "\r\n");
						fileRatio
								.write("RATIO20Seg --> 0107000000 - 0526000000 + 0107000000AnoAnt - 0526000000AnoAnt: "
										+ formataValor.format(totalDivisorRatio20) + "\r\n");
						fileRatio
								.write("RATIO20Result: (RATIO20Pri / RATIO20Seg)*100: "
										+ formataValor.format(totalRatio20) + "\r\n");

						System.out.println("totalRatio20: " + totalRatio20);

						if (totalRatio20 >= IPC + 5)
						{
							ponderacao += 2;
							//file.write("Ponderacao20: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao20: " + ponderacao + "\r\n";
						}

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 21
						ClassificacaoContas cContas37 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0404000000");
						ClassificacaoContas cContas38 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0503000000");
						ClassificacaoContas cContas39 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0505000000");
						ClassificacaoContas cContas40 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0511000000");
						ClassificacaoContas cContas41 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0513000000");
						ClassificacaoContas cContas42 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0515000000");
						ClassificacaoContas cContas43 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0406000000");
						ClassificacaoContas cContas44 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0414000000");
						ClassificacaoContas cContas45 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0413000000");
						ClassificacaoContas cContas46 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0415000000");
						ClassificacaoContas cContas47 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0426000000");
						Conta conta48 = (Conta) entidadeHome.obterEntidadePorApelido("0525010401");
						ClassificacaoContas cContas49 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0527000000");

						//				Calculo do Indicador RATIO 21
						double cContas37ValorAtual = cContas37.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0404000000;", "0404000000;");
						
						double cContas38ValorAtual = cContas38.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0503000000;", "0503000000;");
						
						double cContas39ValorAtual = cContas39.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0505000000;", "0505000000;");
						double cContas39ValorAnterior = cContas39.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0505000000;1", "0505000000;1");
						
						double cContas40ValorAtual = cContas40.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0511000000;", "0511000000;");
						double cContas40ValorAnterior = cContas40.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0511000000;1", "0511000000;1");
						
						double cContas41ValorAtual = cContas41.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0513000000;", "0513000000;");
						double cContas41ValorAnterior = cContas41.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0513000000;1", "0513000000;1");
						
						double cContas42ValorAtual = cContas42.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0515000000;", "0515000000;");
						double cContas42ValorAnterior = cContas42.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0515000000;1", "0515000000;1");
						
						double cContas43ValorAtual = cContas43.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0406000000;", "0406000000;");
						double cContas43ValorAnterior = cContas43.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0406000000;1", "0406000000;1");
						
						double cContas44ValorAtual = cContas44.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0414000000;", "0414000000;");
						double cContas44ValorAnterior = cContas44.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0414000000;1", "0414000000;1");
						
						double cContas45ValorAtual = cContas45.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0413000000;", "0413000000;");
						
						double cContas46ValorAtual = cContas46.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0415000000;", "0415000000;");
						
						double cContas47ValorAtual = cContas47.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0426000000;", "0426000000;");
						double cContas47ValorAnterior = cContas47.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0426000000;1", "0426000000;1");
						
						double conta48ValorAtual = conta48.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0525010401;", "0525010401;");
						double conta48ValorAnterior = conta48.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0525010401;1", "0525010401;1");
						
						double cContas49ValorAtual = cContas49.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0527000000;", "0527000000;");
						double cContas49ValorAnterior = cContas49.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0527000000;1", "0527000000;1");

						double totalDividendoRatio21 = cContas2ValorAtual
								+ cContas3ValorAtual + cContas4ValorAtual
								+ cContas37ValorAtual - cContas6ValorAtual
								- cContas7ValorAtual - cContas38ValorAtual
								- cContas39ValorAtual - cContas8ValorAtual
								- cContas9ValorAtual - cContas10ValorAtual
								- cContas11ValorAtual - cContas40ValorAtual
								- cContas41ValorAtual - cContas42ValorAtual
								+ cContas43ValorAtual + cContas13ValorAtual
								+ cContas15ValorAtual + cContas16ValorAtual
								+ cContas14ValorAtual + cContas44ValorAtual
								+ cContas21ValorAtual + cContas22ValorAtual
								+ cContas23ValorAtual + cContas45ValorAtual
								+ cContas46ValorAtual + cContas47ValorAtual
								- cContas17ValorAtual - cContas18ValorAtual
								- cContas12ValorAtual - cContas19ValorAtual
								- cContas20ValorAtual - cContas25ValorAtual
								- conta48ValorAtual - cContas49ValorAtual;

						double totalDivisorRatio21 = (cContas1ValorAtual
								- cContas1ValorAnterior + cContas2ValorAtual
								+ cContas3ValorAtual + cContas4ValorAtual)
								- (cContas5ValorAtual - cContas5ValorAnterior
								+ cContas6ValorAtual + cContas7ValorAtual);

						double totalRatio21 = 0;

						if (totalDivisorRatio21 > 0)
							totalRatio21 = (totalDividendoRatio21 / totalDivisorRatio21) * 100;

						fileRatio.write("RATIO21Pri --> "
								+ cContas2.obterApelido() + " + "
								+ cContas3.obterApelido() + " + "
								+ cContas4.obterApelido() + " + "
								+ cContas37.obterApelido() + " - "
								+ cContas6.obterApelido() + " - "
								+ cContas7.obterApelido() + " - "
								+ cContas38.obterApelido() + " - "
								+ cContas39.obterApelido() + " - "
								+ cContas8.obterApelido() + " - "
								+ cContas9.obterApelido() + " - "
								+ cContas10.obterApelido() + " - "
								+ cContas11.obterApelido() + " - "
								+ cContas40.obterApelido() + " - "
								+ cContas41.obterApelido() + " - "
								+ cContas42.obterApelido() + " + "
								+ cContas43.obterApelido() + " + "
								+ cContas13.obterApelido() + " + "
								+ cContas15.obterApelido() + " + "
								+ cContas16.obterApelido() + " + "
								+ cContas14.obterApelido() + " + "
								+ cContas44.obterApelido() + " + "
								+ cContas21.obterApelido() + " + "
								+ cContas22.obterApelido() + " + "
								+ cContas23.obterApelido() + " + "
								+ cContas45.obterApelido() + " + "
								+ cContas46.obterApelido() + " + "
								+ cContas47.obterApelido() + " - "
								+ cContas17.obterApelido() + " - "
								+ cContas18.obterApelido() + " - "
								+ cContas12.obterApelido() + " - "
								+ cContas19.obterApelido() + " - "
								+ cContas20.obterApelido() + " - "
								+ cContas25.obterApelido() + " - "
								+ conta48.obterApelido() + " - "
								+ cContas49.obterApelido() + ": "
								+ totalDividendoRatio21 + "\r\n");

						fileRatio.write("RATIO21Seg --> "
								+ cContas1.obterApelido() + " - "
								+ cContas1.obterApelido() + "AnoAnt" + " + "
								+ cContas2.obterApelido() + " + "
								+ cContas3.obterApelido() + " + "
								+ cContas4.obterApelido() + " - "
								+ cContas5.obterApelido() + " - "
								+ cContas5.obterApelido() + "AnoAnt" + " + "
								+ cContas6.obterApelido() + " + "
								+ cContas7.obterApelido() + ": "
								+ totalDivisorRatio21 + "\r\n");
						fileRatio
								.write("RATIO21Result: (RATIO21Pri / RATIO21Seg)*100: "
										+ formataValor.format(totalRatio21) + "\r\n");

						System.out.println("totalRatio21: " + totalRatio21);

						if (totalRatio21 >= -10)
						{
							ponderacao += 2;
							//file.write("Ponderacao21: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao21: " + ponderacao + "\r\n";
						}

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 22
						ClassificacaoContas cContas50 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0101000000");

						//				Calculo do Indicador RATIO 22
						double cContas50ValorAtual = cContas50.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0101000000;", "0101000000;");

						RatioPermanente ratio = aseguradora.obterRatioPermanente();

						double inversao = 0;
						double deudas = 0;

						if (ratio != null)
						{
							inversao = ratio.obterInversao();
							deudas = ratio.obterDeudas();
						}

						double totalDividendoRatio22 = cContas50ValorAtual
								+ inversao;

						double totalDivisorRatio22 = deudas;

						double totalRatio22 = 0;

						if (totalDivisorRatio22 > 0)
							totalRatio22 = (totalDividendoRatio22 / totalDivisorRatio22) * 100;

						System.out.println("totalRatio22: " + totalRatio22);

						fileRatio.write("inversao --> ratio.obterInversao(): "
								+ formataValor.format(inversao) + "\r\n");
						fileRatio.write("deudas --> ratio.obterDeudas(): "
								+ deudas + "\r\n");
						fileRatio
								.write("RATIO22Pri --> 0101000000 + inversao: "
										+ formataValor.format(totalDividendoRatio22) + "\r\n");
						fileRatio.write("RATIO22Seg --> deudas: " + deudas
								+ "\r\n");
						fileRatio
								.write("RATIO22Result: (RATIO22Pri / RATIO22Seg)*100: "
										+ formataValor.format(totalRatio22) + "\r\n");

						if (totalRatio22 >= 100)
						{
							ponderacao += 3;
							//file.write("Ponderacao22: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao22: " + ponderacao + "\r\n";
						}

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 23

						//				Calculo do Indicador RATIO 23

						double ativo = 0;
						double passivo = 0;

						if (ratio != null) {
							ativo = ratio.obterAtivoCorrente();
							passivo = ratio.obterPassivoCorrente();
						}

						double totalDividendoRatio23 = ativo;

						double totalDivisorRatio23 = passivo;

						double totalRatio23 = 0;

						if (totalDivisorRatio23 > 0)
							totalRatio23 = (totalDividendoRatio23 / totalDivisorRatio23) * 100;

						fileRatio
								.write("ativo --> ratio.obterAtivoCorrente(): "
										+ formataValor.format(ativo) + "\r\n");
						fileRatio
								.write("passivo --> ratio.obterPassivoCorrente(): "
										+ formataValor.format(passivo) + "\r\n");
						fileRatio.write("RATIO23Pri --> ativo: " + formataValor.format(ativo)
								+ "\r\n");
						fileRatio.write("RATIO23Seg --> passivo: " + formataValor.format(passivo)
								+ "\r\n");
						fileRatio
								.write("RATIO23Result: (RATIO23Pri / RATIO23Seg)*100: "
										+ formataValor.format(totalRatio23) + "\r\n");

						System.out
								.println("ativo --> ratio.obterAtivoCorrente(): "
										+ formataValor.format(ativo));
						System.out
								.println("passivo --> ratio.obterPassivoCorrente(): "
										+ formataValor.format(passivo));
						System.out.println("RATIO23Pri --> ativo: " + formataValor.format(ativo));
						System.out
								.println("RATIO23Seg --> passivo: " + formataValor.format(passivo));
						System.out
								.println("RATIO23Result: (RATIO23Pri / RATIO23Seg)*100: "
										+ formataValor.format(totalRatio23));

						System.out.println("totalRatio23: " + formataValor.format(totalRatio23));

						if (totalRatio23 >= 180)
						{
							ponderacao += 3;
							//file.write("Ponderacao23: " + ponderacao  + "\r\n");
							ponderacaoStr+="Ponderacao23: " + ponderacao + "\r\n";
						}

						//				CONTAS PRA FAZER O CÁLCULO do RATIOS 24
						ClassificacaoContas cContas51 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0405020000");
						ClassificacaoContas cContas52 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0102010000");
						ClassificacaoContas cContas53 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0103010000");
						ClassificacaoContas cContas54 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0102020000");
						ClassificacaoContas cContas55 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0103020000");
						
						ClassificacaoContas cContas56 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0203000000");
						ClassificacaoContas cContas57 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0106010000");
						ClassificacaoContas cContas58 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0202030000");
						ClassificacaoContas cContas59 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0202040000");
						ClassificacaoContas cContas60 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0214010000");
						ClassificacaoContas cContas61 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0202010000");
						ClassificacaoContas cContas62 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0202020000");
						ClassificacaoContas cContas63 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0202050000");
						ClassificacaoContas cContas64 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0202060000");
						ClassificacaoContas cContas65 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0102032000");
						ClassificacaoContas cContas66 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0103032000");
						ClassificacaoContas cContas67 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0102032100");
						ClassificacaoContas cContas68 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0103032100");
						ClassificacaoContas cContas69 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0106020000");
						ClassificacaoContas cContas70 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0525010700");
						ClassificacaoContas cContas71 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0525010800");
						ClassificacaoContas cContas72 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0525010400");
						ClassificacaoContas cContas73 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0102050000");
						ClassificacaoContas cContas74 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0102060000");
						ClassificacaoContas cContas75 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0104000000");
						ClassificacaoContas cContas76 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0105000000");
						ClassificacaoContas cContas77 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0109010000");
						ClassificacaoContas cContas78 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0109020000");
						ClassificacaoContas cContas79 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0109070000");
						ClassificacaoContas cContas80 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0109080000");
						ClassificacaoContas cContas81 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0206000000");
						ClassificacaoContas cContas82 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0210000000");
						ClassificacaoContas cContas83 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0211000000");
						ClassificacaoContas cContas84 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0205000000");
						ClassificacaoContas cContas86 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0214030000");
						ClassificacaoContas cContas87 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0214040000");
						ClassificacaoContas cContas88 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0109040000");
						ClassificacaoContas cContas89 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0109050000");
						ClassificacaoContas cContas90 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0109060000");
						ClassificacaoContas cContas91 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0211010200");

						//				Calculo do Indicador RATIO 24
						double cContas51ValorAtual = cContas51.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0405020000;", "0405020000;");
						
						double cContas52ValorAtual = cContas52.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0102010000;", "0102010000;");
						
						double cContas53ValorAtual = cContas53.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0103010000;", "0103010000;");
						
						double cContas54ValorAtual = cContas54.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0102020000;", "0102020000;");
						
						double cContas55ValorAtual = cContas55.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0103020000;", "0103020000;");
						
						double cContas56ValorAtual = cContas56.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0203000000;", "0203000000;");
						contasProArquivo.put("0203000000;1", "0203000000;1");
						
						double cContas57ValorAtual = cContas57.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0106010000;", "0106010000;");
						contasProArquivo.put("0106010000;1", "0106010000;1");
						
						double cContas58ValorAtual = cContas58.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0202030000;", "0202030000;");
						contasProArquivo.put("0202030000;1", "0202030000;1");
						
						double cContas59ValorAtual = cContas59.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0202040000;", "0202040000;");
						contasProArquivo.put("0202040000;1", "0202040000;1");
						
						double cContas60ValorAtual = cContas60.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0214010000;", "0214010000;");
						contasProArquivo.put("0214010000;1", "0214010000;1");
						
						double cContas61ValorAtual = cContas61.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0202010000;", "0202010000;");
						contasProArquivo.put("0202010000;1", "0202010000;1");
						
						double cContas62ValorAtual = cContas62.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0202020000;", "0202020000;");
						contasProArquivo.put("0202020000;1", "0202020000;1");
						
						double cContas63ValorAtual = cContas63.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0202050000;", "0202050000;");
						contasProArquivo.put("0202050000;1", "0202050000;1");
						
						double cContas64ValorAtual = cContas64.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0202060000;", "0202060000;");
						contasProArquivo.put("0202060000;1", "0202060000;1");
						
						double cContas65ValorAtual = cContas65.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0102032000;", "0102032000;");
						contasProArquivo.put("0102032000;1", "0102032000;1");
						
						double cContas66ValorAtual = cContas66.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0103032000;", "0103032000;");
						contasProArquivo.put("0103032000;1", "0103032000;1");
						
						double cContas67ValorAtual = cContas67.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0102032100;", "0102032100;");
						contasProArquivo.put("0102032100;1", "0102032100;1");
						
						double cContas68ValorAtual = cContas68.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0103032100;", "0103032100;");
						contasProArquivo.put("0103032100;1", "0103032100;1");
						
						double cContas69ValorAtual = cContas69.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0106020000;", "0106020000;");
						contasProArquivo.put("0106020000;1", "0106020000;1");
						
						double cContas70ValorAtual = cContas70.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0525010700;", "0525010700;");
						contasProArquivo.put("0525010700;1", "0525010700;1");
						
						double cContas71ValorAtual = cContas71.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0525010800;", "0525010800;");
						contasProArquivo.put("0525010800;1", "0525010800;1");
						
						double cContas72ValorAtual = cContas72.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0525010400;", "0525010400;");
						contasProArquivo.put("0525010400;1", "0525010400;1");
						
						double cContas73ValorAtual = cContas73.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0102050000;", "0102050000;");
						contasProArquivo.put("0102050000;1", "0102050000;1");
						
						double cContas74ValorAtual = cContas74.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0102060000;", "0102060000;");
						contasProArquivo.put("0102060000;1", "0102060000;1");
						
						double cContas75ValorAtual = cContas75.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0104000000;", "0104000000;");
						contasProArquivo.put("0104000000;1", "0104000000;1");
						
						double cContas76ValorAtual = cContas76.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0105000000;", "0105000000;");
						contasProArquivo.put("0105000000;1", "0105000000;1");
						
						double cContas77ValorAtual = cContas77.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0109010000;", "0109010000;");
						contasProArquivo.put("0109010000;1", "0109010000;1");
						
						double cContas78ValorAtual = cContas78.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0109020000;", "0109020000;");
						contasProArquivo.put("0109020000;1", "0109020000;1");
						
						double cContas79ValorAtual = cContas79.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0109070000;", "0109070000;");
						contasProArquivo.put("0109070000;1", "0109070000;1");
						
						double cContas80ValorAtual = cContas80.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0109080000;", "0109080000;");
						contasProArquivo.put("0109080000;1", "0109080000;1");
						
						double cContas81ValorAtual = cContas81.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0206000000;", "0206000000;");
						contasProArquivo.put("0206000000;1", "0206000000;1");
						
						double cContas82ValorAtual = cContas82.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0210000000;", "0210000000;");
						contasProArquivo.put("0210000000;1", "0210000000;1");
						
						double cContas83ValorAtual = cContas83.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0211000000;", "0211000000;");
						contasProArquivo.put("0211000000;1", "0211000000;1");
						
						double cContas84ValorAtual = cContas84.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0205000000;", "0205000000;");
						contasProArquivo.put("0205000000;1", "0205000000;1");
						
						//double cContas85ValorAtual =
						// cContas85.obterTotalizacaoExistente(aseguradora,
						// mesAnoAtual);
						double cContas86ValorAtual = cContas86.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0214030000;", "0214030000;");
						contasProArquivo.put("0214030000;1", "0214030000;1");
						
						double cContas87ValorAtual = cContas87.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0214040000;", "0214040000;");
						contasProArquivo.put("0214040000;1", "0214040000;1");
						
						double cContas88ValorAtual = cContas88.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0109040000;", "0109040000;");
						contasProArquivo.put("0109040000;1", "0109040000;1");
						
						double cContas89ValorAtual = cContas89.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0109050000;", "0109050000;");
						contasProArquivo.put("0109050000;1", "0109050000;1");
						
						double cContas90ValorAtual = cContas90.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0109060000;", "0109060000;");
						contasProArquivo.put("0109060000;1", "0109060000;1");
						
						double cContas91ValorAtual = cContas91.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0211010200;", "0211010200;");
						contasProArquivo.put("0211010200;1", "0211010200;1");
						

						double cContas51ValorAnterior = cContas51.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0405020000;1", "0405020000;1");
						
						double cContas52ValorAnterior = cContas52.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0102010000;1", "0102010000;1");
						
						double cContas53ValorAnterior = cContas53.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0103010000;1", "0103010000;1");
						
						double cContas54ValorAnterior = cContas54.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0102020000;1", "0102020000;1");
						
						double cContas55ValorAnterior = cContas55.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0103020000;1", "0103020000;1");
						
						double cContas56ValorAnterior = cContas56.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas57ValorAnterior = cContas57.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas58ValorAnterior = cContas58.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas59ValorAnterior = cContas59.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas60ValorAnterior = cContas60.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas61ValorAnterior = cContas61.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas62ValorAnterior = cContas62.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas63ValorAnterior = cContas63.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas64ValorAnterior = cContas64.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas65ValorAnterior = cContas65.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas66ValorAnterior = cContas66.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas67ValorAnterior = cContas67.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas68ValorAnterior = cContas68.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas69ValorAnterior = cContas69.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas70ValorAnterior = cContas70.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas71ValorAnterior = cContas71.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas72ValorAnterior = cContas72.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas73ValorAnterior = cContas73.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas74ValorAnterior = cContas74.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas75ValorAnterior = cContas75.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas76ValorAnterior = cContas76.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas77ValorAnterior = cContas77.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas78ValorAnterior = cContas78.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas79ValorAnterior = cContas79.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas80ValorAnterior = cContas80.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas81ValorAnterior = cContas81.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas82ValorAnterior = cContas82.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas83ValorAnterior = cContas83.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas84ValorAnterior = cContas84.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						//double cContas85ValorAtual =
						// cContas85.obterTotalizacaoExistente(aseguradora,
						// mesAnoAtual);
						double cContas86ValorAnterior = cContas86.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas87ValorAnterior = cContas87.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas88ValorAnterior = cContas88.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas89ValorAnterior = cContas89.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas90ValorAnterior = cContas90.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						double cContas91ValorAnterior = cContas91.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);

						double totalDividendoRatio24 = (cContas2ValorAtual
								+ cContas3ValorAtual + cContas4ValorAtual + 
								cContas51ValorAtual
								+ cContas52ValorAtual + cContas53ValorAtual
								+ cContas54ValorAtual + cContas55ValorAtual
								+ cContas56ValorAtual + cContas57ValorAtual
								+ cContas58ValorAtual + cContas59ValorAtual
								+ cContas1ValorAtual + cContas60ValorAtual
								+ cContas39ValorAtual + cContas8ValorAtual
								+ cContas9ValorAtual + cContas10ValorAtual
								+ cContas11ValorAtual + cContas40ValorAtual
								+ cContas41ValorAtual + cContas42ValorAtual
								- cContas43ValorAtual - cContas13ValorAtual
								- cContas15ValorAtual - cContas16ValorAtual
								- cContas24ValorAtual - cContas44ValorAtual
								+ cContas39ValorAtual - cContas43ValorAtual
								+ cContas61ValorAtual + cContas62ValorAtual
								+ cContas63ValorAtual + cContas64ValorAtual
								+ cContas26ValorAtual + cContas65ValorAtual
								+ cContas66ValorAtual + cContas67ValorAtual
								+ cContas68ValorAtual + cContas69ValorAtual
								+ cContas39ValorAtual - conta48ValorAtual
								+ cContas70ValorAtual + cContas71ValorAtual
								+ cContas72ValorAtual + cContas49ValorAtual
								- cContas47ValorAtual - cContas17ValorAtual
								+ cContas73ValorAtual + cContas74ValorAtual
								+ cContas75ValorAtual + cContas76ValorAtual
								+ cContas77ValorAtual + cContas78ValorAtual
								+ cContas79ValorAtual + cContas80ValorAtual
								+ cContas81ValorAtual + cContas82ValorAtual
								+ cContas83ValorAtual + cContas6ValorAtual
								+ cContas7ValorAtual - cContas18ValorAtual
								- cContas12ValorAtual + cContas22ValorAtual
								+ cContas23ValorAtual + cContas84ValorAtual
								+ cContas81ValorAtual + cContas86ValorAtual
								+ cContas87ValorAtual + cContas5ValorAtual
								+ cContas88ValorAtual + cContas89ValorAtual
								+ cContas90ValorAtual + cContas72ValorAtual
								+ cContas91ValorAtual)

								- (cContas2ValorAnterior + cContas3ValorAnterior +
								cContas4ValorAnterior
								+ cContas51ValorAnterior
								+ cContas52ValorAnterior
								+ cContas53ValorAnterior
								+ cContas54ValorAnterior
								+ cContas55ValorAnterior
								+ cContas56ValorAnterior
								+ cContas57ValorAnterior
								+ cContas58ValorAnterior
								+ cContas59ValorAnterior
								+ cContas1ValorAnterior
								+ cContas60ValorAnterior
								+ cContas39ValorAnterior
								+ cContas8ValorAnterior + cContas9ValorAnterior
								+ cContas10ValorAnterior
								+ cContas11ValorAnterior
								+ cContas40ValorAnterior
								+ cContas41ValorAnterior
								+ cContas42ValorAnterior
								- cContas43ValorAnterior
								- cContas13ValorAnterior
								- cContas15ValorAnterior
								- cContas16ValorAnterior
								- cContas24ValorAnterior
								- cContas44ValorAnterior
								+ cContas39ValorAnterior
								- cContas43ValorAnterior
								+ cContas61ValorAnterior
								+ cContas62ValorAnterior
								+ cContas63ValorAnterior
								+ cContas64ValorAnterior
								+ cContas26ValorAnterior
								+ cContas65ValorAnterior
								+ cContas66ValorAnterior
								+ cContas67ValorAnterior
								+ cContas68ValorAnterior
								+ cContas69ValorAnterior
								+ cContas39ValorAnterior - conta48ValorAnterior
								+ cContas70ValorAnterior
								+ cContas71ValorAnterior
								+ cContas72ValorAnterior
								+ cContas49ValorAnterior
								- cContas47ValorAnterior
								- cContas17ValorAnterior
								+ cContas73ValorAnterior
								+ cContas74ValorAnterior
								+ cContas75ValorAnterior
								+ cContas76ValorAnterior
								+ cContas77ValorAnterior
								+ cContas78ValorAnterior
								+ cContas79ValorAnterior
								+ cContas80ValorAnterior
								+ cContas81ValorAnterior
								+ cContas82ValorAnterior
								+ cContas83ValorAnterior
								+ cContas6ValorAnterior + cContas7ValorAnterior
								- cContas18ValorAnterior
								- cContas12ValorAnterior
								+ cContas22ValorAnterior
								+ cContas23ValorAnterior
								+ cContas84ValorAnterior
								+ cContas81ValorAnterior
								+ cContas86ValorAnterior
								+ cContas87ValorAnterior
								+ cContas5ValorAnterior
								+ cContas88ValorAnterior
								+ cContas89ValorAnterior
								+ cContas90ValorAnterior
								+ cContas72ValorAnterior
								+ cContas91ValorAnterior);

						double totalDivisorRatio24 = cContas2ValorAnterior
								+ cContas3ValorAnterior + cContas4ValorAnterior
								+ cContas51ValorAnterior
								+ cContas52ValorAnterior
								+ cContas53ValorAnterior
								+ cContas54ValorAnterior
								+ cContas55ValorAnterior
								+ cContas56ValorAnterior
								+ cContas57ValorAnterior
								+ cContas58ValorAnterior
								+ cContas59ValorAnterior
								+ cContas1ValorAnterior
								+ cContas60ValorAnterior
								+ cContas39ValorAnterior
								+ cContas8ValorAnterior + cContas9ValorAnterior
								+ cContas10ValorAnterior
								+ cContas11ValorAnterior
								+ cContas40ValorAnterior
								+ cContas41ValorAnterior
								+ cContas42ValorAnterior
								- cContas43ValorAnterior
								- cContas13ValorAnterior
								- cContas15ValorAnterior
								- cContas16ValorAnterior
								- cContas24ValorAnterior
								- cContas44ValorAnterior
								+ cContas39ValorAnterior
								- cContas43ValorAnterior
								+ cContas61ValorAnterior
								+ cContas62ValorAnterior
								+ cContas63ValorAnterior
								+ cContas64ValorAnterior
								+ cContas26ValorAnterior
								+ cContas65ValorAnterior
								+ cContas66ValorAnterior
								+ cContas67ValorAnterior
								+ cContas68ValorAnterior
								+ cContas69ValorAnterior
								+ cContas39ValorAnterior - conta48ValorAnterior
								+ cContas70ValorAnterior
								+ cContas71ValorAnterior
								+ cContas72ValorAnterior
								+ cContas49ValorAnterior
								- cContas47ValorAnterior
								- cContas17ValorAnterior
								+ cContas73ValorAnterior
								+ cContas74ValorAnterior
								+ cContas75ValorAnterior
								+ cContas76ValorAnterior
								+ cContas77ValorAnterior
								+ cContas78ValorAnterior
								+ cContas79ValorAnterior
								+ cContas80ValorAnterior
								+ cContas81ValorAnterior
								+ cContas82ValorAnterior
								+ cContas83ValorAnterior
								+ cContas6ValorAnterior + cContas7ValorAnterior
								- cContas18ValorAnterior
								- cContas12ValorAnterior
								+ cContas22ValorAnterior
								+ cContas23ValorAnterior
								+ cContas84ValorAnterior
								+ cContas81ValorAnterior
								+ cContas86ValorAnterior
								+ cContas87ValorAnterior
								+ cContas5ValorAnterior
								+ cContas88ValorAnterior
								+ cContas89ValorAnterior
								+ cContas90ValorAnterior
								+ cContas72ValorAnterior
								+ cContas91ValorAnterior;

						double totalRatio24 = 0;

						if (totalDivisorRatio24 > 0)
							totalRatio24 = (totalDividendoRatio24 / totalDivisorRatio24) * 100;

						fileRatio.write("RATIO24Pri --> "
								+ cContas2.obterApelido() + " + "
								+ cContas3.obterApelido() + " + "
								+ cContas4.obterApelido() + " + "
								+ cContas51.obterApelido() + " + "
								+ cContas52.obterApelido() + " + "
								+ cContas53.obterApelido() + " + "
								+ cContas54.obterApelido() + " + "
								+ cContas55.obterApelido() + " + "
								+ cContas56.obterApelido() + " + "
								+ cContas57.obterApelido() + " + "
								+ cContas58.obterApelido() + " + "
								+ cContas59.obterApelido() + " + "
								+ cContas1.obterApelido() + " + "
								+ cContas60.obterApelido() + " + "
								+ cContas39.obterApelido() + " + "
								+ cContas8.obterApelido() + " + "
								+ cContas9.obterApelido() + " + "
								+ cContas10.obterApelido() + " + "
								+ cContas11.obterApelido() + " + "
								+ cContas40.obterApelido() + " + "
								+ cContas41.obterApelido() + " + "
								+ cContas42.obterApelido() + " - "
								+ cContas43.obterApelido() + " - "
								+ cContas13.obterApelido() + " - "
								+ cContas15.obterApelido() + " - "
								+ cContas16.obterApelido() + " - "
								+ cContas24.obterApelido() + " - "
								+ cContas44.obterApelido() + " + "
								+ cContas39.obterApelido() + " - "
								+ cContas43.obterApelido() + " + "
								+ cContas61.obterApelido() + " + "
								+ cContas62.obterApelido() + " + "
								+ cContas63.obterApelido() + " + "
								+ cContas64.obterApelido() + " + "
								+ cContas26.obterApelido() + " + "
								+ cContas65.obterApelido() + " + "
								+ cContas66.obterApelido() + " + "
								+ cContas67.obterApelido() + " + "
								+ cContas68.obterApelido() + " + "
								+ cContas69.obterApelido() + " + "
								+ cContas39.obterApelido() + " - "
								+ conta48.obterApelido() + " + "
								+ cContas70.obterApelido() + " + "
								+ cContas71.obterApelido() + " + "
								+ cContas72.obterApelido() + " + "
								+ cContas49.obterApelido() + " - "
								+ cContas47.obterApelido() + " - "
								+ cContas17.obterApelido() + " + "
								+ cContas73.obterApelido() + " + "
								+ cContas74.obterApelido() + " + "
								+ cContas75.obterApelido() + " + "
								+ cContas76.obterApelido() + " + "
								+ cContas77.obterApelido() + " + "
								+ cContas78.obterApelido() + " + "
								+ cContas79.obterApelido() + " + "
								+ cContas80.obterApelido() + " + "
								+ cContas81.obterApelido() + " + "
								+ cContas82.obterApelido() + " + "
								+ cContas83.obterApelido() + " + "
								+ cContas6.obterApelido() + " + "
								+ cContas7.obterApelido() + " - "
								+ cContas18.obterApelido() + " - "
								+ cContas12.obterApelido() + " + "
								+ cContas22.obterApelido() + " + "
								+ cContas23.obterApelido() + " + "
								+ cContas84.obterApelido() + " + "
								+ cContas81.obterApelido() + " + "
								+ cContas86.obterApelido() + " + "
								+ cContas87.obterApelido() + " + "
								+ cContas5.obterApelido() + " + "
								+ cContas88.obterApelido() + " + "
								+ cContas89.obterApelido() + " + "
								+ cContas90.obterApelido() + " + "
								+ cContas72.obterApelido() + " + "
								+ cContas91.obterApelido()

								+ " - " + cContas2.obterApelido() + "AnoAnt"
								+ " + " + cContas3.obterApelido() + "AnoAnt"
								+ " + " + cContas4.obterApelido() + "AnoAnt"
								+ " + " + cContas51.obterApelido() + "AnoAnt"
								+ " + " + cContas52.obterApelido() + "AnoAnt"
								+ " + " + cContas53.obterApelido() + "AnoAnt"
								+ " + " + cContas54.obterApelido() + "AnoAnt"
								+ " + " + cContas55.obterApelido() + "AnoAnt"
								+ " + " + cContas56.obterApelido() + "AnoAnt"
								+ " + " + cContas57.obterApelido() + "AnoAnt"
								+ " + " + cContas58.obterApelido() + "AnoAnt"
								+ " + " + cContas59.obterApelido() + "AnoAnt"
								+ " + " + cContas1.obterApelido() + "AnoAnt"
								+ " + " + cContas60.obterApelido() + "AnoAnt"
								+ " + " + cContas39.obterApelido() + "AnoAnt"
								+ " + " + cContas8.obterApelido() + "AnoAnt"
								+ " + " + cContas9.obterApelido() + "AnoAnt"
								+ " + " + cContas10.obterApelido() + "AnoAnt"
								+ " + " + cContas11.obterApelido() + "AnoAnt"
								+ " + " + cContas40.obterApelido() + "AnoAnt"
								+ " + " + cContas41.obterApelido() + "AnoAnt"
								+ " + " + cContas42.obterApelido() + "AnoAnt"
								+ " - " + cContas43.obterApelido() + "AnoAnt"
								+ " - " + cContas13.obterApelido() + "AnoAnt"
								+ " - " + cContas15.obterApelido() + "AnoAnt"
								+ " - " + cContas16.obterApelido() + "AnoAnt"
								+ " - " + cContas24.obterApelido() + "AnoAnt"
								+ " - " + cContas44.obterApelido() + "AnoAnt"
								+ " + " + cContas39.obterApelido() + "AnoAnt"
								+ " - " + cContas43.obterApelido() + "AnoAnt"
								+ " + " + cContas61.obterApelido() + "AnoAnt"
								+ " + " + cContas62.obterApelido() + "AnoAnt"
								+ " + " + cContas63.obterApelido() + "AnoAnt"
								+ " + " + cContas64.obterApelido() + "AnoAnt"
								+ " + " + cContas26.obterApelido() + "AnoAnt"
								+ " + " + cContas65.obterApelido() + "AnoAnt"
								+ " + " + cContas66.obterApelido() + "AnoAnt"
								+ " + " + cContas67.obterApelido() + "AnoAnt"
								+ " + " + cContas68.obterApelido() + "AnoAnt"
								+ " + " + cContas69.obterApelido() + "AnoAnt"
								+ " + " + cContas39.obterApelido() + "AnoAnt"
								+ " - " + conta48.obterApelido() + "AnoAnt"
								+ " + " + cContas70.obterApelido() + "AnoAnt"
								+ " + " + cContas71.obterApelido() + "AnoAnt"
								+ " + " + cContas72.obterApelido() + "AnoAnt"
								+ " + " + cContas49.obterApelido() + "AnoAnt"
								+ " - " + cContas47.obterApelido() + "AnoAnt"
								+ " - " + cContas17.obterApelido() + "AnoAnt"
								+ " + " + cContas73.obterApelido() + "AnoAnt"
								+ " + " + cContas74.obterApelido() + "AnoAnt"
								+ " + " + cContas75.obterApelido() + "AnoAnt"
								+ " + " + cContas76.obterApelido() + "AnoAnt"
								+ " + " + cContas77.obterApelido() + "AnoAnt"
								+ " + " + cContas78.obterApelido() + "AnoAnt"
								+ " + " + cContas79.obterApelido() + "AnoAnt"
								+ " + " + cContas80.obterApelido() + "AnoAnt"
								+ " + " + cContas81.obterApelido() + "AnoAnt"
								+ " + " + cContas82.obterApelido() + "AnoAnt"
								+ " + " + cContas83.obterApelido() + "AnoAnt"
								+ " + " + cContas6.obterApelido() + "AnoAnt"
								+ " + " + cContas7.obterApelido() + "AnoAnt"
								+ " - " + cContas18.obterApelido() + "AnoAnt"
								+ " - " + cContas12.obterApelido() + "AnoAnt"
								+ " + " + cContas22.obterApelido() + "AnoAnt"
								+ " + " + cContas23.obterApelido() + "AnoAnt"
								+ " + " + cContas84.obterApelido() + "AnoAnt"
								+ " + " + cContas81.obterApelido() + "AnoAnt"
								+ " + " + cContas86.obterApelido() + "AnoAnt"
								+ " + " + cContas87.obterApelido() + "AnoAnt"
								+ " + " + cContas5.obterApelido() + "AnoAnt"
								+ " + " + cContas88.obterApelido() + "AnoAnt"
								+ " + " + cContas89.obterApelido() + "AnoAnt"
								+ " + " + cContas90.obterApelido() + "AnoAnt"
								+ " + " + cContas72.obterApelido() + "AnoAnt"
								+ " + " + cContas91.obterApelido() + "AnoAnt"
								+ ": " + formataValor.format(totalDividendoRatio24) + "\r\n");

						fileRatio.write("RATIO24Seg -->"
								+ cContas2.obterApelido() + "AnoAnt" + " + "
								+ cContas3.obterApelido() + "AnoAnt" + " + "
								+ cContas4.obterApelido() + "AnoAnt" + " + "
								+ cContas51.obterApelido() + "AnoAnt" + " + "
								+ cContas52.obterApelido() + "AnoAnt" + " + "
								+ cContas53.obterApelido() + "AnoAnt" + " + "
								+ cContas54.obterApelido() + "AnoAnt" + " + "
								+ cContas55.obterApelido() + "AnoAnt" + " + "
								+ cContas56.obterApelido() + "AnoAnt" + " + "
								+ cContas57.obterApelido() + "AnoAnt" + " + "
								+ cContas58.obterApelido() + "AnoAnt" + " + "
								+ cContas59.obterApelido() + "AnoAnt" + " + "
								+ cContas1.obterApelido() + "AnoAnt" + " + "
								+ cContas60.obterApelido() + "AnoAnt" + " + "
								+ cContas39.obterApelido() + "AnoAnt" + " + "
								+ cContas8.obterApelido() + "AnoAnt" + " + "
								+ cContas9.obterApelido() + "AnoAnt" + " + "
								+ cContas10.obterApelido() + "AnoAnt" + " + "
								+ cContas11.obterApelido() + "AnoAnt" + " + "
								+ cContas40.obterApelido() + "AnoAnt" + " + "
								+ cContas41.obterApelido() + "AnoAnt" + " + "
								+ cContas42.obterApelido() + "AnoAnt" + " - "
								+ cContas43.obterApelido() + "AnoAnt" + " - "
								+ cContas13.obterApelido() + "AnoAnt" + " - "
								+ cContas15.obterApelido() + "AnoAnt" + " - "
								+ cContas16.obterApelido() + "AnoAnt" + " - "
								+ cContas24.obterApelido() + "AnoAnt" + " - "
								+ cContas44.obterApelido() + "AnoAnt" + " + "
								+ cContas39.obterApelido() + "AnoAnt" + " - "
								+ cContas43.obterApelido() + "AnoAnt" + " + "
								+ cContas61.obterApelido() + "AnoAnt" + " + "
								+ cContas62.obterApelido() + "AnoAnt" + " + "
								+ cContas63.obterApelido() + "AnoAnt" + " + "
								+ cContas64.obterApelido() + "AnoAnt" + " + "
								+ cContas26.obterApelido() + "AnoAnt" + " + "
								+ cContas65.obterApelido() + "AnoAnt" + " + "
								+ cContas66.obterApelido() + "AnoAnt" + " + "
								+ cContas67.obterApelido() + "AnoAnt" + " + "
								+ cContas68.obterApelido() + "AnoAnt" + " + "
								+ cContas69.obterApelido() + "AnoAnt" + " + "
								+ cContas39.obterApelido() + "AnoAnt" + " - "
								+ conta48.obterApelido() + "AnoAnt" + " + "
								+ cContas70.obterApelido() + "AnoAnt" + " + "
								+ cContas71.obterApelido() + "AnoAnt" + " + "
								+ cContas72.obterApelido() + "AnoAnt" + " + "
								+ cContas49.obterApelido() + "AnoAnt" + " - "
								+ cContas47.obterApelido() + "AnoAnt" + " - "
								+ cContas17.obterApelido() + "AnoAnt" + " + "
								+ cContas73.obterApelido() + "AnoAnt" + " + "
								+ cContas74.obterApelido() + "AnoAnt" + " + "
								+ cContas75.obterApelido() + "AnoAnt" + " + "
								+ cContas76.obterApelido() + "AnoAnt" + " + "
								+ cContas77.obterApelido() + "AnoAnt" + " + "
								+ cContas78.obterApelido() + "AnoAnt" + " + "
								+ cContas79.obterApelido() + "AnoAnt" + " + "
								+ cContas80.obterApelido() + "AnoAnt" + " + "
								+ cContas81.obterApelido() + "AnoAnt" + " + "
								+ cContas82.obterApelido() + "AnoAnt" + " + "
								+ cContas83.obterApelido() + "AnoAnt" + " + "
								+ cContas6.obterApelido() + "AnoAnt" + " + "
								+ cContas7.obterApelido() + "AnoAnt" + " - "
								+ cContas18.obterApelido() + "AnoAnt" + " - "
								+ cContas12.obterApelido() + "AnoAnt" + " + "
								+ cContas22.obterApelido() + "AnoAnt" + " + "
								+ cContas23.obterApelido() + "AnoAnt" + " + "
								+ cContas84.obterApelido() + "AnoAnt" + " + "
								+ cContas81.obterApelido() + "AnoAnt" + " + "
								+ cContas86.obterApelido() + "AnoAnt" + " + "
								+ cContas87.obterApelido() + "AnoAnt" + " + "
								+ cContas5.obterApelido() + "AnoAnt" + " + "
								+ cContas88.obterApelido() + "AnoAnt" + " + "
								+ cContas89.obterApelido() + "AnoAnt" + " + "
								+ cContas90.obterApelido() + "AnoAnt" + " + "
								+ cContas72.obterApelido() + "AnoAnt" + " + "
								+ cContas91.obterApelido() + "AnoAnt" + ": "
								+ formataValor.format(totalDivisorRatio24) + "\r\n");

						fileRatio.write("RATIO24Result: (RATIO24Pri / RATIO24Seg)*100: "
										+ formataValor.format(totalRatio24) + "\r\n");

						System.out.println("RATIO24Pri --> "
								+ cContas2.obterApelido() + " + "
								+ cContas3.obterApelido() + " + "
								+ cContas4.obterApelido() + " + "
								+ cContas51.obterApelido() + " + "
								+ cContas52.obterApelido() + " + "
								+ cContas53.obterApelido() + " + "
								+ cContas54.obterApelido() + " + "
								+ cContas91.obterApelido() + " + "
								+ cContas56.obterApelido() + " + "
								+ cContas57.obterApelido() + " + "
								+ cContas58.obterApelido() + " + "
								+ cContas59.obterApelido() + " + "
								+ cContas1.obterApelido() + " + "
								+ cContas60.obterApelido() + " + "
								+ cContas39.obterApelido() + " + "
								+ cContas8.obterApelido() + " + "
								+ cContas9.obterApelido() + " + "
								+ cContas10.obterApelido() + " + "
								+ cContas11.obterApelido() + " + "
								+ cContas40.obterApelido() + " + "
								+ cContas41.obterApelido() + " + "
								+ cContas42.obterApelido() + " - "
								+ cContas43.obterApelido() + " - "
								+ cContas13.obterApelido() + " - "
								+ cContas15.obterApelido() + " - "
								+ cContas16.obterApelido() + " - "
								+ cContas24.obterApelido() + " - "
								+ cContas44.obterApelido() + " + "
								+ cContas39.obterApelido() + " - "
								+ cContas43.obterApelido() + " + "
								+ cContas61.obterApelido() + " + "
								+ cContas62.obterApelido() + " + "
								+ cContas63.obterApelido() + " + "
								+ cContas64.obterApelido() + " + "
								+ cContas26.obterApelido() + " + "
								+ cContas65.obterApelido() + " + "
								+ cContas66.obterApelido() + " + "
								+ cContas67.obterApelido() + " + "
								+ cContas68.obterApelido() + " + "
								+ cContas69.obterApelido() + " + "
								+ cContas39.obterApelido() + " - "
								+ conta48.obterApelido() + " + "
								+ cContas70.obterApelido() + " + "
								+ cContas71.obterApelido() + " + "
								+ cContas72.obterApelido() + " + "
								+ cContas49.obterApelido() + " - "
								+ cContas47.obterApelido() + " - "
								+ cContas17.obterApelido() + " + "
								+ cContas73.obterApelido() + " + "
								+ cContas74.obterApelido() + " + "
								+ cContas75.obterApelido() + " + "
								+ cContas76.obterApelido() + " + "
								+ cContas77.obterApelido() + " + "
								+ cContas78.obterApelido() + " + "
								+ cContas79.obterApelido() + " + "
								+ cContas80.obterApelido() + " + "
								+ cContas81.obterApelido() + " + "
								+ cContas82.obterApelido() + " + "
								+ cContas83.obterApelido() + " + "
								+ cContas6.obterApelido() + " + "
								+ cContas7.obterApelido() + " - "
								+ cContas18.obterApelido() + " - "
								+ cContas12.obterApelido() + " + "
								+ cContas22.obterApelido() + " + "
								+ cContas23.obterApelido() + " + "
								+ cContas84.obterApelido() + " + "
								+ cContas81.obterApelido() + " + "
								+ cContas86.obterApelido() + " + "
								+ cContas87.obterApelido() + " + "
								+ cContas5.obterApelido() + " + "
								+ cContas88.obterApelido() + " + "
								+ cContas89.obterApelido() + " + "
								+ cContas90.obterApelido() + " + "
								+ cContas72.obterApelido() + " + "
								+ cContas91.obterApelido()

								+ " - " + cContas2.obterApelido() + "AnoAnt"
								+ " + " + cContas3.obterApelido() + "AnoAnt"
								+ " + " + cContas4.obterApelido() + "AnoAnt"
								+ " + " + cContas51.obterApelido() + "AnoAnt"
								+ " + " + cContas52.obterApelido() + "AnoAnt"
								+ " + " + cContas53.obterApelido() + "AnoAnt"
								+ " + " + cContas54.obterApelido() + "AnoAnt"
								+ " + " + cContas91.obterApelido() + "AnoAnt"
								+ " + " + cContas56.obterApelido() + "AnoAnt"
								+ " + " + cContas57.obterApelido() + "AnoAnt"
								+ " + " + cContas58.obterApelido() + "AnoAnt"
								+ " + " + cContas59.obterApelido() + "AnoAnt"
								+ " + " + cContas1.obterApelido() + "AnoAnt"
								+ " + " + cContas60.obterApelido() + "AnoAnt"
								+ " + " + cContas39.obterApelido() + "AnoAnt"
								+ " + " + cContas8.obterApelido() + "AnoAnt"
								+ " + " + cContas9.obterApelido() + "AnoAnt"
								+ " + " + cContas10.obterApelido() + "AnoAnt"
								+ " + " + cContas11.obterApelido() + "AnoAnt"
								+ " + " + cContas40.obterApelido() + "AnoAnt"
								+ " + " + cContas41.obterApelido() + "AnoAnt"
								+ " + " + cContas42.obterApelido() + "AnoAnt"
								+ " - " + cContas43.obterApelido() + "AnoAnt"
								+ " - " + cContas13.obterApelido() + "AnoAnt"
								+ " - " + cContas15.obterApelido() + "AnoAnt"
								+ " - " + cContas16.obterApelido() + "AnoAnt"
								+ " - " + cContas24.obterApelido() + "AnoAnt"
								+ " - " + cContas44.obterApelido() + "AnoAnt"
								+ " + " + cContas39.obterApelido() + "AnoAnt"
								+ " - " + cContas43.obterApelido() + "AnoAnt"
								+ " + " + cContas61.obterApelido() + "AnoAnt"
								+ " + " + cContas62.obterApelido() + "AnoAnt"
								+ " + " + cContas63.obterApelido() + "AnoAnt"
								+ " + " + cContas64.obterApelido() + "AnoAnt"
								+ " + " + cContas26.obterApelido() + "AnoAnt"
								+ " + " + cContas65.obterApelido() + "AnoAnt"
								+ " + " + cContas66.obterApelido() + "AnoAnt"
								+ " + " + cContas67.obterApelido() + "AnoAnt"
								+ " + " + cContas68.obterApelido() + "AnoAnt"
								+ " + " + cContas69.obterApelido() + "AnoAnt"
								+ " + " + cContas39.obterApelido() + "AnoAnt"
								+ " - " + conta48.obterApelido() + "AnoAnt"
								+ " + " + cContas70.obterApelido() + "AnoAnt"
								+ " + " + cContas71.obterApelido() + "AnoAnt"
								+ " + " + cContas72.obterApelido() + "AnoAnt"
								+ " + " + cContas49.obterApelido() + "AnoAnt"
								+ " - " + cContas47.obterApelido() + "AnoAnt"
								+ " - " + cContas17.obterApelido() + "AnoAnt"
								+ " + " + cContas73.obterApelido() + "AnoAnt"
								+ " + " + cContas74.obterApelido() + "AnoAnt"
								+ " + " + cContas75.obterApelido() + "AnoAnt"
								+ " + " + cContas76.obterApelido() + "AnoAnt"
								+ " + " + cContas77.obterApelido() + "AnoAnt"
								+ " + " + cContas78.obterApelido() + "AnoAnt"
								+ " + " + cContas79.obterApelido() + "AnoAnt"
								+ " + " + cContas80.obterApelido() + "AnoAnt"
								+ " + " + cContas81.obterApelido() + "AnoAnt"
								+ " + " + cContas82.obterApelido() + "AnoAnt"
								+ " + " + cContas83.obterApelido() + "AnoAnt"
								+ " + " + cContas6.obterApelido() + "AnoAnt"
								+ " + " + cContas7.obterApelido() + "AnoAnt"
								+ " - " + cContas18.obterApelido() + "AnoAnt"
								+ " - " + cContas12.obterApelido() + "AnoAnt"
								+ " + " + cContas22.obterApelido() + "AnoAnt"
								+ " + " + cContas23.obterApelido() + "AnoAnt"
								+ " + " + cContas84.obterApelido() + "AnoAnt"
								+ " + " + cContas81.obterApelido() + "AnoAnt"
								+ " + " + cContas86.obterApelido() + "AnoAnt"
								+ " + " + cContas87.obterApelido() + "AnoAnt"
								+ " + " + cContas5.obterApelido() + "AnoAnt"
								+ " + " + cContas88.obterApelido() + "AnoAnt"
								+ " + " + cContas89.obterApelido() + "AnoAnt"
								+ " + " + cContas90.obterApelido() + "AnoAnt"
								+ " + " + cContas72.obterApelido() + "AnoAnt"
								+ " + " + cContas91.obterApelido() + "AnoAnt"
								+ ": " + formataValor.format(totalDividendoRatio24));

						System.out.println("RATIO24Seg -->"
								+ cContas2.obterApelido() + "AnoAnt" + " + "
								+ cContas3.obterApelido() + "AnoAnt" + " + "
								+ cContas4.obterApelido() + "AnoAnt" + " + "
								+ cContas51.obterApelido() + "AnoAnt" + " + "
								+ cContas52.obterApelido() + "AnoAnt" + " + "
								+ cContas53.obterApelido() + "AnoAnt" + " + "
								+ cContas54.obterApelido() + "AnoAnt" + " + "
								+ cContas91.obterApelido() + "AnoAnt" + " + "
								+ cContas56.obterApelido() + "AnoAnt" + " + "
								+ cContas57.obterApelido() + "AnoAnt" + " + "
								+ cContas58.obterApelido() + "AnoAnt" + " + "
								+ cContas59.obterApelido() + "AnoAnt" + " + "
								+ cContas1.obterApelido() + "AnoAnt" + " + "
								+ cContas60.obterApelido() + "AnoAnt" + " + "
								+ cContas39.obterApelido() + "AnoAnt" + " + "
								+ cContas8.obterApelido() + "AnoAnt" + " + "
								+ cContas9.obterApelido() + "AnoAnt" + " + "
								+ cContas10.obterApelido() + "AnoAnt" + " + "
								+ cContas11.obterApelido() + "AnoAnt" + " + "
								+ cContas40.obterApelido() + "AnoAnt" + " + "
								+ cContas41.obterApelido() + "AnoAnt" + " + "
								+ cContas42.obterApelido() + "AnoAnt" + " - "
								+ cContas43.obterApelido() + "AnoAnt" + " - "
								+ cContas13.obterApelido() + "AnoAnt" + " - "
								+ cContas15.obterApelido() + "AnoAnt" + " - "
								+ cContas16.obterApelido() + "AnoAnt" + " - "
								+ cContas24.obterApelido() + "AnoAnt" + " - "
								+ cContas44.obterApelido() + "AnoAnt" + " + "
								+ cContas39.obterApelido() + "AnoAnt" + " - "
								+ cContas43.obterApelido() + "AnoAnt" + " + "
								+ cContas61.obterApelido() + "AnoAnt" + " + "
								+ cContas62.obterApelido() + "AnoAnt" + " + "
								+ cContas63.obterApelido() + "AnoAnt" + " + "
								+ cContas64.obterApelido() + "AnoAnt" + " + "
								+ cContas26.obterApelido() + "AnoAnt" + " + "
								+ cContas65.obterApelido() + "AnoAnt" + " + "
								+ cContas66.obterApelido() + "AnoAnt" + " + "
								+ cContas67.obterApelido() + "AnoAnt" + " + "
								+ cContas68.obterApelido() + "AnoAnt" + " + "
								+ cContas69.obterApelido() + "AnoAnt" + " + "
								+ cContas39.obterApelido() + "AnoAnt" + " - "
								+ conta48.obterApelido() + "AnoAnt" + " + "
								+ cContas70.obterApelido() + "AnoAnt" + " + "
								+ cContas71.obterApelido() + "AnoAnt" + " + "
								+ cContas72.obterApelido() + "AnoAnt" + " + "
								+ cContas49.obterApelido() + "AnoAnt" + " - "
								+ cContas47.obterApelido() + "AnoAnt" + " - "
								+ cContas17.obterApelido() + "AnoAnt" + " + "
								+ cContas73.obterApelido() + "AnoAnt" + " + "
								+ cContas74.obterApelido() + "AnoAnt" + " + "
								+ cContas75.obterApelido() + "AnoAnt" + " + "
								+ cContas76.obterApelido() + "AnoAnt" + " + "
								+ cContas77.obterApelido() + "AnoAnt" + " + "
								+ cContas78.obterApelido() + "AnoAnt" + " + "
								+ cContas79.obterApelido() + "AnoAnt" + " + "
								+ cContas80.obterApelido() + "AnoAnt" + " + "
								+ cContas81.obterApelido() + "AnoAnt" + " + "
								+ cContas82.obterApelido() + "AnoAnt" + " + "
								+ cContas83.obterApelido() + "AnoAnt" + " + "
								+ cContas6.obterApelido() + "AnoAnt" + " + "
								+ cContas7.obterApelido() + "AnoAnt" + " - "
								+ cContas18.obterApelido() + "AnoAnt" + " - "
								+ cContas12.obterApelido() + "AnoAnt" + " + "
								+ cContas22.obterApelido() + "AnoAnt" + " + "
								+ cContas23.obterApelido() + "AnoAnt" + " + "
								+ cContas84.obterApelido() + "AnoAnt" + " + "
								+ cContas81.obterApelido() + "AnoAnt" + " + "
								+ cContas86.obterApelido() + "AnoAnt" + " + "
								+ cContas87.obterApelido() + "AnoAnt" + " + "
								+ cContas5.obterApelido() + "AnoAnt" + " + "
								+ cContas88.obterApelido() + "AnoAnt" + " + "
								+ cContas89.obterApelido() + "AnoAnt" + " + "
								+ cContas90.obterApelido() + "AnoAnt" + " + "
								+ cContas72.obterApelido() + "AnoAnt" + " + "
								+ cContas91.obterApelido() + "AnoAnt" + ": "
								+ formataValor.format(totalDivisorRatio24));

						System.out
								.println("RATIO24Result: (RATIO24Pri / RATIO24Seg)*100: "
										+ formataValor.format(totalRatio24));

						System.out.println("totalRatio24: " + totalRatio24);

						if (totalRatio24 >= 0 && totalRatio24 <= 33)
						{
							ponderacao += 1;
							//file.write("Ponderacao24: " + ponderacao  + "\r\n");
							ponderacaoStr+="Ponderacao24: " + ponderacao + "\r\n";
							//file.write("----------------------------------------------------------------\r\n");
						}

						//			CONTAS PRA FAZER O CÁLCULO do RATIOS 25
						ClassificacaoContas cContas92 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0108010000");
						ClassificacaoContas cContas93 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0303020100");
						ClassificacaoContas cContas94 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0107010000");
						ClassificacaoContas cContas95 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0303020200");
						ClassificacaoContas cContas96 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0107060000");

						//			Calculo do Indicador RATIO 25
						double cContas92ValorAtual = cContas92.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0108010000;", "0108010000;");
						double cContas92ValorAnterior = cContas92.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0108010000;1", "0108010000;1");
						contasProArquivo.put("0108010000;2", "0108010000;2");
						
						double cContas93ValorAtual = cContas93.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0303020100;", "0303020100;");
						double cContas93ValorAnterior = cContas93.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0303020100;1", "0303020100;1");
						contasProArquivo.put("0303020100;2", "0303020100;2");
						
						double cContas94ValorAtual = cContas94.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0107010000;", "0107010000;");
						double cContas94ValorAnterior = cContas94.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0107010000;1", "0107010000;1");
						contasProArquivo.put("0107010000;2", "0107010000;2");
						
						double cContas95ValorAtual = cContas95.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0303020200;", "0303020200;");
						double cContas95ValorAnterior = cContas95.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0303020200;1", "0303020200;1");
						contasProArquivo.put("0303020200;2", "0303020200;2");
						
						double cContas96ValorAtual = cContas96.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						contasProArquivo.put("0107060000;", "0107060000;");
						double cContas96ValorAnterior = cContas96.obterTotalizacaoExistente(aseguradora,mesAnoAnterior);
						contasProArquivo.put("0107060000;1", "0107060000;1");
						contasProArquivo.put("0107060000;2", "0107060000;2");

						double cContas92ValorAnterior2Anos = cContas92.obterTotalizacaoExistente(aseguradora, mesDoisAnosAnterior);
						double cContas93ValorAnterior2Anos = cContas93.obterTotalizacaoExistente(aseguradora, mesDoisAnosAnterior);
						double cContas96ValorAnterior2Anos = cContas96.obterTotalizacaoExistente(aseguradora, mesDoisAnosAnterior);
						double cContas94ValorAnterior2Anos = cContas94.obterTotalizacaoExistente(aseguradora, mesDoisAnosAnterior);
						double cContas95ValorAnterior2Anos = cContas95.obterTotalizacaoExistente(aseguradora, mesDoisAnosAnterior);
						
						double totalDividendoRatio25 = 
								((cContas92ValorAtual + cContas92ValorAnterior 
								+ cContas70ValorAtual
								- cContas93ValorAtual + cContas93ValorAnterior)
								- (cContas96ValorAtual - cContas96ValorAnterior)
								+ (cContas94ValorAtual - cContas94ValorAnterior)
								+ cContas34ValorAtual 
								- cContas36ValorAtual
								+ cContas95ValorAtual - cContas95ValorAnterior)

								- ((cContas92ValorAnterior + cContas92ValorAnterior2Anos
								+ cContas70ValorAnterior
								- cContas93ValorAnterior + cContas93ValorAnterior2Anos)
								- (cContas96ValorAnterior - cContas96ValorAnterior2Anos)
								+ (cContas94ValorAnterior - cContas94ValorAnterior2Anos)
								+ cContas34ValorAnterior
								- cContas36ValorAnterior
								+ cContas95ValorAnterior - cContas95ValorAnterior2Anos);

						double totalDivisorRatio25 =
								((cContas92ValorAnterior + cContas92ValorAnterior2Anos
								+ cContas70ValorAnterior
								- cContas93ValorAnterior + cContas93ValorAnterior2Anos)
								- (cContas96ValorAnterior - cContas96ValorAnterior2Anos)
								+ (cContas94ValorAnterior - cContas94ValorAnterior2Anos)
								+ cContas34ValorAnterior
								- cContas36ValorAnterior
								+ cContas95ValorAnterior - cContas95ValorAnterior2Anos);

						double totalRatio25 = 0;

						if (totalDivisorRatio25 > 0)
							totalRatio25 = (totalDividendoRatio25 / totalDivisorRatio25) * 100;

						System.out.println("totalRatio25: " + totalRatio25);

						fileRatio.write("RATIO25Pri --> "
								+ cContas92.obterApelido() + " + "
								+ cContas92.obterApelido() + "AnoAnt" + " + "
								+ cContas70.obterApelido() + " - "
								+ cContas93.obterApelido() + " + "
								+ cContas93.obterApelido() + "AnoAnt" + " - "
								+ cContas96.obterApelido() + " - "
								+ cContas96.obterApelido() + "AnoAnt" + " + "
								+ cContas94.obterApelido() + " - "
								+ cContas94.obterApelido() + "AnoAnt" + " + "
								+ cContas34.obterApelido() + " - "
								+ cContas36.obterApelido() + " + "
								+ cContas95.obterApelido() + " - "
								+ cContas95.obterApelido() + "AnoAnt" + ": "
								+ formataValor.format(totalDividendoRatio25) + "\r\n");

						fileRatio.write("RATIO25Seg -->"
								+ cContas92.obterApelido() + "AnoAnt" + " + "
								+ cContas70.obterApelido() + "AnoAnt" + " - "
								+ cContas93.obterApelido() + "AnoAnt" + " - "
								+ cContas96.obterApelido() + "AnoAnt" + " - "
								+ cContas94.obterApelido() + "AnoAnt" + " - "
								+ cContas34.obterApelido() + "AnoAnt" + " - "
								+ cContas36.obterApelido() + "AnoAnt" + " + "
								+ cContas95.obterApelido() + "AnoAnt" + ": "
								+ formataValor.format(totalDivisorRatio25) + "\r\n");

						fileRatio
								.write("RATIO25Result: (RATIO25Pri / RATIO25Seg)*100: "
										+ formataValor.format(totalRatio25) + "\r\n");

						System.out.println("RATIO25Pri --> "
								+ cContas92.obterApelido() + " + "
								+ cContas92.obterApelido() + "AnoAnt" + " + "
								+ cContas70.obterApelido() + " - "
								+ cContas93.obterApelido() + " + "
								+ cContas93.obterApelido() + "AnoAnt" + " - "
								+ cContas96.obterApelido() + " - "
								+ cContas96.obterApelido() + "AnoAnt" + " + "
								+ cContas94.obterApelido() + " - "
								+ cContas94.obterApelido() + "AnoAnt" + " + "
								+ cContas34.obterApelido() + " - "
								+ cContas36.obterApelido() + " + "
								+ cContas95.obterApelido() + " - "
								+ cContas95.obterApelido() + "AnoAnt" + ": "
								+ formataValor.format(totalDividendoRatio25));

						System.out.println("RATIO25Seg -->"
								+ cContas92.obterApelido() + "AnoAnt" + " + "
								+ cContas70.obterApelido() + "AnoAnt" + " - "
								+ cContas93.obterApelido() + "AnoAnt" + " - "
								+ cContas96.obterApelido() + "AnoAnt" + " - "
								+ cContas94.obterApelido() + "AnoAnt" + " - "
								+ cContas34.obterApelido() + "AnoAnt" + " - "
								+ cContas36.obterApelido() + "AnoAnt" + " + "
								+ cContas95.obterApelido() + "AnoAnt" + ": "
								+ formataValor.format(totalDivisorRatio25));

						System.out
								.println("RATIO25Result: (RATIO25Pri / RATIO25Seg)*100: "
										+ totalRatio25);

						if (totalRatio25 >= 0 && totalRatio25 <= 33)
						{
							ponderacao += 1;
							//file.write("Ponderacao25: " + ponderacao + "\r\n");
							ponderacaoStr+="Ponderacao25: " + ponderacao + "\r\n";
						}
						
						System.out.println("Ponderacao: " + ponderacao);

						double totalPonderacao = (ponderacao * 30) / 59;

						fileRatio.write("Valor Final da Ponderação (ponderacao * 30) / 59: " + formataValor.format(totalPonderacao)+ "\r\n");
						
						ponderacaoStr+="Valor Final da Ponderação (ponderacao * 30) / 59: " + formataValor.format(totalPonderacao) + "\r\n";

						System.out.println("Valor Final da Ponderação: " + totalPonderacao);

						fileRatio
								.write("------------------------------------------------------------------------------------------"
										+ "\r\n");

						MeicosCalculo calculo3 = (MeicosCalculo) mm
								.getEntity("MeicosCalculo");

						calculo3.atribuirOrigem(meicos.obterOrigem());
						calculo3.atribuirDestino(meicos.obterDestino());
						calculo3.atribuirResponsavel(usuarioAtual);
						calculo3.atribuirSuperior(meicos);
						calculo3.atribuirTitulo("Calculo de Meicos");
						calculo3.atribuirTipo("Ratios Financeiros");
						calculo3.atribuirValor(totalPonderacao);
						calculo3.atribuirDataPrevistaInicio(new Date());
						calculo3.atribuirDataPrevistaConclusao(new Date());
						calculos.add(calculo3);
						
						this.gerarArquivosContas(contasProArquivo, aseguradora, mesAnoAnterior, mesDoisAnosAnterior, mesAnoCalculo);
						
					} else { // Otros Indicadores
						//****************************************************************************************
						// Calculo da Planilha 1

						Parametro parametro = (Parametro) entidadeHome.obterEntidadePorApelido("parametros");

						System.out.println("Ramo: " + ramo);

						double valorMinimo = 0;
						
						double A4 = 0;
						
						double A30 = 0; 

						NumberFormat nf = NumberFormat.getInstance();
						
						boolean ramoVida = false;
						boolean ramoPatrimonial = false;
						boolean ramoVidaePatrimonial = false;
						

						if (ramo != null) 
						{
							if (ramo.equals("PATRIMONIALES")) 
							{
								ramoPatrimonial = true;
									
								String valorMinimoPatrimonialStr = parametro.obterAtributo("valorminimopatrimonial").obterValor();

								if (valorMinimoPatrimonialStr != null) 
								{
									//valorMinimoPatrimonialStr =
									// valorMinimoPatrimonialStr.replaceAll(",",
									// ".");

									System.out.println("valorMinimoPatrimonialStr: "+ valorMinimoPatrimonialStr);

									valorMinimo = Double.parseDouble(valorMinimoPatrimonialStr);
									A4 = Double.parseDouble(valorMinimoPatrimonialStr);
									//A30 = Double.parseDouble(valorMinimoPatrimonialStr);
								}
							}
							else if (ramo.equals("PATRIMONIALES Y VIDA")) 
							{
								ramoVidaePatrimonial = true;
								
								String valorMinimoVidaPatrimonialStr = parametro.obterAtributo("valorminimovidapatrimonial").obterValor();
								String valorMinimoPatrimonialStr = parametro.obterAtributo("valorminimopatrimonial").obterValor();
								
								if(valorMinimoPatrimonialStr!=null)
									A4 = Double.parseDouble(valorMinimoPatrimonialStr);
								
								String valorMinimoVidaStr = parametro.obterAtributo("valorminimovida").obterValor();
								if(valorMinimoVidaStr!=null)
										A30 = Double.parseDouble(valorMinimoVidaStr);
								
								if (valorMinimoVidaPatrimonialStr != null) 
								{
									//valorMinimoVidaPatrimonialStr =
									// valorMinimoVidaPatrimonialStr.replaceAll(",",
									// ".");

									System.out.println("valorMinimoVidaPatrimonialStr: "+ valorMinimoVidaPatrimonialStr);

									valorMinimo = Double.parseDouble(valorMinimoVidaPatrimonialStr);
								}
							} 
							else
							{
								ramoVida = true;
								
								String valorMinimoVidaStr = parametro.obterAtributo("valorminimovida").obterValor();
								
								if (valorMinimoVidaStr != null) 
								{
									//valorMinimoVidaStr =
									// valorMinimoVidaStr.replaceAll(",", ".");

									System.out.println("valorMinimoVidaStr: "+ valorMinimoVidaStr);

									valorMinimo = Double.parseDouble(valorMinimoVidaStr);
									//A4 = Double.parseDouble(valorMinimoVidaStr);
									A30 = Double.parseDouble(valorMinimoVidaStr);
								}
							}
						}

						System.out.println("Valor Minimo1: " + valorMinimo);

						System.out.println("Valor Minimo2: " + valorMinimo
								/ 1000);

						//A2
						ClassificacaoContas cContas1Plan1 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0301010000");
						ClassificacaoContas cContas2Plan1 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0301020000");
						ClassificacaoContas cContas3Plan1 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0302010000");
						ClassificacaoContas cContas4Plan1 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0303010000");
						ClassificacaoContas cContas5Plan1 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0303020000");
						ClassificacaoContas cContas6Plan1 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0304000000");
						ClassificacaoContas cContas7Plan1 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0305010000");

						RatioPermanente ratioPermanente = aseguradora.obterRatioPermanente();

						double valorConta1Plan1 = cContas1Plan1
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);
						double valorConta2Plan1 = cContas2Plan1
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);
						double valorConta3Plan1 = cContas3Plan1
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);
						double valorConta4Plan1 = cContas4Plan1
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);
						double valorConta5Plan1 = cContas5Plan1
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);
						double valorConta6Plan1 = cContas6Plan1
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);
						double valorConta7Plan1 = cContas7Plan1
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);

						double A2 = valorConta1Plan1 + valorConta2Plan1
								+ valorConta3Plan1 + valorConta4Plan1
								+ valorConta5Plan1 + valorConta6Plan1
								+ valorConta7Plan1;

						//A3

						double valorRatio1 = 0;
						double valorRatio2 = 0;

						ClassificacaoContas cContas8Plan1 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0109010000");
						Conta cContas9Plan1 = (Conta) entidadeHome
								.obterEntidadePorApelido("0104010201");
						Conta cContas10Plan1 = (Conta) entidadeHome
								.obterEntidadePorApelido("0107010301");
						Conta cContas11Plan1 = (Conta) entidadeHome
								.obterEntidadePorApelido("0107030301");
						Conta cContas12Plan1 = (Conta) entidadeHome
								.obterEntidadePorApelido("0525010401");

						double valorConta8Plan1 = cContas8Plan1
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);
						double valorConta9Plan1 = cContas9Plan1
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);
						double valorConta10Plan1 = cContas10Plan1
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);
						double valorConta11Plan1 = cContas11Plan1
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);
						double valorConta12Plan1 = cContas12Plan1
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);

						if (ratioPermanente != null) 
							valorRatio1 = (ratioPermanente.obterUso() + ratioPermanente.obterLeasing()) / 2;
							//if (ratioPermanente.obterVenda() > 0)
								//valorRatio1 = ((ratioPermanente.obterUso() + ratioPermanente.obterLeasing()) / ratioPermanente.obterVenda()) / 2;

						if (ratioPermanente != null)
							valorRatio2 = ratioPermanente.obterResultados();

						double A3 = valorConta8Plan1 + valorConta9Plan1
								+ valorRatio1 + valorConta10Plan1
								+ valorConta11Plan1 + valorConta12Plan1
								+ valorRatio2;

						//A1
						double A1 = A2 - A3;
						
						file.write("Aseguradora: " + aseguradora.obterNome()
								+ "\r\n");
						
						file.write("MesAno para Calculo: " + mesAnoCalculo+ "\r\n");
						
						file.write("Valor Minimo1 -->: " +formataValor.format( valorMinimo)
										+ "\r\n");
						file.write("Valor Minimo2 -->: " + formataValor.format(valorMinimo / 1000)
								+ "\r\n");
						file.write("A1 --> A1 = A2 - A3: " + formataValor.format(A1) + "\r\n");
						file
								.write("A2 --> 0301010000 + 0301020000 + 0302010000 + 0303010000 + 0303020000 + 0304000000 + 0305010000: "
										+ formataValor.format(A2) + "\r\n");
						//file.write("A3 --> 0109010000 + 0104010201 + ((ratioPermanente.obterUso() + ratioPermanente.obterLeasing()) / ratioPermanente.obterVenda())/2 + 0107010301 + 0107030301 + 0525010401 + ratioPermanente.obterResultados(): "
							//			+ formataValor.format(A3) + "\r\n");
						
						file.write("A3 --> 0109010000 + 0104010201 + ((ratioPermanente.obterUso() + ratioPermanente.obterLeasing()))/2 + 0107010301 + 0107030301 + 0525010401 + ratioPermanente.obterResultados(): "
								+ formataValor.format(A3) + "\r\n");

						//				Calculo da Planilha 2

						//A8
						/*
						 * ClassificacaoContas cContas1Plan2 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0506010000");
						 * ClassificacaoContas cContas2Plan2 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0513010000");
						 * ClassificacaoContas cContas3Plan2 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0513020000");
						 * ClassificacaoContas cContas4Plan2 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0508010000");
						 * ClassificacaoContas cContas5Plan2 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0408000000");
						 * ClassificacaoContas cContas6Plan2 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0409000000");
						 * ClassificacaoContas cContas7Plan2 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0508020000");
						 * ClassificacaoContas cContas8Plan2 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0407000000");
						 * 
						 * double valorConta1Plan2 =
						 * cContas1Plan2.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual); double valorConta2Plan2 =
						 * cContas2Plan2.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual); double valorConta3Plan2 =
						 * cContas3Plan2.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual); double valorConta4Plan2 =
						 * cContas4Plan2.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual); double valorConta5Plan2 =
						 * cContas5Plan2.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual); double valorConta6Plan2 =
						 * cContas6Plan2.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual); double valorConta7Plan2 =
						 * cContas7Plan2.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual); double valorConta8Plan2 =
						 * cContas8Plan2.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual);
						 * 
						 * double A8 = valorConta1Plan2 + valorConta2Plan2 +
						 * valorConta3Plan2 + valorConta4Plan2 -
						 * valorConta5Plan2 - valorConta6Plan2 -
						 * valorConta7Plan2 - valorConta8Plan2;
						 */

						/*RatioTresAnos ratio = aseguradora.obterRatioTresAnos();

						double A8 = 0;
						//ratioHome.obterSinistrosPagos3Anos(dataAgenda, aseguradora);
						
						if (ratio != null)
							A8 = ratio.obterSinistrosPagos()
									+ ratio.obterGastosSinistros()
									- ratio.obterSinistrosRecuperados()
									- ratio.obterGastosRecuperados()
									- ratio.obterRecuperoSinistros();

						file.write("A8 --> ratio3Anos.obterSinistrosPagos() + ratio3Anos.obterGastosSinistros() - ratio3Anos.obterSinistrosRecuperados() - ratio3Anos.obterGastosRecuperados() - ratio3Anos.obterRecuperoSinistros(): "	+ formataValor.format(A8) + "\r\n");
*/
						Map<String, String> contasAcumuladas = new TreeMap<String,String>();
						
						double sinistrosPagos3Anos = ratioHome.obterSinistrosPagos3Anos(dataAgenda, aseguradora, contasAcumuladas);
						double gastosSinistros3Anos = ratioHome.obterGastosSinistros3Anos(dataAgenda, aseguradora, contasAcumuladas);
						double sinistrosRecuperados3Anos = ratioHome.obterSinistrosRecuperados3Anos(dataAgenda, aseguradora, contasAcumuladas);
						double gastosRecuperados3Anos = ratioHome.obterGastosRecuperados3Anos(dataAgenda, aseguradora, contasAcumuladas);
						double recuperadosSinistros3Anos = ratioHome.obterRecuperadosSinistros3Anos(dataAgenda, aseguradora, contasAcumuladas);
						
						//A8
						double A8 = sinistrosPagos3Anos + gastosSinistros3Anos - sinistrosRecuperados3Anos - gastosRecuperados3Anos - recuperadosSinistros3Anos;
						
						file.write("A8 --> sinistrosPagos3Anos + gastosSinistros3Anos - sinistrosRecuperados3Anos - gastosRecuperados3Anos - recuperadosSinistros3Anos: "	+ formataValor.format(A8) + "\r\n");
						
						//A9
						/*double A9 = 0;

						if (ratio != null)
							A9 = ratio.obterSinistrosPagos()
									+ ratio.obterGastosSinistros()
									- ratio.obterRecuperoSinistros();
						
						file.write("A9 --> ratio3Anos.obterSinistrosPagos() + ratio3Anos.obterGastosSinistros() - ratio3Anos.obterRecuperoSinistros(): "+ formataValor.format(A9) + "\r\n");*/
						
						//A9
						double A9 = sinistrosPagos3Anos + gastosSinistros3Anos - recuperadosSinistros3Anos;
						
						file.write("A9 --> sinistrosPagos3Anos + gastosSinistros3Anos - recuperadosSinistros3Anos: "+ formataValor.format(A9) + "\r\n");

						//A10
						double A10 = 0;

						if (A9 != 0)
							A10 = A8 / A9;

						file.write("A10 --> A8 / A9: " + formataValor.format(A10) + "\r\n");

						//A11
						/*
						 * ClassificacaoContas cContas9Plan2 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0401010000");
						 * ClassificacaoContas cContas10Plan2 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0402000000");
						 * ClassificacaoContas cContas11Plan2 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0403000000");
						 * ClassificacaoContas cContas12Plan2 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0501000000");
						 * ClassificacaoContas cContas13Plan2 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0502000000");
						 * 
						 * double valorConta9Plan2 =
						 * cContas9Plan2.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual); double valorConta10Plan2 =
						 * cContas10Plan2.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual); double valorConta11Plan2 =
						 * cContas11Plan2.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual); double valorConta12Plan2 =
						 * cContas12Plan2.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual); double valorConta13Plan2 =
						 * cContas13Plan2.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual);
						 * 
						 * double A11 = valorConta9Plan2 + valorConta10Plan2 +
						 * valorConta11Plan2 - valorConta12Plan2 -
						 * valorConta13Plan2;
						 */

						/*
						 * double A11 = 0;
						RatioUmAno ratio2 = aseguradora.obterRatioUmAno();
						if (ratio2 != null)
							A11 = ratio2.obterPrimasDiretas()
									+ ratio2.obterPrimasAceitas()
									- ratio2.obterPrimasCedidas()
									- ratio2.obterAnulacaoPrimasDiretas()
									+ ratio2.obterAnulacaoPrimasCedidas()
									- ratio2.obterAnulacaoPrimasAtivas();

						file.write("A11 --> ratio1Ano.obterPrimasDiretas() + ratio1Ano.obterPrimasAceitas() - ratio1Ano.obterPrimasCedidas() - ratio1Ano.obterAnulacaoPrimasDiretas() + ratio1Ano.obterAnulacaoPrimasCedidas() - ratio1Ano.obterAnulacaoPrimasAtivas(): "
										+ formataValor.format(A11) + "\r\n");*/
						
						RatioPermanente ratio = aseguradora.obterRatioPermanente();
						
						double primasDiretas = ratioHome.obterPrimaDireta1Ano(dataAgenda, aseguradora, contasAcumuladas);
						double primasAceitas = ratioHome.obterPrimasAceitas1Ano(dataAgenda, aseguradora, contasAcumuladas);
						double primasCedidas = ratioHome.obterPrimasCedidas1Ano(dataAgenda, aseguradora, contasAcumuladas);
						double anulacaoPrimasDiretas = 0;
						double anulacaoPrimasCedidas = 0;
						double anulacaoPrimasAtivas = 0;
						if(ratioPermanente!=null)
						{
							anulacaoPrimasDiretas = ratioPermanente.obterAnulacaoPrimasSegurosDiretos(); 
							anulacaoPrimasCedidas = ratioPermanente.obterAnulacaoPrimasCedidas();
							anulacaoPrimasAtivas = ratioPermanente.obterAnulacaoPrimasReasegurosAtivos();
						}
						
						double A11 = primasDiretas + primasAceitas - primasCedidas - anulacaoPrimasDiretas + anulacaoPrimasCedidas - anulacaoPrimasAtivas;
						
						file.write("A11 --> primasDiretas + primasAceitas - primasCedidas - anulacaoPrimasDiretas + anulacaoPrimasCedidas - anulacaoPrimasAtivas: "	+ formataValor.format(A11) + "\r\n");
						
						/*double A12 = 0;

						if (ratio2 != null)
							A12 = ratio2.obterPrimasDiretas()
									+ ratio2.obterPrimasAceitas()
									- ratio2.obterAnulacaoPrimasDiretas()
									- ratio2.obterAnulacaoPrimasAtivas();

						//double A12 = valorConta9Plan2 + valorConta10Plan2 +
						// valorConta11Plan2;

						file.write("A12 --> ratio1Ano.obterPrimasDiretas() + ratio1Ano.obterPrimasAceitas() - ratio1Ano.obterAnulacaoPrimasDiretas() - ratio1Ano.obterAnulacaoPrimasAtivas(): "
										+ formataValor.format(A12) + "\r\n");*/
						
						//A12
						double A12 = primasDiretas + primasAceitas - anulacaoPrimasDiretas - anulacaoPrimasAtivas;

						file.write("A12 --> primasDiretas + primasAceitas -anulacaoPrimasDiretas - anulacaoPrimasAtivas: " + formataValor.format(A12) + "\r\n");

						//A13
						double A13 = 0;
						if (A12 != 0)
							A13 = A11 / A12;

						file.write("A13 --> A11 / A12: " + formataValor.format(A13) + "\r\n");

						//				***************************************************************************
						//Planilha 4
						/*
						 * ClassificacaoContas cContas1Plan4 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0506010000");
						 * ClassificacaoContas cContas2Plan4 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0513010000");
						 * ClassificacaoContas cContas3Plan4 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0513020000");
						 * ClassificacaoContas cContas4Plan4 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0508010000");
						 * ClassificacaoContas cContas5Plan4 =
						 * (ClassificacaoContas)
						 * entidadeHome.obterEntidadePorApelido("0213000000");
						 * 
						 * double valorConta1Plan4 =
						 * cContas1Plan4.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual); double valorConta2Plan4 =
						 * cContas2Plan4.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual); double valorConta3Plan4 =
						 * cContas3Plan4.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual); double valorConta4Plan4 =
						 * cContas4Plan4.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual); double valorConta5Plan4 =
						 * cContas5Plan4.obterTotalizacaoExistente(aseguradora,
						 * mesAnoAtual);
						 * 
						 * //A19 double A19 = valorConta1Plan4 +
						 * valorConta2Plan4 + valorConta3Plan4 +
						 * valorConta4Plan4;
						 */
						
						double A14 = 0;
						
						if(0.5 > A10)
							A14 = 0.5;
						else
							A14 = A10;
						
						if(A13 > A14)
							A14 = A13;
						
						file.write("A14 --> Maior valor entre 0.5 e A10 e A13: " + formataValor.format(A14) + "\r\n");

						/*if (ratio != null)
							A19 = ratio.obterSinistrosPagos()
									+ ratio.obterGastosSinistros();

						file.write("A19 --> ratio3Anos.obterSinistrosPagos() + ratio3Anos.obterGastosSinistros(): "
										+ formataValor.format(A19) + "\r\n");*/
						
						//A19
						double A19 = sinistrosPagos3Anos + gastosSinistros3Anos;
						
						file.write("A19 --> sinistrosPagos3Anos + gastosSinistros3Anos: "	+ formataValor.format(A19) + "\r\n");
						
						//double A20 = valorConta5Plan4;

						/*if (ratio != null)
							A20 = ratio.obterProvisoes();
						
						file.write("ratio.obterProvisoes(): "+ formataValor.format(A20) + "\r\n");*/
						
						double provisaoTecnicaSinistro3Anos = ratioHome.obterProvisaoTecnicaSinistros3Anos(dataAgenda, aseguradora, contasAcumuladas); 
						
						//A20
						double A20 = provisaoTecnicaSinistro3Anos;
						
						this.gerarContasAcumuladas(contasAcumuladas);
						
						file.write("provisaoTecnicaSinistro3Anos: "+ formataValor.format(A20) + "\r\n");
						
						ClassificacaoContas cContasA20Plan1 = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0213000000");

						double valorContaA20Plan1 = cContasA20Plan1.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						
						file.write("valorContaA20Plan1: "
								+ formataValor.format(valorContaA20Plan1) + "\r\n");
						
						A20 = valorContaA20Plan1 - A20;

						file.write("A20 --> valorContaA20Plan1 - A20: "
								+ formataValor.format(A20) + "\r\n");

						//A21
						double A21 = 0;

						Inscricao inscricao = aseguradora.obterInscricaoAtiva();

						if (inscricao != null) {
							Date dataAtual = new Date();

							Date dataEmissao = inscricao.obterDataResolucao();

							Calendar calendario = Calendar.getInstance();
							calendario.setTime(dataAtual);
							calendario.add(Calendar.MONTH, -12);

							Calendar calendario2 = Calendar.getInstance();
							calendario2.setTime(dataAtual);
							calendario2.add(Calendar.MONTH, -24);
							
							if(dataEmissao.before(calendario2.getTime()))
								A21 = (A19 + A20) / 3;
							else if(dataEmissao.after(calendario.getTime()) && dataEmissao.before(calendario2.getTime()))
									A21 = (A19 + A20) / 2;
							else
								A21 = (A19 + A20);
							
//							if (dataEmissao.after(calendario2.getTime()))
//								A21 = (A19 + A20) / 3;
//							else if(dataEmissao.after(calendario.getTime()) && dataEmissao.before(calendario2.getTime()))
//								A21 = (A19 + A20) / 2;
//							else
//								A21 = (A19 + A20);
						}

						file
								.write("A21 --> Depende A21 = (A19 + A20) / 3 ou A21 = (A19 + A20) / 2 ou A21 = (A19 + A20): "
										+ formataValor.format(A21) + "\r\n");

						//A22
						double A22 = A21 * 0.23;

						file.write("A22 --> A21 * 0.23: " + formataValor.format(A22) + "\r\n");

						//A23
						//double A23 = A22 * A13;
						double A23 = A22 * A14;
						
						if(agenda.obterIPC() > 0.3)
							A23 = (1 + agenda.obterIPC()) * A23;

						//file.write("A23 --> A22 * A13: " + formataValor.format(A23) + "\r\n");
						file.write("A23 --> Depende da variacao do IPC (1 + agenda.obterIPC())*A23 ou A22 * A14: " + formataValor.format(A23) + "\r\n");

						//				**************************************************************************
						//Planilha 3

						//A16
						double A16 = 0.16 * A12;

						file.write("A16 --> 0.16 * A12: " + formataValor.format(A16) + "\r\n");

						//A17
						//double A17 = A16 * A13;
						double A17 = A16 * A14;
						
						if(agenda.obterIPC() > 0.1)
							A17 = (1 + agenda.obterIPC()) * A17;

						file.write("A17 --> Depende da variaco do IPC Olhar o fonte (1 + agenda.obterIPC()) * A17 ou A16 * A14: " + formataValor.format(A17) + "\r\n");

						//********************************************************************
						//Planilha 1A

						//A5
						double A5 = 0;

//						if (valorMinimo > A17) 
//						{
//							if (valorMinimo > A23)
//								A5 = valorMinimo;
//							else
//								A5 = A23;
//						}
//						else 
//						{
//							if (A17 > A23)
//								A5 = A17;
//							else
//								A5 = A23;
//						}
						
//						if (A4 > A17) 
//						{
//							if (A4 > A23)
//								A5 = A4;
//							else
//								A5 = A23;
//						}
//						else 
//						{
//							if (A17 > A23)
//								A5 = A17;
//							else
//								A5 = A23;
//						}
						
						double[] maiorValorVetor2 = new double[3];
						maiorValorVetor2[0] = A4;
						maiorValorVetor2[1] = A17;
						maiorValorVetor2[2] = A23;
						
						double maiorValor2 = 0;
						
						for(int k = 0 ; k < maiorValorVetor2.length ; k++)
						{
							double valor = maiorValorVetor2[k];
							
							if(valor > maiorValor2)
								maiorValor2 = valor;
						}
						
						A5 = maiorValor2;

						file
								.write("A5 --> Depende se o valorMinimo > A17 e > A23, A5 = valorMinimo, senão A5 = A23. Se valorMinimo < A17 e > A23, então A5 = A17, senão A5 = A23: "
										+ formataValor.format(A5) + "\r\n");

						//A6
						double A6 = A1 - A5;

						file.write("A6 --> A1 - A5: " + formataValor.format(A6) + "\r\n");

						//A7
						double A7 = 0;
						if (A5 != 0)
							A7 = A1 / A5;

						file.write("A7 --> A1 / A5: " + formataValor.format(A7) + "\r\n");

						//*******************************************************************************
						//Planilha 6

						Conta cContas1Plan6 = (Conta) entidadeHome
								.obterEntidadePorApelido("0212050101");
						Conta cContas2Plan6 = (Conta) entidadeHome
								.obterEntidadePorApelido("0701010202");
						Conta cContas3Plan6 = (Conta) entidadeHome
								.obterEntidadePorApelido("0701010204");
						Conta cContas4Plan6 = (Conta) entidadeHome
								.obterEntidadePorApelido("0701010205");
						Conta cContas5Plan6 = (Conta) entidadeHome
								.obterEntidadePorApelido("0701010207");
						Conta cContas6Plan6 = (Conta) entidadeHome
								.obterEntidadePorApelido("0212050101");
						ClassificacaoContas cContas7Plan6 = (ClassificacaoContas) entidadeHome
								.obterEntidadePorApelido("0601010200");

						double valorConta1Plan6 = cContas1Plan6
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);
						double valorConta2Plan6 = cContas2Plan6
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);
						double valorConta3Plan6 = cContas3Plan6
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);
						double valorConta4Plan6 = cContas4Plan6
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);
						double valorConta5Plan6 = cContas5Plan6
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);
						double valorConta6Plan6 = cContas6Plan6
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);
						double valorConta7Plan6 = cContas7Plan6
								.obterTotalizacaoExistente(aseguradora,
										mesAnoCalculo);

						//A35
						double A35 = valorConta1Plan6;

						file.write("A35 --> 0212050101: " + formataValor.format(A35) + "\r\n");

						//A36
						double A36 = 0;

						file.write("A36 --> : " + formataValor.format(A36) + "\r\n");

						//A37
						double A37 = 0;
						if (A35 != 0)
							A37 = (A35 - A36) / A35;

						file
								.write("A37 --> (A35 - A36) / A35: " +formataValor.format( A37)
										+ "\r\n");

						//A38
						double A38 = A35 * 0.04;

						file.write("A38 --> A35 * 0.04: " + formataValor.format(A38) + "\r\n");

						//A39
						double A39 = A38 * A37;

						file.write("A39 --> A38 * A37: " + formataValor.format(A39) + "\r\n");

						//A40
						double A40 = A38 * 0.85;

						file.write("A40 --> A38 * 0.85: " + formataValor.format(A40) + "\r\n");

						//A41
						double A41 = 0;

						if (A39 > A40)
							A41 = A39;
						else
							A41 = A40;

						file
								.write("A41 --> Depende se A39 > A40, então A41 = A39, senão A39 = A40: "
										+ formataValor.format(A41) + "\r\n");

						//A54
						double A54 = valorConta2Plan6 + valorConta3Plan6
								+ valorConta4Plan6 + valorConta5Plan6
								- valorConta6Plan6;

						file
								.write("A54 --> 0701010202 + 0701010204 + 0701010205 + 0701010207 - 0212050101: "
										+ formataValor.format(A54) + "\r\n");

						//A42
						double A42 = valorConta7Plan6;

						file.write("A42 --> 0601010200: " + formataValor.format(A42) + "\r\n");

						//A43
						double A43 = 0;
						if (A54 != 0)
							A43 = (A54 - A42) / A54;

						file
								.write("A43 --> (A54 - A42) / A54: " + formataValor.format(A43)
										+ "\r\n");

						//A44
						double A44 = A54 * 0.003;

						file.write("A44 --> A54 * 0.003: " + formataValor.format(A44) + "\r\n");

						//A45
						double A45 = A44 * A43;

						file.write("A45 --> A44 * A43: " + formataValor.format(A45) + "\r\n");

						//A46
						double A46 = A44 * 0.50;

						file.write("A46 --> A44 * 0.50: " + formataValor.format(A46) + "\r\n");

						//A47
						double A47 = 0;

						if (A45 > A46)
							A47 = A45;
						else
							A47 = A46;

						file
								.write("A47 --> Depende se A45 > A46, então A47 = A45, senão A47 = A46: "
										+ formataValor.format(A47) + "\r\n");

						//A48
						double A48 = A41 + A47;

						file.write("A48 --> A41 + A47: " + formataValor.format(A48) + "\r\n");

						//				************************************************************************
						//Planilha 5

						//A32
						double A32 = 0;

						if (A30 > A48)
							A32 = A30;
						else
							A32 = A48;

						file
								.write("A32 --> Depende se valorMinimo > A48, então A32 = valorMinimo, senão A32 = A48: "
										+ formataValor.format(A32) + "\r\n");

						//A33
						double A33 = A1 - A32;

						file.write("A33 --> A1 - A32: " + formataValor.format(A33) + "\r\n");

						//A34
						double A34 = 0;
						if (A32 != 0)
							A34 = A1 / A32;

						file.write("A34 --> A1 / A32: " + formataValor.format(A34) + "\r\n");

						//***********************************************************************************
						//Planilha 7

						//A50
						double A50 = A5 + A32;

						file.write("A50 --> A5 + A32: " + formataValor.format(A50) + "\r\n");

						//A51
						double A51 = A1 - A50;

						file.write("A51 --> A1 - A50: " + formataValor.format(A51) + "\r\n");

						//A52
						double A52 = A1 / A50;

						file.write("A52 --> A1 / A50: "+ formataValor.format(A52) + "\r\n");

						double margenSolvencia = 0;
						
						if(ramoPatrimonial)
						{
							if(A7 > 200)
								A7 = 200;
							
							margenSolvencia = (A7 * 30) / 200;
						}
						else if(ramoVida)
						{
							if(A34 > 200)
								A34 = 200;
							
							margenSolvencia = (A34 * 30) / 200;
						}
						else
						{
							if(A52 > 200)
								A52 = 200;
							
							margenSolvencia = (A52 * 30) / 200;
						}

						file.write("margenSolvencia --> " + formataValor.format(margenSolvencia) + "\r\n");

						MeicosCalculo calculo4 = (MeicosCalculo) mm.getEntity("MeicosCalculo");

						calculo4.atribuirOrigem(meicos.obterOrigem());
						calculo4.atribuirDestino(meicos.obterDestino());
						calculo4.atribuirResponsavel(usuarioAtual);
						calculo4.atribuirSuperior(meicos);
						calculo4.atribuirTitulo("Calculo de Meicos");
						calculo4.atribuirTipo("Margen Solvencia");
						calculo4.atribuirValor(margenSolvencia);
						calculo4.atribuirDataPrevistaInicio(new Date());
						calculo4.atribuirDataPrevistaConclusao(new Date());

						calculos.add(calculo4);

						//Fundo de Garantia

						ClassificacaoContas cContas1fundo = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0101010000");
						ClassificacaoContas cContas2fundo = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0101020000");
						ClassificacaoContas cContas3fundo = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0107010200");
						Conta cContas4fundo = (Conta) entidadeHome.obterEntidadePorApelido("0107010301");
						ClassificacaoContas cContas5fundo = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("0107010100");

						double valorConta1fudo = cContas1fundo.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						double valorConta2fudo = cContas2fundo.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						double valorConta3fudo = cContas3fundo.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						double valorConta4fudo = cContas4fundo.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);
						double valorConta5fudo = cContas5fundo.obterTotalizacaoExistente(aseguradora,mesAnoCalculo);

						double valorFundo = 0;

						//A26
						double A26 = valorConta1fudo + valorConta2fudo;

						file.write("A26 --> 0101010000 + 0101020000: " + formataValor.format(A26)
								+ "\r\n");

						//A27
						double A27 = valorConta3fudo + valorConta4fudo;

						file.write("A27 --> 0107010200 + 0107010301: " + formataValor.format(A27)
								+ "\r\n");

						//A28
						double A29 = A26 + A27 + valorConta5fudo;

						file.write("A29 --> A26 + A27 + 0107010100: " + formataValor.format(A29)
								+ "\r\n");

						//A30
						//double A30 = 0;

//						if (valorMinimo > (A1 * 0.30))
//							A30 = valorMinimo;
//						else
//							A30 = A1 * 0.30;
						
						//Vida e Patrimonio
						//if (A30 < (A1 * 0.30))
							//A30 = A1 * 0.30;
						//else
							//A30 = A1 * 0.30;
						
						double[] maiorValorVetor = new double[4];
						maiorValorVetor[0] = A30 * 0.3;
						maiorValorVetor[1] = A50 * 0.3;
						maiorValorVetor[2] = A1 * 0.3;
						maiorValorVetor[3] = A4 * 0.3;
						
						double maiorValor = 0;
						
						for(int k = 0 ; k < maiorValorVetor.length ; k++)
						{
							double valor = maiorValorVetor[k];
							if(valor > maiorValor)
								maiorValor = valor;
						}
						
						
						file.write("A30 Antes --> "+ formataValor.format(A30) + "\r\n");
						
						A30 = maiorValor;
						
						file.write("A30 --> Depende se o valorMinimo > (A1 * 0.30), então A30 = valorMinimo, senão A30 = A1 * 0.30: "+ formataValor.format(A30) + "\r\n");

						//A31
						double A31 = A29 - A30;

						file.write("A31 --> A29 - A30: " + formataValor.format(A31) + "\r\n");

						//A53
						double A53 = 0;

						if (A30 > 0)
							A53 = A29 / A30;

						file.write("A53 --> A29 / A30: " + formataValor.format(A53) + "\r\n");

						//FRR
						double frr = A10;

						file.write("FRR --> A10: " + formataValor.format(frr) + "\r\n");

						//FRR
						double frp = A13;

						file.write("FRP --> A13: " + formataValor.format(frp) + "\r\n");

						//PPNC
						double ppnc = A1 / 1000000;

						file.write("PPNC --> A1 / 1000000: " + formataValor.format(ppnc) + "\r\n");
						
						file.write(ponderacaoStr);

						aseguradora.excluirClassificacao(mes, ano);

						aseguradora.adicionarClassificacao("Fundo de Garantia",mes, ano, A53);
						aseguradora.adicionarClassificacao("FRR", mes, ano, frr);
						aseguradora.adicionarClassificacao("FRP", mes, ano, frp);
						aseguradora.adicionarClassificacao("PPNC", mes, ano, ppnc);

						//file.write("\r\n");
						
						file.write("------------------------------------------------------------------------------------------"	+ "\r\n");
					}
				}
			}
			
			fileRatio.close();
			file.close();
			fileContas.close();

			for (Iterator k = calculos.iterator(); k.hasNext();)
			{
				MeicosCalculo calculo = (MeicosCalculo) k.next();

				calculo.incluir();
			}			
			
			mm.commitTransaction();
			
			this.calcularPontosQualificacao(agenda);
			
			this.setResponseView(new EventoView(agenda));
			this.setAlert("Meicos Calculado");
			

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(agenda));
			mm.rollbackTransaction();
		}
	}
	
	private void calcularPontosQualificacao(AgendaMeicos agenda) throws Exception
	{
		
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		
		double MAXIMO_PONTOS_OUTROS_INDICADORES = 15;
		double MAXIMO_PONTOS_IN_SITU = 25;
		double MAXIMO_PONTOS_RATIO_FINANCEIRO = 30;
		double MAXIMO_PONTOS_MARGEM_SOLVENCIA = 30;
		mm.beginTransaction();
		try
		{
			for (Iterator i = aseguradoraHome.obterAseguradoras().iterator(); i.hasNext();) 
			{
				Aseguradora aseguradora = (Aseguradora) i.next();
				
				if(aseguradora.obterId() == 5207)
				{
					double valorOutrosIndicadores = 0;
					double valorInSitu = 0;
					double valorRatiosFinanceiros = 0;
					double valorMargenSolvencia =0;
					int classificacao = 0;
		
					for(Iterator j = aseguradora.obterCalculoMeicos(agenda).iterator() ; j.hasNext() ;)
					{
						MeicosCalculo calculo = (MeicosCalculo) j.next();
						
						if(calculo.obterTipo().equals("Otros Indicadores"))
							valorOutrosIndicadores = calculo.obterValorIndicador();
						else if(calculo.obterTipo().equals("In-Situ"))
							valorInSitu = calculo.obterValorIndicador();
						else if(calculo.obterTipo().equals("Ratios Financeiros"))
							valorRatiosFinanceiros = calculo.obterValorIndicador();
						else if(calculo.obterTipo().equals("Margen Solvencia"))
							valorMargenSolvencia = calculo.obterValorIndicador();
					}
					
					double totalPontos = valorOutrosIndicadores + valorInSitu + valorRatiosFinanceiros + valorMargenSolvencia;
					
					double porcentagemOutrosIndicadores = (valorOutrosIndicadores * MAXIMO_PONTOS_OUTROS_INDICADORES)/100;
					double porcentagemInSitu = (valorInSitu * MAXIMO_PONTOS_IN_SITU)/100;
					double porcentagemRatiosFinanceiros = (valorRatiosFinanceiros * MAXIMO_PONTOS_RATIO_FINANCEIRO)/100;
					double porcentagemMargenSolvencia = (valorMargenSolvencia * MAXIMO_PONTOS_MARGEM_SOLVENCIA)/100;
					
					double[] valoresPorcentagem = new double[4];
					valoresPorcentagem[0] = porcentagemOutrosIndicadores;
					valoresPorcentagem[1] = porcentagemInSitu;
					valoresPorcentagem[2] = porcentagemRatiosFinanceiros;
					valoresPorcentagem[3] = porcentagemMargenSolvencia;
					
					if(totalPontos>=90)
					{
						/////////////////////// PRIMEIRO 'SE' DA PLANILHA /////////////////////////////
						if(porcentagemOutrosIndicadores >= 75 && porcentagemInSitu >= 75 && porcentagemRatiosFinanceiros >= 75 && porcentagemMargenSolvencia >= 75)
						{
							int umaMenosQue90 = 0;
							for(int k = 0 ; k < valoresPorcentagem.length ; k++)
							{
								double valor = valoresPorcentagem[k];
								if(valor < 90)
									umaMenosQue90++;
							}
							
							if(umaMenosQue90<=1)
								classificacao = 1;
						}
						else
						{
							/////////////////////// TERCEIRO 'SE' DA PLANILHA /////////////////////////////
							int umaMenosQue75 = 0;
							int variasMenosQue90 = 0;
							for(int k = 0 ; k < valoresPorcentagem.length ; k++)
							{
								double valor = valoresPorcentagem[k];
								if(valor < 75)
								{
									umaMenosQue75++;
									variasMenosQue90++;
								}
								else if(valor < 90)
									variasMenosQue90++;
							}
							
							if(umaMenosQue75<=1 || variasMenosQue90 > 1)
								classificacao = 2;
						}
					}
					/////////////////////// SEGUNDO 'SE' DA PLANILHA /////////////////////////////
					else if(totalPontos>=75 && totalPontos < 90)
					{
						if(porcentagemOutrosIndicadores >= 60 && porcentagemInSitu >= 60 && porcentagemRatiosFinanceiros >= 60 && porcentagemMargenSolvencia >= 60)
						{
							int umaMenosQue75 = 0;
							for(int k = 0 ; k < valoresPorcentagem.length ; k++)
							{
								double valor = valoresPorcentagem[k];
								if(valor < 75)
									umaMenosQue75++;
							}
							
							if(umaMenosQue75<=1)
								classificacao = 2;
						}
					}
					/////////////////////// QUARTO 'SE' DA PLANILHA /////////////////////////////
					else if(totalPontos>=60 && totalPontos < 75)
					{
						if(porcentagemOutrosIndicadores >= 45 && porcentagemInSitu >= 45 && porcentagemRatiosFinanceiros >= 45 && porcentagemMargenSolvencia >= 45)
						{
							int umaMenosQue60 = 0;
							for(int k = 0 ; k < valoresPorcentagem.length ; k++)
							{
								double valor = valoresPorcentagem[k];
								if(valor < 60)
									umaMenosQue60++;
							}
							
							if(umaMenosQue60<=1)
								classificacao = 3;
						}
					}
					/////////////////////// QUINTO 'SE' DA PLANILHA /////////////////////////////
					else if(totalPontos >= 75)
					{
						int umaMenosQue60 = 0;
						int variasMenosQue75 = 0;
						for(int k = 0 ; k < valoresPorcentagem.length ; k++)
						{
							double valor = valoresPorcentagem[k];
							if(valor < 60)
							{
								umaMenosQue60++;
								variasMenosQue75++;
							}
							else if(valor < 75)
								variasMenosQue75++;
						}
						
						/////////////////////// OITAVO 'SE' DA PLANILHA /////////////////////////////
						if(umaMenosQue60>1)
							classificacao = 4;
						else if(umaMenosQue60<=1 || variasMenosQue75 > 1)
							classificacao = 3;
					}
					/////////////////////// SEXTO 'SE' DA PLANILHA /////////////////////////////
					else if(totalPontos < 60)
						classificacao = 4;
					/////////////////////// SETIMO 'SE' DA PLANILHA /////////////////////////////
					else if(totalPontos >= 60)
					{
						int umaMenosQue45 = 0;
						for(int k = 0 ; k < valoresPorcentagem.length ; k++)
						{
							double valor = valoresPorcentagem[k];
							
							if(valor < 45)
								umaMenosQue45++;
						}
						
						if(umaMenosQue45<=1)
							classificacao = 4;
					}
					
					MeicosCalculo resultadoNovo = (MeicosCalculo) mm.getEntity("MeicosCalculo");
		
					resultadoNovo.atribuirOrigem(aseguradora);
					resultadoNovo.atribuirDestino(aseguradora);
					resultadoNovo.atribuirResponsavel(usuarioAtual);
					resultadoNovo.atribuirSuperior(agenda);
					resultadoNovo.atribuirTitulo("Resultado da Classificacao");
					resultadoNovo.atribuirTipo("Resultado da Classificacao");
					resultadoNovo.atribuirValor(classificacao);
					resultadoNovo.atribuirDataPrevistaInicio(new Date());
					resultadoNovo.atribuirDataPrevistaConclusao(new Date());
					resultadoNovo.incluir();
					
					mm.commitTransaction();
				}
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(agenda));
			mm.rollbackTransaction();
		}
	}
	
}