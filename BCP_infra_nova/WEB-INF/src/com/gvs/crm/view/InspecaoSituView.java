package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.OpcaoHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class InspecaoSituView extends PortalView
{
	public InspecaoSituView() throws Exception
	{
		
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(1);
		table.setWidth("100%");
		CRMModelManager mm = new CRMModelManager(user);
		UsuarioHome home = (UsuarioHome) mm.getHome("UsuarioHome");
		OpcaoHome opcaoHome = (OpcaoHome) mm.getHome("OpcaoHome");
		Usuario usuarioAtual = home.obterUsuarioPorUser(user);
		
		Action action = null;
		Link link = null;
		
		if(opcaoHome.obterUsuarios(20).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarProdutividadeAseguradora");
			action.add("lista", false);
			link = new Link("1 - Aseguradora - Pólizas",action);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(21).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarReaseguros");
			action.add("lista", false);
			link = new Link("2 - Aseguradora - Reaseguros",action);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(22).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarReasegurosReaseguradora");
			action.add("lista", false);
			link = new Link("3 - Reaseguradora - Reaseguros",action);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(23).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarProdutividadeAuxiliar");
			action.add("lista", false);
			link = new Link("4 - Agente de Seguro",action);
			table.add(link);
			table.add(new Space());
		}
		
		/*action = new Action("visualizarApolicesSuspeitas");
		action.add("lista", false);
		link = new Link("5 - Operaciones Sospechosas",action);
		table.add(link);
		table.add(new Space());*/
		
		if(opcaoHome.obterUsuarios(24).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarEstatistica");
			action.add("view", true);
			link = new Link("5 - Mayores Ocurrencias",action);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(25).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			Block block = new Block(Block.HORIZONTAL);
			block.add(new Label("6 - Listados"));
			table.add(block);
			block = new Block(Block.HORIZONTAL);
			Label l = new Label("Aseguradora:");
			l.setBold(true);
			block.add(new Space(6));
			block.add(l);
			block.add(new Space(2));
			block.add(new EntidadePopup("id", "entidadeNome", null, "Aseguradora", true));
			
			action = new Action("visualizarRelatorioAseguradora");
			action.add("rel", true);
			action.add("lista", false);
			Button button = new Button("Visualizar",action);
			block.add(new Space(5));
			block.add(button);
			
			table.add(block);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(26).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("selecionarRelEntidadesVigentes");
			action.add("view", true);
			link = new Link("7 - Listados Entidades Vigentes", action);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(27).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("selecionarRelAseguradorasPlanos");
			action.add("view", true);
			link = new Link("8 - Listados Aseguradoras Planes", action);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(28).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("apolicesPorSecao");
			action.add("view", true);
			link = new Link("9 - Pólizas por Sección", action);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(29).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("apolicesPorSecaoAnual");
			action.add("view", true);
			link = new Link("10 - Pólizas por Sección Anual", action);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(30).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarGEE");
			action.add("view", true);
			link = new Link("11 - Información Contable Consolidada", action);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(31).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarReasegurosCorredor");
			action.add("lista", false);
			link = new Link("12 - Aseguradora - Corredora de Reaseguros", action);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(32).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarReasegurosCorredorAseguradora");
			action.add("lista", false);
			link = new Link("13 - Corredora de Reaseguros - Aseguradoras", action);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(33).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarConsolidado");
			action.add("view", true);
			link = new Link("14 - Consolidado Póliza/Sección", action);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(34).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarApoliceSinistroAnual");
			action.add("view", true);
			link = new Link("15 - Histórico Pólizas/Siniestros", action);
			table.add(link);
			table.add(new Space());
		}
		
		return table;
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}

	public String getSelectedGroup() throws Exception
	{
		return null;
	}

	public String getSelectedOption() throws Exception
	{
		return null;
	}

	public View getTitle() throws Exception
	{
		return new Label("Inspección in situ");
	}
}