package com.gvs.crm.view;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.DateLabel;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.MoedaSelect;
import com.gvs.crm.component.ResponsavelLabel;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.component.SuperiorLabel;
import com.gvs.crm.model.Produto;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDouble;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class ProdutoView extends EntidadeAbstrataView {
	private static final int DADOS = 0;

	private static final int COMPONENTES = 1;

	private static final int PRECOS = 2;

	private static final int OCORRENCIAS = 3;

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		Produto produto = (Produto) this.obterEntidade();
		boolean incluir = produto.obterId() == 0;
		Table table = new Table(2);

		int _pasta = Integer.parseInt(properties.getProperty("_pasta", "0"));

		if (incluir || _pasta > 4)
			_pasta = DADOS;

		Link dadosLink = new Link("Dados Básicos", new Action(
				"visualizarDetalhesEntidade"));
		((Label) dadosLink.getCaption()).setBold(_pasta == DADOS);
		dadosLink.getAction().add("id", produto.obterId());
		dadosLink.getAction().add("_pasta", DADOS);
		dadosLink.setEnabled(!incluir);

		Link componentesLink = new Link("Componentes", new Action(
				"visualizarDetalhesEntidade"));
		((Label) componentesLink.getCaption()).setBold(_pasta == COMPONENTES);
		componentesLink.getAction().add("id", produto.obterId());
		componentesLink.getAction().add("_pasta", COMPONENTES);
		componentesLink.setEnabled(!incluir);

		Link precosLink = new Link("Preços", new Action(
				"visualizarDetalhesEntidade"));
		((Label) precosLink.getCaption()).setBold(_pasta == PRECOS);
		precosLink.getAction().add("id", produto.obterId());
		precosLink.getAction().add("_pasta", PRECOS);
		precosLink.setEnabled(!incluir);

		Link ocorrenciasLink = new Link("Ocorrências", new Action(
				"visualizarDetalhesEntidade"));
		((Label) ocorrenciasLink.getCaption()).setBold(_pasta == OCORRENCIAS);
		ocorrenciasLink.getAction().add("id", produto.obterId());
		ocorrenciasLink.getAction().add("_pasta", OCORRENCIAS);
		ocorrenciasLink.setEnabled(!incluir);

		Block block = new Block(Block.HORIZONTAL);
		block.add(dadosLink);
		block.add(new SeparadorLabel());
		block.add(componentesLink);
		block.add(new SeparadorLabel());
		block.add(precosLink);
		block.add(new SeparadorLabel());
		block.add(ocorrenciasLink);
		table.setNextColSpan(table.getColumns());
		table.add(block);

		if (_pasta == DADOS) {
			table.addSubtitle("Dados");
			table.addHeader("Superior:");

			if (produto.obterSuperior().obterId() > 0)
				table.addData(new EntidadePopup("entidadeSuperiorId",
						"entidadeSuperiorNome", produto.obterSuperior(), true));
			else
				table.addData(new SuperiorLabel(produto));

			table.addHeader("Responsável:");
			table.addData(new ResponsavelLabel(produto));
			table.addHeader("Nome:");
			table.addData(new InputString("nome", produto.obterNome(), 64));
			table.addHeader("Apelido:");
			table
					.addData(new InputString("apelido", produto.obterApelido(),
							32));
			table.addHeader("Código Externo:");
			table.addData(new InputString("codigoExterno", produto
					.obterCodigoExterno(), 32));
			table.addHeader("Peso:");
			table.addData(new InputDouble("peso", produto.obterPeso(), 10));

			table.addHeader("Dados Adicionais:");
			table.addData(new EntidadeAtributosView(produto));

			if (!incluir) {
				table.addHeader("Criação:");
				table.addData(new DateLabel(produto.obterCriacao()));
				table.addHeader("Última Atualização:");
				table.addData(new DateLabel(produto.obterAtualizacao()));

			}
			if (incluir) {
				Action incluirAction = new Action("incluirProduto");
				incluirAction.add("superiorId", produto.obterSuperior()
						.obterId());
				Button incluirButton = new Button("Incluir", incluirAction);
				table.addFooter(incluirButton);

				Action cancelarAction = new Action("visualizarDetalhesEntidade");
				cancelarAction.add("id", produto.obterSuperior().obterId());
				table.addFooter(new Button("Cancelar", cancelarAction));
			} else {
				if (!produto.obterSuperior().obterApelido().equals(
						"produtosexcluidos")) {
					Action atualizarAction = new Action("atualizarProduto");
					atualizarAction.add("id", produto.obterId());
					Button atualizarButton = new Button("Atualizar",
							atualizarAction);
					atualizarButton.setEnabled(produto.permiteAtualizar());
					table.addFooter(atualizarButton);
				}

				/*
				 * Atenção
				 */
				if (!produto.obterSuperior().obterApelido().toLowerCase()
						.equals("produtosexcluidos")) {
					Action excluirAction = new Action("excluirProduto");
					excluirAction.add("id", produto.obterId());
					excluirAction.add("origemMenuId", produto.obterSuperior()
							.obterId());
					excluirAction
							.setConfirmation("Confirma exclusão do produto ?");
					Button excluirButton = new Button("Excluir", excluirAction);
					excluirButton.setEnabled(produto.permiteExcluir());
					table.addFooter(excluirButton);
				}
			}
		} else if (_pasta == COMPONENTES) {
			// TODO implementar a view ComponentesView
			table.addSubtitle("Componentes");
			table.setNextColSpan(table.getColumns());
			table.add(new EntidadesView(produto.obterInferiores()));

			Button novoComponenteButton = new Button("[Novo Componente]",
					new Action("novaEntidade"));
			novoComponenteButton.getAction().add("classe", "componente");
			novoComponenteButton.getAction().add("superiorId",
					produto.obterId());
			novoComponenteButton.getAction().add("origemMenuId",
					produto.obterId());
			table.add(novoComponenteButton);
		} else if (_pasta == PRECOS) {
			table.addSubtitle("Preços");
			Table precosTable = new Table(3);
			precosTable.addStyle(Table.STYLE_ALTERNATE);
			precosTable.addHeader("Tabela de Preço");
			precosTable.addHeader("Moeda");
			precosTable.setNextHAlign(Table.HALIGN_RIGHT);
			precosTable.addHeader("Valor");
			for (Iterator i = produto.obterPrecos().iterator(); i.hasNext();) {
				Produto.Preco preco = (Produto.Preco) i.next();
				precosTable.addData(preco.obterTipo());
				if (produto.permiteAtualizar()) {
					precosTable
							.addData(new MoedaSelect("moeda_"
									+ preco.obterTipo(), preco.obterMoeda(),
									"produto"));
					precosTable.setNextHAlign(Table.HALIGN_RIGHT);
					precosTable.addData(new InputDouble("valor_"
							+ preco.obterTipo(), preco.obterValor(), 10));
				} else {
					precosTable.addData(preco.obterMoeda());
					precosTable.setNextHAlign(Table.HALIGN_RIGHT);
					precosTable.addData(new Label(preco.obterValor(),
							"#,##0.00"));
				}
			}
			if (produto.permiteAtualizar()) {
				precosTable.addData(new InputString("tabelaPrecos", "", 32));
				precosTable.addData(new MoedaSelect("moeda", "", "produto"));
				precosTable.addData(new InputDouble("valor", 0, 10));
			}
			Button atualizarButton = new Button("Atualizar", new Action(
					"atualizarPrecosProduto"));
			atualizarButton.getAction().add("id", produto.obterId());
			atualizarButton.setEnabled(produto.permiteAtualizar());
			precosTable.addFooter(atualizarButton);
			table.setNextColSpan(table.getColumns());
			table.add(precosTable);
		} else if (_pasta == OCORRENCIAS) {
			table.addSubtitle("Ocorrências");
			table.setNextColSpan(table.getColumns());
			table.add(new EventosView(produto.obterOcorrencias()));
		}

		return table;
	}
}