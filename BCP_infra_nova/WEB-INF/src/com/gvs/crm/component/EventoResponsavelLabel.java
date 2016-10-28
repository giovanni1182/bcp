package com.gvs.crm.component;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;

import infra.control.Action;
import infra.view.Block;
import infra.view.Link;

public class EventoResponsavelLabel extends Block {
	public EventoResponsavelLabel(Evento evento) throws Exception {
		super(Block.HORIZONTAL);

		Entidade responsavel = evento.obterResponsavel();

		Link link = new Link(responsavel.obterNome(), new Action(
				"visualizarDetalhesEntidade"));
		link.getAction().add("id", responsavel.obterId());

		this.add(link);
	}
}