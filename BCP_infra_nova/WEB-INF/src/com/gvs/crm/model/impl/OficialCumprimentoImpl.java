package com.gvs.crm.model.impl;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.OficialCumprimento;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class OficialCumprimentoImpl extends EntidadeImpl implements
		OficialCumprimento {
	public void atualizarAseguradora(Aseguradora aseguradora) throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select id from oficial where id = ?");
		query.addLong(this.obterId());

		if (query.execute().length == 0) {
			SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
					"insert into oficial (id,aseguradora) values(?, ?)");
			insert.addLong(this.obterId());
			insert.addLong(aseguradora.obterId());

			insert.execute();
		} else {
			SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
					"update oficial set aseguradora = ? where id = ?");
			update.addLong(aseguradora.obterId());
			update.addLong(this.obterId());

			update.execute();
		}
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into oficial(id) values(?)");
		insert.addLong(this.obterId());

		insert.execute();
	}

	public Aseguradora obterAseguradora() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select aseguradora from entidade,oficial where entidade.id=oficial.id and oficial.id = ?");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("aseguradora");

		Aseguradora aseguradora = null;

		if (id > 0) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			aseguradora = (Aseguradora) home.obterEntidadePorId(id);
		}

		return aseguradora;
	}
	
	public boolean permiteAtualizar() throws Exception
	{
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		Entidade lavado = home.obterEntidadePorApelido("divisionlavadodinero");
		
		if(lavado!=null)
		{
			if(this.obterUsuarioAtual().obterSuperiores().contains(lavado))
				return true;
			else
				return false;
		}
		else
			return super.permiteAtualizar();
	}
}