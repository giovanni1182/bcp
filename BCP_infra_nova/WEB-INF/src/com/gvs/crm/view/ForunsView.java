package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.TipoForumSelect;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Forum;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ForunsView extends PortalView {

	private Collection foruns;

	private String tipo;

	private String assunto;

	public ForunsView(Collection foruns, String tipo, String assunto)
			throws Exception {
		this.foruns = foruns;
		this.tipo = tipo;
		this.assunto = assunto;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {

		Table table2 = new Table(2);

		table2.addSubtitle("Buscar Foruns");

		table2.addHeader("Tipo do Forum:");
		table2.add(new TipoForumSelect("tipo", this.tipo));

		table2.addHeader("Asunto:");
		table2.add(new InputString("assunto", this.assunto, 50));

		table2.add("");

		Block block = new Block(Block.HORIZONTAL);

		Button localizarButton = new Button("Buscar", new Action(
				"localizarForuns"));
		block.add(localizarButton);

		Button mostrarButton = new Button("Visualizar todos los Foruns",
				new Action("mostrarTodosForuns"));
		block.add(new Space(4));
		block.add(mostrarButton);

		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.add(block);

		Table table = new Table(6);

		table.setWidth("100%");

		table.addStyle(Table.STYLE_ALTERNATE);
		table.addSubtitle(this.foruns.size() + " Forun(s)");

		table.addHeader("Creación");
		table.addHeader("Tipo");
		table.addHeader("Asunto");
		table.addHeader("Comentarios");
		table.addHeader("Responsable");
		table.addHeader("Status");

		for (Iterator i = this.foruns.iterator(); i.hasNext();) {
			Forum forum = (Forum) i.next();

			String data = new SimpleDateFormat("dd/MM/yyyy").format(forum
					.obterCriacao());

			table.add(data);
			table.add(forum.obterTipo());

			Link link = new Link(forum.obterTitulo(), new Action(
					"visualizarEvento"));
			link.getAction().add("id", forum.obterId());

			table.add(link);

			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(new Label(forum.obterComentarios().size()));
			table.add(forum.obterResponsavel().obterNome());

			if (forum.obterFase().obterCodigo().equals(Forum.EVENTO_PENDENTE))
				table.add("Abierto");
			else
				table.add("Concluido");
		}

		table2.setNextColSpan(table2.getColumns());
		table2.add(table);

		return table2;
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception {
		return new Label("Foruns");
	}

	public Entidade getOrigemMenu() throws Exception {
		return null;
	}
}