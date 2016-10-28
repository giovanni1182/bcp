package com.gvs.crm.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;

import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Inscricao;

import infra.view.report.Font;
import infra.view.report.Image;
import infra.view.report.Region;
import infra.view.report.Report;
import infra.view.report.Table;
import infra.view.report.TableRow;
import infra.view.report.Text;

public class DocumentoReport extends A4Report {
	public DocumentoReport(DocumentoProduto documento, Locale locale)
			throws Exception {
		super();

		Table tablePrincipal = new Table();
		tablePrincipal.addColumnWidth("17cm");
		//		headerTable.setBorder(new Border(null, null, "1pt", null, "solid",
		// "black"));
		tablePrincipal.setFont(new Font("sans-serif", "10pt", null, "bold"));

		//		 constroi o cabeçalho
		Region headerRegion = new Region();

		Table headerTable = new Table();
		headerTable.addColumnWidth("17cm");
		//		headerTable.setBorder(new Border(null, null, "1pt", null, "solid",
		// "black"));
		headerTable.setFont(new Font("sans-serif", "10pt", null, "bold"));

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

		headerTable.addBodyRow(headerRow);
		headerTable.addBodyRow(headerRow1);

		headerRegion.addBlock(headerTable);
		this.setBefore("4.0cm", headerRegion);

		TableRow espaco = new TableRow();
		espaco.setFont(new Font(null, null, null, null));
		espaco.setTextAlign("center");
		espaco.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(espaco);

		TableRow headerRow9 = new TableRow();
		headerRow9.setFont(new Font(null, null, null, "bold"));
		headerRow9.setTextAlign("center");
		headerRow9.addCell(new Text(documento.obterDocumento().obterNome()
				.toUpperCase()
				+ " - " + documento.obterNumero()));
		tablePrincipal.addBodyRow(headerRow9);

		TableRow headerRow2 = new TableRow();
		headerRow2.setFont(new Font(null, null, null, "bold"));
		headerRow2.setTextAlign("center");
		headerRow2.addCell(new Text(documento.obterTituloDocumento()));
		tablePrincipal.addBodyRow(headerRow2);

		TableRow espaco2 = new TableRow();
		espaco2.setFont(new Font(null, null, null, null));
		espaco2.setTextAlign("center");
		espaco2.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(espaco2);

		SimpleDateFormat st = new SimpleDateFormat("dd/MM/yyyy", Locale
				.getDefault());

		TableRow tableRow7 = new TableRow();
		tableRow7.setFont(new Font(null, null, null, null));
		tableRow7.addCell(new Text("Asunción, " + st.format(new Date())));
		tableRow7.setTextAlign("right");
		tablePrincipal.addBodyRow(tableRow7);

		TableRow espaco5 = new TableRow();
		espaco5.setFont(new Font(null, null, null, null));
		espaco5.setTextAlign("center");
		espaco5.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(espaco5);

		for (Iterator i = documento.obterPessoasAntes().iterator(); i.hasNext();) {
			DocumentoProduto.Pessoa pessoa = (DocumentoProduto.Pessoa) i.next();

			TableRow headerRow3 = new TableRow();
			headerRow3.setFont(new Font(null, null, null, "bold"));
			if (pessoa.obterTipo().equals(""))
				headerRow3.addCell(new Text(pessoa.obterPessoa().obterNome()));
			else
				headerRow3.addCell(new Text(pessoa.obterTipo() + ": "
						+ pessoa.obterPessoa().obterNome()));

			tablePrincipal.addBodyRow(headerRow3);

			Entidade.Atributo cargo = (Entidade.Atributo) pessoa.obterPessoa()
					.obterAtributo("cargo");

			if (cargo != null) {
				TableRow headerRow4 = new TableRow();
				headerRow4.setFont(new Font(null, null, null, null));
				headerRow4.addCell(new Text(cargo.obterValor()));
				tablePrincipal.addBodyRow(headerRow4);
			} else {
				TableRow headerRow4 = new TableRow();
				headerRow4.setFont(new Font(null, null, null, null));
				headerRow4.addCell(new Text(""));
				tablePrincipal.addBodyRow(headerRow4);
			}

			TableRow espaco9 = new TableRow();
			espaco9.setFont(new Font(null, null, null, null));
			espaco9.setTextAlign("center");
			espaco9.addCell(new Text("", 2));
			tablePrincipal.addBodyRow(espaco9);

		}

		TableRow espaco4 = new TableRow();
		espaco4.setFont(new Font(null, null, null, null));
		espaco4.setTextAlign("center");
		espaco4.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(espaco4);

		if (documento.obterReferente() != null
				&& !documento.obterReferente().equals("")) {
			TableRow headerRow6 = new TableRow();
			headerRow6.setFont(new Font(null, null, null, null));
			headerRow6.addCell(new Text("Ref.: " + documento.obterReferente()));
			tablePrincipal.addBodyRow(headerRow6);
		}

		TableRow espaco3 = new TableRow();
		espaco3.setFont(new Font(null, null, null, null));
		espaco3.setTextAlign("center");
		espaco3.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(espaco3);

		//String data = new
		// SimpleDateFormat("dd/MM/yyyy").format(documento.obterDataPrevistaInicio());

		/*
		 * TableRow headerRow4 = new TableRow(); headerRow4.setFont(new
		 * Font(null, null, null, null)); headerRow4.addCell(new Text("Fecha del
		 * Documento: " + data)); tablePrincipal.addBodyRow(headerRow4);
		 * 
		 * TableRow headerRow5 = new TableRow(); headerRow5.setFont(new
		 * Font(null, null, null, null)); headerRow5.addCell(new Text("Nº del
		 * Documento: " + documento.obterNumero()));
		 * tablePrincipal.addBodyRow(headerRow5);
		 */

		TableRow espaco6 = new TableRow();
		espaco6.setFont(new Font(null, null, null, null));
		espaco6.setTextAlign("center");
		espaco6.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(espaco6);

		String descricao = "";

		if (documento.obterTexto() != null
				&& !documento.obterTexto().trim().equals(""))
			descricao = documento.obterTexto();
		else {
			descricao = documento.obterDocumento().obterAtributo("descricao")
					.obterValor();

			int cont = 1;

			String pessoas = "\n";

			for (Iterator i = documento.obterInscricoesVinculadas().iterator(); i.hasNext();) 
			{
				Inscricao inscricao = (Inscricao) i.next();

				pessoas += new Integer(cont).toString() + " - " + "C.I nº "	+ inscricao.obterInscricao() + " - " + inscricao.obterOrigem().obterNome() + "\n";

				cont++;
			}

			descricao += pessoas;
		}

		StringTokenizer st2 = new StringTokenizer(descricao, "\n");

		while (st2.hasMoreTokens()) {
			String s = st2.nextToken();

			/*
			 * if(s.equals("\r")) { TableRow espaco8 = new TableRow();
			 * espaco8.setFont(new Font(null, null, null, null));
			 * espaco8.addCell(new Text("",2));
			 * tablePrincipal.addBodyRow(espaco8); } else {
			 */
			TableRow headerRow7 = new TableRow();
			headerRow7.setFont(new Font(null, null, null, null));
			headerRow7.addCell(new Text(s));
			tablePrincipal.addBodyRow(headerRow7);

			TableRow espaco8 = new TableRow();
			espaco8.setFont(new Font(null, null, null, null));
			espaco8.addCell(new Text("", 2));
			tablePrincipal.addBodyRow(espaco8);
			//}
		}

		TableRow espaco7 = new TableRow();
		espaco7.setFont(new Font(null, null, null, null));
		espaco7.setTextAlign("center");
		espaco7.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(espaco7);

		Table tablePessoasDepois = new Table();
		tablePessoasDepois.addColumnWidth("8cm");
		tablePessoasDepois.addColumnWidth("8cm");
		tablePessoasDepois.setFont(new Font("sans-serif", "12pt", null, null));

		int primeiro = 1;

		for (Iterator i = documento.obterPessoasDepois().iterator(); i
				.hasNext();) {
			DocumentoProduto.Pessoa pessoa = (DocumentoProduto.Pessoa) i.next();

			Table tablePessoasDepois2 = new Table();
			tablePessoasDepois2.addColumnWidth("8cm");

			if (primeiro > 1) {
				tablePessoasDepois2.addBodyRow(espaco);
				tablePessoasDepois2.addBodyRow(espaco);
				tablePessoasDepois2.addBodyRow(espaco);
			}

			TableRow headerRow8 = new TableRow();
			headerRow8.setFont(new Font(null, null, null, "bold"));
			if (pessoa.obterTipo().equals(""))
				headerRow8.addCell(new Text(pessoa.obterPessoa().obterNome()));
			else
				headerRow8.addCell(new Text(pessoa.obterTipo() + ": "
						+ pessoa.obterPessoa().obterNome()));

			tablePessoasDepois2.setTextAlign("center");
			tablePessoasDepois2.addBodyRow(headerRow8);

			Entidade.Atributo cargo = (Entidade.Atributo) pessoa.obterPessoa()
					.obterAtributo("cargo");

			if (cargo != null) {
				TableRow headerRow10 = new TableRow();
				headerRow10.setFont(new Font(null, null, null, null));
				headerRow10.addCell(new Text(cargo.obterValor()));
				tablePessoasDepois2.setTextAlign("center");
				tablePessoasDepois2.addBodyRow(headerRow10);
			} else {
				TableRow headerRow10 = new TableRow();
				headerRow10.setFont(new Font(null, null, null, null));
				headerRow10.addCell(new Text(""));
				tablePessoasDepois2.setTextAlign("center");
				tablePessoasDepois2.addBodyRow(headerRow10);
			}

			TableRow tableRowAux = new TableRow();
			tableRowAux.addCell(tablePessoasDepois2);

			tablePessoasDepois.addBodyRow(tableRowAux);

			primeiro++;
		}

		TableRow tableRowAux2 = new TableRow();
		tableRowAux2.addCell(tablePessoasDepois);

		tablePrincipal.addBodyRow(tableRowAux2);

		Region bodyRegion = new Region();
		bodyRegion.addBlock(tablePrincipal);
		this.setBody(bodyRegion);
	}
}