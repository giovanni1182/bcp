package com.gvs.crm.model;

public interface RatioPermanente extends Evento {
	void atribuirAtivoCorrente(double valor) throws Exception;

	void atribuirPassivoCorrente(double valor) throws Exception;

	void atribuirInversao(double valor) throws Exception;

	void atribuirDeudas(double valor) throws Exception;

	void atribuirUso(double valor) throws Exception;

	void atribuirVenda(double valor) throws Exception;

	void atribuirLeasing(double valor) throws Exception;

	void atribuirResultados(double valor) throws Exception;

	void atualizarAtivoCorrente(double valor) throws Exception;

	void atualizarPassivoCorrente(double valor) throws Exception;

	void atualizarInversao(double valor) throws Exception;

	void atualizarDeudas(double valor) throws Exception;

	void atualizarUso(double valor) throws Exception;

	void atualizarVenda(double valor) throws Exception;

	void atualizarLeasing(double valor) throws Exception;

	void atualizarResultados(double valor) throws Exception;

	void incluir() throws Exception;

	double obterAtivoCorrente() throws Exception;

	double obterPassivoCorrente() throws Exception;

	double obterInversao() throws Exception;

	double obterDeudas() throws Exception;

	double obterUso() throws Exception;

	double obterVenda() throws Exception;

	double obterLeasing() throws Exception;

	double obterResultados() throws Exception;
	
	double obterAnulacaoPrimasSegurosDiretos() throws Exception;
	
	double obterAnulacaoPrimasReasegurosAtivos() throws Exception;
	
	double obterAnulacaoPrimasCedidas() throws Exception;
}