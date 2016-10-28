package com.gvs.crm.report;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.StringTokenizer;

import com.gvs.crm.model.Circular;
import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Entidade;

import infra.view.report.Font;
import infra.view.report.Region;
import infra.view.report.Table;
import infra.view.report.TableRow;
import infra.view.report.Text;

public class CircularReport extends StellaReport {
	public CircularReport(Circular circular, Locale locale) throws Exception {
		super(circular.obterSuperior().obterTitulo(), circular);

		DocumentoProduto documento = (DocumentoProduto) circular
				.obterSuperior();

		Table tablePrincipal = new Table();
		tablePrincipal.setFont(new Font("sans-serif", "12pt", null, null));
		tablePrincipal.addColumnWidth("17cm");

		TableRow tableRow = new TableRow();
		tableRow.setFont(new Font(null, null, null, null));
		tableRow.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow);

		TableRow tableRow16 = new TableRow();
		tableRow16.setFont(new Font(null, null, null, null));
		tableRow16.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow16);

		TableRow tableRow3 = new TableRow();
		tableRow3.setFont(new Font(null, null, null, null));
		tableRow3.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow3);

		SimpleDateFormat st = new SimpleDateFormat("dd MMM yyyy", Locale
				.getDefault());

		TableRow tableRow6 = new TableRow();
		tableRow6.setFont(new Font(null, null, null, null));
		tableRow6.setTextAlign("right");
		tableRow6.addCell(new Text("Asunción, "
				+ st.format(documento.obterCriacao())));
		tablePrincipal.addBodyRow(tableRow6);

		TableRow tableRow8 = new TableRow();
		tableRow8.setFont(new Font(null, null, null, null));
		tableRow8.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow8);

		TableRow tableRow5 = new TableRow();
		tableRow5.setFont(new Font(null, null, null, "bold"));
		tableRow5.addCell(new Text(documento.obterReferente()));
		tablePrincipal.addBodyRow(tableRow5);

		TableRow tableRow13 = new TableRow();
		tableRow13.setFont(new Font(null, null, null, "bold"));
		tableRow13.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow13);

		Entidade.Atributo descricao = (Entidade.Atributo) documento
				.obterDocumento().obterAtributo("descricao");

		String descricaoStr = "";

		if (descricao != null)
			descricaoStr = descricao.obterValor();

		if (circular.obterDescricao() != null)
			descricaoStr = circular.obterDescricao();

		StringTokenizer st2 = new StringTokenizer(descricaoStr, "\n");

		while (st2.hasMoreTokens()) {
			String linha = st2.nextToken();

			if (linha.equals("\r")) {
				TableRow tableRow15 = new TableRow();
				tableRow15.setFont(new Font(null, null, null, null));
				tableRow15.addCell(new Text(" ", 2));
				tablePrincipal.addBodyRow(tableRow15);
			} else {
				TableRow tableRow15 = new TableRow();
				tableRow15.setFont(new Font(null, null, null, null));
				tableRow15.addCell(new Text(linha));
				tablePrincipal.addBodyRow(tableRow15);
			}
		}

		TableRow tableRow7 = new TableRow();
		tableRow7.setFont(new Font(null, null, null, "bold"));
		tableRow7.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow7);

		TableRow tableRow9 = new TableRow();
		tableRow9.setFont(new Font(null, null, null, "bold"));
		tableRow9.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow9);

		TableRow tableRow10 = new TableRow();
		tableRow10.setFont(new Font(null, null, null, "bold"));
		tableRow10.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow10);

		TableRow tableRow11 = new TableRow();
		tableRow11.setFont(new Font(null, null, null, "bold"));
		tableRow11.setTextAlign("center");
		tableRow11.addCell(new Text(documento.obterOrigem().obterNome()));
		tablePrincipal.addBodyRow(tableRow11);

		Entidade.Atributo cargo = (Entidade.Atributo) documento.obterOrigem()
				.obterAtributo("cargo");

		TableRow tableRow12 = new TableRow();
		tableRow12.setFont(new Font(null, null, null, null));
		tableRow12.setTextAlign("center");
		if (cargo != null)
			tableRow12.addCell(new Text(cargo.obterValor()));
		else
			tableRow12.addCell(new Text(""));
		tablePrincipal.addBodyRow(tableRow12);

		Region bodyRegion = new Region();
		bodyRegion.addBlock(tablePrincipal);
		this.setBody(bodyRegion);
	}
}