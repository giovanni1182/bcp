package com.gvs.crm.model.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.RatioHome;

import infra.model.Home;

public class RatioHomeImpl extends Home implements RatioHome
{
	public double obterSinistrosPagos3Anos(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception
	{
		double valor = 0;
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorApelido("0506000000");
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		
		String mesAno = formataData.format(dataFim);
		Date dataFimReal = formataData.parse(mesAno);
		
		Calendar c = Calendar.getInstance();
		c.setTime(dataFimReal);
		
		c.add(Calendar.MONTH, -35);
		
		Date dataInicio = c.getTime();
		c.setTime(dataInicio);
		
		//System.out.println("Periodo SinistrosPagos3Anos " + formataData.format(dataInicio) + " a " + formataData.format(dataFimReal));
		
		while(c.getTime().compareTo(dataFimReal)<0)
		{
			mesAno = formataData.format(c.getTime());
			
			valor+=cContas.obterTotalizacaoExistente(aseguradora, mesAno);
			
			c.add(Calendar.MONTH, 1);
		}
		
		contasAcumuladas.put(cContas.obterApelido(), cContas.obterApelido()+";3;"+valor);
		
		//System.out.println("Valor SinistrosPagos3Anos " + valor);
		
		return valor;
	}
	
	public double obterGastosSinistros3Anos(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception
	{
		double valor = 0;
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorApelido("0508010000");
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		
		String mesAno = formataData.format(dataFim);
		Date dataFimReal = formataData.parse(mesAno);
		
		Calendar c = Calendar.getInstance();
		c.setTime(dataFimReal);
		
		c.add(Calendar.MONTH, -35);
		
		Date dataInicio = c.getTime();
		c.setTime(dataInicio);
		
		//System.out.println("Periodo GastosSinistros3Anos " + formataData.format(dataInicio) + " a " + formataData.format(dataFimReal));
		
		while(c.getTime().compareTo(dataFimReal)<0)
		{
			mesAno = formataData.format(c.getTime());
			
			valor+=cContas.obterTotalizacaoExistente(aseguradora, mesAno);
			
			c.add(Calendar.MONTH, 1);
		}
		
		contasAcumuladas.put(cContas.obterApelido(), cContas.obterApelido()+";3;"+valor);
		
		//System.out.println("Valor GastosSinistros3Anos " + valor);
		
		return valor;
	}
	
	public double obterSinistrosRecuperados3Anos(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception
	{
		double valor = 0;
		double valor2 = 0;
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorApelido("0408000000");
		ClassificacaoContas cContas2 = (ClassificacaoContas) home.obterEntidadePorApelido("0409000000");
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		
		String mesAno = formataData.format(dataFim);
		Date dataFimReal = formataData.parse(mesAno);
		
		Calendar c = Calendar.getInstance();
		c.setTime(dataFimReal);
		
		c.add(Calendar.MONTH, -35);
		
		Date dataInicio = c.getTime();
		c.setTime(dataInicio);
		
		//System.out.println("Periodo SinistrosRecuperados3Anos " + formataData.format(dataInicio) + " a " + formataData.format(dataFimReal));
		
		while(c.getTime().compareTo(dataFimReal)<0)
		{
			mesAno = formataData.format(c.getTime());
			
			valor+=cContas.obterTotalizacaoExistente(aseguradora, mesAno);
			valor2+=cContas2.obterTotalizacaoExistente(aseguradora, mesAno);
			
			c.add(Calendar.MONTH, 1);
		}
		
		contasAcumuladas.put(cContas.obterApelido(), cContas.obterApelido()+";3;"+valor);
		contasAcumuladas.put(cContas2.obterApelido(), cContas2.obterApelido()+";3;"+valor2);
		
		//System.out.println("Valor SinistrosRecuperados3Anos " + valor);
		
		return valor + valor2;
	}
	
	public double obterGastosRecuperados3Anos(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception
	{
		double valor = 0;
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorApelido("0508020000");
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		
		String mesAno = formataData.format(dataFim);
		Date dataFimReal = formataData.parse(mesAno);
		
		Calendar c = Calendar.getInstance();
		c.setTime(dataFimReal);
		
		c.add(Calendar.MONTH, -35);
		
		Date dataInicio = c.getTime();
		c.setTime(dataInicio);
		
		//System.out.println("Periodo GastosRecuperados3Anos " + formataData.format(dataInicio) + " a " + formataData.format(dataFimReal));
		
		while(c.getTime().compareTo(dataFimReal)<0)
		{
			mesAno = formataData.format(c.getTime());
			
			valor+=cContas.obterTotalizacaoExistente(aseguradora, mesAno);
			
			c.add(Calendar.MONTH, 1);
		}
		
		contasAcumuladas.put(cContas.obterApelido(), cContas.obterApelido()+";3;"+valor);
		
		//System.out.println("Valor GastosRecuperados3Anos " + valor);
		
		return valor;
	}
	
	public double obterRecuperadosSinistros3Anos(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception
	{
		double valor = 0;
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorApelido("0407000000");
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		
		String mesAno = formataData.format(dataFim);
		Date dataFimReal = formataData.parse(mesAno);
		
		Calendar c = Calendar.getInstance();
		c.setTime(dataFimReal);
		
		c.add(Calendar.MONTH, -35);
		
		Date dataInicio = c.getTime();
		c.setTime(dataInicio);
		
		//System.out.println("Periodo RecuperadosSinistros3Anos " + formataData.format(dataInicio) + " a " + formataData.format(dataFimReal));
		
		while(c.getTime().compareTo(dataFimReal)<0)
		{
			mesAno = formataData.format(c.getTime());
			
			valor+=cContas.obterTotalizacaoExistente(aseguradora, mesAno);
			
			c.add(Calendar.MONTH, 1);
		}
		
		contasAcumuladas.put(cContas.obterApelido(), cContas.obterApelido()+";3;"+valor);
		
		//System.out.println("Valor RecuperadosSinistros3Anos " + valor);
		
		return valor;
	}
	
	public double obterProvisaoTecnicaSinistros3Anos(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception
	{
		double valor = 0;
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorApelido("0213000000");
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		
		String mesAno = formataData.format(dataFim);
		Date dataFimReal = formataData.parse(mesAno);
		
		Calendar c = Calendar.getInstance();
		c.setTime(dataFimReal);
		
		//Para esse cáculo são 37 meses
		c.add(Calendar.MONTH, -36);
		
		Date dataInicio = c.getTime();
		c.setTime(dataInicio);
		
		//System.out.println("Periodo ProvisaoTecnicaSinistros3Anos " + formataData.format(dataInicio) + " a " + formataData.format(dataFimReal));
		
		while(c.getTime().compareTo(dataFimReal)<0)
		{
			mesAno = formataData.format(c.getTime());
			
			valor+=cContas.obterTotalizacaoExistente(aseguradora, mesAno);
			
			c.add(Calendar.MONTH, 1);
		}
		
		contasAcumuladas.put(cContas.obterApelido(), cContas.obterApelido()+";3;"+valor);
		
		//System.out.println("Valor ProvisaoTecnicaSinistros3Anos " + valor);
		
		return valor;
	}
	
	public double obterPrimaDireta1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception
	{
		double valor = 0;
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorApelido("0401010000");
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		
		String mesAno = formataData.format(dataFim);
		Date dataFimReal = formataData.parse(mesAno);
		
		Calendar c = Calendar.getInstance();
		c.setTime(dataFimReal);
		
		c.add(Calendar.MONTH, -11);
		
		Date dataInicio = c.getTime();
		c.setTime(dataInicio);
		
		//System.out.println("Periodo PrimaDireta1Ano " + formataData.format(dataInicio) + " a " + formataData.format(dataFimReal));
		
		while(c.getTime().compareTo(dataFimReal)<0)
		{
			mesAno = formataData.format(c.getTime());
			
			valor+=cContas.obterTotalizacaoExistente(aseguradora, mesAno);
			
			c.add(Calendar.MONTH, 1);
		}
		
		contasAcumuladas.put(cContas.obterApelido(), cContas.obterApelido()+";1;"+valor);
		
		//System.out.println("Valor PrimaDireta1Ano " + valor);
		
		return valor;
	}
	
	public double obterPrimasAceitas1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception
	{
		double valor = 0;
		double valor2 = 0;
		
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorApelido("0402000000");
		ClassificacaoContas cContas2 = (ClassificacaoContas) home.obterEntidadePorApelido("0403000000");
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		
		String mesAno = formataData.format(dataFim);
		Date dataFimReal = formataData.parse(mesAno);
		
		Calendar c = Calendar.getInstance();
		c.setTime(dataFimReal);
		
		c.add(Calendar.MONTH, -11);
		
		Date dataInicio = c.getTime();
		c.setTime(dataInicio);
		
		//System.out.println("Periodo PrimasAceitas1Ano " + formataData.format(dataInicio) + " a " + formataData.format(dataFimReal));
		
		while(c.getTime().compareTo(dataFimReal)<0)
		{
			mesAno = formataData.format(c.getTime());
			
			valor+=cContas.obterTotalizacaoExistente(aseguradora, mesAno); 
			valor2+=cContas2.obterTotalizacaoExistente(aseguradora, mesAno);
			
			c.add(Calendar.MONTH, 1);
		}
		
		contasAcumuladas.put(cContas.obterApelido(), cContas.obterApelido()+";1;"+valor);
		contasAcumuladas.put(cContas2.obterApelido(), cContas2.obterApelido()+";1;"+valor);
		
		//System.out.println("Valor PrimasAceitas1Ano " + valor);
		
		return valor + valor2;
	}
	
	public double obterPrimasCedidas1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception
	{
		double valor = 0;
		double valor2 = 0;
		
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorApelido("0501000000");
		ClassificacaoContas cContas2 = (ClassificacaoContas) home.obterEntidadePorApelido("0502000000");
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		
		String mesAno = formataData.format(dataFim);
		Date dataFimReal = formataData.parse(mesAno);
		
		Calendar c = Calendar.getInstance();
		c.setTime(dataFimReal);
		
		c.add(Calendar.MONTH, -11);
		
		Date dataInicio = c.getTime();
		c.setTime(dataInicio);
		
		//System.out.println("Periodo PrimasCedidas1Ano " + formataData.format(dataInicio) + " a " + formataData.format(dataFimReal));
		
		while(c.getTime().compareTo(dataFimReal)<0)
		{
			mesAno = formataData.format(c.getTime());
			
			valor+=cContas.obterTotalizacaoExistente(aseguradora, mesAno);
			valor2+=cContas2.obterTotalizacaoExistente(aseguradora, mesAno);
			
			c.add(Calendar.MONTH, 1);
		}
		
		contasAcumuladas.put(cContas.obterApelido(), cContas.obterApelido()+";1;"+valor);
		contasAcumuladas.put(cContas2.obterApelido(), cContas2.obterApelido()+";1;"+valor);
		
		//System.out.println("Valor PrimasCedidas1Ano " + valor);
		
		return valor + valor2;
	}
	
	public double obterRendaFixa1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception
	{
		double valor = 0;
		double valor2 = 0;
		
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorApelido("0107010000");
		ClassificacaoContas cContas2 = (ClassificacaoContas) home.obterEntidadePorApelido("0107020000");
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		
		String mesAno = formataData.format(dataFim);
		Date dataFimReal = formataData.parse(mesAno);
		
		Calendar c = Calendar.getInstance();
		c.setTime(dataFimReal);
		
		c.add(Calendar.MONTH, -11);
		
		Date dataInicio = c.getTime();
		c.setTime(dataInicio);
		
		//System.out.println("Periodo RendaFixa1Ano " + formataData.format(dataInicio) + " a " + formataData.format(dataFimReal));
		
		while(c.getTime().compareTo(dataFimReal)<0)
		{
			mesAno = formataData.format(c.getTime());
			
			valor+=cContas.obterTotalizacaoExistente(aseguradora, mesAno);
			valor2+=cContas2.obterTotalizacaoExistente(aseguradora, mesAno);
			
			c.add(Calendar.MONTH, 1);
		}
		
		contasAcumuladas.put(cContas.obterApelido(), cContas.obterApelido()+";1;"+valor);
		contasAcumuladas.put(cContas2.obterApelido(), cContas2.obterApelido()+";1;"+valor);
		
		//System.out.println("Valor RendaFixa1Ano " + valor);
		
		return valor + valor2;
	}
	
	public double obterImobiliares1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception
	{
		double valor = 0;
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorApelido("0107060000");
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		
		String mesAno = formataData.format(dataFim);
		Date dataFimReal = formataData.parse(mesAno);
		
		Calendar c = Calendar.getInstance();
		c.setTime(dataFimReal);
		
		c.add(Calendar.MONTH, -11);
		
		Date dataInicio = c.getTime();
		c.setTime(dataInicio);
		
		//System.out.println("Periodo Imobiliares1Ano " + formataData.format(dataInicio) + " a " + formataData.format(dataFimReal));
		
		while(c.getTime().compareTo(dataFimReal)<0)
		{
			mesAno = formataData.format(c.getTime());
			
			valor+=cContas.obterTotalizacaoExistente(aseguradora, mesAno);
			
			c.add(Calendar.MONTH, 1);
		}
		
		contasAcumuladas.put(cContas.obterApelido(), cContas.obterApelido()+";1;"+valor);
		
		//System.out.println("Valor Imobiliares1Ano " + valor);
		
		return valor;
	}
	
	public double obterBensUsoProprio1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception
	{
		double valor = 0;
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorApelido("0108010000");
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		
		String mesAno = formataData.format(dataFim);
		Date dataFimReal = formataData.parse(mesAno);
		
		Calendar c = Calendar.getInstance();
		c.setTime(dataFimReal);
		
		c.add(Calendar.MONTH, -11);
		
		Date dataInicio = c.getTime();
		c.setTime(dataInicio);
		
		//System.out.println("Periodo BensUsoProprio1Ano " + formataData.format(dataInicio) + " a " + formataData.format(dataFimReal));
		
		while(c.getTime().compareTo(dataFimReal)<0)
		{
			mesAno = formataData.format(c.getTime());
			
			valor+=cContas.obterTotalizacaoExistente(aseguradora, mesAno);
			
			c.add(Calendar.MONTH, 1);
		}
		
		contasAcumuladas.put(cContas.obterApelido(), cContas.obterApelido()+";1;"+valor);
		
		//System.out.println("Valor BensUsoProprio1Ano " + valor);
		
		return valor;
	}
	
	public double obterPrimasDiferidas1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception
	{
		double valor = 0;
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorApelido("0109030000");
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		
		String mesAno = formataData.format(dataFim);
		Date dataFimReal = formataData.parse(mesAno);
		
		Calendar c = Calendar.getInstance();
		c.setTime(dataFimReal);
		
		c.add(Calendar.MONTH, -11);
		
		Date dataInicio = c.getTime();
		c.setTime(dataInicio);
		
		//System.out.println("Periodo PrimasDiferidas1Ano " + formataData.format(dataInicio) + " a " + formataData.format(dataFimReal));
		
		while(c.getTime().compareTo(dataFimReal)<0)
		{
			mesAno = formataData.format(c.getTime());
			
			valor+=cContas.obterTotalizacaoExistente(aseguradora, mesAno);
			
			c.add(Calendar.MONTH, 1);
		}
		
		contasAcumuladas.put(cContas.obterApelido(), cContas.obterApelido()+";1;"+valor);
		
		//System.out.println("Valor PrimasDiferidas1Ano " + valor);
		
		return valor;
	}
	
	public double obterProvisoesTecnicasSeguros1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception
	{
		double valor = 0;
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorApelido("0212000000");
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		
		String mesAno = formataData.format(dataFim);
		Date dataFimReal = formataData.parse(mesAno);
		
		Calendar c = Calendar.getInstance();
		c.setTime(dataFimReal);
		
		c.add(Calendar.MONTH, -11);
		
		Date dataInicio = c.getTime();
		c.setTime(dataInicio);
		
		//System.out.println("Periodo ProvisoesTecnicasSeguros1Ano " + formataData.format(dataInicio) + " a " + formataData.format(dataFimReal));
		
		while(c.getTime().compareTo(dataFimReal)<0)
		{
			mesAno = formataData.format(c.getTime());
			
			valor+=cContas.obterTotalizacaoExistente(aseguradora, mesAno);
			
			c.add(Calendar.MONTH, 1);
		}
		
		contasAcumuladas.put(cContas.obterApelido(), cContas.obterApelido()+";1;"+valor);
		
		//System.out.println("Valor ProvisoesTecnicasSeguros1Ano " + valor);
		
		return valor;
	}
	
	public double obterReservaAtivosFixos1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception
	{
		double valor = 0;
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorApelido("0303020100");
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		
		String mesAno = formataData.format(dataFim);
		Date dataFimReal = formataData.parse(mesAno);
		
		Calendar c = Calendar.getInstance();
		c.setTime(dataFimReal);
		
		c.add(Calendar.MONTH, -11);
		
		Date dataInicio = c.getTime();
		c.setTime(dataInicio);
		
		//System.out.println("Periodo ReservasAtivosFixos1Ano " + formataData.format(dataInicio) + " a " + formataData.format(dataFimReal));
		
		while(c.getTime().compareTo(dataFimReal)<0)
		{
			mesAno = formataData.format(c.getTime());
			
			valor+=cContas.obterTotalizacaoExistente(aseguradora, mesAno);
			
			c.add(Calendar.MONTH, 1);
		}
		
		contasAcumuladas.put(cContas.obterApelido(), cContas.obterApelido()+";1;"+valor);
		
		//System.out.println("Valor ReservasAtivosFixos1Ano " + valor);
		
		return valor;
	}
	
	public double obterReservaLei1Ano(Date dataFim, Aseguradora aseguradora, Map<String, String> contasAcumuladas) throws Exception
	{
		double valor = 0;
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		ClassificacaoContas cContas = (ClassificacaoContas) home.obterEntidadePorApelido("0303020200");
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		
		String mesAno = formataData.format(dataFim);
		Date dataFimReal = formataData.parse(mesAno);
		
		Calendar c = Calendar.getInstance();
		c.setTime(dataFimReal);
		
		c.add(Calendar.MONTH, -11);
		
		Date dataInicio = c.getTime();
		c.setTime(dataInicio);
		
		//System.out.println("Periodo ReservaLei1Ano " + formataData.format(dataInicio) + " a " + formataData.format(dataFimReal));
		
		while(c.getTime().compareTo(dataFimReal)<0)
		{
			mesAno = formataData.format(c.getTime());
			
			valor+=cContas.obterTotalizacaoExistente(aseguradora, mesAno);
			
			c.add(Calendar.MONTH, 1);
		}
		
		contasAcumuladas.put(cContas.obterApelido(), cContas.obterApelido()+";1;"+valor);
		
		//System.out.println("Valor ReservaLei1Ano " + valor);
		
		return valor;
	}
}
