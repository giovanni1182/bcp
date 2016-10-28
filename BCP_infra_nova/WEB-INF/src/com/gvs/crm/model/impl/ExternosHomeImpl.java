package com.gvs.crm.model.impl;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.ExternosHome;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class ExternosHomeImpl extends Home implements ExternosHome
{
	public boolean possuemAgendasNoPerido(Date data) throws Exception
	{
		AseguradoraHome home = (AseguradoraHome) this.getModelManager().getHome("AseguradoraHome");
		
		boolean retorno = true;
		
		int mes = new Integer(new SimpleDateFormat("MM").format(data)).intValue();
		int ano = new Integer(new SimpleDateFormat("yyyy").format(data)).intValue();
		
		for(Iterator i = home.obterAseguradorasPorMenor80OrdenadoPorNome().iterator() ; i.hasNext() ; )
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
			
			if(!aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
			{
				retorno = false;
				break;
			}
		}
		
		return retorno;
	}

	public Collection obterTodasAsContas() throws Exception
	{
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		Map contas = new TreeMap();
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select entidade.id from entidade,classificacao_contas where entidade.id = classificacao_contas.id and apelido<>'0000000000' order by codigo_conta");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorId(id);
			
			contas.put(cContas.obterApelido(),cContas);
		}
		
		query = this.getModelManager().createSQLQuery("crm","select entidade.id from entidade,conta where entidade.id = conta.id order by codigo_conta");
		
		rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Conta conta = (Conta) home.obterEntidadePorId(id);
			
			contas.put(conta.obterApelido(),conta);
		}
		       
		return contas.values();
	}
	
	
}