package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.ApoliceDuplicada;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class ApolicesDuplicadasView extends PortalView 
{
	private Entidade entidade;
	private Collection apolices;
	
	public ApolicesDuplicadasView(Entidade entidade, Collection apolices) throws Exception
	{
		this.entidade = entidade;
		this.apolices = apolices;
	}
	public View getBody(User user, Locale locale, Properties properties) throws Exception 
	{
		Table table = new Table(8);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);
		
		
		Button button = new Button("Excluir", new Action("excluirDuplicidadeApolice"));
		button.getAction().add("excluir", true);
		table.setNextColSpan(table.getColumns());
		table.add(button);
		
		table.addHeader("Qtde");
		table.addHeader("Aseguradora");
		table.addHeader("Numero Apolice");
		table.addHeader("Seção");
		table.addHeader("Status");
		table.addHeader("Plano");
		table.addHeader("Endoso");
		table.addHeader("Certificado");
		
		for(Iterator i = apolices.iterator() ; i.hasNext() ; )
		{
			ApoliceDuplicada apolice = (ApoliceDuplicada) i.next();
			
			table.add(new Label(apolice.obterQtde()));
			table.add(apolice.obterOrigem().obterNome());
			table.add(apolice.obterNumero());
			table.add(apolice.obterSecao().obterApelido());
			table.add(apolice.obterStatus());
			if(apolice.obterPlano()!=null)
				table.add(apolice.obterPlano().obterPlano());
			else
				table.add("");
			
			table.add(new Label(apolice.obterEndoso()));
			table.add(new Label(apolice.obterCertificado()));
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
		return null;
	}

	public Entidade getOrigemMenu() throws Exception 
	{
		return null;
	}
}