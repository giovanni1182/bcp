package com.gvs.crm.model.impl;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Pessoa;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class PessoaImpl extends EntidadeImpl implements Pessoa {
	private Map formacoes;

	public class FormacaoImpl implements Formacao {
		private PessoaImpl pessoa;

		private int id;

		private String instituicao;

		private String curso;

		private String tipo;

		private Date dataInicio;

		private Date dataFim;

		private String experiencia;

		private String tipoEducacao;

		private String cargaHoraria;

		FormacaoImpl(PessoaImpl pessoa, int id, String instituicao,
				String curso, String cargaHoraria, String tipo,
				String tipoEducacao, Date dataInicio, Date dataFim,
				String experiencia) {
			this.pessoa = pessoa;
			this.id = id;
			this.instituicao = instituicao;
			this.curso = curso;
			this.cargaHoraria = cargaHoraria;
			this.tipo = tipo;
			this.tipoEducacao = tipoEducacao;
			this.dataInicio = dataInicio;
			this.dataFim = dataFim;
			this.experiencia = experiencia;
		}

		public void atualizarFormacao(String instituicao, String curso,
				String cargaHoraria, String tipo, String tipoEducacao,
				Date dataInicio, Date dataFim, String experiencia)
				throws Exception {
			SQLUpdate update = this.pessoa
					.getModelManager()
					.createSQLUpdate(
							"update pessoa_formacao set instituicao=?, curso=?, carga_horaria=?, tipo=?, tipo_educacao=?, data_inicio=?, data_fim=?, experiencia=? where entidade=? and id=?");
			update.addString(instituicao);
			update.addString(curso);
			update.addString(cargaHoraria);
			update.addString(tipo);
			update.addString(tipoEducacao);
			update.addLong(dataInicio.getTime());
			update.addLong(dataFim.getTime());
			update.addString(experiencia);
			update.addLong(this.pessoa.obterId());
			update.addInt(this.id);
			update.execute();
		}

		public Pessoa obterPessoa() throws Exception {
			return this.pessoa;
		}

		public int obterId() throws Exception {
			return this.id;
		}

		public String obterInstituicao() throws Exception {
			return this.instituicao;
		}

		public String obterCurso() throws Exception {
			return this.curso;
		}

		public String obterTipo() throws Exception {
			return this.tipo;
		}

		public Date obterDataInicio() throws Exception {
			return this.dataInicio;
		}

		public Date obterDataFim() throws Exception {
			return this.dataFim;
		}

		public String obterExperiencia() throws Exception {
			return this.experiencia;
		}

		public String obterCargaHoraria() throws Exception {
			return this.cargaHoraria;
		}

		public String obterTipoEducacao() throws Exception {
			return this.tipoEducacao;
		}
	}

	public void adicionarFormacao(String instituicao, String curso,
			String cargaHoraria, String tipo, String tipoEducacao,
			Date dataInicio, Date dataFim, String experiencia) throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select MAX(Id) as MX from pessoa_formacao where entidade=?");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"crm",
						"insert into pessoa_formacao (entidade, id, instituicao, curso, carga_horaria, tipo, tipo_educacao, data_inicio, data_fim, experiencia) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addLong(id);
		insert.addString(instituicao);
		insert.addString(curso);
		insert.addString(cargaHoraria);
		insert.addString(tipo);
		insert.addString(tipoEducacao);
		insert.addLong(dataInicio.getTime());
		insert.addLong(dataFim.getTime());
		insert.addString(experiencia);

		insert.execute();
	}

	public Formacao obterFormacao(int id) throws Exception {
		this.obterFormacoes();
		return (Formacao) this.formacoes.get(Integer.toString(id));
	}

	public Collection obterFormacoes() throws Exception {
		this.formacoes = new TreeMap();
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select * from pessoa_formacao where entidade=? order by instituicao");
		query.addLong(this.obterId());
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			int id = rows[i].getInt("id");
			this.formacoes.put(Integer.toString(id), new FormacaoImpl(this, id,
					rows[i].getString("instituicao"), rows[i]
							.getString("curso"), rows[i]
							.getString("carga_horaria"), rows[i]
							.getString("tipo"), rows[i]
							.getString("tipo_educacao"), new Date(rows[i]
							.getLong("data_inicio")), new Date(rows[i]
							.getLong("data_fim")), rows[i]
							.getString("experiencia")));
		}
		return this.formacoes.values();
	}

	public void removerFormacao(Formacao formacao) throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate(
				"delete from pessoa_formacao where entidade=? and id=?");
		delete.addLong(this.obterId());
		delete.addInt(formacao.obterId());
		delete.execute();
		if (this.formacoes != null)
			this.formacoes.remove(Integer.toString(formacao.obterId()));
	}
}