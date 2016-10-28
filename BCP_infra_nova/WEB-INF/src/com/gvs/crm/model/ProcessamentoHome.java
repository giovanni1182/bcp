package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

import com.gvs.crm.model.Processamento.Agenda;

public interface ProcessamentoHome
{
	Processamento obterProcessamentoDoDia(String tipo) throws Exception;
	Collection<Agenda> obterAgendas(Entidade aseguradora, int mes, int ano, String tipo, int erro, Date dataInicio, Date dataFim) throws Exception;
}
	
