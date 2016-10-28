package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Livro;
import com.gvs.crm.model.Processamento;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class ProcessamentoImpl extends EventoImpl implements Processamento
{
	private Date data;
	
	public class AgendaImpl implements Agenda
	{
		private ProcessamentoImpl processamento;
		private int seq;
		private String nomeArquivo;
		private String mensagem;
		private int mes,ano,erro;
		private Evento evento;
		private Entidade aseguradora;
		
		public AgendaImpl(ProcessamentoImpl processamento, int seq, String nomeArquivo, String mensagem, int mes, int ano, Evento evento, Entidade aseguradora, int erro)
		{
			this.processamento = processamento;
			this.nomeArquivo = nomeArquivo;
			this.mensagem = mensagem;
			this.mes = mes;
			this.ano = ano;
			this.evento = evento;
			this.aseguradora = aseguradora;
			this.erro = erro;
		}
		
		public Processamento obterProcessamento()
		{
			return this.processamento;
		}
		
		public int obterSeq()
		{
			return this.seq;
		}

		public String obterNomeArquivo()
		{
			return this.nomeArquivo;
		}

		public String obterMensagem()
		{
			return this.mensagem;
		}

		public int obterMes()
		{
			return this.mes;
		}

		public int obterAno()
		{
			return this.ano;
		}

		public Evento obterEvento()
		{
			return this.evento;
		}
		
		public Entidade obterAseguradora()
		{
			return this.aseguradora;
		}
		
		public boolean agendaComErro()
		{
			return this.erro == 1;
		}
		
		public boolean podeForcar() throws Exception
		{
			SQLQuery query = this.processamento.getModelManager().createSQLQuery("crm","select count(*) as qtde from rotor_prov where arquivo = ? and visivel = 1");
			query.addString(nomeArquivo);
			
			return query.executeAndGetFirstRow().getInt("qtde") > 0;
		}
	}
	
	public void atribuirData(Date data)
	{
		this.data = data;
	}
	
	public void incluir() throws Exception
	{
		super.incluir();
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into processamento(id,data) values(?,?)");
		insert.addLong(this.obterId());
		insert.addDate(data);
		
		insert.execute();
	}
	
	public Date obterData()
	{
		return this.data;
	}
	
	public void addAgenda(String nomeArquivo, String mensagem, Evento evento) throws Exception
	{
		int mes = 0;
		int ano = 0;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select max(seq) as mx from processamento_agenda where id = ?");
		query.addLong(this.obterId());
		
		int seq = query.executeAndGetFirstRow().getInt("mx") + 1;
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into processamento_agenda(id,seq,nome_arquivo,mensagem,mes,ano,id_agenda, id_aseguradora) values(?,?,?,?,?,?,?,?)");
		insert.addLong(this.obterId());
		insert.addInt(seq);
		insert.addString(nomeArquivo);
		insert.addString(mensagem);
		if(evento!=null)
		{
			if(evento instanceof AgendaMovimentacao)
			{
				AgendaMovimentacao agenda = (AgendaMovimentacao) evento;
				
				mes = agenda.obterMesMovimento();
				ano = agenda.obterAnoMovimento();
			}
			else if(evento instanceof Livro)
			{
				Livro livro = (Livro) evento;
				
				mes = livro.obterMes();
				ano = livro.obterAno();
			}
		}
		
		insert.addInt(mes);
		insert.addInt(ano);
		insert.addLong(evento.obterId());
		insert.addLong(evento.obterOrigem().obterId());
		
		insert.execute();
	}
	
	public Collection<Agenda> obterAgendas() throws Exception
	{
		Collection<Agenda> agendas = new ArrayList<Agenda>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select seq,nome_arquivo,mensagem,mes,ano,id_agenda,id_aseguradora,erro from processamento_agenda where id = ? order by seq");
		query.addLong(this.obterId());
		
		SQLRow[] rows = query.execute();
		
		int seq,mes,ano,erro;
		String nomeArquivo,mensagem;
		long eventoId,aseguradoraId;
		Evento evento;
		Entidade aseguradora;
		AgendaImpl agenda;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			seq = rows[i].getInt("seq");
			nomeArquivo = rows[i].getString("nome_arquivo");
			mensagem = rows[i].getString("mensagem");
			mes = rows[i].getInt("mes");
			ano = rows[i].getInt("ano");
			erro = rows[i].getInt("erro");
			eventoId = rows[i].getLong("id_agenda");
			aseguradoraId = rows[i].getLong("id_aseguradora");
			
			evento = null;
			aseguradora = null;
			
			if(eventoId > 0)
				evento = home.obterEventoPorId(eventoId);
			if(aseguradoraId > 0)
				aseguradora = entidadeHome.obterEntidadePorId(aseguradoraId);
			
			agenda = new AgendaImpl(this, seq, nomeArquivo, mensagem, mes, ano, evento, aseguradora, erro);
			
			agendas.add(agenda);
		}
		
		return agendas;
	}
	
	public Agenda instanciarAgenda(int seq) throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		AgendaImpl agenda = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select nome_arquivo,mensagem,mes,ano,id_agenda,id_aseguradora,erro from processamento_agenda where id = ? and seq = ?");
		query.addLong(this.obterId());
		query.addInt(seq);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			String nomeArquivo = rows[i].getString("nome_arquivo");
			String mensagem = rows[i].getString("mensagem");
			int mes = rows[i].getInt("mes");
			int ano = rows[i].getInt("ano");
			int erro = rows[i].getInt("erro");
			long eventoId = rows[i].getLong("id_agenda");
			long aseguradoraId = rows[i].getLong("id_aseguradora");
			
			Evento evento = null;
			Entidade aseguradora = null;
			
			if(eventoId > 0)
				evento = home.obterEventoPorId(eventoId);
			if(aseguradoraId > 0)
				aseguradora = entidadeHome.obterEntidadePorId(aseguradoraId);
			
			agenda = new AgendaImpl(this, seq, nomeArquivo, mensagem, mes, ano, evento, aseguradora, erro);
		}
		
		return agenda;
	}
	
	public boolean existeAgenda(int mes, int ano, int erro) throws Exception
	{
		String sql = "select count(*) as qtde from processamento_agenda where id = " + this.obterId();
		if(mes>0)
			sql+=" and mes = " + mes;
		if(ano>0)
			sql+=" and mes = " + mes;
		if(erro !=-1)
			sql+=" and erro = " + erro;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(this.obterId());
		
		return query.execute().length > 0;
	}
	
	public void forcarProcessamento(String nomeArquivo) throws Exception
	{
		SQLUpdate delete = this.getModelManager().createSQLUpdate("crm","delete from rotor_prov where arquivo = ? and visivel = 1");
		delete.addString(nomeArquivo);
		delete.execute();
		
		String arquivoB = nomeArquivo.replace("A", "B");
		
		delete = this.getModelManager().createSQLUpdate("crm","delete from rotor_prov where arquivo = ? and visivel = 1");
		delete.addString(arquivoB);
		delete.execute();
		
	}
}
