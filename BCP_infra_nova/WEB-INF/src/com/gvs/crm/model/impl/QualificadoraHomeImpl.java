package com.gvs.crm.model.impl;

import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Qualificadora;
import com.gvs.crm.model.QualificadoraHome;

import infra.model.Home;
import infra.sql.SQLQuery;

public class QualificadoraHomeImpl extends Home implements QualificadoraHome
{
	public Qualificadora obterQualificadora(int codigo) throws Exception
	{
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		Qualificadora qualificadora = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select entidade.id from entidade,qualificadora where entidade.id = qualificadora.id and codigo = ?");
		query.addInt(codigo);
		
		long id = query.executeAndGetFirstRow().getLong("id");
		
		if(id > 0)
			qualificadora = (Qualificadora) home.obterEntidadePorId(id);
		
		return qualificadora;
	}

}
