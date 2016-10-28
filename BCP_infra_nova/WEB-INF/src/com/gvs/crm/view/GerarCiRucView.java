package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.SampleModelManager;
import com.gvs.crm.model.UploadedFile;
import com.gvs.crm.model.UploadedFileHome;

import infra.control.Action;
import infra.view.Button;
import infra.view.Image;
import infra.view.Link;
import infra.view.Table;

public class GerarCiRucView extends Table 
{
	public GerarCiRucView(Aseguradora aseguradora) throws Exception
	{
		super(3);
		this.addStyle(STYLE_ALTERNATE);
		
		SampleModelManager mm = new SampleModelManager();
		UploadedFileHome home = (UploadedFileHome) mm.getHome("UploadedFileHome");
		Collection uploadedFiles = home.getAllUploadedFiles(aseguradora);
		
		this.addSubtitle("Generar CI y RUC no encontrados en la Tabla de CRM");
		
		if(uploadedFiles.size() > 0)
		{
			this.add("");
			this.addHeader("Nombre Archivo");
			this.addHeader("Fecha Creacion");
			
			for (Iterator i = uploadedFiles.iterator(); i.hasNext();)
			{
				UploadedFile uploadFile = (UploadedFile) i.next();
				Link downloadLink = new Link(uploadFile.getName(), new Action("download"));
				downloadLink.getAction().add("id", uploadFile.getId());
	
				Link removeLink = new Link(new Image("delete.gif"), new Action("remove"));
				removeLink.getAction().add("id", uploadFile.getId());
				removeLink.getAction().add("idEntidade", aseguradora.obterId());
				removeLink.getAction().setConfirmation("Excluir Anexo ?");
	
				this.addData(removeLink);
				this.addData(downloadLink);
				this.addData(new SimpleDateFormat("dd/MM/yyyy").format(uploadFile.getDate()));
			}
		}
			
		Button gerarRucCiButton = new Button("Generar Nuevo",new Action("gerarCiRucNaoEncontrados"));
		gerarRucCiButton.getAction().add("id", aseguradora.obterId());
		
		this.addFooter(gerarRucCiButton);
	}
}
