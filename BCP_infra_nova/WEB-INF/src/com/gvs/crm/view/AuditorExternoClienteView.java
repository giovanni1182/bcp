package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.OutrosServicosSelect;
import com.gvs.crm.model.AuditorExterno;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputDouble;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class AuditorExternoClienteView extends PortalView {

	private AuditorExterno auditor;

	private AuditorExterno.Cliente cliente;

	private boolean novo;

	public AuditorExternoClienteView(AuditorExterno auditor) throws Exception {
		this.auditor = auditor;
		this.novo = true;
	}

	public AuditorExternoClienteView(AuditorExterno.Cliente cliente)
			throws Exception {
		this.auditor = cliente.obterAuditor();
		this.cliente = cliente;
		this.novo = false;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);

		if (this.novo) {
			table.addHeader("Aseguradora:");
			table.addData(new EntidadePopup("aseguradoraId", "aseguradoraNome",
					null, "aseguradora", true));

			table.addHeader("Honorarios:");
			table.addData(new InputDouble("honorarios", 0, 10));

			table.addHeader("Fecha Contrato Inicio:");
			table.addData(new InputDate("dataInicio", null));

			table.addHeader("Fecha Contrato Cierre:");
			table.addData(new InputDate("dataFim", null));

			table.addHeader("Outros Serviços:");
			table.add(new OutrosServicosSelect("outrosServicos", ""));

			Action incluirAction = new Action("incluirCliente");
			incluirAction.add("entidadeId", this.auditor.obterId());
			Button incluirButton = new Button("Agregar", incluirAction);
			table.addFooter(incluirButton);
		} else {

			table.addHeader("Aseguradora:");
			table.addData(new EntidadePopup("aseguradoraId", "aseguradoraNome",
					this.cliente.obterAseguradora(), "aseguradora", true));

			table.addHeader("Honorarios:");
			table.addData(new InputDouble("honorarios", this.cliente
					.obterHonorarios(), 10));

			table.addHeader("Fecha Contrato Inicio:");
			table.addData(new InputDate("dataInicio", this.cliente
					.obterDataInicio()));

			table.addHeader("Fecha Contrato Cierre:");
			table
					.addData(new InputDate("dataFim", this.cliente
							.obterDataFim()));

			table.addHeader("Outros Serviços:");
			table.add(new OutrosServicosSelect("outrosServicos", this.cliente
					.obterOutrosServicos()));

			Action atualizarAction = new Action("atualizarCliente");
			atualizarAction.add("entidadeId", this.auditor.obterId());
			atualizarAction.add("id", this.cliente.obterId());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			table.addFooter(atualizarButton);

			Action excluirAction = new Action("excluirCliente");
			excluirAction.add("entidadeId", this.auditor.obterId());
			excluirAction.add("id", this.cliente.obterId());
			excluirAction.setConfirmation("Confirma exclusión ?");
			Button excluirButton = new Button("Eliminar", excluirAction);
			table.addFooter(excluirButton);
		}

		Action cancelarAction = new Action("visualizarDetalhesEntidade");
		cancelarAction.add("id", this.auditor.obterId());
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
			return new Label(this.auditor.obterNome() + " - Nuevo Cliente");
		else
			return new Label(this.auditor.obterNome() + " - Cliente");
	}

	public Entidade getOrigemMenu() throws Exception {
		return null;
	}

}