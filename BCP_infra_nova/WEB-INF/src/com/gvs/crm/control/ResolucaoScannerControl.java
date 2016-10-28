package com.gvs.crm.control;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.ResolucaoScanner;
import com.gvs.crm.model.ResolucaoScannerHome;
import com.gvs.crm.model.SampleModelManager;
import com.gvs.crm.model.UploadedFileHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.model.Uteis;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.RelResolucaoScannerView;

import infra.control.Action;
import infra.control.Control;

public class ResolucaoScannerControl extends Control
{
	public void incluirResolucaoScanner(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		ResolucaoScanner resolucao = (ResolucaoScanner) mm.getEntity("ResolucaoScanner");
		SampleModelManager mm2 = new SampleModelManager();
        UploadedFileHome home = (UploadedFileHome) mm2.getHome("UploadedFileHome");
        
        Uteis uteis = new Uteis();
        Collection<String> types = uteis.obterMimeTypes();
		
		mm.beginTransaction();
		try 
		{
			String titulo = action.getString("titulo").trim();
			String numero = action.getString("numero").trim();
			int ano = action.getInt("ano");
			String anoStr = Integer.valueOf(ano).toString();
			
			InputStream content = action.getInputStream("file");
			String fileName = action.getString("file_name");
			String fileType = action.getString("file_content_type");
			long fileSize = action.getLong("file_size");
			
			resolucao.atribuirTitulo(titulo);
			resolucao.atribuirTipo(numero);
			resolucao.atribuirAno(ano);
			
			if(action.getLong("aseguradoraId") == 0)
				throw new Exception("Aseguradora en blanco");
			else
			{
				Aseguradora aseg = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				resolucao.atribuirOrigem(aseg);
			}
			
			if(titulo.equals(""))
				throw new Exception("Titulo en blanco");
			if(numero.equals(""))
				throw new Exception("Numero en blanco");
			if(ano == 0)
				throw new Exception("Año en blanco");
			if(anoStr.length()!=4)
				throw new Exception("Año en formato incorrecto (yyyy)");
			if(content == null)
				throw new Exception("Archivo en blanco");
			if(!types.contains(fileType))
				throw new Exception("Formato del archivo no es valido (PDF o Word)" + fileType);
			
			resolucao.atribuirResponsavel(usuarioAtual);
			resolucao.incluir();
			
			home.addUploadedFile(resolucao, content, fileName, fileType, fileSize, 0);
			
			mm.commitTransaction();
			
			this.setResponseView(new EventoView(resolucao));

		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(resolucao));
			mm.rollbackTransaction();
		}
	}
	
	public void atualizarResolucaoScanner(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		ResolucaoScanner resolucao = (ResolucaoScanner) eventoHome.obterEventoPorId(action.getLong("id"));
		SampleModelManager mm2 = new SampleModelManager();
        UploadedFileHome home = (UploadedFileHome) mm2.getHome("UploadedFileHome");
        
        Uteis uteis = new Uteis();
        Collection<String> types = uteis.obterMimeTypes();
		
		mm.beginTransaction();
		try 
		{
			String titulo = action.getString("titulo").trim();
			String numero = action.getString("numero").trim();
			int ano = action.getInt("ano");
			String anoStr = Integer.valueOf(ano).toString();
			
			InputStream content = action.getInputStream("file");
			String fileName = action.getString("file_name");
			String fileType = action.getString("file_content_type");
			long fileSize = action.getLong("file_size");
			
			resolucao.atribuirTitulo(titulo);
			resolucao.atribuirTipo(numero);
			resolucao.atribuirAno(ano);
			
			if(action.getLong("aseguradoraId") == 0)
				throw new Exception("Aseguradora en blanco");
			else
			{
				Aseguradora aseg = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				resolucao.atribuirOrigem(aseg);
			}
			
			if(titulo.equals(""))
				throw new Exception("Titulo en blanco");
			if(numero.equals(""))
				throw new Exception("Numero en blanco");
			if(ano == 0)
				throw new Exception("Año en blanco");
			if(anoStr.length()!=4)
				throw new Exception("Año en formato incorrecto (yyyy)");
			if(content != null)
			{
				if(!types.contains(fileType))
					throw new Exception("Formato del archivo no es valido (PDF o Word)" + fileType);
				
				home.removeAllUploadedFiles(resolucao);
				
				home.addUploadedFile(resolucao, content, fileName, fileType, fileSize, 0);
			}
			
			resolucao.atualizarTitulo(titulo);
			resolucao.atualizarTipo(numero);
			resolucao.atualizarAno(ano);
			
			mm.commitTransaction();
			
			this.setResponseView(new EventoView(resolucao));

		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(resolucao));
			mm.rollbackTransaction();
		}
	}
	
	public void localizarResolucaoScanner(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		ResolucaoScannerHome home = (ResolucaoScannerHome) mm.getHome("ResolucaoScannerHome");
		Aseguradora aseg = null;
		String titulo = action.getString("titulo").trim();
		String numero = action.getString("numero").trim();
		int ano = action.getInt("ano");
		Collection<ResolucaoScanner> resolucoes = new ArrayList<ResolucaoScanner>();
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new RelResolucaoScannerView(null, "", "", 0, resolucoes, usuarioAtual));
			else
			{
				if(action.getLong("aseguradoraId") > 0)
					aseg = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				resolucoes = home.obterResolucoes(aseg, titulo, numero, ano);
				
				this.setResponseView(new RelResolucaoScannerView(aseg, titulo, numero, ano, resolucoes, usuarioAtual));
			}
		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new RelResolucaoScannerView(aseg, titulo, numero, ano, resolucoes, usuarioAtual));
		}
	}
}
