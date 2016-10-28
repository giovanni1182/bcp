package com.gvs.crm.model.impl;

import java.util.Date;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Inspecao;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class InspecaoImpl extends EventoImpl implements Inspecao
{
	private Entidade inspetor;
	
	public void atribuirInspetor(Entidade inspetor)
	{
		this.inspetor = inspetor;
	}
	
	public void atualizarInspetor(Entidade inspetor) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update inspecao set inspetor=? where id=?");
		update.addLong(inspetor.obterId());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDiasCorridos(String dias) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update inspecao set dias_corridos=? where id=?");
		update.addString(dias);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDataInicioReal(Date data) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update inspecao set data_inicio_real=? where id=?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDataTerminoReal(Date data) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update inspecao set data_termino_real=? where id=?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());
		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into inspecao(id) values(?)");
		insert.addLong(this.obterId());
		insert.execute();
	}

	public Entidade obterInspetor() throws Exception
	{
		if(this.inspetor == null)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select inspetor from inspecao where id=?");
			query.addLong(this.obterId());
	
			long id = query.executeAndGetFirstRow().getLong("inspetor"); 
	
			if (id > 0)
			{
				EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
	
				inspetor = home.obterEntidadePorId(id);
			}
		}
		return inspetor;
	}

	public String obterDiasCorridos() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select dias_corridos from inspecao where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("dias_corridos");
	}

	public Date obterDataInicioReal() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_inicio_real from inspecao where id=?");
		query.addLong(this.obterId());

		Date data = null;

		long dataLong = query.executeAndGetFirstRow().getLong(
				"data_inicio_real");

		if (dataLong > 0)
			data = new Date(dataLong);

		return data;
	}

	public Date obterDataTerminoReal() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_termino_real from inspecao where id=?");
		query.addLong(this.obterId());

		Date data = null;

		long dataLong = query.executeAndGetFirstRow().getLong(
				"data_termino_real");

		if (dataLong > 0)
			data = new Date(dataLong);

		return data;
	}

}