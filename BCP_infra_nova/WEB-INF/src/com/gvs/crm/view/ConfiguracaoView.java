package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EventosEntidadesSelect;
import com.gvs.crm.component.SinalSelect;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class ConfiguracaoView extends PortalView {
	private Entidade parametro;

	private Entidade origemMenu;

	public ConfiguracaoView(Entidade parametro, Entidade origemMenu)
			throws Exception {
		this.parametro = parametro;
		this.origemMenu = origemMenu;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(user);

		Table table = new Table(4);

		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Evento/Entidade");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Campo");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Argumento");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Ação");

		table.add(new EventosEntidadesSelect("evento", null));
		table.add(new InputString("campo", null, 20));

		Block block = new Block(Block.HORIZONTAL);
		block.add(new SinalSelect("operador", null));
		block.add(new InputString("campo", null, 20));
		table.add(block);

		table.add("");

		Action voltarAction = new Action("visualizarDetalhesEntidade");
		voltarAction.add("id", this.parametro.obterId());
		Button voltarButton = new Button("Volver", voltarAction);
		table.addFooter(voltarButton);

		return table;
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception {
		return new Label(this.parametro.obterNome() + " - Configurações");
	}

	public Entidade getOrigemMenu() throws Exception {
		return this.origemMenu;
	}
}