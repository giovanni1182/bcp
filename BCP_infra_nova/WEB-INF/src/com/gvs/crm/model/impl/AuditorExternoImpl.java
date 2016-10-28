package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AuditorExterno;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class AuditorExternoImpl extends EntidadeImpl implements AuditorExterno {
	private Map clientes;

	private Map servicos;

	public class ClienteImpl implements Cliente {
		private AuditorExternoImpl auditor;

		private int id;

		private Aseguradora aseguradora;

		private double honorarios;

		private Date dataIncio;

		private Date dataFim;

		private String outrosServicos;

		public ClienteImpl(AuditorExternoImpl auditor, int id,
				Aseguradora aseguradora, double honorarios, Date dataIncio,
				Date dataFim, String outrosServicos) throws Exception {
			this.auditor = auditor;
			this.id = id;
			this.aseguradora = aseguradora;
			this.honorarios = honorarios;
			this.dataIncio = dataIncio;
			this.dataFim = dataFim;
			this.outrosServicos = outrosServicos;
		}

		public void atualizar(Aseguradora aseguradora, double honorarios,
				Date dataIncio, Date dataFim, String outrosServicos)
				throws Exception {
			SQLUpdate update2 = this.auditor
					.getModelManager()
					.createSQLUpdate(
							"crm",
							"update auditor_externo_servicos set aseguradora=? where entidade=? and aseguradora=?");
			update2.addLong(aseguradora.obterId());
			update2.addLong(this.obterId());
			update2.addLong(this.obterAseguradora().obterId());

			update2.execute();

			SQLUpdate update = this.auditor
					.getModelManager()
					.createSQLUpdate(
							"update auditor_externo_cliente set aseguradora=?, honorarios=?, data_inicio=?, data_fim=?, outros_servicos=? where entidade=? and id=?");
			update.addLong(aseguradora.obterId());
			update.addDouble(honorarios);
			update.addLong(dataIncio.getTime());
			update.addLong(dataFim.getTime());
			update.addString(outrosServicos);
			update.addLong(this.auditor.obterId());
			update.addInt(this.id);
			update.execute();
		}

		public AuditorExterno obterAuditor() throws Exception {
			return this.auditor;
		}

		public int obterId() throws Exception {
			return this.id;
		}

		public Aseguradora obterAseguradora() throws Exception {
			return this.aseguradora;
		}

		public double obterHonorarios() throws Exception {
			return this.honorarios;
		}

		public Date obterDataInicio() throws Exception {
			return this.dataIncio;
		}

		public Date obterDataFim() throws Exception {
			return this.dataFim;
		}

		public String obterOutrosServicos() throws Exception {
			return this.outrosServicos;
		}
	}

	public class ServicoImpl implements Servico {
		private AuditorExternoImpl auditor;

		private Aseguradora aseguradora;

		private int id;

		private String servico;

		private Date dataContrato;

		private double honorarios;

		private String periodo;

		ServicoImpl(AuditorExternoImpl auditor, Aseguradora aseguradora,
				int id, String servico, Date dataContrato, double honorarios,
				String periodo) throws Exception {
			this.auditor = auditor;
			this.aseguradora = aseguradora;
			this.id = id;
			this.servico = servico;
			this.dataContrato = dataContrato;
			this.honorarios = honorarios;
			this.periodo = periodo;
		}

		public void atualizar(String servico, Date dataContrato,
				double honorarios, String periodo) throws Exception {
			SQLUpdate update = this.auditor
					.getModelManager()
					.createSQLUpdate(
							"crm",
							"update auditor_externo_servicos set servico=?, data_contrato=?, honorarios=?, periodo=? where entidade=? and aseguradora=? and id=?");
			update.addString(servico);
			update.addLong(dataContrato.getTime());
			update.addDouble(honorarios);
			update.addString(periodo);
			update.addLong(this.auditor.obterId());
			update.addLong(this.aseguradora.obterId());
			update.addInt(this.id);
			update.execute();
		}

		public AuditorExterno obterAuditor() throws Exception {
			return this.auditor;
		}

		public Aseguradora obterAseguradora() throws Exception {
			return this.aseguradora;
		}

		public int obterId() throws Exception {
			return this.id;
		}

		public String obterServico() throws Exception {
			return this.servico;
		}

		public Date obterDataContrato() throws Exception {
			return this.dataContrato;
		}

		public double obterHonorarios() throws Exception {
			return this.honorarios;
		}

		public String obterPeriodo() throws Exception {
			return this.periodo;
		}
	}

	public void adicionarCliente(Aseguradora aseguradora, double honorarios,
			Date dataIncio, Date dataFim, String outrosServicos)
			throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"select max(id) as MX from auditor_externo_cliente where entidade=?");
		query.addLong(this.obterId());
		long id = query.executeAndGetFirstRow().getLong("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"insert into auditor_externo_cliente (entidade, id, aseguradora, honorarios, data_inicio, data_fim, outros_servicos) values (?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addLong(id);
		insert.addLong(aseguradora.obterId());
		insert.addDouble(honorarios);
		insert.addLong(dataIncio.getTime());
		insert.addLong(dataFim.getTime());
		insert.addString(outrosServicos);
		insert.execute();
	}

	public Cliente obterCliente(int id) throws Exception {
		this.obterClientes();
		return (Cliente) this.clientes.get(Integer.toString(id));
	}

	public Collection obterClientes() throws Exception {
		this.clientes = new TreeMap();
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from auditor_externo_cliente where entidade=?");
		query.addLong(this.obterId());
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			int id = rows[i].getInt("id");

			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			long aseguradoraId = rows[i].getLong("aseguradora");

			Aseguradora aseguradora = (Aseguradora) home
					.obterEntidadePorId(aseguradoraId);

			this.clientes.put(Integer.toString(id), new ClienteImpl(this, id,
					aseguradora, rows[i].getDouble("honorarios"), new Date(
							rows[i].getLong("data_inicio")), new Date(rows[i]
							.getLong("data_fim")), rows[i]
							.getString("outros_servicos")));
		}

		return this.clientes.values();
	}

	public void removerCliente(Cliente cliente) throws Exception {
		SQLUpdate delete2 = this
				.getModelManager()
				.createSQLUpdate("crm",
						"delete from auditor_externo_servicos where entidade=? and aseguradora=?");
		delete2.addLong(this.obterId());
		delete2.addLong(cliente.obterAseguradora().obterId());

		delete2.execute();

		SQLUpdate delete = this
				.getModelManager()
				.createSQLUpdate(
						"delete from auditor_externo_cliente where entidade=? and id=?");
		delete.addLong(this.obterId());
		delete.addInt(cliente.obterId());
		delete.execute();
		if (this.clientes != null)
			this.clientes.remove(Integer.toString(cliente.obterId()));
	}

	public void adicionarServico(Aseguradora aseguradora, String servico,
			Date dataContrato, double honorarios, String periodo)
			throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select max(id) as MX from auditor_externo_servicos where entidade=? and aseguradora=?");
		query.addLong(this.obterId());
		query.addLong(aseguradora.obterId());

		long id = query.executeAndGetFirstRow().getLong("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate(
						"crm",
						"insert into auditor_externo_servicos (entidade, aseguradora, id, servico, data_contrato, honorarios, periodo) values (?, ?, ?, ?, ?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addLong(aseguradora.obterId());
		insert.addLong(id);
		insert.addString(servico);
		insert.addLong(dataContrato.getTime());
		insert.addDouble(honorarios);
		insert.addString(periodo);
		insert.execute();
	}

	public Servico obterServico(Aseguradora aseguradora, int id)
			throws Exception {
		this.obterServicos(aseguradora);
		return (Servico) this.servicos.get(Integer.toString(id));
	}

	public Collection obterServicos(Aseguradora aseguradora) throws Exception {
		this.servicos = new TreeMap();
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select * from auditor_externo_servicos where entidade=? and aseguradora=?");
		query.addLong(this.obterId());
		query.addLong(aseguradora.obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			int id = rows[i].getInt("id");

			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			this.servicos.put(Integer.toString(id), new ServicoImpl(this,
					aseguradora, id, rows[i].getString("servico"), new Date(
							rows[i].getLong("data_contrato")), rows[i]
							.getDouble("honorarios"), rows[i]
							.getString("periodo")));
		}

		return this.servicos.values();
	}

	public Collection obterNomeRamos() throws Exception {
		Collection nomeRamos = new ArrayList();

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select nome from auditor_externo_ramo group by nome");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++)
			nomeRamos.add(rows[i].getString("nome"));

		return nomeRamos;
	}

	public void adicionarNovoRamo(String ramo) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select MAX(id) as MX from auditor_externo_ramo where entidade=?");
		query.addLong(this.obterId());

		int id = query.executeAndGetFirstRow().getInt("MX") + 1;

		SQLUpdate insert = this
				.getModelManager()
				.createSQLUpdate("crm",
						"insert into auditor_externo_ramo(entidade, id, nome) values (?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addInt(id);
		insert.addString(ramo);
		insert.execute();
	}

	public void atualizarAseguradora(Aseguradora aseguradora) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update auditor_externo set aseguradora=? where id=?");
		update.addLong(aseguradora.obterId());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarRamo(String ramo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update auditor_externo set ramo=? where id=?");
		update.addString(ramo);
		update.addLong(this.obterId());
		update.execute();
	}

	public Aseguradora obterAseguradora() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select aseguradora from auditor_externo where id=?");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("aseguradora");

		Aseguradora aseguradora = null;

		if (id > 0) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			aseguradora = (Aseguradora) home.obterEntidadePorId(id);
		}

		return aseguradora;
	}

	public String obterRamo() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select ramo from auditor_externo where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("ramo");
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into auditor_externo(id) values(?)");
		insert.addLong(this.obterId());
		insert.execute();
	}

	public void removerServico(Servico servico) throws Exception {
		SQLUpdate delete = this
				.getModelManager()
				.createSQLUpdate(
						"delete from auditor_externo_servicos where entidade=? and aseguradora=? and id=?");
		delete.addLong(this.obterId());
		delete.addLong(servico.obterAseguradora().obterId());
		delete.addInt(servico.obterId());
		delete.execute();
		if (this.servicos != null)
			this.servicos.remove(Integer.toString(servico.obterId()));
	}
	
	public boolean permiteAtualizar() throws Exception
	{
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		Entidade icf = home.obterEntidadePorApelido("intendenteicf");
		
		if(icf!=null)
		{
			if(this.obterUsuarioAtual().obterSuperiores().contains(icf))
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
		
		if(this.obterInscricoes().size() > 0)
			retorno = false;
		else
		{
			if(this.obterInferiores().size() > 0)
				retorno = false;
		}
		
		return retorno;
	}
}