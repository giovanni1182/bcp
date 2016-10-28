package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CodificacaoCobertura;
import com.gvs.crm.model.CodificacaoPlano;
import com.gvs.crm.model.CodificacaoRisco;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class ListarCodificacaoRiscosView extends PortalView
{

	private Collection riscos;
	
	public ListarCodificacaoRiscosView(Collection riscos) throws Exception
	{
		this.riscos = riscos;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception 
	{
		Table table = new Table(6);
		table.setWidth("70%");
		table.addSubtitle("Riesgos");
		table.addStyle(Table.STYLE_ALTERNATE);
		
		table.addHeader("Codigo del Plan");
		table.addHeader("Nombre del Plan");
		table.addHeader("Codigo de la Cobertura");
		table.addHeader("Nombre de la Cobertura");
		table.addHeader("Codigo del Riesgo");
		table.addHeader("Nombre del Riesgo");
		
		for(Iterator i = riscos.iterator() ; i.hasNext() ; )
		{
			CodificacaoRisco risco = (CodificacaoRisco) i.next();
			
			CodificacaoCobertura cobertura = (CodificacaoCobertura) risco.obterSuperior();
			
			CodificacaoPlano plano = (CodificacaoPlano) cobertura.obterSuperior();
			
			table.add(plano.obterCodigo());
			table.add(plano.obterTitulo());
			
			table.add(cobertura.obterCodigo());
			table.add(cobertura.obterTitulo());
			
			table.add(risco.obterCodigo());
			Link link = new Link(risco.obterTitulo(),new Action("novoEvento"));
			link.getAction().add("classe", "codificacaodetalhe");
			link.getAction().add("superiorId", risco.obterId());
			
			table.add(link);
		}
		
		Button voltarButton = new Button("Volver",new Action("novoEvento"));
		voltarButton.getAction().add("passo", 2);
		
		table.addFooter(voltarButton);
		
		return table;
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
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
		return new Label("Codificación de Riesgos del Siniestro");
	}
}