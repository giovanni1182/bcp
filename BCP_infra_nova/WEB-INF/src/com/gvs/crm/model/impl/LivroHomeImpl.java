package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Livro;
import com.gvs.crm.model.LivroHome;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class LivroHomeImpl extends Home implements LivroHome
{
	public Collection<Livro> obterLivros(Aseguradora aseguradora, String tipo, int mes, int ano) throws Exception
	{
		Collection<Livro> livros = new ArrayList<Livro>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "select evento.id from evento, livro where evento.id = livro.id and mes >= " + mes + " and ano<="+ano;
		if(aseguradora!=null)
			sql+=" and origem = " + aseguradora.obterId();
		if(!tipo.equals(""))
			sql+=" and tipo = '" + tipo +"'";
		
		sql+=" order by ano,mes desc";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Livro livro = (Livro) home.obterEventoPorId(id);
			
			livros.add(livro);
		}
		
		return livros;
	}
	
	public Collection<Livro> obterLivrosMesAno(Aseguradora aseguradora, String tipo, int mes, int ano) throws Exception
	{
		Collection<Livro> livros = new ArrayList<Livro>();
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "select evento.id from evento, livro where evento.id = livro.id and mes = " + mes + " and ano="+ano;
		if(aseguradora!=null)
			sql+=" and origem = " + aseguradora.obterId();
		if(!tipo.equals(""))
			sql+=" and tipo = '" + tipo +"'";
		
		sql+=" order by ano,mes desc";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Livro livro2 = (Livro) home.obterEventoPorId(id);
			
			livros.add(livro2);
		}
		
		return livros;
	}
	
	public Livro obterLivro(Aseguradora aseguradora, String tipo, int mes, int ano) throws Exception
	{
		Livro livro = null;
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "select evento.id from evento, livro where evento.id = livro.id and mes = " + mes + " and ano = "+ano;
		if(aseguradora!=null)
			sql+=" and origem = " + aseguradora.obterId();
		if(!tipo.equals(""))
			sql+=" and tipo = '" + tipo +"'";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			livro = (Livro) home.obterEventoPorId(id);
		}
		
		return livro;
	}
	
	public Livro obterUltimoLivro(Aseguradora aseguradora, String tipo) throws Exception
	{
		Livro livro = null;
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		String sql = "select TOP 1 evento.id from evento, livro where evento.id = livro.id";
		if(aseguradora!=null)
			sql+=" and origem = " + aseguradora.obterId();
		if(!tipo.equals(""))
			sql+=" and tipo = '" + tipo +"'";
		
		sql+=" order by ano desc, mes desc";
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			livro = (Livro) home.obterEventoPorId(id);
		}
		
		return livro;
	}
}