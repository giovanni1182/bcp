package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class SelecaoSuperiorEventoView extends PortalView {
	private Evento evento;

	public SelecaoSuperiorEventoView(Evento evento) throws Exception {
		this.evento = evento;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);
		table.addSubtitle("Selecionar o novo evento superior");
		table.addHeader("Título");
		table.addHeader("Tipo");
		Link removerSuperiorLink = new Link("nenhum", new Action(
				"atualizarSuperiorEvento"));
		removerSuperiorLink.getAction().add("id", this.evento.obterId());
		removerSuperiorLink.getAction().add("superiorId", 0);
		removerSuperiorLink.getAction().setConfirmation(
				"Confirma alteração do superior ?");
		table.addData(removerSuperiorLink);
		table.addData("");
		Collection entidades = this.evento.obterPossiveisSuperiores();
		TreeMap m = new TreeMap();
		for (Iterator i = entidades.iterator(); i.hasNext();) {
			Evento e = (Evento) i.next();
			m.put(e.obterTitulo() + e.obterId(), e);
		}
		for (Iterator i = m.values().iterator(); i.hasNext();) {
			Evento e = (Evento) i.next();
			Link atualizarLink = new Link(e.obterTitulo(), new Action(
					"atualizarSuperiorEvento"));
			atualizarLink.getAction().add("id", this.evento.obterId());
			atualizarLink.getAction().add("superiorId", e.obterId());
			atualizarLink.getAction().setConfirmation(
					"Confirma alteração do superior ?");
			table.addData(atualizarLink);
			table.addData(e.obterClasseDescricao());
		}
		Action cancelarAction = new Action("visualizarEvento");
		cancelarAction.add("id", this.evento.obterId());
		table.addFooter(new Button("Cancelar", cancelarAction));
		return table;
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception {
		return new Label(this.evento.obterTitulo());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gvs.crm.view.PortalView#getOrigemMenu()
	 */
	public Entidade getOrigemMenu() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}