package com.gvs.crm.component;

import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.view.Block;
import infra.view.Image;
import infra.view.InputLong;
import infra.view.InputString;
import infra.view.Link;
import infra.view.Space;

public class EntidadePopup extends Block {
	public EntidadePopup(String entidadeId, String entidadeNome,
			Entidade entidade, String tipo, boolean habilitado)
			throws Exception {
		this(entidadeId, entidadeNome, entidade, tipo, habilitado, false);
	}

	public EntidadePopup(String entidadeId, String entidadeNome,
			Entidade entidade, boolean habilitado, boolean empUsuPes)
			throws Exception {
		this(entidadeId, entidadeNome, entidade, "", habilitado, empUsuPes);
	}

	public EntidadePopup(String entidadeId, String entidadeNome,
			Entidade entidade, boolean habilitado) throws Exception {
		this(entidadeId, entidadeNome, entidade, "", habilitado, false);
	}

	public EntidadePopup(String entidadeId, String entidadeNome,
			Entidade entidade, String tipo, boolean habilitado,
			boolean empUsuPes) throws Exception {
		super(Block.HORIZONTAL);
		long id = 0;
		if (entidade != null)
			id = entidade.obterId();
		String nome = "";
		if (entidade != null && entidade.obterId() > 0)
			nome = entidade.obterNome();
		if (habilitado) {
			this.add(new InputLong(entidadeId, id, 8));
			this.add(new Space(2));
			this.add(new InputString(entidadeNome, nome, 70));
			this.add(new Space(2));
			Action popupEntidadesAction = new Action(Action.POPUP,
					"popupEntidades");
			popupEntidadesAction.add("campoEntidadeId", entidadeId);
			popupEntidadesAction.add("campoEntidadeNome", entidadeNome);
			popupEntidadesAction.add("tipoFixo", tipo);
			popupEntidadesAction.add("empUsuPes", empUsuPes);
			this.add(new Link(new Image("calendar.gif"), popupEntidadesAction));
		} else {
			this.add(new EntidadeNomeLink(entidade));
		}
	}
}