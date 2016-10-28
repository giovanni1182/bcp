package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputInteger;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class AcionistaAseguradoraView extends PortalView {

	private Aseguradora aseguradora;

	private Aseguradora.Acionista acionista;

	private boolean novo;

	public AcionistaAseguradoraView(Aseguradora aseguradora) throws Exception {
		this.aseguradora = aseguradora;
		this.novo = true;
	}

	public AcionistaAseguradoraView(Aseguradora.Acionista acionista)
			throws Exception {
		this.aseguradora = acionista.obterAseguradora();
		this.acionista = acionista;
		this.novo = false;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);

		if (this.novo) {
			table.addHeader("Accionista:");
			table.addData(new InputString("acionista", null, 50));

			table.addHeader("Cantidad de Acciones:");
			table.addData(new InputInteger("quantidade", 0, 6));

			table.addHeader("Tipo de Acciones:");
			table.addData(new InputString("tipo", null, 50));

			Action incluirAction = new Action("incluirAcionistaAseguradora");
			incluirAction.add("entidadeId", this.aseguradora.obterId());
			Button incluirButton = new Button("Agregar", incluirAction);
			table.addFooter(incluirButton);
		} else {
			table.addHeader("Accionista:");
			table.addData(new InputString("acionista", this.acionista
					.obterAcionista(), 50));

			table.addHeader("Cantidad de Acciones:");
			table.addData(new InputInteger("quantidade", this.acionista
					.obterquantidade(), 6));

			table.addHeader("Tipo de Acciones:");
			table.addData(new InputString("tipo", this.acionista.obtertipo(),
					50));

			Action atualizarAction = new Action("atualizarAcionistaAseguradora");
			atualizarAction.add("entidadeId", this.aseguradora.obterId());
			atualizarAction.add("id", this.acionista.obterId());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			table.addFooter(atualizarButton);

			Action excluirAction = new Action("excluirAcionista");
			excluirAction.add("entidadeId", this.aseguradora.obterId());
			excluirAction.add("id", this.acionista.obterId());
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
					+ " - Nuevo Accionista");
		else
			return new Label(this.aseguradora.obterNome() + " - Accionista");
	}

	public Entidade getOrigemMenu() throws Exception {
		return null;
	}

}