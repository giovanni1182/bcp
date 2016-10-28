package com.gvs.crm.view;

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

public class EntidadeEnderecoView extends PortalView {
	private Entidade.Endereco endereco;

	private boolean incluir;

	private Entidade origemMenu;

	private Evento retorno;

	public EntidadeEnderecoView(Entidade.Endereco endereco) {
		this.endereco = endereco;
		this.incluir = endereco.obterId() == 0;
	}

	public EntidadeEnderecoView(Entidade.Endereco endereco, Entidade origemMenu) {
		this.endereco = endereco;
		this.incluir = endereco.obterId() == 0;
		this.origemMenu = origemMenu;
	}

	public EntidadeEnderecoView(Entidade.Endereco endereco,
			Entidade origemMenu, Evento retorno) {
		this.endereco = endereco;
		this.incluir = endereco.obterId() == 0;
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
		table.addHeader("Tipo de Dirección:");
		Block block = new Block(Block.HORIZONTAL);
		Select nomeSelecionado = new Select("nomeSelecionado", 1);
		//nomeSelecionado.add("[selecione o nome]", "", false);
		for (Iterator i = this.endereco.obterEntidade().obterNomesEnderecos()
				.iterator(); i.hasNext();) {
			String nome = (String) i.next();
			nomeSelecionado.add(nome, nome, nome.equals(this.endereco
					.obterNome()));
		}
		block.add(nomeSelecionado);
		block.add(new Space());
		if (usuarioAtual.obterId() == 1)
			block.add(new InputString("nome", this.endereco.obterNome(), 32));
		table.addData(block);
		table.addHeader("Calle:");
		table.addData(new InputString("rua", this.endereco.obterRua(), 32));
		table.addHeader("Número:");
		table
				.addData(new InputString("numero", this.endereco.obterNumero(),
						16));
		table.addHeader("Complemento:");
		table.addData(new InputString("complemento", this.endereco
				.obterComplemento(), 32));
		table.addHeader("Caja Postal:");
		table.addData(new InputString("cep", this.endereco.obterCep(), 16));
		table.addHeader("Barrio:");
		table
				.addData(new InputString("bairro", this.endereco.obterBairro(),
						32));
		table.addHeader("Ciudad:");
		table
				.addData(new InputString("cidade", this.endereco.obterCidade(),
						32));
		table.addHeader("Departamento:");
		table
				.addData(new InputString("estado", this.endereco.obterEstado(),
						16));
		table.addHeader("País:");
		table.addData(new InputString("pais", this.endereco.obterPais(), 16));

		if (this.incluir) {
			Action incluirAction = new Action("incluirEntidadeEndereco");
			incluirAction.add("entidadeId", this.endereco.obterEntidade()
					.obterId());
			if (this.retorno != null)
				incluirAction.add("retornoId", this.retorno.obterId());
			Button incluirButton = new Button("Agregar", incluirAction);
			table.addFooter(incluirButton);
		} else {
			Action atualizarAction = new Action("atualizarEntidadeEndereco");
			atualizarAction.add("entidadeId", this.endereco.obterEntidade()
					.obterId());
			atualizarAction.add("id", this.endereco.obterId());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			table.addFooter(atualizarButton);
			Action excluirAction = new Action("excluirEntidadeEndereco");
			excluirAction.add("entidadeId", this.endereco.obterEntidade()
					.obterId());
			excluirAction.add("id", this.endereco.obterId());
			excluirAction
					.setConfirmation("Confirma exclusión del Direccione ?");
			Button excluirButton = new Button("Excluir", excluirAction);
			table.addFooter(excluirButton);
		}

		Action cancelarAction = null;

		cancelarAction = new Action("visualizarDetalhesEntidade");
		cancelarAction.add("id", this.endereco.obterEntidade().obterId());
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
		if (this.incluir)
			return new Label(this.endereco.obterEntidade().obterNome()
					+ " - Nueva Dirección");
		else
			return new Label(this.endereco.obterEntidade().obterNome()
					+ "- Dirección");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gvs.crm.view.PortalView#getOrigemMenu()
	 */
	public Entidade getOrigemMenu() throws Exception {
		return this.origemMenu;
	}
}