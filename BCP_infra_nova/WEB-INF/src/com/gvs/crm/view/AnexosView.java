package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;

import com.gvs.crm.model.Evento;
import com.gvs.crm.model.SampleModelManager;
import com.gvs.crm.model.UploadedFile;
import com.gvs.crm.model.UploadedFileHome;

import infra.control.Action;
import infra.view.Button;
import infra.view.Image;
import infra.view.InputFile;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;

public class AnexosView extends Table
{
	public AnexosView(Evento evento) throws Exception
	{
		super(1);

		SampleModelManager mm = new SampleModelManager();
		UploadedFileHome home = (UploadedFileHome) mm.getHome("UploadedFileHome");
		Collection uploadedFiles = home.getAllUploadedFiles(evento);

		//this.addSubtitle("Anexos");

		if (uploadedFiles.size() > 0)
		{
			Table table = new Table(4);
			table.addStyle(Table.STYLE_ALTERNATE);
			table.setWidth("100%");

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
				removeLink.getAction().add("idEvento", evento.obterId());
				removeLink.getAction().setConfirmation("Excluir el Anexo ?");
				
				if (evento.permiteAtualizar())
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
			this.addData(table);
		}

		//if (evento.permiteAtualizar())
		//{
		 	Table table = new Table(2);
		 	
		 	table.addSubtitle("Upload");
			
			table.addHeader("Archivo:");
			InputFile input = new InputFile("file", "");
			input.setMultiple(true);
			table.addData(input);

			Action action = new Action("upload");
			action.add("idEvento", evento.obterId());
			
			table.addFooter(new Button("Agregar", action));
			
			this.add(table);
			
		//}
	}
}