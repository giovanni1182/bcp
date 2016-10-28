package com.gvs.crm.model;

import java.util.Date;

public interface SinistroFiniquitadoCentralRisco extends Evento 
{
	void atribuirDataCorte(Date data) throws Exception;
	void atribuirSecao(ClassificacaoContas cContas) throws Exception;
	void atribuirPlano(Plano plano) throws Exception;
	void atribuirQtdeSinistros(int qtde) throws Exception;
	void atribuirMontantePagoGs(double valor) throws Exception;
	void atribuirMoedaMontantePagoME(String moeda) throws Exception;
	void atribuirMontantePagoME(double valor) throws Exception;
	
	// VIGENTES
	void atribuirCapitalGs(double valor) throws Exception;
	void atribuirMoedaCapitalME(String moeda) throws Exception;
	void atribuirCapitalME(double valor) throws Exception;
	
	Date obterDataCorte() throws Exception;
	ClassificacaoContas obterSecao() throws Exception;
	Plano obterPlano() throws Exception;
	int obterQtdeSinistros() throws Exception;
	double obterMontantePagoGs() throws Exception;
	String obterMoedaMontantePagoME() throws Exception;
	double obterMontantePagoME() throws Exception;
	
//	 VIGENTES
	double obterCapitalGs() throws Exception;
	String obterMoedaCapitalME() throws Exception;
	double obterCapitalME() throws Exception;
}
