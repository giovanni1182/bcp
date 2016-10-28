package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.DateLabel;
import com.gvs.crm.component.ResponsavelLabel;
import com.gvs.crm.component.SuperiorLabel;
import com.gvs.crm.model.ClassificacaoDocumento;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Table;
import infra.view.View;

public class ClassificacaoDocumentoView extends EntidadeAbstrataView {

	public View execute(User arg0, Locale arg1, Properties arg2)
			throws Exception {
		ClassificacaoDocumento classificacaoDocumento = (ClassificacaoDocumento) this
				.obterEntidade();

		Table table = new Table(2);
		table.setWidth("100%");
		table.addHeader("Superior:");
		table.addData(new SuperiorLabel(classificacaoDocumento));
		table.addHeader("Responsáble:");
		table.addData(new ResponsavelLabel(classificacaoDocumento));
		table.addHeader("Nombre:");
		table.addData(new InputString("nome", classificacaoDocumento
				.obterNome(), 64));
		table.addHeader("Denominación:");
		table.addData(new InputString("apelido", classificacaoDocumento
				.obterApelido(), 32));
		table.addHeader("Descripciòn:");
		table.addData(new InputString("descricao", classificacaoDocumento
				.obterDescricao(), 64));

		if (classificacaoDocumento.obterId() == 0) {
			Action incluirAction = new Action("incluirClassificacaoDocumento");
			incluirAction.add("superiorId", classificacaoDocumento
					.obterSuperior().obterId());
			Button incluirButton = new Button("Agregar", incluirAction);
			table.addFooter(incluirButton);

			Action cancelarAction = new Action("visualizarPaginaInicial");
			table.addFooter(new Button("Cancelar", cancelarAction));

			Action voltarAction = new Action("novaEntidade");
			voltarAction.add("superiorId", classificacaoDocumento
					.obterSuperior().obterId());
			voltarAction.add("origemMenuId", this.obterOrigemMenu().obterId());

			Button voltarButton = new Button("Volver", voltarAction);
			table.addFooter(voltarButton);
		} else {
			table.addHeader("Creado en:");
			table.addData(new DateLabel(classificacaoDocumento.obterCriacao()));
			table.addHeader("Última Actualizaciòn:");
			table.addData(new DateLabel(classificacaoDocumento
					.obterAtualizacao()));
			Action atualizarAction = new Action(
					"atualizarClassificacaoDocumento");
			atualizarAction.add("id", classificacaoDocumento.obterId());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			atualizarButton.setEnabled(classificacaoDocumento
					.permiteAtualizar());
			table.addFooter(atualizarButton);

			Action excluirAction = new Action("excluirEntidade");
			excluirAction.add("id", classificacaoDocumento.obterId());
			excluirAction.setConfirmation("Confirma exclusión ?");
			Button excluirButton = new Button("Eliminar", excluirAction);
			excluirButton.setEnabled(classificacaoDocumento.permiteExcluir());

			table.addFooter(excluirButton);
		}
		return table;
	}
}