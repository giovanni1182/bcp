package com.gvs.crm.component;

import com.gvs.crm.model.Evento;

import infra.control.Action;
import infra.view.Block;
import infra.view.Image;
import infra.view.InputLong;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;

public class EventoPopup extends Block {
	public EventoPopup(String eventoId, String eventoTitulo, Evento evento,
			boolean habilitado) throws Exception {
		this(eventoId, eventoTitulo, evento, null, habilitado);
	}

	public EventoPopup(String eventoId, String eventoTitulo, Evento evento,
			String tipo, boolean habilitado) throws Exception {
		super(Block.HORIZONTAL);
		long id = 0;
		if (evento != null)
			id = evento.obterId();
		String titulo = "";
		if (evento != null && evento.obterId() > 0)
			titulo = evento.obterTitulo();
		if (habilitado) {
			this.add(new InputLong(eventoId, id, 8));
			this.add(new Space(2));
			this.add(new InputString(eventoTitulo, titulo, 50));
			this.add(new Space(2));
			Action popupEntidadesAction = new Action(Action.POPUP,
					"popupEventos");
			popupEntidadesAction.add("campoEventoId", eventoId);
			popupEntidadesAction.add("campoEventoTitulo", eventoTitulo);
			popupEntidadesAction.add("tipo", tipo);
			this.add(new Link(new Image("calendar.gif"), popupEntidadesAction));
			this.add(new Space(2));

			if (evento != null && evento.obterId() > 0) {
				Link link = new Link("<<< " + evento.obterClasseDescricao(),
						new Action("visualizarEvento"));
				link.getAction().add("id", evento.obterId());
				this.add(link);
			}
		} else {
			if (evento != null && evento.obterId() > 0) {
				Link link = new Link(evento.obterTitulo(), new Action(
						"visualizarEvento"));
				link.getAction().add("id", evento.obterId());

				this.add(link);
			} else
				this.add(new Label(""));
		}
	}
}