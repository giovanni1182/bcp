package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.DateLabel;
import com.gvs.crm.component.ResponsavelLabel;
import com.gvs.crm.component.SuperiorLabel;
import com.gvs.crm.model.ClassificacaoProduto;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Table;
import infra.view.View;

public class ClassificacaoProdutoView extends EntidadeAbstrataView {
	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		ClassificacaoProduto classificacaoProduto = (ClassificacaoProduto) this
				.obterEntidade();
		Table table = new Table(2);
		table.setWidth("100%");
		table.addHeader("Superior:");
		table.addData(new SuperiorLabel(classificacaoProduto));
		table.addHeader("Responsável:");
		table.addData(new ResponsavelLabel(classificacaoProduto));
		table.addHeader("Nome:");
		table.addData(new InputString("nome", classificacaoProduto.obterNome(),
				64));
		table.addHeader("Apelido:");
		table.addData(new InputString("apelido", classificacaoProduto
				.obterApelido(), 32));
		table.addHeader("Descrição:");
		table.addData(new InputString("descricao:", classificacaoProduto
				.obterDescricao(), 64));
		if (classificacaoProduto.obterId() == 0) {
			Action incluirAction = new Action("incluirClassificacaoProduto");
			incluirAction.add("superiorId", classificacaoProduto
					.obterSuperior().obterId());
			Button incluirButton = new Button("Incluir", incluirAction);
			table.addFooter(incluirButton);

			Action cancelarAction = new Action("visualizarPaginaInicial");
			table.addFooter(new Button("Cancelar", cancelarAction));

			Action voltarAction = new Action("novaEntidade");
			voltarAction.add("superiorId", classificacaoProduto.obterSuperior()
					.obterId());
			voltarAction.add("origemMenuId", this.obterOrigemMenu().obterId());

			Button voltarButton = new Button("Voltar", voltarAction);
			table.addFooter(voltarButton);
		} else {
			table.addHeader("Criação:");
			table.addData(new DateLabel(classificacaoProduto.obterCriacao()));
			table.addHeader("Última Atualização:");
			table
					.addData(new DateLabel(classificacaoProduto
							.obterAtualizacao()));
			Action atualizarAction = new Action("atualizarClassificacaoProduto");
			atualizarAction.add("id", classificacaoProduto.obterId());
			Button atualizarButton = new Button("Atualizar", atualizarAction);
			atualizarButton.setEnabled(classificacaoProduto.permiteAtualizar());
			table.addFooter(atualizarButton);
			Action excluirAction = new Action("excluirEntidade");
			excluirAction.add("id", classificacaoProduto.obterId());
			excluirAction
					.setConfirmation("Confirma exclusão da classificação de produto ?");
			Button excluirButton = new Button("Excluir", excluirAction);
			excluirButton.setEnabled(classificacaoProduto.permiteExcluir());
			table.addFooter(excluirButton);
		}
		return table;
	}
}