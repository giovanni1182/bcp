package com.gvs.crm.model.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.gvs.crm.model.Corretora;
import com.gvs.crm.model.CorretoraHome;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Inscricao;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class CorretoraHomeImpl extends Home implements CorretoraHome
{
	public Collection<Corretora> obterCorretoras() throws Exception
	{
		Collection<Corretora> corretores = new ArrayList<Corretora>();
		
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","SELECT entidade.id FROM entidade,corretora where entidade.id = corretora.id order by nome");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Corretora c = (Corretora) home.obterEntidadePorId(id);
			
			corretores.add(c);
		}
		
		return corretores;
	}

	public Collection<String> obterCorretorasVigentes(Date data,boolean ci) throws Exception
	{
		Collection<Corretora> corretoras = this.obterCorretoras();
		Collection<String> retorno = new ArrayList<String>();
		Inscricao inscricao;
		
		for(Corretora c : corretoras)
		{
			//inscricao = c.obterUltimaInscricaoVigente();
			inscricao = c.obterUltimaInscricao();
			
			if(inscricao!=null)
			{
				Date dataValidade = inscricao.obterDataValidade();
				if(data.before(dataValidade))
				{
					String dataResolucao = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataResolucao());
					String numeroResolucao = inscricao.obterNumeroResolucao();
					String dataValidadeStr = new SimpleDateFormat("dd/MM/yyyy").format(dataValidade);
					String ramo = " - ";
					if(inscricao.obterRamo()!=null)
						ramo = inscricao.obterRamo();
					
					String ruc = c.obterRuc();
					if(ruc == null)
						ruc = "-";
					else
					{
						if(ruc.equals(""))
							ruc = "-";
					}
					
					if(ci)
					{
						if(!ruc.equals("-"))
							retorno.add(inscricao.obterInscricao() + ";" + c.obterNome() + ";" + dataResolucao + ";" + numeroResolucao + ";" + dataValidadeStr + ";" + ramo + ";"+ruc);
					}
					else
						retorno.add(inscricao.obterInscricao() + ";" + c.obterNome() + ";" + dataResolucao + ";" + numeroResolucao + ";" + dataValidadeStr + ";" + ramo + ";"+ruc);
				}
			}
			
		}
		
		return retorno;
	}
	
	public Collection<String> obterMaiores(int qtde, Date dataInicio, Date dataFim, String situacao, String tipoValor, double dolar, double euro, double real, double pesoArg, double pesoUru, double yen) throws Exception
	{
		Collection<String> dados = new ArrayList<String>();
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		String somaCapitalME = " + SUM(" +
				"CASE dados_reaseguro.tipo_moeda_capital_gs" +
				" WHEN 'Dólar USA' THEN dados_reaseguro.capital_me*" + dolar +
				" WHEN 'Euro' THEN dados_reaseguro.capital_me*" + euro +
				" WHEN 'Real' THEN dados_reaseguro.capital_me*" + real +
				" WHEN 'Peso Argentino' THEN dados_reaseguro.capital_me*" + pesoArg +
				" WHEN 'Peso Uruguayo' THEN dados_reaseguro.capital_me*" + pesoUru +
				" WHEN 'Yen' THEN dados_reaseguro.capital_me*" + yen +
				" WHEN 'Guaraní' THEN dados_reaseguro.capital_me"+
				" END)";
		
		String somaPrimaME = " + SUM(" +
				"CASE dados_reaseguro.tipo_moeda_prima_gs" +
				" WHEN 'Dólar USA' THEN dados_reaseguro.prima_me*" + dolar +
				" WHEN 'Euro' THEN dados_reaseguro.prima_me*" + euro +
				" WHEN 'Real' THEN dados_reaseguro.prima_me*" + real +
				" WHEN 'Peso Argentino' THEN dados_reaseguro.prima_me*" + pesoArg +
				" WHEN 'Peso Uruguayo' THEN dados_reaseguro.prima_me*" + pesoUru +
				" WHEN 'Yen' THEN dados_reaseguro.prima_me*" + yen +
				" WHEN 'Guaraní' THEN dados_reaseguro.prima_me"+
				" END)";
		
		String somaComissaoME = " + SUM(" +
				"CASE dados_reaseguro.tipo_moeda_comissao_gs" +
				" WHEN 'Dólar USA' THEN dados_reaseguro.comissao_me*" + dolar +
				" WHEN 'Euro' THEN dados_reaseguro.comissao_me*" + euro +
				" WHEN 'Real' THEN dados_reaseguro.comissao_me*" + real +
				" WHEN 'Peso Argentino' THEN dados_reaseguro.comissao_me*" + pesoArg +
				" WHEN 'Peso Uruguayo' THEN dados_reaseguro.comissao_me*" + pesoUru +
				" WHEN 'Yen' THEN dados_reaseguro.comissao_me*" + yen +
				" WHEN 'Guaraní' THEN dados_reaseguro.comissao_me"+
				" END)";
		
		String sql = "";
		
		sql = "select TOP "+qtde+" corretora, (SUM(dados_reaseguro.caiptal_gs)"+somaCapitalME + ") as capital, (SUM(dados_reaseguro.prima_gs)" + somaPrimaME + ") as prima, (SUM(dados_reaseguro.comissao_gs)" + somaComissaoME + ") as comissao from evento,apolice,dados_reaseguro where evento.id = dados_reaseguro.id and superior = apolice.id and corretora > 0";
		
		sql+=" and data_emissao>=" + dataInicio.getTime() + " and data_emissao<="+dataFim.getTime();
		
		if(!situacao.equals("0"))
			sql+=" and situacao_seguro = '" + situacao + "'";
		
		sql+=" group by corretora";
		
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
				long reaseguradoraId = rows[i].getLong("corretora");
				Entidade e = entidadeHome.obterEntidadePorId(reaseguradoraId);
				
				dados.add(e.obterNome()+";"+capital+";"+prima+";"+comissao);
			}
		}
		
		return dados;
	}
}
