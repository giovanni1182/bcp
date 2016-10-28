package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.DateLabel;
import com.gvs.crm.component.ResponsavelLabel;
import com.gvs.crm.component.SuperiorLabel;
import com.gvs.crm.component.UsuarioNivelSelect;
import com.gvs.crm.model.Usuario;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class UsuarioView extends EntidadeAbstrataView
{
	private static final int DETALHES = 1;
	
	//private static final int EVENTOS = 2;
	
	public View execute(User user, Locale locale, Properties properties) throws Exception
	{
		Usuario usuario = (Usuario) this.obterEntidade();
		boolean novo = usuario.obterId() == 0;
		Table table = new Table(2);
		
		int _pasta = Integer.parseInt(properties.getProperty("_pastaUsuario", "1"));
		
		//if (novo || _pasta > 2)
			_pasta = DETALHES;
		
		Link dadosLink = new Link("Detalles", new Action("visualizarDetalhesEntidade"));
		((Label) dadosLink.getCaption()).setBold(_pasta == DETALHES);
		dadosLink.getAction().add("id", usuario.obterId());
		dadosLink.getAction().add("_pastaUsuario", DETALHES);
		dadosLink.setEnabled(!novo);
		
		/*Link eventosLink = new Link("Eventos Vinculados", new Action("visualizarDetalhesEntidade"));
		((Label) eventosLink.getCaption()).setBold(_pasta == EVENTOS);
		eventosLink.getAction().add("id", usuario.obterId());
		eventosLink.getAction().add("_pastaUsuario", EVENTOS);
		eventosLink.setEnabled(!novo);*/
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(dadosLink);
		//block.add(new SeparadorLabel());
		//block.add(eventosLink);
		
		table.setNextColSpan(table.getColumns());
		table.add(block);

		if(_pasta == DETALHES)
		{
			table.addSubtitle("");
			
			if (!novo)
			{
				table.addHeader("Creado en:");
				table.addData(new DateLabel(usuario.obterCriacao()));
			}
			
			table.addHeader("Superior:");
			table.addData(new SuperiorLabel(usuario));
			
			if (!novo)
			{
				table.addHeader("Responsable:");
				table.addData(new ResponsavelLabel(usuario));
			}
			
			table.addHeader("Nombre:");
			table.addData(new InputString("nome", usuario.obterNome(), 64));
			table.addHeader("Denominación:");
			table.addData(new InputString("apelido", usuario.obterApelido(), 32));
			table.addHeader("Clave de Acceso:");
			table.addData(new InputString("chave", usuario.obterChave(), 20));
			table.addHeader("Nivel:");
			table.addData(new UsuarioNivelSelect("nivel",usuario.obterNivel()));
	
			table.addHeader("Datos Adicionales:");
			table.addData(new EntidadeAtributosView(usuario));
	
			table.addSubtitle("Contactos:");
			table.addHeader("Contactos:");
			table.addData(new EntidadeContatosView(usuario));
	
			table.addSubtitle("Direcciones:");
			table.addHeader("Direcciones:");
			table.add(new EntidadeEnderecosView(usuario));
	
			if (novo)
			{
				Button incluirButton = new Button("Agregar", new Action("incluirUsuario"));
				incluirButton.getAction().add("superiorId",usuario.obterSuperior().obterId());
				table.addFooter(incluirButton);
	
				Action cancelarAction = new Action("visualizarPaginaInicial");
				table.addFooter(new Button("Cancelar", cancelarAction));
	
				Action voltarAction = new Action("novaEntidade");
				voltarAction.add("superiorId", usuario.obterSuperior().obterId());
				voltarAction.add("origemMenuId", this.obterOrigemMenu().obterId());
	
				Button voltarButton = new Button("Volver", voltarAction);
				table.addFooter(voltarButton);
			}
			else
			{
				//if (usuario.permiteAtualizar())
				//{
					Button atualizarButton = new Button("Actualizar", new Action("atualizarUsuario"));
					atualizarButton.getAction().add("id", usuario.obterId());
					atualizarButton.setEnabled(usuario.permiteAtualizar());
					table.addFooter(atualizarButton);
				//}
	
				//if (usuario.permiteExcluir()) 
				//{
					Button excluirButton = new Button("Eliminar", new Action("excluirUsuarioLogicamente"));
					excluirButton.getAction().add("id", usuario.obterId());
					excluirButton.getAction().add("_entidadeId",usuario.obterSuperior().obterId());
					excluirButton.setEnabled(usuario.permiteExcluir());
					excluirButton.getAction().setConfirmation("Confirma exclusión del usuario ?");
					table.addFooter(excluirButton);
				//}
	
			}
		}
		/*else if(_pasta == EVENTOS)
		{
			table.addSubtitle("");
			
			table.add(new EventosVinculadosUsuarioView(usuario));
		}*/
		return table;
	}
}