package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import com.gvs.crm.model.Aseguradora;

import infra.control.Action;
import infra.view.Button;
import infra.view.Image;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class AseguradoraFusoesView extends Table {
	public AseguradoraFusoesView(Aseguradora aseguradora) throws Exception {
		super(4);

		this.addSubtitle("Fusiones");

		this.addStyle(super.STYLE_ALTERNATE);

		this.setWidth("60%");

		this.add("");
		this.add("");
		this.addHeader("Compañia");
		this.addHeader("Fecha de la Fusión");

		for (Iterator i = aseguradora.obterFusoes().iterator(); i.hasNext();) {
			Aseguradora.Fusao fusao = (Aseguradora.Fusao) i.next();

			if (aseguradora.permiteAtualizar()) {
				Action visualizarAction = new Action("visualizarFusao");
				visualizarAction.add("entidadeId", aseguradora.obterId());
				visualizarAction.add("id", fusao.obterId());
				this.add(new Link(new Image("replace.gif"), visualizarAction));

				Action excluirAction = new Action("excluirFusao");
				excluirAction.add("entidadeId", aseguradora.obterId());
				excluirAction.add("id", fusao.obterId());
				excluirAction.setConfirmation("Confirma exclusión ?");
				this.add(new Link(new Image("delete.gif"), excluirAction));
			}

			this.add(fusao.obterEmpresa());

			String data = new SimpleDateFormat("dd/MM/yyyy").format(fusao
					.obterDatausao());

			this.add(data);
		}

		this.setNextColSpan(this.getColumns());
		this.add(new Space());

		Button novoLink = new Button("Nueva", new Action("novaFusao"));
		novoLink.getAction().add("entidadeId", aseguradora.obterId());
		this.setNextColSpan(this.getColumns());
		this.add(novoLink);
	}
}