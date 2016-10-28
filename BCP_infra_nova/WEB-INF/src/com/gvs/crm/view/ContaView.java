package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.AtivoCheck;
import com.gvs.crm.component.DateLabel;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.NivelSelect;
import com.gvs.crm.component.ResponsavelLabel;
import com.gvs.crm.model.Conta;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputString;
import infra.view.InputText;
import infra.view.Table;
import infra.view.View;

public class ContaView extends EntidadeAbstrataView 
{
	public View execute(User user, Locale locale, Properties properties) throws Exception 
	{
		Conta conta = (Conta) this.obterEntidade();
		boolean incluir = conta.obterId() == 0;
		Table table = new Table(2);
	//	table.setWidth("100%");
		table.addHeader("Superior:");
		table.addData(new EntidadePopup("entidadeSuperiorId", "superiorNome",
				conta.obterSuperior(), "ClassificacaoContas", conta
						.permiteAtualizar()));

		if (conta.obterId() != 0) {
			table.addHeader("Responsable:");
			table.addData(new ResponsavelLabel(conta));
		}

		table.addHeader("Nombre:");
		table.addData(new InputString("nome", conta.obterNome(), 64));

		table.addHeader("Código de la Cuenta:");
		table.addData(new InputString("codigo", conta.obterCodigo(), 32));

		table.addHeader("Nivel:");
		table.addData(new NivelSelect("nivel", conta.obterNivel()));

		table.addHeader("Activo:");

		if (incluir)
			table.addData(new AtivoCheck("ativo", false));
		else
			table.addData(new AtivoCheck("ativo", conta.obterAtivo()));

		table.addHeader("Descripción:");
		table.addData(new InputText("descricao", conta.obterDescricao(), 30,
				100));

		if (conta.obterId() == 0) {
			Action incluirAction = new Action("incluirConta");
			incluirAction.add("superiorId", conta.obterSuperior().obterId());
			incluirAction.add("origemMenuId", conta.obterSuperior().obterId());
			Button incluirButton = new Button("Incluir", incluirAction);
			table.addFooter(incluirButton);

			Action cancelarAction = new Action("visualizarPaginaInicial");
			table.addFooter(new Button("Cancelar", cancelarAction));

			Action voltarAction = new Action("novaEntidade");
			voltarAction.add("superiorId", conta.obterSuperior().obterId());
			voltarAction.add("origemMenuId", this.obterOrigemMenu().obterId());

			Button voltarButton = new Button("Retornar", voltarAction);
			table.addFooter(voltarButton);
		}
		else
		{
			table.addHeader("Creación:");
			table.addData(new DateLabel(conta.obterCriacao()));
			table.addHeader("Última Actualización:");
			table.addData(new DateLabel(conta.obterAtualizacao()));

			Action atualizarAction = new Action("atualizarConta");
			atualizarAction.add("id", conta.obterId());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			atualizarButton.setEnabled(conta.permiteAtualizar());
			table.addFooter(atualizarButton);

			Action excluirAction = new Action("excluirEntidade");
			excluirAction.add("id", conta.obterId());
			excluirAction.setConfirmation("Confirma exclusion ?");
			Button excluirButton = new Button("Eliminar", excluirAction);
			excluirButton.setEnabled(conta.permiteExcluir() && conta.permiteAtualizar());
			table.addFooter(excluirButton);
		}
		return table;
	}
}