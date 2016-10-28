package com.gvs.crm.control;

import com.gvs.crm.monitor.MonitorLogger;

import infra.control.web.ControlServlet;
import infra.log.Log;
import infra.log.Logger;

public class CRMControlServlet extends ControlServlet {
	public static Logger monitorLogger;

	public void initialize() throws Exception {
		monitorLogger = new MonitorLogger();
		Log.getInstance().addLogger(monitorLogger);
	}
}