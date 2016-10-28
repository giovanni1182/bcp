package com.gvs.crm.model.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.MovimentacaoFinanceiraConta;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class ContaImpl extends EntidadeImpl implements Conta {
	private String descricao;

	private String codigoConta;

	private String ativo;

	private boolean ativoBoolean;

	private String concepto;

	private String noma;

	private String natureza;

	private String dinamica;

	private String nivel;

	private double total;

	public class ContaImpl2 implements Conta2 {

		private long id, superior;

		private String codigoConta2, nomeConta;

		ContaImpl2(long id, long superior, String codigoConta, String nomeConta) {
			/*
			 * System.out.println("Id: " + id); System.out.println("superior: " +
			 * superior); System.out.println("codigoConta: " + codigoConta);
			 * System.out.println("nomeConta: " + nomeConta);
			 */

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

		public String obterCodigoConta() throws Exception {
			return this.codigoConta2;
		}

		public String obterNomeConta() throws Exception {
			return this.nomeConta;
		}

	}

	public void atribuirAtivo(String ativo) throws Exception {
		this.ativo = ativo;
	}

	public void atribuirNivel(String nivel) throws Exception {
		this.nivel = nivel;
	}

	public void atribuirConcepto(String concepto) throws Exception {
		this.concepto = concepto;
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

	public void atualizarAtivo(String ativo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update conta set ativo=? where id=?");
		update.addString(ativo);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarNivel(String nivel) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("update conta set nivel=? where id=?");
		update.addString(nivel);
		update.addLong(this.obterId());
		update.execute();
		
		update = this.getModelManager().createSQLUpdate("update relatorio set nivel=? where id=?");
		update.addString(nivel);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarCodigo(String codigo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update conta set codigo_conta=? where id=?");
		update.addString(codigo);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarConcepto(String concepto) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update conta set concepto=? where id=?");
		update.addString(concepto);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarNoma(String noma) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update conta set noma=? where id=?");
		update.addString(noma);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarNatureza(String natureza) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update conta set natureza=? where id=?");
		update.addString(natureza);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarDinamica(String dinamica) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update conta set dinamica=? where id=?");
		update.addString(dinamica);
		update.addLong(this.obterId());
		update.execute();
	}

	public String obterDescricao() throws Exception {
		if (this.descricao == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select descricao from conta where id=?");
			query.addLong(this.obterId());
			this.descricao = query.executeAndGetFirstRow().getString(
					"descricao");
		}
		return this.descricao;
	}

	public boolean obterAtivo() throws Exception {
		//if (this.ativo == null)
		//{
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select ativo from conta where id=?");
		query.addLong(this.obterId());
		if (query.executeAndGetFirstRow().getString("ativo").equals("true"))
			this.ativoBoolean = true;
		else
			this.ativoBoolean = false;
		//}
		return this.ativoBoolean;
	}

	public String obterNivel() throws Exception {
		if (this.nivel == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select nivel from conta where id=?");
			query.addLong(this.obterId());
			this.nivel = query.executeAndGetFirstRow().getString("nivel");
		}
		return this.nivel;
	}

	public void incluir() throws Exception {
		if (this.codigoConta != null && !this.codigoConta.equals("")) {
			SQLQuery query = this
					.getModelManager()
					.createSQLQuery(
							"select count(id) as quantidade from conta where codigo_conta=?");
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
						"insert into conta (id, codigo_conta, descricao, ativo, concepto, noma, natureza, dinamica, nivel) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		update.addLong(this.obterId());
		update.addString(this.codigoConta);
		update.addString(this.descricao);
		update.addString(this.ativo);
		update.addString(this.concepto);
		update.addString(this.noma);
		update.addString(this.natureza);
		update.addString(this.dinamica);
		update.addString(this.nivel);
		update.execute();
	}

	public String obterCodigo() throws Exception {
		if (this.codigoConta == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select codigo_conta from conta where id=?");
			query.addLong(this.obterId());
			this.codigoConta = query.executeAndGetFirstRow().getString(
					"codigo_conta");
		}
		return this.codigoConta;
	}

	public String obterConcepto() throws Exception {
		if (this.concepto == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select concepto from conta where id=?");
			query.addLong(this.obterId());
			this.concepto = query.executeAndGetFirstRow().getString("concepto");
		}
		return this.concepto;
	}

	public String obterNoma() throws Exception {
		if (this.noma == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select noma from conta where id=?");
			query.addLong(this.obterId());
			this.noma = query.executeAndGetFirstRow().getString("noma");
		}
		return this.noma;
	}

	public double obterTotalizacao(Entidade seguradora, boolean emGuarani,
			String mesAno) throws Exception {
		this.total = 0;

		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		if (home.obterMovimentacao(this, seguradora, mesAno) != null) {
			MovimentacaoFinanceiraConta mf = home.obterMovimentacao(this,
					seguradora, mesAno);

			if (emGuarani)
				this.total += mf.obterSaldoAtual().doubleValue();
			else
				this.total += mf.obterSaldoEstrangeiro().doubleValue();

			if (!this.contas.contains(this)) {
				SQLUpdate update = this
						.getModelManager()
						.createSQLUpdate(
								"crm",
								"insert into relatorio (id, mes_ano, valor, seguradora, nivel, debito, credito, saldo_anterior) values (?, ?, ?, ?, ?, ?, ?, ?)");
				update.addLong(this.obterId());
				String mesAnoModificado = "";

				for (int j = 0; j < mesAno.length(); j++) {

					String caracter = mesAno.substring(j, j + 1);

					if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
						mesAnoModificado += caracter;
				}

				update.addString(mesAnoModificado);
				update.addDouble(mf.obterSaldoAtual().doubleValue());
				update.addLong(seguradora.obterId());
				update.addString(this.obterNivel());
				update.addDouble(mf.obterDebito().doubleValue());
				update.addDouble(mf.obterCredito().doubleValue());
				update.addDouble(mf.obterSaldoAnterior().doubleValue());
				update.execute();

				this.contas.add(this);
			}
		}

		return this.total;
	}

	public MovimentacaoFinanceiraConta obterMovimentacao(Entidade seguradora,
			boolean emGuarani, String mesAno) throws Exception {
		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return home.obterMovimentacao(this, seguradora, mesAno);
	}

	public void incluirRelatorio(String mesAno, double valor,
			double valorMoedaEstrangeira, double debito, double credito,
			double saldoAnterior, Entidade seguradora) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"insert into relatorio (id, mes_ano, valor, seguradora, valor_outro, nivel, debito, credito, saldo_anterior, superior) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		update.addLong(this.obterId());
		String mesAnoModificado = "";

		for (int i = 0; i < mesAno.length(); i++) {

			String caracter = mesAno.substring(i, i + 1);

			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
				mesAnoModificado += caracter;
		}

		update.addString(mesAnoModificado);
		update.addDouble(valor);
		update.addLong(seguradora.obterId());
		update.addDouble(valorMoedaEstrangeira);
		update.addString(this.obterNivel());
		update.addDouble(debito);
		update.addDouble(credito);
		update.addDouble(saldoAnterior);
		update.addLong(this.obterSuperior().obterId());
		update.execute();
	}

	public void incluirRelatorioOutroValor(String mesAno, double valor,
			Entidade seguradora) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"insert into relatorio (id, mes_ano, seguradora, valor_outro,existe_outro_valor, nivel) values (?, ?, ?, ?, ?, ?)");
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
		update.execute();
	}

	public double obterTotalizacaoExistente(Collection<Entidade> aseguradoras, String mesAno) throws Exception 
	{
		double total = 0;
		
		Entidade e;
		
		for(Iterator<Entidade> i = aseguradoras.iterator() ; i.hasNext() ; )
		{
			e = i.next();
			
			total+=this.obterTotalizacaoExistente(e, mesAno);
			
		}

		return total;
	}
	
	public BigDecimal obterTotalizacaoExistenteBIG(Entidade seguradora, String mesAno)throws Exception
	{
		BigDecimal valor = new BigDecimal(obterTotalizacaoExistente(seguradora, mesAno));
		
		return valor;
	}
	
	public BigDecimal obterTotalizacaoExistenteBIG(Collection<Entidade> aseguradoras, String mesAno)throws Exception
	{
		BigDecimal total = new BigDecimal("0.00");

		for(Entidade e : aseguradoras)
			total = total.add(new BigDecimal(this.obterTotalizacaoExistente(e, mesAno)));

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
			 
			total+= this.obterTotalizacaoExistente(aseguradora,mesAnoModificado);
			 
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
	
	
	public double obterTotalizacaoExistente(Entidade seguradora, String mesAno) throws Exception 
	{
		//SQLQuery query = this.getModelManager().createSQLQuery("crm","select valor from relatorio where id=? and mes_ano=? and seguradora=?");
		SQLQuery query = this.getModelManager().createSQLQuery("crm","EXEC obterTotalizacaoExistenteConta ?,?,?");
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
			System.out.println("Total Conta = 0 + Id: " + this.obterApelido());
			
			if (this.obterPrimeiroMes(seguradora) != null)
				total = this.obterTotalizacaoExistente(seguradora, this.obterPrimeiroMes(seguradora));
		}

		return total;
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

	public String obterPrimeiroMes(Entidade seguradora) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"SELECT mes_ano FROM relatorio WHERE seguradora = ? GROUP BY mes_ano");
		query.addLong(seguradora.obterId());

		SQLRow[] rows = query.execute();

		Date data = null;

		String menorData = null;

		for (int i = 0; i < rows.length; i++) 
		{
			String dataStr = rows[i].getString("mes_ano");

			String mes = dataStr.substring(0, 2);

			String ano = dataStr.substring(2, dataStr.length());

			Date data2 = new SimpleDateFormat("MM/yyyy").parse(mes + "/" + ano);

			if (data == null)
				data = data2;
			else 
			{
				if (data2.before(data))
					data = data2;
			}
		}

		if (data != null)
			menorData = new SimpleDateFormat("MMyyyy").format(data);

		return menorData;
	}

	public double obterTotalizacaoCreditoExistente(Entidade seguradora,
			String mesAno) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select credito from relatorio where id=? and mes_ano=? and seguradora=?");
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

		return query.executeAndGetFirstRow().getDouble("credito");
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
		// id="+this.obterId()+" and mes_ano="+mesAnoModificado+" and
		// seguradora="+seguradora.obterId());

		return query.executeAndGetFirstRow().getDouble("saldo_anterior");
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

		//System.out.println("select valor_outro from relatorio where
		// id="+this.obterId()+" and mes_ano="+mesAnoModificado+" and
		// seguradora=" + seguradora.obterId());

		return query.executeAndGetFirstRow().getDouble("valor_outro");
	}

	public String obterNatureza() throws Exception {
		if (this.natureza == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select natureza from conta where id=?");
			query.addLong(this.obterId());
			this.natureza = query.executeAndGetFirstRow().getString("natureza");
		}
		return this.natureza;
	}

	public String obterDinamica() throws Exception {
		if (this.dinamica == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select dinamica from conta where id=?");
			query.addLong(this.obterId());
			this.dinamica = query.executeAndGetFirstRow().getString("dinamica");
		}
		return this.dinamica;
	}

	public void atribuirDescricao(String descricao) throws Exception {
		this.descricao = descricao;
	}

	public void atribuirCodigo(String codigo) throws Exception {
		this.codigoConta = codigo;
	}

	public void atualizarDescricao(String descricao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update conta set descricao=? where id=?");
		update.addString(descricao);
		update.addLong(this.obterId());
		update.execute();
	}

	public void excluir() throws Exception {
		super.excluir();
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"delete from conta where id=?");
		update.addLong(this.obterId());
		update.execute();
	}

	private Collection contas = new ArrayList();

	public Collection obterContas() throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select id,superior,CDconta,DScontas,n5 from pc where n5<>? order by id");
		query.addString("00");
		SQLRow[] rows = query.execute();
		for (int i = 0; i < rows.length; i++) {
			this.contas.add(new ContaImpl2(rows[i].getLong("id"), rows[i]
					.getLong("superior"), rows[i].getString("CDconta"), rows[i]
					.getString("DScontas")));
		}

		return this.contas;
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
	
	public double obterTotalSaldoAtualMensal(Date data, String aseguradoras) throws Exception
	{
		double total = 0;
		
		String mesAno = new SimpleDateFormat("MMyyyy").format(data);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select SUM(valor) as valor from relatorio where id = ? and mes_ano = ? and " + aseguradoras);
		query.addLong(this.obterId());
		query.addString(mesAno);
		
		total += query.executeAndGetFirstRow().getDouble("valor");
			
		//System.out.println("select SUM(valor) as valor from relatorio where id = "+this.obterId()+" and mes_ano = "+mesAno+" and " + this.aseguradorasMenor80);
		
		return total;
	}
}