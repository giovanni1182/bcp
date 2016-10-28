package com.gvs.crm.model;

import java.util.Date;
import java.util.Map;

public interface RatioHome 
{
	double obterSinistrosPagos3Anos(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception;
	double obterGastosSinistros3Anos(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception;
	double obterSinistrosRecuperados3Anos(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception;
	double obterGastosRecuperados3Anos(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception;
	double obterRecuperadosSinistros3Anos(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception;
	double obterProvisaoTecnicaSinistros3Anos(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception;
	double obterPrimaDireta1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception;
	double obterPrimasAceitas1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception;
	double obterPrimasCedidas1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception;
	double obterRendaFixa1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception;
	double obterImobiliares1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception;
	double obterBensUsoProprio1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception;
	double obterPrimasDiferidas1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception;
	double obterProvisoesTecnicasSeguros1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception;
	double obterReservaAtivosFixos1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception;
	double obterReservaLei1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception;
	
	
	
}
