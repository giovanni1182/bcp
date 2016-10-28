package com.gvs.crm.component;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Label;
import infra.view.Select;
import infra.view.View;

public class ResponsavelSelect extends BasicView {
	private String nome;

	private Entidade responsavel;

	boolean habilitado;

	public ResponsavelSelect(String nome, Entidade responsavel,
			boolean habilitado) {
		this.nome = nome;
		this.responsavel = responsavel;
		this.habilitado = habilitado;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		if (this.habilitado) {
			CRMModelManager mm = new CRMModelManager(user);
			UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
			long id;
			if (this.responsavel == null)
				id = usuarioHome.obterUsuarioPorUser(user).obterId();
			else
				id = this.responsavel.obterId();
			Select select = new Select(nome, 1);
			select.add("", "0", false);
			TreeMap map = new TreeMap();
			for (Iterator i = usuarioHome.obterUsuarios().iterator(); i
					.hasNext();) {
				Usuario u = (Usuario) i.next();
				map.put(u.obterNome() + u.obterId(), u);
			}
			for (Iterator i = map.values().iterator(); i.hasNext();) {
				Usuario u = (Usuario) i.next();
				select.add(u.obterNome() + " (" + u.obterEmpresa().obterNome()
						+ ")", String.valueOf(u.obterId()), id == u.obterId());
			}
			return select;
		} else {
			if (this.responsavel == null)
				return new Label("");
			else
				return new EntidadeNomeLink(this.responsavel);
		}
	}
}