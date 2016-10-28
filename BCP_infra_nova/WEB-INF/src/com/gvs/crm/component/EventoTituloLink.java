package com.gvs.crm.component;

import com.gvs.crm.model.Evento;

import infra.control.Action;
import infra.view.Block;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;

public class EventoTituloLink extends Block {
	public EventoTituloLink(Evento evento) throws Exception {
		super(Block.HORIZONTAL);
		Label label = new Label(evento.obterTitulo());
		label.setBold(!evento.foiLido());
		Link eventoLink = new Link(label, new Action("visualizarEvento"));
		eventoLink.getAction().add("id", evento.obterId());
		this.add(eventoLink);
		this.add(new Space());
		EventoComentariosLabel comentariosLabel = new EventoComentariosLabel(
				evento);
		comentariosLabel.setBold(!evento.foiLido());
		this.add(comentariosLabel);
	}
}