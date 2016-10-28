package com.gvs.crm.model.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
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
import com.gvs.crm.model.AnulacaoInstrumento;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.ApoliceHome;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AspectosLegais;
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.CodigoInstrumento;
import com.gvs.crm.model.CodigoInstrumentoHome;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Corretora;
import com.gvs.crm.model.DadosCoaseguro;
import com.gvs.crm.model.DadosPrevisao;
import com.gvs.crm.model.DadosReaseguro;
import com.gvs.crm.model.DadosReaseguroHome;
import com.gvs.crm.model.Emissor;
import com.gvs.crm.model.EmissorHome;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.FaturaSinistro;
import com.gvs.crm.model.Localidade;
import com.gvs.crm.model.LocalidadeHome;
import com.gvs.crm.model.MfAlertaTrempana;
import com.gvs.crm.model.Morosidade;
import com.gvs.crm.model.MovimentacaoFinanceiraConta;
import com.gvs.crm.model.Notificacao;
import com.gvs.crm.model.Parametro;
import com.gvs.crm.model.Plano;
import com.gvs.crm.model.PlanoHome;
import com.gvs.crm.model.Qualificacao;
import com.gvs.crm.model.QualificacaoHome;
import com.gvs.crm.model.Qualificadora;
import com.gvs.crm.model.QualificadoraHome;
import com.gvs.crm.model.RatioPermanente;
import com.gvs.crm.model.RatioTresAnos;
import com.gvs.crm.model.RatioUmAno;
import com.gvs.crm.model.Reaseguradora;
import com.gvs.crm.model.Refinacao;
import com.gvs.crm.model.RegistroAnulacao;
import com.gvs.crm.model.RegistroCobranca;
import com.gvs.crm.model.RegistroGastos;
import com.gvs.crm.model.Sinistro;
import com.gvs.crm.model.SinistroHome;
import com.gvs.crm.model.Suplemento;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.model.Uteis;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class AgendaMovimentacaoImpl extends EventoImpl implements
		AgendaMovimentacao {
	private int movimentoMes;

	private int movimentoAno;

	public void atualizarDataPrevistaConclusao(Date dataPrevistaConclusao) throws Exception
	{
		super.atualizarDataPrevistaConclusao(dataPrevistaConclusao);
	}

	public void atualizarDataPrevistaInicio(Date dataPrevistaInicio) throws Exception
	{
		super.atualizarDataPrevistaInicio(dataPrevistaInicio);
	}

	public void atualizarValidacao(String mod) throws Exception 
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set validacao = ? where id = ?");
		update.addString(mod);
		update.addLong(this.obterId());
		
		update.execute();
	}

	public String obterValidacao() throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select validacao from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("validacao");
	}

	public String obterIcone() throws Exception {
		return "calendar.gif";
	}

	public void atribuirMesMovimento(int mes) throws Exception {
		this.movimentoMes = mes;
	}

	public void atribuirAnoMovimento(int ano) throws Exception {
		this.movimentoAno = ano;
	}

	public int obterMesMovimento() throws Exception 
	{
		if (this.movimentoMes == 0) 
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select movimento_mes from agenda_movimentacao where id=?");
			query.addLong(this.obterId());
			
			this.movimentoMes = query.executeAndGetFirstRow().getInt("movimento_mes");
		}
		return this.movimentoMes;
	}

	public int obterAnoMovimento() throws Exception
	{
		if (this.movimentoAno == 0)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select movimento_ano from agenda_movimentacao where id=?");
			query.addLong(this.obterId());
			this.movimentoAno = query.executeAndGetFirstRow().getInt("movimento_ano");
		}
		return this.movimentoAno;
	}

	public boolean existeAgendaNoPeriodo(int mes, int ano, Entidade asseguradora, String tipo) throws Exception
	{
		boolean existe = false;

		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select movimento_mes, movimento_ano from agenda_movimentacao,evento where evento.id=agenda_movimentacao.id and origem=? and movimento_mes=? and movimento_ano=? and tipo=? and validacao=?");
		query.addLong(asseguradora.obterId());
		query.addInt(mes);
		query.addInt(ano);
		query.addString(tipo);
		query.addString("Total");

		SQLRow[] rows = query.execute();

		if (rows.length > 0)
			existe = true;

		return existe;
	}
	
	public void incluir() throws Exception
	{
		super.incluir();
		SQLUpdate insert = this.getModelManager().createSQLUpdate("insert into agenda_movimentacao (id, movimento_mes, movimento_ano) values (?,?,?)");
		insert.addLong(this.obterId());
		insert.addInt(this.obterMesMovimento());
		insert.addInt(this.obterAnoMovimento());
		insert.execute();
	}

	public void enviarBcp(String comentario) throws Exception
	{
		this.concluir(comentario);
		for (Iterator i = this.obterInferiores().iterator(); i.hasNext();)
			((Evento) i.next()).concluir(null);
	}

	public boolean permiteEnviarBcp() throws Exception {
		boolean permite = false;
		for (Iterator i = this.obterInferiores().iterator(); i.hasNext();) {
			Evento e = (Evento) i.next();
			if (e instanceof Notificacao) {
				Notificacao notificacao = (Notificacao) e;
				if (notificacao.obterTipo().equals(
						"Notificación de Recebimento"))
					permite = true;
			}
		}
		return permite;
	}

	public boolean permiteValidar() throws Exception {
		boolean permite = true;

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"SELECT movimentacao_financeira_conta.data_prevista FROM evento,movimentacao_financeira_conta where origem=? and evento.id=movimentacao_financeira_conta.id group by movimentacao_financeira_conta.data_prevista");
		query.addLong(this.obterOrigem().obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			Date data = new Date(rows[i].getLong("data_prevista"));

			String mesEvento = new SimpleDateFormat("MM").format(data);

			String anoEvento = new SimpleDateFormat("yyyy").format(data);

			if (this.obterMesMovimento() == Integer.parseInt(mesEvento)
					&& this.obterAnoMovimento() == Integer.parseInt(anoEvento))
				return false;
		}

		return permite;
	}

	private String nomeArquivo;
	private EntidadeHome home;
	private Parametro parametro;
	
	public Collection<String> validaArquivo(String nomeArquivo) throws Exception
	{
		home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		parametro = (Parametro) home.obterEntidadePorApelido("parametros");
		Collection<String> linhas = new ArrayList<>();

		this.nomeArquivo = nomeArquivo;
		
		File file = new File("" + "c:/Aseguradoras/Archivos/" + nomeArquivo+ ".txt");
		
		FileReader reader = new FileReader(file);
		BufferedReader in = new BufferedReader(reader);
		String str = null;
		
		if (file.exists())
		{
			while((str = in.readLine())!=null)
				linhas.add(str);
			
			in.close();
			
			return this.validadorArquivo(linhas);
		}
		else
		{
			in.close();
			throw new Exception("Erro: 26 - El Archivo " + nomeArquivo	+ ".txt no fue encontrado");
		}

	}

	private Collection<String> erros = new ArrayList<>();

	private Collection<ClassificacaoContas> classificacaoContasTotalizadas = new ArrayList<ClassificacaoContas>();

	/////////////////// MÉTODO PRA VALIDAR O ARQUIVO CONTABIL
	// ////////////////////////////////////////
	private Collection<String> validadorArquivo(Collection<String> linhas) throws Exception
	{
		FileWriter file = new FileWriter("c:/tmp/LogContabil.txt");
		
		//DecimalFormat formataValor = new DecimalFormat("");
		
		int contador = 1;

		int sequencial = 1;

		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome("UsuarioHome");
		CodigoInstrumentoHome codigoHome = (CodigoInstrumentoHome) this.getModelManager().getHome("CodigoInstrumentoHome");
		EmissorHome emissorHome = (EmissorHome) this.getModelManager().getHome("EmissorHome");
		QualificadoraHome qualificadoraHome = (QualificadoraHome) this.getModelManager().getHome("QualificadoraHome");
		QualificacaoHome qualificacaoHome = (QualificacaoHome) this.getModelManager().getHome("QualificacaoHome");
		LocalidadeHome localidadeHome = (LocalidadeHome) this.getModelManager().getHome("LocalidadeHome");
		
		Uteis uteis = new Uteis();

		Usuario responsavel = null;

		Map<Integer,Integer> tipoRegistro = new TreeMap<>();
		int numeroRegistros = 0;
		boolean erro = false;

		try
		{
			MovimentacaoFinanceiraConta mf;
			MfAlertaTrempana mfAlertaTemprana;
			Entidade entidade;
			Conta conta;
			Date dataInicio;
			ClassificacaoContas cConta;
			CodigoInstrumento codigoInstrumento;
			Emissor emissor;
			Qualificadora qualificadora;
			Qualificacao qualificacao;
			Localidade localidade;
			//int mes,ano;
			
			for (String linha : linhas) 
			{
				System.out.println(linha);
	
				//System.out.println("Contador:" + contador);
				
				file.write("Contador: " + contador + " ;Linha: " + linha + "\r\n");
	
				if (Integer.parseInt(linha.substring(5, 7)) != 1
						&& Integer.parseInt(linha.substring(5, 7)) != 2
						&& Integer.parseInt(linha.substring(5, 7)) != 3
						&& Integer.parseInt(linha.substring(5, 7)) != 9)
					
					erros.add("Linea = " + contador
							+ " - Erro: xx - Tipo de registro: "
							+ linha.substring(5, 7) + " incorrecto");
	
				if (Integer.parseInt(linha.substring(5, 7)) == 1)
				{
					if (!tipoRegistro.containsKey(new Integer(linha.substring(5, 7))))
						tipoRegistro.put(new Integer(linha.substring(5, 7)),new Integer(linha.substring(5, 7)));
					else
						erros.add("Linea = "+ contador + " - Erro: 07 - Existe más de un registro tipo 01.");
				}
	
				if (Integer.parseInt(linha.substring(5, 7)) == 2)
				{
					if (!tipoRegistro.containsKey(new Integer(linha.substring(5, 7))))
						tipoRegistro.put(new Integer(linha.substring(5, 7)),new Integer(linha.substring(5, 7)));
				}
	
				else if (Integer.parseInt(linha.substring(5, 7)) == 3)
				{
					if (!tipoRegistro.containsKey(new Integer(linha.substring(5, 7))))
						tipoRegistro.put(new Integer(linha.substring(5, 7)),new Integer(linha.substring(5, 7)));
				}
				
				else if (Integer.parseInt(linha.substring(5, 7)) == 4)
				{
					if (!tipoRegistro.containsKey(new Integer(linha.substring(5, 7))))
						tipoRegistro.put(new Integer(linha.substring(5, 7)),new Integer(linha.substring(5, 7)));
				}
	
				else if (Integer.parseInt(linha.substring(5, 7)) == 9)
				{
					if (!tipoRegistro.containsKey(new Integer(linha.substring(5, 7))))
						tipoRegistro.put(new Integer(linha.substring(5, 7)),new Integer(linha.substring(5, 7)));
				}
	
				if (contador == linhas.size())
				{ /* Final de arquivo *//*
																	    * Rergistro
																	    * 9
																	    */
					if (linha.length() != 9)
					{
						erros.add("Linea = " + contador	+ " - Erro: 04 - Tamaño de registro inválido.");
						break;
					} 
					else
					{
						/* Registro 9 */
						String str01 = linha.substring(0, 5); // 05 Número
															  // Secuencial
	
						sequencial = Integer.parseInt(str01);
	
						if (contador != numeroRegistros)
							erros.add("Linea = "+ contador + " - Erro: 09 - Número de registros informado no esta coincidiendo con el número de registros enviados.");
	
						if (sequencial != contador)
							erros.add("Linea = "+ contador	+ " - Erro 02 - La secuencia de la numeración del archivo no esta en el orden secuencial y creciente");
	
						if (!tipoRegistro.containsKey(new Integer(2)))
							erros.add("Linea = " + contador	+ " - Erro: 17 - Falta registro tipo 02.");
	
						if (!tipoRegistro.containsKey(new Integer(3)))
							erros.add("Linea = " + contador	+ " - Erro: 18 - Falta registro tipo 03.");
						
						/*if (!tipoRegistro.containsKey(new Integer(4)))
							erros.add("Linea = " + contador	+ " - Erro: 18 - Falta registro tipo 04.");*/
	
						if (!tipoRegistro.containsKey(new Integer(9)))
							erros.add("Linea = " + contador + " - Erro: 21 - Falta registro tipo 09.");
	
						
	
					}
				} 
				else if (contador == 1)
				{/* Comeco de arquivo *//* Rergistro 1 */
	
					if (Integer.parseInt(linha.substring(5, 7)) != 1)
						erros.add("Linea = "+ contador	+ " - Erro: 11 - El registro tipo 01 debe ser el primer registro del archivo.");
					else
					{
						/* Rergistro 1 */
						if (linha.length() != 46)
						{
							erros.add("Linea = "+ contador	+ " - Erro: 04 - El tamaño del registro es diferente del especificado en el formato del registro.");
							break;
						}
	
						String str01 = linha.substring(0, 5); // 05 Numero
															  // Secuencial
						if (str01.length() != 5)
							erros.add("Linha = "+ contador	+ " Coluna 0~5 - Erro: 04 - Tamanho de registro inválido.");
	
						sequencial = Integer.parseInt(str01);
	
						if (sequencial != contador)
							erros.add("Linea = "+ contador	+ " - Erro 02 - La secuencia de la numeración del archivo no esta en el orden secuencial y creciente");
	
						String str02 = linha.substring(5, 7); // 02 Identifica el
															  // registro
						if (str02.length() != 2)
							erros.add("Linea = "+ contador	+ " Coluna 5~7 - Erro: 04 - Tamaño de registro inválido.");
						String str03 = linha.substring(7, 10); // 3 Identifica la
															   // Aseguradora
						if (str03.length() != 3)
							erros.add("Linea = "+ contador	+ " Coluna 7~18 - Erro: 04 - Tamanho de registro inválido.");
	
						seguradora = (Entidade) entidadeHome.obterEntidadePorSigla(str03);
	
						if (seguradora == null)
							erros.add("Linea = " + contador+ " - Erro: 08 - Aseguradora " + str03.trim()+ " inexistente.");
						else if (this.obterOrigem() != seguradora)
							erros.add("Linha = " + contador	+ " - Erro: 25 - El aseguradora "+ str03.trim()
									+ " no es el misma de la agenda.");
	
						String str04 = linha.substring(10, 20); // 10 identifica el
																// Usuario
						if (str04.length() != 10)
							erros.add("Linea = "+ contador+ " Coluna 18~29 - Erro: 04 - Tamaño de registro inválido.");
	
						responsavel = (Usuario) usuarioHome.obterUsuarioPorChave(str04.trim());
	
						if (responsavel == null)
							erros.add("Linea = " + contador+ " - Erro: 10 - Usuario " + str04.trim()+ " inexistente.");
	
						String str05 = linha.substring(20, 24); // 04 Fecha de
																// generación del
																// archivo
						if (str05.length() != 4)
							erros.add("Linea = "+ contador+ " Coluna 29~33 - Erro: 04 - Tamañ de registro inválido.");
						String str06 = linha.substring(24, 26); // 02 Fecha de
																// generación del
																// archivo
						if (str06.length() != 2)
							erros.add("Linea = "+ contador	+ " Coluna 33~35 - Erro: 04 - Tamaño de registro inválido.");
						String str07 = linha.substring(26, 28); // 02 Fecha de
																// generación del
																// archivo
						if (str07.length() != 2)
							erros.add("Linea = "+ contador+ " Coluna 35~37 - Erro: 04 - Tamaño de registro inválido.");
						String str08 = linha.substring(28, 32); // 04 Año / Mes del
																// Movimiento
						if (str08.length() != 4)
							erros.add("Linea = "+ contador	+ " Coluna 37~41 - Erro: 04 - Tamaño de registro inválido.");
	
						ano = str08;
	
						String str09 = linha.substring(32, 34); // 02 Mes del
																// Movimiento
						if (str09.length() != 2)
							erros.add("Linea = "+ contador+ " Coluna 41~43 - Erro: 04 - Tamaño de registro inválido.");
	
						mes = str09;
	
						if (Integer.parseInt(str09) != this.obterMesMovimento()	|| Integer.parseInt(str08) != this.obterAnoMovimento())
							erros.add("Linea = "+ contador	+ " - Erro: 23 - Mes/Año del movimento de la agenda, es diferente del Mes/Año del Archivo");
	
						String str10 = linha.substring(34, 44); // 10 Número total
																// de registros
						if (str10.length() != 10)
							erros.add("Linea = "+ contador	+ " Coluna 43~53 - Erro: 04 - Tamaño de registro inválido.");
	
						numeroRegistros = Integer.parseInt(str10);
					}
				} 
				else if (Integer.parseInt(linha.substring(5, 7)) == 2)
				{ /* Corpo *//*
																					   * Rergistro
																					   * 2 e
																					   * 3
																					   */
					if (Integer.parseInt(linha.substring(5, 7)) == 2)
						if (linha.length() != 142)
						{
							erros.add("Linea = " + contador+ " - Erro: 04 - Tamaño de registro inválido.");
							break;
						}
	
					/* Rergistro 2 */
	
					mf = (MovimentacaoFinanceiraConta) this.getModelManager().getEntity("MovimentacaoFinanceiraConta");
	
					String str01 = linha.substring(0, 5); // 05 Número secuencial
					if (str01.length() != 5)
						erros.add("Linea = "+ contador+ " Coluna 0~5 - Erro: 04 - Tamaño de registro inválido.");
	
					sequencial = Integer.parseInt(str01);
	
					if (sequencial != contador)
						erros.add("Linea = " + contador+ " - Erro: 02 - La secuencia de la numeración del archivo no esta en el orden secuencial y creciente");
	
					String str02 = linha.substring(5, 7); // 02 Identifica el
														  // registro
					if (str02.length() != 2)
						erros.add("Linea = "+ contador+ " Coluna 5~7 - Erro: 04 - Tamaño de registro inválido.");
					String str03 = linha.substring(7, 17); // 10 Cuenta contable
	
					if (str03.length() != 10)
						erros.add("Linea = "+ contador+ " Coluna 7~17 - Erro: 04 - Tamaño de registro inválido.");
	
					String strNivel = linha.substring(7, 9);
	
					entidade = (Entidade) entidadeHome.obterEntidadePorApelido(str03);
					conta = null;
	
					if (entidade instanceof Conta)
						conta = (Conta) entidade;
	
					if (conta == null)
						erros.add("Linea = " + contador + " - Erro: 14 - Cuenta "+ str03.trim() + " inexistente o inválida.");
					else if (!conta.obterAtivo())
						erros.add("Linea = " + contador + " - Erro: 19 - Cuenta "+ str03.trim() + " no esta activa.");
	
					String str04 = linha.substring(17, 27); // 10
					if (str04.length() != 10)
						erros.add("Linea = "+ contador	+ " Coluna 17~27 - Erro: 04 - Tamaño de registro inválido.");
					String str05 = linha.substring(27, 49); // 22 Total del
															// movimiento de débito
					if (str05.length() != 22)
						erros.add("Linea = "+ contador+ " Coluna 27~49 - Erro: 04 - Tamaño de registro inválido.");
					String str06 = linha.substring(49, 71); // 22 Total del
															// movimiento de crédito
					if (str06.length() != 22)
						erros.add("Linea = "+ contador+ " Coluna 49~71 - Erro: 04 - Tamaño de registro inválido.");
					String str07 = linha.substring(71, 72); // 01 Estado del saldo
															// anterior
					if (str07.length() != 1)
						erros.add("Linea = "+ contador+ " Coluna 71~72 - Erro: 04 - Tamaño de registro inválido.");
					String str08 = linha.substring(72, 94); // 22 Saldo del mes
															// anterior
					if (str08.length() != 22)
						erros.add("Linea = "+ contador+ " Coluna 72~94 - Erro: 04 - Tamaño de registro inválido.");
					String str09 = linha.substring(94, 95); // 01 Estado del saldo
															// actual
					if (str09.length() != 1)
						erros.add("Linea = "+ contador+ " Coluna 94~95 - Erro: 04 - Tamaño de registro inválido.");
					String str10 = linha.substring(95, 117); // 22 Saldo actual
					if (str10.length() != 22)
						erros.add("Linea = "+ contador	+ " Coluna 95~117 - Erro: 04 - Tamaño de registro inválido.");
					String str11 = linha.substring(117, 118); // 01 Estado del total
															  // de moneda
															  // extranjera
					if (str11.length() != 1)
						erros.add("Linea = "+ contador+ " Coluna 117~118 - Erro: 04 - Tamaño de registro inválido.");
					String str12 = linha.substring(118, 140); // 22 Total de moneda
															  // extranjera
					if (str12.length() != 22)
						erros.add("Linea = "+ contador	+ " Coluna 118~140 - Erro: 04 - Tamaño de registro inválido.");
	
					if (conta != null)
					{
						mf.atribuirOrigem(seguradora);
						mf.atribuirResponsavel(responsavel);
						dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse("01/" + mes + "/" + ano);
						mf.atribuirDataPrevista(dataInicio);
						mf.atribuirConta(conta);
	
						if (strNivel.equals("01") || strNivel.equals("05") || strNivel.equals("06"))
						{
							if (str07.equals("D"))
								mf.atribuirSaldoAnterior(new BigDecimal(str08));
							else
								mf.atribuirSaldoAnterior(new BigDecimal(str08).multiply(new BigDecimal("-1")));
						} 
						else if (strNivel.equals("02") || strNivel.equals("03") || strNivel.equals("04") || strNivel.equals("07"))
						{
							if (str07.equals("D"))
								mf.atribuirSaldoAnterior(new BigDecimal(str08).multiply(new BigDecimal("-1")));
							else
								mf.atribuirSaldoAnterior(new BigDecimal(str08));
						}
	
						mf.atribuirDebito(new BigDecimal(str05));
						mf.atribuirCredito(new BigDecimal(str06));
	
						if (strNivel.equals("01") || strNivel.equals("05") || strNivel.equals("06"))
						{
							if (str09.equals("D"))
								mf.atribuirSaldoAtual(new BigDecimal(str10));
							else
								mf.atribuirSaldoAtual(new BigDecimal(str10).multiply(new BigDecimal("-1")));
							
							//System.out.println(conta.obterApelido() + " Saldo atual: " + str10);
						}
						else if (strNivel.equals("02") || strNivel.equals("03")	|| strNivel.equals("04") || strNivel.equals("07"))
						{
							if (str09.equals("D"))
								mf.atribuirSaldoAtual(new BigDecimal(str10).multiply(new BigDecimal("-1")));
							else
								mf.atribuirSaldoAtual(new BigDecimal(str10));
							
							//System.out.println(conta.obterApelido() + " Saldo atual: " + str10);
						}
	
						if (strNivel.equals("01") || strNivel.equals("05") || strNivel.equals("06"))
						{
							if (str11.equals("D"))
								mf.atribuirSaldoEstrangeiro(new BigDecimal(str12));
							else
								mf.atribuirSaldoEstrangeiro(new BigDecimal(str12).multiply(new BigDecimal("-1")));
							
						} 
						else if (strNivel.equals("02") || strNivel.equals("03")|| strNivel.equals("04") || strNivel.equals("07"))
						{
							if (str11.equals("D"))
								mf.atribuirSaldoEstrangeiro(new BigDecimal(str12).multiply(new BigDecimal("-1")));
							else
								mf.atribuirSaldoEstrangeiro(new BigDecimal(str12));
						}
	
						mf.atribuirTitulo("Movimiento da Cuenta "+ conta.obterCodigo());
	
						this.movimentacaoes.add(mf);
	
						if (this.saldoAtualContaTotalizado.containsKey(conta.obterCodigo()))
							erros.add("Linea = "+ contador+ " - Erro: 06 - Existe duplicidad del registro "	+ conta.obterCodigo());
						else
						{
							this.contas.add(conta);
	
							//BigDecimal saldoAtual = new BigDecimal(new Double(mf.obterSaldoAtual()).toString()).setScale(30);
							BigDecimal saldoAtual = mf.obterSaldoAtual();
							BigDecimal saldoAnterior = mf.obterSaldoAnterior();
							BigDecimal credito = mf.obterCredito();
							BigDecimal debito = mf.obterDebito();
							BigDecimal saldoMoedaEstrangeira = mf.obterSaldoEstrangeiro();
							
							/*if(conta.obterApelido().equals("0605010101") || conta.obterApelido().equals("0701010101") || conta.obterApelido().equals("0701010110") || conta.obterApelido().equals("0701010111") || conta.obterApelido().equals("0701010203"))
							{
								DecimalFormat formatador = new DecimalFormat();
								//formatador.applyPattern("#,##0.00");
								
								System.out.println("-------- " + conta.obterApelido() + " -------");
								System.out.println("str10 " + str10);
								System.out.println("saldoAtual " + saldoAtual.toPlainString());
								System.out.println("saldoAnterior " + formataValor.format(saldoAnterior));
								System.out.println("credito " + formataValor.format(credito));
								System.out.println("debito " + formataValor.format(debito));
							}*/
	
							this.saldoAtualContaTotalizado.put(conta.obterCodigo(),saldoAtual);
							this.saldoAnteriorContaTotalizado.put(conta.obterCodigo(), saldoAnterior);
							this.creditoContaTotalizado.put(conta.obterCodigo(),credito);
							this.debitoContaTotalizado.put(conta.obterCodigo(),debito);
							this.saldoMoedaEstrangeiraContaTotalizado.put(conta.obterCodigo(), saldoMoedaEstrangeira);
						}
	
						for (Entidade e : conta.obterSuperiores())
						{
							if (e instanceof ClassificacaoContas)
							{
								ClassificacaoContas cContas2 = (ClassificacaoContas) e;
								if (this.saldoAtualClassificacaoContasTotalizado.containsKey(cContas2.obterCodigo()))
								{
									BigDecimal valor = new BigDecimal(this.saldoAtualClassificacaoContasTotalizado.get(cContas2.obterCodigo()).toString());
									//BigDecimal valor2 = valor.add(new BigDecimal(new Double(mf.obterSaldoAtual()).toString()));
									BigDecimal valor2 = valor.add(mf.obterSaldoAtual());
									
									/*if(cContas2.obterApelido().equals("0605010000"))
									{
										System.out.println(formataValor.format(valor) + " + Saldo atual " + mf.obterConta().obterApelido() + " " + formataValor.format(mf.obterSaldoAtual()));
									}*/
									
									this.saldoAtualClassificacaoContasTotalizado.remove(cContas2.obterCodigo());
									this.saldoAtualClassificacaoContasTotalizado.put(cContas2.obterCodigo(),valor2);
								} 
								else
								{
									/*if(cContas2.obterApelido().equals("0605010000"))
									{
										System.out.println("Saldo atual " + mf.obterConta().obterApelido() + " " + formataValor.format(mf.obterSaldoAtual()));
									}*/
									
									//BigDecimal novoValor = new BigDecimal(new Double(mf.obterSaldoAtual()).toString());
									BigDecimal novoValor = mf.obterSaldoAtual();
									this.saldoAtualClassificacaoContasTotalizado.put(cContas2.obterCodigo(),novoValor);
								}
	
								if (this.saldoAnteriorClassificacaoContasTotalizado.containsKey(cContas2.obterCodigo()))
								{
									BigDecimal valor = new BigDecimal(this.saldoAnteriorClassificacaoContasTotalizado.get(cContas2.obterCodigo()).toString());
									BigDecimal valor2 = valor.add(mf.obterSaldoAnterior());
									
									this.saldoAnteriorClassificacaoContasTotalizado.remove(cContas2.obterCodigo());
									this.saldoAnteriorClassificacaoContasTotalizado.put(cContas2.obterCodigo(),	valor2);
								}
								else
								{
									BigDecimal novoValor = mf.obterSaldoAnterior();
									this.saldoAnteriorClassificacaoContasTotalizado.put(cContas2.obterCodigo(),novoValor);
								}
	
								if (this.saldoMoedaEstrangeiraClassificacaoContasTotalizado.containsKey(cContas2.obterCodigo()))
								{
									BigDecimal valor = new BigDecimal(this.saldoMoedaEstrangeiraClassificacaoContasTotalizado.get(cContas2.obterCodigo()).toString());
									BigDecimal valor2 = valor.add(mf.obterSaldoEstrangeiro());
									this.saldoMoedaEstrangeiraClassificacaoContasTotalizado.remove(cContas2.obterCodigo());
									this.saldoMoedaEstrangeiraClassificacaoContasTotalizado.put(cContas2.obterCodigo(),	valor2);
								}
								else
								{
									BigDecimal novoValor = mf.obterSaldoEstrangeiro();
									this.saldoMoedaEstrangeiraClassificacaoContasTotalizado.put(cContas2.obterCodigo(),	novoValor);
								}
	
								if (this.creditoClassificacaoContasTotalizado.containsKey(cContas2.obterCodigo()))
								{
									BigDecimal valor = new BigDecimal(this.creditoClassificacaoContasTotalizado.get(cContas2.obterCodigo()).toString());
									BigDecimal valor2 = valor.add(mf.obterCredito());
									this.creditoClassificacaoContasTotalizado.remove(cContas2.obterCodigo());
									this.creditoClassificacaoContasTotalizado.put(cContas2.obterCodigo(), valor2);
									
									//System.out.println(cContas2.obterCodigo() + " Credito: " + valor2);
								} 
								else 
								{
									BigDecimal novoValor = mf.obterCredito();
									this.creditoClassificacaoContasTotalizado.put(cContas2.obterCodigo(), novoValor);
									
									//System.out.println(cContas2.obterCodigo() + " Credito: " + novoValor);
								}
	
								if (this.debitoClassificacaoContasTotalizado.containsKey(cContas2.obterCodigo()))
								{
									BigDecimal valor = new BigDecimal(this.debitoClassificacaoContasTotalizado.get(cContas2.obterCodigo()).toString());
									BigDecimal valor2 = valor.add(mf.obterDebito());
									this.debitoClassificacaoContasTotalizado.remove(cContas2.obterCodigo());
									this.debitoClassificacaoContasTotalizado.put(cContas2.obterCodigo(), valor2);
								}
								else
								{
									BigDecimal novoValor = mf.obterDebito();
									this.debitoClassificacaoContasTotalizado.put(cContas2.obterCodigo(), novoValor);
								}
	
								if (!this.classificacaoContasTotalizadas.contains(cContas2)	&& !cContas2.obterCodigo().equals("0000000000"))
									this.classificacaoContasTotalizadas.add(cContas2);
							}
						}
					}
				}
				else if (Integer.parseInt(linha.substring(5, 7)) == 3)
				{
					if (linha.length() != 52)
					{
						erros.add("Linea = " + contador+ " - Erro: 04 - Tamaño de registro inválido.");
						break;
					}
	
					/* Rergistro 3 */
					String str01 = linha.substring(0, 5); // 05 Número secuencial
					if (str01.length() != 5)
						erros.add("Linea = "+ contador+ " Coluna 0~5 - Erro: 04 - Tamaño de registro inválido.");
	
					sequencial = Integer.parseInt(str01);
	
					if (sequencial != contador)
						erros.add("Linea = " + contador+ " - Erro: 02 Número sequencial invalido.");
	
					String str02 = linha.substring(5, 7); // 02 Identifica el
														  // registro
					if (str02.length() != 2)
						erros.add("Linea = "+ contador+ " Coluna 5~7 - Erro: 04 - Tamaño de registro inválido.");
					String str03 = linha.substring(7, 17); // 10 Cuenta contable
					if (str03.length() != 10)
						erros.add("Linea = "+ contador+ " Coluna 7~17 - Erro: 04 - Tamaño de registro invalido.");
	
					String strNivel = linha.substring(7, 9);
	
					cConta = null;
					entidade = (Entidade) entidadeHome.obterEntidadePorApelido(str03);
	
					if (entidade instanceof ClassificacaoContas)
						cConta = (ClassificacaoContas) entidade;
	
					if (cConta == null)
						erros.add("Linea = " + contador + " - Erro: 14 - "+ str03.trim()+ " no es una clasificación de cuenta.");
	
					String str04 = linha.substring(17, 27); // 10
					if (str04.length() != 10)
						erros.add("Linea = "+ contador+ " Coluna 17~27 - Erro: 04 - Tamaño de registro inválido.");
					String str05 = linha.substring(27, 28); // 01 Estado del total
															// del nivel
					if (str05.length() != 1)
						erros.add("Linea = "+ contador+ " Coluna 27~28 - Erro: 04 - Tamaño de registro inválido.");
					String str06 = linha.substring(28, 50); // 22 Total del nivel
					if (str06.length() != 22)
						erros.add("Linea = "+ contador+ " Coluna 28~50 - Erro: 04 - Tamaño de registro inválido.");
	
					if (cConta != null)
					{
						if (!this.classificacaoContas.contains(cConta))
						{
							this.classificacaoContas.add(cConta);
							
							//if(cConta.obterApelido().equals("0605010000"))
								//System.out.println("");
	
							if (!this.saldoAtualClassificacaoContasTotalizado.containsKey(cConta.obterCodigo()))
								erros.add("Linea = "+ contador+ " - Erro: 24 - Cuenta "	+ cConta.obterCodigo()+ " totalizada no posee registro de asiento del tipo 02");
							else
							{
								double valorTotalizadoMemoria = Double.parseDouble(this.saldoAtualClassificacaoContasTotalizado.get(cConta.obterCodigo()).toString());
								double valorTotalizadoArquivo = Double.parseDouble(str06);
	
								if (strNivel.equals("01") || strNivel.equals("05")|| strNivel.equals("06"))
								{
									if (str05.equals("C"))
										valorTotalizadoArquivo = valorTotalizadoArquivo	* -1;
								}
								else if (strNivel.equals("02")
										|| strNivel.equals("03")
										|| strNivel.equals("04")
										|| strNivel.equals("07")) {
									if (str05.equals("D"))
										valorTotalizadoArquivo = valorTotalizadoArquivo	* -1;
								}
	
								if (valorTotalizadoMemoria != valorTotalizadoArquivo)
								{
									erros.add("Linea = "+ contador+ " - Erro: 20 - Sumatoria del saldo de la cuenta: "+ cConta.obterCodigo()+ " esta incorrecta.");
									/*System.out.println("Classif.: "	+ cConta.obterCodigo());
									System.out.println("valorTotalizadoMemoria: "+ formataValor.format(valorTotalizadoMemoria));
									System.out.println("valorTotalizadoArquivo: "+ formataValor.format(valorTotalizadoArquivo));
									System.out.println("valorTotalizadoArquivo: "+ str06);
									System.out.println("----------------------: ");*/
								}
							}
						} else
							erros.add("Linea = " + contador+ " - Erro: 21 - Cuenta "+ cConta.obterCodigo() + " esta em duplicidad");
					}
				}
				else if (Integer.parseInt(linha.substring(5, 7)) == 4)
				{
					if (linha.length() != 137)
					{
						erros.add("Linea = " + contador+ " - Erro: 04 - Tamaño de registro inválido.");
						break;
					}
					
					codigoInstrumento = null;
					emissor = null;
					qualificadora = null;
					qualificacao = null;
					localidade = null;
					
					String codAtivoStr = linha.substring(7,8);
					String codInstrumentoStr = linha.substring(8,11);
					String dataExtincaoStr = linha.substring(11,19);
					String emissorStr = linha.substring(19,23);
					String qualificadoraStr = linha.substring(23,25);
					String qualificacaoStr = linha.substring(25,27);
					String valorStr = linha.substring(27,49);
					String porcentagemAcoesStr = linha.substring(49,56);
					String mercadoStr = linha.substring(56,60);
					String patrimonioStr = linha.substring(60,82);
					String numeroFincaStr = linha.substring(82,91);
					String localidadeStr = linha.substring(91,94);
					String contaCorrenteStr = linha.substring(94,109);
					String restringidoStr = linha.substring(109,110);
					String valorRepresentativoStr = linha.substring(110,132);
					String tipoInversaoStr = linha.substring(132,135);
					
					int codAtivo = 0,codInstrumento = 0, codEmissor = 0, codQualificadora = 0, codQualificacao = 0, numeroFinca = 0, codLocalidade = 0, tipoInversao = 0;
					Date dataExtincao = null;
					BigDecimal valor = new BigDecimal(0);
					BigDecimal porcentagemAcoes = new BigDecimal(0);
					BigDecimal patrimonio = new BigDecimal(0);
					BigDecimal valorRepresentativo = new BigDecimal(0);
					
					if(uteis.eNumero(codAtivoStr))
					{
						codAtivo = Integer.valueOf(codAtivoStr);
						
						if(codAtivo!=1 && codAtivo!=2)
							erros.add("Linea = " + contador+ " - Error: 352 - Código del activo diferente de 1 y 2. Información del archivo = " + codAtivoStr);
					}
					else
						erros.add("Linea = " + contador+ " - Error: 351 - Código del activo no es numérico. Información del archivo = " + codAtivoStr);
						
					if(uteis.eNumero(codInstrumentoStr))
					{
						codInstrumento = Integer.valueOf(codInstrumentoStr);
						codigoInstrumento = codigoHome.obterCodigoInstrumento(codInstrumento);
						if(codigoInstrumento == null)
							erros.add("Linea = " + contador+ " - Error: 370 - Código del Instrumento "+codInstrumentoStr +" no fue encontrado");
					}
					else
						erros.add("Linea = " + contador+ " - Error: 353 - Código del Instrumento no es numérico. Información del archivo = " + codInstrumentoStr);
					
					if((codInstrumento >= 1 && codInstrumento <= 3) || codInstrumento == 5)
					{
						if(uteis.eDataContabil(dataExtincaoStr))
						{
							String data = dataExtincaoStr.substring(6,8)+"/"+dataExtincaoStr.substring(4,6)+"/"+dataExtincaoStr.substring(0,4);
							dataExtincao = new SimpleDateFormat("dd/MM/yyyy").parse(data);
						}
						else
							erros.add("Linea = " + contador+ " - Error: 354 - Fecha de extinción o de maturación no es una fecha. Información del archivo = " + dataExtincaoStr);
					}
					
					if((codInstrumento >= 1 && codInstrumento <= 12) || codInstrumento == 18)
					{
						if(uteis.eNumero(emissorStr))
						{
							codEmissor = Integer.valueOf(emissorStr);
							emissor = emissorHome.obterEmissor(codEmissor);
							if(emissor == null)
								erros.add("Linea = " + contador+ " - Error: 371 - Código del Emisor "+emissorStr +" no fue encontrado");
						}
						else
							erros.add("Linea = " + contador+ " - Error: 355 - Código del Emisor no es numérico. Información del archivo = " + emissorStr);
					}
					
					if(codInstrumento == 3 || codInstrumento == 5 || codInstrumento == 7 || codInstrumento == 9 || codInstrumento == 11)
					{
						if(uteis.eNumero(qualificadoraStr))
						{
							codQualificadora = Integer.valueOf(qualificadoraStr);
							qualificadora = qualificadoraHome.obterQualificadora(codQualificadora);
							if(qualificadora == null)
								erros.add("Linea = " + contador+ " - Error: 372 - Código de la Calificadora "+qualificadoraStr +" no fue encontrado");
						}
						else
							erros.add("Linea = " + contador+ " - Error: 356 - Código de la Calificadora no es numérico. Información del archivo = " + qualificadoraStr);
						
						if(uteis.eNumero(qualificacaoStr))
						{
							codQualificacao = Integer.valueOf(qualificacaoStr);
							qualificacao = qualificacaoHome.obterQualificacao(codQualificacao);
							if(qualificacao == null)
								erros.add("Linea = " + contador+ " - Error: 373 - Código de la Calificación "+qualificacaoStr +" no fue encontrado");
						}
						else
							erros.add("Linea = " + contador+ " - Error: 357 - Código de la Calificación no es numérico. Información del archivo = " + qualificacaoStr);
					}
					
					if(uteis.eNumero(valorStr))
						valor = new BigDecimal(valorStr);
					else
						erros.add("Linea = " + contador+ " - Error: 358 - Valor no es numérico. Información del archivo = " + valorStr);
					
					if(codInstrumento >= 8 && codInstrumento <= 10)
					{
						if(uteis.eNumero(porcentagemAcoesStr))
							porcentagemAcoes =  new BigDecimal(porcentagemAcoesStr);
						else
							erros.add("Linea = " + contador+ " - Error: 359 - Porcentaje de las acciones no es numérico. Información del archivo = " + porcentagemAcoesStr);
					}
					
					if(codInstrumento == 9 || codInstrumento == 11)
					{
						if(!mercadoStr.toLowerCase().equals("i") && !mercadoStr.toLowerCase().equals("m") && !mercadoStr.toLowerCase().equals("p") && !mercadoStr.toLowerCase().equals("s"))
							erros.add("Linea = " + contador+ " - Error: 360 - Mercado diferente de I, M, P, S. Información del archivo = " + mercadoStr);
					}
					else
					{
						if(!mercadoStr.equals(""))
							erros.add("Linea = " + contador+ " - Error: 361 - Mercado debe estar en blanco. Información del archivo = " + mercadoStr);
					}
					
					if(codInstrumento == 4 || codInstrumento == 6)
					{
						if(uteis.eNumero(patrimonioStr))
							patrimonio =  new BigDecimal(patrimonioStr);
						else
							erros.add("Linea = " + contador+ " - Error: 362 - Patrimonio no es numérico. Información del archivo = " + patrimonioStr);
					}
					
					if(codInstrumento == 16 || codInstrumento == 17)
					{
						if(uteis.eNumero(numeroFincaStr))
							numeroFinca = Integer.valueOf(numeroFincaStr);
						else
							erros.add("Linea = " + contador+ " - Error: 363 - Número de Finca no es numérico. Información del archivo = " + numeroFincaStr);
						
						if(uteis.eNumero(localidadeStr))
						{
							codLocalidade = Integer.valueOf(localidadeStr);
							localidade = localidadeHome.obterLocalidade(codLocalidade);
							if(localidade == null)
								erros.add("Linea = " + contador+ " - Error: 374 - Localidad "+localidadeStr +" no fue encontrada");
						}
						else
							erros.add("Linea = " + contador+ " - Error: 364 - Localidad no es numérico. Información del archivo = " + localidadeStr);
						
						if(contaCorrenteStr.equals(""))
							erros.add("Linea = " + contador+ " - Error: 365 - Cuenta Corriente Catastral es obligatoria");
						
						if(!restringidoStr.toLowerCase().equals("a") && !restringidoStr.toLowerCase().equals("c") && !restringidoStr.toLowerCase().equals("i") && !restringidoStr.toLowerCase().equals("u") && !restringidoStr.toLowerCase().equals("v") && !restringidoStr.toLowerCase().equals("n"))
							erros.add("Linea = " + contador+ " - Error: 366 - Restringido o no de acuerdo al Artículo 6° diferente de A, C, I, U, V, N. Información del archivo = " + restringidoStr);
						
						if(uteis.eNumero(valorRepresentativoStr))
							valorRepresentativo =  new BigDecimal(valorRepresentativoStr);
						else
							erros.add("Linea = " + contador+ " - Error: 367 - Valor Representativo no es numérico. Información del archivo = " + valorRepresentativoStr);
					}
					else
					{
						if(!contaCorrenteStr.equals(""))
							erros.add("Linea = " + contador+ " - Error: 368 - Cuenta Corriente Catastral debe estar en blanco. Información del archivo = " + contaCorrenteStr);
						if(!restringidoStr.equals(""))
							erros.add("Linea = " + contador+ " - Error: 369 - Restringido o no de acuerdo al Artículo 6° debe estar en blanco. Información del archivo = " + restringidoStr);
					}
					
					if(codInstrumento == 18)
					{
						if(uteis.eNumero(tipoInversaoStr))
							tipoInversao = Integer.valueOf(tipoInversaoStr);
						else
							erros.add("Linea = " + contador+ " - Error: 375 - Tipo de Inversión no es numérico. Información del archivo = " + tipoInversaoStr);
					}
					else
					{
						if(!tipoInversaoStr.equals(""))
							erros.add("Linea = " + contador+ " - Error: 376 - Tipo de Inversión debe estar en blanco. Información del archivo = " + tipoInversaoStr);
					}
					
					if(erros.size() == 0)
					{
						mfAlertaTemprana = (MfAlertaTrempana) this.getModelManager().getEntity("MfAlertaTrempana");
						
						mfAlertaTemprana.atribuirAno(Integer.valueOf(ano));
						mfAlertaTemprana.atribuirMes(Integer.valueOf(mes));
						mfAlertaTemprana.atribuirCodigoAtivo(codAtivo);
						mfAlertaTemprana.atribuirCodigoInstrumento(codigoInstrumento);
						mfAlertaTemprana.atribuirContaCorrente(contaCorrenteStr);
						mfAlertaTemprana.atribuirDataExtincao(dataExtincao);
						mfAlertaTemprana.atribuirEmissor(emissor);
						mfAlertaTemprana.atribuirLocalidade(localidade);
						mfAlertaTemprana.atribuirMercado(mercadoStr);
						mfAlertaTemprana.atribuirNumeroFinca(numeroFinca);
						mfAlertaTemprana.atribuirPatrimonio(patrimonio);
						mfAlertaTemprana.atribuirPorcentagemAcoes(porcentagemAcoes);
						mfAlertaTemprana.atribuirQualificacao(qualificacao);
						mfAlertaTemprana.atribuirQualificadora(qualificadora);
						mfAlertaTemprana.atribuirRestringido(restringidoStr);
						mfAlertaTemprana.atribuirTipoInversao(tipoInversao);
						mfAlertaTemprana.atribuirValor(valor);
						mfAlertaTemprana.atribuirValorRepresentativo(valorRepresentativo);
						
						movimentacaoesAlertaTemprana.add(mfAlertaTemprana);
					}
				}
				
				contador++;
			}
			
			for (ClassificacaoContas cContas : this.classificacaoContasTotalizadas) 
			{
				if (!this.classificacaoContas.contains(cContas))
					erros.add("Erro: 22 - La Cuenta " + cContas.obterCodigo()+ " no fue totalizada");
			}

			//Collection totContasNivel3 = new ArrayList();
			Collection<String> validaTotContasNivel3 = new ArrayList<>();
		
			validaTotContasNivel3 = this.validarTotatizadorContasNivel3(saldoAtualContaTotalizado.values());
			erros = validaTotContasNivel3;
		
			if (erros.size() == 0)
				this.compararContas();
			
			file.write("Comparou as contas \r\n");
		
			if (erros.size() == 0)
			{
				if (this.obterMesMovimento() != 7)
				{
					this.compararSaldoAtualComAnterior();
					file.write("Comparou Saldo atual com anterior \r\n");
				}
			}
		
			if (erros.size() == 0) 
			{
				this.gravarMovimentacaoes();
				file.write("Gravou Movimentações \r\n");
				this.copiarArquivo();
				file.write("Copiou os arquivos \r\n");
			}
		}
		catch (Exception e) 
		{
			System.out.println(e.toString());
			erros.add(e.toString() + " - Linea: " + contador);
			file.write(e.toString() + "\r\n");
			file.close();
			erro = true;
		}
		
		if(!erro)
			file.close();
		
		return erros;
	}

	/////////////////// FIM DO MÉTODO PRA VALIDAR O ARQUIVO CONTABIL
	// ////////////////////////////////////////

	private void copiarArquivo() throws Exception
	{
		InputStream is = null;
		OutputStream os = null;

		byte[] buffer;
		boolean success = true;
		try {
			//is = new FileInputStream("" + "/Aseguradoras/Archivos/" +
			// this.nomeArquivo + ".txt");
			is = new FileInputStream("" + "C:/Aseguradoras/Archivos/"
					+ this.nomeArquivo + ".txt");

			//os = new FileOutputStream("" + "/Aseguradoras/Backup/" +
			// this.nomeArquivo + "_Backup.txt" );
			os = new FileOutputStream("" + "C:/Aseguradoras/Backup/"
					+ this.nomeArquivo + "_Backup.txt");

			buffer = new byte[is.available()];
			is.read(buffer);
			os.write(buffer);
		} catch (IOException e) {
			success = false;
		} catch (OutOfMemoryError e) {
			success = false;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

		File arquivo = new File("C:/Aseguradoras/Archivos/" + this.nomeArquivo
				+ ".txt");
		//File arquivo = new File("/Aseguradoras/Archivos/" + this.nomeArquivo
		// + ".txt");

		arquivo.delete();
		System.out.println("arquivo.delete(): " + arquivo.delete());

	}

	private void compararContas() throws Exception
	{
		DecimalFormat formatador = new DecimalFormat();
		formatador.applyPattern("#,##0.00");
		BigDecimal saldoAtual,saldoAnterior,credito,debito,saldoMoedaEstrangeira,tot;
		
		for (Conta conta : this.contas)
		{
			saldoAtual = this.saldoAtualContaTotalizado.get(conta.obterCodigo());
			saldoAnterior = this.saldoAnteriorContaTotalizado.get(conta.obterCodigo());
			credito = this.creditoContaTotalizado.get(conta.obterCodigo());
			debito = this.debitoContaTotalizado.get(conta.obterCodigo());
			saldoMoedaEstrangeira = this.saldoMoedaEstrangeiraContaTotalizado.get(conta.obterCodigo());

			if (conta.obterApelido().substring(0, 2).equals("01") || conta.obterApelido().substring(0, 2).equals("05") || conta.obterApelido().substring(0, 2).equals("06"))
			{
				//double tot = (saldoAnterior + debito) - credito;
				tot = (saldoAnterior.add(debito)).subtract(credito);
				tot.setScale(30);
				
				if (saldoAtual.doubleValue() != tot.doubleValue())
				{
					erros.add("Erro: 348 - Cuenta " + conta.obterApelido()	+ " el saldo atual no cuadra");
					System.out.println("1 - " +conta.obterApelido()+" saldoAtual " + saldoAtual + " tot " + tot);
				}
			} 
			else if (conta.obterApelido().substring(0, 2).equals("02") || conta.obterApelido().substring(0, 2).equals("03")	|| conta.obterApelido().substring(0, 2).equals("04") || conta.obterApelido().substring(0, 2).equals("07"))
			{
				//double tot = (saldoAnterior - debito) + credito;
				tot = (saldoAnterior.subtract(debito)).add(credito);

				if (saldoAtual.doubleValue() != tot.doubleValue())
				{
					erros.add("Erro: 348 - Cuenta " + conta.obterApelido()+ " el saldo atual no cuadra");
					System.out.println("2 - " +conta.obterApelido()+" saldoAtual " + saldoAtual + " tot " + tot);
				}
			}

		}
	}
	
	private void compararSaldoAtualComAnterior() throws Exception
	{
		ClassificacaoContas cContas1 = (ClassificacaoContas) home.obterEntidadePorApelido("0100000000");
		ClassificacaoContas cContas2 = (ClassificacaoContas) home.obterEntidadePorApelido("0200000000");
		ClassificacaoContas cContas3 = (ClassificacaoContas) home.obterEntidadePorApelido("0300000000");
		ClassificacaoContas cContas4 = (ClassificacaoContas) home.obterEntidadePorApelido("0400000000");
		ClassificacaoContas cContas5 = (ClassificacaoContas) home.obterEntidadePorApelido("0500000000");
		ClassificacaoContas cContas6 = (ClassificacaoContas) home.obterEntidadePorApelido("0600000000");
		ClassificacaoContas cContas7 = (ClassificacaoContas) home.obterEntidadePorApelido("0700000000");

		Collection<ClassificacaoContas> contas = new ArrayList<>();
		contas.add(cContas1);
		contas.add(cContas2);
		contas.add(cContas3);
		contas.add(cContas4);
		contas.add(cContas5);
		contas.add(cContas6);
		contas.add(cContas7);

		int mesModificado = Integer.parseInt(mes);
		int anoModificado = Integer.parseInt(ano);

		if (mesModificado == 1)
		{
			mesModificado = 12;
			anoModificado--;
		}
		else
			mesModificado--;

		String mesModificado2 = new Integer(mesModificado).toString();

		if (mesModificado2.length() == 1)
			mesModificado2 = "0" + mesModificado2;

		String mesAnoModificao = mesModificado2	+ new Integer(anoModificado).toString();
		double saldoAnteriorMesAnterior,saldoAnteriorMesAtual;
		
		for (ClassificacaoContas cContas : contas)
		{
			saldoAnteriorMesAnterior = cContas.obterTotalizacaoExistente(this.obterOrigem(),	mesAnoModificao);

			if (this.saldoAnteriorClassificacaoContasTotalizado.get(cContas.obterCodigo()) != null)
			{
				saldoAnteriorMesAtual = Double.parseDouble(this.saldoAnteriorClassificacaoContasTotalizado.get(cContas.obterCodigo()).toString());

				if (saldoAnteriorMesAtual != saldoAnteriorMesAnterior)
				{
					System.out.println("saldoAnteriorMesAnterior: "	+ saldoAnteriorMesAnterior);
					System.out.println("saldoAnteriorMesAtual: " + saldoAnteriorMesAtual);

					erros.add("Erro: 350 - Saldo Acual de la Cuenta " + cContas.obterApelido() + " no se cuadra en Saldo Anterior");
				}
			}
			
			if(cContas.obterApelido().equals("0100000000") || cContas.obterApelido().equals("0200000000"))
			{
				if(this.saldoAnteriorClassificacaoContasTotalizado.get(cContas.obterCodigo()) != null && this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo())!=null)
				{
					double saldoAnterior = Double.parseDouble(this.saldoAnteriorClassificacaoContasTotalizado.get(cContas.obterCodigo()).toString());
					double saldoAtual = Double.parseDouble(this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo()).toString());
					
					//if(saldoAtual == saldoAnterior)
						//erros.add("Erro: 350 - Saldo Actual de la Cuenta " + cContas.obterApelido() + " és el mismo que el Saldo Anterior (en el archivo)");
				}
			}
		}
	}

	private void gravarMovimentacaoes() throws Exception 
	{
		for(MovimentacaoFinanceiraConta mf : this.movimentacaoes) 
			mf.incluir();
		
		for(MfAlertaTrempana mf : this.movimentacaoesAlertaTemprana) 
			mf.incluir();

		this.gravarTotalizacoes();
	}

	private void gravarTotalizacoes() throws Exception
	{
		BigDecimal saldoAtual,saldoAnterior,credito,debito,moedaEstrangeira,saldoMoedaEstrangeira;
		
		for (ClassificacaoContas cContas : this.classificacaoContas)
		{
			saldoAtual = this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo());
			saldoAnterior = this.saldoAnteriorClassificacaoContasTotalizado.get(cContas.obterCodigo());
			credito = this.creditoClassificacaoContasTotalizado.get(cContas.obterCodigo());
			debito = this.debitoClassificacaoContasTotalizado.get(cContas.obterCodigo());
			moedaEstrangeira = this.saldoMoedaEstrangeiraClassificacaoContasTotalizado.get(cContas.obterCodigo());

			//System.out.println(cContas.obterApelido() + " CreditoCcontas: " + credito);
			//System.out.println(cContas.obterApelido() + " DebitoCcontas: " + debito);
			
			cContas.incluirRelatorio(mes + ano, saldoAtual.doubleValue(), debito.doubleValue(), credito.doubleValue(),saldoAnterior.doubleValue(), moedaEstrangeira.doubleValue(), seguradora);
		}

		for (Conta conta : this.contas)
		{
			saldoAtual = this.saldoAtualContaTotalizado.get(conta.obterCodigo());
			saldoAnterior = this.saldoAnteriorContaTotalizado.get(conta.obterCodigo());
			credito = this.creditoContaTotalizado.get(conta.obterCodigo());
			debito = this.debitoContaTotalizado.get(conta.obterCodigo());
			saldoMoedaEstrangeira = this.saldoMoedaEstrangeiraContaTotalizado.get(conta.obterCodigo());
			
			//System.out.println(conta.obterApelido() + " CreditoCcontas: " + credito);
			//System.out.println(conta.obterApelido() + " DebitoCcontas: " + debito);

			conta.incluirRelatorio(mes + ano, saldoAtual.doubleValue(),saldoMoedaEstrangeira.doubleValue(), debito.doubleValue(), credito.doubleValue(), saldoAnterior.doubleValue(),	seguradora);

		}
	}

	private Collection<Conta> contas = new ArrayList<>();

	private Collection<ClassificacaoContas> classificacaoContas = new ArrayList<>();

	private Map<String, BigDecimal> saldoAtualContaTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> saldoAnteriorContaTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> saldoMoedaEstrangeiraContaTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> debitoContaTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> creditoContaTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> saldoAtualClassificacaoContasTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> saldoAnteriorClassificacaoContasTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> saldoMoedaEstrangeiraClassificacaoContasTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> debitoClassificacaoContasTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> creditoClassificacaoContasTotalizado = new TreeMap<String, BigDecimal>();

	private Collection<MovimentacaoFinanceiraConta> movimentacaoes = new ArrayList<>();
	private Collection<MfAlertaTrempana> movimentacaoesAlertaTemprana = new ArrayList<>();

	private Entidade seguradora;

	private String ano;

	private String mes;

	/*
	 * File f = new File("/tmp/teste.txt"); FileOutputStream fo = new
	 * FileOutputStream(f);
	 */

	//System.getProperty("line.separator");
	/*
	 * String caminhoArquivo = getServletContext().getRealPath("/WEB-INF") +
	 * System.getProperty("file.separator") + nomeArq;
	 * 
	 * FileWriter writer = new FileWriter(caminhoArquivo, true); PrintWriter
	 * arqLog = new PrintWriter(writer, true);
	 * 
	 * SimpleDateFormat formataData = new SimpleDateFormat();
	 * formataData.applyPattern("dd/MM/yyyy 'às' HH:mm:ss"); String
	 * dataHoraAtuais = formataData.format(new
	 * Date(System.currentTimeMillis()));
	 * 
	 * BufferedWrite bw = new BufferedWriter(new
	 * FileWriter("/home/use/arquivo.txt")); depois eh so chamar o bw.writer("")
	 * passando uma string se o array for de string,faz um laço percorrendo todo
	 * o array e sai escrevendo no buffer,depois chama bw.flush() para fechar o
	 * buffer
	 * 
	 * arqLog.println("email: " + email + " Data/Hora: " + dataHoraAtuais);
	 * 
	 * arqLog.close(); writer.close();
	 */

	private Collection<String> validarTotatizadorContasNivel3(Collection<BigDecimal> contasTotalizadas) throws Exception
	{
		//EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		//UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome("UsuarioHome");
		
		//FileWriter w = new FileWriter("c:/tmp/ConsParam.txt");

		try
		{
			ClassificacaoContas cContas;
			Entidade entidade;
			
			for (Parametro.Consistencia consistencia : parametro.obterConsistencias())
			{
				//w.write(consistencia.obterSequencial() + "\r\n");
	
				if (consistencia.obterOperando1().substring(0, 1).equals("c"))
				{
					if (home.obterEntidadePorApelido(consistencia.obterOperando1().substring(1,consistencia.obterOperando1().length())) != null)
					{
						entidade = (Entidade) home.obterEntidadePorApelido(consistencia.obterOperando1().substring(1,consistencia.obterOperando1().length()));
						if (entidade instanceof ClassificacaoContas)
						{
							cContas = (ClassificacaoContas) entidade;
							
							/*if(cContas.obterApelido().equals("0305000000"))
								System.out.println("");*/
							
							if (this.saldoAtualClassificacaoContasTotalizado.containsKey(cContas.obterCodigo()))
								this.analisaOperador(cContas, consistencia,	consistencia.obterOperando1(), consistencia.obterOperador(), consistencia.obterOperando2());
						}
					}
				} 
				else if (consistencia.obterOperando1().equals("A"))
				{
					entidade = (Entidade) home.obterEntidadePorApelido(consistencia.obterOperando2().substring(1,consistencia.obterOperando2().length()));
					if (entidade instanceof ClassificacaoContas)
					{
						cContas = (ClassificacaoContas) entidade;
	
						if (this.saldoAtualClassificacaoContasTotalizado.containsKey(cContas.obterCodigo()))
							this.analisaOperador(cContas, consistencia,	consistencia.obterOperando1(), consistencia.obterOperador(), consistencia.obterOperando2());
					}
					else
						this.analisaOperador(null, consistencia, consistencia.obterOperando1(), consistencia.obterOperador(),consistencia.obterOperando2());
				}
				else if (consistencia.obterOperando1().equals("B"))
				{
					entidade = (Entidade) home.obterEntidadePorApelido(consistencia.obterOperando2().substring(1,consistencia.obterOperando2().length()));
					if (entidade instanceof ClassificacaoContas)
					{
						cContas = (ClassificacaoContas) entidade;
	
						if (this.saldoAtualClassificacaoContasTotalizado.containsKey(cContas.obterCodigo()))
							this.analisaOperador(cContas, consistencia,consistencia.obterOperando1(), consistencia.obterOperador(), consistencia.obterOperando2());
					} 
					else
						this.analisaOperador(null, consistencia, consistencia.obterOperando1(), consistencia.obterOperador(),consistencia.obterOperando2());
				}
			}
			
			//w.close();
		}
		catch (Exception e)
		{
			erros.add(e.toString() + " - Error Interno: (validarTotatizadorContasNivel3)\r\n");
			//w.write(e.toString() + "\r\n");
			//w.close();
		}
		
		return erros;
	}

	private BigDecimal A = new BigDecimal("0.00");

	private BigDecimal B =  new BigDecimal("0.00");

	private Collection analisaOperador(ClassificacaoContas cContas,Parametro.Consistencia consistencia, String operando1,String operador, String operando2) throws Exception
	{
		if (operador.equals("=="))
			this.verificarConsistencias(cContas, consistencia, operando1, 1,operando2);
		else if (operador.equals(">="))
			this.verificarConsistencias(cContas, consistencia, operando1, 2,operando2);
		else if (operador.equals(">"))
			this.verificarConsistencias(cContas, consistencia, operando1, 3,operando2);
		else if (operador.equals("<="))
			this.verificarConsistencias(cContas, consistencia, operando1, 4,operando2);
		else if (operador.equals("<"))
			this.verificarConsistencias(cContas, consistencia, operando1, 5,operando2);
		else if (operador.equals("<>"))
			this.verificarConsistencias(cContas, consistencia, operando1, 6,operando2);
		else if (operador.equals("="))
			this.verificarConsistencias(cContas, consistencia, operando1, 7,operando2);
		else if (operador.equals("+"))
			this.verificarConsistencias(cContas, consistencia, operando1, 8,operando2);
		else
			erros.add("Operador Inválido");

		return erros;

	}

	private Collection verificarConsistencias(ClassificacaoContas cContas,Parametro.Consistencia consistencia, String operando1,int operador, String operando2) throws Exception
	{
		BigDecimal totalizacao = new BigDecimal("0.00"); 
		BigDecimal operando2Double = new BigDecimal("0.00");
		
		switch (operador) 
		{
		case 1: 
		{
			if ((operando1.equals("A") && operando2.equals("B")) || (operando1.equals("B") && operando2.equals("A")))
			{
				if (this.A.doubleValue() == this.B.doubleValue())
				{

				}
				else
				{
					System.out.println("A: " + this.A);
					System.out.println("B: " + this.B);

					erros.add(consistencia.obterMensagem());
				}
			}
			else 
			{
				if(cContas.obterApelido().equals("0305000000"))
					System.out.println("");
					
				totalizacao = this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo());
				operando2Double = new BigDecimal(operando2);

				if (totalizacao.doubleValue() == operando2Double.doubleValue()) 
				{

				} 
				else
				{
					System.out.println(cContas.obterApelido() + " - totalizacao: " + totalizacao);
					System.out.println("operando2: " + operando2);
					erros.add(consistencia.obterMensagem());
				}
			}
			
			//System.out.println("1 " + cContas.obterApelido() + ": " + totalizacao);
			
			break;
		}
		case 2: {
			
			if ((operando1.equals("A") && operando2.equals("B")) || operando1.equals("B") && operando2.equals("A"))
			{
				if (this.A.doubleValue() >= this.B.doubleValue())
				{

				}
				else
					erros.add(consistencia.obterMensagem());
			}
			else
			{
				if (operando1.equals("A"))
				{
					if (this.A.doubleValue() >= 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else if (operando1.equals("B"))
				{
					if (this.B.doubleValue() >= 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else
				{
					totalizacao = this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo());

					if (!operando2.equals("A"))
						operando2Double = new BigDecimal(operando2);
					else if (this.A.doubleValue() > 0)
						operando2Double = this.A;

					if(totalizacao.doubleValue() >= operando2Double.doubleValue())
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
			}
			
			//System.out.println("2 " + cContas.obterApelido() + ": " + totalizacao);
			break;
		}
		case 3: {
			
			if ((operando1.equals("A") && operando2.equals("B")) || operando1.equals("B") && operando2.equals("A"))
			{
				if (this.A.doubleValue() > this.B.doubleValue())
				{

				}
				else
					erros.add(consistencia.obterMensagem());
			}
			else
			{
				if (operando1.equals("A"))
				{
					if (this.A.doubleValue() > 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else if (operando1.equals("B"))
				{
					if (this.B.doubleValue() > 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else
				{
					totalizacao = this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo());

					if (!operando2.equals("A"))
						operando2Double = new BigDecimal(operando2);
					else if (this.A.doubleValue() > 0)
						operando2Double = this.A;

					/*
					 * System.out.println("totalizacao: " + totalizacao);
					 * System.out.println("operando2Double: " +
					 * operando2Double);
					 */

					if (totalizacao.doubleValue() > operando2Double.doubleValue())
					{

					}
					else
					{
						erros.add(consistencia.obterMensagem());
					}
				}
			}
			
			//System.out.println("3 " + cContas.obterApelido() + ": " + totalizacao);
			break;
		}
		case 4: {
			
			if ((operando1.equals("A") && operando2.equals("B")) || operando1.equals("B") && operando2.equals("A"))
			{
				if (this.A.doubleValue() <= this.B.doubleValue())
				{

				}
				else
					erros.add(consistencia.obterMensagem());
			} 
			else
			{
				System.out.println("operando1: " + operando1);

				if (operando1.equals("A"))
				{
					if (this.A.doubleValue() <= 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else if (operando1.equals("B"))
				{
					if (this.B.doubleValue() <= 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else
				{
					totalizacao = this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo());

					if (!operando2.equals("A"))
						operando2Double = new BigDecimal(operando2);
					else if (this.A.doubleValue() > 0)
						operando2Double = this.A;

					/*
					 * System.out.println("totalizacao: " + totalizacao);
					 * System.out.println("operando2Double: " +
					 * operando2Double);
					 */

					if (totalizacao.doubleValue() <= operando2Double.doubleValue())
					{

					}
					else
					{
						erros.add(consistencia.obterMensagem());
					}
				}
			}
			//System.out.println("4 " + cContas.obterApelido() + ": " + totalizacao);
			break;
		}
		case 5: {
			
			if ((operando1.equals("A") && operando2.equals("B")) || operando1.equals("B") && operando2.equals("A"))
			{
				if (this.A.doubleValue() < this.B.doubleValue())
				{

				}
				else
					erros.add(consistencia.obterMensagem());
			}
			else
			{
				if (operando1.equals("A"))
				{
					if (this.A.doubleValue() < 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				} 
				else if (operando1.equals("B"))
				{
					if (this.B.doubleValue() < 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else
				{
					totalizacao = this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo());

					if (!operando2.equals("A"))
						operando2Double = new BigDecimal(operando2);
					else if (this.A.doubleValue() > 0)
						operando2Double = this.A;

					if (totalizacao.doubleValue() < operando2Double.doubleValue())
					{

					}
					else
					{
						erros.add(consistencia.obterMensagem());
					}
				}
			}
			
			//System.out.println("5 " + cContas.obterApelido() + ": " + totalizacao);
			break;
		}
		case 6: {
			
			if ((operando1.equals("A") && operando2.equals("B")) || operando1.equals("B") && operando2.equals("A"))
			{
				if (this.A.doubleValue() != this.B.doubleValue())
				{

				}
				else
					erros.add(consistencia.obterMensagem());
			}
			else
			{
				if (operando1.equals("A"))
				{
					if (this.A.doubleValue() != 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else if (operando1.equals("B"))
				{
					if (this.B.doubleValue() != 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else
				{
					totalizacao = this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo());
					operando2Double = new BigDecimal(operando2);

					if (totalizacao.doubleValue() != operando2Double.doubleValue())
					{

					}
					else
					{
						erros.add(consistencia.obterMensagem());
					}
				}
			}
			//System.out.println("6 " + cContas.obterApelido() + ": " + totalizacao);
			break;
		}
		case 7: {
			if (operando1.equals("A") && operando2.equals("B"))
				this.A = this.B;
			else if (operando1.equals("B") && operando2.equals("A"))
				this.B = this.A;
			else if (operando1.equals("A") && operando2.equals("0"))
				this.A = new BigDecimal("0.00");
			else if (operando1.equals("B") && operando2.equals("0"))
				this.B = new BigDecimal("0.00");

			break;
		}
		case 8: {
			if (operando1.equals("A"))
				this.A = this.A.add(this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo()));
			else if (operando1.equals("B"))
				this.B = this.B.add(this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo()));

			break;
		}

		default: {
			erros.add("Operador Inválido");
		}
		}

		return erros;
	}

	public Collection verificarApolice(String nomeArquivo) throws Exception {
		Collection linhas = new ArrayList();
		String bufferAux = "";

		File file = new File("" + "C:/Aseguradoras/Archivos/" + "A"
				+ nomeArquivo + ".txt");
		File file2 = new File("" + "C:/Aseguradoras/Archivos/" + "B"
				+ nomeArquivo + ".txt");
		//File file = new File( "" + "/Aseguradoras/Archivos/" + "A" +
		// nomeArquivo + ".txt" );
		//File file2 = new File( "" + "/Aseguradoras/Archivos/" + "B" +
		// nomeArquivo + ".txt" );

		if (file.exists()) {
			FileInputStream inputStreamArquivo = new FileInputStream(""
					+ "C:/Aseguradoras/Archivos/" + "A" + nomeArquivo + ".txt");
			//FileInputStream inputStreamArquivo = new FileInputStream( "" +
			// "/Aseguradoras/Archivos/" + "A"+ nomeArquivo + ".txt" );

			DataInputStream inputStreamData = new DataInputStream(
					inputStreamArquivo);

			while ((bufferAux = inputStreamData.readLine()) != null) {
				linhas.add(new String(bufferAux));
			}

			if (file2.exists()) {
				Collection erros = new ArrayList();

				erros = this.validarApolice(linhas);

				linhas.clear();

				FileInputStream inputStreamArquivo2 = new FileInputStream(""
						+ "C:/Aseguradoras/Archivos/" + "B" + nomeArquivo
						+ ".txt");
				//FileInputStream inputStreamArquivo2 = new FileInputStream( ""
				// + "/Aseguradoras/Archivos/" + "B"+ nomeArquivo + ".txt" );

				DataInputStream inputStreamData2 = new DataInputStream(
						inputStreamArquivo2);

				while ((bufferAux = inputStreamData2.readLine()) != null) {
					linhas.add(new String(bufferAux));
				}

				this.validarAsegurado(linhas);

				System.out.println("Total de Erros: " + erros.size());

				if (erros.size() == 0) {
					this.nomeArquivo = "A" + nomeArquivo;
					this.copiarArquivo();

					this.nomeArquivo = "B" + nomeArquivo;
					this.copiarArquivo();
				}

				return erros;
			} else
				throw new Exception("Error: 02 - El Archivo " + "B"
						+ nomeArquivo
						+ ".txt no fue encontrado (Datos del Asegurado)");

		} else
			throw new Exception("Error: 01 - El Archivo " + "A" + nomeArquivo
					+ ".txt no fue encontrado");
	}
	
	

	private Map apolices = new TreeMap();

	private Collection dadosPrevisoes = new ArrayList();

	private Map dadosReaseguros = new TreeMap();

	private Collection dadosCoaseguros = new ArrayList();

	private Map sinistros = new TreeMap();

	private Collection faturas = new ArrayList();

	private Collection anulacoes = new ArrayList();

	private Collection cobrancas = new ArrayList();

	private Collection aspectos2 = new ArrayList();

	private Collection suplementos = new ArrayList();

	private Collection refinacoes = new ArrayList();

	private Collection gastos2 = new ArrayList();

	private Collection anulacoes2 = new ArrayList();

	private Collection morosidades = new ArrayList();

	private Collection ratiosPermanentes = new ArrayList();

	private Collection ratiosUmAno = new ArrayList();

	private Collection ratiosTresAnos = new ArrayList();

	private Collection validarApolice(Collection linhas) throws Exception {
		int numeroLinha = 1;

		PlanoHome planoHome = (PlanoHome) this.getModelManager().getHome(
				"PlanoHome");
		ApoliceHome apoliceHome = (ApoliceHome) this.getModelManager().getHome(
				"ApoliceHome");
		DadosReaseguroHome dadosReaseguroHome = (DadosReaseguroHome) this
				.getModelManager().getHome("DadosReaseguroHome");
		SinistroHome sinistroHome = (SinistroHome) this.getModelManager()
				.getHome("SinistroHome");
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager()
				.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");

		Aseguradora aseguradora = null;

		Usuario usuario = null;

		Date dataGeracao = null;

		String tipoArquivo = null;

		int numeroTotalRegistros = 0;

		for (Iterator i = linhas.iterator(); i.hasNext();) {
			String linha = (String) i.next();

			if (Integer.parseInt(linha.substring(0, 5)) != numeroLinha)
				erros.add("Error: 02 - Numero secuencial invalido - Linea: "
						+ numeroLinha);

			System.out.println("numeroLinha: " + numeroLinha);

			// REGISTRO TIPO 1
			if (Integer.parseInt(linha.substring(5, 7)) == 1) {
				String sigla = linha.substring(7, 10);

				aseguradora = (Aseguradora) entidadeHome
						.obterEntidadePorSigla(sigla);

				if (aseguradora == null)
					erros.add("Error: 03 - Aseguradora " + sigla
							+ " no fue encontrada - Linea: " + numeroLinha);
				else {
					if (!this.obterOrigem().equals(aseguradora))
						erros.add("Error: 06 - Aseguradora " + sigla
								+ " no es la misma de la agenda - Linea: "
								+ numeroLinha);
				}

				String chaveUsuario = linha.substring(10, 19);

				usuario = usuarioHome.obterUsuarioPorChave(chaveUsuario.trim());

				if (usuario == null)
					erros.add("Error: 04 - Usuario " + chaveUsuario.trim()
							+ " no fue encontrado - Linea: " + numeroLinha);

				String anoGeracao = linha.substring(20, 24);

				String mesGeracao = linha.substring(24, 26);

				String diaGeracao = linha.substring(26, 28);

				if (diaGeracao.startsWith(" ") || mesGeracao.startsWith(" ")
						|| diaGeracao.startsWith(" "))
					erros.add("Error: 92 - Fecha Emisión Invalida - Linea: "
							+ numeroLinha);
				else
					dataGeracao = new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaGeracao + "/" + mesGeracao + "/"
									+ anoGeracao);

				String anoReporte = linha.substring(28, 32);

				String mesReporte = linha.substring(32, 34);

				if (this.obterMesMovimento() != Integer.parseInt(mesReporte))
					erros
							.add("Error: 07 - Mes informado es diferente del mes de la agenda");
				if (this.obterAnoMovimento() != Integer.parseInt(anoReporte))
					erros
							.add("Error: 08 - Año informado es diferente del año de la agenda");

				numeroTotalRegistros = Integer
						.parseInt(linha.substring(34, 44));

				tipoArquivo = "";

				if (linha.substring(44, 45).equals("|"))
					tipoArquivo = "Instrumento de cobertura";
			}

			// REGISTRO TIPO 2
			else if (Integer.parseInt(linha.substring(5, 7)) == 2) {

				Apolice apolice = (Apolice) this.getModelManager().getEntity(
						"Apolice");

				String apelidoCconta = linha.substring(7, 17);

				ClassificacaoContas cContas = null;
				cContas = (ClassificacaoContas) entidadeHome
						.obterEntidadePorApelido(apelidoCconta.trim());

				if (cContas == null)
					erros.add("Error: 05 - Cuenta " + apelidoCconta.trim()
							+ " no fue encontrada - Linea: " + numeroLinha);

				String numeroApolice = linha.substring(17, 27);

				String tipoApolice = "";

				if (linha.substring(27, 28).equals(""))
					erros
							.add("Error: 12 - Status del Instrumento es obligatorio - Linea: "
									+ numeroLinha);

				if (linha.substring(27, 28).equals("1"))
					tipoApolice = "Nuevo";
				else if (linha.substring(27, 28).equals("2"))
					tipoApolice = "Renovado";

				Apolice apoliceAnterior = null;

				if (this.apolices != null)
					apoliceAnterior = (Apolice) this.apolices.get(linha
							.substring(28, 38));

				if (apoliceAnterior == null)
					apoliceAnterior = apoliceHome.obterApolice(linha.substring(
							28, 38));

				String statusApolice = "";

				//System.out.println("statusApolice: " +
				// linha.substring(38,39));

				if (linha.substring(38, 39).equals(""))
					erros
							.add("Error: 13 - Tipo del Instrumento es obligatorio - Linea: "
									+ numeroLinha);

				if (linha.substring(38, 39).equals("1"))
					statusApolice = "Póliza Individual";
				else if (linha.substring(38, 39).equals("2"))
					statusApolice = "Póliza Madre";
				else if (linha.substring(38, 39).equals("3"))
					statusApolice = "Certificado de Seguro Colectivo";
				else if (linha.substring(38, 39).equals("4"))
					statusApolice = "Certificado Provisorio";
				else if (linha.substring(38, 39).equals("5"))
					statusApolice = "Nota de Cobertura de Raseguro";

				String apoliceSuperiorStr = linha.substring(39, 49);

				Apolice apoliceSuperior = null;

				if (this.apolices != null)
					apoliceSuperior = (Apolice) this.apolices
							.get(apoliceSuperiorStr);

				if (apoliceSuperior == null)
					apoliceSuperior = apoliceHome
							.obterApolice(apoliceSuperiorStr);

				String afetadorPorSinistro = "";

				if (linha.substring(49, 50).equals("1"))
					afetadorPorSinistro = "Sí";
				else if (linha.substring(49, 50).equals("2"))
					afetadorPorSinistro = "No";

				if (afetadorPorSinistro.equals(""))
					erros
							.add("Error: 14 - Afectado por Sinistro es obligatorio - Linea: "
									+ numeroLinha);

				String apoliceFlutuante = "";

				if (linha.substring(50, 51).equals("1"))
					apoliceFlutuante = "Sí";
				else if (linha.substring(50, 51).equals("2"))
					apoliceFlutuante = "No";

				if (apoliceFlutuante.equals(""))
					erros
							.add("Error: 15 - Póliza Flotante es obligatorio - Linea: "
									+ numeroLinha);

				String codigoPlano = linha.substring(51, 63);

				Plano plano = null;

				if (!codigoPlano.equals("RG-0001")) {
					plano = planoHome.obterPlano(codigoPlano);

					if (plano == null)
						erros.add("Error: 85 - Numero del Plan " + codigoPlano
								+ " no fue encontrado - Linea: " + numeroLinha);
				}

				String numeroFatura = linha.substring(63, 72);

				if (numeroFatura.equals(""))
					erros
							.add("Error: 16 - Numero de la Factura es obligatorio - Linea: "
									+ numeroLinha);

				String modalidadeVenda = "";

				if (linha.substring(72, 73).equals("1"))
					modalidadeVenda = "Con Intermediario";
				else if (linha.substring(72, 73).equals("2"))
					modalidadeVenda = "Sin Intermediario";

				if (modalidadeVenda.equals(""))
					erros
							.add("Error: 17 - Modalidad de Venta es obligatorio - Linea: "
									+ numeroLinha);

				String anoInicioVigencia = linha.substring(73, 77);

				String mesInicioVigencia = linha.substring(77, 79);

				String diaInicioVigencia = linha.substring(79, 81);

				Date dataIncioVigencia = null;

				if (diaInicioVigencia.startsWith(" ")
						|| mesInicioVigencia.startsWith(" ")
						|| anoInicioVigencia.startsWith(" "))
					erros
							.add("Error: 92 - Fecha Inicio Vigencia Invalida - Linea: "
									+ numeroLinha);
				else
					dataIncioVigencia = new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaInicioVigencia + "/" + mesInicioVigencia
									+ "/" + anoInicioVigencia);

				String anoFimVigencia = linha.substring(81, 85);

				String mesFimVigencia = linha.substring(85, 87);

				String diaFimVigencia = linha.substring(87, 89);

				Date dataFimVigencia = null;

				if (diaFimVigencia.startsWith(" ")
						|| mesFimVigencia.startsWith(" ")
						|| anoFimVigencia.startsWith(" "))
					erros
							.add("Error: 92 - Fecha Fin Vigencia Invalida - Linea: "
									+ numeroLinha);
				else
					dataFimVigencia = new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaFimVigencia + "/" + mesFimVigencia + "/"
									+ anoFimVigencia);

				String diasCobertura = linha.substring(89, 94);

				if (diasCobertura.equals(""))
					erros
							.add("Error: 18 - Dias de cobertura es obligatorio - Linea: "
									+ numeroLinha);

				String anoEmissao = linha.substring(94, 98);

				String mesEmissao = linha.substring(98, 100);

				String diaEmissao = linha.substring(100, 102);

				Date dataEmissao = null;

				if (diaEmissao.startsWith(" ") || mesEmissao.startsWith(" ")
						|| anoEmissao.startsWith(" "))
					erros.add("Error: 92 - Fecha Emisión Invalida - Linea: "
							+ numeroLinha);
				else
					dataEmissao = new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaEmissao + "/" + mesEmissao + "/"
									+ anoEmissao);

				String capitalGuaraniStr = linha.substring(102, 124);

				if (capitalGuaraniStr.equals(""))
					erros
							.add("Error: 19 - Capital en Riesgo en Guaranies es obligatorio - Linea: "
									+ numeroLinha);

				double capitalGuarani = Double.parseDouble(capitalGuaraniStr);

				String tipoMoedaCapitalGuarani = this.obterTipoMoeda(linha
						.substring(124, 126));

				String capitalMeStr = linha.substring(126, 148);

				double capitalMe = Double.parseDouble(capitalMeStr);

				String primasSeguroStr = linha.substring(148, 170);

				if (primasSeguroStr.equals(""))
					erros
							.add("Error: 20 - Prima en Guaranies es obligatorio - Linea: "
									+ numeroLinha);

				double primasSeguro = Double.parseDouble(primasSeguroStr);

				String tipoMoedaPrimas = this.obterTipoMoeda(linha.substring(
						170, 172));

				String primasMeStr = linha.substring(172, 194);

				double primasMe = Double.parseDouble(primasMeStr);

				String principalGuaraniStr = linha.substring(194, 216);

				double principalGuarani = Double
						.parseDouble(principalGuaraniStr);

				String tipoMoedaPrincipalGuarani = this.obterTipoMoeda(linha
						.substring(216, 218));

				String principalMeStr = linha.substring(218, 240);

				double principalMe = Double.parseDouble(principalMeStr);

				String incapacidadeGuaraniStr = linha.substring(240, 262);

				double incapacidadeGuarani = Double
						.parseDouble(incapacidadeGuaraniStr);

				String tipoMoedaIncapacidadeGuarani = this.obterTipoMoeda(linha
						.substring(262, 264));

				String incapacidadeMeStr = linha.substring(264, 286);

				double incapacidadeMe = Double.parseDouble(incapacidadeMeStr);

				String enfermidadeGuaraniStr = linha.substring(286, 308);

				double enfermidadeGuarani = Double
						.parseDouble(enfermidadeGuaraniStr);

				String tipoMoedaEnfermidade = this.obterTipoMoeda(linha
						.substring(308, 310));

				String enfermidadeMeStr = linha.substring(310, 332);

				double enfermidadeMe = Double.parseDouble(enfermidadeMeStr);

				String acidentesGuaraniStr = linha.substring(332, 354);

				double acidentesGuarani = Double
						.parseDouble(acidentesGuaraniStr);

				String tipoMoedaAcidente = this.obterTipoMoeda(linha.substring(
						354, 356));

				String acidentesMeStr = linha.substring(356, 378);

				double acidentesMe = Double.parseDouble(acidentesMeStr);

				String outrosGuaraniStr = linha.substring(378, 400);

				double outrosGuarani = Double.parseDouble(outrosGuaraniStr);

				String tipoMoedaOutros = this.obterTipoMoeda(linha.substring(
						400, 402));

				String outrosMeStr = linha.substring(402, 424);

				double outrosMe = Double.parseDouble(outrosMeStr);

				String financimantoGsStr = linha.substring(424, 446);

				if (financimantoGsStr.equals(""))
					erros
							.add("Error: 21 - Interés por Financiamiento en Gs es obligatorio - Linea: "
									+ numeroLinha);

				double financimantoGS = Double.parseDouble(financimantoGsStr);

				String tipoMoedaFinanciamentoGS = this.obterTipoMoeda(linha
						.substring(446, 448));

				String financiamentoMeStr = linha.substring(448, 470);

				double financiamentoMe = Double.parseDouble(financiamentoMeStr);

				String premioGsStr = linha.substring(470, 492);

				if (premioGsStr.equals(""))
					erros
							.add("Error: 22 - Premio en Gs es obligatorio - Linea: "
									+ numeroLinha);

				double premioGs = Double.parseDouble(premioGsStr);

				String tipoMoedaPremio = this.obterTipoMoeda(linha.substring(
						492, 494));

				String premioMeStr = linha.substring(494, 516);

				double premioMe = Double.parseDouble(premioMeStr);

				String formaPagamento = "";

				if (linha.substring(516, 517).equals("1"))
					formaPagamento = "Al contado";
				else if (linha.substring(516, 517).equals("2"))
					formaPagamento = "Financiado";

				if (formaPagamento.equals(""))
					erros
							.add("Error: 23 - Forma de Pago es obligatorio - Linea: "
									+ numeroLinha);

				String qtdeParcelas = linha.substring(517, 520);

				String refinacao = "";

				if (linha.substring(520, 521).equals("1"))
					refinacao = "Sí";
				else if (linha.substring(520, 521).equals("2"))
					refinacao = "No";

				String inscricaoAgente = linha.substring(521, 525);

				Entidade agente = null;

				if (!inscricaoAgente.equals("0000")) {
					if (inscricaoAgente.substring(0, 1).equals("0"))
						agente = entidadeHome
								.obterEntidadePorInscricao(inscricaoAgente
										.substring(1, inscricaoAgente.length()));
					else
						agente = entidadeHome
								.obterEntidadePorInscricao(inscricaoAgente);

					if (agente == null)
						erros.add("Error: 09 - Agente con inscripción "
								+ inscricaoAgente.substring(1, inscricaoAgente
										.length())
								+ " no fue encontrado - Linea: " + numeroLinha);
				}

				String comissaoGsStr = linha.substring(525, 547);

				double comissaoGs = Double.parseDouble(comissaoGsStr);

				String tipoMoedaComissaoGs = this.obterTipoMoeda(linha
						.substring(547, 549));

				String comissaoMeStr = linha.substring(549, 571);

				double comissaoMe = Double.parseDouble(comissaoMeStr);

				String situacaoSeguro = "";

				if (linha.substring(571, 572).equals("1"))
					situacaoSeguro = "Vigente";
				else if (linha.substring(571, 572).equals("2"))
					situacaoSeguro = "No Vigente Pendiente";
				else if (linha.substring(571, 572).equals("3"))
					situacaoSeguro = "No Vigente";

				Corretora corredor = null;

				String inscricaoCorredor = linha.substring(572, 576);

				if (!inscricaoCorredor.equals("0000")) {
					if (inscricaoCorredor.substring(0, 1).equals("0"))
						corredor = (Corretora) entidadeHome
								.obterEntidadePorInscricao(inscricaoCorredor
										.substring(1, inscricaoCorredor
												.length()));
					else
						corredor = (Corretora) entidadeHome
								.obterEntidadePorInscricao(inscricaoCorredor);

					if (corredor == null)
						erros.add("Error: 09 - Corredor con inscripción "
								+ inscricaoCorredor.substring(1,
										inscricaoCorredor.length())
								+ " no fue encontrado - Linea: " + numeroLinha);
				}

				double numeroEndoso = Double.parseDouble(linha.substring(576,
						586));

				double certificado = Double.parseDouble(linha.substring(586,
						593));

				double numeroEndosoAnterior = Double.parseDouble(linha
						.substring(593, 603));

				double certificadoAnterior = Double.parseDouble(linha
						.substring(603, 610));

				apolices.put(numeroApolice, apolice);

				if (erros.size() == 0) {
					EntidadeHome home = (EntidadeHome) this.getModelManager()
							.getHome("EntidadeHome");
					Entidade destino = home.obterEntidadePorApelido("bcp");

					apolice.atribuirOrigem(aseguradora);
					apolice.atribuirDestino(destino);
					apolice.atribuirResponsavel(usuario);
					apolice.atribuirTipo(tipoApolice);
					apolice.atribuirTitulo("Instumento: " + numeroApolice);
					if (apoliceSuperior != null) {
						apolice.atribuirSuperior(apoliceSuperior);
						//apoliceSuperior.atualizarSuperior(this);
					} else
						apolice.atribuirSuperior(this);
					//apolice.incluir();

					apolice.atribuirDataPrevistaInicio(dataIncioVigencia);
					apolice.atribuirDataPrevistaConclusao(dataFimVigencia);
					apolice.atribuirSecao(cContas);
					apolice.atribuirNumeroApolice(numeroApolice);
					apolice.atribuirStatusApolice(statusApolice);
					//if(apoliceAnterior!=null)
					apolice.atribuirApoliceAnterior(apoliceAnterior);
					apolice.atribuirAfetadoPorSinistro(afetadorPorSinistro);
					apolice.atribuirApoliceFlutuante(apoliceFlutuante);
					apolice.atribuirPlano(plano);
					apolice.atribuirNumeroFatura(numeroFatura);
					apolice.atribuirModalidadeVenda(modalidadeVenda);
					apolice.atribuirApoliceFlutuante(apoliceFlutuante);
					apolice.atribuirDiasCobertura(Integer
							.parseInt(diasCobertura));
					apolice.atribuirDataEmissao(dataEmissao);
					apolice.atribuirCapitalGs(capitalGuarani);
					apolice
							.atribuirTipoMoedaCapitalGuarani(tipoMoedaCapitalGuarani);
					apolice.atribuirCapitalMe(capitalMe);
					apolice.atribuirPrimaGs(primasSeguro);
					apolice.atribuirTipoMoedaPrimaGs(tipoMoedaPrimas);
					apolice.atribuirPrimaMe(primasMe);
					apolice.atribuirPrincipalGs(principalGuarani);
					apolice
							.atribuirTipoMoedaPrincipalGs(tipoMoedaPrincipalGuarani);
					apolice.atribuirPrincipalMe(principalMe);
					apolice.atribuirIncapacidadeGs(incapacidadeGuarani);
					apolice
							.atribuirTipoMoedaIncapacidadeGs(tipoMoedaIncapacidadeGuarani);
					apolice.atribuirIncapacidadeMe(incapacidadeMe);
					apolice.atribuirEnfermidadeGs(enfermidadeGuarani);
					apolice
							.atribuirTipoMoedaEnfermidadeGs(tipoMoedaEnfermidade);
					apolice.atribuirEnfermidadeMe(enfermidadeMe);
					apolice.atribuirAcidentesGs(acidentesGuarani);
					apolice.atribuirTipoMoedaAcidentesGs(tipoMoedaAcidente);
					apolice.atribuirAcidentesMe(acidentesMe);
					apolice.atribuirOutrosGs(outrosGuarani);
					apolice.atribuirTipoMoedaOutrosGs(tipoMoedaOutros);
					apolice.atribuirOutrosMe(outrosMe);
					apolice.atribuirFinanciamentoGs(financimantoGS);
					apolice
							.atribuirTipoMoedaFinanciamentoGs(tipoMoedaFinanciamentoGS);
					apolice.atribuirFinanciamentoMe(financiamentoMe);
					apolice.atribuirPremiosGs(premioGs);
					apolice.atribuirTipoMoedaPremiosGs(tipoMoedaPremio);
					apolice.atribuirPremiosMe(premioMe);
					apolice.atribuirFormaPagamento(formaPagamento);
					apolice
							.atribuirQtdeParcelas(Integer
									.parseInt(qtdeParcelas));
					apolice.atribuirRefinacao(refinacao);
					apolice.atribuirPremiosGs(premioGs);
					//if(agente!=null)
					apolice.atribuirInscricaoAgente(agente);
					apolice.atribuirComissaoGs(comissaoGs);
					apolice.atribuirTipoMoedaComissaoGs(tipoMoedaComissaoGs);
					apolice.atribuirComissaoMe(comissaoMe);
					apolice.atribuirSituacaoSeguro(situacaoSeguro);
					if (codigoPlano.equals("RG-0001"))
						apolice.atribuirCodigoPlano(codigoPlano);

					apolice.atribuirCorredor(corredor);
					apolice.atribuirNumeroEndoso(numeroEndoso);
					apolice.atribuirNumeroEndosoAnterior(numeroEndosoAnterior);
					apolice.atribuirCertificado(certificado);
					apolice.atribuirCertificadoAnterior(certificadoAnterior);

					/*
					 * apolice.atualizarDataPrevistaInicio(dataIncioVigencia);
					 * apolice.atualizarDataPrevistaConclusao(dataFimVigencia);
					 * apolice.atualizarSecao(cContas);
					 * apolice.atualizarNumeroApolice(numeroApolice);
					 * apolice.atualizarStatusApolice(statusApolice);
					 * if(apoliceAnterior!=null)
					 * apolice.atualizarApoliceAnterior(apoliceAnterior);
					 * apolice.atualizarAfetadoPorSinistro(afetadorPorSinistro);
					 * apolice.atualizarApoliceFlutuante(apoliceFlutuante);
					 * apolice.atualizarPlano(plano);
					 * apolice.atualizarNumeroFatura(numeroFatura);
					 * apolice.atualizarModalidadeVenda(modalidadeVenda);
					 * apolice.atualizarApoliceFlutuante(apoliceFlutuante);
					 * apolice.atualizarDiasCobertura(Integer.parseInt(diasCobertura));
					 * apolice.atualizarDataEmissao(dataEmissao);
					 * apolice.atualizarCapitalGs(capitalGuarani);
					 * apolice.atualizarTipoMoedaCapitalGuarani(tipoMoedaCapitalGuarani);
					 * apolice.atualizarCapitalMe(capitalMe);
					 * apolice.atualizarPrimaGs(primasSeguro);
					 * apolice.atualizarTipoMoedaPrimaGs(tipoMoedaPrimas);
					 * apolice.atualizarPrimaMe(primasMe);
					 * apolice.atualizarPrincipalGs(principalGuarani);
					 * apolice.atualizarTipoMoedaPrincipalGs(tipoMoedaPrincipalGuarani);
					 * apolice.atualizarPrincipalMe(principalMe);
					 * apolice.atualizarIncapacidadeGs(incapacidadeGuarani);
					 * apolice.atualizarTipoMoedaIncapacidadeGs(tipoMoedaIncapacidadeGuarani);
					 * apolice.atualizarIncapacidadeMe(incapacidadeMe);
					 * apolice.atualizarEnfermidadeGs(enfermidadeGuarani);
					 * apolice.atualizarTipoMoedaEnfermidadeGs(tipoMoedaEnfermidade);
					 * apolice.atualizarEnfermidadeMe(enfermidadeMe);
					 * apolice.atualizarAcidentesGs(acidentesGuarani);
					 * apolice.atualizarTipoMoedaAcidentesGs(tipoMoedaAcidente);
					 * apolice.atualizarAcidentesMe(acidentesMe);
					 * apolice.atualizarOutrosGs(outrosGuarani);
					 * apolice.atualizarTipoMoedaOutrosGs(tipoMoedaOutros);
					 * apolice.atualizarOutrosMe(outrosMe);
					 * apolice.atualizarFinanciamentoGs(financimantoGS);
					 * apolice.atualizarTipoMoedaFinanciamentoGs(tipoMoedaFinanciamentoGS);
					 * apolice.atualizarFinanciamentoMe(financiamentoMe);
					 * apolice.atualizarPremiosGs(premioGs);
					 * apolice.atualizarTipoMoedaPremiosGs(tipoMoedaPremio);
					 * apolice.atualizarPremiosMe(premioMe);
					 * apolice.atualizarFormaPagamento(formaPagamento);
					 * apolice.atualizarQtdeParcelas(Integer.parseInt(qtdeParcelas));
					 * apolice.atualizarRefinacao(refinacao);
					 * apolice.atualizarPremiosGs(premioGs); if(agente!=null)
					 * apolice.atualizarInscricaoAgente(agente);
					 * apolice.atualizarComissaoGs(comissaoGs);
					 * apolice.atualizarTipoMoedaComissaoGs(tipoMoedaComissaoGs);
					 * apolice.atualizarComissaoMe(comissaoMe);
					 * apolice.atualizarSituacaoSeguro(situacaoSeguro);
					 */
				}
			}
			// REGISTRO TIPO 3
			else if (Integer.parseInt(linha.substring(5, 7)) == 3) {
				String apelidoCconta = linha.substring(7, 17);

				ClassificacaoContas cContas = null;
				cContas = (ClassificacaoContas) entidadeHome
						.obterEntidadePorApelido(apelidoCconta.trim());

				if (cContas == null)
					erros.add("Error: 05 - Cuenta " + apelidoCconta.trim()
							+ " no fue encontrada - Linea: " + numeroLinha);

				String numeroInstrumento = linha.substring(17, 27);

				Apolice apolice = null;

				if (this.apolices != null)
					apolice = (Apolice) this.apolices.get(numeroInstrumento);

				if (apolice == null)
					apolice = apoliceHome.obterApolice(numeroInstrumento);

				if (apolice == null)
					erros.add("Error: 73 - Instumento "
							+ numeroInstrumento.trim()
							+ " no fue encontrado - Linea: " + numeroLinha);

				String anoCorte = linha.substring(27, 31);

				String mesCorte = linha.substring(31, 33);

				String diaCorte = linha.substring(33, 35);

				Date dataCorte = null;

				if (diaCorte.startsWith(" ") || mesCorte.startsWith(" ")
						|| anoCorte.startsWith(" "))
					erros.add("Error: 92 - Fecha del Corte Invalida - Linea: "
							+ numeroLinha);
				else
					dataCorte = new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaCorte + "/" + mesCorte + "/" + anoCorte);

				String cursoStr = linha.substring(35, 57);

				if (cursoStr.equals(""))
					erros
							.add("Error: 24 - Riesgo en Curso es obligatorio - Linea: "
									+ numeroLinha);

				double curso = Double.parseDouble(cursoStr);

				String sinistroStr = linha.substring(57, 79);

				double valorSinistro = Double.parseDouble(sinistroStr);

				String reservasStr = linha.substring(79, 101);

				double reservas = Double.parseDouble(reservasStr);

				String fundoStr = linha.substring(101, 123);

				double fundos = Double.parseDouble(fundoStr);

				String premiosStr = linha.substring(123, 145);

				double premios = Double.parseDouble(premiosStr);

				String tipoInstrumento = "";

				if (linha.substring(145, 146).equals("1"))
					tipoInstrumento = "Póliza Individual";
				else if (linha.substring(145, 146).equals("2"))
					tipoInstrumento = "Póliza Madre";
				else if (linha.substring(145, 146).equals("3"))
					tipoInstrumento = "Certificado de Seguro Colectivo";
				else if (linha.substring(145, 146).equals("4"))
					tipoInstrumento = "Certificado Provisorio";
				else if (linha.substring(145, 146).equals("5"))
					tipoInstrumento = "Nota de Cobertura de Reaseguro";

				if (tipoInstrumento.equals(""))
					erros
							.add("Error: 101 - Instrumento es obligatorio - Linea: "
									+ numeroLinha);

				double numeroEndoso = Double.parseDouble(linha.substring(146,
						156));

				double certificado = Double.parseDouble(linha.substring(156,
						163));

				if (erros.size() == 0) {
					DadosPrevisao dadosPrevisao = (DadosPrevisao) this
							.getModelManager().getEntity("DadosPrevisao");

					dadosPrevisao.atribuirOrigem(apolice.obterOrigem());
					dadosPrevisao.atribuirDestino(apolice.obterDestino());
					dadosPrevisao.atribuirResponsavel(apolice
							.obterResponsavel());
					dadosPrevisao.atribuirTitulo("Datos do Instrumento: "
							+ numeroInstrumento);
					dadosPrevisao.atribuirSuperior(apolice);

					dadosPrevisao.atribuirDataCorte(dataCorte);
					dadosPrevisao.atribuirCurso(curso);
					dadosPrevisao.atribuirSinistroPendente(valorSinistro);
					dadosPrevisao.atribuirReservasMatematicas(reservas);
					dadosPrevisao.atribuirFundosAcumulados(fundos);
					dadosPrevisao.atribuirPremios(premios);
					dadosPrevisao.atribuirTipoInstrumento(tipoInstrumento);
					dadosPrevisao.atribuirNumeroEndoso(numeroEndoso);
					dadosPrevisao.atribuirCertificado(certificado);

					dadosPrevisoes.add(dadosPrevisao);
				}

			}
			// REGISTRO TIPO 4
			else if (Integer.parseInt(linha.substring(5, 7)) == 4) {
				DadosReaseguro dadosReaseguro = (DadosReaseguro) this
						.getModelManager().getEntity("DadosReaseguro");

				String apelidoCconta = linha.substring(7, 17);

				ClassificacaoContas cContas = null;
				cContas = (ClassificacaoContas) entidadeHome
						.obterEntidadePorApelido(apelidoCconta.trim());

				if (cContas == null)
					erros.add("Error: 05 - Cuenta " + apelidoCconta.trim()
							+ " no fue encontrada - Linea: " + numeroLinha);

				String tipoInstrumento = "";

				if (linha.substring(17, 18).equals("1"))
					tipoInstrumento = "Póliza Individual";
				else if (linha.substring(17, 18).equals("2"))
					tipoInstrumento = "Póliza Madre";
				else if (linha.substring(17, 18).equals("3"))
					tipoInstrumento = "Certificado de Seguro Colectivo";
				else if (linha.substring(17, 18).equals("4"))
					tipoInstrumento = "Certificado Provisorio";
				else if (linha.substring(17, 18).equals("5"))
					tipoInstrumento = "Nota de Cobertura de Reaseguro";

				if (tipoInstrumento.equals(""))
					erros
							.add("Error: 101 - Instrumento es obligatorio - Linea: "
									+ numeroLinha);

				String numeroInstrumento = linha.substring(18, 28);

				Apolice apolice = null;

				if (this.apolices != null)
					apolice = (Apolice) this.apolices.get(numeroInstrumento);

				if (apolice == null)
					apolice = apoliceHome.obterApolice(numeroInstrumento);

				if (apolice == null)
					erros.add("Error: 73 - Instumento "
							+ numeroInstrumento.trim()
							+ " no fue encontrado - Linea: " + numeroLinha);

				String valorEndosoStr = linha.substring(28, 38);

				double valorEndoso = Double.parseDouble(valorEndosoStr);

				String qtdeStr = linha.substring(38, 40);

				if (qtdeStr.equals(""))
					erros
							.add("Error: 25 - Cantidad de Reaseguros es obligatoria - Linea: "
									+ numeroLinha);

				int qtde = Integer.parseInt(qtdeStr);

				int ultimo = 0;

				for (int w = 0; w < qtde; w++) {
					if (ultimo == 0)
						ultimo = 40;

					String inscricaoReaseguradora1 = linha.substring(ultimo,
							ultimo + 3);
					//String inscricaoReaseguradora1 = linha.substring(29,32);

					if (inscricaoReaseguradora1.equals(""))
						erros
								.add("Error: 25 - Numero de la Reaseguradora es obligatorio - Linea: "
										+ numeroLinha);

					Entidade reaseguradora = null;

					if (!inscricaoReaseguradora1.equals("000")) {
						reaseguradora = entidadeHome
								.obterEntidadePorInscricao(inscricaoReaseguradora1);

						if (reaseguradora == null)
							erros
									.add("Error: 10 - Inscripción "
											+ inscricaoReaseguradora1
											+ " de la Reaseguradora no fue encontrada o esta No Activa - Linea: "
											+ numeroLinha);
					}

					String tipoContratoReaseguro1 = "";

					//linha.substring(32,33)

					if (linha.substring(ultimo + 3, ultimo + 3 + 1).equals("1"))
						tipoContratoReaseguro1 = "Cuota parte";
					else if (linha.substring(ultimo + 3, ultimo + 3 + 1)
							.equals("2"))
						tipoContratoReaseguro1 = "Excedente";
					else if (linha.substring(ultimo + 3, ultimo + 3 + 1)
							.equals("3"))
						tipoContratoReaseguro1 = "Exceso de pérdida";
					else if (linha.substring(ultimo + 3, ultimo + 3 + 1)
							.equals("4"))
						tipoContratoReaseguro1 = "Facultativo no Proporcional";
					else if (linha.substring(ultimo + 3, ultimo + 3 + 1)
							.equals("5"))
						tipoContratoReaseguro1 = "Facultativo Proporcional";
					else if (linha.substring(ultimo + 3, ultimo + 3 + 1)
							.equals("6"))
						tipoContratoReaseguro1 = "Limitación de Siniestralidad";

					if (tipoContratoReaseguro1.equals(""))
						erros
								.add("Error: 26 - Tipo de Contrato de Reaseguro es obligatorio - Linea: "
										+ numeroLinha);

					//String inscricaoCorredoraReaseguro1 =
					// linha.substring(33,36);
					String inscricaoCorredoraReaseguro1 = linha.substring(
							ultimo + 3 + 1, ultimo + 3 + 1 + 3);

					Entidade corretora = null;

					if (!inscricaoCorredoraReaseguro1.equals("000")) {
						corretora = entidadeHome
								.obterEntidadePorInscricao(inscricaoCorredoraReaseguro1);

						if (corretora == null)
							erros
									.add("Error: 11 - Inscripción "
											+ inscricaoCorredoraReaseguro1
											+ " de la Corredora o Reaseguradora no fue encontrada o esta No Activa - Linea: "
											+ numeroLinha);
					}

					String reaseguroGs1Str = linha.substring(
							ultimo + 3 + 1 + 3, ultimo + 3 + 1 + 3 + 22);

					double reaseguroGs1 = Double.parseDouble(reaseguroGs1Str);

					String tipoMoedaReaseguroGs1 = this.obterTipoMoeda(linha
							.substring(ultimo + 3 + 1 + 3 + 22, ultimo + 3 + 1
									+ 3 + 22 + 2));

					String reaseguroMe1Str = linha.substring(ultimo + 3 + 1 + 3
							+ 22 + 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22);

					double reaseguroMe1 = Double.parseDouble(reaseguroMe1Str);

					String primaReaseguro1GsStr = linha.substring(ultimo + 3
							+ 1 + 3 + 22 + 2 + 22, ultimo + 3 + 1 + 3 + 22 + 2
							+ 22 + 22);

					double primaReaseguro1Gs = Double
							.parseDouble(primaReaseguro1GsStr);

					String tipoMoedaPrimaReaseguro1 = this.obterTipoMoeda(linha
							.substring(ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22,
									ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2));

					String primaReaseguro1MeStr = linha.substring(ultimo + 3
							+ 1 + 3 + 22 + 2 + 22 + 22 + 2, ultimo + 3 + 1 + 3
							+ 22 + 2 + 22 + 22 + 2 + 22);

					double primaReaseguro1Me = Double
							.parseDouble(primaReaseguro1MeStr);

					String comissaoReaseguro1GsStr = linha.substring(ultimo + 3
							+ 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22, ultimo + 3 + 1
							+ 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22);

					double comissaoReaseguro1Gs = Double
							.parseDouble(comissaoReaseguro1GsStr);

					String tipoMoedaComissaoReaseguro1 = this
							.obterTipoMoeda(linha.substring(ultimo + 3 + 1 + 3
									+ 22 + 2 + 22 + 22 + 2 + 22 + 22, ultimo
									+ 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22
									+ 22 + 2));

					String comissaoReaseguro1MeStr = linha.substring(ultimo + 3
							+ 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2,
							ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22
									+ 2 + 22);

					double comissaoReaseguro1Me = Double
							.parseDouble(comissaoReaseguro1MeStr);

					String anoInicioVigencia = linha.substring(ultimo + 3 + 1
							+ 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22,
							ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22
									+ 2 + 22 + 4);

					String mesInicioVigencia = linha.substring(ultimo + 3 + 1
							+ 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4,
							ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22
									+ 2 + 22 + 4 + 2);

					String diaInicioVigencia = linha.substring(ultimo + 3 + 1
							+ 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4
							+ 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22
							+ 22 + 2 + 22 + 4 + 2 + 2);

					Date dataInicioVigencia = null;

					if (diaInicioVigencia.startsWith(" ")
							|| mesInicioVigencia.startsWith(" ")
							|| anoInicioVigencia.startsWith(" "))
						erros
								.add("Error: 92 - Fecha Inico Vigencia Invalida - Linea: "
										+ numeroLinha);
					else
						dataInicioVigencia = new SimpleDateFormat("dd/MM/yyyy")
								.parse(diaInicioVigencia + "/"
										+ mesInicioVigencia + "/"
										+ anoInicioVigencia);

					String anoFimVigencia = linha.substring(ultimo + 3 + 1 + 3
							+ 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2
							+ 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22
							+ 22 + 2 + 22 + 4 + 2 + 2 + 4);

					String mesFimVigencia = linha.substring(ultimo + 3 + 1 + 3
							+ 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2
							+ 2 + 4, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2
							+ 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4 + 2);

					String diaFimVigencia = linha.substring(ultimo + 3 + 1 + 3
							+ 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2
							+ 2 + 4 + 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22
							+ 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4 + 2 + 2);

					Date dataFimVigencia = null;

					if (diaFimVigencia.startsWith(" ")
							|| mesFimVigencia.startsWith(" ")
							|| anoFimVigencia.startsWith(" "))
						erros
								.add("Error: 92 - Fecha Fin Vigencia Invalida - Linea: "
										+ numeroLinha);
					else
						dataFimVigencia = new SimpleDateFormat("dd/MM/yyyy")
								.parse(diaFimVigencia + "/" + mesFimVigencia
										+ "/" + anoFimVigencia);

					String situacaoReaseguro1 = "";

					if (linha.substring(
							ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22
									+ 2 + 22 + 4 + 2 + 2 + 4 + 2 + 2,
							ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22
									+ 2 + 22 + 4 + 2 + 2 + 4 + 2 + 2 + 1)
							.equals("1"))
						situacaoReaseguro1 = "Vigente";
					else if (linha.substring(
							ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22
									+ 2 + 22 + 4 + 2 + 2 + 4 + 2 + 2,
							ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22
									+ 2 + 22 + 4 + 2 + 2 + 4 + 2 + 2 + 1)
							.equals("2"))
						situacaoReaseguro1 = "No Vigente";

					if (situacaoReaseguro1.equals(""))
						erros
								.add("Error: 27 - Situacíon de Reaseguro es obligatorio - Linea: "
										+ numeroLinha);

					ultimo = ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22
							+ 22 + 2 + 22 + 4 + 2 + 2 + 4 + 2 + 2 + 1;

					if (reaseguradora != null)
						dadosReaseguros.put(new Long(cContas.obterId()
								+ apolice.obterNumeroApolice()
								+ reaseguradora.obterId())
								+ tipoContratoReaseguro1, dadosReaseguro);
					else
						dadosReaseguros.put(new Long(cContas.obterId()
								+ apolice.obterNumeroApolice())
								+ tipoContratoReaseguro1, dadosReaseguro);

					if (erros.size() == 0) {
						//dadosReaseguro.verificarDuplicidade(apolice, cContas,
						// reaseguradora, tipoContratoReaseguro1);

						dadosReaseguro.atribuirOrigem(apolice.obterOrigem());
						dadosReaseguro.atribuirDestino(apolice.obterDestino());
						dadosReaseguro.atribuirResponsavel(apolice
								.obterResponsavel());
						dadosReaseguro.atribuirTitulo("Datos do Instrumento: "
								+ numeroInstrumento);
						dadosReaseguro.atribuirSuperior(apolice);

						//dadosReaseguro.incluir();

						dadosReaseguro.atribuirReaseguradora(reaseguradora);
						dadosReaseguro
								.atribuirTipoContrato(tipoContratoReaseguro1);
						//if(corretora!=null)
						dadosReaseguro.atribuirCorredora(corretora);
						dadosReaseguro.atribuirCapitalGs(reaseguroGs1);
						dadosReaseguro
								.atribuirTipoMoedaCapitalGs(tipoMoedaReaseguroGs1);
						dadosReaseguro.atribuirCapitalMe(reaseguroMe1);
						dadosReaseguro.atribuirPrimaGs(primaReaseguro1Gs);
						dadosReaseguro
								.atribuirTipoMoedaPrimaGs(tipoMoedaPrimaReaseguro1);
						dadosReaseguro.atribuirPrimaMe(primaReaseguro1Me);
						dadosReaseguro.atribuirComissaoGs(comissaoReaseguro1Gs);
						dadosReaseguro
								.atribuirTipoMoedaComissaoGs(tipoMoedaComissaoReaseguro1);
						dadosReaseguro.atribuirComissaoMe(comissaoReaseguro1Me);
						dadosReaseguro
								.atribuirDataPrevistaInicio(dataInicioVigencia);
						dadosReaseguro
								.atribuirDataPrevistaConclusao(dataFimVigencia);
						dadosReaseguro.atribuirSituacao(situacaoReaseguro1);
						dadosReaseguro.atribuirValorEndoso(valorEndoso);
						dadosReaseguro.atribuirTipoInstrumento(tipoInstrumento);
					}
					/*
					 * else { if(apolice!=null) { apolice.excluir(); break; } }
					 */
				}
			}
			// REGISTRO TIPO 5
			else if (Integer.parseInt(linha.substring(5, 7)) == 5) {
				String apelidoCconta = linha.substring(7, 17);

				ClassificacaoContas cContas = null;
				cContas = (ClassificacaoContas) entidadeHome
						.obterEntidadePorApelido(apelidoCconta.trim());

				if (cContas == null)
					erros.add("Error: 05 - Cuenta " + apelidoCconta.trim()
							+ " no fue encontrada - Linea: " + numeroLinha);

				String tipoInstrumento = "";

				if (linha.substring(17, 18).equals("1"))
					tipoInstrumento = "Póliza Individual";
				else if (linha.substring(17, 18).equals("2"))
					tipoInstrumento = "Póliza Madre";
				else if (linha.substring(17, 18).equals("3"))
					tipoInstrumento = "Certificado de Seguro Colectivo";
				else if (linha.substring(17, 18).equals("4"))
					tipoInstrumento = "Certificado Provisorio";
				else if (linha.substring(17, 18).equals("5"))
					tipoInstrumento = "Nota de Cobertura de Reaseguro";

				if (tipoInstrumento.equals(""))
					erros
							.add("Error: 101 - Instrumento es obligatorio - Linea: "
									+ numeroLinha);

				String numeroInstrumento = linha.substring(18, 28);

				Apolice apolice = null;

				if (this.apolices != null)
					apolice = (Apolice) this.apolices.get(numeroInstrumento);

				if (apolice == null)
					apolice = apoliceHome.obterApolice(numeroInstrumento);

				if (apolice == null)
					erros.add("Error: 73 - Instumento "
							+ numeroInstrumento.trim()
							+ " no fue encontrado - Linea: " + numeroLinha);

				double numeroEndoso = Double.parseDouble(linha
						.substring(28, 38));

				double certificado = Double
						.parseDouble(linha.substring(38, 45));

				String grupo = linha.substring(45, 47);

				String qtdeStr = linha.substring(47, 49);

				if (qtdeStr.equals(""))
					erros
							.add("Error: 27 - Cantidad de Aseguradoras es obligatoria - Linea: "
									+ numeroLinha);

				int qtde = Integer.parseInt(qtdeStr);

				int ultimo = 0;

				for (int w = 0; w < qtde; w++) {
					if (ultimo == 0)
						ultimo = 49;

					String inscricaoAseguradora = linha.substring(ultimo,
							ultimo + 3);

					if (inscricaoAseguradora.equals(""))
						erros
								.add("Error: 27 - Numero de la Aseguradora es obligatorio - Linea: "
										+ numeroLinha);

					Entidade aseguradora2 = null;

					aseguradora2 = entidadeHome
							.obterEntidadePorInscricao(inscricaoAseguradora);

					if (aseguradora2 == null)
						erros.add("Error: 74 - Aseguradora "
								+ inscricaoAseguradora
								+ " - no fue encontrada - Linea: "
								+ numeroLinha);

					String capitalGsStr = linha.substring(ultimo + 3,
							ultimo + 3 + 22);

					if (capitalGsStr.equals(""))
						erros
								.add("Error: 28 - Capital en Riesgo en Gs es obligatorio - Linea: "
										+ numeroLinha);

					double capitalGs = Double.parseDouble(capitalGsStr);

					String tipoMoedaCapitalGs1 = this.obterTipoMoeda(linha
							.substring(ultimo + 3 + 22, ultimo + 3 + 22 + 2));

					String capitalMeStr = linha.substring(ultimo + 3 + 22 + 2,
							ultimo + 3 + 22 + 2 + 22);

					double capitalMe = Double.parseDouble(capitalMeStr);

					String porcentagemCoaseguradoraStr = linha.substring(ultimo
							+ 3 + 22 + 2 + 22, ultimo + 3 + 22 + 2 + 22 + 6);

					if (porcentagemCoaseguradoraStr.equals(""))
						erros
								.add("Error: 29 - Porcentaje de participación es obligatorio - Linea: "
										+ numeroLinha);

					double porcentagemCoaseguradora = Double
							.parseDouble(porcentagemCoaseguradoraStr);

					String primaGsStr = linha.substring(ultimo + 3 + 22 + 2
							+ 22 + 6, ultimo + 3 + 22 + 2 + 22 + 3 + 22);

					if (primaGsStr.equals(""))
						erros
								.add("Error: 30 - Prima en Gs es obligatoria - Linea: "
										+ numeroLinha);

					double primaGs = Double.parseDouble(primaGsStr);

					String tipoMoedaPrimaGs = this.obterTipoMoeda(linha
							.substring(ultimo + 3 + 22 + 2 + 22 + 3 + 22,
									ultimo + 3 + 22 + 2 + 22 + 3 + 22 + 2));

					String primaMeStr = linha.substring(ultimo + 3 + 22 + 2
							+ 22 + 3 + 22 + 2, ultimo + 3 + 22 + 2 + 22 + 3
							+ 22 + 2 + 22);

					double primaMe = Double.parseDouble(primaMeStr);

					ultimo = ultimo + 3 + 22 + 2 + 22 + 3 + 22 + 2 + 22;

					if (erros.size() == 0) {
						DadosCoaseguro dadosCoaseguro = (DadosCoaseguro) this
								.getModelManager().getEntity("DadosCoaseguro");

						//dadosCoaseguro.verificarDuplicidade(apolice, cContas,
						// aseguradora2);

						dadosCoaseguro.atribuirOrigem(apolice.obterOrigem());
						dadosCoaseguro.atribuirDestino(apolice.obterDestino());
						dadosCoaseguro.atribuirResponsavel(apolice
								.obterResponsavel());
						dadosCoaseguro.atribuirTitulo("Datos do Instrumento: "
								+ numeroInstrumento);
						dadosCoaseguro.atribuirSuperior(apolice);

						//dadosCoaseguro.incluir();

						dadosCoaseguro.atribuirAseguradora(aseguradora2);
						dadosCoaseguro.atribuirGrupo(grupo);
						dadosCoaseguro.atribuirCapitalGs(capitalGs);
						dadosCoaseguro
								.atribuirTipoMoedaCapitalGs(tipoMoedaCapitalGs1);
						dadosCoaseguro.atribuirCapitalMe(capitalMe);
						dadosCoaseguro
								.atribuirParticipacao(porcentagemCoaseguradora);
						dadosCoaseguro.atribuirPrimaGs(primaGs);
						dadosCoaseguro
								.atribuirTipoMoedaPrimaGs(tipoMoedaPrimaGs);
						dadosCoaseguro.atribuirPrimaMe(primaMe);
						dadosCoaseguro.atribuirTipoInstrumento(tipoInstrumento);
						dadosCoaseguro.atribuirNumeroEndoso(numeroEndoso);
						dadosCoaseguro.atribuirCertificado(certificado);

						dadosCoaseguros.add(dadosCoaseguro);
					}
					/*
					 * else { if(apolice!=null) { apolice.excluir(); break; } }
					 */
				}
			}
			// REGISTRO TIPO 6
			else if (Integer.parseInt(linha.substring(5, 7)) == 6) {
				Sinistro sinistro = (Sinistro) this.getModelManager()
						.getEntity("Sinistro");

				String apelidoCconta = linha.substring(7, 17);

				ClassificacaoContas cContas = null;
				cContas = (ClassificacaoContas) entidadeHome
						.obterEntidadePorApelido(apelidoCconta.trim());

				if (cContas == null)
					erros.add("Error: 05 - Cuenta " + apelidoCconta.trim()
							+ " no fue encontrada - Linea: " + numeroLinha);

				String tipoInstrumento = "";

				if (linha.substring(17, 18).equals("1"))
					tipoInstrumento = "Póliza Individual";
				else if (linha.substring(17, 18).equals("2"))
					tipoInstrumento = "Póliza Madre";
				else if (linha.substring(17, 18).equals("3"))
					tipoInstrumento = "Certificado de Seguro Colectivo";
				else if (linha.substring(17, 18).equals("4"))
					tipoInstrumento = "Certificado Provisorio";
				else if (linha.substring(17, 18).equals("5"))
					tipoInstrumento = "Nota de Cobertura de Reaseguro";

				if (tipoInstrumento.equals(""))
					erros
							.add("Error: 101 - Instrumento es obligatorio - Linea: "
									+ numeroLinha);

				String numeroInstrumento = linha.substring(18, 28);

				Apolice apolice = null;

				if (this.apolices != null)
					apolice = (Apolice) this.apolices.get(numeroInstrumento);

				if (apolice == null)
					apolice = apoliceHome.obterApolice(numeroInstrumento);

				if (apolice == null)
					erros.add("Error: 73 - Instumento "
							+ numeroInstrumento.trim()
							+ " no fue encontrado - Linea: " + numeroLinha);

				double numeroEndoso = Double.parseDouble(linha
						.substring(28, 38));

				double certificado = Double
						.parseDouble(linha.substring(38, 45));

				String qtdeStr = linha.substring(45, 47);

				if (qtdeStr.equals(""))
					erros
							.add("Error: 31 - Cantidad de Siniestros es obligatoria - Linea: "
									+ numeroLinha);

				int qtde = Integer.parseInt(qtdeStr);

				int ultimo = 0;

				for (int w = 0; w < qtde; w++) {
					if (ultimo == 0)
						ultimo = 47;

					String numeroSinistro = linha.substring(ultimo, ultimo + 6);

					if (numeroSinistro.equals(""))
						erros
								.add("Error: 32 - Numero del Siniestro es obligatorio - Linea: "
										+ numeroLinha);

					String anoSinistro = linha.substring(ultimo + 6,
							ultimo + 6 + 4);

					String mesSinistro = linha.substring(ultimo + 6 + 4,
							ultimo + 6 + 4 + 2);

					String diaSinistro = linha.substring(ultimo + 6 + 4 + 2,
							ultimo + 6 + 4 + 2 + 2);

					Date dataSinistro = null;

					if (diaSinistro.startsWith(" ")
							|| mesSinistro.startsWith(" ")
							|| anoSinistro.startsWith(" "))
						erros
								.add("Error: 92 - Fecha Sinistro Invalida - Linea: "
										+ numeroLinha);
					else
						dataSinistro = new SimpleDateFormat("dd/MM/yyyy")
								.parse(diaSinistro + "/" + mesSinistro + "/"
										+ anoSinistro);

					String anoDenunciaSinistro = linha.substring(ultimo + 6 + 4
							+ 2 + 2, ultimo + 6 + 4 + 2 + 2 + 4);

					String mesDenunciaSinistro = linha.substring(ultimo + 6 + 4
							+ 2 + 2 + 4, ultimo + 6 + 4 + 2 + 2 + 4 + 2);

					String diaDenunciaSinistro = linha
							.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2, ultimo
									+ 6 + 4 + 2 + 2 + 4 + 2 + 2);

					Date dataDenunciaSinistro = null;

					if (diaDenunciaSinistro.startsWith(" ")
							|| mesDenunciaSinistro.startsWith(" ")
							|| anoDenunciaSinistro.startsWith(" "))
						erros
								.add("Error: 92 - Fecha Denuncia del Sinistro Invalida - Linea: "
										+ numeroLinha);
					else
						dataDenunciaSinistro = new SimpleDateFormat(
								"dd/MM/yyyy").parse(diaDenunciaSinistro + "/"
								+ mesDenunciaSinistro + "/"
								+ anoDenunciaSinistro);

					String numeroLiquidador = linha.substring(ultimo + 6 + 4
							+ 2 + 2 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 4 + 2
							+ 2 + 3);

					AuxiliarSeguro auxiliar = null;

					auxiliar = (AuxiliarSeguro) entidadeHome
							.obterEntidadePorInscricao(numeroLiquidador);

					if (auxiliar == null)
						erros
								.add("Error: 84 - Auxiliar de Seguro con Inscripción "
										+ numeroLiquidador
										+ " no fue encontrado - Linea: "
										+ numeroLinha);

					String montanteGsStr = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 4 + 2 + 2 + 3, ultimo + 6 + 4 + 2 + 2 + 4 + 2
							+ 2 + 3 + 22);

					if (montanteGsStr.equals(""))
						erros
								.add("Error: 33 - Monto estimado en Gs es obligatorio - Linea: "
										+ numeroLinha);

					double montanteGs = Double.parseDouble(montanteGsStr);

					String tipoMoedaMontante = this.obterTipoMoeda(linha
							.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3
									+ 22, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2
									+ 3 + 22 + 2));

					String montanteMeStr = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 4 + 2 + 2 + 3 + 22 + 2, ultimo + 6 + 4 + 2
							+ 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22);

					double montanteMe = Double.parseDouble(montanteMeStr);

					String situacaoSinistro = "";

					if (linha.substring(
							ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2
									+ 22,
							ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2
									+ 22 + 1).equals("1"))
						situacaoSinistro = "Pendiente de Liquidación";
					else if (linha.substring(
							ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2
									+ 22,
							ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2
									+ 22 + 1).equals("2"))
						situacaoSinistro = "Controvertido";
					else if (linha.substring(
							ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2
									+ 22,
							ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2
									+ 22 + 1).equals("3"))
						situacaoSinistro = "Pendiente de Pago";
					else if (linha.substring(
							ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2
									+ 22,
							ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2
									+ 22 + 1).equals("4"))
						situacaoSinistro = "Recharzado";
					else if (linha.substring(
							ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2
									+ 22,
							ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2
									+ 22 + 1).equals("5"))
						situacaoSinistro = "Judicializado";
					else if (linha.substring(
							ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2
									+ 22,
							ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2
									+ 22 + 1).equals("6"))
						situacaoSinistro = "Pagado";

					if (situacaoSinistro.equals(""))
						erros
								.add("Error: 34 - Situación del Siniestro es obligatoria - Linea: "
										+ numeroLinha);

					String anoFinalizacao = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1, ultimo + 6
							+ 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4);

					String mesFinalizacao = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4, ultimo
							+ 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1
							+ 4 + 2);

					String diaFinalizacao = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2,
							ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2
									+ 22 + 1 + 4 + 2 + 2);

					Date dataFinalizacao = null;

					if (diaFinalizacao.startsWith(" ")
							|| mesFinalizacao.startsWith(" ")
							|| anoFinalizacao.startsWith(" "))
						erros
								.add("Error: 92 - Fecha Finalización Invalida - Linea: "
										+ numeroLinha);
					else
						dataFinalizacao = new SimpleDateFormat("dd/MM/yyyy")
								.parse(diaFinalizacao + "/" + mesFinalizacao
										+ "/" + anoFinalizacao);

					String anoRecupero = linha.substring(ultimo + 6 + 4 + 2 + 2
							+ 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2,
							ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2
									+ 22 + 1 + 4 + 2 + 2 + 4);

					String mesRecupero = linha.substring(ultimo + 6 + 4 + 2 + 2
							+ 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4,
							ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2
									+ 22 + 1 + 4 + 2 + 2 + 4 + 2);

					String diaRecupero = linha.substring(ultimo + 6 + 4 + 2 + 2
							+ 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4
							+ 2, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22
							+ 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2);

					Date dataRecupero = null;

					if (diaRecupero.startsWith(" ")
							|| mesRecupero.startsWith(" ")
							|| anoRecupero.startsWith(" "))
						erros
								.add("Error: 92 - Fecha Recupero Invalida - Linea: "
										+ numeroLinha);
					else
						dataRecupero = new SimpleDateFormat("dd/MM/yyyy")
								.parse(diaRecupero + "/" + mesRecupero + "/"
										+ anoRecupero);

					String recuperoReaseguradoraStr = linha.substring(ultimo
							+ 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1
							+ 4 + 2 + 2 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 4
							+ 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2
							+ 2 + 22);

					double recuperoReaseguradora = Double
							.parseDouble(recuperoReaseguradoraStr);

					String recuperoTerceiroStr = linha.substring(ultimo + 6 + 4
							+ 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2
							+ 2 + 4 + 2 + 2 + 22, ultimo + 6 + 4 + 2 + 2 + 4
							+ 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2
							+ 2 + 22 + 22);

					double recuperoTerceiro = Double
							.parseDouble(recuperoTerceiroStr);

					String participacaoStr = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2
							+ 4 + 2 + 2 + 22 + 22, ultimo + 6 + 4 + 2 + 2 + 4
							+ 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2
							+ 2 + 22 + 22 + 6);

					double participacao = Double.parseDouble(participacaoStr);

					String descricao = linha.substring(ultimo + 6 + 4 + 2 + 2
							+ 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4
							+ 2 + 2 + 22 + 22 + 6, ultimo + 6 + 4 + 2 + 2 + 4
							+ 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2
							+ 2 + 22 + 22 + 2 + 120);

					if (descricao.equals(""))
						erros
								.add("Error: 35 - Descripción del Siniestro es obligatoria - Linea: "
										+ numeroLinha);

					ultimo = ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2
							+ 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22 + 22 + 2
							+ 120;

					sinistros.put(numeroSinistro, sinistro);

					if (erros.size() == 0) {
						sinistro.atribuirOrigem(apolice.obterOrigem());
						sinistro.atribuirDestino(apolice.obterDestino());
						sinistro
								.atribuirResponsavel(apolice.obterResponsavel());
						sinistro.atribuirTitulo("Datos do Instrumento: "
								+ numeroInstrumento);
						sinistro.atribuirSuperior(apolice);

						sinistro.atribuirNumero(numeroSinistro);
						sinistro.atribuirDataSinistro(dataSinistro);
						sinistro.atribuirDataDenuncia(dataDenunciaSinistro);
						sinistro.atribuirAgente(auxiliar);
						sinistro.atribuirMontanteGs(montanteGs);
						sinistro.atribuirTipoMoedaMontanteGs(tipoMoedaMontante);
						sinistro.atribuirMontanteMe(montanteMe);
						sinistro.atribuirSituacao(situacaoSinistro);
						sinistro.atribuirDataPagamento(dataFinalizacao);
						sinistro.atribuirDataRecuperacao(dataRecupero);
						sinistro
								.atribuirValorRecuperacao(recuperoReaseguradora);
						sinistro
								.atribuirValorRecuperacaoTerceiro(recuperoTerceiro);
						sinistro.atribuirParticipacao(participacao);
						sinistro.atribuirTipoInstrumento(tipoInstrumento);
						sinistro.atribuirNumeroEndoso(numeroEndoso);
						sinistro.atribuirCertificado(certificado);

						String descricao2 = "";

						int cont = 1;

						for (int j = 0; j < descricao.length();) {
							String caracter = descricao.substring(j, cont);

							if (j == 100) {
								boolean entrou = false;

								while (!caracter.equals(" ")) {
									descricao2 += caracter;
									j++;
									cont++;
									if (j == descricao.length())
										break;
									else
										caracter = descricao.substring(j, cont);

									entrou = true;
								}

								if (!entrou) {
									j++;
									cont++;
								} else
									descricao2 += "\n";
							} else {
								descricao2 += caracter;
								cont++;
								j++;
							}
						}

						sinistro.atribuirDescricao(descricao2);
					}
					/*
					 * else { if(apolice!=null) { apolice.excluir(); break; } }
					 */
				}
			}
			// REGISTRO TIPO 7
			else if (Integer.parseInt(linha.substring(5, 7)) == 7) {
				String apelidoCconta = linha.substring(7, 17);

				ClassificacaoContas cContas = null;
				cContas = (ClassificacaoContas) entidadeHome
						.obterEntidadePorApelido(apelidoCconta.trim());

				if (cContas == null)
					erros.add("Error: 05 - Cuenta " + apelidoCconta.trim()
							+ " no fue encontrada - Linea: " + numeroLinha);

				String tipoInstrumento = "";

				if (linha.substring(17, 18).equals("1"))
					tipoInstrumento = "Póliza Individual";
				else if (linha.substring(17, 18).equals("2"))
					tipoInstrumento = "Póliza Madre";
				else if (linha.substring(17, 18).equals("3"))
					tipoInstrumento = "Certificado de Seguro Colectivo";
				else if (linha.substring(17, 18).equals("4"))
					tipoInstrumento = "Certificado Provisorio";
				else if (linha.substring(17, 18).equals("5"))
					tipoInstrumento = "Nota de Cobertura de Reaseguro";

				if (tipoInstrumento.equals(""))
					erros
							.add("Error: 101 - Instrumento es obligatorio - Linea: "
									+ numeroLinha);

				String numeroInstrumento = linha.substring(18, 28);

				double numeroEndoso = Double.parseDouble(linha
						.substring(28, 38));

				double certificado = Double
						.parseDouble(linha.substring(38, 45));

				Apolice apolice = null;

				if (this.apolices != null)
					apolice = (Apolice) this.apolices.get(numeroInstrumento);

				if (apolice == null)
					apolice = apoliceHome.obterApolice(numeroInstrumento);

				if (apolice == null)
					erros.add("Error: 73 - Instumento "
							+ numeroInstrumento.trim()
							+ " no fue encontrado - Linea: " + numeroLinha);

				String qtdeStr = linha.substring(45, 47);

				if (qtdeStr.equals(""))
					erros
							.add("Error: 36 - Cantidad de Facturas es obligatoria - Linea: "
									+ numeroLinha);

				int qtde = Integer.parseInt(qtdeStr);

				int ultimo = 0;

				for (int w = 0; w < qtde; w++) {
					if (ultimo == 0)
						ultimo = 47;

					String numeroSinistro = linha.substring(ultimo, ultimo + 6);

					if (numeroSinistro.equals(""))
						erros
								.add("Error: 37 - Numero del Siniestro es obligatorio - Linea: "
										+ numeroLinha);

					Sinistro sinistro = null;

					if (this.sinistros != null)
						sinistro = (Sinistro) this.sinistros
								.get(numeroSinistro);

					if (sinistro == null)
						sinistro = sinistroHome.obterSinistro(numeroSinistro);

					if (sinistro == null)
						erros.add("Error: 74 - Siniestro "
								+ numeroSinistro.trim()
								+ " no fue encontrado - Linea: " + numeroLinha);

					String anoSinistro = linha.substring(ultimo + 6,
							ultimo + 6 + 4);

					String mesSinistro = linha.substring(ultimo + 6 + 4,
							ultimo + 6 + 4 + 2);

					String diaSinistro = linha.substring(ultimo + 6 + 4 + 2,
							ultimo + 6 + 4 + 2 + 2);

					Date dataSinistro = null;

					if (diaSinistro.startsWith(" ")
							|| mesSinistro.startsWith(" ")
							|| anoSinistro.startsWith(" "))
						erros
								.add("Error: 92 - Fecha Sinistro Invalida - Linea: "
										+ numeroLinha);
					else
						dataSinistro = new SimpleDateFormat("dd/MM/yyyy")
								.parse(diaSinistro + "/" + mesSinistro + "/"
										+ anoSinistro);

					String tipoDocumento = "";

					if (linha.substring(ultimo + 6 + 4 + 2 + 2,
							ultimo + 6 + 4 + 2 + 2 + 1).equals("1"))
						tipoDocumento = "Factura Crédito";
					else if (linha.substring(ultimo + 6 + 4 + 2 + 2,
							ultimo + 6 + 4 + 2 + 2 + 1).equals("2"))
						tipoDocumento = "Autofactura";
					else if (linha.substring(ultimo + 6 + 4 + 2 + 2,
							ultimo + 6 + 4 + 2 + 2 + 1).equals("3"))
						tipoDocumento = "Boleta de Venta";
					else if (linha.substring(ultimo + 6 + 4 + 2 + 2,
							ultimo + 6 + 4 + 2 + 2 + 1).equals("4"))
						tipoDocumento = "Nota de Crédito";
					else if (linha.substring(ultimo + 6 + 4 + 2 + 2,
							ultimo + 6 + 4 + 2 + 2 + 1).equals("5"))
						tipoDocumento = "Nota de Débito";
					else if (linha.substring(ultimo + 6 + 4 + 2 + 2,
							ultimo + 6 + 4 + 2 + 2 + 1).equals("6"))
						tipoDocumento = "Recibo";
					else if (linha.substring(ultimo + 6 + 4 + 2 + 2,
							ultimo + 6 + 4 + 2 + 2 + 1).equals("7"))
						tipoDocumento = "Factura al Contado";
					else if (linha.substring(ultimo + 6 + 4 + 2 + 2,
							ultimo + 6 + 4 + 2 + 2 + 1).equals("8"))
						tipoDocumento = "Otros";

					if (tipoDocumento.equals(""))
						erros
								.add("Error: 37 - Tipo del Documento Recibido es obligatorio - Linea: "
										+ numeroLinha);

					String numeroDocumento = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 1, ultimo + 6 + 4 + 2 + 2 + 1 + 9);

					if (numeroDocumento.equals(""))
						erros
								.add("Error: 38 - Numero del Documento del Proveedor es obligatorio - Linea: "
										+ numeroLinha);

					String numeroFatura = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 1 + 9, ultimo + 6 + 4 + 2 + 2 + 1 + 9 + 9);

					String tipoDocumentoProvedor = "";

					if (linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 9 + 9,
							ultimo + 6 + 4 + 2 + 2 + 1 + 9 + 9 + 1).equals("1"))
						tipoDocumentoProvedor = "CI";
					else if (linha.substring(
							ultimo + 6 + 4 + 2 + 2 + 1 + 9 + 9,
							ultimo + 6 + 4 + 2 + 2 + 1 + 9 + 9 + 1).equals("2"))
						tipoDocumentoProvedor = "RUC";

					if (tipoDocumentoProvedor.equals(""))
						erros
								.add("Error: 102 - Tipo del Documento del Proveedor es obligatorio - Linea: "
										+ numeroLinha);

					String rucProvedor = linha.substring(ultimo + 6 + 4 + 2 + 2
							+ 1 + 9 + 9 + 1, ultimo + 6 + 4 + 2 + 2 + 1 + 9 + 9
							+ 11);

					if (rucProvedor.equals(""))
						erros
								.add("Error: 39 - Numero del RUC o CI del Proveedor es obligatorio - Linea: "
										+ numeroLinha);

					String nomeProvedor = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 1 + 9 + 9 + 11, ultimo + 6 + 4 + 2 + 2 + 1
							+ 9 + 9 + 11 + 60);

					if (nomeProvedor.equals(""))
						erros
								.add("Erro: 40 - Nombre del Proveedor es obligatorio - Linea: "
										+ numeroLinha);

					String anoDocumento = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 1 + 9 + 9 + 11 + 60, ultimo + 6 + 4 + 2 + 2
							+ 1 + 9 + 9 + 11 + 60 + 4);

					String mesDocumento = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 1 + 9 + 9 + 11 + 60 + 4, ultimo + 6 + 4 + 2
							+ 2 + 1 + 9 + 9 + 11 + 60 + 4 + 2);

					String diaDocumento = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 1 + 9 + 9 + 11 + 60 + 4 + 2, ultimo + 6 + 4
							+ 2 + 2 + 1 + 9 + 9 + 11 + 60 + 4 + 2 + 2);

					Date dataDocumento = null;

					if (diaDocumento.startsWith(" ")
							|| mesDocumento.startsWith(" ")
							|| anoDocumento.startsWith(" "))
						erros
								.add("Error: 92 - Fecha Documento Invalida - Linea: "
										+ numeroLinha);
					else
						dataDocumento = new SimpleDateFormat("dd/MM/yyyy")
								.parse(diaDocumento + "/" + mesDocumento + "/"
										+ anoDocumento);

					String montanteDocumentoStr = linha.substring(ultimo + 6
							+ 4 + 2 + 2 + 1 + 9 + 9 + 11 + 60 + 4 + 2 + 2,
							ultimo + 6 + 4 + 2 + 2 + 1 + 9 + 9 + 11 + 60 + 4
									+ 2 + 2 + 22);

					if (montanteDocumentoStr.equals(""))
						erros
								.add("Error: 41 - Valor del Documento es obligatorio - Linea: "
										+ numeroLinha);

					double montanteDocumento = Double
							.parseDouble(montanteDocumentoStr);

					String anoPagamento = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 1 + 9 + 9 + 11 + 60 + 4 + 2 + 2 + 22, ultimo
							+ 6 + 4 + 2 + 2 + 1 + 9 + 9 + 11 + 60 + 4 + 2 + 2
							+ 22 + 4);

					String mesPagamento = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 1 + 9 + 9 + 11 + 60 + 4 + 2 + 2 + 22 + 4,
							ultimo + 6 + 4 + 2 + 2 + 1 + 9 + 9 + 11 + 60 + 4
									+ 2 + 2 + 22 + 4 + 2);

					String diaPagamento = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 1 + 9 + 9 + 11 + 60 + 4 + 2 + 2 + 22 + 4 + 2,
							ultimo + 6 + 4 + 2 + 2 + 1 + 9 + 9 + 11 + 60 + 4
									+ 2 + 2 + 22 + 4 + 2 + 2);

					Date dataPagamento = null;

					if (diaPagamento.startsWith(" ")
							|| mesPagamento.startsWith(" ")
							|| anoPagamento.startsWith(" "))
						erros
								.add("Error: 92 - Fecha Pagamento Invalida - Linea: "
										+ numeroLinha);
					else
						dataPagamento = new SimpleDateFormat("dd/MM/yyyy")
								.parse(diaPagamento + "/" + mesPagamento + "/"
										+ anoPagamento);

					String situacaoFatura = "";

					if (linha.substring(
							ultimo + 6 + 4 + 2 + 2 + 1 + 9 + 9 + 11 + 60 + 4
									+ 2 + 2 + 22 + 4 + 2 + 2,
							ultimo + 6 + 4 + 2 + 2 + 1 + 9 + 9 + 11 + 60 + 4
									+ 2 + 2 + 22 + 4 + 2 + 2 + 1).equals("1"))
						situacaoFatura = "Pagada";
					else if (linha.substring(
							ultimo + 6 + 4 + 2 + 2 + 1 + 9 + 9 + 11 + 60 + 4
									+ 2 + 2 + 22 + 4 + 2 + 2,
							ultimo + 6 + 4 + 2 + 2 + 1 + 9 + 9 + 11 + 60 + 4
									+ 2 + 2 + 22 + 4 + 2 + 2 + 1).equals("2"))
						situacaoFatura = "Pendiente";
					else if (linha.substring(
							ultimo + 6 + 4 + 2 + 2 + 1 + 9 + 9 + 11 + 60 + 4
									+ 2 + 2 + 22 + 4 + 2 + 2,
							ultimo + 6 + 4 + 2 + 2 + 1 + 9 + 9 + 11 + 60 + 4
									+ 2 + 2 + 22 + 4 + 2 + 2 + 1).equals("3"))
						situacaoFatura = "Anulada";

					if (situacaoFatura.equals(""))
						erros
								.add("Error: 42 - Situación de la Factura es obligatoria - Linea: "
										+ numeroLinha);

					ultimo = ultimo + 6 + 4 + 2 + 2 + 1 + 9 + 9 + 11 + 60 + 4
							+ 2 + 2 + 22 + 4 + 2 + 2 + 1;

					if (erros.size() == 0) {
						FaturaSinistro fatura = (FaturaSinistro) this
								.getModelManager().getEntity("FaturaSinistro");

						//fatura.verificarDuplicidade(sinistro, tipoDocumento,
						// numeroDocumento, rucProvedor);

						fatura.atribuirOrigem(sinistro.obterOrigem());
						fatura.atribuirDestino(sinistro.obterDestino());
						fatura.atribuirResponsavel(sinistro.obterResponsavel());
						fatura.atribuirTitulo("Datos do Instrumento: "
								+ numeroInstrumento);
						fatura.atribuirSuperior(sinistro);

						//fatura.incluir();

						fatura.atribuirDataSinistro(dataSinistro);
						fatura.atribuirTipo(tipoDocumento);
						fatura.atribuirNumeroDocumento(numeroDocumento);
						fatura.atribuirNumeroFatura(numeroFatura);
						fatura.atribuirRucProvedor(rucProvedor);
						fatura.atribuirNomeProvedor(nomeProvedor);
						fatura.atribuirDataDocumento(dataDocumento);
						fatura.atribuirMontanteDocumento(montanteDocumento);
						fatura.atribuirDataPagamento(dataPagamento);
						fatura.atribuirSituacaoFatura(situacaoFatura);
						fatura.atribuirTipoInstrumento(tipoInstrumento);
						fatura.atribuirNumeroEndoso(numeroEndoso);
						fatura.atribuirCertificado(certificado);
						fatura
								.atribuirTipoDocumentoProveedor(tipoDocumentoProvedor);

						faturas.add(fatura);
					}
					/*
					 * else { if(apolice!=null) { apolice.excluir(); break; } }
					 */
				}
			}
			// REGISTRO TIPO 8
			else if (Integer.parseInt(linha.substring(5, 7)) == 8) {
				String apelidoCconta = linha.substring(7, 17);

				ClassificacaoContas cContas = null;
				cContas = (ClassificacaoContas) entidadeHome
						.obterEntidadePorApelido(apelidoCconta.trim());

				if (cContas == null)
					erros.add("Error: 05 - Cuenta " + apelidoCconta.trim()
							+ " no fue encontrada - Linea: " + numeroLinha);

				String numeroInstrumento = linha.substring(17, 27);

				Apolice apolice = null;

				if (this.apolices != null)
					apolice = (Apolice) this.apolices.get(numeroInstrumento);

				if (apolice == null)
					apolice = apoliceHome.obterApolice(numeroInstrumento);

				if (apolice == null)
					erros.add("Error: 73 - Instumento "
							+ numeroInstrumento.trim()
							+ " no fue encontrado - Linea: " + numeroLinha);

				String anoAnulacao = linha.substring(27, 31);

				String mesAnulacao = linha.substring(31, 33);

				String diaAnulacao = linha.substring(33, 35);

				Date dataAnulacao = null;

				if (diaAnulacao.startsWith(" ") || mesAnulacao.startsWith(" ")
						|| anoAnulacao.startsWith(" "))
					erros.add("Error: 92 - Fecha Anulación Invalida - Linea: "
							+ numeroLinha);
				else
					dataAnulacao = new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaAnulacao + "/" + mesAnulacao + "/"
									+ anoAnulacao);

				String tipoAnulacao = "";

				if (linha.substring(35, 36).equals("1"))
					tipoAnulacao = "Total";
				else if (linha.substring(35, 36).equals("2"))
					tipoAnulacao = "Parcial";

				String capitalAnuladoGsStr = linha.substring(36, 58);

				double capitalAnuladoGs = Double
						.parseDouble(capitalAnuladoGsStr);

				String tipoMoedaCapitalAnulado = this.obterTipoMoeda(linha
						.substring(58, 60));

				String capitalAnuladoMeStr = linha.substring(60, 82);

				double capitalAnuladoMe = Double
						.parseDouble(capitalAnuladoMeStr);

				String solicitado = "";

				if (linha.substring(82, 83).equals("1"))
					solicitado = "Asegurado";
				else if (linha.substring(82, 83).equals("2"))
					solicitado = "Tomador";
				else if (linha.substring(82, 83).equals("3"))
					solicitado = "Compañia Aseguradora";
				else if (linha.substring(82, 83).equals("4"))
					solicitado = "Otros";

				if (solicitado.equals(""))
					erros
							.add("Error: 43 - Solicitado Por es obligatorio - Linea: "
									+ numeroLinha);

				String diasCorridos = linha.substring(83, 88);

				if (diasCorridos.equals(""))
					erros
							.add("Error: 44 - Dias Corridos es obligatorio - Linea: "
									+ numeroLinha);

				String primaAnuladaGsStr = linha.substring(88, 110);

				if (primaAnuladaGsStr.equals(""))
					erros
							.add("Error: 45 - Prima Anulada en Gs es obligatoria - Linea: "
									+ numeroLinha);

				double primaAnuladaGs = Double.parseDouble(primaAnuladaGsStr);

				String tipoMoedaPrimaAnuladaGs = this.obterTipoMoeda(linha
						.substring(110, 112));

				String primaAnuladaMeStr = linha.substring(112, 134);

				if (primaAnuladaMeStr.equals(""))
					erros
							.add("Error: 46 - Prima Anulada en M/E es obligatoria - Linea: "
									+ numeroLinha);

				double primaAnuladaMe = Double.parseDouble(primaAnuladaMeStr);

				String comissaoAnuladaGsStr = linha.substring(134, 156);

				double comissaoAnuladaGs = Double
						.parseDouble(comissaoAnuladaGsStr);

				String tipoMoedaComissaoAnuladaGs = this.obterTipoMoeda(linha
						.substring(156, 158));

				String comissaoAnuladaMeStr = linha.substring(158, 180);

				if (comissaoAnuladaMeStr.equals(""))
					erros
							.add("Error: 47 - Comisión Anulada en M/E es obligatoria - Linea: "
									+ numeroLinha);

				double comissaoAnuladaMe = Double
						.parseDouble(comissaoAnuladaMeStr);

				String comissaoRecuperarGsStr = linha.substring(180, 202);

				double comissaoRecuperarGs = Double
						.parseDouble(comissaoRecuperarGsStr);

				String tipoMoedaComissaoRecuperarGs = this.obterTipoMoeda(linha
						.substring(202, 204));

				String comissaoRecuperarMeStr = linha.substring(204, 226);

				double comissaoRecuperarMe = Double
						.parseDouble(comissaoRecuperarMeStr);

				String saldoAnuladoGsStr = linha.substring(226, 248);

				if (saldoAnuladoGsStr.equals(""))
					erros
							.add("Error: 48 - Saldo Anulación en Gs es obligatorio - Linea: "
									+ numeroLinha);

				double saldoAnuladoGs = Double.parseDouble(saldoAnuladoGsStr);

				String tipoMoedaSaldoAnuladoGs = this.obterTipoMoeda(linha
						.substring(248, 250));

				String saldoAnuladoMeStr = linha.substring(250, 272);

				if (saldoAnuladoMeStr.equals(""))
					erros
							.add("Error: 49 - Saldo Anulación en M/E es obligatorio - Linea: "
									+ numeroLinha);

				double saldoAnuladoMe = Double.parseDouble(saldoAnuladoMeStr);

				String destinoSaldoAnulacao = "";

				if (linha.substring(272, 273).equals("1"))
					destinoSaldoAnulacao = "Cuando el Saldo es Cero";
				else if (linha.substring(272, 273).equals("2"))
					destinoSaldoAnulacao = "A favor del Asegurado/Tomador";
				else if (linha.substring(272, 273).equals("3"))
					destinoSaldoAnulacao = "A favor de la Compañia";

				String motivoAnulacao = linha.substring(273, 393);

				String tipoInstrumento = "";

				if (linha.substring(393, 394).equals("1"))
					tipoInstrumento = "Póliza Individual";
				else if (linha.substring(393, 394).equals("2"))
					tipoInstrumento = "Póliza Madre";
				else if (linha.substring(393, 394).equals("3"))
					tipoInstrumento = "Certificado de Seguro Colectivo";
				else if (linha.substring(393, 394).equals("4"))
					tipoInstrumento = "Certificado Provisorio";
				else if (linha.substring(393, 394).equals("5"))
					tipoInstrumento = "Nota de Cobertura de Reaseguro";

				if (tipoInstrumento.equals(""))
					erros
							.add("Error: 101 - Instrumento es obligatorio - Linea: "
									+ numeroLinha);

				double numeroEndoso = Double.parseDouble(linha.substring(394,
						404));

				double certificado = Double.parseDouble(linha.substring(404,
						411));

				if (erros.size() == 0) {
					AnulacaoInstrumento anulacao = (AnulacaoInstrumento) this
							.getModelManager().getEntity("AnulacaoInstrumento");

					anulacao.atribuirOrigem(apolice.obterOrigem());
					anulacao.atribuirDestino(apolice.obterDestino());
					anulacao.atribuirResponsavel(apolice.obterResponsavel());
					anulacao.atribuirTitulo("Datos do Instrumento: "
							+ numeroInstrumento);
					anulacao.atribuirSuperior(apolice);

					anulacao.atribuirDataAnulacao(dataAnulacao);
					anulacao.atribuirTipo(tipoAnulacao);
					anulacao.atribuirCapitalGs(capitalAnuladoGs);
					anulacao
							.atribuirTipoMoedaCapitalGs(tipoMoedaCapitalAnulado);
					anulacao.atribuirCapitalMe(capitalAnuladoMe);
					anulacao.atribuirSolicitadoPor(solicitado);
					anulacao.atribuirDiasCorridos(Integer
							.parseInt(diasCorridos));
					anulacao.atribuirPrimaGs(primaAnuladaGs);
					anulacao.atribuirTipoMoedaPrimaGs(tipoMoedaPrimaAnuladaGs);
					anulacao.atribuirPrimaMe(primaAnuladaMe);
					anulacao.atribuirComissaoGs(comissaoAnuladaGs);
					anulacao
							.atribuirTipoMoedaComissaoGs(tipoMoedaComissaoAnuladaGs);
					anulacao.atribuirComissaoMe(comissaoAnuladaMe);
					anulacao.atribuirComissaoRecuperarGs(comissaoRecuperarGs);
					anulacao
							.atribuirTipoMoedaComissaoRecuperarGs(tipoMoedaComissaoRecuperarGs);
					anulacao.atribuirComissaoRecuperarMe(comissaoRecuperarMe);
					anulacao.atribuirSaldoAnulacaoGs(saldoAnuladoGs);
					anulacao
							.atribuirTipoMoedaSaldoAnulacaoGs(tipoMoedaSaldoAnuladoGs);
					anulacao.atribuirSaldoAnulacaoMe(saldoAnuladoMe);
					anulacao.atribuirDestinoSaldoAnulacao(destinoSaldoAnulacao);
					anulacao.atribuirTipoInstrumento(tipoInstrumento);
					anulacao.atribuirNumeroEndoso(numeroEndoso);
					anulacao.atribuirCertificado(certificado);

					apolice.atualizarDataEncerramento(dataAnulacao);
					apolice.atualizarSituacaoSeguro("No Vigente");

					String motivoAnulacao2 = "";

					int cont = 1;

					for (int j = 0; j < motivoAnulacao.length();) {
						String caracter = motivoAnulacao.substring(j, cont);

						if (j == 100) {
							boolean entrou = false;

							while (!caracter.equals(" ")) {
								motivoAnulacao2 += caracter;
								j++;
								cont++;
								if (j == motivoAnulacao.length())
									break;
								else
									caracter = motivoAnulacao
											.substring(j, cont);

								entrou = true;
							}

							if (!entrou) {
								j++;
								cont++;
							} else
								motivoAnulacao2 += "\n";
						} else {
							motivoAnulacao2 += caracter;
							cont++;
							j++;
						}
					}

					anulacao.atribuirDescricao(motivoAnulacao2);

					anulacoes.add(anulacao);
				}
				/*
				 * else { if(apolice!=null) { apolice.excluir(); break; } }
				 */
			}
			// REGISTRO TIPO 9
			else if (Integer.parseInt(linha.substring(5, 7)) == 9) {
				String apelidoCconta = linha.substring(7, 17);

				ClassificacaoContas cContas = null;
				cContas = (ClassificacaoContas) entidadeHome
						.obterEntidadePorApelido(apelidoCconta.trim());

				if (cContas == null)
					erros.add("Error: 05 - Cuenta " + apelidoCconta.trim()
							+ " no fue encontrada - Linea: " + numeroLinha);

				String tipoInstrumento = "";

				if (linha.substring(17, 18).equals("1"))
					tipoInstrumento = "Póliza Individual";
				else if (linha.substring(17, 18).equals("2"))
					tipoInstrumento = "Póliza Madre";
				else if (linha.substring(17, 18).equals("3"))
					tipoInstrumento = "Certificado de Seguro Colectivo";
				else if (linha.substring(17, 18).equals("4"))
					tipoInstrumento = "Certificado Provisorio";
				else if (linha.substring(17, 18).equals("5"))
					tipoInstrumento = "Nota de Cobertura de Reaseguro";

				if (tipoInstrumento.equals(""))
					erros
							.add("Error: 101 - Instrumento es obligatorio - Linea: "
									+ numeroLinha);

				String numeroInstrumento = linha.substring(18, 28);

				Apolice apolice = null;

				if (this.apolices != null)
					apolice = (Apolice) this.apolices.get(numeroInstrumento);

				if (apolice == null)
					apolice = apoliceHome.obterApolice(numeroInstrumento);

				if (apolice == null)
					erros.add("Error: 73 - Instumento "
							+ numeroInstrumento.trim()
							+ " no fue encontrado - Linea: " + numeroLinha);

				double numeroEndoso = Double.parseDouble(linha
						.substring(28, 38));

				double certificado = Double
						.parseDouble(linha.substring(38, 45));

				String qtdeStr = linha.substring(45, 47);

				if (qtdeStr.equals(""))
					erros
							.add("Error: 50 - Cantidad de Cobranzas es obligatoria - Linea: "
									+ numeroLinha);

				int qtde = Integer.parseInt(qtdeStr);

				int ultimo = 0;

				for (int w = 0; w < qtde; w++) {
					if (ultimo == 0)
						ultimo = 47;

					String anoCobranca = linha.substring(ultimo, ultimo + 4);

					String mesCobranca = linha.substring(ultimo + 4,
							ultimo + 4 + 2);

					String diaCobranca = linha.substring(ultimo + 4 + 2,
							ultimo + 4 + 2 + 2);

					Date dataCobranca = null;

					if (diaCobranca.startsWith(" ")
							|| mesCobranca.startsWith(" ")
							|| anoCobranca.startsWith(" "))
						erros
								.add("Error: 92 - Fecha Cobranza Invalida - Linea: "
										+ numeroLinha);
					else
						dataCobranca = new SimpleDateFormat("dd/MM/yyyy")
								.parse(diaCobranca + "/" + mesCobranca + "/"
										+ anoCobranca);

					String anoVencimentoCobranca = linha.substring(
							ultimo + 4 + 2 + 2, ultimo + 4 + 2 + 2 + 4);

					String mesVencimentoCobranca = linha.substring(ultimo + 4
							+ 2 + 2 + 4, ultimo + 4 + 2 + 2 + 4 + 2);

					String diaVencimentoCobranca = linha.substring(ultimo + 4
							+ 2 + 2 + 4 + 2, ultimo + 4 + 2 + 2 + 4 + 2 + 2);

					Date dataVencimentoCobranca = null;

					if (diaVencimentoCobranca.startsWith(" ")
							|| mesVencimentoCobranca.startsWith(" ")
							|| anoVencimentoCobranca.startsWith(" "))
						erros
								.add("Error: 92 - Fecha Vencimiento Cobranza Invalida - Linea: "
										+ numeroLinha);
					else
						dataVencimentoCobranca = new SimpleDateFormat(
								"dd/MM/yyyy").parse(diaVencimentoCobranca + "/"
								+ mesVencimentoCobranca + "/"
								+ anoVencimentoCobranca);

					String cotaCobranca = linha.substring(ultimo + 4 + 2 + 2
							+ 4 + 2 + 2, ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2);

					if (cotaCobranca.equals(""))
						erros
								.add("Error: 51 - Numero de la Cuota de la Cobranza es obligatorio - Linea: "
										+ numeroLinha);

					String valorCobrancaGsStr = linha.substring(ultimo + 4 + 2
							+ 2 + 4 + 2 + 2 + 2, ultimo + 4 + 2 + 2 + 4 + 2 + 2
							+ 2 + 22);

					if (valorCobrancaGsStr.equals(""))
						erros
								.add("Error: 52 - Valor en Gs de la Cobranza es obligatorio - Linea: "
										+ numeroLinha);

					double valorCobrancaGs = Double
							.parseDouble(valorCobrancaGsStr);

					String tipoMoedaCobrancaGs = this
							.obterTipoMoeda(linha.substring(ultimo + 4 + 2 + 2
									+ 4 + 2 + 2 + 2 + 22, ultimo + 4 + 2 + 2
									+ 4 + 2 + 2 + 2 + 22 + 2));

					String valorCobrancaMeStr = linha.substring(ultimo + 4 + 2
							+ 2 + 4 + 2 + 2 + 2 + 22 + 2, ultimo + 4 + 2 + 2
							+ 4 + 2 + 2 + 2 + 22 + 2 + 22);

					double valorCobrancaMe = Double
							.parseDouble(valorCobrancaMeStr);

					String valorInteresCobrancaStr = linha.substring(ultimo + 4
							+ 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2 + 22, ultimo + 4
							+ 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2 + 22 + 22);

					double valorInteresCobranca = Double
							.parseDouble(valorInteresCobrancaStr);

					ultimo = ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2 + 22
							+ 22;

					if (erros.size() == 0) {
						RegistroCobranca cobranca = (RegistroCobranca) this
								.getModelManager()
								.getEntity("RegistroCobranca");

						//cobranca.verificarDuplicidade(apolice, cContas,
						// dataCobranca, Integer.parseInt(cotaCobranca));

						cobranca.atribuirOrigem(apolice.obterOrigem());
						cobranca.atribuirDestino(apolice.obterDestino());
						cobranca
								.atribuirResponsavel(apolice.obterResponsavel());
						cobranca.atribuirTitulo("Datos do Instrumento: "
								+ numeroInstrumento);
						cobranca.atribuirSuperior(apolice);

						//cobranca.incluir();

						cobranca.atribuirDataCobranca(dataCobranca);
						cobranca.atribuirDataVencimento(dataVencimentoCobranca);
						cobranca.atribuirNumeroParcela(Integer
								.parseInt(cotaCobranca));
						cobranca.atribuirValorCobrancaGs(valorCobrancaGs);
						cobranca
								.atribuirTipoMoedaValorCobrancaGs(tipoMoedaCobrancaGs);
						cobranca.atribuirValorCobrancaMe(valorCobrancaMe);
						cobranca.atribuirValorInteres(valorInteresCobranca);
						cobranca.atribuirTipoInstrumento(tipoInstrumento);
						cobranca.atribuirNumeroEndoso(numeroEndoso);
						cobranca.atribuirCertificado(certificado);

						cobrancas.add(cobranca);
					}
					/*
					 * else { if(apolice!=null) { apolice.excluir(); break; } }
					 */
				}
			}
			// REGISTRO TIPO 10
			else if (Integer.parseInt(linha.substring(5, 7)) == 10) {
				String apelidoCconta = linha.substring(7, 17);

				ClassificacaoContas cContas = null;
				cContas = (ClassificacaoContas) entidadeHome
						.obterEntidadePorApelido(apelidoCconta.trim());

				if (cContas == null)
					erros.add("Error: 05 - Cuenta " + apelidoCconta.trim()
							+ " no fue encontrada - Linea: " + numeroLinha);

				String numeroInstrumento = linha.substring(17, 27);

				Apolice apolice = null;

				if (this.apolices != null)
					apolice = (Apolice) this.apolices.get(numeroInstrumento);

				if (apolice == null)
					apolice = apoliceHome.obterApolice(numeroInstrumento);

				if (apolice == null)
					erros.add("Error: 73 - Instumento "
							+ numeroInstrumento.trim()
							+ " no fue encontrado - Linea: " + numeroLinha);

				String numeroOrdem = linha.substring(27, 33);

				if (numeroOrdem.equals(""))
					erros
							.add("Error: 53 - Numero de Orden es obligatorio - Linea: "
									+ numeroLinha);

				String anoNotificacao = linha.substring(33, 37);

				String mesNotificacao = linha.substring(37, 39);

				String diaNotificacao = linha.substring(39, 41);

				Date dataNotificacao = null;

				if (diaNotificacao.startsWith(" ")
						|| mesNotificacao.startsWith(" ")
						|| anoNotificacao.startsWith(" "))
					erros
							.add("Error: 92 - Fecha Notificación Invalida - Linea: "
									+ numeroLinha);
				else
					dataNotificacao = new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaNotificacao + "/" + mesNotificacao + "/"
									+ anoNotificacao);

				String assuntoQuestionado = linha.substring(41, 161);

				if (assuntoQuestionado.equals(""))
					erros
							.add("Error: 54 - Asunto Cuestionado es obligatorio - Linea: "
									+ numeroLinha);

				String demandante = linha.substring(161, 201);

				if (demandante.equals(""))
					erros
							.add("Error: 55 - Actor o Demandante es obligatorio - Linea: "
									+ numeroLinha);

				String demandado = linha.substring(201, 241);

				if (demandado.equals(""))
					erros.add("Error: 56 - Demandado es obligatorio - Linea: "
							+ numeroLinha);

				String julgado = linha.substring(241, 243);

				if (julgado.equals(""))
					erros.add("Error: 57 - Juzgado es obligatorio - Linea: "
							+ numeroLinha);

				String turno = linha.substring(243, 245);

				if (turno.equals(""))
					erros.add("Error: 58 - Turno es obligatorio - Linea: "
							+ numeroLinha);

				String juiz = linha.substring(245, 285);

				if (juiz.equals(""))
					erros.add("Error: 59 - Juez es obligatorio - Linea: "
							+ numeroLinha);

				String numeroSecretaria = linha.substring(285, 287);

				if (numeroSecretaria.equals(""))
					erros
							.add("Error: 60 - Numero de la Secretaria es obligatorio - Linea: "
									+ numeroLinha);

				String advogado = linha.substring(287, 327);

				if (advogado.equals(""))
					erros
							.add("Error: 61 - Abogado que esta a cargo del caso es obligatorio - Linea: "
									+ numeroLinha);

				String circunscricao = linha.substring(327, 329);

				if (circunscricao.equals(""))
					erros
							.add("Error: 62 - Circunscripción es obligatoria - Linea: "
									+ numeroLinha);

				String forum = linha.substring(329, 331);

				if (forum.equals(""))
					erros.add("Error: 63 - Fuero es obligatorio - Linea: "
							+ numeroLinha);

				String anoDemanda = linha.substring(331, 335);

				String mesDemanda = linha.substring(335, 337);

				String diaDemanda = linha.substring(337, 339);

				Date dataDemanda = null;

				if (diaDemanda.startsWith(" ") || mesDemanda.startsWith(" ")
						|| anoDemanda.startsWith(" "))
					erros.add("Error: 92 - Fecha Demanda Invalida - Linea: "
							+ numeroLinha);
				else
					dataDemanda = new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaDemanda + "/" + mesDemanda + "/"
									+ anoDemanda);

				String montanteDemandandoStr = linha.substring(339, 361);

				if (montanteDemandandoStr.equals(""))
					erros
							.add("Error: 64 - Valor demandado es obligatorio - Linea: "
									+ numeroLinha);

				double montanteDemandando = Double
						.parseDouble(montanteDemandandoStr);

				String montanteSentencaStr = linha.substring(361, 383);

				double montanteSentenca = Double
						.parseDouble(montanteSentencaStr);

				String anoCancelamento = linha.substring(383, 387);

				String mesCancelamento = linha.substring(387, 389);

				String diaCancelamento = linha.substring(389, 391);

				Date dataCancelamento = null;

				if (diaCancelamento.startsWith(" ")
						|| mesCancelamento.startsWith(" ")
						|| anoCancelamento.startsWith(" "))
					erros
							.add("Error: 92 - Fecha Cancelación Invalida - Linea: "
									+ numeroLinha);
				else
					dataCancelamento = new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaCancelamento + "/" + mesCancelamento
									+ "/" + anoCancelamento);

				String caraterDemanda = "";

				if (linha.substring(391, 392).equals("1"))
					caraterDemanda = "La propria Compañia";
				else if (linha.substring(391, 392).equals("2"))
					caraterDemanda = "Citada en garantia";

				if (caraterDemanda.equals(""))
					erros
							.add("Error: 65 - Carácter de la demanda es obligatorio - Linea: "
									+ numeroLinha);

				String responsabilidadeMaximaStr = linha.substring(392, 414);

				double responsabilidadeMaxima = Double
						.parseDouble(responsabilidadeMaximaStr);

				String provisaoSinistroStr = linha.substring(414, 436);

				double provisaoSinistro = Double
						.parseDouble(provisaoSinistroStr);

				String objetoCausa = linha.substring(436, 686);

				if (objetoCausa.equals(""))
					erros
							.add("Error: 66 - Objeto de la Causa es obligatorio - Linea: "
									+ numeroLinha);

				String observacoes = linha.substring(686, 936);

				String tipoInstrumento = "";

				if (linha.substring(936, 937).equals("1"))
					tipoInstrumento = "Póliza Individual";
				else if (linha.substring(936, 937).equals("2"))
					tipoInstrumento = "Póliza Madre";
				else if (linha.substring(936, 937).equals("3"))
					tipoInstrumento = "Certificado de Seguro Colectivo";
				else if (linha.substring(936, 937).equals("4"))
					tipoInstrumento = "Certificado Provisorio";
				else if (linha.substring(936, 937).equals("5"))
					tipoInstrumento = "Nota de Cobertura de Reaseguro";

				if (tipoInstrumento.equals(""))
					erros
							.add("Error: 101 - Instrumento es obligatorio - Linea: "
									+ numeroLinha);

				double numeroEndoso = Double.parseDouble(linha.substring(937,
						947));

				double certificado = Double.parseDouble(linha.substring(947,
						954));

				if (erros.size() == 0) {
					AspectosLegais aspectos = (AspectosLegais) this
							.getModelManager().getEntity("AspectosLegais");

					//aspectos.verificarDuplicidade(apolice, cContas,
					// numeroOrdem);

					aspectos.atribuirOrigem(apolice.obterOrigem());
					aspectos.atribuirDestino(apolice.obterDestino());
					aspectos.atribuirResponsavel(apolice.obterResponsavel());
					aspectos.atribuirTitulo("Datos do Instrumento: "
							+ numeroInstrumento);
					aspectos.atribuirSuperior(apolice);

					//aspectos.incluir();

					aspectos.atribuirNumeroOrdem(numeroOrdem);
					aspectos.atribuirDataNotificacao(dataNotificacao);
					aspectos.atribuirAssunto(assuntoQuestionado);
					aspectos.atribuirDemandante(demandante);
					aspectos.atribuirDemandado(demandado);
					aspectos.atribuirJulgado(julgado);
					aspectos.atribuirTurno(turno);
					aspectos.atribuirJuiz(juiz);
					aspectos.atribuirSecretaria(numeroSecretaria);
					aspectos.atribuirAdvogado(advogado);
					aspectos.atribuirCircunscricao(circunscricao);
					aspectos.atribuirForum(forum);
					aspectos.atribuirDataDemanda(dataDemanda);
					aspectos.atribuirMontanteDemandado(montanteDemandando);
					aspectos.atribuirMontanteSentenca(montanteSentenca);
					aspectos.atribuirDataCancelamento(dataCancelamento);
					aspectos.atribuirTipo(caraterDemanda);
					aspectos
							.atribuirResponsabilidadeMaxima(responsabilidadeMaxima);
					aspectos.atribuirSinistroPendente(provisaoSinistro);
					aspectos.atribuirTipoInstrumento(tipoInstrumento);
					aspectos.atribuirNumeroEndoso(numeroEndoso);
					aspectos.atribuirCertificado(certificado);

					String objetoCausa2 = "";

					int cont = 1;

					for (int j = 0; j < objetoCausa.length();) {
						String caracter = objetoCausa.substring(j, cont);

						if (j == 100 || j == 200) {
							boolean entrou = false;

							while (!caracter.equals(" ")) {
								objetoCausa2 += caracter;
								j++;
								cont++;
								if (j == objetoCausa.length())
									break;
								else
									caracter = objetoCausa.substring(j, cont);

								entrou = true;
							}

							if (!entrou) {
								j++;
								cont++;
							} else
								objetoCausa2 += "\n";

						} else {
							objetoCausa2 += caracter;
							cont++;
							j++;
						}
					}

					//System.out.println("Objeto de la Causa: " +
					// objetoCausa2);

					aspectos.atribuirObjetoCausa(objetoCausa2);

					cont = 1;

					String observacoes2 = "";

					for (int j = 0; j < observacoes.length();) {
						String caracter = observacoes.substring(j, cont);

						if (j == 100 || j == 200) {
							boolean entrou = false;

							while (!caracter.equals(" ")) {
								observacoes2 += caracter;
								j++;
								cont++;
								if (j == observacoes.length())
									break;
								else
									caracter = observacoes.substring(j, cont);

								entrou = true;
							}

							if (!entrou) {
								j++;
								cont++;
							} else
								observacoes2 += "\n";

						} else {
							observacoes2 += caracter;
							cont++;
							j++;
						}
					}

					//System.out.println("Obs: " + observacoes2);
					aspectos.atribuirDescricao(observacoes2);

					aspectos2.add(aspectos);
				}
				/*
				 * else { if(apolice!=null) { apolice.excluir(); break; } }
				 */
			}
			// REGISTRO TIPO 11
			else if (Integer.parseInt(linha.substring(5, 7)) == 11) {
				String apelidoCconta = linha.substring(7, 17);

				ClassificacaoContas cContas = null;
				cContas = (ClassificacaoContas) entidadeHome
						.obterEntidadePorApelido(apelidoCconta.trim());

				if (cContas == null)
					erros.add("Error: 05 - Cuenta " + apelidoCconta.trim()
							+ " no fue encontrada - Linea: " + numeroLinha);

				String tipoInstrumento = "";

				if (linha.substring(17, 18).equals("1"))
					tipoInstrumento = "Póliza Individual";
				else if (linha.substring(17, 18).equals("2"))
					tipoInstrumento = "Póliza Madre";
				else if (linha.substring(17, 18).equals("3"))
					tipoInstrumento = "Certificado de Seguro Colectivo";
				else if (linha.substring(17, 18).equals("4"))
					tipoInstrumento = "Certificado Provisorio";
				else if (linha.substring(17, 18).equals("5"))
					tipoInstrumento = "Nota de Cobertura de Reaseguro";

				if (tipoInstrumento.equals(""))
					erros
							.add("Error: 101 - Instrumento es obligatorio - Linea: "
									+ numeroLinha);

				String numeroInstrumento = linha.substring(18, 28);

				Apolice apolice = null;

				if (this.apolices != null)
					apolice = (Apolice) this.apolices.get(numeroInstrumento);

				if (apolice == null)
					apolice = apoliceHome.obterApolice(numeroInstrumento);

				if (apolice == null)
					erros.add("Error: 73 - Instumento "
							+ numeroInstrumento.trim()
							+ " no fue encontrado - Linea: " + numeroLinha);

				double numeroEndoso2 = Double.parseDouble(linha.substring(28,
						38));

				double certificado2 = Double.parseDouble(linha
						.substring(38, 45));

				String qtdeStr = linha.substring(45, 47);

				if (qtdeStr.equals(""))
					erros
							.add("Error: 67 - Cantidad de Endosos es obligatoria - Linea: "
									+ numeroLinha);

				int qtde = Integer.parseInt(qtdeStr);

				int ultimo = 0;

				for (int w = 0; w < qtde; w++) {
					if (ultimo == 0)
						ultimo = 47;

					String numeroEndoso = linha.substring(ultimo, ultimo + 10);

					if (numeroEndoso.equals(""))
						erros
								.add("Error: 68 - Numero del Endoso es obligatorio - Linea: "
										+ numeroLinha);

					String anoEmissao = linha.substring(ultimo + 10,
							ultimo + 10 + 4);

					String mesEmissao = linha.substring(ultimo + 10 + 4,
							ultimo + 10 + 4 + 2);

					String diaEmissao = linha.substring(ultimo + 10 + 4 + 2,
							ultimo + 10 + 4 + 2 + 2);

					Date dataEmissao = null;

					if (diaEmissao.startsWith(" ")
							|| mesEmissao.startsWith(" ")
							|| anoEmissao.startsWith(" "))
						erros
								.add("Error: 92 - Fecha Emisión Invalida - Linea: "
										+ numeroLinha);
					else
						dataEmissao = new SimpleDateFormat("dd/MM/yyyy")
								.parse(diaEmissao + "/" + mesEmissao + "/"
										+ anoEmissao);

					String anoVigenciaInicial = linha.substring(ultimo + 10 + 4
							+ 2 + 2, ultimo + 10 + 4 + 2 + 2 + 4);

					String mesVigenciaInicial = linha.substring(ultimo + 10 + 4
							+ 2 + 2 + 4, ultimo + 10 + 4 + 2 + 2 + 4 + 2);

					String diaVigenciaInicial = linha.substring(ultimo + 10 + 4
							+ 2 + 2 + 4 + 2, ultimo + 10 + 4 + 2 + 2 + 4 + 2
							+ 2);

					Date dataVigenciaInicial = null;

					if (diaVigenciaInicial.startsWith(" ")
							|| mesVigenciaInicial.startsWith(" ")
							|| anoVigenciaInicial.startsWith(" "))
						erros
								.add("Error: 92 - Fecha Inicio Vigencia Invalida - Linea: "
										+ numeroLinha);
					else
						dataVigenciaInicial = new SimpleDateFormat("dd/MM/yyyy")
								.parse(diaVigenciaInicial + "/"
										+ mesVigenciaInicial + "/"
										+ anoVigenciaInicial);

					String anoVigenciaVencimento = linha.substring(ultimo + 10
							+ 4 + 2 + 2 + 4 + 2 + 2, ultimo + 10 + 4 + 2 + 2
							+ 4 + 2 + 2 + 4);

					String mesVigenciaVencimento = linha.substring(ultimo + 10
							+ 4 + 2 + 2 + 4 + 2 + 2 + 4, ultimo + 10 + 4 + 2
							+ 2 + 4 + 2 + 2 + 4 + 2);

					String diaVigenciaVencimento = linha.substring(ultimo + 10
							+ 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2, ultimo + 10 + 4
							+ 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2);

					Date dataVigenciaVencimento = null;

					if (diaVigenciaVencimento.startsWith(" ")
							|| mesVigenciaVencimento.startsWith(" ")
							|| anoVigenciaVencimento.startsWith(" "))
						erros
								.add("Error: 92 - Fecha Fin Vigencia Invalida - Linea: "
										+ numeroLinha);
					else
						dataVigenciaVencimento = new SimpleDateFormat(
								"dd/MM/yyyy").parse(diaVigenciaVencimento + "/"
								+ mesVigenciaVencimento + "/"
								+ anoVigenciaVencimento);

					String razaoEmissao = linha.substring(ultimo + 10 + 4 + 2
							+ 2 + 4 + 2 + 2 + 4 + 2 + 2, ultimo + 10 + 4 + 2
							+ 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120);

					if (razaoEmissao.equals(""))
						erros
								.add("Error: 69 - Razón o causa es obligatoria - Linea: "
										+ numeroLinha);

					String primaGsStr = linha.substring(ultimo + 10 + 4 + 2 + 2
							+ 4 + 2 + 2 + 4 + 2 + 2 + 120, ultimo + 10 + 4 + 2
							+ 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22);

					if (primaGsStr.equals(""))
						erros
								.add("Error: 70 - Prima en Gs del Endoso es obligatoria - Linea: "
										+ numeroLinha);

					double primaGs = Double.parseDouble(primaGsStr);

					String tipoMoedaPrimaGs = this.obterTipoMoeda(linha
							.substring(ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4
									+ 2 + 2 + 120 + 22, ultimo + 10 + 4 + 2 + 2
									+ 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22 + 2));

					String primaMeStr = linha.substring(ultimo + 10 + 4 + 2 + 2
							+ 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22 + 2, ultimo + 10
							+ 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22 + 2
							+ 22);

					double primaMe = Double.parseDouble(primaMeStr);

					ultimo = ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2
							+ 120 + 22 + 2 + 22;

					if (erros.size() == 0) {
						Suplemento suplemento = (Suplemento) this
								.getModelManager().getEntity("Suplemento");

						suplemento.atribuirOrigem(apolice.obterOrigem());
						suplemento.atribuirDestino(apolice.obterDestino());
						suplemento.atribuirResponsavel(apolice
								.obterResponsavel());
						suplemento.atribuirTitulo("Datos do Instrumento: "
								+ numeroInstrumento);
						suplemento.atribuirSuperior(apolice);

						suplemento.atribuirNumero(numeroEndoso);
						suplemento.atribuirDataEmissao(dataEmissao);
						suplemento
								.atribuirDataPrevistaInicio(dataVigenciaInicial);
						suplemento
								.atribuirDataPrevistaConclusao(dataVigenciaVencimento);
						suplemento.atribuirPrimaGs(primaGs);
						suplemento.atribuirTipoMoedaPrimaGs(tipoMoedaPrimaGs);
						suplemento.atribuirPrimaMe(primaMe);
						suplemento.atribuirTipoInstrumento(tipoInstrumento);
						suplemento.atribuirNumeroEndoso(numeroEndoso2);
						suplemento.atribuirCertificado(certificado2);

						String razaoEmissao2 = "";

						int cont = 1;

						for (int j = 0; j < razaoEmissao.length();) {
							String caracter = razaoEmissao.substring(j, cont);

							if (j == 100) {
								boolean entrou = false;

								while (!caracter.equals(" ")) {
									razaoEmissao2 += caracter;
									j++;
									cont++;
									if (j == razaoEmissao.length())
										break;
									else
										caracter = razaoEmissao.substring(j,
												cont);

									entrou = true;
								}

								if (!entrou) {
									j++;
									cont++;
								} else
									razaoEmissao2 += "\n";
							} else {
								razaoEmissao2 += caracter;
								cont++;
								j++;
							}
						}

						suplemento.atribuirRazao(razaoEmissao2);

						suplementos.add(suplemento);

					}
					/*
					 * else { if(apolice!=null) { apolice.excluir(); break; } }
					 */
				}
			}
			// REGISTRO TIPO 12
			else if (Integer.parseInt(linha.substring(5, 7)) == 12) {
				String apelidoCconta = linha.substring(7, 17);

				ClassificacaoContas cContas = null;
				cContas = (ClassificacaoContas) entidadeHome
						.obterEntidadePorApelido(apelidoCconta.trim());

				if (cContas == null)
					erros.add("Error: 05 - Cuenta " + apelidoCconta.trim()
							+ " no fue encontrada - Linea: " + numeroLinha);

				String numeroInstrumento = linha.substring(17, 27);

				Apolice apolice = null;

				if (this.apolices != null)
					apolice = (Apolice) this.apolices.get(numeroInstrumento);

				if (apolice == null)
					apolice = apoliceHome.obterApolice(numeroInstrumento);

				if (apolice == null)
					erros.add("Error: 73 - Instumento "
							+ numeroInstrumento.trim()
							+ " no fue encontrado - Linea: " + numeroLinha);

				String anoFinalizacao = linha.substring(27, 31);

				String mesFinalizacao = linha.substring(31, 33);

				String diaFinalizacao = linha.substring(33, 35);

				Date dataFinalizacao = null;

				if (diaFinalizacao.startsWith(" ")
						|| mesFinalizacao.startsWith(" ")
						|| anoFinalizacao.startsWith(" "))
					erros
							.add("Error: 92 - Fecha Finalización Invalida - Linea: "
									+ numeroLinha);
				else
					dataFinalizacao = new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaFinalizacao + "/" + mesFinalizacao + "/"
									+ anoFinalizacao);

				String tipoInstrumento = "";

				if (linha.substring(35, 36).equals("1"))
					tipoInstrumento = "Póliza Individual";
				else if (linha.substring(35, 36).equals("2"))
					tipoInstrumento = "Póliza Madre";
				else if (linha.substring(35, 36).equals("3"))
					tipoInstrumento = "Certificado de Seguro Colectivo";
				else if (linha.substring(35, 36).equals("4"))
					tipoInstrumento = "Certificado Provisorio";
				else if (linha.substring(35, 36).equals("5"))
					tipoInstrumento = "Nota de Cobertura de Reaseguro";

				if (tipoInstrumento.equals(""))
					erros
							.add("Error: 101 - Instrumento es obligatorio - Linea: "
									+ numeroLinha);

				double numeroEndoso = Double.parseDouble(linha
						.substring(36, 46));

				double certificado = Double
						.parseDouble(linha.substring(46, 53));

				if (erros.size() == 0) {
					//apolice.excluirFinalizacao();

					//apolice.adicionarFinalizacao(dataFinalizacao);

					apolice.atualizarDataEncerramento(dataFinalizacao);
					apolice.atualizarSituacaoSeguro("No Vigente");
				}
				/*
				 * else { apolice.excluir(); break; }
				 */

			}
			// REGISTRO TIPO 13
			else if (Integer.parseInt(linha.substring(5, 7)) == 13) {
				String apelidoCconta = linha.substring(7, 17);

				ClassificacaoContas cContas = null;
				cContas = (ClassificacaoContas) entidadeHome
						.obterEntidadePorApelido(apelidoCconta.trim());

				if (cContas == null)
					erros.add("Error: 05 - Cuenta " + apelidoCconta.trim()
							+ " no fue encontrada - Linea: " + numeroLinha);

				String numeroInstrumento = linha.substring(17, 27);

				Apolice apolice = null;

				if (this.apolices != null)
					apolice = (Apolice) this.apolices.get(numeroInstrumento);

				if (apolice == null)
					apolice = apoliceHome.obterApolice(numeroInstrumento);

				if (apolice == null)
					erros.add("Error: 73 - Instumento "
							+ numeroInstrumento.trim()
							+ " no fue encontrado - Linea: " + numeroLinha);

				String anoInicioVigencia = linha.substring(27, 31);

				String mesInicioVigencia = linha.substring(31, 33);

				String diaInicioVigencia = linha.substring(33, 35);

				Date dataInicioVigencia = null;

				if (diaInicioVigencia.startsWith(" ")
						|| mesInicioVigencia.startsWith(" ")
						|| anoInicioVigencia.startsWith(" "))
					erros
							.add("Error: 92 - Fecha Inicio Vigencia Invalida - Linea: "
									+ numeroLinha);
				else
					dataInicioVigencia = new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaInicioVigencia + "/" + mesInicioVigencia
									+ "/" + anoInicioVigencia);

				String anoFimVigencia = linha.substring(35, 39);

				String mesFimVigencia = linha.substring(39, 41);

				String diaFimVigencia = linha.substring(41, 43);

				Date dataFimVigencia = null;

				if (diaFimVigencia.startsWith(" ")
						|| mesFimVigencia.startsWith(" ")
						|| anoFimVigencia.startsWith(" "))
					erros.add("Error: 92 - Fecha Fin Invalida - Linea: "
							+ numeroLinha);
				else
					dataFimVigencia = new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaFimVigencia + "/" + mesFimVigencia + "/"
									+ anoFimVigencia);

				String financiamentoGsStr = linha.substring(43, 65);

				if (financiamentoGsStr.equals(""))
					erros
							.add("Error: 71 - Interés por Financiamento en Gs es obligatorio - Linea: "
									+ numeroLinha);

				double financiamentoGs = Double.parseDouble(financiamentoGsStr);

				String tipoMoedaFinanciamentoGs = this.obterTipoMoeda(linha
						.substring(65, 67));

				String financiamentoMeStr = linha.substring(67, 89);

				double financiamentoMe = Double.parseDouble(financiamentoMeStr);

				String qtde = linha.substring(89, 92);

				if (qtde.equals(""))
					erros
							.add("Error: 72 - Cantidad de Cuotas de la Refinanciación es obligatoria - Linea: "
									+ numeroLinha);

				String tipoInstrumento = "";

				if (linha.substring(92, 93).equals("1"))
					tipoInstrumento = "Póliza Individual";
				else if (linha.substring(92, 93).equals("2"))
					tipoInstrumento = "Póliza Madre";
				else if (linha.substring(92, 93).equals("3"))
					tipoInstrumento = "Certificado de Seguro Colectivo";
				else if (linha.substring(92, 93).equals("4"))
					tipoInstrumento = "Certificado Provisorio";
				else if (linha.substring(92, 93).equals("5"))
					tipoInstrumento = "Nota de Cobertura de Reaseguro";

				if (tipoInstrumento.equals(""))
					erros
							.add("Error: 101 - Instrumento es obligatorio - Linea: "
									+ numeroLinha);

				double numeroEndoso = Double.parseDouble(linha.substring(93,
						103));

				double certificado = Double.parseDouble(linha.substring(103,
						110));

				if (erros.size() == 0) {
					Refinacao refinacao = (Refinacao) this.getModelManager()
							.getEntity("Refinacao");

					refinacao.atribuirOrigem(apolice.obterOrigem());
					refinacao.atribuirDestino(apolice.obterDestino());
					refinacao.atribuirResponsavel(apolice.obterResponsavel());
					refinacao.atribuirTitulo("Datos do Instrumento: "
							+ numeroInstrumento);
					refinacao.atribuirSuperior(apolice);

					refinacao.atribuirDataPrevistaInicio(dataInicioVigencia);
					refinacao.atribuirDataPrevistaConclusao(dataFimVigencia);
					refinacao.atribuirFinanciamentoGs(financiamentoGs);
					refinacao
							.atribuirTipoMoedaFinanciamentoGs(tipoMoedaFinanciamentoGs);
					refinacao.atribuirFinanciamentoMe(financiamentoMe);
					refinacao.atribuirQtdeParcelas(Integer.parseInt(qtde));
					refinacao.atribuirTipoInstrumento(tipoInstrumento);
					refinacao.atribuirNumeroEndoso(numeroEndoso);
					refinacao.atribuirCertificado(certificado);

					refinacoes.add(refinacao);
				}
				/*
				 * else { if(apolice!=null) { apolice.excluir(); break; } }
				 */
			}
			// REGISTRO TIPO 14
			else if (Integer.parseInt(linha.substring(5, 7)) == 14) {
				String apelidoCconta = linha.substring(7, 17);

				ClassificacaoContas cContas = null;
				cContas = (ClassificacaoContas) entidadeHome
						.obterEntidadePorApelido(apelidoCconta.trim());

				if (cContas == null)
					erros.add("Error: 05 - Cuenta " + apelidoCconta.trim()
							+ " no fue encontrada - Linea: " + numeroLinha);

				String tipoInstrumento = "";

				if (linha.substring(17, 18).equals("1"))
					tipoInstrumento = "Póliza Individual";
				else if (linha.substring(17, 18).equals("2"))
					tipoInstrumento = "Póliza Madre";
				else if (linha.substring(17, 18).equals("3"))
					tipoInstrumento = "Certificado de Seguro Colectivo";
				else if (linha.substring(17, 18).equals("4"))
					tipoInstrumento = "Certificado Provisorio";
				else if (linha.substring(17, 18).equals("5"))
					tipoInstrumento = "Nota de Cobertura de Reaseguro";

				if (tipoInstrumento.equals(""))
					erros
							.add("Error: 101 - Instrumento es obligatorio - Linea: "
									+ numeroLinha);

				String numeroInstrumento = linha.substring(18, 28);

				Apolice apolice = null;

				if (this.apolices != null)
					apolice = (Apolice) this.apolices.get(numeroInstrumento);

				if (apolice == null)
					apolice = apoliceHome.obterApolice(numeroInstrumento);

				if (apolice == null)
					erros.add("Error: 73 - Instumento "
							+ numeroInstrumento.trim()
							+ " no fue encontrado - Linea: " + numeroLinha);

				double numeroEndoso = Double.parseDouble(linha
						.substring(28, 38));

				double certificado = Double
						.parseDouble(linha.substring(38, 45));

				String qtdeStr = linha.substring(45, 47);

				int qtde = Integer.parseInt(qtdeStr);

				int ultimo = 0;

				for (int w = 0; w < qtde; w++) {
					if (ultimo == 0)
						ultimo = 47;

					String numeroSinistro = linha.substring(ultimo, ultimo + 6);

					Sinistro sinistro = null;

					if (this.sinistros != null)
						sinistro = (Sinistro) this.sinistros
								.get(numeroSinistro);

					if (sinistro == null)
						erros.add("Error: 74 - Siniestro "
								+ numeroSinistro.trim()
								+ " no fue encontrado - Linea: " + numeroLinha);

					if (numeroSinistro.equals(""))
						erros
								.add("Error: 37 - Numero del Siniestro es obligatorio - Linea: "
										+ numeroLinha);

					String anoSinistro = linha.substring(ultimo + 6,
							ultimo + 6 + 4);

					String mesSinistro = linha.substring(ultimo + 6 + 4,
							ultimo + 6 + 4 + 2);

					String diaSinistro = linha.substring(ultimo + 6 + 4 + 2,
							ultimo + 6 + 4 + 2 + 2);

					Date dataSinistro = null;

					if (diaSinistro.startsWith(" ")
							|| mesSinistro.startsWith(" ")
							|| anoSinistro.startsWith(" "))
						erros
								.add("Error: 92 - Fecha Sinistro Invalida - Linea: "
										+ numeroLinha);
					else
						dataSinistro = new SimpleDateFormat("dd/MM/yyyy")
								.parse(diaSinistro + "/" + mesSinistro + "/"
										+ anoSinistro);

					String tipoPagamento = "";

					if (linha.substring(ultimo + 6 + 4 + 2 + 2,
							ultimo + 6 + 4 + 2 + 2 + 1).equals("1"))
						tipoPagamento = "Liquidador";
					else if (linha.substring(ultimo + 6 + 4 + 2 + 2,
							ultimo + 6 + 4 + 2 + 2 + 1).equals("2"))
						tipoPagamento = "Asegurado";
					else if (linha.substring(ultimo + 6 + 4 + 2 + 2,
							ultimo + 6 + 4 + 2 + 2 + 1).equals("3"))
						tipoPagamento = "Tercero";
					else if (linha.substring(ultimo + 6 + 4 + 2 + 2,
							ultimo + 6 + 4 + 2 + 2 + 1).equals("4"))
						tipoPagamento = "Otros";

					String numeroLiquidador = linha.substring(ultimo + 6 + 4
							+ 2 + 2 + 1, ultimo + 6 + 4 + 2 + 2 + 1 + 3);

					AuxiliarSeguro auxiliar = null;

					auxiliar = (AuxiliarSeguro) entidadeHome
							.obterEntidadePorInscricao(numeroLiquidador);

					if (auxiliar == null)
						erros
								.add("Error: 84 - Auxiliar de Seguro con Inscripción "
										+ numeroLiquidador
										+ " no fue encontrado - Linea: "
										+ numeroLinha);

					String nomeTerceiro = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 1 + 3, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60);

					String abonadoGsStr = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 1 + 3 + 60, ultimo + 6 + 4 + 2 + 2 + 1 + 3
							+ 60 + 22);

					double abonadoGs = Double.parseDouble(abonadoGsStr);

					String tipoMoedaAbonoGs = this.obterTipoMoeda(linha
							.substring(
									ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22,
									ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22
											+ 2));

					String abonadoMeStr = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 1 + 3 + 60 + 22 + 2, ultimo + 6 + 4 + 2 + 2
							+ 1 + 3 + 60 + 22 + 2 + 22);

					double abonadoMe = Double.parseDouble(abonadoMeStr);

					String anoPagamento = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 1 + 3 + 60 + 22 + 2 + 22, ultimo + 6 + 4 + 2
							+ 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4);

					String mesPagamento = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4, ultimo + 6 + 4
							+ 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2);

					String diaPagamento = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2, ultimo + 6
							+ 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2);

					Date dataPagamento = null;

					if (diaPagamento.startsWith(" ")
							|| mesPagamento.startsWith(" ")
							|| anoPagamento.startsWith(" "))
						erros
								.add("Error: 92 - Fecha Pagamento Invalida - Linea: "
										+ numeroLinha);
					else
						dataPagamento = new SimpleDateFormat("dd/MM/yyyy")
								.parse(diaPagamento + "/" + mesPagamento + "/"
										+ anoPagamento);

					String numeroCheque = linha.substring(ultimo + 6 + 4 + 2
							+ 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2, ultimo
							+ 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2
							+ 2 + 10);

					String bancoStr = linha.substring(ultimo + 6 + 4 + 2 + 2
							+ 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10, ultimo
							+ 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2
							+ 2 + 10 + 10);

					Conta banco = null;

					banco = (Conta) entidadeHome
							.obterEntidadePorApelido(bancoStr);

					if (banco == null)
						erros.add("Error: 88 - Banco " + bancoStr
								+ " no fue encontrado - Linea: " + numeroLinha);

					String situacaoSinistro = "";

					if (linha.substring(
							ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22
									+ 4 + 2 + 2 + 10 + 10,
							ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22
									+ 4 + 2 + 2 + 10 + 10 + 1).equals("1"))
						situacaoSinistro = "Pendiente de Liquidación";
					else if (linha.substring(
							ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22
									+ 4 + 2 + 2 + 10 + 10,
							ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22
									+ 4 + 2 + 2 + 10 + 10 + 1).equals("2"))
						situacaoSinistro = "Controvertido";
					else if (linha.substring(
							ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22
									+ 4 + 2 + 2 + 10 + 10,
							ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22
									+ 4 + 2 + 2 + 10 + 10 + 1).equals("3"))
						situacaoSinistro = "Pendiente de Pago";
					else if (linha.substring(
							ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22
									+ 4 + 2 + 2 + 10 + 10,
							ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22
									+ 4 + 2 + 2 + 10 + 10 + 1).equals("4"))
						situacaoSinistro = "Rechazado";
					else if (linha.substring(
							ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22
									+ 4 + 2 + 2 + 10 + 10,
							ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22
									+ 4 + 2 + 2 + 10 + 10 + 1).equals("5"))
						situacaoSinistro = "Judicializado";

					String situacaoPagamento = "";

					if (linha.substring(
							ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22
									+ 4 + 2 + 2 + 10 + 10 + 1,
							ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22
									+ 4 + 2 + 2 + 10 + 10 + 1 + 1).equals("1"))
						situacaoPagamento = "Normal";
					else if (linha.substring(
							ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22
									+ 4 + 2 + 2 + 10 + 10 + 1,
							ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22
									+ 4 + 2 + 2 + 10 + 10 + 1 + 1).equals("2"))
						situacaoPagamento = "Anulado";

					ultimo = ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22
							+ 4 + 2 + 2 + 10 + 10 + 1 + 1;

					if (erros.size() == 0) {
						RegistroGastos gastos = (RegistroGastos) this
								.getModelManager().getEntity("RegistroGastos");

						gastos.atribuirOrigem(sinistro.obterOrigem());
						gastos.atribuirDestino(sinistro.obterDestino());
						gastos.atribuirResponsavel(sinistro.obterResponsavel());
						gastos.atribuirTitulo("Datos do Instrumento: "
								+ numeroInstrumento);
						gastos.atribuirSuperior(sinistro);

						gastos.atribuirDataSinistro(dataSinistro);
						gastos.atribuirTipo(tipoPagamento);
						gastos.atribuirAuxiliarSeguro(auxiliar);
						gastos.atribuirNomeTerceiro(nomeTerceiro);
						gastos.atribuirAbonoGs(abonadoGs);
						gastos.atribuirTipoMoedaAbonoGs(tipoMoedaAbonoGs);
						gastos.atribuirAbonoMe(abonadoMe);
						gastos.atribuirDataPagamento(dataPagamento);
						gastos.atribuirNumeroCheque(numeroCheque);
						gastos.atribuirBanco(banco);
						gastos.atribuirSituacaoSinistro(situacaoSinistro);
						gastos.atribuirSituacaoPagamento(situacaoPagamento);
						gastos.atribuirTipoInstrumento(tipoInstrumento);
						gastos.atribuirNumeroEndoso(numeroEndoso);
						gastos.atribuirCertificado(certificado);

						gastos2.add(gastos);
					}
					/*
					 * else { if(apolice!=null) { apolice.excluir(); break; } }
					 */
				}
			}
			// REGISTRO TIPO 15
			else if (Integer.parseInt(linha.substring(5, 7)) == 15) {
				String apelidoCconta = linha.substring(7, 17);

				ClassificacaoContas cContas = null;
				cContas = (ClassificacaoContas) entidadeHome
						.obterEntidadePorApelido(apelidoCconta.trim());

				if (cContas == null)
					erros.add("Error: 05 - Cuenta " + apelidoCconta.trim()
							+ " no fue encontrada - Linea: " + numeroLinha);

				String numeroInstrumento = linha.substring(17, 27);

				Apolice apolice = null;

				if (this.apolices != null)
					apolice = (Apolice) this.apolices.get(numeroInstrumento);

				if (apolice == null)
					apolice = apoliceHome.obterApolice(numeroInstrumento);

				if (apolice == null)
					erros.add("Error: 73 - Instumento "
							+ numeroInstrumento.trim()
							+ " no fue encontrado - Linea: " + numeroLinha);

				String inscricaoReaseguradora = linha.substring(27, 30);

				Reaseguradora reaseguradora = null;

				reaseguradora = (Reaseguradora) entidadeHome
						.obterEntidadePorInscricao(inscricaoReaseguradora);

				if (reaseguradora == null)
					erros.add("Error: 10 - Inscripción de la Reaseguradora "
							+ inscricaoReaseguradora
							+ " no fue encontrada o esta No Activa - Linea: "
							+ numeroLinha);

				String tipoContrato = "";

				if (linha.substring(30, 31).equals("1"))
					tipoContrato = "Cuota parte";
				else if (linha.substring(30, 31).equals("2"))
					tipoContrato = "Excedente";
				else if (linha.substring(30, 31).equals("3"))
					tipoContrato = "Exceso de pérdida";
				else if (linha.substring(30, 31).equals("4"))
					tipoContrato = "Facultativo no Proporcional";
				else if (linha.substring(30, 31).equals("5"))
					tipoContrato = "Facultativo Proporcional";
				else if (linha.substring(30, 31).equals("6"))
					tipoContrato = "Limitación de Siniestralidad";

				String anoAnulacao = linha.substring(31, 35);

				String mesAnulacao = linha.substring(35, 37);

				String diaAnulacao = linha.substring(37, 39);

				Date dataAnulacao = null;

				if (diaAnulacao.startsWith(" ") || mesAnulacao.startsWith(" ")
						|| anoAnulacao.startsWith(" "))
					erros.add("Error: 92 - Fecha Anulación Invalida - Linea: "
							+ numeroLinha);
				else
					dataAnulacao = new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaAnulacao + "/" + mesAnulacao + "/"
									+ anoAnulacao);

				String tipoAnulacao = "";

				if (linha.substring(39, 40).equals("1"))
					tipoAnulacao = "Total";
				else if (linha.substring(39, 40).equals("2"))
					tipoAnulacao = "Parcial";

				String capitalAnuladoGsStr = linha.substring(40, 62);

				double capitalAnuladoGs = Double
						.parseDouble(capitalAnuladoGsStr);

				String tipoMoedaCapitalAnuladoGs = this.obterTipoMoeda(linha
						.substring(62, 64));

				String capitalAnuladoMeStr = linha.substring(64, 86);

				double capitalAnuladoMe = Double
						.parseDouble(capitalAnuladoMeStr);

				String diaCorridos = linha.substring(86, 91);

				String primaAnuladaGsStr = linha.substring(91, 113);

				double primaAnuladaGs = Double.parseDouble(primaAnuladaGsStr);

				String tipoMoedaPrimaAnuladaGs = this.obterTipoMoeda(linha
						.substring(113, 115));

				String primaAnuladaMeStr = linha.substring(115, 137);

				double primaAnuladaMe = Double.parseDouble(primaAnuladaMeStr);

				String comissaoAnuladaGsStr = linha.substring(137, 159);

				double comissaoAnuladaGs = Double
						.parseDouble(comissaoAnuladaGsStr);

				String tipoMoedaComissaoAnuladaGs = this.obterTipoMoeda(linha
						.substring(159, 161));

				String comissaoAnuladaMeStr = linha.substring(161, 183);

				double comissaoAnuladaMe = Double
						.parseDouble(comissaoAnuladaMeStr);

				String motivoAnulacao = linha.substring(183, 303);

				DadosReaseguro dadosReaseguro = null;

				if (this.dadosReaseguros != null) {
					if (reaseguradora != null)
						dadosReaseguro = (DadosReaseguro) this.dadosReaseguros
								.get(new Long(cContas.obterId()
										+ apolice.obterNumeroApolice()
										+ reaseguradora.obterId())
										+ tipoContrato);
					else
						dadosReaseguro = (DadosReaseguro) this.dadosReaseguros
								.get(new Long(cContas.obterId()
										+ apolice.obterNumeroApolice())
										+ tipoContrato);
				}

				if (dadosReaseguro == null)
					dadosReaseguro = dadosReaseguroHome.obterDadosReaseguro(
							cContas, apolice, reaseguradora, tipoContrato);

				if (dadosReaseguro == null)
					erros.add("Error: 83 - Reaseguradora "
							+ inscricaoReaseguradora
							+ " no fue encontrada o esta No Activa - Linea: "
							+ numeroLinha);

				if (erros.size() == 0) {
					RegistroAnulacao anulacao = (RegistroAnulacao) this
							.getModelManager().getEntity("RegistroAnulacao");

					anulacao.verificarDuplicidade(apolice, cContas,
							reaseguradora, tipoContrato);

					anulacao.atribuirOrigem(dadosReaseguro.obterOrigem());
					anulacao.atribuirDestino(dadosReaseguro.obterDestino());
					anulacao.atribuirResponsavel(dadosReaseguro
							.obterResponsavel());
					anulacao.atribuirTitulo("Datos do Instrumento: "
							+ numeroInstrumento);
					anulacao.atribuirSuperior(dadosReaseguro);

					dadosReaseguro.atribuirSituacao("No Vigente");

					//anulacao.incluir();

					anulacao.atribuirReaeguradora(reaseguradora);
					anulacao.atribuirTipoContrato(tipoContrato);
					anulacao.atribuirDataAnulacao(dataAnulacao);
					anulacao.atribuirTipo(tipoAnulacao);
					anulacao.atribuirCapitalGs(capitalAnuladoGs);
					anulacao
							.atribuirTipoMoedaCapitalGs(tipoMoedaCapitalAnuladoGs);
					anulacao.atribuirCapitalMe(capitalAnuladoMe);
					anulacao
							.atribuirDiasCorridos(Integer.parseInt(diaCorridos));
					anulacao.atribuirPrimaGs(primaAnuladaGs);
					anulacao.atribuirTipoMoedaPrimaGs(tipoMoedaPrimaAnuladaGs);
					anulacao.atribuirPrimaMe(primaAnuladaMe);
					anulacao.atribuirComissaoGs(comissaoAnuladaGs);
					anulacao
							.atribuirTipoMoedaComissaoGs(tipoMoedaComissaoAnuladaGs);

					int cont = 1;

					String motivoAnulacao2 = "";

					for (int j = 0; j < motivoAnulacao.length();) {
						String caracter = motivoAnulacao.substring(j, cont);

						if (j == 100 || j == 200) {
							boolean entrou = false;

							while (!caracter.equals(" ")) {
								motivoAnulacao2 += caracter;
								j++;
								cont++;
								if (j == motivoAnulacao.length())
									break;
								else
									caracter = motivoAnulacao
											.substring(j, cont);

								entrou = true;
							}

							if (!entrou) {
								j++;
								cont++;
							} else
								motivoAnulacao2 += "\n";

						} else {
							motivoAnulacao2 += caracter;
							cont++;
							j++;
						}
					}

					anulacao.atribuirDescricao(motivoAnulacao2);

					anulacoes2.add(anulacao);
				}
				/*
				 * else { if(apolice!=null) { apolice.excluir(); break; } }
				 */
			}
			// REGISTRO TIPO 16
			else if (Integer.parseInt(linha.substring(5, 7)) == 16) {
				String apelidoCconta = linha.substring(7, 17);

				ClassificacaoContas cContas = null;
				cContas = (ClassificacaoContas) entidadeHome
						.obterEntidadePorApelido(apelidoCconta.trim());

				if (cContas == null)
					erros.add("Error: 05 - Cuenta " + apelidoCconta.trim()
							+ " no fue encontrada - Linea: " + numeroLinha);

				String numeroInstrumento = linha.substring(17, 27);

				Apolice apolice = null;

				if (this.apolices != null)
					apolice = (Apolice) this.apolices.get(numeroInstrumento);

				if (apolice == null)
					apolice = apoliceHome.obterApolice(numeroInstrumento);

				if (apolice == null)
					erros.add("Error: 73 - Instumento "
							+ numeroInstrumento.trim()
							+ " no fue encontrado - Linea: " + numeroLinha);

				String anoCorte = linha.substring(27, 31);

				String mesCorte = linha.substring(31, 33);

				String diaCorte = linha.substring(33, 35);

				Date dataCorte = null;

				if (diaCorte.startsWith(" ") || mesCorte.startsWith(" ")
						|| anoCorte.startsWith(" "))
					erros.add("Error: 92 - Fecha Corte Invalida - Linea: "
							+ numeroLinha);
				else
					dataCorte = new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaCorte + "/" + mesCorte + "/" + anoCorte);

				String numeroParcela = linha.substring(35, 37);

				String anoVencimento = linha.substring(37, 41);

				String mesVencimento = linha.substring(41, 43);

				String diaVencimento = linha.substring(43, 45);

				Date dataVencimento = null;

				if (diaVencimento.startsWith(" ")
						|| mesVencimento.startsWith(" ")
						|| anoVencimento.startsWith(" "))
					erros
							.add("Error: 92 - Fecha Vencimiento Invalida - Linea: "
									+ numeroLinha);
				else
					dataVencimento = new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaVencimento + "/" + mesVencimento + "/"
									+ anoVencimento);

				String diaAtraso = linha.substring(45, 48);

				String valorGsStr = linha.substring(48, 70);

				double valorGs = Double.parseDouble(valorGsStr);

				String tipoMoeda = this.obterTipoMoeda(linha.substring(70, 72));

				String valorMeStr = linha.substring(72, 94);

				double valorMe = Double.parseDouble(valorMeStr);

				if (erros.size() == 0) {
					Morosidade morosidade = (Morosidade) this.getModelManager()
							.getEntity("Morosidade");

					morosidade.atribuirOrigem(apolice.obterOrigem());
					morosidade.atribuirDestino(apolice.obterDestino());
					morosidade.atribuirResponsavel(apolice.obterResponsavel());
					morosidade.atribuirTitulo("Datos do Instrumento: "
							+ numeroInstrumento);
					morosidade.atribuirSuperior(apolice);

					//morosidade.incluir();

					morosidade.atribuirDataCorte(dataCorte);
					morosidade.atribuirNumeroParcela(Integer
							.parseInt(numeroParcela));
					morosidade.atribuirDataVencimento(dataVencimento);
					morosidade.atribuirDiasAtraso(Integer.parseInt(diaAtraso));
					morosidade.atribuirValorGs(valorGs);
					morosidade.atribuirTipoMoedaValorGs(tipoMoeda);
					morosidade.atribuirValorMe(valorMe);

					morosidades.add(morosidade);
				}

				/*
				 * else { if(apolice!=null) { apolice.excluir(); break; } }
				 */
			}
			//REGISTRO TIPO 18
			else if (Integer.parseInt(linha.substring(5, 7)) == 18) {
				String ativoCorrenteStr = linha.substring(7, 29);

				double ativoCorrente = Double.parseDouble(ativoCorrenteStr);

				String passivoCorrenteStr = linha.substring(29, 51);

				double passivoCorrente = Double.parseDouble(passivoCorrenteStr);

				String inversaoStr = linha.substring(51, 73);

				double inversao = Double.parseDouble(inversaoStr);

				String deudasStr = linha.substring(73, 95);

				double deudas = Double.parseDouble(deudasStr);

				String usoStr = linha.substring(95, 117);

				double uso = Double.parseDouble(usoStr);

				String vendaStr = linha.substring(117, 139);

				double venda = Double.parseDouble(vendaStr);

				String leasingStr = linha.substring(139, 161);

				double leasing = Double.parseDouble(leasingStr);

				String resultadoStr = linha.substring(161, 183);

				double resultado = Double.parseDouble(resultadoStr);

				EntidadeHome home = (EntidadeHome) this.getModelManager()
						.getHome("EntidadeHome");

				RatioPermanente ratio = (RatioPermanente) this
						.getModelManager().getEntity("RatioPermanente");

				Entidade bcp = home.obterEntidadePorApelido("bcp");

				ratio.atribuirOrigem(aseguradora);
				ratio.atribuirDestino(bcp);
				ratio.atribuirResponsavel(this.obterUsuarioAtual());
				ratio.atribuirTitulo("Ratio Financeiro - Permanente");
				ratio.atribuirDataPrevistaInicio(dataGeracao);
				//ratio.incluir();

				ratio.atribuirAtivoCorrente(ativoCorrente);
				ratio.atribuirPassivoCorrente(passivoCorrente);
				ratio.atribuirInversao(inversao);
				ratio.atribuirDeudas(deudas);
				ratio.atribuirUso(uso);
				ratio.atribuirVenda(venda);
				ratio.atribuirLeasing(leasing);
				ratio.atribuirResultados(resultado);

				ratiosPermanentes.add(ratio);

				/*
				 * for(Iterator k =
				 * aseguradora.obterEventosComoOrigem().iterator() ; k.hasNext() ; ) {
				 * Evento e = (Evento) k.next();
				 * 
				 * if(e instanceof RatioPermanente)
				 * if(e.obterId()!=ratio.obterId())
				 * if(e.obterFase().obterCodigo().equals(Evento.EVENTO_PENDENTE))
				 * e.atualizarFase(Evento.EVENTO_CONCLUIDO); }
				 */
			}
			//	    	REGISTRO TIPO 19
			else if (Integer.parseInt(linha.substring(5, 7)) == 19) {
				String primasDiretasStr = linha.substring(7, 29);

				double primasDiretas = Double.parseDouble(primasDiretasStr);

				String primasAceitasStr = linha.substring(29, 51);

				double primasAceitas = Double.parseDouble(primasAceitasStr);

				String primasCedidasStr = linha.substring(51, 73);

				double primasCedidas = Double.parseDouble(primasCedidasStr);

				String anulacaoPrimasDiretasStr = linha.substring(73, 95);

				double anulacaoPrimasDiretas = Double
						.parseDouble(anulacaoPrimasDiretasStr);

				String anulacaoPrimasAtivasStr = linha.substring(95, 117);

				double anulacaoPrimasAtivas = Double
						.parseDouble(anulacaoPrimasAtivasStr);

				String anulacaoPrimasCedidasStr = linha.substring(117, 139);

				double anulacaoPrimasCedidas = Double
						.parseDouble(anulacaoPrimasCedidasStr);

				EntidadeHome home = (EntidadeHome) this.getModelManager()
						.getHome("EntidadeHome");

				RatioUmAno ratio = (RatioUmAno) this.getModelManager()
						.getEntity("RatioUmAno");

				Entidade bcp = home.obterEntidadePorApelido("bcp");

				ratio.atribuirOrigem(aseguradora);
				ratio.atribuirDestino(bcp);
				ratio.atribuirResponsavel(this.obterUsuarioAtual());
				ratio.atribuirTitulo("Ratio Financeiro - Un Año");
				ratio.atribuirDataPrevistaInicio(dataGeracao);
				//ratio.incluir();

				ratio.atribuirPrimasDiretas(primasDiretas);
				ratio.atribuirPrimasAceitas(primasAceitas);
				ratio.atribuirPrimasCedidas(primasCedidas);
				ratio.atribuirAnulacaoPrimasDiretas(anulacaoPrimasDiretas);
				ratio.atribuirAnulacaoPrimasAtivas(anulacaoPrimasAtivas);
				ratio.atribuirAnulacaoPrimasCedidas(anulacaoPrimasCedidas);

				ratiosUmAno.add(ratio);

				/*
				 * for(Iterator k =
				 * aseguradora.obterEventosComoOrigem().iterator() ; k.hasNext() ; ) {
				 * Evento e = (Evento) k.next();
				 * 
				 * if(e instanceof RatioUmAno) if(e.obterId()!=ratio.obterId())
				 * if(e.obterFase().obterCodigo().equals(Evento.EVENTO_PENDENTE))
				 * e.atualizarFase(Evento.EVENTO_CONCLUIDO); }
				 */

			}
			//	    	REGISTRO TIPO 20
			else if (Integer.parseInt(linha.substring(5, 7)) == 20) {
				String sinistrosPagosStr = linha.substring(7, 29);

				double sinistrosPagos = Double.parseDouble(sinistrosPagosStr);

				String gastosSinistroStr = linha.substring(29, 51);

				double gastosSinistro = Double.parseDouble(gastosSinistroStr);

				String sinistrosRecuperadosStr = linha.substring(51, 73);

				double sinistrosRecuperados = Double
						.parseDouble(sinistrosRecuperadosStr);

				String gastosRecupeadosStr = linha.substring(73, 95);

				double gastosRecupeados = Double
						.parseDouble(gastosRecupeadosStr);

				String recuperoSinistroStr = linha.substring(95, 117);

				double recuperoSinistro = Double
						.parseDouble(recuperoSinistroStr);

				String provisoesStr = linha.substring(117, 139);

				double provisoes = Double.parseDouble(provisoesStr);

				EntidadeHome home = (EntidadeHome) this.getModelManager()
						.getHome("EntidadeHome");

				RatioTresAnos ratio = (RatioTresAnos) this.getModelManager()
						.getEntity("RatioTresAnos");

				Entidade bcp = home.obterEntidadePorApelido("bcp");

				ratio.atribuirOrigem(aseguradora);
				ratio.atribuirDestino(bcp);
				ratio.atribuirResponsavel(this.obterUsuarioAtual());
				ratio.atribuirTitulo("Ratio Financeiro - Tres Año");
				ratio.atribuirDataPrevistaInicio(dataGeracao);
				//ratio.incluir();

				ratio.atribuirSinistrosPagos(sinistrosPagos);
				ratio.atribuirGastosSinistros(gastosSinistro);
				ratio.atribuirSinistrosRecuperados(sinistrosRecuperados);
				ratio.atribuirGastosRecuperados(gastosRecupeados);
				ratio.atribuirRecuperoSinistros(recuperoSinistro);
				ratio.atribuirProvisoes(provisoes);

				ratiosTresAnos.add(ratio);

			}
			// REGISTRO TIPO 99
			else if (Integer.parseInt(linha.substring(5, 7)) == 99) {
				if (numeroLinha != numeroTotalRegistros)
					erros.add("Error: 86 - Numero total de registros ("
							+ numeroLinha + ") no es el mismo del archivo ("
							+ numeroTotalRegistros + ") - Linea: "
							+ numeroLinha);
			}

			numeroLinha++;
		}

		return erros;
	}

	private void gravarEventosDaApolice() throws Exception {

		//System.out.println("Apolices: " + this.apolices.size());

		System.out.println("Gravando Eventos......: ");

		for (Iterator i = this.apolices.values().iterator(); i.hasNext();) {
			Apolice apolice = (Apolice) i.next();

			apolice.incluir();
		}

		System.out.println("Gravou Apolice......: ");

		for (Iterator i = this.dadosPrevisoes.iterator(); i.hasNext();) {
			DadosPrevisao dados = (DadosPrevisao) i.next();

			Apolice apolice = (Apolice) dados.obterSuperior();

			dados.verificarDuplicidade(apolice, apolice.obterSecao(), dados
					.obterDataCorte(), dados.obterNumeroEndoso());

			dados.incluir();
		}

		System.out.println("Gravou Dados Previsão......: ");

		for (Iterator i = this.dadosReaseguros.values().iterator(); i.hasNext();) {
			DadosReaseguro dados = (DadosReaseguro) i.next();

			Apolice apolice = (Apolice) dados.obterSuperior();

			if (dados.obterReaseguradora() != null)
				dados.verificarDuplicidade(apolice, apolice.obterSecao(), dados
						.obterReaseguradora(), dados.obterTipoContrato(), dados
						.obterValorEndoso());

			dados.incluir();
		}

		System.out.println("Gravou Dados Reaseguro......: ");

		for (Iterator i = this.dadosCoaseguros.iterator(); i.hasNext();) {
			DadosCoaseguro dados = (DadosCoaseguro) i.next();

			Apolice apolice = (Apolice) dados.obterSuperior();

			dados.verificarDuplicidade(apolice, apolice.obterSecao(), dados
					.obterAseguradora(), dados.obterNumeroEndoso());

			dados.incluir();
		}

		System.out.println("Gravou Dados Coaseguro......: ");

		for (Iterator i = this.sinistros.values().iterator(); i.hasNext();) {
			Sinistro sinistro = (Sinistro) i.next();

			Apolice apolice = (Apolice) sinistro.obterSuperior();

			sinistro.verificarDuplicidade(apolice, apolice.obterSecao(),
					sinistro.obterNumero(), sinistro.obterNumeroEndoso());

			sinistro.incluir();
		}

		System.out.println("Gravou Sinistro......: ");

		for (Iterator i = this.faturas.iterator(); i.hasNext();) {
			FaturaSinistro fatura = (FaturaSinistro) i.next();

			Sinistro sinistro = (Sinistro) fatura.obterSuperior();

			//fatura.verificarDuplicidade(sinistro, fatura.obterTipo(), fatura
					//.obterNumeroDocumento(), fatura.obterRucProvedor(), fatura
					//.obterNumeroEndoso(), fatura.obterDataPagamento());

			fatura.incluir();
		}

		System.out.println("Gravou Faturas Sinistro......: ");

		for (Iterator i = this.anulacoes.iterator(); i.hasNext();) {
			AnulacaoInstrumento anulacao = (AnulacaoInstrumento) i.next();

			Apolice apolice = (Apolice) anulacao.obterSuperior();

			anulacao.verificarDuplicidade(apolice, apolice.obterSecao(),
					anulacao.obterDataAnulacao(), anulacao.obterNumeroEndoso());

			anulacao.incluir();
		}

		System.out.println("Gravou Anulações......: ");

		for (Iterator i = this.cobrancas.iterator(); i.hasNext();) {
			RegistroCobranca cobranca = (RegistroCobranca) i.next();

			Apolice apolice = (Apolice) cobranca.obterSuperior();

			cobranca
					.verificarDuplicidade(apolice, apolice.obterSecao(),
							cobranca.obterDataCobranca(), cobranca
									.obterNumeroParcela(), cobranca
									.obterNumeroEndoso());

			cobranca.incluir();
		}

		System.out.println("Gravou Cobrança......: ");

		for (Iterator i = this.aspectos2.iterator(); i.hasNext();) {
			AspectosLegais aspectos = (AspectosLegais) i.next();

			Apolice apolice = (Apolice) aspectos.obterSuperior();

			aspectos.verificarDuplicidade(apolice, apolice.obterSecao(),
					aspectos.obterNumeroOrdem(), aspectos.obterNumeroEndoso());

			aspectos.incluir();
		}

		System.out.println("Gravou Aspectos Legais......: ");

		for (Iterator i = this.suplementos.iterator(); i.hasNext();) {
			Suplemento suplemento = (Suplemento) i.next();

			Apolice apolice = (Apolice) suplemento.obterSuperior();

			suplemento.verificarDuplicidade(apolice, apolice.obterSecao(),
					suplemento.obterNumero());

			suplemento.incluir();
		}

		System.out.println("Gravou Suplementos......: ");

		for (Iterator i = this.refinacoes.iterator(); i.hasNext();) {
			Refinacao refinacao = (Refinacao) i.next();

			Apolice apolice = (Apolice) refinacao.obterSuperior();

			refinacao.verificarDuplicidade(apolice, apolice.obterSecao(),
					refinacao.obterNumeroEndoso());

			refinacao.incluir();
		}

		System.out.println("Gravou Refinanciación......: ");

		for (Iterator i = this.gastos2.iterator(); i.hasNext();) {
			RegistroGastos gastos = (RegistroGastos) i.next();

			Sinistro sinistro = (Sinistro) gastos.obterSuperior();

			//gastos.verificarDuplicidade(sinistro, gastos.obterDataSinistro(),
				//	gastos.obterNumeroEndoso());

			gastos.incluir();
		}

		System.out.println("Gravou Registros de Gastos......: ");

		for (Iterator i = this.anulacoes2.iterator(); i.hasNext();) {
			RegistroAnulacao anulacao = (RegistroAnulacao) i.next();

			anulacao.incluir();
		}

		System.out.println("Gravou Anulações 2......: ");

		for (Iterator i = this.morosidades.iterator(); i.hasNext();) {
			Morosidade morosidade = (Morosidade) i.next();

			morosidade.incluir();
		}

		System.out.println("Gravou Morosidade......: ");

		for (Iterator i = this.ratiosPermanentes.iterator(); i.hasNext();) {
			RatioPermanente ratios = (RatioPermanente) i.next();

			for (Iterator k = ratios.obterOrigem().obterEventosComoOrigem()
					.iterator(); k.hasNext();) {
				Evento e = (Evento) k.next();

				if (e instanceof RatioTresAnos)
					if (e.obterFase().obterCodigo().equals(
							Evento.EVENTO_PENDENTE))
						e.atualizarFase(Evento.EVENTO_CONCLUIDO);
			}

			ratios.incluir();
		}

		System.out.println("Gravou Ratios Permanentes......: ");

		for (Iterator i = this.ratiosUmAno.iterator(); i.hasNext();) {
			RatioUmAno ratios = (RatioUmAno) i.next();

			for (Iterator k = ratios.obterOrigem().obterEventosComoOrigem()
					.iterator(); k.hasNext();) {
				Evento e = (Evento) k.next();

				if (e instanceof RatioTresAnos)
					if (e.obterFase().obterCodigo().equals(
							Evento.EVENTO_PENDENTE))
						e.atualizarFase(Evento.EVENTO_CONCLUIDO);
			}

			ratios.incluir();
		}

		System.out.println("Gravou Ratios 1 Ano......: ");

		for (Iterator i = this.ratiosTresAnos.iterator(); i.hasNext();) {
			RatioTresAnos ratios = (RatioTresAnos) i.next();

			for (Iterator k = ratios.obterOrigem().obterEventosComoOrigem()
					.iterator(); k.hasNext();) {
				Evento e = (Evento) k.next();

				if (e instanceof RatioTresAnos)
					if (e.obterFase().obterCodigo().equals(
							Evento.EVENTO_PENDENTE))
						e.atualizarFase(Evento.EVENTO_CONCLUIDO);
			}

			ratios.incluir();
		}

		System.out.println("Gravou Ratios 3 Anos......: ");

	}

	private Collection validarAsegurado(Collection linhas) throws Exception {
		int numeroLinha = 1;

		ApoliceHome apoliceHome = (ApoliceHome) this.getModelManager().getHome(
				"ApoliceHome");
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager()
				.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");
		Apolice apolice = null;

		Aseguradora aseguradora = null;

		Usuario usuario = null;

		Date dataGeracao = null;

		String tipoArquivo = null;

		int numeroTotalRegistros = 0;

		for (Iterator i = linhas.iterator(); i.hasNext();) {
			String linha = (String) i.next();

			if (Integer.parseInt(linha.substring(0, 5)) != numeroLinha)
				erros
						.add("Error: 02 - Numero secuencial invalido (Archivo Datos del Asegurado) - Linea: "
								+ numeroLinha);

			System.out.println("numeroLinhaB: " + numeroLinha);

			// REGISTRO TIPO 1
			if (Integer.parseInt(linha.substring(5, 7)) == 1) {
				String sigla = linha.substring(7, 10);

				aseguradora = (Aseguradora) entidadeHome
						.obterEntidadePorSigla(sigla);

				if (aseguradora == null)
					erros
							.add("Error: 03 - Aseguradora "
									+ sigla
									+ " no fue encontrada (Archivo Datos del Asegurado) - Linea: "
									+ numeroLinha);
				else {
					if (!this.obterOrigem().equals(aseguradora))
						erros
								.add("Error: 06 - Aseguradora "
										+ sigla
										+ " no es la misma de la agenda (Archivo Datos del Asegurado) - Linea: "
										+ numeroLinha);
				}

				String chaveUsuario = linha.substring(10, 19);

				usuario = usuarioHome.obterUsuarioPorChave(chaveUsuario.trim());

				if (usuario == null)
					erros
							.add("Error: 04 - Usuario "
									+ chaveUsuario.trim()
									+ " no fue encontrado (Archivo Datos del Asegurado) - Linea: "
									+ numeroLinha);

				String anoGeracao = linha.substring(20, 24);

				String mesGeracao = linha.substring(24, 26);

				String diaGeracao = linha.substring(26, 28);

				if (diaGeracao.startsWith(" ") || mesGeracao.startsWith(" ")
						|| anoGeracao.startsWith(" "))
					erros.add("Error: 92 - Fecha Emisión Invalida - Linea: "
							+ numeroLinha);
				else
					dataGeracao = new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaGeracao + "/" + mesGeracao + "/"
									+ anoGeracao);

				String anoReporte = linha.substring(28, 32);

				String mesReporte = linha.substring(32, 34);

				if (this.obterMesMovimento() != Integer.parseInt(mesReporte))
					erros
							.add("Error: 07 - Mes informado es diferente del mes de la agenda (Archivo Datos del Asegurado)");
				if (this.obterAnoMovimento() != Integer.parseInt(anoReporte))
					erros
							.add("Error: 08 - Año informado es diferente del año de la agenda (Archivo Datos del Asegurado)");

				numeroTotalRegistros = Integer
						.parseInt(linha.substring(34, 44));

				tipoArquivo = "";

				if (!linha.substring(44, 45).toLowerCase().equals("n"))
					erros.add("Error: 82 - Tipo de Archivo Invalido - Linea: "
							+ numeroLinha);
			}
			// REGISTRO TIPO 17
			else if (Integer.parseInt(linha.substring(5, 7)) == 17) {
				String apelidoCconta = linha.substring(7, 17);

				ClassificacaoContas cContas = null;
				cContas = (ClassificacaoContas) entidadeHome
						.obterEntidadePorApelido(apelidoCconta.trim());

				if (cContas == null)
					erros
							.add("Error: 05 - Cuenta "
									+ apelidoCconta.trim()
									+ " no fue encontrada (Archivo Datos del Asegurado) - Linea: "
									+ numeroLinha);

				String numeroInstrumento = linha.substring(17, 27);

				if (this.apolices != null)
					apolice = (Apolice) this.apolices.get(numeroInstrumento);

				if (apolice == null)
					apolice = apoliceHome.obterApolice(numeroInstrumento);

				if (apolice == null)
					erros
							.add("Error: 73 - Instumento "
									+ numeroInstrumento.trim()
									+ " no fue encontrado (Archivo Datos del Asegurado) - Linea: "
									+ numeroLinha);

				String nomeAsegurado = linha.substring(27, 87);

				String tipoPessoa = "";

				if (linha.substring(87, 88).equals("1"))
					tipoPessoa = "Persona Fisica";
				else if (linha.substring(87, 88).equals("2"))
					tipoPessoa = "Persona Juridica";

				String tipoIdentificacao = "";

				if (linha.substring(88, 89).equals("1"))
					tipoIdentificacao = "Cédula de Identidad Paraguaya";
				else if (linha.substring(88, 89).equals("2"))
					tipoIdentificacao = "Cédula de Identidad Extranjera";
				else if (linha.substring(88, 89).equals("3"))
					tipoIdentificacao = "Passaporte";
				else if (linha.substring(88, 89).equals("4"))
					tipoIdentificacao = "RUC";
				else if (linha.substring(88, 89).equals("5"))
					tipoIdentificacao = "Otro";

				String numeroIdentificacao = linha.substring(89, 104);

				String anoNascimento = linha.substring(104, 108);

				String mesNascimento = linha.substring(108, 110);

				String diaNascimento = linha.substring(110, 112);

				Date dataNascimento = null;

				if (diaNascimento.startsWith(" ")
						|| mesNascimento.startsWith(" ")
						|| anoNascimento.startsWith(" ")) {
					if (cContas.obterApelido().startsWith("04010120")
							&& tipoPessoa.equals("Persona Fisica"))
						erros
								.add("Error: 92 - Fecha de Nacimiento invalida - Linea: "
										+ numeroLinha);
					else {

					}
				} else
					dataNascimento = new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaNascimento + "/" + mesNascimento + "/"
									+ anoNascimento);

				String tomadorSeguro = linha.substring(112, 172);

				if (erros.size() == 0) {
					apolice.atribuirNomeAsegurado(nomeAsegurado);
					apolice.atribuirTipoPessoa(tipoPessoa);
					apolice.atribuirTipoIdentificacao(tipoIdentificacao);
					apolice.atribuirNumeroIdentificacao(numeroIdentificacao);
					apolice.atribuirDataNascimento(dataNascimento);
					apolice.atribuirNomeTomador(tomadorSeguro);
				}

			}

			numeroLinha++;
		}

		if (erros.size() == 0)
			this.gravarEventosDaApolice();

		return erros;
	}

	private String obterTipoMoeda(String cod) throws Exception {
		String moeda = "";

		if (cod.equals("01"))
			moeda = "Guaraní";
		else if (cod.equals("02"))
			moeda = "Dólar USA";
		else if (cod.equals("03"))
			moeda = "Euro";
		else if (cod.equals("04"))
			moeda = "Real";
		else if (cod.equals("05"))
			moeda = "Peso Arg";
		else if (cod.equals("06"))
			moeda = "Peso Uru";
		else if (cod.equals("07"))
			moeda = "Yen";

		return moeda;
	}
	
	public boolean permiteAtualizar() throws Exception
	{
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		Entidade informatica = home.obterEntidadePorApelido("informatica");
		
		if(this.obterId() > 0)
		{
			if(this.obterFase().obterCodigo().equals(Evento.EVENTO_CONCLUIDO))
				return super.permiteAtualizar();
			else
			{
				if(informatica!=null)
				{
					if(this.obterUsuarioAtual().obterSuperiores().contains(informatica) || this.obterUsuarioAtual().obterId() == 1)
						return true;
					else
						return false;
				}
				else
					return super.permiteAtualizar();
			}
		}
		else
			return true;
	}
	
	public void atualizarQtdeA(int total) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set qtdeA = ? where id = ?");
		update.addInt(total);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public void atualizarQtdeB(int total) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set qtdeB = ? where id = ?");
		update.addInt(total);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public int obterQtdeRegistrosA() throws Exception
	{
		int qtde = 0;
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select qtdeA from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		qtde = query.executeAndGetFirstRow().getInt("qtdeA");
		
		if(qtde == 0)
		{
			//String[] mesAnoArray = ultimaAgendaStr.split("/");
			//String mesAno = mesAnoArray[1] + mesAnoArray[0];
			String mesAno = "";
			mesAno+= this.obterAnoMovimento();
			
			if(new Integer(this.obterMesMovimento()).toString().length() == 1)
				mesAno += "0" + this.obterMesMovimento();
			else
				mesAno += this.obterMesMovimento();
			
			String sigla = this.obterOrigem().obterSigla();
			File file = new File("C:/Aseguradoras/Archivos/A" + sigla + mesAno + ".txt");
			
			if(file.exists())
			{
				Scanner scan = new Scanner(file);
				
				String linha = scan.nextLine();
				
				//qtde = Integer.parseInt(linha.substring(34, 44));
				qtde = Integer.parseInt(linha.substring(36, 46));
				
				this.atualizarQtdeA(qtde);
			}
		}
		
		return qtde;
	}
	
	public int obterQtdeRegistrosB() throws Exception
	{
		int qtde = 0;
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select qtdeB from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		qtde = query.executeAndGetFirstRow().getInt("qtdeB");
		
		if(qtde == 0)
		{
			String mesAno = "";
			mesAno+= this.obterAnoMovimento();
			
			if(new Integer(this.obterMesMovimento()).toString().length() == 1)
				mesAno += "0" + this.obterMesMovimento();
			else
				mesAno += this.obterMesMovimento();
			
			String sigla = this.obterOrigem().obterSigla();
			File file = new File("C:/Aseguradoras/Archivos/B" + sigla + mesAno + ".txt");
			
			if(file.exists())
			{
				Scanner scan = new Scanner(file);
				
				String linha = scan.nextLine();
				
				//qtde = Integer.parseInt(linha.substring(34, 44));
				qtde = Integer.parseInt(linha.substring(36, 46));
				
				this.atualizarQtdeB(qtde);
			}
		}
		
		return qtde;
	}
	
	public Date obterDataModificacaoArquivo() throws Exception
	{
		Date data = null;
		
		String mesAno = "";
		mesAno+= this.obterAnoMovimento();
		
		if(new Integer(this.obterMesMovimento()).toString().length() == 1)
			mesAno += "0" + this.obterMesMovimento();
		else
			mesAno += this.obterMesMovimento();
		
		String sigla = this.obterOrigem().obterSigla();
		
		File file = new File("C:/Aseguradoras/Archivos/A" + sigla + mesAno + ".txt");
		
		if(file.exists())
		{
			long dataLong = file.lastModified();
			
			if(dataLong > 0)
				data = new Date(dataLong);
		}
		
		return data;
	}
	
	public void atualizarEspecial(String especial) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set especial = ? where id = ?");
		update.addString(especial);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public String obterEspecial() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select especial from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("especial");
	}
	
	public boolean eEspecial() throws Exception
	{
		if(this.obterEspecial().equals("Sim"))
			return true;
		else
			return false;
	}
	
	public void atualizarInscricaoEspecial(String especial) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set inscricao_especial = ? where id = ?");
		update.addString(especial);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public void atualizarSuplementosEspecial(String especial) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set suplemento_especial = ? where id = ?");
		update.addString(especial);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public void atualizarCapitalEspecial(String especial) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set capital_especial = ? where id = ?");
		update.addString(especial);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public void atualizarDataEspecial(String especial) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set fecha_especial = ? where id = ?");
		update.addString(especial);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public void atualizarDocumentoEspecial(String especial) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set documento_especial = ? where id = ?");
		update.addString(especial);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public void atualizarApAnteriorEspecial(String especial) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set ap_anterior_especial = ? where id = ?");
		update.addString(especial);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public void atualizarEndosoApolice(String especial) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set endoso_apolice = ? where id = ?");
		update.addString(especial);
		update.addLong(this.obterId());
		
		update.execute();
	}

	public String obterInscricaoEspecial() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select inscricao_especial from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("inscricao_especial");
	}
	
	public String obterSuplementoEspecial() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select suplemento_especial from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("suplemento_especial");
	}
	
	public String obterCapitalEspecial() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select capital_especial from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("capital_especial");
	}
	
	public String obterDataEspecial() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select fecha_especial from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("fecha_especial");
	}
	
	public String obterDocumentoEspecial() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select documento_especial from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("documento_especial");
	}
	
	public String obterApAnteriorEspecial() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select ap_anterior_especial from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("ap_anterior_especial");
	}
	
	public String obterEndosoApolice() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select endoso_apolice from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("endoso_apolice");
	}

	public void atualizaUltimaAgenda(String tipo) throws Exception
	    {
	    	long asegId = this.obterOrigem().obterId();
	    	int mes = this.obterMesMovimento();
	    	int ano = this.obterAnoMovimento();
	    	long agendaId = this.obterId();
	    	
	    	SQLQuery query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from ultima_agenda where aseguradora_id = ? and mes > ? and ano > ? and tipo = ?");
	    	query.addLong(asegId);
	    	query.addInt(mes);
	    	query.addInt(ano);
	    	query.addString(tipo);
	    	
	    	int qtde = query.executeAndGetFirstRow().getInt("qtde"); 
	    	
	    	if(qtde == 0)
	    	{
	    		query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from ultima_agenda where aseguradora_id = ? and tipo = ?");
	        	query.addLong(this.obterOrigem().obterId());
	        	query.addString(tipo);
	        	
	        	qtde = query.executeAndGetFirstRow().getInt("qtde");
	        	
	        	if(qtde == 0)
	        	{
	        		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into ultima_agenda(aseguradora_id, agenda_id, mes, ano, tipo) values(?,?,?,?,?)");
	        		insert.addLong(asegId);
	        		insert.addLong(agendaId);
	        		insert.addInt(mes);
	        		insert.addInt(ano);
	        		insert.addString(tipo);
	        		
	        		insert.execute();
	        	}
	        	else
	        	{
	        		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update ultima_agenda set agenda_id = ?, mes = ?, ano = ? where aseguradora_id = ? and tipo = ?");
	        		update.addLong(agendaId);
	        		update.addInt(mes);
	        		update.addInt(ano);
	        		update.addLong(asegId);
	        		update.addString(tipo);
	        		
	        		update.execute();
	        	}
	    	}
	    }
}