package com.gvs.crm.model;

import java.util.Collection;

public interface InscricaoHome {
	boolean verificarInscricao(Entidade entidade, String inscricao)
			throws Exception;

	boolean verificarInscricao(AuxiliarSeguro auxiliar, String atividade,
			String inscricaoStr) throws Exception;

	Collection obterInscricoes(String entidade, String fase, int pagina)
			throws Exception;
	
	Entidade verificarInscricao2(AuxiliarSeguro auxiliar,String atividade, String inscricaoStr) throws Exception;

}