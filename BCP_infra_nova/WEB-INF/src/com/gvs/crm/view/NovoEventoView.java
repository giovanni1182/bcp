package com.gvs.crm.view;

import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class NovoEventoView extends PortalView
{
	class Classe
	{
		public Classe(String classe, String grupo, String descricao)
		{
			this.classe = classe;
			this.grupo = grupo;
			this.descricao = descricao;
		}

		public String grupo;

		public String classe;

		public String descricao;
	}

	private int passo;

	private Evento superior;

	private Date data;

	private Entidade origem, origemMenu;

	public NovoEventoView(int passo, Evento superior, Date data, Entidade origem)
			throws Exception {
		this.passo = passo;
		this.superior = superior;
		this.data = data;
		this.origem = origem;
	}

	public NovoEventoView(int passo, Evento superior, Date data,
			Entidade origem, Entidade origemMenu) throws Exception {
		this.passo = passo;
		this.superior = superior;
		this.data = data;
		this.origem = origem;
		this.origemMenu = origemMenu;
	}

	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(user);
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(user);
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		Table table = new Table(2);
		Link link;
		switch (this.passo) {
		case 1:
			table.addHeader("Superior:");
			Table superioresTable = new Table(3);
			link = new Link("[nenhum]", new Action("novoEvento"));
			link.getAction().add("passo", 2);
			link.getAction().add("origemMenuId", this.origemMenu.obterId());
			if (this.data != null)
				link.getAction().add("data", this.data);
			if (this.origem != null)
				link.getAction().add("origemId", this.origem.obterId());
			link.getAction().add("origemMenuId", this.origemMenu.obterId());
			superioresTable.addData("");
			superioresTable.addData("");
			superioresTable.addData(link);
			table.addData(superioresTable);
			break;
		case 2:
			table.addHeader("Superior:");
			if (this.superior == null)
				table.addData("Ningún");
			else
				table.addData(this.superior.obterTitulo());
			table.addHeader("Tipo:");
			Table tiposTable = new Table(2);
			Map classes = new TreeMap();
			for (Iterator i = eventoHome.obterClasses(this.superior).iterator(); i.hasNext();) 
			{
				String classe = (String) i.next();
				String grupo = eventoHome.obterClasseGrupo(classe);
				String descricao = eventoHome.obterClasseDescricao(classe);
				
				if(usuarioAtual.obterAseguradorasResEscaneada().size() > 0 && !usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
				{
					if(classe.equals("resolucaoscanner"))
						classes.put(grupo + descricao, new Classe(classe, grupo,descricao));
				}
				else
				{
					if(classe.equals("plano"))
					{
						if (usuarioAtual.obterChave().equals("admin") || usuarioAtual.obterChave().equals("fkubota") || usuarioAtual.obterChave().equals("dpenayo") || usuarioAtual.obterChave().equals("mriveros"))
							classes.put(grupo + descricao, new Classe(classe, grupo,descricao));
					}
					else if(classe.equals("processo"))
					{
						Entidade ial = entidadeHome.obterEntidadePorApelido("intendenteial");
						
						if (usuarioAtual.obterChave().equals("admin") || usuarioAtual.obterSuperiores().contains(ial))
							classes.put(grupo + descricao, new Classe(classe, grupo,descricao));
					}
					else if(classe.equals("agendamovimentacao"))
					{
						//Entidade informatica = entidadeHome.obterEntidadePorApelido("informatica");
						
						//if (usuarioAtual.obterChave().equals("admin") || usuarioAtual.obterSuperiores().contains(informatica))
							classes.put(grupo + descricao, new Classe(classe, grupo,descricao));
					}
					else if(classe.equals("agendameicos"))
					{
						Entidade icf = entidadeHome.obterEntidadePorApelido("intendenteicf");
						
						if (usuarioAtual.obterChave().equals("admin") || usuarioAtual.obterSuperiores().contains(icf))
							classes.put(grupo + descricao, new Classe(classe, grupo,descricao));
					}
					else
						classes.put(grupo + descricao, new Classe(classe, grupo,descricao));
				}
				
				System.out.println("classe: " + classe);
				
			}
			
			String grupo = "";
			
			for (Iterator i = classes.values().iterator(); i.hasNext();) 
			{
				Classe c = (Classe) i.next();
				if (!grupo.equals(c.grupo)) 
				{
					tiposTable.addHeader(c.grupo);
					grupo = c.grupo;
				}
				else 
				{
					tiposTable.addHeader("");
				}

				String nome = c.descricao;
				String classe = c.classe;
				
				if(classe.equals("codificacaocobertura"))
					link = new Link(nome, new Action("listarCodificacaoPlanos"));
				else if(classe.equals("codificacaorisco"))
					link = new Link(nome, new Action("listarCodificacaoCoberturas"));
				else if(classe.equals("codificacaodetalhe"))
					link = new Link(nome, new Action("listarCodificacaoRiscos"));
				else
					link = new Link(nome, new Action("novoEvento"));
				link.getAction().add("classe", c.classe);
				if (this.data != null)
					link.getAction().add("data", this.data);
				if (this.superior != null)
					link.getAction().add("superiorId", this.superior.obterId());
				if (this.origem != null)
					link.getAction().add("origemId", this.origem.obterId());
				
				link.getAction().add("origemMenuId", this.origemMenu.obterId());
				tiposTable.addData(link);
			}
			table.addData(tiposTable);
			break;
		}

		if (this.superior != null) {
			Button voltarButton = new Button("Volver", new Action(
					"visualizarEvento"));
			voltarButton.getAction().add("id", this.superior.obterId());
			table.addFooter(voltarButton);
		}

		return table;
	}

	public String getSelectedGroup() throws Exception {
		if (this.origem == null || this.origem.obterId() == 0)
			return "Menú Principal";
		else
			return origem.obterNome();
	}

	public String getSelectedOption() throws Exception {
		return "Nuevo Evento...";
	}

	public View getTitle() throws Exception {
		return new Label("Nuevo Evento");
	}

	public Entidade getOrigemMenu() throws Exception {
		if (this.origemMenu != null && this.origemMenu.obterId() != 0)
			return this.origemMenu;
		else
			return this.origem;
	}
}