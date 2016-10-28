package com.gvs.crm.monitor;

import com.gvs.crm.control.CRMControlServlet;

import infra.control.Action;
import infra.control.Control;

public class MonitorControl extends Control {
	public void desabilitarMonitor(Action action) throws Exception {
		MonitorLogger monitorLogger = (MonitorLogger) CRMControlServlet.monitorLogger;
		monitorLogger.enabled = false;
		this.setResponseView(new MonitorView(monitorLogger));
	}

	public void habilitarMonitor(Action action) throws Exception {
		MonitorLogger monitorLogger = (MonitorLogger) CRMControlServlet.monitorLogger;
		monitorLogger.enabled = true;
		this.setResponseView(new MonitorView(monitorLogger));
	}

	public void limparMonitor(Action action) throws Exception {
		MonitorLogger monitorLogger = (MonitorLogger) CRMControlServlet.monitorLogger;
		monitorLogger.clear();
		this.setResponseView(new MonitorView(monitorLogger));
	}

	public void visualizarMonitor(Action action) throws Exception {
		MonitorLogger monitorLogger = (MonitorLogger) CRMControlServlet.monitorLogger;
		this.setResponseView(new MonitorView(monitorLogger));
	}
}