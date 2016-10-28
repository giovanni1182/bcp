package com.gvs.crm.model.impl;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.AgendaMeicos;
import com.gvs.crm.model.AgendaMeicosHome;
import com.gvs.crm.model.EventoHome;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class AgendaMeicosHomeImpl extends Home implements AgendaMeicosHome {
	public Collection obterAgendas() throws Exception {
		Map agendas = new TreeMap();

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select id from evento where classe='AgendaMeicos'");

		SQLRow[] rows = query.execute();

		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");

			AgendaMeicos agenda = (AgendaMeicos) home.obterEventoPorId(id);

			agendas.put(new Long(agenda.obterDataPrevistaInicio().getTime()
					+ agenda.obterId()), agenda);
		}

		return agendas.values();
	}
}