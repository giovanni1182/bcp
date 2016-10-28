package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadeNomeLink;
import com.gvs.crm.component.EventoImage;
import com.gvs.crm.component.EventoTipoLabel;
import com.gvs.crm.component.EventoTituloLink;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Usuario;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class CompromissosSemanaisView extends BasicView {
	private Usuario usuario;

	private Entidade origemMenu;

	public CompromissosSemanaisView(Usuario usuario, Entidade origemMenu) {
		this.usuario = usuario;
		this.origemMenu = origemMenu;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		String diaString = properties.getProperty("_dia");

		Calendar dia = Calendar.getInstance();
		dia.setFirstDayOfWeek(Calendar.SUNDAY);
		if (diaString == null) {
			dia.setTimeInMillis(System.currentTimeMillis());
			dia.set(Calendar.MILLISECOND, 0);
			dia.set(Calendar.SECOND, 0);
			dia.set(Calendar.MINUTE, 0);
			dia.set(Calendar.HOUR_OF_DAY, 0);
			dia.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		} else {
			dia.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(diaString));
		}
		dia.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

		Calendar semanaAnterior = Calendar.getInstance();
		semanaAnterior.setTime(dia.getTime());
		semanaAnterior.add(Calendar.WEEK_OF_MONTH, -1);
		semanaAnterior.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

		Calendar proximaSemana = Calendar.getInstance();
		proximaSemana.setTime(dia.getTime());
		proximaSemana.add(Calendar.WEEK_OF_MONTH, 1);
		proximaSemana.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

		Calendar hoje = Calendar.getInstance();
		hoje.setTimeInMillis(System.currentTimeMillis());
		hoje.set(Calendar.MILLISECOND, 0);
		hoje.set(Calendar.SECOND, 0);
		hoje.set(Calendar.MINUTE, 0);
		hoje.set(Calendar.HOUR_OF_DAY, 0);

		Table table = new Table(8);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);

		table.setNextWidth("70");
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(new SimpleDateFormat("yyyy").format(dia.getTime()));

		table.setNextWidth("40");
		table.addHeader("");

		table.setNextWidth("40");
		table.addHeader("Inicio");

		table.setNextWidth("40");
		table.addHeader("Fin");

		table.setNextWidth("*");
		table.addHeader("Título");

		table.setNextWidth("*");
		table.addHeader("Tipo");

		table.setNextWidth("*");
		table.addHeader("Fase");

		table.setNextWidth("*");
		table.addHeader("Objeto");
		Collection<Evento> compromissos = new ArrayList<>();
		
		for (int d = 0; d < 7; d++)
		{
			compromissos = usuario.obterCompromissos(dia.getTime());

			Label diaLabel = new Label(dia.getTime(), "EEE dd/MMM");
			diaLabel.setItalic(dia.getTimeInMillis() < hoje.getTimeInMillis());
			//			diaLabel.setBold(dia.getTimeInMillis() ==
			// hoje.getTimeInMillis());
			diaLabel.setBold(new SimpleDateFormat("dd/MM/yyyy").format(
					dia.getTime()).equals(
					new SimpleDateFormat("dd/MM/yyyy").format(hoje.getTime())));
			Link diaLink = new Link(diaLabel, new Action("novoEvento"));
			diaLink.getAction().add("passo", 1);
			diaLink.getAction().add("data", dia.getTime());
			diaLink.getAction().add("origemMenuId", this.origemMenu.obterId());
			table.setNextHAlign(Table.HALIGN_RIGHT);

			if (compromissos.size() == 0) {
				table.addData(diaLink);
				table.addData(new Space());
				table.addData(new Space());
				table.addData(new Space());
				table.addData(new Space());
				table.addData(new Space());
				table.addData(new Space());
				table.addData(new Space());
			}
			else
			{
				boolean primeiro = true;
				for (Evento evento : compromissos)
				{
					if (primeiro) 
					{
						primeiro = false;
						table.addData(diaLink);
					}
					else 
						table.addData("");
					table.addData(new EventoImage(evento));
					table.addData(new Label(evento.obterDataPrevistaInicio(), "HH:mm"));
					table.addData(new Label(evento.obterDataPrevistaConclusao(), "HH:mm"));
					table.addData(new EventoTituloLink(evento));
					table.addData(new EventoTipoLabel(evento));
					if(evento.obterFase()!=null)
						table.addData(evento.obterFase().obterNome());
					else
						table.add("");
					if(evento.obterOrigem()!=null)
						table.addData(new EntidadeNomeLink(evento.obterOrigem()));
					else
						table.add("");
				}
			}

			dia.add(Calendar.DAY_OF_WEEK, 1);
		}

		Link semanaAnteriorLink = new Link("<< Semana Anterior", new Action(
				"visualizarPaginaInicial"));
		semanaAnteriorLink.getAction().add("_dia", semanaAnterior.getTime());
		semanaAnteriorLink.getAction()
				.add("origemMenuId", origemMenu.obterId());

		Link semanaAtualLink = new Link("Semana Actual", new Action(
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