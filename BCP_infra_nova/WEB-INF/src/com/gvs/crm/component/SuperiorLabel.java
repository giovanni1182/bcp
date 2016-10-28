package com.gvs.crm.component;

import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.view.Block;
import infra.view.Image;
import infra.view.Link;
import infra.view.Space;

public class SuperiorLabel extends Block {
	public SuperiorLabel(Entidade entidade) throws Exception {
		super(Block.HORIZONTAL);
		Entidade superior = entidade.obterSuperior();
		Link superiorLink = new Link(superior.obterNome(), new Action(
				"visualizarDetalhesEntidade"));
		superiorLink.getAction().add("id", superior.obterId());
		this.add(superiorLink);
		if (entidade.permiteAtualizarSuperior()) {
			this.add(new Space());
			Action action = new Action("selecionarSuperiorEntidade");
			action.add("id", entidade.obterId());
			this.add(new Link(new Image("replace.gif"), action));
		}
	}
}