package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.DateLabel;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.NivelSelect;
import com.gvs.crm.component.ResponsavelLabel;
import com.gvs.crm.model.ClassificacaoContas;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputString;
import infra.view.InputText;
import infra.view.Table;
import infra.view.View;

public class ClassificacaoContasView extends EntidadeAbstrataView 
{
	public View execute(User user, Locale locale, Properties properties) throws Exception 
	{
		ClassificacaoContas classificacaoContas = (ClassificacaoContas) this.obterEntidade();
		Table table = new Table(2);
		//table.setWidth("100%");
		table.addHeader("Superior:");
		table.addData(new EntidadePopup("entidadeSuperiorId", "superiorNome",
				classificacaoContas.obterSuperior(), "ClassificacaoContas",
				true));

		if (classificacaoContas.obterId() != 0) {
			table.addHeader("Responsable:");
			table.addData(new ResponsavelLabel(classificacaoContas));
		}

		table.addHeader("Nombre:");
		table.addData(new InputString("nome", classificacaoContas.obterNome(),
				64));

		table.addHeader("Código de la Cuenta:");
		table.addData(new InputString("codigo", classificacaoContas
				.obterCodigo(), 32));

		table.addHeader("Nivel:");
		table
				.addData(new NivelSelect("nivel", classificacaoContas
						.obterNivel()));

		table.addHeader("Descripción:");
		table.addData(new InputText("descricao", classificacaoContas
				.obterDescricao(), 30, 100));

		if (classificacaoContas.obterId() == 0) 
		{
			Action incluirAction = new Action("incluirClassificacaoContas");
			incluirAction.add("superiorId", classificacaoContas.obterSuperior().obterId());
			Button incluirButton = new Button("Agregar", incluirAction);
			table.addFooter(incluirButton);

			Action cancelarAction = new Action("visualizarPaginaInicial");
			table.addFooter(new Button("Cancelar", cancelarAction));

			Action voltarAction = new Action("novaEntidade");
			voltarAction.add("superiorId", classificacaoContas.obterSuperior().obterId());

			Button voltarButton = new Button("Volver", voltarAction);
			table.addFooter(voltarButton);
		} 
		else 
		{
			table.addHeader("Creación:");
			table.addData(new DateLabel(classificacaoContas.obterCriacao()));
			table.addHeader("Última Actualización:");
			table.addData(new DateLabel(classificacaoContas.obterAtualizacao()));

			Action atualizarAction = new Action("atualizarClassificacaoContas");
			atualizarAction.add("id", classificacaoContas.obterId());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			atualizarButton.setEnabled(classificacaoContas.permiteAtualizar());
			table.addFooter(atualizarButton);

			Action excluirAction = new Action("excluirEntidade");
			excluirAction.add("id", classificacaoContas.obterId());
			excluirAction.setConfirmation("Confirma exclusion ?");
			Button excluirButton = new Button("Eliminar", excluirAction);
			excluirButton.setEnabled(classificacaoContas.permiteExcluir() && classificacaoContas.permiteAtualizar());
			table.addFooter(excluirButton);
		}
		return table;
	}
}