package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface TarefaHome {
	/*
	 * Obter todas as tarefas: - do responsável passado. - com data de início
	 * previsto inferior ou igual a data2. - com data de conclusão prevista
	 * superior ou igual a data1.
	 * 
	 * Ordenadas por prioridade e data de início previsto e data de criação
	 */
	Collection obterTarefasPorResponsavel(Usuario responsavel, Date data1,
			Date data2) throws Exception;
}