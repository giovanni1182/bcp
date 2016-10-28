package com.gvs.crm.model.impl;

import com.gvs.crm.model.VariacaoIPC;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class VariacaoIPCImpl extends EventoImpl implements VariacaoIPC
{
	private int mes,ano;
	private double variacao;
	
	public void incluir() throws Exception
	{
		super.incluir();
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into variacao_ipc(id,mes,ano,variacao) values(?,?,?,?)");
		insert.addLong(this.obterId());
		insert.addInt(this.mes);
		insert.addInt(this.ano);
		insert.addDouble(this.variacao);
		
		insert.execute();
	}
	
	public void atribuirMes(int mes) throws Exception
	{
		this.mes = mes;
	}
	public void atribuirAno(int ano) throws Exception
	{
		this.ano = ano;
	}

	public void atribuirVariacao(double variacao) throws Exception
	{
		this.variacao = variacao;
	}

	public void atualizarMes(int mes) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update variacao_ipc set mes = ? where id = ?");
		update.addInt(mes);
		update.addLong(this.obterId());
		
		update.execute();
	}

	public void atualizarAno(int ano) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update variacao_ipc set ano = ? where id = ?");
		update.addInt(ano);
		update.addLong(this.obterId());
		
		update.execute();
	}

	public void atualizarVariacao(double variacao) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update variacao_ipc set variacao = ? where id = ?");
		update.addDouble(variacao);
		update.addLong(this.obterId());
		
		update.execute();
	}

	public int obterMes() throws Exception
	{
		if(this.mes == 0)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select mes from variacao_ipc where id = ?");
			query.addLong(this.obterId());
			
			this.mes = query.executeAndGetFirstRow().getInt("mes");
		}
		return this.mes;
	}

	public int obterAno() throws Exception
	{
		if(this.ano == 0)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select ano from variacao_ipc where id = ?");
			query.addLong(this.obterId());
		
			this.ano = query.executeAndGetFirstRow().getInt("ano");
		}
		return ano;
	}

	public double obterVariacao() throws Exception
	{
		if(this.variacao == 0)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select variacao from variacao_ipc where id = ?");
			query.addLong(this.obterId());
			
			this.variacao = query.executeAndGetFirstRow().getDouble("variacao");
		}
		return this.variacao;
	}

}
