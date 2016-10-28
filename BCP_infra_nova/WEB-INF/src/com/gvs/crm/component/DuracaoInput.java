package com.gvs.crm.component;

import infra.view.Block;
import infra.view.InputInteger;

public class DuracaoInput extends Block {
	public DuracaoInput(String nome, long duracao, boolean habilitado) {
		super(Block.HORIZONTAL);
		if (habilitado)
			this.add(new InputInteger(nome, ((int) duracao / 3600000), 5));
		else
			this.add(new DuracaoLabel(duracao));
	}
}