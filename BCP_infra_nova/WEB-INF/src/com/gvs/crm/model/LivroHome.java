package com.gvs.crm.model;

import java.util.Collection;

public interface LivroHome 
{
	Collection<Livro> obterLivros(Aseguradora aseguradora, String tipo, int mes, int ano) throws Exception;
	Livro obterLivro(Aseguradora aseguradora, String tipo, int mes, int ano) throws Exception;
	Livro obterUltimoLivro(Aseguradora aseguradora, String tipo) throws Exception;
	Collection<Livro> obterLivrosMesAno(Aseguradora aseguradora, String tipo, int mes, int ano) throws Exception;
}
