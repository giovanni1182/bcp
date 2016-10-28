package com.gvs.crm.model;

public interface ResolucaoScanner extends Evento
{
	void incluir() throws Exception;
	void atribuirAno(int ano);
	void atualizarAno(int ano) throws Exception;
	
	int obterAno() throws Exception;
}
