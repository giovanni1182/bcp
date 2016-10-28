package com.gvs.crm.model.impl;


public class ArredondarImpl
{
	public ArredondarImpl()
	{
		
	}
	
	public double retornaValorArredondadoPraCima(double valor)
	{
		String arre = new Double(valor).toString(); 
		
		arre = arre.replace('.', ',');
		
		String[] arre2 = arre.split(",");
		
		if(arre2[1].startsWith("5"))
		{
			if(valor<0)
				valor-=0.5;
			else
				valor+=0.5;
		}
		else if(arre2[1].startsWith("6"))
		{
			if(valor<0)
				valor-=0.4;
			else
				valor+=0.4;
		}
		else if(arre2[1].startsWith("7"))
		{
			if(valor<0)
				valor-=0.3;
			else
				valor+=0.3;
		}
		else if(arre2[1].startsWith("8"))
		{
			if(valor<0)
				valor-=0.2;
			else
				valor+=0.2;
		}
		else if(arre2[1].startsWith("9"))
		{
			if(valor<0)
				valor-=0.1;
			else
				valor+=0.1;
		}
		
		return valor;
	}
}
