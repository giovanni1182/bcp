package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Parametro;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class FeriadoView extends PortalView {

	private Entidade entidade;

	private Parametro.Feriado feriado;

	private Entidade origemMenu;

	private boolean novo;

	public FeriadoView(Parametro.Feriado feriado, Entidade origemMenu)
			throws Exception {
		this.entidade = feriado.obterEntidade();
		this.feriado = feriado;
		this.origemMenu = origemMenu;
		this.novo = false;
	}

	public FeriadoView(Entidade entidade, Entidade origemMenu) throws Exception {
		this.entidade = entidade;
		this.origemMenu = origemMenu;
		this.novo = true;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(user);

		Table table = new Table(2);

		table.addHeader("Fecha:");

		if (this.novo)
			table.add(new InputDate("data", null));
		else
			table.add(new InputDate("data", feriado.obterDataFeriado()));

		table.addHeader("Descripción:");

		if (this.novo)
			table.add(new InputText("descricao", null, 4, 80));
		else
			table.add(new InputText("descricao", feriado
					.obterDescricaoFeriado(), 4, 80));

		if (this.novo) {
			Action incluirAction = new Action("incluirFeriado");
			incluirAction.add("entidadeId", this.entidade.obterId());

			Button incluirButton = new Button("Agregar", incluirAction);
			table.addFooter(incluirButton);
		} else {
			Action atualizarAction = new Action("atualizarFeriado");
			atualizarAction.add("entidadeId", this.entidade.obterId());
			atualizarAction.add("id", this.feriado.obterId());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			table.addFooter(atualizarButton);
		}

		Action voltarAction = new Action("visualizarDetalhesEntidade");
		voltarAction.add("id", this.entidade.obterId());
		Button voltarButton = new Button("Volver", voltarAction);
		table.addFooter(voltarButton);

		table.setNextColSpan(table.getColumns());
		table.add(new FeriadosView(this.entidade));
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
			return new Label(this.entidade.obterNome() + " - Nuevo Feriado");
		else
			return new Label(this.entidade.obterNome() + "- Feriado");
	}

	public Entidade getOrigemMenu() throws Exception {
		return this.origemMenu;
	}
}

