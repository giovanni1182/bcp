package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
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
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class RelOpcoesView extends PortalView
{
	private Usuario usuario;
	private int opcao;
	private Collection dados;
	
	public RelOpcoesView(Usuario usuario, int opcao, Collection dados) throws Exception
	{
		this.usuario = usuario;
		this.opcao = opcao;
		this.dados = dados;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		table.setWidth("40%");
		CRMModelManager mm = new CRMModelManager(user);
		OpcaoHome opcaoHome = (OpcaoHome) mm.getHome("OpcaoHome");
		
		table.addHeader("Usuario:");
		table.add(new EntidadePopup("usuarioId", "usuarioNome", usuario ,"usuario",true));
		
		table.addHeader("Opciones:");
		table.add(new OpcoesSelect("liberarOpcao", opcao, true));
		
		//Button botao = new Button("Generar Pantalha", new Action("relOpcoes"));
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(new Space(15));
		//block.add(botao);
		Button botao = new Button("Generar Excel", new Action("relOpcoes"));
		botao.getAction().add("excel", true);
		block.add(new Space(4));
		block.add(botao);
		
		table.setNextColSpan(table.getColumns());
		table.add(new Space());
		table.setNextColSpan(table.getColumns());
		table.add(block);
		
		if(this.dados.size() > 0)
		{
			table.addSubtitle("");
			
			Table table2 = new Table(2);
			table2.setWidth("100%");
			table2.addStyle(Table.STYLE_ALTERNATE);
			if(this.usuario!=null)
			{
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.addHeader("Usuario");
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.addHeader("Opciones");
				
				table2.add(this.usuario.obterNome());
			}
			else
			{
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.addHeader("Opción");
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.addHeader("Usuarios");
				
				table2.add(opcaoHome.obterNome(opcao));
			}
			
			Block block2 = new Block(Block.VERTICAL);
			
			for(Iterator i = this.dados.iterator() ; i.hasNext() ; )
			{
				if(this.usuario!=null)
				{
					String opcaoStr = (String) i.next();
					block2.add(new Label(opcaoStr));
				}
				else
				{
					Usuario usuario2 = (Usuario) i.next();
					block2.add(new Label(usuario2.obterNome()));
				}
			}
			
			table2.add(block2);
			
			table.setNextColSpan(table.getColumns());
			table.add(table2);
		}
		
		return table;
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
		return new Label("Listado Opciones");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}