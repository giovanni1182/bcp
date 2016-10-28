package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Inscricao;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class InscricaoImpl extends EventoImpl implements Inscricao {
	public class RamoImpl implements Ramo
	{

		private InscricaoImpl inscricao;

		private int seq;

		private String ramo;

		public RamoImpl(InscricaoImpl inscricao, int seq, String ramo) throws Exception
		{
			this.inscricao = inscricao;
			this.seq = seq;
			this.ramo = ramo;
		}

		public Inscricao obterEvento() throws Exception {
			return this.inscricao;
		}

		public int obterSeq() throws Exception {
			return this.seq;
		}

		public String obterRamo() throws Exception {
			return this.ramo;
		}
	}
	
	public class SuspensaoImpl implements Suspensao
	{
		private InscricaoImpl inscricao;
		private Date data;
		
		public SuspensaoImpl(InscricaoImpl inscricao, Date data)
		{
			this.inscricao = inscricao;
			this.data = data;
		}
		
		public Inscricao obterEvento() throws Exception
		{
			return this.inscricao;
		}

		public Date obterData() throws Exception
		{
			return this.data;
		}
	}

	private Map ramos;

	public void atualizarAseguradora(Aseguradora aseguradora) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update inscricao set aseguradora=? where id=?");
		update.addLong(aseguradora.obterId());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarAgente(Entidade agente) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update inscricao set agente=? where id=?");
		update.addLong(agente.obterId());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarInscricao(String inscricao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update inscricao set inscricao=? where id=?");
		update.addString(inscricao);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarNumeroApolice(String numero) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update inscricao set numero_apolice=? where id=?");
		update.addString(numero);
		update.addLong(this.obterId());
		update.execute();
	}
	
	public void atualizarNumeroSecao(String numero) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update inscricao set numero_secao=? where id=?");
		update.addString(numero);
		update.addLong(this.obterId());
		update.execute();
	}
	
	public void atualizarCesion(int numero) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm", "update inscricao set cesion_hasta=? where id=?");
		update.addInt(numero);
		update.addLong(this.obterId());
		update.execute();
	}

	public boolean validarResolucao(String resolucaoStr) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,documento_produto where evento.id = documento_produto.id and numero=?");
		query.addString(resolucaoStr);

		if (query.execute().length == 0)
			return true;
		else {
			boolean renovar = true;

			long id = query.executeAndGetFirstRow().getLong("id");

			EventoHome home = (EventoHome) this.getModelManager().getHome(
					"EventoHome");

			DocumentoProduto documento = (DocumentoProduto) home
					.obterEventoPorId(id);

			if (documento.obterDocumento().obterApelido().equals("resolucion")) {
				for (Iterator i = documento.obterFases().iterator(); i
						.hasNext();) {
					Evento.Fase fase = (Evento.Fase) i.next();

					if (fase.obterCodigo().equals(DocumentoProduto.APROVADA))
						renovar = false;
				}

				return renovar;
			} else
				return true;
		}
	}

	public void atualizarNumeroResolucao(String resolucao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update inscricao set resolucao=? where id=?");
		update.addString(resolucao);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDataResolucao(Date dataResolucao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update inscricao set data_resolucao=? where id=?");
		update.addLong(dataResolucao.getTime());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDataValidade(Date dataValidade) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update inscricao set data_validade=? where id=?");
		update.addLong(dataValidade.getTime());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDataEmissao(Date data) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update inscricao set data_emissao=? where id=?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDataVencimento(Date data) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update inscricao set data_vencimento=? where id=?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarSituacao(String situacao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update inscricao set situacao=? where id=?");
		update.addString(situacao);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarApolice(Apolice apolice) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update inscricao set apolice=? where id=?");
		update.addLong(apolice.obterId());
		update.addLong(this.obterId());
		update.execute();
	}

	public void incluir() throws Exception {
		super.incluir();

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into inscricao(id) values(?)");
		insert.addLong(this.obterId());
		insert.execute();
	}

	public Aseguradora obterAseguradora() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select aseguradora from inscricao where id=?");
		query.addLong(this.obterId());

		Aseguradora aseguradora = null;

		if (query.executeAndGetFirstRow().getLong("aseguradora") > 0) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			aseguradora = (Aseguradora) home.obterEntidadePorId(query
					.executeAndGetFirstRow().getLong("aseguradora"));
		}

		return aseguradora;
	}

	public Entidade obterAgente() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select agente from inscricao where id=?");
		query.addLong(this.obterId());

		Entidade agente = null;

		if (query.executeAndGetFirstRow().getLong("agente") > 0) {
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
					"EntidadeHome");

			agente = home.obterEntidadePorId(query.executeAndGetFirstRow()
					.getLong("agente"));
		}

		return agente;
	}

	public String obterInscricao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select inscricao from inscricao where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("inscricao");
	}
	
	public String obterNumeroApolice() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select numero_apolice from inscricao where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("numero_apolice");
	}

	public String obterNumeroSecao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select numero_secao from inscricao where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("numero_secao");
	}

	public String obterNumeroResolucao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select resolucao from inscricao where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("resolucao");
	}

	public Date obterDataResolucao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_resolucao from inscricao where id=?");
		query.addLong(this.obterId());

		Date data = null;

		if (query.executeAndGetFirstRow().getLong("data_resolucao") != 0)
			data = new Date(query.executeAndGetFirstRow().getLong(
					"data_resolucao"));

		return data;
	}

	public Date obterDataValidade() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_validade from inscricao where id=?");
		query.addLong(this.obterId());

		Date data = null;

		if (query.executeAndGetFirstRow().getLong("data_validade") != 0)
			data = new Date(query.executeAndGetFirstRow().getLong(
					"data_validade"));

		return data;
	}

	public void adicionarDocumentoVinculado(DocumentoProduto documento)
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
		insert.addLong(documento.obterId());
		insert.execute();
	}

	public Collection obterDocumentosVinculados() throws Exception {
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

	public String obterSituacao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select situacao from inscricao where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("situacao");
	}

	public Date obterDataEmissao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_emissao from inscricao where id=?");
		query.addLong(this.obterId());

		Date data = null;

		long dataLong = query.executeAndGetFirstRow().getLong("data_emissao");

		if (dataLong > 0)
			data = new Date(dataLong);

		return data;
	}

	public Date obterDataVencimento() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_vencimento from inscricao where id=?");
		query.addLong(this.obterId());

		Date data = null;

		long dataLong = query.executeAndGetFirstRow()
				.getLong("data_vencimento");

		if (dataLong > 0)
			data = new Date(dataLong);

		return data;
	}
	
	public int obterCesion() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select cesion_hasta from inscricao where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getInt("cesion_hasta");
	}

	public Apolice obterApolice() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery(	"crm",
						"select apolice from inscricao,evento,apolice where evento.id=inscricao.apolice and inscricao.apolice=apolice.id and inscricao.id = ?");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("apolice");

		Apolice apolice = null;

		if (id > 0)
		{
			EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
			apolice = (Apolice) home.obterEventoPorId(id);
		}

		return apolice;
	}

	public void adicionarNovoRamo(String ramo) throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select MAX(seq) as MX from inscricao_ramo where id=?");
		query.addLong(this.obterId());

		int id = query.executeAndGetFirstRow().getInt("MX") + 1;

		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into inscricao_ramo(id, seq, nome) values (?, ?, ?)");
		insert.addLong(this.obterId());
		insert.addInt(id);
		insert.addString(ramo);
		insert.execute();
	}

	public void atualizarRamo(String ramo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update inscricao set ramo=? where id=?");
		update.addString(ramo);
		update.addLong(this.obterId());
		update.execute();
	}

	public String obterRamo() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select nome from inscricao_ramo where id=?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("nome");
	}
	
	

	public Ramo obterRamo(int seq) throws Exception {
		this.obterRamos();

		return (Ramo) this.ramos.get(new Integer(seq));
	}

	public Collection obterRamos() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from inscricao_ramo where id=?");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		this.ramos = new TreeMap();

		for (int i = 0; i < rows.length; i++) {
			int seq = rows[i].getInt("seq");
			String ramo = rows[i].getString("nome");

			this.ramos.put(new Integer(seq), new RamoImpl(this, seq, ramo));
		}

		return this.ramos.values();
	}

	public void excluirRamo(Inscricao.Ramo ramo) throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate("crm",
				"delete from inscricao_ramo where id=? and seq=?");
		delete.addLong(this.obterId());
		delete.addInt(ramo.obterSeq());

		delete.execute();
	}

	public void exclurInscricaoVinculada(DocumentoProduto documento)
			throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate("crm",
				"delete evento_entidades where id=? and sub_evento=?");
		delete.addLong(this.obterId());
		delete.addLong(documento.obterId());
		delete.execute();
	}

	public Collection obterNomeRamos() throws Exception {
		Collection nomeRamos = new ArrayList();

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select nome from inscricao_ramo group by nome");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++)
			nomeRamos.add(rows[i].getString("nome"));

		return nomeRamos;
	}
	
	public void addSuspensao(Date data) throws Exception
	{
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into inscricao_suspensa(id,data) values(?,?)");
		insert.addLong(this.obterId());
		insert.addLong(data.getTime());
		
		insert.execute();
	}
	
	public Collection<Suspensao> obterSuspensoes() throws Exception
	{
		Collection<Suspensao> suspensoes = new ArrayList<Inscricao.Suspensao>();
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select data from inscricao_suspensa where id = ?");
		query.addLong(this.obterId());
		
		SQLRow[] rows = query.execute();
		
		SuspensaoImpl suspensao;
		Date data;
		long dataLong;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			dataLong = rows[i].getLong("data");
			data = new Date(dataLong);
			
			suspensao = new SuspensaoImpl(this, data);
			
			suspensoes.add(suspensao);
		}
		
		return suspensoes;
	}
}