package com.gvs.crm.model.impl;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Reaseguradora;
import com.gvs.crm.model.Usuario;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class ReaseguradoraImpl extends EntidadeImpl implements Reaseguradora {

	private Map classificacoes;

	public class ClassificacaoImpl implements Classificacao {

		private ReaseguradoraImpl reaseguradora;

		private int id;

		private String classificacao;

		private String nivel;

		private String codigo;

		private String qualificacao;

		private Date data;

		ClassificacaoImpl(ReaseguradoraImpl reaseguradora, int id,
				String classificacao, String nivel, String codigo,
				String qualificacao, Date data) throws Exception {
			this.reaseguradora = reaseguradora;
			this.id = id;
			this.classificacao = classificacao;
			this.nivel = nivel;
			this.codigo = codigo;
			this.qualificacao = qualificacao;
			this.data = data;
		}

		public void atualizar(String classificacao, String nivel,
				String codigo, String qualificacao, Date data) throws Exception {
			SQLUpdate update = this.reaseguradora
					.getModelManager()
					.createSQLUpdate(
							"update reaseguradora_classificacao set classificacao=?, nivel=?, codigo = ?, qualificacao=?, data=? where entidade=? and id=?");
			update.addString(classificacao);
			update.addString(nivel);
			update.addString(codigo);
			update.addString(qualificacao);
			update.addLong(data.getTime());
			update.addLong(this.reaseguradora.obterId());
			update.addInt(this.id);
			update.execute();
		}

		public Reaseguradora obterReaseguradora() throws Exception {
			return this.reaseguradora;
		}

		public int obterId() throws Exception {
			return this.id;
		}

		public String obterCodigo() throws Exception {
			return this.codigo;
		}

		public String obterClassificacao() throws Exception {
			return this.classificacao;
		}

		public String obterNivel() throws Exception {
			return this.nivel;
		}

		public String obterQualificacao() throws Exception {
			return this.qualificacao;
		}

		public Date obterData() throws Exception {
			return this.data;
		}
	}

	public void adicionarClassificacao(String classificacao, String nivel,
			String codigo, String qualificacao, Date data) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"select max(id) as MX from reaseguradora_classificacao where entidade=?");
		query.addLong(this.obterId());
		long id = query.executeAndGetFirstRow().getLong("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into reaseguradora_classificacao (entidade, id, classificacao, nivel, codigo, qualificacao, data) values (?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addLong(id);
		insert.addString(classificacao);
		insert.addString(nivel);
		insert.addString(codigo);
		insert.addString(qualificacao);
		insert.addLong(data.getTime());
		insert.execute();
	}

	public void adicionarClassificacaoNivel(String nivel) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select max(seq) as MX from reaseguradora_classificacao_nivel where entidade=?");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"crm",
						"insert into reaseguradora_classificacao_nivel(entidade, seq, nivel) values (?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addLong(id);
		insert.addString(nivel);
		insert.execute();
	}

	public Collection obterNiveis() throws Exception {
		Map niveis = new TreeMap();

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select nivel from reaseguradora_classificacao_nivel group by nivel");

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			String nivel = rows[i].getString("nivel");

			niveis.put(nivel, nivel);
		}

		return niveis.values();
	}

	public Classificacao obterClassificacao(int id) throws Exception {
		this.obterClassificacoes();
		return (Classificacao) this.classificacoes.get(Integer.toString(id));
	}

	public Collection obterClassificacoes() throws Exception
	{
		this.classificacoes = new TreeMap();
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select * from reaseguradora_classificacao where entidade=?");
		query.addLong(this.obterId());
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
		{
			int id = rows[i].getInt("id");

			this.classificacoes.put(Integer.toString(id),new ClassificacaoImpl(this, id, rows[i].getString("classificacao"), rows[i].getString("nivel"), rows[i].getString("codigo"),rows[i].getString("qualificacao"), new Date(rows[i].getLong("data"))));
		}

		return this.classificacoes.values();
	}

	public void removerClassificacao(Reaseguradora.Classificacao classificacao)
			throws Exception {
		SQLUpdate delete = this
				.getModelManager()
				.createSQLUpdate(
						"delete from reaseguradora_classificacao where entidade=? and id=?");
		delete.addLong(this.obterId());
		delete.addInt(classificacao.obterId());
		delete.execute();
		if (this.classificacoes != null)
			this.classificacoes.remove(Integer
					.toString(classificacao.obterId()));

		//System.out.println("delete from reaseguradora_classificacao where
		// entidade="+this.obterId()+" and id=" + classificacao.obterId());

		SQLUpdate delete2 = this
				.getModelManager()
				.createSQLUpdate(
						"delete from reaseguradora_classificacao_nivel where entidade=? and nivel=?");
		delete2.addLong(this.obterId());
		delete2.addString(classificacao.obterNivel());
		delete2.execute();

		//System.out.println("delete from reaseguradora_classificacao_nivel
		// where entidade="+this.obterId()+" and nivel=" +
		// classificacao.obterClassificacao());
	}
	
	public boolean permiteAtualizar() throws Exception
	{
		/*EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		Entidade icoras = home.obterEntidadePorApelido("jefedivisionreasegurosicoras");
		
		if(icoras!=null)
		{
			if(this.obterUsuarioAtual().obterSuperiores().contains(icoras))
				return true;
			else
				return false;
		}
		else
			return super.permiteAtualizar();*/
		
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
				SQLQuery query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from evento,dados_reaseguro where evento.id = dados_reaseguro.id and reaseguradora = ?");
				query.addLong(this.obterId());
				
				if(query.executeAndGetFirstRow().getInt("qtde") > 0)
					retorno = false;
				else
				{
					SQLQuery query2 = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from evento,registro_anulacao where evento.id = registro_anulacao.id and reaseguradora = ?");
					query2.addLong(this.obterId());
					
					if(query2.executeAndGetFirstRow().getInt("qtde") > 0)
						retorno = false;
				}
			}
		}*/
		return retorno;
	}
}