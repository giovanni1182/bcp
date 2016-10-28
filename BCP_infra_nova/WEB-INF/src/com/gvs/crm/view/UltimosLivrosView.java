package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Livro;
import com.gvs.crm.model.LivroHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class UltimosLivrosView extends PortalView
{
	private Collection<Aseguradora> aseguradoras;
	private LivroHome home;
	
	public UltimosLivrosView(Collection<Aseguradora> aseguradoras, LivroHome home) throws Exception
	{
		this.aseguradoras = aseguradoras;
		this.home = home;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		table.addStyle(Table.STYLE_ALTERNATE);
		
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Aseguradora");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Ultimo Libro");
		
		for(Iterator<Aseguradora> i = this.aseguradoras.iterator() ; i.hasNext() ; )
		{
			Aseguradora aseguradora = i.next();
			
			table.add(aseguradora.obterNome());
			
			Livro ultimoLivro = home.obterUltimoLivro(aseguradora, "");
			table.setNextHAlign(Table.HALIGN_CENTER);
			if(ultimoLivro!=null)
			{
				String tipo = ultimoLivro.obterTipo();
				int mes = ultimoLivro.obterMes();
				int ano = ultimoLivro.obterAno();
				
				String mesStr = new Integer(mes).toString();
				if(mesStr.length() == 1)
					mesStr = "0"+ mesStr;
				
				Link link = new Link(tipo + " - " + mesStr + "/" + ano, new Action("visualizarEvento"));
				link.getAction().add("id", ultimoLivro.obterId());
				link.setNovaJanela(true);
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(link);
			}
			else
				table.add("");
		}
		
		return table;
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
		return new Label("Ultimos Libros Electrónicos");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}