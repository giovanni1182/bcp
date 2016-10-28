package com.gvs.crm.model.impl;

import com.gvs.crm.model.MalaDireta;

public class MalaDiretaImpl extends EventoImpl implements MalaDireta {
	public String obterIcone() throws Exception {
		return "calendar.gif";
	}
}