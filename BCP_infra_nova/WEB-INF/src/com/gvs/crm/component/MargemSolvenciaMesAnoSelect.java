package com.gvs.crm.component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import infra.view.Select;

public class MargemSolvenciaMesAnoSelect extends Select
{
	public MargemSolvenciaMesAnoSelect(String nome, String valor) throws Exception
	{
		super(nome,1);
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		
		for(int i = 0 ; i < 13 ; i++)
		{
			String mesAno = new SimpleDateFormat("MM/yyyy").format(c.getTime());
			
			if(valor!=null)
				this.add(mesAno, mesAno, mesAno.equals(valor));
			else
				this.add(mesAno, mesAno, false);
			
			c.add(Calendar.MONTH, -1);
		}
	}
}
