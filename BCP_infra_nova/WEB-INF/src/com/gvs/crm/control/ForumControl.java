package com.gvs.crm.control;

import java.util.ArrayList;
import java.util.Collection;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Forum;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.ForunsView;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class ForumControl extends Control {
	public void incluirForum(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Forum forum = (Forum) mm.getEntity("Forum");

		mm.beginTransaction();
		try {

			Entidade destino = entidadeHome.obterEntidadePorApelido("bcp");

			forum.atribuirTipo(action.getString("tipo"));
			forum.atribuirTitulo(action.getString("titulo"));

			if (action.getString("tipo") == null
					|| action.getString("tipo").equals(""))
				throw new Exception("Elegiré el Tipo do Forum");

			if (action.getString("titulo") == null
					|| action.getString("titulo").equals(""))
				throw new Exception("Elegiré el titulo do Forum");

			forum.atribuirOrigem(destino);
			forum.atribuirDestino(destino);
			forum.atribuirResponsavel(usuarioAtual);
			forum.atribuirTipo(action.getString("tipo"));
			forum.atribuirTitulo(action.getString("titulo"));
			forum.incluir();

			this.setResponseView(new EventoView(forum));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(forum));
			mm.rollbackTransaction();
		}
	}

	public void localizarForuns(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());

		mm.beginTransaction();
		try 
		{
			Collection foruns = new ArrayList();

			if (!action.getString("assunto").equals("")	&& !action.getString("tipo").equals(""))
				foruns = eventoHome.localizarForuns(action.getString("assunto"), action.getString("tipo"));
			else if (!action.getString("assunto").equals(""))
				foruns = eventoHome.localizarForuns(action.getString("assunto"), null);
			else if (!action.getString("tipo").equals(""))
				foruns = eventoHome.localizarForuns(null, action.getString("tipo"));

			this.setResponseView(new ForunsView(foruns, action.getString("tipo"), action.getString("assunto")));
			mm.commitTransaction();
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuario, usuario));
			mm.rollbackTransaction();
		}
	}

	public void mostrarTodosForuns(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());

		mm.beginTransaction();
		try {
			Collection foruns = new ArrayList();

			foruns = eventoHome.localizarForuns("", "");

			this.setResponseView(new ForunsView(foruns, "", ""));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuario, usuario));
			mm.rollbackTransaction();
		}
	}
}