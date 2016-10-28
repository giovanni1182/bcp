package com.gvs.crm.model.impl;

import com.gvs.crm.model.CodificacaoCobertura;
import com.gvs.crm.model.CodificacaoPlano;
import com.gvs.crm.model.EventoHome;

import infra.sql.SQLQuery;

public class CodificacaoPlanoImpl extends CodificacoesImpl implements CodificacaoPlano 
{

	public CodificacaoCobertura obterCobertura(String codigo) throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoCobertura' and superior = ? and codigo = ?");
		query.addLong(this.obterId());
		query.addString(codigo);
		
		CodificacaoCobertura cobertura = null;
		
		if(query.executeAndGetFirstRow().getLong("id") > 0)
		{
			long id = query.executeAndGetFirstRow().getLong("id");
			cobertura = (CodificacaoCobertura) home.obterEventoPorId(id);
		}
		
		return cobertura;
	}
}
