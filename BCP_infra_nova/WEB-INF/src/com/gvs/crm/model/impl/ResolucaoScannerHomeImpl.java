package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.ResolucaoScanner;
import com.gvs.crm.model.ResolucaoScannerHome;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class ResolucaoScannerHomeImpl extends Home implements ResolucaoScannerHome
{
	public Collection<ResolucaoScanner> obterResolucoes(Aseguradora aseguradora, String titulo, String numero, int ano) throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		Collection<ResolucaoScanner> resolucoes = new ArrayList<ResolucaoScanner>();
		
		String sql = "select evento.id from evento, resolucao_scanner where evento.id = resolucao_scanner.id";
		if(aseguradora!=null)
			sql+=" and origem = " + aseguradora.obterId();
		if(!titulo.equals(""))
			sql+=" and titulo like '%" + titulo + "%'";
		if(!numero.equals(""))
			sql+=" and tipo like '%" + numero + "%'";
		if(ano > 0)
			sql+=" and ano = " + ano;
		
		sql+=" order by ano";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			ResolucaoScanner resolucao = (ResolucaoScanner) home.obterEventoPorId(id);
			
			resolucoes.add(resolucao);
		}
		
		return resolucoes;
	}
}