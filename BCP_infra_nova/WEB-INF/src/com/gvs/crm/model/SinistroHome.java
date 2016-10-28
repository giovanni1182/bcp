package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface SinistroHome 
{
	Sinistro obterSinistro(String numero) throws Exception;
	Collection<Entidade> obterAseguradorasCentralRisco(String nomeAsegurado, String documento) throws Exception;
	void atualizarDatas() throws Exception;
	void modificarAfetadoPorSinistro() throws Exception;
	void manutSinistro() throws Exception;
	Collection<Sinistro> obterSinistros(Aseguradora aseguradora, String secao, String situacao, Date dataInicio, Date dataFim, String nomeAsegurado, String situacaoSinistro) throws Exception;
	Collection<Sinistro> obterMaiores(Aseguradora aseguradora, String tipoPessoa, Date dataInicio, Date dataFim, int qtde, String situacao, String secao, double monto, String modalidade, String tipoInstrumento) throws Exception;
}