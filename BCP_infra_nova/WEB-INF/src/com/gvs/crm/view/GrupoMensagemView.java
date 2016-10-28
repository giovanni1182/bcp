package com.gvs.crm.view;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.ResponsavelLabel;
import com.gvs.crm.component.SuperiorLabel;
import com.gvs.crm.model.GrupoMensagem;
import com.gvs.crm.model.GrupoMensagem.Membro;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Image;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class GrupoMensagemView extends EntidadeAbstrataView {
	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		GrupoMensagem grupo = (GrupoMensagem) this.obterEntidade();
		boolean incluir = grupo.obterId() == 0;
		Table table = new Table(2);
		table.setWidth("100%");
		table.addHeader("Superior:");

		table.add(new SuperiorLabel(grupo));

		if (grupo.obterId() != 0) {
			table.addHeader("Responsable:");
			table.addData(new ResponsavelLabel(grupo));
		}

		table.addHeader("Nombre:");
		table.addData(new InputString("nome", grupo.obterNome(), 64));

		if (!incluir) {
			table.addHeader("Novo Membro:");
			table.addData(new EntidadePopup("membroId", "membroNome", null,
					"Usuario", true));

		}

		if (!incluir) {
			table.add("");
			Button incluirMembroButton = new Button("Inserir Membro",
					new Action("adicionarMembro"));
			incluirMembroButton.getAction().add("id", grupo.obterId());
			table.add(incluirMembroButton);
			table.add("");
			table.add("");
		}

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		table.addHeader("Membros:");
		table.add("");

		if (!incluir) {
			Map m = new TreeMap();

			for (Iterator i = grupo.obterMembros().values().iterator(); i
					.hasNext();) {
				Membro membro = (Membro) i.next();
				m.put(membro.obterMembro().obterNome(), membro);
			}

			for (Iterator i = m.values().iterator(); i.hasNext();) {
				Membro membro = (Membro) i.next();
				Block block = new Block(Block.HORIZONTAL);
				if (grupo.permiteAtualizar()) {
					Action excluirAction = new Action("excluirMembro");
					excluirAction.add("id", grupo.obterId());
					excluirAction.add("idMembro", membro.obterMembro()
							.obterId());
					block.add(new Link(new Image("delete.gif"), excluirAction));
					block.add(new Space(2));

				}

				Action action = new Action("visualizarDetalhesEntidade");
				action.add("id", membro.obterMembro().obterId());

				Link nome = new Link(membro.obterMembro().obterNome(), action);
				block.add(nome);
				block.add(new Label(": " + membro.obterEmail()));
				table.setNextColSpan(table.getColumns());

				table.addData(block);
			}
		}

		if (incluir) {
			Button incluirButton = new Button("Agregar Grupo", new Action(
					"incluirGrupoMensagem"));
			incluirButton.getAction().add("superiorId",
					grupo.obterSuperior().obterId());
			table.addFooter(incluirButton);
		} else {
			Button atualizarButton = new Button("Actualizar Grupo", new Action(
					"atualizarGrupoMensagem"));
			atualizarButton.getAction().add("id", grupo.obterId());

			table.addFooter(atualizarButton);

			Action excluirAction = new Action("excluirGrupoMensagem");
			excluirAction.setConfirmation("Confirma exclusion ?");
			excluirAction.add("id", grupo.obterId());
			Button excluirButton = new Button("Eliminar Grupo", excluirAction);
			excluirButton.setEnabled(grupo.permiteExcluir());
			table.addFooter(excluirButton);
		}
		return table;
	}
}