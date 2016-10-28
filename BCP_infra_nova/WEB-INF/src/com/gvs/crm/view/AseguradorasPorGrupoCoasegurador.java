package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class AseguradorasPorGrupoCoasegurador extends PortalView {

	private Aseguradora aseguradora;

	private Collection lista;

	private String codigo;

	public AseguradorasPorGrupoCoasegurador(Aseguradora aseguradora,
			Collection lista, String codigo) throws Exception {
		this.aseguradora = aseguradora;
		this.lista = lista;
		this.codigo = codigo;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(1);

		table.addSubtitle(lista.size() + " Aseguradora(s)");

		table.addStyle(Table.STYLE_ALTERNATE);

		table.addHeader("Aseguradora");

		for (Iterator i = lista.iterator(); i.hasNext();) {
			Aseguradora aseguradora = (Aseguradora) i.next();

			table.add(aseguradora.obterNome());
		}

		Button voltarButton = new Button("Volver", new Action(
				"visualizarDetalhesEntidade"));
		voltarButton.getAction().add("id", aseguradora.obterId());

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
		return new Label("Aseguradoras do Grupo Coasegurador " + this.codigo);
	}

	public Entidade getOrigemMenu() throws Exception {
		return null;
	}
}