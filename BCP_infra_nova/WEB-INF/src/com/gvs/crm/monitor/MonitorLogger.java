package com.gvs.crm.monitor;

import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import infra.log.Event;
import infra.log.Logger;

public class MonitorLogger extends Logger {
	public class Cell {
		public long average;

		public long count;

		public long max;

		public long min;

		public void addValue(long value) {
			this.count++;
			this.average = (this.count == 1 ? value
					: ((this.average * (this.count - 1)) + value) / this.count);
			this.min = (this.min == 0 || value < this.min) ? value : this.min;
			this.max = (value > this.max) ? value : this.max;
		}
	}

	public class Record {
		public Cell[] cells = new Cell[60];

		public void addEvent(Event event) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(event.getStartingTime());
			int index = c.get(Calendar.HOUR_OF_DAY);
			if (this.cells[index] == null)
				this.cells[index] = new Cell();
			this.cells[index].addValue(event.getElapsedTime());
		}
	}

	public Map hosts = new TreeMap();

	public Map messages = new TreeMap();

	public Map users = new TreeMap();

	public boolean enabled = false;

	public void clear() {
		this.hosts = new TreeMap();
		this.messages = new TreeMap();
		this.users = new TreeMap();
	}

	public void log(Event event) {
		if (!this.enabled)
			return;
		event.setEndingTime();

		Record record;

		String message = event.getCategory() + " : " + event.getMessage();
		record = (Record) this.messages.get(message);
		if (record == null) {
			record = new Record();
			this.messages.put(message, record);
		}
		record.addEvent(event);

		String host = event.getProperty("host");
		if (host != null) {
			record = (Record) this.hosts.get(host);
			if (record == null) {
				record = new Record();
				this.hosts.put(host, record);
			}
			record.addEvent(event);
		}

		String user = event.getProperty("user");
		if (user != null) {
			record = (Record) this.users.get(user);
			if (record == null) {
				record = new Record();
				this.users.put(user, record);
			}
			record.addEvent(event);
		}
	}
}