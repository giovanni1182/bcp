package com.gvs.crm.model.impl;

import com.gvs.crm.model.Livro;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class LivroImpl extends EventoImpl implements Livro
{
	private int mes,ano;
	
	public void incluir() throws Exception
	{
		super.incluir();
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into livro(id,mes,ano) values(?,?,?)");
		insert.addLong(this.obterId());
		insert.addInt(this.mes);
		insert.addInt(this.ano);
		
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

	public int obterMes() throws Exception
	{
		if(this.mes == 0)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select mes from livro where id = ?");
			query.addLong(this.obterId());
			
			this.mes = query.executeAndGetFirstRow().getInt("mes");
		}
		return this.mes;
	}

	public int obterAno() throws Exception
	{
		if(this.ano == 0)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select ano from livro where id = ?");
			query.addLong(this.obterId());
			
			this.ano = query.executeAndGetFirstRow().getInt("ano");
		}
		return this.ano;
	}

	public void atualizarMes(int mes) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update livro set mes = ? where id = ?");
		update.addInt(mes);
		update.addLong(this.obterId());
		
		update.execute();
		
		this.mes = mes;
	}

	public void atualizarAno(int ano) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update livro set ano = ? where id = ?");
		update.addInt(ano);
		update.addLong(this.obterId());
		
		update.execute();
		
		this.ano = ano;
	}
}