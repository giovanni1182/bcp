package com.gvs.crm.component;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class PlanosAseguradorasSelect extends BasicView
{
	private String nome;

	private Aseguradora aseguradora;
	private boolean todas;
	private boolean especial,modificado;
	
	public PlanosAseguradorasSelect(String nome, Aseguradora aseguradora, boolean todas, boolean especial, boolean modificado) throws Exception
	{
		this.nome = nome;
		this.aseguradora = aseguradora;
		this.todas = todas;
		this.especial = especial;
		this.modificado = modificado;
	}

	public View execute(User user, Locale arg1, Properties arg2) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(user);
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(user);
		
		Select select = new Select(this.nome, 1);
		if(todas)
			select.add("[Todas]", 0, false);
		else
			select.add("", 0, false);
		
		select.add("Planes Especiales", -1, especial);
		//if(usuarioAtual.obterId() == 1)
			//select.add("Planes Modificados", -2, modificado);

		for (Aseguradora aseguradora : entidadeHome.obterAseguradoras()) 
		{
			//select.add(aseguradora.obterNome(), aseguradora.obterNome(),aseguradora.obterNome().equals(this.aseguradora));
			
			if(this.aseguradora!=null)
				select.add(aseguradora.obterNome(), aseguradora.obterId(),aseguradora.obterId() == this.aseguradora.obterId());
			else
				select.add(aseguradora.obterNome(), aseguradora.obterId(),false);
		}

		return select;
	}
}