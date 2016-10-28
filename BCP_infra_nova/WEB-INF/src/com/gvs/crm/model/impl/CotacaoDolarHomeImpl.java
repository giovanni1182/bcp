package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.gvs.crm.model.CotacaoDolar;
import com.gvs.crm.model.CotacaoDolarHome;
import com.gvs.crm.model.EventoHome;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class CotacaoDolarHomeImpl extends Home implements CotacaoDolarHome
{
	public Collection<CotacaoDolar> obter10Ultimas() throws Exception
	{
		Collection<CotacaoDolar> cotacoes = new ArrayList<CotacaoDolar>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select TOP 10 evento.id from evento,cotacao_dolar where evento.id = cotacao_dolar.id order by data_prevista_inicio DESC");
		
		SQLRow[] rows = query.execute();
		
		long id;
		CotacaoDolar cotacao;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			cotacao = (CotacaoDolar) home.obterEventoPorId(id);
			
			cotacoes.add(cotacao);
		}
		
		return cotacoes;
	}
	
	public Collection<CotacaoDolar> obterCotacoes(int mes, int ano) throws Exception
	{
		Collection<CotacaoDolar> cotacoes = new ArrayList<CotacaoDolar>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "select evento.id from evento,cotacao_dolar where evento.id = cotacao_dolar.id";
		if(mes > 0)
			sql+=" and mes = " + mes;
		if(ano>0)
			sql+=" and ano = " + ano;
		
		sql+=" order by data_prevista_inicio";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		long id;
		CotacaoDolar cotacao;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			cotacao = (CotacaoDolar) home.obterEventoPorId(id);
			
			cotacoes.add(cotacao);
		}
		
		return cotacoes;
	}
	
	public CotacaoDolar obterCotacao(int mes, int ano) throws Exception
	{
		CotacaoDolar cotacao = null;
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "select TOP 1 evento.id from evento,cotacao_dolar where evento.id = cotacao_dolar.id and mes = " + mes+ " and ano = " + ano;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		long id;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			cotacao = (CotacaoDolar) home.obterEventoPorId(id);
		}
		
		return cotacao;
	}
	
	public boolean temCotacao(int mes, int ano, CotacaoDolar cotacao) throws Exception
	{
		String sql = "select count(*) as qtde from evento,cotacao_dolar where evento.id = cotacao_dolar.id and mes = " + mes+ " and ano = " + ano;
		if(cotacao!=null)
			sql+=" and evento.id<>"+cotacao.obterId();
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		return query.executeAndGetFirstRow().getInt("qtde") == 0;
	}
}
