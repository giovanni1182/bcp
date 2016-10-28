package com.gvs.crm.view;

import java.util.Iterator;

import com.gvs.crm.model.Evento;

import infra.view.Label;
import infra.view.Space;
import infra.view.Table;

public class ComentariosView extends Table {
	public ComentariosView(Evento evento) throws Exception {
		super(2);
		this.setWidth("100%");
		this.addStyle(Table.STYLE_ALTERNATE);
		for (Iterator i = evento.obterComentarios().iterator(); i.hasNext();) {
			Evento.Comentario comentario = (Evento.Comentario) i.next();

			this.setNextWidth("100");
			this.addData(new Label(comentario.obterCriacao(),
					"dd/MM/yyyy HH:mm"));
			Label tituloLabel = new Label(comentario.obterTitulo());
			tituloLabel.setBold(true);
			this.addData(tituloLabel);

			this.addData(new Space(1));
			this.addData(new Label(comentario.obterComentario()));
		}
	}
}