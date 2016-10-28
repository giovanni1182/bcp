package com.gvs.crm.component;

import com.gvs.crm.model.Evento;

import infra.view.Select;

public class PrioridadeSelect extends Select {
	public PrioridadeSelect(String nome, int prioridade, boolean habilitado) {
		super(nome, 1);
		this.add("Alta", Integer.toString(Evento.PRIORIDADE_ALTA),
				prioridade == Evento.PRIORIDADE_ALTA);
		this.add("Normal", Integer.toString(Evento.PRIORIDADE_NORMAL),
				prioridade == Evento.PRIORIDADE_NORMAL || prioridade == 0);
		this.add("Baja", Integer.toString(Evento.PRIORIDADE_BAIXA),
				prioridade == Evento.PRIORIDADE_BAIXA);
		this.setEnabled(habilitado);
	}
}