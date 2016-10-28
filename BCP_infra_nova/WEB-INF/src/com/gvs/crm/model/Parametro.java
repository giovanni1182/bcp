package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface Parametro extends Entidade {
	public interface Feriado {
		void atualizarValor(String descricao, Date data) throws Exception;

		String obterDescricaoFeriado() throws Exception;

		Date obterDataFeriado() throws Exception;

		Entidade obterEntidade() throws Exception;

		int obterId() throws Exception;
	}

	public interface Consistencia {
		void atualizarValor(String operando1, String operador,
				String operando2, String mensagem, int regra) throws Exception;

		Entidade obterEntidade() throws Exception;

		int obterSequencial() throws Exception;

		int obterRegra() throws Exception;

		String obterOperando1() throws Exception;

		String obterOperando2() throws Exception;

		String obterOperador() throws Exception;

		String obterMensagem() throws Exception;
	}

	/*
	 * void incluir() throws Exception;
	 * 
	 * void atualizarTipoValorMinimo(String tipo) throws Exception; void
	 * atualizarValorMinimo(double valor) throws Exception;
	 * 
	 * String obterTipoValorMinimo() throws Exception; double obterValorMinimo()
	 * throws Exception;
	 */

	/*
	 * public interface Configuracao { void atualizarValor(int sequencial,
	 * String eventoEntidade, String campo, String argumento, String acao)
	 * throws Exception; Entidade obterEntidade() throws Exception; String
	 * obterEventoEntidade() throws Exception; String obterCampo() throws
	 * Exception; String obterArgumento() throws Exception; String obterAcao()
	 * throws Exception; int obterSequencial() throws Exception; }
	 */

	public interface ControleDocumento {
		void atualizar(String descricao, Date dataLimite) throws Exception;

		Parametro obterParametro() throws Exception;

		int obterSequencial() throws Exception;

		String obterDescricao() throws Exception;

		Date obterDataLimite() throws Exception;
	}

	public interface Indicador {
		void atualizar(String descricao, int peso) throws Exception;

		Parametro obterParametro() throws Exception;

		int obterSequencial() throws Exception;

		String obterTipo() throws Exception;

		String obterDescricao() throws Exception;

		int obterPeso() throws Exception;

		boolean eExcludente() throws Exception;
	}

	void adicionarControleDocumento(String descricao, Date dataLimite)
			throws Exception;

	void adicionarConfiguracao(String eventoEntidade, String campo,
			String argumento, String acao) throws Exception;

	void adicionarConsistencia(String operando1, String operador,
			String operando2, String mensagem, int regra) throws Exception;

	void adicionarIndicador(String tipo, String descricao, int peso,
			String excludente) throws Exception;

	void adicionarFeriado(String descricao, Date data) throws Exception;

	Collection obterFeriados() throws Exception;

	Collection<Consistencia> obterConsistencias() throws Exception;

	Map obterIndicadores(String tipo) throws Exception;

	Map obterControleDocumentos() throws Exception;

	Indicador obterIndicador(String tipo, int seq) throws Exception;

	ControleDocumento obterControleDocumento(int seq) throws Exception;

	void removerControleDocumento(ControleDocumento controle) throws Exception;

	void removerIndicador(Indicador indicador) throws Exception;

	//Configuracao obterConfiguracao(int sequencial) throws Exception;

	Feriado obterFeriado(int id) throws Exception;

	Consistencia obterConsistencia(int id, int regra) throws Exception;

	void removerFeriado(Feriado feriado) throws Exception;

	void removerConsistencia(Consistencia consistencia, int regra)
			throws Exception;

	void excluirControleDocumento(ControleDocumento controle) throws Exception;

}