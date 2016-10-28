package com.gvs.crm.model.impl;

import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Localidade;
import com.gvs.crm.model.LocalidadeHome;

import infra.model.Home;
import infra.sql.SQLQuery;

public class LocalidadeHomeImpl extends Home implements LocalidadeHome
{
	public Localidade obterLocalidade(int codigo) throws Exception
	{
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		Localidade localidade = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select entidade.id from entidade,localidade where entidade.id = localidade.id and codigo = ?");
		query.addInt(codigo);
		
		long id = query.executeAndGetFirstRow().getLong("id");
		
		if(id > 0)
			localidade = (Localidade) home.obterEntidadePorId(id);
		
		return localidade;
	}
}
