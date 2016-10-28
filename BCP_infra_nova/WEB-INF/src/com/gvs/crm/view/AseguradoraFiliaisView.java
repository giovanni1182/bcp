package com.gvs.crm.view;

import java.util.Iterator;

import com.gvs.crm.model.Aseguradora;

import infra.control.Action;
import infra.view.Button;
import infra.view.Image;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class AseguradoraFiliaisView extends Table {
	public AseguradoraFiliaisView(Aseguradora aseguradora) throws Exception {
		super(8);

		this.addSubtitle("Sucursales");

		this.addStyle(super.STYLE_ALTERNATE);

		this.setWidth("100%");

		this.add("");
		this.add("");
		this.addHeader("Nombre");
		this.addHeader("Tipo");
		this.addHeader("Teléfono");
		this.addHeader("Ciudad");
		this.addHeader("Dirección");
		this.addHeader("Email");

		for (Iterator i = aseguradora.obterFiliais().iterator(); i.hasNext();) {
			Aseguradora.Filial filial = (Aseguradora.Filial) i.next();

			if (aseguradora.permiteAtualizar()) {
				Action visualizarAction = new Action("visualizarFilial");
				visualizarAction.add("entidadeId", aseguradora.obterId());
				visualizarAction.add("id", filial.obterId());
				this.add(new Link(new Image("replace.gif"), visualizarAction));

				Action excluirAction = new Action("excluirFilial");
				excluirAction.add("entidadeId", aseguradora.obterId());
				excluirAction.add("id", filial.obterId());
				excluirAction.setConfirmation("Confirma exclusión ?");
				this.add(new Link(new Image("delete.gif"), excluirAction));
			} else {
				this.add("");
				this.add("");
			}

			this.add(filial.obterFilial());
			this.add(filial.obterTipo());
			this.add(filial.obterTelefone());
			this.add(filial.obterCidade());
			this.add(filial.obterEndereco());
			this.add(filial.obterEmail());
		}

		this.setNextColSpan(this.getColumns());
		this.add(new Space());

		Button novoLink = new Button("Nueva", new Action("novaFilial"));
		novoLink.getAction().add("entidadeId", aseguradora.obterId());
		this.setNextColSpan(this.getColumns());
		this.add(novoLink);
	}
}