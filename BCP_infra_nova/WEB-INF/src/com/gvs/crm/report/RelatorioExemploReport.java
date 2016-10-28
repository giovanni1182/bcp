package com.gvs.crm.report;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;

import com.gvs.crm.model.Entidade;

import infra.view.report.Border;
import infra.view.report.Font;
import infra.view.report.Image;
import infra.view.report.Region;
import infra.view.report.Table;
import infra.view.report.TableRow;
import infra.view.report.Text;

public class RelatorioExemploReport extends CRMReport {
	public RelatorioExemploReport(Collection entidades) throws Exception {
		super("Relatório exemplo");

		// constroi o corpo
		Table table = new Table();
		table.setFont(new Font("sans-serif", "10pt", null, null));
		table.addColumnWidth("1cm");
		table.addColumnWidth("3cm");
		table.addColumnWidth("8cm");
		table.addColumnWidth("4cm");

		TableRow header = new TableRow();
		header.setFont(new Font(null, null, null, "bold"));
		header.setBorder(new Border(null, null, "1pt", null, "solid", "black"));
		header.addCell(new Text(""));
		header.addCell(new Text("Classe"));
		header.addCell(new Text("Nome"));
		header.addCell(new Text("Última Atualização"));
		table.addHeaderRow(header);

		for (Iterator i = entidades.iterator(); i.hasNext();) {
			Entidade e = (Entidade) i.next();
			TableRow row = new TableRow();
			row.addCell(new Image(e.obterIcone()));
			row.addCell(new Text(e.obterDescricaoClasse()));
			row.addCell(new Text(e.obterNome()));
			row.addCell(new Text(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
					.format(e.obterAtualizacao())));
			table.addBodyRow(row);
		}

		Region bodyRegion = new Region();
		bodyRegion.addBlock(table);
		this.setBody(bodyRegion);
	}
}