package com.gvs.crm.model;

import java.util.Collection;

public interface EnvioArquivos extends Evento 
{
	void incluir() throws Exception;
	void addArquivo(String nomeArquivo) throws Exception;
	Collection<String> obterArquivos() throws Exception;
}
