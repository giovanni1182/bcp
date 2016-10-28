package com.gvs.crm.model.impl;

import java.util.HashMap;
import java.util.Map;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.GrupoMensagem;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class GrupoMensagemImpl extends EntidadeImpl implements GrupoMensagem {
	public class MembroImpl implements Membro {
		private EntidadeImpl entidade;

		private Entidade membro;

		MembroImpl(EntidadeImpl entidade, Entidade membro) {
			this.entidade = entidade;
			this.membro = membro;
		}

		public Entidade obterEntidade() throws Exception {
			return this.entidade;
		}

		public Entidade obterMembro() throws Exception {
			return this.membro;
		}

		public String obterEmail() throws Exception {
			Entidade.Atributo email = (Entidade.Atributo) this.membro
					.obterAtributo("email");

			String emailStr = "";

			if (email != null)
				emailStr = this.membro.obterAtributo("email").obterValor();

			return emailStr;
		}
	}

	private Map membros = new HashMap();;

	public Map obterMembros() throws Exception {

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select entidade.nome,entidade.id,membro_id from entidade,grupo where entidade.id=grupo.id and grupo.id=?");

		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			long membroId = rows[i].getLong("membro_id");

			Entidade membro = home.obterEntidadePorId(membroId);

			this.membros.put(new Long(membroId), new MembroImpl(this, membro));
		}

		return this.membros;
	}

	public Entidade obterMembro(Entidade membro) throws Exception {
		if (this.membros == null)
			this.obterMembros();
		return (Entidade) this.membros.get(new Long(membro.obterId()));
	}

	public void adicionarMembro(Entidade membro) throws Exception {
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into grupo (id, membro_id) values (?, ?)");
		insert.addLong(this.obterId());
		insert.addLong(membro.obterId());
		insert.execute();
	}

	public void removerMembro(Entidade membro) throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate("crm",
				"delete from grupo where id=? and membro_id=?");
		delete.addLong(this.obterId());
		delete.addLong(membro.obterId());
		delete.execute();
		if (this.membros != null)
			this.membros.remove(Long.toString(membro.obterId()));
	}

	public void excluir() throws Exception {
		super.excluir();
		SQLUpdate delete = this.getModelManager().createSQLUpdate("crm",
				"delete from grupo where id=?");
		delete.addLong(this.obterId());
		delete.execute();
	}
}