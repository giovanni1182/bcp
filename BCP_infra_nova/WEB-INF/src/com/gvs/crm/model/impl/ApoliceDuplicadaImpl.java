package com.gvs.crm.model.impl;

import com.gvs.crm.model.ApoliceDuplicada;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Plano;

public class ApoliceDuplicadaImpl extends EventoImpl implements ApoliceDuplicada 
{

	private int qtde;
	private String numero;
	private ClassificacaoContas conta;
	private String status;
	private Plano plano;
	private double endoso;
	private double certificado;
	
	public void atribuirQtde(int qtde) throws Exception 
	{
		this.qtde = qtde;
	}

	public void atribuirNumero(String numero) throws Exception 
	{
		this.numero = numero;
	}

	public void atribuirSecao(ClassificacaoContas conta) throws Exception 
	{
		this.conta = conta;
	}

	public void atribuirStatus(String status) throws Exception 
	{
		this.status = status;
	}

	public void atribuirPlano(Plano plano) throws Exception 
	{
		this.plano = plano;
	}

	public void atribuirEndoso(double endoso) throws Exception 
	{
		this.endoso = endoso;
	}

	public void atribuirCertificado(double certificado) throws Exception 
	{
		this.certificado = certificado;
	}

	public int obterQtde() throws Exception 
	{
		return this.qtde;
	}

	public String obterNumero() throws Exception 
	{
		return this.numero;
	}

	public ClassificacaoContas obterSecao() throws Exception 
	{
		return this.conta;
	}

	public String obterStatus() throws Exception 
	{
		return this.status;
	}

	public Plano obterPlano() throws Exception 
	{
		return this.plano;
	}

	public double obterEndoso() throws Exception 
	{
		return this.endoso;
	}

	public double obterCertificado() throws Exception 
	{
		return this.certificado;
	}
}