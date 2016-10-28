package com.gvs.crm.report;

import infra.view.report.Margin;
import infra.view.report.Report;

public class A4Report extends Report {
	public A4Report() {
		this.setHeigth("29.7cm");
		this.setWidth("21cm");
		this.setMargin(new Margin("1cm", "2cm", "1cm", "1cm"));
	}
}