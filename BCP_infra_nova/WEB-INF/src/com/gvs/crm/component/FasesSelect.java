package com.gvs.crm.component;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class FasesSelect extends BasicView {
	private Evento evento;

	private String nome;

	private boolean proximaFase;

	public FasesSelect(String nome, Evento evento, boolean proximaFase) {
		this.nome = nome;
		this.evento = evento;
		this.proximaFase = proximaFase;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Select select = new Select(this.nome, 1);

		boolean carregar = false;

		for (Iterator i = eventoHome.obterFases(this.evento.obterClasse())
				.iterator(); i.hasNext();) {
			String fase = (String) i.next();

			if (this.proximaFase) {
				if (this.evento.obterFase().obterCodigo().equals(fase)
						|| this.evento.obterFase().obterCodigo().equals(
								"pendente"))
					carregar = true;
			} else {
				if (!this.evento.obterFase().obterCodigo().equals(fase))
					carregar = true;
				else
					break;

			}

			if (!this.evento.obterFase().obterCodigo().equals(fase) && carregar)
				select.add(eventoHome.obterNomeFase(fase), fase, false);
		}
		return select;
	}
}