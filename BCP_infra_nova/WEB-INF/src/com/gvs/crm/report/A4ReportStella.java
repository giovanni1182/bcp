package com.gvs.crm.report;

import infra.view.report.Margin;
import infra.view.report.Report;

public class A4ReportStella extends Report {
	public A4ReportStella() {
		this.setHeigth("29.7cm");
		this.setWidth("21cm");
		this.setMargin(new Margin("0.1cm", "2cm", "1cm", "1cm"));
	}
}