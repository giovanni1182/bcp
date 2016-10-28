package com.gvs.crm.model;

import java.math.BigDecimal;
import java.util.Date;

public interface MovimentacaoFinanceiraConta extends Evento {
	void atribuirDataPrevista(Date data) throws Exception;

	void atribuirConta(Entidade conta) throws Exception;

	void atribuirDebito(BigDecimal valor) throws Exception;

	void atribuirCredito(BigDecimal valor) throws Exception;

	void atribuirSaldoAnterior(BigDecimal valor) throws Exception;

	void atribuirSaldoAtual(BigDecimal valor) throws Exception;

	void atribuirSaldoEstrangeiro(BigDecimal valor) throws Exception;

	void atualizarDataPrevista(Date data) throws Exception;

	void atualizarConta(Entidade conta) throws Exception;

	void atualizarDebito(double valor) throws Exception;

	void atualizarCredito(double valor) throws Exception;

	void atualizarSaldoAnterior(double valor) throws Exception;

	void atualizarSaldoAtual(double valor) throws Exception;

	void atualizarSaldoEstrangeiro(double valor) throws Exception;

	Date obterDataPrevista() throws Exception;

	Entidade obterConta() throws Exception;

	BigDecimal obterCredito() throws Exception;

	BigDecimal obterDebito() throws Exception;

	BigDecimal obterSaldoAnterior() throws Exception;

	BigDecimal obterSaldoAtual() throws Exception;

	BigDecimal obterSaldoEstrangeiro() throws Exception;

}