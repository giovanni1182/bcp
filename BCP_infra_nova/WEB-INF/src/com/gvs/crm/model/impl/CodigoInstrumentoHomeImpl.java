package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.gvs.crm.model.CodigoInstrumento;
import com.gvs.crm.model.CodigoInstrumentoHome;
import com.gvs.crm.model.EntidadeHome;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class CodigoInstrumentoHomeImpl extends Home implements CodigoInstrumentoHome
{
	public CodigoInstrumento obterCodigoInstrumento(int codigo) throws Exception
	{
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		CodigoInstrumento codigoInstrumento = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select entidade.id from entidade,codigo_instrumento where entidade.id = codigo_instrumento.id and codigo = ?");
		query.addInt(codigo);
		
		long id = query.executeAndGetFirstRow().getLong("id");
		
		if(id > 0)
			codigoInstrumento = (CodigoInstrumento) home.obterEntidadePorId(id);
		
		return codigoInstrumento;
	}
	
	public Collection<CodigoInstrumento> obterCodigosInstrumento() throws Exception
	{
		Collection<CodigoInstrumento> codigos = new ArrayList<>();
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select entidade.id from entidade,codigo_instrumento where entidade.id = codigo_instrumento.id order by codigo");
		
		SQLRow[] rows = query.execute();
		long id;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			
			codigos.add((CodigoInstrumento) home.obterEntidadePorId(id));
		}
		
		return codigos;
	}
}