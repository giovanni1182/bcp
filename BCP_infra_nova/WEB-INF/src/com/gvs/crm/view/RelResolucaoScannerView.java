package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.AseguradorasResScannerSelect;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.ResolucaoScanner;
import com.gvs.crm.model.Usuario;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputInteger;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class RelResolucaoScannerView extends PortalView
{
	private Aseguradora aseguradora;
	private String titulo, numero;
	private int ano;
	private Collection<ResolucaoScanner> resolucoes;
	private Usuario usuario;
	
	public RelResolucaoScannerView(Aseguradora aseguradora, String titulo, String numero, int ano, Collection<ResolucaoScanner> resolucoes, Usuario usuario)
	{
		this.aseguradora = aseguradora;
		this.titulo = titulo;
		this.numero = numero;
		this.ano = ano;
		this.resolucoes = resolucoes;
		this.usuario = usuario;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		
		table.addHeader("Aseguradora:");
		if(usuario.obterNivel().equals(Usuario.ADMINISTRADOR))
			table.add(new AseguradorasResScannerSelect("aseguradoraId", this.aseguradora, true, usuario));
		else
			table.add(new AseguradorasResScannerSelect("aseguradoraId", this.aseguradora, false, usuario));
		table.addHeader("Titulo:");
		table.add(new InputString("titulo", this.titulo, 60));
		table.addHeader("Numero:");
		table.add(new InputString("numero", this.numero, 15));
		table.addHeader("Año:");
		table.add(new InputInteger("ano", this.ano, 4));
		
		Button buscarButton = new Button("Buscar", new Action("localizarResolucaoScanner"));
		table.addFooter(buscarButton);
		
		mainTable.add(table);
		
		if(this.resolucoes.size() > 0)
		{
			table = new Table(4);
			table.setWidth("50%");
			table.addStyle(Table.STYLE_ALTERNATE);
			
			table.addSubtitle("");
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Aseguradora");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Titulo");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Numero");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Año");
			
			for(Iterator<ResolucaoScanner> i = this.resolucoes.iterator() ; i.hasNext() ; )
			{
				ResolucaoScanner resolucao = i.next();
				
				table.add(resolucao.obterOrigem().obterNome());
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				Link link = new Link(resolucao.obterTitulo(), new Action("visualizarEvento"));
				link.getAction().add("id", resolucao.obterId());
				link.setNovaJanela(true);
				table.add(link);
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(resolucao.obterTipo());
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(new Label(resolucao.obterAno()));
			}
			
			mainTable.add(table);
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
		return new Label("Buscar Resolución Escaneada");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}