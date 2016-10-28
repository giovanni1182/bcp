package com.gvs.crm.component;

import com.gvs.crm.model.Evento;

import infra.control.Action;
import infra.view.Block;
import infra.view.Link;
import infra.view.Space;

public class EventoLink extends Block {
	public EventoLink(Evento evento) throws Exception {
		super(Block.HORIZONTAL);
		this.add(new EventoImage(evento));
		this.add(new Space());
		//Button superiorButton = new Button(evento.obterTitulo(), new
		// Action("visualizarEvento"));
		Link superiorLink = new Link(evento.obterTitulo(), new Action(
				"visualizarEvento"));
		superiorLink.getAction().add("id", evento.obterId());
		this.add(superiorLink);
	}
}