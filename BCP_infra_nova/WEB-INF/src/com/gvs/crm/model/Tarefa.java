package com.gvs.crm.model;

import java.util.Date;

public interface Tarefa extends Evento {
	static final String EVENTO_INICIADO = "iniciado";

	/*
	 * Conclusão da tarefa.
	 * 
	 * Se a tarefa não possui sub-eventos. - Atualizar a data efetiva de
	 * conclusão. - Adicionar um comentário com o título "Concluído por <usuário
	 * atual>". Se a tarefa possui sub-eventos. - Atualizar a data efetiva de
	 * conclusão. - Adicionar um comentário com o título "Concluído". Fimse. Se
	 * a tarefa possui um super-evento. - Concluir o super-evento se todos os
	 * seus sub-eventos estão concluídos.
	 */
	void concluir(Date dataEfetivaConclusao, String comentario)
			throws Exception;

	/*
	 * Início da tarefa
	 * 
	 * Se a tarefa não possui sub-eventos. - Atualizar a data efetiva de início. -
	 * Adicionar um comentário com o título "Iniciada por <usuário atual>". Se a
	 * tarefa possui sub-eventos. - Atualizar a data efetiva de início. -
	 * Adicionar um comentário com o título "Iniciada". Fimse. Se a tarefa
	 * possui uma tarefa com super-evento - Iniciar a super-tarefa caso esteja
	 * da fase "pendente".
	 */
	void iniciar(Date dataEfetivaInicio) throws Exception;

	/*
	 * Obter a data efetiva de conclusão da tarefa.
	 */
	Date obterDataEfetivaConclusao() throws Exception;

	/*
	 * Obter a data efetiva de início da tarefa.
	 */
	Date obterDataEfetivaInicio() throws Exception;

	/*
	 * A duração efetiva será calculada através da diferença entre a data de
	 * início e a data de conclusão da tarefa.
	 */
	long obterDuracaoEfetiva() throws Exception;

	/*
	 * A tarefa permite ser iniciada se: - não possui sub-eventos. - a tarefa
	 * está na fase "pendente". - o usuário atual é o responsável pela tarefa.
	 */
	boolean permiteIniciar() throws Exception;

	/*
	 * A tarefa permite ser concluída se: - não possui sub-eventos. - a tarefa
	 * está na fase "iniciada". - o usuário atual é o responsável pela tarefa.
	 */
	boolean permiteConcluir() throws Exception;
}