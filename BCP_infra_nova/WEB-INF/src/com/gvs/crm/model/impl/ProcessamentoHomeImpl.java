package com.gvs.crm.model.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Processamento;
import com.gvs.crm.model.Processamento.Agenda;
import com.gvs.crm.model.ProcessamentoHome;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class ProcessamentoHomeImpl extends Home implements ProcessamentoHome
{
	public Processamento obterProcessamentoDoDia(String tipo) throws Exception
	{
		Processamento processamento = null;
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String dataAtual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,processamento where evento.id = processamento.id and data = ? and tipo = ?");
		query.addString(dataAtual);
		query.addString(tipo);
		
		long id = query.executeAndGetFirstRow().getLong("id");
		
		if(id > 0)
			processamento = (Processamento) home.obterEventoPorId(id);
		
		return processamento;
	}
	
	public Collection<Agenda> obterAgendas(Entidade aseguradora, int mes, int ano, String tipo, int erro, Date dataInicio, Date dataFim) throws Exception
	{
		Collection<Agenda> agendas = new ArrayList<Agenda>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "select evento.id,seq from evento,processamento,processamento_agenda where evento.id = processamento.id and processamento.id = processamento_agenda.id";
		//String sql = "select evento.id from evento,processamento where evento.id = processamento.id";
		if(aseguradora!=null)
			sql+=" and id_aseguradora = " + aseguradora.obterId();
		if(mes > 0)
			sql+=" and mes = " + mes;
		if(ano > 0)
			sql+=" and ano = " + ano;
		if(!tipo.equals(""))
			sql+=" and tipo = '" + tipo + "'";
		if(erro !=-1)
			sql+=" and erro = " + erro;
		if(dataInicio!=null)
			sql+=" and data_prevista_inicio>= " + dataInicio.getTime();
		if(dataFim!=null)
			sql+=" and data_prevista_inicio<= " + dataFim.getTime();
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			int seq = rows[i].getInt("seq");
			
			Processamento p = (Processamento) home.obterEventoPorId(id);
			
			agendas.add(p.instanciarAgenda(seq));
		}
		
		return agendas;
	}
}