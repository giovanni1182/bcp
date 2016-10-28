package com.gvs.crm.component;

import com.gvs.crm.model.Evento;

import infra.view.Block;
import infra.view.Label;
import infra.view.Space;

public class CriacaoLabel extends Block {
	public CriacaoLabel(Evento evento) throws Exception {
		super(Block.HORIZONTAL);
		this.add(new EntidadeNomeLink(evento.obterCriador()));
		this.add(new Space(2));
		this.add(new Label("en"));
		this.add(new Space(2));
		this.add(new DateLabel(evento.obterCriacao()));
	}
}