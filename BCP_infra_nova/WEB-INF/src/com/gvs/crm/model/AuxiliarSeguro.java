package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface AuxiliarSeguro extends Entidade {
	public interface Ramo {
		AuxiliarSeguro obterAuxiliarSeguro() throws Exception;

		int obterSeq() throws Exception;

		String obterRamo() throws Exception;
	}

	void atualizarAseguradora(Aseguradora aseguradora) throws Exception;

	Aseguradora obterAseguradora() throws Exception;
	Collection obterAseguradoras2(Date dataInicio, Date dataFim) throws Exception;

	Collection obterAseguradoras(Date dataInicio, Date dataFim)
			throws Exception;
	
	Collection<Aseguradora> obterAseguradorasCorredor(Date dataInicio, Date dataFim) throws Exception;

	//void atualizarRamo(String ramo) throws Exception;

	void adicionarNovoRamo(String ramo) throws Exception;

	/*
	 * String obterRamo() throws Exception;
	 * 
	 * void incluir() throws Exception;
	 */

	Collection obterNomeRamos() throws Exception;

	Ramo obterRamo(int seq) throws Exception;

	Collection obterRamos() throws Exception;

	void excluirRamo(AuxiliarSeguro.Ramo ramo) throws Exception;
	boolean permiteAtualizar() throws Exception;
	boolean permiteAtualizarEndereco() throws Exception;
	String permiteAtualizarStr() throws Exception;
	
	boolean permiteExcluir() throws Exception;

}