package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AuditorExterno;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Image;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class AuditorExternoServicosView extends PortalView {
	private AuditorExterno auditor;

	private Aseguradora aseguradora;

	public AuditorExternoServicosView(AuditorExterno auditor,
			Aseguradora aseguradora) throws Exception {
		this.auditor = auditor;
		this.aseguradora = aseguradora;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(6);

		table.addSubtitle("Otros Servicios");

		table.addStyle(Table.STYLE_ALTERNATE);

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Cliente: " + aseguradora.obterNome());

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		table.add("");
		table.add("");
		table.addHeader("Otros Servicios");
		table.addHeader("Fecha Contrato");
		table.addHeader("Honorarios");
		table.addHeader("Periodo");

		for (Iterator i = auditor.obterServicos(aseguradora).iterator(); i
				.hasNext();) {
			AuditorExterno.Servico servico = (AuditorExterno.Servico) i.next();

			if (auditor.permiteAtualizar()) {
				Action visualizarAction = new Action("visualizarServico");
				visualizarAction.add("auditorId", servico.obterAuditor()
						.obterId());
				visualizarAction.add("aseguradoraId", servico
						.obterAseguradora().obterId());
				visualizarAction.add("id", servico.obterId());
				table.add(new Link(new Image("replace.gif"), visualizarAction));

				Action excluirAction = new Action("excluirServico");
				excluirAction
						.add("auditorId", servico.obterAuditor().obterId());
				excluirAction.add("aseguradoraId", servico.obterAseguradora()
						.obterId());
				excluirAction.add("id", servico.obterId());
				excluirAction.setConfirmation("Confirma exclusión ?");
				table.add(new Link(new Image("delete.gif"), excluirAction));
			} else {
				table.add("");
				table.add("");
			}

			table.add(servico.obterServico());

			String data = new SimpleDateFormat("dd/MM/yyyy").format(servico
					.obterDataContrato());

			table.add(data);

			table.add(new Label(servico.obterHonorarios()));

			table.add(servico.obterPeriodo());
		}

		Button novoLink = new Button("Nuevo", new Action("novoServico"));
		novoLink.getAction().add("auditorId", auditor.obterId());
		novoLink.getAction().add("aseguradoraId", aseguradora.obterId());
		table.addFooter(novoLink);

		Button voltarLink = new Button("Volver", new Action(
				"visualizarDetalhesEntidade"));
		voltarLink.getAction().add("id", auditor.obterId());
		table.addFooter(voltarLink);

		return table;
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception {
		return new Label("Servicios " + this.aseguradora.obterNome());
	}

	public Entidade getOrigemMenu() throws Exception {
		return null;
	}
}