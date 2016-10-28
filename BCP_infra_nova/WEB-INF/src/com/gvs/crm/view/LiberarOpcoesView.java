package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.OpcoesSelect;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.OpcaoHome;
import com.gvs.crm.model.Usuario;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Image;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class LiberarOpcoesView extends PortalView
{
	private OpcaoHome opcaoHome;
	private int opcao;
	
	public LiberarOpcoesView(int opcao) throws Exception
	{
		this.opcao = opcao;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(user);
		opcaoHome = (OpcaoHome) mm.getHome("OpcaoHome");
		
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		table.addHeader("Opciones:");
		table.add(new OpcoesSelect("liberarOpcao", opcao, false));
		
		Button button = new Button("Buscar", new Action("liberarOpcoes"));
		button.getAction().add("view", true);
		
		table.addFooter(button);
		
		mainTable.add(table);

		if(opcao > 0)
			mainTable.add(montaTabela(opcao));
		
		return mainTable;
	}
	
	private Table montaTabela(int opcao) throws Exception
	{
		Table table = new Table(2);
		table.addSubtitle(opcaoHome.obterNome(opcao));
		
		table.addHeader("Usuario:");
		table.add(new EntidadePopup("usuario", "usuarioNome", null, "usuario",true));
		
		Button incluirButton = new Button("Agregar", new Action("liberarOpcoes"));
		incluirButton.getAction().add("opcao2", opcao);
		
		table.setNextColSpan(table.getColumns());
		table.add(new Space());
		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add(incluirButton);
		
		table.addSubtitle("Usuarios");
		Block block;
		Link link;
		for(Usuario usuario : opcaoHome.obterUsuarios(opcao))
		{
			block = new Block(Block.HORIZONTAL);
			
			link = new Link(new Image("delete.gif"), new Action("excluirOpcao"));
			link.getAction().add("opcao2", opcao);
			link.getAction().add("usuario2", usuario.obterId());
			
			block.add(link);
			block.add(new Space(4));
			block.add(new Label(usuario.obterNome()));
			
			table.setNextColSpan(table.getColumns());
			table.add(block);
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
		return new Label("Liberación de opciones");
	}
}