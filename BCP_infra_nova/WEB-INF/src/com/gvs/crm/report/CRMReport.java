package com.gvs.crm.report;

import java.text.SimpleDateFormat;
import java.util.Date;

import infra.view.report.Border;
import infra.view.report.Font;
import infra.view.report.Region;
import infra.view.report.Report;
import infra.view.report.Table;
import infra.view.report.TableRow;
import infra.view.report.Text;

public class CRMReport extends A4Report {
	public CRMReport(String title) {
		super();

		// constroi o cabeçalho
		Region headerRegion = new Region();
		Table headerTable = new Table();
		headerTable.addColumnWidth("17cm");
		headerTable.setBorder(new Border(null, null, "1pt", null, "solid",
				"black"));
		headerTable.setFont(new Font("sans-serif", "14pt", null, "bold"));
		TableRow headerRow = new TableRow();
		headerRow.addCell(new Text("Horst CRM - " + title));
		headerTable.addBodyRow(headerRow);
		headerRegion.addBlock(headerTable);
		this.setBefore("1cm", headerRegion);

		// constroi o rodapé
		Region footerRegion = new Region();
		Table footerTable = new Table();
		footerTable.addColumnWidth("8.5cm");
		footerTable.addColumnWidth("8.5cm");
		footerTable.setBorder(new Border("1pt", null, null, null, "solid",
				"black"));
		footerTable.setFont(new Font("sans-serif", "8pt", "italic", null));
		TableRow footerRow = new TableRow();
		footerRow.addCell(new Text("Horst Soluções - "
				+ new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
		Text pageNumberText = new Text("Página ?");
		pageNumberText.setTextAlign("right");
		pageNumberText.addParameter(Report.PAGE_NUMBER);
		footerRow.addCell(pageNumberText);
		footerTable.addBodyRow(footerRow);
		footerRegion.addBlock(footerTable);
		this.setAfter("1cm", footerRegion);
	}
}