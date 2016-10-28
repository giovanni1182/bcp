package com.gvs.crm.control;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.util.IOUtils;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.CodigoInstrumento;
import com.gvs.crm.model.CodigoInstrumentoHome;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.model.Uteis;
import com.gvs.crm.view.CodigosInstrumentoView;

import infra.config.InfraProperties;
import infra.control.Action;
import infra.control.Control;

public class CodigoInstrumentoControl extends Control 
{
	public void relCodigosInstrumento(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		CodigoInstrumentoHome codHome = (CodigoInstrumentoHome) mm.getHome("CodigoInstrumentoHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this.getUser());
		Collection<CodigoInstrumento> codigos = codHome.obterCodigosInstrumento();
		int codigo = 0;
		String descricao = "";
		
		mm.beginTransaction();
		try 
		{
			if(action.getBoolean("incluir"))
			{
				Uteis uteis = new Uteis();
				
				descricao = action.getString("descricao").trim();
				
				CodigoInstrumento codIntrumento = (CodigoInstrumento) mm.getEntity("CodigoInstrumento");
				
				String codigoStr = action.getString("codigo");
				if(uteis.eNumero(codigoStr))
					codigo = Integer.valueOf(codigoStr);
				else
					throw new Exception("Código del Instrumento no es numérico");
				
				if(descricao.length() == 0)
					throw new Exception("Descripción en blanco");
				
				Entidade superior = entidadeHome.obterEntidadePorApelido("codigosInstrumento");
				if(superior == null)
				{
					superior = (Entidade) mm.getEntity("departamento");
					superior.atribuirNome("Códigos de Instrumentos");
					superior.atribuirApelido("codigosInstrumento");
					superior.atribuirResponsavel(responsavel);
					superior.atribuirSuperior(entidadeHome.obterEntidadePorApelido("bcp"));
					
					superior.incluir();
				}
				
				if(descricao.length() > 200)
					codIntrumento.atribuirNome(codigoStr + " - " + descricao.substring(0,199));
				else
					codIntrumento.atribuirNome(codigoStr + " - " + descricao);
				codIntrumento.atribuirResponsavel(responsavel);
				codIntrumento.atribuirSuperior(superior);
				codIntrumento.atribuirCodigo(codigo);
				codIntrumento.atribuirDescricao(descricao);
				codIntrumento.incluir();
				
				this.setAlert("Código incluido");
			}
			else if(action.getBoolean("upload"))
			{
				Uteis uteis = new Uteis();
				Collection<FileItem> files = action.getFiles("arquivo");
				if(files == null)
					throw new Exception("Elegir el Archivo");
				
				for(FileItem f : files)
				{
					String nomeArquivo = f.getName();
					
					if (nomeArquivo != null) 
						nomeArquivo = FilenameUtils.getName(nomeArquivo);
					
					if(nomeArquivo.indexOf(".txt") == -1)
						throw new Exception("Tipo del Archivo incorrecto");
					
					InfraProperties infra = InfraProperties.getInstance();
					
					String caminho = infra.getProperty("report.temp.path")+"/"+new Date().getTime()+".txt";
					
					IOUtils.copy(f.getInputStream(), new FileOutputStream(caminho));
					
					FileReader reader = new FileReader(caminho);
					BufferedReader in = new BufferedReader(reader);
					String linha;
					String[] linhaSuja;
					String cod2,descricao2;
					
					while ((linha = in.readLine()) != null)
					{
						linhaSuja = linha.split(";");
						if(linhaSuja.length!=2)
							throw new Exception("Línea del Archivo incorrecta = " + linha);
						else
						{
							cod2 = linhaSuja[0];
							descricao2 = linhaSuja[1];
							
							if(!uteis.eNumero(cod2))
								throw new Exception("Código del Instrumento no es numérico línea = " + linha);
							if(descricao2.trim().length() == 0)
								throw new Exception("Descripción en blanco línea = " + linha);
						}
					}
					
					CodigoInstrumento codIntrumento;
					Entidade superior = entidadeHome.obterEntidadePorApelido("codigosInstrumento");
					if(superior == null)
					{
						superior = (Entidade) mm.getEntity("departamento");
						superior.atribuirNome("Códigos de Instrumentos");
						superior.atribuirApelido("codigosInstrumento");
						superior.atribuirResponsavel(responsavel);
						superior.atribuirSuperior(entidadeHome.obterEntidadePorApelido("bcp"));
						
						superior.incluir();
					}
					
					int codigoreal;
					reader = new FileReader(caminho);
					in = new BufferedReader(reader);
					while ((linha = in.readLine()) != null)
					{
						linhaSuja = linha.split(";");
						cod2 = linhaSuja[0];
						descricao2 = linhaSuja[1];
						
						codigoreal = Integer.valueOf(cod2);
						
						codIntrumento = codHome.obterCodigoInstrumento(codigoreal);
						if(codIntrumento == null)
						{
							codIntrumento = (CodigoInstrumento) mm.getEntity("CodigoInstrumento");
							if(descricao.length() > 200)
								codIntrumento.atribuirNome(cod2 + " - " + descricao.substring(0,199));
							else
								codIntrumento.atribuirNome(cod2 + " - " + descricao);
							codIntrumento.atribuirResponsavel(responsavel);
							codIntrumento.atribuirSuperior(superior);
							codIntrumento.atribuirCodigo(codigoreal);
							codIntrumento.atribuirDescricao(descricao2);
							codIntrumento.incluir();
						}
						else
							codIntrumento.atualizarDescricao(descricao2);
					}
					
					in.close();
				}
				
				this.setAlert("Códigos Actualizados");
			}
			else if(action.getBoolean("atualizar"))
			{
				String key;
				String[] sujo;
				CodigoInstrumento codInstrumento;
				
				for(Object keyObj : action.getParameters().keySet())
				{
					key = keyObj.toString();
					
					if(key.startsWith("descricaoReal_"))
					{
						sujo = key.split("_");
						
						codInstrumento = (CodigoInstrumento) entidadeHome.obterEntidadePorId(Long.valueOf(sujo[1]));
						
						codInstrumento.atualizarDescricao(action.getString(key));
					}
				}
				
				this.setAlert("Códigos Actualizados");
			}
			else if(action.getBoolean("excluir"))
			{
				CodigoInstrumento codInstrumento = (CodigoInstrumento) entidadeHome.obterEntidadePorId(action.getLong("id"));
				
				codInstrumento.excluir();
			}
			
			codigos = codHome.obterCodigosInstrumento();
			
			mm.commitTransaction();
			
			this.setResponseView(new CodigosInstrumentoView(codigos, codigo, descricao));
		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new CodigosInstrumentoView(codigos, codigo, descricao));
			mm.rollbackTransaction();
		}
	}
}
