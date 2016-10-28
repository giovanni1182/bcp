package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

import com.gvs.crm.model.impl.ProcessoImpl;

public interface Processo extends Evento {

	static final String PROCESSO_CANCELADO = "cancelado";

	static final String PROCESSO_RECORRIDO = "recorrido";

	public interface Pessoa {
		ProcessoImpl obterProcesso() throws Exception;

		int obterId() throws Exception;

		Entidade obterPessoa() throws Exception;

		String obterTipo() throws Exception;
	}

	void adicionarPessoa(Entidade pessoa, String tipo) throws Exception;

	Pessoa obterPessoa(int id) throws Exception;

	Collection obterPessoas() throws Exception;

	void removerPessoa(Pessoa pessoa) throws Exception;

	void atualizarOrigem(Entidade origem) throws Exception;

	Collection obterOrigens() throws Exception;

	void excluirOrigem(Entidade origem) throws Exception;

	void atualizarResponsaveis(Entidade responsavel) throws Exception;

	Collection obterResponsaveis() throws Exception;

	void excluirResponsavel(Entidade responsavel) throws Exception;

	void atualizarExpediente(String expediente) throws Exception;

	void atualizarValorAcao(double valorAcao) throws Exception;

	void atualizarJulgado(String julgado) throws Exception;

	void atualizarJuiz(String juiz) throws Exception;

	void atualizarSecretaria(String secretaria) throws Exception;

	void atualizarFiscal(String fiscal) throws Exception;

	void atualizarTurno(String turno) throws Exception;

	void atualizarCargo(String cargo) throws Exception;

	void atualizarObjeto(String objeto) throws Exception;

	void atualizarCircunscricao(String circunscricao) throws Exception;

	void atualizarForum(String forum) throws Exception;

	void atualizarDataDemanda(Date dataDemanda) throws Exception;

	void atualizarSentenca(String sentenca) throws Exception;

	void atualizarDataCancelamento(Date dataCancelamento) throws Exception;

	void incluir() throws Exception;

	String obterExpediente() throws Exception;

	double obterValorAcao() throws Exception;

	String obterJulgado() throws Exception;

	String obterJuiz() throws Exception;

	String obterSecretaria() throws Exception;

	String obterFiscal() throws Exception;

	String obterTurno() throws Exception;

	String obterCargo() throws Exception;

	String obterObjeto() throws Exception;

	String obterCircunscricao() throws Exception;

	String obterForum() throws Exception;

	Date obterDataDemanda() throws Exception;

	String obterSentenca() throws Exception;

	Date obterDataCancelamento() throws Exception;
	boolean permiteAtualizar() throws Exception;

}