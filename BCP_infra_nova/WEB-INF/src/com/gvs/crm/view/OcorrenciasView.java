package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Produto;

import infra.control.Action;
import infra.security.User;
import infra.view.Label;
import infra.view.View;

public class OcorrenciasView extends PortalView {
	private Produto produto;

	public OcorrenciasView(Produto produto) throws Exception {
		this.produto = produto;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Action action = new Action("visualizarOcorrencias");
		action.add("id", this.produto.obterId());
		return new EventosView(this.produto.obterOcorrencias(), action);
	}

	public String getSelectedGroup() throws Exception {
		return this.produto.obterNome();
	}

	public String getSelectedOption() throws Exception {
		return "Ocorrências";
	}

	public View getTitle() throws Exception {
		return new Label(this.produto.obterNome() + " - Ocorrências");
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