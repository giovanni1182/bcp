package com.gvs.crm.component;

import java.util.Locale;
import java.util.Properties;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Label;
import infra.view.Space;
import infra.view.View;

public class DuracaoLabel extends BasicView {
	private boolean bold;

	private long duracao;

	public DuracaoLabel(long duracao) {
		this.duracao = duracao;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		Block block = new Block(Block.HORIZONTAL);
		int h = ((int) duracao / 3600000);
		block.add(new Label(h));
		block.add(new Space());
		Label label = new Label(h == 1 ? "hora" : "horas");
		label.setBold(this.bold);
		block.add(label);
		return block;
	}
}