package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.gvs.crm.model.RucCiHome;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class RucCiHomeImpl extends Home implements RucCiHome
{
	/*Función: get_persona_por_documento (<tipo de documento>, <número de documento>)
    Retorna: string delimitado por comillas y punto y coma
    Formato: tipo de persona, país Swift, nombre, apellido, fecha de nacimiento*/
    
	public Collection<String> obterPessoaPorDoc(String tipoDoc, String numeroDoc) throws Exception
	{
		Collection<String> pessoas = new ArrayList<String>();
		
		//SQLQuery query = this.getModelManager().createSQLQuery("orcl","select wapps.pkg_get_persona.get_persona_por_documento('"+tipoDoc+"','"+numeroDoc+"') as coluna from dual");
		
		SQLQuery query = this.getModelManager().createSQLQuery("orcl","select RF_PAIS_SWIFT, RF_NOMBRE,RF_APELLIDO,RF_FECHA_NAC from PERSONAS_REF where RF_TIPO_IDENT = ? and RF_NUMERO = ?");
		query.addString(tipoDoc);
		query.addString(numeroDoc);
		
		if(query.execute().length > 0)
		{
			SQLRow[] rows = query.execute();
			pessoas.add(";" + rows[0].getString("RF_PAIS_SWIFT") + ";" + rows[0].getString("RF_NOMBRE")+";"+rows[0].getString("RF_APELLIDO")+";"+rows[0].getDate("RF_FECHA_NAC"));
		}
		
		return pessoas;
	}
	
	/*Función: get_persona_por_nombre (<tipo de persona>,<nombre>,<apellido|NULL>,<fecha de nacimiento|NULL>)
    Retorna: string delimitado por comillas y punto y coma
    Formato: tipo de persona, país Swift, tipo de documento, numero de documento
	Cuando el tipo de persona es 1 (jurídica), apellido y fecha de nacimiento deben ser nulos, caso contrario son obligatorios.*/
	
	public Collection<String> obterPessoaPorNome(String tipoPessoa,String nome, String sobreNome, Date dataNasc) throws Exception
	{
		//wapps.pkg_get_persona.get_persona_por_nombre('1', 'SUPERMERCADO PARANA S.A.', '', null)
		//Resultado:  "1";"PY";"RUC";"80000013-7"
		
		return null;
	}
	
	
}