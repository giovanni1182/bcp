package com.gvs.crm.model.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.AgendaMovimentacaoHome;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.FtpBcp;
import com.gvs.crm.model.Livro;
import com.gvs.crm.model.LivroHome;
import com.gvs.crm.model.Notificacao;
import com.gvs.crm.model.Parametro;
import com.gvs.crm.model.SampleModelManager;
import com.gvs.crm.model.UploadedFile;
import com.gvs.crm.model.UploadedFileHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.Uteis;
import com.zehon.FileTransferStatus;
import com.zehon.exception.FileTransferException;
import com.zehon.sftp.SFTP;
import com.zehon.sftp.SFTPClient;

import infra.config.InfraProperties;
import infra.model.Home;

public class FtpBcpImpl extends Home implements FtpBcp
{
	  private FileWriter fileLog = null;
	  private Map<String,String> arquivosErroSFTP;
	  private Map<String,String> arquivosSTFP;
	  private Map<String,AgendaMovimentacao> agendasContabeis;
	  private String pastaArquivos = "C:/Aseguradoras/Archivos/";
	  private String pastaLivros = "C:/Aseguradoras/libros/";
	  private AgendaMovimentacaoHome agendaHome;
	    
	 public void conectarSFTP()throws Exception
	    {
	    	//Adicionar os JARs
	        //zehon_file_transfer-1.1.6.jar
	        //commons-vfs-2.0.jar
	        //commons-logging-1.0.4.jar
	    	//jsch-0.1.41.jar
		 	if(agendasContabeis == null)
		 		agendasContabeis = new TreeMap<String,AgendaMovimentacao>();
		 	if(agendaHome == null)
		 		agendaHome = (AgendaMovimentacaoHome) this.getModelManager().getHome("AgendaMovimentacaoHome");
	    	
		 	EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		 	LivroHome livroHome = (LivroHome) this.getModelManager().getHome("LivroHome");
		 	Usuario usuarioAtual = (Usuario) home.obterEntidadePorId(1);//Admin
		 	Entidade destino = (Entidade) home.obterEntidadePorApelido("SuperSeg");//BCP
		 	
		 	SampleModelManager mm = new SampleModelManager();
	        UploadedFileHome upHome = (UploadedFileHome) mm.getHome("UploadedFileHome");
	        
	        Uteis uteis = new Uteis();
		 
		 	if(this.arquivosErroSFTP == null)
	    		this.arquivosErroSFTP = new TreeMap<String,String>();
	    	if(this.arquivosSTFP == null)
	    		this.arquivosSTFP = new TreeMap<String,String>();
	    	
	    	Date data = new Date();
	    	
	    	fileLog = new FileWriter("C:/Logs/Log_" + data.getTime() + ".txt");
	    	
			String arquivoErro = "";
			
	    	try
	    	{
	    		InfraProperties infra = InfraProperties.getInstance();
	    		String host = infra.getProperty("ftp.host");
	    		String porta = infra.getProperty("ftp.port");
	    		String user = infra.getProperty("ftp.user");
	    		String pass = infra.getProperty("ftp.password");
	    		String diretorio = infra.getProperty("ftp.pastaraiz");
	    		String diretorioCentral = infra.getProperty("ftp.diretoriocentral");
	    		String diretorioContabil = infra.getProperty("ftp.diretoriocontabil");
		    	String diretorioLivros = infra.getProperty("ftp.diretoriolivros");
		    	String diretorioLog = infra.getProperty("ftp.diretoriolog");
	    		
	    		System.out.println("Conectando ao SFTP " + host);
	    		
	    		SFTPClient sftp = new SFTPClient();
		    	sftp.setPort(Integer.valueOf(porta));
		    	sftp.setServerName(host);
		    	sftp.setUsername(user);
		    	sftp.setPassword(pass);
		    	
		    	String[] diretorioRaiz = sftp.getFileNamesInFolder(diretorio);
		    	
		    	for(int i = 0 ; i < diretorioRaiz.length ; i++)
		    	{
		    		String pasta = diretorioRaiz[i]; //Nome Aseguradora
		    		System.out.println(pasta);
		    		
		    		String segundaPastaCentral = diretorio + "/" + pasta + diretorioCentral;
		    		
		    		//CENTRAL DE INFORMAÇÕES
		    		String[] arquivos = sftp.getFileNamesInFolder(segundaPastaCentral);
		    		
		    		for(int j = 0 ; j < arquivos.length ; j++)
		    		{
		    			String arquivo = arquivos[j];
		    			
		    			boolean existe = sftp.fileExists(segundaPastaCentral, arquivo);
		    			if(existe)
		    			{
		    				//Fazer isso, pq alguns arquivos com a extensão txt em maiusculo
		    				String arquivoAux = arquivo.substring(0, 1) +  arquivo.substring(1, arquivo.length()).toLowerCase();
			    			boolean arquivoA = false;
			    			
			    			String nomeArquivoAux = arquivoAux.substring(0, arquivoAux.indexOf(".txt"));
			    			String caminhoArquivo = "C:/Logs/Log_"+nomeArquivoAux+"_" + new Date().getTime() + ".txt";
			    			String pastaFTP = diretorio+"/"+pasta+diretorioLog;
			    			
			    			FileWriter logIndividual = new FileWriter(caminhoArquivo);
			    			
			    			if(arquivoAux.indexOf(".txt")!=-1 && (arquivoAux.indexOf("A") !=-1 || arquivoAux.indexOf("B") !=-1))
			    			{
			    				fileLog.write("***** Archivo "+arquivo+" *****\r\n");
			    				logIndividual.write("***** Archivo "+arquivo+" *****\r\n");
			    				
			    				if(arquivoAux.substring(0, 1).equals("A"))
			    					arquivoA = true;
			    				
			    				String tipoArquivo = "Instrumento";
			    				
			    				String siglaArquivo = arquivo.substring(1, 4);
			    				
				    			System.out.println("Fazendo download do arquivo " + arquivo);
				    			arquivoErro = arquivo;
				    			
				    			if(!arquivosErroSFTP.containsKey(arquivoErro))
				    			{
					    			Entidade aseguradora = home.obterEntidadePorSigla(siglaArquivo);
					    			if(aseguradora != null)
					    			{
					    				String anoArquivo = arquivo.substring(4,8);
					    				String mesArquivo = arquivo.substring(8,10);
					    				
					    				AgendaMovimentacao agendaMovimentacao = agendaHome.obterAgendaNoPeriodo(Integer.valueOf(mesArquivo), Integer.valueOf(anoArquivo), aseguradora, tipoArquivo);
					    				
					    				if(agendaMovimentacao == null)
					    				{
					    					agendaMovimentacao = (AgendaMovimentacao) this.getModelManager().getEntity("AgendaMovimentacao");
					    					agendaMovimentacao.atribuirOrigem(aseguradora);
						    				agendaMovimentacao.atribuirTipo(tipoArquivo);
					    				}
					    				
				    					boolean agendaPendente = false;
				    					
				    					Collection agendas = aseguradora.obterAgendas(tipoArquivo);
				    					
				    					for(Iterator k = agendas.iterator() ; k.hasNext() ; )
				    					{
				    						AgendaMovimentacao agenda = (AgendaMovimentacao) k.next();
				    						
		    								if(agenda.obterFase().obterCodigo().equals(AgendaMovimentacao.EVENTO_PENDENTE) && agenda.obterId()!=agendaMovimentacao.obterId())
		    								{
		    									fileLog.write("Existe Agenda Pendiente " + agenda.obterMesMovimento() + "/" + agenda.obterAnoMovimento() + ", Aseguradora " +aseguradora.obterNome() +  "\r\n");
		    									fileLog.write("O archivo " + agenda.obterMesMovimento() + "/" + agenda.obterAnoMovimento() +" no fue encontrado en FTP\r\n");
		    									fileLog.write("\r\n");
		    									
		    									logIndividual.write("Existe Agenda Pendiente " + agenda.obterMesMovimento() + "/" + agenda.obterAnoMovimento() + ", Aseguradora " +aseguradora.obterNome() +  "\r\n");
		    									logIndividual.write("O archivo " + agenda.obterMesMovimento() + "/" + agenda.obterAnoMovimento() +" no fue encontrado en FTP\r\n");
		    									
		    									this.arquivosErroSFTP.put(arquivoErro, arquivoErro);
		    									agendaPendente = true;
		    									break;
		    								}
				    					}
				    					
				    					if(!agendaPendente)
				    					{
				    						data = new Date();
							    			
							    			String horaDW = new SimpleDateFormat("HH:mm:ss").format(data);
							    			
							    			fileLog.write("Inicio do download " + horaDW + "\r\n");
							    			logIndividual.write("Inicio do download " + horaDW + "\r\n");
							    			
						    				int status = sftp.getFile(arquivo, segundaPastaCentral, pastaArquivos);
							    			
						    				data = new Date();
						    				
						    				horaDW = new SimpleDateFormat("HH:mm:ss").format(data);
						    				
							    			if(FileTransferStatus.SUCCESS == status)
							    			{
							    				fileLog.write("Sucesso no download para pasta "+pastaArquivos+"" + "\r\n");
							    				fileLog.write("Termino do download " + horaDW + "\r\n");
							    				
							    				logIndividual.write("Sucesso no download\r\n");
							    				logIndividual.write("Termino do download " + horaDW + "\r\n");
							    				
							    			}
							    			else if(FileTransferStatus.FAILURE == status)
							    			{
							    				fileLog.write("Falha no download para pasta "+pastaArquivos+"" + "\r\n");
							    				fileLog.write("Termino da falha no download " + horaDW + "\r\n");
							    				
							    				logIndividual.write("Falha no download\r\n");
							    				logIndividual.write("Termino da falha no download " + horaDW + "\r\n");
							    			}
							    			
							    			arquivosSTFP.put(arquivo, segundaPastaCentral);
							    			
							    			if(arquivoA)
							    			{
							    				if(agendaMovimentacao.obterId() == 0)
							    				{
							    					agendaMovimentacao.atribuirResponsavel(usuarioAtual);
							    					agendaMovimentacao.atribuirDescricao("Archivo Ftp - " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
							    					agendaMovimentacao.atribuirDestino(destino);
							    					agendaMovimentacao.atribuirTitulo(mesArquivo +" - " + anoArquivo);
							    					agendaMovimentacao.atribuirDataPrevistaInicio(new Date());
							    					agendaMovimentacao.atribuirAnoMovimento(Integer.valueOf(anoArquivo));
							    					agendaMovimentacao.atribuirMesMovimento(Integer.valueOf(mesArquivo));
							    					agendaMovimentacao.incluir();
							    					
							    					agendaMovimentacao.atualizarValidacao("Total");
							    					
						    						agendaMovimentacao.atualizarEspecial("Sim");
						    						agendaMovimentacao.atualizarInscricaoEspecial("Sim");
							    					agendaMovimentacao.atualizarSuplementosEspecial("Sim");
							    					agendaMovimentacao.atualizarCapitalEspecial("Sim");
							    					agendaMovimentacao.atualizarDataEspecial("Sim");
							    					agendaMovimentacao.atualizarDocumentoEspecial("Sim");
						    					
						    						agendaMovimentacao.atualizarFase(AgendaMovimentacao.AGENDADA);
							    				}
							    				else
							    				{
							    					String descricaoAntiga = agendaMovimentacao.obterDescricao();
							    					descricaoAntiga+="\n"+"Archivo Ftp - " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
							    					agendaMovimentacao.atualizarDescricao(descricaoAntiga);
							    					
							    					agendaMovimentacao.atualizarEspecial("Sim");
						    						agendaMovimentacao.atualizarInscricaoEspecial("Sim");
							    					agendaMovimentacao.atualizarSuplementosEspecial("Sim");
							    					agendaMovimentacao.atualizarCapitalEspecial("Sim");
							    					agendaMovimentacao.atualizarDataEspecial("Sim");
							    					agendaMovimentacao.atualizarDocumentoEspecial("Sim");
							    					
							    					if(agendaMovimentacao.obterTipo().equals("Instrumento") && !agendaMovimentacao.obterFase().obterCodigo().equals(AgendaMovimentacao.AGENDADA))
							    						agendaMovimentacao.atualizarFase(AgendaMovimentacao.AGENDADA);
							    				}
							    			}
					    					
					    					int statusDelete = sftp.deleteFile(arquivo, segundaPastaCentral);
							    			
							    			if(FileTransferStatus.SUCCESS == statusDelete)
							    				fileLog.write("Archivo Excluido do FTP com sucesso" + "\r\n");
							    			else if(FileTransferStatus.FAILURE == statusDelete)
							    				fileLog.write("Falha na exclusão do archivo no FTP" + "\r\n");
							    			
							    			if(arquivoA)
							    			{
								    			fileLog.write("Agendada com sucesso\r\n");
								    			fileLog.write("\r\n");
								    			
								    			logIndividual.write("Agendada com sucesso\r\n");
								    			logIndividual.write("\r\n");
							    			}
				    					}
					    			}
					    			else
					    			{
					    				fileLog.write("Não achou Aseguradora com sigla " + siglaArquivo + "\r\n");
					    				fileLog.write("\r\n");
					    				
					    				logIndividual.write("Não achou Aseguradora com sigla " + siglaArquivo + "\r\n");
					    				logIndividual.write("\r\n");
					    				
					    				this.arquivosErroSFTP.put(arquivoErro, arquivoErro);
					    			}
				    			}
		    				}
		    				
		    				logIndividual.close();
		    				
		    				try
			    			{
			    				int uploadStatus = SFTP.sendFile(caminhoArquivo, pastaFTP, host, user, pass);
			    		        if(FileTransferStatus.SUCCESS == uploadStatus){
			    		            fileLog.write("Archivo " + caminhoArquivo + ", enviado com sucesso para o FTP\r\n");
			    		        }
			    		        else if(FileTransferStatus.FAILURE == uploadStatus){
			    		        	fileLog.write("Falha ao enviar o Archivo " + caminhoArquivo + " para o FTP\r\n");
			    		        }
			    			}
			    			catch(FileTransferException e)
			    			{
			    				fileLog.write("Erro interno ao enviar o Archivo " + caminhoArquivo + " para o FTP\r\n");
			    				fileLog.write(e.getMessage()+"\r\n");
			    			}
		    			}
	    			}
		    		
		    		String segundaPastaContabil = diretorio + "/" + pasta + diretorioContabil;
		    		
		    		//ARQUIVOS CONTABEIS
		    		String[] arquivosContabeis = sftp.getFileNamesInFolder(segundaPastaContabil);
		    		
		    		for(int j = 0 ; j < arquivosContabeis.length ; j++)
		    		{
		    			String arquivo = arquivosContabeis[j];
		    			
		    			boolean existe = sftp.fileExists(segundaPastaContabil, arquivo);
		    			
		    			if(existe)
		    			{
			    			//Fazer isso, pq alguns arquivos com a extensão txt em maiusculo
			    			String arquivoAux = arquivo.toLowerCase();
			    			
			    			String nomeArquivoAux = arquivoAux.substring(0, arquivoAux.indexOf(".txt"));
			    			String caminhoArquivo = "C:/Logs/Log_"+nomeArquivoAux+"_" + new Date().getTime() + ".txt";
			    			String pastaFTP = diretorio+"/"+pasta+diretorioLog;
			    			
			    			FileWriter logIndividual = new FileWriter(caminhoArquivo);
			    			
			    			//Fazer isso pra só pegar arquivos contabeis
			    			if(arquivoAux.indexOf(".txt")!=-1 && arquivoAux.length() == 13)
			    			{
			    				fileLog.write("***** Archivo "+arquivo+" *****\r\n");
			    				logIndividual.write("***** Archivo "+arquivo+" *****\r\n");
			    				
			    				String tipoArquivo = "Contabil";
			    				
			    				String siglaArquivo = arquivo.substring(0, 3);
					    		System.out.println("Fazendo download do arquivo " + arquivo);
					    		arquivoErro = arquivo;
					    			
					    		if(!arquivosErroSFTP.containsKey(arquivoErro))
					    		{
						    		Entidade aseguradora = home.obterEntidadePorSigla(siglaArquivo);
						    		if(aseguradora != null)
						    		{
						    			String anoArquivo = arquivo.substring(3,7);
						    			String mesArquivo = arquivo.substring(7,9);
						    				
						    			AgendaMovimentacao agendaMovimentacao = agendaHome.obterAgendaNoPeriodo(Integer.valueOf(mesArquivo), Integer.valueOf(anoArquivo), aseguradora, tipoArquivo);
						    				
						    			if(agendaMovimentacao == null)
						    			{
						    				agendaMovimentacao = (AgendaMovimentacao) this.getModelManager().getEntity("AgendaMovimentacao");
						    				agendaMovimentacao.atribuirOrigem(aseguradora);
							    			agendaMovimentacao.atribuirTipo(tipoArquivo);
						    			}
						    				
					    				boolean agendaPendente = false;
					    					
				    					Collection agendas = aseguradora.obterAgendas(tipoArquivo);
				    					
				    					for(Iterator k = agendas.iterator() ; k.hasNext() ; )
				    					{
				    						AgendaMovimentacao agenda = (AgendaMovimentacao) k.next();
				    						
		    								if(agenda.obterFase().obterCodigo().equals(AgendaMovimentacao.EVENTO_PENDENTE) && agenda.obterId()!=agendaMovimentacao.obterId())
		    								{
		    									fileLog.write("Existe Agenda Pendiente " + agenda.obterMesMovimento() + "/" + agenda.obterAnoMovimento() + ", Aseguradora " +aseguradora.obterNome() +  "\r\n");
		    									fileLog.write("O archivo " + agenda.obterMesMovimento() + "/" + agenda.obterAnoMovimento() +" no fue encontrado en FTP\r\n");
		    									fileLog.write("\r\n");
		    									
		    									logIndividual.write("Existe Agenda Pendiente " + agenda.obterMesMovimento() + "/" + agenda.obterAnoMovimento() + ", Aseguradora " +aseguradora.obterNome() +  "\r\n");
		    									logIndividual.write("O archivo " + agenda.obterMesMovimento() + "/" + agenda.obterAnoMovimento() +" no fue encontrado en FTP\r\n");
		    									
		    									this.arquivosErroSFTP.put(arquivoErro, arquivoErro);
		    									agendaPendente = true;
		    									break;
		    								}
				    					}
					    					
				    					if(!agendaPendente)
				    					{
				    						data = new Date();
							    			
							    			String horaDW = new SimpleDateFormat("HH:mm:ss").format(data);
							    			
							    			fileLog.write("Inicio do download " + horaDW + "\r\n");
							    			logIndividual.write("Inicio do download " + horaDW + "\r\n");
							    			
						    				int status = sftp.getFile(arquivo, segundaPastaContabil, pastaArquivos);
							    			
						    				data = new Date();
						    				
						    				horaDW = new SimpleDateFormat("HH:mm:ss").format(data);
						    				
							    			if(FileTransferStatus.SUCCESS == status)
							    			{
							    				fileLog.write("Sucesso no download para pasta "+pastaArquivos+"" + "\r\n");
							    				fileLog.write("Termino do download " + horaDW + "\r\n");
							    				
							    				logIndividual.write("Sucesso no download\r\n");
							    				logIndividual.write("Termino do download " + horaDW + "\r\n");
							    				
							    			}
							    			else if(FileTransferStatus.FAILURE == status)
							    			{
							    				fileLog.write("Falha no download para pasta "+pastaArquivos+"" + "\r\n");
							    				fileLog.write("Termino da falha no download " + horaDW + "\r\n");
							    				
							    				logIndividual.write("Falha no download\r\n");
							    				logIndividual.write("Termino da falha no download " + horaDW + "\r\n");
							    			}
							    			
							    			arquivosSTFP.put(arquivo, segundaPastaCentral);
							    			
							    			
						    				if(agendaMovimentacao.obterId() == 0)
						    				{
						    					agendaMovimentacao.atribuirResponsavel(usuarioAtual);
						    					agendaMovimentacao.atribuirDescricao("Archivo Ftp - " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
						    					agendaMovimentacao.atribuirDestino(destino);
						    					agendaMovimentacao.atribuirTitulo(mesArquivo +" - " + anoArquivo);
						    					agendaMovimentacao.atribuirDataPrevistaInicio(new Date());
						    					agendaMovimentacao.atribuirAnoMovimento(Integer.valueOf(anoArquivo));
						    					agendaMovimentacao.atribuirMesMovimento(Integer.valueOf(mesArquivo));
						    					agendaMovimentacao.incluir();
						    					
						    					agendaMovimentacao.atualizarValidacao("Total");
						    					
					    						agendasContabeis.put(arquivo,agendaMovimentacao);
					    						
					    						this.validarArquivosContabeisSFTP(agendaMovimentacao, home, fileLog, arquivoAux, host, user, pass, diretorio, diretorioContabil, pastaFTP);
						    				}
						    				else
						    				{
						    					String descricaoAntiga = agendaMovimentacao.obterDescricao();
						    					descricaoAntiga+="\n"+"Archivo Ftp - " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
						    					agendaMovimentacao.atualizarDescricao(descricaoAntiga);
						    					
						    					agendasContabeis.put(arquivo,agendaMovimentacao);
							    					
						    					this.validarArquivosContabeisSFTP(agendaMovimentacao, home, fileLog, arquivoAux, host, user, pass, diretorio, diretorioContabil, pastaFTP);
						    				}
					    					
					    					int statusDelete = sftp.deleteFile(arquivo, segundaPastaContabil);
							    			
							    			if(FileTransferStatus.SUCCESS == statusDelete)
							    				fileLog.write("Archivo Excluido do FTP com sucesso" + "\r\n");
							    			else if(FileTransferStatus.FAILURE == statusDelete)
							    				fileLog.write("Falha na exclusão do archivo no FTP" + "\r\n");
						    			}
						    			else
						    			{
						    				fileLog.write("Não achou Aseguradora com sigla " + siglaArquivo + "\r\n");
						    				fileLog.write("\r\n");
						    				
						    				logIndividual.write("Não achou Aseguradora com sigla " + siglaArquivo + "\r\n");
						    				logIndividual.write("\r\n");
						    				
						    				this.arquivosErroSFTP.put(arquivoErro, arquivoErro);
						    			}
					    			}
			    				}
			    				
			    				logIndividual.close();
			    				
			    				try
				    			{
				    				int uploadStatus = SFTP.sendFile(caminhoArquivo, pastaFTP, host, user, pass);
				    		        if(FileTransferStatus.SUCCESS == uploadStatus){
				    		            fileLog.write("Archivo " + caminhoArquivo + ", enviado com sucesso para o FTP\r\n");
				    		        }
				    		        else if(FileTransferStatus.FAILURE == uploadStatus){
				    		        	fileLog.write("Falha ao enviar o Archivo " + caminhoArquivo + " para o FTP\r\n");
				    		        }
				    			}
				    			catch(FileTransferException e)
				    			{
				    				fileLog.write("Erro interno ao enviar o Archivo " + caminhoArquivo + " para o FTP\r\n");
				    				fileLog.write(e.getMessage()+"\r\n");
				    			}
			    			}
		    			}
		    		}
		    		
		    		//VERIFICANDO OS LIVROS
		    		String segundaPastaLivros = diretorio + "/" + pasta + diretorioLivros;
		    		String[] livros = sftp.getFileNamesInFolder(segundaPastaLivros);
		    		
		    		for(int j = 0 ; j < livros.length ; j++)
		    		{
		    			String nomeArquivo = livros[j];
		    			
		    			boolean existe = sftp.fileExists(segundaPastaLivros, nomeArquivo);
		    			
		    			String tipo = nomeArquivo.substring(0,1);
		            	
		    			//PRA NÃO CONFUNDIR COM DIRETORIO
		            	if(tipo.toLowerCase().equals("l") && existe)
		            	{
		            		fileLog.write("***** Libro "+nomeArquivo+" *****\r\n");
		            		
		            		data = new Date();
			    			
			    			String horaDW = new SimpleDateFormat("HH:mm:ss").format(data);
			    			
			    			fileLog.write("Inicio do download " + horaDW + "\r\n");
			    			
		    				int status = sftp.getFile(nomeArquivo, segundaPastaLivros, pastaLivros);
			    			
		    				data = new Date();
		    				
		    				horaDW = new SimpleDateFormat("HH:mm:ss").format(data);
		    				
			    			if(FileTransferStatus.SUCCESS == status)
			    			{
			    				fileLog.write("Sucesso no download para pasta "+pastaLivros+"" + "\r\n");
			    				fileLog.write("Termino do download " + horaDW + "\r\n");
			    				
			    			}
			    			else if(FileTransferStatus.FAILURE == status)
			    			{
			    				fileLog.write("Falha no download para pasta "+pastaLivros+"" + "\r\n");
			    				fileLog.write("Termino da falha no download " + horaDW + "\r\n");
			    			}
		            		
			            	String sigla = nomeArquivo.substring(1,4); 
			            	
		            		arquivoErro = nomeArquivo;
		            		
		            		if(!arquivosErroSFTP.containsKey(arquivoErro))
			    			{
			            		String codLivro = nomeArquivo.substring(4,13);
				            	
				            	int ano = Integer.parseInt(nomeArquivo.substring(13,17));
				            	
				            	int mes = Integer.parseInt(nomeArquivo.substring(17,19));
				            	
				            	String extensao = nomeArquivo.substring(20,nomeArquivo.length());
				            	
				            	File arquivoLivro = new File(pastaLivros+nomeArquivo);
				            	
				            	String mimeType = Files.probeContentType(arquivoLivro.toPath());
				            	
				            	if(mimeType == null)
				            	{
				            		if(extensao.startsWith("xl"))
				            			mimeType = "application/vnd.ms-excel";
				            		else if(extensao.startsWith("pd"))
				            			mimeType = "application/pdf";
				            		else if(extensao.startsWith("do"))
				            			mimeType = "application/msword";
				            	}
				            	
				            	System.out.println(sigla + " " + codLivro + " " + ano + " " + mes + " " + extensao + " " + mimeType);
				            	
				            	Aseguradora aseg = (Aseguradora) home.obterEntidadePorSigla(sigla);
				            	if(aseg!=null)
				            	{
				            		//System.out.println(aseg.obterNome());
				            		String tipoLivro = uteis.obterNomeArquivo2(codLivro);
				            		
				            		Livro livro = livroHome.obterLivro(aseg, tipoLivro, mes, ano);
				    				if(livro == null)
				    				{
					            		livro = (Livro) mm.getEntity("Livro");
					            		livro.atribuirTitulo(tipoLivro);
					            		livro.atribuirTipo(tipoLivro);
					    				livro.atribuirMes(mes);
					    				livro.atribuirAno(ano);
					    				livro.atribuirOrigem(aseg);
					    				livro.incluir();
					    				
					    				livro.atualizarFase(Evento.EVENTO_CONCLUIDO);
				    				}
				    				
				    				Collection<UploadedFile> files = upHome.getAllUploadedFiles(livro);
				    				Collection<UploadedFile> filesExcluir = new ArrayList<UploadedFile>();
				    				
				    				for(Iterator<UploadedFile> k = files.iterator() ; k.hasNext() ; )
				    				{
				    					UploadedFile up = k.next();
				    					
				    					if(up.getName().equals(nomeArquivo))
				    						filesExcluir.add(up);
				    				}
				    				
				    				for(Iterator<UploadedFile> k = filesExcluir.iterator() ; k.hasNext() ; )
				    				{
				    					UploadedFile up = k.next();
				    					
				    					upHome.removeUploadedFile(up.getId());
				    				}
				    				
			    					InputStream input = new FileInputStream(pastaLivros+nomeArquivo);
			    					upHome.addUploadedFile(livro, input, nomeArquivo, mimeType, input.available(), 0);
			    					input.close();
			    					
			    					fileLog.write("Adicionou no Libro " + livro.obterTitulo() + " - " + livro.obterId() + "\r\n");
				            	}
				            	
				            	 InputStream is = new FileInputStream(pastaLivros+nomeArquivo);
				                 OutputStream os = new FileOutputStream("C:/Aseguradoras/backup_libros/" + nomeArquivo);
				                 byte buffer[] = new byte[is.available()];
				                 is.read(buffer);
				                 os.write(buffer);
				                 
				                 if(is != null)
				                     is.close();
				                 if(os != null)
				                     os.close();
				            	
				                 arquivoLivro.delete();
				                 fileLog.write("Libro excluido com sucesso da pasta "+pastaLivros+ "\r\n");
				                 
				                 int statusDelete = sftp.deleteFile(nomeArquivo, segundaPastaLivros);
					    			
				                 if(FileTransferStatus.SUCCESS == statusDelete)
				                	 fileLog.write("Libro Excluido do FTP com sucesso" + "\r\n");
				                 else if(FileTransferStatus.FAILURE == statusDelete)
				                	 fileLog.write("Falha na exclusão do Libro no FTP" + "\r\n");
				                 
				                 fileLog.write("\r\n");
		    				}
	    				}
	            	}
		    	}
	    	}
	    	catch (FileTransferException e)
	    	{
	    		System.out.println(e.toString());
	    		fileLog.write("Erro interno:" + e.getMessage() + "\r\n");
	    		fileLog.close();
	    		this.arquivosErroSFTP.put(arquivoErro, arquivoErro);
	    		this.conectarSFTP();
			}
	    	
	    	fileLog.close();
	    }
	    
	    public void conectarFTP()throws Exception
	    {
	    	String arquivoErro = "";
	    	if(agendasContabeis == null)
		 		agendasContabeis = new TreeMap<String,AgendaMovimentacao>();
		 	if(agendaHome == null)
		 		agendaHome = (AgendaMovimentacaoHome) this.getModelManager().getHome("AgendaMovimentacaoHome");
	    	
		 	EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		 	LivroHome livroHome = (LivroHome) this.getModelManager().getHome("LivroHome");
		 	Usuario usuarioAtual = (Usuario) home.obterEntidadePorId(1);//Admin
		 	Entidade destino = (Entidade) home.obterEntidadePorApelido("SuperSeg");//BCP
		 	
		 	SampleModelManager mm = new SampleModelManager();
	        UploadedFileHome upHome = (UploadedFileHome) mm.getHome("UploadedFileHome");
	        
	        Uteis uteis = new Uteis();
		 
		 	if(this.arquivosErroSFTP == null)
	    		this.arquivosErroSFTP = new TreeMap<String,String>();
	    	if(this.arquivosSTFP == null)
	    		this.arquivosSTFP = new TreeMap<String,String>();
	    	
	    	Date data = new Date();
	    	
	    	fileLog = new FileWriter("C:/Logs/Log_" + new Date().getTime() + ".txt");
	    	
	    	//Só para o BCP
	    	FTPSClient f = new FTPSClient(false);
	    	
	    	try
	    	{
	    		//fileLog.write("Entrou no try\r\n");
	    		
	    		InfraProperties infra = InfraProperties.getInstance();
	    		String host = infra.getProperty("ftp.host");
	    		String porta = infra.getProperty("ftp.port");
	    		String user = infra.getProperty("ftp.user");
	    		String pass = infra.getProperty("ftp.password");
	    		String diretorio = infra.getProperty("ftp.pastaraiz");
	    		String diretorioCentral = infra.getProperty("ftp.diretoriocentral");
	    		String diretorioContabil = infra.getProperty("ftp.diretoriocontabil");
		    	String diretorioLivros = infra.getProperty("ftp.diretoriolivros");
		    	String diretorioLog = infra.getProperty("ftp.diretoriolog");
	    		
	    		System.out.println("Conectando ao FTP " + host);
	    		//fileLog.write("Conectando ao FTP " + host + "\r\n");
	    		
	    		//fileLog.write("Tentando a conexão com "+host+"\r\n");
		    	f.connect(host,Integer.valueOf(porta));
		    	
	    		if(f.login(user, pass))
	    		{
	    			f.execPBSZ(0);
	    			f.execPROT("P");
	    			f.enterLocalPassiveMode(); // important!
	    			
	    			//fileLog.write("Conectou com sucesso " + host + "\r\n");
	    			f.setFileType(FTP.BINARY_FILE_TYPE);
	    			f.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
	    			
	    			//fileLog.write("Pasta " + f.printWorkingDirectory() + "\r\n");
	    			
	    			//boolean b = f.changeWorkingDirectory(diretorio);
	    			
	    			//fileLog.write("Alteração de pasta " + b + "\r\n");
	    			
	    			//fileLog.write("Alterou diretorio para " + f.printWorkingDirectory() + "\r\n");
	    			
	    			int reply = f.getReplyCode();
	    			if(FTPReply.isPositiveCompletion(reply))
	    			{
	    				 System.out.println("Connected Success");
	    				 fileLog.write("Connected Success " + host + "\r\n");
	    			}
	    			else
	    			{
	    				f.disconnect();
	    				System.out.println("Connection Failed");
	    				fileLog.write("Connection Failed " + host  + "\r\n");
	    				throw new Exception("Connection Failed");
	    			}
	                
	    			FTPFile[] files = f.listDirectories();
	    			
	    			for(int i = 0 ; i < files.length ; i++)
	    			{
	    				FTPFile file = files[i];
	    				String nomePasta = file.getName();
	    				
	    				if(!nomePasta.equals(".") && !nomePasta.equals("..") && nomePasta.substring(0, 1).equals("0"))
	    				{
		    				System.out.println(nomePasta);
		    				//fileLog.write("nomePasta " + nomePasta + "\r\n");
		    				
		    				//ARQUUIVOS CENTRAL DE INFORMAÇÕES
		    				//FTPFile[] filesCentral = f.listFiles(diretorio+"/"+nomePasta+diretorioCentral);
		    				FTPFile[] filesCentral = f.listFiles(diretorio+nomePasta+diretorioCentral);
		    				
		    				for(int j = 0 ; j < filesCentral.length ; j++)
			    			{
		    					FTPFile arquivo = filesCentral[j];
		    					String nomeArquivo = arquivo.getName();
		    					
		    					if(!nomeArquivo.equals(".") && !nomeArquivo.equals(".."))
			    				{
			    					String arquivoAux = nomeArquivo.substring(0, 1) +  nomeArquivo.substring(1, nomeArquivo.length()).toLowerCase();
					    			boolean arquivoA = false;
					    			
					    			String nomeArquivoAux = arquivoAux.substring(0, arquivoAux.indexOf(".txt"));
					    			
					    			String nomeArquivoFTP = "Log_"+nomeArquivoAux+"_" + new Date().getTime() + ".txt";

					    			String caminhoArquivo = "C:/Logs/"+nomeArquivoFTP;
					    			
					    			String pastaFTP = diretorio+nomePasta+diretorioLog+"/"+nomeArquivoFTP;
					    			
					    			FileWriter logIndividual = new FileWriter(caminhoArquivo);
					    			
					    			if(arquivoAux.indexOf(".txt")!=-1 && (arquivoAux.indexOf("A") !=-1 || arquivoAux.indexOf("B") !=-1))
					    			{
					    				fileLog.write("***** Archivo "+nomeArquivo+" *****\r\n");
					    				logIndividual.write("***** Archivo "+nomeArquivo+" *****\r\n");
					    				
					    				if(arquivoAux.substring(0, 1).equals("A"))
					    					arquivoA = true;
					    				
					    				String tipoArquivo = "Instrumento";
					    				
					    				String siglaArquivo = nomeArquivo.substring(1, 4);
					    				
						    			//System.out.println("Fazendo download do arquivo " + nomeArquivo);
						    			arquivoErro = nomeArquivo;
						    			
						    			if(!arquivosErroSFTP.containsKey(arquivoErro))
						    			{
							    			Entidade aseguradora = home.obterEntidadePorSigla(siglaArquivo);
							    			if(aseguradora != null)
							    			{
							    				String anoArquivo = nomeArquivo.substring(4,8);
							    				String mesArquivo = nomeArquivo.substring(8,10);
							    				
							    				AgendaMovimentacao agendaMovimentacao = agendaHome.obterAgendaNoPeriodo(Integer.valueOf(mesArquivo), Integer.valueOf(anoArquivo), aseguradora, tipoArquivo);
							    				
							    				if(agendaMovimentacao == null)
							    				{
							    					agendaMovimentacao = (AgendaMovimentacao) this.getModelManager().getEntity("AgendaMovimentacao");
							    					agendaMovimentacao.atribuirOrigem(aseguradora);
								    				agendaMovimentacao.atribuirTipo(tipoArquivo);
							    				}
							    				
						    					boolean agendaPendente = false;
						    					
						    					Collection agendas = aseguradora.obterAgendas(tipoArquivo);
						    					
						    					for(Iterator k = agendas.iterator() ; k.hasNext() ; )
						    					{
						    						AgendaMovimentacao agenda = (AgendaMovimentacao) k.next();
						    						
				    								if(agenda.obterFase().obterCodigo().equals(AgendaMovimentacao.EVENTO_PENDENTE) && agenda.obterId()!=agendaMovimentacao.obterId())
				    								{
				    									fileLog.write("Existe Agenda Pendiente " + agenda.obterMesMovimento() + "/" + agenda.obterAnoMovimento() + ", Aseguradora " +aseguradora.obterNome() +  "\r\n");
				    									fileLog.write("El archivo " + agenda.obterMesMovimento() + "/" + agenda.obterAnoMovimento() +" no fue encontrado en FTP\r\n");
				    									fileLog.write("\r\n");
				    									
				    									logIndividual.write("Existe Agenda Pendiente " + agenda.obterMesMovimento() + "/" + agenda.obterAnoMovimento() + ", Aseguradora " +aseguradora.obterNome() +  "\r\n");
				    									logIndividual.write("El archivo " + agenda.obterMesMovimento() + "/" + agenda.obterAnoMovimento() +" no fue encontrado en FTP\r\n");
				    									
				    									this.arquivosErroSFTP.put(arquivoErro, arquivoErro);
				    									agendaPendente = true;
				    									break;
				    								}
						    					}
						    					
						    					if(!agendaPendente)
						    					{
						    						data = new Date();
									    			
									    			String horaDW = new SimpleDateFormat("HH:mm:ss").format(data);
									    			
									    			fileLog.write("Inicio do download " + horaDW + "\r\n");
									    			logIndividual.write("Inicio do download " + horaDW + "\r\n");
									    			
									    			File downloadFile = new File(pastaArquivos+nomeArquivo);
									    			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
									    			
									    			String caminhoFTP = diretorio+nomePasta+diretorioCentral+"/"+nomeArquivo;
									    			
									    			System.out.println(caminhoFTP);
									    			
									    			boolean sucess = f.retrieveFile(caminhoFTP, outputStream);
									    		    if(sucess)
									    		    {
									    		        fileLog.write("Sucesso no download para pasta "+pastaArquivos+"" + "\r\n");
									    				fileLog.write("Termino do download " + horaDW + "\r\n");
									    				
									    				logIndividual.write("Sucesso no download\r\n");
									    				logIndividual.write("Termino do download " + horaDW + "\r\n");
									    		    }
									    		    else
									    		    {
									    				fileLog.write("Falla en el download carpeta "+pastaArquivos+"" + "\r\n");
									    				fileLog.write("Termino da falha no download " + horaDW + "\r\n");
									    				
									    				logIndividual.write("Termino no download\r\n");
									    				logIndividual.write("Falla en el download carpeta "+pastaArquivos+"" + "\r\n");
									    		    }
									    		        
									    			if (outputStream != null)
									    				outputStream.close();
									    			
								    				data = new Date();
								    				
								    				horaDW = new SimpleDateFormat("HH:mm:ss").format(data);
								    				
									    			if(arquivoA)
									    			{
									    				if(agendaMovimentacao.obterId() == 0)
									    				{
									    					agendaMovimentacao.atribuirResponsavel(usuarioAtual);
									    					agendaMovimentacao.atribuirDescricao("Archivo Ftp - " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
									    					agendaMovimentacao.atribuirDestino(destino);
									    					agendaMovimentacao.atribuirTitulo(mesArquivo +" - " + anoArquivo);
									    					agendaMovimentacao.atribuirDataPrevistaInicio(new Date());
									    					agendaMovimentacao.atribuirAnoMovimento(Integer.valueOf(anoArquivo));
									    					agendaMovimentacao.atribuirMesMovimento(Integer.valueOf(mesArquivo));
									    					agendaMovimentacao.incluir();
									    					
									    					agendaMovimentacao.atualizarValidacao("Total");
								    						agendaMovimentacao.atualizarFase(AgendaMovimentacao.AGENDADA);
									    				}
									    				else
									    				{
									    					String descricaoAntiga = agendaMovimentacao.obterDescricao();
									    					descricaoAntiga+="\n"+"Archivo Ftp - " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
									    					agendaMovimentacao.atualizarDescricao(descricaoAntiga);
									    					
									    					if(!agendaMovimentacao.obterFase().obterCodigo().equals(AgendaMovimentacao.AGENDADA))
									    						agendaMovimentacao.atualizarFase(AgendaMovimentacao.AGENDADA);
									    				}
									    				
									    				agendaMovimentacao.atualizarEspecial("Sim");
							    						agendaMovimentacao.atualizarInscricaoEspecial("Sim");
								    					agendaMovimentacao.atualizarSuplementosEspecial("Sim");
								    					agendaMovimentacao.atualizarCapitalEspecial("Sim");
								    					agendaMovimentacao.atualizarDataEspecial("Sim");
								    					agendaMovimentacao.atualizarDocumentoEspecial("Sim");
								    					agendaMovimentacao.atualizarApAnteriorEspecial("Sim");
									    			}
							    					
							    					sucess = f.deleteFile(caminhoFTP);
									    			
									    			if(sucess)
									    			{
									    				fileLog.write("Archivo Excluido del FTP con exito" + "\r\n");
									    				logIndividual.write("Archivo Excluido del FTP con exito" + "\r\n");
									    			}
									    			else
									    			{
									    				fileLog.write("Falla en exclusión del archivo en el FTP" + "\r\n");
									    				logIndividual.write("Falla en exclusión del archivo en el FTP " + caminhoFTP + "\r\n");
									    			}
									    			
									    			if(arquivoA)
									    			{
										    			fileLog.write("Agendada con exito\r\n");
										    			fileLog.write("\r\n");
										    			
										    			logIndividual.write("Agendada con exito\r\n");
										    			logIndividual.write("\r\n");
									    			}
						    					}
							    			}
							    			else
							    			{
							    				fileLog.write("No encontró Aseguradora con sigla " + siglaArquivo + "\r\n");
							    				fileLog.write("\r\n");
							    				
							    				logIndividual.write("No encontró Aseguradora con sigla " + siglaArquivo + "\r\n");
							    				logIndividual.write("\r\n");
							    				
							    				this.arquivosErroSFTP.put(arquivoErro, arquivoErro);
							    			}
						    			}
				    				}
					    			
					    			logIndividual.close();
				    				
				    				try
					    			{
				    					 FileInputStream f2 = new FileInputStream(caminhoArquivo);
				    					 f.changeWorkingDirectory(pastaFTP);
				    					 
				    					 System.out.println(pastaFTP);
				    					 
				    					 boolean sucess = f.storeFile(pastaFTP, f2);
				    					 
					    		        if(sucess)
					    		            fileLog.write("Archivo " + caminhoArquivo + ", enviado con exito para el FTP\r\n");
					    		        else
					    		        	fileLog.write("Falla al enviar el Archivo " + caminhoArquivo + " para el FTP\r\n");
					    			}
					    			catch(Exception e)
					    			{
					    				fileLog.write("Error interno al enviar el Archivo " + caminhoArquivo + " para el FTP\r\n");
					    				fileLog.write(e.getMessage()+"\r\n");
					    			}
			    				}
			    			}
		    				
		    				//ARQUIVOS CONTABEIS
		    				FTPFile[] filesContabeil = f.listFiles(diretorio+nomePasta+diretorioContabil);
		    				
		    				for(int j = 0 ; j < filesContabeil.length ; j++)
			    			{
		    					FTPFile arquivo = filesContabeil[j];
		    					String nomeArquivo = arquivo.getName();
		    					
		    					if(!nomeArquivo.equals(".") && !nomeArquivo.equals(".."))
			    				{
			    					//Fazer isso, pq alguns arquivos com a extensão txt em maiusculo
			    					String arquivoAux = nomeArquivo.toLowerCase();
			    					
			    					String nomeArquivoAux = arquivoAux.substring(0, arquivoAux.indexOf(".txt"));
			    					
			    					String nomeArquivoFTP = "Log_"+nomeArquivoAux+"_" + new Date().getTime() + ".txt";
	
					    			String caminhoArquivo = "C:/Logs/"+nomeArquivoFTP;
					    			
					    			String pastaFTP = diretorio+nomePasta+diretorioLog+"/"+nomeArquivoFTP;
					    			String pastaFTP2 = diretorio+nomePasta+diretorioLog;
						    			
					    			//Fazer isso pra só pegar arquivos contabeis
					    			if(arquivoAux.indexOf(".txt")!=-1 && arquivoAux.length() == 13)
					    			{
					    				FileWriter logIndividual = new FileWriter(caminhoArquivo);
					    				
					    				fileLog.write("***** Archivo "+nomeArquivo+" *****\r\n");
					    				logIndividual.write("***** Archivo "+nomeArquivo+" *****\r\n");
					    				
					    				String tipoArquivo = "Contabil";
					    				
					    				String siglaArquivo = nomeArquivo.substring(0, 3);
							    		
							    		arquivoErro = nomeArquivo;
							    		
							    		String caminhoFTP = diretorio+nomePasta+diretorioContabil+"/"+nomeArquivo;
							    		
							    		if(!arquivosErroSFTP.containsKey(arquivoErro))
							    		{
								    		Entidade aseguradora = home.obterEntidadePorSigla(siglaArquivo);
								    		boolean agendaConcluida = false;
								    		
								    		if(aseguradora != null)
								    		{
								    			String anoArquivo = nomeArquivo.substring(3,7);
								    			String mesArquivo = nomeArquivo.substring(7,9);
								    			
								    			AgendaMovimentacao agendaMovimentacao = agendaHome.obterAgendaNoPeriodo(Integer.valueOf(mesArquivo), Integer.valueOf(anoArquivo), aseguradora, tipoArquivo);
								    				
								    			if(agendaMovimentacao == null)
								    			{
								    				agendaMovimentacao = (AgendaMovimentacao) this.getModelManager().getEntity("AgendaMovimentacao");
								    				agendaMovimentacao.atribuirOrigem(aseguradora);
									    			agendaMovimentacao.atribuirTipo(tipoArquivo);
								    			}
								    			else if(agendaMovimentacao.obterFase().obterCodigo().equals(AgendaMovimentacao.EVENTO_CONCLUIDO))
								    			{
								    				fileLog.write("Archivo já foi processado com exito " + nomeArquivo + "\r\n");
								    				logIndividual.write("Archivo já foi processado com exito " + nomeArquivo + "\r\n");
								    				
								    				f.deleteFile(caminhoFTP);
								    				
								    				agendaConcluida = true;
								    			}
								    			
								    			if(!agendaConcluida)
								    			{
								    				boolean agendaPendente = false;
								    				
							    					Collection agendas = aseguradora.obterAgendas(tipoArquivo);
							    					
							    					for(Iterator k = agendas.iterator() ; k.hasNext() ; )
							    					{
							    						AgendaMovimentacao agenda = (AgendaMovimentacao) k.next();
							    						
					    								if(agenda.obterFase().obterCodigo().equals(AgendaMovimentacao.EVENTO_PENDENTE) && agenda.obterId()!=agendaMovimentacao.obterId())
					    								{
					    									fileLog.write("Agenda pendiente, id: " + agenda.obterId() + "\r\n");
					    									fileLog.write("agendaMovimentacao pendiente, id: " + agendaMovimentacao.obterId() + "\r\n");
					    									
					    									fileLog.write("Existe Agenda Pendiente " + agenda.obterMesMovimento() + "/" + agenda.obterAnoMovimento() + ", Aseguradora " +aseguradora.obterNome() +  "\r\n");
					    									fileLog.write("El archivo " + agenda.obterMesMovimento() + "/" + agenda.obterAnoMovimento() +" no fue encontrado en FTP\r\n");
					    									
					    									logIndividual.write("Existe Agenda Pendiente " + agenda.obterMesMovimento() + "/" + agenda.obterAnoMovimento() + ", Aseguradora " +aseguradora.obterNome() +  "\r\n");
					    									logIndividual.write("El archivo " + agenda.obterMesMovimento() + "/" + agenda.obterAnoMovimento() +" no fue encontrado en FTP\r\n");
					    									
					    									this.arquivosErroSFTP.put(arquivoErro, arquivoErro);
					    									agendaPendente = true;
					    									break;
					    								}
							    					}
								    				
							    					if(!agendaPendente)
							    					{
							    						data = new Date();
										    			
										    			String horaDW = new SimpleDateFormat("HH:mm:ss").format(data);
										    			
										    			fileLog.write("Inicio do download " + horaDW + "\r\n");
										    			//logIndividual.write("Inicio do download " + horaDW + "\r\n");
										    			
										    			File downloadFile = new File(pastaArquivos+nomeArquivo);
										    			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
										    			
										    			//String caminhoFTP = diretorio+nomePasta+diretorioContabil+"/"+nomeArquivo;
										    			
										    			System.out.println(caminhoFTP);
										    			
										    			boolean sucess = f.retrieveFile(caminhoFTP, outputStream);
										    		    if(sucess)
										    		    {
										    		        fileLog.write("Exito en el download carpeta "+pastaArquivos+"" + "\r\n");
										    				fileLog.write("Terminó el download " + horaDW + "\r\n");
										    				
										    				//logIndividual.write("Exito en el download\r\n");
										    				//logIndividual.write("Terminó el download " + horaDW + "\r\n");
										    		    }
										    		    else
										    		    {
										    				fileLog.write("Falla en el download para pasta "+pastaArquivos+"" + "\r\n");
										    				fileLog.write("Falla en el download " + horaDW + "\r\n");
										    				
										    				logIndividual.write("Falla en el download " + horaDW + "\r\n");
										    		    }
										    		    
										    			if(outputStream != null)
										    				outputStream.close();
										    			
									    				data = new Date();
									    				
									    				horaDW = new SimpleDateFormat("HH:mm:ss").format(data);
									    				
									    				if(agendaMovimentacao.obterId() == 0)
									    				{
									    					agendaMovimentacao.atribuirResponsavel(usuarioAtual);
									    					agendaMovimentacao.atribuirDescricao("Archivo Ftp - " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
									    					agendaMovimentacao.atribuirDestino(destino);
									    					agendaMovimentacao.atribuirTitulo(mesArquivo +" - " + anoArquivo);
									    					agendaMovimentacao.atribuirDataPrevistaInicio(new Date());
									    					agendaMovimentacao.atribuirAnoMovimento(Integer.valueOf(anoArquivo));
									    					agendaMovimentacao.atribuirMesMovimento(Integer.valueOf(mesArquivo));
									    					agendaMovimentacao.incluir();
									    					
									    					agendaMovimentacao.atualizarValidacao("Total");
									    					
								    						this.validarArquivosContabeisFTP(agendaMovimentacao, home, fileLog, arquivoAux, host, user, pass, diretorio, diretorioContabil, pastaFTP2, f);
									    				}
									    				else
									    				{
									    					String descricaoAntiga = agendaMovimentacao.obterDescricao();
									    					descricaoAntiga+="\n"+"Archivo Ftp - " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
									    					agendaMovimentacao.atualizarDescricao(descricaoAntiga);
									    					
									    					this.validarArquivosContabeisFTP(agendaMovimentacao, home, fileLog, arquivoAux, host, user, pass, diretorio, diretorioContabil, pastaFTP2, f);
									    				}
								    					
									    				sucess = f.deleteFile(caminhoFTP);
										    			
										    			if(sucess)
										    			{
										    				fileLog.write("Archivo Excluido do FTP com sucesso" + "\r\n");
										    				logIndividual.write("Archivo Excluido do FTP com sucesso" + "\r\n");
										    			}
										    			else
										    			{
										    				fileLog.write("Falla na exclusão do archivo no FTP" + "\r\n");
										    				logIndividual.write("Falla na exclusão do archivo no FTP " + caminhoFTP + "\r\n");
										    			}
									    			}
								    			}
								    		}
								    		else
								    		{
								    			fileLog.write("No encontró Aseguradora con sigla " + siglaArquivo + "\r\n");
							    				fileLog.write("\r\n");
							    				
							    				logIndividual.write("No encontró Aseguradora con sigla " + siglaArquivo + "\r\n");
							    				logIndividual.write("\r\n");
								    		}
							    		}
						    				
							    		logIndividual.close();
					    			}
				    				try
					    			{
				    					 FileInputStream f2 = new FileInputStream(caminhoArquivo);
				    					 f.changeWorkingDirectory(pastaFTP);
				    					 
				    					 System.out.println(pastaFTP);
				    					 
				    					 boolean sucess = f.storeFile(pastaFTP, f2);
				    					 
					    		        if(sucess)
					    		            fileLog.write("Archivo " + caminhoArquivo + ", enviado con exito en el FTP\r\n\r\n");
					    		        else
					    		        	fileLog.write("Falla al enviar el Archivo " + caminhoArquivo + " para el FTP\r\n\r\n");
					    			}
					    			catch(Exception e)
					    			{
					    				fileLog.write("Error interno al enviar el Archivo " + caminhoArquivo + " para el FTP\r\n");
					    				fileLog.write(e.getMessage()+"\r\n");
					    			}
				    			}
			    			}
		    				
		    				//LIVROS
		    				FTPFile[] filesLivros = f.listFiles(diretorio+nomePasta+diretorioLivros);
				    		
				    		for(int j = 0 ; j < filesLivros.length ; j++)
				    		{
				    			FTPFile arquivo = filesContabeil[j];
		    					String nomeArquivo = arquivo.getName();
		    					
		    					if(!nomeArquivo.equals(".") && !nomeArquivo.equals(".."))
			    				{
					    			String tipo = nomeArquivo.substring(0,1);
					            	
					    			//PRA NÃO CONFUNDIR COM DIRETORIO
					            	if(tipo.toLowerCase().equals("l"))
					            	{
					            		fileLog.write("***** Libro "+nomeArquivo+" *****\r\n");
					            		
						            	String sigla = nomeArquivo.substring(1,4); 
						            	
					            		arquivoErro = nomeArquivo;
					            		
					            		if(!arquivosErroSFTP.containsKey(arquivoErro))
						    			{
					            			data = new Date();
							    			
							    			String horaDW = new SimpleDateFormat("HH:mm:ss").format(data);
							    			
							    			fileLog.write("Inicio do download " + horaDW + "\r\n");
							    			
							    			String caminhoFTP = diretorio+nomePasta+diretorioLivros+"/"+nomeArquivo;
							    			
							    			File downloadFile = new File(pastaLivros+nomeArquivo);
							    			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
							    			
							    			boolean sucess = f.retrieveFile(caminhoFTP, outputStream);
							    		    if(sucess)
							    		    {
							    		        fileLog.write("Exito en el download carpeta "+pastaArquivos+"" + "\r\n");
							    				fileLog.write("Terminó el download " + horaDW + "\r\n");
							    				
							    				//logIndividual.write("Exito en el download\r\n");
							    				//logIndividual.write("Terminó el download " + horaDW + "\r\n");
							    		    }
							    		    else
							    		    {
							    				fileLog.write("Falla en el download para pasta "+pastaArquivos+"" + "\r\n");
							    				fileLog.write("Falla en el download " + horaDW + "\r\n");
							    		    }
							    		    
							    			if(outputStream != null)
							    				outputStream.close();
							    			
						            		String codLivro = nomeArquivo.substring(4,13);
							            	
							            	int ano = Integer.parseInt(nomeArquivo.substring(13,17));
							            	
							            	int mes = Integer.parseInt(nomeArquivo.substring(17,19));
							            	
							            	String extensao = nomeArquivo.substring(20,nomeArquivo.length());
							            	
							            	File arquivoLivro = new File(pastaLivros+nomeArquivo);
							            	
							            	String mimeType = Files.probeContentType(arquivoLivro.toPath());
							            	
							            	if(mimeType == null)
							            	{
							            		if(extensao.startsWith("xl"))
							            			mimeType = "application/vnd.ms-excel";
							            		else if(extensao.startsWith("pd"))
							            			mimeType = "application/pdf";
							            		else if(extensao.startsWith("do"))
							            			mimeType = "application/msword";
							            	}
							            	
							            	System.out.println(sigla + " " + codLivro + " " + ano + " " + mes + " " + extensao + " " + mimeType);
							            	
							            	Aseguradora aseg = (Aseguradora) home.obterEntidadePorSigla(sigla);
							            	if(aseg!=null)
							            	{
							            		//System.out.println(aseg.obterNome());
							            		String tipoLivro = uteis.obterNomeArquivo2(codLivro);
							            		
							            		Livro livro = livroHome.obterLivro(aseg, tipoLivro, mes, ano);
							    				if(livro == null)
							    				{
								            		livro = (Livro) mm.getEntity("Livro");
								            		livro.atribuirTitulo(tipoLivro);
								            		livro.atribuirTipo(tipoLivro);
								    				livro.atribuirMes(mes);
								    				livro.atribuirAno(ano);
								    				livro.atribuirOrigem(aseg);
								    				livro.incluir();
								    				
								    				livro.atualizarFase(Evento.EVENTO_CONCLUIDO);
							    				}
							    				
							    				Collection<UploadedFile> filesGravadas = upHome.getAllUploadedFiles(livro);
							    				Collection<UploadedFile> filesExcluir = new ArrayList<UploadedFile>();
							    				
							    				for(Iterator<UploadedFile> k = filesGravadas.iterator() ; k.hasNext() ; )
							    				{
							    					UploadedFile up = k.next();
							    					
							    					if(up.getName().equals(nomeArquivo))
							    						filesExcluir.add(up);
							    				}
							    				
							    				for(Iterator<UploadedFile> k = filesExcluir.iterator() ; k.hasNext() ; )
							    				{
							    					UploadedFile up = k.next();
							    					
							    					upHome.removeUploadedFile(up.getId());
							    				}
							    				
						    					InputStream input = new FileInputStream(pastaLivros+nomeArquivo);
						    					upHome.addUploadedFile(livro, input, nomeArquivo, mimeType, input.available(), 0);
						    					input.close();
						    					
						    					fileLog.write("Agregou Libro " + livro.obterTitulo() + " - " + livro.obterId() + "\r\n");
							            	}
							            	
							            	 InputStream is = new FileInputStream(pastaLivros+nomeArquivo);
							                 OutputStream os = new FileOutputStream("C:/Aseguradoras/backup_libros/" + nomeArquivo);
							                 byte buffer[] = new byte[is.available()];
							                 is.read(buffer);
							                 os.write(buffer);
							                 
							                 if(is != null)
							                     is.close();
							                 if(os != null)
							                     os.close();
							            	
							                 arquivoLivro.delete();
							                 fileLog.write("Libro excluido con exito "+pastaLivros+ "\r\n");
							                 
							                 sucess = f.deleteFile(caminhoFTP);
								    			
							    			if(sucess)
							    				fileLog.write("Libro excluido do FTP con exito" + "\r\n");
							    			else
							    				fileLog.write("Falla na exclusión del Libro no FTP" + "\r\n");
					    				}
				    				}
				            	}
				    		}
	    				}
	    			}
		    	}
	    		else
	    		{
	    			System.out.println("Não conectou ao FTP " + host);
	    			fileLog.write("Não conectou ao FTP " + host);
	    		}
	    		
	    		f.logout();
		    	f.disconnect();
	    	}
	    	catch (Exception e)
	    	{
	    		fileLog.write(e.getMessage() + ", ver senão é preciso desabilitar o Firewall do Windows\r\n");
	    		fileLog.close();
	    		System.out.println(e.getMessage());
	    		throw new Exception(e.getMessage());
			}
	    	
	    	fileLog.close();
	    }
	    
	    private Collection<Long> agendasContabeisErros;
	    
	    private void validarArquivosContabeisSFTP(AgendaMovimentacao agendaMovimentacao, EntidadeHome entidadeHome, FileWriter fileLog, String nomeArquivo, String host, String user, String pass, String diretorioRaiz, String diretorioArquivos, String pastaFTP) throws Exception
	    {
	    	Collection validacaoErros;
	    	if(agendasContabeisErros == null)
	    		 agendasContabeisErros = new ArrayList<Long>();
	    	
	    	AgendaMovimentacao agendaAtual = null;
	    	
	    	try
	    	{
    			agendaAtual = agendaMovimentacao;
    			
    			String sigla = agendaMovimentacao.obterOrigem().obterSigla();
    			
    			validacaoErros = new ArrayList();
    			
    			String descricaoFTP = "";
    			
    			String nomeArquivoAux = nomeArquivo.substring(0, nomeArquivo.indexOf(".txt"));
    			
    			String caminhoArquivo = "C:/tmp/Log_"+nomeArquivoAux+"_"+new Date().getTime()+".txt";
    			
    			FileWriter fileFTP = new FileWriter(caminhoArquivo);
    			
    			if(!agendasContabeisErros.contains(agendaMovimentacao.obterId()))
    			{
		    		String ano2 = new Integer(agendaMovimentacao.obterAnoMovimento()).toString();
		    		String mes2 = new Integer(agendaMovimentacao.obterMesMovimento()).toString();
	
					if (mes2.length() == 1)
						mes2 = "0" + mes2;
			
					validacaoErros = agendaMovimentacao.validaArquivo(sigla.trim()+ ano2 + mes2);
					
					if (validacaoErros.size() > 0) 
					{
						Entidade destino = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("planodecontas");
						String msgErros = "";

						System.out.println("Erros: " + validacaoErros.size());

						for (Iterator j = validacaoErros.iterator(); j.hasNext();)
						{
							String msgAux = (String) j.next();
							msgErros += msgAux + "\n";
						}

						System.out.println("Erros2: ");

						Parametro parametro = (Parametro) entidadeHome.obterEntidadePorApelido("parametros");
						String descricao = parametro.obterAtributo("notificacaocritica").obterValor();

						Notificacao notificacao = (Notificacao) this.getModelManager().getEntity("Notificacao");
						notificacao.atribuirSuperior(agendaMovimentacao);
						notificacao.atribuirOrigem(agendaMovimentacao.obterOrigem());
						notificacao.atribuirDestino(destino);
						notificacao.atribuirResponsavel(agendaMovimentacao.obterResponsavel());
						notificacao.atribuirTipo("Notificación de Error de Validación");
						notificacao.atribuirTitulo("Notificación de Error de Validación");
						notificacao.atribuirDescricao(descricao + "\n" + msgErros);
						notificacao.incluir();
						
						descricaoFTP = descricao + "\r\n" + msgErros;

						for (Iterator j = agendaMovimentacao.obterInferiores().iterator(); j.hasNext();)
						{
							Evento e = (Evento) j.next();
							if (e instanceof Notificacao && e.obterId() != notificacao.obterId())
							{
								if (e.obterTipo().equals("Notificación de Error de Validación"))
									e.atualizarFase(Evento.EVENTO_CONCLUIDO);
							}
						}

						agendaMovimentacao.atualizarFase(Evento.EVENTO_PENDENTE);
					} 
					else
					{

						Parametro parametro = (Parametro) entidadeHome.obterEntidadePorApelido("parametros");
						String descricao = parametro.obterAtributo("notificacaorecebimento").obterValor();

						Entidade destino = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("planodecontas");
						Notificacao notificacao = (Notificacao) this.getModelManager().getEntity("Notificacao");
						notificacao.atribuirSuperior(agendaMovimentacao);
						notificacao.atribuirOrigem(agendaMovimentacao.obterOrigem());
						notificacao.atribuirDestino(destino);
						notificacao.atribuirResponsavel(agendaMovimentacao.obterResponsavel());
						notificacao.atribuirTipo("Notificación de Recibimiento");
						notificacao.atribuirTitulo("Notificación de Recibimiento");
						notificacao.atribuirDescricao(descricao);
						notificacao.incluir();
						
						descricaoFTP = descricao;

						for (Iterator j = agendaMovimentacao.obterInferiores().iterator(); j.hasNext();)
						{
							Evento e = (Evento) j.next();
							e.atualizarFase(Evento.EVENTO_CONCLUIDO);
						}

						agendaMovimentacao.atualizarFase(Evento.EVENTO_CONCLUIDO);
						
						agendaMovimentacao.atualizaUltimaAgenda("Contabil");

						AgendaMovimentacao novaAgenda = (AgendaMovimentacao) this.getModelManager().getEntity("AgendaMovimentacao");
						novaAgenda.atribuirOrigem(agendaMovimentacao.obterOrigem());
						novaAgenda.atribuirDestino(agendaMovimentacao.obterDestino());
						novaAgenda.atribuirResponsavel(agendaMovimentacao
								.obterResponsavel());
						novaAgenda.atribuirTitulo(" (Consistencia del archivo)");

						Calendar mesSeguinte = Calendar.getInstance();
						mesSeguinte.setTime(new Date());
						mesSeguinte.add(Calendar.MONTH, 1);

						String dia = parametro.obterAtributo("diaagenda").obterValor();

						String mes = new SimpleDateFormat("MM").format(mesSeguinte.getTime());
						String ano = new SimpleDateFormat("yyyy").format(mesSeguinte.getTime());

						String data = dia + "/" + mes + "/" + ano;

						Date dataModificada = new SimpleDateFormat("dd/MM/yyyy").parse(data);

						int mesMovimento = agendaMovimentacao.obterMesMovimento() + 1;
						int anoMovimento = agendaMovimentacao.obterAnoMovimento();

						if (mesMovimento > 12)
						{
							mesMovimento = 1;
							anoMovimento += 1;
						}

						novaAgenda.atribuirDataPrevistaInicio(dataModificada);
						novaAgenda.atribuirTipo(agendaMovimentacao.obterTipo());
						novaAgenda.atribuirMesMovimento(mesMovimento);
						novaAgenda.atribuirAnoMovimento(anoMovimento);
						novaAgenda.incluir();

						String mesModificado = "";

						if (new Integer(mesMovimento).toString().length() == 1)
							mesModificado = "0" + mesMovimento;
						else
							mesModificado = new Integer(mesMovimento).toString();

						novaAgenda.atualizarTitulo(mesModificado + " - " + anoMovimento	+ " (Consistencia del archivo)");
					}
					
					fileFTP.write(descricaoFTP);
	    			fileFTP.close();
	    			
	    			try
	    			{
	    				int uploadStatus = SFTP.sendFile(caminhoArquivo, pastaFTP, host, user, pass);
	    		        if(FileTransferStatus.SUCCESS == uploadStatus){
	    		            fileLog.write("Archivo " + caminhoArquivo + ", enviado com sucesso para o FTP\r\n");
	    		        }
	    		        else if(FileTransferStatus.FAILURE == uploadStatus){
	    		        	fileLog.write("Falha ao enviar o Archivo " + caminhoArquivo + " para o FTP\r\n");
	    		        }
	    			}
	    			catch(FileTransferException e)
	    			{
	    				fileLog.write("Erro interno ao enviar o Archivo " + caminhoArquivo + " para o FTP\r\n");
	    				fileLog.write(e.getMessage()+"\r\n");
	    			}
	    		}
	    	}
	    	catch (Exception exception)
			{
				this.agendasContabeisErros.add(agendaAtual.obterId());
				System.out.println(exception.getMessage());
			}
	    }
	    
	    private void validarArquivosContabeisFTP(AgendaMovimentacao agendaMovimentacao, EntidadeHome entidadeHome, FileWriter fileLog, String nomeArquivo, String host, String user, String pass, String diretorioRaiz, String diretorioArquivos, String diretorioFTP, FTPClient f) throws Exception
	    {
	    	Collection validacaoErros;
	    	if(agendasContabeisErros == null)
	    		 agendasContabeisErros = new ArrayList<Long>();
	    	
	    	AgendaMovimentacao agendaAtual = null;
	    	
	    	try
	    	{
    			agendaAtual = agendaMovimentacao;
    			
    			String sigla = agendaMovimentacao.obterOrigem().obterSigla();
    			
    			validacaoErros = new ArrayList();
    			
    			String descricaoFTP = "";
    			
    			String nomeArquivoAux = nomeArquivo.substring(0, nomeArquivo.indexOf(".txt"));
    			
    			String nomeArquivoFTP = "Log_"+nomeArquivoAux+"_"+new Date().getTime()+".txt";
    			
    			String caminhoArquivo = "C:/tmp/"+nomeArquivoFTP;
    			
    			FileWriter fileFTP = new FileWriter(caminhoArquivo);
    			
    			if(!agendasContabeisErros.contains(agendaMovimentacao.obterId()))
    			{
		    		String ano2 = new Integer(agendaMovimentacao.obterAnoMovimento()).toString();
		    		String mes2 = new Integer(agendaMovimentacao.obterMesMovimento()).toString();
	
					if (mes2.length() == 1)
						mes2 = "0" + mes2;
			
					validacaoErros = agendaMovimentacao.validaArquivo(sigla.trim()+ ano2 + mes2);
					
					if (validacaoErros.size() > 0) 
					{
						Entidade destino = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("planodecontas");
						String msgErros = "";

						System.out.println("Erros: " + validacaoErros.size());

						for (Iterator j = validacaoErros.iterator(); j.hasNext();)
						{
							String msgAux = (String) j.next();
							msgErros += msgAux + "\n";
						}

						System.out.println("Erros2: ");

						Parametro parametro = (Parametro) entidadeHome.obterEntidadePorApelido("parametros");
						String descricao = parametro.obterAtributo("notificacaocritica").obterValor();

						Notificacao notificacao = (Notificacao) this.getModelManager().getEntity("Notificacao");
						notificacao.atribuirSuperior(agendaMovimentacao);
						notificacao.atribuirOrigem(agendaMovimentacao.obterOrigem());
						notificacao.atribuirDestino(destino);
						notificacao.atribuirResponsavel(agendaMovimentacao.obterResponsavel());
						notificacao.atribuirTipo("Notificación de Error de Validación");
						notificacao.atribuirTitulo("Notificación de Error de Validación");
						notificacao.atribuirDescricao(descricao + "\n" + msgErros);
						notificacao.incluir();
						
						descricaoFTP = descricao + "\r\n" + msgErros;

						for (Iterator j = agendaMovimentacao.obterInferiores().iterator(); j.hasNext();)
						{
							Evento e = (Evento) j.next();
							if (e instanceof Notificacao && e.obterId() != notificacao.obterId())
							{
								if (e.obterTipo().equals("Notificación de Error de Validación"))
									e.atualizarFase(Evento.EVENTO_CONCLUIDO);
							}
						}

						agendaMovimentacao.atualizarFase(Evento.EVENTO_PENDENTE);
					} 
					else
					{
						Parametro parametro = (Parametro) entidadeHome.obterEntidadePorApelido("parametros");
						String descricao = parametro.obterAtributo("notificacaorecebimento").obterValor();

						Entidade destino = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("planodecontas");
						Notificacao notificacao = (Notificacao) this.getModelManager().getEntity("Notificacao");
						notificacao.atribuirSuperior(agendaMovimentacao);
						notificacao.atribuirOrigem(agendaMovimentacao.obterOrigem());
						notificacao.atribuirDestino(destino);
						notificacao.atribuirResponsavel(agendaMovimentacao.obterResponsavel());
						notificacao.atribuirTipo("Notificación de Recibimiento");
						notificacao.atribuirTitulo("Notificación de Recibimiento");
						notificacao.atribuirDescricao(descricao);
						notificacao.incluir();
						
						descricaoFTP = descricao;

						for (Iterator j = agendaMovimentacao.obterInferiores().iterator(); j.hasNext();)
						{
							Evento e = (Evento) j.next();
							e.atualizarFase(Evento.EVENTO_CONCLUIDO);
						}

						agendaMovimentacao.atualizarFase(Evento.EVENTO_CONCLUIDO);
						
						agendaMovimentacao.atualizaUltimaAgenda("Contabil");

						AgendaMovimentacao novaAgenda = (AgendaMovimentacao) this.getModelManager().getEntity("AgendaMovimentacao");
						novaAgenda.atribuirOrigem(agendaMovimentacao.obterOrigem());
						novaAgenda.atribuirDestino(agendaMovimentacao.obterDestino());
						novaAgenda.atribuirResponsavel(agendaMovimentacao
								.obterResponsavel());
						novaAgenda.atribuirTitulo(" (Consistencia del archivo)");

						Calendar mesSeguinte = Calendar.getInstance();
						mesSeguinte.setTime(new Date());
						mesSeguinte.add(Calendar.MONTH, 1);

						String dia = parametro.obterAtributo("diaagenda").obterValor();

						String mes = new SimpleDateFormat("MM").format(mesSeguinte.getTime());
						String ano = new SimpleDateFormat("yyyy").format(mesSeguinte.getTime());

						String data = dia + "/" + mes + "/" + ano;

						Date dataModificada = new SimpleDateFormat("dd/MM/yyyy").parse(data);

						int mesMovimento = agendaMovimentacao.obterMesMovimento() + 1;
						int anoMovimento = agendaMovimentacao.obterAnoMovimento();

						if (mesMovimento > 12)
						{
							mesMovimento = 1;
							anoMovimento += 1;
						}

						novaAgenda.atribuirDataPrevistaInicio(dataModificada);
						novaAgenda.atribuirTipo(agendaMovimentacao.obterTipo());
						novaAgenda.atribuirMesMovimento(mesMovimento);
						novaAgenda.atribuirAnoMovimento(anoMovimento);
						novaAgenda.incluir();

						String mesModificado = "";

						if (new Integer(mesMovimento).toString().length() == 1)
							mesModificado = "0" + mesMovimento;
						else
							mesModificado = new Integer(mesMovimento).toString();

						novaAgenda.atualizarTitulo(mesModificado + " - " + anoMovimento	+ " (Consistencia del archivo)");
					}
					
					fileFTP.write(descricaoFTP);
	    			fileFTP.close();
	    			
	    			try
	    			{
	    				 FileInputStream f2 = new FileInputStream(caminhoArquivo);
    					 f.changeWorkingDirectory(diretorioFTP);
    					 
    					 System.out.println(diretorioFTP+"/"+nomeArquivoFTP);
    					 
    					 boolean sucess = f.storeFile(diretorioFTP+"/"+nomeArquivoFTP, f2);
    					 
	    		        if(sucess)
	    		            fileLog.write("Archivo " + caminhoArquivo + ", enviado com sucesso para o FTP\r\n");
	    		        else
	    		        	fileLog.write("Falha ao enviar o Archivo " + caminhoArquivo + " para o FTP\r\n");
	    			}
	    			catch(Exception e)
	    			{
	    				fileLog.write("Erro interno ao enviar o Archivo " + caminhoArquivo + " para o FTP\r\n");
	    				fileLog.write(e.getMessage()+"\r\n");
	    			}
	    		}
	    	}
	    	catch (Exception exception)
			{
				this.agendasContabeisErros.add(agendaAtual.obterId());
				System.out.println(exception.getMessage());
			}
	    }
}
