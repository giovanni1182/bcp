package com.gvs.crm.component;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.PlanoHome;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class RamosSelect extends BasicView {

	private String nome;

	private String ramo;

	public RamosSelect(String nome, String ramo) throws Exception {
		this.nome = nome;
		this.ramo = ramo;
	}

	public View execute(User user, Locale arg1, Properties arg2)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		PlanoHome planoHome = (PlanoHome) mm.getHome("PlanoHome");
		Select select = new Select(this.nome, 1);
		select.add("[Todos]", "", false);

		for (Iterator i = planoHome.obterNomesRamo().iterator(); i.hasNext();) {
			String ramo = (String) i.next();

			select.add(ramo, ramo, ramo.equals(this.ramo));
		}

		return select;
	}
}