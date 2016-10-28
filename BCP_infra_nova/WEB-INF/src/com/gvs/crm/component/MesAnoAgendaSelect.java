package com.gvs.crm.component;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class MesAnoAgendaSelect extends BasicView {

	private String nome;

	private String valor;

	public MesAnoAgendaSelect(String nome, String valor) throws Exception {
		this.nome = nome;
		this.valor = valor;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Select select = new Select(this.nome, 1);
		select.add("", "", false);

		Map datas = new TreeMap();

		for (Iterator i = eventoHome.obterMesAnoAgendas().iterator(); i
				.hasNext();) {
			String mesAno = (String) i.next();

			if (mesAno.length() == 5)
				datas.put(new Integer(mesAno.substring(1, mesAno.length())
						+ mesAno.substring(0, 1)), mesAno);
			else
				datas.put(new Integer(mesAno.substring(2, mesAno.length())
						+ mesAno.substring(0, 2)), mesAno);
		}

		for (Iterator i = datas.values().iterator(); i.hasNext();) {
			String mesAno = (String) i.next();

			String mesAnoModificado = "";

			if (mesAno.length() == 5)
				mesAnoModificado = mesAno.substring(0, 1) + " - "
						+ mesAno.substring(1, mesAno.length());
			else
				mesAnoModificado = mesAno.substring(0, 2) + " - "
						+ mesAno.substring(2, mesAno.length());

			select.add(mesAnoModificado, mesAno, mesAno.equals(valor));
		}

		return select;
	}
}