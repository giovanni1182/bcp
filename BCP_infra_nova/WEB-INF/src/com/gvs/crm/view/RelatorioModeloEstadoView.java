package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.Border;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.EntidadeHome;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class RelatorioModeloEstadoView extends BasicView {
	private Aseguradora aseguradora;

	private String mesAno;

	private boolean lista;

	private String mes;

	private String ano;

	public RelatorioModeloEstadoView(Aseguradora aseguradora, String mes,
			String ano, boolean lista) throws Exception {
		this.aseguradora = aseguradora;
		this.mes = mes;
		this.ano = ano;
		this.mesAno = mes + ano;
		this.lista = lista;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(12);

		table.addSubtitle("Visualizar Relatório");

		Block block = new Block(Block.HORIZONTAL);

		block.add(new Label("Mes:"));
		block.add(new Space());
		block.add(new InputString("mes", this.mes, 2));
		block.add(new Space(2));
		block.add(new Label("Año:"));
		block.add(new Space());
		block.add(new InputString("ano", this.ano, 4));

		//block.add(new MesAnoBalancoSelect("mesAno", this.aseguradora,
		// this.mesAno));
		block.add(new Space(2));
		Button visualizarButton = new Button("Visualizar", new Action(
				"visualizarRelatorioAseguradoraModeloEstado"));
		visualizarButton.getAction().add("lista", true);
		visualizarButton.getAction().add("id", this.aseguradora.obterId());
		block.add(visualizarButton);
		table.setNextColSpan(table.getColumns());
		table.add(block);
		table.setNextColSpan(table.getColumns());
		table.addData(new Space());

		if (this.lista) {
			CRMModelManager mm = new CRMModelManager(user);
			EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

			ClassificacaoContas cContas1 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0301010000");
			ClassificacaoContas cContas1_1 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0301020000");

			ClassificacaoContas cContas2 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0302010000");

			ClassificacaoContas cContas3 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0303010100");
			ClassificacaoContas cContas3_1 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0303010200");
			ClassificacaoContas cContas3_2 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0303010300");

			ClassificacaoContas cContas4 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0303020100");
			ClassificacaoContas cContas4_1 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0303020200");

			ClassificacaoContas cContas5 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0304000000");

			ClassificacaoContas cContas6 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0305000000");

			table.addStyle(Table.STYLE_ALTERNATE);

			table.addSubtitle("");

			table.setWidth("100%");

			table.addHeader("Descripción");
			table.setNextColSpan(2);
			table.setNextHAlign(Table.HALIGN_CENTER);

			table.addHeader("Cap. Integrado");
			table.setNextHAlign(Table.HALIGN_CENTER);

			table.addHeader("Pendientes");

			table.setNextColSpan(3);
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Reservas sobre Utilidades");

			table.setNextColSpan(2);
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Reservas Patrimoniais");

			table.setNextColSpan(2);
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Resultados");

			table.addHeader("Total");

			table.add("");
			table.addHeader("En Efectivo");
			table.addHeader("Cap. Secundario");
			table.addHeader("Aportes a Cap.");
			table.addHeader("Legal");
			table.addHeader("Estatutariais");
			table.addHeader("Facultativas");
			table.addHeader("De Revalúo");
			table.addHeader("Ley 827/96");
			table.addHeader("Acumulados");
			table.addHeader("Del Ejercicio");
			table.add("");

			double valorConta1 = cContas1.obterTotalizacaoSaldoAnoAnterior(
					this.aseguradora, this.mesAno);
			double valorConta1_1 = cContas1_1.obterTotalizacaoSaldoAnoAnterior(
					this.aseguradora, this.mesAno);

			table.add("1 - Integración de Capital");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta1));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta1_1));//,"#,##0.00"));
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			double totalConta1 = valorConta1 + valorConta1_1;
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(totalConta1));//,"#,##0.00"));

			double valorConta2 = cContas2.obterTotalizacaoSaldoAnoAnterior(
					this.aseguradora, this.mesAno);

			table.add("2 - Aportes a Capitalizar");
			table.add("");
			table.add("");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta2));//,"#,##0.00"));
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			double totalConta2 = valorConta2;
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(totalConta2));//,"#,##0.00"));

			double valorConta3 = cContas3.obterTotalizacaoSaldoAnoAnterior(
					this.aseguradora, this.mesAno);
			double valorConta3_1 = cContas3_1.obterTotalizacaoSaldoAnoAnterior(
					this.aseguradora, this.mesAno);
			double valorConta3_2 = cContas3_2.obterTotalizacaoSaldoAnoAnterior(
					this.aseguradora, this.mesAno);

			table.add("3 - Constituición de Reservas");
			table.add("");
			table.add("");
			table.add("");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta3));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta3_1, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta3_2));//,"#,##0.00"));
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			double totalConta3 = valorConta3 + valorConta3_1 + valorConta3_2;
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(totalConta3));//,"#,##0.00"));

			double valorConta4 = cContas4.obterTotalizacaoSaldoAnoAnterior(
					this.aseguradora, this.mesAno);
			double valorConta4_1 = cContas4_1.obterTotalizacaoSaldoAnoAnterior(
					this.aseguradora, this.mesAno);

			table.add("4 - Capitalización de Reservas");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta4));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta4_1));//,"#,##0.00"));
			table.add("");
			table.add("");
			double totalConta4 = valorConta4 + valorConta4_1;
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(totalConta4));//,"#,##0.00"));

			double valorConta5 = cContas5.obterTotalizacaoSaldoAnoAnterior(
					this.aseguradora, this.mesAno);

			table.add("5 - Ajustes de Resultados Anteriores");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta5));//,"#,##0.00"));
			table.add("");
			double totalConta5 = valorConta5;
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(totalConta5));//,"#,##0.00"));

			double valorConta6 = cContas6.obterTotalizacaoSaldoAnoAnterior(
					this.aseguradora, this.mesAno);
			table.add("6 - Distribución de Dividendos");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta6));//,"#,##0.00"));
			double totalConta6 = valorConta6;
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(totalConta6));//,"#,##0.00"));

			table.addHeader("Saldo del Ejercicio Anterior");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta1));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta1_1));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta2));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta3));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta3_1));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta3_2));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta4));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta4_1));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta5));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta6));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalConta1 + totalConta2 + totalConta3
					+ totalConta4 + totalConta5 + totalConta6));//,"#,##0.00"));

			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			// ANO ATUAL

			double valorConta1Atual = cContas1.obterTotalizacaoExistente(
					this.aseguradora, this.mesAno);
			double valorConta1_1Atual = cContas1_1.obterTotalizacaoExistente(
					this.aseguradora, this.mesAno);

			table.add("1 - Integración de Capital");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta1Atual));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta1_1Atual));//,"#,##0.00"));
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			double totalConta1Atual = valorConta1Atual + valorConta1_1Atual;
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(totalConta1Atual));//,"#,##0.00"));

			double valorConta2Atual = cContas2.obterTotalizacaoExistente(
					this.aseguradora, this.mesAno);

			table.add("2 - Aportes a Capitalizar");
			table.add("");
			table.add("");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta2Atual));//,"#,##0.00"));
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			double totalConta2Atual = valorConta2Atual;
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(totalConta2Atual));//,"#,##0.00"));

			double valorConta3Atual = cContas3.obterTotalizacaoExistente(
					this.aseguradora, this.mesAno);
			double valorConta3_1Atual = cContas3_1.obterTotalizacaoExistente(
					this.aseguradora, this.mesAno);
			double valorConta3_2Atual = cContas3_2.obterTotalizacaoExistente(
					this.aseguradora, this.mesAno);

			table.add("3 - Constituición de Reservas");
			table.add("");
			table.add("");
			table.add("");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta3Atual));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta3_1Atual));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta3_2Atual));//,"#,##0.00"));
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			double totalConta3Atual = valorConta3Atual + valorConta3_1Atual
					+ valorConta3_2Atual;
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(totalConta3Atual));//,"#,##0.00"));

			double valorConta4Atual = cContas4.obterTotalizacaoExistente(
					this.aseguradora, this.mesAno);
			double valorConta4_1Atual = cContas4_1.obterTotalizacaoExistente(
					this.aseguradora, this.mesAno);

			table.add("4 - Capitalización de Reservas");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta4Atual));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta4_1Atual));//,"#,##0.00"));
			table.add("");
			table.add("");
			double totalConta4Atual = valorConta4Atual + valorConta4_1Atual;
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(totalConta4Atual));//,"#,##0.00"));

			double valorConta5Atual = cContas5.obterTotalizacaoExistente(
					this.aseguradora, this.mesAno);

			table.add("5 - Ajustes de Resultados Anteriores");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta5Atual));//,"#,##0.00"));
			table.add("");
			double totalConta5Atual = valorConta5Atual;
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(totalConta5Atual));//,"#,##0.00"));

			double valorConta6Atual = cContas6.obterTotalizacaoExistente(
					this.aseguradora, this.mesAno);
			table.add("6 - Distribución de Dividendos");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.add("");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorConta6Atual));//,"#,##0.00"));
			double totalConta6Atual = valorConta6Atual;
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(totalConta6Atual));//,"#,##0.00"));

			table.addHeader("Saldo del Ejercicio Actual");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta1Atual));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta1_1Atual));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta2Atual));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta3Atual));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta3_1Atual));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta3_2Atual));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta4Atual));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta4_1Atual));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta5Atual));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorConta6Atual));//,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalConta1Atual + totalConta2Atual
					+ totalConta3Atual + totalConta4Atual + totalConta5Atual
					+ totalConta6Atual));//,"#,##0.00"));
		}

		Button voltarButton = new Button("Volver", new Action(
				"visualizarRelatorioAseguradora"));
		voltarButton.getAction().add("id", this.aseguradora.obterId());
		voltarButton.getAction().add("_eventos", "1");

		table.addFooter(voltarButton);

		Border border = new Border(table);

		return border;
	}
}