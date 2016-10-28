package com.gvs.crm.model;

import java.util.Date;

/*
 * Interface que representa um evento de Movimentação Financeira.
 */
public interface MovimentacaoFinanceira extends Evento {
	static String EVENTO_REALIZADO = "realizado";

	static String EVENTO_PREVISTO = "previsto";

	static String EVENTO_CANCELADO = "cancelado";

	/*
	 * Atribui a data prevista do evento
	 */
	void atribuirDataPrevista(Date data) throws Exception;

	/*
	 * Atribui a data realizada do evento
	 */
	void atribuirDataRealizada(Date data) throws Exception;

	/*
	 * Atribui o valor previsto do evento
	 */
	void atribuirValorPrevisto(double valor) throws Exception;

	/*
	 * Atribui o valor realizado do evento
	 */
	void atribuirValorRealizado(double valor) throws Exception;

	void atribuirConta(Conta conta) throws Exception;

	/*
	 * Atribui a condição de pagamento prevista do evento.
	 */
	void atribuirCondicaoPrevista(String condicao) throws Exception;

	/*
	 * Atribui a condição de pagamento realizada.
	 */
	void atribuirCondicaoRealizada(String condicao) throws Exception;

	/*
	 * Atribui os dias.
	 */
	void atribuirDias(double dias) throws Exception;

	/*
	 * Atualizar a data prevista.
	 */
	void atualizarDataPrevista(Date data) throws Exception;

	/*
	 * Atualizar a data realizada.
	 */
	void atualizarDataRealizada(Date data) throws Exception;

	/*
	 * Atualizar o valor previsto.
	 */
	void atualizarValorPrevisto(double valor) throws Exception;

	void atualizarBanco(Banco banco) throws Exception;

	void atualizarNumeroCheque(String numeroCheque) throws Exception;

	Banco obterBanco() throws Exception;

	String obterNumeroCheque() throws Exception;

	/*
	 * Atualizar o valor realizado.
	 */
	void atualizarValorRealizado(double valor) throws Exception;

	void atualizarConta(Conta conta) throws Exception;

	/*
	 * Atualizar a condição de pagamento prevista.
	 */
	void atualizarCondicaoPrevista(String condicao) throws Exception;

	/*
	 * Atualizar a condição de pagamento realizada.
	 */
	void atualizarCondicaoRealizada(String condicao) throws Exception;

	/*
	 * Atualizar os dias.
	 */
	void atualizarDias(double dias) throws Exception;

	/*
	 * Atualiza a fase do evento para cancelado Verificar se pode cancelar.
	 */
	void cancelar() throws Exception;

	/*
	 * Atualiza a fase do evento para prevista e executa o saldo da origem e
	 * destino.
	 */
	void prever() throws Exception;

	/*
	 * Atualiza a fase do evento para realizado, a data realizada, valor
	 * realizado e executa o saldo da origem e destino.
	 */
	void realizar(Date data, double valor, String condicao, String comentario)
			throws Exception;

	/*
	 * Obtem a data prevista do evento
	 */
	Date obterDataPrevista() throws Exception;

	/*
	 * Obtem a data realizada do evento
	 */
	Date obterDataRealizada() throws Exception;

	/*
	 * Obtem o valor previsto do evento
	 */
	double obterValorPrevisto() throws Exception;

	/*
	 * Obtem o valor realizado do evento
	 */
	double obterValorRealizado() throws Exception;

	Entidade obterConta() throws Exception;

	/*
	 * Obtem a condição prevista do evento
	 */
	String obterCondicaoPrevista() throws Exception;

	/*
	 * Obtem a condição realizada do evento
	 */
	String obterCondicaoRealizada() throws Exception;

	/*
	 * Obtem a condição realizada do evento
	 */
	double obterDias() throws Exception;

	/*
	 * Verifica se permite cancelar.
	 */
	boolean permiteCancelar() throws Exception;

	/*
	 * Verifica se permite prever.
	 */
	boolean permitePrever() throws Exception;

	/*
	 * Verifica se permite realizar
	 */
	boolean permiteRealizar() throws Exception;

	/*
	 * Verifica se permite mostrar se esta en realizado.
	 */
	boolean permiteMostrarRealizado() throws Exception;

	void atualizarReferencia(Evento evento) throws Exception;

	Evento obterReferencia() throws Exception;

}