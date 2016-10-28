package com.gvs.crm.model.impl;

import com.gvs.crm.model.EventoContato;

public class EventoContatoImpl extends EventoImpl implements EventoContato {
	public String obterIcone() throws Exception {
		return "calendar.gif";
	}

	public boolean permiteExcluir() throws Exception
	{
		return this.obterUsuarioAtual().equals(this.obterResponsavel());
	}
}