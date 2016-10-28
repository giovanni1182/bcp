package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Usuario;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Check;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class RelOpcaoResEscaneadaView extends PortalView
{
	private Collection<Aseguradora> aseguradoras, esolhidas;
	private Usuario usuario;
	private Collection<Usuario> usuariosResEscaneada;
	
	public RelOpcaoResEscaneadaView(Usuario usuario, Collection<Aseguradora> escolhidas, Collection<Aseguradora> aseguradoras,Collection<Usuario> usuariosResEscaneada) throws Exception
	{
		this.usuario = usuario;
		this.esolhidas = escolhidas;
		this.aseguradoras = aseguradoras;
		this.usuariosResEscaneada = usuariosResEscaneada;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(2);
		
		Table tableCol1 = new Table(1);
		Table table = new Table(2);
		
		table.addHeader("Usuario:");
		table.add(new EntidadePopup("usuarioId", "usuarioNome", usuario, "usuario",true));
		
		Button incluirButton = new Button("Consultar", new Action("relOpcoesResEscaneada"));
		incluirButton.getAction().add("view", true);
		table.addFooter(incluirButton);
		
		tableCol1.add(table);
		
		if(this.usuario!=null)
		{
			table = new Table(2);
			table.addSubtitle("Aseguradoras");
			for(Iterator<Aseguradora> i = aseguradoras.iterator() ; i.hasNext() ; )
			{
				Aseguradora aseg = i.next();
				
				Block block = new Block(Block.HORIZONTAL);
				block.add(new Check("check", new Long(aseg.obterId()).toString(),this.esolhidas.contains(aseg)));
				block.add(new Space(2));
				block.add(new Label(aseg.obterNome()));
				
				table.add(block);
			}
			
			incluirButton = new Button("Agregar", new Action("relOpcoesResEscaneada"));
			table.addFooter(incluirButton);
			
			tableCol1.add(table);
		}
		
		mainTable.add(tableCol1);
		
		table = new Table(1);
		table.addStyle(Table.STYLE_ALTERNATE);
		table.addSubtitle("Usuarios Registrados");
		
		for(Iterator<Usuario> i = this.usuariosResEscaneada.iterator() ; i.hasNext() ; )
		{
			Usuario u = i.next();
			
			Link link = new Link(u.obterNome(), new Action("relOpcoesResEscaneada"));
			link.getAction().add("usuarioId2", u.obterId());
			link.getAction().add("view", true);
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(link);
		}
		
		mainTable.add(table);
		
		return mainTable;
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
		return new Label("Resolución Escaneada");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}