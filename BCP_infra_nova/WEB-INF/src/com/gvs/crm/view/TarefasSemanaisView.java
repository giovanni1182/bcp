package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.DuracaoLabel;
import com.gvs.crm.component.EventoImage;
import com.gvs.crm.component.EventoTituloLink;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Tarefa;
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

public class TarefasSemanaisView extends BasicView {
	private Usuario usuario;

	private Entidade origemMenu;

	public TarefasSemanaisView(Usuario usuario, Entidade origemMenu)
			throws Exception {
		this.usuario = usuario;
		this.origemMenu = origemMenu;
	}

	private void adicionarTarefa(Table table, Tarefa tarefa) throws Exception {
		table.addData(new EventoImage(tarefa));
		table.addData(new EventoTituloLink(tarefa));
		table.addData(new Label(tarefa.obterDataPrevistaInicio(),
				"EEE dd/MM/yyyy"));
		table.addData(new Label(tarefa.obterDataPrevistaConclusao(),
				"EEE dd/MM/yyyy"));
		table.addData(new DuracaoLabel(tarefa.obterDuracao()));
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		String diaString = properties.getProperty("_dia");

		Calendar domingo = Calendar.getInstance();
		domingo.setFirstDayOfWeek(Calendar.SUNDAY);
		if (diaString == null) {
			domingo.setTimeInMillis(System.currentTimeMillis());
			domingo.set(Calendar.MILLISECOND, 0);
			domingo.set(Calendar.SECOND, 0);
			domingo.set(Calendar.MINUTE, 0);
			domingo.set(Calendar.HOUR_OF_DAY, 0);
			domingo.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		} else {
			domingo
					.setTime(new SimpleDateFormat("dd/MM/yyyy")
							.parse(diaString));
		}
		domingo.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

		Calendar sabado = Calendar.getInstance();
		sabado.setTime(domingo.getTime());
		sabado.add(Calendar.DAY_OF_WEEK, 6);

		Calendar hoje = Calendar.getInstance();
		hoje.setTimeInMillis(System.currentTimeMillis());
		hoje.set(Calendar.MILLISECOND, 0);
		hoje.set(Calendar.SECOND, 0);
		hoje.set(Calendar.MINUTE, 0);
		hoje.set(Calendar.HOUR_OF_DAY, 0);

		Calendar semanaAnterior = Calendar.getInstance();
		semanaAnterior.setTime(domingo.getTime());
		semanaAnterior.add(Calendar.WEEK_OF_MONTH, -1);

		Calendar proximaSemana = Calendar.getInstance();
		proximaSemana.setTime(domingo.getTime());
		proximaSemana.add(Calendar.WEEK_OF_MONTH, 1);

		Table table = new Table(5);
		table.addStyle(Table.STYLE_ALTERNATE);
		table.setWidth("100%");
		table.addHeader("");
		table.addHeader("Título");
		table.addHeader("Início");
		table.addHeader("Conclusão");
		table.addHeader("Duração");

		table.addSubtitle("Tarefas atrasadas");
		for (Iterator i = usuario.obterTarefasAtrasadas(hoje.getTime())
				.iterator(); i.hasNext();)
			this.adicionarTarefa(table, (Tarefa) i.next());

		table.addSubtitle("Tarefas sem data definida");
		for (Iterator i = usuario.obterTarefasPendentes().iterator(); i
				.hasNext();)
			this.adicionarTarefa(table, (Tarefa) i.next());

		Block block = new Block(Block.HORIZONTAL);
		block.add(new Label("Tarefas da semana de"));
		block.add(new Space());
		block.add(new Label(domingo.getTime(), "EEEE dd/MM/yyyy"));
		block.add(new Space());
		block.add(new Label("até"));
		block.add(new Space());
		block.add(new Label(sabado.getTime(), "EEEE dd/MM/yyyy"));
		table.addSubtitle(block);
		for (Iterator i = usuario.obterTarefasPendentes(domingo.getTime(),
				sabado.getTime()).iterator(); i.hasNext();)
			this.adicionarTarefa(table, (Tarefa) i.next());

		Link semanaAnteriorLink = new Link("<< Semana Anterior", new Action(
				"visualizarPaginaInicial"));
		semanaAnteriorLink.getAction().add("_dia", semanaAnterior.getTime());
		semanaAnteriorLink.getAction()
				.add("origemMenuId", origemMenu.obterId());

		Link semanaAtualLink = new Link("Semana Atual", new Action(
				"visualizarPaginaInicial"));
		semanaAtualLink.getAction().add("_dia", hoje.getTime());
		semanaAtualLink.getAction().add("origemMenuId", origemMenu.obterId());

		Link proximaSemanaLink = new Link("Próxima Semana >>", new Action(
				"visualizarPaginaInicial"));
		proximaSemanaLink.getAction().add("_dia", proximaSemana.getTime());
		proximaSemanaLink.getAction().add("origemMenuId", origemMenu.obterId());

		Table linksTable = new Table(3);
		linksTable.setWidth("100%");
		linksTable.addData(semanaAnteriorLink);
		linksTable.setNextHAlign(Table.HALIGN_CENTER);
		linksTable.addData(semanaAtualLink);
		linksTable.setNextHAlign(Table.HALIGN_RIGHT);
		linksTable.addData(proximaSemanaLink);

		table.setNextColSpan(table.getColumns());
		table.add(linksTable);

		return table;
	}
}