package com.gvs.crm.component;

import com.gvs.crm.model.Evento;

import infra.view.Block;
import infra.view.Image;
import infra.view.Label;

public class EventoComentariosLabel extends Block {
	private Label label = new Label("");

	public EventoComentariosLabel(Evento evento) throws Exception {
		super(Block.HORIZONTAL);
		int i = evento.obterQuantidadeComentarios();
		if (i > 0) {
			this.add(new Image("note.gif"));
			if (i > 1) {
				this.label.setValue("(" + i + ")");
				this.add(label);
			}
		}
	}

	public void setBold(boolean bold) {
		this.label.setBold(bold);
	}
}