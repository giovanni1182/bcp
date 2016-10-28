package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;

import com.gvs.crm.model.Aseguradora;

import infra.control.Action;
import infra.view.Button;
import infra.view.Image;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class ReaseguradorasAseguradoraView extends Table {
	public ReaseguradorasAseguradoraView(Aseguradora aseguradora)
			throws Exception {
		super(7);

		this.addSubtitle("Reaseguradoras");
		this.addStyle(super.STYLE_ALTERNATE);
		this.setWidth("100%");

		this.add("");
		this.add("");
		this.addHeader("Reaseguradoras");
		this.addHeader("Corredora");
		this.addHeader("Tipo de Contrato");
		this.addHeader("Fecha Vencimiento");
		this.addHeader("% Participación");

		Collection reaseguradoras = aseguradora.obterReaseguradoras();

		for (Iterator i = reaseguradoras.iterator(); i.hasNext();) {
			Aseguradora.Reaseguradora reaseguradora = (Aseguradora.Reaseguradora) i
					.next();

			if (aseguradora.permiteAtualizar()) {
				Action visualizarAction = new Action("visualizarReaseguradora");
				visualizarAction.add("entidadeId", aseguradora.obterId());
				visualizarAction.add("id", reaseguradora.obterId());
				this.add(new Link(new Image("replace.gif"), visualizarAction));

				Action excluirAction = new Action("excluirReaseguradora");
				excluirAction.add("entidadeId", aseguradora.obterId());
				excluirAction.add("id", reaseguradora.obterId());
				excluirAction.setConfirmation("Confirma exclusión ?");
				this.add(new Link(new Image("delete.gif"), excluirAction));
			} else {
				this.add("");
				this.add("");
			}

			this.add(reaseguradora.obterReaseguradora().obterNome());
			this.add(reaseguradora.obterCorretora().obterNome());
			this.add(reaseguradora.obterTipoContrato());

			String data = new SimpleDateFormat("dd/MM/yyyy")
					.format(reaseguradora.obterDataVencimento());
			this.add(data);

			this.add(new Label(reaseguradora.obterParticipacao()));
		}

		this.setNextColSpan(this.getColumns());
		this.add(new Space());

		Button novoLink = new Button("Nueva", new Action("novaReaseguradora"));
		novoLink.getAction().add("entidadeId", aseguradora.obterId());
		this.setNextColSpan(this.getColumns());
		this.add(novoLink);
	}
}