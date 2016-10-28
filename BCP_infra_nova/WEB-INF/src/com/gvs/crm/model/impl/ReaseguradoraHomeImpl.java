package com.gvs.crm.model.impl;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Inscricao;
import com.gvs.crm.model.Reaseguradora;
import com.gvs.crm.model.ReaseguradoraHome;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class ReaseguradoraHomeImpl extends Home implements ReaseguradoraHome
{
	public Collection obterReaseguradoras() throws Exception
	{
		Map reaseguradoras = new TreeMap();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select id from entidade where classe='Reaseguradora'");

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
		{
			long id = rows[i].getLong("id");

			Reaseguradora reaseguradora = (Reaseguradora) home.obterEntidadePorId(id);

			reaseguradoras.put(reaseguradora.obterNome(), reaseguradora);
		}

		return reaseguradoras.values();
	}

	public Collection obterReaseguradorasPorDataResolucao(Date data)
			throws Exception {
		Map reaseguradoras = new TreeMap();

		for (Iterator i = this.obterReaseguradoras().iterator(); i.hasNext();) 
		{
			Reaseguradora reaseguradora = (Reaseguradora) i.next();

			for (Iterator j = reaseguradora.obterInscricoes().iterator(); j.hasNext();) 
			{
				Inscricao inscricao = (Inscricao) j.next();

				if (inscricao.obterSituacao().equals("Vigente")) 
				{
					String mesResolucao = new SimpleDateFormat("MM").format(inscricao.obterDataResolucao());
					String anoResolucao = new SimpleDateFormat("yyyy").format(inscricao.obterDataResolucao());
					
					String mesValidade = new SimpleDateFormat("MM").format(inscricao.obterDataValidade());
					String anoValidade = new SimpleDateFormat("yyyy").format(inscricao.obterDataValidade());
					
					Date dataResolucao = new SimpleDateFormat("MM/yyyy").parse(mesResolucao + "/" + anoResolucao);
					Date dataValidade = new SimpleDateFormat("MM/yyyy").parse(mesValidade + "/" + anoValidade);
					
					if((data.after(dataResolucao) || data.equals(dataResolucao)) && (data.before(dataValidade) || data.equals(dataValidade)))
					{
						if(reaseguradora.obterSigla()!=null)
							reaseguradoras.put(reaseguradora.obterSigla(),reaseguradora);
						else
							reaseguradoras.put(reaseguradora.obterNome(),reaseguradora);
					}
				}
			}
		}

		return reaseguradoras.values();
	}
	
	public Collection obterReaseguradorasVigentes(Date data) throws Exception
	{
		Map lista = new TreeMap();
		
		for(Iterator i = this.obterReaseguradoras().iterator() ; i.hasNext() ; )
		{
			Reaseguradora r = (Reaseguradora) i.next();
			
			String pais = r.obterAtributo("nome").obterValor();
			
			if(pais!=null && !pais.equals(""))
			{
				if(!pais.toLowerCase().equals("paraguay") || !pais.toLowerCase().equals("paraguai"))
				{
					if(r.obterUltimaInscricao()!=null)
					{
						Inscricao inscricao = r.obterUltimaInscricao();
						
						if(data.before(inscricao.obterDataValidade()))
						{
							String dataResolucao = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataResolucao());
							String numeroResolucao = inscricao.obterNumeroResolucao();
							String dataValidade = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataValidade());
							
							String ramo = "";
							
							if(inscricao.obterRamo()!=null)
								ramo = inscricao.obterRamo();
							
							lista.put(new Integer(inscricao.obterInscricao()),inscricao.obterInscricao() + ";" + r.obterNome() + ";" + dataResolucao + ";" + numeroResolucao + ";" + dataValidade + ";" + pais + ";" + ramo + ";" + inscricao.obterCesion());
						}
					}
				}
			}
		}
		
		return lista.values();
	}
	
	public Collection<Reaseguradora> obterReaseguradorasVigentes2(Date data) throws Exception
	{
		Map<String, Reaseguradora> lista = new TreeMap<String, Reaseguradora>();
		
		for(Iterator i = this.obterReaseguradoras().iterator() ; i.hasNext() ; )
		{
			Reaseguradora r = (Reaseguradora) i.next();
			
			if(r.obterUltimaInscricao()!=null)
			{
				Inscricao inscricao = r.obterUltimaInscricao();
				
				if(data.compareTo(inscricao.obterDataValidade())<=0)
				{
					Reaseguradora reaseguradora = (Reaseguradora) inscricao.obterOrigem();
					lista.put(reaseguradora.obterNome(), reaseguradora);
				}
			}
		}
		
		return lista.values();
	}
	
	public Collection<String> obterMaiores(Date dataInicio, Date dataFim, String situacao, String tipoValor, double dolar, double euro, double real, double pesoArg, double pesoUru, double yen, boolean porPais) throws Exception
	{
		Collection<String> dados = new ArrayList<String>();
		
		String somaCapitalME = " + SUM(" +
				"CASE dados_reaseguro.tipo_moeda_capital_gs" +
				" WHEN 'Dólar USA' THEN dados_reaseguro.capital_me*" + dolar +
				" WHEN 'Euro' THEN dados_reaseguro.capital_me*" + euro +
				" WHEN 'Real' THEN dados_reaseguro.capital_me*" + real +
				" WHEN 'Peso Arg' THEN dados_reaseguro.capital_me*" + pesoArg +
				" WHEN 'Peso Uru' THEN dados_reaseguro.capital_me*" + pesoUru +
				" WHEN 'Yen' THEN dados_reaseguro.capital_me*" + yen +
				" WHEN 'Guaraní' THEN dados_reaseguro.capital_me"+
				" END)";
		
		String somaPrimaME = " + SUM(" +
				"CASE dados_reaseguro.tipo_moeda_prima_gs" +
				" WHEN 'Dólar USA' THEN dados_reaseguro.prima_me*" + dolar +
				" WHEN 'Euro' THEN dados_reaseguro.prima_me*" + euro +
				" WHEN 'Real' THEN dados_reaseguro.prima_me*" + real +
				" WHEN 'Peso Arg' THEN dados_reaseguro.prima_me*" + pesoArg +
				" WHEN 'Peso Uru' THEN dados_reaseguro.prima_me*" + pesoUru +
				" WHEN 'Yen' THEN dados_reaseguro.prima_me*" + yen +
				" WHEN 'Guaraní' THEN dados_reaseguro.prima_me"+
				" END)";
		
		String somaComissaoME = " + SUM(" +
				"CASE dados_reaseguro.tipo_moeda_comissao_gs" +
				" WHEN 'Dólar USA' THEN dados_reaseguro.comissao_me*" + dolar +
				" WHEN 'Euro' THEN dados_reaseguro.comissao_me*" + euro +
				" WHEN 'Real' THEN dados_reaseguro.comissao_me*" + real +
				" WHEN 'Peso Arg' THEN dados_reaseguro.comissao_me*" + pesoArg +
				" WHEN 'Peso Uru' THEN dados_reaseguro.comissao_me*" + pesoUru +
				" WHEN 'Yen' THEN dados_reaseguro.comissao_me*" + yen +
				" WHEN 'Guaraní' THEN dados_reaseguro.comissao_me"+
				" END)";
		
		String sql = "";
		
		if(!porPais)
		{
			sql = "select entidade.nome, (SUM(dados_reaseguro.caiptal_gs)"+somaCapitalME + ") as capital, (SUM(dados_reaseguro.prima_gs)" + somaPrimaME + ") as prima, (SUM(dados_reaseguro.comissao_gs)" + somaComissaoME + ") as comissao from evento,apolice,dados_reaseguro,entidade,plano where evento.id = dados_reaseguro.id and evento.superior = apolice.id and apolice.plano = plano.id and entidade.id = reaseguradora";
			
			sql+=" and data_emissao>=" + dataInicio.getTime() + " and data_emissao<="+dataFim.getTime();
			
			if(!situacao.equals("0"))
				sql+=" and situacao_seguro = '" + situacao + "'";
			
			//sql+=" group by reaseguradora";
			sql+=" group by entidade.nome, entidade.nome WITH ROLLUP";
		}
		else
		{
			sql = "select cast(entidade_atributo.valor as varchar(300)) as nacionalidade, (SUM(dados_reaseguro.caiptal_gs)"+somaCapitalME + ") as capital, (SUM(dados_reaseguro.prima_gs)" + somaPrimaME + ") as prima, (SUM(dados_reaseguro.comissao_gs)" + somaComissaoME + ") as comissao from evento,apolice,dados_reaseguro,entidade_atributo,plano where evento.id = dados_reaseguro.id and evento.superior = apolice.id and apolice.plano = plano.id and reaseguradora > 0 and reaseguradora = entidade_atributo.entidade and (entidade_atributo.nome = 'nome' or entidade_atributo.nome = 'nacionalidade')";
			
			sql+=" and data_emissao>=" + dataInicio.getTime() + " and data_emissao<="+dataFim.getTime();
			
			if(!situacao.equals("0"))
				sql+=" and situacao_seguro = '" + situacao + "'";
			
			sql+=" group by cast(entidade_atributo.valor as varchar(300)), cast(entidade_atributo.valor as varchar(300)) WITH ROLLUP";
		}
		
		//sql+=" order by entidade.nome";
		
		if(tipoValor.equals("valorPrima"))
			sql+=" order by prima desc";
		else if(tipoValor.equals("valorCapital"))
			sql+=" order by capital desc";
		else
			sql+=" order by comissao desc";
		
		System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			double capital = rows[i].getDouble("capital");
			double prima = rows[i].getDouble("prima");
			double comissao = rows[i].getDouble("comissao");
			
			if(capital > 0 || prima > 0 || comissao > 0)
			{
				if(!porPais)
				{
					String nome = rows[i].getString("nome");
					dados.add(nome+";"+capital+";"+prima+";"+comissao);
					
				}
				else
				{
					String nacionalidade = rows[i].getString("nacionalidade");
					/*if(nacionalidade==null)
						nacionalidade = "Desconocido";*/
					
					dados.add(nacionalidade+";"+capital+";"+prima+";"+comissao);
				}
			}
		}
		
		return dados;
	}
	
	public Collection<String> obterMaioresPorSecao(Date dataInicio, Date dataFim, String situacao, String tipoValor, double dolar, double euro, double real, double pesoArg, double pesoUru, double yen, String secao, String modalidade) throws Exception
	{
		Collection<String> dados = new ArrayList<String>();
		
		String somaCapitalME = " + SUM(" +
				"CASE dados_reaseguro.tipo_moeda_capital_gs" +
				" WHEN 'Dólar USA' THEN dados_reaseguro.capital_me*" + dolar +
				" WHEN 'Euro' THEN dados_reaseguro.capital_me*" + euro +
				" WHEN 'Real' THEN dados_reaseguro.capital_me*" + real +
				" WHEN 'Peso Arg' THEN dados_reaseguro.capital_me*" + pesoArg +
				" WHEN 'Peso Uru' THEN dados_reaseguro.capital_me*" + pesoUru +
				" WHEN 'Yen' THEN dados_reaseguro.capital_me*" + yen +
				" WHEN 'Guaraní' THEN dados_reaseguro.capital_me"+
				" END)";
		
		String somaPrimaME = " + SUM(" +
				"CASE dados_reaseguro.tipo_moeda_prima_gs" +
				" WHEN 'Dólar USA' THEN dados_reaseguro.prima_me*" + dolar +
				" WHEN 'Euro' THEN dados_reaseguro.prima_me*" + euro +
				" WHEN 'Real' THEN dados_reaseguro.prima_me*" + real +
				" WHEN 'Peso Arg' THEN dados_reaseguro.prima_me*" + pesoArg +
				" WHEN 'Peso Uru' THEN dados_reaseguro.prima_me*" + pesoUru +
				" WHEN 'Yen' THEN dados_reaseguro.prima_me*" + yen +
				" WHEN 'Guaraní' THEN dados_reaseguro.prima_me"+
				" END)";
		
		String somaComissaoME = " + SUM(" +
				"CASE dados_reaseguro.tipo_moeda_comissao_gs" +
				" WHEN 'Dólar USA' THEN dados_reaseguro.comissao_me*" + dolar +
				" WHEN 'Euro' THEN dados_reaseguro.comissao_me*" + euro +
				" WHEN 'Real' THEN dados_reaseguro.comissao_me*" + real +
				" WHEN 'Peso Arg' THEN dados_reaseguro.comissao_me*" + pesoArg +
				" WHEN 'Peso Uru' THEN dados_reaseguro.comissao_me*" + pesoUru +
				" WHEN 'Yen' THEN dados_reaseguro.comissao_me*" + yen +
				" WHEN 'Guaraní' THEN dados_reaseguro.comissao_me"+
				" END)";
		
		String sql = "";
		
		sql = "select plano.secao, (SUM(dados_reaseguro.caiptal_gs)"+somaCapitalME + ") as capital, (SUM(dados_reaseguro.prima_gs)" + somaPrimaME + ") as prima, (SUM(dados_reaseguro.comissao_gs)" + somaComissaoME + ") as comissao from evento,apolice,dados_reaseguro,plano where evento.id = dados_reaseguro.id and evento.superior = apolice.id and apolice.plano = plano.id and reaseguradora > 0";
		
		if(!secao.equals(""))
			sql+=" and plano.secao = '" + secao + "'";
		if(!modalidade.equals(""))
			sql+=" and plano.plano = '" + modalidade + "'";
		
		sql+=" and data_emissao>=" + dataInicio.getTime() + " and data_emissao<="+dataFim.getTime();
		
		if(!situacao.equals("0"))
			sql+=" and situacao_seguro = '" + situacao + "'";
		
		sql+=" group by plano.secao, plano.secao WITH ROLLUP";
		
		if(tipoValor.equals("valorPrima"))
			sql+=" order by prima desc";
		else if(tipoValor.equals("valorCapital"))
			sql+=" order by capital desc";
		else
			sql+=" order by comissao desc";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			double capital = rows[i].getDouble("capital");
			double prima = rows[i].getDouble("prima");
			double comissao = rows[i].getDouble("comissao");
			
			if(capital > 0 || prima > 0 || comissao > 0)
			{
				String secao2 = rows[i].getString("secao");
				
				dados.add(secao2+";"+capital+";"+prima+";"+comissao);
			}
		}
		
		//Com plano RG 001
		
		/*sql = "select (SUM(dados_reaseguro.caiptal_gs)"+somaCapitalME + ") as capital, (SUM(dados_reaseguro.prima_gs)" + somaPrimaME + ") as prima, (SUM(dados_reaseguro.comissao_gs)" + somaComissaoME + ") as comissao from evento,apolice,dados_reaseguro where evento.id = dados_reaseguro.id and superior = apolice.id and apolice.plano = 0 and reaseguradora > 0";
		
		sql+=" and data_emissao>=" + dataInicio.getTime() + " and data_emissao<="+dataFim.getTime();
		
		if(!situacao.equals("0"))
			sql+=" and situacao_seguro = '" + situacao + "'";
		
		if(tipoValor.equals("valorPrima"))
			sql+=" order by prima desc";
		else if(tipoValor.equals("valorCapital"))
			sql+=" order by capital desc";
		else
			sql+=" order by comissao desc";
		
		System.out.println(sql);
		
		query = this.getModelManager().createSQLQuery("crm", sql);
		
		rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			double capital = rows[i].getDouble("capital");
			double prima = rows[i].getDouble("prima");
			double comissao = rows[i].getDouble("comissao");
			
			if(capital > 0 || prima > 0 || comissao > 0)
			{
				String secao2 = "RG.001";
				
				dados.add(secao2+";"+capital+";"+prima+";"+comissao);
			}
		}*/
		
		//this.teste(dataInicio, dataFim, situacao, tipoValor, dolar, euro, real, pesoArg, pesoUru, yen, secao, modalidade);
		
		
		return dados;
	}
	
	private void teste(Date dataInicio, Date dataFim, String situacao, String tipoValor, double dolar, double euro, double real, double pesoArg, double pesoUru, double yen, String secao, String modalidade) throws Exception
	{
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");
		
		//Collection<String> dados = new ArrayList<String>();
		FileWriter writer = new FileWriter(new File("c:/tmp/teste.txt"));
		
		String somaCapitalME = " + SUM(" +
				"CASE dados_reaseguro.tipo_moeda_capital_gs" +
				" WHEN 'Dólar USA' THEN dados_reaseguro.capital_me*" + dolar +
				" WHEN 'Euro' THEN dados_reaseguro.capital_me*" + euro +
				" WHEN 'Real' THEN dados_reaseguro.capital_me*" + real +
				" WHEN 'Peso Arg' THEN dados_reaseguro.capital_me*" + pesoArg +
				" WHEN 'Peso Uru' THEN dados_reaseguro.capital_me*" + pesoUru +
				" WHEN 'Yen' THEN dados_reaseguro.capital_me*" + yen +
				" WHEN 'Guaraní' THEN dados_reaseguro.capital_me"+
				" END)";
		
		String somaPrimaME = " + SUM(" +
				"CASE dados_reaseguro.tipo_moeda_prima_gs" +
				" WHEN 'Dólar USA' THEN dados_reaseguro.prima_me*" + dolar +
				" WHEN 'Euro' THEN dados_reaseguro.prima_me*" + euro +
				" WHEN 'Real' THEN dados_reaseguro.prima_me*" + real +
				" WHEN 'Peso Arg' THEN dados_reaseguro.prima_me*" + pesoArg +
				" WHEN 'Peso Uru' THEN dados_reaseguro.prima_me*" + pesoUru +
				" WHEN 'Yen' THEN dados_reaseguro.prima_me*" + yen +
				" WHEN 'Guaraní' THEN dados_reaseguro.prima_me"+
				" END)";
		
		String somaComissaoME = " + SUM(" +
				"CASE dados_reaseguro.tipo_moeda_comissao_gs" +
				" WHEN 'Dólar USA' THEN dados_reaseguro.comissao_me*" + dolar +
				" WHEN 'Euro' THEN dados_reaseguro.comissao_me*" + euro +
				" WHEN 'Real' THEN dados_reaseguro.comissao_me*" + real +
				" WHEN 'Peso Arg' THEN dados_reaseguro.comissao_me*" + pesoArg +
				" WHEN 'Peso Uru' THEN dados_reaseguro.comissao_me*" + pesoUru +
				" WHEN 'Yen' THEN dados_reaseguro.comissao_me*" + yen +
				" WHEN 'Guaraní' THEN dados_reaseguro.comissao_me"+
				" END)";
		
		String sql = "";
		
		sql = "select entidade.nome, plano.secao, (SUM(dados_reaseguro.caiptal_gs)"+somaCapitalME + ") as capital, (SUM(dados_reaseguro.prima_gs)" + somaPrimaME + ") as prima, (SUM(dados_reaseguro.comissao_gs)" + somaComissaoME + ") as comissao from evento,apolice,dados_reaseguro,plano,entidade where evento.id = dados_reaseguro.id and evento.superior = apolice.id and apolice.plano = plano.id and reaseguradora = entidade.id";
		
		if(!secao.equals(""))
			sql+=" and plano.secao = '" + secao + "'";
		if(!modalidade.equals(""))
			sql+=" and plano.plano = '" + modalidade + "'";
		
		sql+=" and data_emissao>=" + dataInicio.getTime() + " and data_emissao<="+dataFim.getTime();
		
		if(!situacao.equals("0"))
			sql+=" and situacao_seguro = '" + situacao + "'";
		
		sql+=" group by plano.secao,entidade.nome";
		
		//sql+=" order by entidade.nome";
		
		if(tipoValor.equals("valorPrima"))
			sql+=" order by prima desc";
		else if(tipoValor.equals("valorCapital"))
			sql+=" order by capital desc";
		else
			sql+=" order by comissao desc";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", sql);
		
		SQLRow[] rows = query.execute();
		
		Map<String, Double> dados = new TreeMap<String, Double>();
		Map<String, Double> dados2 = new TreeMap<String, Double>();
		Map<String, Double> dados3 = new TreeMap<String, Double>();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			double capital = rows[i].getDouble("capital");
			double prima = rows[i].getDouble("prima");
			double comissao = rows[i].getDouble("comissao");
			
			//System.out.println(capital);
			
			if(capital > 0 || prima > 0 || comissao > 0)
			{
				String secao2 = rows[i].getString("secao");
				String nome = rows[i].getString("nome");
				
				if(dados.containsKey(nome))
				{
					double valor2 = dados.get(nome);
					valor2+=capital;
					dados.put(nome, valor2);
					
					valor2 = dados2.get(nome);
					valor2+=prima;
					dados2.put(nome, valor2);
					
					valor2 = dados3.get(nome);
					valor2+=comissao;
					dados3.put(nome, valor2);
				}
				else
				{
					dados.put(nome, capital);
					dados2.put(nome, prima);
					dados3.put(nome, comissao);
				}
				
				//writer.write(nome+";"+secao2+";"+formataValor.format(capital)+";"+formataValor.format(prima)+";"+formataValor.format(comissao)+"\n");
				
				//dados.add(secao2+";"+capital+";"+prima+";"+comissao);
			}
		}
		
		for(Iterator<String> i = dados.keySet().iterator() ; i.hasNext() ; )
		{
			String nome = i.next();
			
			double valor = dados.get(nome);
			double valor2 = dados2.get(nome);
			double valor3 = dados3.get(nome);
			
			//writer.write(nome+"        "+formataValor.format(valor)+"\n");
			//System.out.println(nome+"  "+formataValor.format(valor2) + "  "+ formataValor.format(valor)+ "  "+ formataValor.format(valor3));
		}
		
		//Com plano RG 001
		
		/*sql = "select (SUM(dados_reaseguro.caiptal_gs)"+somaCapitalME + ") as capital, (SUM(dados_reaseguro.prima_gs)" + somaPrimaME + ") as prima, (SUM(dados_reaseguro.comissao_gs)" + somaComissaoME + ") as comissao from evento,apolice,dados_reaseguro where evento.id = dados_reaseguro.id and superior = apolice.id and apolice.plano = 0 and reaseguradora > 0";
		
		sql+=" and data_emissao>=" + dataInicio.getTime() + " and data_emissao<="+dataFim.getTime();
		
		if(!situacao.equals("0"))
			sql+=" and situacao_seguro = '" + situacao + "'";
		
		if(tipoValor.equals("valorPrima"))
			sql+=" order by prima desc";
		else if(tipoValor.equals("valorCapital"))
			sql+=" order by capital desc";
		else
			sql+=" order by comissao desc";
		
		//System.out.println(sql);
		
		query = this.getModelManager().createSQLQuery("crm", sql);
		
		rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			double capital = rows[i].getDouble("capital");
			double prima = rows[i].getDouble("prima");
			double comissao = rows[i].getDouble("comissao");
			
			if(capital > 0 || prima > 0 || comissao > 0)
			{
				String secao2 = "RG.001";
				
				dados.add(secao2+";"+capital+";"+prima+";"+comissao);
			}
		}*/
		
		writer.close();
		
	}
}