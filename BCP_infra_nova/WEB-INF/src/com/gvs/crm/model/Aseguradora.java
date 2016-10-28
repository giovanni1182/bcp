package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;



public interface Aseguradora extends Entidade
{
	public interface Reaseguradora
	{
		void atualizar(Entidade reaseguradora, Entidade corretora,
				String tipoContrato, Date dataVencimento, int participacao,
				String observacao) throws Exception;

		Aseguradora obterAseguradora() throws Exception;

		int obterId() throws Exception;

		Entidade obterReaseguradora() throws Exception;

		Entidade obterCorretora() throws Exception;

		String obterTipoContrato() throws Exception;

		Date obterDataVencimento() throws Exception;

		int obterParticipacao() throws Exception;

		String obterObservacao() throws Exception;
	}

	public interface Acionista {
		void atualizar(String acionista, int quantidade, String tipo)
				throws Exception;

		Aseguradora obterAseguradora() throws Exception;

		int obterId() throws Exception;

		String obterAcionista() throws Exception;

		int obterquantidade() throws Exception;

		String obtertipo() throws Exception;
	}

	public interface Filial {
		void atualizar(String filial, String tipo, String telefone,
				String cidade, String endereco, String email) throws Exception;

		Aseguradora obterAseguradora() throws Exception;

		int obterId() throws Exception;

		String obterFilial() throws Exception;

		String obterTipo() throws Exception;

		String obterTelefone() throws Exception;

		String obterCidade() throws Exception;

		String obterEndereco() throws Exception;

		String obterEmail() throws Exception;
	}

	public interface Fusao {
		void atualizar(String empresa, Date data) throws Exception;

		Aseguradora obterAseguradora() throws Exception;

		int obterId() throws Exception;

		String obterEmpresa() throws Exception;

		Date obterDatausao() throws Exception;
	}

	public interface Classificacao {
		Aseguradora obterAseguradora() throws Exception;

		String obterNome() throws Exception;

		String obterMes() throws Exception;
		
		String obterAno() throws Exception;

		double obterValor() throws Exception;
	}
	
	public interface MargemSolvencia
	{
		Aseguradora obterAseguradora() throws Exception;

		int obterSeq() throws Exception;

		double obterValor() throws Exception;

		String obterMesAno() throws Exception;
	}

	Collection obterAgendas() throws Exception;
	AgendaMovimentacao obterAgendaPendente(String tipo) throws Exception;
	
	public Collection obtenerPolizas(Date fecha_desde, Date fecha_hasta, int nro_pag) throws Exception;
	public Collection obtenerPlanCtas (int mes, int year)throws Exception ;
	
	/*
	 * public interface Plano { void atualizar(String ramo, String secao, String
	 * plano, Evento resolucao, Date data, String situacao, String descricao)
	 * throws Exception; Aseguradora obterAseguradora() throws Exception; int
	 * obterId() throws Exception; String obterRamo() throws Exception; String
	 * obterSecao() throws Exception; String obterPlano() throws Exception;
	 * Evento obterResolucao() throws Exception; Date obterData() throws
	 * Exception; String obterSituacao() throws Exception; String
	 * obterDescricao() throws Exception; }
	 */

	public interface Coasegurador {
		void atualizar(String codigo) throws Exception;

		Aseguradora obterAseguradora() throws Exception;

		int obterId() throws Exception;

		String obterCodigo() throws Exception;
	}

	Collection obterMeicos(String tipo) throws Exception;

	void adicionarReaseguradora(Entidade reaseguradora, Entidade corretora,
			String tipoContrato, Date dataVencimento, int participacao,
			String observacao) throws Exception;

	void adicionarClassificacao(String nome, String mes, String ano,double valor)
			throws Exception;

	Reaseguradora obterReaseguradora(int id) throws Exception;

	Collection obterReaseguradoras() throws Exception;

	void removerReaseguradora(Aseguradora.Reaseguradora reaseguradora)
			throws Exception;
	
	Collection<Apolice> obterApolicesSemAgentesPorPeridodo(Date dataInicio, Date dataFim)throws Exception;
	Collection<Entidade> obterCorredoresPorPeridodo(Date dataInicio, Date dataFim)throws Exception;
	Collection<Apolice> obterApolicesCorretor(AuxiliarSeguro corretora, Date dataInicio,Date dataFim) throws Exception;
	Collection<String> obterApolicesAcumuladas(Entidade agente, Date dataInicio,Date dataFim, boolean auxiliar) throws Exception;
	Collection<Apolice> obterApolicesProducao(Entidade agente, Date dataInicio,Date dataFim, boolean auxiliar) throws Exception;

	Collection obterCalculoMeicos(AgendaMeicos agenda) throws Exception;

	Collection obterAgentes() throws Exception;

	Collection obterApolices(Entidade agente, Date dataInicio, Date dataFim)
			throws Exception;

	Collection obterAgentesPorPeridodo(Date dataInicio, Date dataFim)
			throws Exception;

	void adicionarAcionista(String acionista, int quantidade, String tipo)
			throws Exception;

	Acionista obterAcionista(int id) throws Exception;

	Collection obterAcionistas() throws Exception;

	void removerAcionista(Aseguradora.Acionista acionista) throws Exception;

	void adicionarFilial(String filial, String tipo, String telefone,
			String cidade, String endereco, String email) throws Exception;

	Filial obterFilial(int id) throws Exception;

	Collection obterFiliais() throws Exception;

	void removerFilial(Aseguradora.Filial filial) throws Exception;

	void adicionarFusao(String empresa, Date dataFusao) throws Exception;

	Fusao obterFusao(int id) throws Exception;

	Collection obterFusoes() throws Exception;

	void removerFusao(Aseguradora.Fusao fusao) throws Exception;

	RatioPermanente obterRatioPermanente() throws Exception;

	RatioUmAno obterRatioUmAno() throws Exception;

	RatioTresAnos obterRatioTresAnos() throws Exception;

	Collection obterRatiosPermanentes() throws Exception;

	Collection obterRatiosUmAno() throws Exception;

	Collection obterRatiosTresAnos() throws Exception;

	/*
	 * void adicionarPlano(String ramo, String secao, String plano, Evento
	 * resolucao, Date data, String situacao, String descricao) throws
	 * Exception; Plano obterPlano(int id) throws Exception; void
	 * removerPlano(Aseguradora.Plano plano) throws Exception;
	 */

	Collection<Plano> obterPlanos() throws Exception;

	Collection<Plano> obterPlanosOrdenadorPorSecao() throws Exception;
	Map obterPlanosApolicesPeriodo(Date dataInicio, Date dataFim) throws Exception;
	Map obterPlanosSinistrosPeriodo(Date dataInicio, Date dataFim) throws Exception;

	void adicionarCoasegurador(String codigo) throws Exception;

	Coasegurador obterCoasegurador(int id) throws Exception;

	Collection obterCoaseguradores() throws Exception;

	void removerCoasegurador(Aseguradora.Coasegurador coasegurador)
			throws Exception;

	Collection obterCoaseguradoresAnuario() throws Exception;

	Collection obterCoaseguradorasPorGrupo(String codigo) throws Exception;

	Collection obterClassificacao(String mes, String ano) throws Exception;

	double obterMargemSolvencia() throws Exception;

	double obterCapitalGs(Entidade reaseguradora, Date dataInicio,
			Date dataFim, String tipoContrato) throws Exception;

	double obterPrimaGs(Entidade reaseguradora, Date dataInicio, Date dataFim,
			String tipoContrato) throws Exception;

	double obterComissaoGs(Entidade reaseguradora, Date dataInicio,
			Date dataFim, String tipoContrato) throws Exception;

	double obterCapitalGsCorretora(Entidade corretora, Date dataInicio,
			Date dataFim, String tipoContrato) throws Exception;

	double obterPrimaGsCorretora(Entidade corretora, Date dataInicio,
			Date dataFim, String tipoContrato) throws Exception;

	double obterComissaoGsCorretora(Entidade corretora, Date dataInicio,
			Date dataFim, String tipoContrato) throws Exception;
	
	boolean permiteExcluir() throws Exception;
	
	Collection<String> obterSecoesSinistrosNoVigenteJudicializado(Date dataInicio, Date dataFim) throws Exception;
	Collection<String> obterSecoesSinistrosVigenteJudicializado(Date dataInicio, Date dataFim) throws Exception;
	

	void excluirClassificacao(String mes, String ano) throws Exception;
	boolean permiteAtualizar() throws Exception;
	Collection<String> obterSecoesSinistrosVigente(Date dataInicio, Date dataFim) throws Exception;
	Collection<String> obterSecoesSinistrosNoVigente(Date dataInicio, Date dataFim) throws Exception;
	AgendaMovimentacao obterUltimaAgendaMCO() throws Exception;
	AgendaMovimentacao obterUltimaAgendaMCI() throws Exception;
	AgendaMovimentacao obterAgendaMCISeguinte() throws Exception;
	Collection<Apolice> obterApolicesPorSecao(ClassificacaoContas cContas, Date dataInicio, Date dataFim,String situacao,String situacao2) throws Exception;
	Collection<Apolice> obterApolicesPorModalidade(Date dataInicio, Date dataFim, String secao, String modalidade) throws Exception;
	boolean existeAgendaNoPeriodo(int mes, int ano, String tipo) throws Exception;
	Map obterMargensSolvencia() throws Exception;
	MargemSolvencia obterMargemSolvencia(int seq) throws Exception;
	void adicionarMargemSolvencia(String mesAno, double valor) throws Exception;
	void incluir() throws Exception;
	int obterQtdeApolicesPorPeriodo(Date dataInicio, Date dataFim, String secao, String situacaoSeguro)throws Exception;
	int obterQtdeApolicesPorPeriodo(Date dataInicio, Date dataFim, String secao) throws Exception;
	Collection<String> obterQtdeSinistrosPorPeriodo(Date dataInicio, Date dataFim) throws Exception;
	Collection<String> obterQtdeApolicesPorPeriodo(Date dataInicio, Date dataFim, boolean valores) throws Exception;
	
	String[] obterQtdeApolicesPorPeriodoNOVO(Date dataInicio, Date dataFim, boolean valores, String secao, String modalidade, String ramo) throws Exception;
	String[] obterQtdeSinistrosPorPeriodoNOVO(Date dataInicio, Date dataFim, boolean valores, String secao, String modalidade, String ramo) throws Exception;
	
	//String obterValoresSinistrosPorPeriodoRelSecaoAnual(Date dataInicio, Date dataFim, String secao) throws Exception;
	int obterQtdeSinistrosPorPeriodo(Date dataInicio, Date dataFim, String secao, String situacaoSeguro)throws Exception;
	int obterQtdeSinistrosPorPeriodo(Date dataInicio, Date dataFim, String secao) throws Exception;
	 
	double obterPorcentagemSinistros(int totalPeriodo) throws Exception;
	double obterPorcentagemApolices(int totalPeriodo) throws Exception;
	String obterComentarioControle() throws Exception;
	Date obterUltimoEnvioCorreio() throws Exception;
	void atualizarComentarioControle(String comentario) throws Exception;
	void atualizarUltimoEnvioCorreio(Date data) throws Exception;
	
	//Map<String,String> obterNomePlanosPeriodo(Date dataInicio, Date dataFim, String plano, boolean admin) throws Exception;
	Map<String,String> obterNomePlanosPeriodo(Date dataInicio, Date dataFim, String plano, boolean admin, String ramo) throws Exception;
	//Map<String,String> obterNomePlanosSinistrosPorPeriodo(Date dataInicio, Date dataFim, String plano) throws Exception;
	Map<String,String> obterNomePlanosSinistrosPorPeriodo(Date dataInicio, Date dataFim, String plano, boolean admin, String ramo) throws Exception;
	
	String[] obterQtdeApolicesPorModalidade(Date dataInicio, Date dataFim, String secao, String modalidade) throws Exception;
	Collection<String> obterQtdePorTipoPessoa(Date dataInicio, Date dataFim, String ramo, String secao, String modalidade) throws Exception;
	Collection<String> obterPlanosPorTipoPessoa(Date dataInicio, Date dataFim, String ramo, String secao, String modalidade) throws Exception;
	String obterQtdePorTipoPessoa2(Date dataInicio, Date dataFim, String ramo, String secao, String modalidade) throws Exception;
	void atualizarGrupoAlertaTrempana(String grupo) throws Exception;
	String obterGrupoAlertaTrempana() throws Exception;
	
}