package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface EventoHome {
	Collection obterTeste(int pagina) throws Exception;

	Collection localizarApolices(String pesquisa, String aseguradora,
			String secao) throws Exception;

	Collection localizarForuns(String assunto, String tipo) throws Exception;

	Collection localizarEventos(String pesquisa) throws Exception;

	Collection localizarEventos(String pesquisa, String classe)
			throws Exception;
	
	Collection obterAgendas(String fase) throws Exception;

	String obterClasseDescricao(String classe) throws Exception;

	String obterClasseGrupo(String classe) throws Exception;

	Collection obterAgendas(boolean fasePendente) throws Exception;

	Collection obterClasses(Evento superior) throws Exception;

	Collection obterClassesDescricao(Evento superior) throws Exception;

	Collection<Evento> obterCompromissos(Usuario responsavel, Date dataPrevistaInicio)
			throws Exception;

	Evento obterEventoPorId(long id) throws Exception;

	Collection<Evento> obterEventosInferiores(Evento superior) throws Exception;

	Collection obterEventosPorCriador(Usuario criador) throws Exception;

	Collection obterEventosPorCriadorHistorico(Usuario criador, int pagina)	throws Exception;

	Collection obterEventosPorDestino(Entidade entidade) throws Exception;

	Collection obterEventosPorDestinoHistorico(Entidade destino, int pagina)throws Exception;

	Collection obterEventosPorOrigem(Entidade entidade) throws Exception;

	Collection obterEventosPorOrigemHistorico(Entidade origem, int pagina) throws Exception;

	Collection obterEventosPorResponsavel(Entidade responsavel)	throws Exception;

	Collection obterEventosPorResponsavelHistorico(Entidade responsavel, int pagina)throws Exception;

	Collection obterEventosPorResponsavelTecnico(Entidade responsavelTecnico)throws Exception;

	Collection obterEventosAgenda(String mesAno, int pagina) throws Exception;

	Collection obterEventosSuperiores(Evento evento) throws Exception;

	Collection obterMesAnoAgendas() throws Exception;

	Collection obterMesAnoBalanco(Aseguradora aseguradora) throws Exception;

	Evento obterEventoSuperior(Evento evento) throws Exception;

	Collection obterTiposEntidadeInscricao() throws Exception;

	Collection obterInscricoes(int pagina) throws Exception;

	Collection obterInscricoesPendentes() throws Exception;

	Collection obterPendencias(Usuario responsavel) throws Exception;

	Collection obterPossiveisSuperiores(Evento evento) throws Exception;

	Collection obterTarefasAtrasadas(Usuario responsavel, Date data)
			throws Exception;

	Collection obterTarefasPendentes(Usuario responsavel) throws Exception;

	Collection obterTarefasPendentes(Usuario responsavel, Date data1, Date data2)
			throws Exception;

	Collection obterTiposEventos(String classeEvento) throws Exception;

	Collection obterComponentesParaImpressao(String classeEvento)
			throws Exception;

	Collection obterFases(String classeEvento) throws Exception;

	String obterNomeFase(String fase) throws Exception;

	MovimentacaoFinanceiraConta obterMovimentacao(Entidade entidade,
			Entidade seguradora, String mesAno) throws Exception;

	double obterTotalizacaoCredito(Entidade entidade, String mesAno)
			throws Exception;

	double obterTotalizacaoCredito() throws Exception;

	double obterTotalizacaoDebito(Entidade entidade, String mesAno)
			throws Exception;

	double obterTotalizacaoDebito() throws Exception;

	double obterTotalizacaoSaldoAnterior(Entidade entidade, String mesAno)
			throws Exception;

	double obterTotalizacaoSaldoAnterior() throws Exception;

	double obterTotalizacaoSaldoAnteriorMoedaEstrangeira(Entidade entidade,
			String mesAno) throws Exception;

	double obterTotalizacaoSaldoAnteriorMoedaEstrangeira() throws Exception;

	double obterTotalizacaoSaldoMoedaEstrangeira(Entidade entidade,
			String mesAno) throws Exception;

	double obterTotalizacaoSaldoMoedaEstrangeira() throws Exception;

	//************************** Só para o cartão
	// *********************************************
	Collection obterMovimentoContabil(Entidade entidade, Date mes)
			throws Exception;

	Collection obterContasPagarReceber(Entidade entidade, Date dia)
			throws Exception;

	Collection obterFluxoCaixa(Entidade entidade) throws Exception;

	double obterTotalCreditoPrevisto(Entidade entidade) throws Exception;

	double obterTotalCreditoRealizado(Entidade entidade) throws Exception;

	double obterTotalDebitoPrevisto(Entidade entidade) throws Exception;

	double obterTotalDebitoRealizado(Entidade entidade) throws Exception;

	double obterTotalizacao(Entidade entidade, String mesAno) throws Exception;
}