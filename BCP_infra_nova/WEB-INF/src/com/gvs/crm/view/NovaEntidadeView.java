package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class NovaEntidadeView extends PortalView {
	private Entidade superior, origemMenu;

	public NovaEntidadeView() throws Exception {
	}

	public NovaEntidadeView(Entidade origemMenu) throws Exception {
		this(null, origemMenu);
	}

	public NovaEntidadeView(Entidade superior, Entidade origemMenu)
			throws Exception {
		this.superior = superior;
		this.origemMenu = origemMenu;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(user);
		
		Table table = new Table(2);

		if (this.superior == null) {

			table.addHeader("");
			table
					.addData("Elegir la Compañia o el Departamento que desea para colocar la Entidad nueva:");

			table.addHeader("");
			table.addData(new EntidadePopup("superiorId", "superiorNome", null,
					true));

			Button avancarButton = new Button("Continuar", new Action(
					"novaEntidade"));
			avancarButton.getAction().add("origemMenuId",
					this.origemMenu.obterId());
			table.addFooter(avancarButton);

			/*
			 * Table superioresTable = new Table(2); for (Iterator i =
			 * entidadeHome.obterPossiveisSuperioresParaCadastro(null).iterator();
			 * i.hasNext();) { Entidade e = (Entidade) i.next(); Link link = new
			 * Link(e.obterNome(), new Action("novaEntidade"));
			 * link.getAction().add("superiorId", e.obterId());
			 * link.getAction().add("origemMenuId", this.origemMenu.obterId());
			 * superioresTable.addData(new Image(e.obterIcone()));
			 * superioresTable.addData(link); } table.addData(superioresTable);
			 */
		} else {
			table.addHeader("Superior:");
			table.addData(this.superior.obterNome());
			Collection tipos = entidadeHome.obterClassesInferiores(this.superior);
			table.addHeader("Tipo:");
			Block tiposBlock = new Block(Block.VERTICAL);
			for (Iterator i = tipos.iterator(); i.hasNext();) 
			{
				String tipo = (String) i.next();
				String descricao = entidadeHome.obterDescricaoClasse(tipo);
				Link link = new Link(descricao, new Action("novaEntidade"));
				link.getAction().add("classe", tipo);
				link.getAction().add("superiorId", this.superior.obterId());
				link.getAction().add("origemMenuId", this.origemMenu.obterId());
				if(tipo.equals("parametro") || tipo.equals("usuario")) 
				{
					Entidade informatica = entidadeHome.obterEntidadePorApelido("informatica");
					
					if (usuarioAtual.obterChave().equals("admin") || usuarioAtual.obterSuperiores().contains(informatica))
						tiposBlock.add(link);
				} 
				else if(tipo.equals("ClassificacaoContas") || tipo.equals("Conta"))
				{
					Entidade ieta = entidadeHome.obterEntidadePorApelido("intendenteieta");
					
					if (usuarioAtual.obterChave().equals("admin") || usuarioAtual.obterSuperiores().contains(ieta))
						tiposBlock.add(link);
				}
				else if(tipo.equals("AuxiliarSeguro"))
				{
					Entidade icoras = entidadeHome.obterEntidadePorApelido("jefedivisioncontrolicoras");
					
					if (usuarioAtual.obterChave().equals("admin") || usuarioAtual.obterSuperiores().contains(icoras))
						tiposBlock.add(link);
				}
				else if(tipo.equals("Aseguradora"))
				{
					Entidade ial = entidadeHome.obterEntidadePorApelido("intendenteial");
					
					if (usuarioAtual.obterChave().equals("admin") || usuarioAtual.obterSuperiores().contains(ial))
						tiposBlock.add(link);
				}
				else if(tipo.equals("AuditorExterno"))
				{
					Entidade icf = entidadeHome.obterEntidadePorApelido("intendenteicf");
					
					if (usuarioAtual.obterChave().equals("admin") || usuarioAtual.obterSuperiores().contains(icf))
						tiposBlock.add(link);
				}
				else if(tipo.equals("Reaseguradora"))
				{
					Entidade icoras = entidadeHome.obterEntidadePorApelido("jefedivisionreasegurosicoras");
					
					if (usuarioAtual.obterChave().equals("admin") || usuarioAtual.obterSuperiores().contains(icoras))
						tiposBlock.add(link);
				}
				
				else
					tiposBlock.add(link);
			}
			table.addData(tiposBlock);
		}

		if (superior == null) {
			Button voltarButton = new Button("Volver", new Action(
					"visualizarPaginaInicial"));
			voltarButton.getAction().add("origemMenuId",
					this.origemMenu.obterId());
			table.addFooter(voltarButton);
		} else {
			Button voltarButton = new Button("Volver", new Action(
					"novaEntidade"));
			voltarButton.getAction().add("origemMenuId",
					this.origemMenu.obterId());
			table.addFooter(voltarButton);
		}

		return table;
	}

	public String getSelectedGroup() throws Exception {
		if (this.superior == null)
			return "Menú Principal";
		else
			return this.superior.obterNome();
	}

	public String getSelectedOption() throws Exception {
		return "Nueva Entidad...";
	}

	public View getTitle() throws Exception {
		return new Label("Nueva Entidad");
	}

	public Entidade getOrigemMenu() throws Exception {
		return this.origemMenu;
	}
}