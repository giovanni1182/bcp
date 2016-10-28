package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Usuario;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class AuxiliarSeguroImpl extends EntidadeImpl implements AuxiliarSeguro {
	private Map ramos;

	public class RamoImpl implements Ramo {

		private AuxiliarSeguroImpl auxiliar;

		private int seq;

		private String ramo;

		public RamoImpl(AuxiliarSeguroImpl auxiliar, int seq, String ramo)
				throws Exception {
			this.auxiliar = auxiliar;
			this.seq = seq;
			this.ramo = ramo;
		}

		public AuxiliarSeguro obterAuxiliarSeguro() throws Exception {
			return this.auxiliar;
		}

		public int obterSeq() throws Exception {
			return this.seq;
		}

		public String obterRamo() throws Exception {
			return this.ramo;
		}
	}

	public void atualizarAseguradora(Aseguradora aseguradora) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update auxiliar_seguro set aseguradora=? where id=?");
		update.addLong(aseguradora.obterId());
		update.addLong(this.obterId());
		update.execute();
	}

	public Aseguradora obterAseguradora() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select aseguradora from auxiliar_seguro where id=?");
		query.addLong(this.obterId());

		Aseguradora aseguradora = null;

		long id = query.executeAndGetFirstRow().getLong("aseguradora");

		if (id > 0) 
		{
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

			aseguradora = (Aseguradora) home.obterEntidadePorId(id);
		}

		return aseguradora;
	}

	public Collection obterAseguradoras(Date dataInicio, Date dataFim) throws Exception
	{
		Map aseguradoras = new TreeMap();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select origem from evento,apolice where evento.id = apolice.id and agente = ? and data_prevista_inicio>=? and data_prevista_inicio<=? group by origem");
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
		{
			long id = rows[i].getLong("origem");

			if (id > 0)
			{
				Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(id);
				aseguradoras.put(aseguradora.obterNome(), aseguradora);
			}
		}

		return aseguradoras.values();
	}
	
	public Collection obterAseguradoras2(Date dataInicio, Date dataFim) throws Exception
	{
		Map aseguradoras = new TreeMap();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select origem from evento,apolice where evento.id = apolice.id and agente = ? and data_emissao>=? and data_emissao<=? group by origem");
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
		{
			long id = rows[i].getLong("origem");

			if (id > 0)
			{
				Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(id);
				aseguradoras.put(aseguradora.obterNome(), aseguradora);
			}
		}

		return aseguradoras.values();
	}
	
	public Collection<Aseguradora> obterAseguradorasCorredor(Date dataInicio, Date dataFim) throws Exception
	{
		Map<String, Aseguradora> aseguradoras = new TreeMap<String, Aseguradora>();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select origem from evento,apolice where evento.id = apolice.id and corredor = ? and data_prevista_inicio>=? and data_prevista_inicio<=? group by origem");
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
		{
			long id = rows[i].getLong("origem");

			if (id > 0)
			{
				Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(id);

				aseguradoras.put(aseguradora.obterNome(), aseguradora);
			}
		}

		return aseguradoras.values();
	}

	public void adicionarNovoRamo(String ramo) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select MAX(id) as MX from auxiliar_seguro_ramo where entidade=?");
		query.addLong(this.obterId());

		int id = query.executeAndGetFirstRow().getInt("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate("crm",
						"insert into auxiliar_seguro_ramo(entidade, id, nome) values (?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addInt(id);
		insert.addString(ramo);
		insert.execute();
	}

	/*
	 * public void atualizarRamo(String ramo) throws Exception { SQLUpdate
	 * update = this.getModelManager().createSQLUpdate("crm", "update
	 * auxiliar_seguro set ramo=? where id=?"); update.addString(ramo);
	 * update.addLong(this.obterId()); update.execute(); }
	 * 
	 * public String obterRamo() throws Exception { SQLQuery query =
	 * this.getModelManager().createSQLQuery("crm", "select ramo from
	 * auxiliar_seguro where id=?"); query.addLong(this.obterId());
	 * 
	 * return query.executeAndGetFirstRow().getString("ramo"); }
	 */

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into auxiliar_seguro(id) values(?)");
		insert.addLong(this.obterId());
		insert.execute();
	}

	public Collection obterNomeRamos() throws Exception {
		Collection nomeRamos = new ArrayList();

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select nome from auxiliar_seguro_ramo group by nome");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++)
			nomeRamos.add(rows[i].getString("nome"));

		return nomeRamos;
	}

	public Ramo obterRamo(int seq) throws Exception {
		this.obterRamos();

		return (Ramo) this.ramos.get(new Integer(seq));
	}

	public Collection obterRamos() throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select * from auxiliar_seguro_ramo where entidade=?");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		this.ramos = new TreeMap();

		for (int i = 0; i < rows.length; i++) 
		{
			int seq = rows[i].getInt("id");
			String ramo = rows[i].getString("nome");

			this.ramos.put(new Integer(seq), new RamoImpl(this, seq, ramo));
		}

		return this.ramos.values();
	}

	public void excluirRamo(AuxiliarSeguro.Ramo ramo) throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate("crm",
				"delete from auxiliar_seguro_ramo where entidade=? and id=?");
		delete.addLong(this.obterId());
		delete.addInt(ramo.obterSeq());

		delete.execute();
	}
	
	public boolean permiteAtualizar() throws Exception
	{
		//Entidade icoras = home.obterEntidadePorApelido("jefedivisioncontrolicoras");
		
		Usuario usuarioAtual = this.obterUsuarioAtual();
		
		boolean retorno = false;
		
		String nivel = usuarioAtual.obterNivel();
		
		if(nivel.equals(Usuario.ADMINISTRADOR))
			retorno = true;
		else
		{
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
			Entidade icoras = home.obterEntidadePorApelido("itendenteicoras");
			
			if(icoras!=null)
			{
				if(usuarioAtual.obterSuperioresMap().containsKey(icoras.obterId()))
				{
					if(nivel.equals(Usuario.INTENDENTE_ICORAS) || nivel.equals(Usuario.DIVISAO_CONTROL_AUXILIARES))
						retorno = true;
				}
				else
					retorno = super.permiteAtualizar();
			}
			else
				retorno = super.permiteAtualizar();
		}
		
		return retorno;
	}
	
	public String permiteAtualizarStr() throws Exception
	{
		Usuario usuarioAtual = this.obterUsuarioAtual();
		
		String retorno = "";
		
		String nivel = usuarioAtual.obterNivel();
		
		if(nivel.equals(Usuario.ADMINISTRADOR))
			retorno += "Nivel admin = True\n";
		else
		{
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
			Entidade icoras = home.obterEntidadePorApelido("itendenteicoras");
			
			if(icoras!=null)
			{
				retorno += "Achou departamento ICORAS id: "+icoras.obterId()+"\n";
				retorno += "Superiores:\n";
				
				Map<Long,Entidade> superioresMap = usuarioAtual.obterSuperioresMap();
				
				for(Iterator<Entidade> i = superioresMap.values().iterator() ; i.hasNext() ; )
				{
					Entidade e = i.next();
					
					retorno+=e.obterNome() + " id: "+e.obterId() + "\n";
				}
				
				if(superioresMap.containsKey(icoras.obterId()))
				{
					if(nivel.equals(Usuario.INTENDENTE_ICORAS) || nivel.equals(Usuario.DIVISAO_CONTROL_AUXILIARES))
						retorno += "Nivel INTENDENTE_ICORAS ou DIVISAO_CONTROL_AUXILIARES\n";
				}
				else
					retorno += "Usuário atual não tem como Superior o ICORAS\n";
			}
			else
				retorno += "Não Achou departamento ICORAS\n";
		}
		
		return retorno;
	}
	
	public boolean permiteAtualizarEndereco() throws Exception
	{
		//Entidade icoras = home.obterEntidadePorApelido("jefedivisioncontrolicoras");
		
		Usuario usuarioAtual = this.obterUsuarioAtual();
		
		boolean retorno = false;
		
		if(usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			retorno = true;
		else
		{
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
			Entidade icoras = home.obterEntidadePorApelido("itendenteicoras");
			
			if(icoras!=null)
			{
				if(usuarioAtual.obterSuperiores().contains(icoras))
				{
					if(usuarioAtual.obterNivel().equals(Usuario.INTENDENTE_ICORAS) || usuarioAtual.obterNivel().equals(Usuario.DIVISAO_CONTROL_AUXILIARES) || usuarioAtual.obterNivel().equals(Usuario.ANALISTA_AUXILIARES))
						retorno = true;
				}
				else
					retorno = super.permiteAtualizar();
			}
			else
				retorno = super.permiteAtualizar();
		}
		
		return retorno;
	}
	
	public boolean permiteExcluir() throws Exception
	{
		boolean retorno = true;
		
		if(this.obterInscricoes().size() > 0)
			retorno = false;
		/*else
		{
			if(this.obterInferiores().size() > 0)
				retorno = false;
			else
			{
				SQLQuery query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from evento,apolice where evento.id = apolice.id and agente = ?");
				query.addLong(this.obterId());
				
				if(query.executeAndGetFirstRow().getInt("qtde") > 0)
					retorno = false;
				else
				{
					SQLQuery query2 = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from evento,sinistro where evento.id = sinistro.id and agente = ?");
					query2.addLong(this.obterId());
					
					if(query2.executeAndGetFirstRow().getInt("qtde") > 0)
						retorno = false;
					else
					{
						SQLQuery query3 = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from evento,registro_gastos where evento.id = registro_gastos.id and agente = ?");
						query3.addLong(this.obterId());
						
						if(query3.executeAndGetFirstRow().getInt("qtde") > 0)
							retorno = false;
					}
				}
			}
		}*/
		
		return retorno;
	}
}