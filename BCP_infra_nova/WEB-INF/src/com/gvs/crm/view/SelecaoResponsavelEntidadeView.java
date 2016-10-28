package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Usuario;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class SelecaoResponsavelEntidadeView extends PortalView {
	private Entidade entidade;

	private Collection usuarios;

	public SelecaoResponsavelEntidadeView(Entidade entidade, Collection usuarios)
			throws Exception {
		this.entidade = entidade;
		this.usuarios = usuarios;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);
		table.addSubtitle("Confirma la alteración de la Responsabilidad");
		table.addHeader("Unidade");
		table.addHeader("Usuário");
		TreeMap map = new TreeMap();
		for (Iterator i = this.usuarios.iterator(); i.hasNext();) {
			Usuario usuario = (Usuario) i.next();
			map.put(usuario.obterSuperior().obterNome() + usuario.obterNome(),
					usuario);
		}
		for (Iterator i = map.values().iterator(); i.hasNext();) {
			Usuario usuario = (Usuario) i.next();
			table.addData(usuario.obterSuperior().obterNome());
			Action atualizarAction = new Action("atualizarResponsavelEntidade");
			atualizarAction.add("id", this.entidade.obterId());
			atualizarAction.add("responsavelId", usuario.obterId());
			atualizarAction
					.setConfirmation("Confirma la alteración de la Responsabilidad ?");
			table.addData(new Link(usuario.obterNome(), atualizarAction));
		}
		Action cancelarAction = new Action("visualizarDetalhesEntidade");
		cancelarAction.add("id", this.entidade.obterId());
		table.addFooter(new Button("Cancelar", cancelarAction));
		return table;
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception {
		return new Label(this.entidade.obterNome());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gvs.crm.view.PortalView#getOrigemMenu()
	 */
	public Entidade getOrigemMenu() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}