package com.gvs.crm.component;

import infra.view.Block;
import infra.view.Label;
import infra.view.Space;

public class SeparadorLabel extends Block {
	public SeparadorLabel() {
		super(Block.HORIZONTAL);
		this.add(new Space(2));
		this.add(new Label("|"));
		this.add(new Space(2));
	}
}