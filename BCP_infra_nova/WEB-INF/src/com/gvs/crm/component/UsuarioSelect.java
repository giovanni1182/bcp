package com.gvs.crm.component;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Usuario;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class UsuarioSelect extends BasicView {
	private String nome;

	private Collection usuarios;

	public UsuarioSelect(String nome, Collection usuarios) {
		this.nome = nome;
		this.usuarios = usuarios;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		Select select = new Select(nome, 1);
		select.add("", "0", false);
		for (Iterator i = this.usuarios.iterator(); i.hasNext();) {
			Usuario u = (Usuario) i.next();
			select.add(u.obterNome() + " (" + u.obterEmpresa().obterNome()
					+ ")", Long.toString(u.obterId()), false);
		}
		return select;
	}
}