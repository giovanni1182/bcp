package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class SelecaoSuperiorEntidadeView extends PortalView {
	private Entidade entidade;

	public SelecaoSuperiorEntidadeView(Entidade entidade) throws Exception {
		this.entidade = entidade;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);
		table.addSubtitle("Selecionar a nova entidade superior");
		table.addHeader("Superior");
		table.addHeader("Unidade");
		Collection entidades = this.entidade.obterPossiveisSuperiores();
		TreeMap m = new TreeMap();
		for (Iterator i = entidades.iterator(); i.hasNext();) {
			Entidade e = (Entidade) i.next();
			Entidade s = e.obterSuperior();
			if (s == null)
				m.put("" + e.obterId(), e);
			else
				m.put(s.obterNome() + e.obterId(), e);
		}
		for (Iterator i = m.values().iterator(); i.hasNext();) {
			Entidade e = (Entidade) i.next();
			Entidade s = e.obterSuperior();
			if (s == null)
				table.addData("-");
			else
				table.addData(s.obterNome());
			Action atualizarAction = new Action("atualizarSuperiorEntidade");
			atualizarAction.add("id", this.entidade.obterId());
			atualizarAction.add("superiorId", e.obterId());
			atualizarAction.setConfirmation("Confirma alteração do superior ?");
			table.addData(new Link(e.obterNome(), atualizarAction));
		}
		Action cancelarAction = new Action("visualizarDetalhesEntidade");
		cancelarAction.add("id", this.entidade.obterId());
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
		return new Label(this.entidade.obterNome());
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