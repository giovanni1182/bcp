package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface EnvioArquivosHome 
{
	Collection<EnvioArquivos> obterArquivos(Entidade aseguradora, Date dataInicio, Date dataFim, Usuario usuario) throws Exception;
}
