package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;

import com.gvs.crm.model.UploadedFile;

import infra.control.Action;
import infra.view.Button;
import infra.view.InputFile;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;

/**
 * Classe que representa a página que mostra os arquivos armazenados e um
 * formulário para o upload de arquivos.
 * 
 * @author Gustavo Schmal
 */

public class UploadedFilesView extends Table {
	public UploadedFilesView(Collection uploadedFiles) throws Exception {
		super(2);
		if (uploadedFiles.size() > 0) {
			this.addHeader("Uploaded files:");
			Table table = new Table(5);
			table.addHeader("id");
			table.addHeader("Name");
			table.addHeader("Type");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader("Size");
			table.addHeader("");
			for (Iterator i = uploadedFiles.iterator(); i.hasNext();) {
				UploadedFile uploadFile = (UploadedFile) i.next();
				Link downloadLink = new Link(uploadFile.getName(), new Action(
						"download"));
				downloadLink.getAction().add("id", uploadFile.getId());
				Link removeLink = new Link("[remove]", new Action("remove"));
				removeLink.getAction().add("id", uploadFile.getId());
				removeLink.getAction()
						.setConfirmation("Remove uploaded file ?");
				table.addData(new Label(uploadFile.getId()));
				table.addData(downloadLink);
				table.addData(uploadFile.getType());
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.addData(new Label(uploadFile.getSize()));
				table.addData(removeLink);
			}
			this.addData(table);
		}
		this.addHeader("Select new file to upload:");
		this.addData(new InputFile("file", ""));
		this.addFooter(new Button("Upload", new Action("upload")));
	}
}