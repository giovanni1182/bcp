package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Usuario;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class AgendaView extends BasicView {
	private Usuario usuario;

	private Entidade origemMenu;

	public AgendaView(Usuario usuario, Entidade origemMenu) {
		this.usuario = usuario;
		this.origemMenu = origemMenu;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		String agenda = properties.getProperty("_agenda");
		if (agenda == null)
			agenda = "compromissos_semana";

		Table table = new Table(1);
		table.setWidth("100%");
		table.addSubtitle("Agenda");

		Label compromissosLabel = new Label("Compromisos");
		compromissosLabel.setBold(agenda.indexOf("compromissos") >= 0);
		Link compromissosLink = new Link(compromissosLabel, new Action(
				"visualizarPaginaInicial"));
		compromissosLink.getAction().add("_agenda", "compromissos_semana");
		compromissosLink.getAction().add("origemMenuId", origemMenu.obterId());

		/*
		 * Label tarefasLabel = new Label("Tareas");
		 * tarefasLabel.setBold(agenda.indexOf("tarefas") >= 0); Link
		 * tarefasLink = new Link(tarefasLabel, new
		 * Action("visualizarPaginaInicial"));
		 * tarefasLink.getAction().add("_agenda", "tarefas_semana");
		 * tarefasLink.getAction().add("origemMenuId", origemMenu.obterId());
		 */

		Table linksTable = new Table(2);
		linksTable.setWidth("100%");
		Block esquerdoBlock = new Block(Block.HORIZONTAL);
		esquerdoBlock.add(compromissosLink);
		esquerdoBlock.add(new SeparadorLabel());
		//esquerdoBlock.add(tarefasLink);
		linksTable.add(esquerdoBlock);

		Block direitoBlock = new Block(Block.HORIZONTAL);
		if (agenda.indexOf("compromissos") >= 0) {
			Label compromissosSemanaLabel = new Label("Semana");
			compromissosSemanaLabel.setBold(agenda.indexOf("semana") >= 0);
			Link compromissosSemanaLink = new Link(compromissosSemanaLabel,
					new Action("visualizarPaginaInicial"));
			compromissosSemanaLink.getAction().add("_agenda",
					"compromissos_semana");
			compromissosSemanaLink.getAction().add("origemMenuId",
					this.origemMenu.obterId());

			/*
			 * Label compromissosMesLabel = new Label("Mes");
			 * compromissosMesLabel.setBold(agenda.indexOf("mes") >= 0); Link
			 * compromissosMesLink = new Link(compromissosMesLabel, new
			 * Action("visualizarPaginaInicial"));
			 * compromissosMesLink.getAction().add("_agenda",
			 * "compromissos_mes");
			 * compromissosMesLink.getAction().add("origemMenuId",
			 * this.origemMenu.obterId());
			 */

			direitoBlock.add(compromissosSemanaLink);
			//direitoBlock.add(new SeparadorLabel());
			//direitoBlock.add(compromissosMesLink);
		}
		linksTable.setNextHAlign(Table.HALIGN_RIGHT);
		linksTable.add(direitoBlock);

		table.add(linksTable);
		if (agenda.equals("compromissos_semana"))
			table.add(new CompromissosSemanaisView(usuario, this.origemMenu));
		else if (agenda.equals("tarefas_semana"))
			table.add(new TarefasSemanaisView(usuario, this.origemMenu));
		else
			table.add(new Space());

		return table;
	}
}