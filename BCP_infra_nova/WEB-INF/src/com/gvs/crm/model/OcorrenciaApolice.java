package com.gvs.crm.model;

import java.util.Date;

public interface OcorrenciaApolice extends Evento {
	void atualizarApolice(Apolice apolice) throws Exception;

	void atualizarExpediente(String expediente) throws Exception;

	void atualizarDataSuspeita(Date data) throws Exception;

	void atualizarDataReporte(Date data) throws Exception;

	void atualizarNumeroConta(String numeroConta) throws Exception;

	void atualizarEntidade(String entidade) throws Exception;

	void atualizarTitular(String titular) throws Exception;

	void atualizarCidade(String cidade) throws Exception;

	void atualizarPais(String pais) throws Exception;

	void atualizarRazao(String razao) throws Exception;

	void atualizarEndereco(String endereco) throws Exception;

	void atualizarBairro(String bairro) throws Exception;

	void atualizarTelefone(String telefone) throws Exception;

	void incluir() throws Exception;

	Apolice obterApolice() throws Exception;

	String obterExpediente() throws Exception;

	Date obterDataSuspeita() throws Exception;

	Date obterDataReporte() throws Exception;

	String obterNumeroConta() throws Exception;

	String obterEntidade() throws Exception;

	String obterTitular() throws Exception;

	String obterCidade() throws Exception;

	String obterPais() throws Exception;

	String obterRazao() throws Exception;

	String obterEndereco() throws Exception;

	String obterBairro() throws Exception;

	String obterTelefone() throws Exception;
}