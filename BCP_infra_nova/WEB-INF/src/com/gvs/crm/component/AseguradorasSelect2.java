package com.gvs.crm.component;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class AseguradorasSelect2 extends BasicView 
{
	private String nome;
	private Entidade aseguradora;
	private boolean totais;
	private boolean todas;
	
	public AseguradorasSelect2(String nome,Entidade aseguradora, boolean totais, boolean todas) throws Exception 
	{
		this.nome = nome;
		this.aseguradora = aseguradora;
		this.totais = totais;
		this.todas = todas;
	}

	public View execute(User user, Locale arg1, Properties arg2) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(user);
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Select select = new Select(this.nome, 1);
		if(this.todas)
			select.add("[Todas]", 0, false);
		if(this.totais)
			select.add("[Solamente totales]", 714, false);

		for (Iterator i = entidadeHome.obterAseguradoras().iterator(); i.hasNext();) 
		{
			Aseguradora aseguradora = (Aseguradora) i.next();

			if(this.aseguradora!=null)
				select.add(aseguradora.obterNome(), aseguradora.obterId(),aseguradora.equals(this.aseguradora));
			else
				select.add(aseguradora.obterNome(), aseguradora.obterId(),false);
		}

		return select;
	}
}