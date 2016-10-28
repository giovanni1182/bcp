package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EventoImage;
import com.gvs.crm.component.EventoTituloLink;
import com.gvs.crm.component.MesAnoAgendaSelect;
import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Button;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class AgendasView extends BasicView {

	private Collection eventos;

	private Action action;

	private Entidade entidade;

	private String mesAno;
	private int pagina;

	public AgendasView(Collection eventos, Action action, Entidade entidade,String mesAno, int pagina) 
	{
		this.eventos = eventos;
		this.action = action;
		this.entidade = entidade;
		this.mesAno = mesAno;
		this.pagina = pagina;
	}

	public View execute(User user, Locale locale, Properties properties)throws Exception 
	{
		//String _fase = properties.getProperty("_fase", "");
		//String _classeEvento = properties.getProperty("_classeEvento", "");
		
		if (pagina <= 0)
			pagina = 1;

		int anterior = pagina - 1;
		int proximo = pagina + 1;

		Table table = new Table(7);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);

		table.addSubtitle("Visualizar Agendas");

		Block block = new Block(Block.HORIZONTAL);

		block.add(new MesAnoAgendaSelect("mesAno", mesAno));
		block.add(new Space(2));
		Button visualizarButton = new Button("Visualizar", action);
		block.add(visualizarButton);
		table.setNextColSpan(table.getColumns());
		table.add(block);
		table.setNextColSpan(table.getColumns());
		table.addData(new Space());

		if (eventos.size() == 0) 
		{
			table.setNextColSpan(table.getColumns());
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addData("Ningún evento a ser visualizado.");
		}
		else 
		{
			table.setNextColSpan(table.getColumns());
			table.addHeader(eventos.size()
					+ (eventos.size() == 1 ? " Evento" : " Eventos"));
			table.setNextColSpan(table.getColumns());
			table.addData(new Space());
			table.addHeader("");
			table.addHeader("Creado En");
			table.addHeader("Título");
			table.addHeader("Tipo");
			table.addHeader("Fase");
			table.addHeader("Origen");
			table.addHeader("Responsable");

			for (Iterator i = eventos.iterator(); i.hasNext();) 
			{
				AgendaMovimentacao agenda = (AgendaMovimentacao) i.next();
	
				Label criacaoLabel = new Label(agenda.obterCriacao(),
						"dd/MM/yyyy HH:mm");
				criacaoLabel.setBold(!agenda.foiLido());
	
				EventoTituloLink tituloLink = new EventoTituloLink(agenda);
	
				Label tipoLabel = new Label(agenda.obterClasseDescricao());
				tipoLabel.setBold(!agenda.foiLido());
	
				Label tipoLabel2 = new Label(agenda.obterTipo());
				tipoLabel2.setBold(!agenda.foiLido());
	
				Label faseLabel = new Label(agenda.obterFase().obterNome());
				faseLabel.setBold(!agenda.foiLido());
	
				Label origemLabel = new Label(agenda.obterOrigem().obterNome());
				origemLabel.setBold(!agenda.foiLido());
	
				Label responsavelLabel = null;
	
				if (agenda.obterResponsavel() != null)
					responsavelLabel = new Label(agenda.obterResponsavel()
							.obterNome());
				else
					responsavelLabel = new Label("excluido");
	
				responsavelLabel.setBold(!agenda.foiLido());
	
				table.addData(new EventoImage(agenda));
				table.addData(criacaoLabel);
				table.addData(new EventoTituloLink(agenda));
				table.addData(tipoLabel);
				table.addData(faseLabel);
				table.addData(origemLabel);
				table.addData(responsavelLabel);
				
			}
			
			/*Button anteriorLink = new Button("<<< Anterior", action);
			anteriorLink.getAction().add("_pagina", anterior);

			Button proximoLink = new Button("Próxima >>>", action);
			proximoLink.getAction().add("_pagina", proximo);

			if(anterior > 0)
				table.addFooter(anteriorLink);

			if(eventos.size() >= 29)
				table.addFooter(proximoLink);*/
		}
		
		return table;
	}
}