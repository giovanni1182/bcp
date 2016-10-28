package com.gvs.crm.model;

import java.util.Date;

public interface Tarefa extends Evento {
	static final String EVENTO_INICIADO = "iniciado";

	/*
	 * Conclus�o da tarefa.
	 * 
	 * Se a tarefa n�o possui sub-eventos. - Atualizar a data efetiva de
	 * conclus�o. - Adicionar um coment�rio com o t�tulo "Conclu�do por <usu�rio
	 * atual>". Se a tarefa possui sub-eventos. - Atualizar a data efetiva de
	 * conclus�o. - Adicionar um coment�rio com o t�tulo "Conclu�do". Fimse. Se
	 * a tarefa possui um super-evento. - Concluir o super-evento se todos os
	 * seus sub-eventos est�o conclu�dos.
	 */
	void concluir(Date dataEfetivaConclusao, String comentario)
			throws Exception;

	/*
	 * In�cio da tarefa
	 * 
	 * Se a tarefa n�o possui sub-eventos. - Atualizar a data efetiva de in�cio. -
	 * Adicionar um coment�rio com o t�tulo "Iniciada por <usu�rio atual>". Se a
	 * tarefa possui sub-eventos. - Atualizar a data efetiva de in�cio. -
	 * Adicionar um coment�rio com o t�tulo "Iniciada". Fimse. Se a tarefa
	 * possui uma tarefa com super-evento - Iniciar a super-tarefa caso esteja
	 * da fase "pendente".
	 */
	void iniciar(Date dataEfetivaInicio) throws Exception;

	/*
	 * Obter a data efetiva de conclus�o da tarefa.
	 */
	Date obterDataEfetivaConclusao() throws Exception;

	/*
	 * Obter a data efetiva de in�cio da tarefa.
	 */
	Date obterDataEfetivaInicio() throws Exception;

	/*
	 * A dura��o efetiva ser� calculada atrav�s da diferen�a entre a data de
	 * in�cio e a data de conclus�o da tarefa.
	 */
	long obterDuracaoEfetiva() throws Exception;

	/*
	 * A tarefa permite ser iniciada se: - n�o possui sub-eventos. - a tarefa
	 * est� na fase "pendente". - o usu�rio atual � o respons�vel pela tarefa.
	 */
	boolean permiteIniciar() throws Exception;

	/*
	 * A tarefa permite ser conclu�da se: - n�o possui sub-eventos. - a tarefa
	 * est� na fase "iniciada". - o usu�rio atual � o respons�vel pela tarefa.
	 */
	boolean permiteConcluir() throws Exception;
}