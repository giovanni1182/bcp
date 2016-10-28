package com.gvs.crm.component;

import java.util.Locale;
import java.util.Properties;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class DiaSelect extends BasicView {
	private String nome;

	private String dia;

	public DiaSelect(String nome, String dia) {
		this.nome = nome;
		this.dia = dia;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		String[] dias = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
				"11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
				"21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
				"31", };

		Select select = new Select(nome, 1);

		for (int i = 0; i < dias.length; i++)
			select.add(dias[i], dias[i], dias[i].equals(dia));

		return select;
	}
}