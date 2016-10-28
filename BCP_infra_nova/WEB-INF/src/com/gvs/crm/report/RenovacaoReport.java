package com.gvs.crm.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Inscricao;
import com.gvs.crm.model.Renovacao;

import infra.view.report.Font;
import infra.view.report.Region;
import infra.view.report.Table;
import infra.view.report.TableRow;
import infra.view.report.Text;

public class RenovacaoReport extends StellaReport {
	public RenovacaoReport(Renovacao renovacao, Locale locale) throws Exception {
		super(renovacao.obterSuperior().obterTitulo(), renovacao);

		Inscricao inscricao = (Inscricao) renovacao.obterSuperior();

		Table tablePrincipal = new Table();
		tablePrincipal.setFont(new Font("sans-serif", "12pt", null, null));
		tablePrincipal.addColumnWidth("17cm");

		TableRow tableRow = new TableRow();
		tableRow.setFont(new Font(null, null, null, null));
		tableRow.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow);

		TableRow tableRow2 = new TableRow();
		tableRow2.setFont(new Font(null, null, null, null));
		tableRow2.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow2);

		TableRow tableRow3 = new TableRow();
		tableRow3.setFont(new Font(null, null, null, null));
		tableRow3.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow3);

		SimpleDateFormat st = new SimpleDateFormat("dd/MM/yyyy", Locale
				.getDefault());

		TableRow tableRow4 = new TableRow();
		tableRow4.setFont(new Font(null, null, null, null));
		tableRow4.setTextAlign("right");
		tableRow4.addCell(new Text("Asunción, " + st.format(new Date())));
		tablePrincipal.addBodyRow(tableRow4);

		TableRow tableRow5 = new TableRow();
		tableRow5.setFont(new Font(null, null, null, null));
		tableRow5.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow5);

		Entidade.Atributo nacionalidade = (Entidade.Atributo) inscricao
				.obterOrigem().obterAtributo("nacionalidade");

		String nacionalidadeStr = "";
		String idadeStr = "";

		if (nacionalidade != null)
			nacionalidadeStr = nacionalidade.obterValor();

		String rua, bairro, cep, estado, pais, numero, complemento, cidade;

		rua = bairro = cep = estado = pais = numero = complemento = cidade = "";

		for (Iterator i = inscricao.obterOrigem().obterEnderecos().iterator(); i
				.hasNext();) {
			Entidade.Endereco endereco = (Entidade.Endereco) i.next();

			if (endereco.obterNome().equals("Dirección Comercial")) {
				rua = endereco.obterRua();
				bairro = endereco.obterBairro();
				cep = endereco.obterCep();
				estado = endereco.obterEstado();
				pais = endereco.obterPais();
				numero = endereco.obterNumero();
				complemento = endereco.obterComplemento();
				cidade = endereco.obterCidade();
			}
		}

		String foneResidencial, foneCelular;

		foneResidencial = foneCelular = "";

		for (Iterator i = inscricao.obterOrigem().obterContatos().iterator(); i
				.hasNext();) {
			Entidade.Contato contato = (Entidade.Contato) i.next();

			if (contato.obterNome().equals("Teléfono Comercial"))
				foneResidencial = contato.obterValor();
			else if (contato.obterNome().equals("Teléfono Celular"))
				foneCelular = contato.obterValor();
		}

		TableRow tableRow6 = new TableRow();
		tableRow6.setFont(new Font(null, null, null, null));
		tableRow6.addCell(new Text("", 2));
		tablePrincipal.addBodyRow(tableRow6);

		TableRow tableRow7 = new TableRow();
		tableRow7.setFont(new Font(null, null, null, null));
		tableRow7.addCell(new Text("Quien subscribe "
				+ inscricao.obterOrigem().obterNome().toUpperCase()));
		tablePrincipal.addBodyRow(tableRow7);

		TableRow tableRow8 = new TableRow();
		tableRow8.setFont(new Font(null, null, null, null));
		tableRow8.addCell(new Text("con Inscripción Nº "
				+ inscricao.obterInscricao()
				+ " com vigencia al "
				+ new SimpleDateFormat("dd/MM/yyyy").format(inscricao
						.obterDataValidade())));
		tablePrincipal.addBodyRow(tableRow8);

		TableRow tableRow9 = new TableRow();
		tableRow9.setFont(new Font(null, null, null, null));
		tableRow9.addCell(new Text("de nacionalidad " + nacionalidadeStr
				+ ", domiciliado en la calle" + rua + " Nº: " + numero
				+ " Ciudad " + cidade));
		tablePrincipal.addBodyRow(tableRow9);

		/*
		 * TableRow tableRow10 = new TableRow(); tableRow10.setFont(new
		 * Font(null, null, null, null)); tableRow10.addCell(new Text("de la
		 * calle " + rua + " Nº: " + numero + " Ciudad " + cidade));
		 * tablePrincipal.addBodyRow(tableRow10);
		 */

		TableRow tableRow11 = new TableRow();
		tableRow11.setFont(new Font(null, null, null, null));
		tableRow11.addCell(new Text("Teléfono Nº: " + foneResidencial
				+ " Celular Nº " + foneCelular
				+ " solicita la renovación de la"));
		tablePrincipal.addBodyRow(tableRow11);

		String ramos = "";

		for (Iterator i = inscricao.obterRamos().iterator(); i.hasNext();) {
			Inscricao.Ramo ramo = (Inscricao.Ramo) i.next();

			if (ramos.equals(""))
				ramos = ramo.obterRamo();
			else
				ramos += ", " + ramo.obterRamo();
		}

		TableRow tableRow12 = new TableRow();
		tableRow12.setFont(new Font(null, null, null, null));
		tableRow12.addCell(new Text("Inscripción Nº "
				+ inscricao.obterInscricao()
				+ " para ejercer la profesión en el(los) ramo(s) " + ramos));
		tablePrincipal.addBodyRow(tableRow12);

		TableRow tableRow13 = new TableRow();
		tableRow13.setFont(new Font(null, null, null, null));
		tableRow13.addCell(new Text("para la seguiente empresa "
				+ inscricao.obterOrigem().obterNome().toUpperCase()));
		tablePrincipal.addBodyRow(tableRow13);

		TableRow tableRow14 = new TableRow();
		tableRow14.setFont(new Font(null, null, null, null));
		tableRow14.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow14);

		TableRow tableRow15 = new TableRow();
		tableRow15.setFont(new Font(null, null, null, null));
		tableRow15.addCell(new Text(
				"Para tal efecto, acompaño los recautos siguientes:"));
		tablePrincipal.addBodyRow(tableRow15);

		TableRow tableRow16 = new TableRow();
		tableRow16.setFont(new Font(null, null, null, null));
		tableRow16.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow16);

		TableRow tableRow17 = new TableRow();
		tableRow17.setFont(new Font(null, null, null, null));
		if (renovacao.obterMatriculaAnterior())
			tableRow17
					.addCell(new Text(
							"(X) 1. Fotocopia de la Matricula anterior, del recurrente y la corredora."));
		else
			tableRow17
					.addCell(new Text(
							"( ) 1. Fotocopia de la Matricula anterior, del recurrente y la corredora."));
		tablePrincipal.addBodyRow(tableRow17);

		TableRow tableRow29 = new TableRow();
		tableRow29.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow29);

		TableRow tableRow18 = new TableRow();
		tableRow18.setFont(new Font(null, null, null, null));
		if (renovacao.obterCertificadoAntecedentes())
			tableRow18
					.addCell(new Text(
							"(X) 2. Original del Cetificado de Antecedentes Policiales del recurrente, titular del poder."));
		else
			tableRow18
					.addCell(new Text(
							"( ) 2. Original del Cetificado de Antecedentes Policiales del recurrente, titular del poder."));
		tablePrincipal.addBodyRow(tableRow18);

		TableRow tableRow30 = new TableRow();
		tableRow30.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow30);

		TableRow tableRow19 = new TableRow();
		tableRow19.setFont(new Font(null, null, null, null));
		if (renovacao.obterCertificadoJudicial())
			tableRow19
					.addCell(new Text(
							"(X) 3. Original del Certificado de No-Interdicción Judicial, expedido por la Dirección General de los Registros Públicos, de la corredora y del apoderado."));
		else
			tableRow19
					.addCell(new Text(
							"( ) 3. Original del Certificado de No-Interdicción Judicial, expedido por la Dirección General de los Registros Públicos, de la corredora y del apoderado."));
		tablePrincipal.addBodyRow(tableRow19);

		TableRow tableRow31 = new TableRow();
		tableRow31.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow31);

		TableRow tableRow20 = new TableRow();
		tableRow20.setFont(new Font(null, null, null, null));
		if (renovacao.obterCertificadoTributario())
			tableRow20
					.addCell(new Text(
							"(X) 4. Fotocopia autenticada del Certificado de Cumplimento Tributario, correspondiente al semestre de la presentacíon de la solicitud."));
		else
			tableRow20
					.addCell(new Text(
							"( ) 4. Fotocopia autenticada del Certificado de Cumplimento Tributario, correspondiente al semestre de la presentacíon de la solicitud."));
		tablePrincipal.addBodyRow(tableRow20);

		TableRow tableRow32 = new TableRow();
		tableRow32.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow32);

		TableRow tableRow21 = new TableRow();
		tableRow21.setFont(new Font(null, null, null, null));
		if (renovacao.obterDeclaracao())
			tableRow21
					.addCell(new Text(
							"(X) 5. Declaración Jurada de no tener ninguna de las inhabilidades que señala la Ley Nº 827/96 De Seguros, Capitulos III, del titular del poder."));
		else
			tableRow21
					.addCell(new Text(
							"( ) 5. Declaración Jurada de no tener ninguna de las inhabilidades que señala la Ley Nº 827/96 De Seguros, Capitulos III, del titular del poder."));
		tablePrincipal.addBodyRow(tableRow21);

		TableRow tableRow33 = new TableRow();
		tableRow33.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow33);

		TableRow tableRow22 = new TableRow();
		tableRow22.setFont(new Font(null, null, null, null));
		if (renovacao.obterComprovanteMatricula())
			tableRow22.addCell(new Text(
					"(X) 6. Comprobante de pago de Matricula (2 copias)."));
		else
			tableRow22.addCell(new Text(
					"( ) 6. Comprobante de pago de Matricula (2 copias)."));
		tablePrincipal.addBodyRow(tableRow22);

		TableRow tableRow34 = new TableRow();
		tableRow34.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow34);

		TableRow tableRow23 = new TableRow();
		tableRow23.setFont(new Font(null, null, null, null));
		if (renovacao.obterApoliceSeguro())
			tableRow23
					.addCell(new Text(
							"(X) 7. Presentación de una Póliza de Seguros de Garantia de Desempenho Profesional."));
		else
			tableRow23
					.addCell(new Text(
							"( ) 7. Presentación de una Póliza de Seguros de Garantia de Desempenho Profesional."));
		tablePrincipal.addBodyRow(tableRow23);

		TableRow tableRow35 = new TableRow();
		tableRow35.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow35);

		TableRow tableRow24 = new TableRow();
		tableRow24.setFont(new Font(null, null, null, null));
		if (renovacao.obterLivro())
			tableRow24
					.addCell(new Text(
							"(X) 8. Presentación del Libro de Producción rubricado por el juzgado correspondiente."));
		else
			tableRow24
					.addCell(new Text(
							"( ) 8. Presentación del Libro de Producción rubricado por el juzgado correspondiente."));
		tablePrincipal.addBodyRow(tableRow24);

		TableRow tableRow25 = new TableRow();
		tableRow25.setFont(new Font(null, null, null, null));
		tableRow25.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow25);

		TableRow tableRow26 = new TableRow();
		tableRow26.setFont(new Font(null, null, null, null));
		tableRow26.addCell(new Text("Muy atentamente"));
		tablePrincipal.addBodyRow(tableRow26);

		TableRow tableRow27 = new TableRow();
		tableRow27.setFont(new Font(null, null, null, null));
		tableRow27.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow27);

		TableRow tableRow28 = new TableRow();
		tableRow28.setFont(new Font(null, null, null, null));
		tableRow28
				.addCell(new Text(
						"NOTA: Las documentaciones que se acompanãn, están en el orden señalado y foliadas en"));
		tablePrincipal.addBodyRow(tableRow28);

		TableRow tableRow40 = new TableRow();
		tableRow40.setFont(new Font(null, null, null, null));
		tableRow40.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow40);

		TableRow tableRow41 = new TableRow();
		tableRow41.setFont(new Font(null, null, null, null));
		tableRow41.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow41);

		TableRow tableRow42 = new TableRow();
		tableRow42.setFont(new Font(null, null, null, null));
		tableRow42.addCell(new Text(" ", 2));
		tablePrincipal.addBodyRow(tableRow42);

		TableRow tableRow43 = new TableRow();
		tableRow43.setFont(new Font(null, null, null, null));
		tableRow43.setTextAlign("center");
		tableRow43.addCell(new Text(
				"_______________________________________________________"));
		tablePrincipal.addBodyRow(tableRow43);

		TableRow tableRow44 = new TableRow();
		tableRow44.setFont(new Font(null, null, null, null));
		tableRow44.setTextAlign("center");
		tableRow44.addCell(new Text(inscricao.obterOrigem().obterNome()));
		tablePrincipal.addBodyRow(tableRow44);

		Region bodyRegion = new Region();
		bodyRegion.addBlock(tablePrincipal);
		this.setBody(bodyRegion);
	}
}