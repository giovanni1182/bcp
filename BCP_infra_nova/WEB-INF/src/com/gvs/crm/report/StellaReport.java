package com.gvs.crm.report;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gvs.crm.model.Circular;
import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Informe;
import com.gvs.crm.model.Memorando;
import com.gvs.crm.model.Renovacao;
import com.gvs.crm.model.Resolucao;

import infra.view.report.Font;
import infra.view.report.Image;
import infra.view.report.Region;
import infra.view.report.Report;
import infra.view.report.Table;
import infra.view.report.TableRow;
import infra.view.report.Text;

public class StellaReport extends A4Report {
	public StellaReport(String title, Evento evento) throws Exception {
		super();

		DocumentoProduto documento = null;

		String numero = "";

		if (!(evento instanceof Renovacao)) {
			documento = (DocumentoProduto) evento.obterSuperior();
			if (documento.obterNumero() != null)
				numero = documento.obterNumero();
		}

		// constroi o cabeçalho
		Region headerRegion = new Region();

		Table headerTable = new Table();
		headerTable.addColumnWidth("17cm");
		//		headerTable.setBorder(new Border(null, null, "1pt", null, "solid",
		// "black"));
		headerTable.setFont(new Font("sans-serif", "12pt", null, "bold"));

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
		//		headerRow1.addCell(new Text("Battistella Ind. e Com. Ltda"));
		headerRow1.addCell(new Image("logo_bcp.gif"));

		TableRow tableRow = new TableRow();
		tableRow.setTextAlign("center");

		if (evento instanceof Memorando)
			tableRow.addCell(new Text("MEMORANDO SS.SG Nº " + numero));
		else if (evento instanceof Informe)
			tableRow.addCell(new Text("INFORME SS.ICORAS.DCAS. Nº "
					+ documento.obterNumero() + " RESERVADO"));
		else if (evento instanceof Resolucao)
			tableRow.addCell(new Text("RESOLUCIÓN SS.SG. Nº "
					+ documento.obterNumero()));
		else if (evento instanceof Circular)
			tableRow.addCell(new Text("CIRCULAR SS.SG. Nº "
					+ documento.obterNumero()));
		else if (evento instanceof Renovacao)
			tableRow.addCell(new Text("RENOVACIÓN - CORREDOR DE SEGUROS"));

		TableRow tableRow2 = new TableRow();
		tableRow2.addCell(new Text(" ", 2));

		headerTable.addBodyRow(headerRow);
		headerTable.addBodyRow(headerRow1);
		headerTable.addBodyRow(tableRow2);
		headerTable.addBodyRow(tableRow);

		headerRegion.addBlock(headerTable);
		this.setBefore("4.0cm", headerRegion);
	}
}