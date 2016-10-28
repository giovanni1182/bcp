package com.gvs.crm.report;

import infra.view.report.Font;
import infra.view.report.Region;
import infra.view.report.Report;
import infra.view.report.Table;
import infra.view.report.TableRow;
import infra.view.report.Text;

public class StellaSemReport extends A4ReportStella {
	public StellaSemReport(String title, String aux) {
		super();
		// constroi o cabeçalho
		Region headerRegion = new Region();
		Table headerTable = new Table();
		headerTable.addColumnWidth("17cm");
		headerTable.setFont(new Font("sans-serif", "14pt", null, "bold"));
		TableRow headerRow = new TableRow();
		headerRow.setFont(new Font("sans-serif", "8pt", null, null));
		Text pageNumberText = new Text(aux + " / " + "Página ?");
		pageNumberText.setTextAlign("right");
		pageNumberText.addParameter(Report.PAGE_NUMBER);
		headerRow.addCell(pageNumberText);
		headerTable.addBodyRow(headerRow);
		headerRegion.addBlock(headerTable);
		this.setBefore("3.1cm", headerRegion);
	}
}