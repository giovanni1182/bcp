package com.gvs.crm.model.impl;

import com.gvs.crm.model.ResolucaoScanner;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class ResolucaoScannerImpl extends EventoImpl implements ResolucaoScanner
{
	private int ano;
	public void incluir() throws Exception
	{
		super.incluir();
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into resolucao_scanner(id,ano) values(?,?)");
		insert.addLong(this.obterId());
		insert.addInt(ano);
		
		insert.execute();
	}
	
	public void atribuirAno(int ano)
	{
		this.ano = ano;
	}

	public void atualizarAno(int ano) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm", "update resolucao_scanner set ano = ? where id = ?");
		update.addInt(ano);
		update.addLong(this.obterId());
		
		update.execute();
	}

	public int obterAno() throws Exception
	{
		if(this.ano == 0)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select ano from resolucao_scanner where id = ?");
			query.addLong(this.obterId());
			
			this.ano = query.executeAndGetFirstRow().getInt("ano");
		}
		return this.ano;
	}
}