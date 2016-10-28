package com.gvs.crm.report;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.StringTokenizer;

import com.gvs.crm.model.Ditame;
import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Entidade;

import infra.view.report.Font;
import infra.view.report.Region;
import infra.view.report.Table;
import infra.view.report.TableRow;
import infra.view.report.Text;

public class DitameReport extends StellaReport {
	public DitameReport(Ditame ditame, Locale locale) throws Exception {
		super(ditame.obterSuperior().obterTitulo(), ditame);

		DocumentoProduto documento = (DocumentoProduto) ditame.obterSuperior();

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
		tableRow4.addCell(new Text(documento.obterTitulo()));
		tablePrincipal.addBodyRow(tableRow4);

		SimpleDateFormat st = new SimpleDateFormat("dd MMM yyyy", Locale
				.getDefault());

		TableRow tableRow12 = new TableRow();
		tableRow12.setFont(new Font(null, null, null, null));
		tableRow12.addCell(new Text("Asunción, "
				+ st.format(documento.obterCriacao())));
		tableRow12.setTextAlign("right");
		tablePrincipal.addBodyRow(tableRow12);

		TableRow tableRow14 = new TableRow();
		tableRow14.setFont(new Font(null, null, null, "bold"));
		tableRow14.addCell(new Text("Señor"));
		tablePrincipal.addBodyRow(tableRow14);

		TableRow tableRow11 = new TableRow();
		tableRow11.setFont(new Font(null, null, null, "bold"));
		tableRow11.addCell(new Text(documento.obterSuperIntendente()
				.obterNome()
				+ ", Superintendente de Seguros"));
		tablePrincipal.addBodyRow(tableRow11);

		TableRow tableRow13 = new TableRow();
		tableRow13.setFont(new Font(null, null, null, null));
		tableRow13.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow13);

		Entidade.Atributo descricao = (Entidade.Atributo) documento
				.obterDocumento().obterAtributo("descricao");

		String descricaoStr = "";

		if (descricao != null)
			descricaoStr = descricao.obterValor();

		if (ditame.obterDescricao() != null)
			descricaoStr = ditame.obterDescricao();

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

		Region bodyRegion = new Region();
		bodyRegion.addBlock(tablePrincipal);
		this.setBody(bodyRegion);
	}
}