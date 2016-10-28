package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Plano;
import com.gvs.crm.model.PlanoHome;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class PlanoHomeImpl extends Home implements PlanoHome
{
	public Plano obterPlano(String codigo) throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,plano where evento.id = plano.id and identificador = ?");
		query.addString(codigo);

		SQLRow[] rows = query.execute();

		Plano plano = null;

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		for (int i = 0; i < rows.length; i++) 
		{
			long id = rows[i].getLong("id");

			plano = (Plano) home.obterEventoPorId(id);
		}

		return plano;
	}
	
	public Plano obterPlano(Entidade aseguradora, String codigo) throws Exception 
	{
		Plano plano = null;
        
        EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
        
        SQLQuery query = this.getModelManager().createSQLQuery("crm", "select evento.id from evento,plano where evento.id = plano.id and identificador = ? and situacao = 'Activo' and origem = ?");
        query.addString(codigo);
        query.addLong(aseguradora.obterId());
        
        long id = query.executeAndGetFirstRow().getLong("id");
        
        if(id > 0)
            plano = (Plano)home.obterEventoPorId(id);
        
        return plano;
	}

	public Collection<String> obterNomesPlano() throws Exception 
	{
		//SQLQuery query = this.getModelManager().createSQLQuery("crm","SELECT plano.id, evento.origem, plano.plano  FROM evento,entidade,plano where evento.classe='Plano' and evento.id = plano.id and evento.origem=entidade.Id and evento.origem is not null and plano is not null");
		//SQLQuery query = this.getModelManager().createSQLQuery("crm","SELECT plano.plano FROM evento,entidade,plano where evento.classe='Plano' and evento.id = plano.id and evento.origem=entidade.Id and evento.origem is not null and plano is not null group by plano order by plano");
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select nome from plano_planos group by nome");
		SQLRow[] rows = query.execute();

		Map<String,String> planos = new TreeMap<>();
		String plano;
		
		for (int i = 0; i < rows.length; i++) 
		{
			plano = rows[i].getString("nome");
			if(plano.trim().length() > 0)
				planos.put(plano, plano);
		}

		return planos.values();
	}

	public Collection obterNomesRamo() throws Exception {
		Map ramos = new TreeMap();

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"SELECT plano.id, evento.origem, plano.ramo  FROM evento,entidade,plano where evento.id = plano.id and evento.origem=entidade.Id and evento.origem is not null");

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			String ramo = rows[i].getString("ramo");

			ramos.put(ramo, ramo);
		}

		return ramos.values();
	}

	public Collection<String> obterNomesSecao() throws Exception
	{
		Collection<String> secoes = new ArrayList<>();

		SQLQuery query = this.getModelManager().createSQLQuery("crm","SELECT plano.secao FROM evento,entidade,plano where evento.id = plano.id and evento.origem=entidade.Id and evento.origem is not null group by plano.secao order by plano.secao");

		SQLRow[] rows = query.execute();
		String secao;
		
		for (int i = 0; i < rows.length; i++)
		{
			secao = rows[i].getString("secao");

			secoes.add(secao);
		}

		return secoes;
	}
	
	public Collection<String> obterSecoesPorRamo(String ramo) throws Exception
	{
		Collection<String> secoes = new ArrayList<String>();

		if(ramo.equals(""))
			return this.obterNomesSecao();
		else
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","SELECT plano.secao FROM evento,entidade,plano where evento.id = plano.id and evento.origem=entidade.Id and evento.origem is not null and seg_ramo = ? group by plano.secao order by plano.secao");
			//SQLQuery query = this.getModelManager().createSQLQuery("crm","SELECT seg_ramo FROM evento,entidade,plano where evento.id = plano.id and evento.origem=entidade.Id and evento.origem is not null and seg_ramo = ? group by plano.secao order by plano.secao");
			query.addString(ramo);
			
			//System.out.println("SELECT seg_ramo FROM evento,entidade,plano where evento.id = plano.id and evento.origem=entidade.Id and evento.origem is not null and seg_ramo = '"+ramo+"' group by plano.secao order by plano.secao");
			
			SQLRow[] rows = query.execute();
			String secao;
			
			for (int i = 0; i < rows.length; i++)
			{
				//secao = rows[i].getString("secao");
				secao = rows[i].getString("secao");
				if(secao.trim().length() > 0)
					secoes.add(secao);
			}
	
			return secoes;
		}
	}
	
	public Collection<String> obterModalidadePorSecao(String secao) throws Exception
	{
		Collection<String> modalidades = new ArrayList<String>();

		if(secao.equals(""))
			return this.obterNomesPlano();
		else
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","SELECT plano.plano FROM evento,entidade,plano where evento.id = plano.id and evento.origem=entidade.Id and evento.origem is not null and seg_secao = ? group by plano.plano order by plano.plano");
			query.addString(secao);
			
			SQLRow[] rows = query.execute();
	
			for (int i = 0; i < rows.length; i++)
			{
				String modalidade = rows[i].getString("plano");
	
				modalidades.add(modalidade);
			}
	
			return modalidades;
		}
	}

	public Collection obterNomesSituacao() throws Exception {
		Map situacoes = new TreeMap();

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"SELECT plano.id, evento.origem, plano.situacao  FROM evento,entidade,plano where evento.id = plano.id and evento.origem=entidade.Id and evento.origem is not null");

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			String situacao = rows[i].getString("situacao");

			situacoes.put(situacao, situacao);
		}

		return situacoes.values();
	}

	public Collection obterPlanos() throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","SELECT evento.id, evento.origem, plano.plano  FROM evento,entidade,plano where evento.classe='Plano' and evento.id = plano.id and evento.origem=entidade.Id and evento.origem is not null");

		SQLRow[] rows = query.execute();

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		Map planos = new TreeMap();

		for (int i = 0; i < rows.length; i++) 
		{
			long id = rows[i].getLong("id");

			Plano plano = (Plano) home.obterEventoPorId(id);

			planos.put(new Long(plano.obterId()), plano);
		}

		return planos.values();
	}
	
	public Collection obterPlanos(String numero) throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","SELECT evento.id FROM evento,entidade,plano where evento.classe='Plano' and evento.id = plano.id and evento.origem=entidade.Id and evento.origem is not null and identificador = ?");
		query.addString(numero);
		
		SQLRow[] rows = query.execute();

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		Map planos = new TreeMap();

		for (int i = 0; i < rows.length; i++) 
		{
			long id = rows[i].getLong("id");

			Plano plano = (Plano) home.obterEventoPorId(id);

			planos.put(new Long(plano.obterId()), plano);
		}

		return planos.values();
	}
	
	public Collection<Plano> localizarPlanos(String ramo, String secao, String codigo, String situacao, Aseguradora aseguradora) throws Exception
	{
		Collection<Plano> planos = new ArrayList<Plano>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "select evento.id from evento,plano where evento.id = plano.id";
		if(!ramo.equals(""))
			sql+=" and ramo = '"+ramo+"'";
		if(!secao.equals(""))
			sql+=" and secao = '"+secao+"'";
		if(!codigo.equals(""))
			sql+=" and plano = '"+codigo+"'";
		if(!situacao.equals(""))
			sql+=" and situacao = '"+situacao+"'";
		if(aseguradora!=null)
			sql+=" and origem = "+ aseguradora.obterId();
		
		sql+=" and especial = 0";
		
		sql+=" order by criacao";
		
		System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Plano plano = (Plano) home.obterEventoPorId(id);
			
			planos.add(plano);
		}
		
		return planos;
	}
	
	public Collection<Plano> localizarPlanos(String ramo, String secao, String codigo, String situacao, Aseguradora aseguradora, boolean especial, boolean modificado) throws Exception
	{
		Collection<Plano> planos = new ArrayList<Plano>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "select evento.id from evento,plano where evento.id = plano.id";
		if(!ramo.equals(""))
			sql+=" and ramo = '"+ramo+"'";
		if(!secao.equals(""))
			sql+=" and secao = '"+secao+"'";
		if(!codigo.equals(""))
			sql+=" and plano = '"+codigo+"'";
		if(!situacao.equals(""))
			sql+=" and situacao = '"+situacao+"'";
		if(aseguradora!=null)
			sql+=" and origem = "+ aseguradora.obterId();
		if(especial)
			sql+=" and especial = 1";
		if(modificado)
			sql+=" and especial = 2";
		if(!especial && !modificado)
			sql+=" and especial = 0";
		
		sql+=" order by criacao";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Plano plano = (Plano) home.obterEventoPorId(id);
			
			planos.add(plano);
		}
		
		return planos;
	}
	
	public void atualizarSecao(String nomeAntigo, String nomeNovo) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update plano_secao set nome = ? where nome = ?");
		update.addString(nomeNovo);
		update.addString(nomeAntigo);
		
		update.execute();
		
		update = this.getModelManager().createSQLUpdate("crm","update plano set secao = ? where secao = ?");
		update.addString(nomeNovo);
		update.addString(nomeAntigo);
		
		update.execute();
	}
	
	public void atualizarCodigo(String nomeAntigo, String nomeNovo) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update plano_planos set nome = ? where nome = ?");
		update.addString(nomeNovo);
		update.addString(nomeAntigo);
		
		update.execute();
		
		update = this.getModelManager().createSQLUpdate("crm","update plano set plano = ? where plano = ?");
		update.addString(nomeNovo);
		update.addString(nomeAntigo);
		
		update.execute();
	}
	
	public boolean permiteExcluirSecao(String secao) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from plano where secao = ?");
		query.addString(secao);
		
		if(query.executeAndGetFirstRow().getInt("qtde") == 0)
			return true;
		else
			return false;
	}
	
	public boolean permiteExcluirCodigo(String codigo) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from plano where plano = ?");
		query.addString(codigo);
		
		if(query.executeAndGetFirstRow().getInt("qtde") == 0)
			return true;
		else
			return false;
	}
	
	public void atualizarSegmentoRamo(String ramo, String secao) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update plano set seg_ramo = ? where secao = ?");
		update.addString(ramo);
		update.addString(secao);
		
		update.execute();
	}
	
	public void atualizarSegmentoSecao(String secao, String modalidade) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update plano set seg_secao = ? where plano = ?");
		update.addString(secao);
		update.addString(modalidade);
		
		update.execute();
	}
	
	public String obterSegRamo(String secao) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select top 1 seg_ramo from plano where secao = ?");
		query.addString(secao);
		
		return query.executeAndGetFirstRow().getString("seg_ramo");
	}
	
	public String obterSegSecao(String modalidade) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select top 1 seg_secao from plano where plano = ?");
		query.addString(modalidade);
		
		return query.executeAndGetFirstRow().getString("seg_secao");
	}
	
	public void manutPlano() throws Exception
    {
    	EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
    	EntidadeHome entiadeHome = (EntidadeHome)getModelManager().getHome("EntidadeHome");
    	
    	String sql = "select TOP 100000 evento.id,origem,identificador from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and apolice.plano > 0 and origem = 5217 order by evento.id desc";
    	
    	SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
    	
    	SQLRow[] rows = query.execute();
    	
    	for(int i = 0 ; i < rows.length ; i++)
    	{
    		long id = rows[i].getLong("id");
    		long asegId =  rows[i].getLong("origem");
    		String identificador = rows[i].getString("identificador");
    		
    		Apolice apolice = (Apolice) home.obterEventoPorId(id);
    		Plano planoGravado = apolice.obterPlano();
    		Entidade aseguradora = entiadeHome.obterEntidadePorId(asegId);
    		
    		Plano plano = this.obterPlano(aseguradora, identificador);
    		
    		if(plano == null)
    			System.out.println("Não encontrou plano " + identificador + " para aseguradora " + aseguradora.obterNome() + " Apolice " + apolice.obterNumeroApolice());
    		else
    		{
    			if(planoGravado.obterId()!=plano.obterId())
    				//System.out.println("Não precisou mudar plano " + identificador + " para aseguradora " + aseguradora.obterNome());
    			//else
    				System.out.println("Precisou mudar plano " + identificador + " para aseguradora " + aseguradora.obterNome() + " Apolice " + apolice.obterNumeroApolice());
    		}
    	}
    }
	
	public Plano obterPlanoEspecial(String identificador) throws Exception
    {
    	Plano plano = null;
    	EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
    	
    	SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,plano where evento.id = plano.id and identificador = ? and situacao = 'Activo' and especial = 1");
    	query.addString(identificador);
    	
    	long id = query.executeAndGetFirstRow().getInt("id");
    	if(id > 0)
    		plano = (Plano) home.obterEventoPorId(id);
    	
    	return plano;
    }
	
	public Collection<Plano> obterPlanosEspeciais() throws Exception
    {
		Collection<Plano> planos = new ArrayList<Plano>();
		
		Plano plano = null;
		
    	EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
    	
    	SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,plano where evento.id = plano.id and especial = 1 order by titulo");
    	
    	SQLRow[] rows = query.execute();
    	
    	for(int i = 0 ; i < rows.length ; i++)
    	{
	    	long id = rows[i].getLong("id");
	    	if(id > 0)
	    	{
	    		plano = (Plano) home.obterEventoPorId(id);
	    		
	    		planos.add(plano);
	    	}
    	}
    	
    	return planos;
    }
}