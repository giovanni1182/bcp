package com.gvs.crm.view;

import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.impl.FilaProcessamento;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Image;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class FilaProcessamentoView extends PortalView 
{
	private Collection<FilaProcessamento> arquivos;
	
	public FilaProcessamentoView(Collection<FilaProcessamento> arquivos)
	{
		this.arquivos = arquivos;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(3);
		table.setWidth("15%");
		table.addStyle(Table.STYLE_ALTERNATE);
		table.addSubtitle(this.arquivos.size() + " Archivos");
		
		int cont = 1;
		Link link;
		
		if(arquivos.size() > 0)
		{
			table.addHeader("");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Archivo");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Status");
			
			for(FilaProcessamento fila : this.arquivos)
			{
				link = new Link(new Image("delete.gif"), new Action("relArquivosEmProcessamento"));
				link.getAction().add("excluir", true);
				link.getAction().add("nome", fila.getNome());
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(link);
				
				table.add(cont + " - " + fila.getNome());
				table.add(fila.getStatus());
				
				
				cont++;
			}
		}
		
		mainTable.add(table);
		
		table = new Table(2);
		table.addSubtitle("Eliminar Archivo");
		
		table.addHeader("Nombre:");
		table.add(new InputString("nomeArquivo","",30));
		
		Button excluirButton = new Button("Eliminar", new Action("relArquivosEmProcessamento"));
		excluirButton.getAction().add("excluir2", true);
		
		table.addFooter(excluirButton);
		
		mainTable.add(table);
		
		return mainTable;
	}

	public String getSelectedGroup() throws Exception
	{
		return null;
	}

	public String getSelectedOption() throws Exception
	{
		return null;
	}

	public View getTitle() throws Exception
	{
		return new Label("Cola de Procesamiento");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}