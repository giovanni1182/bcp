package com.gvs.crm.view;

import java.util.Iterator;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Parametro;

import infra.control.Action;
import infra.view.Block;
import infra.view.Image;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;

public class FeriadosView extends Table {
	public FeriadosView(Entidade entidade) throws Exception {
		super(3);

		Parametro parametro = (Parametro) entidade;

		for (Iterator i = parametro.obterFeriados().iterator(); i.hasNext();) {
			Block block = new Block(Block.HORIZONTAL);

			Parametro.Feriado feriado = (Parametro.Feriado) i.next();

			if (parametro.permiteAtualizar()) {
				Action visualizarAction = new Action("visualizarFeriado");
				visualizarAction.add("entidadeId", parametro.obterId());
				visualizarAction.add("id", feriado.obterId());
				visualizarAction.add("origemMenuId", parametro.obterId());
				block.add(new Link(new Image("replace.gif"), visualizarAction));

				Action excluirAction = new Action("excluirFeriado");
				excluirAction.add("entidadeId", parametro.obterId());
				excluirAction.add("id", feriado.obterId());
				excluirAction.setConfirmation("Confirma exclusión ?");
				block.add(new Link(new Image("delete.gif"), excluirAction));
			}
			this.addData(block);
			this.addHeader(feriado.obterDescricaoFeriado() + " : ");
			this.addHeader(new Label(feriado.obterDataFeriado()));
		}
	}
}