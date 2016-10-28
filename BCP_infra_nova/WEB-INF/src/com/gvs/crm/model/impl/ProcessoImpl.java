package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Processo;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class ProcessoImpl extends EventoImpl implements Processo {
	private double valorAcao;

	private Map pessoas;

	public class PessoaImpl implements Pessoa {

		private ProcessoImpl processo;

		private int id;

		private Entidade pessoa;

		private String tipo;

		PessoaImpl(ProcessoImpl processo, int id, Entidade pessoa, String tipo)
				throws Exception {
			this.processo = processo;
			this.id = id;
			this.pessoa = pessoa;
			this.tipo = tipo;
		}

		public ProcessoImpl obterProcesso() throws Exception {
			return this.processo;
		}

		public int obterId() throws Exception {
			return this.id;
		}

		public Entidade obterPessoa() throws Exception {
			return this.pessoa;
		}

		public String obterTipo() throws Exception {
			return this.tipo;

		}
	}

	public void atualizarExpediente(String expediente) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update processo set expediente=? where id=?");
		update.addString(expediente);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarValorAcao(double valorAcao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update processo set valor_acao=? where id=?");
		update.addDouble(valorAcao);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarJulgado(String julgado) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update processo set julgado=? where id=?");
		update.addString(julgado);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarJuiz(String juiz) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update processo set juiz=? where id=?");
		update.addString(juiz);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarSecretaria(String secretaria) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update processo set secretaria=? where id=?");
		update.addString(secretaria);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarFiscal(String fiscal) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update processo set fiscal=? where id=?");
		update.addString(fiscal);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarTurno(String turno) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update processo set turno=? where id=?");
		update.addString(turno);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarCargo(String cargo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update processo set cargo=? where id=?");
		update.addString(cargo);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarObjeto(String objeto) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update processo set objeto=? where id=?");
		update.addString(objeto);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarCircunscricao(String circunscricao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update processo set circunscricao=? where id=?");
		update.addString(circunscricao);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarForum(String forum) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update processo set forum=? where id=?");
		update.addString(forum);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDataDemanda(Date dataDemanda) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update processo set data_demanda=? where id=?");
		update.addLong(dataDemanda.getTime());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarSentenca(String sentenca) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update processo set sentenca=? where id=?");
		update.addString(sentenca);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDataCancelamento(Date dataCancelamento)
			throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update processo set data_cancelamento=? where id=?");
		update.addLong(dataCancelamento.getTime());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarOrigem(Entidade origem) throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select MAX(seq) as MX from evento_entidades where id=?");
		query.addLong(this.obterId());

		int seq = query.executeAndGetFirstRow().getInt("MX") + 1;

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into evento_entidades(id,seq,origem) values(?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addInt(seq);
		insert.addLong(origem.obterId());
		insert.execute();
	}

	public Collection obterOrigens() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select origem from evento_entidades where id=?");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		Collection origens = new ArrayList();

		for (int i = 0; i < rows.length; i++) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			origens.add(home.obterEntidadePorId(rows[i].getLong("origem")));
		}

		return origens;
	}

	public void excluirOrigem(Entidade origem) throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate("crm",
				"delete from evento_entidades where id=? and origem=?");
		delete.addLong(this.obterId());
		delete.addLong(origem.obterId());
		delete.execute();
	}

	public void atualizarResponsaveis(Entidade responsavel) throws Exception {

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select MAX(seq) as MX from evento_entidades where id=?");
		query.addLong(this.obterId());

		int seq = query.executeAndGetFirstRow().getInt("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate("crm",
						"insert into evento_entidades(id,seq,responsavel) values(?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addInt(seq);
		insert.addLong(responsavel.obterId());
		insert.execute();
	}

	public Collection obterResponsaveis() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select responsavel from evento_entidades where id=?");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		Collection responsaveis = new ArrayList();

		for (int i = 0; i < rows.length; i++) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			responsaveis.add(home.obterEntidadePorId(rows[i]
					.getLong("responsavel")));
		}

		return responsaveis;
	}

	public void excluirResponsavel(Entidade responsavel) throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate("crm",
				"delete from evento_entidades where id=? and responsavel=?");
		delete.addLong(this.obterId());
		delete.addLong(responsavel.obterId());
		delete.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate(
				"insert into processo(id) values (?)");
		insert.addLong(this.obterId());
		insert.execute();
	}

	public String obterExpediente() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select expediente from processo where id=?");
		query.addLong(this.obterId());

		String expediente = query.executeAndGetFirstRow().getString(
				"expediente");

		return expediente;
	}

	public double obterValorAcao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select valor_acao from processo where id=?");
		query.addLong(this.obterId());
		this.valorAcao = query.executeAndGetFirstRow().getDouble("valor_acao");
		return this.valorAcao;
	}

	public String obterJulgado() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select julgado from processo where id=?");
		query.addLong(this.obterId());

		String julgado = query.executeAndGetFirstRow().getString("julgado");

		return julgado;
	}

	public String obterJuiz() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select juiz from processo where id=?");
		query.addLong(this.obterId());

		String juiz = query.executeAndGetFirstRow().getString("juiz");

		return juiz;
	}

	public String obterSecretaria() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select secretaria from processo where id=?");
		query.addLong(this.obterId());

		String secretaria = query.executeAndGetFirstRow().getString(
				"secretaria");

		return secretaria;
	}

	public String obterFiscal() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select fiscal from processo where id=?");
		query.addLong(this.obterId());

		String fiscal = query.executeAndGetFirstRow().getString("fiscal");

		return fiscal;
	}

	public String obterTurno() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select turno from processo where id=?");
		query.addLong(this.obterId());

		String turno = query.executeAndGetFirstRow().getString("turno");

		return turno;
	}

	public String obterCargo() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select cargo from processo where id=?");
		query.addLong(this.obterId());

		String cargo = query.executeAndGetFirstRow().getString("cargo");

		return cargo;
	}

	public Pessoa obterPessoa(int id) throws Exception {
		this.obterPessoas();
		return (Pessoa) this.pessoas.get(new Integer(id));
	}

	public Collection obterPessoas() throws Exception {
		this.pessoas = new TreeMap();

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from processo_pessoas where id=?");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			long pessoaId = rows[i].getLong("pessoa");

			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			Entidade pessoa = home.obterEntidadePorId(pessoaId);

			int seq = rows[i].getInt("seq");

			this.pessoas.put(new Integer(seq), new PessoaImpl(this, seq,
					pessoa, rows[i].getString("tipo")));
		}

		return this.pessoas.values();
	}

	public void removerPessoa(Pessoa pessoa) throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate("crm",
				"delete from processo_pessoas where id=? and seq=?");
		delete.addLong(this.obterId());
		delete.addLong(pessoa.obterId());
		delete.execute();
		if (this.pessoas != null)
			this.pessoas.remove(new Integer(pessoa.obterId()));
	}

	public void adicionarPessoa(Entidade pessoa, String tipo) throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select MAX(seq) as MX from processo_pessoas where id = ?");
		query.addLong(this.obterId());

		int seq = query.executeAndGetFirstRow().getInt("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate("crm",
						"insert into processo_pessoas(id,seq,pessoa,tipo) values(?,?,?,?)");
		insert.addLong(this.obterId());
		insert.addInt(seq);
		insert.addLong(pessoa.obterId());
		insert.addString(tipo);

		insert.execute();
	}

	public String obterObjeto() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select objeto from processo where id=?");
		query.addLong(this.obterId());

		String objeto = query.executeAndGetFirstRow().getString("objeto");

		return objeto;
	}

	public String obterCircunscricao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select circunscricao from processo where id=?");
		query.addLong(this.obterId());

		String circunscricao = query.executeAndGetFirstRow().getString(
				"circunscricao");

		return circunscricao;
	}

	public String obterForum() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select forum from processo where id=?");
		query.addLong(this.obterId());

		String forum = query.executeAndGetFirstRow().getString("forum");

		return forum;
	}

	public Date obterDataDemanda() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_demanda from processo where id=?");
		query.addLong(this.obterId());

		Date dataDemanda = null;

		if (query.executeAndGetFirstRow().getLong("data_demanda") > 0)
			dataDemanda = new Date(query.executeAndGetFirstRow().getLong(
					"data_demanda"));

		return dataDemanda;
	}

	public String obterSentenca() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select sentenca from processo where id=?");
		query.addLong(this.obterId());

		String sentenca = query.executeAndGetFirstRow().getString("sentenca");

		return sentenca;
	}

	public Date obterDataCancelamento() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_cancelamento from processo where id=?");
		query.addLong(this.obterId());

		Date dataCancelamento = null;

		if (query.executeAndGetFirstRow().getLong("data_cancelamento") > 0)
			dataCancelamento = new Date(query.executeAndGetFirstRow().getLong(
					"data_cancelamento"));

		return dataCancelamento;
	}
	
	public boolean permiteAtualizar() throws Exception
	{
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		Entidade ial = home.obterEntidadePorApelido("intendenteial");
		
		if(ial!=null)
		{
			if(this.obterUsuarioAtual().obterSuperiores().contains(ial))
				return true;
			else
				return false;
		}
		else
			return super.permiteAtualizar();
	}
	
}