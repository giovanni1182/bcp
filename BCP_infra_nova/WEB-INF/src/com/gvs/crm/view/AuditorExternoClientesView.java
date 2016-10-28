package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import com.gvs.crm.model.AuditorExterno;

import infra.control.Action;
import infra.view.Button;
import infra.view.Image;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class AuditorExternoClientesView extends Table {
	public AuditorExternoClientesView(AuditorExterno auditor) throws Exception {
		super(7);

		this.addSubtitle("Clientes");

		this.setWidth("100%");

		this.addStyle(super.STYLE_ALTERNATE);

		this.add("");
		this.add("");
		this.addHeader("Aseguradora");
		this.addHeader("Honorarios");
		this.addHeader("Fecha Contrato Inicio");
		this.addHeader("Fecha Contrato Cierre");
		this.addHeader("Outros Servicos");

		for (Iterator i = auditor.obterClientes().iterator(); i.hasNext();) {
			AuditorExterno.Cliente cliente = (AuditorExterno.Cliente) i.next();

			if (auditor.permiteAtualizar()) {
				Action visualizarAction = new Action("visualizarCliente");
				visualizarAction.add("entidadeId", auditor.obterId());
				visualizarAction.add("id", cliente.obterId());
				this.add(new Link(new Image("replace.gif"), visualizarAction));

				Action excluirAction = new Action("excluirCliente");
				excluirAction.add("entidadeId", auditor.obterId());
				excluirAction.add("id", cliente.obterId());
				excluirAction.setConfirmation("Confirma exclusión ?");
				this.add(new Link(new Image("delete.gif"), excluirAction));
			} else {
				this.add("");
				this.add("");
			}

			this.add(cliente.obterAseguradora().obterNome());

			this.add(new Label(cliente.obterHonorarios()));

			String dataInicio = new SimpleDateFormat("dd/MM/yyyy")
					.format(cliente.obterDataInicio());
			String dataFim = new SimpleDateFormat("dd/MM/yyyy").format(cliente
					.obterDataFim());

			this.add(dataInicio);

			this.add(dataFim);

			if (cliente.obterOutrosServicos().equals("Si")) {
				Link link2 = new Link(cliente.obterOutrosServicos(),
						new Action("visualizarServicos"));
				link2.getAction().add("auditorId", auditor.obterId());
				link2.getAction().add("aseguradoraId",
						cliente.obterAseguradora().obterId());

				this.add(link2);
			} else
				this.add(cliente.obterOutrosServicos());
		}

		this.setNextColSpan(this.getColumns());
		this.add(new Space());

		Button novoLink = new Button("Nuevo", new Action("novoCliente"));
		novoLink.getAction().add("entidadeId", auditor.obterId());
		this.setNextColSpan(this.getColumns());
		this.add(novoLink);
	}
}