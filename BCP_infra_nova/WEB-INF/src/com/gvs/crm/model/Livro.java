package com.gvs.crm.model;

public interface Livro extends Evento 
{
	void incluir() throws Exception;
	void atribuirMes(int mes) throws Exception;
	void atribuirAno(int ano) throws Exception;
	
	int obterMes() throws Exception;
	int obterAno() throws Exception;
	
	void atualizarMes(int mes) throws Exception;
	void atualizarAno(int ano) throws Exception;
}
