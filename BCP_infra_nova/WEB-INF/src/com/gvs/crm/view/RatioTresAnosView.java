package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.RatioTresAnos;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class RatioTresAnosView extends EventoAbstratoView {

	public View execute(User arg0, Locale arg1, Properties arg2)
			throws Exception {
		RatioTresAnos ratio = (RatioTresAnos) this.obterEvento();

		Table table = new Table(2);

		table.addHeader("Siniestros Pagados:");
		table.add(new Label(ratio.obterSinistrosPagos(), "#,##0.00"));

		table.addHeader("Gastos de liquidación de Siniestros:");
		table.add(new Label(ratio.obterGastosSinistros(), "#,##0.00"));

		table.addHeader("Siniestros Recuperados Reaseguros Cedidos:");
		table.add(new Label(ratio.obterSinistrosRecuperados(), "#,##0.00"));

		table.addHeader("Gastos de Salvataje y Recupero:");
		table.add(new Label(ratio.obterGastosRecuperados(), "#,##0.00"));

		table.addHeader("Recupero de Siniestros:");
		table.add(new Label(ratio.obterRecuperoSinistros(), "#,##0.00"));

		table.addHeader("Provisiones Técnicas de Siniestros:");
		table.add(new Label(ratio.obterProvisoes(), "#,##0.00"));

		Button voltarButton = new Button("Volver", new Action(
				"visualizarDetalhesEntidade"));
		voltarButton.getAction().add("id", ratio.obterOrigem().obterId());

		table.addFooter(voltarButton);

		return table;
	}
}