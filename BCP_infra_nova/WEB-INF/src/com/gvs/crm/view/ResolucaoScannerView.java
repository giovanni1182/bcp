package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.AseguradorasResScannerSelect;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Raiz;
import com.gvs.crm.model.ResolucaoScanner;
import com.gvs.crm.model.SampleModelManager;
import com.gvs.crm.model.UploadedFile;
import com.gvs.crm.model.UploadedFileHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Image;
import infra.view.InputFile;
import infra.view.InputInteger;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class ResolucaoScannerView extends EventoAbstratoView
{
	public View execute(User user, Locale arg1, Properties arg2) throws Exception
	{
		CRMModelManager mm2 = new CRMModelManager(user);
		UsuarioHome home2 = (UsuarioHome) mm2.getHome("UsuarioHome");
		Usuario usuario = home2.obterUsuarioPorUser(user);
		ResolucaoScanner resolucao = (ResolucaoScanner) this.obterEvento();
		
		boolean novo = resolucao.obterId() == 0;
		
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		table.addHeader("Aseguradora:");
		
		Entidade origem = resolucao.obterOrigem();
		Aseguradora aseg = null;
		if(origem!=null && !(origem instanceof Raiz))
			aseg = (Aseguradora) origem;
		
		table.add(new AseguradorasResScannerSelect("aseguradoraId", aseg, false, usuario));
		table.addHeader("Titulo:");
		table.add(new InputString("titulo", resolucao.obterTitulo(), 60));
		table.addHeader("Numero:");
		table.add(new InputString("numero", resolucao.obterTipo(), 15));
		table.addHeader("Año:");
		table.add(new InputInteger("ano", resolucao.obterAno(), 4));
		table.addHeader("Archivo:");
		table.addData(new InputFile("file", ""));

		if(novo)
		{
			Button incluirButton = new Button("Agregar", new Action("incluirResolucaoScanner"));
			table.addFooter(incluirButton);
			
			mainTable.add(table);
		}
		else
		{
			Button atualizarButton = new Button("Actualizar", new Action("atualizarResolucaoScanner"));
			atualizarButton.getAction().add("id", resolucao.obterId());
			
			Button excluirButton = new Button("Eliminar", new Action("excluirEvento"));
			excluirButton.getAction().add("id", resolucao.obterId());
			
			table.addFooter(atualizarButton);
			table.addFooter(excluirButton);
			
			mainTable.add(table);
			
			SampleModelManager mm = new SampleModelManager();
			UploadedFileHome home = (UploadedFileHome) mm.getHome("UploadedFileHome");
			Collection uploadedFiles = home.getAllUploadedFiles(resolucao);
			
			if (uploadedFiles.size() > 0)
			{
				table = new Table(4);
				table.addStyle(Table.STYLE_ALTERNATE);
				table.setWidth("30%");
				
				table.addSubtitle("Archivo");

				table.addHeader("");
				table.addHeader("Nombre");
				table.addHeader("Tipo");
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.addHeader("Tamaño");

				for (Iterator i = uploadedFiles.iterator(); i.hasNext();)
				{
					UploadedFile uploadFile = (UploadedFile) i.next();
					
					Link removeLink = new Link(new Image("delete.gif"), new Action("remove"));
					removeLink.getAction().add("id", uploadFile.getId());
					removeLink.getAction().add("idEvento", resolucao.obterId());
					removeLink.getAction().setConfirmation("Excluir el Anexo ?");
					if(resolucao.permiteAtualizar())
						table.addData(removeLink);
					else
						table.addData("");
					
					Link downloadLink = new Link(uploadFile.getName(), new Action("download"));
					downloadLink.getAction().add("id", uploadFile.getId());
					
					table.addData(downloadLink);
					table.addData(uploadFile.getType());
					table.setNextHAlign(Table.HALIGN_RIGHT);
					table.addData(new Label(uploadFile.getSize()));
					
				}

				mainTable.add(table);
			}
		}
		
		return mainTable;
	}
}