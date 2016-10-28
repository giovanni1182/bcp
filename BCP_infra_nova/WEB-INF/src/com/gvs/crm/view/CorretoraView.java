package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.Corretora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Select;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class CorretoraView extends EntidadeAbstrataView {
	private static final int DETALHE = 0;

	private static final int PERSONAS = 1;

	private static final int CONTROLE = 2;

	public View execute(User user, Locale locale, Properties properties) throws Exception
	{
		Corretora corretora = (Corretora) this.obterEntidade();

		boolean incluir = corretora.obterId() == 0;

		Table table = new Table(2);
		//table.setWidth("100%");

		int _pasta = Integer.parseInt(properties.getProperty("_pastaCorretora",	"0"));

		if (incluir || _pasta > 2)
			_pasta = DETALHE;

		Link dadosLink = new Link("Detalles", new Action(
				"visualizarDetalhesEntidade"));
		((Label) dadosLink.getCaption()).setBold(_pasta == DETALHE);
		dadosLink.getAction().add("id", corretora.obterId());
		dadosLink.getAction().add("_pastaCorretora", DETALHE);
		dadosLink.setEnabled(!incluir);

		Link pessoasLink = new Link("Personas", new Action(
				"visualizarDetalhesEntidade"));
		((Label) pessoasLink.getCaption()).setBold(_pasta == PERSONAS);
		pessoasLink.getAction().add("id", corretora.obterId());
		pessoasLink.getAction().add("_pastaCorretora", PERSONAS);
		pessoasLink.setEnabled(!incluir);

		Link controleLink = new Link("Control SIS", new Action(
				"visualizarDetalhesEntidade"));
		((Label) controleLink.getCaption()).setBold(_pasta == CONTROLE);
		controleLink.getAction().add("id", corretora.obterId());
		controleLink.getAction().add("_pastaCorretora", CONTROLE);
		controleLink.setEnabled(!incluir);

		Block block2 = new Block(Block.HORIZONTAL);
		block2.add(dadosLink);
		block2.add(new SeparadorLabel());
		block2.add(pessoasLink);
		block2.add(new SeparadorLabel());
		block2.add(controleLink);

		table.setNextColSpan(table.getColumns());
		table.add(block2);

		if (_pasta == DETALHE) {
			table.addSubtitle("Detalles");

			if (!incluir) {
				table.addHeader("Creado en:");
				String data = new SimpleDateFormat("dd/MM/yyyy")
						.format(corretora.obterCriacao());
				table.add(data);
			}

			table.addHeader("Superior:");

			if (incluir)
				table.add(new EntidadePopup("entidadeSuperiorId",
						"superiorNome", corretora.obterSuperior(), true));
			else
				table.add(new EntidadePopup("entidadeSuperiorId",
						"superiorNome", corretora.obterSuperior(), corretora
								.permiteAtualizar()));

			table.addHeader("Responsable:");
			if (incluir)
				table.add(new EntidadePopup("responsavelId", "responsavelNome",
						corretora.obterResponsavel(), "Usuario", true));
			else
				table.add(new EntidadePopup("responsavelId", "responsavelNome",
						corretora.obterResponsavel(), "Usuario", corretora
								.permiteAtualizar()));

			Entidade.Atributo tipo = (Entidade.Atributo) corretora
					.obterAtributo("tipo");

			table.addHeader("Tipo:");
			Select select = new Select("atributo_tipo", 1);
			select.add("", "", false);
			if (tipo != null) {
				select.add("Reaseguradora", "Reaseguradora", "Reaseguradora"
						.equals(tipo.obterValor()));
				select.add("Seguradora", "Seguradora", "Seguradora".equals(tipo
						.obterValor()));
				select.add("Corredora", "Corredora", "Corredora".equals(tipo
						.obterValor()));
			} else {
				select.add("Reaseguradora", "Reaseguradora", false);
				select.add("Seguradora", "Seguradora", false);
				select.add("Corredora", "Corredora", false);

			}

			table.add(select);

			table.addHeader("Nombre:");
			table.add(new InputString("nome", corretora.obterNome(), 60));

			Entidade.Atributo nomeAbreviado = (Entidade.Atributo) corretora
					.obterAtributo("nomeabreviado");

			String nomeAbreviadoStr = "";
			if (nomeAbreviado != null)
				nomeAbreviadoStr = nomeAbreviado.obterValor();

			table.addHeader("Denominácion:");
			table.add(new InputString("atributo_nomeabreviado",
					nomeAbreviadoStr, 15));

			Entidade.Atributo nacionalidade = (Entidade.Atributo) corretora
					.obterAtributo("nacionalidade");

			String nacionalidadeStr = "";
			if (nacionalidade != null)
				nacionalidadeStr = nacionalidade.obterValor();

			table.addHeader("Pais de Origen:");
			table.add(new InputString("atributo_nacionalidade",
					nacionalidadeStr, 30));

			table.addHeader("Nº de Inscripciòn:");
			if (corretora.obterSigla() != null)
				table.add(corretora.obterSigla());
			else
				table.add("");

			table.addHeader("Identificación:");

			Block block = new Block(Block.HORIZONTAL);

			block.add(new InputString("ruc", corretora.obterRuc(), 15));
			block.add(new Space());
			block.add(new Label("Cédula de identidad(CI) ou RUC"));

			table.add(block);

			Entidade.Atributo situacao = (Entidade.Atributo) corretora
					.obterAtributo("situacao");

			table.addHeader("Stuación:");
			if (situacao != null) {
				if (situacao.obterValor() == null) {
					InputString input = new InputString("atributo_situacao",
							"No Activa", 20);
					input.setEnabled(false);

					table.addHeader(input);
				} else
					table.addHeader(situacao.obterValor());
			} else {
				InputString input = new InputString("atributo_situacao",
						"No Activa", 20);
				input.setEnabled(false);

				table.addHeader(input);
			}

			table.addSubtitle("Contactos:");
			table.addHeader("Contactos:");
			table.addData(new EntidadeContatosView(corretora));

			table.addSubtitle("Direcciones:");
			table.addHeader("Direcciones:");
			table.add(new EntidadeEnderecosView(corretora));

			if (incluir) {
				Button incluirButton = new Button("Agregar", new Action(
						"incluirCorretora"));

				table.addFooter(incluirButton);
			}
			else
			{
				Button atualizarButton = new Button("Actualizar", new Action(
						"atualizarCorretora"));
				atualizarButton.getAction().add("id", corretora.obterId());
				atualizarButton.setEnabled(corretora.permiteAtualizar());
				table.addFooter(atualizarButton);

				Button excluirButton = new Button("Eliminar", new Action("excluirEntidade"));
				excluirButton.getAction().add("id", corretora.obterId());
				excluirButton.setEnabled(corretora.permiteExcluir() && corretora.permiteAtualizar());
				table.addFooter(excluirButton);
			}
		}

		else if (_pasta == PERSONAS) {
			table.addSubtitle("Personas");
			table.setNextColSpan(table.getColumns());
			table.add(new PersonasAseguradoraView(corretora));
		}

		else if (_pasta == CONTROLE) {
			table.setNextColSpan(table.getColumns());
			table.add(new InscricoesView(corretora));
		}

		return table;
	}

}