package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.DateLabel;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.ResponsavelLabel;
import com.gvs.crm.component.SuperiorLabel;
import com.gvs.crm.model.EntidadeDocumento;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Table;
import infra.view.View;

public class EntidadeDocumentoView extends EntidadeAbstrataView {
	public View execute(User arg0, Locale arg1, Properties arg2)
			throws Exception {
		EntidadeDocumento documento = (EntidadeDocumento) this.obterEntidade();

		boolean incluir = documento.obterId() == 0;
		Table table = new Table(2);

		table.addSubtitle("Datos");

		table.addHeader("Superior:");
		if (documento.obterSuperior().obterId() > 0)
			table.addData(new EntidadePopup("entidadeSuperiorId",
					"entidadeSuperiorNome", documento.obterSuperior(), true));
		else
			table.addData(new SuperiorLabel(documento));

		table.addHeader("Responsable:");
		table.addData(new ResponsavelLabel(documento));
		table.addHeader("Nombre:");
		table.addData(new InputString("nome", documento.obterNome(), 64));
		table.addHeader("Denominación:");
		table.addData(new InputString("apelido", documento.obterApelido(), 32));

		table.addHeader("Datos Adicionales:");
		table.addData(new EntidadeAtributosView(documento));

		if (!incluir) {
			table.addHeader("Creado en:");
			table.addData(new DateLabel(documento.obterCriacao()));
			table.addHeader("Última Actualización:");
			table.addData(new DateLabel(documento.obterAtualizacao()));

		}
		if (incluir) {
			Action incluirAction = new Action("incluirEntidadeDocumento");
			incluirAction
					.add("superiorId", documento.obterSuperior().obterId());
			Button incluirButton = new Button("Agregar", incluirAction);
			table.addFooter(incluirButton);

			Action cancelarAction = new Action("visualizarDetalhesEntidade");
			cancelarAction.add("id", documento.obterSuperior().obterId());
			table.addFooter(new Button("Cancelar", cancelarAction));
		} else {
			Action atualizarAction = new Action("atualizarEntidadeDocumento");
			atualizarAction.add("id", documento.obterId());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			atualizarButton.setEnabled(documento.permiteAtualizar());
			table.addFooter(atualizarButton);

			Action excluirAction = new Action("excluirEntidade");
			excluirAction.add("id", documento.obterId());
			excluirAction.add("origemMenuId", documento.obterSuperior()
					.obterId());
			excluirAction.setConfirmation("Confirma exclusión ?");
			Button excluirButton = new Button("Eliminar", excluirAction);
			excluirButton.setEnabled(documento.permiteExcluir());
			table.addFooter(excluirButton);

		}
		return table;
	}
}