package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.SinalSelect;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Parametro;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputString;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ConsistenciaView extends PortalView {

	private Entidade entidade;

	private Parametro.Consistencia consistencia;

	private Entidade origemMenu;

	private boolean novo;

	public ConsistenciaView(Parametro.Consistencia consistencia,
			Entidade origemMenu) throws Exception {
		this.entidade = consistencia.obterEntidade();
		this.consistencia = consistencia;
		this.origemMenu = origemMenu;
		this.novo = false;
	}

	public ConsistenciaView(Entidade entidade, Entidade origemMenu)
			throws Exception {
		this.entidade = entidade;
		this.origemMenu = origemMenu;
		this.novo = true;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(5);

		table.addHeader("Regra");
		table.addHeader("Sequencial");
		table.addHeader("Operando1");
		table.addHeader("Operador");
		table.addHeader("Operando2");

		if (novo)
			table.addData(new Label(""));
		else
			table.addData(new Label(consistencia.obterRegra()));

		if (novo)
			table.addData("");
		else
			table.addData(new Label(consistencia.obterSequencial()));

		if (novo)
			table.addData(new InputString("operando1", null, 20));
		else
			table.addData(new InputString("operando1", consistencia
					.obterOperando1(), 20));

		if (novo)
			table.addData(new SinalSelect("operador", null));
		else
			table.addData(new SinalSelect("operador", consistencia
					.obterOperador()));

		if (novo)
			table.addData(new InputString("operando2", null, 20));
		else
			table.addData(new InputString("operando2", consistencia
					.obterOperando2(), 20));

		table.addHeader("Mensagem");
		table.setNextColSpan(table.getColumns());

		if (this.novo)
			table.add(new InputText("mensagem", null, 2, 80));
		else
			table.add(new InputText("mensagem", consistencia.obterMensagem(),
					2, 80));

		Action incluirAction = new Action("incluirConsistencia");
		incluirAction.add("entidadeId", this.entidade.obterId());

		Button incluirButton = new Button("Agregar", incluirAction);
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add("");
		table.add(incluirButton);

		if (!this.novo) {
			Action atualizarAction = new Action("atualizarConsistencia");
			atualizarAction.add("entidadeId", this.entidade.obterId());
			atualizarAction.add("id", this.consistencia.obterSequencial());
			atualizarAction.add("regra", this.consistencia.obterRegra());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			table.add(atualizarButton);

			Action excluirAction = new Action("excluirConsistencia");
			excluirAction.add("entidadeId", this.entidade.obterId());
			excluirAction.add("id", this.consistencia.obterSequencial());
			excluirAction.add("regra", this.consistencia.obterRegra());
			excluirAction.setConfirmation("Confirma exclusión ?");
			Button excluirButton = new Button("Excluir", excluirAction);
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(excluirButton);
		}

		Action voltarAction = new Action("visualizarDetalhesEntidade");
		voltarAction.add("id", this.entidade.obterId());
		Button voltarButton = new Button("Volver", voltarAction);
		table.add(voltarButton);
		table.add("");

		if (this.novo) {
			table.add("");
			table.add("");
		}

		table.setNextColSpan(table.getColumns());
		table.addData(new Space());

		table.setNextColSpan(table.getColumns());
		table.addData(new ConsistenciasView(entidade));

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
			return new Label(this.entidade.obterNome()
					+ " - Nueva Consistencia");
		else
			return new Label(this.entidade.obterNome() + "- Consistencia");
	}

	public Entidade getOrigemMenu() throws Exception {
		return this.origemMenu;
	}
}

