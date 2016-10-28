package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Morosidade;
import com.gvs.crm.model.MorosidadeHome;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class MorosidadeHomeImpl extends Home implements MorosidadeHome 
{
	public Collection<Entidade> obterAseguradorasCentralRisco(String nomeAsegurado, String documento) throws Exception
	{
		Map<Long,Entidade> sinistros = new TreeMap<>();
		
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		String sql = "select origem from evento,apolice,morosidade where evento.id = morosidade.id and evento.superior = apolice.id";
		if(nomeAsegurado.length() > 0)
			sql+=" and nome_asegurado like ?";
		if(documento.length() > 0)
			sql+=" and RTrim(LTrim(numero_identificacao)) = '"+documento+"'";
		
		sql+=" group by origem";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", sql);
		if(nomeAsegurado.length() > 0)
			query.addString("%"+nomeAsegurado+"%");
		
		SQLRow[] rows = query.execute();
		long id;
		Entidade entidade;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("origem");
			
			entidade = home.obterEntidadePorId(id);
			
			sinistros.put(new Long(id), entidade);
		}
		
		sql = "select origem from evento,apolice,morosidade,apolice_asegurados where evento.id = morosidade.id and evento.superior = apolice.id and apolice.id = apolice_asegurados.id";
		if(nomeAsegurado.length() > 0)
			sql+=" and (nome like ? or sobre_nome like ?)";
		if(documento.length() > 0)
			sql+=" and numero_documento = '"+documento+"'";
		
		sql+=" group by origem";
		
		query = this.getModelManager().createSQLQuery("crm", sql);
		if(nomeAsegurado.length() > 0)
		{
			query.addString("%"+nomeAsegurado+"%");
			query.addString("%"+nomeAsegurado+"%");
		}
		
		rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("origem");
			
			entidade = home.obterEntidadePorId(id);
			
			sinistros.put(new Long(id), entidade);
		}
		
		return sinistros.values();
	}
	
	public Map<Long,Collection<Morosidade>> obterAseguradorasCentralRiscoNovo(String nomeAsegurado, String documento) throws Exception
	{
		Map<Long,Collection<Morosidade>> sinistros = new TreeMap<>();
		
		//EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql2;
		String sql = "select origem,evento.id,data_corte,numero_parcela,data_vencimento,superior from evento,apolice,morosidade where evento.id = morosidade.id and evento.superior = apolice.id";
		if(nomeAsegurado.length() > 0)
			sql+=" and nome_asegurado like ?";
		if(documento.length() > 0)
			sql+=" and RTrim(LTrim(numero_identificacao)) = '"+documento+"'";
		
		sql+=" order by origem";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", sql);
		if(nomeAsegurado.length() > 0)
			query.addString("%"+nomeAsegurado+"%");
		
		SQLRow[] rows = query.execute();
		long id,dataCorte,dataVencimento,origem,superior;
		int numeroParcela;
		Collection<Morosidade> morosidades;
		Morosidade morosidade;
		Collection<Long> morosidadesAdicionadas = new ArrayList<>();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			origem = rows[i].getLong("origem");
			id = rows[i].getLong("id");
			dataCorte = rows[i].getLong("data_corte");
			dataVencimento = rows[i].getLong("data_vencimento");
			numeroParcela = rows[i].getInt("numero_parcela");
			superior = rows[i].getLong("superior");
			
			//Verificar se tem Registro de Cobrança
			
			sql2 = "select count(*) as qtde from evento,registro_cobranca where evento.id = registro_cobranca.id and superior = " + superior + " and  data_vencimento = " + dataVencimento + " and numero_parcela = " + numeroParcela + " and origem = " + origem;
			query = this.getModelManager().createSQLQuery("crm",sql2);
			//System.out.println(sql2);
			
			if(query.executeAndGetFirstRow().getInt("qtde") == 0)
			{
				//entidade = home.obterEntidadePorId(origem);
				morosidade = (Morosidade) eventoHome.obterEventoPorId(id);
				
				morosidadesAdicionadas.add(id);
				
				if(sinistros.containsKey(origem))
				{
					morosidades = sinistros.get(origem);
					morosidades.add(morosidade);
					
					sinistros.put(origem, morosidades);
				}
				else
				{
					morosidades = new ArrayList<>();
					morosidades.add(morosidade);
					
					sinistros.put(origem, morosidades);
				}
			}
		}
		
		//System.out.println("morosidadesAdicionadas " + morosidadesAdicionadas.size());
		
		//System.out.println(sql);
		
		sql = "select origem,evento.id,data_corte,numero_parcela,data_vencimento,superior from evento,apolice,morosidade,apolice_asegurados where evento.id = morosidade.id and evento.superior = apolice.id and apolice.id = apolice_asegurados.id";
		if(nomeAsegurado.length() > 0)
			sql+=" and (nome + ' ' + sobre_nome like ?)";
		if(documento.length() > 0)
			sql+=" and numero_documento = '"+documento+"'";
		
		sql+=" order by origem";
		
		query = this.getModelManager().createSQLQuery("crm", sql);
		if(nomeAsegurado.length() > 0)
			query.addString("%"+nomeAsegurado+"%");
		
		rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			origem = rows[i].getLong("origem");
			id = rows[i].getLong("id");
			dataCorte = rows[i].getLong("data_corte");
			dataVencimento = rows[i].getLong("data_vencimento");
			numeroParcela = rows[i].getInt("numero_parcela");
			superior = rows[i].getLong("superior");
			if(!morosidadesAdicionadas.contains(id))
			{
				//Verificar se tem Registro de Cobrança
				query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from evento,registro_cobranca where evento.id = registro_cobranca.id and superior = " + superior + " and data_vencimento = " + dataVencimento + " and numero_parcela = " + numeroParcela + " and origem = " + origem);
				if(query.executeAndGetFirstRow().getInt("qtde") == 0)
				{
					//entidade = home.obterEntidadePorId(origem);
					
					if(sinistros.containsKey(origem))
					{
						morosidades = sinistros.get(origem);
						morosidades.add((Morosidade) eventoHome.obterEventoPorId(id));
						
						sinistros.put(origem, morosidades);
					}
					else
					{
						morosidades = new ArrayList<>();
						morosidades.add((Morosidade) eventoHome.obterEventoPorId(id));
						
						sinistros.put(origem, morosidades);
					}
				}
			}
		}
		
		return sinistros;
	}
}
