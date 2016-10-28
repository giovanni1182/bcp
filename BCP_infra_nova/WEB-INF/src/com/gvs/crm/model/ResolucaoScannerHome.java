package com.gvs.crm.model;

import java.util.Collection;

public interface ResolucaoScannerHome 
{
	Collection<ResolucaoScanner> obterResolucoes(Aseguradora aseguradora, String titulo, String numero, int ano) throws Exception;
}
