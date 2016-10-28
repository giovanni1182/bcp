package com.gvs.crm.component;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EventoHome;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class TipoEntidadeInscricaoSelect extends BasicView {
	private String nome;

	private String valor;

	public TipoEntidadeInscricaoSelect(String nome, String valor)
			throws Exception {
		this.nome = nome;
		this.valor = valor;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Select select = new Select(nome, 1);
		select.add("Entidad - [Todas]", "", false);
		select.add("Agentes de Seguros", "Agentes de Seguros",
				"Agentes de Seguros".equals(valor));
		select
				.add("Aseguradoras", "Aseguradoras", "Aseguradoras"
						.equals(valor));
		select.add("Auditor Externo", "Auditor Externo", "Auditor Externo"
				.equals(valor));
		select.add("Corredora de Reaseguro", "Corredora de Reaseguro",
				"Corredora de Reaseguro".equals(valor));
		select.add("Corredores de Seguros", "Corredores de Seguros",
				"Corredores de Seguros".equals(valor));
		select.add("Liquidadores de Siniestros", "Liquidadores de Siniestros",
				"Liquidadores de Siniestros".equals(valor));
		select.add("Reaseguradora", "Reaseguradora", "Reaseguradora"
				.equals(valor));

		/*
		 * for (Iterator i =
		 * eventoHome.obterTiposEntidadeInscricao().iterator(); i.hasNext();) {
		 * String tipo = (String) i.next();
		 * 
		 * select.add(tipo, tipo, tipo.equals(this.valor)); }
		 */

		return select;
	}
}