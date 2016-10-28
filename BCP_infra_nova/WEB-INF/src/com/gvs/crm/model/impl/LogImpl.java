package com.gvs.crm.model.impl;

import com.gvs.crm.model.Log;

import infra.sql.SQLUpdate;

public class LogImpl extends EventoImpl implements Log
{
	public void incluir() throws Exception
	{
		super.incluir();
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("insert into log (id) values(?)");
		insert.addLong(this.obterId());
		insert.execute();
	}
}