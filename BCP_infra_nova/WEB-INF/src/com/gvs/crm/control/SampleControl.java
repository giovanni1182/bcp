package com.gvs.crm.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;

import org.apache.commons.fileupload.FileItem;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.SampleModelManager;
import com.gvs.crm.model.UploadedFile;
import com.gvs.crm.model.UploadedFileHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EntidadeView;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.HelpView;
import com.gvs.crm.view.PaginaInicialView;
import com.gvs.crm.view.UploadedFilesView;

import infra.control.Action;
import infra.control.Control;

/**
 * @author Gustavo Schmal
 */

public class SampleControl extends Control {
	// Ação executada quando o usuário clica em um arquivo que se deseja fazer
	// download.
	public void download(Action action) throws Exception
	{
		SampleModelManager mm = new SampleModelManager();
		UploadedFileHome home = (UploadedFileHome) mm
				.getHome("UploadedFileHome");

		// instancia a entidade
		UploadedFile uploadedFile = home.getUploadedFileById(action
				.getLong("id"));

		// obter um InputStream o arquivos a ser feito download
		InputStream content = home.getUploadedFileContent(uploadedFile);

		// atribui o arquivo a ser feito download, o nome, o tipo e o tamanho
		this.setResponseInputStream(content);
		this.setResponseFileName(uploadedFile.getName());
		this.setResponseContentType(uploadedFile.getType());
		this.setResponseContentSize(uploadedFile.getSize());
	}
	
	public void downloadArquivo(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		
		File f = new File("C:/tmp/" + action.getString("arquivoDownload"));
		
		try
		{
			if(f.exists())
			{
				InputStream inputStream = new FileInputStream(f);
			    this.setResponseInputStream(inputStream);
				this.setResponseFileName(f.getName());
				this.setResponseContentType(f.getName().substring(f.getName().lastIndexOf("."), f.getName().length()));
				this.setResponseContentSize(f.length());
			}
			else
				throw new Exception("Archivo " + action.getString("arquivoDownload") + " no fue encontrado");
		}
		catch (Exception e)
		{
			this.setAlert(Util.translateException(e));
			this.setResponseView(new PaginaInicialView(usuario, usuario));
		}
	}

	// ação inicial que mostra todos os arquivos feitos upload e permite a
	// adição de novos arquivos e a remoção dos arquivos gravados
	public void initialDownload(Action action) throws Exception {
		SampleModelManager mm = new SampleModelManager();
		UploadedFileHome home = (UploadedFileHome) mm
				.getHome("UploadedFileHome");
		Collection uploadedFiles = home.getAllUploadedFiles();
		this.setResponseView(new UploadedFilesView(uploadedFiles));
	}

	// remove um arquivo.
	public void remove(Action action) throws Exception {
		SampleModelManager mm = new SampleModelManager();
		CRMModelManager mm2 = new CRMModelManager(this.getUser());
		UploadedFileHome home = (UploadedFileHome) mm.getHome("UploadedFileHome");
		EventoHome eventoHome = (EventoHome) mm2.getHome("EventoHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm2.getHome("EntidadeHome");
		home.removeUploadedFile(action.getLong("id"));
		long idEvento = action.getLong("idEvento");
		long idEntidade = action.getLong("idEntidade");
		if (idEvento == 0 && idEntidade == 0)
			this.forward(new Action("initialDownload"));
		else if(idEvento > 0)
		{
			Evento evento = (Evento) eventoHome.obterEventoPorId(idEvento);
			this.setResponseView(new EventoView(evento));
		}
		else if(idEntidade > 0)
		{
			Entidade e = entidadeHome.obterEntidadePorId(idEntidade);
			this.setResponseView(new EntidadeView(e));
			
		}
	}

	// ação executada quando o usuário clicar no botão "Upload" para que o
	// arquivo selecionado no componente InputFile possa ser carregado (upload)
	// e gravado no banco de dados.
	public void upload(Action action) throws Exception {
		CRMModelManager mm2 = new CRMModelManager(this.getUser());
		EventoHome eventoHome = (EventoHome) mm2.getHome("EventoHome");
		SampleModelManager mm = new SampleModelManager();
		UploadedFileHome home = (UploadedFileHome) mm
				.getHome("UploadedFileHome");

		long idEvento = action.getLong("idEvento");
		Evento evento = null;
		if (idEvento != 0)
			evento = (Evento) eventoHome.obterEventoPorId(idEvento);
		try 
		{
			Collection<FileItem> files = action.getFiles("file");
			
			if(files!=null)
			{
				if(files.size() == 0)
					throw new Exception("Elegiré el Archivo");
				
				String type;
				for(FileItem fileItem : files)
				{
					type = fileItem.getContentType();
					
					if (idEvento == 0)
						home.addUploadedFile(fileItem.getInputStream(), fileItem.getName(), type, fileItem.getInputStream().available(), 0);
					else
						home.addUploadedFile(evento, fileItem.getInputStream(), fileItem.getName(), type, fileItem.getInputStream().available(), 0);
					
					/*tamanho = fileItem.getSize();
					if(type.indexOf("image") > -1)
					{
						if(tamanho>1048576)//1MB
							throw new Exception("O tamanho máximo de cada Foto é 1 MB, a pergunta não foi incluida");
					}
					else if(type.indexOf("video/mp4") > -1)
					{
						if(tamanho>10485760 )//10MB
							throw new Exception("O tamanho máximo de cada Video é 10 MB, a pergunta não foi atualizada");
					}
					else
						throw new Exception("Formato de arquivo inválido " + type);*/
				}
			}
			else
				throw new Exception("Elegiré el Archivo");

			/*InputStream content = action.getInputStream("file");
			String fileName = action.getString("file_name");
			String fileType = action.getString("file_content_type");
			long fileSize = action.getLong("file_size");

			if (idEvento == 0)
				home.addUploadedFile(content, fileName, fileType, fileSize, 0);
			else
				home.addUploadedFile(evento, content, fileName, fileType, fileSize, 0);*/

			if (idEvento == 0)
				this.forward(new Action("initial"));
			else
				this.setResponseView(new EventoView(evento));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			if (idEvento == 0)
				this.forward(new Action("initial"));
			else
				this.setResponseView(new EventoView(evento));
		}
	}

	public void abrirHelp(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());

		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Entidade entidade = home.obterEntidadePorId(action.getLong("id"));

		try {
			this.setResponseView(new HelpView(entidade));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new HelpView(entidade));
		}
	}
}