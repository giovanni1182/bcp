package com.gvs.crm.model;

public interface RatioTresAnos extends Evento {
	void atribuirSinistrosPagos(double valor) throws Exception;

	void atribuirGastosSinistros(double valor) throws Exception;

	void atribuirSinistrosRecuperados(double valor) throws Exception;

	void atribuirGastosRecuperados(double valor) throws Exception;

	void atribuirRecuperoSinistros(double valor) throws Exception;

	void atribuirProvisoes(double valor) throws Exception;

	void atualizarSinistrosPagos(double valor) throws Exception;

	void atualizarGastosSinistros(double valor) throws Exception;

	void atualizarSinistrosRecuperados(double valor) throws Exception;

	void atualizarGastosRecuperados(double valor) throws Exception;

	void atualizarRecuperoSinistros(double valor) throws Exception;

	void atualizarProvisoes(double valor) throws Exception;

	void incluir() throws Exception;

	double obterSinistrosPagos() throws Exception;

	double obterGastosSinistros() throws Exception;

	double obterSinistrosRecuperados() throws Exception;

	double obterGastosRecuperados() throws Exception;

	double obterRecuperoSinistros() throws Exception;

	double obterProvisoes() throws Exception;
}