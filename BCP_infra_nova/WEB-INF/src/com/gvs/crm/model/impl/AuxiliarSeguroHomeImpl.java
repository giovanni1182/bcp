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
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.AuxiliarSeguroHome;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Entidade.Contato;
import com.gvs.crm.model.Entidade.Endereco;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Inscricao;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class AuxiliarSeguroHomeImpl extends Home implements AuxiliarSeguroHome {

	public Collection obterAuxiliares() throws Exception {
		Collection auxiliares = new ArrayList();

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select entidade.id, nome from entidade,auxiliar_seguro where entidade.id = auxiliar_seguro.id order by nome ASC");

		SQLRow[] rows = query.execute();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");

			AuxiliarSeguro auxiliar = (AuxiliarSeguro) home
					.obterEntidadePorId(id);

			auxiliares.add(auxiliar);
		}

		return auxiliares;
	}

	public Collection obterAuxiliaresPorDataResolucao(Date data)
			throws Exception {
		Map auxiliares = new TreeMap();

		for (Iterator i = this.obterAuxiliares().iterator(); i.hasNext();) {
			AuxiliarSeguro auxiliar = (AuxiliarSeguro) i.next();

			if (!auxiliares.containsKey(auxiliar.obterNome())) {
				for (Iterator j = auxiliar.obterInscricoes().iterator(); j
						.hasNext();) {
					Inscricao inscricao = (Inscricao) j.next();

					if (inscricao.obterDataResolucao() != null
							&& inscricao.obterDataValidade() != null) {
						/*
						 * String dataStr = new
						 * SimpleDateFormat("dd/MM/yyyy").format(data); String
						 * dataResolucaoStr = new
						 * SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataResolucao());
						 * String dataValidadeStr = new
						 * SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataValidade());
						 * 
						 * System.out.println(dataStr + " > " + dataResolucaoStr + " - " +
						 * dataStr +" < " + dataValidadeStr);
						 *  
						 */if (data.after(inscricao.obterDataResolucao())
								&& data.before(inscricao.obterDataValidade()))
							auxiliares.put(auxiliar.obterNome(), auxiliar);
					}
				}
			}
		}

		return auxiliares.values();
	}

	public Collection obterAuxiliaresCorredoresDeSegurosPorDataResolucao(
			Date data) throws Exception {

		Map auxiliares = new TreeMap();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select entidade.id from entidade,entidade_atributo where entidade.id = entidade_atributo.entidade and entidade_atributo.nome='atividade' and valor like '%Corredores de Seguros%'");
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) 
		{
			long id = rows[i].getLong("id");

			AuxiliarSeguro auxiliar = (AuxiliarSeguro) home.obterEntidadePorId(id);

			for (Iterator j = auxiliar.obterInscricoes().iterator(); j.hasNext();) 
			{
				Inscricao inscricao = (Inscricao) j.next();

				if (inscricao.obterSituacao().equals("Vigente")) 
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
							if(auxiliar.obterSigla()!=null)
								auxiliares.put(auxiliar.obterSigla(), auxiliar);
							else
								auxiliares.put(auxiliar.obterNome(), auxiliar);
						}
							
					}
				}
			}
		}

		/*
		 * Collection auxiliares = new ArrayList();
		 * 
		 * for(Iterator i =
		 * this.obterAuxiliaresPorDataResolucao(data).iterator() ; i.hasNext() ; ) {
		 * AuxiliarSeguro auxiliar = (AuxiliarSeguro) i.next();
		 * 
		 * AuxiliarSeguro.Atributo atividade = (AuxiliarSeguro.Atributo)
		 * auxiliar.obterAtributo("atividade");
		 * 
		 * if(atividade!=null) { if(atividade.obterValor().equals("Corredores de
		 * Seguros")) auxiliares.add(auxiliar); } }
		 */

		return auxiliares.values();
	}

	public Collection obterAuxiliaresLiquidadoresPorDataResolucao(Date data)
			throws Exception {
		Map auxiliares = new TreeMap();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select entidade.id from entidade,entidade_atributo where entidade.id = entidade_atributo.entidade and entidade_atributo.nome='atividade' and valor like '%Liquidadores de Siniestros%'");
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");

			AuxiliarSeguro auxiliar = (AuxiliarSeguro) home
					.obterEntidadePorId(id);

			for (Iterator j = auxiliar.obterInscricoes().iterator(); j
					.hasNext();) {
				Inscricao inscricao = (Inscricao) j.next();

				if (inscricao.obterSituacao().equals("Vigente")) {
					if (inscricao.obterDataResolucao() != null
							&& inscricao.obterDataValidade() != null) {

						if((data.after(inscricao.obterDataResolucao()) || data.equals(inscricao.obterDataResolucao()))	&& (data.before(inscricao.obterDataValidade()) || data.equals(inscricao.obterDataValidade())))
						{
							if(auxiliar.obterSigla()!=null)
								auxiliares.put(auxiliar.obterSigla(), auxiliar);
							else
								auxiliares.put(auxiliar.obterNome(), auxiliar);
						}
							
					}
				}
			}
		}

		/*
		 * Collection auxiliares = new ArrayList();
		 * 
		 * for(Iterator i =
		 * this.obterAuxiliaresPorDataResolucao(data).iterator() ; i.hasNext() ; ) {
		 * AuxiliarSeguro auxiliar = (AuxiliarSeguro) i.next();
		 * 
		 * AuxiliarSeguro.Atributo atividade = (AuxiliarSeguro.Atributo)
		 * auxiliar.obterAtributo("atividade");
		 * 
		 * if(atividade!=null) { if(atividade.obterValor().equals("Liquidadores
		 * de Siniestros")) auxiliares.add(auxiliar); } }
		 */

		return auxiliares.values();
	}

	public Collection obterAuxiliaresAuditoresPorDataResolucao(Date data)
			throws Exception {
		Map auxiliares = new TreeMap();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select entidade.id from entidade,entidade_atributo where entidade.id = entidade_atributo.entidade and entidade_atributo.nome='atividade' and valor like '%Auditores Externos%'");
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");

			AuxiliarSeguro auxiliar = (AuxiliarSeguro) home
					.obterEntidadePorId(id);

			for (Iterator j = auxiliar.obterInscricoes().iterator(); j
					.hasNext();) {
				Inscricao inscricao = (Inscricao) j.next();

				if (inscricao.obterSituacao().equals("Vigente")) {
					if (inscricao.obterDataResolucao() != null
							&& inscricao.obterDataValidade() != null) {

						if (data.after(inscricao.obterDataResolucao())
								&& data.before(inscricao.obterDataValidade()))
							auxiliares.put(auxiliar.obterNome(), auxiliar);
					}
				}
			}
		}

		/*
		 * for(Iterator i =
		 * this.obterAuxiliaresPorDataResolucao(data).iterator() ; i.hasNext() ; ) {
		 * AuxiliarSeguro auxiliar = (AuxiliarSeguro) i.next();
		 * 
		 * AuxiliarSeguro.Atributo atividade = (AuxiliarSeguro.Atributo)
		 * auxiliar.obterAtributo("atividade");
		 * 
		 * if(atividade!=null) { if(atividade.obterValor().equals("Auditores
		 * Externos")) auxiliares.add(auxiliar); } }
		 */

		return auxiliares.values();
	}

	public Collection obterAuxiliaresAgentesDeSegurosPorDataResolucao(Date data)
			throws Exception {
		Map auxiliares = new TreeMap();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select entidade.id from entidade,entidade_atributo where entidade.id = entidade_atributo.entidade and entidade_atributo.nome='atividade' and valor like '%Agentes de Seguros%'");
		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) 
		{
			long id = rows[i].getLong("id");

			AuxiliarSeguro auxiliar = (AuxiliarSeguro) home.obterEntidadePorId(id);

			for (Iterator j = auxiliar.obterInscricoes().iterator(); j.hasNext();) 
			{
				Inscricao inscricao = (Inscricao) j.next();

				if (inscricao.obterSituacao().equals("Vigente")) 
				{
					if (inscricao.obterDataResolucao() != null && inscricao.obterDataValidade() != null) 
					{
						String mesResolucao = new SimpleDateFormat("MM").format(inscricao.obterDataResolucao());
						String anoResolucao = new SimpleDateFormat("yyyy").format(inscricao.obterDataResolucao());
						
						String mesValidade = new SimpleDateFormat("MM").format(inscricao.obterDataValidade());
						String anoValidade = new SimpleDateFormat("yyyy").format(inscricao.obterDataValidade());
						
						Date dataResolucao = new SimpleDateFormat("MM/yyyy").parse(mesResolucao + "/" + anoResolucao);
						Date dataValidade = new SimpleDateFormat("MM/yyyy").parse(mesValidade + "/" + anoValidade);
						
						if((data.after(dataResolucao) || data.equals(dataResolucao)) && (data.before(dataValidade) || data.equals(dataValidade)))
						{
							if(auxiliar.obterSigla()!=null)
								auxiliares.put(auxiliar.obterSigla(), auxiliar);
							else
								auxiliares.put(auxiliar.obterNome(), auxiliar);
						}
							
					}
				}
			}
		}

		/*
		 * for(Iterator i =
		 * this.obterAuxiliaresPorDataResolucao(data).iterator() ; i.hasNext() ; ) {
		 * AuxiliarSeguro auxiliar = (AuxiliarSeguro) i.next();
		 * 
		 * AuxiliarSeguro.Atributo atividade = (AuxiliarSeguro.Atributo)
		 * auxiliar.obterAtributo("atividade");
		 * 
		 * if(atividade!=null) { if(atividade.obterValor().equals("Agentes de
		 * Seguros")) auxiliares.add(auxiliar); } }
		 */

		return auxiliares.values();
	}
	
	public Collection obterLiquidadoresSinistroVigente(Date data, boolean ci) throws Exception
	{
		Map lista = new TreeMap();
		
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		String sql = "select entidade.id from entidade,auxiliar_seguro_ramo,entidade_atributo where entidade.id = auxiliar_seguro_ramo.entidade and auxiliar_seguro_ramo.entidade = entidade_atributo.entidade and entidade_atributo.nome='atividade' and valor like '%Liquidadores de Siniestros%'";
		
		if(ci)
			sql+=" and LEN(ruc) > 0";
		
		sql+=" group by entidade.id";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			AuxiliarSeguro aux = (AuxiliarSeguro) home.obterEntidadePorId(id);
			
			if(aux.obterUltimaInscricao()!=null)
			{
				Inscricao inscricao = aux.obterUltimaInscricao();
				
				if(data.before(inscricao.obterDataValidade()))
				{
					String dataResolucao = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataResolucao());
					String numeroResolucao = inscricao.obterNumeroResolucao();
					String dataValidade = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataValidade());
					
					String ruc = aux.obterRuc();
					if(ruc == null)
						ruc = "-";
					else
					{
						if(ruc.equals(""))
							ruc = "-";
					}
					
					String telefone = aux.obterContato("Teléfono Comercial");
					if(telefone==null)
						telefone = " ";
					
					String enderecoStr = " ";
					String cidade = " ";
					
					Entidade.Endereco endereco = aux.obterEndereco("Dirección Comercial");
					if(endereco!=null)
					{
						enderecoStr=endereco.obterRua().trim();
						enderecoStr+=", " + endereco.obterNumero();
						
						String bairro = endereco.obterBairro();
						if(!bairro.equals(""))
							enderecoStr+=" - " + bairro;
						
						String cep = endereco.obterCep();
						if(!cep.equals(""))
							enderecoStr+=" - " + cep;
						
						cidade = endereco.obterCidade();
						
					}
					
					String dataEmissaoStr = " ";
					String dataVencimentoStr = " ";
					
					Date dataEmissao = inscricao.obterDataEmissao();
					if(dataEmissao!=null)
						dataEmissaoStr = new SimpleDateFormat("dd/MM/yyyy").format(dataEmissao);
					
					Date dataVencimento = inscricao.obterDataVencimento();
					if(dataVencimento!=null)
						dataVencimentoStr = new SimpleDateFormat("dd/MM/yyyy").format(dataVencimento);
					
					
					lista.put(aux.obterNome(),inscricao.obterInscricao() + ";" + aux.obterNome() + ";" + dataResolucao + ";" + numeroResolucao + ";" + dataValidade+";"+ruc+";"+telefone+";"+enderecoStr+";"+cidade+";"+dataEmissaoStr+";"+dataVencimentoStr);
				}
			}
		}
		
		return lista.values();
	}
	
	public Collection obterCorredoresSegurosVigente(Date data, boolean ci) throws Exception
	{
		Map lista = new TreeMap();
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		String sql = "select entidade.id from entidade,auxiliar_seguro_ramo,entidade_atributo where entidade.id = auxiliar_seguro_ramo.entidade and auxiliar_seguro_ramo.entidade = entidade_atributo.entidade and entidade_atributo.nome='atividade' and valor like '%Corredores de Seguros%'";
		
		if(ci)
			sql+=" and LEN(ruc) > 0";
		
		sql+=" group by entidade.id";
		
		System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			AuxiliarSeguro aux = (AuxiliarSeguro) home.obterEntidadePorId(id);
			
			if(aux.obterUltimaInscricao()!=null)
			{
				Inscricao inscricao = aux.obterUltimaInscricao();
				
				if(data.before(inscricao.obterDataValidade()))
				{
					String dataResolucao = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataResolucao());
					String numeroResolucao = inscricao.obterNumeroResolucao();
					String dataValidade = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataValidade());
					String enderecoStr = "";
					String fone = "";
					
					for(Iterator<Endereco> j = aux.obterEnderecos().iterator() ; j.hasNext() ; )
					{
						Endereco endereco = j.next();
						if(endereco.obterNome().indexOf("Comercial")>-1)
							enderecoStr = endereco.obterRua() + "," + endereco.obterNumero() + " " + endereco.obterComplemento() + " " + endereco.obterBairro() + " " + endereco.obterCep() + " " + endereco.obterCidade() + " " + endereco.obterEstado() + " " + endereco.obterPais(); 
					}
					
					for(Iterator<Contato> j = aux.obterContatos().iterator() ; j.hasNext() ; )
					{
						Contato contato = j.next();
						
						if(contato.obterNome().indexOf("Teléfono Comercial") >-1)
							fone = contato.obterValor(); 
					}
					
					if(enderecoStr.equals(""))
						enderecoStr = "-";
					if(fone.equals(""))
						fone = "-";
					
					String ruc = aux.obterRuc();
					if(ruc == null)
						ruc = "-";
					else
					{
						if(ruc.equals(""))
							ruc = "-";
					}
					
					lista.put(aux.obterNome(),inscricao.obterInscricao() + ";" + aux.obterNome() + ";" + dataResolucao + ";" + numeroResolucao + ";" + dataValidade + ";" + enderecoStr + ";" + fone + ";" + ruc);
					
					//System.out.println(aux.obterNome() + "; " + inscricao.obterInscricao() + ";" + aux.obterNome() + ";" + dataResolucao + ";" + numeroResolucao + ";" + dataValidade + ";" + enderecoStr + ";" + fone + ";" + ruc);
				}
			}
		}
		
		return lista.values();
	}
	
	public Collection obterAgentesSegurosVigente(Date data, boolean ci) throws Exception
	{
		Map<String,String> lista = new TreeMap<String,String>();
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		String sql = "select entidade.id from entidade,auxiliar_seguro_ramo,entidade_atributo where entidade.id = auxiliar_seguro_ramo.entidade and auxiliar_seguro_ramo.entidade = entidade_atributo.entidade and entidade_atributo.nome='atividade' and valor like '%Agentes de Seguros%' ";
		
		if(ci)
			sql+=" and LEN(ruc) > 0";
		
		sql+=" group by entidade.id";
				
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			AuxiliarSeguro aux = (AuxiliarSeguro) home.obterEntidadePorId(id);
			
			if(aux.obterUltimaInscricao()!=null)
			{
				Inscricao inscricao = aux.obterUltimaInscricao();
				
				if(data.before(inscricao.obterDataValidade()))
				{
					String dataResolucao = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataResolucao());
					String numeroResolucao = inscricao.obterNumeroResolucao();
					String dataValidade = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataValidade());
					String ramoStr = "";
					String enderecoStr = "";
					String fone = "";
					
					for(Iterator j = aux.obterRamos().iterator() ; j.hasNext() ; )
					{
						AuxiliarSeguro.Ramo ramo = (AuxiliarSeguro.Ramo) j.next();
						
						ramoStr = ramo.obterRamo();
					}
					
					for(Iterator<Endereco> j = aux.obterEnderecos().iterator() ; j.hasNext() ; )
					{
						Endereco endereco = j.next();
						
						if(endereco.obterNome().indexOf("Comercial")>-1)
							enderecoStr = endereco.obterRua() + "," + endereco.obterNumero() + " " + endereco.obterComplemento() + " " + endereco.obterBairro() + " " + endereco.obterCep() + " " + endereco.obterCidade() + " " + endereco.obterEstado() + " " + endereco.obterPais(); 
					}
					
					for(Iterator<Contato> j = aux.obterContatos().iterator() ; j.hasNext() ; )
					{
						Contato contato = j.next();
						
						if(contato.obterNome().indexOf("Comercial")>-1)
							fone = contato.obterValor(); 
					}
					
					if(!ramoStr.equals(""))
					{
						if(ramoStr.equals("Caución"))
							ramoStr = "C";
						else if(ramoStr.equals("Patrimonial") || ramoStr.equals("Patrimoniales"))
							ramoStr = "P";
						else if(ramoStr.equals("Vida"))
							ramoStr = "V";
						else if(ramoStr.equals("Patrimoniales y Vida"))
							ramoStr = "P y V";
						else
							ramoStr = "-";
							
					}
					else
						ramoStr = "-";
					
					if(enderecoStr.equals(""))
						enderecoStr = "-";
					if(fone.equals(""))
						fone = "-";
					
					String ruc = aux.obterRuc();
					if(ruc == null)
						ruc = "-";
					else
					{
						if(ruc.equals(""))
							ruc = "-";
					}
					
					lista.put(aux.obterNome() + i,inscricao.obterInscricao() + ";" + aux.obterNome() + ";" + dataResolucao + ";" + numeroResolucao + ";" + dataValidade + ";" + ramoStr + ";" + enderecoStr + ";" + fone + ";"+ruc);
				}
			}
		}
		
		return lista.values();
	}
	
	public Collection<Apolice> obterApolicesMaioresAgentesPorPrima(AuxiliarSeguro agente,Date dataInicio,Date dataFim,String situacao, boolean auxiliar) throws Exception
	{
		Collection<Apolice> apolices = new ArrayList<Apolice>();
		
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		 String	sql = "select evento.id,origem from evento,apolice,entidade where evento.id = apolice.id and origem = entidade.id and prima_gs > 0";
		
		 if(agente!=null)
		 {
			 if(auxiliar)
				 sql+=" and agente = " + agente.obterId();
			 else
				 sql+=" and corredor = " + agente.obterId(); 
		 }
			
		else
		{ 
			if(auxiliar)
				sql+=" and agente > 0";
			else
				sql+=" and corredor > 0";
		}
		
		if(!situacao.equals("0"))
			sql+=" and situacao_seguro='"+situacao+"'";
		
		sql+= " and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime();
		sql+= " ORDER BY entidade.nome,prima_gs";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Apolice apolice = (Apolice) eventoHome.obterEventoPorId(id);
			
			apolices.add(apolice);
				
		}
		return apolices;
	}
	
	public Collection<String> obterApolicesMaioresAgentesPorPrimaStr(AuxiliarSeguro agente,Date dataInicio,Date dataFim, int maxRegistros, String situacao, double monto, boolean auxiliar) throws Exception
	{
		Collection<String> dados = new ArrayList<String>();
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		String	sql = "";
		
		if(auxiliar)
		{
			if(!situacao.equals("0"))
				sql = "select TOP "+ maxRegistros + " SUM(prima_gs) as prima,situacao_seguro,agente from evento,apolice where evento.id = apolice.id";
			else
				sql = "select TOP "+ maxRegistros + " SUM(prima_gs) as prima,agente from evento,apolice where evento.id = apolice.id";
	
			if(agente!=null)
				sql+=" and agente = " + agente.obterId();
			else
				sql+=" and agente > 0";
			
			if(!situacao.equals("0"))
				sql+=" and situacao_seguro='"+situacao+"'";
			
			if(!situacao.equals("0"))
				sql+= " and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime() + " GROUP BY situacao_seguro,agente";
			else
				sql+= " and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime() + " GROUP BY agente";
			
			if(monto > 0)
				sql+=" HAVING SUM(prima_gs)>=" + monto;
			else
				sql+=" HAVING SUM(prima_gs)> 0 ";
			
			sql+= " ORDER BY prima DESC";
			
			//System.out.println(sql);
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("agente");
				String situacao2 = "";
				if(!situacao.equals("0"))
					situacao2 = rows[i].getString("situacao_seguro");
				else
					situacao2 = "Todas";
				
				double valor = rows[i].getDouble("prima");
				
				Entidade e = home.obterEntidadePorId(id);
				
				dados.add(e.obterNome()+";"+situacao2+";"+valor+";"+id);
			}
		}
		else
		{
			if(!situacao.equals("0"))
				sql = "select TOP "+ maxRegistros + " SUM(prima_gs) as prima,situacao_seguro,corredor from evento,apolice where evento.id = apolice.id";
			else
				sql = "select TOP "+ maxRegistros + " SUM(prima_gs) as prima,corredor from evento,apolice where evento.id = apolice.id";
	
			if(agente!=null)
				sql+=" and corredor = " + agente.obterId();
			else
				sql+=" and corredor > 0";
			
			if(!situacao.equals("0"))
				sql+=" and situacao_seguro='"+situacao+"'";
			
			if(!situacao.equals("0"))
				sql+= " and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime() + " GROUP BY situacao_seguro,corredor";
			else
				sql+= " and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime() + " GROUP BY corredor";
			
			if(monto > 0)
				sql+=" HAVING SUM(prima_gs)>=" + monto;
			else
				sql+=" HAVING SUM(prima_gs)> 0 ";
			
			sql+= " ORDER BY prima DESC";
			
			//System.out.println(sql);
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("corredor");
				String situacao2 = "";
				if(!situacao.equals("0"))
					situacao2 = rows[i].getString("situacao_seguro");
				else
					situacao2 = "Todas";
				
				double valor = rows[i].getDouble("prima");
				
				Entidade e = home.obterEntidadePorId(id);
				
				dados.add(e.obterNome()+";"+situacao2+";"+valor+";"+id);
			}
		}
		
		return dados;
	}
	
	
	
	public Collection<Apolice> obterApolicesMaioresAgentesPorCapital(AuxiliarSeguro agente,Date dataInicio,Date dataFim,String situacao, boolean auxiliar) throws Exception
	{
		Collection<Apolice> apolices = new ArrayList<Apolice>();
		
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		 String	sql = "select evento.id,origem from evento,apolice,entidade where evento.id = apolice.id and origem = entidade.id and capital_gs > 0";
		
		 if(agente!=null)
		 {
			 if(auxiliar)
				 sql+=" and agente = " + agente.obterId();
			 else
				 sql+=" and corredor = " + agente.obterId(); 
		 }
			
		else
		{ 
			if(auxiliar)
				sql+=" and agente > 0";
			else
				sql+=" and corredor > 0";
		}
		
		if(!situacao.equals("0"))
			sql+=" and situacao_seguro='"+situacao+"'";
		
		sql+= " and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime();
		sql+= " ORDER BY entidade.nome,capital_gs";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Apolice apolice = (Apolice) eventoHome.obterEventoPorId(id);
			
			apolices.add(apolice);
				
		}
		return apolices;
	}
	
	public Collection<String> obterApolicesMaioresAgentesPorCapitalStr(AuxiliarSeguro agente,Date dataInicio,Date dataFim, int maxRegistros, String situacao, double monto, boolean auxiliar) throws Exception
	{
		Collection<String> dados = new ArrayList<String>();
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		String	sql = "";
		
		if(auxiliar)
		{
			if(!situacao.equals("0"))
				sql = "select TOP "+ maxRegistros + " SUM(capital_gs) as prima,situacao_seguro,agente from evento,apolice where evento.id = apolice.id";
			else
				sql = "select TOP "+ maxRegistros + " SUM(capital_gs) as prima,agente from evento,apolice where evento.id = apolice.id";
	
			if(agente!=null)
				sql+=" and agente = " + agente.obterId();
			else
				sql+=" and agente > 0";
			
			if(!situacao.equals("0"))
				sql+=" and situacao_seguro='"+situacao+"'";
			
			if(!situacao.equals("0"))
				sql+= " and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime() + " GROUP BY situacao_seguro,agente";
			else
				sql+= " and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime() + " GROUP BY agente";
			
			if(monto > 0)
				sql+=" HAVING SUM(capital_gs)>=" + monto;
			else
				sql+=" HAVING SUM(capital_gs)> 0 ";
			
			sql+= " ORDER BY prima DESC";
			
			//System.out.println(sql);
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("agente");
				String situacao2 = "";
				if(!situacao.equals("0"))
					situacao2 = rows[i].getString("situacao_seguro");
				else
					situacao2 = "Todas";
				
				double valor = rows[i].getDouble("prima");
				
				Entidade e = home.obterEntidadePorId(id);
				
				dados.add(e.obterNome()+";"+situacao2+";"+valor+";"+id);
			}
		}
		else
		{
			if(!situacao.equals("0"))
				sql = "select TOP "+ maxRegistros + " SUM(capital_gs) as prima,situacao_seguro,corredor from evento,apolice where evento.id = apolice.id";
			else
				sql = "select TOP "+ maxRegistros + " SUM(capital_gs) as prima,corredor from evento,apolice where evento.id = apolice.id";
	
			if(agente!=null)
				sql+=" and corredor = " + agente.obterId();
			else
				sql+=" and corredor > 0";
			
			if(!situacao.equals("0"))
				sql+=" and situacao_seguro='"+situacao+"'";
			
			if(!situacao.equals("0"))
				sql+= " and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime() + " GROUP BY situacao_seguro,corredor";
			else
				sql+= " and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime() + " GROUP BY corredor";
			
			if(monto > 0)
				sql+=" HAVING SUM(capital_gs)>=" + monto;
			else
				sql+=" HAVING SUM(capital_gs)> 0 ";
			
			sql+= " ORDER BY prima DESC";
			
			//System.out.println(sql);
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("corredor");
				String situacao2 = "";
				if(!situacao.equals("0"))
					situacao2 = rows[i].getString("situacao_seguro");
				else
					situacao2 = "Todas";
				
				double valor = rows[i].getDouble("prima");
				
				Entidade e = home.obterEntidadePorId(id);
				
				dados.add(e.obterNome()+";"+situacao2+";"+valor+";"+id);
			}
		}
		
		return dados;
	}
	
	public Collection<Apolice> obterApolicesMaioresAgentesPorComissao(AuxiliarSeguro agente,Date dataInicio,Date dataFim,String situacao, boolean auxiliar) throws Exception
	{
		Collection<Apolice> apolices = new ArrayList<Apolice>();
		
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		 String	sql = "select evento.id,origem from evento,apolice,entidade where evento.id = apolice.id and origem = entidade.id and comissao_gs > 0";
		
		 if(agente!=null)
		 {
			 if(auxiliar)
				 sql+=" and agente = " + agente.obterId();
			 else
				 sql+=" and corredor = " + agente.obterId(); 
		 }
			
		else
		{ 
			if(auxiliar)
				sql+=" and agente > 0";
			else
				sql+=" and corredor > 0";
		}
		
		if(!situacao.equals("0"))
			sql+=" and situacao_seguro='"+situacao+"'";
		
		sql+= " and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime();
		sql+= " ORDER BY entidade.nome,comissao_gs";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Apolice apolice = (Apolice) eventoHome.obterEventoPorId(id);
			
			apolices.add(apolice);
				
		}
		return apolices;
	}
	
	public Collection<String> obterApolicesMaioresAgentesPorComissaoStr(AuxiliarSeguro agente,Date dataInicio,Date dataFim, int maxRegistros, String situacao, double monto, boolean auxiliar) throws Exception
	{
		Collection<String> dados = new ArrayList<String>();
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		String	sql = "";
		
		if(auxiliar)
		{
			if(!situacao.equals("0"))
				sql = "select TOP "+ maxRegistros + " SUM(comissao_gs) as prima,situacao_seguro,agente from evento,apolice where evento.id = apolice.id";
			else
				sql = "select TOP "+ maxRegistros + " SUM(comissao_gs) as prima,agente from evento,apolice where evento.id = apolice.id";
	
			if(agente!=null)
				sql+=" and agente = " + agente.obterId();
			else
				sql+=" and agente > 0";
			
			if(!situacao.equals("0"))
				sql+=" and situacao_seguro='"+situacao+"'";
			
			if(!situacao.equals("0"))
				sql+= " and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime() + " GROUP BY situacao_seguro,agente";
			else
				sql+= " and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime() + " GROUP BY agente";
			
			if(monto > 0)
				sql+=" HAVING SUM(comissao_gs)>=" + monto;
			else
				sql+=" HAVING SUM(comissao_gs)> 0 ";
			
			sql+= " ORDER BY prima DESC";
			
			//System.out.println(sql);
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("agente");
				String situacao2 = "";
				if(!situacao.equals("0"))
					situacao2 = rows[i].getString("situacao_seguro");
				else
					situacao2 = "Todas";
				
				double valor = rows[i].getDouble("prima");
				
				Entidade e = home.obterEntidadePorId(id);
				
				dados.add(e.obterNome()+";"+situacao2+";"+valor+";"+id);
			}
		}
		else
		{
			if(!situacao.equals("0"))
				sql = "select TOP "+ maxRegistros + " SUM(comissao_gs) as prima,situacao_seguro,corredor from evento,apolice where evento.id = apolice.id";
			else
				sql = "select TOP "+ maxRegistros + " SUM(comissao_gs) as prima,corredor from evento,apolice where evento.id = apolice.id";
	
			if(agente!=null)
				sql+=" and corredor = " + agente.obterId();
			else
				sql+=" and corredor > 0";
			
			if(!situacao.equals("0"))
				sql+=" and situacao_seguro='"+situacao+"'";
			
			if(!situacao.equals("0"))
				sql+= " and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime() + " GROUP BY situacao_seguro,corredor";
			else
				sql+= " and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime() + " GROUP BY corredor";
			
			if(monto > 0)
				sql+=" HAVING SUM(comissao_gs)>=" + monto;
			else
				sql+=" HAVING SUM(comissao_gs)> 0 ";
			
			sql+= " ORDER BY prima DESC";
			
			//System.out.println(sql);
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("corredor");
				String situacao2 = "";
				if(!situacao.equals("0"))
					situacao2 = rows[i].getString("situacao_seguro");
				else
					situacao2 = "Todas";
				
				double valor = rows[i].getDouble("prima");
				
				Entidade e = home.obterEntidadePorId(id);
				
				dados.add(e.obterNome()+";"+situacao2+";"+valor+";"+id);
			}
		}
		
		return dados;
	}
	
	public Collection<Aseguradora> obterAseguradorasApolicesAcumuladas(Entidade agente, Date dataInicio,Date dataFim, boolean auxiliar) throws Exception
	{
		Collection<Aseguradora> aseguradoras = new ArrayList<Aseguradora>();
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		String sql = "";
		
		if(auxiliar)
			sql = "select entidade.id,SUM(comissao_gs), SUM(comissao_me), SUM(prima_gs), SUM(premio_gs), SUM(capital_gs), SUM(capital_me) from evento,apolice,entidade where evento.id = apolice.id and origem = entidade.id and agente ="+agente.obterId()+" and data_emissao >="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" group by entidade.id ";//HAVING SUM(comissao_gs)>0 or SUM(comissao_me)>0 or SUM(prima_gs)>0 or SUM(premio_gs)>0 or SUM(capital_gs)>0 or SUM(capital_me)>0";
		else
			sql = "select entidade.id,SUM(comissao_gs), SUM(comissao_me), SUM(prima_gs), SUM(premio_gs), SUM(capital_gs), SUM(capital_me) from evento,apolice,entidade where evento.id = apolice.id and origem = entidade.id and corredor ="+agente.obterId()+" and data_emissao >="+dataInicio.getTime()+" and data_emissao<="+dataFim.getTime()+" group by entidade.id";// HAVING SUM(comissao_gs)>0 or SUM(comissao_me)>0 or SUM(prima_gs)>0 or SUM(premio_gs)>0 or SUM(capital_gs)>0 or SUM(capital_me)>0";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		//System.out.println(sql);
		
		//query.addLong(this.obterId());
		//query.addLong(agente.obterId());
		//query.addLong(dataInicio.getTime());
		//query.addLong(dataFim.getTime());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
		{
			long id = rows[i].getLong("id");
			
			aseguradoras.add((Aseguradora)home.obterEntidadePorId(id));
			
		}

		return aseguradoras;
	}
	
	public Collection<String> obterApolicesComissaoAgentesPorCapital(AuxiliarSeguro agente,Date dataInicio,Date dataFim, String situacao, boolean auxiliar) throws Exception
	{
		Map<String, String> dados = new TreeMap<String,String>();
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		String	sql = "";
		
		if(auxiliar)
		{
			if(!situacao.equals("0"))
				sql = "select agente,situacao_seguro, count(*) as qtde, plano.plano,SUM(capital_gs) as capital, SUM(capital_me) as me from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
			else
				sql = "select agente,count(*) as qtde, plano.plano,SUM(capital_gs) as capital, SUM(capital_me) as me from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
	
			if(agente!=null)
				sql+=" and agente = " + agente.obterId();
			else
				sql+=" and agente > 0";
			
			if(!situacao.equals("0"))
				sql+=" and situacao_seguro='"+situacao+"'";
			
			sql+=" and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime();
			
			if(!situacao.equals("0"))
				sql+= " GROUP BY agente,situacao_seguro,plano.plano";
			else
				sql+= " GROUP BY agente,plano.plano";
			
			//System.out.println(sql);
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("agente");
				String situacao2 = "";
				if(!situacao.equals("0"))
					situacao2 = rows[i].getString("situacao_seguro");
				else
					situacao2 = "Todas";
				
				int qtde = rows[i].getInt("qtde");
				String plano = rows[i].getString("plano");
				
				double valorGs = rows[i].getDouble("capital");
				double valorMe = rows[i].getDouble("me");
				
				Entidade e = home.obterEntidadePorId(id);
				
				dados.put(e.obterNome() + i, e.obterNome()+";"+situacao2+";"+qtde+";"+plano+";"+valorGs+";"+valorMe+";"+id);
			}
		}
		else
		{
			if(!situacao.equals("0"))
				sql = "select corredor,situacao_seguro, count(*) as qtde, plano.plano,SUM(capital_gs) as capital, SUM(capital_me) as me from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
			else
				sql = "select corredor,count(*) as qtde, plano.plano,SUM(capital_gs) as capital, SUM(capital_me) as me from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
	
			if(agente!=null)
				sql+=" and corredor = " + agente.obterId();
			else
				sql+=" and corredor > 0";
			
			if(!situacao.equals("0"))
				sql+=" and situacao_seguro='"+situacao+"'";
			
			sql+=" and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime();
			
			if(!situacao.equals("0"))
				sql+= " GROUP BY corredor,situacao_seguro,plano.plano";
			else
				sql+= " GROUP BY corredor,plano.plano";
			
			//System.out.println(sql);
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("corredor");
				String situacao2 = "";
				if(!situacao.equals("0"))
					situacao2 = rows[i].getString("situacao_seguro");
				else
					situacao2 = "Todas";
				
				int qtde = rows[i].getInt("qtde");
				String plano = rows[i].getString("plano");
				
				double valorGs = rows[i].getDouble("capital");
				double valorMe = rows[i].getDouble("me");
				
				Entidade e = home.obterEntidadePorId(id);
				
				dados.put(e.obterNome() + i, e.obterNome()+";"+situacao2+";"+qtde+";"+plano+";"+valorGs+";"+valorMe+";"+id);
			}
		}
		
		return dados.values();
	}
	
	public Collection<String> obterApolicesComissaoAgentesPorPrima(AuxiliarSeguro agente,Date dataInicio,Date dataFim, String situacao, boolean auxiliar) throws Exception
	{
		Map<String, String> dados = new TreeMap<String,String>();
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		String	sql = "";
		
		if(auxiliar)
		{
			if(!situacao.equals("0"))
				sql = "select agente,situacao_seguro, count(*) as qtde, plano.plano,SUM(prima_gs) as capital, SUM(prima_me) as me from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
			else
				sql = "select agente,count(*) as qtde, plano.plano,SUM(prima_gs) as capital, SUM(prima_me) as me from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
	
			if(agente!=null)
				sql+=" and agente = " + agente.obterId();
			else
				sql+=" and agente > 0";
			
			if(!situacao.equals("0"))
				sql+=" and situacao_seguro='"+situacao+"'";
			
			sql+=" and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime();
			
			if(!situacao.equals("0"))
				sql+= " GROUP BY agente,situacao_seguro,plano.plano";
			else
				sql+= " GROUP BY agente,plano.plano";
			
			//System.out.println(sql);
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("agente");
				String situacao2 = "";
				if(!situacao.equals("0"))
					situacao2 = rows[i].getString("situacao_seguro");
				else
					situacao2 = "Todas";
				
				int qtde = rows[i].getInt("qtde");
				String plano = rows[i].getString("plano");
				
				double valorGs = rows[i].getDouble("capital");
				double valorMe = rows[i].getDouble("me");
				
				Entidade e = home.obterEntidadePorId(id);
				
				dados.put(e.obterNome() + i, e.obterNome()+";"+situacao2+";"+qtde+";"+plano+";"+valorGs+";"+valorMe+";"+id);
			}
		}
		else
		{
			if(!situacao.equals("0"))
				sql = "select corredor,situacao_seguro, count(*) as qtde, plano.plano,SUM(prima_gs) as capital, SUM(prima_me) as me from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
			else
				sql = "select corredor,count(*) as qtde, plano.plano,SUM(prima_gs) as capital, SUM(prima_me) as me from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
	
			if(agente!=null)
				sql+=" and corredor = " + agente.obterId();
			else
				sql+=" and corredor > 0";
			
			if(!situacao.equals("0"))
				sql+=" and situacao_seguro='"+situacao+"'";
			
			sql+=" and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime();
			
			if(!situacao.equals("0"))
				sql+= " GROUP BY corredor,situacao_seguro,plano.plano";
			else
				sql+= " GROUP BY corredor,plano.plano";
			
			//System.out.println(sql);
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("corredor");
				String situacao2 = "";
				if(!situacao.equals("0"))
					situacao2 = rows[i].getString("situacao_seguro");
				else
					situacao2 = "Todas";
				
				int qtde = rows[i].getInt("qtde");
				String plano = rows[i].getString("plano");
				
				double valorGs = rows[i].getDouble("capital");
				double valorMe = rows[i].getDouble("me");
				
				Entidade e = home.obterEntidadePorId(id);
				
				dados.put(e.obterNome() + i, e.obterNome()+";"+situacao2+";"+qtde+";"+plano+";"+valorGs+";"+valorMe+";"+id);
			}
		}
		
		return dados.values();
	}
	
	public Collection<String> obterApolicesComissaoAgentesPorComissao(AuxiliarSeguro agente,Date dataInicio,Date dataFim, String situacao, boolean auxiliar) throws Exception
	{
		Map<String, String> dados = new TreeMap<String,String>();
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		String	sql = "";
		
		if(auxiliar)
		{
			if(!situacao.equals("0"))
				sql = "select agente,situacao_seguro, count(*) as qtde, plano.plano,SUM(comissao_gs) as capital, SUM(comissao_me) as me from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
			else
				sql = "select agente,count(*) as qtde, plano.plano,SUM(comissao_gs) as capital, SUM(comissao_me) as me from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
	
			if(agente!=null)
				sql+=" and agente = " + agente.obterId();
			else
				sql+=" and agente > 0";
			
			if(!situacao.equals("0"))
				sql+=" and situacao_seguro='"+situacao+"'";
			
			sql+=" and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime();
			
			if(!situacao.equals("0"))
				sql+= " GROUP BY agente,situacao_seguro,plano.plano";
			else
				sql+= " GROUP BY agente,plano.plano";
			
			//System.out.println(sql);
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("agente");
				String situacao2 = "";
				if(!situacao.equals("0"))
					situacao2 = rows[i].getString("situacao_seguro");
				else
					situacao2 = "Todas";
				
				int qtde = rows[i].getInt("qtde");
				String plano = rows[i].getString("plano");
				
				double valorGs = rows[i].getDouble("capital");
				double valorMe = rows[i].getDouble("me");
				
				Entidade e = home.obterEntidadePorId(id);
				
				dados.put(e.obterNome() + i, e.obterNome()+";"+situacao2+";"+qtde+";"+plano+";"+valorGs+";"+valorMe+";"+id);
			}
		}
		else
		{
			if(!situacao.equals("0"))
				sql = "select corredor,situacao_seguro, count(*) as qtde, plano.plano,SUM(comissao_gs) as capital, SUM(comissao_me) as me from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
			else
				sql = "select corredor,count(*) as qtde, plano.plano,SUM(comissao_gs) as capital, SUM(comissao_me) as me from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
	
			if(agente!=null)
				sql+=" and corredor = " + agente.obterId();
			else
				sql+=" and corredor > 0";
			
			if(!situacao.equals("0"))
				sql+=" and situacao_seguro='"+situacao+"'";
			
			sql+=" and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime();
			
			if(!situacao.equals("0"))
				sql+= " GROUP BY corredor,situacao_seguro,plano.plano";
			else
				sql+= " GROUP BY corredor,plano.plano";
			
			//System.out.println(sql);
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("corredor");
				String situacao2 = "";
				if(!situacao.equals("0"))
					situacao2 = rows[i].getString("situacao_seguro");
				else
					situacao2 = "Todas";
				
				int qtde = rows[i].getInt("qtde");
				String plano = rows[i].getString("plano");
				
				double valorGs = rows[i].getDouble("capital");
				double valorMe = rows[i].getDouble("me");
				
				Entidade e = home.obterEntidadePorId(id);
				
				dados.put(e.obterNome() + i, e.obterNome()+";"+situacao2+";"+qtde+";"+plano+";"+valorGs+";"+valorMe+";"+id);
			}
		}
		
		return dados.values();
	}
	
	public Collection<Apolice> obterApolicesComissaoAgentes(AuxiliarSeguro agente,Date dataInicio,Date dataFim, String situacao, String plano, boolean auxiliar) throws Exception
	{
		Collection<Apolice> apolices = new ArrayList<Apolice>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String	sql = "";
		
		if(auxiliar)
		{
			sql = "select evento.id from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
	
			if(agente!=null)
				sql+=" and agente = " + agente.obterId();
			else
				sql+=" and agente > 0";
			
			if(!situacao.equals("0"))
				sql+=" and situacao_seguro='"+situacao+"'";
			
			sql+=" and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime() + " and plano.plano = '" + plano + "'";
			
			sql+= " ORDER BY data_prevista_inicio";
			
			//System.out.println(sql);
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("id");
				
				Apolice apolice = (Apolice) home.obterEventoPorId(id);
				
				apolices.add(apolice);
			}
		}
		else
		{
			sql = "select evento.id from evento,apolice,plano where evento.id = apolice.id and apolice.plano = plano.id";
			
			if(agente!=null)
				sql+=" and corredor = " + agente.obterId();
			else
				sql+=" and corredor > 0";
			
			if(!situacao.equals("0"))
				sql+=" and situacao_seguro='"+situacao+"'";
			
			sql+=" and data_emissao >= " + dataInicio.getTime() + " and data_emissao <= " + dataFim.getTime() + " and plano.plano = '" + plano + "'";
			
			sql+= " ORDER BY data_prevista_inicio";
			
			//System.out.println(sql);
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("id");
				
				Apolice apolice = (Apolice) home.obterEventoPorId(id);
				
				apolices.add(apolice);
			}
		}
		
		return apolices;
	}

}