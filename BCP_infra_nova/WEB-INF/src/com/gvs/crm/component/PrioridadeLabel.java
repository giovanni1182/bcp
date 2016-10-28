package com.gvs.crm.component;

import com.gvs.crm.model.Evento;

import infra.view.Label;

public class PrioridadeLabel extends Label {
	public PrioridadeLabel(int prioridade) {
		super("");
		if (prioridade == Evento.PRIORIDADE_ALTA)
			this.setValue("Alta");
		if (prioridade == Evento.PRIORIDADE_NORMAL)
			this.setValue("Normal");
		if (prioridade == Evento.PRIORIDADE_BAIXA)
			this.setValue("Baixa");
	}
}