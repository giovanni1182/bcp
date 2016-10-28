package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.AgendaMeicos;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.MeicosAseguradora;
import com.gvs.crm.model.MeicosCalculo;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class AgendaMeicosImpl extends EventoImpl implements AgendaMeicos
{
	public Collection obterEventoMeicos() throws Exception 
	{
		Map<String, MeicosAseguradora> meicos = new TreeMap<String, MeicosAseguradora>();

		for (Iterator i = this.obterInferiores().iterator(); i.hasNext();) 
		{
			Evento e = (Evento) i.next();

			if (e instanceof MeicosAseguradora) 
				meicos.put(e.obterOrigem().obterId()+e.obterTipo(), (MeicosAseguradora) e);
		}

		return meicos.values();
	}

	public void incluir() throws Exception 
	{
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate(
				"insert into agenda_meicos(id) values(?)");
		insert.addLong(this.obterId());

		insert.execute();
	}

	public void atualizarIPC(double ipc) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update agenda_meicos set ipc_ultimo_ano = ? where id = ?");
		update.addDouble(ipc);
		update.addLong(this.obterId());

		update.execute();
	}

	public double obterIPC() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select ipc_ultimo_ano from agenda_meicos where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("ipc_ultimo_ano");
	}

	public void atualizarIPC3Anos(double ipc) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update agenda_meicos set ipc_tres_anos = ? where id = ?");
		update.addDouble(ipc);
		update.addLong(this.obterId());

		update.execute();
	}

	public double obterIPC3Anos() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select ipc_tres_anos from agenda_meicos where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("ipc_tres_anos");
	}

	public Collection obterCalculoMeicos(String tipo) throws Exception {
		Collection calculos = new ArrayList();

		for (Iterator i = this.obterInferiores().iterator(); i.hasNext();) {
			Evento e = (Evento) i.next();

			if (e instanceof MeicosAseguradora) {
				for (Iterator j = e.obterInferiores().iterator(); j.hasNext();) {
					Evento e2 = (Evento) i.next();

					if (e2 instanceof MeicosCalculo)
						if (e2.obterTipo().equals(tipo))
							calculos.add(e2);
				}
			}
		}

		return calculos;
	}

	public void excluirCalculoMeicos() throws Exception {
		Collection calculos = new ArrayList();

		for (Iterator i = this.obterInferiores().iterator(); i.hasNext();)
		{
			Evento e = (Evento) i.next();

			if (e instanceof MeicosAseguradora) 
			{
				for (Iterator j = e.obterInferiores().iterator(); j.hasNext();) 
				{
					Evento e2 = (Evento) j.next();

					if (e2 instanceof MeicosCalculo)
						e2.excluir();
				}
			}
			else if(e instanceof MeicosCalculo)
				e.excluir();
		}
	}
	
	public boolean permiteAtualizar() throws Exception
	{
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		Entidade icf = home.obterEntidadePorApelido("intendenteicf");
		
		if(icf!=null)
		{
			if(this.obterUsuarioAtual().obterSuperiores().contains(icf) || this.obterUsuarioAtual().obterId() == 1)
				return true;
			else
				return false;
		}
		else
			return super.permiteAtualizar();
	}
	
	public double obterValorQualificacao(Aseguradora aseguradora) throws Exception
	{
		MeicosCalculo calculo = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select valor from evento,meicos_calculo where origem=? and superior =? and tipo ='Resultado da Classificacao'");
		query.addLong(aseguradora.obterId());
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getDouble("valor");
	}
	
}