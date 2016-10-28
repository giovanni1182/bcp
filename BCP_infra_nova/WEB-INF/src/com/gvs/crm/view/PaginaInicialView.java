package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Usuario;

import infra.security.User;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class PaginaInicialView extends PortalView {
	private Usuario usuario;

	private Entidade origemMenu;

	public PaginaInicialView(Usuario usuario, Entidade origemMenu)
			throws Exception {
		this.usuario = usuario;
		this.origemMenu = origemMenu;
	}

	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(1);
		table.setWidth("100%");
		if(usuario.obterNome().toLowerCase().indexOf("e70") == -1)
		{
			table.add(new AgendaView(this.usuario, this.origemMenu));
			table.add(new EventosPendentesView(this.usuario, origemMenu));
		}
		return table;
	}

	public String getSelectedGroup() throws Exception {
		return "Menu Principal";
	}

	public String getSelectedOption() throws Exception {
		return "Página Inicial";
	}

	public View getTitle() throws Exception {
		return new Label("Página Inicial");
	}

	public Entidade getOrigemMenu() throws Exception {
		return this.origemMenu;
	}
}