package com.gvs.crm.component;

import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class Border extends Table {
	public Border(View view) {
		this(null, view);
	}

	public Border(View title, View view) {
		super(3);
		this.setWidth("100%");
		this.addStyle("tborder");

		this.setNextWidth("16");
		this.setNextStyle("tborder-upleft");
		this.add(new Space());
		this.setNextStyle("tborder-up");
		this.add(new Space());
		this.setNextWidth("16");
		this.setNextStyle("tborder-upright");
		this.add(new Space());

		if (title != null) {
			this.setNextStyle("tborder-left");
			this.add(new Space());
			this.setNextStyle("tborder_title");
			this.add(title);
			this.setNextStyle("tborder-right");
			this.add(new Space());
			this.setNextStyle("tborder-left");
			this.add(new Space());
			this.setNextStyle("tborder_body");
			this.add(new Space());
			this.setNextStyle("tborder-right");
			this.add(new Space());
		}

		this.setNextStyle("tborder-left");
		this.add(new Space());
		this.setNextStyle("tborder_body");
		this.add(view);
		this.setNextStyle("tborder-right");
		this.add(new Space());

		this.setNextWidth("16");
		this.setNextStyle("tborder-downleft");
		this.add(new Space());
		this.setNextStyle("tborder-down");
		this.add(new Space());
		this.setNextWidth("16");
		this.setNextStyle("tborder-downright");
		this.add(new Space());

	}
}