package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputInteger;
import infra.view.InputString;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class ReaseguradoraAseguradoraView extends PortalView {
	private Aseguradora aseguradora;

	private Aseguradora.Reaseguradora reaseguradora;

	private boolean novo;

	public ReaseguradoraAseguradoraView(Aseguradora aseguradora)
			throws Exception {
		this.aseguradora = aseguradora;
		this.novo = true;
	}

	public ReaseguradoraAseguradoraView(Aseguradora.Reaseguradora reaseguradora)
			throws Exception {
		this.aseguradora = reaseguradora.obterAseguradora();
		this.reaseguradora = reaseguradora;
		this.novo = false;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);

		if (this.novo) {
			table.addHeader("Reaseguradora:");
			table.addData(new EntidadePopup("reaseguradoraId",
					"reaseguradoraNome", null, "reaseguradora", true));

			table.addHeader("Corredora:");
			table.addData(new EntidadePopup("corretoraId", "corretoraNome",
					null, "corretora", true));

			table.addHeader("Tipo de Contrato:");
			table.addData(new InputString("tipoContrato", null, 50));

			table.addHeader("Fecha Vencimiento:");
			table.addData(new InputDate("dataVencimento", null));

			table.addHeader("% Participación:");
			table.addData(new InputInteger("participacao", 0, 6));

			table.addHeader("Observaciones:");
			table.addData(new InputText("observacao", null, 5, 80));

			Action incluirAction = new Action("incluirReaseguradoraAseguradora");
			incluirAction.add("entidadeId", this.aseguradora.obterId());
			Button incluirButton = new Button("Agregar", incluirAction);
			table.addFooter(incluirButton);
		} else {
			table.addHeader("Reaseguradora:");
			table.addData(new EntidadePopup("reaseguradoraId",
					"reaseguradoraNome", this.reaseguradora
							.obterReaseguradora(), "reaseguradora", true));

			table.addHeader("Corredora:");
			table.addData(new EntidadePopup("corretoraId", "corretoraNome",
					this.reaseguradora.obterCorretora(), true));

			table.addHeader("Tipo de Contrato:");
			table.addData(new InputString("tipoContrato", this.reaseguradora
					.obterTipoContrato(), 50));

			table.addHeader("Fecha Vencimiento:");
			table.addData(new InputDate("dataVencimento", this.reaseguradora
					.obterDataVencimento()));

			table.addHeader("% Participación:");
			table.addData(new InputInteger("participacao", this.reaseguradora
					.obterParticipacao(), 6));

			table.addHeader("Observaciones:");
			table.addData(new InputText("observacao", this.reaseguradora
					.obterObservacao(), 5, 80));

			Action atualizarAction = new Action(
					"atualizarReaseguradoraAseguradora");
			atualizarAction.add("entidadeId", this.aseguradora.obterId());
			atualizarAction.add("id", this.reaseguradora.obterId());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			table.addFooter(atualizarButton);

			Action excluirAction = new Action("excluirReaseguradora");
			excluirAction.add("entidadeId", this.aseguradora.obterId());
			excluirAction.add("id", this.reaseguradora.obterId());
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
					+ " - Nueva Reaseguradora");
		else
			return new Label(this.aseguradora.obterNome() + " - Reaseguradora");
	}

	public Entidade getOrigemMenu() throws Exception {
		return null;
	}

}