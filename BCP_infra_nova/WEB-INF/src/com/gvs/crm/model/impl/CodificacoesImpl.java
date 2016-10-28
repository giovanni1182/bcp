package com.gvs.crm.model.impl;

import com.gvs.crm.model.Codificacoes;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class CodificacoesImpl extends EventoImpl implements Codificacoes
{
	public void incluir() throws Exception
	{
		super.incluir();
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into codificacoes(id) values(?)");
		insert.addLong(this.obterId());
		
		insert.execute();
	}
	public void atualizarCodigo(String codigo) throws Exception 
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update codificacoes set codigo = ? where id = ?");
		update.addString(codigo);
		update.addLong(this.obterId());
		
		update.execute();
	}

	public String obterCodigo() throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select codigo from codificacoes where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("codigo");
	}
}
