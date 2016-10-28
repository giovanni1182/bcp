package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface AuditorExterno extends Entidade {
	public interface Cliente {
		void atualizar(Aseguradora aseguradora, double honorarios,
				Date dataIncio, Date dataFim, String outrosServicos)
				throws Exception;

		AuditorExterno obterAuditor() throws Exception;

		int obterId() throws Exception;

		Aseguradora obterAseguradora() throws Exception;

		double obterHonorarios() throws Exception;

		Date obterDataInicio() throws Exception;

		Date obterDataFim() throws Exception;

		String obterOutrosServicos() throws Exception;
	}

	public interface Servico {
		void atualizar(String servico, Date dataContrato, double honorarios,
				String periodo) throws Exception;

		AuditorExterno obterAuditor() throws Exception;

		Aseguradora obterAseguradora() throws Exception;

		int obterId() throws Exception;

		String obterServico() throws Exception;

		Date obterDataContrato() throws Exception;

		double obterHonorarios() throws Exception;

		String obterPeriodo() throws Exception;
	}

	void adicionarCliente(Aseguradora aseguradora, double honorarios,
			Date dataIncio, Date dataFim, String outrosServicos)
			throws Exception;

	Cliente obterCliente(int id) throws Exception;

	Collection obterClientes() throws Exception;

	void removerCliente(Cliente cliente) throws Exception;

	void adicionarServico(Aseguradora aseguradora, String servico,
			Date dataContrato, double honorarios, String periodo)
			throws Exception;

	Servico obterServico(Aseguradora aseguradora, int id) throws Exception;

	Collection obterServicos(Aseguradora aseguradora) throws Exception;

	void removerServico(Servico servico) throws Exception;
	
	boolean permiteExcluir() throws Exception;
	
	boolean permiteAtualizar() throws Exception;

	void atualizarAseguradora(Aseguradora aseguradora) throws Exception;

	void atualizarRamo(String ramo) throws Exception;

	void adicionarNovoRamo(String ramo) throws Exception;

	Aseguradora obterAseguradora() throws Exception;

	String obterRamo() throws Exception;

	void incluir() throws Exception;

	Collection obterNomeRamos() throws Exception;
}