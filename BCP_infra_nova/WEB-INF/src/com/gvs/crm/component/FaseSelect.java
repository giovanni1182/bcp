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
 * Processo".equals(valor)); this.add("Entrada na Justiça", "Entrada na
 * Justiça", "Entrada na Justiça".equals(valor)); this.add("Conciliação",
 * "Conciliação", "Conciliação".equals(valor)); this.add("Instrução",
 * "Instrução", "Instrução".equals(valor)); this.add("Alegações Finais",
 * "Alegações Finais", "Alegações Finais".equals(valor)); this.add("Sentença",
 * "Sentença", "Sentença".equals(valor)); this.add("Recurso Sentença", "Recurso
 * Sentença", "Recurso Sentença".equals(valor)); this.add("Tribunal Acórdão",
 * "Tribunal Acórdão", "Tribunal Acórdão".equals(valor)); this.add("Recurso
 * Tribunal", "Recurso Tribunal", "Recurso Tribunal".equals(valor));
 * this.add("Brasília Acórdão", "Brasília Acórdão", "Brasília
 * Acórdão".equals(valor)); this.add("Recurso Brasília", "Recurso Brasília",
 * "Recurso Brasília".equals(valor)); this.add("STF", "STF",
 * "STF".equals(valor)); this.add("Remessa", "Remessa",
 * "Remessa".equals(valor)); this.add("Execução Concluso", "Execução Concluso",
 * "Execução Concluso".equals(valor));
 */

