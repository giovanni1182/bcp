package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Usuario;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputPassword;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class SenhaUsuarioView extends PortalView {
	private Usuario usuario;

	private Entidade origemMenu;

	public SenhaUsuarioView(Usuario usuario, Entidade origemMenu)
			throws Exception {
		this.usuario = usuario;
		this.origemMenu = origemMenu;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);
		table.addSubtitle("Cambio de Contraseña");
		table.addHeader("Contraseña Actual:");
		table.addData(new InputPassword("senhaAtual", 16));
		table.addHeader("Nueva Contraseña:");
		Block novasSenhasBlock = new Block(Block.HORIZONTAL);
		novasSenhasBlock.add(new InputPassword("novaSenha1", 16));
		novasSenhasBlock.add(new Label("&nbsp;"));
		novasSenhasBlock.add(new InputPassword("novaSenha2", 16));
		table.addData(novasSenhasBlock);
		Action confirmarAction = new Action("atualizarSenhaUsuario");
		confirmarAction.add("id", this.usuario.obterId());
		table.addFooter(new Button("Aceptar", confirmarAction));
		Action cancelarAction = new Action("visualizarPaginaInicial");
		table.addFooter(new Button("Cancelar", cancelarAction));
		return table;
	}

	public String getSelectedGroup() throws Exception {
		return "Menu Principal";
	}

	public String getSelectedOption() throws Exception {
		return "Alterar Senha...";
	}

	public View getTitle() throws Exception {
		return new Label(this.usuario.obterNome());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gvs.crm.view.PortalView#getOrigemMenu()
	 */
	public Entidade getOrigemMenu() throws Exception {
		return this.origemMenu;
	}
}