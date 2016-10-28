package com.gvs.crm.model;

public interface RatioUmAno extends Evento {
	void atribuirPrimasDiretas(double valor) throws Exception;

	void atribuirPrimasAceitas(double valor) throws Exception;

	void atribuirPrimasCedidas(double valor) throws Exception;

	void atribuirAnulacaoPrimasDiretas(double valor) throws Exception;

	void atribuirAnulacaoPrimasAtivas(double valor) throws Exception;

	void atribuirAnulacaoPrimasCedidas(double valor) throws Exception;

	void atualizarPrimasDiretas(double valor) throws Exception;

	void atualizarPrimasAceitas(double valor) throws Exception;

	void atualizarPrimasCedidas(double valor) throws Exception;

	void atualizarAnulacaoPrimasDiretas(double valor) throws Exception;

	void atualizarAnulacaoPrimasAtivas(double valor) throws Exception;

	void atualizarAnulacaoPrimasCedidas(double valor) throws Exception;

	void incluir() throws Exception;

	double obterPrimasDiretas() throws Exception;

	double obterPrimasAceitas() throws Exception;

	double obterPrimasCedidas() throws Exception;

	double obterAnulacaoPrimasDiretas() throws Exception;

	double obterAnulacaoPrimasAtivas() throws Exception;

	double obterAnulacaoPrimasCedidas() throws Exception;
}