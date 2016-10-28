package com.gvs.crm.view;

import java.util.Iterator;

import com.gvs.crm.model.Aseguradora;

import infra.control.Action;
import infra.view.Button;
import infra.view.Image;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class AseguradoraCoaseguradorasView extends Table {
	public AseguradoraCoaseguradorasView(Aseguradora aseguradora)
			throws Exception {
		super(3);

		this.addSubtitle("Coaseguradoras");

		this.addStyle(super.STYLE_ALTERNATE);

		this.setWidth("40%");

		this.add("");
		this.add("");
		this.addHeader("Grupo Coasegurador");

		for (Iterator i = aseguradora.obterCoaseguradores().iterator(); i
				.hasNext();) {
			Aseguradora.Coasegurador coasegurador = (Aseguradora.Coasegurador) i
					.next();

			if (aseguradora.permiteAtualizar()) {
				Action visualizarAction = new Action("visualizarCoasegurador");
				visualizarAction.add("entidadeId", aseguradora.obterId());
				visualizarAction.add("id", coasegurador.obterId());
				this.add(new Link(new Image("replace.gif"), visualizarAction));

				Action excluirAction = new Action("excluirCoasegurador");
				excluirAction.add("entidadeId", aseguradora.obterId());
				excluirAction.add("id", coasegurador.obterId());
				excluirAction.setConfirmation("Confirma exclusión ?");
				this.add(new Link(new Image("delete.gif"), excluirAction));
			} else {
				this.add("");
				this.add("");
			}

			Link link = new Link(coasegurador.obterCodigo(), new Action(
					"listarCoaseguradoras"));
			link.getAction().add("entidadeId", aseguradora.obterId());
			link.getAction().add("codigo", coasegurador.obterCodigo());

			this.add(link);
		}

		this.setNextColSpan(this.getColumns());
		this.add(new Space());

		Button novoLink = new Button("Nuevo", new Action("novoCoasegurador"));
		novoLink.getAction().add("entidadeId", aseguradora.obterId());
		this.setNextColSpan(this.getColumns());
		this.add(novoLink);
	}
}