package com.gvs.crm.component;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Evento;

import infra.security.User;
import infra.view.BasicView;
import infra.view.InputText;
import infra.view.Label;
import infra.view.View;

public class EventoDescricaoInput extends BasicView {
	private Evento evento;

	private boolean habilitado;

	private String nome;

	public EventoDescricaoInput(String nome, Evento evento, boolean habilitado) {
		this.nome = nome;
		this.evento = evento;
		this.habilitado = habilitado;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		if (this.habilitado)
			return new InputText(this.nome, this.evento.obterDescricao(), 5, 80);
		else
			return new Label(this.evento.obterDescricao());
	}
}