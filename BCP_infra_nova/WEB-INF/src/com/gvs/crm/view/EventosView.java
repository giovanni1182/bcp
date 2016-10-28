package com.gvs.crm.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.component.EventoImage;
import com.gvs.crm.component.EventoTituloLink;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Processo;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Button;
import infra.view.Image;
import infra.view.Label;
import infra.view.Link;
import infra.view.Select;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class EventosView extends BasicView {
	private Collection eventos;

	private Action faseAction;
	private int pagina;

	public EventosView(Collection eventos) {
		this.eventos = eventos;
	}
	
	public EventosView(Collection eventos, int pagina) 
	{
		this.eventos = eventos;
		this.pagina = pagina;
	}

	public EventosView(Collection eventos, Action faseAction) 
	{
		this.eventos = eventos;
		this.faseAction = faseAction;
	}
	
	public EventosView(Collection eventos, Action faseAction, int pagina) 
	{
		this.eventos = eventos;
		this.faseAction = faseAction;
		this.pagina = pagina;
	}

	public View execute(User user, Locale locale, Properties properties) throws Exception 
	{
		if (pagina <= 0)
			pagina = 1;

		int anterior = pagina - 1;
		int proximo = pagina + 1;
		
		String _fase = properties.getProperty("_fase", "");
		String _classeEvento = properties.getProperty("_classeEvento", "");

		Table table = new Table(8);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);

		// obtém a lista de fases presentes na coleção de eventos e cria a
		// coleção com o eventos filtrados
		Map fases = new TreeMap();
		Map classeEvento = new TreeMap();

		Collection eventosFiltrados = new ArrayList();
		Collection eventosFiltradosPorFase = new ArrayList();
		Collection itens = new ArrayList();

		//Eventos tipo Mensagem
		if (this.faseAction == null) 
		{
			for (Iterator i = this.eventos.iterator(); i.hasNext();) 
			{
				Evento e = (Evento) i.next();
				String classe = e.obterClasse();
				eventosFiltradosPorFase.add(e);
			}
		}
		//Eventos tipo Outros
		else
		{
			if (itens.size() > 0)
				this.eventos.removeAll(itens);

			for (Iterator i = this.eventos.iterator(); i.hasNext();) 
			{
				Evento t = (Evento) i.next();
				classeEvento.put(t.obterClasseDescricao(), t.obterClasse());
			}

			if (!classeEvento.containsValue(_classeEvento))
				_classeEvento = "";

			for (Iterator i = this.eventos.iterator(); i.hasNext();) 
			{
				Evento e = (Evento) i.next();
				String classe = e.obterClasse();
				if (_classeEvento.equals("") || _classeEvento.equals(classe))
					eventosFiltrados.add(e);
			}

			for (Iterator i = eventosFiltrados.iterator(); i.hasNext();) 
			{
				Evento e = (Evento) i.next();
				Evento.Fase f = e.obterFase();
				if (_fase.equals("") || _fase.equals(f.obterCodigo()))
					eventosFiltradosPorFase.add(e);
			}

			for (Iterator i = eventosFiltrados.iterator(); i.hasNext();) 
			{
				Evento e = (Evento) i.next();
				Evento.Fase f = e.obterFase();
				fases.put(f.obterNome(), f.obterCodigo());
			}

			if (!fases.containsValue(_fase))
				_fase = "";

			if (eventosFiltradosPorFase.size() == 0)
				eventosFiltradosPorFase = eventosFiltrados;
		}

		if (eventosFiltradosPorFase.size() == 0 && eventosFiltrados.size() == 0) 
		{
			table.setNextColSpan(table.getColumns());
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addData("Ningún evento a ser visualizado.");
		} 
		else 
		{
			table.setNextColSpan(table.getColumns());
			table.addHeader(eventosFiltradosPorFase.size()
					+ (eventosFiltradosPorFase.size() == 1 ? " Evento"
							: " Eventos"));
			table.setNextColSpan(table.getColumns());
			table.addData(new Space());
			table.addHeader("");
			table.addHeader("Creado En");

			table.addHeader("Título");

			Select classeEventoSelect = new Select("_classeEvento", 1,
					this.faseAction);
			classeEventoSelect.add("[Todos]", "", false);

			Block block = new Block(Block.HORIZONTAL);
			//Button visualizarButton = new Button("Visualizar",
			// this.faseAction);

			Link link = new Link(new Image("visualizar.GIF"), this.faseAction);

			if (this.faseAction == null)
				table.addHeader("Evento");
			else {
				for (Iterator i = classeEvento.keySet().iterator(); i.hasNext();) {
					String nome = (String) i.next();
					String codigo = (String) classeEvento.get(nome);
					classeEventoSelect.add(nome, codigo, _classeEvento
							.equals(codigo));
				}
				block.add(classeEventoSelect);
				block.add(new Space());
				block.add(link);
				table.addHeader(block);
			}

			table.addHeader("Tipo");

			if (this.faseAction == null) {
				table.addHeader("Fase");
			} else {
				Block block2 = new Block(Block.HORIZONTAL);
				//Button visualizarButton2 = new Button("Visualizar",
				// this.faseAction);

				Link link2 = new Link(new Image("visualizar.GIF"),
						this.faseAction);

				Select faseSelect = new Select("_fase", 1, this.faseAction);
				faseSelect.add("[Todas]", "", false);
				for (Iterator i = fases.keySet().iterator(); i.hasNext();) {
					String nome = (String) i.next();
					String codigo = (String) fases.get(nome);
					faseSelect.add(nome, codigo, _fase.equals(codigo));
				}
				block2.add(faseSelect);
				block2.add(new Space());
				block2.add(link2);
				table.addHeader(block2);
			}

			table.addHeader("Origen");
			table.addHeader("Responsable");

			for (Iterator i = eventosFiltradosPorFase.iterator(); i.hasNext();) {
				Evento e = (Evento) i.next();

				Label criacaoLabel = new Label(e.obterCriacao(),
						"dd/MM/yyyy HH:mm");
				criacaoLabel.setBold(!e.foiLido());

				EventoTituloLink tituloLink = new EventoTituloLink(e);

				Label tipoLabel = new Label(e.obterClasseDescricao());
				tipoLabel.setBold(!e.foiLido());

				Label tipoLabel2 = new Label(e.obterTipo());
				tipoLabel2.setBold(!e.foiLido());

				Label faseLabel = new Label(e.obterFase().obterNome());
				faseLabel.setBold(!e.foiLido());

				/*
				 * Label criadorLabel = new Label(e.obterCriador().obterNome());
				 * criadorLabel.setBold(!e.foiLido());
				 */

				Label origemLabel = new Label("");

				Block block3 = new Block(Block.VERTICAL);

				if (e instanceof Processo) {
					Processo processo = (Processo) e;

					for (Iterator j = processo.obterPessoas().iterator(); j
							.hasNext();) {
						Processo.Pessoa pessoa = (Processo.Pessoa) j.next();

						if (pessoa.obterTipo().equals("Autor"))
							block3.add(new Label(pessoa.obterPessoa()
									.obterNome()));
					}
				} else {
					if (e.obterOrigem() != null) {
						origemLabel = new Label(e.obterOrigem().obterNome());
						origemLabel.setBold(!e.foiLido());
					}
				}

				block3.add(origemLabel);

				/*
				 * if(e.obterOrigem()!=null) { origemLabel = new
				 * Label(e.obterOrigem().obterNome());
				 * origemLabel.setBold(!e.foiLido()); } else origemLabel = new
				 * Label("");
				 */

				Label responsavelLabel = null;

				if (e.obterResponsavel() != null) {
					responsavelLabel = new Label(e.obterResponsavel()
							.obterNome());
					responsavelLabel.setBold(!e.foiLido());
				} else
					responsavelLabel = new Label("");

				table.addData(new EventoImage(e));
				table.addData(criacaoLabel);
				table.addData(new EventoTituloLink(e));
				table.addData(tipoLabel);
				table.addData(tipoLabel2);
				table.addData(faseLabel);
				//table.addData(criadorLabel);
				table.addData(block3);
				table.addData(responsavelLabel);
			}
			
			if(this.faseAction!=null)
			{
				Button anteriorLink = new Button("<<< Anterior", this.faseAction);
				anteriorLink.getAction().add("_pagina", anterior);
				//anteriorLink.getAction().add("lista", true);
		
				Button proximoLink = new Button("Próxima >>>", this.faseAction);
				proximoLink.getAction().add("_pagina", proximo);
				//proximoLink.getAction().add("lista", true);
		
				//if(anterior > 0)
					//table.addFooter(anteriorLink);
		
				//if(eventosFiltradosPorFase.size() >= 29)
					//table.addFooter(proximoLink);
			}
		}
		
		eventosFiltradosPorFase = null;
		eventosFiltrados = null;
		this.eventos = null;
		
		return table;
	}
}