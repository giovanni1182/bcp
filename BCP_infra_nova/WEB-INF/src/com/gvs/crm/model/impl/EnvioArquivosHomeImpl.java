package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EnvioArquivos;
import com.gvs.crm.model.EnvioArquivosHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Usuario;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class EnvioArquivosHomeImpl extends Home implements EnvioArquivosHome
{
	public Collection<EnvioArquivos> obterArquivos(Entidade aseguradora, Date dataInicio, Date dataFim, Usuario usuario) throws Exception
	{
		Collection<EnvioArquivos> envios = new ArrayList<>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "select evento.id from evento,envio_arquivo where evento.id = envio_arquivo.id";
		if(aseguradora!=null)
			sql+=" and origem = "+aseguradora.obterId();
		if(dataInicio!=null)
			sql+=" and criacao>="+dataInicio.getTime();
		if(dataFim!=null)
			sql+=" and criacao<="+dataFim.getTime();
		if(usuario!=null)
			sql+=" and responsavel = " + usuario.obterId();
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		long id;
		EnvioArquivos envio;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			
			envio = (EnvioArquivos) home.obterEventoPorId(id);
			
			envios.add(envio);
		}
		
		return envios;
	}

}
