package com.gvs.crm.model.impl;

import com.gvs.crm.model.Qualificadora;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class QualificadoraImpl extends EntidadeImpl implements Qualificadora
{
	private int codigo;
	private String descricao;
	
	public void incluir() throws Exception
	{
		super.incluir();
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into qualificadora(id,codigo,descricao) values(?,?,?)");
		insert.addLong(this.obterId());
		insert.addInt(this.codigo);
		insert.addString(this.descricao);
		
		insert.execute();
	}
	public void atribuirCodigo(int codigo) 
	{
		this.codigo = codigo;
	}

	public void atribuirDescricao(String descricao)
	{
		this.descricao = descricao;
	}

	public int obterCodigo() throws Exception 
	{
		if(this.codigo == 0)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select descricao from qualificadora where id = ?");
			query.addLong(this.obterId());
			
			this.codigo = query.executeAndGetFirstRow().getInt("codigo");
		}
		return this.codigo;
	}

	public String obterDescricao() throws Exception
	{
		if(this.descricao == null)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select descricao from qualificadora where id = ?");
			query.addLong(this.obterId());
			
			this.descricao = query.executeAndGetFirstRow().getString("descricao");
		}
		return this.descricao;
	}

	public void atualizarCodigo(int codigo) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update qualificadora set codigo = ? where id = ?");
		update.addInt(codigo);
		update.addLong(this.obterId());
		
		update.execute();
	}

	public void atualizarDescricao(String descricao) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update qualificadora set descricao = ? where id = ?");
		update.addString(descricao);
		update.addLong(this.obterId());
		
		update.execute();
	}
}
