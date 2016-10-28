package com.gvs.crm.model.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Inscricao;
import com.gvs.crm.model.MorosidadeCentralRisco;
import com.gvs.crm.model.Plano;
import com.gvs.crm.model.SinistroFiniquitadoCentralRisco;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class AseguradoraHomeImpl extends Home implements AseguradoraHome {

	public Collection<Aseguradora> obterAseguradoras() throws Exception 
	{
		Collection<Aseguradora> aseguradoras = new ArrayList<Aseguradora>();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select id from entidade where classe = 'Aseguradora' order by nome");

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) 
		{
			long id = rows[i].getLong("id");

			Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(id);

			aseguradoras.add(aseguradora);
		}

		return aseguradoras;
	}
	
	public void criarTabelaAseguradora() throws Exception
	{
		for(Iterator i = this.obterAseguradoras().iterator() ; i.hasNext() ; )
		{
			Aseguradora a = (Aseguradora) i.next();
			
			SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into aseguradora(id) values(?)");
			insert.addLong(a.obterId());
			
			insert.execute();
		}
	}

	public Collection obterAseguradorasRelatorio(String mesAno)
			throws Exception {
		Map aseguradoras = new TreeMap();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select seguradora,mes_ano from relatorio,entidade where entidade.id = relatorio.seguradora and mes_ano=? group by seguradora,mes_ano");
		query.addString(mesAno);

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("seguradora");

			Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(id);

			aseguradoras.put(aseguradora.obterNome(), aseguradora);
		}

		return aseguradoras.values();
	}

	public Collection obterAseguradorasPorDataResolucao(Date data) throws Exception
	{
		Map aseguradoras = new TreeMap();

		//System.out.println("MES: " + mes + " ANO: " + ano);

		for (Iterator i = this.obterAseguradoras().iterator(); i.hasNext();)
		{
			Aseguradora aseguradora = (Aseguradora) i.next();

			for (Iterator j = aseguradora.obterInscricoes().iterator(); j.hasNext();) 
			{
				Inscricao inscricao = (Inscricao) j.next();

				if (inscricao.obterSituacao().equals("Vigente")) 
				{
					int numeroInscricao = Integer.parseInt(inscricao.obterInscricao());

					if (numeroInscricao <= 80 && numeroInscricao!=16) 
					{
						if (inscricao.obterDataResolucao() != null	&& inscricao.obterDataValidade() != null) 
						{
							String mesResolucao = new SimpleDateFormat("MM").format(inscricao.obterDataResolucao());
							String anoResolucao = new SimpleDateFormat("yyyy").format(inscricao.obterDataResolucao());
							
							String mesValidade = new SimpleDateFormat("MM").format(inscricao.obterDataValidade());
							String anoValidade = new SimpleDateFormat("yyyy").format(inscricao.obterDataValidade());
							
							Date dataResolucao = new SimpleDateFormat("MM/yyyy").parse(mesResolucao + "/" + anoResolucao);
							Date dataValidade = new SimpleDateFormat("MM/yyyy").parse(mesValidade + "/" + anoValidade);
							
							
							if ((data.after(dataResolucao) || data.equals(dataResolucao)) && (data.before(dataValidade) || data.equals(dataValidade)))
							{
								if(aseguradora.obterSigla()!=null)
									aseguradoras.put(aseguradora.obterSigla(),aseguradora);
								else
									aseguradoras.put(aseguradora.obterNome(),aseguradora);
							}
								
						}
					}
				}
			}
		}

		return aseguradoras.values();
	}

	public Collection obterAseguradorasPorMaior80DataResolucao(Date data)
			throws Exception {
		Map aseguradoras = new TreeMap();

		//System.out.println("MES: " + mes + " ANO: " + ano);

		for (Iterator i = this.obterAseguradoras().iterator(); i.hasNext();) {
			Aseguradora aseguradora = (Aseguradora) i.next();

			for (Iterator j = aseguradora.obterInscricoes().iterator(); j.hasNext();) 
			{
				Inscricao inscricao = (Inscricao) j.next();
				
				if (inscricao.obterSituacao().equals("Vigente")) 
				{
					int numeroInscricao = Integer.parseInt(inscricao.obterInscricao());
	
					if (numeroInscricao > 80 && numeroInscricao!=16) 
					{
						if (inscricao.obterDataResolucao() != null	&& inscricao.obterDataValidade() != null) 
						{
							String mesResolucao = new SimpleDateFormat("MM").format(inscricao.obterDataResolucao());
							String anoResolucao = new SimpleDateFormat("yyyy").format(inscricao.obterDataResolucao());
							
							String mesValidade = new SimpleDateFormat("MM").format(inscricao.obterDataValidade());
							String anoValidade = new SimpleDateFormat("yyyy").format(inscricao.obterDataValidade());
							
							Date dataResolucao = new SimpleDateFormat("MM/yyyy").parse(mesResolucao + "/" + anoResolucao);
							Date dataValidade = new SimpleDateFormat("MM/yyyy").parse(mesValidade + "/" + anoValidade);
							
							
							if((data.after(dataResolucao) || data.equals(dataResolucao)) && (data.before(dataValidade) || data.equals(dataValidade)))
							{
								if(aseguradora.obterSigla()!=null)
									aseguradoras.put(aseguradora.obterSigla(),aseguradora);
								else
									aseguradoras.put(aseguradora.obterNome(),aseguradora);
							}
						}
					}
				}
			}
		}

		return aseguradoras.values();
	}
	
	public Collection obterAseguradorasPorMaior80()throws Exception 
	{
		Map aseguradoras = new TreeMap();

		for (Iterator i = this.obterAseguradoras().iterator(); i.hasNext();) 
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
		
			for (Iterator j = aseguradora.obterInscricoes().iterator(); j.hasNext();) 
			{
				Inscricao inscricao = (Inscricao) j.next();
				
				if (inscricao.obterSituacao().equals("Vigente")) 
				{
					int numeroInscricao = Integer.parseInt(inscricao.obterInscricao());
					
					if (numeroInscricao > 80 && numeroInscricao!=16) 
						aseguradoras.put(aseguradora.obterSigla(), aseguradora);
				}
			}
		}
		return aseguradoras.values();
	}
	
	public Collection obterAseguradorasPorMaior80OrdenadasPorNome()throws Exception 
	{
		//Map aseguradoras = new TreeMap();
		Collection aseguradoras = new ArrayList();
		
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		//SQLQuery query = this.getModelManager().createSQLQuery("crm","select origem from evento,inscricao where evento.id = inscricao.id and situacao='Vigente' and CAST(inscricao AS INT)>80 group by origem");
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select origem,nome from evento,inscricao,entidade where evento.id = inscricao.id and evento.origem = entidade.id and situacao='Vigente' and CAST(inscricao AS INT)>80 group by origem,nome order by nome");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("origem");
			
			Entidade e = home.obterEntidadePorId(id);
			
			if(e instanceof Aseguradora)
				aseguradoras.add(e);
		}
		
		return aseguradoras;
	}
	
	public Collection obterAseguradorasPorMenor80()throws Exception 
	{
		Map aseguradoras = new TreeMap();

		for (Iterator i = this.obterAseguradoras().iterator(); i.hasNext();) 
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
		
			for (Iterator j = aseguradora.obterInscricoes().iterator(); j.hasNext();) 
			{
				Inscricao inscricao = (Inscricao) j.next();
				
				if (inscricao.obterSituacao().equals("Vigente")) 
				{
					int numeroInscricao = Integer.parseInt(inscricao.obterInscricao());
		
					if (numeroInscricao <= 80 && numeroInscricao!=16) 
						aseguradoras.put(aseguradora.obterSigla(), aseguradora);
				}
			}
		}
		return aseguradoras.values();
	}
	
	public Collection<Aseguradora> obterAseguradorasPorMenor80OrdenadoPorNome()throws Exception 
	{
		Collection<Aseguradora> aseguradoras = new ArrayList<Aseguradora>();
		
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select origem,nome from evento,inscricao,entidade where evento.id = inscricao.id and evento.origem = entidade.id and situacao='Vigente' and CAST(inscricao AS INT)<=80 group by origem,nome order by nome");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("origem");
			
			Entidade e = home.obterEntidadePorId(id);
			
			if(e instanceof Aseguradora)
				aseguradoras.add((Aseguradora)e);
		}
		
		return aseguradoras;
	}

	public Map obterReaseguradoras(Aseguradora aseguradora, Date dataInicio,Date dataFim) throws Exception 
	{
		Map reaseguradoras = new TreeMap();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		String sql = "select reaseguradora, tipo_contrato,SUM(caiptal_gs) as capital, SUM(prima_gs) as prima, SUM(comissao_gs) as comissao from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = " + aseguradora.obterId() + 
					"and reaseguradora > 0 and data_ini_apo >= "+dataInicio.getTime()+" and data_fim_apo <= "+dataFim.getTime()+" group by reaseguradora,tipo_contrato";
		
		//System.out.println(sql);

		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) 
		{
			long id = rows[i].getLong("reaseguradora");
			String tipoContrato = rows[i].getString("tipo_contrato");
			double capital = rows[i].getDouble("capital");
			double prima = rows[i].getDouble("prima");
			double comissao = rows[i].getDouble("comissao");

			Entidade reaseguradora = (Entidade) home.obterEntidadePorId(id);

			reaseguradoras.put(reaseguradora.obterNome() + "_" + tipoContrato, id + "_" + tipoContrato +"_"+capital+"_"+prima+"_"+comissao);
		}
		
		return reaseguradoras;
	}

	public Map obterCorretores(Aseguradora aseguradora, Date dataInicio,Date dataFim) throws Exception 
	{
		Map corretores = new TreeMap();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select corretora, tipo_contrato from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and corretora > 0 and data_prevista_inicio >= ? and data_prevista_conclusao <= ? group by corretora,tipo_contrato");
		query.addLong(aseguradora.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		//System.out.println("select corretora, tipo_contrato from evento, dados_reaseguro where evento.id = dados_reaseguro.id  and data_prevista_inicio >= "+dataInicio.getTime()+" and data_prevista_conclusao <= "+dataFim.getTime()+" and origem = "+aseguradora.obterId()+" group by corretora,tipo_contrato");

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) 
		{
			long id = rows[i].getLong("corretora");

			String tipoContrato = rows[i].getString("tipo_contrato");

			Entidade corretora = (Entidade) home.obterEntidadePorId(id);

			corretores.put(corretora.obterNome() + "_" + tipoContrato, corretora.obterId() + "_" + tipoContrato);
		}
		
		return corretores;
	}
	
	public Collection<SinistroFiniquitadoCentralRisco> obterSinistrosFiniquitadosCentralRisco(Entidade aseguradora, String nomeAsegurado, String documento)  throws Exception
	{
		Map<String,SinistroFiniquitadoCentralRisco> sinistros = new TreeMap<String,SinistroFiniquitadoCentralRisco>();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		//SQLQuery query = this.getModelManager().createSQLQuery("crm", "select count(*) as qtde,secao,plano,MAX(criacao) as data, SUM(montante_gs) as montante_gs,tipo_moeda_montante_gs,SUM(montante_me) as montante_me from evento,apolice,sinistro where origem = ? and sinistro.situacao = 'Pagado' and evento.id = sinistro.id and evento.superior = apolice.id and nome_asegurado = ? group by secao,plano,tipo_moeda_montante_gs");
		String sql = "select count(*) as qtde,secao,plano,MAX(criacao) as data, SUM(montante_gs) as montante_gs,tipo_moeda_montante_gs,SUM(montante_me) as montante_me from evento,apolice,sinistro where evento.id = sinistro.id and evento.superior = apolice.id and origem = "+aseguradora.obterId()+" and sinistro.situacao = 'Pagado' ";
		if(nomeAsegurado.length() > 0)
			sql+=" and nome_asegurado like ?";
		if(documento.length() > 0)
			sql+=" and RTrim(LTrim(numero_identificacao)) = '"+documento+"'";
		
		sql+=" group by secao,plano,tipo_moeda_montante_gs";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", sql);
		if(nomeAsegurado.length() > 0)
			query.addString("%"+nomeAsegurado+"%");
		
		SQLRow[] rows = query.execute();
		int qtde;
		long dataLong;
		Date data;
		ClassificacaoContas cContas;
		Plano plano;
		double valorMontanteGS,valorMontanteME;
		String tipoMoeda;
		SinistroFiniquitadoCentralRisco sinistro;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			qtde = rows[i].getInt("qtde");
			dataLong = rows[i].getLong("data");
			data = new Date(dataLong);
			cContas = (ClassificacaoContas) entidadeHome.obterEntidadePorId(rows[i].getLong("secao"));
			plano = (Plano) home.obterEventoPorId(rows[i].getLong("plano"));
			valorMontanteGS = rows[i].getDouble("montante_gs");
			tipoMoeda = rows[i].getString("tipo_moeda_montante_gs");
			valorMontanteME = rows[i].getDouble("montante_me");
			
			sinistro = (SinistroFiniquitadoCentralRisco) this.getModelManager().getEntity("SinistroFiniquitadoCentralRisco");
			
			sinistro.atribuirQtdeSinistros(qtde);
			sinistro.atribuirSecao(cContas);
			sinistro.atribuirDataCorte(data);
			sinistro.atribuirPlano(plano);
			sinistro.atribuirMontantePagoGs(valorMontanteGS);
			sinistro.atribuirMoedaMontantePagoME(tipoMoeda);
			sinistro.atribuirMontantePagoME(valorMontanteME);
			
			sinistros.put(cContas.obterId()+"_"+plano.obterId()+"_"+tipoMoeda, sinistro);
		}
		
		sql = "select count(*) as qtde,secao,plano,MAX(criacao) as data, SUM(montante_gs) as montante_gs,tipo_moeda_montante_gs,SUM(montante_me) as montante_me from evento,apolice,sinistro,apolice_asegurados where evento.id = sinistro.id and evento.superior = apolice.id and apolice.id = apolice_asegurados.id and origem = "+aseguradora.obterId()+" and sinistro.situacao = 'Pagado'";
		if(nomeAsegurado.length() > 0)
			sql+=" and (nome + ' '+ sobre_nome like ?)";
		if(documento.length() > 0)
			sql+=" and numero_documento = '"+documento+"'";
		
		sql+=" group by secao,plano,tipo_moeda_montante_gs";
		
		query = this.getModelManager().createSQLQuery("crm", sql);
		if(nomeAsegurado.length() > 0)
			query.addString("%"+nomeAsegurado+"%");
		
		rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			qtde = rows[i].getInt("qtde");
			dataLong = rows[i].getLong("data");
			data = new Date(dataLong);
			cContas = (ClassificacaoContas) entidadeHome.obterEntidadePorId(rows[i].getLong("secao"));
			plano = (Plano) home.obterEventoPorId(rows[i].getLong("plano"));
			valorMontanteGS = rows[i].getDouble("montante_gs");
			tipoMoeda = rows[i].getString("tipo_moeda_montante_gs");
			valorMontanteME = rows[i].getDouble("montante_me");
			
			sinistro = (SinistroFiniquitadoCentralRisco) this.getModelManager().getEntity("SinistroFiniquitadoCentralRisco");
			
			sinistro.atribuirQtdeSinistros(qtde);
			sinistro.atribuirSecao(cContas);
			sinistro.atribuirDataCorte(data);
			sinistro.atribuirPlano(plano);
			sinistro.atribuirMontantePagoGs(valorMontanteGS);
			sinistro.atribuirMoedaMontantePagoME(tipoMoeda);
			sinistro.atribuirMontantePagoME(valorMontanteME);
			
			if(!sinistros.containsKey(cContas.obterId()+"_"+plano.obterId()+"_"+tipoMoeda))
				sinistros.put(cContas.obterId()+"_"+plano.obterId()+"_"+tipoMoeda, sinistro);
		}
		
		return sinistros.values();
	}
	
	public Collection<SinistroFiniquitadoCentralRisco> obterSinistrosVigentesCentralRisco(Entidade aseguradora, String nomeAsegurado, String documento)  throws Exception
	{
		Map<String,SinistroFiniquitadoCentralRisco> sinistros = new TreeMap<String,SinistroFiniquitadoCentralRisco>();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		//SQLQuery query = this.getModelManager().createSQLQuery("crm", "select count(*) as qtde,MAX(criacao) as data, secao,plano,SUM(montante_gs) as montante_gs,tipo_moeda_montante_gs,SUM(montante_me) as montante_me,SUM(capital_gs) as capital_gs,tipo_moeda_capital_gs,SUM(capital_me) as capital_me from evento,apolice,sinistro where origem = ? and sinistro.situacao <> 'Pagado' and evento.id = sinistro.id and evento.superior = apolice.id and nome_asegurado = ? group by secao,plano,tipo_moeda_montante_gs,tipo_moeda_capital_gs");
		
		String sql = "select count(*) as qtde,MAX(criacao) as data, secao,plano,SUM(montante_gs) as montante_gs,tipo_moeda_montante_gs,SUM(montante_me) as montante_me,SUM(capital_gs) as capital_gs,tipo_moeda_capital_gs,SUM(capital_me) as capital_me from evento,apolice,sinistro where origem = "+aseguradora.obterId()+" and sinistro.situacao <> 'Pagado' and evento.id = sinistro.id and evento.superior = apolice.id";
		if(nomeAsegurado.length() > 0)
			sql+=" and nome_asegurado like ?";
		if(documento.length() > 0)
			sql+=" and RTrim(LTrim(numero_identificacao)) = '"+documento+"'";
		
		sql+=" group by secao,plano,tipo_moeda_montante_gs,tipo_moeda_capital_gs";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", sql);
		if(nomeAsegurado.length() > 0)
			query.addString("%"+nomeAsegurado+"%");
		
		SQLRow[] rows = query.execute();
		int qtde;
		long dataLong;
		Date data;
		ClassificacaoContas cContas;
		Plano plano;
		double valorMontanteGS,valorMontanteME,valorCapitalGS,valorCapitalME;
		String tipoMoeda,tipoMoedaCapital;
		SinistroFiniquitadoCentralRisco sinistro;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			qtde = rows[i].getInt("qtde");
			dataLong = rows[i].getLong("data");
			data = new Date(dataLong);
			cContas = (ClassificacaoContas) entidadeHome.obterEntidadePorId(rows[i].getLong("secao"));
			plano = (Plano) home.obterEventoPorId(rows[i].getLong("plano"));
			valorMontanteGS = rows[i].getDouble("montante_gs");
			tipoMoeda = rows[i].getString("tipo_moeda_montante_gs");
			valorMontanteME = rows[i].getDouble("montante_me");
			valorCapitalGS = rows[i].getDouble("capital_gs");
			tipoMoedaCapital = rows[i].getString("tipo_moeda_capital_gs");
			valorCapitalME = rows[i].getDouble("capital_me");
			
			sinistro = (SinistroFiniquitadoCentralRisco) this.getModelManager().getEntity("SinistroFiniquitadoCentralRisco");
			
			sinistro.atribuirQtdeSinistros(qtde);
			sinistro.atribuirSecao(cContas);
			sinistro.atribuirDataCorte(data);
			sinistro.atribuirPlano(plano);
			sinistro.atribuirMontantePagoGs(valorMontanteGS);
			sinistro.atribuirMoedaMontantePagoME(tipoMoeda);
			sinistro.atribuirMontantePagoME(valorMontanteME);
			sinistro.atribuirCapitalGs(valorCapitalGS);
			sinistro.atribuirMoedaCapitalME(tipoMoedaCapital);
			sinistro.atribuirCapitalME(valorCapitalME);
			
			sinistros.put(cContas.obterId()+"_"+plano.obterId()+"_"+tipoMoeda+"_"+tipoMoedaCapital, sinistro);
		}
		
		sql = "select count(*) as qtde,MAX(criacao) as data, secao,plano,SUM(montante_gs) as montante_gs,tipo_moeda_montante_gs,SUM(montante_me) as montante_me,SUM(capital_gs) as capital_gs,tipo_moeda_capital_gs,SUM(capital_me) as capital_me from evento,apolice,sinistro,apolice_asegurados where evento.id = sinistro.id and evento.superior = apolice.id and apolice.id = apolice_asegurados.id and origem = "+aseguradora.obterId()+" and sinistro.situacao <> 'Pagado'";
		if(nomeAsegurado.length() > 0)
			sql+=" and (nome + ' ' + sobre_nome like ?)";
		if(documento.length() > 0)
			sql+=" and numero_documento = '"+documento+"'";
		
		sql+=" group by secao,plano,tipo_moeda_montante_gs,tipo_moeda_capital_gs";
		
		query = this.getModelManager().createSQLQuery("crm", sql);
		if(nomeAsegurado.length() > 0)
			query.addString("%"+nomeAsegurado+"%");
		 
		rows = query.execute();
		 
		for(int i = 0 ; i < rows.length ; i++)
		{
			qtde = rows[i].getInt("qtde");
			dataLong = rows[i].getLong("data");
			data = new Date(dataLong);
			cContas = (ClassificacaoContas) entidadeHome.obterEntidadePorId(rows[i].getLong("secao"));
			plano = (Plano) home.obterEventoPorId(rows[i].getLong("plano"));
			valorMontanteGS = rows[i].getDouble("montante_gs");
			tipoMoeda = rows[i].getString("tipo_moeda_montante_gs");
			valorMontanteME = rows[i].getDouble("montante_me");
			valorCapitalGS = rows[i].getDouble("capital_gs");
			tipoMoedaCapital = rows[i].getString("tipo_moeda_capital_gs");
			valorCapitalME = rows[i].getDouble("capital_me");
				
			sinistro = (SinistroFiniquitadoCentralRisco) this.getModelManager().getEntity("SinistroFiniquitadoCentralRisco");
				
			sinistro.atribuirQtdeSinistros(qtde);
			sinistro.atribuirSecao(cContas);
			sinistro.atribuirDataCorte(data);
			sinistro.atribuirPlano(plano);
			sinistro.atribuirMontantePagoGs(valorMontanteGS);
			sinistro.atribuirMoedaMontantePagoME(tipoMoeda);
			sinistro.atribuirMontantePagoME(valorMontanteME);
			sinistro.atribuirCapitalGs(valorCapitalGS);
			sinistro.atribuirMoedaCapitalME(tipoMoedaCapital);
			sinistro.atribuirCapitalME(valorCapitalME);
			
			if(!sinistros.containsKey(cContas.obterId()+"_"+plano.obterId()+"_"+tipoMoeda+"_"+tipoMoedaCapital))
				sinistros.put(cContas.obterId()+"_"+plano.obterId()+"_"+tipoMoeda+"_"+tipoMoedaCapital, sinistro);
		}
		return sinistros.values();
	}
	
	public Collection<MorosidadeCentralRisco> obterMorosidadesCentralRisco(Entidade aseguradora, String nomeAsegurado, String documento)  throws Exception
	{
		Map<String,MorosidadeCentralRisco> morosidades = new TreeMap<>();
		
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		String sql = "select count(*) as qtde,MAX(criacao) as data, secao,SUM(dias_atraso) as dias,SUM(valor_gs) as deudas_gs,tipo_moeda_valor_gs,SUM(valor_me) as deudas_me from evento,apolice,morosidade where origem = "+aseguradora.obterId()+" and evento.id = morosidade.id and evento.superior = apolice.id";
		if(nomeAsegurado.length() > 0)
			sql+=" and nome_asegurado like ?";
		if(documento.length() > 0)
			sql+=" and RTrim(LTrim(numero_identificacao)) = '"+documento+"'";
		
		sql+=" group by secao,tipo_moeda_valor_gs";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", sql);
		if(nomeAsegurado.length() > 0)
			query.addString("%"+nomeAsegurado+"%");
		
		SQLRow[] rows = query.execute();
		int qtde,dias;
		long dataLong;
		Date data;
		ClassificacaoContas cContas;
		double valorDeudasGS,valorDeudasME;
		String tipoMoeda;
		MorosidadeCentralRisco morosidade;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			qtde = rows[i].getInt("qtde");
			dias = rows[i].getInt("dias");
			dataLong = rows[i].getLong("data");
			data = new Date(dataLong);
			cContas = (ClassificacaoContas) entidadeHome.obterEntidadePorId(rows[i].getLong("secao"));
			valorDeudasGS = rows[i].getDouble("deudas_gs");
			tipoMoeda = rows[i].getString("tipo_moeda_valor_gs");
			valorDeudasME = rows[i].getDouble("deudas_me");
			
			morosidade = (MorosidadeCentralRisco) this.getModelManager().getEntity("MorosidadeCentralRisco");
			
			morosidade.atribuirCotasAtraso(qtde);
			morosidade.atribuirDataCorte(data);
			morosidade.atribuirSecao(cContas);
			morosidade.atribuirDeudasGs(valorDeudasGS);
			morosidade.atribuirMoedaDeudasMe(tipoMoeda);
			morosidade.atribuirDeudasMe(valorDeudasME);
			morosidade.atribuirDiasMora(dias);
			
			morosidades.put(cContas.obterId()+"_"+tipoMoeda, morosidade);
		}
		
		sql = "select count(*) as qtde,MAX(criacao) as data, secao,SUM(dias_atraso) as dias,SUM(valor_gs) as deudas_gs,tipo_moeda_valor_gs,SUM(valor_me) as deudas_me from evento,apolice,morosidade,apolice_asegurados where origem = "+aseguradora.obterId()+" and evento.id = morosidade.id and evento.superior = apolice.id and apolice.id = apolice_asegurados.id";
		if(nomeAsegurado.length() > 0)
			sql+=" and (nome like ? or sobre_nome like ?)";
		if(documento.length() > 0)
			sql+=" and numero_documento = '"+documento+"'";
		
		sql+=" group by secao,tipo_moeda_valor_gs";
		
		query = this.getModelManager().createSQLQuery("crm", sql);
		if(nomeAsegurado.length() > 0)
		{
			query.addString("%"+nomeAsegurado+"%");
			query.addString("%"+nomeAsegurado+"%");
		}
		
		rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			qtde = rows[i].getInt("qtde");
			dias = rows[i].getInt("dias");
			dataLong = rows[i].getLong("data");
			data = new Date(dataLong);
			cContas = (ClassificacaoContas) entidadeHome.obterEntidadePorId(rows[i].getLong("secao"));
			valorDeudasGS = rows[i].getDouble("deudas_gs");
			tipoMoeda = rows[i].getString("tipo_moeda_valor_gs");
			valorDeudasME = rows[i].getDouble("deudas_me");
			
			morosidade = (MorosidadeCentralRisco) this.getModelManager().getEntity("MorosidadeCentralRisco");
			
			morosidade.atribuirCotasAtraso(qtde);
			morosidade.atribuirDataCorte(data);
			morosidade.atribuirSecao(cContas);
			morosidade.atribuirDeudasGs(valorDeudasGS);
			morosidade.atribuirMoedaDeudasMe(tipoMoeda);
			morosidade.atribuirDeudasMe(valorDeudasME);
			morosidade.atribuirDiasMora(dias);
			
			if(!morosidades.containsKey(cContas.obterId()+"_"+tipoMoeda))
				morosidades.put(cContas.obterId()+"_"+tipoMoeda, morosidade);
		}
		
		return morosidades.values();
	}
	
	public Collection obterContas() throws Exception
	{
		Map contas = new TreeMap();
		
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select entidade.id from entidade,classificacao_contas where entidade.id = classificacao_contas.id");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			if(id!=7)
			{
				ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorId(id);
				
				contas.put(cContas.obterApelido(), cContas);
			}
		}
		
		SQLQuery query2 = this.getModelManager().createSQLQuery("crm", "select entidade.id from entidade,conta where entidade.id = conta.id");
		
		SQLRow[] rows2 = query2.execute();
		
		for(int i = 0 ; i < rows2.length ; i++)
		{
			long id = rows2[i].getLong("id");
			
			Entidade e = home.obterEntidadePorId(id);
			
			if(e instanceof Conta)
				contas.put(e.obterApelido(), e);
		}
		
		return contas.values();
	}
	
	public Collection<Apolice> obterApolicesLavagemDinheiroPorPrima(Aseguradora aseguradora,String tipoPessoa,Date dataInicio,Date dataFim,int maxRegistros, String situacao, String secao, double monto, String modalidade,String tipoInstrumento) throws Exception
	{
		Collection<Apolice> apolices = new ArrayList<Apolice>();
		
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "";
		
		if(maxRegistros > 0)
			//sql = "select TOP "+ maxRegistros + " MAX(prima_gs)as prima,evento.id from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and prima_gs > 0";
			sql = "select TOP "+ maxRegistros + " MAX(prima_gs)as prima,evento.id from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
		else
			//sql = "select MAX(prima_gs)as prima,evento.id from evento,apolice where evento.id = apolice.id and prima_gs > 0";
			sql = "select MAX(prima_gs)as prima,evento.id from evento,apolice where evento.id = apolice.id";
		
		if(aseguradora!=null)
			sql+=" and origem = " + aseguradora.obterId();
		
		if(!situacao.equals("0"))
			sql+=" and situacao_seguro='"+situacao+"'";
		
		if(!tipoPessoa.equals("0"))
			sql+=" and tipo_pessoa = '" + tipoPessoa + "'";
		
		if(!secao.equals(""))
			sql+=" and plano.secao = '" + secao + "'";
		
		if(!modalidade.equals(""))
			sql+=" and plano.plano = '" + modalidade + "'";
		if(monto > 0)
			sql+=" and prima_gs>=" + monto;
					
		if(!tipoInstrumento.equals("0"))
			sql+=" and status_apolice='"+tipoInstrumento+"'";
		
		sql+= " and data_emissao>= " + dataInicio.getTime() + " and data_emissao<= " + dataFim.getTime() + " GROUP BY evento.id";
		sql+= " ORDER BY prima DESC";
		
		//sql+=" and situacao_seguro='"+situacao+"' and tipo_pessoa = '" + tipoPessoa + "' and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime() + " GROUP BY evento.id ORDER BY prima DESC";
		
		System.out.println("SQL: " + sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		long id;
		Apolice apolice;
		Evento e;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			
			e = eventoHome.obterEventoPorId(id);
			if(e instanceof Apolice)
			{
				apolice = (Apolice) e;
				apolices.add(apolice);
			}
				
		}
		return apolices;
	}
	
	public Collection<Apolice> obterApolicesLavagemDinheiroPorSinistro(Aseguradora aseguradora,String tipoPessoa,Date dataInicio,Date dataFim,int maxRegistros, String situacao, String secao, double monto, String modalidade,String tipoInstrumento) throws Exception
	{
		Collection<Apolice> apolices = new ArrayList<Apolice>();
		
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "";
		
		if(maxRegistros > 0)
			//sql = "select TOP "+ maxRegistros + " SUM(sinistro.montante_gs) AS montante, evento.superior from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id";
			sql = "select TOP "+ maxRegistros + " max(sum_montante) AS montante, superior from (select SUM(sinistro.montante_gs)AS sum_montante,evento.superior as superior from sinistro, evento,apolice,plano where evento.id = sinistro.id and superior=apolice.id and apolice.plano = plano.id";
		else
			sql = "select SUM(sinistro.montante_gs) AS montante, evento.superior from evento,sinistro,apolice where evento.id = sinistro.id and superior=apolice.id";
			//sql = "select TOP 15 max(sum_montante) AS montante, superior from (select SUM(sinistro.montante_gs)AS sum_montante,evento.superior as superior from sinistro, evento,apolice where evento.id = sinistro.id and superior=apolice.id";
		
		if(aseguradora!=null)
			sql+=" and origem = " + aseguradora.obterId();
			
		if(!situacao.equals("0"))
			sql+=" and situacao_seguro='"+situacao+"'";
		
		if(!secao.equals(""))
			sql+=" and plano.secao = '" + secao + "'";
		
		if(!modalidade.equals(""))
			sql+=" and plano.plano = '" + modalidade + "'";
		
		if(!tipoPessoa.equals("0"))
			sql+=" and tipo_pessoa = '" + tipoPessoa + "'";
		
		if(!tipoInstrumento.equals("0"))
			sql+=" and status_apolice='"+tipoInstrumento+"'";
		
		//sql+=" and data_sinistro >= " + dataInicio.getTime() + " and data_sinistro <= " + dataFim.getTime() + " GROUP BY evento.superior";
		sql+=" and data_emissao>= " + dataInicio.getTime() + " and data_emissao<= " + dataFim.getTime() + " GROUP BY evento.superior";
		
		if(monto>0)
			sql+=" having SUM(sinistro.montante_gs)>=" + monto;
		
		sql+=" ) as tab GROUP BY superior";
		
		sql+=" ORDER BY montante DESC";
		
		//System.out.println("SQL: " + sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		long id;
		Apolice apolice;
		Evento e;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("superior");
			
			e = eventoHome.obterEventoPorId(id);
			if(e instanceof Apolice)
			{
				apolice = (Apolice) e;
				
				apolices.add(apolice);
			}
		}
		
		return apolices;
		
	}
	
	public Collection<Apolice> obterApolicesLavagemDinheiroPorCapital(Aseguradora aseguradora,String tipoPessoa,Date dataInicio,Date dataFim,int maxRegistros, String situacao, String secao, double monto, String modalidade,String tipoInstrumento) throws Exception
	{
		Collection<Apolice> apolices = new ArrayList<Apolice>();
		
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "";
		
		if(maxRegistros > 0)
			//sql = "select TOP "+ maxRegistros + " MAX(capital_gs)as capital,evento.id from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and capital_gs > 0";
			sql = "select TOP "+ maxRegistros + " MAX(capital_gs)as capital,evento.id from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
		else
			//sql = "select MAX(capital_gs)as capital,evento.id from evento,apolice where evento.id = apolice.id and capital_gs > 0";
			sql = "select MAX(capital_gs)as capital,evento.id from evento,apolice where evento.id = apolice.id";
		
		if(aseguradora!=null)
			sql+=" and origem = " + aseguradora.obterId();
		
		if(!situacao.equals("0"))
			sql+=" and situacao_seguro='"+situacao+"'";
		
		if(!secao.equals(""))
			sql+=" and plano.secao = '" + secao + "'";
		
		if(!modalidade.equals(""))
			sql+=" and plano.plano = '" + modalidade + "'";
		
		if(!tipoPessoa.equals("0"))
			sql+=" and tipo_pessoa = '" + tipoPessoa + "'";
		
		if(monto>0)
			sql+=" and capital_gs>=" + monto;
		
		if(!tipoInstrumento.equals("0"))
			sql+=" and status_apolice='"+tipoInstrumento+"'";
		
		sql+=" and data_emissao>= " + dataInicio.getTime() + " and data_emissao<= " + dataFim.getTime() + " GROUP BY evento.id";
		
		sql+=" ORDER BY capital DESC";
		
		//System.out.println("SQL: " + sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		long id;
		Apolice apolice;
		Evento e;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			
			e = eventoHome.obterEventoPorId(id);
			if(e instanceof Apolice)
			{
				apolice = (Apolice) e;
				
				apolices.add(apolice);
			}
				
		}
		return apolices;
	}
	
	public Collection<String> obterQtdeApolicesPorPeriodoTODAS(Date dataInicio, Date dataFim, boolean valores, boolean admin) throws Exception
	{
		Collection<String> dados = new ArrayList<String>();
		
		String sql = "";
		if(valores)
			sql = "SELECT plano.secao, count(*) as qtde,SUM(prima_gs) as primaGs, SUM(prima_me) as primaMe FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and data_emissao>=? and data_emissao<=? GROUP BY plano.secao";
		else
			sql = "SELECT plano.secao, count(*) as qtde FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and data_emissao>=? and data_emissao<=? GROUP BY plano.secao";
		
		if(!admin)
			sql+=" having Len(Ltrim(Rtrim(plano.secao))) > 0";
		
		sql+=" order by plano.secao";
		
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
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
		
		if(admin)
		{
			// PARA TESTE DO PLANO RG 001 DEPOIS APAGAR 
			if(valores)
				sql = "SELECT count(*) as qtde,SUM(prima_gs) as primaGs, SUM(prima_me) as primaMe FROM evento,apolice where evento.id = apolice.id and apolice.plano = 0 and data_emissao>=? and data_emissao<=?";
			else
				sql = "SELECT count(*) as qtde FROM evento,apolice where evento.id = apolice.id and apolice.plano = 0 and data_emissao>=? and data_emissao<=?";
			
			query = this.getModelManager().createSQLQuery("crm",sql);
			query.addLong(dataInicio.getTime());
			query.addLong(dataFim.getTime());
			
			rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				String plano = "Sección no definida";
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
		}
		
		return dados;
	}
	
	public String[] obterQtdeApolicesPorPeriodoTODASNOVO(Date dataInicio, Date dataFim, boolean valores, String secao, String modalidade, String ramo) throws Exception
	{
		String[] dados = new String[1];
		
		String sql = "";
		if(valores)
		{
			if(secao.equals("Sección no definida"))
				sql = "SELECT SUM(prima_gs) as primaGs, SUM(prima_me) as primaMe FROM evento,apolice where evento.id = apolice.id and apolice.plano = 0 and data_emissao>="+dataInicio.getTime()+" and data_emissao<=" + dataFim.getTime();
			else
			{
				sql = "SELECT SUM(prima_gs) as primaGs, SUM(prima_me) as primaMe FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" and plano.secao = '"+secao+"'";
				
				if(ramo!=null)
					sql+=" and ramo = '"+ramo+"'";
				if(!modalidade.equals(""))
					sql+=" and plano.plano = '"+modalidade+"'";
			}
		}
		else
		{
			if(secao.equals("Sección no definida"))
				sql = "SELECT count(*) as qtde FROM evento,apolice where evento.id = apolice.id and apolice.plano = 0 and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime();
			else
			{
				sql = "SELECT count(*) as qtde FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" and plano.secao='"+secao+"'";
				
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
		
		// PARA TESTE DO PLANO RG 001 DEPOIS APAGAR 
		/*if(valores)
			sql = "SELECT count(*) as qtde,SUM(prima_gs) as primaGs, SUM(prima_me) as primaMe FROM evento,apolice where evento.id = apolice.id and apolice.plano = 0 and data_emissao>=? and data_emissao<=?";
		else
			sql = "SELECT count(*) as qtde FROM evento,apolice where evento.id = apolice.id and apolice.plano = 0 and data_emissao>=? and data_emissao<=?";
		
		query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			String plano = "Sección no definida";
			int qtde = rows[i].getInt("qtde");
			if(valores)
			{
				double primaGs = rows[i].getDouble("primaGs");
				double primaMe = rows[i].getDouble("primaMe");
				
				dados.add(plano + ";" + qtde + ";" + primaGs + ";" + primaMe);
			}
			else
				dados.add(plano + ";" + qtde + ";" + 0 + ";" + 0);
		}*/
		
		return dados;
	}
	
	public Map<String,String> obterNomePlanoPeriodoTODAS(Date dataInicio, Date dataFim, String ramo, String secao) throws Exception
	{
		Map<String,String> dados = new TreeMap<String,String>();
		
		String sql = "";
		if(secao.equals(""))
		{
			sql = "SELECT plano.secao FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and data_emissao>=? and data_emissao<=?";
			if(ramo!=null)
				sql+=" and ramo = '"+ ramo + "'";
			
			sql+=" GROUP BY plano.secao";
			
			sql+=" having Len(Rtrim(Ltrim(plano.secao))) > 0";
			sql+=" order by plano.secao";
		}
		else
		{
			sql = "SELECT plano.plano FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and data_emissao>=? and data_emissao<=?";
			if(ramo!=null)
				sql+=" and ramo = '"+ ramo + "' and plano.secao = '"+secao+"'";
			
			sql+=" GROUP BY plano.plano order by plano.plano";
		}
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			if(secao.equals(""))
			{
				String plano2 = rows[i].getString("secao").trim();
				if(plano2.length() > 0)
					dados.put(plano2+";-",plano2+";-");
			}
			else
			{
				String mod = rows[i].getString("plano").trim();
				if(mod.length() > 0)
					dados.put(secao+";"+mod,secao+";"+mod);
			}
		}
		
		//dados.add("Sección no definida;-");
		
		return dados;
	}
	
	public Collection<String> obterQtdeSinistrosPorPeriodoTODAS(Date dataInicio, Date dataFim, boolean admin) throws Exception
	{
		Collection<String> dados = new ArrayList<String>();
		
		String sql = "SELECT plano.secao,count(*) as qtde FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>= ? and data_sinistro<=? GROUP BY plano.secao";
		
		if(!admin)
			sql+=" having Len(Ltrim(Rtrim(plano.secao))) > 0";
		
		sql+=" order by plano.secao";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			String plano = rows[i].getString("secao");
			int qtde = rows[i].getInt("qtde");
			
			dados.add(plano + ";" + qtde);
		}
		
		if(admin)
		{
			// PARA TESTE DO PLANO RG 001 DEPOIS APAGAR 
			sql = "SELECT count(*) as qtde FROM evento,sinistro,apolice where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = 0 and data_sinistro>= ? and data_sinistro<=?";
			
			query = this.getModelManager().createSQLQuery("crm",sql);
			query.addLong(dataInicio.getTime());
			query.addLong(dataFim.getTime());
			
			rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				String plano = "Sección no definida";
				int qtde = rows[i].getInt("qtde");
			
				dados.add(plano + ";" + qtde);
			}
		}
		
		return dados;
	}
	
	public String[] obterQtdeSinistrosPorPeriodoTODASNOVO(Date dataInicio, Date dataFim, boolean valores, String secao, String modalidade, String ramo) throws Exception
	{
		String[] dados = new String[1];
		
		String sql = "";
		if(valores)
		{
			if(secao.equals("Sección no definida"))
				sql = "SELECT SUM(fatura_sinistro.montante_pago) as montanteGs, SUM(fatura_sinistro.montante_me) as montanteMe FROM evento,sinistro,fatura_sinistro where evento.id = fatura_sinistro.id and superior = sinistro.id and sinistro.data_sinistro>= "+dataInicio.getTime()+" and sinistro.data_sinistro<= "+dataFim.getTime()+" and secao_apolice is null";
			else
			{
				sql = "SELECT SUM(fatura_sinistro.montante_pago) as montanteGs, SUM(fatura_sinistro.montante_me) as montanteMe FROM evento,sinistro,fatura_sinistro where evento.id = fatura_sinistro.id and superior = sinistro.id and sinistro.data_sinistro>= "+dataInicio.getTime()+" and sinistro.data_sinistro<= "+dataFim.getTime()+" and secao_apolice = '"+secao+"'";
				if(!modalidade.equals(""))
					sql+=" and plano_modalidade = '"+modalidade+"'";
			}
		}
		else
		{
			if(secao.equals("Sección no definida"))
				sql = "SELECT count(*) as qtde FROM evento,sinistro,apolice where evento.id = sinistro.id and superior = apolice.id and sinistro.data_sinistro>= "+dataInicio.getTime()+" and sinistro.data_sinistro<= "+dataFim.getTime()+" and apolice.plano = 0";
			else
			{
				sql = "SELECT count(*) as qtde FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and superior = apolice.id and apolice.plano = plano.id and sinistro.data_sinistro>= "+dataInicio.getTime()+" and sinistro.data_sinistro<= "+dataFim.getTime()+" and plano.secao = '"+secao+"'";
				
				if(ramo!=null)
					sql+=" and ramo ='"+ramo+"'";
				
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
			
				dados[i] = 0 + "; " + montanteGs + ";"+ montanteMe;
			}
			else
			{
				int qtde = rows[i].getInt("qtde");
				dados[i] = qtde + "; " + 0 + ";"+ 0;
			}
		}
		
		// PARA TESTE DO PLANO RG 001 DEPOIS APAGAR 
		/*sql = "SELECT count(*) as qtde FROM evento,sinistro,apolice where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = 0 and data_sinistro>= ? and data_sinistro<=?";
		
		query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			String plano = "Sección no definida";
			int qtde = rows[i].getInt("qtde");
		
			dados.add(plano + ";" + qtde);
		}*/
		
		return dados;
	}
	
	public Map<String,String> obterNomePlanosSinistrosPorPeriodoTODAS(Date dataInicio, Date dataFim, String ramo, String secao) throws Exception
	{
		Map<String,String> dados = new TreeMap<String,String>();
		String sql = "";
		
		if(secao.equals(""))
		{
			sql = "SELECT plano.secao FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>=? and data_sinistro<=?";
			if(ramo!=null)
				sql+=" and ramo = '"+ramo+"'";
			
			sql+=" GROUP BY plano.secao";
			sql+=" having Len(Rtrim(Ltrim(plano.secao)))> 0 ";
			sql+=" order by plano.secao";
		}
		else
		{
			sql = "SELECT plano.plano FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>=? and data_sinistro<=?";
			if(ramo!=null)
				sql+=" and ramo = '"+ramo+"' and plano.secao = '"+secao+"'";
			
			sql+=" GROUP BY plano.plano order by plano.plano";
		}
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			if(secao.equals(""))
			{
				String plano2 = rows[i].getString("secao").trim();
				if(plano2.length() > 0)
					dados.put(plano2+";-",plano2+";-");
			}
			else
			{
				String mod = rows[i].getString("plano").trim();
				if(mod.length() > 0)
					dados.put(secao+";"+mod,secao+";"+mod);
			}
		}
		
		//dados.add("Sección no definida;-");
		
		return dados;
	}
	
	public String obterValoresSinistrosPorPeriodoRelSecaoAnualTODAS(Date dataInicio, Date dataFim, String secao) throws Exception
	{
		String dados = "";
		
		String sql = "SELECT evento.id FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>= ? and data_sinistro<=? and plano.secao = ?";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
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
		
		//dados+=totalGs + ";" + totalMe;
		
		return dados;
	}
	
	public String[] obterQtdeApolicesPorModalidade(Date dataInicio, Date dataFim, String secao, String modalidade) throws Exception
	{
		String[] dados = new String[1];
		
		String sql = "";
		sql = "SELECT count(*) as qtde, SUM(capital_gs) as capital, SUM(prima_gs) as prima FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" and plano.secao='"+secao+"'";
		if(!modalidade.equals(""))
			sql+=" and plano.plano = '"+modalidade+"'";
			
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			int qtde = rows[i].getInt("qtde");
			double capital = rows[i].getDouble("capital");
			double prima = rows[i].getDouble("prima");
			
			sql = "SELECT SUM(caiptal_gs) as capital FROM evento,dados_reaseguro,apolice,plano where evento.id = dados_reaseguro.id and superior = apolice.id and apolice.plano = plano.id and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" and plano.secao = '"+secao+"'";
			if(!modalidade.equals(""))
				sql+=" and plano.plano = '"+modalidade+"'";
			
			query = this.getModelManager().createSQLQuery("crm",sql);
			double reaseguro = query.executeAndGetFirstRow().getDouble("capital");
			
			dados[i] = qtde + ";" + capital + ";" + prima+";"+reaseguro;
		}
		
		return dados;
	}
	
	public Collection<Entidade> obterAgentesPorPeridodo(Aseguradora aseguradora, Date dataInicio, Date dataFim)throws Exception 
	{
		Map<String,Entidade> agentes = new TreeMap<String,Entidade>();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		String sql = "select agente from evento,apolice where evento.id = apolice.id and data_emissao>=? and data_emissao<=? and agente > 0";
		if(aseguradora!=null)
			sql+=" and origem = " + aseguradora.obterId();
		
		sql+=" group by agente";

		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		SQLRow[] rows = query.execute();
		long id;
		Entidade agente;
		
		for (int i = 0; i < rows.length; i++) 
		{
			id = rows[i].getLong("agente");

			agente = home.obterEntidadePorId(id);

			agentes.put(agente.obterNome() + i, agente);
		}
		return agentes.values();
	}
	
	public Collection<Entidade> obterCorredoresPorPeridodo(Aseguradora aseguradora, Date dataInicio, Date dataFim)throws Exception 
	{
		Map<String, Entidade> agentes = new TreeMap<String, Entidade>();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

		String sql = "select corredor from evento,apolice where evento.id = apolice.id and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" and corredor > 0";
		if(aseguradora!=null)
			sql+=" and origem = " + aseguradora.obterId();
		
		sql+=" group by corredor";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		//System.out.println(sql);

		SQLRow[] rows = query.execute();
		long id;
		Entidade agente;
		
		for (int i = 0; i < rows.length; i++) 
		{
			id = rows[i].getLong("corredor");

			agente = home.obterEntidadePorId(id);

			agentes.put(agente.obterNome() + i, agente);
		}
		return agentes.values();
	}
	
	public Collection<Entidade> obterGrupoAlertaTrempana(Aseguradora aseguradora) throws Exception
	{
		Collection<Entidade> grupo = new ArrayList<Entidade>();
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select id from grupo_alerta_trempana where grupo = ?");
		query.addString(aseguradora.obterGrupoAlertaTrempana());
		
		SQLRow[] rows = query.execute();
		Entidade e;
		long id;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			
			e = home.obterEntidadePorId(id);
			
			grupo.add(e);
		}
		
		return grupo;
	}
	
	public Collection<Entidade> obterAseguradorasComGrupoAlertaTrempana() throws Exception
	{
		Collection<Entidade> aseguradoras = new ArrayList<Entidade>();
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select id from grupo_alerta_trempana where LEN(grupo) > 0 and grupo is not null");
		
		SQLRow[] rows = query.execute();
		Entidade e;
		long id;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			
			e = home.obterEntidadePorId(id);
			
			aseguradoras.add(e);
		}
		
		return aseguradoras;
	}
}