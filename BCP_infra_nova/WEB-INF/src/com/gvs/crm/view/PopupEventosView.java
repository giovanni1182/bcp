package com.gvs.crm.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.Border;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.ApoliceHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EventoHome;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class PopupEventosView extends BasicView {
	private String campoEventoId;

	private String campoEventoTitulo;

	private String tipoFixo;

	private String tipo;

	private String nome;

	private String aseguradora;

	private String secao;

	public PopupEventosView(String campoEventoId, String campoEventoTitulo,
			String tipo, String nome, String aseguradora, String secao) {
		this.campoEventoId = campoEventoId;
		this.campoEventoTitulo = campoEventoTitulo;
		this.tipo = tipo;
		this.nome = nome;
		this.aseguradora = aseguradora;
		this.secao = secao;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		ApoliceHome apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
		Table table = new Table(2);
		table.setWidth("100%");
		table.addHeader("Nº Instrumento:");
		table.addData(new InputString("nome", this.nome, 40));
		/*
		 * table.addHeader("Aseguradora:"); table.addData(new
		 * InputString("aseguradora", this.aseguradora, 12));
		 * table.addHeader("Sección:"); table.addData(new InputString("secao",
		 * this.secao, 12));
		 */

		Action popupEntidadesAction = new Action("popupEventos");
		popupEntidadesAction.add("campoEventoId", this.campoEventoId);
		popupEntidadesAction.add("campoEventoTitulo", this.campoEventoTitulo);
		popupEntidadesAction.add("tipo", this.tipo);
		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		Block block = new Block(Block.HORIZONTAL);
		block.add(new Button("Buscar", popupEntidadesAction));
		block.add(new Space(2));
		block.add(new Button("Cancelar", new Action(Action.CLOSE)));
		table.addData(block);
		Collection eventos = new ArrayList();

		if (this.nome.length() > 0 || this.aseguradora.length() > 0
				|| this.secao.length() > 0)
			eventos = apoliceHome.localizarApolices(this.nome, null, null,
					null, null);

		if (eventos.size() > 0) {
			Table entidadesTable = new Table(5);
			entidadesTable.setWidth("100%");
			entidadesTable.addStyle(Table.STYLE_ALTERNATE);
			entidadesTable.addHeader("Id");
			entidadesTable.addHeader("Titulo");
			entidadesTable.addHeader("Tipo");
			entidadesTable.addHeader("Aseguradora");
			entidadesTable.addHeader("Sección");

			for (Iterator i = eventos.iterator(); i.hasNext();) {
				Apolice apolice = (Apolice) i.next();

				Action returnAction = new Action(Action.RETURN);
				entidadesTable.addData(new Label(apolice.obterId()));
				entidadesTable.addData(new Link(apolice.obterTitulo(),
						returnAction));

				returnAction.add(this.campoEventoId, apolice.obterId());
				returnAction.add(this.campoEventoTitulo, apolice.obterTitulo());

				entidadesTable.addData(apolice.obterClasseDescricao());

				String aseguradora = apolice.obterOrigem().obterNome();
				entidadesTable.addData(aseguradora);

				String secao = apolice.obterSecao().obterNome() + " - "
						+ apolice.obterSecao().obterApelido();
				entidadesTable.addData(secao);

			}
			table.setNextColSpan(table.getColumns());
			table.addData(entidadesTable);
		} else {
			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			table.setNextColSpan(table.getColumns());
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Ningún evento a ser visualizado.");
		}
		return new Border(table);
	}
}