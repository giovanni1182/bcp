package com.gvs.crm.report;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.StringTokenizer;

import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Informe;

import infra.view.report.Font;
import infra.view.report.Region;
import infra.view.report.Table;
import infra.view.report.TableRow;
import infra.view.report.Text;

public class InformeReport extends StellaReport {
	public InformeReport(Informe informe, Locale locale) throws Exception {
		super(informe.obterSuperior().obterTitulo(), informe);

		DocumentoProduto documento = (DocumentoProduto) informe.obterSuperior();

		Table tablePrincipal = new Table();
		tablePrincipal.setFont(new Font("sans-serif", "10pt", null, null));
		tablePrincipal.addColumnWidth("17cm");

		TableRow tableRow = new TableRow();
		tableRow.setFont(new Font(null, null, null, null));
		tableRow.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow);

		TableRow tableRow16 = new TableRow();
		tableRow16.setFont(new Font(null, null, null, null));
		tableRow16.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow16);

		SimpleDateFormat st = new SimpleDateFormat("dd MMM yyyy", Locale
				.getDefault());

		TableRow tableRow17 = new TableRow();
		tableRow17.setFont(new Font(null, null, null, null));
		tableRow17.setTextAlign("right");
		tableRow17.addCell(new Text("Asunción, "
				+ st.format(documento.obterCriacao())));
		tablePrincipal.addBodyRow(tableRow17);

		TableRow tableRow24 = new TableRow();
		tableRow24.setFont(new Font(null, null, null, null));
		tableRow24.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow24);

		TableRow tableRow4 = new TableRow();
		tableRow4.setFont(new Font(null, null, null, "bold"));
		tableRow4.addCell(new Text(documento.obterTitulo()));
		tablePrincipal.addBodyRow(tableRow4);

		TableRow tableRow5 = new TableRow();
		tableRow5.setFont(new Font(null, null, null, null));
		tableRow5.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow5);

		TableRow tableRow8 = new TableRow();
		tableRow8.setFont(new Font(null, null, null, null));
		tableRow8.setTextAlign("right");
		tableRow8.addCell(new Text("Ref.: " + documento.obterReferente()));
		tablePrincipal.addBodyRow(tableRow8);

		TableRow tableRow9 = new TableRow();
		tableRow9.setFont(new Font(null, null, null, "bold"));
		tableRow9.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow9);

		StringTokenizer st2 = null;

		if (informe.obterDescricao() != null)
			st2 = new StringTokenizer(informe.obterDescricao(), "\n");
		else {
			Entidade.Atributo descricao = (Entidade.Atributo) documento
					.obterDocumento().obterAtributo("descricao");
			if (descricao != null)
				st2 = new StringTokenizer(descricao.obterValor(), "\n");
			else
				st2 = new StringTokenizer("");
		}

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

		TableRow tableRow6 = new TableRow();
		tableRow6.setFont(new Font(null, null, null, null));
		tableRow6.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow6);

		TableRow tableRow19 = new TableRow();
		tableRow19.setFont(new Font(null, null, null, null));
		tableRow19.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow19);

		TableRow tableRow20 = new TableRow();
		tableRow20.setFont(new Font(null, null, null, null));
		tableRow20.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow20);

		TableRow tableRow21 = new TableRow();
		tableRow21.setFont(new Font(null, null, null, null));
		tableRow21.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow21);

		Table tablePessoas = new Table();
		tablePessoas.setFont(new Font("sans-serif", "10pt", null, null));
		tablePessoas.addColumnWidth("8cm");
		tablePessoas.addColumnWidth("8cm");

		TableRow tableRow7 = new TableRow();
		tableRow7.setFont(new Font(null, null, null, "bold"));
		tableRow7.setTextAlign("center");
		tableRow7.addCell(new Text(documento.obterAnalista().obterNome()));
		tableRow7.addCell(new Text(documento.obterChefeDivisao().obterNome()));
		tablePessoas.addBodyRow(tableRow7);

		Entidade.Atributo cargoAnalista = (Entidade.Atributo) documento
				.obterAnalista().obterAtributo("cargo");
		Entidade.Atributo cargoChefe = (Entidade.Atributo) documento
				.obterChefeDivisao().obterAtributo("cargo");

		TableRow tableRow10 = new TableRow();
		tableRow10.setFont(new Font(null, null, null, null));
		tableRow10.setTextAlign("center");
		if (cargoAnalista != null)
			tableRow10.addCell(new Text(cargoAnalista.obterValor()));
		else
			tableRow10.addCell(new Text(""));
		if (cargoChefe != null)
			tableRow10.addCell(new Text(cargoChefe.obterValor()));
		else
			tableRow10.addCell(new Text(""));

		tablePessoas.addBodyRow(tableRow10);

		TableRow tableRowAux2 = new TableRow();
		tableRowAux2.addCell(tablePessoas);
		tablePrincipal.addBodyRow(tableRowAux2);

		TableRow tableRow11 = new TableRow();
		tableRow11.setFont(new Font(null, null, null, null));
		tableRow11.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow11);

		TableRow tableRow12 = new TableRow();
		tableRow12.setFont(new Font(null, null, null, null));
		tableRow12
				.addCell(new Text(
						"Al Señor SUPERINTENDENTE DE SEGUROS, con mi parecer favorable."));
		tablePrincipal.addBodyRow(tableRow12);

		TableRow tableRow18 = new TableRow();
		tableRow18.setFont(new Font(null, null, null, null));
		tableRow18.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow18);

		TableRow tableRow22 = new TableRow();
		tableRow22.setFont(new Font(null, null, null, null));
		tableRow22.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow22);

		TableRow tableRow23 = new TableRow();
		tableRow23.setFont(new Font(null, null, null, null));
		tableRow23.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow23);

		TableRow tableRow13 = new TableRow();
		tableRow13.setFont(new Font(null, null, null, "bold"));
		tableRow13.setTextAlign("center");
		tableRow13.addCell(new Text(
				"INTENDENCIA DE REASEGUROS Y AUXILIAR DEL SEGURO"));
		tablePrincipal.addBodyRow(tableRow13);

		Entidade.Atributo cargoIntendente = (Entidade.Atributo) documento
				.obterIntendente().obterAtributo("cargo");

		TableRow tableRow14 = new TableRow();
		tableRow14.setFont(new Font(null, null, null, null));
		tableRow14.setTextAlign("center");
		if (cargoIntendente != null)
			tableRow14.addCell(new Text(documento.obterIntendente().obterNome()
					+ " - " + cargoIntendente.obterValor()));
		else
			tableRow14
					.addCell(new Text(documento.obterIntendente().obterNome()));

		tablePrincipal.addBodyRow(tableRow14);

		Region bodyRegion = new Region();
		bodyRegion.addBlock(tablePrincipal);
		this.setBody(bodyRegion);
	}
}