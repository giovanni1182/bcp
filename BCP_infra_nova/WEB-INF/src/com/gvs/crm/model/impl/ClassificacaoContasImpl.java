package com.gvs.crm.model.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AspectosLegais;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.FaturaSinistro;
import com.gvs.crm.model.RegistroGastos;
import com.gvs.crm.model.Sinistro;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class ClassificacaoContasImpl extends EntidadeImpl implements
		ClassificacaoContas {

	public class ClassificacaoImpl2 implements ClassificacaoConta2 {

		private long id, superior;

		private String codigoConta2, nomeConta;

		ClassificacaoImpl2(long id, long superior, String codigoConta,
				String nomeConta) {
			this.id = id;
			this.superior = superior;
			this.codigoConta2 = codigoConta;
			this.nomeConta = nomeConta;
		}

		public long obterContaId() throws Exception {
			return this.id;
		}

		public long obterSuperiorId() throws Exception {
			return this.superior;
		}

		public String obterCodigoClassificacaoConta() throws Exception {
			return this.codigoConta2;
		}

		public String obterNomeClassificacaoConta() throws Exception {
			return this.nomeConta;
		}
	}

	private String descricao;

	private String codigoConta;

	private String concepto;

	private String noma;

	private String natureza;

	private String dinamica;

	private String nivel;

	private Collection inferioresNivel1 = new ArrayList();

	private Collection inferioresNivel2 = new ArrayList();

	private Collection inferioresNivel3 = new ArrayList();

	private Collection inferioresNivel4 = new ArrayList();

	private Collection inferioresNivel5 = new ArrayList();

	public Collection obterInferioresNivel1() throws Exception {
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select entidade.id from entidade,classificacao_contas where entidade.id=classificacao_contas.id and superior=? and nivel=?");
		query.addLong(this.obterId());
		query.addString("Nivel 1");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			this.inferioresNivel1.add(home.obterEntidadePorId(rows[i]
					.getLong("id")));
		}

		return this.inferioresNivel1;
	}

	public Collection obterInferioresNivel2() throws Exception {
		//this.inferioresNivel2.addAll(this.obterInferioresNivel1());

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select entidade.id from entidade,classificacao_contas where entidade.id=classificacao_contas.id and superior=? and nivel=?");
		query.addLong(this.obterId());
		query.addString("Nivel 2");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			this.inferioresNivel2.add(home.obterEntidadePorId(rows[i]
					.getLong("id")));
		}

		return this.inferioresNivel2;
	}

	public Collection obterInferioresNivel3() throws Exception {
		//this.inferioresNivel3.addAll(this.obterInferioresNivel2());

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select entidade.id from entidade,classificacao_contas where entidade.id=classificacao_contas.id and superior=? and nivel=?");
		query.addLong(this.obterId());
		query.addString("Nivel 3");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			this.inferioresNivel3.add(home.obterEntidadePorId(rows[i]
					.getLong("id")));
		}

		return this.inferioresNivel3;
	}

	public Collection obterInferioresNivel4() throws Exception {
		//this.inferioresNivel4.addAll(this.obterInferioresNivel3());

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select entidade.id from entidade,classificacao_contas where entidade.id=classificacao_contas.id and superior=? and nivel=?");
		query.addLong(this.obterId());
		query.addString("Nivel 4");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			this.inferioresNivel4.add(home.obterEntidadePorId(rows[i]
					.getLong("id")));
		}

		return this.inferioresNivel4;
	}

	public Collection obterInferioresNivel5(Entidade seguradora, String mesAno)
			throws Exception {
		//this.inferioresNivel5.addAll(this.obterInferioresNivel4());

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select id from relatorio where nivel=? and seguradora=? and mes_ano=? and superior=?");
		query.addString("Nivel 5");
		query.addLong(seguradora.obterId());

		String mesAnoModificado = "";

		for (int i = 0; i < mesAno.length(); i++) {

			String caracter = mesAno.substring(i, i + 1);

			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
				mesAnoModificado += caracter;
		}

		query.addString(mesAnoModificado);

		query.addLong(this.obterId());
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			this.inferioresNivel5.add(home.obterEntidadePorId(rows[i]
					.getLong("id")));
		}

		/*
		 * if(rows.length==0) { for(Iterator i =
		 * this.obterInferiores().iterator() ; i.hasNext() ; ) { Entidade e =
		 * (Entidade) i.next(); if(e instanceof Conta) { Conta conta = (Conta)
		 * e; if(conta.obterNivel().equals("Nivel 5"))
		 * this.inferioresNivel5.add(conta); } } }
		 */

		return this.inferioresNivel5;
	}

	public void atualizarCodigo(String codigo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update classificacao_contas set codigo_conta=? where id=?");
		update.addString(codigo);
		update.addLong(this.obterId());
		update.execute();
	}

	public String obterDescricao() throws Exception {
		if (this.descricao == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select descricao from classificacao_contas where id=?");
			query.addLong(this.obterId());
			this.descricao = query.executeAndGetFirstRow().getString(
					"descricao");
		}
		return this.descricao;
	}

	public String obterConcepto() throws Exception {
		if (this.concepto == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select concepto from classificacao_contas where id=?");
			query.addLong(this.obterId());
			this.concepto = query.executeAndGetFirstRow().getString("concepto");
		}
		return this.concepto;
	}

	public String obterNivel() throws Exception {
		if (this.nivel == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select nivel from classificacao_contas where id=?");
			query.addLong(this.obterId());
			this.nivel = query.executeAndGetFirstRow().getString("nivel");
		}
		return this.nivel;
	}

	public String obterNoma() throws Exception {
		if (this.noma == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select noma from classificacao_contas where id=?");
			query.addLong(this.obterId());
			this.noma = query.executeAndGetFirstRow().getString("noma");
		}
		return this.noma;
	}

	public String obterNatureza() throws Exception {
		if (this.natureza == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select natureza from classificacao_contas where id=?");
			query.addLong(this.obterId());
			this.natureza = query.executeAndGetFirstRow().getString("natureza");
		}
		return this.natureza;
	}

	public String obterDinamica() throws Exception {
		if (this.dinamica == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select dinamica from classificacao_contas where id=?");
			query.addLong(this.obterId());
			this.dinamica = query.executeAndGetFirstRow().getString("dinamica");
		}
		return this.dinamica;
	}

	public void incluir() throws Exception {
		if (this.codigoConta != null && !this.codigoConta.equals("")) {
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery(
							"select count(id) as quantidade from classificacao_contas where codigo_conta=?");
			query.addString(this.codigoConta);
			if (query.executeAndGetFirstRow().getLong("quantidade") > 0)
				throw new Exception("El Código de la Cuenta '"
						+ this.obterCodigo()
						+ "' está siendo utilizado por otra Cuenta");
		}

		super.incluir();
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate(
						"insert into classificacao_contas (id, codigo_conta, descricao, concepto, noma, natureza, dinamica, nivel) values (?, ?, ?, ?, ?, ?, ?, ?)");
		update.addLong(this.obterId());
		update.addString(this.codigoConta);
		update.addString(this.descricao);
		update.addString(this.concepto);
		update.addString(this.noma);
		update.addString(this.natureza);
		update.addString(this.dinamica);
		update.addString(this.nivel);
		update.execute();
	}

	/*
	 * private long obterMaiorId() throws Exception { SQLQuery query =
	 * this.getModelManager().createSQLQuery("select max(id_auxiliar) from
	 * relatorio"); return
	 * (query.executeAndGetFirstRow().getLong("max(id_auxiliar)") + 1); }
	 */

	public void incluirRelatorio(String mesAno, double valor, double debito, double credito, double saldoAnterior, double saldoMoedaEstrangeira,Entidade seguradora) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
						"insert into relatorio (id, mes_ano, valor, seguradora, valor_outro, nivel, debito, credito, saldo_anterior) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		update.addLong(this.obterId());
		String mesAnoModificado = "";

		for (int i = 0; i < mesAno.length(); i++)
		{
			String caracter = mesAno.substring(i, i + 1);

			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
				mesAnoModificado += caracter;
		}

		update.addString(mesAnoModificado);
		update.addDouble(valor);
		update.addLong(seguradora.obterId());
		update.addDouble(saldoMoedaEstrangeira);
		update.addString(this.obterNivel());
		update.addDouble(debito);
		update.addDouble(credito);
		update.addDouble(saldoAnterior);
		//System.out.println("insert into relatorio (id, mes_ano, valor,
		// existe_valor_guarani, seguradora, nivel, debito, credito,
		// saldo_anterior) values ("+this.obterId()+", "+mesAnoModificado+",
		// "+valor+", 1, "+seguradora.obterId()+", "+this.obterNivel()+",
		// "+debito+", "+credito+", "+saldoAnterior+")");
		update.execute();
	}

	public void incluirRelatorioOutroValor(String mesAno, double valor,
			double debito, double credito, double saldoAnterior,
			Entidade seguradora) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"insert into relatorio (id, mes_ano, seguradora, valor_outro,existe_outro_valor, nivel, debito, credito, saldo_anterior) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		update.addLong(this.obterId());
		String mesAnoModificado = "";

		for (int i = 0; i < mesAno.length(); i++) {

			String caracter = mesAno.substring(i, i + 1);

			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
				mesAnoModificado += caracter;
		}

		update.addString(mesAnoModificado);
		update.addLong(seguradora.obterId());
		update.addDouble(valor);
		update.addInt(1);
		update.addString(this.obterNivel());
		update.addDouble(debito);
		update.addDouble(credito);
		update.addDouble(saldoAnterior);
		update.execute();
	}

	public String obterCodigo() throws Exception {
		if (this.codigoConta == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select codigo_conta from classificacao_contas where id=?");
			query.addLong(this.obterId());
			this.codigoConta = query.executeAndGetFirstRow().getString(
					"codigo_conta");
		}
		return this.codigoConta;
	}

	public void atribuirDescricao(String descricao) throws Exception {
		this.descricao = descricao;
	}

	public void atribuirConcepto(String concepto) throws Exception {
		this.concepto = concepto;
	}

	public void atribuirNivel(String nivel) throws Exception {
		this.nivel = nivel;
	}

	public void atribuirNoma(String noma) throws Exception {
		this.noma = noma;
	}

	public void atribuirNatureza(String natureza) throws Exception {
		this.natureza = natureza;
	}

	public void atribuirDinamica(String dinamica) throws Exception {
		this.dinamica = dinamica;
	}

	public void atribuirCodigo(String codigo) throws Exception {
		this.codigoConta = codigo;
	}

	public void atualizarDescricao(String descricao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update classificacao_contas set descricao=? where id=?");
		update.addString(descricao);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarConcepto(String concepto) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update classificacao_contas set concepto=? where id=?");
		update.addString(concepto);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarNivel(String nivel) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("update classificacao_contas set nivel=? where id=?");
		update.addString(nivel);
		update.addLong(this.obterId());
		update.execute();
		
		update = this.getModelManager().createSQLUpdate("update relatorio set nivel=? where id=?");
		update.addString(nivel);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarNoma(String noma) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update classificacao_contas set noma=? where id=?");
		update.addString(noma);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarNatureza(String natureza) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update classificacao_contas set natureza=? where id=?");
		update.addString(natureza);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDinamica(String dinamica) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update classificacao_contas set dinamica=? where id=?");
		update.addString(dinamica);
		update.addLong(this.obterId());
		update.execute();
	}

	public void excluir() throws Exception {
		super.excluir();
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"delete from classificacao_contas where id=?");
		update.addLong(this.obterId());
		update.execute();
	}

	private Collection classificacaoContas = new ArrayList();

	public Collection obterClassificacaoContas() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select id,superior,CDconta,DScontas,n5 from pc where n5=? order by id");
		query.addString("00");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			this.classificacaoContas.add(new ClassificacaoImpl2(rows[i]
					.getLong("id"), rows[i].getLong("superior"), rows[i]
					.getString("CDconta"), rows[i].getString("DScontas")));
		}

		return this.classificacaoContas;
	}

	private double total = 0;

	private double totalClassificacaoContas = 0;

	private double totalCredito = 0;

	private double totalDebito = 0;

	private double totalSaldoAnterior = 0;

	private double totalSaldoMoedaEstrangeira = 0;

	public double obterTotalizacao(Entidade seguradora, boolean emGuarani,
			String mesAno) throws Exception {
		this.obterTotalizacaoClassificaoContas(this, emGuarani, mesAno);

		try {
			if (this.total != 0 || this.totalCredito != 0
					|| this.totalDebito != 0 || this.totalSaldoAnterior != 0
					|| this.totalSaldoMoedaEstrangeira != 0) {
				this.incluirRelatorio(mesAno, this.totalClassificacaoContas,
						this.totalDebito, this.totalCredito,
						this.totalSaldoAnterior,
						this.totalSaldoMoedaEstrangeira, seguradora);
			}

		} catch (Exception exception) {
			this.getModelManager().rollbackTransaction();
		}

		return this.total;
	}

	public double obterTotalizacaoClassificaoContas(Entidade entidade,
			boolean emGuarani, String mesAno) throws Exception {
		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");

		for (Iterator i = entidade.obterInferiores().iterator(); i.hasNext();) {
			Entidade e = (Entidade) i.next();
			if (e instanceof ClassificacaoContas)
				this.obterTotalizacaoClassificaoContas(e, emGuarani, mesAno);
			else if (e instanceof Conta) {
				if (emGuarani)
					this.totalClassificacaoContas += home.obterTotalizacao(e,
							mesAno);
				else
					this.totalClassificacaoContas += home
							.obterTotalizacaoSaldoMoedaEstrangeira(e, mesAno);

				this.total = this.totalClassificacaoContas;
				this.totalDebito += home.obterTotalizacaoDebito();
				this.totalCredito += home.obterTotalizacaoCredito();
				this.totalSaldoAnterior += home.obterTotalizacaoSaldoAnterior();
				this.totalSaldoMoedaEstrangeira += home
						.obterTotalizacaoSaldoMoedaEstrangeira();
			}
		}

		return this.totalClassificacaoContas;
	}

	public double obterTotalizacaoCredito(Entidade entidade, String mesAno)
			throws Exception {
		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");

		return home.obterTotalizacaoCredito(this, mesAno);
	}

	public double obterTotalizacaoDebito(Entidade entidade, String mesAno)
			throws Exception {
		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");

		return home.obterTotalizacaoDebito(this, mesAno);
	}

	public double obterTotalizacaoSaldoAnterior(Entidade entidade, String mesAno)
			throws Exception {
		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");

		return home.obterTotalizacaoSaldoAnterior(this, this.obterMesAnoAnterior(mesAno));
	}

	public double obterTotalizacaoSaldoAnteriorMoedaEstrangeira(
			Entidade entidade, String mesAno) throws Exception {
		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");

		return home.obterTotalizacaoSaldoAnterior(this, this
				.obterMesAnoAnterior(mesAno));
	}

	public double obterTotalizacaoSaldoAnteriorExistente(Entidade seguradora,
			String mesAno) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select saldo_anterior from relatorio where id=? and mes_ano=? and seguradora=?");
		query.addLong(this.obterId());

		String mesAnoModificado = "";

		for (int i = 0; i < mesAno.length(); i++) {

			String caracter = mesAno.substring(i, i + 1);

			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
				mesAnoModificado += caracter;
		}

		query.addString(mesAnoModificado);
		query.addLong(seguradora.obterId());

		//System.out.println("select valor from relatorio where
		// id="+this.obterId()+" and mes_ano="+mesAno+" and
		// seguradora="+seguradora.obterId());

		return query.executeAndGetFirstRow().getDouble("saldo_anterior");
	}

	private String obterMesAnoAnterior(String mesAno) throws Exception {
		String mesAnoModificado = "";
		String mesAnoModificado2 = "";

		for (int i = 0; i < mesAno.length(); i++) {

			String caracter = mesAno.substring(i, i + 1);

			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
				mesAnoModificado += caracter;
		}

		int mesAnterior = Integer.parseInt(mesAnoModificado.substring(0, 2));
		int anoAnterior = Integer.parseInt(mesAnoModificado.substring(2,
				mesAnoModificado.length()));

		if (mesAnterior == 1) {
			mesAnterior = 12;
			anoAnterior--;
		} else
			mesAnterior--;

		if (new Integer(mesAnterior).toString().length() == 1)
			mesAnoModificado2 = "0" + new Integer(mesAnterior).toString()
					+ new Integer(anoAnterior).toString();
		else
			mesAnoModificado2 = new Integer(mesAnterior).toString()
					+ new Integer(anoAnterior).toString();

		return mesAnoModificado2;
	}

	public double obterTotalizacaoSaldoAnteriorMoedaEstrangeiraExistente(
			Entidade seguradora, String mesAno) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select saldo_anteiror_moeda_estrangeira from relatorio where id=? and mes_ano=? and seguradora=?");
		query.addLong(this.obterId());
		query.addString(this.obterMesAnoAnterior(mesAno));
		query.addLong(seguradora.obterId());
		//System.out.println("select valor from relatorio where
		// id="+this.obterId()+" and mes_ano="+mesAnoModificado+" and
		// seguradora="+seguradora.obterId());

		return query.executeAndGetFirstRow().getDouble(
				"saldo_anterior_moeda_estrangeira");
	}

	public double obterTotalizacaoExistente(Entidade seguradora, String mesAno)throws Exception
	{
		//SQLQuery query = this.getModelManager().createSQLQuery("crm","select valor from relatorio where id=? and mes_ano=? and seguradora=?");
		SQLQuery query = this.getModelManager().createSQLQuery("crm","EXEC obterTotalizacaoExistenteCcontas ?,?,?");
		query.addLong(this.obterId());

		String mesAnoModificado = "";

		for (int i = 0; i < mesAno.length(); i++) 
		{
			String caracter = mesAno.substring(i, i + 1);

			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
				mesAnoModificado += caracter;
		}
		
		query.addString(mesAnoModificado);
		query.addLong(seguradora.obterId());

		//if(query.execute().length > 0)
			return query.executeAndGetFirstRow().getDouble("valor");
		//else
			//return 0;
	}
	
	public BigDecimal obterTotalizacaoExistenteBIG(Entidade seguradora, String mesAno)throws Exception
	{
		BigDecimal valor = new BigDecimal(obterTotalizacaoExistente(seguradora,mesAno));
		
		return valor;
	}
	
	public double obterTotalizacaoExistente(Collection<Entidade> aseguradoras, String mesAno)throws Exception
	{
		double total = 0;

		for(Entidade e : aseguradoras)
		{
			total+=this.obterTotalizacaoExistente(e,mesAno);
		}

		return total;
	}
	
	public BigDecimal obterTotalizacaoExistenteBIG(Collection<Entidade> aseguradoras, String mesAno)throws Exception
	{
		BigDecimal total = new BigDecimal("0.00");

		for(Entidade e : aseguradoras)
			total = total.add(new BigDecimal(this.obterTotalizacaoExistente(e, mesAno)));

		return total;
	}
	
	public double obterTotalizacaoExistenteAnual(Collection<Entidade> aseguradoras, int ano)throws Exception
	{
		double total = 0;
		
		String dataInicioStr = "01/"+ano;
		String dataFimStr = "12/"+ano;
		
		Date dataInicio = new SimpleDateFormat("MM/yyyy").parse(dataInicioStr);
		Date dataFim = new SimpleDateFormat("MM/yyyy").parse(dataFimStr);
		
		Calendar c = Calendar.getInstance();
		
		for(Entidade e : aseguradoras)
		{
			c.setTime(dataInicio);
			
			while(c.getTime().compareTo(dataFim)<=0)
			{
				String mesAnoModificado = new SimpleDateFormat("MMyyyy").format(c.getTime());
				 
				total+=this.obterTotalizacaoExistente(e,mesAnoModificado);
				 
				c.add(Calendar.MONTH, 1);
			}
		}

		return total;
	}
	
	public BigDecimal obterTotalizacaoExistenteAnualBIG(Collection<Entidade> aseguradoras, int ano)throws Exception
	{
		BigDecimal total = new BigDecimal("0.00");
		
		String dataInicioStr = "01/"+ano;
		String dataFimStr = "12/"+ano;
		
		Date dataInicio = new SimpleDateFormat("MM/yyyy").parse(dataInicioStr);
		Date dataFim = new SimpleDateFormat("MM/yyyy").parse(dataFimStr);
		
		Calendar c = Calendar.getInstance();
		
		for(Entidade e : aseguradoras)
		{
			c.setTime(dataInicio);
			
			while(c.getTime().compareTo(dataFim)<=0)
			{
				String mesAnoModificado = new SimpleDateFormat("MMyyyy").format(c.getTime());
				 
				total = total.add(this.obterTotalizacaoExistenteBIG(e,mesAnoModificado));
				 
				c.add(Calendar.MONTH, 1);
			}
		}

		return total;
	}
	
	public BigDecimal obterTotalizacaoExistenteAnualBIG(Entidade aseguradora, int ano)throws Exception
	{
		BigDecimal total = new BigDecimal("0.00");
		
		String dataInicioStr = "01/"+ano;
		String dataFimStr = "12/"+ano;
		
		Date dataInicio = new SimpleDateFormat("MM/yyyy").parse(dataInicioStr);
		Date dataFim = new SimpleDateFormat("MM/yyyy").parse(dataFimStr);
		
		Calendar c = Calendar.getInstance();
		
		c.setTime(dataInicio);
			
		while(c.getTime().compareTo(dataFim)<=0)
		{
			String mesAnoModificado = new SimpleDateFormat("MMyyyy").format(c.getTime());
			 
			total = total.add(this.obterTotalizacaoExistenteBIG(aseguradora,mesAnoModificado));
			 
			c.add(Calendar.MONTH, 1);
		}

		return total;
	}
	
	public double obterTotalizacaoExistenteAnual(Entidade aseguradora, int ano)throws Exception
	{
		double total = 0;
		
		String dataInicioStr = "01/"+ano;
		String dataFimStr = "12/"+ano;
		
		Date dataInicio = new SimpleDateFormat("MM/yyyy").parse(dataInicioStr);
		Date dataFim = new SimpleDateFormat("MM/yyyy").parse(dataFimStr);
		
		Calendar c = Calendar.getInstance();
		
		c.setTime(dataInicio);
			
		while(c.getTime().compareTo(dataFim)<=0)
		{
			String mesAnoModificado = new SimpleDateFormat("MMyyyy").format(c.getTime());
			 
			total+=this.obterTotalizacaoExistente(aseguradora,mesAnoModificado);
			 
			c.add(Calendar.MONTH, 1);
		}

		return total;
	}
	
	public BigDecimal obterTotalizacaoExistenteAnualFiscalBIG(Collection<Entidade> aseguradoras, int ano)throws Exception
	{
		BigDecimal total = new BigDecimal("0.00");
		String dataFimStr,dataInicioStr;
		
		dataInicioStr = "07/"+ano;
		dataFimStr = "06/"+(ano+1);
		
		Date dataInicio = new SimpleDateFormat("MM/yyyy").parse(dataInicioStr);
		Date dataFim = new SimpleDateFormat("MM/yyyy").parse(dataFimStr);
		
		Calendar c = Calendar.getInstance();
		
		for(Entidade e : aseguradoras)
		{
			c.setTime(dataInicio);
			
			while(c.getTime().compareTo(dataFim)<=0)
			{
				String mesAnoModificado = new SimpleDateFormat("MMyyyy").format(c.getTime());
				 
				total = total.add(this.obterTotalizacaoExistenteBIG(e,mesAnoModificado));
				 
				c.add(Calendar.MONTH, 1);
			}
		}

		return total;
	}
	
	public BigDecimal obterTotalizacaoExistenteAnualFiscalBIG(Entidade aseguradora, int ano)throws Exception
	{
		BigDecimal total = new BigDecimal("0.00");
		
		String dataFimStr,dataInicioStr;
		
		dataInicioStr = "07/"+ano;
		dataFimStr = "06/"+(ano+1);
		
		Date dataInicio = new SimpleDateFormat("MM/yyyy").parse(dataInicioStr);
		Date dataFim = new SimpleDateFormat("MM/yyyy").parse(dataFimStr);
		
		Calendar c = Calendar.getInstance();
		
		c.setTime(dataInicio);
			
		while(c.getTime().compareTo(dataFim)<=0)
		{
			String mesAnoModificado = new SimpleDateFormat("MMyyyy").format(c.getTime());
			 
			total = total.add(this.obterTotalizacaoExistenteBIG(aseguradora,mesAnoModificado));
			 
			c.add(Calendar.MONTH, 1);
		}

		return total;
	}

	public double obterTotalizacaoExistenteUltimos12Meses(Entidade seguradora)
			throws Exception {
		Date dataAtual = new Date();

		Calendar calendario = Calendar.getInstance();
		calendario.setTime(dataAtual);

		double valor = 0;

		for (int i = 0; i < 12; i++) {

			calendario.add(Calendar.MONTH, -1);

			String mesAnoModificado = new SimpleDateFormat("MMyyyy")
					.format(calendario.getTime());

			SQLQuery query = this
					.getModelManager()
					.createSQLQuery("crm",
							"select valor from relatorio where id=? and mes_ano=? and seguradora=?");
			query.addLong(this.obterId());

			query.addString(mesAnoModificado);
			query.addLong(seguradora.obterId());

			valor += query.executeAndGetFirstRow().getDouble("valor");
		}

		return valor;
	}

	public double obterTotalizacaoExistenteOutraMoeda(Entidade seguradora,
			String mesAno) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select valor_outro from relatorio where id=? and mes_ano=? and seguradora=?");
		query.addLong(this.obterId());

		String mesAnoModificado = "";

		for (int i = 0; i < mesAno.length(); i++) {

			String caracter = mesAno.substring(i, i + 1);

			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
				mesAnoModificado += caracter;
		}

		query.addString(mesAnoModificado);
		query.addLong(seguradora.obterId());
		//query.addInt(1);

		//System.out.println("select valor_outro from relatorio where
		// id="+this.obterId()+" and mes_ano="+mesAnoModificado+" and
		// seguradora=" + seguradora.obterId());

		return query.executeAndGetFirstRow().getDouble("valor_outro");
	}

	public double obterTotalizacaoDebitoExistente(Entidade seguradora,
			String mesAno) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select debito from relatorio where id=? and mes_ano=? and seguradora=?");
		query.addLong(this.obterId());

		String mesAnoModificado = "";

		for (int i = 0; i < mesAno.length(); i++) {
			String caracter = mesAno.substring(i, i + 1);

			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
				mesAnoModificado += caracter;
		}

		query.addString(mesAnoModificado);
		query.addLong(seguradora.obterId());
		//System.out.println("select valor from relatorio where
		// id="+this.obterId()+" and mes_ano="+mesAnoModificado+" and
		// seguradora="+seguradora.obterId());

		return query.executeAndGetFirstRow().getDouble("debito");
	}

	public double obterTotalizacaoCreditoAnoAnterior(Entidade seguradora,
			String mesAno) throws Exception {
		double total = 0;

		String mes = mesAno.substring(0, 2);
		String ano = mesAno.substring(2, mesAno.length());

		String dataStr = "01/" + mes + "/" + ano;

		Date dataEscolhida = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);

		Calendar data = Calendar.getInstance();
		data.setTime(dataEscolhida);

		data.add(Calendar.MONTH, -12);

		String mesAnoAnterior = new SimpleDateFormat("MMyyyy").format(data
				.getTime());

		total = this.obterTotalizacaoCreditoExistente(seguradora,
				mesAnoAnterior);

		if (total == 0) {
			if (this.obterPrimeiroMes(seguradora) != null)
				total = this.obterTotalizacaoCreditoExistente(seguradora, this
						.obterPrimeiroMes(seguradora));
		}

		return total;
	}

	public double obterTotalizacaoSaldoAnoAnterior(Entidade seguradora,
			String mesAno) throws Exception {
		double total = 0;

		String mes = mesAno.substring(0, 2);
		String ano = mesAno.substring(2, mesAno.length());

		String dataStr = "01/" + mes + "/" + ano;

		Date dataEscolhida = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);

		Calendar data = Calendar.getInstance();
		data.setTime(dataEscolhida);

		data.add(Calendar.MONTH, -12);

		String mesAnoAnterior = new SimpleDateFormat("MMyyyy").format(data.getTime());

		total = this.obterTotalizacaoExistente(seguradora, mesAnoAnterior);

		if (total == 0) 
		{
			//System.out.println("Total Classi = 0 + Id: " + this.obterApelido());
			
			if (this.obterPrimeiroMes(seguradora) != null)
				total = this.obterTotalizacaoExistente(seguradora, this.obterPrimeiroMes(seguradora));
		}

		return total;
	}

	public String obterPrimeiroMes(Entidade seguradora) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"SELECT mes_ano FROM relatorio WHERE seguradora = ? GROUP BY mes_ano");
		query.addLong(seguradora.obterId());

		/*
		 * if(this.obterApelido().equals("0505000000")) {
		 * System.out.println("SELECT mes_ano FROM relatorio WHERE id =
		 * "+this.obterId()+" and seguradora = "+seguradora.obterId()+" GROUP BY
		 * mes_ano"); }
		 */

		SQLRow[] rows = query.execute();

		Date data = null;

		String menorData = null;

		for (int i = 0; i < rows.length; i++) {
			String dataStr = rows[i].getString("mes_ano");

			String mes = dataStr.substring(0, 2);

			String ano = dataStr.substring(2, dataStr.length());

			Date data2 = new SimpleDateFormat("dd/MM/yyyy").parse("01/" + mes
					+ "/" + ano);

			if (data == null)
				data = data2;
			else {
				if (data2.before(data))
					data = data2;
			}
		}

		if (data != null)
			menorData = new SimpleDateFormat("MMyyyy").format(data);

		return menorData;
	}

	public double obterTotalizacaoCreditoExistente(Entidade seguradora,String mesAno) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select credito from relatorio where id=? and mes_ano=? and seguradora=?");
		query.addLong(this.obterId());

		String mesAnoModificado = "";

		if(mesAno.indexOf("/")!=-1)
		{
			for (int i = 0; i < mesAno.length(); i++) 
			{
				String caracter = mesAno.substring(i, i + 1);
	
				if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
					mesAnoModificado += caracter;
			}
			query.addString(mesAnoModificado);
		}
		else
			query.addString(mesAno);

		query.addLong(seguradora.obterId());

		//System.out.println("select credito from relatorio where
		// id="+this.obterId()+" and mes_ano="+mesAnoModificado+" and
		// seguradora="+seguradora.obterId());

		return query.executeAndGetFirstRow().getDouble("credito");
	}
	
	/*public double obterTotalizacaoSaldoExistente(Entidade seguradora,String mesAno) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select valor from relatorio where id=? and mes_ano=? and seguradora=?");
		query.addLong(this.obterId());

		String mesAnoModificado = "";

		if(mesAno.indexOf("/")!=-1)
		{
			for (int i = 0; i < mesAno.length(); i++) 
			{
				String caracter = mesAno.substring(i, i + 1);
	
				if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
					mesAnoModificado += caracter;
			}
			query.addString(mesAnoModificado);
		}
		else
			query.addString(mesAno);

		query.addLong(seguradora.obterId());

		return query.executeAndGetFirstRow().getDouble("valor");
	}*/

	public boolean existeValor(String mesAno) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select existe_valor_guarani from relatorio where id=? and mes_ano=?");
		query.addLong(this.obterId());

		String mesAnoModificado = "";

		for (int i = 0; i < mesAno.length(); i++) {

			String caracter = mesAno.substring(i, i + 1);

			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
				mesAnoModificado += caracter;
		}

		//System.out.println("select existe_valor_guarani from relatorio where
		// id="+this.obterId()+" and mes_ano=" + mesAnoModificado +" and
		// existe_valor_guarani=" + 1);

		query.addString(mesAnoModificado);

		if (query.executeAndGetFirstRow().getInt("existe_valor_guarani") == 1)
			return true;
		else
			return false;
	}

	public boolean existeOutroValor(String mesAno) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select valor_outro from relatorio where id=? and mes_ano=? and existe_outro_valor=?");
		query.addLong(this.obterId());

		String mesAnoModificado = "";

		for (int i = 0; i < mesAno.length(); i++) {

			String caracter = mesAno.substring(i, i + 1);

			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
				mesAnoModificado += caracter;
		}
		query.addString(mesAnoModificado);
		query.addInt(1);

		SQLRow[] rows = query.execute();

		if (rows.length > 0)
			return true;
		else
			return false;
	}
	
	public boolean permiteAtualizar() throws Exception
	{
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		Entidade ieta = home.obterEntidadePorApelido("intendenteieta");
		
		if(this.obterUsuarioAtual().obterId() == 1)
			return true;
		else if(ieta!=null)
		{
			if(this.obterUsuarioAtual().obterSuperiores().contains(ieta))
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
		
		if(this.obterInferiores().size() > 0)
			retorno = false;
		else
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from evento,movimentacao_financeira_conta where evento.id = movimentacao_financeira_conta.id and conta = ?");
			query.addLong(this.obterId());
			
			if(query.executeAndGetFirstRow().getInt("qtde") > 0)
				retorno = false;
		}
		
		return retorno;
	}
	
	/*public int obterQtdeSinistrosPorSecaoVigente(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select count(*) as qtde from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and secao=? and data_sinistro>=? and data_sinistro<=? and situacao_seguro = 'Vigente' group by origem,secao");
		query.addLong(aseguradora.obterId());
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		return query.executeAndGetFirstRow().getInt("qtde");
	}*/
	
	public int obterQtdeSinistrosPorSecaoNoVigente(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select count(*) as qtde from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and secao=? and data_sinistro>=? and data_sinistro<=? and situacao_seguro = 'No Vigente' group by origem,secao");
		query.addLong(aseguradora.obterId());
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		return query.executeAndGetFirstRow().getInt("qtde");
	}
	
	public double obterValorSinistrosRecuperadosPorSecaoVigente(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception 
	{
		EventoHome eventohome = (EventoHome) this.getModelManager().getHome("EventoHome");
		double total = 0;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select evento.id from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and secao=?" +
				" and data_sinistro>= ? and data_sinistro<=? and situacao_seguro = 'Vigente' and data_prevista_inicio>=? and data_prevista_conclusao<=?");
		query.addLong(aseguradora.obterId());
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Sinistro sinistro = (Sinistro) eventohome.obterEventoPorId(id);
			Apolice apolice = (Apolice) sinistro.obterSuperior();
			
			boolean temAspecto = false;
			
			for(Iterator j = apolice.obterInferiores().iterator() ; j.hasNext() ; )
			{
				Evento e = (Evento) j.next();
				
				if(e instanceof AspectosLegais)
				{
					temAspecto = true;
					break;
				}
			}
			
			if(!temAspecto)
			{
				for(Iterator j = sinistro.obterInferiores().iterator() ; j.hasNext() ; )
				{
					Evento e = (Evento) j.next();
					
					if(e instanceof RegistroGastos)
					{
						RegistroGastos registroGastos = (RegistroGastos) e;
						total+=registroGastos.obterAbonoGs();
					}
					else if(e instanceof FaturaSinistro)
					{
						FaturaSinistro faturaSinistro = (FaturaSinistro) e;
						total+=faturaSinistro.obterMontantePago();
					}
				}
			}
		}
		
		//System.out.println("select SUM(valor_recuperacao) as soma from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and secao=? and data_prevista_inicio>=? and data_prevista_conclusao<=? group by origem,secao");
		
		return total;
	}
	
	public double obterValorSinistrosRecuperadosPorSecaoVigenteJudicializado(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception 
	{
		EventoHome eventohome = (EventoHome) this.getModelManager().getHome("EventoHome");
		double total = 0;
		
		String sql = "select evento.id from evento,aspectos_legais,apolice where evento.id = aspectos_legais.id and superior = apolice.id";
		
		sql+=" and origem = " + aseguradora.obterId() + " and secao = "+this.obterId()+" and situacao_seguro = 'Vigente'";
		sql+=" and data_prevista_inicio>=" + dataInicio.getTime() + " and data_prevista_conclusao<=" + dataFim.getTime();
		sql+=" order by data_prevista_inicio";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
				
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			AspectosLegais aspecto = (AspectosLegais) eventohome.obterEventoPorId(id);
			Apolice apolice = (Apolice) aspecto.obterSuperior();
			
			for(Iterator j = apolice.obterInferiores().iterator() ; j.hasNext() ; )
			{
				Evento e = (Evento) j.next();
				
				if(e instanceof Sinistro)
				{
					Sinistro sinistro = (Sinistro) e;
					
					if(sinistro.obterSituacao().equals("Pagado") && sinistro.obterDataSinistro().compareTo(dataInicio)>=0 && sinistro.obterDataSinistro().compareTo(dataFim)<=0)
					{					
						for(Iterator k = sinistro.obterInferiores().iterator() ; k.hasNext() ; )
						{
							Evento e2 = (Evento) k.next();
							
							if(e2 instanceof RegistroGastos)
							{
								RegistroGastos registroGastos = (RegistroGastos) e2;
								total+=registroGastos.obterAbonoGs();
							}
							else if(e2 instanceof FaturaSinistro)
							{
								FaturaSinistro faturaSinistro = (FaturaSinistro) e2;
								total+=faturaSinistro.obterMontantePago();
							}
						}
					}
				}
			}
		}
		
		
		/*SQLQuery query = this.getModelManager().createSQLQuery("crm", "select evento.id from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and secao=?" +
				" and data_sinistro>= ? and data_sinistro<=? and situacao_seguro = 'Vigente'");
		query.addLong(aseguradora.obterId());
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Sinistro sinistro = (Sinistro) eventohome.obterEventoPorId(id);
			Apolice apolice = (Apolice) sinistro.obterSuperior();
			
			boolean temAspecto = false;
			
			for(Iterator j = apolice.obterInferiores().iterator() ; j.hasNext() ; )
			{
				Evento e = (Evento) j.next();
				
				if(e instanceof AspectosLegais)
				{
					temAspecto = true;
					break;
				}
			}
			
			if(temAspecto)
			{
				for(Iterator j = sinistro.obterInferiores().iterator() ; j.hasNext() ; )
				{
					Evento e = (Evento) j.next();
					
					if(e instanceof RegistroGastos)
					{
						RegistroGastos registroGastos = (RegistroGastos) e;
						total+=registroGastos.obterAbonoGs();
					}
					else if(e instanceof FaturaSinistro)
					{
						FaturaSinistro faturaSinistro = (FaturaSinistro) e;
						total+=faturaSinistro.obterMontantePago();
					}
				}
			}
		}*/
		
		//System.out.println("select SUM(valor_recuperacao) as soma from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and secao=? and data_prevista_inicio>=? and data_prevista_conclusao<=? group by origem,secao");
		
		return total;
	}
	
	public double obterValorSinistrosRecuperadosPorSecaoNoVigenteJudicializado(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception 
	{
		EventoHome eventohome = (EventoHome) this.getModelManager().getHome("EventoHome");
		double total = 0;
		
		String sql = "select evento.id from evento,aspectos_legais,apolice where evento.id = aspectos_legais.id and superior = apolice.id";
		
		sql+=" and origem = " + aseguradora.obterId() + " and secao = "+this.obterId()+" and situacao_seguro = 'No Vigente'";
		sql+=" and data_prevista_inicio>=" + dataInicio.getTime() + " and data_prevista_conclusao<=" + dataFim.getTime();
		sql+=" order by data_prevista_inicio";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
				
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			AspectosLegais aspecto = (AspectosLegais) eventohome.obterEventoPorId(id);
			Apolice apolice = (Apolice) aspecto.obterSuperior();
			
			for(Iterator j = apolice.obterInferiores().iterator() ; j.hasNext() ; )
			{
				Evento e = (Evento) j.next();
				
				if(e instanceof Sinistro)
				{
					Sinistro sinistro = (Sinistro) e;
					
					if(sinistro.obterSituacao().equals("Pagado") && sinistro.obterDataSinistro().compareTo(dataInicio)>=0 && sinistro.obterDataSinistro().compareTo(dataFim)<=0)
					{					
						for(Iterator k = sinistro.obterInferiores().iterator() ; k.hasNext() ; )
						{
							Evento e2 = (Evento) k.next();
							
							if(e2 instanceof RegistroGastos)
							{
								RegistroGastos registroGastos = (RegistroGastos) e2;
								total+=registroGastos.obterAbonoGs();
							}
							else if(e2 instanceof FaturaSinistro)
							{
								FaturaSinistro faturaSinistro = (FaturaSinistro) e2;
								total+=faturaSinistro.obterMontantePago();
							}
						}
					}
				}
			}
		}
		
		/*EventoHome eventohome = (EventoHome) this.getModelManager().getHome("EventoHome");
		double total = 0;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select evento.id from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and secao=?" +
				" and data_sinistro>= ? and data_sinistro<=? and situacao_seguro = 'No Vigente'");
		query.addLong(aseguradora.obterId());
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Sinistro sinistro = (Sinistro) eventohome.obterEventoPorId(id);
			Apolice apolice = (Apolice) sinistro.obterSuperior();
			
			boolean temAspecto = false;
			
			for(Iterator j = apolice.obterInferiores().iterator() ; j.hasNext() ; )
			{
				Evento e = (Evento) j.next();
				
				if(e instanceof AspectosLegais)
				{
					temAspecto = true;
					break;
				}
			}
			
			if(temAspecto)
			{
				for(Iterator j = sinistro.obterInferiores().iterator() ; j.hasNext() ; )
				{
					Evento e = (Evento) j.next();
					
					if(e instanceof RegistroGastos)
					{
						RegistroGastos registroGastos = (RegistroGastos) e;
						total+=registroGastos.obterAbonoGs();
					}
					else if(e instanceof FaturaSinistro)
					{
						FaturaSinistro faturaSinistro = (FaturaSinistro) e;
						total+=faturaSinistro.obterMontantePago();
					}
				}
			}
		}*/
		
		//System.out.println("select SUM(valor_recuperacao) as soma from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and secao=? and data_prevista_inicio>=? and data_prevista_conclusao<=? group by origem,secao");
		
		return total;
	}
	
	public double obterValorSinistrosRecuperadosPorSecaoNoVigente(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception 
	{
		EventoHome eventohome = (EventoHome) this.getModelManager().getHome("EventoHome");
		double total = 0;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select evento.id from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and secao=?" +
				" and data_sinistro>= ? and data_sinistro<=? and situacao_seguro = 'No Vigente' and data_prevista_inicio>=? and data_prevista_conclusao<=?");
		query.addLong(aseguradora.obterId());
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Sinistro sinistro = (Sinistro) eventohome.obterEventoPorId(id);
			Apolice apolice = (Apolice) sinistro.obterSuperior();
			
			boolean temAspecto = false;
			
			for(Iterator j = apolice.obterInferiores().iterator() ; j.hasNext() ; )
			{
				Evento e = (Evento) j.next();
				
				if(e instanceof AspectosLegais)
				{
					temAspecto = true;
					break;
				}
			}
			
			if(!temAspecto)
			{
				for(Iterator j = sinistro.obterInferiores().iterator() ; j.hasNext() ; )
				{
					Evento e = (Evento) j.next();
					
					if(e instanceof RegistroGastos)
					{
						RegistroGastos registroGastos = (RegistroGastos) e;
						total+=registroGastos.obterAbonoGs();
					}
					else if(e instanceof FaturaSinistro)
					{
						FaturaSinistro faturaSinistro = (FaturaSinistro) e;
						total+=faturaSinistro.obterMontantePago();
					}
				}
			}
		}
		
		//System.out.println("select SUM(valor_recuperacao) as soma from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and secao=? and data_prevista_inicio>=? and data_prevista_conclusao<=? group by origem,secao");
		
		return total;
	}

	/*public double obterValorSinistrosPagosPorSecaoVigente(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select SUM(montante_gs) as soma from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and secao=? " +
				"and data_sinistro>=? and data_sinistro<=? and situacao_seguro = 'Vigente' group by origem,secao");
		query.addLong(aseguradora.obterId());
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		return query.executeAndGetFirstRow().getDouble("soma");
	}*/
	
	public double obterValorSinistrosPagosPorSecaoNoVigente(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select SUM(montante_gs) as soma from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id and origem=? and situacao='Pagado' and secao=? " +
				"and data_sinistro>=? and data_sinistro<=? and situacao_seguro = 'No Vigente' group by origem,secao");
		query.addLong(aseguradora.obterId());
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		return query.executeAndGetFirstRow().getDouble("soma");
	}
	
	private String aseguradorasMenor80 = "";
	
	/*public void instanciarAseguradorasMenor80() throws Exception
	{
		AseguradoraHome home = (AseguradoraHome) this.getModelManager().getHome("AseguradoraHome");
		int cont = 0;
		
		for(Iterator i = home.obterAseguradorasPorMenor80OrdenadoPorNome().iterator() ; i.hasNext() ; )
		{
			Aseguradora aseg = (Aseguradora) i.next();
			
			if(aseg.obterId()!=5205)
			{
				if(cont == 0)
					aseguradorasMenor80+="(seguradora=" + aseg.obterId();
				else
					aseguradorasMenor80+=" or seguradora=" + aseg.obterId();
				
				cont++;
			}
		}
		
		aseguradorasMenor80+=")";
		
		//System.out.println(aseguradorasMenor80);
	}*/
	
	public double obterTotalSaldoAtualMensal(Date data, String aseguradoras) throws Exception
	{
		double total = 0;
		
		String mesAno = new SimpleDateFormat("MMyyyy").format(data);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select SUM(valor) as valor from relatorio where id = ? and mes_ano = ? and " + aseguradoras);
		query.addLong(this.obterId());
		query.addString(mesAno);
		
		total += query.executeAndGetFirstRow().getDouble("valor");
			
		//System.out.println("select SUM(valor) as valor from relatorio where id = "+this.obterId()+" and mes_ano = "+mesAno+" and " + aseguradoras);
		
		return total;
	}
}