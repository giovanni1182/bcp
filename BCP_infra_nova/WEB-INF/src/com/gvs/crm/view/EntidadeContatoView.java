package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Select;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class EntidadeContatoView extends PortalView {
	private Entidade entidade;

	private Entidade.Contato contato;

	private boolean novo;

	private Entidade origemMenu;

	private Evento retorno;

	public EntidadeContatoView(Entidade entidade) throws Exception {
		this.entidade = entidade;
		this.novo = true;
	}

	public EntidadeContatoView(Entidade.Contato contato) throws Exception {
		this.entidade = contato.obterEntidade();
		this.contato = contato;
		this.novo = false;
	}

	public EntidadeContatoView(Entidade.Contato contato, Entidade origemMenu)
			throws Exception {
		this.entidade = contato.obterEntidade();
		this.contato = contato;
		this.novo = false;
		this.origemMenu = origemMenu;
	}

	public EntidadeContatoView(Entidade entidade, Entidade origemMenu)
			throws Exception {
		this.entidade = entidade;
		this.novo = true;
		this.origemMenu = origemMenu;
	}

	public EntidadeContatoView(Entidade entidade, Entidade origemMenu,
			Evento retorno) throws Exception {
		this.entidade = entidade;
		this.novo = true;
		this.origemMenu = origemMenu;
		this.retorno = retorno;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(user);

		Table table = new Table(2);
		if (this.novo) {
			table.addHeader("Tipo do Contacto:");
			Block block = new Block(Block.HORIZONTAL);
			Select tipoSelecionado = new Select("nomeSelecionado", 1);
			//tipoSelecionado.add("[selecione o nome]", "", false);
			Collection nomes = this.entidade.obterNomesContatos();
			for (Iterator i = nomes.iterator(); i.hasNext();) {
				String nome = (String) i.next();
				tipoSelecionado.add(nome, nome, false);
			}
			block.add(tipoSelecionado);
			block.add(new Space());

			if (usuarioAtual.obterId() == 1)
				block.add(new InputString("nome", "", 32));

			table.addData(block);

			table.addHeader("Persona de Contacto:");
			table.addData(new InputString("nome_contato", "", 50));

			table.addHeader("Dato de Contato:");
			table.addData(new InputString("valor", "", 80));

			Action incluirAction = new Action("incluirEntidadeContato");
			incluirAction.add("entidadeId", this.entidade.obterId());
			if (this.retorno != null)
				incluirAction.add("retornoId", this.retorno.obterId());
			Button incluirButton = new Button("Agregar", incluirAction);
			table.addFooter(incluirButton);
		} else {
			table.addHeader("Tipo do Contacto:");
			table.addData(this.contato.obterNome());

			table.addHeader("Nombre do Contacto:");
			table.addData(new InputString("nome_contato", this.contato
					.obterNomeContato(), 50));

			table.addHeader("Dato de Contato:");
			table.addData(new InputString("valor", this.contato.obterValor(),
					80));

			Action atualizarAction = new Action("atualizarEntidadeContato");
			atualizarAction.add("entidadeId", this.entidade.obterId());
			atualizarAction.add("id", this.contato.obterId());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			table.addFooter(atualizarButton);

			Action excluirAction = new Action("excluirEntidadeContato");
			excluirAction.add("entidadeId", this.entidade.obterId());
			excluirAction.add("id", this.contato.obterId());
			excluirAction.setConfirmation("Confirma exclusión del contacto ?");
			Button excluirButton = new Button("Eliminar", excluirAction);
			table.addFooter(excluirButton);
		}
		Action cancelarAction = new Action("visualizarDetalhesEntidade");
		cancelarAction.add("id", this.entidade.obterId());
		table.addFooter(new Button("Cancelar", cancelarAction));
		return table;
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception {
		if (this.novo)
			return new Label(this.entidade.obterNome() + " - Nuevo Contacto");
		else
			return new Label(this.entidade.obterNome() + "- Contacto");
	}

	public Entidade getOrigemMenu() throws Exception {
		return this.origemMenu;
	}
}