package com.gvs.crm.model.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Inscricao;
import com.gvs.crm.model.MovimentacaoFinanceiraConta;
import com.gvs.crm.model.Raiz;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.config.InfraProperties;
import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class EventoHomeImpl extends Home implements EventoHome {
	private HashMap eventosPorId = new HashMap();

	private double totalCredito;

	private double totalDebito;

	private double total;

	private double totalSaldoAnterior;

	private double totalSaldoAnteriorMoedaEstrangeira;

	private double totalSaldoMoedaEstrangeira;

	private Collection colecao;

	private int tamanho;

	private int pagina;

	public Collection obterTeste(int pagina) throws Exception
	{
		Collection colecao = new ArrayList();
		
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id, classe from evento,agenda_movimentacao where evento.id = agenda_movimentacao.id and classe = 'AgendaMovimentacao' order by evento.id");
		query.setCurrentPage(pagina);
		query.setRowsByPage(20);

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");

			String classe = rows[i].getString("classe");

			AgendaMovimentacao agenda = (AgendaMovimentacao) this
					._instanciarEvento(classe, id);

			colecao.add(agenda);
		}

		return colecao;
	}

	protected EventoImpl _instanciarEvento(String classe, long id)throws Exception 
	{
		EventoImpl eventoImpl = (EventoImpl) this.eventosPorId.get(new Long(id));
		if (eventoImpl == null) 
		{
			if (classe == null || classe.equals("")) 
			{
				//SQLQuery query = this.getModelManager().createSQLQuery("crm","select classe from evento where id=?");
				SQLQuery query = this.getModelManager().createSQLQuery("crm","EXEC instanciarEvento ?");
				query.addLong(id);
				if(query.execute().length > 0)
					classe = query.execute()[0].getString("classe");
			}
			if(classe!=null)
			{
				eventoImpl = (EventoImpl) this.getModelManager().getEntity(classe);
				eventoImpl.atribuirId(id);
				this.eventosPorId.put(new Long(id), eventoImpl);
			}
		}
		return eventoImpl;
	}

	protected Collection<Evento> _instanciarEventos(SQLRow[] rows) throws Exception 
	{
		ArrayList<Evento> eventos = new ArrayList<>();
		for (int i = 0; i < rows.length; i++)
			
			eventos.add(this._instanciarEvento(rows[i].getString("classe"), rows[i].getLong("id")));
		return eventos;
	}

	public Collection localizarEventos(String pesquisa) throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select id, classe from evento where classe<>'DadosReaseguro' and classe<>'DadosCoaseguro' and classe<>'Sinistro' and classe<>'FaturaSinistro' and classe<>'AnulacaoInstrumento' and classe<>'RegistroCobranca'" +
						" and classe<>'AspectosLegais' and classe<>'Suplemento' and classe<>'Refinacao' and classe<>'RegistroGastos' and classe<>'RegistroAnulacao' and classe<>'Morosidade' and titulo like ? or descricao like ?");
		query.addString("%" + pesquisa + "%");
		query.addString("%" + pesquisa + "%");
		return this._instanciarEventos(query.execute());
	}

	public Collection localizarApolices(String pesquisa, String aseguradora,String secao) throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery(	"crm",
						"SELECT evento.id, evento.classe FROM evento CROSS JOIN apolice CROSS JOIN entidade "
								+ "WHERE (evento.titulo LIKE ?) AND (evento.classe = 'Apolice') OR (evento.classe = 'Apolice') AND (apolice.numero_apolice LIKE ?) OR (evento.classe = 'Apolice') "
								+ "AND (entidade.apelido = ?) OR (evento.classe = 'Apolice') AND (entidade.sigla = ?) GROUP BY evento.id, evento.classe");
		query.addString("%" + pesquisa + "%");
		query.addString("%" + pesquisa + "%");
		query.addString(secao);
		query.addString(aseguradora);

		return this._instanciarEventos(query.execute());
	}

	public Collection localizarEventos(String pesquisa, String classe)throws Exception 
	{
		SQLQuery query;
		if (classe == null || classe.length() == 0) 
		{
			query = this.getModelManager().createSQLQuery("crm","select id, classe from evento where titulo like ?");
		} 
		else 
		{
			StringTokenizer st = new StringTokenizer(classe, ",");
			String s = "";
			int i = 0;
			while (st.hasMoreTokens())
			{
				String m = st.nextToken();

				if (i == 0)
					s = "'" + m + "'";
				else
					s += " or classe=" + "'" + m + "'";

				i++;
			}

			query = this.getModelManager().createSQLQuery("crm","select id, classe from evento where (classe=" + s + ") and titulo like ?");
		}

		query.addString("%" + pesquisa + "%");

		return this._instanciarEventos(query.execute());
	}

	public Collection localizarForuns(String assunto, String tipo)
			throws Exception {
		SQLQuery query = null;

		if (assunto == null)
		{
			query = this.getModelManager().createSQLQuery("crm","select id, classe from evento where classe='Forum' and tipo like ?");
			query.addString("%" + tipo + "%");
		}
		else if (tipo == null)
		{
			query = this.getModelManager().createSQLQuery("crm","select id, classe from evento where classe='Forum' and titulo like ?");
			query.addString("%" + assunto + "%");
		}
		else
		{
			query = this.getModelManager().createSQLQuery("crm","select id, classe from evento where classe='Forum' and (titulo like ? and tipo like ?)");
			query.addString("%" + assunto + "%");
			query.addString("%" + tipo + "%");
		}
		
		return this._instanciarEventos(query.execute());
	}

	public Collection obterAgendas(boolean fasePendente) throws Exception
	{
		SQLQuery query = null;

		if (fasePendente)
		{
			query = this.getModelManager().createSQLQuery("crm","select evento.id, classe from evento,fase,agenda_movimentacao where evento.id=agenda_movimentacao.id and evento.id=fase.id and fase.termino=0 and fase.codigo=?");
			query.addString(Evento.EVENTO_PENDENTE);
		}
		else
			query = this.getModelManager().createSQLQuery("crm","select evento.id, classe from evento,agenda_movimentacao where evento.id=agenda_movimentacao.id");

		return this._instanciarEventos(query.execute());
	}
	
	public Collection obterAgendas(String fase) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id, classe from evento,fase,agenda_movimentacao where evento.id=agenda_movimentacao.id and evento.id=fase.id and fase.termino=0 and fase.codigo=? and tipo='Instrumento' order by movimento_ano,movimento_mes");
		query.addString(fase);
		//classe='AgendaMovimentacao' 

		return this._instanciarEventos(query.execute());
	}

	public Collection obterMesAnoAgendas() throws Exception {
		Collection mesAno = new ArrayList();

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select movimento_mes,movimento_ano from evento,agenda_movimentacao where classe = ? and evento.id = agenda_movimentacao.id group by movimento_mes,movimento_ano");
		query.addString("AgendaMovimentacao");

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
			mesAno.add(new Integer(rows[i].getInt("movimento_mes")).toString()
					+ new Integer(rows[i].getInt("movimento_ano")).toString());

		return mesAno;
	}

	public Collection obterMesAnoBalanco(Aseguradora aseguradora)
			throws Exception {
		Map mesAno = new TreeMap();

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"SELECT mes_ano FROM relatorio WHERE seguradora=? GROUP BY mes_ano");
		query.addLong(aseguradora.obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
			mesAno.put(rows[i].getString("mes_ano"), rows[i]
					.getString("mes_ano"));
		return mesAno.values();
	}

	public String obterClasseDescricao(String classe) throws Exception {
		return InfraProperties.getInstance().getProperty(classe + ".descricao");
	}

	public String obterClasseGrupo(String classe) throws Exception {
		return InfraProperties.getInstance().getProperty(classe + ".grupo");
	}

	public Collection obterClasses(Evento superior) throws Exception {
		InfraProperties ip = InfraProperties.getInstance();
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this
				.getModelManager().getUser());
		Entidade empresa = usuarioAtual.obterEmpresa();
		String apelidoEmpresa = empresa.obterApelido();
		if (apelidoEmpresa == null)
			apelidoEmpresa = "";
		StringTokenizer st;
		if (superior == null)
			st = new StringTokenizer(ip.getProperty("eventos"), ",");
		else
			st = new StringTokenizer(ip.getProperty(superior.obterClasse()
					+ ".inferiores"), ",");
		List classes = new ArrayList();
		if (empresa instanceof Raiz || !apelidoEmpresa.equals("")) {
			while (st.hasMoreTokens()) {
				String classe = st.nextToken();
				String empresas = ip.getProperty(classe + ".empresa");
				if (empresas.equals("*"))
					classes.add(classe);
				else if (!apelidoEmpresa.equals("")
						&& empresas.indexOf(apelidoEmpresa) >= 0)
					classes.add(classe);
			}
		}
		return classes;
	}

	public Collection obterClassesDescricao(Evento superior) throws Exception {
		InfraProperties ip = InfraProperties.getInstance();
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this
				.getModelManager().getUser());
		Entidade empresa = usuarioAtual.obterEmpresa();
		String apelidoEmpresa = empresa.obterApelido();
		if (apelidoEmpresa == null)
			apelidoEmpresa = "";
		StringTokenizer st;
		if (superior == null)
			st = new StringTokenizer(ip.getProperty("eventos"), ",");
		else
			st = new StringTokenizer(ip.getProperty(superior.obterClasse()
					+ ".inferiores"), ",");
		List classes = new ArrayList();
		if (empresa instanceof Raiz || !apelidoEmpresa.equals("")) {
			while (st.hasMoreTokens()) {
				String classe = st.nextToken();
				String descricao = ip.getProperty(classe + ".descricao");
				classes.add(descricao);
			}
		}
		return classes;
	}

	public Collection<Evento> obterCompromissos(Usuario responsavel,Date dataPrevistaInicio) throws Exception
	{
		Calendar c = Calendar.getInstance();
		c.setTime(dataPrevistaInicio);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date data1 = c.getTime();
		c.add(Calendar.DAY_OF_YEAR, 1);
		Date data2 = c.getTime();
		String classes = InfraProperties.getInstance().getProperty("eventos.compromisso");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select id,classe from evento where (evento.responsavel=? or evento.responsavel=?) and data_prevista_inicio>=? and data_prevista_inicio<? order by data_prevista_inicio,data_prevista_conclusao");
		//SQLQuery query = this.getModelManager().createSQLQuery("crm","EXEC obterCompromissos ?,?,?,?");
		query.addLong(responsavel.obterId());
		query.addLong(responsavel.obterSuperior().obterId());
		query.addLong(data1.getTime());
		query.addLong(data2.getTime());

		SQLRow[] rows = query.execute();

		Collection<Evento> eventos = new ArrayList<Evento>();
		long id;
		String classe;
		
		for (int i = 0; i < rows.length; i++) 
		{
			id = rows[i].getLong("id");
			classe = rows[i].getString("classe").toLowerCase();
			if (classes.indexOf(classe) >= 0)
				eventos.add(this._instanciarEvento(classe, id));
		}
		return eventos;
	}

	public Evento obterEventoPorId(long id) throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select classe from evento where id=?");
		query.addLong(id);
		SQLRow row = query.executeAndGetFirstRow();
		return this._instanciarEvento(row.getString("classe"), id);
	}

	public Collection<Evento> obterEventosInferiores(Evento superior) throws Exception 
	{
		Collection<Evento> inferiores = new ArrayList<>();

		if (superior.obterId() > 0) 
		{
			//SQLQuery query = this.getModelManager().createSQLQuery("crm","select id, classe from evento where superior=? order by prioridade,criacao DESC");
			SQLQuery query = this.getModelManager().createSQLQuery("crm","EXEC obterEventosInferiores ?");
			query.addLong(superior.obterId());
			inferiores = this._instanciarEventos(query.execute());
		}

		return inferiores;
	}

	public Collection obterEventosPorCriador(Usuario criador) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select id, classe from evento where criador=? order by prioridade,criacao DESC");
		query.addLong(criador.obterId());
		return this._instanciarEventos(query.execute());
	}

	public Collection obterEventosPorCriadorHistorico(Usuario criador, int pagina) throws Exception 
	{
		StringTokenizer eventos = new StringTokenizer(InfraProperties.getInstance().getProperty("eventos.historicoeventos.criador"),",");
		
		String classe = null;

		while (eventos.hasMoreTokens()) 
		{
			String nomeEvento = eventos.nextToken();

			if (classe == null)
				classe = "'" + nomeEvento + "'";
			else
				classe += " or classe = " + "'" + nomeEvento + "'";
		}

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select id, classe from evento where criador=? and (classe = "	+ classe + ") order by prioridade,criacao DESC");
		
		//String sql = "SELECT TOP (30) id FROM evento WHERE (id NOT IN(SELECT TOP ("+pagina+" * 30 ) id from evento where criador = ? and (classe = "+classe+"))) where criador = ? and (classe = "+classe+") order by prioridade,criacao DESC";
		//SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(criador.obterId());
		//query.addLong(criador.obterId());
		query.setCurrentPage(pagina);
		query.setRowsByPage(30);
		
		return this._instanciarEventos(query.execute());
	}

	public Collection obterEventosPorDestino(Entidade destino) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select id, classe from evento where destino=? order by prioridade,criacao DESC");
		query.addLong(destino.obterId());
		return this._instanciarEventos(query.execute());
	}

	public Collection obterEventosPorDestinoHistorico(Entidade destino, int pagina)	throws Exception 
	{
		StringTokenizer eventos = new StringTokenizer(InfraProperties.getInstance().getProperty("eventos.historicoeventos.destino"),",");
		String classe = null;

		while (eventos.hasMoreTokens()) 
		{
			String nomeEvento = eventos.nextToken();

			if (classe == null)
				classe = "'" + nomeEvento + "'";
			else
				classe += " or classe = " + "'" + nomeEvento + "'";
		}

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select id, classe from evento where destino=? and (classe = "	+ classe + ") order by prioridade,criacao DESC");
		//String sql = "SELECT TOP (30) id FROM evento WHERE (id NOT IN(SELECT TOP ("+pagina+" * 30 ) id from evento where destino = ? and (classe = "+classe+"))) where destino = ? and (classe = "+classe+") order by prioridade,criacao DESC";
		//SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(destino.obterId());
		//query.addLong(destino.obterId());
		query.setCurrentPage(pagina);
		query.setRowsByPage(30);
		return this._instanciarEventos(query.execute());
	}

	public Collection obterEventosPorOrigem(Entidade origem) throws Exception {
		Collection eventosPorOrigem = new ArrayList();

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select id, classe from evento where origem=? order by prioridade,criacao DESC");
		query.addLong(origem.obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
			eventosPorOrigem.add(this._instanciarEvento(rows[i]
					.getString("classe"), rows[i].getLong("id")));

		SQLQuery query2 = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id, classe from evento,evento_entidades where evento.id = evento_entidades.id and evento_entidades.origem=? order by prioridade,criacao DESC");
		query2.addLong(origem.obterId());

		SQLRow[] rows2 = query2.execute();

		for (int i = 0; i < rows2.length; i++) {
			Evento e = this._instanciarEvento(rows2[i].getString("classe"),
					rows2[i].getLong("id"));
			if (!eventosPorOrigem.contains(e))
				eventosPorOrigem.add(e);
		}

		return eventosPorOrigem;
	}

	public Collection obterEventosPorOrigemHistorico(Entidade origem, int pagina)throws Exception 
	{
		StringTokenizer eventos = new StringTokenizer(InfraProperties.getInstance().getProperty("eventos.historicoeventos.origem"),	",");
		String classe = null;

		while (eventos.hasMoreTokens()) 
		{
			String nomeEvento = eventos.nextToken();

			if (classe == null)
				classe = "'" + nomeEvento + "'";
			else
				classe += " or classe = " + "'" + nomeEvento + "'";
		}

		Collection eventosPorOrigem = new ArrayList();

		SQLQuery query = this.getModelManager().createSQLQuery(	"crm","select id, classe from evento where origem=? and (classe = "	+ classe + ") order by prioridade,criacao DESC");
		query.addLong(origem.obterId());
		query.setCurrentPage(pagina);
		query.setRowsByPage(30);
		
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
			eventosPorOrigem.add(this._instanciarEvento(rows[i]	.getString("classe"), rows[i].getLong("id")));

		SQLQuery query2 = this.getModelManager().createSQLQuery("crm","select evento.id, classe from evento,evento_entidades where evento.id = evento_entidades.id and evento_entidades.origem=? and (classe = "+ classe + ") order by prioridade,criacao DESC");
		query2.addLong(origem.obterId());
		query2.setCurrentPage(pagina);
		query2.setRowsByPage(30);

		SQLRow[] rows2 = query2.execute();

		for (int i = 0; i < rows2.length; i++) 
		{
			Evento e = this._instanciarEvento(rows2[i].getString("classe"),	rows2[i].getLong("id"));
			if (!eventosPorOrigem.contains(e))
				eventosPorOrigem.add(e);
		}

		return eventosPorOrigem;
	}

	public Collection obterEventosPorResponsavel(Entidade responsavel)
			throws Exception {
		Collection eventosPorResponsavel = new ArrayList();

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select id, classe from evento where responsavel=? order by prioridade,criacao DESC");
		query.addLong(responsavel.obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
			eventosPorResponsavel.add(this._instanciarEvento(rows[i]
					.getString("classe"), rows[i].getLong("id")));

		SQLQuery query2 = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id, classe from evento,evento_entidades where evento.id = evento_entidades.id and evento_entidades.responsavel=? order by prioridade,criacao DESC");
		query2.addLong(responsavel.obterId());

		SQLRow[] rows2 = query2.execute();

		for (int i = 0; i < rows2.length; i++) {
			Evento e = this._instanciarEvento(rows2[i].getString("classe"),
					rows2[i].getLong("id"));
			if (!eventosPorResponsavel.contains(e))
				eventosPorResponsavel.add(e);
		}

		return eventosPorResponsavel;
	}

	public Collection obterEventosPorResponsavelHistorico(Entidade responsavel, int pagina)throws Exception 
	{
		StringTokenizer eventos = new StringTokenizer(InfraProperties.getInstance().getProperty("eventos.historicoeventos.responsavel"), ",");
		String classe = null;

		while (eventos.hasMoreTokens()) 
		{
			String nomeEvento = eventos.nextToken();

			if (classe == null)
				classe = "'" + nomeEvento + "'";
			else
				classe += " or classe = " + "'" + nomeEvento + "'";
		}

		Collection eventosPorResponsavel = new ArrayList();

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select id, classe from evento where responsavel=? and (classe = "	+ classe + ") order by prioridade,criacao DESC");
		//String sql = "SELECT TOP (30) id FROM evento WHERE (id NOT IN(SELECT TOP ("+pagina+" * 30 ) id from evento where responsavel = ? and (classe = "+classe+"))) where responsavel = ? and (classe = "+classe+") order by prioridade,criacao DESC";
		//SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(responsavel.obterId());
		//query.addLong(responsavel.obterId());
		query.setCurrentPage(pagina);
		query.setRowsByPage(30);
		
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
			eventosPorResponsavel.add(this._instanciarEvento(rows[i].getString("classe"), rows[i].getLong("id")));

		SQLQuery query2 = this.getModelManager().createSQLQuery("crm","select evento.id, classe from evento,evento_entidades where evento.id = evento_entidades.id and evento_entidades.responsavel=? and (classe = "+ classe + ") order by prioridade,criacao DESC");
		//String sql2 = "SELECT TOP (30) evento.id,classe FROM evento,evento_entidades WHERE (evento.id NOT IN(SELECT TOP ("+pagina+" * 30) evento.id,classe FROM evento,evento_entidades where evento.id = evento_entidades.id and evento_entidades.responsavel=? and (classe = "+classe+"))) where evento.id = evento_entidades.id and evento_entidades.responsavel=? and (classe = "+ classe + ") order by prioridade,criacao DESC";
		//SQLQuery query2 = this.getModelManager().createSQLQuery("crm",sql2);
		query2.addLong(responsavel.obterId());
		//query2.addLong(responsavel.obterId());
		query2.setCurrentPage(pagina);
		query2.setRowsByPage(30);

		SQLRow[] rows2 = query2.execute();

		for (int i = 0; i < rows2.length; i++) 
		{
			Evento e = this._instanciarEvento(rows2[i].getString("classe"),	rows2[i].getLong("id"));
			if (!eventosPorResponsavel.contains(e))
				eventosPorResponsavel.add(e);
		}

		return eventosPorResponsavel;
	}

	public Collection obterEventosAgenda(String mesAno, int pagina) throws Exception {
		Collection agendas = new ArrayList();

		//select movimento_mes,movimento_ano from evento,agenda_movimentacao
		// where evento.id = agenda_movimentacao.id group by
		// movimento_mes,movimento_ano

		if (mesAno != null && !mesAno.equals("")) 
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id, classe from evento,agenda_movimentacao where evento.id = agenda_movimentacao.id and classe=? and movimento_mes=? and movimento_ano=? order by prioridade,criacao DESC");
			query.addString("AgendaMovimentacao");

			if (mesAno.length() == 5) 
			{
				query.addInt(Integer.parseInt(mesAno.substring(0, 1)));
				query.addInt(Integer.parseInt(mesAno.substring(1, mesAno.length())));
			} 
			else 
			{
				query.addInt(Integer.parseInt(mesAno.substring(0, 2)));
				query.addInt(Integer.parseInt(mesAno.substring(2, mesAno.length())));
			}
			
			//query.setCurrentPage(pagina);
			//query.setRowsByPage(30);
			

			SQLRow[] rows = query.execute();

			for (int i = 0; i < rows.length; i++) 
			{
				long id = rows[i].getLong("id");
				AgendaMovimentacao agenda = (AgendaMovimentacao) this.obterEventoPorId(id);
				agendas.add(agenda);
			}
		}

		return agendas;
	}

	public Collection obterEventosPorResponsavelTecnico(
			Entidade responsavelTecnico) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id, evento.classe from evento,pedido where evento.id=pedido.id and pedido.comissionado=? order by prioridade,criacao DESC");
		query.addLong(responsavelTecnico.obterId());
		return this._instanciarEventos(query.execute());

	}

	public Collection obterEventosSuperiores(Evento evento) throws Exception {
		ArrayList superiores = new ArrayList();
		Evento s = evento.obterSuperior();
		while (s != null) {
			superiores.add(s);
			s = s.obterSuperior();
		}
		return superiores;
	}

	public Evento obterEventoSuperior(Evento evento) throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select superior from evento where id=?");
		query.addLong(evento.obterId());
		SQLRow row = query.executeAndGetFirstRow();
		long superiorId = row.getLong("superior");
		if (superiorId == 0)
			return null;
		else
			return this._instanciarEvento(null, superiorId);
	}

	public Collection obterInscricoesPendentes() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id, classe from evento,fase where classe='inscricao' and evento.id = fase.id and fase.codigo='pendente' and termino = 0");

		SQLRow[] rows = query.execute();

		Map inscricoes = new TreeMap();

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");

			String classe = rows[i].getString("classe");

			Inscricao inscricao = (Inscricao) this
					._instanciarEvento(classe, id);

			inscricoes.put(new Long(inscricao.obterCriacao().getTime()),
					inscricao);
		}

		return inscricoes.values();
	}

	public Collection obterInscricoes(int pagina) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id, inscricao, classe from evento,inscricao where evento.id = inscricao.id order by inscricao");
		query.setCurrentPage(pagina);
		query.setRowsByPage(30);

		SQLRow[] rows = query.execute();

		Map inscricoes = new TreeMap();

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");

			String classe = rows[i].getString("classe");

			Inscricao inscricao = (Inscricao) this
					._instanciarEvento(classe, id);

			if (inscricao.obterOrigem() != null)
				inscricoes.put(new Long(inscricao.obterCriacao().getTime()),
						inscricao);
		}

		return inscricoes.values();
	}

	public Collection obterTiposEntidadeInscricao() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select entidade.classe as classe from evento,inscricao,entidade where evento.id = inscricao.id and origem = entidade.id group by entidade.classe");

		SQLRow[] rows = query.execute();

		Map tipos = new TreeMap();

		for (int i = 0; i < rows.length; i++) {
			String tipo = rows[i].getString("classe");

			if (tipo.equals("Corretora"))
				tipos.put("Corredora de Reaseguro", "Corredora de Reaseguro");
			else if (tipo.equals("AuxiliarSeguro")) {
				tipos.put("Agentes de Seguros", "Agentes de Seguros");
				tipos.put("Corredores de Seguros", "Corredores de Seguros");
				tipos.put("Liquidadores de Siniestros",
						"Liquidadores de Siniestros");
			} else if (tipo.equals("AuditorExterno"))
				tipos.put("Auditor Externo", "Auditor Externo");
			else
				tipos.put(tipo, tipo);
		}

		return tipos.values();
	}

	public Collection obterPendencias(Usuario responsavel) throws Exception
	{
		StringTokenizer eventosStr = new StringTokenizer(InfraProperties.getInstance().getProperty("eventos.pendencia"), ",");

		String classe = null,nomeEvento;

		while (eventosStr.hasMoreTokens())
		{
			nomeEvento = eventosStr.nextToken();

			if (classe == null)
				classe = "'" + nomeEvento + "'";
			else
				classe += " or classe = " + "'" + nomeEvento + "'";
		}

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id,evento.classe from evento,fase where evento.id=fase.id and (evento.responsavel=? or evento.responsavel=?) and (classe = "
								+ classe
								+ ") and fase.termino=0 and fase.codigo<>? and fase.codigo<>'cancelado' and fase.codigo<>'aceito' and fase.codigo<>'recusado' and fase.codigo<>'reprovado' order by evento.prioridade,evento.criacao DESC");
		query.addLong(responsavel.obterId());
		query.addLong(responsavel.obterSuperior().obterId());
		query.addString(Evento.EVENTO_CONCLUIDO);
		SQLRow[] rows = query.execute();

		Collection eventos = new ArrayList();
		long id;
		String classe2;
		Evento e;
		
		for (int i = 0; i < rows.length; i++)
		{
			id = rows[i].getLong("id");
			classe2 = rows[i].getString("classe").toLowerCase().trim();
			
			e = this._instanciarEvento(classe2, id);
			eventos.add(e);
		}

		SQLQuery query2 = this.getModelManager().createSQLQuery("crm",
						"select evento.id,evento.classe from evento,fase,evento_entidades where evento.id = evento_entidades.id and evento.id=fase.id and (evento_entidades.responsavel=? or evento_entidades.responsavel=?) and (classe = "
								+ classe
								+ ") and fase.termino=0 and fase.codigo<>? and fase.codigo<>'cancelado' and fase.codigo<>'aceito' and fase.codigo<>'recusado' and fase.codigo<>'aprovado' and fase.codigo<>'reprovado' order by evento.prioridade,evento.criacao DESC");
		query2.addLong(responsavel.obterId());
		query2.addLong(responsavel.obterSuperior().obterId());
		query2.addString(Evento.EVENTO_CONCLUIDO);

		SQLRow[] rows2 = query2.execute();
		
		for (int i = 0; i < rows2.length; i++)
		{
			id = rows2[i].getLong("id");
			classe2 = rows2[i].getString("classe").toLowerCase();
			e = this._instanciarEvento(classe2, id);
			if (!eventos.contains(e))
				eventos.add(e);
		}
		
		Collection<String> niveis = UsuarioImpl.obterNiveisInspecao().values();
		if(niveis.contains(responsavel.obterNivel()))
		{
			UsuarioHome home = (UsuarioHome) this.getModelManager().getHome("UsuarioHome");
			
			String sql = "select evento.id, classe from evento,inspecao where evento.id = inspecao.id and (responsavel = ? or inspetor = ?) order by data_prevista_inicio DESC";
			
			Collection<Usuario> usuariosDepartamentos = home.obterUsuariosDepartamento(responsavel.obterSuperior());
			SQLQuery query3;
			SQLRow[] rows3;
			
			for(Usuario usuario : usuariosDepartamentos)
			{
				query3 = this.getModelManager().createSQLQuery("crm",sql);
				query3.addLong(usuario.obterId());
				query3.addLong(usuario.obterId());
				
				rows3 = query3.execute();

				for (int j = 0; j < rows3.length; j++)
				{
					id = rows3[j].getLong("id");
					classe2 = rows3[j].getString("classe").toLowerCase();
					e = this._instanciarEvento(classe2, id);
					if (!eventos.contains(e))
						eventos.add(e);
				}
			}
		}

		return eventos;
	}

	public Collection obterPossiveisSuperiores(Evento evento) throws Exception {
		String classeEvento = null;
		if (evento != null)
			classeEvento = evento.obterClasse().toLowerCase();
		InfraProperties ip = InfraProperties.getInstance();
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this
				.getModelManager().getUser());
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id,evento.classe from evento,fase where evento.id=fase.id and evento.responsavel=? and fase.termino=0 and fase.codigo=?");
		query.addLong(usuarioAtual.obterId());
		query.addString(Evento.EVENTO_PENDENTE);
		SQLRow[] rows = query.execute();
		Collection eventos = new ArrayList();
		for (int i = 0; i < rows.length; i++) {
			Evento e = this._instanciarEvento(rows[i].getString("classe"),
					rows[i].getLong("id"));
			if (evento == null) {
				if (!ip.getProperty(
						e.obterClasse().toLowerCase() + ".inferiores").equals(
						""))
					eventos.add(e);
			} else {
				if (!e.equals(evento))
					if (ip.getProperty(
							e.obterClasse().toLowerCase() + ".inferiores")
							.indexOf(classeEvento) >= 0)
						if (!e.obterSuperiores().contains(evento))
							eventos.add(e);
			}
		}
		return eventos;
	}

	public Collection obterTarefasAtrasadas(Usuario responsavel, Date data)
			throws Exception {
		String classes = InfraProperties.getInstance().getProperty(
				"eventos.tarefa");
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id,evento.classe from evento,fase where evento.id=fase.id and (evento.responsavel=? or evento.responsavel=?) and fase.termino=0 and fase.codigo=? and evento.data_prevista_conclusao<? order by evento.prioridade,evento.criacao");
		query.addLong(responsavel.obterId());
		query.addLong(responsavel.obterSuperior().obterId());
		query.addString(Evento.EVENTO_PENDENTE);
		query.addLong(data.getTime());
		SQLRow[] rows = query.execute();
		Collection eventos = new ArrayList();
		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");
			String classe = rows[i].getString("classe").toLowerCase();
			if (classes.indexOf(classe) >= 0)
				eventos.add(this._instanciarEvento(classe, id));
		}
		return eventos;
	}

	public Collection obterTarefasPendentes(Usuario responsavel)
			throws Exception {
		String classes = InfraProperties.getInstance().getProperty(
				"eventos.tarefa");
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id,evento.classe from evento,fase where evento.id=fase.id and (evento.responsavel=? or evento.responsavel=?) and fase.termino=0 and fase.codigo=? and (isnull(evento.data_prevista_inicio) or isnull(evento.data_prevista_conclusao)) order by evento.prioridade,evento.criacao");
		query.addLong(responsavel.obterId());
		query.addLong(responsavel.obterSuperior().obterId());
		query.addString(Evento.EVENTO_PENDENTE);
		SQLRow[] rows = query.execute();
		Collection eventos = new ArrayList();
		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");
			String classe = rows[i].getString("classe").toLowerCase();
			if (classes.indexOf(classe) >= 0)
				eventos.add(this._instanciarEvento(classe, id));
		}
		return eventos;
	}

	public Collection obterTarefasPendentes(Usuario responsavel, Date data1,
			Date data2) throws Exception {
		String classes = InfraProperties.getInstance().getProperty(
				"eventos.tarefa");
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id,evento.classe from evento,fase where evento.id=fase.id and (evento.responsavel=? or evento.responsavel=?) and fase.termino=0 and fase.codigo=? and (evento.data_prevista_inicio<=? and evento.data_prevista_conclusao>=?) order by evento.prioridade,evento.criacao");
		query.addLong(responsavel.obterId());
		query.addLong(responsavel.obterSuperior().obterId());
		query.addString(Evento.EVENTO_PENDENTE);
		query.addLong(data2.getTime());
		query.addLong(data1.getTime());
		SQLRow[] rows = query.execute();
		Collection eventos = new ArrayList();
		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");
			String classe = rows[i].getString("classe").toLowerCase();
			if (classes.indexOf(classe) >= 0)
				eventos.add(this._instanciarEvento(classe, id));
		}
		return eventos;
	}

	public Collection obterTiposEventos(String classeEvento) throws Exception {
		String s = InfraProperties.getInstance().getProperty(
				classeEvento.toLowerCase() + ".tipos");
		StringTokenizer st = new StringTokenizer(s, ",");
		Collection tipos = new ArrayList();
		while (st.hasMoreTokens())
			tipos.add(st.nextToken());
		return tipos;
	}

	public Collection obterFases(String classeEvento) throws Exception 
	{
		String s = InfraProperties.getInstance().getProperty(classeEvento.toLowerCase() + ".fases");
		StringTokenizer st = new StringTokenizer(s, ",");
		Collection fases = new ArrayList();
		while (st.hasMoreTokens())
			fases.add(st.nextToken());
		return fases;
	}

	public String obterNomeFase(String fase) throws Exception 
	{
		String s = InfraProperties.getInstance().getProperty("fase." + fase.toLowerCase() + ".nome");

		return s;
	}

	public Collection obterComponentesParaImpressao(String classeEvento)
			throws Exception {
		String s = InfraProperties.getInstance().getProperty(
				classeEvento.toLowerCase() + ".impressao");
		StringTokenizer st = new StringTokenizer(s, ",");
		Collection tipos = new ArrayList();
		while (st.hasMoreTokens())
			tipos.add(st.nextToken());
		return tipos;
	}

	//************************** Só para o cartão
	// *********************************************

	private Collection movimentacoes;

	private Collection contasPagarReceber;

	private Collection movimentoContabil;

	private Collection fluxoCaixa;

	public Collection obterContasPagarReceber(Entidade entidade, Date dia)
			throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id,evento.classe from evento,fase,movimentacao_financeira where evento.id=fase.id and evento.id=movimentacao_financeira.id and fase.termino=0 and fase.codigo<>? and evento.classe=? and movimentacao_financeira.data_prevista>=? and movimentacao_financeira.data_prevista<=? and (evento.origem=? or evento.destino=?) order by evento.criacao DESC");
		query.addString(Evento.EVENTO_CONCLUIDO);
		query.addString("MovimentacaoFinanceira");
		query.addLong(dia.getTime());
		query.addLong(dia.getTime());
		query.addLong(entidade.obterId());
		query.addLong(entidade.obterId());
		this.contasPagarReceber = new ArrayList();

		SQLRow[] rows = query.execute();

		//System.out.println("select evento.id,evento.classe from
		// evento,fase,movimentacao_financeira where evento.id=fase.id and
		// evento.id=movimentacao_financeira.id and fase.termino=0 and
		// fase.codigo<>'concluido' and evento.classe='MovimentacaoFinanceira'
		// and movimentacao_financeira.data_prevista="+dia.getTime()+" and
		// (evento.origem="+entidade.obterId()+" or
		// evento.destino="+entidade.obterId()+") order by evento.criacao
		// DESC");

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");
			String classe = rows[i].getString("classe");
			this.contasPagarReceber.add(this._instanciarEvento(classe, id));
		}

		return this.contasPagarReceber;
	}

	public MovimentacaoFinanceiraConta obterMovimentacao(Entidade entidade,
			Entidade seguradora, String mesAno) throws Exception {
		MovimentacaoFinanceiraConta mfRetorno = null;

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id,evento.classe from evento,movimentacao_financeira_conta where evento.id=movimentacao_financeira_conta.id and evento.classe=? and movimentacao_financeira_conta.conta=? and origem=? order by evento.criacao DESC");
		query.addString("MovimentacaoFinanceiraConta");
		query.addLong(entidade.obterId());
		query.addLong(seguradora.obterId());

		//System.out.println("select evento.id,evento.classe from
		// evento,movimentacao_financeira_conta where
		// evento.id=movimentacao_financeira_conta.id and evento.classe= and
		// movimentacao_financeira_conta.conta=? and origem=? order by
		// evento.criacao DESC");

		SQLRow[] rows = query.execute();
		if (rows.length > 0) {
			MovimentacaoFinanceiraConta mf = (MovimentacaoFinanceiraConta) this
					.obterEventoPorId(query.executeAndGetFirstRow().getLong(
							"id"));
			String mes = new SimpleDateFormat("MM").format(mf
					.obterDataPrevista());
			String ano = new SimpleDateFormat("yyyy").format(mf
					.obterDataPrevista());
			String mesAnoEvento = mes + " - " + ano;
			if (mesAnoEvento.equals(mesAno))
				mfRetorno = mf;

		}

		return mfRetorno;

	}

	public Collection obterMovimentoContabil(Entidade entidade, Date mes)
			throws Exception {

		SimpleDateFormat fm = new SimpleDateFormat("MM");
		String mesAtual = fm.format(mes);

		SimpleDateFormat fa = new SimpleDateFormat("yyyy");
		String ano = fa.format(mes);

		String dataInicio = "01/" + mesAtual + "/" + ano;
		Date dataModificadaInicio = new SimpleDateFormat("dd/MM/yyyy")
				.parse(dataInicio);

		String dataFim = "31/" + mesAtual + "/" + ano;
		Date dataModificadaFim = new SimpleDateFormat("dd/MM/yyyy")
				.parse(dataFim);

		/*
		 * System.out.println("Mes: " + mes); System.out.println("ano: " + ano);
		 * 
		 * System.out.println("Inicio: " + dataModificadaInicio);
		 * System.out.println("Fim: " + dataModificadaFim);
		 */

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id,evento.classe from evento,fase,movimentacao_financeira where evento.id=fase.id and evento.id=movimentacao_financeira.id and fase.codigo=? and evento.classe=? and movimentacao_financeira.data_realizada>=? and movimentacao_financeira.data_realizada<=? and (evento.origem=? or evento.destino=?) order by evento.criacao DESC");
		query.addString(Evento.EVENTO_CONCLUIDO);
		query.addString("MovimentacaoFinanceira");
		query.addLong(dataModificadaInicio.getTime());
		query.addLong(dataModificadaFim.getTime());
		query.addLong(entidade.obterId());
		query.addLong(entidade.obterId());
		SQLRow[] rows = query.execute();

		//System.out.println("select evento.id,evento.classe from evento,fase,
		// movimentacao_financeira where evento.id=fase.id and fase.termino<>0
		// and fase.codigo='Concluido' and
		// evento.classe='MovimentacaoFinanceira' and
		// movimentacao_financeira.data_realizada>=" +
		// dataModificadaInicio.getTime() +" and
		// movimentacao_financeira.data_realizada<=" +
		// dataModificadaFim.getTime() +" and (evento.origem="+
		// entidade.obterId() +" or evento.destino="+entidade.obterId()+") order
		// by evento.criacao DESC");

		this.movimentoContabil = new ArrayList();
		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");
			String classe = rows[i].getString("classe");
			this.movimentoContabil.add(this._instanciarEvento(classe, id));
		}

		return this.movimentoContabil;
	}

	public Collection obterFluxoCaixa(Entidade entidade) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select id, classe from evento where (classe=? or classe=?) and (origem=? or destino=?) order by criacao DESC");
		query.addString("MovimentacaoFinanceira");
		query.addString("MovimentacaoFinanceiraCartaoCredito");
		query.addLong(entidade.obterId());
		query.addLong(entidade.obterId());
		SQLRow[] rows = query.execute();
		this.fluxoCaixa = new ArrayList();
		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");
			String classe = rows[i].getString("classe");
			this.fluxoCaixa.add(this._instanciarEvento(classe, id));
		}
		//System.out.println("select id, classe from evento where classe=? and
		// (origem="+entidade.obterId()+" or destino="+entidade.obterId()+")
		// order by criacao DESC");
		return this.fluxoCaixa;
	}

	public double obterTotalCreditoPrevisto(Entidade entidade) throws Exception {
		double valor = 0;

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id, evento.classe, movimentacao_financeira.valor_realizado, movimentacao_financeira.valor_previsto from evento, movimentacao_financeira, fase where evento.id=movimentacao_financeira.id"
								+ " and evento.id=fase.id and fase.termino=0 and fase.codigo<>? and evento.destino=? and classe=?");

		query.addString(Evento.EVENTO_CONCLUIDO);
		query.addLong(entidade.obterId());
		query.addString("MovimentacaoFinanceira");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			valor += rows[i].getDouble("valor_previsto");
		}

		return valor;

	}

	public double obterTotalCreditoRealizado(Entidade entidade)
			throws Exception {
		double valor = 0;

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id, evento.classe, movimentacao_financeira.valor_realizado from evento, movimentacao_financeira, fase where evento.id=movimentacao_financeira.id "
								+ "and evento.id=fase.id and fase.termino<>0 and fase.codigo=? and evento.destino=? and classe=?");

		query.addString(Evento.EVENTO_CONCLUIDO);
		query.addLong(entidade.obterId());
		query.addString("MovimentacaoFinanceira");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			valor += rows[i].getDouble("valor_previsto");
		}

		return valor;
	}

	public double obterTotalDebitoPrevisto(Entidade entidade) throws Exception {
		double valor = 0;

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id, evento.classe, movimentacao_financeira.valor_previsto, movimentacao_financeira.valor_realizado from evento, movimentacao_financeira, fase where evento.id=movimentacao_financeira.id "
								+ "and evento.id=fase.id and fase.termino=0 and fase.codigo<>? and evento.origem=? and classe=?");

		query.addString(Evento.EVENTO_CONCLUIDO);
		query.addLong(entidade.obterId());
		query.addString("MovimentacaoFinanceira");
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			valor += rows[i].getDouble("valor_previsto");
		}

		return valor;
	}

	public double obterTotalDebitoRealizado(Entidade entidade) throws Exception {
		double valor = 0;

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id, evento.classe, movimentacao_financeira.valor_realizado from evento, movimentacao_financeira, fase where evento.id=movimentacao_financeira.id "
								+ "and evento.id=fase.id and fase.termino=0 and fase.codigo<>? and evento.origem=? and classe=?");

		query.addString(Evento.EVENTO_CONCLUIDO);
		query.addLong(entidade.obterId());
		query.addString("MovimentacaoFinanceira");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			valor += rows[i].getDouble("valor_previsto");
		}

		return valor;
	}

	public double obterTotalizacaoCredito(Entidade entidade, String mesAno)
			throws Exception {
		double valor = 0;

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id, evento.classe, movimentacao_financeira_conta.saldo_atual, movimentacao_financeira_conta.debito, movimentacao_financeira_conta.credito from evento, movimentacao_financeira_conta where evento.id=movimentacao_financeira_conta.id "
								+ "and movimentacao_financeira_conta.conta=? and classe=?");

		query.addLong(entidade.obterId());
		query.addString("MovimentacaoFinanceiraConta");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			MovimentacaoFinanceiraConta mf = (MovimentacaoFinanceiraConta) this
					.obterEventoPorId(rows[i].getLong("id"));
			String mes = new SimpleDateFormat("MM").format(mf
					.obterDataPrevista());
			String ano = new SimpleDateFormat("yyyy").format(mf
					.obterDataPrevista());
			String mesAnoEvento = mes + " - " + ano;
			if (mesAnoEvento.equals(mesAno))
				valor += rows[i].getDouble("credito");
		}

		return valor;
	}

	public double obterTotalizacaoCredito() throws Exception {
		return this.totalCredito;
	}

	public double obterTotalizacaoDebito(Entidade entidade, String mesAno)
			throws Exception {
		double valor = 0;

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id, evento.classe, movimentacao_financeira_conta.saldo_atual, movimentacao_financeira_conta.debito, movimentacao_financeira_conta.credito from evento, movimentacao_financeira_conta where evento.id=movimentacao_financeira_conta.id "
								+ "and movimentacao_financeira_conta.conta=? and classe=?");

		query.addLong(entidade.obterId());
		query.addString("MovimentacaoFinanceiraConta");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			MovimentacaoFinanceiraConta mf = (MovimentacaoFinanceiraConta) this
					.obterEventoPorId(rows[i].getLong("id"));
			String mes = new SimpleDateFormat("MM").format(mf
					.obterDataPrevista());
			String ano = new SimpleDateFormat("yyyy").format(mf
					.obterDataPrevista());
			String mesAnoEvento = mes + " - " + ano;
			if (mesAnoEvento.equals(mesAno))
				valor += rows[i].getDouble("debito");
		}

		return valor;
	}

	public double obterTotalizacaoDebito() throws Exception {
		return this.totalDebito;
	}

	public double obterTotalizacao(Entidade entidade, String mesAno)
			throws Exception {
		double valor = 0;
		this.totalCredito = 0;
		this.totalDebito = 0;
		this.totalSaldoAnterior = 0;

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id, evento.classe, movimentacao_financeira_conta.saldo_atual, movimentacao_financeira_conta.debito, movimentacao_financeira_conta.credito, movimentacao_financeira_conta.saldo_anterior from evento, movimentacao_financeira_conta where evento.id=movimentacao_financeira_conta.id "
								+ "and movimentacao_financeira_conta.conta=? and classe=?");

		query.addLong(entidade.obterId());
		query.addString("MovimentacaoFinanceiraConta");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			MovimentacaoFinanceiraConta mf = (MovimentacaoFinanceiraConta) this
					.obterEventoPorId(rows[i].getLong("id"));
			String mes = new SimpleDateFormat("MM").format(mf
					.obterDataPrevista());
			String ano = new SimpleDateFormat("yyyy").format(mf
					.obterDataPrevista());
			String mesAnoEvento = mes + " - " + ano;
			if (mesAnoEvento.equals(mesAno)) {
				valor += rows[i].getDouble("saldo_atual");
				this.totalCredito += rows[i].getDouble("credito");
				this.totalDebito += rows[i].getDouble("debito");
				this.totalSaldoAnterior += rows[i].getDouble("saldo_anterior");
			}
		}

		return valor;
	}

	public double obterTotalizacaoSaldoAnterior() throws Exception {
		return this.totalSaldoAnterior;
	}

	public double obterTotalizacaoSaldoAnterior(Entidade entidade, String mesAno)
			throws Exception {
		double valor = 0;

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id, evento.classe, movimentacao_financeira_conta.saldo_atual, movimentacao_financeira_conta.debito, movimentacao_financeira_conta.credito from evento, movimentacao_financeira_conta where evento.id=movimentacao_financeira_conta.id "
								+ "and movimentacao_financeira_conta.conta=? and classe=?");

		query.addLong(entidade.obterId());
		query.addString("MovimentacaoFinanceiraConta");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			MovimentacaoFinanceiraConta mf = (MovimentacaoFinanceiraConta) this
					.obterEventoPorId(rows[i].getLong("id"));
			String mes = new SimpleDateFormat("MM").format(mf
					.obterDataPrevista());
			String ano = new SimpleDateFormat("yyyy").format(mf
					.obterDataPrevista());

			String mesAnoEvento = mes + " - " + ano;

			if (mesAnoEvento.equals(mesAno))
				valor += rows[i].getDouble("saldo_anterior");
		}

		return valor;
	}

	public double obterTotalizacaoSaldoAnteriorMoedaEstrangeira()
			throws Exception {
		return this.totalSaldoMoedaEstrangeira;
	}

	public double obterTotalizacaoSaldoAnteriorMoedaEstrangeira(
			Entidade entidade, String mesAno) throws Exception {
		double valor = 0;

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id, evento.classe, movimentacao_financeira_conta.saldo_atual, movimentacao_financeira_conta.debito, movimentacao_financeira_conta.credito from evento, movimentacao_financeira_conta where evento.id=movimentacao_financeira_conta.id "
								+ "and movimentacao_financeira_conta.conta=? and classe=?");

		query.addLong(entidade.obterId());
		query.addString("MovimentacaoFinanceiraConta");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			MovimentacaoFinanceiraConta mf = (MovimentacaoFinanceiraConta) this
					.obterEventoPorId(rows[i].getLong("id"));
			String mes = new SimpleDateFormat("MM").format(mf
					.obterDataPrevista());
			String ano = new SimpleDateFormat("yyyy").format(mf
					.obterDataPrevista());

			String mesAnoEvento = mes + " - " + ano;
			if (mesAnoEvento.equals(mesAno))
				valor += rows[i].getDouble("saldo_estrangeiro");
		}

		return valor;
	}

	public double obterTotalizacaoSaldoMoedaEstrangeira(Entidade entidade,
			String mesAno) throws Exception {
		double valor = 0;

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id, evento.classe, movimentacao_financeira_conta.saldo_atual, movimentacao_financeira_conta.debito, movimentacao_financeira_conta.credito from evento, movimentacao_financeira_conta where evento.id=movimentacao_financeira_conta.id "
								+ "and movimentacao_financeira_conta.conta=? and classe=?");

		query.addLong(entidade.obterId());
		query.addString("MovimentacaoFinanceiraConta");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			MovimentacaoFinanceiraConta mf = (MovimentacaoFinanceiraConta) this
					.obterEventoPorId(rows[i].getLong("id"));
			String mes = new SimpleDateFormat("MM").format(mf
					.obterDataPrevista());
			String ano = new SimpleDateFormat("yyyy").format(mf
					.obterDataPrevista());

			String mesAnoEvento = mes + " - " + ano;
			if (mesAnoEvento.equals(mesAno))
				valor += rows[i].getDouble("saldo_estrangeiro");
		}

		return valor;
	}

	public double obterTotalizacaoSaldoMoedaEstrangeira() throws Exception {
		return this.totalSaldoMoedaEstrangeira;
	}
}