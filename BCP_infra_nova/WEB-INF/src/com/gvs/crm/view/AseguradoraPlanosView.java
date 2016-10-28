package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Plano;

import infra.control.Action;
import infra.view.Button;
import infra.view.Image;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class AseguradoraPlanosView extends Table {
	public AseguradoraPlanosView(Aseguradora aseguradora) throws Exception {
		super(6);

		this.addSubtitle("Planes");

		this.addStyle(super.STYLE_ALTERNATE);

		this.setWidth("100%");

		this.add("");
		this.add("");
		this.addHeader("Plan");
		this.addHeader("Nº de la Resolución");
		this.addHeader("Fecha de la Resolución");
		this.addHeader("Situación");

		for (Iterator i = aseguradora.obterPlanos().iterator(); i.hasNext();) {
			Plano plano = (Plano) i.next();

			if (aseguradora.permiteAtualizar()) {
				Action visualizarAction = new Action("visualizarEvento");
				visualizarAction.add("id", plano.obterId());
				this.add(new Link(new Image("replace.gif"), visualizarAction));

				Action excluirAction = new Action("excluirEvento");
				excluirAction.add("id", plano.obterId());
				excluirAction.setConfirmation("Confirma exclusión ?");
				this.add(new Link(new Image("delete.gif"), excluirAction));
			} else {
				this.add("");
				this.add("");
			}

			this.add(plano.obterPlano());

			this.add(plano.obterResolucao());

			String data = new SimpleDateFormat("dd/MM/yyyy").format(plano
					.obterDataResolucao());

			this.add(data);

			this.add(plano.obterSituacao());
		}

		this.setNextColSpan(this.getColumns());
		this.add(new Space());

		Button novoLink = new Button("Nuevo Plan", new Action("novoEvento"));
		novoLink.getAction().add("origemId", aseguradora.obterId());
		novoLink.getAction().add("classe", "plano");
		this.setNextColSpan(this.getColumns());
		this.add(novoLink);
	}
}