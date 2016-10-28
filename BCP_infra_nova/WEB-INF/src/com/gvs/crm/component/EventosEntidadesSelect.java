package com.gvs.crm.component;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class EventosEntidadesSelect extends BasicView {
	private String nome;

	private String classe;

	public EventosEntidadesSelect(String nome, String classe) {
		this.nome = nome;
		this.classe = classe;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Select select = new Select(this.nome, 1);
		select.add("", "", false);
		for (Iterator i = eventoHome.obterClassesDescricao(null).iterator(); i
				.hasNext();) {
			String tipo = (String) i.next();
			select.add(tipo, tipo, tipo.equals(classe));
		}

		for (Iterator i = entidadeHome.obterTiposEntidade().values().iterator(); i
				.hasNext();) {
			String tipo = (String) i.next();
			select.add(tipo, tipo, tipo.equals(classe));
		}

		return select;
	}
}