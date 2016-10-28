package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.gvs.crm.model.EnvioArquivos;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class EnvioArquivosImpl extends EventoImpl implements EnvioArquivos
{
	public Collection<String> arquivos;
	
	public void incluir() throws Exception
	{
		super.incluir();
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into envio_arquivo(id) values(?)");
		insert.addLong(this.obterId());
		
		insert.execute();
		
		if(arquivos!=null)
		{
			SQLQuery query;
			int seq;
			
			for(String nomeArquivo : arquivos)
			{
				query = this.getModelManager().createSQLQuery("crm","select max(seq) as mx from envio_arquivos where id = ?");
				query.addLong(this.obterId());
				
				seq = query.executeAndGetFirstRow().getInt("mx") + 1;
				
				insert = this.getModelManager().createSQLUpdate("crm","insert into envio_arquivos(id,seq,nome_arquivo) values(?,?,?)");
				insert.addLong(this.obterId());
				insert.addInt(seq);
				insert.addString(nomeArquivo);
				insert.execute();
			}
		}
	}
	
	public void addArquivo(String nomeArquivo) throws Exception
	{
		if(arquivos == null)
			arquivos = new ArrayList<>();
		
		arquivos.add(nomeArquivo);
	}

	public Collection<String> obterArquivos() throws Exception
	{
		Collection<String> arquivos = new ArrayList<>();
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select nome_arquivo from envio_arquivos where id = ? order by nome_arquivo");
		query.addLong(this.obterId());
		
		SQLRow[] rows = query.execute();
		
		String nomeArquivo;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			nomeArquivo = rows[i].getString("nome_arquivo");
			
			arquivos.add(nomeArquivo);
		}
		
		return arquivos;
	}
}