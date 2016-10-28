package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.StatusInscricaoSelect;
import com.gvs.crm.component.TipoEntidadeInscricaoSelect;
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Inscricao;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Image;
import infra.view.InputLong;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ListaInscricoesView extends PortalView {

	private Collection inscricoes;

	private boolean lista;

	private String tipo;

	private String status;

	private int pagina;

	public ListaInscricoesView(Collection inscricoes, boolean lista, String tipo, String status, int pagina) throws Exception 
	{
		this.inscricoes = inscricoes;
		this.lista = lista;
		this.tipo = tipo;
		this.status = status;
		this.pagina = pagina;
	}

	public View getBody(User user, Locale locale, Properties properties)throws Exception 
	{
		if (this.tipo == null)
			this.tipo = "";
		if (this.status == null)
			this.status = "";

		if (pagina <= 0)
			pagina = 1;

		int anterior = pagina - 1;
		int proximo = pagina + 1;

		//Collection inscricoes2 = new ArrayList();
		
		//System.out.println("Inscrições na View: " + this.inscricoes.size());

		Table table = new Table(6);
		if(tipo.equals("Reaseguradora"))
			table = new Table(7);
			
		table.setWidth("100%");

		table.addStyle(Table.STYLE_ALTERNATE);

		table.addHeader("");
		table.addHeader("Nombre");
		table.addHeader("Inscripción");
		table.addHeader("Validez");

		Block block = new Block(Block.HORIZONTAL);
		block.add(new TipoEntidadeInscricaoSelect("tipo", tipo));
		block.add(new Space(2));
		Link link = new Link(new Image("visualizar.GIF"), new Action("visualizarInsricoes"));
		link.getAction().add("lista", true);
		block.add(link);

		table.add(block);

		Block block2 = new Block(Block.HORIZONTAL);
		block2.add(new StatusInscricaoSelect("status", status));
		block2.add(new Space(2));
		Link link2 = new Link(new Image("visualizar.GIF"), new Action("visualizarInsricoes"));
		link2.getAction().add("lista", true);
		block2.add(link2);

		table.add(block2);
		
		if(tipo.equals("Reaseguradora"))
		{
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Pais");
		}

		if (this.lista) 
		{
			int j = 1;
			
			if (inscricoes.size() == 0) 
			{
				table.setNextColSpan(table.getColumns());
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Ningúna Inscripición encontrada");
			} 
			else 
			{
				for (Iterator i = inscricoes.iterator(); i.hasNext();) 
				{
					Inscricao inscricao = (Inscricao) i.next();
					
					InputLong input = new InputLong("inscricaoId",inscricao.obterId(),10);
					input.setEnabled(false);
					input.setVisible(false);
					
					table.add(input);

					Entidade origem = inscricao.obterOrigem();
					
					Link link3 = new Link(origem.obterNome(),new Action("visualizarDetalhesEntidade"));
					link3.getAction().add("id",	inscricao.obterOrigem().obterId());

					table.add(link3);

					Link link4 = new Link(inscricao.obterInscricao(),new Action("visualizarEvento"));
					link4.getAction().add("id", inscricao.obterId());

					table.add(link4);

					if (inscricao.obterDataValidade() != null)
						table.add(new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataValidade()));
					else
						table.add("");

					if (inscricao.obterOrigem() instanceof AuxiliarSeguro) 
					{
						Entidade.Atributo atividade = (Entidade.Atributo) inscricao.obterOrigem().obterAtributo("atividade");

						if (atividade != null)
							table.add(atividade.obterValor());
					} 
					else
						table.add(inscricao.obterOrigem().obterDescricaoClasse());
					
					table.add(inscricao.obterSituacao());
					
					if(tipo.equals("Reaseguradora"))
					{
						Entidade.Atributo nacionalidade = origem.obterAtributo("nome");
						table.add(nacionalidade.obterValor());
					}
				}
			}

			/*Button anteriorLink = new Button("<<< Anterior", new Action("visualizarInsricoes"));
			anteriorLink.getAction().add("_pagina", anterior);
			anteriorLink.getAction().add("lista", true);

			Button proximoLink = new Button("Próxima >>>", new Action("visualizarInsricoes"));
			proximoLink.getAction().add("_pagina", proximo);
			proximoLink.getAction().add("lista", true);

			if(anterior > 0)
				table.addFooter(anteriorLink);

			if(inscricoes.size() >= 25)
				table.addFooter(proximoLink);*/
		}
		
		this.inscricoes.clear();

		return table;
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception {
		return new Label("Listado de Control de Inscripciones en la SIS");
	}

	public Entidade getOrigemMenu() throws Exception {
		return null;
	}
}