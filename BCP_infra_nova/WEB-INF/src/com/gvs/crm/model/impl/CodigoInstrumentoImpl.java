package com.gvs.crm.model.impl;

import com.gvs.crm.model.CodigoInstrumento;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class CodigoInstrumentoImpl extends EntidadeImpl implements CodigoInstrumento 
{
	private int codigo;
	private String descricao;
	
	public void incluir() throws Exception
	{
		super.incluir();
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from codigo_instrumento where codigo = ?");
		query.addInt(codigo);
		
		if(query.executeAndGetFirstRow().getInt("qtde") > 0)
			throw new Exception("Ya se está utilizando el código " + codigo);
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into codigo_instrumento(id,codigo,descricao) values(?,?,?)");
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
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select codigo from codigo_instrumento where id = ?");
			query.addLong(this.obterId());
			
			this.codigo = query.executeAndGetFirstRow().getInt("codigo");
		}
		return this.codigo;
	}

	public String obterDescricao() throws Exception
	{
		if(this.descricao == null)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select descricao from codigo_instrumento where id = ?");
			query.addLong(this.obterId());
			
			this.descricao = query.executeAndGetFirstRow().getString("descricao");
		}
		return this.descricao;
	}

	public void atualizarCodigo(int codigo) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update codigo_instrumento set codigo = ? where id = ?");
		update.addInt(codigo);
		update.addLong(this.obterId());
		
		update.execute();
	}

	public void atualizarDescricao(String descricao) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update codigo_instrumento set descricao = ? where id = ?");
		update.addString(descricao);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public boolean permiteExcluir() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from mf_alerta_temprana where cod_instrumento = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getInt("qtde") == 0;
	}
	
	public void excluir() throws Exception
	{
		SQLUpdate delete = this.getModelManager().createSQLUpdate("crm","delete from codigo_instrumento where id = ?");
		delete.addLong(this.obterId());
		delete.execute();
		
		delete = this.getModelManager().createSQLUpdate("crm","delete from entidade where id = ?");
		delete.addLong(this.obterId());
		delete.execute();
	}
}