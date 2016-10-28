package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EventoLink;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Participacao;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ParticipacaoView extends EventoAbstratoView {
	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		Participacao participacao = (Participacao) this.obterEvento();
		Evento superior = participacao.obterSuperior();

		Table table = new Table(2);
		//table.setWidth("100%");

		if (superior != null) {
			table.addHeader("Superior:");
			table.add(new EventoLink(superior));
		}
		table.addHeader("Evento Atado:");
		table.add(new EventoSumarioView(participacao.obterSuperior()));

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		if (participacao.permiteAceitar()) {
			Button aceitarButton = new Button("Aceptar", new Action(
					"aceitarParticipacao"));
			aceitarButton.getAction().add("id", participacao.obterId());
			aceitarButton.getAction().add("view", true);
			table.addFooter(aceitarButton);
		}

		if (participacao.permiteRejeitar()) {
			Button recusarButton = new Button("Recusar", new Action(
					"recusarParticipacao"));
			recusarButton.getAction().add("id", participacao.obterId());
			recusarButton.getAction().add("view", true);
			table.addFooter(recusarButton);
		}

		if (participacao.permiteEncaminhar()) {
			Button encaminharButton = new Button("Remitir", new Action(
					"encaminharEvento"));
			encaminharButton.getAction().add("view", true);
			encaminharButton.getAction().add("id", participacao.obterId());
			encaminharButton.getAction().add("origemMenuId",
					this.obterOrigemMenu().obterId());
			table.addFooter(encaminharButton);
		}

		Button voltarButton = new Button("Volver", new Action(
				"visualizarPaginaInicial"));
		voltarButton.getAction().add("origemMenuId",
				participacao.obterOrigem().obterId());
		table.addFooter(voltarButton);

		return table;
	}
}