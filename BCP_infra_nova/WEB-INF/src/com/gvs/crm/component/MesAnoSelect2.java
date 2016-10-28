package com.gvs.crm.component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class MesAnoSelect2 extends BasicView {

	private Entidade entidade;

	private String nome;

	private String mesAno;

	public MesAnoSelect2(String nome, Entidade entidade, String mesAno)
			throws Exception {
		this.nome = nome;
		this.entidade = entidade;
		this.mesAno = mesAno;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {

		Select select = new Select(this.nome, 1);

		for (Iterator i = this.entidade.obterDatasMovimentacao().iterator(); i
				.hasNext();) {
			Date data = (Date) i.next();

			String mes = new SimpleDateFormat("MM").format(data);
			String ano = new SimpleDateFormat("yyyy").format(data);

			String mesAnoEvento = mes + " - " + ano;

			select.add(mesAnoEvento, mesAnoEvento, mesAnoEvento
					.equals(this.mesAno));

		}
		return select;
	}
}