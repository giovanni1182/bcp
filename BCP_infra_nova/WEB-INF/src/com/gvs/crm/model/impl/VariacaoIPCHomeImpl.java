package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.VariacaoIPC;
import com.gvs.crm.model.VariacaoIPCHome;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class VariacaoIPCHomeImpl extends Home implements VariacaoIPCHome
{
	public Collection<VariacaoIPC> obter10Ultimas() throws Exception
	{
		Collection<VariacaoIPC> variacoes = new ArrayList<VariacaoIPC>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select TOP 10 evento.id from evento,variacao_ipc where evento.id = variacao_ipc.id order by data_prevista_inicio DESC");
		
		SQLRow[] rows = query.execute();
		
		long id;
		VariacaoIPC variacao;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			variacao = (VariacaoIPC) home.obterEventoPorId(id);
			
			variacoes.add(variacao);
		}
		
		return variacoes;
	}
	
	public Collection<VariacaoIPC> obterVariacoes(int mes, int ano) throws Exception
	{
		Collection<VariacaoIPC> variacoes = new ArrayList<VariacaoIPC>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "select evento.id from evento,variacao_ipc where evento.id = variacao_ipc.id";
		if(mes > 0)
			sql+=" and mes = " + mes;
		if(ano>0)
			sql+=" and ano = " + ano;
		
		sql+=" order by data_prevista_inicio";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		long id;
		VariacaoIPC variacao;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			variacao = (VariacaoIPC) home.obterEventoPorId(id);
			
			variacoes.add(variacao);
		}
		
		return variacoes;
	}
	
	public VariacaoIPC obterVariacao(int mes, int ano) throws Exception
	{
		VariacaoIPC variacao = null;
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "select TOP 1 evento.id from evento,variacao_ipc where evento.id = variacao_ipc.id and mes = " + mes+ " and ano = " + ano;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		long id;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			variacao = (VariacaoIPC) home.obterEventoPorId(id);
		}
		
		return variacao;
	}
	
	public boolean temVariacao(int mes, int ano, VariacaoIPC variacao) throws Exception
	{
		String sql = "select count(*) as qtde from evento,variacao_ipc where evento.id = variacao_ipc.id and mes = " + mes+ " and ano = " + ano;
		if(variacao!=null)
			sql+=" and evento.id<>"+variacao.obterId();
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		return query.executeAndGetFirstRow().getInt("qtde") == 0;
	}
}
