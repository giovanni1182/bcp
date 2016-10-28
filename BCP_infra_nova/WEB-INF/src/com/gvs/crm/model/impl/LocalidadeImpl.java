package com.gvs.crm.model.impl;

import com.gvs.crm.model.Localidade;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class LocalidadeImpl extends EntidadeImpl implements Localidade
{
	private int codigo;
	private String cidade,estado;
	
	public void incluir() throws Exception
	{
		super.incluir();
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into localidade(id,codigo,cidade,estado) values(?,?,?,?)");
		insert.addLong(this.obterId());
		insert.addInt(this.codigo);
		insert.addString(this.cidade);
		insert.addString(this.estado);
		
		insert.execute();
	}
	
	public void atribuirCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	public void atribuirCidade(String cidade)
	{
		this.cidade = cidade;
	}

	public void atribuirEstado(String estado)
	{
		this.estado = estado;
	}

	public int obterCodigo() throws Exception
	{
		if(this.codigo == 0)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select descricao from localidade where id = ?");
			query.addLong(this.obterId());
			
			this.codigo = query.executeAndGetFirstRow().getInt("codigo");
		}
		return this.codigo;
	}

	public String obterCidade() throws Exception
	{
		if(this.cidade == null)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select cidade from localidade where id = ?");
			query.addLong(this.obterId());
			
			this.cidade = query.executeAndGetFirstRow().getString("cidade");
		}
		return this.cidade;
	}

	public String obterEstador() throws Exception
	{
		if(this.estado == null)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select estado from localidade where id = ?");
			query.addLong(this.obterId());
			
			this.estado = query.executeAndGetFirstRow().getString("estado");
		}
		return this.estado;
	}

	public void atualizarCodigo(int codigo) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update localidade set codigo = ? where id = ?");
		update.addInt(codigo);
		update.addLong(this.obterId());
		
		update.execute();
	}

	public void atualizarCidade(String cidade) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update localidade set cidade = ? where id = ?");
		update.addString(cidade);
		update.addLong(this.obterId());
		
		update.execute();
	}

	public void atualizarEstado(String estado) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update localidade set estado = ? where id = ?");
		update.addString(estado);
		update.addLong(this.obterId());
		
		update.execute();
	}
}