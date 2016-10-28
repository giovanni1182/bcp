package com.gvs.crm.model;

public interface MeicosCalculo extends Evento
{
	void atribuirValor(double valor) throws Exception;

	void atualizarValorIndicador(double valor) throws Exception;

	void incluir() throws Exception;

	double obterValorIndicador() throws Exception;
}