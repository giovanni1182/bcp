package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface Processamento extends Evento
{
	public interface Agenda
	{
		Processamento obterProcessamento();
		public int obterSeq();
		public String obterNomeArquivo();
		public String obterMensagem();
		public int obterMes();
		public int obterAno();
		Evento obterEvento();
		Entidade obterAseguradora();
		boolean agendaComErro();
		boolean podeForcar() throws Exception;
	}
	
	void incluir() throws Exception;
	void atribuirData(Date data);
	Date obterData();
	void addAgenda(String nomeArquivo, String mensagem, Evento evento) throws Exception;
	Collection<Agenda> obterAgendas() throws Exception;
	Agenda instanciarAgenda(int seq) throws Exception;
	boolean existeAgenda(int mes, int ano, int erro) throws Exception;
	void forcarProcessamento(String nomeArquivo) throws Exception;
}
