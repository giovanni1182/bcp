package com.gvs.crm.model.impl;

import com.gvs.crm.model.Renovacao;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

public class RenovacaoImpl extends EventoImpl implements Renovacao {
	public void atualizarMatriculaAnterior(int arg) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update renovacao set matricula_anterior=? where id=?");
		update.addInt(arg);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarCertificadoAntecedentes(int arg) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update renovacao set certificado_antecedentes=? where id=?");
		update.addInt(arg);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarCertificadoJudicial(int arg) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update renovacao set cetificado_judicial=? where id=?");
		update.addInt(arg);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarCertificadoTributario(int arg) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update renovacao set certificado_tributario=? where id=?");
		update.addInt(arg);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDeclaracao(int arg) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update renovacao set declaracao=? where id=?");
		update.addInt(arg);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarComprovanteMatricula(int arg) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update renovacao set comprovante_matricula=? where id=?");
		update.addInt(arg);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarApoliceSeguro(int arg) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update renovacao set apolice_seguro=? where id=?");
		update.addInt(arg);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarLivro(int arg) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update renovacao set livro=? where id=?");
		update.addInt(arg);
		update.addLong(this.obterId());
		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into renovacao(id) values(?)");
		insert.addLong(this.obterId());
		insert.execute();
	}

	public boolean obterMatriculaAnterior() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select matricula_anterior from renovacao where id=?");
		query.addLong(this.obterId());

		if (query.executeAndGetFirstRow().getInt("matricula_anterior") == 0)
			return false;
		else
			return true;
	}

	public boolean obterCertificadoAntecedentes() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select certificado_antecedentes from renovacao where id=?");
		query.addLong(this.obterId());

		if (query.executeAndGetFirstRow().getInt("certificado_antecedentes") == 0)
			return false;
		else
			return true;
	}

	public boolean obterCertificadoJudicial() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select cetificado_judicial from renovacao where id=?");
		query.addLong(this.obterId());

		if (query.executeAndGetFirstRow().getInt("cetificado_judicial") == 0)
			return false;
		else
			return true;
	}

	public boolean obterCertificadoTributario() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select certificado_tributario from renovacao where id=?");
		query.addLong(this.obterId());

		if (query.executeAndGetFirstRow().getInt("certificado_tributario") == 0)
			return false;
		else
			return true;
	}

	public boolean obterDeclaracao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select declaracao from renovacao where id=?");
		query.addLong(this.obterId());

		if (query.executeAndGetFirstRow().getInt("declaracao") == 0)
			return false;
		else
			return true;
	}

	public boolean obterComprovanteMatricula() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select comprovante_matricula from renovacao where id=?");
		query.addLong(this.obterId());

		if (query.executeAndGetFirstRow().getInt("comprovante_matricula") == 0)
			return false;
		else
			return true;
	}

	public boolean obterApoliceSeguro() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select apolice_seguro from renovacao where id=?");
		query.addLong(this.obterId());

		if (query.executeAndGetFirstRow().getInt("apolice_seguro") == 0)
			return false;
		else
			return true;
	}

	public boolean obterLivro() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select livro from renovacao where id=?");
		query.addLong(this.obterId());

		if (query.executeAndGetFirstRow().getInt("livro") == 0)
			return false;
		else
			return true;
	}
}