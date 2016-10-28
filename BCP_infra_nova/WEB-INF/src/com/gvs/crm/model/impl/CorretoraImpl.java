package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Corretora;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Usuario;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class CorretoraImpl extends EntidadeImpl implements Corretora {
	public void atualizarAseguradora(Aseguradora aseguradora) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update corretora set aseguradora=? where id=?");
		update.addLong(aseguradora.obterId());
		update.addLong(this.obterId());
		update.execute();
	}

	public Aseguradora obterAseguradora() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select aseguradora from corretora where id=?");
		query.addLong(this.obterId());

		Aseguradora aseguradora = null;

		long id = query.executeAndGetFirstRow().getLong("aseguradora");

		if (id > 0) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			aseguradora = (Aseguradora) home.obterEntidadePorId(id);
		}

		return aseguradora;
	}

	public void adicionarNovoRamo(String ramo) throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select MAX(id) as MX from corretora_ramo where entidade=?");
		query.addLong(this.obterId());

		int id = query.executeAndGetFirstRow().getInt("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate("crm",
						"insert into corretora_ramo(entidade, id, nome) values (?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addInt(id);
		insert.addString(ramo);
		insert.execute();
	}

	public void atualizarRamo(String ramo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update corretora set ramo=? where id=?");
		update.addString(ramo);
		update.addLong(this.obterId());
		update.execute();
	}

	public String obterRamo() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select ramo from corretora where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("ramo");
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into corretora(id) values(?)");
		insert.addLong(this.obterId());
		insert.execute();
	}

	public Collection obterNomeRamos() throws Exception {
		Collection nomeRamos = new ArrayList();

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select nome from corretora_ramo group by nome");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++)
			nomeRamos.add(rows[i].getString("nome"));

		return nomeRamos;
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
				SQLQuery query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from evento,dados_reaseguro where evento.id = dados_reaseguro.id and corretora = ?");
				query.addLong(this.obterId());
				
				if(query.executeAndGetFirstRow().getInt("qtde") > 0)
					retorno = false;
				else
				{
					SQLQuery query2 = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from evento,apolice where evento.id = apolice.id and corredor = ?");
					query2.addLong(this.obterId());
					
					if(query2.executeAndGetFirstRow().getInt("qtde") > 0)
						retorno = false;
				}
			}
		}*/
		
		return retorno;
	}
	
	public boolean permiteAtualizar() throws Exception
	{
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
					if(usuarioAtual.obterNivel().equals(Usuario.INTENDENTE_ICORAS) || usuarioAtual.obterNivel().equals(Usuario.DIVISAO_CONTROL_REASEGUROS))
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
}