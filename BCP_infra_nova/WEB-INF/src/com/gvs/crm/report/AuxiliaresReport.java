package com.gvs.crm.report;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;

import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Inscricao;

import infra.view.report.Font;
import infra.view.report.Region;
import infra.view.report.Table;
import infra.view.report.TableRow;
import infra.view.report.Text;

public class AuxiliaresReport extends A4Report {
	public AuxiliaresReport(Collection auxuliares) throws Exception {
		super();

		System.out.println("Size Aux: " + auxuliares.size());

		TableRow espaco = new TableRow();
		espaco.addCell(new Text("", 2));

		Table tablePrincipal = new Table();
		tablePrincipal.setFont(new Font("sans-serif", "10pt", null, null));
		tablePrincipal.addColumnWidth("18cm");

		int w = 1;

		for (Iterator i = auxuliares.iterator(); i.hasNext();) {
			AuxiliarSeguro auxiliar = (AuxiliarSeguro) i.next();

			System.out.println("Id: " + auxiliar.obterId());

			Table tableDados = new Table();
			tableDados.addColumnWidth("8cm");
			tableDados.addColumnWidth("8cm");

			if (w > 1) {
				/*
				 * tablePrincipal.addBodyRow(espaco);
				 * tablePrincipal.addBodyRow(espaco);
				 * tablePrincipal.addBodyRow(espaco);
				 */

				super.setPageBreak(1);

			}

			TableRow row1 = new TableRow();
			row1.addCell(new Text("Nombre: " + auxiliar.obterNome()));
			if (auxiliar.obterAseguradora() != null)
				row1.addCell(new Text("Aseguradora: "
						+ auxiliar.obterAseguradora().obterNome()));
			else
				row1.addCell(new Text("Aseguradora:"));

			tableDados.addBodyRow(row1);

			for (Iterator j = auxiliar.obterAtributos().iterator(); j.hasNext();) {
				Entidade.Atributo atributo = (Entidade.Atributo) j.next();

				if (!atributo.obterNome().equals("inscricao")) {
					TableRow row2 = new TableRow();
					row2.addCell(new Text(atributo.obterTitulo() + ": "
							+ atributo.obterValor()));

					tableDados.addBodyRow(row2);
				}
			}

			TableRow row22 = new TableRow();
			row22.setFont(new Font(null, null, null, null));
			row22.addCell(new Text("C�dula de identidad(RUC): "
					+ auxiliar.obterRuc()));
			tableDados.addBodyRow(row22);

			TableRow row2 = new TableRow();
			row2.setFont(new Font(null, null, null, "bold"));
			row2.addCell(new Text("Ramos"));
			tableDados.addBodyRow(row2);

			for (Iterator j = auxiliar.obterRamos().iterator(); j.hasNext();) {
				AuxiliarSeguro.Ramo ramo = (AuxiliarSeguro.Ramo) j.next();

				TableRow row3 = new TableRow();
				row3.addCell(new Text(ramo.obterRamo()));

				tableDados.addBodyRow(row3);
			}

			tableDados.addBodyRow(espaco);

			TableRow row3 = new TableRow();
			row3.setFont(new Font(null, null, null, "bold"));
			row3.addCell(new Text("Contactos"));
			tableDados.addBodyRow(row3);

			for (Iterator j = auxiliar.obterContatos().iterator(); j.hasNext();) {
				Entidade.Contato contato = (Entidade.Contato) j.next();

				TableRow row4 = new TableRow();
				row4.addCell(new Text(contato.obterNome() + ": "
						+ contato.obterValor()));

				tableDados.addBodyRow(row4);
			}

			tableDados.addBodyRow(espaco);

			TableRow row4 = new TableRow();
			row4.setFont(new Font(null, null, null, "bold"));
			row4.addCell(new Text("Direcciones"));
			tableDados.addBodyRow(row4);

			for (Iterator j = auxiliar.obterEnderecos().iterator(); j.hasNext();) {
				Entidade.Endereco endereco = (Entidade.Endereco) j.next();

				TableRow row5 = new TableRow();
				row5.addCell(new Text(endereco.obterNome()));

				tableDados.addBodyRow(row5);

				TableRow row6 = new TableRow();
				row6.addCell(new Text("Calle:" + endereco.obterRua()));
				row6.addCell(new Text("N�mero:" + endereco.obterNumero()));
				tableDados.addBodyRow(row6);

				TableRow row7 = new TableRow();
				row7.addCell(new Text("Complemento:"
						+ endereco.obterComplemento()));
				row7.addCell(new Text("Caja Postal:" + endereco.obterCep()));
				tableDados.addBodyRow(row7);

				TableRow row8 = new TableRow();
				row8.addCell(new Text("Barrio:" + endereco.obterBairro()));
				row8.addCell(new Text("Ciudad:" + endereco.obterCidade()));
				tableDados.addBodyRow(row8);

				TableRow row9 = new TableRow();
				row9
						.addCell(new Text("Departamento:"
								+ endereco.obterEstado()));
				row9.addCell(new Text("Pa�s:" + endereco.obterPais()));
				tableDados.addBodyRow(row9);
			}

			tableDados.addBodyRow(espaco);

			TableRow aux = new TableRow();
			aux.addCell(tableDados);
			tablePrincipal.addBodyRow(aux);

			tablePrincipal.addBodyRow(espaco);
			tablePrincipal.addBodyRow(espaco);

			Table tableApolice = new Table();
			tableApolice.addColumnWidth("4cm");
			tableApolice.addColumnWidth("4cm");
			tableApolice.addColumnWidth("4cm");
			tableApolice.addColumnWidth("4cm");

			for (Iterator j = auxiliar.obterEventosComoOrigem().iterator(); j
					.hasNext();) {
				Evento e = (Evento) j.next();

				if (e instanceof Inscricao) {
					Inscricao inscricao = (Inscricao) e;

					TableRow row10 = new TableRow();
					row10.setFont(new Font(null, null, null, "bold"));

					if (inscricao.obterInscricao() != null)
						row10.addCell(new Text("Inscripci�n: "
								+ inscricao.obterInscricao()));
					else
						row10.addCell(new Text("Inscripci�n: "));

					tableApolice.addBodyRow(row10);

					TableRow row11 = new TableRow();
					row11.setFont(new Font(null, null, null, "bold"));

					if (inscricao.obterNumeroResolucao() != null)
						row11.addCell(new Text("N� de la Resoluci�n: "
								+ inscricao.obterNumeroResolucao()));
					else
						row11.addCell(new Text("N� de la Resoluci�n:"));

					tableApolice.addBodyRow(row11);

					TableRow row12 = new TableRow();
					row12.setFont(new Font(null, null, null, "bold"));

					if (inscricao.obterDataResolucao() != null)
						row12
								.addCell(new Text("Fecha de la Resoluci�n: "
										+ new SimpleDateFormat("dd/MM/yyyy")
												.format(inscricao
														.obterDataResolucao())));
					else
						row12.addCell(new Text("Fecha de la Resoluci�n:"));

					tableApolice.addBodyRow(row12);

					TableRow row13 = new TableRow();
					row13.setFont(new Font(null, null, null, "bold"));

					if (inscricao.obterDataValidade() != null)
						row13
								.addCell(new Text("Fecha de Validad: "
										+ new SimpleDateFormat("dd/MM/yyyy")
												.format(inscricao
														.obterDataValidade())));
					else
						row13.addCell(new Text("Fecha de Validad:"));

					tableApolice.addBodyRow(row13);

					TableRow row14 = new TableRow();
					row14.setFont(new Font(null, null, null, "bold"));

					if (inscricao.obterSituacao() != null)
						row14.addCell(new Text("Situaci�n: "
								+ inscricao.obterSituacao()));
					else
						row14.addCell(new Text("Situaci�n:"));

					tableApolice.addBodyRow(row14);

					TableRow row15 = new TableRow();
					row15.setFont(new Font(null, null, null, "bold"));

					String ramoStr = "";
					for (Iterator k = inscricao.obterRamos().iterator(); k
							.hasNext();) {
						Inscricao.Ramo ramo = (Inscricao.Ramo) k.next();

						if (ramo.obterRamo() != null)
							ramoStr += ramo.obterRamo() + "\n";
					}

					row15.addCell(new Text("Ramo en que opera: " + ramoStr));

					tableApolice.addBodyRow(row15);

				}
			}

			tableApolice.addBodyRow(espaco);
			tableApolice.addBodyRow(espaco);

			TableRow aux2 = new TableRow();
			aux2.addCell(tableApolice);
			tablePrincipal.addBodyRow(aux2);

			w++;
		}

		Region bodyRegion = new Region();
		bodyRegion.addBlock(tablePrincipal);
		this.setBody(bodyRegion);
	}
}