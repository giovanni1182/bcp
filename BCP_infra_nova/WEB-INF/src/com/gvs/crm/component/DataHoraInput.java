package com.gvs.crm.component;

import java.text.SimpleDateFormat;
import java.util.Date;

import infra.view.Block;
import infra.view.InputDate;
import infra.view.Select;
import infra.view.Space;

public class DataHoraInput extends Block {
	public DataHoraInput(String nomeData, String nomeHora, Date valorInicial,
			boolean habilitado) {
		super(Block.HORIZONTAL);
		if (habilitado) {
			this.add(new InputDate(nomeData, valorInicial));
			this.add(new Space(1));
			String hora = "";
			if (valorInicial != null)
				hora = new SimpleDateFormat("HH:mm").format(valorInicial);
			String[] horas = { "00:30", "01:00", "01:30", "02:00", "02:30",
					"03:00", "03:30", "04:00", "04:30", "05:00", "05:30",
					"06:00", "06:30", "07:00", "07:30", "08:00", "08:30",
					"09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
					"12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
					"15:00", "15:30", "16:00", "16:30", "17:00", "17:30",
					"18:00", "18:30", "19:00", "19:30", "20:00", "20:30",
					"21:00", "21:30", "22:00", "22:30", "23:00", "23:30", };
			Select select = new Select(nomeHora, 1);
			for (int i = 0; i < horas.length; i++)
				select.add(horas[i], horas[i], horas[i].equals(hora));
			this.add(select);
		} else {
			this.add(new DateLabel(valorInicial));
			//this.add(new Space(1));
			//this.add(new Label(new
			// SimpleDateFormat("HH:mm").format(valorInicial)));
		}
	}
}