package com.gvs.crm.report;

import infra.view.report.Margin;
import infra.view.report.Report;

public class A4ReportCartao extends Report {
	public A4ReportCartao() {
		this.setHeigth("29.7cm");
		this.setWidth("21cm");
		this.setMargin(new Margin("0.5cm", "0.5cm", "0.5cm", "0.5cm"));
	}
}