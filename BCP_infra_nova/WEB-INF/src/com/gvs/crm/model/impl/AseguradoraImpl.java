package com.gvs.crm.model.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.AgendaMeicos;
import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AspectosLegais;
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.MeicosAseguradora;
import com.gvs.crm.model.MeicosCalculo;
import com.gvs.crm.model.Plano;
import com.gvs.crm.model.RatioPermanente;
import com.gvs.crm.model.RatioTresAnos;
import com.gvs.crm.model.RatioUmAno;
import com.gvs.crm.model.Sinistro;
import com.gvs.crm.model.Usuario;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class AseguradoraImpl extends EntidadeImpl implements Aseguradora {
	private Map reaseguradoras;

	private Map acionistas;

	private Map filiais;

	private Map fusoes;

	private Map<Long,Plano> planos;

	private Map coaseguradores;

	public class ReaseguradoraImpl implements Reaseguradora {
		private AseguradoraImpl aseguradora;

		private int id;

		private Entidade reaseguradora;

		private Entidade corretora;

		private String tipoContrato;

		private Date dataVencimento;

		private int participacao;

		private String observacao;

		ReaseguradoraImpl(AseguradoraImpl aseguradora, int id,
				Entidade reaseguradora, Entidade corretora,
				String tipoContrato, Date dataVencimento, int participacao,
				String observacao) {
			this.aseguradora = aseguradora;
			this.id = id;
			this.reaseguradora = reaseguradora;
			this.corretora = corretora;
			this.tipoContrato = tipoContrato;
			this.dataVencimento = dataVencimento;
			this.participacao = participacao;
			this.observacao = observacao;
		}

		public void atualizar(Entidade reaseguradora, Entidade corretora,
				String tipoContrato, Date dataVencimento, int participacao,
				String observacao) throws Exception {
			SQLUpdate update = this.aseguradora
					.getModelManager()
					.createSQLUpdate(
							"update aseguradora_reaseguradora set reaseguradora=?, corretora=?, tipo_contrato=?, data_vencimento=?, participacao=?, observacao=? where entidade=? and id=?");
			update.addLong(reaseguradora.obterId());
			update.addLong(corretora.obterId());
			update.addString(tipoContrato);
			update.addLong(dataVencimento.getTime());
			update.addInt(participacao);
			update.addString(observacao);
			update.addLong(this.aseguradora.obterId());
			update.addInt(this.id);
			update.execute();
		}

		public Aseguradora obterAseguradora() throws Exception {
			return this.aseguradora;
		}

		public int obterId() throws Exception {
			return this.id;
		}

		public Entidade obterReaseguradora() throws Exception {
			return this.reaseguradora;
		}

		public Entidade obterCorretora() throws Exception {
			return this.corretora;
		}

		public String obterTipoContrato() throws Exception {
			return this.tipoContrato;
		}

		public Date obterDataVencimento() throws Exception {
			return this.dataVencimento;
		}

		public int obterParticipacao() throws Exception {
			return this.participacao;
		}

		public String obterObservacao() throws Exception {
			return this.observacao;
		}
	}

	public class AcionistaImpl implements Acionista {
		private AseguradoraImpl aseguradora;

		private int id;

		private String acionista;

		private int quantidade;

		private String tipo;

		AcionistaImpl(AseguradoraImpl aseguradora, int id, String acionista,
				int quantidade, String tipo) throws Exception {
			this.aseguradora = aseguradora;
			this.id = id;
			this.acionista = acionista;
			this.quantidade = quantidade;
			this.tipo = tipo;
		}

		public void atualizar(String acionista, int quantidade, String tipo)
				throws Exception {
			SQLUpdate update = this.aseguradora
					.getModelManager()
					.createSQLUpdate(
							"update aseguradora_acionista set acionista=?, quantidade=?, tipo=? where entidade=? and id=?");
			update.addString(acionista);
			update.addInt(quantidade);
			update.addString(tipo);
			update.addLong(this.aseguradora.obterId());
			update.addInt(this.id);
			update.execute();
		}

		public Aseguradora obterAseguradora() throws Exception {
			return this.aseguradora;
		}

		public int obterId() throws Exception {
			return this.id;
		}

		public String obterAcionista() throws Exception {
			return this.acionista;
		}

		public int obterquantidade() throws Exception {
			return this.quantidade;
		}

		public String obtertipo() throws Exception {
			return this.tipo;
		}
	}

	public class FilialImpl implements Filial {
		private AseguradoraImpl aseguradora;

		private int id;

		private String filial;

		private String tipo;

		private String telefone;

		private String cidade;

		private String endereco;

		private String email;

		FilialImpl(AseguradoraImpl aseguradora, int id, String filial,
				String tipo, String telefone, String cidade, String endereco,
				String email) throws Exception {
			this.aseguradora = aseguradora;
			this.id = id;
			this.filial = filial;
			this.tipo = tipo;
			this.telefone = telefone;
			this.cidade = cidade;
			this.endereco = endereco;
			this.email = email;
		}

		public void atualizar(String filial, String tipo, String telefone,
				String cidade, String endereco, String email) throws Exception {
			SQLUpdate update = this.aseguradora
					.getModelManager()
					.createSQLUpdate(
							"update aseguradora_filial set filial=?, tipo=?, telefone=?, cidade=?, endereco=?, email=? where entidade=? and id=?");
			update.addString(filial);
			update.addString(tipo);
			update.addString(telefone);
			update.addString(cidade);
			update.addString(endereco);
			update.addString(email);
			update.addLong(this.aseguradora.obterId());
			update.addInt(this.id);
			update.execute();
		}

		public Aseguradora obterAseguradora() throws Exception {
			return this.aseguradora;
		}

		public int obterId() throws Exception {
			return this.id;
		}

		public String obterFilial() throws Exception {
			return this.filial;
		}

		public String obterTipo() throws Exception {
			return this.tipo;
		}

		public String obterTelefone() throws Exception {
			return this.telefone;
		}

		public String obterCidade() throws Exception {
			return this.cidade;
		}

		public String obterEndereco() throws Exception {
			return this.endereco;
		}

		public String obterEmail() throws Exception {
			return this.email;
		}
	}

	public class FusaoImpl implements Fusao {
		private AseguradoraImpl aseguradora;

		private int id;

		private String empresa;

		private Date dataFusao;

		FusaoImpl(AseguradoraImpl aseguradora, int id, String empresa,
				Date dataFusao) throws Exception {
			this.aseguradora = aseguradora;
			this.id = id;
			this.empresa = empresa;
			this.dataFusao = dataFusao;
		}

		public void atualizar(String empresa, Date data) throws Exception {
			SQLUpdate update = this.aseguradora
					.getModelManager()
					.createSQLUpdate(
							"update aseguradora_fusao set empresa=?, data=? where entidade=? and id=?");
			update.addString(empresa);
			update.addLong(data.getTime());
			update.addLong(this.aseguradora.obterId());
			update.addInt(this.id);
			update.execute();
		}

		public Aseguradora obterAseguradora() throws Exception {
			return this.aseguradora;
		}

		public int obterId() throws Exception {
			return this.id;
		}

		public String obterEmpresa() throws Exception {
			return this.empresa;
		}

		public Date obterDatausao() throws Exception {
			return this.dataFusao;
		}
	}

	public class ClassificacaoImpl implements Classificacao
	{
		private AseguradoraImpl aseguradora;

		private int seq;

		private String nome;

		private String mes;
		
		private String ano;

		private double valor;

		public ClassificacaoImpl(AseguradoraImpl aseguradora, int seq,String nome, String mes, String ano, double valor) throws Exception 
		{
			this.aseguradora = aseguradora;
			this.seq = seq;
			this.nome = nome;
			this.mes = mes;
			this.ano = ano;
			this.valor = valor;
		}

		public Aseguradora obterAseguradora() throws Exception 
		{
			return this.aseguradora;
		}

		public int obterSeq() throws Exception 
		{
			return this.seq;
		}

		public String obterNome() throws Exception 
		{
			return this.nome;
		}

		public String obterMes() throws Exception 
		{
			return this.mes;
		}
		
		public String obterAno() throws Exception 
		{
			return this.ano;
		}

		public double obterValor() throws Exception 
		{
			return this.valor;
		}
	}
	
	public class MargemSolvenciaImpl implements MargemSolvencia
	{
		private AseguradoraImpl aseguradora;

		private int seq;

		private String mesAno;

		private double valor;

		public MargemSolvenciaImpl(AseguradoraImpl aseguradora, int seq,String mesAno, double valor) throws Exception 
		{
			this.aseguradora = aseguradora;
			this.seq = seq;
			this.mesAno = mesAno;
			this.valor = valor;
		}

		public Aseguradora obterAseguradora() throws Exception 
		{
			return this.aseguradora;
		}

		public int obterSeq() throws Exception 
		{
			return this.seq;
		}

		public String obterMesAno() throws Exception 
		{
			return this.mesAno;
		}

		public double obterValor() throws Exception 
		{
			return this.valor;
		}
	}

	public Collection obterAgendas() throws Exception
	{
		Collection agendas = new ArrayList();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		//if(Integer.parseInt(this.obterSigla()) < 80) 
		//{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,agenda_movimentacao where evento.id = agenda_movimentacao.id " +
					"and evento.origem = ? and classe='AgendaMovimentacao' order by agenda_movimentacao.movimento_mes, agenda_movimentacao.movimento_ano desc ");
				
			query.addLong(this.obterId());
		
			SQLRow[] filas =  query.execute();
		
			for(int i = 0 ; i < filas.length;i++) 
			{
			
				long id = filas[i].getLong("id");
				
				AgendaMovimentacao agenda = (AgendaMovimentacao) home.obterEventoPorId(id);
				
				agendas.add(agenda);
			}
				
		//}
		return agendas; 
	}
	
	public AgendaMovimentacao obterAgendaPendente(String tipo) throws Exception
	{
		AgendaMovimentacao agenda = null;
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		//if(Integer.parseInt(this.obterSigla()) < 80) 
		//{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,agenda_movimentacao,fase where evento.id = agenda_movimentacao.id " +
					"and agenda_movimentacao.id = fase.id and evento.origem = ? and classe='AgendaMovimentacao' and tipo = ? and codigo='pendente' and termino = 0 order by agenda_movimentacao.movimento_mes, agenda_movimentacao.movimento_ano desc ");
				
			query.addLong(this.obterId());
			query.addString(tipo);
		
			SQLRow[] filas =  query.execute();
		
			for(int i = 0 ; i < filas.length;i++) 
			{
				long id = filas[i].getLong("id");
				
				agenda = (AgendaMovimentacao) home.obterEventoPorId(id);
			}
				
		//}
		return agenda; 
	}
	
	public Collection obtenerPlanCtas (int mes, int year)throws Exception  {
		Collection plan_ctas = new ArrayList();
		String mes1 = new Integer(mes).toString();
		String year1 = new Integer(year).toString();
		SQLQuery plan = this.getModelManager().createSQLQuery("crm","select Id from relatorio where mes_ano = ? and   seguradora = ?");
		plan.addString(mes1+year1);
		plan.addLong(this.obterId());
		SQLRow[] filas =  plan.execute();
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
        
		
		for(int i = 0 ; i < filas.length;i++) {
			
			long id = filas[i].getLong("id");
		Entidade entidade = (Entidade)	home.obterEntidadePorId(id);		
		//ClassificacaoContas clasifctas = (ClassificacaoContas) home.obterEntidadePorId(id);
		
		plan_ctas.add(entidade);
					
		}
		return plan_ctas;

	}
	public Collection obtenerPolizas(Date fecha_desde, Date fecha_hasta, int nro_pag ) throws Exception  {
		
		Collection poliza = new ArrayList();
		
		SQLQuery pol = this.getModelManager().createSQLQuery("crm","select top 150 evento.id from evento, apolice where evento.id = apolice.id " +
				"and evento.data_prevista_inicio >= ? and evento.data_prevista_conclusao <= ? and evento.origem = ?");
	
		pol.addLong(fecha_desde.getTime());
		pol.addLong(fecha_hasta.getTime());
		pol.addLong(this.obterId());
		pol.setCurrentPage(nro_pag);
		pol.setRowsByPage(20);
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLRow[] filas =  pol.execute();
		
		for(int i = 0 ; i < filas.length;i++) {
			
			long id = filas[i].getLong("id");
					
		Apolice apolice = (Apolice) home.obterEventoPorId(id);
		
		poliza.add(apolice);
		
			
		}
		
		return poliza;
	}
	
	/*
	 * public class PlanoImpl implements Plano {
	 * 
	 * private AseguradoraImpl aseguradora; private int id; private String ramo;
	 * private String secao; private String plano; private Evento resolucao;
	 * private Date data; private String situacao; private String descricao;
	 * 
	 * PlanoImpl(AseguradoraImpl aseguradora, int id, String ramo, String secao,
	 * String plano, Evento resolucao, Date data, String situacao, String
	 * descricao) throws Exception { this.aseguradora = aseguradora; this.id =
	 * id; this.ramo = ramo; this.secao = secao; this.plano = plano;
	 * this.resolucao = resolucao; this.data = data; this.situacao = situacao;
	 * this.descricao = descricao; }
	 * 
	 * public void atualizar(String ramo, String secao, String plano, Evento
	 * resolucao, Date data, String situacao, String descricao) throws Exception {
	 * SQLUpdate update =
	 * this.aseguradora.getModelManager().createSQLUpdate("update
	 * aseguradora_plano set ramo=?, secao=?, plano=?, resolucao=?, data=?,
	 * situacao=?, descricao=? where entidade=? and id=?");
	 * update.addString(ramo); update.addString(secao); update.addString(plano);
	 * update.addLong(null); update.addLong(data.getTime());
	 * update.addString(situacao); update.addString(descricao);
	 * update.addLong(this.aseguradora.obterId()); update.addInt(this.id);
	 * update.execute(); }
	 * 
	 * public Aseguradora obterAseguradora() throws Exception { return
	 * this.aseguradora; }
	 * 
	 * public int obterId() throws Exception { return this.id; }
	 * 
	 * public String obterRamo() throws Exception { return this.ramo; }
	 * 
	 * public String obterSecao() throws Exception { return this.secao; }
	 * 
	 * public String obterPlano() throws Exception { return this.plano; }
	 * 
	 * public Evento obterResolucao() throws Exception { return this.resolucao; }
	 * 
	 * public Date obterData() throws Exception { return this.data; }
	 * 
	 * public String obterSituacao() throws Exception { return this.situacao; }
	 * 
	 * public String obterDescricao() throws Exception { return this.descricao; } }
	 */

	public class CoaseguradorImpl implements Coasegurador {
		private AseguradoraImpl aseguradora;

		private int id;

		private String codigo;

		CoaseguradorImpl(AseguradoraImpl aseguradora, int id, String codigo)
				throws Exception {
			this.aseguradora = aseguradora;
			this.id = id;
			this.codigo = codigo;
		}

		public void atualizar(String codigo) throws Exception {
			SQLUpdate update = this.aseguradora
					.getModelManager()
					.createSQLUpdate(
							"update aseguradora_coasegurador set codigo=? where entidade=? and id=?");
			update.addString(codigo);
			update.addLong(this.aseguradora.obterId());
			update.addInt(this.id);
			update.execute();
		}

		public Aseguradora obterAseguradora() throws Exception {
			return this.aseguradora;
		}

		public int obterId() throws Exception {
			return this.id;
		}

		public String obterCodigo() throws Exception {
			return this.codigo;
		}
	}

	public void adicionarReaseguradora(Entidade reaseguradora,
			Entidade corretora, String tipoContrato, Date dataVencimento,
			int participacao, String observacao) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"select max(id) as MX from aseguradora_reaseguradora where entidade=?");
		query.addLong(this.obterId());
		long id = query.executeAndGetFirstRow().getLong("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into aseguradora_reaseguradora (entidade, id, reaseguradora, corretora, tipo_contrato, data_vencimento, participacao, observacao) values (?, ?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addLong(id);
		insert.addLong(reaseguradora.obterId());
		insert.addLong(corretora.obterId());
		insert.addString(tipoContrato);
		insert.addLong(dataVencimento.getTime());
		insert.addInt(participacao);
		insert.addString(observacao);
		insert.execute();
	}

	public void adicionarClassificacao(String nome, String mes, String ano, double valor) throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("select count(*) as qtde from aseguradora_classificacao where id = ? and nome = ? and mes = ? and ano = ?");
		query.addLong(this.obterId());
		query.addString(nome);
		query.addString(mes);
		query.addString(ano);
		
		if(query.executeAndGetFirstRow().getInt("qtde") == 0)
		{
			query = this.getModelManager().createSQLQuery("select max(seq) as MX from aseguradora_classificacao where id = ? and mes = ? and ano = ?");
			query.addLong(this.obterId());
			query.addString(mes);
			query.addString(ano);
	
			int seq = query.executeAndGetFirstRow().getInt("MX") + 1;
	
			SQLUpdate insert = this.getModelManager().createSQLUpdate("insert into aseguradora_classificacao(id, seq, nome, mes, ano, valor) values (?, ?, ?, ?, ?, ?)");
			insert.addLong(this.obterId());
			insert.addInt(seq);
			insert.addString(nome);
			insert.addString(mes);
			insert.addString(ano);
			insert.addDouble(valor);
	
			insert.execute();
		}
		else
		{
			SQLUpdate update = this.getModelManager().createSQLUpdate("update aseguradora_classificacao set valor = ? where id = ? and nome = ? and mes = ? and ano = ?");
			update.addDouble(valor);
			update.addLong(this.obterId());
			update.addString(nome);
			update.addString(mes);
			update.addString(ano);
	
			update.execute();
		}
	}

	public Reaseguradora obterReaseguradora(int id) throws Exception {
		this.obterReaseguradoras();
		return (Reaseguradora) this.reaseguradoras.get(Integer.toString(id));
	}

	public Collection obterReaseguradoras() throws Exception {

		this.reaseguradoras = new TreeMap();
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from aseguradora_reaseguradora where entidade=?");
		query.addLong(this.obterId());
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			int id = rows[i].getInt("id");

			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			long reaseguradoraId = rows[i].getLong("reaseguradora");
			long corretoraId = rows[i].getLong("corretora");

			Entidade reaseguradora = home.obterEntidadePorId(reaseguradoraId);
			Entidade corretora = home.obterEntidadePorId(corretoraId);

			this.reaseguradoras.put(Integer.toString(id),
					new ReaseguradoraImpl(this, id, reaseguradora, corretora,
							rows[i].getString("tipo_contrato"), new Date(
									rows[i].getLong("data_vencimento")),
							rows[i].getInt("participacao"), rows[i]
									.getString("observacao")));
		}

		return this.reaseguradoras.values();
	}

	public void removerReaseguradora(Aseguradora.Reaseguradora reaseguradora)
			throws Exception {
		SQLUpdate delete = this
				.getModelManager()
				.createSQLUpdate(
						"delete from aseguradora_reaseguradora where entidade=? and id=?");
		delete.addLong(this.obterId());
		delete.addInt(reaseguradora.obterId());
		delete.execute();
		if (this.reaseguradoras != null)
			this.reaseguradoras.remove(Integer
					.toString(reaseguradora.obterId()));
	}

	public Collection obterCalculoMeicos(AgendaMeicos agenda) throws Exception 
	{
		Map calculos = new TreeMap();

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,meicos_calculo,fase where evento.id = meicos_calculo.id and meicos_calculo.id = fase.id and codigo='pendente' and termino = 0 and origem = ? group by evento.id");
		query.addLong(this.obterId());
		
		System.out.println("select evento.id from evento,meicos_calculo,fase where evento.id = meicos_calculo.id and meicos_calculo.id = fase.id and codigo='pendente' and termino = 0 and origem = "+this.obterId()+" group by evento.id");

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) 
		{
			long id = rows[i].getLong("id");

			MeicosCalculo calculo = (MeicosCalculo) home.obterEventoPorId(id);

			if (calculo.obterSuperiores().contains(agenda))
				calculos.put(new Long(calculo.obterId()), calculo);
		}

		return calculos.values();
	}

	public Collection obterAgentesPorPeridodo(Date dataInicio, Date dataFim)throws Exception 
	{
		Map agentes = new TreeMap();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select agente from evento,apolice where evento.id = apolice.id and origem = ? and data_emissao>=? and data_emissao<=? and agente > 0 group by agente");
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) 
		{
			long id = rows[i].getLong("agente");

			Entidade agente = home.obterEntidadePorId(id);

			agentes.put(agente.obterNome() + i, agente);
		}
		return agentes.values();
	}
	
	public Collection<Entidade> obterCorredoresPorPeridodo(Date dataInicio, Date dataFim)throws Exception 
	{
		Map<String, Entidade> agentes = new TreeMap<String, Entidade>();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select corredor from evento,apolice where evento.id = apolice.id and origem = ? and data_emissao>=? and data_emissao<=? and corredor > 0 group by corredor");
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) 
		{
			long id = rows[i].getLong("agente");

			Entidade agente = home.obterEntidadePorId(id);

			agentes.put(agente.obterNome() + i, agente);
		}
		return agentes.values();
	}
	
	public Collection<Apolice> obterApolicesSemAgentesPorPeridodo(Date dataInicio, Date dataFim)throws Exception 
	{
		Collection<Apolice> apolices = new ArrayList<Apolice>();

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,apolice where evento.id = apolice.id and origem = ? and data_emissao>=? and data_emissao<=? and agente = 0  order by data_emissao");
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) 
		{
			long id = rows[i].getLong("id");

			Apolice apolice = (Apolice) home.obterEventoPorId(id);

			apolices.add(apolice);
		}

		return apolices;
	}

	public Collection obterAgentes() throws Exception {
		Map agentes = new TreeMap();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select agente from evento,apolice where origem = ? and evento.id = apolice.id");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("agente");

			Entidade agente = home.obterEntidadePorId(id);

			agentes.put(new Long(agente.obterId()), agente);
		}

		return agentes.values();
	}

	public Collection obterApolices(Entidade agente, Date dataInicio,Date dataFim) throws Exception
	{
		Map apolices = new TreeMap();

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select evento.id from evento,apolice where evento.id = apolice.id and origem = ? and agente =? and data_prevista_inicio >=? and data_prevista_inicio<=? group by evento.id");
		query.addLong(this.obterId());
		query.addLong(agente.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		SQLRow[] rows = query.execute();

		//System.out.println("select evento.id from evento,apolice where
		// evento.id = apolice.id and origem = "+this.obterId()+" and agente
		// ="+agente.obterId()+" and criacao>="+dataInicio.getTime()+" and
		// criacao<="+dataFim.getTime()+" group by evento.id");

		for (int i = 0; i < rows.length; i++)
		{
			long id = rows[i].getLong("id");

			Apolice apolice = (Apolice) home.obterEventoPorId(id);

			apolices.put(new Long(apolice.obterDataPrevistaInicio().getTime()+ apolice.obterId()), apolice);
		}

		return apolices.values();
	}
	
	public Collection<Apolice> obterApolicesProducao(Entidade agente, Date dataInicio,Date dataFim, boolean auxiliar) throws Exception
	{
		Collection<Apolice> apolices = new ArrayList<Apolice>();

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		String sql = "";
		
		if(auxiliar)
			sql = "select evento.id from evento,apolice where evento.id = apolice.id and origem = "+this.obterId()+" and agente ="+agente.obterId()+" and data_emissao >="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" order by data_emissao";
		else
			sql = "select evento.id from evento,apolice where evento.id = apolice.id and origem = "+this.obterId()+" and corredor ="+agente.obterId()+" and data_emissao >="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" order by data_emissao";
		
		System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
		{
			long id = rows[i].getLong("id");

			Apolice apolice = (Apolice) home.obterEventoPorId(id);

			apolices.add(apolice);
		}

		return apolices;
	}
	
	public Collection<String> obterApolicesAcumuladas(Entidade agente, Date dataInicio,Date dataFim, boolean auxiliar) throws Exception
	{
		Collection<String> dados = new ArrayList<String>();
		
		String sql = "";
		
		if(auxiliar)
			sql = "select SUM(comissao_gs) as cgs, SUM(comissao_me) as cme, SUM(prima_gs) as pgs, SUM(premio_gs) as prgs, SUM(capital_gs) as cags, SUM(capital_me) as came from evento,apolice where evento.id = apolice.id and origem = "+this.obterId()+" and agente ="+agente.obterId()+" and data_emissao >="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime();
		else
			sql = "select SUM(comissao_gs) as cgs, SUM(comissao_me) as cme, SUM(prima_gs) as pgs, SUM(premio_gs) as prgs, SUM(capital_gs) as cags, SUM(capital_me) as came from evento,apolice where evento.id = apolice.id and origem = "+this.obterId()+" and corredor ="+agente.obterId()+" and data_emissao >="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime();
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		//query.addLong(this.obterId());
		//query.addLong(agente.obterId());
		//query.addLong(dataInicio.getTime());
		//query.addLong(dataFim.getTime());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
		{
			double comissaoGs = rows[i].getDouble("cgs");
			double comissaoMe = rows[i].getDouble("cme");
			double prima = rows[i].getDouble("pgs");
			double premio = rows[i].getDouble("prgs");
			double capitalGs = rows[i].getDouble("cags");
			double capitalMe = rows[i].getDouble("came");
			
			if(comissaoGs >0 || comissaoMe>0 || prima>0 || premio>0 || capitalGs>0 || capitalMe>0)
				dados.add(comissaoGs+";"+comissaoMe+";"+prima+";"+premio+";"+capitalGs+";"+capitalMe);
			
		}

		return dados;
	}
	
	public Collection<Apolice> obterApolicesCorretor(AuxiliarSeguro corretora, Date dataInicio,Date dataFim) throws Exception
	{
		Map<Long,Apolice> apolices = new TreeMap<Long,Apolice>();

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select evento.id from evento,apolice where evento.id = apolice.id and origem = ? and corredor =? and data_prevista_inicio >=? and data_prevista_inicio<=? group by evento.id");
		query.addLong(this.obterId());
		query.addLong(corretora.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		SQLRow[] rows = query.execute();

		//System.out.println("select evento.id from evento,apolice where
		// evento.id = apolice.id and origem = "+this.obterId()+" and agente
		// ="+agente.obterId()+" and criacao>="+dataInicio.getTime()+" and
		// criacao<="+dataFim.getTime()+" group by evento.id");

		for (int i = 0; i < rows.length; i++)
		{
			long id = rows[i].getLong("id");

			Apolice apolice = (Apolice) home.obterEventoPorId(id);

			apolices.put(new Long(apolice.obterDataPrevistaInicio().getTime()+ apolice.obterId()), apolice);
		}

		return apolices.values();
	}

	public Collection obterMeicos(String tipo) throws Exception 
	{
		Map meicos = new TreeMap();

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		if(tipo.equals("Controle de Documentos"))
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,aseguradora_documentos,fase where evento.id = aseguradora_documentos.id and evento.id = fase.id and origem = ? and tipo = ? and codigo='pendente' and termino = 0");
			query.addLong(this.obterId());
			query.addString(tipo);
	
			SQLRow[] rows = query.execute();
	
			for (int i = 0; i < rows.length; i++)
			{
				long id = rows[i].getLong("id");
	
				MeicosAseguradora meico = (MeicosAseguradora) home.obterEventoPorId(id);
	
				meicos.put(meico.obterDataPrevistaInicio(), meico);
			}
		}
		else
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,aseguradora_indicadores,fase where evento.id = aseguradora_indicadores.id and evento.id = fase.id and origem = ? and tipo = ? and codigo='pendente' and termino = 0");
			query.addLong(this.obterId());
			query.addString(tipo);
	
			SQLRow[] rows = query.execute();
	
			for (int i = 0; i < rows.length; i++)
			{
				long id = rows[i].getLong("id");
	
				MeicosAseguradora meico = (MeicosAseguradora) home.obterEventoPorId(id);
	
				meicos.put(meico.obterDataPrevistaInicio(), meico);
			}
		}

		return meicos.values();
	}

	public void adicionarAcionista(String acionista, int quantidade, String tipo)
			throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"select max(id) as MX from aseguradora_acionista where entidade=?");
		query.addLong(this.obterId());
		long id = query.executeAndGetFirstRow().getLong("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into aseguradora_acionista (entidade, id, acionista, quantidade, tipo) values (?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addLong(id);
		insert.addString(acionista);
		insert.addInt(quantidade);
		insert.addString(tipo);
		insert.execute();
	}

	public Acionista obterAcionista(int id) throws Exception {
		this.obterAcionistas();
		return (Acionista) this.acionistas.get(Integer.toString(id));
	}

	public Collection obterAcionistas() throws Exception {
		this.acionistas = new TreeMap();
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from aseguradora_acionista where entidade=?");
		query.addLong(this.obterId());
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			int id = rows[i].getInt("id");

			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			this.acionistas.put(Integer.toString(id), new AcionistaImpl(this,
					id, rows[i].getString("acionista"), rows[i]
							.getInt("quantidade"), rows[i].getString("tipo")));
		}

		return this.acionistas.values();
	}

	public void removerAcionista(Aseguradora.Acionista acionista)
			throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate(
				"delete from aseguradora_acionista where entidade=? and id=?");
		delete.addLong(this.obterId());
		delete.addInt(acionista.obterId());
		delete.execute();
		if (this.acionistas != null)
			this.acionistas.remove(Integer.toString(acionista.obterId()));
	}

	public void adicionarFilial(String filial, String tipo, String telefone,
			String cidade, String endereco, String email) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"select max(id) as MX from aseguradora_filial where entidade=?");
		query.addLong(this.obterId());
		long id = query.executeAndGetFirstRow().getLong("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into aseguradora_filial (entidade, id, filial, tipo, telefone, cidade, endereco, email) values (?, ?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addLong(id);
		insert.addString(filial);
		insert.addString(tipo);
		insert.addString(telefone);
		insert.addString(cidade);
		insert.addString(endereco);
		insert.addString(email);
		insert.execute();
	}

	public Filial obterFilial(int id) throws Exception {
		this.obterFiliais();
		return (Filial) this.filiais.get(Integer.toString(id));
	}

	public Collection obterFiliais() throws Exception {
		this.filiais = new TreeMap();
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from aseguradora_filial where entidade=?");
		query.addLong(this.obterId());
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			int id = rows[i].getInt("id");

			this.filiais.put(Integer.toString(id), new FilialImpl(this, id,
					rows[i].getString("filial"), rows[i].getString("tipo"),
					rows[i].getString("telefone"), rows[i].getString("cidade"),
					rows[i].getString("endereco"), rows[i].getString("email")));
		}

		return this.filiais.values();
	}

	public void removerFilial(Aseguradora.Filial filial) throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate(
				"delete from aseguradora_filial where entidade=? and id=?");
		delete.addLong(this.obterId());
		delete.addInt(filial.obterId());
		delete.execute();
		if (this.filiais != null)
			this.filiais.remove(Integer.toString(filial.obterId()));
	}

	public void adicionarFusao(String empresa, Date dataFusao) throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery(
				"select max(id) as MX from aseguradora_fusao where entidade=?");
		query.addLong(this.obterId());
		long id = query.executeAndGetFirstRow().getLong("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into aseguradora_fusao (entidade, id, empresa, data) values (?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addLong(id);
		insert.addString(empresa);
		insert.addLong(dataFusao.getTime());
		insert.execute();
	}

	public Fusao obterFusao(int id) throws Exception {
		this.obterFusoes();
		return (Fusao) this.fusoes.get(Integer.toString(id));
	}

	public Collection obterFusoes() throws Exception {
		this.fusoes = new TreeMap();
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from aseguradora_fusao where entidade=?");
		query.addLong(this.obterId());
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			int id = rows[i].getInt("id");

			this.fusoes.put(Integer.toString(id), new FusaoImpl(this, id,
					rows[i].getString("empresa"), new Date(rows[i]
							.getLong("data"))));
		}

		return this.fusoes.values();
	}

	public void removerFusao(Aseguradora.Fusao fusao) throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate(
				"delete from aseguradora_fusao where entidade=? and id=?");
		delete.addLong(this.obterId());
		delete.addInt(fusao.obterId());
		delete.execute();
		if (this.fusoes != null)
			this.fusoes.remove(Integer.toString(fusao.obterId()));
	}

	public void adicionarPlano(String ramo, String secao, String plano,
			Evento resolucao, Date data, String situacao, String descricao)
			throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery(
				"select max(id) as MX from aseguradora_plano where entidade=?");
		query.addLong(this.obterId());
		long id = query.executeAndGetFirstRow().getLong("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into aseguradora_plano (entidade, id, ramo, secao, plano, resolucao, data, situacao, descricao) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addLong(id);
		insert.addString(ramo);
		insert.addString(secao);
		insert.addString(plano);
		insert.addLong(null);
		insert.addLong(data.getTime());
		insert.addString(situacao);
		insert.addString(descricao);
		insert.execute();
	}

	public RatioPermanente obterRatioPermanente() throws Exception 
	{
		RatioPermanente ratio = null;

		SQLQuery query = this.getModelManager().createSQLQuery("select evento.id from evento,ratio_permanente,fase where evento.id = ratio_permanente.id and evento.id = fase.id and origem = ? and fase.codigo='pendente' and termino = 0 group by evento.id");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("id");

		if (id > 0) 
		{
			EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

			ratio = (RatioPermanente) home.obterEventoPorId(id);
		}

		return ratio;
	}

	public RatioUmAno obterRatioUmAno() throws Exception 
	{
		RatioUmAno ratio = null;

		SQLQuery query = this.getModelManager().createSQLQuery("select evento.id from evento,ratio_um_ano,fase where evento.id = ratio_um_ano.id and evento.id = fase.id and origem = ? and fase.codigo='pendente' and termino = 0 group by evento.id");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("id");

		if (id > 0) 
		{
			EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

			ratio = (RatioUmAno) home.obterEventoPorId(id);
		}

		return ratio;
	}

	public RatioTresAnos obterRatioTresAnos() throws Exception {
		RatioTresAnos ratio = null;

		SQLQuery query = this.getModelManager().createSQLQuery("select evento.id from evento,ratio_tres_anos,fase where evento.id = ratio_tres_anos.id and evento.id = fase.id and origem = ? and fase.codigo='pendente' and termino = 0 group by evento.id");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("id");

		if (id > 0) 
		{
			EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

			ratio = (RatioTresAnos) home.obterEventoPorId(id);
		}

		return ratio;
	}

	public Collection obterRatiosPermanentes() throws Exception 
	{
		Collection ratios = new ArrayList();

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,ratio_permanente where evento.id = ratio_permanente.id and origem = ?");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		for (int i = 0; i < rows.length; i++) 
		{
			long id = rows[i].getLong("id");

			RatioPermanente ratio = (RatioPermanente) home.obterEventoPorId(id);

			ratios.add(ratio);
		}

		return ratios;

	}

	public Collection obterRatiosUmAno() throws Exception {
		Collection ratios = new ArrayList();

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,ratio_um_ano where evento.id = ratio_um_ano.id and origem = ?");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");

			RatioUmAno ratio = (RatioUmAno) home.obterEventoPorId(id);

			ratios.add(ratio);
		}

		return ratios;

	}

	public Collection obterRatiosTresAnos() throws Exception {
		Collection ratios = new ArrayList();

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,ratio_tres_anos where evento.id = ratio_tres_anos.id and origem = ?");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");

			RatioTresAnos ratio = (RatioTresAnos) home.obterEventoPorId(id);

			ratios.add(ratio);
		}

		return ratios;

	}

	/*
	 * public Plano obterPlano(int id) throws Exception { this.obterPlanos();
	 * return (Plano) this.planos.get(Integer.toString(id)); }
	 */

	public Collection obterPlanos() throws Exception {
		this.planos = new TreeMap();
		/*
		 * SQLQuery query = this.getModelManager().createSQLQuery("crm", "select *
		 * from aseguradora_plano where entidade=?");
		 * query.addLong(this.obterId()); SQLRow[] rows = query.execute();
		 * 
		 * for (int i = 0; i < rows.length; i++) { int id =
		 * rows[i].getInt("id");
		 * 
		 * this.planos.put(Integer.toString(id), new PlanoImpl(this, id,
		 * rows[i].getString("ramo"), rows[i].getString("secao"),
		 * rows[i].getString("plano"), null, new Date(rows[i].getLong("data")),
		 * rows[i].getString("situacao"), rows[i].getString("descricao"))); }
		 */

		for (Iterator i = this.obterEventosComoOrigem().iterator(); i.hasNext();) 
		{
			Evento e = (Evento) i.next();

			if (e instanceof Plano) 
			{
				Plano plano = (Plano) e;

				this.planos.put(new Long(plano.obterDataResolucao().getTime()),
						plano);
			}
		}

		return this.planos.values();
	}

	public Collection<Plano> obterPlanosOrdenadorPorSecao() throws Exception
	{
		Collection<Plano> planos = new ArrayList<>();

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,plano where evento.id = plano.id and origem = ? and especial = 0 and situacao='Activo' order by secao,plano");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();
		Plano plano;
		
		for (int i = 0; i < rows.length; i++)
		{
			plano = (Plano) home.obterEventoPorId(rows[i].getInt("id"));

			planos.add(plano);
		}

		return planos;
	}
	
	public Map obterPlanosApolicesPeriodo(Date dataInicio, Date dataFim) throws Exception
	{
		Map planos = new TreeMap();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "SELECT apolice.plano FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and origem = ? and data_emissao>=? and data_emissao<=? GROUP BY apolice.plano";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		System.out.println("SELECT apolice.plano FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and origem = "+this.obterId()+" and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" GROUP BY apolice.plano");
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long planoId = rows[i].getLong("plano");
			
			Plano plano = (Plano) home.obterEventoPorId(planoId);
			
			planos.put(plano.obterSecao(),plano);
		}
		
		return planos;
	}
	
	public Map obterPlanosSinistrosPeriodo(Date dataInicio, Date dataFim) throws Exception
	{
		Map planos = new TreeMap();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","SELECT apolice.plano FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and origem = ? and data_sinistro>= ? and data_sinistro<=? GROUP BY apolice.plano");
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long planoId = rows[i].getLong("plano");
			
			Plano plano = (Plano) home.obterEventoPorId(planoId);
			
			planos.put(plano.obterSecao(),plano);
		}
		
		return planos;
	}

	/*
	 * public void removerPlano(Aseguradora.Plano plano) throws Exception {
	 * SQLUpdate delete = this.getModelManager().createSQLUpdate("delete from
	 * aseguradora_plano where entidade=? and id=?");
	 * delete.addLong(this.obterId()); delete.addInt(plano.obterId());
	 * delete.execute(); if (this.planos != null)
	 * this.planos.remove(Integer.toString(plano.obterId())); }
	 */

	public void adicionarCoasegurador(String codigo) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"select max(id) as MX from aseguradora_coasegurador where entidade=?");
		query.addLong(this.obterId());
		long id = query.executeAndGetFirstRow().getLong("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into aseguradora_coasegurador (entidade, id, codigo) values (?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addLong(id);
		insert.addString(codigo);
		insert.execute();
	}

	public Coasegurador obterCoasegurador(int id) throws Exception {
		this.obterCoaseguradores();
		return (Coasegurador) this.coaseguradores.get(Integer.toString(id));
	}

	public Collection obterCoaseguradores() throws Exception {
		this.coaseguradores = new TreeMap();
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from aseguradora_coasegurador where entidade=?");
		query.addLong(this.obterId());
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			int id = rows[i].getInt("id");

			this.coaseguradores.put(Integer.toString(id), new CoaseguradorImpl(
					this, id, rows[i].getString("codigo")));
		}

		return this.coaseguradores.values();
	}

	public Collection obterCoaseguradoresAnuario() throws Exception {
		this.coaseguradores = new TreeMap();
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from aseguradora_coasegurador where entidade=?");
		query.addLong(this.obterId());
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			int id = rows[i].getInt("id");

			this.coaseguradores.put(Integer.toString(id), new CoaseguradorImpl(
					this, id, rows[i].getString("codigo")));
		}

		return this.coaseguradores.values();
	}

	public void removerCoasegurador(Aseguradora.Coasegurador coasegurador)
			throws Exception {
		SQLUpdate delete = this
				.getModelManager()
				.createSQLUpdate(
						"delete from aseguradora_coasegurador where entidade=? and id=?");
		delete.addLong(this.obterId());
		delete.addInt(coasegurador.obterId());
		delete.execute();
		if (this.coaseguradores != null)
			this.coaseguradores
					.remove(Integer.toString(coasegurador.obterId()));
	}

	public Collection obterCoaseguradorasPorGrupo(String codigo)
			throws Exception {
		Collection lista = new ArrayList();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select entidade.id from entidade, aseguradora_coasegurador where entidade.id = aseguradora_coasegurador.entidade and codigo=?");
		query.addString(codigo);

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");

			Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(id);

			//Inscricao inscricao = (Inscricao)
			// aseguradora.obterInscricaoAtiva();

			//if(inscricao!=null)
			//{
			//int numeroInscricao =
			// Integer.parseInt(inscricao.obterInscricao());

			//if(numeroInscricao > 80)
			lista.add(aseguradora);
			//}
		}

		return lista;
	}

	public Collection obterClassificacao(String mes, String ano) throws Exception 
	{
		Map classificacao = new TreeMap();

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select * from aseguradora_classificacao where id = ? and mes = ? and ano = ?");
		query.addLong(this.obterId());
		query.addString(mes);
		query.addString(ano);

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) 
		{
			int seq = rows[i].getInt("seq");
			String nome = rows[i].getString("nome");
			String mes2 = rows[i].getString("mes");
			String ano2 = rows[i].getString("ano");
			double valor = rows[i].getDouble("valor");

			classificacao.put(new Integer(seq), new ClassificacaoImpl(this,seq, nome, ano2, mes, valor));
		}

		return classificacao.values();
	}

	public void excluirClassificacao(String mes, String ano) throws Exception 
	{
		SQLUpdate delete = this.getModelManager().createSQLUpdate("delete from aseguradora_classificacao where id = ? and mes = ? and ano = ?");
		delete.addLong(this.obterId());
		delete.addString(mes);
		delete.addString(ano);

		delete.execute();
	}

	public double obterMargemSolvencia() throws Exception {
		double margem = 0;

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,fase where origem = ? and classe = 'MeicosCalculo' and tipo = 'Margen Solvencia' and evento.id = fase.id and fase.codigo = 'pendente' and termino = 0");
		query.addLong(this.obterId());

		MeicosCalculo calculo = null;

		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");

		long id = query.executeAndGetFirstRow().getLong("id");

		if (id > 0)
			calculo = (MeicosCalculo) home.obterEventoPorId(id);

		return calculo.obterValorIndicador();
	}

	public double obterCapitalGs(Entidade reaseguradora, Date dataInicio,
			Date dataFim, String tipoContrato) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select SUM(caiptal_gs) as valor from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and reaseguradora = ? and tipo_contrato = ? and data_prevista_inicio >= ? and data_prevista_conclusao <= ?");
		query.addLong(this.obterId());
		query.addLong(reaseguradora.obterId());
		query.addString(tipoContrato);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		return query.executeAndGetFirstRow().getDouble("valor");
	}

	public double obterPrimaGs(Entidade reaseguradora, Date dataInicio,
			Date dataFim, String tipoContrato) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select SUM(prima_gs) as valor from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and reaseguradora = ? and tipo_contrato = ? and data_prevista_inicio >= ? and data_prevista_conclusao <= ?");
		query.addLong(this.obterId());
		query.addLong(reaseguradora.obterId());
		query.addString(tipoContrato);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		return query.executeAndGetFirstRow().getDouble("valor");
	}

	public double obterComissaoGs(Entidade reaseguradora, Date dataInicio,
			Date dataFim, String tipoContrato) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select SUM(comissao_gs) as valor from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and reaseguradora = ? and tipo_contrato = ? and data_prevista_inicio >= ? and data_prevista_conclusao <= ?");
		query.addLong(this.obterId());
		query.addLong(reaseguradora.obterId());
		query.addString(tipoContrato);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		return query.executeAndGetFirstRow().getDouble("valor");
	}

	public double obterCapitalGsCorretora(Entidade corretora, Date dataInicio, Date dataFim, String tipoContrato) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						//"select SUM(caiptal_gs) as valor from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and reaseguradora = ? and tipo_contrato = ? and data_prevista_inicio >= ? and data_prevista_conclusao <= ?");
		"select SUM(caiptal_gs) as valor from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and corretora = ? and tipo_contrato = ? and data_prevista_inicio >= ? and data_prevista_conclusao <= ?");
		query.addLong(this.obterId());
		query.addLong(corretora.obterId());
		query.addString(tipoContrato);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		return query.executeAndGetFirstRow().getDouble("valor");
	}

	public double obterPrimaGsCorretora(Entidade corretora, Date dataInicio, Date dataFim, String tipoContrato) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						//"select SUM(prima_gs) as valor from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and reaseguradora = ? and tipo_contrato = ? and data_prevista_inicio >= ? and data_prevista_conclusao <= ?");
		"select SUM(prima_gs) as valor from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and corretora = ? and tipo_contrato = ? and data_prevista_inicio >= ? and data_prevista_conclusao <= ?");
		query.addLong(this.obterId());
		query.addLong(corretora.obterId());
		query.addString(tipoContrato);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		return query.executeAndGetFirstRow().getDouble("valor");
	}

	public double obterComissaoGsCorretora(Entidade corretora, Date dataInicio, Date dataFim, String tipoContrato) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						//"select SUM(comissao_gs) as valor from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and reaseguradora = ? and tipo_contrato = ? and data_prevista_inicio >= ? and data_prevista_conclusao <= ?");
						"select SUM(comissao_gs) as valor from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and corretora = ? and tipo_contrato = ? and data_prevista_inicio >= ? and data_prevista_conclusao <= ?");
		query.addLong(this.obterId());
		query.addLong(corretora.obterId());
		query.addString(tipoContrato);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		return query.executeAndGetFirstRow().getDouble("valor");
	}
	
	public boolean permiteAtualizar() throws Exception
	{
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		Entidade ial = home.obterEntidadePorApelido("intendenteial");
		
		if(ial!=null)
		{
			if(this.obterUsuarioAtual().obterSuperiores().contains(ial) || this.obterUsuarioAtual().obterId() == 1 || this.obterUsuarioAtual().obterNivel().equals(Usuario.ADMINISTRADOR))
				return true;
			else
				return false;
		}
		else
			return super.permiteAtualizar();
	}
	
	public boolean permiteExcluir() throws Exception
	{
		boolean retorno = true;
		
		if(this.obterAgendas().size() > 0)
			retorno = false;
		else
		{
			if(this.obterInferiores().size() > 0)
				retorno = false;
			else
			{
				if(this.obterInscricoes().size() > 0)
					retorno = false;
			}
		}
		
		return retorno;
	}
	
	public Collection<String> obterSecoesSinistrosVigente(Date dataInicio, Date dataFim) throws Exception
	{
		Map<String,String> secoes = new TreeMap<String,String>();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		//SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and data_sinistro>=? and data_sinistro<=? and situacao_seguro = 'Vigente'");
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and data_sinistro>=? and data_sinistro<=? and situacao_seguro = 'Vigente' and data_prevista_inicio>=? and data_prevista_conclusao<=?");
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		long id;
		Sinistro sinistro;
		Apolice apolice;
		boolean temAspectos;
		String linhaSuja;
		String[] linha;
		int qtde;
		double valor;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			
			sinistro = (Sinistro) home.obterEventoPorId(id);
			apolice = (Apolice) sinistro.obterSuperior();
			temAspectos = false;
			
			for(Evento e : apolice.obterInferiores())
			{
				if(e instanceof AspectosLegais)
				{
					temAspectos = true;
					break;
				}
			}
			
			if(!temAspectos)
			{
				if(secoes.containsKey(apolice.obterSecao().obterApelido()))
				{
					linhaSuja = secoes.get(apolice.obterSecao().obterApelido()).toString();
					
					linha = linhaSuja.split(";");
					
					qtde = Integer.parseInt(linha[1]);
					qtde++;
					
					valor = Double.valueOf(linha[2]);
					valor+=sinistro.obterMontanteGs();
					
					secoes.put(apolice.obterSecao().obterApelido(), apolice.obterSecao().obterId() + ";" + qtde + ";" + valor);
				}
				else
					secoes.put(apolice.obterSecao().obterApelido(), apolice.obterSecao().obterId() + ";" + 1 + ";" + sinistro.obterMontanteGs());
			}
		}
		
		return secoes.values();
	}
	
	public Collection<String> obterSecoesSinistrosNoVigente(Date dataInicio, Date dataFim) throws Exception
	{
		Map<String,String> secoes = new TreeMap<String,String>();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		//SQLQuery query = this.getModelManager().createSQLQuery("crm","select secao,count(*) as qtde,SUM(montante_gs) as soma from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and data_sinistro>=? and data_sinistro<=? and situacao_seguro = 'No Vigente' group by secao");
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and data_sinistro>=? and data_sinistro<=? and situacao_seguro = 'No Vigente' and data_prevista_inicio>=? and data_prevista_conclusao<=?");
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		long id;
		Sinistro sinistro;
		Apolice apolice;
		boolean temAspectos;
		String linhaSuja;
		String[] linha;
		int qtde;
		double valor;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			
			sinistro = (Sinistro) home.obterEventoPorId(id);
			apolice = (Apolice) sinistro.obterSuperior();
			temAspectos = false;
			
			for(Evento e : apolice.obterInferiores())
			{
				if(e instanceof AspectosLegais)
				{
					temAspectos = true;
					break;
				}
			}
			
			if(!temAspectos)
			{
				if(secoes.containsKey(apolice.obterSecao().obterApelido()))
				{
					linhaSuja = secoes.get(apolice.obterSecao().obterApelido()).toString();
					
					linha = linhaSuja.split(";");
					
					qtde = Integer.parseInt(linha[1]);
					qtde++;
					
					valor = Double.parseDouble(linha[2]);
					valor+=sinistro.obterMontanteGs();
					
					secoes.put(apolice.obterSecao().obterApelido(), apolice.obterSecao().obterId() + ";" + qtde + ";" + valor);
				}
				else
					secoes.put(apolice.obterSecao().obterApelido(), apolice.obterSecao().obterId() + ";" + 1 + ";" + sinistro.obterMontanteGs());
			}
		}
		
		return secoes.values();
	}
	
	public Collection<String> obterSecoesSinistrosVigenteJudicializado(Date dataInicio, Date dataFim) throws Exception
	{
		Map<String,String> secoes = new TreeMap<String,String>();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "select evento.id from evento,aspectos_legais,apolice where evento.id = aspectos_legais.id and superior = apolice.id";
		
		sql+=" and origem = " + this.obterId() + " and situacao_seguro = 'Vigente'";
		sql+=" and data_prevista_inicio>=" + dataInicio.getTime() + " and data_prevista_conclusao<=" + dataFim.getTime();
		sql+=" group by evento.id";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		long id;
		AspectosLegais aspecto;
		Apolice apolice;
		Sinistro sinistro;
		String linhaSuja;
		String[] linha;
		int qtde;
		double valor;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			
			aspecto = (AspectosLegais) home.obterEventoPorId(id);
			apolice = (Apolice) aspecto.obterSuperior();
			
			for(Evento e : apolice.obterInferiores())
			{
				if(e instanceof Sinistro)
				{
					sinistro = (Sinistro) e;
					
					if(sinistro.obterSituacao().equals("Pagado") && sinistro.obterDataSinistro().compareTo(dataInicio)>=0 && sinistro.obterDataSinistro().compareTo(dataFim)<=0)
					{					
						if(secoes.containsKey(apolice.obterSecao().obterApelido()))
						{
							linhaSuja = secoes.get(apolice.obterSecao().obterApelido()).toString();
							
							linha = linhaSuja.split(";");
							
							qtde = Integer.parseInt(linha[1]);
							qtde++;
							
							valor = Double.parseDouble(linha[2]);
							valor+=sinistro.obterMontanteGs();
							
							secoes.put(apolice.obterSecao().obterApelido(), apolice.obterSecao().obterId() + ";" + qtde + ";" + valor);
						}
						else
							secoes.put(apolice.obterSecao().obterApelido(), apolice.obterSecao().obterId() + ";" + 1 + ";" + sinistro.obterMontanteGs());
					}
				}
			}
		}
		
		//SQLQuery query = this.getModelManager().createSQLQuery("crm","select secao, count(*) as qtde,SUM(montante_gs) as soma from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and data_sinistro>=? and data_sinistro<=? and situacao_seguro = 'Vigente' group by secao");
		/*SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and data_sinistro>=? and data_sinistro<=? and situacao_seguro = 'Vigente'");
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Sinistro sinistro = (Sinistro) home.obterEventoPorId(id);
			Apolice apolice = (Apolice) sinistro.obterSuperior();
			boolean temAspectos = false;
			
			for(Iterator j = apolice.obterInferiores().iterator() ; j.hasNext() ; )
			{
				Evento e = (Evento) j.next();
				
				if(e instanceof AspectosLegais)
				{
					temAspectos = true;
					break;
				}
			}
			
			if(temAspectos)
			{
				if(secoes.containsKey(apolice.obterSecao().obterApelido()))
				{
					String linhaSuja = secoes.get(apolice.obterSecao().obterApelido()).toString();
					
					String[] linha = linhaSuja.split(";");
					
					int qtde = Integer.parseInt(linha[1]);
					qtde++;
					
					double valor = Double.parseDouble(linha[2]);
					valor+=sinistro.obterMontanteGs();
					
					secoes.put(apolice.obterSecao().obterApelido(), apolice.obterSecao().obterId() + ";" + qtde + ";" + valor);
				}
				else
					secoes.put(apolice.obterSecao().obterApelido(), apolice.obterSecao().obterId() + ";" + 1 + ";" + sinistro.obterMontanteGs());
			}
		}*/
		
		return secoes.values();
	}
	
	public Collection<String> obterSecoesSinistrosNoVigenteJudicializado(Date dataInicio, Date dataFim) throws Exception
	{
		Map<String,String> secoes = new TreeMap<String,String>();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "select evento.id from evento,aspectos_legais,apolice where evento.id = aspectos_legais.id and superior = apolice.id";
		
		sql+=" and origem = " + this.obterId() + " and situacao_seguro = 'No Vigente'";
		sql+=" and data_prevista_inicio>=" + dataInicio.getTime() + " and data_prevista_conclusao<=" + dataFim.getTime();
		sql+=" group by evento.id";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		long id;
		AspectosLegais aspecto;
		Apolice apolice;
		Sinistro sinistro;
		String linhaSuja;
		String[] linha;
		int qtde;
		double valor;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			
			aspecto = (AspectosLegais) home.obterEventoPorId(id);
			apolice = (Apolice) aspecto.obterSuperior();
			
			for(Evento e : apolice.obterInferiores())
			{
				if(e instanceof Sinistro)
				{
					sinistro = (Sinistro) e;
					
					if(sinistro.obterSituacao().equals("Pagado") && sinistro.obterDataSinistro().compareTo(dataInicio)>=0 && sinistro.obterDataSinistro().compareTo(dataFim)<=0)
					{					
						if(secoes.containsKey(apolice.obterSecao().obterApelido()))
						{
							linhaSuja = secoes.get(apolice.obterSecao().obterApelido()).toString();
							
							linha = linhaSuja.split(";");
							
							qtde = Integer.parseInt(linha[1]);
							qtde++;
							
							valor = Double.parseDouble(linha[2]);
							valor+=sinistro.obterMontanteGs();
							
							secoes.put(apolice.obterSecao().obterApelido(), apolice.obterSecao().obterId() + ";" + qtde + ";" + valor);
						}
						else
							secoes.put(apolice.obterSecao().obterApelido(), apolice.obterSecao().obterId() + ";" + 1 + ";" + sinistro.obterMontanteGs());
					}
				}
			}
		}
		
		/*Map secoes = new TreeMap();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		//SQLQuery query = this.getModelManager().createSQLQuery("crm","select secao,count(*) as qtde,SUM(montante_gs) as soma from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and data_sinistro>=? and data_sinistro<=? and situacao_seguro = 'No Vigente' group by secao");
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and data_sinistro>=? and data_sinistro<=? and situacao_seguro = 'No Vigente'");
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Sinistro sinistro = (Sinistro) home.obterEventoPorId(id);
			Apolice apolice = (Apolice) sinistro.obterSuperior();
			boolean temAspectos = false;
			
			for(Iterator j = apolice.obterInferiores().iterator() ; j.hasNext() ; )
			{
				Evento e = (Evento) j.next();
				
				if(e instanceof AspectosLegais)
				{
					temAspectos = true;
					break;
				}
			}
			
			if(temAspectos)
			{
				if(secoes.containsKey(apolice.obterSecao().obterApelido()))
				{
					String linhaSuja = secoes.get(apolice.obterSecao().obterApelido()).toString();
					
					String[] linha = linhaSuja.split(";");
					
					int qtde = Integer.parseInt(linha[1]);
					qtde++;
					
					double valor = Double.parseDouble(linha[2]);
					valor+=sinistro.obterMontanteGs();
					
					secoes.put(apolice.obterSecao().obterApelido(), apolice.obterSecao().obterId() + ";" + qtde + ";" + valor);
				}
				else
					secoes.put(apolice.obterSecao().obterApelido(), apolice.obterSecao().obterId() + ";" + 1 + ";" + sinistro.obterMontanteGs());
			}
		}*/
		
		return secoes.values();
	}
	
	public AgendaMovimentacao obterUltimaAgendaMCO() throws Exception
	{
		AgendaMovimentacao agenda = null;
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		/*
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -1);
		
		Date dataLimite = c.getTime();
		c.setTime(new Date());
		
		while(c.getTime().compareTo(dataLimite)>0)
		{
			String mes = new SimpleDateFormat("MM").format(c.getTime());
			String ano = new SimpleDateFormat("yyyy").format(c.getTime());
			
			//SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,agenda_movimentacao where evento.id = agenda_movimentacao.id and movimento_mes = ? and movimento_ano = ? and origem = ?");
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,agenda_movimentacao,fase where evento.id = agenda_movimentacao.id and agenda_movimentacao.id = fase.id and tipo = 'Contabil' and origem = ? and movimento_mes = ? and movimento_ano = ? and codigo = 'concluido' and termino = 0");
			query.addLong(this.obterId());
			query.addInt(Integer.parseInt(mes));
			query.addInt(Integer.parseInt(ano));
			
			long id = query.executeAndGetFirstRow().getLong("id");
			
			if(id > 0)
			{
				AgendaMovimentacao agenda2 = (AgendaMovimentacao) home.obterEventoPorId(id);
				{
					agenda = agenda2;
					break;
				}
			}
			c.add(Calendar.MONTH, -1);
		}*/
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select agenda_id from ultima_agenda where aseguradora_id = ? and tipo = 'Contabil'");
		query.addLong(this.obterId());
		
		long id = query.executeAndGetFirstRow().getLong("agenda_id");
		
		if(id > 0)
		{
			AgendaMovimentacao agenda2 = (AgendaMovimentacao) home.obterEventoPorId(id);
			agenda = agenda2;
		}
		
		return  agenda;
	}
	
	public AgendaMovimentacao obterUltimaAgendaMCI() throws Exception
	{
		AgendaMovimentacao agenda = null;
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		/*Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -1);
		
		Date dataLimite = c.getTime();
		c.setTime(new Date());
		
		while(c.getTime().compareTo(dataLimite)>0)
		{
			String mes = new SimpleDateFormat("MM").format(c.getTime());
			String ano = new SimpleDateFormat("yyyy").format(c.getTime());
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,agenda_movimentacao,fase where evento.id = agenda_movimentacao.id and agenda_movimentacao.id = fase.id and origem = ? and movimento_mes = ? and movimento_ano = ? and tipo = 'Instrumento' and codigo = 'concluido' and termino = 0");
			query.addLong(this.obterId());
			query.addInt(Integer.parseInt(mes));
			query.addInt(Integer.parseInt(ano));
			
			long id = query.executeAndGetFirstRow().getLong("id");
			
			if(id > 0)
			{
				AgendaMovimentacao agenda2 = (AgendaMovimentacao) home.obterEventoPorId(id);
				agenda = agenda2;
				break;
			}
			
			c.add(Calendar.MONTH, -1);
		}*/
		
		if(this.obterId() == 5230)
			System.out.println("");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select agenda_id from ultima_agenda where aseguradora_id = ? and tipo = 'Instrumento'");
		query.addLong(this.obterId());
		
		long id = query.executeAndGetFirstRow().getLong("agenda_id");
		
		if(id > 0)
		{
			AgendaMovimentacao agenda2 = (AgendaMovimentacao) home.obterEventoPorId(id);
			agenda = agenda2;
		}
		
		if(agenda == null)
		{
			query = this.getModelManager().createSQLQuery("crm","select top 1 evento.id from evento,agenda_movimentacao,fase where evento.id = agenda_movimentacao.id and agenda_movimentacao.id = fase.id and codigo = 'concluido' and termino = 0 and origem = ? and tipo = 'Instrumento' order by movimento_ano desc, movimento_mes desc");
			query.addLong(this.obterId());
			
			id = query.executeAndGetFirstRow().getLong("id");
			
			if(id > 0)
			{
				AgendaMovimentacao agenda2 = (AgendaMovimentacao) home.obterEventoPorId(id);
				agenda = agenda2;
			}
		}
		
		return  agenda;
		
		/*String retorno = ""; 
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select MAX(agenda_movimentacao.movimento_ano) as ano from evento,agenda_movimentacao,fase where evento.id = agenda_movimentacao.id and agenda_movimentacao.id = fase.id and codigo = 'concluido' and termino = 0 and origem = ? and tipo = 'Instrumento' ");
		query.addLong(this.obterId());
		
		int ano = query.executeAndGetFirstRow().getInt("ano");
		int mes = 0;

		if(ano > 0)
		{
			SQLQuery query2 = this.getModelManager().createSQLQuery("crm","select MAX(agenda_movimentacao.movimento_mes) as mes from evento,agenda_movimentacao,fase where evento.id = agenda_movimentacao.id and agenda_movimentacao.id = fase.id and codigo = 'concluido' and termino = 0 and origem = ? and tipo = 'Instrumento' and movimento_ano = ?");
			query2.addLong(this.obterId());
			query2.addInt(ano);
			
			mes = query2.executeAndGetFirstRow().getInt("mes");
			
			if(mes<10)
				retorno = "0" + mes + "/" + ano;
			else
				retorno = mes + "/" + ano;
		}
		
		return  retorno;*/
	}
	
	/*public AgendaMovimentacao obterUltimaAgendaMCI2() throws Exception
	{
		AgendaMovimentacao ag = null;
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String mesAno = this.obterUltimaAgendaMCI();
		
		if(mesAno.length() > 0)
		{
			String s[] = mesAno.split("/");
			
			String mes = "";
			String ano = "";
			
			mes = s[0];
			ano = s[1];
			
			if(mes.startsWith("0"))
				mes = mes.substring(1, mes.length());
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,agenda_movimentacao where evento.id = agenda_movimentacao.id and origem = ? and movimento_mes = ? and movimento_ano = ?");
			query.addLong(this.obterId());
			query.addInt(Integer.parseInt(mes));
			query.addInt(Integer.parseInt(ano));
			
			long id = query.executeAndGetFirstRow().getLong("id");
			
			ag = (AgendaMovimentacao) home.obterEventoPorId(id);
		}
		
		return  ag;
	}*/
	
	public AgendaMovimentacao obterAgendaMCISeguinte() throws Exception
	{
		AgendaMovimentacao ag = null;
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		AgendaMovimentacao ultimaAgenda = this.obterUltimaAgendaMCI();
		
		if(ultimaAgenda!=null)
		{
			String mesAno = "";
			
			int mes = ultimaAgenda.obterMesMovimento();
			int ano = ultimaAgenda.obterAnoMovimento();
			
			if(new Integer(mes).toString().length() == 1)
				mesAno = "0" + mes + "/" + ano;
			else
				mesAno = mes + "/" + ano;
			
			Date dataUltimaAgenda = new SimpleDateFormat("MM/yyyy").parse(mesAno);
			
			Calendar calendario = Calendar.getInstance();
			calendario.setTime(dataUltimaAgenda);
			calendario.add(Calendar.MONTH, 1);
			
			for(int i = 0 ; i < 2 ; i++)
			{
				SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,agenda_movimentacao,fase where evento.id = agenda_movimentacao.id and evento.id = fase.id and origem = ? and movimento_mes = ? and movimento_ano = ? and codigo='pendente' and termino = 0");
				query.addLong(this.obterId());
				query.addInt(calendario.get(Calendar.MONTH));
				query.addInt(calendario.get(Calendar.YEAR));
				
				long id = query.executeAndGetFirstRow().getLong("id");
				if(id > 0)
				{
					ag = (AgendaMovimentacao) home.obterEventoPorId(id);
					break;
				}
				
				calendario.add(Calendar.MONTH, 1);
			}
		}
		
		return  ag;
	}
	
	public Collection<Apolice> obterApolicesPorSecao(ClassificacaoContas cContas, Date dataInicio, Date dataFim, String situacao, String situacao2) throws Exception
	{
		Map<Long,Apolice> apolices = new TreeMap<Long,Apolice>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,apolice,sinistro where evento.id = sinistro.id and superior = apolice.id and origem = ? and secao = ? and data_sinistro>=? and data_sinistro<=? and situacao='Pagado' and situacao_seguro = ? and data_prevista_inicio>=? and data_prevista_conclusao<=?");
		query.addLong(this.obterId());
		query.addLong(cContas.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		query.addString(situacao2);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		//System.out.println("select evento.id from evento,apolice,sinistro where evento.id = sinistro.id and superior = apolice.id and origem = "+this.obterId()+" and secao = "+cContas.obterId()+" and data_sinistro>="+dataInicio.getTime()+" and data_sinistro<="+dataFim.getTime()+" and situacao='Pagado' and situacao_seguro = " + situacao);
		
		SQLRow[] rows = query.execute();
		long id;
		Sinistro sinistro;
		Apolice apolice;
		boolean judicializado;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			
			sinistro = (Sinistro) home.obterEventoPorId(id);
			
			apolice = (Apolice) sinistro.obterSuperior();
			
			if(situacao.equals("Vigente Judicializado") || situacao.equals("No Vigente Judicializado"))
			{
				for(Evento e : apolice.obterInferiores())
				{
					if(e instanceof AspectosLegais)
					{
						//apolices.put(new Long(apolice.obterDataPrevistaInicio().getTime() + i),apolice);
						apolices.put(apolice.obterDataPrevistaInicio().getTime() + apolice.obterId(),apolice);
						break;
					}
				}
			}
			else if (situacao.equals("Vigente") || situacao.equals("No Vigente"))
			{
				judicializado = false;
				
				for(Evento e : apolice.obterInferiores())
				{
					if(e instanceof AspectosLegais)
					{
						judicializado = true;
						break;
					}
				}
				if(!judicializado)
					apolices.put(apolice.obterDataPrevistaInicio().getTime() + apolice.obterId(),apolice);
			}
		}
		
		return apolices.values();
	}
	
	public Collection<Apolice> obterApolicesPorModalidade(Date dataInicio, Date dataFim, String secao, String modalidade) throws Exception
	{
		Collection<Apolice> apolices = new ArrayList<Apolice>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and origem = ? and data_emissao>=? and data_emissao<=? and plano.secao = ? and plano.plano = ? order by data_emissao");
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		query.addString(secao);
		query.addString(modalidade);
		
		//System.out.println("select evento.id from evento,apolice,sinistro where evento.id = sinistro.id and superior = apolice.id and origem = "+this.obterId()+" and secao = "+cContas.obterId()+" and data_sinistro>="+dataInicio.getTime()+" and data_sinistro<="+dataFim.getTime()+" and situacao='Pagado' and situacao_seguro = " + situacao);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Apolice apolice = (Apolice) home.obterEventoPorId(id);
			
			apolices.add(apolice);
		}
		
		return apolices;
	}
	
	public boolean existeAgendaNoPeriodo(int mes, int ano, String tipo) throws Exception
	{
	    SQLQuery query = getModelManager().createSQLQuery("crm", "select movimento_mes, movimento_ano from agenda_movimentacao,evento,fase where evento.id=agenda_movimentacao.id and origem=? and movimento_mes=? and movimento_ano=? and tipo=? and evento.id=fase.id and codigo='concluido' and termino = 0");
	    query.addLong(this.obterId());
	    query.addInt(mes);
	    query.addInt(ano);
	    query.addString(tipo);
	    SQLRow rows[] = query.execute();
	    if(rows.length > 0)
	        return true;
	    else
	    	return false;
	}
	
	public Map obterMargensSolvencia() throws Exception
	{
		Map margens = new TreeMap();
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select seq,mes_ano,valor from aseguradora_margen_solvencia where id = ?");
		query.addLong(this.obterId());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			int seq = rows[i].getInt("seq");
			String mesAno = rows[i].getString("mes_ano");
			double valor = rows[i].getDouble("valor");
			
			margens.put(new Integer(seq), new MargemSolvenciaImpl(this,seq,mesAno,valor));
		}
		
		return margens;
	}
	
	public MargemSolvencia obterMargemSolvencia(int seq) throws Exception
	{
		return (MargemSolvencia) this.obterMargensSolvencia().get(new Integer(seq));
	}
	
	public void adicionarMargemSolvencia(String mesAno, double valor) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select MAX(seq) as mx from aseguradora_margen_solvencia where id = ?");
		query.addLong(this.obterId());
		
		int seq = query.executeAndGetFirstRow().getInt("mx") + 1;
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into aseguradora_margen_solvencia values(?,?,?,?)");
		insert.addLong(this.obterId());
		insert.addInt(seq);
		insert.addString(mesAno);
		insert.addDouble(valor);
		
		insert.execute();
	}
	
	public void incluir() throws Exception
	{
		super.incluir();
		
		/*SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into aseguradora(id) values(?)");
		insert.addLong(this.obterId());
		
		insert.execute();*/
	}
	
	public double obterPorcentagemApolices(int totalPeriodo) throws Exception
	{
		double porcentagem = 0;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from evento,apolice where evento.id = apolice.id and origem = ?");
		query.addLong(this.obterId());
		
		double total = query.executeAndGetFirstRow().getDouble("qtde");
		
		if(total > 0)
			porcentagem = (totalPeriodo * 100) / total; 
		
		query = null;
		
		return porcentagem;
	}
	
	public double obterPorcentagemSinistros(int totalPeriodo) throws Exception
	{
		double porcentagem = 0;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from evento,sinistro where evento.id = sinistro.id and origem = ?");
		query.addLong(this.obterId());
		
		double total = query.executeAndGetFirstRow().getDouble("qtde");
		
		if(total > 0)
			porcentagem = (totalPeriodo * 100) / total; 
		
		query = null;
		
		return porcentagem;
	}
	
	public int obterQtdeApolicesPorPeriodo(Date dataInicio, Date dataFim, String secao, String situacaoSeguro) throws Exception
	{
		int qtde = 0;
		SQLQuery query = null;
		String situacao = "";
		String sql = "";
		
		if(secao.equals(""))
		{
			sql = "select count(*) as qtde from evento,apolice where evento.id = apolice.id and origem = ? and data_emissao>=? and data_emissao<= ?";
			
			if(situacaoSeguro.equals("acumulado"))
				situacao = " and situacao_seguro <> 'Anulada'";
			else
				situacao = " and situacao_seguro = 'Anulada'";
			
			sql+=situacao;
			
			query = this.getModelManager().createSQLQuery("crm",sql);
			query.addLong(this.obterId());
			query.addLong(dataInicio.getTime());
			query.addLong(dataFim.getTime());
			
			qtde = query.executeAndGetFirstRow().getInt("qtde");
		}
		else
		{
			sql = "select count(*) as qtde from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and origem = ? and data_emissao>=? and data_emissao<= ? and plano.secao = ?";
			
			if(situacaoSeguro.equals("acumulado"))
				situacao = " and situacao_seguro <> 'Anulada'";
			else
				situacao = " and situacao_seguro = 'Anulada'";
			
			sql+=situacao;
			
			query = this.getModelManager().createSQLQuery("crm",sql);
			query.addLong(this.obterId());
			query.addLong(dataInicio.getTime());
			query.addLong(dataFim.getTime());
			query.addString(secao);
			
			qtde = query.executeAndGetFirstRow().getInt("qtde");
		}
		
		query = null;
		
		return qtde;
	}
	
	public int obterQtdeApolicesPorPeriodo(Date dataInicio, Date dataFim, Plano plano) throws Exception
	{
		int qtde = 0;
		
		String sql = "select count(*) as qtde from evento,apolice where evento.id = apolice.id and origem = ? and data_emissao>=? and data_emissao<=? and apolice.plano = ?";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		query.addLong(plano.obterId());
		
		qtde = query.executeAndGetFirstRow().getInt("qtde");
		
		//System.out.println(plano.obterId() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataInicio.getTime()) + "ate " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataFim) + " Qtde: " + qtde);
		
		return qtde;
	}
	
	public int obterQtdeApolicesPorPeriodo(Date dataInicio, Date dataFim, String secao) throws Exception
	{
		int qtde = 0;
		
		String sql = "select count(*) as qtde from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and origem = ? and data_emissao>=? and data_emissao<=? and plano.secao = ?";
		//String sql = "SELECT apolice.plano, count(*) as qtde FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and origem = ? and data_emissao>=? and data_emissao<=? GROUP BY apolice.plano";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		query.addString(secao);
		
		qtde = query.executeAndGetFirstRow().getInt("qtde");
		
		return qtde;
	}
	
	public Collection<String> obterQtdeApolicesPorPeriodo(Date dataInicio, Date dataFim, boolean valores) throws Exception
	{
		Collection<String> dados = new ArrayList<String>();
		String sql = ""; 
				
		if(valores)
			sql = "SELECT plano.secao, count(*) as qtde,SUM(prima_gs) as primaGs, SUM(prima_me) as primaMe FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and origem = ? and data_emissao>=? and data_emissao<=? GROUP BY plano.secao order by plano.secao";
		else
			sql = "SELECT plano.secao, count(*) as qtde FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and origem = ? and data_emissao>=? and data_emissao<=? GROUP BY plano.secao order by plano.secao";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			String plano = rows[i].getString("secao");
			int qtde = rows[i].getInt("qtde");
			if(valores)
			{
				double primaGs = rows[i].getDouble("primaGs");
				double primaMe = rows[i].getDouble("primaMe");
				
				dados.add(plano + ";" + qtde + ";" + primaGs + ";" + primaMe);
			}
			else
				dados.add(plano + ";" + qtde + ";" + 0 + ";" + 0);
		}
		
		return dados;
	}
	
	public String[] obterQtdeApolicesPorPeriodoNOVO(Date dataInicio, Date dataFim, boolean valores, String secao, String modalidade, String ramo) throws Exception
	{
		String[] dados = new String[1];
		String sql = "";
		
		if(valores)
		{
			if(secao.equals("Sección no definida"))
				sql = "SELECT count(*) as qtde,SUM(prima_gs) as primaGs, SUM(prima_me) as primaMe FROM evento,apolice where evento.id = apolice.id and apolice.plano = 0 and origem = "+this.obterId()+" and data_emissao>="+dataInicio.getTime()+" and data_emissao<=" + dataFim.getTime();
			else
			{
				sql = "SELECT count(*) as qtde,SUM(prima_gs) as primaGs, SUM(prima_me) as primaMe FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and origem = "+this.obterId()+" and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" and plano.secao = '"+secao+"'";
				
				if(ramo!=null)
					sql+=" and ramo = '"+ramo+"'";
				if(!modalidade.equals(""))
					sql+=" and plano.plano = '"+modalidade+"'";
			}
		}
		else
		{
			if(secao.equals("Sección no definida"))
				sql = "SELECT count(*) as qtde FROM evento,apolice where evento.id = apolice.id and apolice.plano = 0 and origem = "+this.obterId()+" and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime();
			else
			{
				sql = "SELECT count(*) as qtde FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and origem = "+this.obterId()+" and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" and  plano.secao = '"+secao+"'";
				if(ramo!=null)
					sql+=" and ramo = '"+ramo+"'";
				
				if(!modalidade.equals(""))
					sql+=" and plano.plano = '"+modalidade+"'";
			}
		}
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		//System.out.println(sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			if(valores)
			{
				double primaGs = rows[i].getDouble("primaGs");
				double primaMe = rows[i].getDouble("primaMe");
				
				dados[i] = 0 + ";" + primaGs + ";" + primaMe;
			}
			else
			{
				int qtde = rows[i].getInt("qtde");
				dados[i] = qtde + ";" + 0 + ";" + 0;
			}
		}
		
		return dados;
	}
	
	public Map<String,String> obterNomePlanosPeriodo(Date dataInicio, Date dataFim, String plano, boolean admin, String ramo) throws Exception
	{
		Map<String,String> dados = new TreeMap<String,String>();
		String sql = ""; 
		
		if(plano.equals(""))
		{
			sql = "SELECT plano.secao FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and origem = "+this.obterId()+" and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime();
			
			if(ramo!=null)
				sql+=" and ramo = '"+ramo + "'";
			
			sql+=" GROUP BY plano.secao";
			
			if(!admin)
				sql+=" having Len(Rtrim(Ltrim(plano.secao))) > 0";
			
			sql+=" order by plano.secao";
		}
		else
		{
			sql = "SELECT plano.plano FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and origem = "+this.obterId()+" and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime();
			if(ramo!=null)
				sql+=" and ramo = '"+ramo + "' and plano.secao = '"+plano+"'";
			
			sql+=" GROUP BY plano.plano order by plano.plano";
		}
		
		System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			if(plano.equals(""))
			{
				String plano2 = rows[i].getString("secao").trim();
				if(plano2.length() > 0)
					dados.put(plano2+";-",plano2+";-");
			}
			else
			{
				String mod = rows[i].getString("plano").trim();
				if(mod.length() > 0)
					dados.put(plano+";"+mod,plano+";"+mod);
			}
		}
		
		//dados.put("ZZSección no definida;-","Sección no definida;-");
		
		return dados;
	}
	
	public int obterQtdeSinistrosPorPeriodo(Date dataInicio, Date dataFim, Plano plano) throws Exception
	{
		int qtde = 0;
		
		String sql = "select count(*) as qtde from evento,sinistro,apolice where evento.id = sinistro.id and evento.superior = apolice.id and  origem = ? and data_sinistro>=? and data_sinistro<= ? and apolice.plano = ?";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		query.addLong(plano.obterId());
		
		qtde = query.executeAndGetFirstRow().getInt("qtde");
		
		return qtde;
	}
	
	public int obterQtdeSinistrosPorPeriodo(Date dataInicio, Date dataFim, String secao) throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		int qtde = 0;
		
		String sql = "select count(*) as qtde from evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and origem = ? and data_sinistro>=? and data_sinistro<= ? and plano.secao = ?";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		query.addString(secao);
		
		qtde = query.executeAndGetFirstRow().getInt("qtde");
		
		return qtde;
	}
	
	public Collection<String> obterQtdeSinistrosPorPeriodo(Date dataInicio, Date dataFim) throws Exception
	{
		Collection<String> dados = new ArrayList<String>();
		
		//String sql = "select count(*) as qtde from evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and origem = ? and data_sinistro>=? and data_sinistro<= ? and plano.secao = ?";
		String sql = "SELECT count(*) as qtde FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and origem = ? and data_sinistro>= ? and data_sinistro<=? GROUP BY plano.secao";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			String plano = rows[i].getString("secao");
			int qtde = rows[i].getInt("qtde");
			
			dados.add(plano + ";" + qtde);
		}
		
		
		return dados;
	}
	
	public String[] obterQtdeSinistrosPorPeriodoNOVO(Date dataInicio, Date dataFim, boolean valores, String secao, String modalidade, String ramo) throws Exception
	{
		String[] dados = new String[1];
		
		String sql = "";
		if(valores)
		{
			if(secao.equals("Sección no definida"))
				sql = "SELECT SUM(fatura_sinistro.montante_pago) as montanteGs, SUM(fatura_sinistro.montante_me) as montanteMe FROM evento,sinistro,fatura_sinistro where evento.id = fatura_sinistro.id and superior = sinistro.id and origem = "+this.obterId()+" and sinistro.data_sinistro>= "+dataInicio.getTime()+" and sinistro.data_sinistro<= "+dataFim.getTime()+" and secao_apolice is null";
			else
			{
				sql = "SELECT SUM(fatura_sinistro.montante_pago) as montanteGs, SUM(fatura_sinistro.montante_me) as montanteMe FROM evento,sinistro,fatura_sinistro where evento.id = fatura_sinistro.id and superior = sinistro.id and origem = "+this.obterId()+" and sinistro.data_sinistro>= "+dataInicio.getTime()+" and sinistro.data_sinistro<= "+dataFim.getTime()+" and secao_apolice = '"+secao+"'";
				if(!modalidade.equals(""))
					sql+=" and plano_modalidade = '"+modalidade+"'";
			}
		}
		else
		{
			if(secao.equals("Sección no definida"))
				sql = "SELECT count(*) as qtde FROM evento,sinistro,apolice where evento.id = sinistro.id and superior = apolice.id and origem = "+this.obterId()+" and sinistro.data_sinistro>= "+dataInicio.getTime()+" and sinistro.data_sinistro<= "+dataFim.getTime()+" and apolice.plano = 0";
			else
			{
				sql = "SELECT count(*) as qtde FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and superior = apolice.id and apolice.plano = plano.id and origem = "+this.obterId()+" and sinistro.data_sinistro>= "+dataInicio.getTime()+" and sinistro.data_sinistro<= "+dataFim.getTime()+" and plano.secao = '"+secao+"'";
				
				if(ramo!=null)
					sql+=" and ramo = '"+ramo+"'";
				if(!modalidade.equals(""))
					sql+=" and plano.plano = '"+modalidade+"'";
			}
		}
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			if(valores)
			{
				double montanteGs = rows[i].getDouble("montanteGs");
				double montanteMe = rows[i].getDouble("montanteMe");
			
				dados[0] = 0 + "; " + montanteGs + ";"+ montanteMe;
			}
			else
			{
				int qtde = rows[i].getInt("qtde");
				dados[0] = qtde + "; " + 0 + ";"+ 0;
			}
		}
		
		return dados;
	}
	
	public Map<String,String> obterNomePlanosSinistrosPorPeriodo(Date dataInicio, Date dataFim, String plano, boolean admin, String ramo) throws Exception
	{
		Map<String,String> dados = new TreeMap<String,String>();
		String sql = "";
		
		if(plano.equals(""))
		{
			sql = "SELECT plano.secao FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and origem = ? and data_sinistro>= ? and data_sinistro<=?";
			if(ramo!=null)
				sql+=" and ramo = '"+ramo+"'";
			
			sql+=" GROUP BY plano.secao";
			if(!admin)
				sql+=" having Len(Rtrim(Ltrim(plano.secao)))> 0 ";
			
			sql+=" order by plano.secao";
		}
		else
		{
			sql = "SELECT plano.plano FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and origem = ? and data_sinistro>= ? and data_sinistro<=?";
			if(ramo!=null)
				sql+=" and ramo = '"+ramo+"' and plano.secao = '"+plano+"'";
			
			sql+=" GROUP BY plano.plano order by plano.plano";
		}
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			if(plano.equals(""))
			{
				String plano2 = rows[i].getString("secao").trim();
				if(plano2.length() > 0)
					dados.put(plano2+";-",plano2+";-");
			}
			else
			{
				String mod = rows[i].getString("plano").trim();
				if(mod.length() > 0)
					dados.put(plano+";"+mod,plano+";"+mod);
			}
		}
		
		//dados.put("ZZSección no definida;-","Sección no definida;-");
		
		return dados;
	}
	
	public String obterValoresSinistrosPorPeriodoRelSecaoAnual(Date dataInicio, Date dataFim, String secao) throws Exception
	{
		String dados = "";
		
		String sql = "SELECT evento.id FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and origem = ? and data_sinistro>= ? and data_sinistro<=? and plano.secao = ?";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		query.addString(secao);
		
		SQLRow[] rows = query.execute();
		
		double totalGs = 0;
		double totalMe = 0;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			sql = "select SUM(montante_pago) as montanteGs, SUM(montante_me) as montanteMe from evento, fatura_sinistro where evento.id = fatura_sinistro.id and superior = " + id;
			query = this.getModelManager().createSQLQuery("crm",sql);
			
			totalGs+=query.executeAndGetFirstRow().getDouble("montanteGs");
			totalMe+=query.executeAndGetFirstRow().getDouble("montanteMe");
		}
		
		dados+=totalGs + ";" + totalMe;
		
		return dados;
	}
	
	public int obterQtdeSinistrosPorPeriodo(Date dataInicio, Date dataFim, String secao, String situacaoSeguro) throws Exception
	{
		int qtde = 0;
		SQLQuery query = null;
		SQLQuery query2 = null;
		String sql = "";
		String sql2 = "";
		String situacao = "";
		
		if(secao.equals(""))
		{
			if(situacaoSeguro.equals("acumulado"))
			{
				sql = "select count(*) as qtde from evento,sinistro,apolice where evento.id = sinistro.id and evento.superior = apolice.id and  origem = ? and data_sinistro>=? and data_sinistro<= ? and situacao_seguro <> 'Anulada'";
				
				query = this.getModelManager().createSQLQuery("crm",sql);
				query.addLong(this.obterId());
				query.addLong(dataInicio.getTime());
				query.addLong(dataFim.getTime());
				
				qtde = query.executeAndGetFirstRow().getInt("qtde");
			}
			else
			{
				sql = "select count(*) as qtde from evento,sinistro,apolice where evento.id = sinistro.id and evento.superior = apolice.id and  origem = ? and data_sinistro>=? and data_sinistro<= ?";
				
				query = this.getModelManager().createSQLQuery("crm",sql);
				query.addLong(this.obterId());
				query.addLong(dataInicio.getTime());
				query.addLong(dataFim.getTime());
				
				qtde = query.executeAndGetFirstRow().getInt("qtde");
				
				sql2 = "select count(*) as qtde from evento,sinistro,apolice where evento.id = sinistro.id and evento.superior = apolice.id and  origem = ? and data_sinistro>=? and data_sinistro<= ? and situacao_seguro <> 'Anulada'";
				
				query2 = this.getModelManager().createSQLQuery("crm",sql2);
				query2.addLong(this.obterId());
				query2.addLong(dataInicio.getTime());
				query2.addLong(dataFim.getTime());
				
				qtde -= query2.executeAndGetFirstRow().getInt("qtde");
			}
		}
		else
		{
			if(situacaoSeguro.equals("acumulado"))
			{
				sql = "select count(*) as qtde from evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and origem = ? and data_sinistro>=? and data_sinistro<= ? and plano.secao = ? and situacao_seguro <> 'Anulada'";
				
				query = this.getModelManager().createSQLQuery("crm",sql);
				query.addLong(this.obterId());
				query.addLong(dataInicio.getTime());
				query.addLong(dataFim.getTime());
				query.addString(secao);
				
				qtde = query.executeAndGetFirstRow().getInt("qtde");
			}
			else
			{
				sql = "select count(*) as qtde from evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and origem = ? and data_sinistro>=? and data_sinistro<= ? and plano.secao = ?";
				
				query = this.getModelManager().createSQLQuery("crm",sql);
				query.addLong(this.obterId());
				query.addLong(dataInicio.getTime());
				query.addLong(dataFim.getTime());
				query.addString(secao);
				
				qtde = query.executeAndGetFirstRow().getInt("qtde");
				
				sql2 = "select count(*) as qtde from evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and origem = ? and data_sinistro>=? and data_sinistro<= ? and plano.secao = ? and situacao_seguro <> 'Anulada'";
				
				query2 = this.getModelManager().createSQLQuery("crm",sql2);
				query2.addLong(this.obterId());
				query2.addLong(dataInicio.getTime());
				query2.addLong(dataFim.getTime());
				query2.addString(secao);
				
				qtde -= query2.executeAndGetFirstRow().getInt("qtde");
			}
		}
		
		query = null;
		
		return qtde;
	}
	
	public void atualizarUltimoEnvioCorreio(Date data) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update controle_agenda set data_correio = ? where id = ?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public void atualizarComentarioControle(String comentario) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select id from controle_agenda where id = ?");
		query.addLong(this.obterId());
		
		if(query.execute().length == 0)
		{
			SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into controle_agenda(id,comentario) values(?,?)");
			insert.addLong(this.obterId());
			insert.addString(comentario);
			
			insert.execute();
		}
		else
		{
			SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update controle_agenda set comentario = ? where id = ?");
			update.addString(comentario);
			update.addLong(this.obterId());
			
			update.execute();
		}
	}
	
	public Date obterUltimoEnvioCorreio() throws Exception
	{
		Date data = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select data_correio from controle_agenda where id = ?");
		query.addLong(this.obterId());
		
		long dataLong = query.executeAndGetFirstRow().getLong("data_correio");
		
		if(dataLong > 0)
			data = new Date(dataLong);
		
		return data;
	}
	
	public String obterComentarioControle() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select comentario from controle_agenda where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("comentario");
	}
	
	public String[] obterQtdeApolicesPorModalidade(Date dataInicio, Date dataFim, String secao, String modalidade) throws Exception
	{
		String[] dados = new String[1];
		String sql = "";
		
		sql = "SELECT count(*) as qtde, SUM(capital_gs) as capital, SUM(prima_gs) as prima FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and origem = "+this.obterId()+" and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" and plano.secao = '"+secao+"'";
		if(!modalidade.equals(""))
			sql+=" and plano.plano = '"+modalidade+"'";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		//System.out.println(sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			int qtde = rows[i].getInt("qtde");
			double capital = rows[i].getDouble("capital");
			double prima = rows[i].getDouble("prima");
			
			sql = "SELECT SUM(caiptal_gs) as capital FROM evento,dados_reaseguro,apolice,plano where evento.id = dados_reaseguro.id and superior = apolice.id and apolice.plano = plano.id and origem = "+this.obterId()+" and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" and plano.secao = '"+secao+"'";
			if(!modalidade.equals(""))
				sql+=" and plano.plano = '"+modalidade+"'";
			
			query = this.getModelManager().createSQLQuery("crm",sql);
			double reaseguro = query.executeAndGetFirstRow().getDouble("capital");
			
			dados[i] = qtde + ";" + capital + ";" + prima+";"+reaseguro;
		}
		
		return dados;
	}
	
	public Collection<String> obterQtdePorTipoPessoa(Date dataInicio, Date dataFim, String ramo, String secao, String modalidade) throws Exception
	{
		Collection<String> dados = new ArrayList<String>();
		String sql = "";
		
		if(secao.equals(""))
		{
			sql = "select tipo_pessoa, plano.secao, count(*) as qtde from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
			if(ramo!=null)
				sql+=" and ramo = '"+ramo+"'";
			
			sql+=" and origem = " + this.obterId();
			sql+=" and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime() + " group by tipo_pessoa,plano.secao order by tipo_pessoa,plano.secao";
		}
		else if(modalidade.equals(""))
		{
			sql = "select tipo_pessoa, plano.plano, count(*) as qtde from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
			if(ramo!=null)
				sql+=" and ramo = '"+ramo+"' and plano.secao = '"+secao+"'";
			
			sql+=" and origem = " + this.obterId();
			sql+=" and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime() + " group by tipo_pessoa,plano.plano order by tipo_pessoa,plano.plano";
		}
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		String d = "", tipoPessoa;
		int qtde;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			tipoPessoa = rows[i].getString("tipo_pessoa");
			
			if(secao.equals(""))
				d = rows[i].getString("secao");
			else if(modalidade.equals(""))
				d = rows[i].getString("plano");
			
			qtde = rows[i].getInt("qtde");
			
			dados.add(tipoPessoa + ";"+d+";"+qtde);
		}
		
		return dados;
	}
	
	public Collection<String> obterPlanosPorTipoPessoa(Date dataInicio, Date dataFim, String ramo, String secao, String modalidade) throws Exception
	{
		Collection<String> dados = new ArrayList<String>();
		String sql = "";
		
		//if(secao.equals(""))
		//{
			sql = "select plano.secao from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
			if(ramo!=null)
				sql+=" and ramo = '"+ramo+"'";
			
			sql+=" and origem = " + this.obterId();
			sql+=" and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime() + " group by plano.secao order by plano.secao";
		/*}
		else if(modalidade.equals(""))
		{
			sql = "select plano.plano from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
			if(ramo!=null)
				sql+=" and ramo = '"+ramo+"' and plano.secao = '"+secao+"'";
			
			sql+=" and origem = " + this.obterId();
			sql+=" and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime() + " group by plano.plano order plano.plano";
		}*/
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		String d = "";
		int qtde;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			//if(secao.equals(""))
				d = rows[i].getString("secao");
			//else if(modalidade.equals(""))
				//d = rows[i].getString("plano");
			
			dados.add(d);
		}
		
		return dados;
	}
	
	public String obterQtdePorTipoPessoa2(Date dataInicio, Date dataFim, String ramo, String secao, String modalidade) throws Exception
	{
		String sql = "";
		String dados = "";
		
		sql = "select tipo_pessoa, count(*) as qtde from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
		
		if(ramo!=null)
			sql+=" and ramo = '"+ramo+"'";
		if(!secao.equals(""))
			sql+=" and plano.secao = '"+secao+"'";
		if(!modalidade.equals(""))
			sql+=" and plano.plano = '"+modalidade+"'";
		
		sql+=" and origem = " + this.obterId();
		sql+=" and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime();
		
		sql+=" group by tipo_pessoa having count(*) > 0 order by tipo_pessoa";
		
		System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		int qtde;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			qtde = rows[i].getInt("qtde");
					
			if(dados.length() == 0)
				dados = qtde+"";
			else
				dados +="@"+qtde;
		}
		
		return dados;
	}
	
	public void atualizarGrupoAlertaTrempana(String grupo) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from grupo_alerta_trempana where id = ?");
		query.addLong(this.obterId());
		
		int qtde = query.executeAndGetFirstRow().getInt("qtde");
		
		if(qtde == 0)
		{
			SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into grupo_alerta_trempana(id,grupo) values(?,?)");
			insert.addLong(this.obterId());
			insert.addString(grupo);
			
			insert.execute();
		}
		else
		{
			SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update grupo_alerta_trempana set grupo = ? where id = ?");
			update.addString(grupo);
			update.addLong(this.obterId());
			
			update.execute();
		}
	}
	
	public String obterGrupoAlertaTrempana() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select grupo from grupo_alerta_trempana where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("grupo");
	}
}