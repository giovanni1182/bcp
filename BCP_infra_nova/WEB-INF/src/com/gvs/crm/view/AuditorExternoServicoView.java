package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AuditorExterno;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputDouble;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class AuditorExternoServicoView extends PortalView {

	private AuditorExterno auditor;

	private Aseguradora aseguradora;

	private AuditorExterno.Servico servico;

	private boolean novo;

	public AuditorExternoServicoView(AuditorExterno auditor,
			Aseguradora aseguradora) throws Exception {
		this.auditor = auditor;
		this.aseguradora = aseguradora;
		this.novo = true;
	}

	public AuditorExternoServicoView(AuditorExterno.Servico servico)
			throws Exception {
		this.auditor = servico.obterAuditor();
		this.aseguradora = servico.obterAseguradora();
		this.servico = servico;
		this.novo = false;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);

		if (this.novo) {
			table.addHeader("Otro Servicio:");
			table.addData(new InputString("servico", null, 40));

			table.addHeader("Fecha Contrato:");
			table.addData(new InputDate("dataContrato", null));

			table.addHeader("Honorarios:");
			table.addData(new InputDouble("honorarios", 0, 10));

			table.addHeader("Periodo:");
			table.addData(new InputString("periodo", null, 40));

			Action incluirAction = new Action("incluirServico");
			incluirAction.add("auditorId", this.auditor.obterId());
			incluirAction.add("aseguradoraId", this.aseguradora.obterId());
			Button incluirButton = new Button("Agregar", incluirAction);
			table.addFooter(incluirButton);
		} else {

			table.addHeader("Otro Servicio:");
			table.addData(new InputString("servico", this.servico
					.obterServico(), 40));

			table.addHeader("Fecha Contrato:");
			table.addData(new InputDate("dataContrato", this.servico
					.obterDataContrato()));

			table.addHeader("Honorarios:");
			table.addData(new InputDouble("honorarios", this.servico
					.obterHonorarios(), 10));

			table.addHeader("Periodo:");
			table.addData(new InputString("periodo", this.servico
					.obterPeriodo(), 40));

			Action atualizarAction = new Action("atualizarServico");
			atualizarAction.add("auditorId", this.auditor.obterId());
			atualizarAction.add("aseguradoraId", this.aseguradora.obterId());
			atualizarAction.add("id", this.servico.obterId());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			table.addFooter(atualizarButton);

			Action excluirAction = new Action("excluirServico");
			excluirAction.add("auditorId", this.auditor.obterId());
			excluirAction.add("aseguradoraId", this.aseguradora.obterId());
			excluirAction.add("id", this.servico.obterId());
			excluirAction.setConfirmation("Confirma exclusión ?");
			Button excluirButton = new Button("Eliminar", excluirAction);
			table.addFooter(excluirButton);
		}

		Action cancelarAction = new Action("visualizarServicos");
		cancelarAction.add("auditorId", this.auditor.obterId());
		cancelarAction.add("aseguradoraId", this.aseguradora.obterId());
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
			return new Label(this.auditor.obterNome() + " - Nuevo Servicio");
		else
			return new Label(this.auditor.obterNome() + " - Servicio");
	}

	public Entidade getOrigemMenu() throws Exception {
		return null;
	}
}