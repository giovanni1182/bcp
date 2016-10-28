package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Inscricao;
import com.gvs.crm.model.InscricaoHome;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class InscricaoHomeImpl extends Home implements InscricaoHome {
	public boolean verificarInscricao(Entidade entidade, String inscricao)
			throws Exception {
		boolean existe = false;

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,inscricao,entidade where evento.id = inscricao.id and origem = entidade.id and origem <> ? and inscricao = ? and entidade.classe = ? group by evento.id");
		query.addLong(entidade.obterId());
		query.addString(inscricao);
		query.addString(entidade.obterClasse());

		if (query.execute().length > 0)
			existe = true;
		
		System.out.println("select evento.id from evento,inscricao,entidade where evento.id = inscricao.id and origem = entidade.id and origem <> "+entidade.obterId()+" and inscricao = "+inscricao+" and entidade.classe = "+entidade.obterClasse()+" group by evento.id");

		return existe;
	}

	public boolean verificarInscricao(AuxiliarSeguro auxiliar,String atividade, String inscricaoStr) throws Exception
	{
		boolean existe = false;

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,inscricao,entidade,auxiliar_seguro where evento.id = inscricao.id and origem = entidade.id and entidade.id = auxiliar_seguro.id and origem <> ? and inscricao = ? and entidade.classe = ? group by evento.id");
		query.addLong(auxiliar.obterId());
		query.addString(inscricaoStr);
		query.addString(auxiliar.obterClasse());

		//System.out.println("select evento.id from
		// evento,inscricao,entidade,auxiliar_seguro,entidade_atributo where
		// evento.id = inscricao.id and origem = entidade.id and entidade.id =
		// auxiliar_seguro.id and entidade.id = entidade_atributo.entidade and
		// origem <> "+auxiliar.obterId()+" and inscricao = "+inscricao+" and
		// entidade.classe = "+auxiliar.obterClasse()+" and
		// entidade_atributo.nome='atividade' and entidade_atributo.valor<>
		// "+atividade+" group by evento.id");

		SQLRow[] rows = query.execute();
		
		System.out.println("select evento.id from evento,inscricao,entidade,auxiliar_seguro where evento.id = inscricao.id and origem = entidade.id and entidade.id = auxiliar_seguro.id and origem <> "+auxiliar.obterId()+" and inscricao = "+inscricaoStr+" and entidade.classe = "+auxiliar.obterClasse()+" group by evento.id");

		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome("EventoHome");

		for (int i = 0; i < rows.length; i++)
		{
			long id = rows[i].getLong("id");

			Inscricao inscricao = (Inscricao) eventoHome.obterEventoPorId(id);

			String atividade2 = inscricao.obterOrigem().obterAtributo("atividade").obterValor();

			if (atividade2.equals(atividade)) 
			{
				existe = true;
				break;
			}

		}

		return existe;
	}
	
	public Entidade verificarInscricao2(AuxiliarSeguro auxiliar,String atividade, String inscricaoStr) throws Exception
	{
		Entidade entidade = null;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,inscricao,entidade,auxiliar_seguro where evento.id = inscricao.id and origem = entidade.id and entidade.id = auxiliar_seguro.id and origem <> ? and inscricao = ? and entidade.classe = ? group by evento.id");
		query.addLong(auxiliar.obterId());
		query.addString(inscricaoStr);
		query.addString(auxiliar.obterClasse());

		//System.out.println("select evento.id from
		// evento,inscricao,entidade,auxiliar_seguro,entidade_atributo where
		// evento.id = inscricao.id and origem = entidade.id and entidade.id =
		// auxiliar_seguro.id and entidade.id = entidade_atributo.entidade and
		// origem <> "+auxiliar.obterId()+" and inscricao = "+inscricao+" and
		// entidade.classe = "+auxiliar.obterClasse()+" and
		// entidade_atributo.nome='atividade' and entidade_atributo.valor<>
		// "+atividade+" group by evento.id");

		SQLRow[] rows = query.execute();
		
		System.out.println("select evento.id from evento,inscricao,entidade,auxiliar_seguro where evento.id = inscricao.id and origem = entidade.id and entidade.id = auxiliar_seguro.id and origem <> "+auxiliar.obterId()+" and inscricao = "+inscricaoStr+" and entidade.classe = "+auxiliar.obterClasse()+" group by evento.id");

		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome("EventoHome");

		for (int i = 0; i < rows.length; i++)
		{
			long id = rows[i].getLong("id");

			Inscricao inscricao = (Inscricao) eventoHome.obterEventoPorId(id);

			String atividade2 = inscricao.obterOrigem().obterAtributo("atividade").obterValor();

			if (atividade2.equals(atividade)) 
			{
				entidade = inscricao.obterOrigem();
				System.out.println("Id inscri:" + inscricao.obterId());
				System.out.println("entidade:" + entidade.obterId());
				break;
			}

		}

		return entidade;
	}

	public Collection obterInscricoes(String entidade, String fase, int pagina)	throws Exception 
	{
		//Map inscricoes = new TreeMap();
		Collection inscricoes = new ArrayList();
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome("EventoHome");
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

		String sql = "";
		boolean soEvento = false;
		
		if (entidade.equals("") && fase.equals(""))
		{
			sql = "select evento.id, inscricao, classe from evento,inscricao where evento.id = inscricao.id order by inscricao";
			soEvento = true;
		}
		else if (entidade.equals("Agentes de Seguros"))
		{
			//sql = "select entidade.id from entidade,entidade_atributo where entidade.id = entidade_atributo.entidade and entidade_atributo.nome='atividade' and valor like '%Agentes de Seguros%' order by entidade.nome";
			if(fase.equals(""))
				sql = "select evento.id from evento,inscricao,entidade,entidade_atributo where entidade.id = entidade_atributo.entidade and entidade_atributo.nome='atividade' and valor like '%Agentes de Seguros%' and evento.id = inscricao.id and origem = entidade.id order by inscricao";
			else
				sql = "select evento.id from evento,inscricao,entidade,entidade_atributo where entidade.id = entidade_atributo.entidade and entidade_atributo.nome='atividade' and valor like '%Agentes de Seguros%' and evento.id = inscricao.id and origem = entidade.id and situacao='"+ fase + "' order by inscricao";
			
			soEvento = true;
		}
		else if (entidade.equals("Aseguradoras"))
		{
			//sql = "select entidade.id from entidade where classe = 'Aseguradora' order by entidade.nome";
			if(fase.equals(""))
				sql = "select evento.id from evento,inscricao,entidade where entidade.classe = 'Aseguradora' and evento.id = inscricao.id and origem = entidade.id order by inscricao";
			else
				sql = "select evento.id from evento,inscricao,entidade where entidade.classe = 'Aseguradora' and evento.id = inscricao.id and origem = entidade.id and situacao='" + fase + "' order by inscricao";
				
			soEvento = true;
		}
		else if (entidade.equals("Auditor Externo"))
		{
			if(fase.equals(""))
				sql = "select evento.id from evento,inscricao,entidade where entidade.classe = 'AuditorExterno' and evento.id = inscricao.id and origem = entidade.id order by inscricao";
			else
				sql = "select evento.id from evento,inscricao,entidade where entidade.classe = 'AuditorExterno' and evento.id = inscricao.id and origem = entidade.id and situacao='" + fase + "' order by inscricao";
			
			soEvento = true;
		}
		else if (entidade.equals("Corredora de Reaseguro"))
		{
			if(fase.equals(""))
				sql = "select evento.id from evento,inscricao,entidade where entidade.classe = 'Corretora' and evento.id = inscricao.id and origem = entidade.id order by inscricao";
			else
				sql = "select evento.id from evento,inscricao,entidade where entidade.classe = 'Corretora' and evento.id = inscricao.id and origem = entidade.id and situacao='" + fase + "' order by inscricao";
			
			soEvento = true;
		}
		else if (entidade.equals("Corredores de Seguros"))
		{
			if(fase.equals(""))
				sql = "select evento.id from evento,inscricao,entidade,entidade_atributo where entidade.id = entidade_atributo.entidade and entidade_atributo.nome='atividade' and valor like '%Corredores de Seguros%' and evento.id = inscricao.id and origem = entidade.id order by inscricao";
			else
				sql = "select evento.id from evento,inscricao,entidade,entidade_atributo where entidade.id = entidade_atributo.entidade and entidade_atributo.nome='atividade' and valor like '%Corredores de Seguros%' and evento.id = inscricao.id and origem = entidade.id and situacao='"+ fase + "' order by inscricao";
			
			soEvento = true;
			
			//sql = "select entidade.id from entidade,entidade_atributo where entidade.id = entidade_atributo.entidade and entidade_atributo.nome='atividade' and valor like '%Corredores de Seguros%' order by entidade.nome";
		}
		else if (entidade.equals("Liquidadores de Siniestros"))
		{
			if(fase.equals(""))
				sql = "select evento.id from evento,inscricao,entidade,entidade_atributo where entidade.id = entidade_atributo.entidade and entidade_atributo.nome='atividade' and valor like '%Liquidadores de Siniestros%' and evento.id = inscricao.id and origem = entidade.id order by inscricao";
			else
				sql = "select evento.id from evento,inscricao,entidade,entidade_atributo where entidade.id = entidade_atributo.entidade and entidade_atributo.nome='atividade' and valor like '%Liquidadores de Siniestros%' and evento.id = inscricao.id and origem = entidade.id and situacao='"+ fase + "' order by inscricao";
			
			soEvento = true;
			
			//sql = "select entidade.id from entidade,entidade_atributo where entidade.id = entidade_atributo.entidade and entidade_atributo.nome='atividade' and valor like '%Liquidadores de Siniestros%' order by entidade.nome";
		}
		else if (entidade.equals("Reaseguradora"))
		{
			if(fase.equals(""))
				sql = "select evento.id from evento,inscricao,entidade where entidade.classe = 'Reaseguradora' and evento.id = inscricao.id and origem = entidade.id order by inscricao";
			else
				sql = "select evento.id from evento,inscricao,entidade where entidade.classe = 'Reaseguradora' and evento.id = inscricao.id and origem = entidade.id and situacao='" + fase + "' order by inscricao";
			
			soEvento = true;
			
			//sql = "select entidade.id from entidade where classe = 'Reaseguradora' order by nome";
		}
		else
		{
			sql = "select evento.id, inscricao, classe from evento,inscricao where evento.id = inscricao.id and situacao = '"+fase+"' order by inscricao";
			soEvento = true;
		}
		
		//System.out.println("SQL: " + sql);

		SQLQuery query = this.getModelManager().createSQLQuery("crm", sql);
		//query.setCurrentPage(pagina);
		//query.setRowsByPage(30);

		SQLRow[] rows = query.execute();

		if (entidade.equals("") && fase.equals("")) 
		{
			for (int i = 0; i < rows.length; i++) 
			{
				long id = rows[i].getLong("id");

				Inscricao inscricao = (Inscricao) eventoHome.obterEventoPorId(id);

				//inscricoes.put(inscricao.obterInscricao()+ inscricao.obterCriacao().getTime(), inscricao);
				inscricoes.add(inscricao);
			}
		} 
		else 
		{
			//System.out.println("rows.length: " + rows.length);

			if(soEvento)
			{
				for (int i = 0; i < rows.length; i++) 
				{
					long id = rows[i].getLong("id");
					
					//System.out.println("id: " + id);

					Inscricao inscricao = (Inscricao) eventoHome.obterEventoPorId(id);

					//inscricoes.put(inscricao.obterInscricao()+ inscricao.obterCriacao().getTime(), inscricao);
					inscricoes.add(inscricao);
				}
			}
			else
			{
				for (int i = 0; i < rows.length; i++) 
				{
					long id = rows[i].getLong("id");
	
					Entidade entidade2 = (Entidade) entidadeHome.obterEntidadePorId(id);
					
					//System.out.println("Nome: " + entidade2.obterNome());
	
					for (Iterator j = entidade2.obterInscricoes().iterator(); j.hasNext();) 
					{
						Inscricao inscricao = (Inscricao) j.next();
						
						//System.out.println("inscricao: " + inscricao.obterInscricao());
						
	
						if (fase.equals(""))
						{
							//inscricoes.put(inscricao.obterInscricao()+ inscricao.obterOrigem().obterNome() + inscricao.obterCriacao() + i,inscricao);
							inscricoes.add(inscricao);
						}
						else 
						{
							if (fase.equals(inscricao.obterSituacao()))
							{
								//inscricoes.put(inscricao.obterInscricao()+ inscricao.obterOrigem().obterNome() + inscricao.obterCriacao() + i,inscricao);
								inscricoes.add(inscricao);
							}
						}
					}
				}
			}
		}

		return inscricoes;
	}

}