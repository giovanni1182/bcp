package com.gvs.crm.model.impl;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.CodificacaoCobertura;
import com.gvs.crm.model.CodificacaoDetalhe;
import com.gvs.crm.model.CodificacaoPlano;
import com.gvs.crm.model.CodificacaoRisco;
import com.gvs.crm.model.CodificacoesHome;
import com.gvs.crm.model.EventoHome;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class CodificacoesHomeImpl extends Home implements CodificacoesHome 
{

	public CodificacaoPlano obterPlano(String codigo) throws Exception 
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoPlano' and codigo = ?");
		query.addString(codigo);
		
		CodificacaoPlano plano = null;
		
		if(query.executeAndGetFirstRow().getLong("id") > 0)
		{
			long id = query.executeAndGetFirstRow().getLong("id");
			plano = (CodificacaoPlano) home.obterEventoPorId(id);
		}
		
		return plano;
	}

	public String obterMaiorCodigoPlano() throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select codificacoes.codigo from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoPlano'");
		
		SQLRow[] rows = query.execute();
		
		if(rows.length == 0)
			return "001";
		else
			return this.retornaMaiorNumero(rows,"plano");
	}

	public String obterMaiorCodigoRisco(CodificacaoCobertura cobertura) throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select codificacoes.codigo from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoRisco' and superior = ?");
		query.addLong(cobertura.obterId());
		
		SQLRow[] rows = query.execute();
		
		if(rows.length == 0)
			return "01";
		else
			return this.retornaMaiorNumero(rows, "risco");
	}

	public String obterMaiorCodigoCobertura(CodificacaoPlano plano) throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select codificacoes.codigo from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoCobertura' and superior = ?");
		query.addLong(plano.obterId());
		
		SQLRow[] rows = query.execute();
		
		if(rows.length == 0)
			return "01";
		else
			return this.retornaMaiorNumero(rows, "cobertura");
	}

	public String obterMaiorCodigoDetalhe(CodificacaoRisco risco) throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select codificacoes.codigo from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoDetalhe' and superior = ?");
		query.addLong(risco.obterId());
		
		SQLRow[] rows = query.execute();
		
		if(rows.length == 0)
			return "001";
		else
			return this.retornaMaiorNumero(rows, "detalhe");
	}
	
	private String retornaMaiorNumero(SQLRow[] rows, String tipoPlano) throws Exception
	{
		int maior = 0;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			String codigoQ = rows[i].getString("codigo");
			
			int codigoInt = Integer.parseInt(codigoQ);
			
			if(codigoInt > maior)
				maior = codigoInt; 
		}
		
		maior++;
		
		String maiorStr = new Integer(maior).toString();
		
		if(tipoPlano.equals("plano") || tipoPlano.equals("detalhe"))
		{
			if(maiorStr.length() == 1)
				maiorStr = "00" + maiorStr;
			else if(maiorStr.length() == 2)
				maiorStr = "0" + maiorStr;
		}
		else
		{
			if(maiorStr.length() == 1)
				maiorStr = "0" + maiorStr;
		}
		
		return maiorStr;
	}

	public boolean verificaCodigoCobertura(String codigo, CodificacaoPlano plano) throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select codigo from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoCobertura' and superior = ? and codigo = ?");
		query.addLong(plano.obterId());
		query.addString(codigo);
		
		if(query.execute().length > 0)
			return true;
		else
			return false;
	}

	public boolean verificaCodigoDetalhe(String codigo, CodificacaoRisco risco) throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select codigo from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoDetalhe' and superior = ? and codigo = ?");
		query.addLong(risco.obterId());
		query.addString(codigo);
		
		if(query.execute().length > 0)
			return true;
		else
			return false;
	}

	public boolean verificaCodigoPlano(String codigo) throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select codigo from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoPlano' and codigo = ?");
		query.addString(codigo);
		
		if(query.execute().length > 0)
			return true;
		else
			return false;
	}

	public boolean verificaCodigoRisco(String codigo,CodificacaoCobertura cobertura) throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select codigo from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoRisco' and superior = ? and codigo = ?");
		query.addLong(cobertura.obterId());
		query.addString(codigo);
		
		if(query.execute().length > 0)
			return true;
		else
			return false;
	}
	
	public Collection obterCodificacaoPlanos() throws Exception
	{
		Map planos = new TreeMap();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoPlano'");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			CodificacaoPlano plano = (CodificacaoPlano) home.obterEventoPorId(id);
			
			String codigo = plano.obterCodigo();
			
			int codigoInt = Integer.parseInt(codigo);
			
			planos.put(new Integer(codigoInt), plano);
		}
		
		return planos.values();
	}

	public Collection obterCodificacaoCoberturas() throws Exception
	{
		Map coberturas = new TreeMap();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoCobertura'");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			CodificacaoCobertura cobertura = (CodificacaoCobertura) home.obterEventoPorId(id);
			
			CodificacaoPlano plano = (CodificacaoPlano) cobertura.obterSuperior();
			
			int codigoInt = Integer.parseInt(cobertura.obterCodigo());
			
			int codigoPlano = Integer.parseInt(plano.obterCodigo());
			
			coberturas.put(new Long(codigoPlano + codigoInt + cobertura.obterCriacao().getTime()), cobertura);
		}
		
		return coberturas.values();
	}

	public Collection obterCodificacaoRiscos() throws Exception
	{
		Map riscos = new TreeMap();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoRisco'");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			CodificacaoRisco risco = (CodificacaoRisco) home.obterEventoPorId(id);
			
			CodificacaoCobertura cobertura = (CodificacaoCobertura) risco.obterSuperior();
			
			CodificacaoPlano plano = (CodificacaoPlano) cobertura.obterSuperior();
			
			int codigoInt = Integer.parseInt(risco.obterCodigo());
			
			int codigoCobertura = Integer.parseInt(cobertura.obterCodigo());
			
			int codigoPlano = Integer.parseInt(plano.obterCodigo());
			
			riscos.put(new Long(codigoPlano + codigoCobertura + codigoInt + risco.obterCriacao().getTime()), risco);
		}
		
		return riscos.values();
	}
	
	public Collection obterCodificacaoDetalhes() throws Exception
	{
		Map riscos = new TreeMap();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,codificacoes where evento.id = codificacoes.id and classe='CodificacaoDetalhe'");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			CodificacaoDetalhe detalhe = (CodificacaoDetalhe) home.obterEventoPorId(id);
			
			CodificacaoRisco risco = (CodificacaoRisco) detalhe.obterSuperior();
			
			CodificacaoCobertura cobertura = (CodificacaoCobertura) risco.obterSuperior();
			
			CodificacaoPlano plano = (CodificacaoPlano) cobertura.obterSuperior();
			
			int codigoInt = Integer.parseInt(detalhe.obterCodigo());
			
			int codigoRisco = Integer.parseInt(risco.obterCodigo());
			
			int codigoCobertura = Integer.parseInt(cobertura.obterCodigo());
			
			int codigoPlano = Integer.parseInt(plano.obterCodigo());
			
			riscos.put(new Long(codigoPlano + codigoCobertura + codigoRisco + codigoInt + detalhe.obterCriacao().getTime()), detalhe);
		}
		
		return riscos.values();
	}
	
	
}
