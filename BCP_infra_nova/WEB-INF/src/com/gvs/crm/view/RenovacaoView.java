package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Inscricao;
import com.gvs.crm.model.Renovacao;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Check;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class RenovacaoView extends EventoAbstratoView {
	public View execute(User arg0, Locale locale, Properties arg2)
			throws Exception {
		Renovacao renovacao = (Renovacao) this.obterEvento();

		Inscricao inscricao = (Inscricao) renovacao.obterSuperior();

		Table table = new Table(1);

		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("RENOVACIÓN - CORREDOR DE SEGUROS");

		table.add(new Space());

		SimpleDateFormat st = new SimpleDateFormat("dd/MM/yyyy", Locale
				.getDefault());

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.add("Asunción, " + st.format(new Date()));

		table.add(new Space());

		Entidade entidadeSubscreve = null;
		Entidade empresa = null;

		entidadeSubscreve = inscricao.obterOrigem();
		empresa = inscricao.obterOrigem();

		Entidade.Atributo nacionalidade = (Entidade.Atributo) entidadeSubscreve
				.obterAtributo("nacionalidade");
		Entidade.Atributo idade = (Entidade.Atributo) entidadeSubscreve
				.obterAtributo("idade");

		String nacionalidadeStr = "";
		String idadeStr = "";

		if (nacionalidade != null)
			nacionalidadeStr = nacionalidade.obterValor();
		if (idade != null)
			idadeStr = idade.obterValor();

		String rua, bairro, cep, estado, pais, numero, complemento, cidade;

		rua = bairro = cep = estado = pais = numero = complemento = cidade = "";

		for (Iterator i = entidadeSubscreve.obterEnderecos().iterator(); i
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

		for (Iterator i = entidadeSubscreve.obterContatos().iterator(); i
				.hasNext();) {
			Entidade.Contato contato = (Entidade.Contato) i.next();

			if (contato.obterNome().equals("Teléfono Comercial"))
				foneResidencial = contato.obterValor();
			else if (contato.obterNome().equals("Teléfono Celular"))
				foneCelular = contato.obterValor();
		}

		String ramos = "";

		for (Iterator i = inscricao.obterRamos().iterator(); i.hasNext();) {
			Inscricao.Ramo ramo = (Inscricao.Ramo) i.next();

			if (ramos.equals(""))
				ramos = ramo.obterRamo();
			else
				ramos += ", " + ramo.obterRamo();
		}

		table.add("Quien subscribe "
				+ entidadeSubscreve.obterNome().toUpperCase());
		table.add("con Inscripción Nº "
				+ inscricao.obterInscricao()
				+ " com vigencia al "
				+ new SimpleDateFormat("dd/MM/yyyy").format(inscricao
						.obterDataValidade()));
		table.add("de nacionalidad " + nacionalidadeStr
				+ " domiciliado en la calle " + rua + " Nº: " + numero
				+ " Ciudad " + cidade);
		table.add("Teléfono Nº: " + foneResidencial + " Celular Nº "
				+ foneCelular + " solicita la renovación de la");
		table.add("Inscripción de Nº " + inscricao.obterInscricao()
				+ " para ejercer la profesión en el(los) ramo(s) " + ramos);
		table.add("para la seguiente empresa "
				+ empresa.obterNome().toUpperCase());

		table.add(new Space());

		table.add("Para tal efecto, acompaño los recautos siguientes:");

		table.add(new Space());

		Block block = new Block(Block.HORIZONTAL);

		block.add(new Check("matriculaAnterior", "true", renovacao
				.obterMatriculaAnterior()));
		block.add(new Space(3));
		block
				.add(new Label(
						"1. Fotocopia de la Matricula anterior, del recurrente y la corredora."));
		table.add(block);

		Block block2 = new Block(Block.HORIZONTAL);

		block2.add(new Check("certificadoAntecedentes", "true", renovacao
				.obterCertificadoAntecedentes()));
		block2.add(new Space(3));
		block2
				.add(new Label(
						"2. Original del Cetificado de Antecedentes Policiales del recurrente, titular del poder."));
		table.add(block2);

		Block block3 = new Block(Block.HORIZONTAL);

		block3.add(new Check("certificadoJudicial", "true", renovacao
				.obterCertificadoJudicial()));
		block3.add(new Space(3));
		block3
				.add(new Label(
						"3. Original del Certificado de No-Interdicción Judicial, expedido por la Dirección General de los Registros Públicos, de la corredora y del apoderado."));
		table.add(block3);

		Block block4 = new Block(Block.HORIZONTAL);

		block4.add(new Check("certificadoTributario", "true", renovacao
				.obterCertificadoTributario()));
		block4.add(new Space(3));
		block4
				.add(new Label(
						"4. Fotocopia autenticada del Certificado de Cumplimento Tributario, correspondiente al semestre de la presentacíon de la solicitud."));
		table.add(block4);

		Block block5 = new Block(Block.HORIZONTAL);

		block5
				.add(new Check("declaracao", "true", renovacao
						.obterDeclaracao()));
		block5.add(new Space(3));
		block5
				.add(new Label(
						"5. Declaración Jurada de no tener ninguna de las inhabilidades que señala la Ley Nº 827/96 De Seguros, Capitulos III, del titular del poder."));
		table.add(block5);

		Block block6 = new Block(Block.HORIZONTAL);

		block6.add(new Check("comprovanteMatricula", "true", renovacao
				.obterComprovanteMatricula()));
		block6.add(new Space(3));
		block6
				.add(new Label(
						"6. Comprobante de pago de Matricula (2 copias)."));
		table.add(block6);

		Block block7 = new Block(Block.HORIZONTAL);

		block7.add(new Check("apoliceSeguro", "true", renovacao
				.obterApoliceSeguro()));
		block7.add(new Space(3));
		block7
				.add(new Label(
						"7. Presentación de una Póliza de Seguros de Garantia de Desempenho Profesional."));
		table.add(block7);

		Block block8 = new Block(Block.HORIZONTAL);

		block8.add(new Check("livro", "true", renovacao.obterLivro()));
		block8.add(new Space(3));
		block8
				.add(new Label(
						"8. Presentación del Libro de Producción rubricado por el juzgado correspondiente."));
		table.add(block8);

		table.add(new Space());

		table.add("Muy atentamente");

		table.add(new Space());

		table
				.add("NOTA: Las documentaciones que se acompanãn, están en el orden señalado y foliadas en");

		table.setNextColSpan(table.getColumns());
		table.add(new Space());
		table.setNextColSpan(table.getColumns());
		table.add(new Space());
		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add("______________________________________");

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add(inscricao.obterOrigem().obterNome());

		table.add(new Space());

		Button atualizarButton = new Button("Actualizar", new Action(
				"atualizarRenovacao"));
		atualizarButton.getAction().add("id", renovacao.obterId());

		table.addFooter(atualizarButton);

		Button imprimirButton = new Button("Imprimir", new Action(
				"abrirRenovacao"));
		imprimirButton.getAction().add("id", renovacao.obterId());

		table.addFooter(imprimirButton);

		Button voltarButton = new Button("Volver", new Action(
				"visualizarEvento"));
		voltarButton.getAction().add("id", renovacao.obterSuperior().obterId());

		table.addFooter(voltarButton);

		return table;
	}
}