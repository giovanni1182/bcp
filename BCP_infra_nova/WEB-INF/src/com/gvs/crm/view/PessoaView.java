package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Pessoa;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputString;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Link;
import infra.view.Select;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class PessoaView extends EntidadeAbstrataView {
	private static final int DETALHE = 0;

	private static final int FORMACION = 1;

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		Pessoa pessoa = (Pessoa) this.obterEntidade();

		boolean incluir = pessoa.obterId() == 0;

		Table table = new Table(2);
		table.setWidth("100%");

		int _pasta = Integer.parseInt(properties.getProperty("_pastaPessoa",
				"0"));

		if (incluir || _pasta > 1)
			_pasta = DETALHE;

		Link dadosLink = new Link("Detalles", new Action(
				"visualizarDetalhesEntidade"));
		((Label) dadosLink.getCaption()).setBold(_pasta == DETALHE);
		dadosLink.getAction().add("id", pessoa.obterId());
		dadosLink.getAction().add("_pastaPessoa", DETALHE);
		dadosLink.setEnabled(!incluir);

		Link componentesLink = new Link("Formación", new Action(
				"visualizarDetalhesEntidade"));
		((Label) componentesLink.getCaption()).setBold(_pasta == FORMACION);
		componentesLink.getAction().add("id", pessoa.obterId());
		componentesLink.getAction().add("_pastaPessoa", FORMACION);
		componentesLink.setEnabled(!incluir);

		Block block = new Block(Block.HORIZONTAL);
		block.add(dadosLink);
		block.add(new SeparadorLabel());
		block.add(componentesLink);

		table.setNextColSpan(table.getColumns());
		table.add(block);

		if (_pasta == DETALHE) {
			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			if (!incluir) {
				table.addHeader("Creado en:");
				String data = new SimpleDateFormat("dd/MM/yyyy").format(pessoa
						.obterCriacao());
				table.add(data);
			}

			table.addHeader("Superior:");

			if (incluir)
				table.add(new EntidadePopup("superiorId", "superiorNome",
						pessoa.obterSuperior(), true));
			else {
				Link link = new Link("<<< Superior", new Action(
						"visualizarDetalhesEntidade"));
				link.getAction().add("id", pessoa.obterSuperior().obterId());

				Block block2 = new Block(Block.HORIZONTAL);

				block2.add(new EntidadePopup("superiorId", "superiorNome",
						pessoa.obterSuperior(), true));

				block2.add(new Space(4));

				block2.add(link);

				table.add(block2);

			}

			table.addHeader("Responsable:");
			if (incluir)
				table.add(new EntidadePopup("responsavelId", "responsavelNome",
						pessoa.obterResponsavel(), "Usuario", true));
			else
				table.add(new EntidadePopup("responsavelId", "responsavelNome",
						pessoa.obterResponsavel(), "Usuario", pessoa
								.permiteAtualizar()));

			Entidade.Atributo tipo = (Entidade.Atributo) pessoa
					.obterAtributo("tipo");

			table.addHeader("Nombre:");
			table.add(new InputString("nome", pessoa.obterNome(), 60));

			table.addHeader("Tipo de Persona:");
			Select select = new Select("atributo_tipo", 1);
			select.add("", "", false);
			if (tipo != null) {
				select.add("Socio", "Socio", "Socio".equals(tipo.obterValor()));
				select.add("Funcionario", "Funcionario", "Funcionario"
						.equals(tipo.obterValor()));
			} else {
				select.add("Socio", "Socio", false);
				select.add("Funcionario", "Funcionario", false);
			}

			table.add(select);

			Entidade.Atributo cargo = (Entidade.Atributo) pessoa
					.obterAtributo("cargo");

			table.addHeader("Cargo:");
			Select select3 = new Select("atributo_cargo", 1);
			select3.add("", "", false);
			if (cargo != null) {
				select3.add("Presidente", "Presidente", "Presidente"
						.equals(cargo.obterValor()));
				select3.add("Director Comercial", "Director Comercial",
						"Director Comercial".equals(cargo.obterValor()));
				select3.add("Gerente Comercial", "Gerente Comercial",
						"Gerente Comercial".equals(cargo.obterValor()));
				select3.add("Auditor Externo", "Auditor Externo",
						"Auditor Externo".equals(cargo.obterValor()));
				select3.add("Auditor Junior", "Auditor Junior",
						"Auditor Junior".equals(cargo.obterValor()));
				select3.add("Auditor", "Auditor", "Auditor".equals(cargo
						.obterValor()));

			} else {
				select3.add("Presidente", "Presidente", false);
				select3.add("Director Comercial", "Director Comercial", false);
				select3.add("Gerente Comercial", "Gerente Comercial", false);
				select3.add("Auditor Externo", "Auditor Externo", false);
				select3.add("Auditor Junior", "Auditor Junior", false);
				select3.add("Auditor", "Auditor", false);
			}

			table.add(select3);

			Entidade.Atributo participacao = (Entidade.Atributo) pessoa
					.obterAtributo("participacao");

			String participacaoStr = "";
			if (participacao != null)
				participacaoStr = participacao.obterValor();

			Table table2 = new Table(1);
			table2.addSubtitle("Datos de la Sociedad");

			Block block2 = new Block(Block.HORIZONTAL);

			Label label = new Label("Participación:");
			label.setBold(true);

			block2.add(label);
			block2.add(new Space());
			block2.add(new InputString("atributo_participacao",
					participacaoStr, 8));
			block2.add(new Space());
			block2.add(new Label("%"));

			table2.add(block2);

			table.setNextColSpan(table.getColumns());
			table.add(table2);

			Table table3 = new Table(2);

			table3.addSubtitle("Datos Generales");

			Entidade.Atributo tipoResponsabilidade = (Entidade.Atributo) pessoa
					.obterAtributo("tiporesponsabilidade");

			table3.addHeader("Tipo de Responsabilidad:");
			Select select2 = new Select("atributo_tiporesponsabilidade", 1);
			select2.add("", "", false);

			if (tipoResponsabilidade != null) {
				select2.add("Apoderado", "Apoderado", "Apoderado"
						.equals(tipoResponsabilidade.obterValor()));
			} else {
				select2.add("Apoderado", "Apoderado", false);
			}

			table3.add(select2);

			Entidade.Atributo cumpleAnos = (Entidade.Atributo) pessoa
					.obterAtributo("cumpleanos");

			table3.addHeader("Fecha de Cumpleaños:");

			if (cumpleAnos != null && !cumpleAnos.obterValor().equals(""))
				table3.add(new InputDate("atributo_cumpleanos",
						new SimpleDateFormat("dd/MM/yyyy").parse(cumpleAnos
								.obterValor())));
			else
				table3.add(new InputDate("atributo_cumpleanos", null));

			Entidade.Atributo registro = (Entidade.Atributo) pessoa
					.obterAtributo("registro");

			String registroStr = "";
			if (registro != null)
				registroStr = registro.obterValor();

			table3.addHeader("Registro Contribuyente:");
			table3.add(new InputString("atributo_registro", registroStr, 15));

			Entidade.Atributo patente = (Entidade.Atributo) pessoa
					.obterAtributo("patente");

			String patenteStr = "";
			if (patente != null)
				patenteStr = patente.obterValor();

			table3.addHeader("Patente Municipal:");
			table3.add(new InputString("atributo_patente", patenteStr, 15));

			Entidade.Atributo contador = (Entidade.Atributo) pessoa
					.obterAtributo("contador");

			String contadorStr = "";
			if (contador != null)
				contadorStr = contador.obterValor();

			table3.addHeader("Matricula Contador:");
			table3.add(new InputString("atributo_contador", contadorStr, 15));

			Entidade.Atributo experiencia = (Entidade.Atributo) pessoa
					.obterAtributo("experiencia");

			String experienciaStr = "";
			if (experiencia != null)
				experienciaStr = experiencia.obterValor();

			table3.addHeader("Experiencia en Auditoria:");
			table3.add(new InputText("atributo_experiencia", experienciaStr, 5,
					80));

			table.setNextColSpan(table.getColumns());
			table.add(table3);

			table.addSubtitle("Contactos:");
			table.addHeader("Contactos:");
			table.addData(new EntidadeContatosView(pessoa));

			table.addSubtitle("Direcciones:");
			table.addHeader("Direcciones:");
			table.add(new EntidadeEnderecosView(pessoa));

			if (incluir) {
				Button incluirButton = new Button("Agregar", new Action(
						"incluirPessoa"));
				if (pessoa.obterSuperior() != null)
					incluirButton.getAction().add("superriorId",
							pessoa.obterSuperior().obterId());
				table.addFooter(incluirButton);
			} else {
				Button atualizarButton = new Button("Actualizar", new Action(
						"atualizarPessoa"));
				atualizarButton.getAction().add("id", pessoa.obterId());
				atualizarButton.setEnabled(pessoa.permiteAtualizar());
				table.addFooter(atualizarButton);

				Button excluirButton = new Button("Eliminar", new Action(
						"excluirEntidade"));
				excluirButton.getAction().add("id", pessoa.obterId());
				excluirButton.setEnabled(pessoa.permiteExcluir());
				table.addFooter(excluirButton);
			}
		}

		else if (_pasta == FORMACION) {
			table.addSubtitle("Formación");
			table.setNextColSpan(table.getColumns());
			table.add(new PessoaFormacoesView(pessoa));
		}

		return table;
	}

}