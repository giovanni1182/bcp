package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface AgendaMovimentacao extends Evento {

	public static String AGENDADA = "agendado";
	public static String TOTAL = "Total";
	public static String PARCIAL = "Parcial";

	void atribuirMesMovimento(int mes) throws Exception;

	void atribuirAnoMovimento(int ano) throws Exception;
	void atualizarValidacao(String mod) throws Exception;

	int obterMesMovimento() throws Exception;

	int obterAnoMovimento() throws Exception;
	
	String obterValidacao() throws Exception;

	boolean permiteValidar() throws Exception;

	boolean permiteEnviarBcp() throws Exception;

	void enviarBcp(String comentario) throws Exception;

	Collection<String> validaArquivo(String nomeArquivo) throws Exception;

	Collection verificarApolice(String nomeArquivo) throws Exception;
	
	boolean existeAgendaNoPeriodo(int mes, int ano, Entidade asseguradora,
			String tipo) throws Exception;
	
	boolean permiteAtualizar() throws Exception;
	
	int obterQtdeRegistrosB() throws Exception;
	int obterQtdeRegistrosA() throws Exception;
	void atualizarQtdeB(int total) throws Exception;
	void atualizarQtdeA(int total) throws Exception;
	Date obterDataModificacaoArquivo() throws Exception;
	void atualizarEspecial(String especial) throws Exception;
	String obterEspecial() throws Exception;
	void atualizarInscricaoEspecial(String especial) throws Exception;
	void atualizarSuplementosEspecial(String especial) throws Exception;
	void atualizarCapitalEspecial(String especial) throws Exception;
	void atualizarDataEspecial(String especial) throws Exception;
	void atualizarDocumentoEspecial(String especial) throws Exception;
	void atualizarApAnteriorEspecial(String especial) throws Exception;
	void atualizarEndosoApolice(String especial) throws Exception;
	String obterInscricaoEspecial() throws Exception;
	String obterSuplementoEspecial() throws Exception;
	String obterCapitalEspecial() throws Exception;
	String obterDataEspecial() throws Exception;
	String obterDocumentoEspecial() throws Exception;
	String obterApAnteriorEspecial() throws Exception;
	String obterEndosoApolice() throws Exception;
	boolean eEspecial() throws Exception;
	void atualizaUltimaAgenda(String tipo) throws Exception;
}