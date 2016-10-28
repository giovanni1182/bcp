package com.gvs.crm.component;

import com.gvs.crm.model.Evento;

import infra.view.Block;
import infra.view.Label;

public class EventoSuperiorLabel extends Block 
{
	public EventoSuperiorLabel(Evento evento) throws Exception 
	{
		super(Block.HORIZONTAL);
		Evento superior = evento.obterSuperior();
		if (superior == null) 
			this.add(new Label("ningún"));
		else 
			this.add(new EventoLink(superior));
		
		/*if (evento.obterId() > 0 && evento.permiteAtualizar()) 
		{
			this.add(new Space());
			Link atualizarSuperiorLink = new Link(new Image("replace.gif"),
					new Action("selecionarEventoSuperior"));
			atualizarSuperiorLink.getAction().add("id", evento.obterId());
			this.add(atualizarSuperiorLink);
		}*/
	}
}