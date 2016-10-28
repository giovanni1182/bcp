package com.gvs.crm.component;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Usuario;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class AseguradorasResScannerSelect extends BasicView 
{
	private String nome;

	private Aseguradora aseguradora;
	private boolean todas;
	private Usuario usuario;
	
	public AseguradorasResScannerSelect(String nome, Aseguradora aseguradora, boolean todas, Usuario usuario) throws Exception
	{
		this.nome = nome;
		this.aseguradora = aseguradora;
		this.todas = todas;
		this.usuario = usuario;
	}

	public View execute(User user, Locale arg1, Properties arg2) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(user);
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Select select = new Select(this.nome, 1);
		if(todas)
			select.add("[Todas]", 0, false);
		//else
			//select.add("", 0, false);
		
		Collection<Aseguradora> aseguradoras = this.usuario.obterAseguradorasResEscaneada().values();

		for (Iterator i = entidadeHome.obterAseguradoras().iterator(); i.hasNext();) 
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
			
			if(aseguradoras.contains(aseguradora) || usuario.obterNivel().equals(Usuario.ADMINISTRADOR))
			{
				if(this.aseguradora!=null)
					select.add(aseguradora.obterNome(), aseguradora.obterId(),aseguradora.obterId() == this.aseguradora.obterId());
				else
					select.add(aseguradora.obterNome(), aseguradora.obterId(),false);
			}
		}

		return select;
	}
}
