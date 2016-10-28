package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class UsuariosView extends PortalView
{
	private Collection<Usuario> usuarios;
	
	public UsuariosView(Collection<Usuario> usuarios) throws Exception
	{
		this.usuarios = usuarios;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(user);
		UsuarioHome home = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = home.obterUsuarioPorUser(user);
		
		boolean admin = usuarioAtual.obterId() == 1;
		
		Table table = new Table(5);
		table.addStyle(Table.STYLE_ALTERNATE);
		table.setWidth("90%");
		
		table.addSubtitle("");
		
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Fecha Catastro");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Nombre");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Login");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Contraseña");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Nivel");
		
		Link link;
		
		for(Usuario usuario : this.usuarios)
		{
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(new SimpleDateFormat("dd/MM/yyyy").format(usuario.obterCriacao()));
			
			link = new Link(usuario.obterNome(),new Action("visualizarDetalhesEntidade"));
			link.getAction().add("id", usuario.obterId());
			table.add(link);
			table.add(usuario.obterChave());
			if(admin)
				table.add(usuario.obterSenha());
			else
				table.add("-");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(usuario.obterNivel());
		}
		
		Button excelButton = new Button("Generar PDF",new Action("usuariosCadastradosPDF"));
		table.addFooter(excelButton);
		
		
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
		return new Label("Listado Usuarios");
	}

}
