package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Usuario;

import infra.security.User;
import infra.view.Label;
import infra.view.View;

public class ResponsabilidadesView extends PortalView {
	private static final String ENTIDADES = "1";

	private Usuario usuario;

	private boolean usuarioAtual;

	private Entidade origemMenu;

	public ResponsabilidadesView(Usuario usuario, boolean usuarioAtual,
			Entidade origemMenu) throws Exception {
		this.usuario = usuario;
		this.usuarioAtual = usuarioAtual;
		this.origemMenu = origemMenu;

	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		return new EntidadesView(this.usuario.obterEntidadeDeResponsabilidade());
	}

	public String getSelectedGroup() throws Exception {
		if (this.usuarioAtual)
			return "Menu Principal";
		else
			return this.usuario.obterNome();
	}

	public String getSelectedOption() throws Exception {
		return "Responsabilidades";
	}

	public View getTitle() throws Exception {
		return new Label(this.usuario.obterNome() + " - Eventos");
	}

	public Entidade getOrigemMenu() throws Exception {
		return this.origemMenu;
	}
}