package com.gvs.crm.model;

public interface VariacaoIPC extends Evento
{
	public void incluir() throws Exception;
	public void atribuirMes(int mes) throws Exception;
	public void atribuirAno(int ano) throws Exception;
	public void atribuirVariacao(double cotacao) throws Exception;
	
	public void atualizarMes(int mes) throws Exception;
	public void atualizarAno(int ano) throws Exception;
	public void atualizarVariacao(double cotacao) throws Exception;
	
	public int obterMes() throws Exception;
	public int obterAno() throws Exception;
	public double obterVariacao() throws Exception;
}
