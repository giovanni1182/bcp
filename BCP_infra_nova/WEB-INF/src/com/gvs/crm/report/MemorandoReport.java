package com.gvs.crm.report;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.StringTokenizer;

import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Memorando;

import infra.view.report.Font;
import infra.view.report.Region;
import infra.view.report.Table;
import infra.view.report.TableRow;
import infra.view.report.Text;

public class MemorandoReport extends StellaReport {
	public MemorandoReport(Memorando memorando, Locale locale) throws Exception {
		super(memorando.obterSuperior().obterTitulo(), memorando);

		DocumentoProduto documento = (DocumentoProduto) memorando
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
		tableRow3.setFont(new Font(null, null, null, "bold"));
		tableRow3.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow3);

		TableRow tableRow4 = new TableRow();
		tableRow4.setFont(new Font(null, null, null, "bold"));
		tableRow4
				.addCell(new Text("A : " + documento.obterOrigem().obterNome()));
		tablePrincipal.addBodyRow(tableRow4);

		Entidade.Atributo cargo = (Entidade.Atributo) documento.obterOrigem()
				.obterAtributo("cargo");

		TableRow tableRow5 = new TableRow();
		tableRow5.setFont(new Font(null, null, null, null));
		if (cargo != null)
			tableRow5.addCell(new Text(cargo.obterValor()));
		else
			tableRow5.addCell(new Text(""));
		//tableRow5.setTextAlign("center");
		tablePrincipal.addBodyRow(tableRow5);

		TableRow tableRow8 = new TableRow();
		tableRow8.setFont(new Font(null, null, null, "bold"));
		tableRow8.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow8);

		TableRow tableRow6 = new TableRow();
		tableRow6.setFont(new Font(null, null, null, "bold"));
		tableRow6.addCell(new Text("DE : "
				+ documento.obterResponsavel().obterNome()));
		//tableRow6.setTextAlign("center");
		tablePrincipal.addBodyRow(tableRow6);

		Entidade.Atributo cargo2 = (Entidade.Atributo) documento
				.obterResponsavel().obterAtributo("cargo");

		TableRow tableRow7 = new TableRow();
		tableRow7.setFont(new Font(null, null, null, null));
		if (cargo2 != null)
			tableRow7.addCell(new Text(cargo2.obterValor()));
		else
			tableRow7.addCell(new Text(""));
		//tableRow7.setTextAlign("center");
		tablePrincipal.addBodyRow(tableRow7);

		TableRow tableRow9 = new TableRow();
		tableRow9.setFont(new Font(null, null, null, "bold"));
		tableRow9.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow9);

		TableRow tableRow10 = new TableRow();
		tableRow10.setFont(new Font(null, null, null, "bold"));
		tableRow10.addCell(new Text("MOTIVO : " + documento.obterTitulo()));
		//tableRow10.setTextAlign("center");
		tablePrincipal.addBodyRow(tableRow10);

		TableRow tableRow11 = new TableRow();
		tableRow11.setFont(new Font(null, null, null, "bold"));
		tableRow11.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow11);

		SimpleDateFormat st = new SimpleDateFormat("dd MMM yyyy", Locale
				.getDefault());

		TableRow tableRow12 = new TableRow();
		tableRow12.setFont(new Font(null, null, null, null));
		tableRow12.addCell(new Text("FECHA : "
				+ st.format(documento.obterCriacao())));
		//tableRow12.setTextAlign("center");
		tablePrincipal.addBodyRow(tableRow12);

		TableRow tableRow13 = new TableRow();
		tableRow13.setFont(new Font(null, null, null, "bold"));
		tableRow13.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow13);

		TableRow tableRow14 = new TableRow();
		tableRow14.setFont(new Font(null, null, null, "bold"));
		tableRow14.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow14);

		Entidade.Atributo descricao = (Entidade.Atributo) documento
				.obterDocumento().obterAtributo("descricao");

		String descricaoStr = "";

		if (descricao != null)
			descricaoStr = descricao.obterValor();

		if (memorando.obterDescricao() != null)
			descricaoStr = memorando.obterDescricao();

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

		/*
		 * TableRow tableRow15 = new TableRow(); tableRow15.setFont(new
		 * Font(null, null, null, null)); tableRow15.addCell(new
		 * Text(memorando.obterDescricao()));
		 * tablePrincipal.addBodyRow(tableRow15);
		 */

		Region bodyRegion = new Region();
		bodyRegion.addBlock(tablePrincipal);
		this.setBody(bodyRegion);
	}
}