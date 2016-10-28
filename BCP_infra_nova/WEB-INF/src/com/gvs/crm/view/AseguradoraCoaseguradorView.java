package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class AseguradoraCoaseguradorView extends PortalView {
	private Aseguradora aseguradora;

	private Aseguradora.Coasegurador coasegurador;

	private boolean novo;

	public AseguradoraCoaseguradorView(Aseguradora aseguradora)
			throws Exception {
		this.aseguradora = aseguradora;
		this.novo = true;
	}

	public AseguradoraCoaseguradorView(Aseguradora.Coasegurador coasegurador)
			throws Exception {
		this.aseguradora = coasegurador.obterAseguradora();
		this.coasegurador = coasegurador;
		this.novo = false;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);

		if (this.novo) {
			table.addHeader("Grupo:");
			table.addData(new InputString("grupo", null, 50));

			Action incluirAction = new Action("incluirCoaseguradorAseguradora");
			incluirAction.add("entidadeId", this.aseguradora.obterId());
			Button incluirButton = new Button("Agregar", incluirAction);
			table.addFooter(incluirButton);
		} else {
			table.addHeader("Grupo:");
			table.addData(new InputString("grupo", this.coasegurador
					.obterCodigo(), 50));

			Action atualizarAction = new Action(
					"atualizarCoaseguradorAseguradora");
			atualizarAction.add("entidadeId", this.aseguradora.obterId());
			atualizarAction.add("id", this.coasegurador.obterId());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			table.addFooter(atualizarButton);

			Action excluirAction = new Action("excluirCoasegurador");
			excluirAction.add("entidadeId", this.aseguradora.obterId());
			excluirAction.add("id", this.coasegurador.obterId());
			excluirAction.setConfirmation("Confirma exclusión ?");
			Button excluirButton = new Button("Eliminar", excluirAction);
			table.addFooter(excluirButton);
		}

		Action cancelarAction = new Action("visualizarDetalhesEntidade");
		cancelarAction.add("id", this.aseguradora.obterId());
		table.addFooter(new Button("Volver", cancelarAction));
		return table;
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception {
		if (this.novo)
			return new Label(this.aseguradora.obterNome()
					+ " - Nuevo Coasegurador");
		else
			return new Label(this.aseguradora.obterNome() + " - Coasegurador");
	}

	public Entidade getOrigemMenu() throws Exception {
		return null;
	}
}