package com.gvs.crm.component;

import com.gvs.crm.model.Evento;

import infra.view.Block;
import infra.view.Image;

public class EventoImage extends Block {
	public EventoImage(Evento evento) throws Exception {
		super(Block.HORIZONTAL);
		this.add(new Image(evento.obterIcone()));
		if (evento.obterPrioridade() == Evento.PRIORIDADE_ALTA)
			this.add(new Image("exclamation.gif"));
		else if (evento.obterPrioridade() == Evento.PRIORIDADE_BAIXA)
			this.add(new Image("downarrow.gif"));
		if(evento.obterFase()!=null)
		{
			if (evento.obterFase().equals(Evento.EVENTO_CONCLUIDO))
				this.add(new Image("check.gif"));
		}
	}
}