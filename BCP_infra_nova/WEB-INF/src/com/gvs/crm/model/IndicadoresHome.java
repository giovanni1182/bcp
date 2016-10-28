package com.gvs.crm.model;

import java.util.Date;


public interface IndicadoresHome
{
	void calcularIndicadoresTecnicos(Date mesAno, Usuario usuario, boolean montarArquivo) throws Exception;
	
	double obterGastosDeExportacaoPNA(Entidade aseg) throws Exception;
	double obterGastosDeProducaoPNA(Entidade aseg) throws Exception;
	double obterGastosOperativosPD(Entidade aseg) throws Exception;
	double obterMagemDeGanancia(Entidade aseg) throws Exception;
	double obterPNAtivoTotal(Entidade aseg) throws Exception;
	double obterProvisoesTecnicas(Entidade aseg) throws Exception;
	double obterResultadoTecnicoSemPN(Entidade aseg) throws Exception;
	double obterRetornoSemPN(Entidade aseg) throws Exception;
	double obterSinistrosBrutosPD(Entidade aseg) throws Exception;
	double obterSinistrosNetosPDNR(Entidade aseg) throws Exception;
	
	//Margem Ponderada
	double obterMargemPonderadaGastosDeExportacaoPNA() throws Exception;
	double obterMargemPonderadaGastosDeProducaoPNA() throws Exception;
	double obterMargemPonderadaGastosOperativosPD() throws Exception;
	double obterMargemPonderadaMagemDeGanancia() throws Exception;
	double obterMagemPonderadaPNAtivoTotal() throws Exception;
	double obterMargemPonderadaProvisoesTecnicas() throws Exception;
	double obterMargemPonderadaResultadoTecnicoSemPN() throws Exception;
	double obterMargemPonderadaRetornoSemPN() throws Exception;
	double obterMagemPonderadaSinistrosBrutosPD() throws Exception;
	double obterMargemPonderadaSinistrosNetosPDNR() throws Exception;
	
	//Relatório GEE
	void setMesAno(Date data);
	void instanciarContasGEE() throws Exception;
	double obterPrimasSegurosGEE(Date data) throws Exception;
	
	
}
