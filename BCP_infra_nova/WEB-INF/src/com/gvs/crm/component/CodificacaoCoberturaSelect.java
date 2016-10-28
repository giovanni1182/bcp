package com.gvs.crm.component;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.CodificacaoCobertura;
import com.gvs.crm.model.CodificacoesHome;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class CodificacaoCoberturaSelect extends BasicView
{

	private String nome;
	private long valor;
	
	public CodificacaoCoberturaSelect(String nome, long valor) throws Exception
	{
		this.nome = nome;
		this.valor = valor;
	}
	
	public View execute(User user, Locale locale, Properties properties) throws Exception 
	{
		Select select = new Select(this.nome, 1);
		
		CRMModelManager mm = new CRMModelManager(user);
		CodificacoesHome home = (CodificacoesHome) mm.getHome("CodificacoesHome");
		
		select.add("Todos", 0, false);
		
		for(Iterator i = home.obterCodificacaoCoberturas().iterator() ; i.hasNext() ; )
		{
			CodificacaoCobertura cobertura = (CodificacaoCobertura) i.next();
			
			String caption = cobertura.obterCodigo() + " - " + cobertura.obterTitulo();
			
			if(valor > 0)
				select.add(caption, cobertura.obterId(), cobertura.obterId() == valor);
			else
				select.add(caption, cobertura.obterId(), false);
		}
		
		return select;
	}

}
