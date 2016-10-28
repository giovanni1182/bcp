package com.gvs.crm.model;

import java.util.Collection;
import java.util.Map;

public interface MorosidadeHome 
{
	Collection<Entidade> obterAseguradorasCentralRisco(String nomeAsegurado, String documento) throws Exception;
	Map<Long,Collection<Morosidade>> obterAseguradorasCentralRiscoNovo(String nomeAsegurado, String documento) throws Exception;
}
