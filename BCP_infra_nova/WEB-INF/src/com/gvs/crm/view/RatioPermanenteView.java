package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.RatioPermanente;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class RatioPermanenteView extends EventoAbstratoView {

	public View execute(User arg0, Locale arg1, Properties arg2)
			throws Exception {
		RatioPermanente ratio = (RatioPermanente) this.obterEvento();

		Table table = new Table(2);

		table.addHeader("Activo Corriente:");
		table.add(new Label(ratio.obterAtivoCorrente(), "#,##0.00"));

		table.addHeader("Pasivo Corriente:");
		table.add(new Label(ratio.obterPassivoCorrente(), "#,##0.00"));

		table.addHeader("Inversiones menores a 180 dias:");
		table.add(new Label(ratio.obterInversao(), "#,##0.00"));

		table.addHeader("Deudas menores a 180 dias:");
		table.add(new Label(ratio.obterDeudas(), "#,##0.00"));

		table.addHeader("Inmuebles destinados a Uso:");
		table.add(new Label(ratio.obterUso(), "#,##0.00"));

		table.addHeader("Inmuebles destinados a Venta:");
		table.add(new Label(ratio.obterVenda(), "#,##0.00"));

		table.addHeader("Inmuebles destinados a Alquier o Leasing:");
		table.add(new Label(ratio.obterLeasing(), "#,##0.00"));

		table.addHeader("Propuesta de Distribución de Resultados:");
		table.add(new Label(ratio.obterResultados(), "#,##0.00"));

		Button voltarButton = new Button("Volver", new Action(
				"visualizarDetalhesEntidade"));
		voltarButton.getAction().add("id", ratio.obterOrigem().obterId());

		table.addFooter(voltarButton);

		return table;
	}

}