package com.gvs.crm.model.impl;

import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Qualificacao;
import com.gvs.crm.model.QualificacaoHome;

import infra.model.Home;
import infra.sql.SQLQuery;

public class QualificacaoHomeImpl extends Home implements QualificacaoHome
{
	public Qualificacao obterQualificacao(int codigo) throws Exception
	{
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		Qualificacao qualificacao = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select entidade.id from entidade,qualificacao where entidade.id = qualificacao.id and codigo = ?");
		query.addInt(codigo);
		
		long id = query.executeAndGetFirstRow().getLong("id");
		
		if(id > 0)
			qualificacao = (Qualificacao) home.obterEntidadePorId(id);
		
		return qualificacao;
	}
}