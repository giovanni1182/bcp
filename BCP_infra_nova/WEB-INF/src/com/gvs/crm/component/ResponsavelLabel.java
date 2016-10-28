package com.gvs.crm.component;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Usuario;

import infra.control.Action;
import infra.view.Block;
import infra.view.Image;
import infra.view.Link;
import infra.view.Space;

public class ResponsavelLabel extends Block {
	public ResponsavelLabel(Entidade entidade) throws Exception {
		super(Block.HORIZONTAL);
		Usuario responsavel = entidade.obterResponsavel();
		if (responsavel != null) {
			Link responsavelLink = new Link(responsavel.obterNome(),
					new Action("visualizarDetalhesEntidade"));
			responsavelLink.getAction().add("id", responsavel.obterId());
			this.add(responsavelLink);
			if (!(entidade instanceof Usuario)) {
				if (entidade.permiteAtualizarResponsavel()) {
					this.add(new Space());
					Action action = new Action("selecionarResponsavelEntidade");
					action.add("id", entidade.obterId());
					this.add(new Link(new Image("replace.gif"), action));
				}
			} else {
				if (entidade.permiteAtualizar()) {
					this.add(new Space());
					Action action = new Action("selecionarResponsavelEntidade");
					action.add("id", entidade.obterId());
					this.add(new Link(new Image("replace.gif"), action));
				}
			}
		}
	}
}