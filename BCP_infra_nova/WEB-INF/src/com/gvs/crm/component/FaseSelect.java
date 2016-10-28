package com.gvs.crm.component;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EventoHome;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class FaseSelect extends BasicView {

	private String nome;

	private String valor;

	public FaseSelect(String nome, String valor) throws Exception {
		this.nome = nome;
		this.valor = valor;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Select select = new Select(this.nome, 1);

		select.add("[Todas]", "", "".equals(valor));

		for (Iterator i = eventoHome.obterFases("plano").iterator(); i
				.hasNext();) {
			String fase = (String) i.next();

			String nome = eventoHome.obterNomeFase(fase);

			select.add(nome, nome, nome.equals(valor));
		}

		return select;
	}
}

/*
 * this.add("Montagem Processo", "Montagem Processo", "Montagem
 * Processo".equals(valor)); this.add("Entrada na Justi�a", "Entrada na
 * Justi�a", "Entrada na Justi�a".equals(valor)); this.add("Concilia��o",
 * "Concilia��o", "Concilia��o".equals(valor)); this.add("Instru��o",
 * "Instru��o", "Instru��o".equals(valor)); this.add("Alega��es Finais",
 * "Alega��es Finais", "Alega��es Finais".equals(valor)); this.add("Senten�a",
 * "Senten�a", "Senten�a".equals(valor)); this.add("Recurso Senten�a", "Recurso
 * Senten�a", "Recurso Senten�a".equals(valor)); this.add("Tribunal Ac�rd�o",
 * "Tribunal Ac�rd�o", "Tribunal Ac�rd�o".equals(valor)); this.add("Recurso
 * Tribunal", "Recurso Tribunal", "Recurso Tribunal".equals(valor));
 * this.add("Bras�lia Ac�rd�o", "Bras�lia Ac�rd�o", "Bras�lia
 * Ac�rd�o".equals(valor)); this.add("Recurso Bras�lia", "Recurso Bras�lia",
 * "Recurso Bras�lia".equals(valor)); this.add("STF", "STF",
 * "STF".equals(valor)); this.add("Remessa", "Remessa",
 * "Remessa".equals(valor)); this.add("Execu��o Concluso", "Execu��o Concluso",
 * "Execu��o Concluso".equals(valor));
 */

