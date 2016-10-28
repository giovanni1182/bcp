package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.EventoTipoSelect;
import com.gvs.crm.model.Forum;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ForumView extends EventoAbstratoView {

	public View execute(User arg0, Locale arg1, Properties arg2)
			throws Exception {

		Forum forum = (Forum) this.obterEvento();

		boolean incluir = forum.obterId() == 0;

		Table table = new Table(2);

		if (!incluir)
		{
			table.addHeader("Creado por:");
			table.add(new CriacaoLabel(forum));

			table.addHeader("Responsable:");
			table.addData(forum.obterResponsavel().obterNome());
		}

		table.addHeader("Tipo do Forum:");
		if (incluir)
			table.add(new EventoTipoSelect("tipo", forum, true));
		else
			table.add(forum.obterTipo());

		table.addHeader("Asunto:");
		if (incluir)
			table.add(new InputString("titulo", forum.obterTitulo(), 50));
		else
			table.add(forum.obterTitulo());

		if (incluir)
		{
			Button incluirButton = new Button("Agregar", new Action(
					"incluirForum"));

			table.addFooter(incluirButton);
		} else {

			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			table.setNextColSpan(table.getColumns());
			table.addHeader("Comentarios");

			table.setNextColSpan(table.getColumns());
			table.add(new ComentariosView(forum));

			Button comentarButton = new Button("Agregar Comentario",
					new Action("comentarEvento"));
			comentarButton.getAction().add("id", forum.obterId());
			comentarButton.getAction().add("view", true);
			comentarButton.setEnabled(!forum.obterFase().obterCodigo().equals(
					Forum.EVENTO_CONCLUIDO));

			table.addFooter(comentarButton);

			if (forum.permiteConcluir()) {
				Button concluirButton = new Button("Concluir", new Action(
						"concluirEvento"));
				concluirButton.getAction().add("id", forum.obterId());
				concluirButton.getAction().add("view", true);

				table.addFooter(concluirButton);
			}
		}

		return table;
	}
}