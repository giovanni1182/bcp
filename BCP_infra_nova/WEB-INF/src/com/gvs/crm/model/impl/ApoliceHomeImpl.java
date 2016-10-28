package com.gvs.crm.model.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.ApoliceDuplicada;
import com.gvs.crm.model.ApoliceHome;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AspectosLegais;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Plano;
import com.gvs.crm.model.Sinistro;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class ApoliceHomeImpl extends Home implements ApoliceHome {
	public Apolice obterApolice(String numero) throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,apolice where evento.id = apolice.id and numero_apolice=? and origem = ?");
		query.addString(numero);

		long id = query.executeAndGetFirstRow().getLong("id");
		Apolice apolice = null;

		if (id > 0) {
			EventoHome home = (EventoHome) this.getModelManager().getHome(
					"EventoHome");

			apolice = (Apolice) home.obterEventoPorId(id);
		}

		return apolice;
	}

	public Collection<Apolice> localizarApolices(String numeroInstrumento, String secao, Aseguradora aseguradora, String nomeAsegurado, String codigoPlano, int pagina,
			String situacao, Date dataInicio, Date dataFim, String rucCi, String tomador, String tipoInstrumento, String nomeAsegurado1, String nomeAsegurado2) throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		Collection<Apolice> apolices = new ArrayList<Apolice>();

		
			String sql = "";
			String sql2 = "";
			
			if(!codigoPlano.equals("") || !secao.equals(""))
			{
				sql = "select evento.id from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
				sql2 = "select evento.id,data_prevista_inicio from evento,apolice,plano,apolice_asegurados,apolice_tomadores where evento.id = apolice.id and apolice.plano = plano.id and apolice_asegurados.id = apolice.id and apolice_tomadores.id = apolice.id";
			}
			else
			{
				sql = "select evento.id from evento,apolice where evento.id = apolice.id";
				sql2 = "select evento.id,data_prevista_inicio from evento,apolice,apolice_asegurados,apolice_tomadores where evento.id = apolice.id and apolice_asegurados.id = apolice.id and apolice_tomadores.id = apolice.id";
			}
	
			if(numeroInstrumento != null && !numeroInstrumento.equals(""))
			{
				if(numeroInstrumento.length() !=10)
				{
					//sql += " and SUBSTRING(numero_apolice,8,3) = '"+numeroInstrumento+"'";
					//sql += " and SUBSTRING(numero_apolice,"+(10 - numeroInstrumento.length()+1)+",10) = '"+numeroInstrumento+"'";
					sql += " and numero_apolice like '%"+numeroInstrumento+"%'";
					sql2+=" and numero_apolice like '%"+numeroInstrumento+"%'";
				}
				else
				{
					sql += " and numero_apolice = '"+numeroInstrumento+"'";
					sql2+= " and numero_apolice = '"+numeroInstrumento+"'";
				}
			}
			
			if(!secao.equals(""))
			{
				sql += " and plano.secao = '"+secao + "'";
				sql2+=" and plano.secao = '"+secao + "'";
			}
			if (aseguradora != null)
			{
				sql += " and origem = "+aseguradora.obterId();
				sql2+=" and origem = "+aseguradora.obterId();
			}
			if (nomeAsegurado != null && !nomeAsegurado.equals(""))
				sql += " and nome_asegurado like '%" + nomeAsegurado +"%'";
			//if(!nomeAsegurado1.equals(""))
				//sql += " and CHARINDEX('"+nomeAsegurado1+"',nome_asegurado)>0 and CHARINDEX('"+nomeAsegurado2+"',nome_asegurado)>0";
			if (tomador != null && !tomador.equals(""))
				sql += " and nome_tomador like '%" + tomador +"%'";
			if (!codigoPlano.equals(""))
			{
				sql += " and plano.plano = '"+codigoPlano+"'";
				sql2+=" and plano.plano = '"+codigoPlano+"'";
			}
			if(rucCi!=null && !rucCi.equals(""))
				sql += " and LTrim(RTrim(numero_identificacao)) = '"+rucCi+"'";
			if(!situacao.equals("0"))
			{
				sql += " and situacao_seguro = '"+situacao+"'";
				sql2+=" and situacao_seguro = '"+situacao+"'";
			}
			if(dataInicio!=null && dataFim!=null)
			{
				sql += " and data_emissao>= "+dataInicio.getTime()+" and data_emissao<= "+ dataFim.getTime();
				sql2+=" and data_emissao>= "+dataInicio.getTime()+" and data_emissao<= "+ dataFim.getTime();
			}
			if(!tipoInstrumento.equals("0"))
			{
				sql+=" and status_apolice = '"+tipoInstrumento+"'";
				sql2+=" and status_apolice = '"+tipoInstrumento+"'";
			}
	
			sql += " order by evento.data_prevista_inicio,evento.data_prevista_conclusao,nome_asegurado,criacao";
			
			//System.out.println(sql);
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm", sql);
	
			SQLRow[] rows = query.execute();
			
			Evento e;
			long id;
			
			for (int i = 0; i < rows.length; i++)
			{
				id = rows[i].getLong("id");
	
				e = home.obterEventoPorId(id);
				if(e instanceof Apolice)
					apolices.add((Apolice)e);
			}
			
			if(!nomeAsegurado.equals("") || !tomador.equals("") || !rucCi.equals(""))
			{
				if(!nomeAsegurado.equals(""))
					sql2+=" and (apolice_asegurados.nome + ' ' + apolice_asegurados.sobre_nome like '%"+nomeAsegurado+"%')";
				if(!tomador.equals(""))
					sql2+=" and (apolice_tomadores.nome + ' ' + apolice_tomadores.sobre_nome like '%"+tomador+"%')";
			
				if(!rucCi.equals(""))
					sql2 += " and (apolice_asegurados.numero_documento = '"+rucCi+"' or apolice_tomadores.numero_documento = '"+rucCi+"')";
			
				sql2+=" group by evento.id,data_prevista_inicio order by data_prevista_inicio";
				
				//System.out.println(sql2);
				
				query = this.getModelManager().createSQLQuery("crm", sql2);
				
				rows = query.execute();
				
				for (int i = 0; i < rows.length; i++)
				{
					id = rows[i].getLong("id");
		
					e = home.obterEventoPorId(id);
					if(e instanceof Apolice)
					{
						if(!apolices.contains(e))
							apolices.add((Apolice)e);
					}
				}
			}
			
		return apolices;
	}
	
	public Collection localizarApolices(String numeroInstrumento, ClassificacaoContas cContas, Aseguradora aseguradora, String nomeAsegurado, String codigoPlano) throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		Collection apolices = new ArrayList();

		String sql = "select evento.id from evento,apolice,plano where evento.id = apolice.id";

		if (numeroInstrumento != null && !numeroInstrumento.equals(""))
			sql += " and numero_apolice = ?";
		if (cContas != null)
			sql += " and apolice.secao = ?";
		if (aseguradora != null)
			sql += " and origem = ?";
		if (nomeAsegurado != null && !nomeAsegurado.equals(""))
			sql += " and nome_asegurado like ?";
		if (codigoPlano != null && !codigoPlano.equals(""))
			sql += " and (apolice.plano = plano.id and identificador = ?)";

		sql += " group by evento.id";

		//SQLQuery query = this.getModelManager().createSQLQuery("crm", "select
		// evento.id from evento,apolice,plano where evento.id = apolice.id and
		// (numero_apolice = ? or apolice.secao = ? or origem = ? or
		// nome_asegurado like ? or (apolice.plano = plano.id and identificador
		// = ?)) group by evento.id");
		//query.addString(numeroInstrumento);
		SQLQuery query = this.getModelManager().createSQLQuery("crm", sql);

		if (numeroInstrumento != null && !numeroInstrumento.equals(""))
			query.addString(numeroInstrumento);
		if (cContas != null)
			query.addLong(cContas.obterId());
		if (aseguradora != null)
			query.addLong(aseguradora.obterId());
		if (nomeAsegurado != null && !nomeAsegurado.equals(""))
			query.addString("%" + nomeAsegurado + "%");
		if (codigoPlano != null && !codigoPlano.equals(""))
			query.addString(codigoPlano);
		
		SQLRow[] rows = query.execute();

		//System.out.println("select evento.id from evento,apolice where
		// evento.id = apolice.id and (numero_apolice = "+numeroInstrumento+" or
		// secao = ? or origem = ? or nome_asegurado like ? or plano = ?) group
		// by evento.id");

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");

			Apolice apolice = (Apolice) home.obterEventoPorId(id);

			apolices.add(apolice);
		}

		return apolices;
	}

	public Collection obterApolicesSuspeitas1() throws Exception 
	{
		Map apolices = new TreeMap();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,apolice where evento.id = apolice.id and tipo_moeda_prima_gs=? and prima_me>10000 and situacao_seguro = 'Vigente'");
		query.addString("Dólar USA");

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) 
		{
			long id = rows[i].getLong("id");

			Apolice apolice = (Apolice) home.obterEventoPorId(id);

			apolices.put(apolice.obterNomeAsegurado() + apolice.obterId(),apolice);
		}

		return apolices.values();
	}

	public Collection obterApolicesSuspeitas3() throws Exception
	{
		Map apolices = new TreeMap();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		SQLQuery query2 = this.getModelManager().createSQLQuery("crm","select tipo_indentificacao, numero_identificacao, SUM(prima_me) as soma from evento,apolice where evento.id = apolice.id and tipo_moeda_prima_gs=? and situacao_seguro = ? GROUP BY tipo_indentificacao, numero_identificacao HAVING SUM(prima_me)>10000");
		//SQLQuery query2 = this.getModelManager().createSQLQuery("crm","select tipo_indentificacao, numero_identificacao, SUM(capital_me) as soma from evento,apolice where evento.id = apolice.id and tipo_moeda_prima_gs=? and situacao_seguro = ? GROUP BY tipo_indentificacao, numero_identificacao HAVING SUM(capital_me)>10000");
		query2.addString("Dólar USA");
		query2.addString("No Vigente");

		SQLRow[] rows2 = query2.execute();

		for (int i = 0; i < rows2.length; i++) 
		{
			String numero = rows2[i].getString("numero_identificacao");

			Apolice apolice = (Apolice) this.obterApolicePorNumeroIdentificacao(numero);

			if (!apolices.containsValue(apolice))
				apolices.put(apolice.obterNomeAsegurado() + apolice.obterId(),apolice);
		}
		return apolices.values();
	}

	public Collection obterApolicesSuspeitas2() throws Exception 
	{
		Map apolices = new TreeMap();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		SQLQuery query3 = this.getModelManager().createSQLQuery("crm","select evento.id from evento,sinistro where evento.id = sinistro.id and tipo_moeda_montante_gs=? and montante_me>10000");
		query3.addString("Dólar USA");

		SQLRow[] rows3 = query3.execute();

		for (int i = 0; i < rows3.length; i++) 
		{
			long id = rows3[i].getLong("id");

			Sinistro sinistro = (Sinistro) home.obterEventoPorId(id);

			Apolice apolice = (Apolice) sinistro.obterSuperior();

			apolices.put(apolice.obterNomeAsegurado() + apolice.obterId(),apolice);
		}

		return apolices.values();
	}

	public Apolice obterApolicePorNumeroIdentificacao(String numero)
			throws Exception {
		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,apolice where evento.id = apolice.id and numero_identificacao=?");
		query.addString(numero);

		Apolice apolice = null;

		long id = query.executeAndGetFirstRow().getLong("id");

		if (id > 0)
			apolice = (Apolice) home.obterEventoPorId(id);

		return apolice;
	}

	public Collection obterSecaoApolice(Aseguradora aseguradora) throws Exception 
	{
		Map secoes = new TreeMap();

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select secao from evento,apolice where origem = ? and evento.id = apolice.id group by secao");
		query.addLong(aseguradora.obterId());

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
		{
			long id = rows[i].getLong("secao");

			if (id > 0)
			{
				Entidade secao = home.obterEntidadePorId(id);

				secoes.put(secao.obterApelido(), secao.obterApelido());
			}
		}

		return secoes.values();
	}

	public Collection obterPlanosApolice(Aseguradora aseguradora)
			throws Exception {
		Map planos = new TreeMap();

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select plano from evento,apolice where origem = ? and evento.id = apolice.id group by plano");
		query.addLong(aseguradora.obterId());

		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getInt("plano");

			if (id > 0) {
				Plano plano = (Plano) home.obterEventoPorId(id);

				planos.put(new Long(plano.obterId()), plano);
			}

		}

		return planos.values();
	}

	public Collection obterTiposApolice(Aseguradora aseguradora)
			throws Exception {
		Map tipos = new TreeMap();

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select tipo from evento,apolice where origem = ? and evento.id = apolice.id group by tipo");
		query.addLong(aseguradora.obterId());

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			String tipo = rows[i].getString("tipo");

			tipos.put(tipo, tipo);

		}

		return tipos.values();
	}
	
	private Collection obterApolices(Entidade aseguradora, String numero, ClassificacaoContas secao, String status, Plano plano, double endoso, double certificado)throws Exception
	{
		Collection apolices = new ArrayList();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select evento.id from evento,apolice where evento.id  = apolice.id and origem = ? and numero_apolice = ? and secao = ? and status_apolice = ? and plano = ? and numero_endoso = ? and certificado = ?");
		query.addLong(aseguradora.obterId());
		query.addString(numero);
		query.addLong(secao.obterId());
		query.addString(status);
		if(plano!=null)
			query.addLong(plano.obterId());
		else
			query.addLong(0);
		query.addDouble(endoso);
		query.addDouble(certificado);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Apolice apolice = (Apolice) home.obterEventoPorId(id);
			
			apolices.add(apolice);
		}
		
		return apolices;
	}
	
	public Collection obterApolicesDuplicadasTeste()throws Exception 
	{
		Map apolices = new TreeMap();
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","SELECT evento.origem, apolice.numero_apolice,secao,status_apolice,plano,numero_endoso,certificado" +
				" FROM entidade CROSS JOIN evento INNER JOIN apolice ON evento.id = apolice.id AND evento.origem = entidade.id where numero_apolice='0105000017'");

		
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		EventoHome eventohome = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLRow[] rows = query.execute();
		
		for (int i = 0; i < rows.length; i++) 
		{
			//int qtde = rows[i].getInt("qtde");
			long origemLong = rows[i].getLong("origem");
			String numero = rows[i].getString("numero_apolice");
			long secaoLong = rows[i].getLong("secao");
			String status = rows[i].getString("status_apolice");
			long planoLong = rows[i].getLong("plano");
			double endoso = rows[i].getDouble("numero_endoso");
			double certificado = rows[i].getDouble("certificado");
			
			ApoliceDuplicada apolice = (ApoliceDuplicada) this.getModelManager().getEntity("ApoliceDuplicada");
			
			apolice.atribuirQtde(0);
			apolice.atribuirOrigem(home.obterEntidadePorId(origemLong));
			apolice.atribuirNumero(numero);
			apolice.atribuirSecao((ClassificacaoContas)home.obterEntidadePorId(secaoLong));
			apolice.atribuirStatus(status);
			apolice.atribuirPlano((Plano)eventohome.obterEventoPorId(planoLong));
			apolice.atribuirEndoso(endoso);
			apolice.atribuirCertificado(certificado);
			
			apolices.put(new Integer(i), apolice);
		
		}
		
		return apolices.values();
	}
	
	public void excluirApolicesDuplicadas() throws Exception
	{
		
		for(Iterator i = this.obterApolicesDuplicadas().iterator() ; i.hasNext() ; )
		{
			ApoliceDuplicada apoliceDuplicada = (ApoliceDuplicada) i.next();
			
			Collection apolices = this.obterApolices(apoliceDuplicada.obterOrigem(), apoliceDuplicada.obterNumero(), apoliceDuplicada.obterSecao(), apoliceDuplicada.obterStatus(), apoliceDuplicada.obterPlano(), apoliceDuplicada.obterEndoso(), apoliceDuplicada.obterCertificado());
			
			Date data = null;
			
			Apolice ultimaApolice = null;
			
			for(Iterator j = apolices.iterator() ; j.hasNext() ; )
			{
				Apolice apolice = (Apolice) j.next();
				
				if(data == null)
				{
					data = apolice.obterCriacao();
					ultimaApolice = apolice;
				}
				else
				{
					if(apolice.obterCriacao().after(data))
					{
						data = apolice.obterCriacao();
						ultimaApolice = apolice;
					}
				}
			}
			
			for(Iterator j = apolices.iterator() ; j.hasNext() ; )
			{
				Apolice apolice = (Apolice) j.next();
				
				System.out.println("Apolice: " + apolice.obterNumeroApolice());
				
				if(apolice.obterId()!=ultimaApolice.obterId())
				{
					for(Iterator k = apolice.obterInferiores().iterator() ; k.hasNext() ; )
					{
						Evento e = (Evento) k.next();
						
						e.atualizarSuperior(ultimaApolice);
					}
					
					apolice.excluir();
				}
			}
		}
		
		//this.obterApolicesDuplicadas().clear();
	}
	
	public Collection obterApolicesDuplicadas()throws Exception 
	{
		Map apolices = new TreeMap();
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm"," SELECT COUNT(*) AS qtde, evento.origem, apolice.numero_apolice,secao,status_apolice,plano,numero_endoso,certificado" +
							" FROM entidade CROSS JOIN evento INNER JOIN apolice ON evento.id = apolice.id AND evento.origem = entidade.id" +
							" GROUP BY evento.origem, apolice.numero_apolice,secao,status_apolice,plano,numero_endoso,certificado HAVING (COUNT(*) > 1)");

		
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		EventoHome eventohome = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLRow[] rows = query.execute();
		
		for (int i = 0; i < rows.length; i++) 
		{
			int qtde = rows[i].getInt("qtde");
			long origemLong = rows[i].getLong("origem");
			String numero = rows[i].getString("numero_apolice");
			long secaoLong = rows[i].getLong("secao");
			String status = rows[i].getString("status_apolice");
			long planoLong = rows[i].getLong("plano");
			double endoso = rows[i].getDouble("numero_endoso");
			double certificado = rows[i].getDouble("certificado");
			
			ApoliceDuplicada apolice = (ApoliceDuplicada) this.getModelManager().getEntity("ApoliceDuplicada");
			
			apolice.atribuirQtde(qtde);
			apolice.atribuirOrigem(home.obterEntidadePorId(origemLong));
			apolice.atribuirNumero(numero);
			apolice.atribuirSecao((ClassificacaoContas)home.obterEntidadePorId(secaoLong));
			apolice.atribuirStatus(status);
			if(planoLong>0)
				apolice.atribuirPlano((Plano)eventohome.obterEventoPorId(planoLong));
			else
				apolice.atribuirPlano(null);
			apolice.atribuirEndoso(endoso);
			apolice.atribuirCertificado(certificado);
			
			apolices.put(new Integer(i), apolice);
		
		}
		
		return apolices.values();
	}

	public Collection obterSituacoesApolice(Aseguradora aseguradora)
			throws Exception {
		Map situacoes = new TreeMap();

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select situacao_seguro from evento,apolice where origem = ? and evento.id = apolice.id group by situacao_seguro");
		query.addLong(aseguradora.obterId());

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			String situacao = rows[i].getString("situacao_seguro");

			situacoes.put(situacao, situacao);

		}

		return situacoes.values();
	}

	public Collection obterApolicesPorReaseguradora(Aseguradora aseguradora,
			Entidade reaseguradora, Date dataInicio, Date dataFim,
			String tipoContrato) throws Exception {
		Map apolices = new TreeMap();

		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select superior from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and reaseguradora = ? and tipo_contrato = ? and data_prevista_inicio >= ? and data_prevista_conclusao <= ?");
		query.addLong(aseguradora.obterId());
		query.addLong(reaseguradora.obterId());
		query.addString(tipoContrato);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		//System.out.println("select superior from evento, dados_reaseguro
		// where evento.id = dados_reaseguro.id and origem =
		// "+aseguradora.obterId()+" and reaseguradora =
		// "+reaseguradora.obterId()+" and tipo_contrato = "+tipoContrato+" and
		// data_prevista_inicio >= "+dataInicio.getTime()+" and
		// data_prevista_conclusao <= " + dataFim.getTime());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("superior");

			if (id > 0) {
				Apolice apolice = (Apolice) home.obterEventoPorId(id);

				apolices.put(new Long(apolice.obterDataPrevistaInicio()
						.getTime()
						+ i), apolice);
			}
		}

		return apolices.values();
	}

	public Collection obterApolicesPorCorretora(Aseguradora aseguradora,Entidade corretora, Date dataInicio, Date dataFim,String tipoContrato) throws Exception 
	{
		Map apolices = new TreeMap();

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select superior from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and corretora = ? and tipo_contrato = ? and data_prevista_inicio >= ? and data_prevista_inicio <= ?");
		query.addLong(aseguradora.obterId());
		query.addLong(corretora.obterId());
		query.addString(tipoContrato);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		//System.out.println("select superior from evento, dados_reaseguro
		// where evento.id = dados_reaseguro.id and origem =
		// "+aseguradora.obterId()+" and reaseguradora =
		// "+reaseguradora.obterId()+" and tipo_contrato = "+tipoContrato+" and
		// data_prevista_inicio >= "+dataInicio.getTime()+" and
		// data_prevista_conclusao <= " + dataFim.getTime());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("superior");

			if (id > 0) {
				Apolice apolice = (Apolice) home.obterEventoPorId(id);

				apolices.put(new Long(apolice.obterDataPrevistaInicio()
						.getTime()
						+ i), apolice);
			}
		}

		return apolices.values();
	}
	
	public Collection obterApolicesPorCorretoraAseguradora(Aseguradora aseguradora,Entidade corretora, Date dataInicio, Date dataFim,String tipoContrato) throws Exception 
	{
		Map apolices = new TreeMap();

		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select superior from evento, dados_reaseguro where evento.id = dados_reaseguro.id and origem = ? and reaseguradora = ? and tipo_contrato = ? and data_prevista_inicio >= ? and data_prevista_inicio <= ?");
		query.addLong(aseguradora.obterId());
		query.addLong(corretora.obterId());
		query.addString(tipoContrato);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		//System.out.println("select superior from evento, dados_reaseguro
		// where evento.id = dados_reaseguro.id and origem =
		// "+aseguradora.obterId()+" and reaseguradora =
		// "+reaseguradora.obterId()+" and tipo_contrato = "+tipoContrato+" and
		// data_prevista_inicio >= "+dataInicio.getTime()+" and
		// data_prevista_conclusao <= " + dataFim.getTime());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("superior");

			if (id > 0) {
				Apolice apolice = (Apolice) home.obterEventoPorId(id);

				apolices.put(new Long(apolice.obterDataPrevistaInicio()
						.getTime()
						+ i), apolice);
			}
		}

		return apolices.values();
	}
	
	
	public Collection obterNomesAseguradoEmBranco() throws Exception
	{
		Map apolices = new TreeMap();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,apolice where evento.id = apolice.id and (LEN(LTRIM(Rtrim(nome_asegurado)))=0 or nome_asegurado is null) order by origem");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Apolice apolice = (Apolice) home.obterEventoPorId(id);
			
			apolices.put(apolice.obterNomeAsegurado() + i,apolice);
		}
		
		return apolices.values();
	}
	
	public void atualizarSituacaoApoliceAnterior() throws Exception
	{
		Map apolices = new TreeMap();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select apolice_anterior from evento,apolice where evento.id = apolice.id and apolice_anterior > 0");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("apolice_anterior");
			
			SQLQuery query2 = this.getModelManager().createSQLQuery("crm","select id from evento where id = ?");
			query2.addLong(id);
			
			if(query2.execute().length > 0)
			{
				Apolice apolice = (Apolice) home.obterEventoPorId(id);
			
				if(apolice.obterSituacaoSeguro().equals("Vigente"))
					apolice.atualizarSituacaoSeguro("No Vigente");
			}
		}
	}
	
	public void atualizarNoVigenteApolicesVenciadas() throws Exception
	{
		Map apolices = new TreeMap();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,apolice where evento.id = apolice.id and situacao_seguro='Vigente' and data_prevista_conclusao < ?");
		query.addLong(new Date().getTime());
		//334491

		System.out.println("select evento.id from evento,apolice where evento.id = apolice.id and situacao_seguro='Vigente' and data_prevista_conclusao < " + new Date().getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Apolice apolice = (Apolice) home.obterEventoPorId(id);
			
			apolice.atualizarSituacaoSeguro("No Vigente");
		}
	}

	public Collection obterApolicesSuspeitas(String rucCi) throws Exception
	{
		Collection apolices = new ArrayList();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,apolice where evento.id = apolice.id and RTrim(numero_identificacao) = ? order by criacao ASC");
		query.addString(rucCi);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Apolice apolice = (Apolice) home.obterEventoPorId(id);
			
			apolices.add(apolice);
		}
		
		return apolices;
	}
	
	public Apolice obterApolice(Entidade aseguradora, String numero) throws Exception
	{
	    SQLQuery query = getModelManager().createSQLQuery("crm", "select top 1 evento.id from evento,apolice where evento.id = apolice.id and numero_apolice=? and origem = ?");
	    query.addString(numero);
	    query.addLong(aseguradora.obterId());
	    long id = query.executeAndGetFirstRow().getLong("id");
	    Apolice apolice = null;
	    if(id > 0)
	    {
	        EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
	        apolice = (Apolice)home.obterEventoPorId(id);
	    }
	    
	    return apolice;
	}
	
	public Collection obterConsolidado(String opcao, String situacaoSeguro, Date dataInicio, Date dataFim) throws Exception
	{
		Collection colecao = new ArrayList();
		
		if(opcao.equals("Pólizas"))
		{
			String sql = "";
			
			sql = "select plano.secao, COUNT(*) as qtde,SUM(capital_gs) as capitalGS,SUM(capital_me) as capitalME,SUM(prima_gs) as primaGS,SUM(prima_me) as primaME from apolice,plano where apolice.plano = plano.id and data_emissao>=? and data_emissao<= ? and situacao_seguro = ?  GROUP BY plano.secao order by plano.secao asc";
			//			sql = "select plano.secao, COUNT(*) as qtde,SUM(capital_gs) as capitalGS,SUM(capital_me) as capitalME,SUM(prima_gs) as primaGS,SUM(prima_me) as primaME from [bd_bcp].[dbo].[apolice],[bd_bcp].[dbo].[plano] where apolice.plano = plano.id and data_emissao>=? and data_emissao<= ? and situacao_seguro = 'Anulada' GROUP BY plano.secao order by plano.secao asc";
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
			query.addLong(dataInicio.getTime());
			query.addLong(dataFim.getTime());
			query.addString(situacaoSeguro);
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				String secao = rows[i].getString("secao");
				int qtde = rows[i].getInt("qtde");
				double capitalGS = rows[i].getDouble("capitalGS");
				double capitalME = rows[i].getDouble("capitalME");
				double primaGS = rows[i].getDouble("primaGS");
				double primaME = rows[i].getDouble("primaME");
				
				//System.out.println(capitalME);
				if(qtde>0)
					colecao.add(secao + ";" + qtde + ";" + capitalGS + ";" + capitalME + ";" + primaGS + ";" + primaME);
			}
		}
		else
		{
			//String sql = "select plano.secao, COUNT(*) as qtde,SUM(capital_gs) as capitalGS,SUM(capital_me) as capitalME,SUM(prima_gs) as primaGS,SUM(premio_me) as primaME from [bd_bcp].[dbo].[evento],[bd_bcp].[dbo].[apolice],[bd_bcp].[dbo].[plano],[bd_bcp].[dbo].[sinistro] where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>=? and data_sinistro<= ? GROUp by plano.secao order by plano.secao asc";
			SQLQuery query2 = null;
			String sql = "";
			String sql2 = "";
			
			if(!situacaoSeguro.equals("Aulada"))
			{
				sql = "select plano.secao, COUNT(*) as qtde,SUM(capital_gs) as capitalGS,SUM(prima_gs) as primaGS, SUM(montante_gs) as montanteGS from [bd_bcp].[dbo].[evento],[bd_bcp].[dbo].[apolice],[bd_bcp].[dbo].[plano],[bd_bcp].[dbo].[sinistro] where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>=? and data_sinistro<= ? and situacao_seguro = ? GROUp by plano.secao order by plano.secao asc";
				sql2 = "select SUM(montante_gs) as montanteGS from [bd_bcp].[dbo].[evento],[bd_bcp].[dbo].[apolice],[bd_bcp].[dbo].[plano],[bd_bcp].[dbo].[sinistro] where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>=? and data_sinistro<= ? and sinistro.situacao = ? and plano.secao = ? and situacao_seguro = ?";
				
				SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
				query.addLong(dataInicio.getTime());
				query.addLong(dataFim.getTime());
				query.addString(situacaoSeguro);
				
				SQLRow[] rows = query.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					String secao = rows[i].getString("secao");
					int qtde = rows[i].getInt("qtde");
					double capitalGS = rows[i].getDouble("capitalGS");
					double primaGS = rows[i].getDouble("primaGS");
					double montanteGS = rows[i].getDouble("montanteGS");
					
					query2 = this.getModelManager().createSQLQuery("crm",sql2);
					
					query2.addLong(dataInicio.getTime());
					query2.addLong(dataFim.getTime());
					query2.addString("Judicializado");
					query2.addString(secao);
					query2.addString(situacaoSeguro);
					
					double juizGS = query2.executeAndGetFirstRow().getDouble("montanteGS");
					
					if(qtde>0)
						colecao.add(secao + ";" + qtde + ";" + capitalGS + ";" + montanteGS + ";" + primaGS + ";" + juizGS);
				}
			}
			else
			{
				String sqlTotal = "select plano.secao, COUNT(*) as qtde,SUM(capital_gs) as capitalGS,SUM(prima_gs) as primaGS, SUM(montante_gs) as montanteGS from [bd_bcp].[dbo].[evento],[bd_bcp].[dbo].[apolice],[bd_bcp].[dbo].[plano],[bd_bcp].[dbo].[sinistro] where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>=? and data_sinistro<= ? GROUp by plano.secao order by plano.secao asc";
				String sqlTotal2 = "select SUM(montante_gs) as montanteGS from [bd_bcp].[dbo].[evento],[bd_bcp].[dbo].[apolice],[bd_bcp].[dbo].[plano],[bd_bcp].[dbo].[sinistro] where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>=? and data_sinistro<= ? and sinistro.situacao = ? and plano.secao = ?";
				
				System.out.println("Total: select plano.secao, COUNT(*) as qtde,SUM(capital_gs) as capitalGS,SUM(prima_gs) as primaGS, SUM(montante_gs) as montanteGS from evento,apolice,plano,sinistro where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>="+dataInicio.getTime()+" and data_sinistro<= "+dataFim.getTime()+" GROUp by plano.secao order by plano.secao asc");
				
				String sql3 = "select plano.secao, COUNT(*) as qtde,SUM(capital_gs) as capitalGS,SUM(prima_gs) as primaGS, SUM(montante_gs) as montanteGS from [bd_bcp].[dbo].[evento],[bd_bcp].[dbo].[apolice],[bd_bcp].[dbo].[plano],[bd_bcp].[dbo].[sinistro] where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>=? and data_sinistro<= ? and situacao_seguro <> 'Anulada' GROUp by plano.secao order by plano.secao asc";
				
				System.out.println("Acumulado: select plano.secao, COUNT(*) as qtde,SUM(capital_gs) as capitalGS,SUM(prima_gs) as primaGS, SUM(montante_gs) as montanteGS from [bd_bcp].[dbo].[evento],[bd_bcp].[dbo].[apolice],[bd_bcp].[dbo].[plano],[bd_bcp].[dbo].[sinistro] where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>="+dataInicio.getTime()+" and data_sinistro<= "+dataFim.getTime()+" and situacao_seguro <> 'Anulada' GROUp by plano.secao order by plano.secao asc");
				
				String sql4 = "select SUM(montante_gs) as montanteGS from [bd_bcp].[dbo].[evento],[bd_bcp].[dbo].[apolice],[bd_bcp].[dbo].[plano],[bd_bcp].[dbo].[sinistro] where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>=? and data_sinistro<= ? and sinistro.situacao = ? and plano.secao = ? and situacao_seguro<> 'Anulada'";
				
				SQLQuery query = this.getModelManager().createSQLQuery("crm",sqlTotal);
				query.addLong(dataInicio.getTime());
				query.addLong(dataFim.getTime());
				
				SQLRow[] rows = query.execute();
				
				SQLQuery query3 = this.getModelManager().createSQLQuery("crm",sql3);
				query3.addLong(dataInicio.getTime());
				query3.addLong(dataFim.getTime());
				
				SQLRow[] rows2 = query3.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					String secaoTotal = rows[i].getString("secao");
					int qtdeTotal = rows[i].getInt("qtde");
					double capitalGSTotal = rows[i].getDouble("capitalGS");
					double primaGSTotal = rows[i].getDouble("primaGS");
					double montanteGSTotal = rows[i].getDouble("montanteGS");
					
					String secao = rows2[i].getString("secao");
					int qtde = rows2[i].getInt("qtde");
					double capitalGS = rows2[i].getDouble("capitalGS");
					double primaGS = rows2[i].getDouble("primaGS");
					double montanteGS = rows2[i].getDouble("montanteGS");
					
					qtdeTotal-=qtde;
					capitalGSTotal-=capitalGS;
					primaGSTotal-=primaGS;
					montanteGSTotal-=montanteGS;
					
					query2 = this.getModelManager().createSQLQuery("crm",sqlTotal2);
					query2.addLong(dataInicio.getTime());
					query2.addLong(dataFim.getTime());
					query2.addString("Judicializado");
					query2.addString(secaoTotal);
					
					double juizGS = query2.executeAndGetFirstRow().getDouble("montanteGS");
					
					if(juizGS > 0)
					{
						SQLQuery query4 = this.getModelManager().createSQLQuery("crm",sql4);
						query4.addLong(dataInicio.getTime());
						query4.addLong(dataFim.getTime());
						query4.addString("Judicializado");
						query4.addString(secaoTotal);
						
						SQLRow[] rows3 = query4.execute();
					
						juizGS-=rows3[i].getDouble("montanteGS");
					}
					
					if(qtdeTotal > 0)
						colecao.add(secao + ";" + qtdeTotal + ";" + capitalGSTotal + ";" + montanteGSTotal + ";" + primaGSTotal + ";" + juizGS);
				}
			}
		}
		
		return colecao;
	}
	
	public Collection<String> obterConsolidado(Aseguradora aseguradora, String situacaoSeguro, String opcao, Date dataInicio, Date dataFim, boolean admin) throws Exception
	{
		Collection<String> colecao = new ArrayList<String>();
		
		if(opcao.equals("Pólizas"))
		{
			String sql = "";
			
			sql = "select plano.secao, COUNT(*) as qtde,SUM(capital_gs) as capitalGS,SUM(capital_me) as capitalME,SUM(prima_gs) as primaGS,SUM(prima_me) as primaME, plano.plano from apolice,plano,evento where evento.id = apolice.id and apolice.plano = plano.id and data_emissao>="+dataInicio.getTime()+" and data_emissao<= "+dataFim.getTime();
			if(aseguradora!=null)
				sql+=" and origem = "+aseguradora.obterId();
			if(!situacaoSeguro.equals(""))
				sql+=" and situacao_seguro = '"+situacaoSeguro+"'";
			
			sql+=" GROUP BY plano.secao, plano.plano";
			
			if(!admin)
				sql+=" having Len(Rtrim(Ltrim(plano.secao))) > 0";
				
			sql+=" order by plano.secao";
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
			
			//System.out.println(sql);
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				String secao = rows[i].getString("secao");
				String modalidade = rows[i].getString("plano");
				int qtde = rows[i].getInt("qtde");
				double capitalGS = rows[i].getDouble("capitalGS");
				double capitalME = rows[i].getDouble("capitalME");
				double primaGS = rows[i].getDouble("primaGS");
				double primaME = rows[i].getDouble("primaME");
				
				//System.out.println(capitalME);
				if(qtde>0 && !secao.equals(""))
					colecao.add(secao + ";" + qtde + ";" + capitalGS + ";" + capitalME + ";" + primaGS + ";" + primaME + ";" + modalidade);
			}
			
			if(admin)
			{
				// PARA TESTES COM O PLANO RG 001 DEPOIS APAGAR
				sql = "select COUNT(*) as qtde,SUM(capital_gs) as capitalGS,SUM(capital_me) as capitalME,SUM(prima_gs) as primaGS,SUM(prima_me) as primaME from apolice,evento where evento.id = apolice.id and apolice.plano = 0 and data_emissao>="+dataInicio.getTime()+" and data_emissao<= "+dataFim.getTime();
				if(aseguradora!=null)
					sql+=" and origem = "+aseguradora.obterId();
				if(!situacaoSeguro.equals(""))
					sql+=" and situacao_seguro = '"+situacaoSeguro+"'";
			
				query = this.getModelManager().createSQLQuery("crm",sql);
				
				//System.out.println(sql);
				
				rows = query.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					String secao = "Sección no definida";
					int qtde = rows[i].getInt("qtde");
					double capitalGS = rows[i].getDouble("capitalGS");
					double capitalME = rows[i].getDouble("capitalME");
					double primaGS = rows[i].getDouble("primaGS");
					double primaME = rows[i].getDouble("primaME");
					
					//System.out.println(capitalME);
					if(qtde>0)
						colecao.add(secao + ";" + qtde + ";" + capitalGS + ";" + capitalME + ";" + primaGS + ";" + primaME + "; ");
				}
			}
		}
		else
		{
			String sql = "";
			String sql2 ="";
			String sql3 ="";
			String sql4 ="";
			SQLQuery query2 = null;
			
			//if(!situacaoSeguro.equals("Anulada"))
			//{
				sql = "select plano.secao, COUNT(*) as qtde,SUM(capital_gs) as capitalGS,SUM(prima_gs) as primaGS, SUM(montante_gs) as montanteGS, plano.plano from evento,apolice,plano,sinistro where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>="+dataInicio.getTime()+" and data_sinistro<= "+dataFim.getTime();
				if(aseguradora!=null)
					sql+=" and origem = " + aseguradora.obterId();
				if(!situacaoSeguro.equals(""))
					sql+=" and situacao_seguro = '"+situacaoSeguro+"'";
				
				sql+=" GROUp by plano.secao, plano.plano";
				
				if(!admin)
					sql+=" having Len(Rtrim(Ltrim(plano.secao))) > 0";
					
				sql+=" order by plano.secao";
				
				SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
				
				SQLRow[] rows = query.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					String secao = rows[i].getString("secao");
					String modalidade = rows[i].getString("plano");
					int qtde = rows[i].getInt("qtde");
					double capitalGS = rows[i].getDouble("capitalGS");
					double primaGS = rows[i].getDouble("primaGS");
					double montanteGS = rows[i].getDouble("montanteGS");
					
					sql2 = "select SUM(montante_gs) as montanteGS from evento,apolice,plano,sinistro where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>="+dataInicio.getTime()+" and data_sinistro<= "+dataFim.getTime()+" and sinistro.situacao = 'Judicializado' and plano.secao = '"+secao+"'";
					if(aseguradora!=null)
						sql2+=" and origem = " + aseguradora.obterId();
					if(!situacaoSeguro.equals(""))
						sql2+=" and situacao_seguro = '"+situacaoSeguro+"'";
					
					query2 = this.getModelManager().createSQLQuery("crm",sql2);
					double juizGS = query2.executeAndGetFirstRow().getDouble("montanteGS");
					
					if(qtde>0 && !secao.equals(""))
						colecao.add(secao + ";" + qtde + ";" + capitalGS + ";" + montanteGS + ";" + primaGS + ";" + juizGS + ";" + modalidade);
				}
			/*}
			else
			{
				String sqlTotal = "select plano.secao, COUNT(*) as qtde,SUM(capital_gs) as capitalGS,SUM(prima_gs) as primaGS, SUM(montante_gs) as montanteGS, plano.plano from evento,apolice,plano,sinistro where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>="+dataInicio.getTime()+" and data_sinistro<= "+dataFim.getTime();
				if(aseguradora!=null)
					sqlTotal+=" and evento.origem = " + aseguradora.obterId();
				
				sqlTotal+=" GROUp by plano.secao,plano.plano order by plano.secao asc";
				
				SQLQuery query = this.getModelManager().createSQLQuery("crm",sqlTotal);
				
				SQLRow[] rows = query.execute();
				
				sql3 = "select plano.secao, COUNT(*) as qtde,SUM(capital_gs) as capitalGS,SUM(prima_gs) as primaGS, SUM(montante_gs) as montanteGS, plano.plano from evento,apolice,plano,sinistro where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>="+dataInicio.getTime()+" and data_sinistro<= "+dataFim.getTime()+" and situacao_seguro <> 'Anulada' GROUp by plano.secao,plano.plano order by plano.secao";
				if(aseguradora!=null)
					sql3+=" and origem = " + aseguradora.obterId();
				
				SQLQuery query3 = this.getModelManager().createSQLQuery("crm",sql3);
				
				SQLRow[] rows2 = query3.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					String secaoTotal = rows[i].getString("secao");
					String modalidadeTotal = rows[i].getString("plano");
					int qtdeTotal = rows[i].getInt("qtde");
					double capitalGSTotal = rows[i].getDouble("capitalGS");
					double primaGSTotal = rows[i].getDouble("primaGS");
					double montanteGSTotal = rows[i].getDouble("montanteGS");
					
					//System.out.println(secaoTotal + " " +qtdeTotal);
					
					String secao = rows2[i].getString("secao");
					String modalidade = rows2[i].getString("plano");
					int qtde = rows2[i].getInt("qtde");
					double capitalGS = rows2[i].getDouble("capitalGS");
					double primaGS = rows2[i].getDouble("primaGS");
					double montanteGS = rows2[i].getDouble("montanteGS");
					
					qtdeTotal-=qtde;
					capitalGSTotal-=capitalGS;
					primaGSTotal-=primaGS;
					montanteGSTotal-=montanteGS;
					
					String sqlTotal2 = "select SUM(montante_gs) as montanteGS from evento,apolice,plano,sinistro where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>="+dataInicio.getTime()+" and data_sinistro<= "+dataFim.getTime()+" and sinistro.situacao = 'Judicializado' and plano.secao = '"+secaoTotal+"' and plano.plano='" + modalidadeTotal + "'";
					if(aseguradora!=null)
						sqlTotal2+=" and origem = " + aseguradora.obterId();
					
					query2 = this.getModelManager().createSQLQuery("crm",sqlTotal2);
					
					double juizGS = query2.executeAndGetFirstRow().getDouble("montanteGS");
					
					if(juizGS > 0)
					{
						sql4 = "select SUM(montante_gs) as montanteGS from evento,apolice,plano,sinistro where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>="+dataInicio.getTime()+" and data_sinistro<= "+dataFim.getTime()+" and sinistro.situacao = 'Judicializado' and plano.secao = '"+secaoTotal+"' and plano.plano = '" + modalidadeTotal + "' and situacao_seguro<> 'Anulada'";
						if(aseguradora!=null)
							sql4+=" and origem = " + aseguradora.obterId();
						
						SQLQuery query4 = this.getModelManager().createSQLQuery("crm",sql4);
						
						SQLRow[] rows3 = query4.execute();
					
						juizGS-=rows3[i].getDouble("montanteGS");
					}
					
					if(qtdeTotal > 0 && !secao.equals(""))
						colecao.add(secao + ";" + qtdeTotal + ";" + capitalGSTotal + ";" + montanteGSTotal + ";" + primaGSTotal + ";" + juizGS+ ";" + modalidade);
				}
			}*/
		}
		
		return colecao;
	}
	
	public Collection<String> obterSecoes(boolean admin) throws Exception
	{
		Collection<String> secoes = new ArrayList<String>();
		
		//String sql = "SELECT plano.secao FROM apolice,plano where apolice.plano = plano.id group by plano.secao";
		String sql = "SELECT plano.secao FROM plano group by plano.secao";
		
		if(!admin)
			sql+=" HAVING len(ltrim(rtrim(plano.secao))) > 0";
		
		sql+=" order by plano.secao";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			String secao = rows[i].getString("secao");
			
			secoes.add(secao);
		}
		
		if(admin)
			secoes.add("Sección no definida");
		
		return secoes;
	}
	
	public Map obterQtdeApolicesAnualPorSecao(Date dataInicio, Date dataFim, String situacaoSeguro, boolean userAdmin) throws Exception
	{
		Map informacoes = new TreeMap();
		
		//String sql = "SELECT count(*) as qtde FROM apolice,plano where apolice.plano = plano.id and data_emissao>= ? and data_emissao<=? and plano.secao = ? and situacao_seguro = ?";
		
		String sql = "SELECT plano.secao, count(*) as qtde FROM apolice,plano where apolice.plano = plano.id and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime();
		if(!situacaoSeguro.equals(""))
			sql+=" and situacao_seguro ='"+situacaoSeguro+"'";
		
		sql+=" GROUP BY plano.secao";
		
		sql+=" order by plano.secao";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			String secao = rows[i].getString("secao");
			int qtde = rows[i].getInt("qtde");
			
			informacoes.put(secao,qtde);
		}
		
		if(userAdmin)
		{
			sql = "SELECT count(*) as qtde FROM apolice where apolice.plano = 0 and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime();
			if(!situacaoSeguro.equals(""))
				sql+=" and situacao_seguro ='"+situacaoSeguro+"'";
			
			//System.out.println(sql);
			
			query = this.getModelManager().createSQLQuery("crm",sql);
			
			rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				String secao = "Sección no definida";
				int qtde = rows[i].getInt("qtde");
				
				informacoes.put(secao,qtde);
			}
		}
		
		
		return informacoes;
	}
	
	public int obterQtdeApolicesAnualPorSecao(Date dataInicio, Date dataFim, String secao, String situacaoSeguro) throws Exception
	{
		String sql = "SELECT count(*) as qtde FROM apolice,plano where apolice.plano = plano.id and data_emissao>= ? and data_emissao<=? and plano.secao = ? and situacao_seguro = ?";
		
		//System.out.println("SELECT count(*) as qtde FROM apolice,plano where apolice.plano = plano.id and data_emissao>= "+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" and plano.secao = "+secao+" and situacao_seguro = " + situacaoSeguro);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		query.addString(secao);
		query.addString(situacaoSeguro);
		
		return query.executeAndGetFirstRow().getInt("qtde");
	}
	
	public int obterQtdeSinistrosAnualPorSecao(Date dataInicio, Date dataFim, String secao, String situacaoSeguro) throws Exception
	{
		int qtde = 0;
		
		if(!situacaoSeguro.equals("Anulada"))
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","SELECT count(*) as qtde FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>= ? and data_sinistro<=? and plano.secao = ? and situacao_seguro = ?");
			query.addLong(dataInicio.getTime());
			query.addLong(dataFim.getTime());
			query.addString(secao);
			query.addString(situacaoSeguro);
			
			qtde = query.executeAndGetFirstRow().getInt("qtde");
		}
		else
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","SELECT count(*) as qtde FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>= ? and data_sinistro<=? and plano.secao = ?");
			query.addLong(dataInicio.getTime());
			query.addLong(dataFim.getTime());
			query.addString(secao);
			
			qtde = query.executeAndGetFirstRow().getInt("qtde");
			
			SQLQuery query2 = this.getModelManager().createSQLQuery("crm","SELECT count(*) as qtde FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>= ? and data_sinistro<=? and plano.secao = ? and situacao_seguro <> 'Anulada'");
			query2.addLong(dataInicio.getTime());
			query2.addLong(dataFim.getTime());
			query2.addString(secao);
			
			qtde -= query2.executeAndGetFirstRow().getInt("qtde");
		}
		
		return qtde;
		
		
	}
	
	public Map obterQtdeSinistrosAnualPorSecao(Date dataInicio, Date dataFim, String situacaoSeguro, boolean admin) throws Exception
	{
		Map informacoes = new TreeMap();
		
		//if(!situacaoSeguro.equals("Anulada"))
		//{
			String sql = "SELECT plano.secao, count(*) as qtde FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>="+dataInicio.getTime()+" and data_sinistro<="+dataFim.getTime();
			if(!situacaoSeguro.equals(""))
				sql+=" and situacao_seguro = '" + situacaoSeguro +"'";
						
			sql+=" GROUP BY plano.secao order by plano.secao asc";
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm", sql);
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				String secao = rows[i].getString("secao");
				int qtde = rows[i].getInt("qtde");
				
				//System.out.println(secao + " " + qtde);
				
				informacoes.put(secao,qtde);
			}
			/*}
		else
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","SELECT plano.secao,count(*) as qtde FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>= ? and data_sinistro<=? GROUP BY plano.secao order by plano.secao asc");
			query.addLong(dataInicio.getTime());
			query.addLong(dataFim.getTime());
			
			SQLRow[] rows = query.execute();
			
			SQLQuery query2 = this.getModelManager().createSQLQuery("crm","SELECT plano.secao, count(*) as qtde FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and data_sinistro>= ? and data_sinistro<=? and situacao_seguro <> 'Anulada' GROUP BY plano.secao order by plano.secao asc");
			query2.addLong(dataInicio.getTime());
			query2.addLong(dataFim.getTime());
			
			SQLRow[] rows2 = query2.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				String secao = rows[i].getString("secao");
				int qtde = rows[i].getInt("qtde");
				
				qtde-=rows2[i].getInt("qtde");
				
				if(qtde > 0)
					informacoes.put(secao,qtde);
			}
		}*/
		
		if(admin)
		{
			sql = "SELECT count(*) as qtde FROM evento,sinistro,apolice where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = 0 and data_sinistro>="+dataInicio.getTime()+" and data_sinistro<="+dataFim.getTime();
			if(!situacaoSeguro.equals(""))
				sql+=" and situacao_seguro = '" + situacaoSeguro +"'";
						
			query = this.getModelManager().createSQLQuery("crm", sql);
			
			rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				String secao = "Sección no definida";
				int qtde = rows[i].getInt("qtde");
				
				//System.out.println(secao + " " + qtde);
				
				informacoes.put(secao,qtde);
			}
		}
		
		return informacoes;
	}
	
	public void manutAnulacao() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select superior from evento,anulacao_instrumento,apolice where evento.id = anulacao_instrumento.id and superior = apolice.id");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("superior");
			
			SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update apolice set situacao_seguro = 'Anulada' where id = ?");
			update.addLong(id);
			
			update.execute();
		}
	}
	
	public Map obterQtdeApolicesPeriodo(Aseguradora aseg, Date dataInicio, Date dataFim, boolean admin) throws Exception
	{
		Map informacoes = new TreeMap();
		
		String sql = "SELECT plano.secao, count(*) as qtde FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and origem = ? and data_emissao>=? and data_emissao<=? GROUP BY plano.secao";
		
		if(!admin)
			sql+= " having Len(ltrim(rtrim(plano.secao))) > 0";
		
		sql+=" order by plano.secao";
		
		//System.out.println("SELECT plano.secao, count(*) as qtde FROM evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and origem = "+aseg.obterId()+" and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" GROUP BY plano.secao order by plano.secao asc");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(aseg.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			String secao = rows[i].getString("secao");
			int qtde = rows[i].getInt("qtde");
			
			informacoes.put(secao,secao + "_" + qtde);
		}
		
		if(admin)
		{
			// PARA TESTE DO PLANO RG 001 DEPOIS APAGAR 
			sql = "SELECT count(*) as qtde FROM evento,apolice where evento.id = apolice.id and apolice.plano = 0 and origem = ? and data_emissao>=? and data_emissao<=?";
			
			query = this.getModelManager().createSQLQuery("crm",sql);
			query.addLong(aseg.obterId());
			query.addLong(dataInicio.getTime());
			query.addLong(dataFim.getTime());
			
			rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				String secao = "Sección no definida";
				int qtde = rows[i].getInt("qtde");
				
				informacoes.put(secao,secao + "_" + qtde);
			}
		}
		
		return informacoes;
	}
	
	public Map obterQtdeSinistrosPeriodo(Aseguradora aseg, Date dataInicio, Date dataFim, boolean admin) throws Exception
	{
		Map informacoes = new TreeMap();
		String sql = "SELECT plano.secao, count(*) as qtde FROM evento,sinistro,apolice,plano where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = plano.id and origem = ? and data_sinistro>= ? and data_sinistro<=? GROUP BY plano.secao";
		
		if(!admin)
			sql+= " having Len(ltrim(rtrim(plano.secao))) > 0";
		
		sql+=" order by plano.secao";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		query.addLong(aseg.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			String secao = rows[i].getString("secao");
			int qtde = rows[i].getInt("qtde");
			
			informacoes.put(secao,secao + "_" + qtde);
		}
		if(admin)
		{
			// PARA TESTE DO PLANO RG 001 DEPOIS APAGAR 
			query = this.getModelManager().createSQLQuery("crm","SELECT count(*) as qtde FROM evento,sinistro,apolice where evento.id = sinistro.id and evento.superior = apolice.id and apolice.plano = 0 and origem = ? and data_sinistro>= ? and data_sinistro<=?");
			query.addLong(aseg.obterId());
			query.addLong(dataInicio.getTime());
			query.addLong(dataFim.getTime());
			
			rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				String secao = "Sección no definida";
				int qtde = rows[i].getInt("qtde");
				
				informacoes.put(secao,secao + "_" + qtde);
			}
		}
		
		return informacoes;
	}
	
	public Collection<String> obterDemandaJudicial(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception
	{
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		Map<String,String> aspectos = new TreeMap<String,String>();
		
		String sql = "select origem,secao,SUM(montante_demandado) as montante,count(*) as qtde from evento,aspectos_legais,apolice where evento.id = aspectos_legais.id and superior = apolice.id";
		if(aseguradora!=null)
			sql+=" and origem = " + aseguradora.obterId();
		
		sql+=" and data_prevista_inicio>=" + dataInicio.getTime() + " and data_prevista_conclusao<=" + dataFim.getTime();
		
		sql+=" group by origem,secao";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long origemId = rows[i].getLong("origem");
			long secaoId = rows[i].getLong("secao");
			double montante = rows[i].getDouble("montante");
			int qtde = rows[i].getInt("qtde");
			
			Entidade origem = home.obterEntidadePorId(origemId);
			Entidade secao = home.obterEntidadePorId(secaoId);
			String linha = origem.obterId() + ";" + secao.obterId() + ";" + montante + ";" + qtde;
			
			//System.out.println(linha);
			
			aspectos.put(origem.obterNome() + secao.obterNome(), linha);
		}
		
		return aspectos.values();
	}
	
	public Collection<AspectosLegais> obterApolicesDemandaJudicial(Aseguradora aseguradora, ClassificacaoContas secao, Date dataInicio, Date dataFim) throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		Collection<AspectosLegais> aspectos = new ArrayList<AspectosLegais>();
		
		String sql = "select evento.id from evento,aspectos_legais,apolice where evento.id = aspectos_legais.id and superior = apolice.id";
		
		sql+=" and origem = " + aseguradora.obterId();
		sql+=" and secao = " + secao.obterId();
		sql+=" and data_prevista_inicio>=" + dataInicio.getTime() + " and data_prevista_conclusao<=" + dataFim.getTime();
		sql+=" group by evento.id";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			AspectosLegais aspecto = (AspectosLegais) home.obterEventoPorId(id);
			
			aspectos.add(aspecto);
		}
		
		return aspectos;
	}
	
	public String obterQtdeMontanteDemandaJudicial(Aseguradora aseguradora, Date dataInicio) throws Exception
	{
		String mesAno = new SimpleDateFormat("yyyyMM").format(dataInicio);
		
		int qtde = 0;
		double montante = 0;
		
		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio));
		int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio));
		
		if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Instrumento"))
		{
			String nomeArquivo = "A"+aseguradora.obterSigla()+mesAno + ".txt";
			
			File file = new File("C:/Aseguradoras/Archivos/"+nomeArquivo);
			
			//System.out.println(nomeArquivo);
			
			if(file.exists())
			{
				FileReader reader = new FileReader(file);  
				BufferedReader in = new BufferedReader(reader);  
				String string;
				
				while ((string = in.readLine()) != null)
				{
					String linha = string;
	            	
	               	if(linha.trim().length() > 0)
	               	{
	               		try
	               		{
		               		if(Integer.parseInt(linha.substring(5, 7)) == 10)
		               		{
		               			String montanteDemandandoStr = linha.substring(339, 361);
		               			double montanteDemandando = Double.parseDouble(montanteDemandandoStr);
		               			montante+=montanteDemandando;
		               			qtde++;
		               		}
	               		}
	               		catch (Exception e)
	               		{
	               			
						}
	               	}
				}
				
				//System.out.println("Achou arquivo " + nomeArquivo);
			}
		}
		
		return qtde + ";"+montante;
	}
	
	public String obterDadosAquivoApolice(Aseguradora aseguradora, Date dataInicio) throws Exception
	{
		String mesAno = new SimpleDateFormat("yyyyMM").format(dataInicio);
		
		String nomeArquivo = "A"+aseguradora.obterSigla()+mesAno + ".txt";
		
		File file = new File("C:/Aseguradoras/Archivos/"+nomeArquivo);
		
		int qtde = 0;
		int qtdeNova = 0;
		int qtdeRenovada = 0;
		double capitalGS = 0;
		double primaGS = 0;
		
		//System.out.println(nomeArquivo);
		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio));
		int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio));
		
		if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Instrumento"))
		{
			if(file.exists())
			{
				FileReader reader = new FileReader(file);  
				BufferedReader in = new BufferedReader(reader);  
				String string;
				
				while ((string = in.readLine()) != null)
				{
					String linha = string;
	            	
	               	if(linha.trim().length() > 0)
	               	{
	               		try
	               		{
		               		if(Integer.parseInt(linha.substring(5, 7)) == 2)
		               		{
		               			String tipoApoliceSub = linha.substring(27, 28);
		               			if(tipoApoliceSub.equals("1"))
		               				qtdeNova++;
				                else if(tipoApoliceSub.equals("2"))
				                	qtdeRenovada++;
		               			
		               			String capitalGuaraniStr = linha.substring(108, 130);
				                if(!capitalGuaraniStr.equals(""))
				                	capitalGS+= Double.parseDouble(capitalGuaraniStr);
				                
				                String primasSeguroStr = linha.substring(154, 176);
				                if(!primasSeguroStr.equals(""))
				                	primaGS = Double.parseDouble(primasSeguroStr);
		               			
		               			qtde++;
		               		}
	               		}
	               		catch (Exception e)
	               		{
	               			
						}
	               	}
				}
				
				//System.out.println("Achou arquivo " + nomeArquivo);
			}
		}
		return qtde + ";"+qtdeNova + ";" + qtdeRenovada + ";" + capitalGS + ";" + primaGS;
	}
	
	public String obterDadosAquivoFinanciamento(Aseguradora aseguradora, Date dataInicio) throws Exception
	{
		String mesAno = new SimpleDateFormat("yyyyMM").format(dataInicio);
		
		String nomeArquivo = "A"+aseguradora.obterSigla()+mesAno + ".txt";
		
		File file = new File("C:/Aseguradoras/Archivos/"+nomeArquivo);
		
		int qtde = 0;
		
		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio));
		int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio));
		
		if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Instrumento"))
		{
			if(file.exists())
			{
				FileReader reader = new FileReader(file);  
				BufferedReader in = new BufferedReader(reader);  
				String string;
				
				while ((string = in.readLine()) != null)
				{
					String linha = string;
	            	
	               	if(linha.trim().length() > 0)
	               	{
	               		try
	               		{
		               		if(Integer.parseInt(linha.substring(5, 7)) == 13)
		               			qtde++;
	               		}
	               		catch (Exception e)
	               		{
	               			
						}
	               	}
				}
				
				//System.out.println("Achou arquivo " + nomeArquivo);
			}
		}
		
		return new Integer(qtde).toString();
	}
	
	//REGISTRO 03
	public String obterDadosAquivoReservas(Aseguradora aseguradora, Date dataInicio) throws Exception
	{
		String mesAno = new SimpleDateFormat("yyyyMM").format(dataInicio);
		
		String nomeArquivo = "A"+aseguradora.obterSigla()+mesAno + ".txt";
		
		File file = new File("C:/Aseguradoras/Archivos/"+nomeArquivo);
		
		int qtde = 0;
		double risco = 0;
		double sinistro = 0;
		double reserva = 0;
		
		//System.out.println(nomeArquivo);
		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio));
		int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio));
		
		if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Instrumento"))
		{
			if(file.exists())
			{
				FileReader reader = new FileReader(file);  
				BufferedReader in = new BufferedReader(reader);  
				String string;
				
				while ((string = in.readLine()) != null)
				{
					String linha = string;
	            	
	               	if(linha.trim().length() > 0)
	               	{
	               		try
	               		{
		               		if(Integer.parseInt(linha.substring(5, 7)) == 3)
		               		{
		               			String cursoStr = linha.substring(35, 57);
		               			if(!cursoStr.equals(""))
		               				risco+= Double.parseDouble(cursoStr);
		               			
		               			String sinistroStr = linha.substring(57, 79);
		               			if(!sinistroStr.equals(""))
		               				sinistro+=Double.parseDouble(sinistroStr);
		               			
		               			String reservasStr = linha.substring(79, 101);
		               			if(!reservasStr.equals(""))
		               				reserva = Double.parseDouble(reservasStr);
				                
				                qtde++;
		               		}
	               		}
	               		catch (Exception e)
	               		{
	               			
						}
	               	}
				}
				
				//System.out.println("Achou arquivo " + nomeArquivo);
			}
		}
		
		return new Integer(qtde).toString() + ";"+risco+";"+sinistro+";"+reserva;
	}
	
	//REGISTRO 04
	public String obterDadosAquivoReaseguros(Aseguradora aseguradora, Date dataInicio) throws Exception
	{
		String mesAno = new SimpleDateFormat("yyyyMM").format(dataInicio);
		
		String nomeArquivo = "A"+aseguradora.obterSigla()+mesAno + ".txt";
		
		File file = new File("C:/Aseguradoras/Archivos/"+nomeArquivo);
		
		int qtdeTotal = 0;
		double capital = 0;
		double prima = 0;
		double comissao = 0;
		
		//System.out.println(nomeArquivo);
		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio));
		int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio));
		
		if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Instrumento"))
		{
			if(file.exists())
			{
				FileReader reader = new FileReader(file);  
				BufferedReader in = new BufferedReader(reader);  
				String string;
				
				while ((string = in.readLine()) != null)
				{
					String linha = string;
	            	
	               	if(linha.trim().length() > 0)
	               	{
	               		try
	               		{
		               		if(Integer.parseInt(linha.substring(5, 7)) == 4)
		               		{
		               			String qtdeStr = linha.substring(45, 47);
		               			int qtde=Integer.parseInt(qtdeStr);
		               			qtdeTotal+=qtde;
		               			
		               			int ultimo = 0;
		               			
		               			for(int w = 0; w < qtde; w++)
				                {
				                    if(ultimo == 0)
				                        ultimo = 47;
				                    
				                    String reaseguroGs1Str = linha.substring(ultimo + 3 + 1 + 3, ultimo + 3 + 1 + 3 + 22);
				                    if(!reaseguroGs1Str.equals(""))
				                    	capital+= Double.parseDouble(reaseguroGs1Str);
				                    
				                    String primaReaseguro1GsStr = linha.substring(ultimo + 3 + 1 + 3 + 22 + 2 + 22, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22);
				                    if(!primaReaseguro1GsStr.equals(""))
				                    	prima+= Double.parseDouble(primaReaseguro1GsStr);
			               			
				                    String comissaoReaseguro1GsStr = linha.substring(ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22);
				                    if(!comissaoReaseguro1GsStr.equals(""))
				                    	comissao+= Double.parseDouble(comissaoReaseguro1GsStr);
			               			
				                    ultimo+=3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4 + 2 + 2 + 1;
				                }
		               		}
	               		}
	               		catch (Exception e)
	               		{
	               			
						}
	               	}
				}
				
				//System.out.println("Achou arquivo " + nomeArquivo);
			}
		}
		
		return qtdeTotal+";"+capital+";"+prima+";"+comissao;
	}
	
	//REGISTRO 05
	public String obterDadosAquivoCoaseguros(Aseguradora aseguradora, Date dataInicio) throws Exception
	{
		String mesAno = new SimpleDateFormat("yyyyMM").format(dataInicio);
		
		String nomeArquivo = "A"+aseguradora.obterSigla()+mesAno + ".txt";
		
		File file = new File("C:/Aseguradoras/Archivos/"+nomeArquivo);
		
		int qtdeTotal = 0;
		double capital = 0;
		double prima = 0;
		
		//System.out.println(nomeArquivo);
		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio));
		int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio));
		
		if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Instrumento"))
		{
			if(file.exists())
			{
				FileReader reader = new FileReader(file);  
				BufferedReader in = new BufferedReader(reader);  
				String string;
				
				while ((string = in.readLine()) != null)
				{
					String linha = string;
	            	
	               	if(linha.trim().length() > 0)
	               	{
	               		try
	               		{
		               		if(Integer.parseInt(linha.substring(5, 7)) == 5)
		               		{
		               			String qtdeStr = linha.substring(47, 49);
		               			int qtde=Integer.parseInt(qtdeStr);
		               			qtdeTotal+=qtde;
		               			
		               			int ultimo = 0;
		               			
		               			for(int w = 0; w < qtde; w++)
				                {
				                    if(ultimo == 0)
				                        ultimo = 49;
				                    
				                    String capitalGsStr = linha.substring(ultimo + 3, ultimo + 3 + 22);
				                    if(!capitalGsStr.equals(""))
				                    	capital+= Double.parseDouble(capitalGsStr);
				                    
				                    String primaGsStr = linha.substring(ultimo + 3 + 22 + 2 + 22 + 6, ultimo + 3 + 22 + 2 + 22 + 6 + 22);
				                    if(!primaGsStr.equals(""))
				                    	prima+= Double.parseDouble(primaGsStr);
				                    
				                    ultimo += 3 + 22 + 2 + 22 + 6 + 22 + 2 + 22;
				                }
		               		}
	               		}
	               		catch (Exception e)
	               		{
	               			
						}
	               	}
				}
				
				//System.out.println("Achou arquivo " + nomeArquivo);
			}
		}
		
		return qtdeTotal+";"+capital+";"+prima;
	}
	
	//REGISTRO 06
	public String obterDadosAquivoSinistros(Aseguradora aseguradora, Date dataInicio) throws Exception
	{
		String mesAno = new SimpleDateFormat("yyyyMM").format(dataInicio);
		
		String nomeArquivo = "A"+aseguradora.obterSigla()+mesAno + ".txt";
		
		File file = new File("C:/Aseguradoras/Archivos/"+nomeArquivo);
		
		int qtdeTotal = 0;
		double montante = 0;
		double recReaseguro = 0;
		double recTerceiro = 0;
		
		//System.out.println(nomeArquivo);
		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio));
		int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio));
		
		if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Instrumento"))
		{
			if(file.exists())
			{
				FileReader reader = new FileReader(file);  
				BufferedReader in = new BufferedReader(reader);  
				String string;
				
				while ((string = in.readLine()) != null)
				{
					String linha = string;
	            	
	               	if(linha.trim().length() > 0)
	               	{
	               		try
	               		{
		               		if(Integer.parseInt(linha.substring(5, 7)) == 6)
		               		{
		               			String qtdeStr = linha.substring(45, 47);
		               			int qtde=Integer.parseInt(qtdeStr);
		               			qtdeTotal+=qtde;
		               			
		               			int ultimo = 0;
		               			
		               			for(int w = 0; w < qtde; w++)
				                {
				                    if(ultimo == 0)
				                        ultimo = 47;
				                    
				                    String montanteGsStr = linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22);
				                    if(!montanteGsStr.equals(""))
				                    	montante+= Double.parseDouble(montanteGsStr);
				                    
				                    String recuperoReaseguradoraStr = linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22);
				                    if(!recuperoReaseguradoraStr.equals(""))
				                    	recReaseguro+= Double.parseDouble(recuperoReaseguradoraStr);
				                    
				                    String recuperoTerceiroStr = linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22 + 22);
				                    if(!recuperoTerceiroStr.equals(""))
				                    	recTerceiro+= Double.parseDouble(recuperoTerceiroStr);
				                    
				                    ultimo = ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22 + 22 + 6 + 120;
				                }
		               		}
	               		}
	               		catch (Exception e)
	               		{
	               			
						}
	               	}
				}
				
				//System.out.println("Achou arquivo " + nomeArquivo);
			}
		}
		
		return qtdeTotal+";"+montante+";"+recReaseguro+";"+recTerceiro;
	}
	
	//REGISTRO 07
	public String obterDadosAquivoPagos(Aseguradora aseguradora, Date dataInicio) throws Exception
	{
		String mesAno = new SimpleDateFormat("yyyyMM").format(dataInicio);
		
		String nomeArquivo = "A"+aseguradora.obterSigla()+mesAno + ".txt";
		
		File file = new File("C:/Aseguradoras/Archivos/"+nomeArquivo);
		
		int qtdeTotal = 0;
		double montanteDocumentoPago = 0;
		
		//System.out.println(nomeArquivo);
		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio));
		int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio));
		
		if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Instrumento"))
		{
			if(file.exists())
			{
				FileReader reader = new FileReader(file);  
				BufferedReader in = new BufferedReader(reader);  
				String string;
				
				while ((string = in.readLine()) != null)
				{
					String linha = string;
	            	
	               	if(linha.trim().length() > 0)
	               	{
	               		try
	               		{
		               		if(Integer.parseInt(linha.substring(5, 7)) == 7)
		               		{
		               			String qtdeStr = linha.substring(45, 47);
		               			int qtde=Integer.parseInt(qtdeStr);
		               			qtdeTotal+=qtde;
		               			
		               			int ultimo = 0;
		               			
		               			for(int w = 0; w < qtde; w++)
				                {
				                    if(ultimo == 0)
				                        ultimo = 47;
				                    
				                    String montanteDocumentoPagoStr = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22);
				                    if(!montanteDocumentoPagoStr.equals(""))
				                    	montanteDocumentoPago+= Double.parseDouble(montanteDocumentoPagoStr);
				                    
				                    ultimo += 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22 + 4 + 2 + 2 + 1;
				                }
		               		}
	               		}
	               		catch (Exception e)
	               		{
	               			
						}
	               	}
				}
				
				//System.out.println("Achou arquivo " + nomeArquivo);
			}
		}
		
		return qtdeTotal+";"+montanteDocumentoPago;
	}
	
	//REGISTRO 08
	public String obterDadosAquivoAnulacao(Aseguradora aseguradora, Date dataInicio) throws Exception
	{
		String mesAno = new SimpleDateFormat("yyyyMM").format(dataInicio);
		
		String nomeArquivo = "A"+aseguradora.obterSigla()+mesAno + ".txt";
		
		File file = new File("C:/Aseguradoras/Archivos/"+nomeArquivo);
		
		int qtde = 0;
		double capital = 0;
		double prima = 0;
		double comissaoAnulada = 0;
		double comissaoRecuperada = 0;
		double saldoAnulacao = 0;
		
		//System.out.println(nomeArquivo);
		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio));
		int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio));
		
		if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Instrumento"))
		{
			if(file.exists())
			{
				FileReader reader = new FileReader(file);  
				BufferedReader in = new BufferedReader(reader);  
				String string;
				
				while ((string = in.readLine()) != null)
				{
					String linha = string;
	            	
	               	if(linha.trim().length() > 0)
	               	{
	               		try
	               		{
		               		if(Integer.parseInt(linha.substring(5, 7)) == 8)
		               		{
		               			String capitalAnuladoGsStr = linha.substring(36, 58);
		               			if(!capitalAnuladoGsStr.equals(""))
		               				capital+= Double.parseDouble(capitalAnuladoGsStr);
		               			
		               			String primaAnuladaGsStr = linha.substring(88, 110);
				                if(!primaAnuladaGsStr.equals(""))
				                	prima+= Double.parseDouble(primaAnuladaGsStr);
				                
				                String comissaoAnuladaGsStr = linha.substring(134, 156);
				                if(!comissaoAnuladaGsStr.equals(""))
				                	comissaoAnulada+= Double.parseDouble(comissaoAnuladaGsStr);
				                
				                String comissaoRecuperarGsStr = linha.substring(180, 202);
				                if(!comissaoRecuperarGsStr.equals(""))
				                	comissaoRecuperada+=Double.parseDouble(comissaoRecuperarGsStr);
				                
				                String saldoAnuladoGsStr = linha.substring(226, 248);
				                if(saldoAnuladoGsStr.equals(""))
				                	saldoAnulacao+=Double.parseDouble(saldoAnuladoGsStr);
				                
				                qtde++;
		               		}
	               		}
	               		catch (Exception e)
	               		{
	               			
						}
	               	}
				}
				
				//System.out.println("Achou arquivo " + nomeArquivo);
			}
		}
		
		return qtde + ";"+capital+";"+prima+";"+comissaoAnulada+";"+comissaoRecuperada+";"+saldoAnulacao;
	}
	
	//REGISTRO 09
	public String obterDadosAquivoCobranca(Aseguradora aseguradora, Date dataInicio) throws Exception
	{
		String mesAno = new SimpleDateFormat("yyyyMM").format(dataInicio);
		
		String nomeArquivo = "A"+aseguradora.obterSigla()+mesAno + ".txt";
		
		File file = new File("C:/Aseguradoras/Archivos/"+nomeArquivo);
		
		int qtdeTotal = 0;
		double valorCobranca = 0;
		double interes = 0;
		
		//System.out.println(nomeArquivo);
		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio));
		int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio));
		
		if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Instrumento"))
		{
			if(file.exists())
			{
				FileReader reader = new FileReader(file);  
				BufferedReader in = new BufferedReader(reader);  
				String string;
				
				while ((string = in.readLine()) != null)
				{
					String linha = string;
	            	
	               	if(linha.trim().length() > 0)
	               	{
	               		try
	               		{
		               		if(Integer.parseInt(linha.substring(5, 7)) == 9)
		               		{
		               			String qtdeStr = linha.substring(45, 47);
		               			int qtde=Integer.parseInt(qtdeStr);
		               			qtdeTotal+=qtde;
		               			
		               			int ultimo = 0;
		               			
		               			for(int w = 0; w < qtde; w++)
				                {
				                    if(ultimo == 0)
				                        ultimo = 47;
				                    
				                    String valorCobrancaGsStr = linha.substring(ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2, ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22);
				                    if(!valorCobrancaGsStr.equals(""))
				                    	valorCobranca+= Double.parseDouble(valorCobrancaGsStr);
				                    
				                    String valorInteresCobrancaStr = linha.substring(ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2 + 22, ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2 + 22 + 22);
				                    if(!valorInteresCobrancaStr.equals(""))
				                    	interes+= Double.parseDouble(valorInteresCobrancaStr);
				                    
				                    ultimo += 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2 + 22 + 22;
				                }
		               		}
	               		}
	               		catch (Exception e)
	               		{
	               			
						}
	               	}
				}
				
				//System.out.println("Achou arquivo " + nomeArquivo);
			}
		}
		
		return qtdeTotal+";"+valorCobranca+";"+interes;
	}
	
	//REGISTRO 11
	public String obterDadosAquivoEndoso(Aseguradora aseguradora, Date dataInicio) throws Exception
	{
		String mesAno = new SimpleDateFormat("yyyyMM").format(dataInicio);
		
		String nomeArquivo = "A"+aseguradora.obterSigla()+mesAno + ".txt";
		
		File file = new File("C:/Aseguradoras/Archivos/"+nomeArquivo);
		
		int qtdeTotal = 0;
		double valorAjustes = 0;
		
		//System.out.println(nomeArquivo);
		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio));
		int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio));
		
		if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Instrumento"))
		{
			if(file.exists())
			{
				FileReader reader = new FileReader(file);  
				BufferedReader in = new BufferedReader(reader);  
				String string;
				
				while ((string = in.readLine()) != null)
				{
					String linha = string;
	            	
	               	if(linha.trim().length() > 0)
	               	{
	               		try
	               		{
		               		if(Integer.parseInt(linha.substring(5, 7)) == 11)
		               		{
		               			String qtdeStr = linha.substring(45, 47);
		               			int qtde=Integer.parseInt(qtdeStr);
		               			qtdeTotal+=qtde;
		               			
		               			int ultimo = 0;
		               			
		               			for(int w = 0; w < qtde; w++)
				                {
				                    if(ultimo == 0)
				                        ultimo = 47;
				                    
				                    String primaGsStr = linha.substring(ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22);
				                    if(!primaGsStr.equals(""))
				                    {
					                    double primaGs = 0;
					                    if(primaGsStr.substring(0, 1).equals("-"))
					                        primaGs = Double.parseDouble(primaGsStr.substring(1, primaGsStr.length())) * -1;
					                    else
					                        primaGs = Double.parseDouble(primaGsStr);
					                    
					                    valorAjustes+=primaGs;
					                    
					                    ultimo +=10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22 + 2 + 22;
				                    }
				                }
		               		}
	               		}
	               		catch (Exception e)
	               		{
	               			
						}
	               	}
				}
				
				//System.out.println("Achou arquivo " + nomeArquivo);
			}
		}
		
		return qtdeTotal+";"+valorAjustes;
	}
	
	//REGISTRO 12
	public String obterDadosAquivoFinalizacao(Aseguradora aseguradora, Date dataInicio) throws Exception
	{
		String mesAno = new SimpleDateFormat("yyyyMM").format(dataInicio);
		
		String nomeArquivo = "A"+aseguradora.obterSigla()+mesAno + ".txt";
		
		File file = new File("C:/Aseguradoras/Archivos/"+nomeArquivo);
		
		int qtdeTotal = 0;
		
		//System.out.println(nomeArquivo);
		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio));
		int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio));
		
		if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Instrumento"))
		{
			if(file.exists())
			{
				FileReader reader = new FileReader(file);  
				BufferedReader in = new BufferedReader(reader);  
				String string;
				
				while ((string = in.readLine()) != null)
				{
					String linha = string;
	            	
	               	if(linha.trim().length() > 0)
	               	{
	               		try
	               		{
		               		if(Integer.parseInt(linha.substring(5, 7)) == 12)
		               			qtdeTotal++;
	               		}
	               		catch (Exception e)
	               		{
	               			
						}
	               	}
				}
				
				//System.out.println("Achou arquivo " + nomeArquivo);
			}
		}
		
		return new Integer(qtdeTotal).toString();
	}
	
	//REGISTRO 14
	public String obterDadosAquivoGastos(Aseguradora aseguradora, Date dataInicio) throws Exception
	{
		String mesAno = new SimpleDateFormat("yyyyMM").format(dataInicio);
		
		String nomeArquivo = "A"+aseguradora.obterSigla()+mesAno + ".txt";
		
		File file = new File("C:/Aseguradoras/Archivos/"+nomeArquivo);
		
		int qtdeTotal = 0;
		double liquidador = 0;
		double asegurado = 0;
		double terceiro = 0;
		double outros = 0;
		
		//System.out.println(nomeArquivo);
		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio));
		int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio));
		
		if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Instrumento"))
		{
			if(file.exists())
			{
				FileReader reader = new FileReader(file);  
				BufferedReader in = new BufferedReader(reader);  
				String string;
				
				while ((string = in.readLine()) != null)
				{
					String linha = string;
	            	
	               	if(linha.trim().length() > 0)
	               	{
	               		try
	               		{
		               		if(Integer.parseInt(linha.substring(5, 7)) == 14)
		               		{
		               			String qtdeStr = linha.substring(45, 47);
		               			int qtde=Integer.parseInt(qtdeStr);
		               			qtdeTotal+=qtde;
		               			
		               			int ultimo = 0;
		               			
		               			for(int w = 0; w < qtde; w++)
				                {
				                    if(ultimo == 0)
				                        ultimo = 47;
				                    
				                    String abonadoGsStr = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22);
				                    double abonadoGs = Double.parseDouble(abonadoGsStr);
				                    
				                    String tipoPagamento = linha.substring(ultimo + 6 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 1);
				                    
				                    if(tipoPagamento.equals("1"))
				                    	liquidador+=abonadoGs;
				                    else if(tipoPagamento.equals("2"))
				                    	asegurado+=abonadoGs;
				                    else if(tipoPagamento.equals("3"))
				                    	terceiro+=abonadoGs;
				                    else if(tipoPagamento.equals("4"))
				                    	outros+=abonadoGs;
				                    
				                    ultimo += 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10 + 10 + 1 + 1;
				                }
		               		}
	               		}
	               		catch (Exception e)
	               		{
	               			
						}
	               	}
				}
				
				//System.out.println("Achou arquivo " + nomeArquivo);
			}
		}
		return qtdeTotal+";"+liquidador+";"+asegurado+";"+terceiro+";"+outros;
	}
	
	//REGISTRO 15
	public String obterDadosAquivoAnulacaoReaseguro(Aseguradora aseguradora, Date dataInicio) throws Exception
	{
		String mesAno = new SimpleDateFormat("yyyyMM").format(dataInicio);
		
		String nomeArquivo = "A"+aseguradora.obterSigla()+mesAno + ".txt";
		
		File file = new File("C:/Aseguradoras/Archivos/"+nomeArquivo);
		
		int qtdeTotal = 0;
		double capital = 0; 
		double prima = 0;
		double comissao = 0;
		
		//System.out.println(nomeArquivo);
		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio));
		int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio));
		
		if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Instrumento"))
		{
			if(file.exists())
			{
				FileReader reader = new FileReader(file);  
				BufferedReader in = new BufferedReader(reader);  
				String string;
				
				while ((string = in.readLine()) != null)
				{
					String linha = string;
	            	
	               	if(linha.trim().length() > 0)
	               	{
	               		try
	               		{
		               		if(Integer.parseInt(linha.substring(5, 7)) == 15)
		               		{
		               			String capitalAnuladoGsStr = linha.substring(40, 62);
		               			if(!capitalAnuladoGsStr.equals(""))
		               				capital+= Double.parseDouble(capitalAnuladoGsStr);
		               			
		               			String primaAnuladaGsStr = linha.substring(91, 113);
		               			if(!primaAnuladaGsStr.equals(""))
		               				prima+= Double.parseDouble(primaAnuladaGsStr);
		               			
		               			String comissaoAnuladaGsStr = linha.substring(137, 159);
		               			if(!comissaoAnuladaGsStr.equals(""))
		               				comissao+= Double.parseDouble(comissaoAnuladaGsStr);
		               			
		               			qtdeTotal++;
		               		}
	               		}
	               		catch (Exception e)
	               		{
	               			
						}
	               	}
				}
				
				//System.out.println("Achou arquivo " + nomeArquivo);
			}
		}
		
		return qtdeTotal + ";" + capital + ";" + prima + ";" + comissao;
	}
	
	//REGISTRO 16
	public String obterDadosAquivoMorosidade(Aseguradora aseguradora, Date dataInicio) throws Exception
	{
		String mesAno = new SimpleDateFormat("yyyyMM").format(dataInicio);
		
		String nomeArquivo = "A"+aseguradora.obterSigla()+mesAno + ".txt";
		
		File file = new File("C:/Aseguradoras/Archivos/"+nomeArquivo);
		
		int qtdeTotal = 0;
		double valor = 0; 
		
		//System.out.println(nomeArquivo);
		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio));
		int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio));
		
		if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Instrumento"))
		{
			if(file.exists())
			{
				FileReader reader = new FileReader(file);  
				BufferedReader in = new BufferedReader(reader);  
				String string;
				
				while ((string = in.readLine()) != null)
				{
					String linha = string;
	            	
	               	if(linha.trim().length() > 0)
	               	{
	               		try
	               		{
		               		if(Integer.parseInt(linha.substring(5, 7)) == 16)
		               		{
		               			String valorGsStr = linha.substring(48, 70);
		               			if(!valorGsStr.equals(""))
		               				valor+= Double.parseDouble(valorGsStr);
		               			
		               			qtdeTotal++;
		               		}
	               		}
	               		catch (Exception e)
	               		{
	               			
						}
	               	}
				}
				
				//System.out.println("Achou arquivo " + nomeArquivo);
			}
		}
		
		return qtdeTotal + ";" + valor;
	}
	
	//REGISTRO 17
	public String obterDadosAquivoAsegurado(Aseguradora aseguradora, Date dataInicio) throws Exception
	{
		String mesAno = new SimpleDateFormat("yyyyMM").format(dataInicio);
		
		String nomeArquivo = "B"+aseguradora.obterSigla()+mesAno + ".txt";
		
		File file = new File("C:/Aseguradoras/Archivos/"+nomeArquivo);
		
		int qtdeTotal = 0;
		
		//System.out.println(nomeArquivo);
		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio));
		int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio));
		
		if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Instrumento"))
		{
			if(file.exists())
			{
				FileReader reader = new FileReader(file);  
				BufferedReader in = new BufferedReader(reader);  
				String string;
				
				while ((string = in.readLine()) != null)
				{
					String linha = string;
	            	
	               	if(linha.trim().length() > 0)
	               	{
	               		try
	               		{
		               		if(Integer.parseInt(linha.substring(5, 7)) == 17)
		               			qtdeTotal++;
	               		}
	               		catch (Exception e)
	               		{
	               			
						}
	               	}
				}
				
				//System.out.println("Achou arquivo " + nomeArquivo);
			}
		}
		
		return new Integer(qtdeTotal).toString();
	}
	
	public Collection<String> obterApolicesRG001(Aseguradora aseguradora, String situacaoSeguro, Date dataInicio, Date dataFim, boolean especial, boolean modificado) throws Exception
	{
		Map<String, String> informacoes = new TreeMap<String, String>();
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		String sql = "";
		
		if(especial)
			sql = "select origem, COUNT(*) as qtde from apolice,evento,plano where evento.id = apolice.id and apolice.plano = plano.id and especial = 1 and data_emissao>="+dataInicio.getTime()+" and data_emissao<= "+dataFim.getTime();
		else if(modificado)
			sql = "select origem, COUNT(*) as qtde from apolice,evento,plano where evento.id = apolice.id and apolice.plano = plano.id and especial = 2 and data_emissao>="+dataInicio.getTime()+" and data_emissao<= "+dataFim.getTime();
		
		if(!especial && !modificado)
			sql = "select origem, COUNT(*) as qtde from apolice,evento where evento.id = apolice.id and apolice.plano = 0 and data_emissao>="+dataInicio.getTime()+" and data_emissao<= "+dataFim.getTime();
			
		if(aseguradora!=null)
			sql+=" and origem = "+aseguradora.obterId();
		if(!situacaoSeguro.equals(""))
			sql+=" and situacao_seguro = '"+situacaoSeguro+"'";
		
		sql+= " group by origem";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("origem");
			
			int qtde = rows[i].getInt("qtde");
			
			Entidade e = home.obterEntidadePorId(id);
			
			String nome = e.obterNome();
			
			informacoes.put(nome, nome +";"+qtde+";"+id);
		}
		
		return informacoes.values();
	}
	
	public Collection<Apolice> obterApolicesHistoricoAnual(Date dataInicio, Date dataFim, String situacaoSeguro, String secao) throws Exception
	{
		Collection<Apolice> apolices = new ArrayList<Apolice>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		String sql = "";
		if(secao.indexOf("no definida") == -1)
			sql = "select evento.id from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" and situacao_seguro = '"+situacaoSeguro+"' and plano.secao = '"+secao + "'";
		else
			sql = "select evento.id from evento,apolice where evento.id = apolice.id and apolice.plano = 0 and data_emissao>="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" and situacao_seguro = '"+situacaoSeguro+"'";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Apolice apolice = (Apolice) home.obterEventoPorId(id);
			
			apolices.add(apolice);
		}
		
		return apolices;
	}
	
	public Collection<Apolice> obterSinistrosHistoricoAnual(Date dataInicio, Date dataFim, String situacaoSeguro, String secao) throws Exception
	{
		Collection<Apolice> apolices = new ArrayList<Apolice>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		String sql = "";
		if(secao.indexOf("no definida") == -1)
			sql = "select superior from evento,sinistro,apolice,plano where evento.id = sinistro.id and superior = apolice.id and apolice.plano = plano.id and data_sinistro>="+dataInicio.getTime()+" and data_sinistro<="+dataFim.getTime()+" and situacao_seguro = '"+situacaoSeguro+"' and plano.secao = '"+secao + "'";
		else
			sql = "select superior from evento,sinistro,apolice where evento.id = sinistro.id and superior = apolice.id and apolice.plano = 0 and data_sinistro>="+dataInicio.getTime()+" and data_sinistro<="+dataFim.getTime()+" and situacao_seguro = '"+situacaoSeguro+"'";
		
		sql+=" group by superior";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("superior");
			
			Apolice apolice = (Apolice) home.obterEventoPorId(id);
			
			apolices.add(apolice);
		}
		
		return apolices;
	}
	
	public Collection<Apolice> obterApolicesRG001Aseg(Aseguradora aseguradora, String situacaoSeguro, Date dataInicio, Date dataFim, boolean especial, boolean modificado) throws Exception
	{
		Collection<Apolice> apolices = new ArrayList<Apolice>();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		String sql = "";
		
		if(especial)
			sql = "select evento.id from apolice,evento,plano where evento.id = apolice.id and apolice.plano = plano.id and especial = 1 and data_emissao>="+dataInicio.getTime()+" and data_emissao<= "+dataFim.getTime();
		else if(modificado)
			sql = "select evento.id from apolice,evento,plano where evento.id = apolice.id and apolice.plano = plano.id and especial = 2 and data_emissao>="+dataInicio.getTime()+" and data_emissao<= "+dataFim.getTime();
		
		if(!especial && !modificado)
			sql = "select evento.id from apolice,evento where evento.id = apolice.id and apolice.plano = 0 and data_emissao>="+dataInicio.getTime()+" and data_emissao<= "+dataFim.getTime();
		
		if(aseguradora!=null)
			sql+=" and origem = "+aseguradora.obterId();
		if(!situacaoSeguro.equals(""))
			sql+=" and situacao_seguro = '"+situacaoSeguro+"'";
		
		sql+=" order by data_emissao";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Apolice apolice = (Apolice) home.obterEventoPorId(id);
			
			apolices.add(apolice);
			
		}
		
		return apolices;
	}
	
	public Collection<String> obterApolicesReaseguro(Aseguradora aseguradora,Date dataInicio,Date dataFim,int qtde, String situacao, double valor, double valorMenor, double valorDolar, double valorMenorDolar) throws Exception
	{
		DecimalFormat format = new DecimalFormat("#,##0.00");
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		Collection<String> apolices = new ArrayList<String>();
		
		String sql = "";
		if(qtde > 0)
			sql+="select top " + qtde + " evento.id";
		else
			sql = "select evento.id";
		
		sql+=",capital_gs,prima_gs,tipo_moeda_capital_gs,capital_me from evento,apolice where evento.id = apolice.id and data_emissao>=" + dataInicio.getTime() + " and data_emissao<="+dataFim.getTime();
		if(aseguradora!=null)
			sql+=" and origem = " + aseguradora.obterId();
		if(!situacao.equals("0"))
			sql+=" and situacao_seguro = '" + situacao + "'";
		
		sql+=" order by data_emissao";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		int com = 0;
		int sem = 0;
		int semCapitalMaior = 0;
		int semCapitalMaiorDolar = 0;
		int semCapitalMenor = 0;
		int semCapitalMenorDolar = 0;
		
		double primaCom = 0;
		double capitalCom = 0;
		double primaSem = 0;
		double capitalSem = 0;
		double capitalSemMaior = 0;
		double capitalSemMenor = 0;
		double primaSemMaior = 0;
		double capitalSemMaiorDolar = 0;
		double primaSemMaiorDolar = 0;
		double primaSemMenor = 0;
		double capitalSemMenorDolar = 0;
		double primaSemMenorDolar = 0;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			double capitalApolice = rows[i].getDouble("capital_gs");
			double primaApolice = rows[i].getDouble("prima_gs");
			String tipoMoeda = rows[i].getString("tipo_moeda_capital_gs");
			double capitalMe = rows[i].getDouble("capital_me");
			
			sql = "select count(*) as qtde from evento,dados_reaseguro where evento.id = dados_reaseguro.id and superior = " + id + " and dados_reaseguro.situacao = 'Vigente'";
			query = this.getModelManager().createSQLQuery("crm",sql);
			int qteDados = query.executeAndGetFirstRow().getInt("qtde");
			if(qteDados == 0 )
			{
				sem++;
				primaSem+=primaApolice;
				capitalSem+=capitalMe;
				
				if(valor>0 && capitalApolice>=valor)
				{
					semCapitalMaior++;
					primaSemMaior+=primaApolice;
					capitalSemMaior+=capitalMe;
				}
				if(valorMenor>0 && capitalApolice<=valorMenor)
				{
					semCapitalMenor++;
					primaSemMenor+=primaApolice;
					capitalSemMenor+=capitalMe;
				}
				if(tipoMoeda.equals("Dólar USA"))
				{
					if(valorDolar>0 && capitalMe>=valorDolar)
					{
						semCapitalMaiorDolar++;
						capitalSemMaiorDolar+=capitalMe;
						primaSemMaiorDolar+=primaApolice;
					}
					if(valorMenorDolar>0 && capitalMe<=valorMenorDolar)
					{
						semCapitalMenorDolar++;
						capitalSemMenorDolar+=capitalMe;
						primaSemMenorDolar+=primaApolice;
					}
				}
			}
			else
			{
				//com++;
				//primaCom+=primaApolice;
				sql = "select evento.id as qtde from evento,dados_reaseguro where evento.id = dados_reaseguro.id and superior = " + id + " and dados_reaseguro.situacao = 'Vigente'";
				if(valor > 0)
					sql+=" and caiptal_gs>="+valor;
				if(valorMenor>0)
					sql+=" and caiptal_gs<="+valorMenor;
				
				//System.out.println(sql);
				
				query = this.getModelManager().createSQLQuery("crm",sql);
				//qteDados = query.executeAndGetFirstRow().getInt("qtde");
				SQLRow[] rows2 = query.execute();
				boolean soma = true;
				for(int j = 0 ; j < rows2.length ; j++)
				{
					long dadoId = rows2[j].getLong("id");
					
					sql = "select count(*) as qtde from evento,registro_anulacao where evento.id = registro_anulacao.id and superior = " + dadoId + " and tipo = 'Total'";
					query = this.getModelManager().createSQLQuery("crm",sql);
					
					qteDados = query.executeAndGetFirstRow().getInt("qtde"); 
					//System.out.println(qtde);
					if(qtde>0)
					{
						soma = false;
						break;
					}
				}
				if(soma)
				{
					com++;
					primaCom+=primaApolice;
					capitalCom+=capitalMe;
				}
			}
		}
		
		apolices.add("Pólizas con Reaseguro;"+com+";"+primaCom+";"+capitalCom);
		apolices.add("Pólizas sin Reaseguro;"+sem+";"+primaSem+";"+capitalSem);
		if(valor > 0)
			apolices.add("Pólizas sin Reaseguro con capital de póliza más que " + format.format(valor) + ";" + semCapitalMaior+";"+primaSemMaior+";"+capitalSemMaior);
		if(valorMenor > 0)
			apolices.add("Pólizas sin Reaseguro con capital de póliza menos que " + format.format(valorMenor) + ";" + semCapitalMenor+";"+primaSemMenor+";"+capitalSemMenor);
		if(valorDolar > 0)
			apolices.add("Pólizas sin Reaseguro con capital de póliza más que " + format.format(valorDolar) + " Dólar USA;" + semCapitalMaiorDolar+";"+primaSemMaiorDolar+";"+capitalSemMaiorDolar);
		if(valorMenorDolar > 0)
			apolices.add("Pólizas sin Reaseguro con capital de póliza menos que " + format.format(valorMenorDolar) + " Dólar USA;" + semCapitalMenorDolar+";"+primaSemMenorDolar+";"+capitalSemMenorDolar);
		
		return apolices;
	}
	
	public Collection<Apolice> obterApolicesReaseguro2(Aseguradora aseguradora,Date dataInicio,Date dataFim,int qtde, String situacao,double valor, double valorMenor, boolean comReaseguro, double valorDolar, double valorMenorDolar) throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		Collection<Apolice> apolices = new ArrayList<Apolice>();
		
		String sql = "";
		if(qtde > 0)
			sql+="select top " + qtde + " evento.id";
		else
			sql = "select evento.id";
		
		sql+=",capital_gs,tipo_moeda_capital_gs,capital_me from evento,apolice where evento.id = apolice.id and data_emissao>=" + dataInicio.getTime() + " and data_emissao<="+dataFim.getTime();
		if(aseguradora!=null)
			sql+=" and origem = " + aseguradora.obterId();
		if(!situacao.equals("0"))
			sql+=" and situacao_seguro = '" + situacao + "'";
		
		sql+=" order by data_emissao";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			double capitalApolice = rows[i].getDouble("capital_gs");
			String tipoMoeda = rows[i].getString("tipo_moeda_capital_gs");
			double capitalMe = rows[i].getDouble("capital_me");
			
			Apolice apolice = (Apolice) home.obterEventoPorId(id);
			
			if(comReaseguro)
			{
				sql = "select count(*) as qtde from evento,dados_reaseguro where evento.id = dados_reaseguro.id and superior = " + id + " and dados_reaseguro.situacao = 'Vigente'";
				if(valor > 0)
					sql+=" and caiptal_gs>="+valor;
				if(valorMenor>0)
					sql+=" and caiptal_gs<="+valorMenor;
				
				//System.out.println(sql);
				
				query = this.getModelManager().createSQLQuery("crm",sql);
				int qteDados = query.executeAndGetFirstRow().getInt("qtde");
				if(qteDados > 0)
					apolices.add(apolice);
			}
			else
			{
				sql = "select count(*) as qtde from evento,dados_reaseguro where evento.id = dados_reaseguro.id and superior = " + id + "and dados_reaseguro.situacao = 'Vigente'";
				
				query = this.getModelManager().createSQLQuery("crm",sql);
				int qteDados = query.executeAndGetFirstRow().getInt("qtde");
				if(qteDados == 0)
				{
					boolean addApolice = false;
					
					if(valorDolar > 0 || valorMenorDolar > 0)
					{
						if(tipoMoeda.equals("Dólar USA"))
						{
							if(valorDolar>0 && capitalMe>=valorDolar)
								addApolice = true;
							else if(valorMenorDolar>0 && capitalMe<=valorMenorDolar)
								addApolice = true;
						}
					}
					else
					{
						if(valor > 0 || valorMenor > 0)
						{
							if(valor>0 && capitalApolice>=valor)
								addApolice = true;
							else if(valorMenor>0 && capitalApolice<=valorMenor)
								addApolice = true;
						}
					}
					
					if(valorDolar == 0 && valorMenorDolar == 0 && valor == 0 && valorMenor == 0)
						addApolice = true;
					
					if(addApolice)
					{
						if(!apolices.contains(apolice))
							apolices.add(apolice);
					}
					
				}
			}
		}
		
		return apolices;
	}
	
	
}