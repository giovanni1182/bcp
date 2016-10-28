package com.gvs.crm.model.impl;

import java.util.Date;

import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.MorosidadeCentralRisco;

public class MorosidadeCentralRiscoImpl extends EventoImpl implements MorosidadeCentralRisco 
{
	private Date data;
	private ClassificacaoContas cContas;
	private int dias;
	private int cotas;
	private double deudasGs;
	private String moeda;
	private double deudasMe;
	
	public void atribuirDataCorte(Date data) throws Exception 
	{
		this.data = data;
	}

	public void atribuirSecao(ClassificacaoContas cContas) throws Exception 
	{
		this.cContas = cContas;
	}

	public void atribuirDiasMora(int dias) throws Exception 
	{
		this.dias = dias;
	}

	public void atribuirCotasAtraso(int cotas) throws Exception 
	{
		this.cotas = cotas;
	}

	public void atribuirDeudasGs(double deudasGs) throws Exception 
	{	
		this.deudasGs = deudasGs;
	}

	public void atribuirMoedaDeudasMe(String moeda) throws Exception 
	{
		this.moeda = moeda;
	}

	public void atribuirDeudasMe(double deudasMe) throws Exception 
	{
		this.deudasMe = deudasMe;
	}

	public Date obterDataCorte() throws Exception 
	{
		return this.data;
	}

	public ClassificacaoContas obterSecao() throws Exception 
	{
		return this.cContas;
	}

	public int obterDiasMora() throws Exception 
	{
		return this.dias;
	}

	public int obterCotasAtraso() throws Exception 
	{
		return this.cotas;
	}

	public double obterDeudasGs() throws Exception 
	{
		return this.deudasGs;
	}

	public String obterMoedaDeudasMe() throws Exception 
	{
		return this.moeda;
	}

	public double obterDeudasMe() throws Exception 
	{
		return this.deudasMe;
	}
}