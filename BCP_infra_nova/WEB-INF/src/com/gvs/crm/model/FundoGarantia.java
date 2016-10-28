package com.gvs.crm.model;

public interface FundoGarantia extends Evento {
	void atribuirValor(double valor) throws Exception;

	void incluir() throws Exception;

	double obterValor() throws Exception;
}