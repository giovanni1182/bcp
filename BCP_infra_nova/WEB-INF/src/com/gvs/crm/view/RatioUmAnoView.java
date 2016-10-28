package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.RatioUmAno;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class RatioUmAnoView extends EventoAbstratoView {

	public View execute(User arg0, Locale arg1, Properties arg2)
			throws Exception {
		RatioUmAno ratio = (RatioUmAno) this.obterEvento();

		Table table = new Table(2);

		table.addHeader("Primas Directas:");
		table.add(new Label(ratio.obterPrimasDiretas(), "#,##0.00"));

		table.addHeader("Primas Reaseguros Aceptados:");
		table.add(new Label(ratio.obterPrimasAceitas(), "#,##0.00"));

		table.addHeader("Primas Reaseguros Cedidos:");
		table.add(new Label(ratio.obterPrimasCedidas(), "#,##0.00"));

		table.addHeader("Anulaciones Primas de Seguros Directos:");
		table.add(new Label(ratio.obterAnulacaoPrimasDiretas(), "#,##0.00"));

		table.addHeader("Anulaciones Primas de Reaseguros Activos:");
		table.add(new Label(ratio.obterAnulacaoPrimasAtivas(), "#,##0.00"));

		table.addHeader("Anulaciones Primas de Cedidas y Retrocedidas:");
		table.add(new Label(ratio.obterAnulacaoPrimasCedidas(), "#,##0.00"));

		Button voltarButton = new Button("Volver", new Action(
				"visualizarDetalhesEntidade"));
		voltarButton.getAction().add("id", ratio.obterOrigem().obterId());

		table.addFooter(voltarButton);

		return table;
	}
}