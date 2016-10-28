package com.gvs.crm.model;

import java.util.Date;

public interface MorosidadeCentralRisco extends Evento 
{
	void atribuirDataCorte(Date data) throws Exception;
	void atribuirSecao(ClassificacaoContas cContas) throws Exception;
	void atribuirDiasMora(int dias) throws Exception;
	void atribuirCotasAtraso(int cotas) throws Exception;
	void atribuirDeudasGs(double deudasGs) throws Exception;
	void atribuirMoedaDeudasMe(String moeda) throws Exception;
	void atribuirDeudasMe(double deudasMe) throws Exception;
	
	Date obterDataCorte() throws Exception;
	ClassificacaoContas obterSecao() throws Exception;
	int obterDiasMora() throws Exception;
	int obterCotasAtraso() throws Exception;
	double obterDeudasGs() throws Exception;
	String obterMoedaDeudasMe() throws Exception;
	double obterDeudasMe() throws Exception;
}
