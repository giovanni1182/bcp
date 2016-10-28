package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface Inscricao extends Evento
{

	public static String SUPERINTENDENTE = "superintendente";

	public static String APROVADA = "aprovado";

	public static String REJEITADA = "rejeitado";

	public static String SUSPENSA = "suspenso";

	public static String CANCELADA = "cancelado";

	public interface Ramo
	{
		Inscricao obterEvento() throws Exception;
		int obterSeq() throws Exception;
		String obterRamo() throws Exception;
	}
	
	public interface Suspensao
	{
		Inscricao obterEvento() throws Exception;
		Date obterData() throws Exception;
	}

	void atualizarInscricao(String inscricao) throws Exception;

	void atualizarNumeroResolucao(String resolucao) throws Exception;

	void atualizarDataResolucao(Date dataResolucao) throws Exception;

	void atualizarDataValidade(Date dataValidade) throws Exception;

	void atualizarSituacao(String situacao) throws Exception;

	void atualizarDataEmissao(Date data) throws Exception;

	void atualizarDataVencimento(Date data) throws Exception;

	void atualizarAseguradora(Aseguradora aseguradora) throws Exception;

	void atualizarAgente(Entidade agente) throws Exception;

	void atualizarApolice(Apolice apolice) throws Exception;

	void atualizarNumeroApolice(String numero) throws Exception;

	void atualizarNumeroSecao(String numero) throws Exception;
	void atualizarCesion(int numero) throws Exception;

	void incluir() throws Exception;

	boolean validarResolucao(String renovacao) throws Exception;

	void adicionarDocumentoVinculado(DocumentoProduto documento)
			throws Exception;

	Collection obterDocumentosVinculados() throws Exception;

	String obterInscricao() throws Exception;

	String obterNumeroResolucao() throws Exception;

	Date obterDataResolucao() throws Exception;

	Date obterDataValidade() throws Exception;

	String obterSituacao() throws Exception;
	
	int obterCesion() throws Exception;

	Aseguradora obterAseguradora() throws Exception;

	Date obterDataVencimento() throws Exception;

	Date obterDataEmissao() throws Exception;

	Entidade obterAgente() throws Exception;

	Apolice obterApolice() throws Exception;

	String obterNumeroApolice() throws Exception;

	String obterNumeroSecao() throws Exception;

	void atualizarRamo(String ramo) throws Exception;

	void adicionarNovoRamo(String ramo) throws Exception;

	String obterRamo() throws Exception;

	Collection obterNomeRamos() throws Exception;

	Collection obterRamos() throws Exception;

	Ramo obterRamo(int seq) throws Exception;

	void excluirRamo(Inscricao.Ramo ramo) throws Exception;

	void exclurInscricaoVinculada(DocumentoProduto documento) throws Exception;
	void addSuspensao(Date data) throws Exception;
	Collection<Suspensao> obterSuspensoes() throws Exception;
}