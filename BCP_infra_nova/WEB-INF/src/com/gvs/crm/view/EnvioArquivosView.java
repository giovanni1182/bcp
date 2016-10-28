package com.gvs.crm.view;

import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.EnvioArquivos;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputFile;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class EnvioArquivosView extends EventoAbstratoView 
{
	public View execute(User user, Locale locale, Properties properties) throws Exception
	{
		EnvioArquivos envio = (EnvioArquivos) this.obterEvento();
		
		boolean novo = envio.obterId() == 0;
		
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		if(!novo)
		{
			table.addHeader("Seguradora:");
			table.add(envio.obterOrigem().obterNome());
			table.addHeader("Responsable:");
			table.add(envio.obterResponsavel().obterNome());
			table.addHeader("Fecha:");
			table.add(new Label(envio.obterCriacao(),"dd/MM/yyyy HH:mm:ss"));
		}
		
		table.addHeader("Archivos:");
		if(!novo)
		{
			Table t = new Table(1);
			t.addStyle(Table.STYLE_ALTERNATE);
			
			Collection<String> arquivos = envio.obterArquivos();
			
			for(String nomeArquivo : arquivos)
				t.add(new Label(nomeArquivo));
			
			table.add(t);
		}
		else
		{
			InputFile input = new InputFile("file", null);
			input.setMultiple(true);
			input.setAccept(".zip,.pdf,.doc,.docx,.xls,.xlsx,.txt");
			table.add(input);
			
			Button incluirButton = new Button("Agregar", new Action("incluirEnvioArquivo"));
			table.addFooter(incluirButton);
		}
		
		mainTable.add(table);
		return mainTable;
	}
}
