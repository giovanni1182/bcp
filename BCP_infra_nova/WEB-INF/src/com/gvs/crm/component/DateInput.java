package com.gvs.crm.component;

import java.util.Date;

import infra.view.Block;
import infra.view.InputDate;

public class DateInput extends Block {
	public DateInput(String nomeData, Date valorInicial, boolean habilitado) {
		super(Block.HORIZONTAL);
		if (habilitado)
			this.add(new InputDate(nomeData, valorInicial));
		else
			this.add(new DateLabel(valorInicial));
	}
}