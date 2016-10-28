package com.gvs.crm.component;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class EstoqueSelect extends BasicView {
	private String nome;

	private Entidade entidade;

	private long id = 0;

	private boolean habilitado;

	public EstoqueSelect(String nome, Entidade entidade, boolean habilitado) {
		this.nome = nome;
		this.entidade = entidade;
		if (this.entidade != null)
			this.id = this.entidade.obterId();
		this.habilitado = habilitado;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Select select = new Select(nome, 1);
		select.setEnabled(this.habilitado);
		select.add("", "0", false);
		TreeMap map = new TreeMap();
		for (Iterator i = entidadeHome.obterEntidades().iterator(); i.hasNext();) {
			Entidade e = (Entidade) i.next();
			if (e.obterClasse().equals("estoque"))
				map.put(e.obterNome() + e.obterId(), e);
		}
		for (Iterator i = map.values().iterator(); i.hasNext();) {
			Entidade e = (Entidade) i.next();
			select.add(e.obterNome() + " (" + e.obterEmpresa().obterNome()
					+ ")", String.valueOf(e.obterId()), id == e.obterId());
		}
		return select;
	}
}