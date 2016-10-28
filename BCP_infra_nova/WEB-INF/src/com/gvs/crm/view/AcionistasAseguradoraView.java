package com.gvs.crm.view;

import java.util.Iterator;

import com.gvs.crm.model.Aseguradora;

import infra.control.Action;
import infra.view.Button;
import infra.view.Image;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class AcionistasAseguradoraView extends Table {
	public AcionistasAseguradoraView(Aseguradora aseguradora) throws Exception {
		super(5);

		this.addSubtitle("Accionistas");

		this.addStyle(super.STYLE_ALTERNATE);

		this.setWidth("80%");

		this.add("");
		this.add("");
		this.addHeader("Nombre");
		this.addHeader("Cantidad de Acciones");
		this.addHeader("Tipo de Acciones");

		for (Iterator i = aseguradora.obterAcionistas().iterator(); i.hasNext();) {
			Aseguradora.Acionista acionista = (Aseguradora.Acionista) i.next();

			if (aseguradora.permiteAtualizar()) {
				Action visualizarAction = new Action("visualizarAcionista");
				visualizarAction.add("entidadeId", aseguradora.obterId());
				visualizarAction.add("id", acionista.obterId());
				this.add(new Link(new Image("replace.gif"), visualizarAction));

				Action excluirAction = new Action("excluirAcionista");
				excluirAction.add("entidadeId", aseguradora.obterId());
				excluirAction.add("id", acionista.obterId());
				excluirAction.setConfirmation("Confirma exclusión ?");
				this.add(new Link(new Image("delete.gif"), excluirAction));
			} else {
				this.add("");
				this.add("");
			}

			this.add(acionista.obterAcionista());
			this.add(new Label(acionista.obterquantidade()));
			this.add(acionista.obtertipo());
		}

		this.setNextColSpan(this.getColumns());
		this.add(new Space());

		Button novoLink = new Button("Nuevo", new Action("novoAcionista"));
		novoLink.getAction().add("entidadeId", aseguradora.obterId());
		this.setNextColSpan(this.getColumns());
		this.add(novoLink);
	}
}