package com.gvs.crm.component;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class MesAnoBalancoSelect extends BasicView {

	private String nome;

	private String valor;

	private Aseguradora aseguradora;

	public MesAnoBalancoSelect(String nome, Aseguradora aseguradora,
			String valor) throws Exception {
		this.nome = nome;
		this.aseguradora = aseguradora;
		this.valor = valor;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Select select = new Select(this.nome, 1);
		//select.add("", "", false);

		Map datas = new TreeMap();

		for (Iterator i = eventoHome.obterMesAnoBalanco(this.aseguradora)
				.iterator(); i.hasNext();) {
			String mesAno = (String) i.next();

			select.add(mesAno.substring(0, 2) + " - "
					+ mesAno.substring(2, mesAno.length()), mesAno, mesAno
					.equals(valor));
		}

		return select;
	}
}