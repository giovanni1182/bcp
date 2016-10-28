package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Select;
import infra.view.Table;
import infra.view.View;

public class PesquisaRucCiView extends PortalView
{
	private String tipoDoc,numeroDoc;
	private Collection<String> pessoas;
	private boolean listar;
	
	public PesquisaRucCiView(String tipoDoc, String numeroDoc, Collection<String> pessoas, boolean listar)
	{
		this.tipoDoc = tipoDoc;
		this.numeroDoc = numeroDoc;
		this.pessoas = pessoas;
		this.listar = listar;
	}
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		
		Select select = new Select("tipoDoc", 1);
		select.add("CI", "CI", "CI".equals(tipoDoc));
		select.add("CRC", "CRC", "CRC".equals(tipoDoc));
		select.add("CRP", "CRP", "CRP".equals(tipoDoc));
		select.add("RUC", "RUC", "RUC".equals(tipoDoc));
		
		table.addHeader("Tipo Documento:");
		table.add(select);
		table.addHeader("Nº Documento");
		table.add(new InputString("numeroDoc", this.numeroDoc, 15));
		
		Button buscarButton = new Button("Buscar", new Action("pesquisaRucCi"));
		buscarButton.getAction().add("listar", true);
		table.addFooter(buscarButton);
		
		mainTable.add(table);
		
		if(this.listar)
		{
			if(this.pessoas.size() > 0)
			{
				table = new Table(5);
				table.setWidth("40%");
				table.addStyle(Table.STYLE_ALTERNATE);
				table.addSubtitle(this.pessoas.size() + " encontradas");
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Tipo Pessoa");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("País");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Nome");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Sobre Nome");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Data Nascimento");
				
				for(Iterator<String> i = this.pessoas.iterator() ; i.hasNext() ; )
				{
					String linha = i.next();
					
					String[] linhaSuja = linha.split(";");
					
					String tipoPessoa = linhaSuja[0];
					String pais = linhaSuja[1];
					String nome = linhaSuja[2];
					String sobreNome = linhaSuja[3];
					String dataStr = linhaSuja[4];
					
					Date data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataStr);
					
					table.add(tipoPessoa);
					table.setNextHAlign(Table.HALIGN_CENTER);
					table.add(pais);
					table.add(nome);
					table.add(sobreNome);
					table.setNextHAlign(Table.HALIGN_CENTER);
					table.add(new SimpleDateFormat("dd/MM/yyyy").format(data));
				}
				
				mainTable.add(table);
			}
			else
				mainTable.addHeader("No encontrado el documento");
		}
		
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
		return new Label("Pesquisa RUC/CI");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}