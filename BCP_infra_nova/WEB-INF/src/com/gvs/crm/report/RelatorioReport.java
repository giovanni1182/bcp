package com.gvs.crm.report;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.EntidadeHome;

import infra.view.report.Border;
import infra.view.report.Font;
import infra.view.report.Image;
import infra.view.report.Region;
import infra.view.report.Report;
import infra.view.report.Table;
import infra.view.report.TableRow;
import infra.view.report.Text;

public class RelatorioReport extends A4Report {
	public RelatorioReport(EntidadeHome home, Aseguradora aseguradora,
			String mesAno) throws Exception {
		super();

		DecimalFormat formatador = new DecimalFormat();
		formatador.applyPattern("#,##0.00");

		Table tablePrincipal = new Table();
		tablePrincipal.setBorder(new Border("1pt", "1pt", "1pt", "1pt",
				"solid", "black"));
		tablePrincipal.addColumnWidth("4cm");
		tablePrincipal.addColumnWidth("4cm");
		tablePrincipal.addColumnWidth("4cm");
		tablePrincipal.addColumnWidth("4cm");
		tablePrincipal.setFont(new Font("sans-serif", "8pt", null, null));

		Region headerRegion = new Region();

		Table headerTable = new Table();
		headerTable.addColumnWidth("17cm");
		headerTable.setFont(new Font("sans-serif", "8t", null, null));

		Text pageNumberText = new Text(new SimpleDateFormat("dd/MM/yyyy HH:mm")
				.format(new Date())
				+ " / " + "Página ?");
		pageNumberText.setTextAlign("right");
		pageNumberText.addParameter(Report.PAGE_NUMBER);

		TableRow headerRow = new TableRow();
		headerRow.setFont(new Font("sans-serif", "8pt", null, null));
		headerRow.addCell(pageNumberText);

		TableRow headerRow1 = new TableRow();
		headerRow1.setTextAlign("center");
		headerRow1.addCell(new Image("logo_bcp.gif"));

		headerTable.addBodyRow(headerRow);
		headerTable.addBodyRow(headerRow1);

		headerRegion.addBlock(headerTable);
		this.setBefore("4.0cm", headerRegion);

		TableRow tableRow = new TableRow();
		tableRow.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow.setFont(new Font(null, null, null, "bold"));
		tableRow.setTextAlign("center");
		tableRow.addCell(new Text("ACTIVO"));
		tableRow.addCell(new Text("VALOR"));
		tableRow.addCell(new Text("PASSIVO Y PATR. NETO"));
		tableRow.addCell(new Text("VALOR"));
		tablePrincipal.addBodyRow(tableRow);

		ClassificacaoContas ativo1 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0101000000");
		ClassificacaoContas passivo1 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0201000000");

		double valorAtivo1 = ativo1.obterTotalizacaoExistente(aseguradora,
				mesAno);
		double valorPassivo1 = passivo1.obterTotalizacaoExistente(aseguradora,
				mesAno);

		TableRow tableRow1 = new TableRow();
		tableRow1.addCell(new Text(ativo1.obterNome()));
		tableRow1.addCell(new Text(formatador.format(valorAtivo1)));
		tableRow1.addCell(new Text(passivo1.obterNome()));
		tableRow1.addCell(new Text(formatador.format(valorPassivo1)));
		tablePrincipal.addBodyRow(tableRow1);

		ClassificacaoContas ativo2 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0102000000");
		ClassificacaoContas passivo2 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0202000000");

		double valorAtivo2 = ativo2.obterTotalizacaoExistente(aseguradora,
				mesAno);
		double valorPassivo2 = passivo2.obterTotalizacaoExistente(aseguradora,
				mesAno);

		TableRow tableRow2 = new TableRow();
		tableRow2.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow2.addCell(new Text(ativo2.obterNome()));
		tableRow2.addCell(new Text(formatador.format(valorAtivo2)));
		tableRow2.addCell(new Text(passivo2.obterNome()));
		tableRow2.addCell(new Text(formatador.format(valorPassivo2)));
		tablePrincipal.addBodyRow(tableRow2);

		ClassificacaoContas ativo3 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0103000000");
		ClassificacaoContas passivo3 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0203000000");

		double valorAtivo3 = ativo3.obterTotalizacaoExistente(aseguradora,
				mesAno);
		double valorPassivo3 = passivo3.obterTotalizacaoExistente(aseguradora,
				mesAno);

		TableRow tableRow3 = new TableRow();
		tableRow3.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow3.addCell(new Text(ativo3.obterNome()));
		tableRow3.addCell(new Text(formatador.format(valorAtivo3)));
		tableRow3.addCell(new Text(passivo3.obterNome()));
		tableRow3.addCell(new Text(formatador.format(valorPassivo3)));
		tablePrincipal.addBodyRow(tableRow3);

		ClassificacaoContas ativo4 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0104000000");
		ClassificacaoContas passivo4 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0204000000");

		double valorAtivo4 = ativo4.obterTotalizacaoExistente(aseguradora,
				mesAno);
		double valorPassivo4 = passivo4.obterTotalizacaoExistente(aseguradora,
				mesAno);

		TableRow tableRow4 = new TableRow();
		tableRow4.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow4.addCell(new Text(ativo4.obterNome()));
		tableRow4.addCell(new Text(formatador.format(valorAtivo4)));
		tableRow4.addCell(new Text(passivo4.obterNome()));
		tableRow4.addCell(new Text(formatador.format(valorPassivo4)));
		tablePrincipal.addBodyRow(tableRow4);

		ClassificacaoContas ativo5 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0105000000");
		ClassificacaoContas passivo5 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0205000000");

		double valorAtivo5 = ativo5.obterTotalizacaoExistente(aseguradora,
				mesAno);
		double valorPassivo5 = passivo5.obterTotalizacaoExistente(aseguradora,
				mesAno);

		TableRow tableRow5 = new TableRow();
		tableRow5.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow5.addCell(new Text(ativo5.obterNome()));
		tableRow5.addCell(new Text(formatador.format(valorAtivo5)));
		tableRow5.addCell(new Text(passivo5.obterNome()));
		tableRow5.addCell(new Text(formatador.format(valorPassivo5)));
		tablePrincipal.addBodyRow(tableRow5);

		ClassificacaoContas ativo6 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0106000000");
		ClassificacaoContas passivo6 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0206000000");

		double valorAtivo6 = ativo6.obterTotalizacaoExistente(aseguradora,
				mesAno);
		double valorPassivo6 = passivo6.obterTotalizacaoExistente(aseguradora,
				mesAno);

		TableRow tableRow6 = new TableRow();
		tableRow6.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow6.addCell(new Text(ativo6.obterNome()));
		tableRow6.addCell(new Text(formatador.format(valorAtivo6)));
		tableRow6.addCell(new Text(passivo6.obterNome()));
		tableRow6.addCell(new Text(formatador.format(valorPassivo6)));
		tablePrincipal.addBodyRow(tableRow6);

		ClassificacaoContas ativo7 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0107000000");
		ClassificacaoContas passivo10 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0210000000");

		double valorAtivo7 = ativo7.obterTotalizacaoExistente(aseguradora,
				mesAno);
		double valorPassivo10 = passivo10.obterTotalizacaoExistente(
				aseguradora, mesAno);

		TableRow tableRow7 = new TableRow();
		tableRow7.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow7.addCell(new Text(ativo7.obterNome()));
		tableRow7.addCell(new Text(formatador.format(valorAtivo7)));
		tableRow7.addCell(new Text(passivo10.obterNome()));
		tableRow7.addCell(new Text(formatador.format(valorPassivo10)));
		tablePrincipal.addBodyRow(tableRow7);

		ClassificacaoContas ativo8 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0108000000");
		ClassificacaoContas passivo11 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0211000000");

		double valorAtivo8 = ativo8.obterTotalizacaoExistente(aseguradora,
				mesAno);
		double valorPassivo11 = passivo11.obterTotalizacaoExistente(
				aseguradora, mesAno);

		TableRow tableRow8 = new TableRow();
		tableRow8.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow8.addCell(new Text(ativo8.obterNome()));
		tableRow8.addCell(new Text(formatador.format(valorAtivo8)));
		tableRow8.addCell(new Text(passivo11.obterNome()));
		tableRow8.addCell(new Text(formatador.format(valorPassivo11)));
		tablePrincipal.addBodyRow(tableRow8);

		ClassificacaoContas ativo9 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0109000000");
		ClassificacaoContas passivo12 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0212000000");

		double valorAtivo9 = ativo9.obterTotalizacaoExistente(aseguradora,
				mesAno);
		double valorPassivo12 = passivo12.obterTotalizacaoExistente(
				aseguradora, mesAno);

		TableRow tableRow9 = new TableRow();
		tableRow9.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow9.addCell(new Text(ativo9.obterNome()));
		tableRow9.addCell(new Text(formatador.format(valorAtivo9)));
		tableRow9.addCell(new Text(passivo12.obterNome()));
		tableRow9.addCell(new Text(formatador.format(valorPassivo12)));
		tablePrincipal.addBodyRow(tableRow9);

		ClassificacaoContas passivo13 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0213000000");

		double valorPassivo13 = passivo13.obterTotalizacaoExistente(
				aseguradora, mesAno);

		TableRow tableRow10 = new TableRow();
		tableRow10.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow10.addCell(new Text(""));
		tableRow10.addCell(new Text(""));
		tableRow10.addCell(new Text(passivo13.obterNome()));
		tableRow10.addCell(new Text(formatador.format(valorPassivo13)));
		tablePrincipal.addBodyRow(tableRow10);

		ClassificacaoContas passivo14 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0214000000");

		double valorPassivo14 = passivo14.obterTotalizacaoExistente(
				aseguradora, mesAno);

		TableRow tableRow11 = new TableRow();
		tableRow11.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow11.addCell(new Text(""));
		tableRow11.addCell(new Text(""));
		tableRow11.addCell(new Text(passivo14.obterNome()));
		tableRow11.addCell(new Text(formatador.format(valorPassivo14)));
		tablePrincipal.addBodyRow(tableRow11);

		TableRow espaco = new TableRow();
		espaco.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(espaco);

		double totalAtivo = valorAtivo1 + valorAtivo2 + valorAtivo3
				+ valorAtivo4 + valorAtivo5 + valorAtivo6 + valorAtivo7
				+ valorAtivo8 + valorAtivo9;

		double totalPassivo = valorPassivo1 + valorPassivo2 + valorPassivo3
				+ valorPassivo4 + valorPassivo5 + valorPassivo6
				+ valorPassivo10 + valorPassivo11 + valorPassivo12
				+ valorPassivo13 + valorPassivo14;

		TableRow tableRow12 = new TableRow();
		tableRow12.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow12.setFont(new Font(null, null, null, "bold"));
		tableRow12.addCell(new Text(""));
		tableRow12.addCell(new Text(""));
		tableRow12.addCell(new Text("Passivo Total"));
		tableRow12.addCell(new Text(formatador.format(totalPassivo)));
		tablePrincipal.addBodyRow(tableRow12);

		tablePrincipal.addBodyRow(espaco);

		TableRow tableRow13 = new TableRow();
		tableRow13.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow13.setFont(new Font(null, null, null, "bold"));
		tableRow13.addCell(new Text(""));
		tableRow13.addCell(new Text(""));
		tableRow13.addCell(new Text("PATRIMONIO NETO"));
		tableRow13.addCell(new Text(""));
		tablePrincipal.addBodyRow(tableRow13);

		ClassificacaoContas patrimonio1 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0301000000");

		double valorPatrimonio1 = patrimonio1.obterTotalizacaoExistente(
				aseguradora, mesAno);

		TableRow tableRow14 = new TableRow();
		tableRow14.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow14.addCell(new Text(""));
		tableRow14.addCell(new Text(""));
		tableRow14.addCell(new Text(patrimonio1.obterNome()));
		tableRow14.addCell(new Text(formatador.format(valorPatrimonio1)));
		tablePrincipal.addBodyRow(tableRow14);

		ClassificacaoContas patrimonio2 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0302000000");

		double valorPatrimonio2 = patrimonio2.obterTotalizacaoExistente(
				aseguradora, mesAno);

		TableRow tableRow15 = new TableRow();
		tableRow15.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow15.addCell(new Text(""));
		tableRow15.addCell(new Text(""));
		tableRow15.addCell(new Text(patrimonio2.obterNome()));
		tableRow15.addCell(new Text(formatador.format(valorPatrimonio2)));
		tablePrincipal.addBodyRow(tableRow15);

		ClassificacaoContas patrimonio3 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0303000000");

		double valorPatrimonio3 = patrimonio3.obterTotalizacaoExistente(
				aseguradora, mesAno);

		TableRow tableRow16 = new TableRow();
		tableRow16.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow16.addCell(new Text(""));
		tableRow16.addCell(new Text(""));
		tableRow16.addCell(new Text(patrimonio3.obterNome()));
		tableRow16.addCell(new Text(formatador.format(valorPatrimonio3)));
		tablePrincipal.addBodyRow(tableRow16);

		ClassificacaoContas patrimonio4 = (ClassificacaoContas) home
				.obterEntidadePorApelido("0304000000");

		double valorPatrimonio4 = patrimonio4.obterTotalizacaoExistente(
				aseguradora, mesAno);

		TableRow tableRow17 = new TableRow();
		tableRow17.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow17.addCell(new Text(""));
		tableRow17.addCell(new Text(""));
		tableRow17.addCell(new Text(patrimonio4.obterNome()));
		tableRow17.addCell(new Text(formatador.format(valorPatrimonio4)));
		tablePrincipal.addBodyRow(tableRow17);

		double totalPatrimonio = valorPatrimonio1 + valorPatrimonio2
				+ valorPatrimonio3 + valorPatrimonio4;

		double totalExercicio = totalAtivo - (totalPassivo + totalPatrimonio);

		TableRow tableRow18 = new TableRow();
		tableRow18.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow18.addCell(new Text(""));
		tableRow18.addCell(new Text(""));
		tableRow18.addCell(new Text("Resultado del Ejercicio"));
		tableRow18.addCell(new Text(formatador.format(totalExercicio)));
		tablePrincipal.addBodyRow(tableRow18);

		tablePrincipal.addBodyRow(espaco);

		double totalPatrimonioNeto = totalPatrimonio + totalExercicio;

		TableRow tableRow19 = new TableRow();
		tableRow19.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow19.addCell(new Text(""));
		tableRow19.addCell(new Text(""));
		tableRow19.addCell(new Text("Total Patrimonio Neto"));
		tableRow19.addCell(new Text(formatador.format(totalPatrimonioNeto)));
		tablePrincipal.addBodyRow(tableRow19);

		tablePrincipal.addBodyRow(espaco);

		TableRow tableRow20 = new TableRow();
		tableRow20.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid",
				"black"));
		tableRow20.addCell(new Text(""));
		tableRow20.addCell(new Text(""));
		tableRow20.addCell(new Text("Total Passivo y Patr. Neto"));
		tableRow20.addCell(new Text(formatador.format(totalPassivo
				+ totalPatrimonioNeto)));
		tablePrincipal.addBodyRow(tableRow20);

		Region bodyRegion = new Region();
		bodyRegion.addBlock(tablePrincipal);
		this.setBody(bodyRegion);
	}
}