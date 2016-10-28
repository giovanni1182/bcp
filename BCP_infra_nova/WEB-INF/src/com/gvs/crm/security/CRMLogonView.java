package com.gvs.crm.security;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.Border;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputPassword;
import infra.view.InputString;
import infra.view.Label;
import infra.view.LogonView;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class CRMLogonView extends LogonView {
	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		//       	Label label = new Label("Acesso al Sistema Contable Estandarizado");
		//       	label.setBold(true);

		Table table = new Table(2);
		table.setTitle("Iniciar Sesión");
		table.setNextColSpan(table.getColumns());
		table.add(new Space());
		table.addHeader(new Label("Usuario:"));
		table.addData(new InputString("key", this.getKey(), 15));
		table.addHeader(new Label("Contraseña:"));
		table.addData(new InputPassword("password", 15));
		table.addFooter(new Button("Ingresar", new Action("_logon")));
		table.setNextColSpan(table.getColumns());
		Border border = new Border(table);
		border.setWidth(null);
		//border.addSubtitle(label);
		return border;
	}
}