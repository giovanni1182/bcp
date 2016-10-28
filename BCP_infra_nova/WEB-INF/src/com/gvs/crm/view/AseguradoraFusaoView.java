package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class AseguradoraFusaoView extends PortalView {

	private Aseguradora aseguradora;

	private Aseguradora.Fusao fusao;

	private boolean novo;

	public AseguradoraFusaoView(Aseguradora aseguradora) throws Exception {
		this.aseguradora = aseguradora;
		this.novo = true;
	}

	public AseguradoraFusaoView(Aseguradora.Fusao fusao) throws Exception {
		this.aseguradora = fusao.obterAseguradora();
		this.fusao = fusao;
		this.novo = false;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);

		if (this.novo) {
			table.addHeader("Compañia:");
			table.addData(new InputString("empresa", null, 50));

			table.addHeader("Fecha de la Fusión:");
			table.addData(new InputDate("data", null));

			Action incluirAction = new Action("incluirFusaoAseguradora");
			incluirAction.add("entidadeId", this.aseguradora.obterId());
			Button incluirButton = new Button("Agregar", incluirAction);
			table.addFooter(incluirButton);
		} else {
			table.addHeader("Compañia:");
			table.addData(new InputString("empresa", this.fusao.obterEmpresa(),
					50));

			table.addHeader("Fecha de la Fusión:");
			table.addData(new InputDate("data", this.fusao.obterDatausao()));

			Action atualizarAction = new Action("atualizarFusaoAseguradora");
			atualizarAction.add("entidadeId", this.aseguradora.obterId());
			atualizarAction.add("id", this.fusao.obterId());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			table.addFooter(atualizarButton);

			Action excluirAction = new Action("excluirFusao");
			excluirAction.add("entidadeId", this.aseguradora.obterId());
			excluirAction.add("id", this.fusao.obterId());
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
			return new Label(this.aseguradora.obterNome() + " - Nueva Fusión");
		else
			return new Label(this.aseguradora.obterNome() + " - Fusión");
	}

	public Entidade getOrigemMenu() throws Exception {
		return null;
	}
}