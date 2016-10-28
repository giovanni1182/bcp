package com.gvs.crm.model.impl;

import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.AgendaMovimentacaoHome;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EventoHome;

import infra.model.Home;
import infra.sql.SQLQuery;

public class AgendaMovimentacaoHomeImpl extends Home implements AgendaMovimentacaoHome
{
	public AgendaMovimentacao obterAgendaNoPeriodo(int mes, int ano, Entidade asseguradora, String tipo) throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		AgendaMovimentacao agenda = null;
		
		//SQLQuery query = this.getModelManager().createSQLQuery("crm", "select evento.id from agenda_movimentacao,evento where evento.id=agenda_movimentacao.id and origem=? and movimento_mes=? and movimento_ano=? and tipo=? and validacao=?");
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select evento.id from agenda_movimentacao,evento where evento.id=agenda_movimentacao.id and origem=? and movimento_mes=? and movimento_ano=? and tipo=?");
		query.addLong(asseguradora.obterId());
		query.addInt(mes);
		query.addInt(ano);
		query.addString(tipo);
		//query.addString("Total");

		long id = query.executeAndGetFirstRow().getLong("id");
		
		if(id > 0)
			agenda = (AgendaMovimentacao) home.obterEventoPorId(id);
			
		return agenda;
	}
}