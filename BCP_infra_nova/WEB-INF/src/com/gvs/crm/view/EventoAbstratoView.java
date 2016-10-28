package com.gvs.crm.view;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;

import infra.view.BasicView;

public abstract class EventoAbstratoView extends BasicView {
	private Evento evento;

	private Entidade origemMenu;

	public void atribuirEvento(Evento evento) {
		this.evento = evento;
	}

	public void atribuirEvento(Evento evento, Entidade origemMenu) {
		this.evento = evento;
		this.origemMenu = origemMenu;
	}

	public Evento obterEvento() {
		return this.evento;
	}

	public Entidade obterOrigemMenu() {
		return this.origemMenu;
	}
}