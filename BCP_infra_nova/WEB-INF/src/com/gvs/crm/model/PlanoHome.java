package com.gvs.crm.model;

import java.util.Collection;

public interface PlanoHome {
	Plano obterPlano(String codigo) throws Exception;

	Collection obterNomesRamo() throws Exception;

	Collection<String> obterNomesPlano() throws Exception;

	Collection<String> obterNomesSecao() throws Exception;

	Collection obterNomesSituacao() throws Exception;

	Collection obterPlanos() throws Exception;
	
	Collection obterPlanos(String numero) throws Exception;
	
	Collection<Plano> localizarPlanos(String ramo, String secao, String codigo, String situacao, Aseguradora aseguradora) throws Exception;
	Collection<Plano> localizarPlanos(String ramo, String secao, String codigo, String situacao, Aseguradora aseguradora, boolean especial, boolean modificado) throws Exception;
	void atualizarCodigo(String nomeAntigo, String nomeNovo) throws Exception;
	void atualizarSecao(String nomeAntigo, String nomeNovo) throws Exception;
	boolean permiteExcluirSecao(String secao) throws Exception;
	boolean permiteExcluirCodigo(String codigo) throws Exception;
	
	Collection<String> obterSecoesPorRamo(String ramo) throws Exception;
	Collection<String> obterModalidadePorSecao(String secao) throws Exception;
	void atualizarSegmentoRamo(String ramo, String secao) throws Exception;
	void atualizarSegmentoSecao(String secao, String modalidade) throws Exception;
	String obterSegRamo(String secao) throws Exception;
	String obterSegSecao(String modalidade) throws Exception;
	void manutPlano() throws Exception;
	Collection<Plano> obterPlanosEspeciais() throws Exception;
	Plano obterPlanoEspecial(String identificador) throws Exception;
}