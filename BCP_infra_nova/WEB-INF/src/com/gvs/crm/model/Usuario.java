package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface Usuario extends Entidade 
{
	static final String ADMINISTRADOR = "Administrador";
	static final String NIVEL1 = "Nivel 1";
	static final String INTENDENTE_ICORAS = "Intendente ICORAS";
	static final String INTENDENTE_IETA = "Intendente IETA";
	static final String DIVISAO_CONTROL_AUXILIARES = "División de Control de Auxiliares";
	static final String ANALISTA_AUXILIARES = "Analista de Auxiliares";
	static final String DIVISAO_CONTROL_REASEGUROS = "División de Control de Reaseguros";
	static final String ANALISTA_REASEGUROS= "Analista de Reaseguros";
	static final String SUPERINTENDENTE = "Superintendente";
	static final String COORDENACAO_ADSCRITORIA = "Coordinación de la Adscritoria";
	static final String INTENDENDE_IAL = "Intendente IAL";
	static final String INTENDENTE_ICF = "Intendente ICF";
	static final String DIVISAO_ADMINISTRATIVA = "División Administrativa";
	static final String DIVISAO_LAVAGEM_DINHEIRO = "División Lavado de Dinero";
	static final String DIVISAO_DEFESA_USUARIO = "División de Defensa del Usuario";
	static final String DIVISAO_INFORMATICA = "División de Informática";
	static final String DIVISAO_SERVICOS_ADMIN = "División de Dictámines y Servicios Administrativos";
	static final String DIVISAO_PRECESSOS = "División de Sumários Administrativos y Procesos Judiciales";
	static final String DIVISAO_ANALISE_FINANCEIRA = "División de Análisis Financiero";
	static final String DIVISAO_AUDITORIA = "División de Auditoría";
	static final String DIVISAO_ESTUDOS_ATUAIS = "División de Estudios Actuariales";
	static final String DIVISAO_ESTUDOS_TECNICOS = "División de Estudios Técnicos";
	static final String BANCO_DE_DADOS = "Base de Datos";
	
	//static final String NIVEL2 = "Nivel 2";
	//static final String EXTERNO = "Externo";
	
	
	void atribuirChave(String chave) throws Exception;

	void atribuirAlcada(double alcada) throws Exception;

	void atualizarAlcada(double alcada) throws Exception;

	void atualizarSenha(String senhaAtual, String novaSenha1, String novaSenha2)
			throws Exception;

	void converterEmPessoa() throws Exception;

	double obterAlcada() throws Exception;

	String obterChave() throws Exception;
	String obterSenha() throws Exception;

	Collection<Evento> obterCompromissos(Date date) throws Exception;

	Collection obterEntidadeDeResponsabilidade() throws Exception;

	Collection obterEventosCriados() throws Exception;

	Collection obterEventosCriadosHistorico(int pagina) throws Exception;

	Collection obterEventosPendentes() throws Exception;

	Collection obterTarefasAtrasadas(Date dataLimite) throws Exception;

	Collection obterTarefasPendentes() throws Exception;

	Collection obterTarefasPendentes(Date inicio, Date termino)
			throws Exception;

	boolean permiteAtualizarSenha() throws Exception;

	boolean permiteConverterParaPessoa() throws Exception;

	boolean possuiResponsabilidades() throws Exception;

	boolean possuiSenha() throws Exception;

	boolean verificarSenha(String senha) throws Exception;

	Collection verificarAgendas() throws Exception;
	
	Collection obterEventosVinculador() throws Exception;
	
	boolean estaVisivel() throws Exception;
	
	void excluirLogicamente()throws Exception;
	
	String obterNivel() throws Exception;
	
	void atualizarNivel(String nivel) throws Exception;
	
	boolean permiteExcluir() throws Exception;
	
	void atualizarLogPendente() throws Exception;
	String obterTempoConectado(Date dataInicio, Date dataFim) throws Exception;
	Map<Long,Aseguradora> obterAseguradorasResEscaneada() throws Exception;
	void addResEscaneada(Aseguradora aseg) throws Exception;
	//Collection<Usuario> obterUsuariosDepartamento() throws Exception;
}