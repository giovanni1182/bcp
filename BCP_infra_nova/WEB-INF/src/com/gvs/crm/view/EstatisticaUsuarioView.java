package com.gvs.crm.view;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Usuario;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class EstatisticaUsuarioView extends PortalView
{
	private Date dataInicio, dataFim;
	private Collection<Usuario> usuarios;
	
	public EstatisticaUsuarioView(Date dataInicio, Date dataFim, Collection<Usuario> usuarios) throws Exception
	{
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.usuarios = usuarios;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		table.setWidth("40%");
		
		table.setNextWidth("10");
		table.addHeader("Fecha:");
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputDate("dataInicio", this.dataInicio));
		block.add(new Space(2));
		block.add(new Label("hasta"));
		block.add(new Space(2));
		block.add(new InputDate("dataFim", this.dataFim));
		table.add(block);
		
		Button buscarButton = new Button("Consultar", new Action("estatisticaUsuario"));
		Button excelButton = new Button("Generar Excel", new Action("estatisticaUsuario"));
		excelButton.getAction().add("excel", true);
		
		table.setNextColSpan(table.getColumns());
		table.add(new Space());
		
		block = new Block(Block.HORIZONTAL);
		block.add(new Space(15));
		block.add(buscarButton);
		block.add(new Space(4));
		block.add(excelButton);
		table.setNextColSpan(table.getColumns());
		table.add(block);
		
		if(this.usuarios.size() > 0)
		{
			Table table2 = new Table(3);
			table2.setWidth("100%");
			
			table2.addStyle(Table.STYLE_ALTERNATE);
			table2.addSubtitle("");
			
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Nombre");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Login");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Duración");
			
			for(Iterator<Usuario> i = this.usuarios.iterator() ; i.hasNext() ; )
			{
				Usuario usuario = i.next();
				
				table2.add(usuario.obterNome());
				table2.add(usuario.obterChave());
				table2.setNextHAlign(Table.HALIGN_RIGHT);
				table2.add(usuario.obterTempoConectado(dataInicio, dataFim));
			}
			
			table.setNextColSpan(table.getColumns());
			table.add(table2);
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
		return new Label("Utilización del Sistema");
	}
}