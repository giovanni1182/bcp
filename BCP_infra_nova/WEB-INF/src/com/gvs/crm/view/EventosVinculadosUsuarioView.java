package com.gvs.crm.view;

import java.util.Iterator;

import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Log;
import com.gvs.crm.model.Usuario;

import infra.control.Action;
import infra.view.Image;
import infra.view.Link;
import infra.view.Table;

public class EventosVinculadosUsuarioView extends Table
{
	public EventosVinculadosUsuarioView(Usuario usuario) throws Exception
	{
		super(4);
		this.addStyle(STYLE_ALTERNATE);
		this.setWidth("100%");
		
		this.addHeader("");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Titulo");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Classe");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Fase");
		
		for(Iterator i = usuario.obterEventosVinculador().iterator() ; i.hasNext() ; )
		{
			Evento e = (Evento) i.next();
			
			if(e instanceof Log)
			{
				Link link = new Link(new Image("delete.gif"),new Action("excluirEvento"));
				link.getAction().add("id", e.obterId());
				link.getAction().add("entidadeId", usuario.obterId());
				
				this.add(link);
			}
			else
				this.add("");
			
			this.add(e.obterTitulo());
			this.add(e.obterClasseDescricao());
			this.add(e.obterFase().obterNome());
		}
	}
}
