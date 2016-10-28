package com.gvs.crm.component;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.CodificacaoPlano;
import com.gvs.crm.model.CodificacoesHome;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class CodificacaoPlanoSelect extends BasicView
{

	private String nome;
	private long valor;
	
	public CodificacaoPlanoSelect(String nome, long valor) throws Exception
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
		
		for(Iterator i = home.obterCodificacaoPlanos().iterator() ; i.hasNext() ; )
		{
			CodificacaoPlano plano = (CodificacaoPlano) i.next();
			
			String caption = plano.obterCodigo() + " - " + plano.obterTitulo();
			
			if(valor > 0)
				select.add(caption, plano.obterId(), plano.obterId() == valor);
			else
				select.add(caption, plano.obterId(), false);
		}
		
		return select;
	}

}
