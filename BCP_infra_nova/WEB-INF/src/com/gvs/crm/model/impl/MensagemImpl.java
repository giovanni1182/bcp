package com.gvs.crm.model.impl;

import com.gvs.crm.model.Mensagem;

public class MensagemImpl extends EventoImpl implements Mensagem {
	public String obterIcone() throws Exception {
		return "message.gif";
	}

	public boolean permiteExcluir() throws Exception {
		return this.obterUsuarioAtual().equals(this.obterCriador());
	}
}