package com.gvs.crm.component;

import java.util.Date;

import infra.view.Label;

public class DateLabel extends Label {
	public DateLabel(Date date) {
		super("");
		if (date != null && date.getTime() > 0) {
			this.setValue(date);
			this.setPattern("dd/MM/yyyy HH:mm");
		}
	}
}