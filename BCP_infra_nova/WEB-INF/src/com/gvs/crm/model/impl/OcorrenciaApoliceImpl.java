package com.gvs.crm.model.impl;

import java.util.Date;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.OcorrenciaApolice;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class OcorrenciaApoliceImpl extends EventoImpl implements
		OcorrenciaApolice {
	public void atualizarApolice(Apolice apolice) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update ocorrencia_apolice set apolice=? where id=?");
		update.addLong(apolice.obterId());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarExpediente(String expediente) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update ocorrencia_apolice set expediente=? where id=?");
		update.addString(expediente);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDataSuspeita(Date data) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update ocorrencia_apolice set data_suspeita=? where id=?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDataReporte(Date data) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update ocorrencia_apolice set data_reporte=? where id=?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarNumeroConta(String numeroConta) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update ocorrencia_apolice set numero_conta=? where id=?");
		update.addString(numeroConta);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarEntidade(String entidade) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update ocorrencia_apolice set entidade=? where id=?");
		update.addString(entidade);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarTitular(String titular) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update ocorrencia_apolice set titular=? where id=?");
		update.addString(titular);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarEndereco(String endereco) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update ocorrencia_apolice set endereco=? where id=?");
		update.addString(endereco);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarBairro(String bairro) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update ocorrencia_apolice set bairro=? where id=?");
		update.addString(bairro);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarTelefone(String telefone) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update ocorrencia_apolice set telefone=? where id=?");
		update.addString(telefone);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarCidade(String cidade) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update ocorrencia_apolice set cidade=? where id=?");
		update.addString(cidade);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarPais(String pais) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update ocorrencia_apolice set pais=? where id=?");
		update.addString(pais);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarRazao(String razao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update ocorrencia_apolice set razao=? where id=?");
		update.addString(razao);
		update.addLong(this.obterId());
		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into ocorrencia_apolice(id) values(?)");
		insert.addLong(this.obterId());
		insert.execute();
	}

	public Apolice obterApolice() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select apolice from ocorrencia_apolice where id=?");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("apolice");

		Apolice apolice = null;

		if (id > 0) {
			EventoHome home = (EventoHome) this.getModelManager().getHome(
					"EventoHome");

			apolice = (Apolice) home.obterEventoPorId(id);
		}

		return apolice;
	}

	public String obterExpediente() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select expediente from ocorrencia_apolice where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("expediente");
	}

	public Date obterDataSuspeita() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_suspeita from ocorrencia_apolice where id=?");
		query.addLong(this.obterId());

		long dataLong = query.executeAndGetFirstRow().getLong("data_suspeita");

		Date data = null;

		if (dataLong > 0)
			data = new Date(dataLong);

		return data;
	}

	public Date obterDataReporte() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_reporte from ocorrencia_apolice where id=?");
		query.addLong(this.obterId());

		long dataLong = query.executeAndGetFirstRow().getLong("data_reporte");

		Date data = null;

		if (dataLong > 0)
			data = new Date(dataLong);

		return data;
	}

	public String obterNumeroConta() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select numero_conta from ocorrencia_apolice where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("numero_conta");
	}

	public String obterEntidade() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select entidade from ocorrencia_apolice where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("entidade");
	}

	public String obterTitular() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select titular from ocorrencia_apolice where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("titular");
	}

	public String obterCidade() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select cidade from ocorrencia_apolice where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("cidade");
	}

	public String obterPais() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select pais from ocorrencia_apolice where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("pais");
	}

	public String obterRazao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select razao from ocorrencia_apolice where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("razao");
	}

	public String obterEndereco() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select endereco from ocorrencia_apolice where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("endereco");
	}

	public String obterBairro() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select bairro from ocorrencia_apolice where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("bairro");
	}

	public String obterTelefone() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select telefone from ocorrencia_apolice where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("telefone");
	}
}