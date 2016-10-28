package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.DadosReaseguro;
import com.gvs.crm.model.DadosReaseguroHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Reaseguradora;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class DadosReaseguroHomeImpl extends Home implements DadosReaseguroHome {
	public DadosReaseguro obterDadosReaseguro(ClassificacaoContas cContas,
			Apolice apolice, Reaseguradora reaseguradora, String tipoContrato)
			throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,dados_reaseguro,apolice where evento.id = dados_reaseguradora.id and superior = ? and secao = ? and reaseguradora = ? and tipo_contrato = ?");
		query.addLong(apolice.obterId());
		query.addLong(cContas.obterId());
		query.addLong(reaseguradora.obterId());
		query.addString(tipoContrato);

		DadosReaseguro dados = null;

		long id = query.executeAndGetFirstRow().getLong("id");

		if (id > 0) {
			EventoHome home = (EventoHome) this.getModelManager().getHome(
					"EventoHome");

			dados = (DadosReaseguro) home.obterEventoPorId(id);
		}

		return dados;
	}

	/*
	 * public Collection obterReaseguros(Aseguradora aseguradora, Date
	 * dataInicio, Date dataFim) throws Exception { Map reaseguros = new
	 * TreeMap();
	 * 
	 * SQLQuery query = this.getModelManager().createSQLQuery("crm", "select
	 * reaseguradora, tipo_contrato from evento, dados_reaseguro where evento.id =
	 * dados_reaseguro.id and origem = ? and data_prevista_inicio >= ? and
	 * data_prevista_conclusao <= ? group by reaseguradora,tipo_contrato");
	 * query.addLong(aseguradora.obterId());
	 * query.addLong(dataInicio.getTime()); query.addLong(dataFim.getTime());
	 * 
	 * System.out.println("select reaseguradora, tipo_contrato from evento,
	 * dados_reaseguro where evento.id = dados_reaseguro.id and origem =
	 * "+aseguradora.obterId()+" and data_prevista_inicio >=
	 * "+dataInicio.getTime()+" and data_prevista_conclusao <=
	 * "+dataFim.getTime()+" group by reaseguradora,tipo_contrato");
	 * 
	 * SQLRow[] rows = query.execute();
	 * 
	 * EventoHome home = (EventoHome)
	 * this.getModelManager().getHome("EventoHome");
	 * 
	 * for(int i = 0 ; i < rows.length ; i++) { long id = rows[i].getLong("id");
	 * 
	 * Long dataInicio2 = new Long(rows[i].getLong("data_prevista_inicio") + i);
	 * 
	 * DadosReaseguro dados = (DadosReaseguro) home.obterEventoPorId(id);
	 * 
	 * reaseguros.put(dataInicio2, dados); }
	 * 
	 * return reaseguros.values(); }
	 */
	
	public Collection<String> obterDadosReaseguro(Aseguradora aseguradora, Date dataInicio, Date dataFim, String situacao, String tipoValor) throws Exception
	{
		Map<String,String> dados = new TreeMap<String,String>();
		Collection<Long> apolicesId = new ArrayList<Long>();
		Collection<Long> apolicesId2 = new ArrayList<Long>();
		
		String sql = "select evento.id, tipo_contrato,superior,";
		
		if(tipoValor.equals("valorPrima"))
			sql+="dados_reaseguro.prima_gs as valor";
		else if(tipoValor.equals("valorCapital"))
			sql+="dados_reaseguro.caiptal_gs as valor";
		else if(tipoValor.equals("valorComissao"))
			sql+="dados_reaseguro.comissao_gs as valor";
		
		sql += " from evento,dados_reaseguro,apolice where evento.id  = dados_reaseguro.id and superior = apolice.id and data_emissao>="+ dataInicio.getTime() + " and data_emissao<="+dataFim.getTime() + " and dados_reaseguro.situacao = 'Vigente'";
		
		if(aseguradora!=null)
			sql+=" and origem = " + aseguradora.obterId();
		if(!situacao.equals("0"))
			sql+=" and situacao_seguro = '" + situacao + "'";
		
		sql+=" order by tipo_contrato";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			long apoliceId = rows[i].getLong("superior");
			if(!apolicesId2.contains(apoliceId))
				apolicesId2.add(apoliceId);
			
			String tipoContrato = rows[i].getString("tipo_contrato");
			double valor = rows[i].getDouble("valor");
			
			//VERIFICA AS ANULAÇÕES PARA DIMINUIR DO VALOR DO DADO REASEGURO
			double valorParcial = 0;
			double valorTotal = 0;
			
			sql = "select tipo,";
			if(tipoValor.equals("valorPrima"))
				sql+=" prima_gs as valor";
			else if(tipoValor.equals("valorCapital"))
				sql+=" capital_gs as valor";
			else if(tipoValor.equals("valorComissao"))
				sql+=" comissao_gs as valor";
			
			//É MAIS RAPIDO QUE FAZER LEN(tipo)>0
			//sql+=" from evento,registro_anulacao where evento.id = registro_anulacao.id and superior = " + id + " and tipo IS NOT NULL";
			sql+=" from evento,registro_anulacao where evento.id = registro_anulacao.id and superior = " + id + " and (tipo = 'Parcial' or tipo='Total')";
			query = this.getModelManager().createSQLQuery("crm",sql);
			
			SQLRow[] rows2 = query.execute();
			
			//System.out.println(rows2.length + " " + id);
			
			boolean naoSomar = false;
			
			for(int j = 0 ; j < rows2.length ; j++)
			{
				String tipoAnulacao = rows2[j].getString("tipo");
				double valorAnulacao = rows2[j].getDouble("valor");
				if(tipoAnulacao.equals("Parcial"))
					valorParcial+=valorAnulacao;
				else
				{
					valorTotal+=valorAnulacao;
					naoSomar = true;
				}
			}
			
			if(dados.containsKey(tipoContrato))
			{
				if(rows2.length == 0)
				{
					String linha = dados.get(tipoContrato);
					String[] linhaSuja = linha.split(";");
					
					double valorMap = Double.valueOf(linhaSuja[1]);
					int qtde = Integer.valueOf(linhaSuja[2]);
					if(!apolicesId.contains(apoliceId))
						qtde++;
					
					valorMap+=valor;
					
					dados.put(tipoContrato, tipoContrato+";"+valorMap+";"+qtde);
				}
				else
				{
					if(valorParcial == 0 && valorTotal == 0)
					{
						String linha = dados.get(tipoContrato);
						String[] linhaSuja = linha.split(";");
						
						double valorMap = Double.valueOf(linhaSuja[1]);
						int qtde = Integer.valueOf(linhaSuja[2]);
						
						valorMap+=valor;
						//if(!apolicesId.contains(apoliceId) && !naoSomar)
						if(!apolicesId.contains(apoliceId) && !naoSomar)
							qtde++;
						
						dados.put(tipoContrato, tipoContrato+";"+valorMap+";"+qtde);
					}
					else
					{
						if(valorParcial > 0)
						{
							String linha = dados.get(tipoContrato);
							String[] linhaSuja = linha.split(";");
							
							double valorMap = Double.valueOf(linhaSuja[1]);
							int qtde = Integer.valueOf(linhaSuja[2]);
							
							valorMap-=valorParcial;
							//if(!apolicesId.contains(apoliceId) && valorTotal == 0)
							if(!apolicesId.contains(apoliceId) && valorTotal == 0)
								qtde++;
							
							dados.put(tipoContrato, tipoContrato+";"+valorMap+";"+qtde);
						}
						if(valorTotal > 0)
						{
							String linha = dados.get(tipoContrato);
							String[] linhaSuja = linha.split(";");
							
							double valorMap = Double.valueOf(linhaSuja[1]);
							int qtde = Integer.valueOf(linhaSuja[2]);
							
							valorMap-=valorTotal;
							//NÃO CONTAR A QTDE PQ É ANULACAO TOTAL
							
							dados.put(tipoContrato, tipoContrato+";"+valorMap+";"+qtde);
						}
					}
				}
			}
			else
				dados.put(tipoContrato, tipoContrato+";"+valor+";1");
			
			apolicesId.add(apoliceId);
		}
		
		System.out.println(apolicesId2.size());
		
		return dados.values();
	}
	
	public Collection<Apolice> obterApolicesDadosReaseguro(Aseguradora aseguradora, Date dataInicio, Date dataFim, String situacao, String tipoContrato) throws Exception
	{
		Collection<Apolice> apolices = new ArrayList<Apolice>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "select evento.id from evento,dados_reaseguro,apolice where evento.id  = dados_reaseguro.id and superior = apolice.id and data_emissao>="+ dataInicio.getTime() + " and data_emissao<="+dataFim.getTime() + " and dados_reaseguro.situacao = 'Vigente'";
		
		if(aseguradora!=null)
			sql+=" and origem = " + aseguradora.obterId();
		if(!situacao.equals("0"))
			sql+=" and situacao_seguro = '" + situacao + "'";
		
		sql+=" and tipo_contrato = '"+tipoContrato+"'";
		
		sql+=" order by data_emissao";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			sql = "select count(*) as qtde from evento,registro_anulacao where evento.id = registro_anulacao.id and superior = " + id + " and tipo = 'Total'";
			query = this.getModelManager().createSQLQuery("crm",sql);
			
			int qtde = query.executeAndGetFirstRow().getInt("qtde"); 
			//System.out.println(qtde);
			if(qtde == 0)
			{
				DadosReaseguro dado = (DadosReaseguro) home.obterEventoPorId(id);
				
				Apolice apolice = (Apolice) dado.obterSuperior();
				if(!apolices.contains(apolice))
					apolices.add(apolice);
			}
		}
		
		return apolices;
	}
}