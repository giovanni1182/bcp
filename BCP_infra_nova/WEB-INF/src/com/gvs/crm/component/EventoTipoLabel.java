package com.gvs.crm.component;

import com.gvs.crm.model.Evento;

import infra.view.Label;

public class EventoTipoLabel extends Label {
	public EventoTipoLabel(Evento evento) throws Exception {
		super("");
		if (evento.obterTipo() == null || evento.obterTipo().equals(""))
			this.setValue(evento.obterClasseDescricao());
		else
			this.setValue(evento.obterTipo());

	}
}