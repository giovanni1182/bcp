package com.gvs.crm.monitor;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import com.gvs.crm.component.Border;

import infra.control.Action;
import infra.view.Button;
import infra.view.Label;
import infra.view.Table;

public class MonitorView extends Table {
	public MonitorView(MonitorLogger logger) {
		super(1);
		this.setWidth("100%");

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		int hour = c.get(Calendar.HOUR_OF_DAY);

		Table table = new Table(1 + 5 * 4);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);
		table.setTitle("Monitor");

		table.addHeader("Description");
		for (int i = 0; i < 5; i++) {
			table.setNextColSpan(4);
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader(new Label(i + hour - 5 + 1));
		}
		table.addHeader("");
		for (int i = 0; i < 5; i++) {
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Cnt");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Avg");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Min");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Max");
		}

		table.addSubtitle("Messages");
		this.addRecords(table, logger.messages, hour);

		table.addSubtitle("Hosts");
		this.addRecords(table, logger.hosts, hour);

		table.addSubtitle("Hosts");
		this.addRecords(table, logger.users, hour);

		if (logger.enabled)
			table.addFooter(new Button("Desabilitar", new Action(
					"desabilitarMonitor")));
		else
			table.addFooter(new Button("Habilitar", new Action(
					"habilitarMonitor")));

		table
				.addFooter(new Button("Atualizar", new Action(
						"visualizarMonitor")));

		Button limparButton = new Button("Limpar", new Action("limparMonitor"));
		//limparButton.getAction().setConfirmation("Confirma limpeza das
		// informações do monitor ?");
		table.addFooter(limparButton);

		Border border = new Border(table);
		border.setWidth("100%");
		this.add(border);
	}

	public void addRecords(Table table, Map records, int hour) {
		for (Iterator i = records.keySet().iterator(); i.hasNext();) {
			String messageName = (String) i.next();
			table.addData(messageName);
			MonitorLogger.Record record = (MonitorLogger.Record) records
					.get(messageName);
			for (int j = 0; j < 5; j++) {
				int index = j + hour - 5 + 1;
				if (index < 0 || record.cells[index] == null) {
					table.setNextColSpan(4);
					table.addData("");
				} else {
					table.setNextHAlign(Table.HALIGN_RIGHT);
					table.addData(new Label(record.cells[index].count));
					table.setNextHAlign(Table.HALIGN_RIGHT);
					table.addData(new Label(record.cells[index].average));
					table.setNextHAlign(Table.HALIGN_RIGHT);
					table.addData(new Label(record.cells[index].min));
					table.setNextHAlign(Table.HALIGN_RIGHT);
					table.addData(new Label(record.cells[index].max));
				}
			}
		}
	}
}