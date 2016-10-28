package com.gvs.crm.component;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Label;
import infra.view.Select;
import infra.view.View;

public class EventoTipoSelect extends BasicView {
	private Evento evento;

	private boolean habilitado;

	private String nome;

	public EventoTipoSelect(String nome, Evento evento, boolean habilitado) {
		this.nome = nome;
		this.evento = evento;
		this.habilitado = habilitado;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		if (habilitado) {
			CRMModelManager mm = new CRMModelManager(user);
			EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
			Select select = new Select(this.nome, 1);
			select.add("", "", false);
			for (Iterator i = eventoHome.obterTiposEventos(
					this.evento.obterClasse()).iterator(); i.hasNext();) {
				String tipo = (String) i.next();
				select.add(tipo, tipo, tipo.equals(this.evento.obterTipo()));
			}
			return select;
		} else {
			return new Label(this.evento.obterTipo());
		}
	}
}