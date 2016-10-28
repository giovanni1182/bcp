package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeDocumento;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Inscricao;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class DocumentoProdutoImpl extends EventoImpl implements
		DocumentoProduto {
	public class PessoaImpl implements Pessoa {

		private DocumentoProdutoImpl documento;

		private int seq;

		private Entidade pessoa;

		private String tipo;

		private int posicao;

		public PessoaImpl(DocumentoProdutoImpl documento, int seq,
				Entidade pessoa, String tipo, int posicao) throws Exception {
			this.documento = documento;
			this.seq = seq;
			this.pessoa = pessoa;
			this.tipo = tipo;
			this.posicao = posicao;
		}

		public DocumentoProduto obterDocumento() throws Exception {
			return this.documento;
		}

		public int obterSeq() throws Exception {
			return this.seq;
		}

		public Entidade obterPessoa() throws Exception {
			return this.pessoa;
		}

		public String obterTipo() throws Exception {
			return this.tipo;
		}

		public int obterPosicao() throws Exception {
			return this.posicao;
		}
	}

	public void atualizarDocumento(EntidadeDocumento documento)
			throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update documento_produto set documento=? where id=?");
		update.addLong(documento.obterId());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarNumero(String numero) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update documento_produto set numero=? where id=?");
		update.addString(numero);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarTexto(String texto) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update documento_produto set texto=? where id=?");
		update.addString(texto);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarReferente(String referente) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update documento_produto set ref=? where id=?");
		update.addString(referente);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarAnalista(Entidade analista) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update documento_produto set analista=? where id=?");
		update.addLong(analista.obterId());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarChefeDivisao(Entidade chefeDivisao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update documento_produto set chefe=? where id=?");
		update.addLong(chefeDivisao.obterId());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarIntendente(Entidade intendente) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update documento_produto set intendente=? where id=?");
		update.addLong(intendente.obterId());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarSuperIntendente(Entidade superIntendente)
			throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update documento_produto set superintendente=? where id=?");
		update.addLong(superIntendente.obterId());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarTituloDocumento(String tituloDocumento)
			throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update documento_produto set titulo_documento=? where id=?");
		update.addString(tituloDocumento);
		update.addLong(this.obterId());
		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into documento_produto(id) values(?)");
		insert.addLong(this.obterId());
		insert.execute();
	}

	public void exclurInscricaoVinculada(Inscricao inscricao) throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate("crm",
				"delete evento_entidades where id=? and sub_evento=?");
		delete.addLong(this.obterId());
		delete.addLong(inscricao.obterId());
		delete.execute();
	}

	public Collection obterInscricoesVinculadas() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select sub_evento from evento_entidades where id=?");
		query.addLong(this.obterId());

		Collection inscricoes = new ArrayList();

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			EventoHome home = (EventoHome) this.getModelManager().getHome(
					"EventoHome");

			inscricoes
					.add(home.obterEventoPorId(rows[i].getLong("sub_evento")));
		}

		return inscricoes;
	}

	public void adicionarInscricaoVinculado(Inscricao inscricao)
			throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select MAX(seq) as MX from evento_entidades where id=?");
		query.addLong(this.obterId());

		int id = query.executeAndGetFirstRow().getInt("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate("crm",
						"insert into evento_entidades(id, seq, sub_evento) values (?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addInt(id);
		insert.addLong(inscricao.obterId());
		insert.execute();
	}

	public EntidadeDocumento obterDocumento() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select documento from documento_produto where id=?");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("documento");

		EntidadeDocumento documento = null;

		if (id > 0) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			documento = (EntidadeDocumento) home.obterEntidadePorId(id);
		}

		return documento;
	}

	public String obterNumero() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select numero from documento_produto where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("numero");

	}

	public String obterTituloDocumento() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select titulo_documento from documento_produto where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("titulo_documento");

	}

	public String obterReferente() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select ref from documento_produto where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("ref");

	}

	public Entidade obterAnalista() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select analista from documento_produto where id=?");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("analista");

		Entidade analista = null;

		if (id > 0) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			analista = home.obterEntidadePorId(id);
		}

		return analista;
	}

	public Entidade obterChefeDivisao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select chefe from documento_produto where id=?");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("chefe");

		Entidade chefe = null;

		if (id > 0) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			chefe = home.obterEntidadePorId(id);
		}

		return chefe;
	}

	public Entidade obterIntendente() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select intendente from documento_produto where id=?");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("intendente");

		Entidade intendente = null;

		if (id > 0) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			intendente = home.obterEntidadePorId(id);
		}

		return intendente;
	}

	public Entidade obterSuperIntendente() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select superintendente from documento_produto where id=?");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("superintendente");

		Entidade superIntendente = null;

		if (id > 0) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			superIntendente = home.obterEntidadePorId(id);
		}

		return superIntendente;
	}

	public String obterTexto() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select texto from documento_produto where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("texto");
	}

	public void adicionarNovaPessoa(Entidade pessoa, String tipo, int posicao)
			throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select MAX(seq) as MX from documento_pessoas where id=?");
		query.addLong(this.obterId());

		int id = query.executeAndGetFirstRow().getInt("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"crm",
						"insert into documento_pessoas(id, seq, pessoa, tipo, posicao) values (?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addInt(id);
		insert.addLong(pessoa.obterId());
		insert.addString(tipo);
		insert.addInt(posicao);
		insert.execute();

		//System.out.println("insert into documento_pessoas(id, seq, pessoa,
		// tipo, posicao) values ("+this.obterId()+", "+id+",
		// "+pessoa.obterId()+", "+tipo+", "+posicao+")");
	}

	public Collection obterPessoasAntes() throws Exception {
		Map pessoas = new TreeMap();

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select pessoa,tipo,posicao from documento_pessoas where id=? and posicao=0");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		int j = 1;

		for (int i = 0; i < rows.length; i++) {
			int seq = rows[i].getInt("seq");

			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			Entidade pessoa = home
					.obterEntidadePorId(rows[i].getLong("pessoa"));

			String tipo = rows[i].getString("tipo");

			int posicao = rows[i].getInt("posicao");

			pessoas.put(tipo + j, new PessoaImpl(this, seq, pessoa, tipo,
					posicao));

			j++;
		}

		return pessoas.values();
	}

	public Collection obterPessoasDepois() throws Exception {
		Map pessoas = new TreeMap();

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select pessoa,tipo,posicao from documento_pessoas where id=? and posicao=1");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		int j = 1;

		for (int i = 0; i < rows.length; i++) {
			int seq = rows[i].getInt("seq");

			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			Entidade pessoa = home
					.obterEntidadePorId(rows[i].getLong("pessoa"));

			String tipo = rows[i].getString("tipo");

			int posicao = rows[i].getInt("posicao");

			pessoas.put(tipo + j, new PessoaImpl(this, seq, pessoa, tipo,
					posicao));

			j++;
		}

		return pessoas.values();
	}

	public Collection obterTiposPessoas() throws Exception {
		Collection tipos = new ArrayList();

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo from documento_pessoas group by tipo");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++)
			tipos.add(rows[i].getString("tipo"));

		return tipos;
	}

	public void excluirPessoaDocumento(Entidade pessoa, int posicao)
			throws Exception {
		SQLUpdate delete = this
				.getModelManager()
				.createSQLUpdate("crm",
						"delete documento_pessoas where id=? and pessoa=? and posicao=?");
		delete.addLong(this.obterId());
		delete.addLong(pessoa.obterId());
		delete.addInt(posicao);
		delete.execute();
	}
}