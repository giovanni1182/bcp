package com.gvs.crm.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Excel 
{
	private String caminho;
	
	public void setCaminho(String caminho)
	{
		this.caminho = caminho;
	}
	
	public InputStream obterArquivo() throws Exception
	{
	    InputStream inputStream = new FileInputStream(this.caminho);
		
	    return inputStream;
	}
	
	public File obterArquivo2()throws Exception
	{
		File file = new File(this.caminho);
		
		return file;
	}
	
	public String getMesExtenso(Date data)
	{
		String mesStr = "";
		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(data));
		
		if(mes == 1)
			mesStr = "Enero";
		else if(mes == 2)
			mesStr = "Febrero";
		else if(mes == 3)
			mesStr = "Marzo";
		else if(mes == 4)
			mesStr = "Abril";
		else if(mes == 5)
			mesStr = "Mayo";
		else if(mes == 6)
			mesStr = "Junio";
		else if(mes == 7)
			mesStr = "Julio";
		else if(mes == 8)
			mesStr = "Agosto";
		else if(mes == 9)
			mesStr = "Septiembre";
		else if(mes == 10)
			mesStr = "Octubre";
		else if(mes == 11)
			mesStr = "Noviembre";
		else if(mes == 12)
			mesStr = "Diciembre";
			
		return mesStr;
	}
}
