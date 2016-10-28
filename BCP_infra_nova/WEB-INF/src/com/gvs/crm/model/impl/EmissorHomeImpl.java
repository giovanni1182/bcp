package com.gvs.crm.model.impl;

import com.gvs.crm.model.Emissor;
import com.gvs.crm.model.EmissorHome;
import com.gvs.crm.model.EntidadeHome;

import infra.model.Home;
import infra.sql.SQLQuery;

public class EmissorHomeImpl extends Home implements EmissorHome
{
	public Emissor obterEmissor(int codigo) throws Exception
	{
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		Emissor emissor = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select entidade.id from entidade,emissor where entidade.id = emissor.id and codigo = ?");
		query.addInt(codigo);
		
		long id = query.executeAndGetFirstRow().getLong("id");
		
		if(id > 0)
			emissor = (Emissor) home.obterEntidadePorId(id);
		
		return emissor;
	}
}
