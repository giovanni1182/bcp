/*
 * Created on Mar 10, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Prueba;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

/**
 * @author User
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Pruebaimpl extends Home implements Prueba {

	/* (non-Javadoc)
	 * @see com.gvs.crm.model.Prueba#GetAseguradoras()
	 */
	public Collection GetAseguradoras() throws Exception {
		SQLQuery Aseguradoras = this.getModelManager().createSQLQuery("crm","select id from entidade where classe = 'Aseguradora'"); 
				
		Collection retorno = new ArrayList();
		SQLRow[] resultset = Aseguradoras.execute(); 
		
		EntidadeHome home =(EntidadeHome) this.getModelManager().getHome("EntidadeHome"); 
		
		for(int i = 0 ; i < resultset.length;i++) {
			
			long id = resultset[i].getLong("id");
			
			Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(id);
			retorno.add(aseguradora);
			
		}
		return retorno;
	}

}
