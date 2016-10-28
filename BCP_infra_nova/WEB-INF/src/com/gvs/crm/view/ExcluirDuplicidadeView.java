package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class ExcluirDuplicidadeView extends PortalView {
	private Entidade entidade;

	public ExcluirDuplicidadeView(Entidade entidade) throws Exception {
		this.entidade = entidade;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);
		table.setWidth("100%");

		table.addSubtitle("Excluir Duplicidades");

		table.addHeader("Aseguradora:");
		table.add(new EntidadePopup("aseguradoraId", "aseguradoraNome", null,
				true));

		table.addHeader("Fecha:");
		table.add(new InputDate("data", null));

		Button excluirButton = new Button("Excluir", new Action(
				"excluirMovimentos"));
		excluirButton.getAction().add("id", this.entidade.obterId());

		table.addFooter(excluirButton);

		return table;
	}

	public String getSelectedGroup() throws Exception {
		return this.entidade.obterNome();
	}

	public String getSelectedOption() throws Exception {
		return "Menú Principal";
	}

	public View getTitle() throws Exception {
		return new Label("Excluir Duplicidade");
	}

	public Entidade getOrigemMenu() throws Exception {
		return this.entidade;
	}

}