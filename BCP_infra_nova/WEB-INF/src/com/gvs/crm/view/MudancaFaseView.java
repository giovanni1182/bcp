package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.DataHoraInput;
import com.gvs.crm.component.EventoComentarioInput;
import com.gvs.crm.component.FasesSelect;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Processo;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class MudancaFaseView extends PortalView {
	private Evento evento;

	private Action action;

	private String nomeBotao;

	private boolean proxima;

	private boolean cancelar;

	public MudancaFaseView(Evento evento, Action action, String nomeBotao,
			boolean proxima, boolean cancelar) throws Exception {
		this.evento = evento;
		this.action = action;
		this.nomeBotao = nomeBotao;
		this.proxima = proxima;
		this.cancelar = cancelar;
	}

	public View getBody(User user, Locale locale, Properties properties)throws Exception
	{
		Table table = new Table(2);
		//table.setWidth("100%");
		table.setNextColSpan(table.getColumns());
		table.add(new EventoSumarioView(this.evento));
		table.addSubtitle(evento.obterClasseDescricao());

		if (this.evento instanceof Processo)
		{
			table.addSubtitle("Agenda");

			table.addHeader("Data/Hora:");
			table.add(new DataHoraInput("data", "hora", null, true));
			table.setNextColSpan(table.getColumns());
			table.add(new Space());
		}

		table.addHeader("Comentário:");
		table.add(new EventoComentarioInput("comentario", ""));

		if (this.proxima)
		{
			table.addHeader("Próxima Fase:");
			table.addHeader(new FasesSelect("fase", this.evento, true));
		}
		else
		{
			table.addHeader("Fase Anterior:");
			table.addHeader(new FasesSelect("fase", this.evento, false));
		}

		Button enviarButton = null;

		this.action.getParameters().keySet().remove("view");

		if (this.proxima)
			enviarButton = new Button("Próxima Fase >>>", new Action(
					"proximaFase"));
		else
			enviarButton = new Button("<<< Fase Anterior", new Action(
					"faseAnterior"));

		enviarButton.getAction().add("view", false);
		enviarButton.getAction().add("id", this.evento.obterId());

		table.addFooter(enviarButton);

		Button voltarButton = new Button("Voltar", new Action(
				"visualizarEvento"));
		voltarButton.getAction().add("id", this.evento.obterId());
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
		return new Label(this.evento.obterClasseDescricao());
	}

	public Entidade getOrigemMenu() throws Exception {
		Entidade entidadeMenu = null;
		if (evento.obterOrigem() != null) {
			entidadeMenu = evento.obterOrigem();
		}
		return entidadeMenu;
	}
}