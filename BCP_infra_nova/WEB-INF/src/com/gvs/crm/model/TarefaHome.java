package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface TarefaHome {
	/*
	 * Obter todas as tarefas: - do respons�vel passado. - com data de in�cio
	 * previsto inferior ou igual a data2. - com data de conclus�o prevista
	 * superior ou igual a data1.
	 * 
	 * Ordenadas por prioridade e data de in�cio previsto e data de cria��o
	 */
	Collection obterTarefasPorResponsavel(Usuario responsavel, Date data1,
			Date data2) throws Exception;
}