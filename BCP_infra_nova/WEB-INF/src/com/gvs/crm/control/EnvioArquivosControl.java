package com.gvs.crm.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.fileupload.FileItem;
import org.apache.poi.util.IOUtils;
import org.apache.tika.Tika;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EnvioArquivos;
import com.gvs.crm.model.EnvioArquivosHome;
import com.gvs.crm.model.Raiz;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.RelEnvioArquivosView;

import infra.config.InfraProperties;
import infra.control.Action;
import infra.control.Control;

public class EnvioArquivosControl extends Control 
{
	private Entidade aseguradora;
	private EntidadeHome home;
	
	public void incluirEnvioArquivo(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		home = (EntidadeHome) mm.getHome("EntidadeHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		EnvioArquivos envio = (EnvioArquivos) mm.getEntity("EnvioArquivo");
		InfraProperties infra = InfraProperties.getInstance();
		mm.beginTransaction();
		//FileWriter writer = new FileWriter(infra.getProperty("report.temp.path")+"/Log_Envio_arquivo"+new Date().getTime()+".txt");
		try
		{
			Date dataAtual = new Date();
			
			envio.atribuirTitulo("Envio Archivo " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataAtual));
			envio.atribuirResponsavel(usuarioAtual);
			
			Collection<FileItem> files = action.getFiles("file");
			Map<String,InputStream> arquivoCertos = new TreeMap<>();
			ZipEntry entry;
			Tika tika = new Tika();
		    String mimeType,nomeArquivo,primeiraLetra,sigla,caminho;
		    ZipFile zipFile;
		    aseguradora = null;
		    
			if(files!=null)
			{
				for(FileItem fileItem : files)
				{
					mimeType = tika.detect(fileItem.getInputStream());
					
					if(mimeType.indexOf("zip") > -1)
					{
						nomeArquivo = fileItem.getName();
						
						if(nomeArquivo.lastIndexOf("/") > -1)
							nomeArquivo = nomeArquivo.substring(nomeArquivo.lastIndexOf("/")+1,nomeArquivo.length());
						if(nomeArquivo.lastIndexOf("\\") > -1)
							nomeArquivo = nomeArquivo.substring(nomeArquivo.lastIndexOf("\\")+1,nomeArquivo.length());
						
						caminho = infra.getProperty("report.temp.path") + "/" + nomeArquivo;
						
						IOUtils.copy(fileItem.getInputStream(), new FileOutputStream(caminho));
						zipFile = new ZipFile(caminho);
						
						Enumeration entries = zipFile.entries();
						
						while (entries.hasMoreElements()) 
						{
							entry = (ZipEntry) entries.nextElement();
							
							sigla = null;
							
							if(!entry.isDirectory())
							{
								nomeArquivo = entry.getName();
								
								if(nomeArquivo.lastIndexOf("/") > -1)
									nomeArquivo = nomeArquivo.substring(nomeArquivo.lastIndexOf("/")+1,nomeArquivo.length());
								if(nomeArquivo.lastIndexOf("\\") > -1)
									nomeArquivo = nomeArquivo.substring(nomeArquivo.lastIndexOf("\\")+1,nomeArquivo.length());
									
								primeiraLetra = nomeArquivo.substring(0, 1).toLowerCase();
								mimeType = tika.detect(zipFile.getInputStream(entry));
								//System.out.println(mimeType);
								
								if(mimeType.indexOf("xls") == -1 || mimeType.indexOf("pdf") == -1 || mimeType.indexOf("doc") == -1 || mimeType.indexOf("text") == -1 || mimeType.indexOf("excel") == -1 || mimeType.indexOf("word") == -1)
								{
									if(primeiraLetra.toLowerCase().equals("l"))
									{
										String extensao = nomeArquivo.substring(20,nomeArquivo.length());
										
										if(extensao.startsWith("xl"))
											mimeType = "application/vnd.ms-excel";
					            		else if(extensao.startsWith("pd"))
					            			mimeType = "application/pdf";
					            		else if(extensao.startsWith("do"))
					            			mimeType = "application/msword";
									}
									else if(primeiraLetra.toLowerCase().equals("a") || primeiraLetra.toLowerCase().equals("b"))
									{
										String extensao = nomeArquivo.substring(11,nomeArquivo.length());
										if(extensao.startsWith("txt"))
											mimeType = "text/plain";
									}
									else if(primeiraLetra.equals("0"))
									{
										String extensao = nomeArquivo.substring(10,nomeArquivo.length());
										if(extensao.startsWith("txt"))
											mimeType = "text/plain";
									}
								}
								
								if(mimeType.equals("text/plain") && nomeArquivo.indexOf("txt") > -1)
								{
									if(primeiraLetra.equals("a") || primeiraLetra.equals("b"))
									{
										try
										{
											sigla = nomeArquivo.substring(1,4);
										}
										catch (Exception exception)
										{
											throw new Exception("Nombre de lo Archivo no es válido " + nomeArquivo);
										}
										
										try
										{
											byte[] bytes = IOUtils.toByteArray(zipFile.getInputStream(entry));
											
											String DEFAULT_ENCODING = "WINDOWS-1252";
										    org.mozilla.universalchardet.UniversalDetector detector = new org.mozilla.universalchardet.UniversalDetector(null);
										    detector.handleData(bytes, 0, bytes.length);
										    detector.dataEnd();
										    String encoding = detector.getDetectedCharset();
										    detector.reset();
										    if (encoding == null) 
										        encoding = DEFAULT_ENCODING;
										    
										    System.out.println("Encoding " + encoding);
										    
										    if(encoding.indexOf("UTF") != -1)
										    	throw new Exception("Archivo "+nomeArquivo+" con el Encoding UTF-8. Encoding correcto es ANSI");
										}
										catch (Exception exception)
										{
											throw new Exception("Archivo "+nomeArquivo+" con el Encoding UTF-8. Encoding correcto es ANSI");
										}
										
									}
									else if(nomeArquivo.length() == 13)
									{
										try
										{
											sigla = nomeArquivo.substring(0,3);
										}
										catch (Exception exception)
										{
											throw new Exception("Nombre de lo Archivo no es válido " + nomeArquivo);
										}
									}
								}
								else if(mimeType.indexOf("excel") > -1 || mimeType.indexOf("word") > -1 || mimeType.indexOf("pdf") > -1)
								{
									if(primeiraLetra.equals("l"))
									{
										try
										{
											sigla = nomeArquivo.substring(1,4);
										}
										catch (Exception exception)
										{
											throw new Exception("Nombre de lo Archivo no es válido " + nomeArquivo);
										}
									}
								}
								
								if(sigla!=null)
								{
									try
									{
										Integer.valueOf(sigla);
									}
									catch (Exception exception)
									{
										throw new Exception("Sigla de lo Archivo no es numérica " + nomeArquivo);
									}
									
									try
									{
										if(!compararAseguradora(sigla))
											throw new Exception("Seguradoras diferentes en los Archivos");
											
										arquivoCertos.put(nomeArquivo, zipFile.getInputStream(entry));
									}
									catch (Exception exception)
									{
										throw new Exception("Seguradoras diferentes en los Archivos");
									}
								}
							}
								
						}
					}
					else
					{
						nomeArquivo = fileItem.getName();
						
						if(nomeArquivo.lastIndexOf("/") > -1)
							nomeArquivo = nomeArquivo.substring(nomeArquivo.lastIndexOf("/")+1,nomeArquivo.length());
						if(nomeArquivo.lastIndexOf("\\") > -1)
							nomeArquivo = nomeArquivo.substring(nomeArquivo.lastIndexOf("\\")+1,nomeArquivo.length());
						
						//writer.write("Nome do Arquivo:" + nomeArquivo);
						
						primeiraLetra = nomeArquivo.substring(0, 1).toLowerCase();
						
						//writer.write("\r\nprimeiraLetra:" + primeiraLetra);
						
						//writer.write("\r\nmimeType antes do primeiro if:" + mimeType);
						
						if(mimeType.indexOf("xls") == -1 && mimeType.indexOf("pdf") == -1 && mimeType.indexOf("doc") == -1 && mimeType.indexOf("text") == -1 && mimeType.indexOf("excel") == -1 && mimeType.indexOf("word") == -1)
						{
							if(primeiraLetra.toLowerCase().equals("l"))
							{
								String extensao = nomeArquivo.substring(20,nomeArquivo.length());
								
								if(extensao.startsWith("xl"))
									mimeType = "application/vnd.ms-excel";
			            		else if(extensao.startsWith("pd"))
			            			mimeType = "application/pdf";
			            		else if(extensao.startsWith("do"))
			            			mimeType = "application/msword";
								
								//writer.write("\r\nmimeType modificado primeiro if:" + mimeType);
							}
							else if(primeiraLetra.toLowerCase().equals("a") || primeiraLetra.toLowerCase().equals("b"))
							{
								String extensao = nomeArquivo.substring(11,nomeArquivo.length());
								if(extensao.startsWith("txt"))
									mimeType = "text/plain";
								
								//writer.write("\r\nmimeType modificado segundo if:" + mimeType);
							}
							else if(primeiraLetra.equals("0"))
							{
								String extensao = nomeArquivo.substring(10,nomeArquivo.length());
								if(extensao.startsWith("txt"))
									mimeType = "text/plain";
								
								//writer.write("\r\nmimeType modificado terceiro if:" + mimeType);
							}
						}
						
						if(mimeType.indexOf("xls") == -1 && mimeType.indexOf("pdf") == -1 && mimeType.indexOf("doc") == -1 && mimeType.indexOf("text") == -1 && mimeType.indexOf("excel") == -1 && mimeType.indexOf("word") == -1)
							throw new Exception("Archivos permitidos .zip, Excel, Word, .pdf, .txt");
						
						//writer.write("\r\nmimeType final:" + mimeType);
						
						sigla = null;
						
						//System.out.println(mimeType);
						
						if(mimeType.equals("text/plain") && nomeArquivo.indexOf("txt") > -1)
						{
							if(primeiraLetra.equals("a") || primeiraLetra.equals("b"))
							{
								try
								{
									sigla = nomeArquivo.substring(1,4);
									//writer.write("\r\nsigla primeiro try:" + sigla);
								}
								catch (Exception exception)
								{
									throw new Exception("Nombre de lo Archivo no es válido " + nomeArquivo);
								}
							}
							else if(nomeArquivo.length() == 13)
							{
								try
								{
									sigla = nomeArquivo.substring(0,3);
									//writer.write("\r\nsigla segundo try:" + sigla);
								}
								catch (Exception exception)
								{
									throw new Exception("Nombre de lo Archivo no es válido " + nomeArquivo);
								}
							}
							
							try
							{
								byte[] bytes = fileItem.get();
								
								String DEFAULT_ENCODING = "WINDOWS-1252";
							    org.mozilla.universalchardet.UniversalDetector detector = new org.mozilla.universalchardet.UniversalDetector(null);
							    detector.handleData(bytes, 0, bytes.length);
							    detector.dataEnd();
							    String encoding = detector.getDetectedCharset();
							    detector.reset();
							    if (encoding == null) 
							        encoding = DEFAULT_ENCODING;
							    
							    System.out.println("Encoding " + encoding);
							    
							    if(encoding.indexOf("UTF") != -1)
							    	throw new Exception("Archivo "+nomeArquivo+" con el Encoding UTF-8. Encoding correcto es ANSI");
							}
							catch (Exception exception)
							{
								throw new Exception("Archivo "+nomeArquivo+" con el Encoding UTF-8. Encoding correcto es ANSI");
							}
						}
						else if(mimeType.indexOf("excel") > -1 || mimeType.indexOf("word") > -1 || mimeType.indexOf("pdf") > -1)
						{
							if(primeiraLetra.equals("l"))
							{
								try
								{
									sigla = nomeArquivo.substring(1,4);
									//writer.write("\r\nsigla terceiro try:" + sigla);
								}
								catch (Exception exception)
								{
									throw new Exception("Nombre de lo Archivo no es válido " + nomeArquivo);
								}
							}
						}
						
						//writer.write("\r\nsigla final:" + sigla);
						
						if(sigla!=null)
						{
							try
							{
								Integer.valueOf(sigla);
							}
							catch (Exception exception)
							{
								throw new Exception("Sigla de lo Archivo no es numérica " + nomeArquivo);
							}
							
							try
							{
								if(!compararAseguradora(sigla))
									throw new Exception("Seguradoras diferentes en los Archivos");
									
								arquivoCertos.put(nomeArquivo, fileItem.getInputStream());
								//writer.write("\r\nput em arquivoCertos.put:" + nomeArquivo+"\r\n\r\n");
							}
							catch (Exception exception)
							{
								throw new Exception("Seguradoras diferentes en los Archivos");
							}
						}
						//else
							//writer.write("\r\nsigla == null:" + sigla);
					}
				}
			}
			else
				throw new Exception("Archivos en Blanco");
			
			//writer.write("\r\nsize arquivoCertos:" + arquivoCertos.size());
			
			if(arquivoCertos.size() == 0)
				throw new Exception("Los Archivos no pertenecen a Central de Información");
			
			caminho = infra.getProperty("arquivos.url");
			
			if(!new File(caminho).exists())
				throw new Exception("Carpeta del destino no se encuentro en el Servidor BCP");
			
			for(String nome : arquivoCertos.keySet())
			{
				try
				{
					System.out.println(caminho+"/"+nome);
					IOUtils.copy(arquivoCertos.get(nome), new FileOutputStream(caminho+"/"+nome));
					
					home.excluirArquivoFila(nome);
					//writer.write("\r\ncopiou arquivo:" + nome);
					
					envio.addArquivo(nome);
				}
				catch (Exception exception)
				{
					this.setAlert(Util.translateException(exception));
					this.setResponseView(new EventoView(envio));
				}
			}
			
			//writer.close();
			
			envio.atribuirOrigem(aseguradora);
			envio.incluir();
			
			envio.atualizarFase(EnvioArquivos.EVENTO_CONCLUIDO);
			
			mm.commitTransaction();
			this.setResponseView(new EventoView(envio));

		}
		catch (Exception exception)
		{
			//writer.write("\r\nErro: "+exception.getMessage());
			//writer.close();
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(envio));
			mm.rollbackTransaction();
		}
	}
	
	private boolean compararAseguradora(String sigla)
	{
		boolean retorno = false;
		try
		{
			if(aseguradora == null)
			{
				aseguradora = home.obterEntidadePorSigla(sigla);
				if(!(aseguradora instanceof Raiz))
					retorno = true;
				
			}
			else
			{
				if(!(home.obterEntidadePorSigla(sigla) instanceof Raiz))
				{
					if(aseguradora.obterId() == home.obterEntidadePorSigla(sigla).obterId())
						retorno = true;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	public void relArquivosEnviados(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EnvioArquivosHome home = (EnvioArquivosHome) mm.getHome("EnvioArquivosHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		
		Collection<EnvioArquivos> envios = new ArrayList<>();
		Date dataInicio = null,dataFim = null;
		Entidade aseguradora = null;
		Usuario usuario = null;
		
		try
		{
			if(!action.getBoolean("view"))
			{
				dataInicio = action.getDate("dataInicio");
				dataFim = action.getDate("dataFim");
				if(dataFim!=null)
				{
					String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
					dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				}
				
				boolean admin = usuarioAtual.obterId() == 1;
				if(admin)
				{
					long aseguradoraId = action.getLong("aseguradoraId");
					long usuarioId = action.getLong("usuarioId");
					
					if(aseguradoraId > 0)
						aseguradora = entidadeHome.obterEntidadePorId(aseguradoraId);
					if(usuarioId > 0)
						usuario = (Usuario) entidadeHome.obterEntidadePorId(usuarioId);
				}
				else
					usuario = usuarioAtual;
				
				envios = home.obterArquivos(aseguradora, dataInicio, dataFim, usuario);
			}
			
			this.setResponseView(new RelEnvioArquivosView(aseguradora, dataInicio, dataFim, envios, usuario));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new RelEnvioArquivosView(aseguradora, dataInicio, dataFim, envios, usuario));
		}
	}
}
