package com.gvs.crm.model.impl;

import com.gvs.crm.model.CotacaoDolar;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class CotacaoDolarImpl extends EventoImpl implements CotacaoDolar
{
	private int mes,ano;
	private double cotacao;
	
	public void incluir() throws Exception
	{
		super.incluir();
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into cotacao_dolar(id,mes,ano,cotacao) values(?,?,?,?)");
		insert.addLong(this.obterId());
		insert.addInt(this.mes);
		insert.addInt(this.ano);
		insert.addDouble(this.cotacao);
		
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

	public void atribuirCotacao(double cotacao) throws Exception
	{
		this.cotacao = cotacao;
	}

	public void atualizarMes(int mes) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update cotacao_dolar set mes = ? where id = ?");
		update.addInt(mes);
		update.addLong(this.obterId());
		
		update.execute();
	}

	public void atualizarAno(int ano) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update cotacao_dolar set ano = ? where id = ?");
		update.addInt(ano);
		update.addLong(this.obterId());
		
		update.execute();
	}

	public void atualizarCotacao(double cotacao) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update cotacao_dolar set cotacao = ? where id = ?");
		update.addDouble(cotacao);
		update.addLong(this.obterId());
		
		update.execute();
	}

	public int obterMes() throws Exception
	{
		if(this.mes == 0)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select mes from cotacao_dolar where id = ?");
			query.addLong(this.obterId());
			
			this.mes = query.executeAndGetFirstRow().getInt("mes");
		}
		return this.mes;
	}

	public int obterAno() throws Exception
	{
		if(this.ano == 0)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select ano from cotacao_dolar where id = ?");
			query.addLong(this.obterId());
		
			this.ano = query.executeAndGetFirstRow().getInt("ano");
		}
		return ano;
	}

	public double obterCotacao() throws Exception
	{
		if(this.cotacao == 0)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select cotacao from cotacao_dolar where id = ?");
			query.addLong(this.obterId());
			
			this.cotacao = query.executeAndGetFirstRow().getDouble("cotacao");
		}
		return this.cotacao;
	}
}