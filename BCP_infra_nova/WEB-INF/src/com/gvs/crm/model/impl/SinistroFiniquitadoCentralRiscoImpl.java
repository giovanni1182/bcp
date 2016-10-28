package com.gvs.crm.model.impl;

import java.util.Date;

import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Plano;
import com.gvs.crm.model.SinistroFiniquitadoCentralRisco;

public class SinistroFiniquitadoCentralRiscoImpl extends EventoImpl implements SinistroFiniquitadoCentralRisco 
{

	private Date data;
	private ClassificacaoContas cContas;
	private Plano plano;
	private int qtde;
	private double valorMontanteGs;
	private String moeda;
	private double valorMontanteME;
	private double valorCapitalGs;
	private String moedaCapitalME;
	private double valorCapitalMe;
	
	
	
	public void atribuirCapitalGs(double valorCapitalGs) throws Exception 
	{
		this.valorCapitalGs = valorCapitalGs;

	}
	public void atribuirCapitalME(double valorCapitalMe) throws Exception 
	{
		this.valorCapitalMe = valorCapitalMe;

	}
	public void atribuirMoedaCapitalME(String moedaCapitalME) throws Exception 
	{
		this.moedaCapitalME = moedaCapitalME;

	}
	public void atribuirDataCorte(Date data) throws Exception 
	{
		this.data = data;
	}

	public void atribuirSecao(ClassificacaoContas cContas) throws Exception 
	{
		this.cContas = cContas;
	}

	public void atribuirPlano(Plano plano) throws Exception 
	{
		this.plano = plano;
	}

	public void atribuirQtdeSinistros(int qtde) throws Exception 
	{
		this.qtde = qtde;
	}

	public void atribuirMontantePagoGs(double valorMontanteGs) throws Exception 
	{
		this.valorMontanteGs = valorMontanteGs;
	}

	public void atribuirMoedaMontantePagoME(String moeda) throws Exception 
	{
		this.moeda = moeda;
	}

	public void atribuirMontantePagoME(double valorMontanteME) throws Exception 
	{
		this.valorMontanteME = valorMontanteME;
	}

	public Date obterDataCorte() throws Exception 
	{
		return this.data;
	}

	public ClassificacaoContas obterSecao() throws Exception 
	{
		return this.cContas;
	}

	public Plano obterPlano() throws Exception 
	{
		return this.plano;
	}

	public int obterQtdeSinistros() throws Exception 
	{
		return this.qtde;
	}

	public double obterMontantePagoGs() throws Exception 
	{
		return this.valorMontanteGs;
	}

	public String obterMoedaMontantePagoME() throws Exception 
	{
		return this.moeda;
	}

	public double obterMontantePagoME() throws Exception 
	{
		return this.valorMontanteME;
	}
	
	public double obterCapitalGs() throws Exception 
	{
		return this.valorCapitalGs;
	}
	public double obterCapitalME() throws Exception 
	{
		return this.valorCapitalMe;
	}
	public String obterMoedaCapitalME() throws Exception 
	{
		return this.moedaCapitalME;
	}
}