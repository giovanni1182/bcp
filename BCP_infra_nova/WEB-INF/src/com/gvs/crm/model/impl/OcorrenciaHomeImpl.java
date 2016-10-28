package com.gvs.crm.model.impl;

import java.util.Collection;

import com.gvs.crm.model.OcorrenciaHome;
import com.gvs.crm.model.Produto;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class OcorrenciaHomeImpl extends Home implements OcorrenciaHome {
	public Collection obterOcorrenciasPorProduto(Produto produto)
			throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id, evento.classe from evento,ocorrencia where evento.id=ocorrencia.id and ocorrencia.produto=? order by evento.prioridade,evento.criacao");
		query.addLong(produto.obterId());
		SQLRow[] rows = query.execute();
		EventoHomeImpl eventoHome = (EventoHomeImpl) this.getModelManager()
				.getHome("EventoHome");
		return eventoHome._instanciarEventos(rows);
	}

	public boolean possuiOcorrencias(Produto produto) throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select count(*) quantidade from ocorrencia where produto=?");
		query.addLong(produto.obterId());
		int quantidade = query.executeAndGetFirstRow().getInt("quantidade");
		return quantidade > 0;
	}
}