package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.TipoFilialSelect;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputString;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class AseguradoraFilialView extends PortalView {

	private Aseguradora aseguradora;

	private Aseguradora.Filial filial;

	private boolean novo;

	public AseguradoraFilialView(Aseguradora aseguradora) throws Exception {
		this.aseguradora = aseguradora;
		this.novo = true;
	}

	public AseguradoraFilialView(Aseguradora.Filial filial) throws Exception {
		this.aseguradora = filial.obterAseguradora();
		this.filial = filial;
		this.novo = false;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);

		if (this.novo) {
			table.addHeader("Sucursales:");
			table.addData(new InputString("filial", null, 50));

			table.addHeader("Tipo:");
			table.addData(new TipoFilialSelect("tipo", ""));

			table.addHeader("Teléfono:");
			table.addData(new InputString("telefone", null, 18));

			table.addHeader("Ciudad:");
			table.addData(new InputString("cidade", null, 35));

			table.addHeader("Dirección:");
			table.addData(new InputText("endereco", null, 5, 70));

			table.addHeader("Email:");
			table.addData(new InputString("email", null, 30));

			Action incluirAction = new Action("incluirFilialAseguradora");
			incluirAction.add("entidadeId", this.aseguradora.obterId());
			Button incluirButton = new Button("Agregar", incluirAction);
			table.addFooter(incluirButton);
		} else {

			table.addHeader("Sucursales:");
			table.addData(new InputString("filial", this.filial.obterFilial(),
					50));

			table.addHeader("Tipo:");
			table
					.addData(new TipoFilialSelect("tipo", this.filial
							.obterTipo()));

			table.addHeader("Teléfono:");
			table.addData(new InputString("telefone", this.filial
					.obterTelefone(), 18));

			table.addHeader("Ciudad:");
			table.addData(new InputString("cidade", this.filial.obterCidade(),
					35));

			table.addHeader("Dirección:");
			table.addData(new InputText("endereco",
					this.filial.obterEndereco(), 5, 70));

			table.addHeader("Email:");
			table
					.addData(new InputString("email", this.filial.obterEmail(),
							30));

			Action atualizarAction = new Action("atualizarFilialAseguradora");
			atualizarAction.add("entidadeId", this.aseguradora.obterId());
			atualizarAction.add("id", this.filial.obterId());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			table.addFooter(atualizarButton);

			Action excluirAction = new Action("excluirFilial");
			excluirAction.add("entidadeId", this.aseguradora.obterId());
			excluirAction.add("id", this.filial.obterId());
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
					+ " - Nueva Sucursales");
		else
			return new Label(this.aseguradora.obterNome() + " - Sucursales");
	}

	public Entidade getOrigemMenu() throws Exception {
		return null;
	}
}