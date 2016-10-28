package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Log;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Table;
import infra.view.View;

public class LogView extends EventoAbstratoView {
	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		Log log = (Log) this.obterEvento();
		Table table = new Table(2);

		table.addHeader("Titulo:");
		table.add(log.obterTitulo());
		table.addHeader("Tipo:");
		table.add(log.obterTipo());
		table.addHeader("Descripcion:");
		table.add(log.obterDescricao());

		CRMModelManager mm = new CRMModelManager(user);

		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade administrador = (Entidade) home
				.obterEntidadePorApelido("admin");
		Button voltarButton = new Button("Volver", new Action("exibirLogs"));
		voltarButton.getAction().add("id", administrador.obterId());

		Button excluirButton = new Button("Eliminar", new Action(
				"excluirEvento"));
		excluirButton.getAction().add("id", log.obterId());

		table.addFooter(voltarButton);

		table.addFooter(excluirButton);

		return table;
	}
}